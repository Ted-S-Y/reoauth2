package com.rbros.oauth2.security.oauth.user;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
	 
    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }
 
    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }
 
    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
 
    @Override
    public String getMobl() {
        return (String) attributes.get("mobl");
    }
 
    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }
}