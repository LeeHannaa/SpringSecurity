package com.example.securitystudy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인(기본)에 등록
public class SecurityConfig{
    // .antMatchers -> requestMatchers
    @Bean // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해줌
    public BCryptPasswordEncoder encoderPwd(){ // 비밀번호 암호화
        return new BCryptPasswordEncoder();
    }

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
                        .loginPage("/loginForm").permitAll()
                        .loginProcessingUrl("/login") // /login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인 진행
                        .defaultSuccessUrl("/")
                        // loginForm을 요청해서 로그인 하면 "/"으로 보내는데 특정 페이지에서 로그인을 하면 로그인 후 특정 페이지로 다시 반환
                );
        return http.build();
    }

}
