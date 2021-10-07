package com.rbros.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rbros.oauth2.security.CustomUserDetailsService;
import com.rbros.oauth2.security.RestAuthenticationEntryPoint;
import com.rbros.oauth2.security.TokenAuthenticationFilter;
import com.rbros.oauth2.security.oauth.CustomOAuth2UserService;
import com.rbros.oauth2.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.rbros.oauth2.security.oauth.OAuth2AuthenticationFailureHandler;
import com.rbros.oauth2.security.oauth.OAuth2AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

/**
 * 백엔드의 전반적인 보안 설정
 *
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomUserDetailsService customUserDetailsService;

	private final CustomOAuth2UserService customOAuth2UserService;

	private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

	@Bean
	public TokenAuthenticationFilter tokenAuthenticationFilter() {
		return new TokenAuthenticationFilter();
	}

	/*
	 * By default, Spring OAuth2 uses
	 * HttpSessionOAuth2AuthorizationRequestRepository to save the authorization
	 * request. But, since our service is stateless, we can't save it in the
	 * session. We'll save the request in a Base64 encoded cookie instead.
	 */
	@Bean
	public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
		return new HttpCookieOAuth2AuthorizationRequestRepository();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http
	         .cors()															// cors 사용
	             .and()
	         .sessionManagement()
	             .sessionCreationPolicy(SessionCreationPolicy.STATELESS)		// 세션 매니저 무상태성으로 변경 (세션 사용안함)
	             .and()
	         .csrf().disable()													// csrf 사용안함
	         .headers().frameOptions().disable()								// header <frame></frame> 끄기 -> h2-console 을 위함
	             .and()
	         .formLogin().disable()												// form login 사용안함
	         .httpBasic().disable()												// http basic login 사용안함
	         .exceptionHandling()
	             .authenticationEntryPoint(new RestAuthenticationEntryPoint())	// 인증 또는 인가에 대한 exception 핸들링 클래스를 정의
	             .and()
	         .authorizeRequests()
	             .antMatchers("/",
	                 "/error",
	                 "/favicon.ico",
	                 "/h2-console/**")
	                 .permitAll()
	             .antMatchers("/auth/**", "/oauth2/**")
	                 .permitAll()
	             .anyRequest()
	                 .authenticated()
	             .and()
	         .oauth2Login()
	             .authorizationEndpoint()
	                 .baseUri("/oauth2/authorize")								// authorizationEndpoint 는 각 플랫폼으로 리다이렉트를 하기위한 기본 url
	                 															// -> http://localhost:8080/oauth2/authorize/naver -> 네이버 로그인화면 이동
	                 .authorizationRequestRepository(cookieAuthorizationRequestRepository())
	                 .and()
	             .redirectionEndpoint()
	                 .baseUri("/oauth2/callback/*")								// - redirectEndpoint
	                 															// -> 네이버로 부터 accessToken 을 받아왔을 때 우리 서버에서 처리 할 url
	                 															// -> http://localhost:8080/oauth2/callback/naver -> naver 는 registrationId 가 된다
	                 .and()
	             .userInfoEndpoint()
	                 .userService(customOAuth2UserService)						// -> accessToken 을ㅈ 가지고 플랫폼(네이버)에서 해당 유저에 대한 정보를 가져온다 (restTemplate 이용)
	                 															// -> userService 를 통해 가져온 정보를 가공한다
	                 .and()
	             .successHandler(oAuth2AuthenticationSuccessHandler)			// -> Client 에서 요청한 redirect_uri 파라미터가 서버 application.yml 에 authorizedRedirectUris 에 설정과 같은 지
	             																// 매칭하는 작업과 토큰을 생성하여 해당 리다이렉트 주소로 토큰을 파라미터로 넘겨주는 일을 한다
	             																// -> { 해당 주소 } ?token={ JWT Token }
	             .failureHandler(oAuth2AuthenticationFailureHandler);			// -> 에러를 처리하는 일을 하며, 해당 리다이렉트 주소 리다이렉트 하며 에러메시지를 전달한다
		   																		// -> { 해당 주소 } ?error={ message }

		// Add our custom Token based authentication filter
		http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		// - TokenAuthenticationFilter 를 UsernameAuthenticationFilter 앞에 놓아 인증을 시도하는 필터로 사용된다
		//   -> 클라이언트에서 Header 에 Authorization Bearer {JWT} 을 넘겨주어야 인증이 된다
	}
}