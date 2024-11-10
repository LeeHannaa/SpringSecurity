package com.example.securitystudy.config.auth;

import com.example.securitystudy.Entity.User;
import com.example.securitystudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 시큐리티 세션에는 Authentication 타입이, Authentication에는 UserDetails(PrincipalDetails) 타입이 들어가야함
    // 함수 실행 후 = 시큐리티 세션(내부 Authentication(내부 UserDetails))
    // 함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username은 input으로 받아오는 name과 동일
        User user = userRepository.findByUsername(username);
        if(user != null) return new PrincipalDetails(user);
        else return null;
    }
}