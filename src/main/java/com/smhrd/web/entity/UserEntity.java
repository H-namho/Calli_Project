package com.smhrd.web.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="user_account")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;
	@Column(name = "login_id", nullable = false, unique = true)
	private String loginId;
	@Column(name = "login_pw", nullable = true)
	private String loginPw;
	@Column(name = "user_name", nullable = true)
	private String userName;
	@Column(name="user_email", nullable = false)
	private String userEmail;
	@Column(name="user_phone_num",nullable = true)
	private String userPhone;
	@Column(name="user_role", nullable = false)
	private String userRole;

	private int balance;
	@Column(name="free_token")
	private int freeToken;
	@CreationTimestamp
	@Column(name="created_at" ,updatable=false)
	private LocalDateTime createAt;
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updateAt;
	
	
	// 일반회원
    public static UserEntity createLocal(String loginId, String encPw, String userName, String userEmail, String userPhone) {
        UserEntity user = new UserEntity();
        user.loginId = loginId;
        user.loginPw = encPw;
        user.userName = userName;
        user.userEmail = userEmail;
        user.userPhone = userPhone;
        user.applyDefaults();
        return user;
    }
    
    // OAuth 회원
    public static UserEntity createOAuth(String loginId,String userName, String userEmail) {
        UserEntity user = new UserEntity();

        user.loginId=loginId;
        user.loginPw = null; // 소셜은 비번 없음
        user.userName = userName;
        user.userEmail = userEmail;
        user.applyDefaults();
        return user;
    }
    
   
    private void applyDefaults() {
        this.userRole = "USER";
        this.freeToken = 3;
        this.balance = 0;
    }
}
