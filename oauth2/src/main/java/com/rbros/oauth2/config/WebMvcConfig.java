package com.rbros.oauth2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
 
    private final long MAX_AGE_SECS = 3600;	//  최대 1시간 허용
 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedOrigins("*")	// - allowedOrigins(*) : 외부에서 들어오는 모든 url 들을 허용 (보통은 파트너 또는 api 를 사용하는 곳만 열어둔다) http://www.naver.com/** , http://localhost:3000/** 처럼..
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")	// 허용되는 메소드들을 정의
        .allowedHeaders("*")	// 허용되는 헤더를 정의
        .allowCredentials(true)	// 자격증명을 허용
        .maxAge(MAX_AGE_SECS);
    }
}
