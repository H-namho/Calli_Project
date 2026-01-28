package com.smhrd.web.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wishlist")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WishlistEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wishlist_id")
	private Integer wishlistId;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "calli_id", nullable = false)
	private ImageGenEntity calli;
	
	@Column(name = "wished_at",nullable = false,updatable = false)
	@CreationTimestamp
	private LocalDateTime wishedAt;
}
