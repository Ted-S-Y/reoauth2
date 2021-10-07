package com.rbros.oauth2.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rbros.oauth2.entity.AuthProvider;
import com.rbros.oauth2.entity.User;
import com.rbros.oauth2.exception.BadRequestException;
import com.rbros.oauth2.payload.ApiResponse;
import com.rbros.oauth2.payload.AuthResponse;
import com.rbros.oauth2.payload.LoginRequest;
import com.rbros.oauth2.payload.SignUpRequest;
import com.rbros.oauth2.repository.UserRepository;
import com.rbros.oauth2.security.TokenProvider;

/**
 * 서버에 이메일로 가입하기 위한 Auth Api
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
 
    @Autowired
    private AuthenticationManager authenticationManager;
 
    @Autowired
    private UserRepository userRepository;
 
//    @Autowired
//    private PasswordEncoder passwordEncoder;
 
    @Autowired
    private TokenProvider tokenProvider;
 
    /**
     * 로그인
     * 
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
 
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
 
        SecurityContextHolder.getContext().setAuthentication(authentication);
 
        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }
 
    /**
     * 가입 
     * 
     * @param signUpRequest
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    	
    	// 중복 체크
        if(userRepository.existsByMobile(signUpRequest.getMobile())) {
            throw new BadRequestException("Email address already in use.");
        }
 
        // Creating user's account
        User user = new User();
        user.setMbrNm(signUpRequest.getName());
        user.setMobl(signUpRequest.getMobile());
        user.setProvider(AuthProvider.local);
 
        User result = userRepository.save(user);
 
        URI location = ServletUriComponentsBuilder				
                .fromCurrentContextPath()		// 사용자 요청 Uri
                .path("/user/me")				// buildAndExpand를 통해 얻은 값이 들어오거나 일반 값 설정
                .buildAndExpand(result.getId())	// 넣어줄값
                .toUri();						// uri생성			
 
        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@"));
    }
 
}