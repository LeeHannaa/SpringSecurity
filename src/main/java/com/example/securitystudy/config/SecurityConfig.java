package com.example.securitystudy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인(기본)에 등록
public class SecurityConfig{
    // .antMatchers -> requestMatchers
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 웹페이지 접근 권한 부여
        http
                //CSRF 토큰이 없거나 유효하지 않은 경우, Spring Security는 요청을 거부하고 403 Forbidden 오류를 반환
                .csrf(AbstractHttpConfigurer::disable) //CSRF 보호 기능이 비활성화되어, CSRF 토큰을 체크하지 않음
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll() // 이 외의 페이지 접근 다 허용
                )
                .formLogin((formLogin) -> formLogin
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/user")
                );
        return http.build();
    }

}
