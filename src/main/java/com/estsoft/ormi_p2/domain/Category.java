package com.estsoft.ormi_p2.domain;

public enum Category {
    RECOMMENDATION("재료 기반 요리 추천"),
    SHARE("사용자 레시피 공유"),
    REQUEST("레시피 요청 게시판"),
    CHAT("요리 수다방"),
    TIPS("자취 꿀 TIP"),
    QA("요리 궁금증 Q&A");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Category fromDisplayName(String displayName) {
        for (Category category : Category.values()) {
            if (category.getDisplayName().equals(displayName)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown display name: " + displayName);
    }
}