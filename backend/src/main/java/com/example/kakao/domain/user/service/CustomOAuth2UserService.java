package com.example.kakao.domain.user.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.kakao.domain.user.entity.UserEntity;
import com.example.kakao.domain.user.oauth.CustomOAuth2User;
import com.example.kakao.domain.user.oauth.GoogleResponse;
import com.example.kakao.domain.user.oauth.NaverResponse;
import com.example.kakao.domain.user.oauth.OAuth2Response;
import com.example.kakao.domain.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("소셜로그인 실행 {}", oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = switch(registrationId){
            case "naver" -> new NaverResponse(oAuth2User.getAttributes());
            case "google" -> new GoogleResponse(oAuth2User.getAttributes());
            default -> null;
        };
        
        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+"_"+oAuth2Response.getProviderId();
        Optional<UserEntity> existUserData = userRepository.findByUsername(username); // 이미 로그인을 한번 이상 했는지?

        if (!existUserData.isPresent()) {
            UserEntity userEntity = UserEntity.builder()
                                    .username(username)
                                    .email(oAuth2Response.getEmail())
                                    .name(oAuth2Response.getName())
                                    .password(UUID.randomUUID().toString())
                                    .role("ROLE_USER")
                                    .type(oAuth2Response.getProvider()).build();
            
            userRepository.save(userEntity);

            return new CustomOAuth2User(userEntity);
        } else {
            UserEntity updateUserEntity = existUserData.get();
            updateUserEntity.updateUserInfo(oAuth2Response.getEmail(), oAuth2Response.getName());
            userRepository.save(updateUserEntity); // 바뀐게 잇나 없나 확인 해서 업데이트

            return new CustomOAuth2User(updateUserEntity);
        }
    }
    
}
