package com.rbros.oauth2.security.oauth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import com.rbros.oauth2.security.util.CookieUtils;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final int cookieExpireSeconds = 180;
 
    /** cookie �� ����Ǿ��ִ� authorizationRequest ���� �����´� authorizationUri, authorizationGrantType, responseType, clientId, redirectUri, scopes, additionalParameters */
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }
 
    /** �÷������� ������ ���� Request �� `oauth2_auth_request` ���  cookie �� ���� �Ѵ� authorizationUri, authorizationGrantType, responseType, clientId, redirectUri, scopes, additionalParameters */
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }
 
        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);
 
        /*
         * http://localhost:8080/oauth2/authorize/naver?redirect_uri=http://localhost:3000/oauth/redirect �� ��û �޾��� ��
         * http://localhost:3000/oauth/redirect �� �����´�
         * �׸��� �����ϴ� ��� cookie �� �־��ش�
         */
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
        }
    }
 
    /** remove �� ������ �ؼ� cookie �� �������� remove �� successHandler �Ǵ� failureHandler ���� �� �� �ֵ��� �Ѵ� */
    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }
 
    /**
     * ����� ������ �� ������ �� �� ���� �����̷�Ʈ�� �ϸ� ������ �����ִ� ��Ű���� �������ֱ� ���� ���ȴ�
     *  OAuth2AuthorizationRequest �� Ŭ���̾�Ʈ���� �ĸ����ͷ� ��û�� redirect_uri �� �ȴ�
     * */
    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
    
}
