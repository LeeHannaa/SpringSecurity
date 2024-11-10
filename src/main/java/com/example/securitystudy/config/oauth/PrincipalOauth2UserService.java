package com.example.securitystudy.config.oauth;

import com.example.securitystudy.Entity.User;
import com.example.securitystudy.config.auth.PrincipalDetails;
import com.example.securitystudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    // 함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{ //  로그인 후 구글로부터 받은 userRequest 데이터에 대한 후처리 되는 함수
        // 구글 로그인 완료 -> code를 리턴받음 (OAuth-Client라이브러리가 처리) -> AccessToken 요청 -> userRequest 받음
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
        System.out.println("getAccessToken : " + userRequest.getAccessToken());
        System.out.println("userRequest : " + userRequest);
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("super.loadUser(userRequest) : " + oAuth2User.getAttributes()); // 회원 프로필 정보 받기

        // 구글 로그인 시 강제 회원가입 진행
        String provider = userRequest.getClientRegistration().getClientId(); // google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        // 해당 id로 회원가입 되어있는지 확인
        User user = userRepository.findByUsername(username);
        if(user == null){
            user = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}

/*
[정리]
시큐리티 세션에는 Authentication 객체 타입만 들어올 수 있음  -> 필요할 때마다 Controller에서 DI를 할 수 있음.
Authentication 안에는 UserDetails type(일반적인 로그인), OAuth2User type(Oauth 로그인) 두가지 타입이 들어갈 수 있음.
Authentication이 시큐리티 세션에 들어가는 순간 로그인이 되었다고 간주.
OAuth2User도 PrincipalDetails type으로 넣어주면 일반적인 로그인이든 Oauth 로그인이든 PrincipalDetails로 받을 수 있음!!
=> PrincipalDetails에 OAuth2User만 implements 해주면 됨!!
*/