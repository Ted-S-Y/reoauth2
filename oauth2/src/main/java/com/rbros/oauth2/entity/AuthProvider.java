package com.rbros.oauth2.entity;

/**
 * 각 플랫폼명을 String 이 아닌 Enum 으로 관리하기 위한 클래스
 *
 */
public enum  AuthProvider {
    local,
    facebook,
    google,
    github,
    naver,
    kakao
}
