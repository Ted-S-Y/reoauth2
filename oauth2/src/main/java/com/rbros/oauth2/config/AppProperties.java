package com.rbros.oauth2.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oauth2 = new OAuth2();
 
    /**
     * JWT 토큰의 암호키와, 만료기간을 설정할 때 사용
     *
     */
    @Getter
    @Setter
    public static class Auth {
        private String tokenSecret;    		// JWT 토큰의 암호키
        private long tokenExpirationMsec;	// 만료기간
    }
 
    /**
     * 프론트 엔드 클라이언트가 /oauth2/authorize 요청에서 지정한 redirectUri
     *
     */
    @Getter
    public static final class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();
 
        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }
}