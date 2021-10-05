package com.rbros.oauth2.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import lombok.extern.slf4j.Slf4j;

/**
 * 이 클래스는 사용자가 인증없이 보안된 리소스에 액세스하려고 할 때 호출됩니다. 이 경우 401 Unauthorized 응답만 반환합니다.
 */
@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
 
 
    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        log.error("승인되지 않은 오류로 응답합니다. 메세지: {}", e.getMessage());
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                e.getLocalizedMessage());
    }
}