package com.rbros.oauth2.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class SignUpRequest {
    @NotBlank
    private String name;
 
    @NotBlank
    @Email
    private String email;
 
    @NotBlank
    private String password;
}
 