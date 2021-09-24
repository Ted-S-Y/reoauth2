package com.rbros.oauth2.security.oauth.user;

import java.util.Map;

/**
 * {
 *   resultcode=00,
 *   message=success,
 *   response={
 *     id=���̵�,
 *     profile_image=�̹����ּ�.png,
 *     email=�̸���, name=�̸�
 *   }
 * }
 */
public class NaverOAuth2UserInfo extends OAuth2UserInfo {
 
    /** naver �� response �ȿ� ������ֱ� ������ response �� ���ش� */
    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get("response"));
    }
 
    @Override
    public String getId() {
        return (String) attributes.get("id");
    }
 
    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
 
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
 
    @Override
    public String getImageUrl() {
        return (String) attributes.get("profile_image");
    }
}