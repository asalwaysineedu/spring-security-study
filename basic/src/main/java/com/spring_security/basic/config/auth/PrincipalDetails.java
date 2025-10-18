package com.spring_security.basic.config.auth;

import com.spring_security.basic.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 1. spring security가 /login으로 요청이 날아오면 그 요청을 낚아채서 로그인을 진행시킨다.
// 2. 로그인에 성공하면 security session을 만들어준다. (Security ContextHolder)
// 오브젝트 타입은 Authentication 타입 객체임
// Authentication 객체 안에는 User에 관한 정보가 들어있음

// security session => authentication => userDetails(= principalDetails)

public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 특정 유저의 권한을 리턴함.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) user::getRole);
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
