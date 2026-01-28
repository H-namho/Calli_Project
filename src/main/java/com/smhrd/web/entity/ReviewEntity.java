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
@Table(  name = "review")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Integer reviewId;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "calli_id", nullable = false)
	private ImageGenEntity calli;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "download_id", nullable = false)
	private DownloadEntity download;
	
	@Column(name = "review_rating", nullable = false)
	private int reviewRating;
	@Column(name = "review_content", nullable = false)
	private String reviewContent;
	@Column(name = "review_at", updatable = false, nullable = false)
	@CreationTimestamp
	private LocalDateTime reviewAt;
	
}
