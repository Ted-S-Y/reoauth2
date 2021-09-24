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

@Service
public class CustomUserDetailsService implements UserDetailsService {
 
    @Autowired
    UserRepository userRepository;
 
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
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