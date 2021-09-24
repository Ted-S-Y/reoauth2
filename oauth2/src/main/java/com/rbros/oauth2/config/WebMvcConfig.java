package com.rbros.oauth2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
 
    private final long MAX_AGE_SECS = 3600;	//  �ִ� 1�ð� ���
 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        .allowedOrigins("*")	// �ܺο��� ������ ��� url ���� ����Ѵ� (������ ��Ʈ�� �Ǵ� api �� ����ϴ� ���� ����д�)	http://www.naver.com/** , http://localhost:3000/** ó��..
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")	// ���Ǵ� �޼ҵ���� ���� �Ѵ�
        .allowedHeaders("*")	// ���Ǵ� ����� �����Ѵ�
        .allowCredentials(true)	// �ڰ������� ����Ѵ�
        .maxAge(MAX_AGE_SECS);
    }
}
