package com.rbros.oauth2.payload;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

/**
 * 가입 요청을 담기 위한 DTO 클래스
 *
 */
@Getter
public class SignUpRequest {
    @NotBlank
    private String name;
 
    @NotBlank
    private String mobile;
 
    @NotBlank
    private String password;	
}
 