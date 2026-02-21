package com.example.gread.app.home.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public enum ReaderType {
    TYPE_A("감성 몰입형 독자", List.of("인물의 감정 변화와 문장의 온도를 따라", "읽는 편이에요. 읽는 동안만큼은 현실보다", "이야기의 세계에 머무는 걸 좋아해요."), List.of("811", "812", "813")),
    TYPE_B("사유 감상형 독자", List.of("느낀 감정을 쉽게 놓지 않는 독자예요.", "읽는 순간의 감정도 중요하지만, 책을", "덮은 뒤에도 생각이 이어지는 글에 끌려요."), List.of("812", "813")),
    TYPE_C("공감 휴식형 독자", List.of("짧은 글로 마음을 쉬게 하는 독자예요.", "길지 않은 문장 속에서 위로와 공감을 찾고,", "일상의 틈에 가볍게 읽을 수 있는 글을 선호해요."), List.of("814", "816")),
    TYPE_D("감정 리프레시형 독자", List.of("가볍게 읽고 기분을 전환하고 싶은 독자예요.", "깊은 몰입보단 부담 없이 읽히는 글을 좋아해요.", "새로운 시선이나 웃음을 주는 이야기에 끌려요."), List.of("817")),
    TYPE_E("깊은 사유형 독자", List.of("이야기보다 의미를 따라 읽는 독자예요.", "전개보다는 생각의 구조와 질문에 집중해요.", "한 권의 책을 천천히 곱씹으며 읽는 편이에요."), List.of("812", "813")),
    TYPE_F("관점 확장형 독자", List.of("읽고 난 뒤의 생각을 중요하게 여겨요.", "책을 통해 사회와 인간, 현실을 다른", "시선으로 바라보고 싶어요."), List.of("818")),
    TYPE_G("기록 공감형 독자", List.of("타인의 삶을 조용히 들여다보는 독자예요.", "현실적인 문장에서 안정감을 느껴요.", "일상의 기록처럼 담담한 이야기를 좋아해요."), List.of("816")),
    TYPE_H("현실 인식형 독자", List.of("사실과 맥락을 통해 세상을 이해하려는 독자예요.", "이야기보다는 정보와 흐름에 집중해요.", "우리가 사는 사회를 이해하는 독서를 선호해요."), List.of("818"));

    private final String title;
    private final List<String> descriptionLines;
    private final List<String> minorCodes; // 타입을 String으로 변경

    ReaderType(String title, List<String> descriptionLines, List<String> minorCodes) {
        this.title = title;
        this.descriptionLines = descriptionLines;
        this.minorCodes = minorCodes;
    }

    public List<String> getMajorNames() {
        Set<String> majorNames = new HashSet<>();
        for (String code : minorCodes) { // 타입을 String으로 변경
            switch (code) {
                case "811":
                case "812":
                case "815":
                    majorNames.add("창작");
                    break;
                case "813":
                case "817":
                    majorNames.add("에세이");
                    break;
                case "814":
                    majorNames.add("유머");
                    break;
                case "816":
                case "818":
                    majorNames.add("저널리즘");
                    break;
            }
        }
        return new ArrayList<>(majorNames);
    }
}
