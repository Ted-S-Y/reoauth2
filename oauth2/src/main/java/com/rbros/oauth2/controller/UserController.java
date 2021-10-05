package com.rbros.oauth2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbros.oauth2.entity.User;
import com.rbros.oauth2.exception.ResourceNotFoundException;
import com.rbros.oauth2.repository.UserRepository;
import com.rbros.oauth2.security.CurrentUser;
import com.rbros.oauth2.security.UserPrincipal;

/**
 * 서버에 로그인이 성공한 경우 접근할 수 있는 User Api
 */
@RestController
public class UserController {
	
    @Autowired
    private UserRepository userRepository;
 								
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}		
