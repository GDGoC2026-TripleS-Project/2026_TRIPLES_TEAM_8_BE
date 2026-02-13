package com.example.gread.app.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NlcSearchResponse {

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ParamData {
        @JsonProperty("kwd") private String kwd;
        @JsonProperty("category") private String category;
        @JsonProperty("pageNum") private Integer pageNum;
        @JsonProperty("pageSize") private Integer pageSize;
        @JsonProperty("sort") private String sort;
        @JsonProperty("total") private Integer total;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NlcItem {
        @JsonProperty("title_info") private String titleInfo;
        @JsonProperty("type_name") private String typeName;
        @JsonProperty("place_info") private String placeInfo;
        @JsonProperty("author_info") private String authorInfo;
        @JsonProperty("pub_info") private String pubInfo;
        @JsonProperty("menu_name") private String menuName;
        @JsonProperty("media_name") private String mediaName;
        @JsonProperty("manage_name") private String manageName;
        @JsonProperty("pub_year_info") private String pubYearInfo;
        @JsonProperty("control_no") private String controlNo;
        @JsonProperty("doc_yn") private String docYn;
        @JsonProperty("org_link") private String orgLink;

        // ✅ 종키 id
        @JsonProperty("id") private String externalId;

        @JsonProperty("type_code") private String typeCode;
        @JsonProperty("lic_yn") private String licYn;
        @JsonProperty("lic_text") private String licText;
        @JsonProperty("reg_date") private String regDate;
        @JsonProperty("detail_link") private String detailLink;
        @JsonProperty("isbn") private String isbn;
        @JsonProperty("call_no") private String callNo;
        @JsonProperty("kdc_code_1s") private String kdcCode1s;
        @JsonProperty("kdc_name_1s") private String kdcName1s;
    }
}
