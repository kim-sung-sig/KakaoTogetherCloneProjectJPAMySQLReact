package com.example.kakao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.kakao.DTO.UserDTO;
import com.example.kakao.entity.UserEntity;
import com.example.kakao.oauth.CustomOAuth2User;
import com.example.kakao.oauth.GoogleResponse;
import com.example.kakao.oauth.NaverResponse;
import com.example.kakao.oauth.OAuth2Response;
import com.example.kakao.repository.UserRepository;

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
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity existData = userRepository.findByUsername(username); // 이미 로그인을 한번 이상 했는지?

        if (existData == null) {
            UserEntity userEntity = UserEntity.builder()
                                                .username(username)
                                                .email(oAuth2Response.getEmail())
                                                .name(oAuth2Response.getName())
                                                .role("ROLE_USER")
                                                .type(oAuth2Response.getProvider()).build();
            userRepository.save(userEntity);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }
        else {
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());
            userRepository.save(existData); // 바뀐게 잇나 없나 확인 해서 업데이트

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(existData.getRole());
            return new CustomOAuth2User(userDTO);
        }
    }
}
