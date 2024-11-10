package com.example.securitystudy.config;

import com.example.securitystudy.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인(기본)에 등록
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // @Secured 활성화, @PreAuthorize, @PostAuthorize 활성화
@RequiredArgsConstructor
public class SecurityConfig{

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;


    private final CorsFilter corsFilter;

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
                .httpBasic(httpBasic -> httpBasic.disable())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll() // 이 외의 페이지 접근 다 허용
                )
                .formLogin((formLogin) -> formLogin// form 을 사용한 로그인 방식 사용 안함 (기본적인 http 로그인 방식 사용 안함) -> jwt의 기본
                        .loginPage("/loginForm").permitAll()
                        .loginProcessingUrl("/login") // /login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인 진행
                        .defaultSuccessUrl("/") // loginForm을 요청해서 로그인 하면 "/"으로 보내는데 특정 페이지에서 로그인을 하면 로그인 후 특정 페이지로 다시 반환
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/loginForm") // 구글 로그인이 완료된 후 처리가 필요!
                        // 1.코드 받기(인증) 2.엑세스토큰(권한) 3.사용자 프로필 정보 가져오기 4.(정보를 토대로 자동으로 회원가입 진행) or (추가적인 정보 필요 시 추가적인 정보 입력칸으로 이동 후 회원가입)
                        // Tip! 구글 로그인이 완료되면 엑세스 토큰 + 사용자 프로필 정보 한번에 받아옴
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(principalOauth2UserService)) // OAuth2 로그인 사용자 서비스

                );


        return http.build();
    }

}
