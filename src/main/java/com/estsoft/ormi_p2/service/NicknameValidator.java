package com.estsoft.ormi_p2.service;

import java.util.regex.Pattern;

public class NicknameValidator {

    private static final Pattern VALID_PATTERN = Pattern.compile("^[가-힣a-zA-Z0-9]+$");

    public static boolean isValid(String nickname) {
        if (nickname == null || nickname.isBlank()) return false;

        // 1. 특수문자/공백 검사
        if (!VALID_PATTERN.matcher(nickname).matches()) return false;

        // 2. 글자 수 검사
        int koreanCount = (int) nickname.codePoints().filter(NicknameValidator::isKorean).count();
        int totalCount = (int) nickname.codePoints().count();

        // 한글이 포함되면 최대 6자, 없으면 최대 12자
        if (koreanCount > 0 && totalCount <= 6) return true;
        if (koreanCount == 0 && totalCount <= 12) return true;

        return false;
    }

    private static boolean isKorean(int codePoint) {
        return (codePoint >= 0xAC00 && codePoint <= 0xD7A3); // 한글 유니코드 범위
    }
}

