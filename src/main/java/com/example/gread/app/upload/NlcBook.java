package com.example.gread.app.upload;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "nlc_books",
        uniqueConstraints = @UniqueConstraint(name = "uk_nlc_books_external_id", columnNames = "external_id"),
        indexes = {
                @Index(name = "idx_nlc_books_kwd", columnList = "kwd"),
                @Index(name = "idx_nlc_books_type_name", columnList = "type_name")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NlcBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 검색 파라미터 기록(추적용)
    @Column(name = "kwd", length = 200)
    private String kwd;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "page_num")
    private Integer pageNum;

    @Column(name = "page_size")
    private Integer pageSize;

    @Column(name = "sort", length = 50)
    private String sort;

    @Column(name = "total")
    private Integer total;

    // 결과 필드
    @Column(name = "title_info", columnDefinition = "TEXT")
    private String titleInfo;

    @Column(name = "type_name", length = 100)
    private String typeName;

    @Column(name = "place_info", columnDefinition = "TEXT")
    private String placeInfo;

    @Column(name = "author_info", columnDefinition = "TEXT")
    private String authorInfo;

    @Column(name = "pub_info", columnDefinition = "TEXT")
    private String pubInfo;

    @Column(name = "menu_name", length = 100)
    private String menuName;

    @Column(name = "media_name", length = 100)
    private String mediaName;

    @Column(name = "manage_name", length = 100)
    private String manageName;

    @Column(name = "pub_year_info", length = 50)
    private String pubYearInfo;

    @Column(name = "control_no", length = 100)
    private String controlNo;

    @Column(name = "doc_yn", length = 10)
    private String docYn;

    @Column(name = "org_link", columnDefinition = "TEXT")
    private String orgLink;

    @Column(name = "external_id", length = 100, nullable = false)
    private String externalId;

    @Column(name = "type_code", length = 50)
    private String typeCode;

    @Column(name = "lic_yn", length = 10)
    private String licYn;

    @Column(name = "lic_text", columnDefinition = "TEXT")
    private String licText;

    @Column(name = "reg_date", length = 50)
    private String regDate;

    @Column(name = "detail_link", columnDefinition = "TEXT")
    private String detailLink;

    @Column(name = "isbn", length = 100)
    private String isbn;

    @Column(name = "call_no", length = 100)
    private String callNo;

    @Column(name = "kdc_code_1s", length = 50)
    private String kdcCode1s;

    @Column(name = "kdc_name_1s", length = 200)
    private String kdcName1s;
}
