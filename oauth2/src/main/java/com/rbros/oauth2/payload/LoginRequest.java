package com.rbros.oauth2.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;

/**
 * 로그인요청을 담기 위한 DTO 클래스
 * 
 */
@Getter
public class LoginRequest {
    @NotBlank
    @Email
    private String email;
 
    @NotBlank
    private String password;
}