package com.spring_security.basic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // <= 얘를 등록하면 spring security filter가 spring filter chain에 등록됨
public class SecurityConfig {
    // Spring Security 2.x: WebSecurityConfigurerAdapter 클래스를 상속받고 configure() 메소드를 오버라이딩하여 설정을 구현했습니다.
    // Spring Security 2.7 이상: SecurityFilterChain 인터페이스를 반환하는 @Bean을 등록하는 방식으로 변경되었습니다. requestMatchers()가 antMatchers()를 대체했습니다.
    // Spring Security 3.1 이상: 람다 형식을 사용해야만 내부 메소드를 구현할 수 있습니다.

    // 필독)
    // WebSecurityConfigurerAdapter는 Spring Security 5.7.1과 Spring Boot 2.7.0 이후 버전에서 **사용 중단(deprecated)**되었기 때문에 더 이상 존재하지 않습니다.
    // 이 문제를 해결하려면 WebSecurityConfigurerAdapter를 상속하는 대신,
    // @Bean을 사용하여 SecurityFilterChain 빈을 직접 선언하는 새로운 방식의 설정으로 변경해야 합니다.

    // SecurityConfig 얘는 Spring Security의 진입점 설정 파일임
    // 즉 스프링 시큐리티의 기본 필터 체인(요청 흐름)을 어떻게 구성할지 정의하는 설정 파일...
    // 그럼 얘가 왜 필요하냐?
    // 스프링 시큐리티는 “자동 설정(Auto Configuration)”을 제공하지만,
    // 실제 서비스에서는 거의 항상 인증(Authentication), 인가(Authorization),
    // 필터(Filtering) 규칙 등을 직접 커스터마이징 해야하기 때문임

    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    // 🍀🍀🍀 filterChain 🍀🍀🍀
    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    // 시큐리티 대부분의 설정을 담당하는 메소드
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // csrf(AbstractHttpConfigurer::disable) => 스프링 시큐리티 설정에서 **CSRF 공격을 방어하는 기능을 비활성화**하는 코드
        // authorizeHttpRequests() => 요청 URL별 접근 권한(인가)을 설정하는 핵심 메서드
        // requestMatchers("/path/**") => 특정 경로 패턴 지정
        // permitAll() => 특정 경로에 대한 접근을 인증 여부와 관계없이 모든 사용자에게 허용하는 역할
        // denyAll() => 전부 접근 불가
        // authenticated() => 인증된 사용자만 접근
        // hasRole("USER") => 특정 역할(ROLE_)을 가진 사용자만 접근
        // hasAuthority("SCOPE_read") => 특정 권한(Authority) 보유 시 접근 허용

        // withObjectPostProcessor() => 특정 필터나 객체가 생성될 때 추가 로직을 넣고 싶을 때 사용
        // shouldFilterAllDispatcherTypes() => 기본적으로 Spring Security 필터는 REQUEST 타입에만 적용되지만, 필요하면 FORWARD, ERROR 등에도 필터 적용 가능.
        //                                     DSL에서는 잘 안 쓰고, AbstractHttpConfigurer 내부에서 쓰임.
        //                                     주로 프레임워크 내부에서 필터 적용 범위를 제어하는 설정.
        // dispatcherTypeMatchers() => 특정 DispatcherType을 매칭 조건으로 사용해서, 어떤 요청에 보안 필터를 적용할지 정하는 객체/설정.
        //                             Spring Security 내부에서 어떤 DispatcherType에 보안 규칙을 적용할지 선택하는 옵션.
        //                             일반 MVC나 REST API 개발에서는 기본값(Request만 필터 적용) 그대로 두면 됨.

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/loginForm")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/"));

        return http.build();
    }
}