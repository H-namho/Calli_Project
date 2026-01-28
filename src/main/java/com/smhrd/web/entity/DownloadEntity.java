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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "image_download")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DownloadEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "download_id")
	private Integer downloadId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id",nullable = false)
	private UserEntity user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "calli_id",nullable = false)
	private ImageGenEntity image;
	
//	@Column(name = "count", nullable = false)
//	@Builder.Default
//	private Integer count=0;
//	
	@Column(name = "downloaded_at", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime downloadedAt;
	
//	public void increamentCount() {
//		this.count++;
//	}
}

