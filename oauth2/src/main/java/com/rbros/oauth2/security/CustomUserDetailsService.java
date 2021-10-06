package com.rbros.oauth2.security;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rbros.oauth2.entity.User;
import com.rbros.oauth2.exception.ResourceNotFoundException;
import com.rbros.oauth2.repository.UserRepository;

/**
 * TokenFilter 에서 DB 로 인증을 받기 위해 만들어진 Service 클래스
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
 
    @Autowired
    UserRepository userRepository;
 
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String mobl)
            throws UsernameNotFoundException {
        User user = userRepository.findByMobl(mobl)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with mobile : " + mobl)
        );
 
        return UserPrincipal.create(user);
    }
 
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", id)
        );
 
        return UserPrincipal.create(user);
    }
}