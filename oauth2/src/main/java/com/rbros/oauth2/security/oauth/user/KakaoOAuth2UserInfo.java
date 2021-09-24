package com.rbros.oauth2.security.oauth.user;

import java.util.Map;

/**
 * {
 *  id=11111111,
 *  connected_at=2021-08-14T14:02:42Z,
 *  properties={
 *     nickname=�г���
 *  },
 *  kakao_account={
 *     profile_nickname_needs_agreement=false,
 *     profile_image_needs_agreement=false,
 *     profile={
 *         nickname=�г���,
 *         thumbnail_image_url=xxx.jpg,
 *         profile_image_url=xxx.jpg,
 *         is_default_image=true
 *     },
 *     has_email=true,
 *     email_needs_agreement=false,
 *     is_email_valid=true,
 *     is_email_verified=true,
 *     email=xxx@naver.com
 *  }
 * }
 * */
public class KakaoOAuth2UserInfo extends OAuth2UserInfo{
 
    /** īī���� Integer �� �޾����� �׷��� (String) �Ǵ� (Long) ���� cascading �� ���� �ʴ´�... �׷��� Integer �� �޾��ش� */
    private Integer id;
 
    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get("kakao_account"));
        this.id = (Integer) attributes.get("id");
    }
 
    @Override
    public String getId() {
        return this.id.toString();
    }
 
    @Override
    public String getName() {
        return (String) ((Map<String, Object>) attributes.get("profile")).get("nickname");
    }
 
    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }
 
    @Override
    public String getImageUrl() {
        return (String) ((Map<String, Object>) attributes.get("profile")).get("thumbnail_image_url");
    }
}