package com.rbros.oauth2.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.rbros.oauth2.entity.User;

import lombok.Getter;
import lombok.Setter;

/**
 * Security 에서 인증 객체로 사용할 클래스
 */
@Getter
public class UserPrincipal implements OAuth2User, UserDetails {
    private Long id;
    private String mbrNm;
    private String mobl;
    private Collection<? extends GrantedAuthority> authorities;
    @Setter
    private Map<String, Object> attributes;
 
    public UserPrincipal(Long id, String mobl, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.mobl = mobl;
        this.authorities = authorities;
    }
 
    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));
 
        return new UserPrincipal(
                user.getId(),
                user.getMobl(),
                authorities
        );
    }
 
    public static UserPrincipal create(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }
    
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}
 
    @Override
    public String getUsername() {
        return mobl;
    }
 
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
 
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
 
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
 
    @Override
    public boolean isEnabled() {
        return true;
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
 
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
 
    @Override
    public String getName() {
        return mbrNm;
    }
}