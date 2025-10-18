package com.spring_security.basic.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// SecurityConfig에서 loginProcessingUrl("/login");
// login 요청이 오면 Spring Security가 자동으로 UserDetailsService 타입으로 등록된 객체를 찾아
// loadUserByUsername() 메서드를 실행해 사용자 인증을 처리한다.

// loginProcessingUrl("/login") => 로그인 요청을 가로채는 경로 지정
// UsernamePasswordAuthenticationFilter => 로그인 요청을 감지하고 인증 로직 실행
// UserDetailsService => 사용자 정보를 DB에서 불러오는 로직 담당
// loadUserByUsername(String username) => 실제 DB 조회가 이루어지는 메서드
// SecurityContext => 인증 완료된 사용자 정보를 저장하는 곳
// Security Session => SecurityContext를 보관하는 HTTP 세션

@Service
public class PrincipalDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
