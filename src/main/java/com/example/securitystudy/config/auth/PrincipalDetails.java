package com.example.securitystudy.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인 진행
// 로그인 완료 후 시큐리티가 session을 만들어 줌 (Security ContextHolder에 세션 정보 저장)
// 오브젝트 => Authentication 타입의 객체여야 함
// Authentication 안에 User 정보가 있어야 됨.
// User 오브젝트 타입 => UserDetails 타입 객체여야 함.

// Security Session 안에 세션 정보 저장 => Authentication type => UserDetails type (PrincipalDetails)

import com.example.securitystudy.Entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// 시큐리티 세션 타입은 Authentication 객체
// Authentication는 UserDetails, OAuth2User 타입을 담을 수 있음
// => 유저 정보를 빼올 때 PrincipalDetails 타입만 맞추면 됨!!

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;
    // 일반 로그인
    public PrincipalDetails(User user){
        this.user = user;
    }
    // OAuth 로그인
    public PrincipalDetails(User user, Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }

    // 해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        // 만약 1년 동안 회원이 로그인을 안하면 휴면 계정으로 변경
        // 로그인 날짜 저장해서 현재 시간과 비교 후 return 값 변경
        return true;
    }


    // OAuth2User
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
