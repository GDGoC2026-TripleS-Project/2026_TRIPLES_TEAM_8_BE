// src/main/java/com/example/gread/app/upload/NlcIngestService.java
package com.example.gread.app.upload;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NlcIngestService {

    private final NlcIngestPageService pageService;

    public List<String> defaultSeeds() {
        return List.of(
                "가","나","다","라","마","바","사","아","자","차","카","타","파","하",
                "0","1","2","3","4","5","6","7","8","9"
        );
    }

    // ✅ 여긴 트랜잭션 걸지 않음
    public int ingestAll(IngestRequest req) throws Exception {
        List<String> seeds = (req.getSeeds() == null || req.getSeeds().isEmpty())
                ? defaultSeeds()
                : req.getSeeds();

        int totalSaved = 0;

        for (String seed : seeds) {
            for (int pageNum = req.getFromPage(); pageNum <= req.getToPage(); pageNum++) {
                totalSaved += pageService.ingestOnePage(seed, pageNum, req); // ✅ 프록시 경유 → REQUIRES_NEW 작동
            }
        }
        return totalSaved;
    }
}
