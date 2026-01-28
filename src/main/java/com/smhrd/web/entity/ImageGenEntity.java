package com.smhrd.web.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.smhrd.web.enumm.ImageStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image_generation")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ImageGenEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "calli_id")
	private Integer calliId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id",nullable = false)
	private UserEntity user;
	@Column(name = "calli_path", nullable = true, length = 512)
	private String calliPath;
	
	@Column(name = "text_prompt", nullable = false)
	private String textPrompt;
	@Column(name = "style_prompt", nullable = false)
	private String stylePrompt;
	@Column(name = "bg_prompt", nullable = false)
	private String bgPrompt;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ImageStatus status;
	@Column(nullable = false)
	private int size;
	@Column(name = "model_name", nullable = false)
	private String modelName;
	@Column(name = "model_version", nullable = false)
	private String modelVersion;
	@Column(name="error_message")
	private String errorMessage;
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	public void makesuc(String path) {
		this.calliPath = path;
		this.status = ImageStatus.SUCCESS;
		
	}
	
	public void makefail(String errorMesaage) {
		this.status = ImageStatus.FAIL;
		this.errorMessage=errorMesaage;
	}
	
}
