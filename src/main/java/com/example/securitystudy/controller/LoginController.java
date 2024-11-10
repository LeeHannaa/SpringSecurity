package com.example.securitystudy.controller;

import com.example.securitystudy.config.auth.PrincipalDetails;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {
    @GetMapping("/login/user/info")
    public @ResponseBody String loginInfo(@AuthenticationPrincipal PrincipalDetails userDetails){
        // PrincipalDetails implements UserDetails
        System.out.println("/login/user/info =================");
        System.out.println("userDetails : " + userDetails.getUser());
        return "일반 로그인 경로";
    }

    @GetMapping("/login/google/info")
    public @ResponseBody String loginGoogleInfo(@AuthenticationPrincipal PrincipalDetails userDetails){
        // PrincipalDetails implements OAuth2User
        System.out.println("/login/google/info =================");
        System.out.println("userDetails : " + userDetails.getAttributes());
        return "구글 로그인 경로";
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println("principalDetails : " + principalDetails.getUser());
        return "user";
    }
}
