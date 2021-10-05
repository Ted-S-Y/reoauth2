package com.rbros.oauth2.security.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.rbros.oauth2.security.util.CookieUtils;

import lombok.RequiredArgsConstructor;

/**
 * OAuth2 로그인 과정 중 실패했을 때 처리하는 클래스
 * 에러 메시지를 query 에 담아 redirect_uri 쿠키에 담겨져있던 곳으로 리다이렉트 된다
 * -> http://localhost:3000/oauth2/redirect?error={errorMessage}
 * cf ) redirect_uri 가 없는 경우 기본으로 http://localhost:8080/?error={errorMessage} 가 된다
 *
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
 
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String targetUrl = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(("/"));
 
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();
 
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
 
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}