package com.rbros.oauth2.security.oauth;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.rbros.oauth2.entity.AuthProvider;
import com.rbros.oauth2.entity.User;
import com.rbros.oauth2.exception.OAuth2AuthenticationProcessingException;
import com.rbros.oauth2.repository.UserRepository;
import com.rbros.oauth2.security.UserPrincipal;
import com.rbros.oauth2.security.oauth.user.OAuth2UserInfo;
import com.rbros.oauth2.security.oauth.user.OAuth2UserInfoFactory;

/**
 * Spring OAuth2 에서 제공하는 OAuth2User 을 가공하여 OAuth2UserInfo 로 만들고 
 * OAuth2UserInfo 에 Email 이 있는지 검사와, A 라는 플랫폼으로 가입이 되어있는데, 
 * B 플랫폼으로 가입 하려는 경우 검사를 진행하며, 이미 존재하는 계정에 경우에는 Update 를 진행하고,
 * 없는 경우에는 새로 Insert 하며, UserPrincipal 을 리턴한다
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
 
    @Autowired
    private UserRepository userRepository;
 
    /**
     * 계정 불러오기 
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
 
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }
 
    /**
     * 계정처리
     * 
     * @param oAuth2UserRequest
     * @param oAuth2User
     * @return
     */
    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
 
        final String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
 
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());
 
        if(StringUtils.isEmpty(oAuth2UserInfo.getMobl())) {
            throw new OAuth2AuthenticationProcessingException("Mobile not found from OAuth2 provider");
        }
 
        Optional<User> userOptional = userRepository.findByMobl(oAuth2UserInfo.getMobl());
        User user;
 
        // 이미 존재하는 경우
        if(userOptional.isPresent()) {
            user = userOptional.get();
 
            // 가져온 유저의 공급자명과 넘어온 공급자명이 다른 경우
            if(!user.getProvider().equals(AuthProvider.valueOf(registrationId))) {
 
                // 이미 다른 공급자가 존재하기 때문에 가입할 수 없다
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
        	// 존재하지 않으면 계정등록
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }
 
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }
 
    /**
     * 신규 계정 등록 
     * 
     * @param oAuth2UserRequest
     * @param oAuth2UserInfo
     * @return
     */
    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
 
        User user = User.socialBuilder()
                .provider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                .providerId(oAuth2UserInfo.getId())
                .mbrNm(oAuth2UserInfo.getName())
                .mobl(oAuth2UserInfo.getMobl())
                .imageUrl(oAuth2UserInfo.getImageUrl())
                .build();
 
        return userRepository.save(user);
    }
 
    /**
     * 계정 업데이트
     * 
     * @param existingUser
     * @param oAuth2UserInfo
     * @return
     */
    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.updateNameAndImage(oAuth2UserInfo.getName(), oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
 
}