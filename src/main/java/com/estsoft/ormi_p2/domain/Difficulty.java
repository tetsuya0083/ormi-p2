package com.estsoft.ormi_p2.domain;

public enum Difficulty {
    EASY("하"),
    MEDIUM("중"),
    HARD("상"),
    NONE("없음");

    private final String displayName;

    Difficulty(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Difficulty fromString(String value) {
        if (value == null) {
            return null;
        }

        try {
            return Difficulty.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}