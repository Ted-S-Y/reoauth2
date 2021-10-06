package com.rbros.oauth2.security.oauth.user;

import java.util.Map;

/**
 * 각 플랫폼 확장성을 위한 추상클래스
 *
 */
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;
 
    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
 
    public Map<String, Object> getAttributes() {
        return attributes;
    }
 
    public abstract String getId();
 
    public abstract String getName();
 
//    public abstract String getEmail();
    public abstract String getMobl();
 
    public abstract String getImageUrl();
}