package com.smhrd.web.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "oauth_account")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OauthEntity {
	
	@Id
	@Column(name = "provider_user_id" , nullable = false) // provider + _+ providerId 로 저장될 예정 
	private String providerId;
	@Column(nullable = false)
	private String provider;
	
    // ✅ 1:1 매핑 (FK는 oauth_account.user_id)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true, // ✅ 1:1을 DB 레벨로 강제
        foreignKey = @ForeignKey(name = "fk_oauth_account_user")
    )
	private UserEntity user;
	@Column(name = "user_name", nullable = false)
	private String userName;
	@Column(name = "user_email" , nullable = false)
	private String userEmail;
	@CreationTimestamp
	@Column(name = "connected_at")
	private LocalDateTime connectedAt;
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
  

}
