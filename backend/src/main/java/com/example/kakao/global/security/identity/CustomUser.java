package com.example.kakao.global.security.identity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.kakao.domain.user.entity.UserEntity;

public class CustomUser implements UserDetails, OAuth2User{

    private final UserEntity userEntity;

    public CustomUser(UserEntity userEntity){

        this.userEntity = userEntity;
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
    public String getUsername(){
        return userEntity.getUsername();
    }

    @Override
    public String getPassword(){
        return userEntity.getPassword();
    }

    @Override
    public String getName() {
        return userEntity.getName();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
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
    
}
