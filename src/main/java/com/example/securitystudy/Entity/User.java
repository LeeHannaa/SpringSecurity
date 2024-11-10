package com.example.securitystudy.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@NoArgsConstructor //파라미터가 없는 디폴트 생성자를 생성
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String role; // ADMIN, USER ..
    @CreationTimestamp
    private Timestamp createDate;
    private String provider; //google
    private String providerId; //googleId

//    public List<String> getRoleList(){
//        if(this.roles.length() > 0) return Arrays.asList(this.roles.split(","));
//        return new ArrayList<>();
//    }

    @Builder
    public User(String username, String password, String email, String role, String provider, String providerId, Timestamp createDate){
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createDate = createDate;
    }
}
