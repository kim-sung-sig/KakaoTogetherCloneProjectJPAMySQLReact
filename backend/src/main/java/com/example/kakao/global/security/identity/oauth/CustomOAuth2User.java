package com.example.kakao.global.security.identity.oauth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.kakao.domain.user.entity.UserEntity;

public class CustomOAuth2User implements OAuth2User {

    private final UserEntity userEntity;

    public CustomOAuth2User(UserEntity userEntity) {

        this.userEntity = userEntity;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userEntity.getRole();
            }
        });
        return collection;
    }

    public Long getId(){
        return userEntity.getId();
    }
    
    @Override
    public String getName() {
        return userEntity.getName();
    }

    public String getUsername() {
        return userEntity.getUsername();
    }

}
