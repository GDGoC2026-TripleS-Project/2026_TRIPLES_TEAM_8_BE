package com.example.gread.app.upload;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NlcIngestPageService {

    private final NlcBookRepository repository;
    private final ObjectMapper objectMapper;

    @Value("${nlc.base-url:https://www.nl.go.kr}")
    private String baseUrl;

    @Value("${nlc.key}")
    private String apiKey;

    private WebClient webClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    // ✅ 페이지 단위 커밋
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int ingestOnePage(String kwd, int pageNum, IngestRequest req) throws Exception {

        // ✅ 여기 람다/임포트 꼬이면 바로 Object로 타입이 깨짐.
        // 반드시 아래 형태로 써야 WebClient 체이닝이 정상 동작함.
        var uri = UriComponentsBuilder
                .fromUriString("https://www.nl.go.kr/NL/search/openApi/search.do")
                .queryParam("key", apiKey)
                .queryParam("apiType", "json")
                .queryParam("srchTarget", "total")
                .queryParam("kwd", kwd)
                .queryParam("pageSize", req.getPageSize())
                .queryParam("pageNum", pageNum)
                .build(true)
                .toUri();

        System.out.println("[NLC] request.uri=" + uri);
        System.out.println("[NLC] apiKey.len=" + (apiKey == null ? -1 : apiKey.length()));

        String raw = webClient()
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block();



        if (raw == null || raw.isBlank()) {
            System.out.println("[NLC] raw empty. kwd=" + kwd + ", page=" + pageNum);
            return 0;
        }

        System.out.println("[NLC] kwd=" + kwd + ", page=" + pageNum + ", size=" + req.getPageSize());
        System.out.println("[NLC] raw.head=" + head(raw));

        JsonNode rootNode = objectMapper.readTree(raw);

        // ✅ 에러 응답이면 바로 컷 (이러면 “0건” 이유가 콘솔에 남음)
        if (looksLikeError(rootNode)) {
            System.out.println("[NLC] ERROR response raw.head=" + head(raw));
            return 0;
        }

        // ✅ items 경로 여러 케이스 대응
        ArrayNode itemsNode = firstArray(rootNode, "root", "result", "item");
        if (itemsNode == null) itemsNode = firstArray(rootNode, "result", "item");

        if (itemsNode == null || itemsNode.isEmpty()) {
            System.out.println("[NLC] items missing/empty. raw.head=" + head(raw));
            return 0;
        }

        // ✅ item DTO로 변환
        List<NlcSearchResponse.NlcItem> items = new ArrayList<>();
        for (JsonNode n : itemsNode) {
            items.add(objectMapper.treeToValue(n, NlcSearchResponse.NlcItem.class));
        }

        System.out.println("[NLC] items.size=" + items.size());
        if (items.isEmpty()) return 0;

        // ✅ paramData 안전 파싱 + fallback
        NlcSearchResponse.ParamData param = null;
        JsonNode paramNode = firstNode(rootNode, "root", "paramData");
        if (paramNode == null) paramNode = firstNode(rootNode, "paramData");
        if (paramNode != null && !paramNode.isNull()) {
            param = objectMapper.treeToValue(paramNode, NlcSearchResponse.ParamData.class);
        }

        String pKwd = (param != null && param.getKwd() != null) ? param.getKwd() : kwd;
        String pCategory = (param != null && param.getCategory() != null) ? param.getCategory() : req.getCategory();
        Integer pPageNum = (param != null && param.getPageNum() != null) ? param.getPageNum() : pageNum;
        Integer pPageSize = (param != null && param.getPageSize() != null) ? param.getPageSize() : req.getPageSize();
        String pSort = (param != null) ? param.getSort() : null;
        Integer pTotal = (param != null) ? param.getTotal() : null;

        // ✅ 배치 저장 + flush로 제약조건 에러 즉시 표면화
        List<NlcBook> batch = new ArrayList<>();

        for (var it : items) {
            String externalId = it.getExternalId();
            if (externalId == null || externalId.isBlank()) continue;

            String typeName = it.getTypeName();

            if (req.isBooksOnly()) {
                if (typeName == null || !typeName.contains("도서")) continue;
            }

            if (repository.existsByExternalId(externalId)) continue;

            batch.add(NlcBook.builder()
                    .kwd(pKwd)
                    .category(pCategory)
                    .pageNum(pPageNum)
                    .pageSize(pPageSize)
                    .sort(pSort)
                    .total(pTotal)

                    .titleInfo(it.getTitleInfo())
                    .typeName(typeName)
                    .placeInfo(it.getPlaceInfo())
                    .authorInfo(it.getAuthorInfo())
                    .pubInfo(it.getPubInfo())
                    .menuName(it.getMenuName())
                    .mediaName(it.getMediaName())
                    .manageName(it.getManageName())
                    .pubYearInfo(it.getPubYearInfo())
                    .controlNo(it.getControlNo())
                    .docYn(it.getDocYn())
                    .orgLink(it.getOrgLink())
                    .externalId(externalId)
                    .typeCode(it.getTypeCode())
                    .licYn(it.getLicYn())
                    .licText(it.getLicText())
                    .regDate(it.getRegDate())
                    .detailLink(it.getDetailLink())
                    .isbn(it.getIsbn())
                    .callNo(it.getCallNo())
                    .kdcCode1s(it.getKdcCode1s())
                    .kdcName1s(it.getKdcName1s())
                    .build());
        }

        if (batch.isEmpty()) {
            System.out.println("[NLC] batch empty after filtering. kwd=" + kwd + ", page=" + pageNum);
            return 0;
        }

        repository.saveAll(batch);
        repository.flush();

        System.out.println("[NLC] saved=" + batch.size() + " (kwd=" + kwd + ", page=" + pageNum + ")");
        return batch.size();
    }

    // -------------------------
    // helpers
    // -------------------------

    private String head(String raw) {
        return raw.substring(0, Math.min(500, raw.length()));
    }

    private JsonNode firstNode(JsonNode node, String... path) {
        JsonNode cur = node;
        for (String p : path) {
            if (cur == null) return null;
            cur = cur.get(p);
        }
        return cur;
    }

    private ArrayNode firstArray(JsonNode node, String... path) {
        JsonNode cur = firstNode(node, path);
        return (cur != null && cur.isArray()) ? (ArrayNode) cur : null;
    }

    private boolean looksLikeError(JsonNode root) {
        JsonNode errorCode = root.get("errorCode");
        JsonNode errorMessage = root.get("errorMessage");
        if (errorCode != null || errorMessage != null) return true;

        JsonNode nestedError = firstNode(root, "root", "error");
        if (nestedError != null && !nestedError.isNull()) return true;

        // 최후 보루(디버깅용)
        return root.toString().toLowerCase().contains("error");
    }
}
