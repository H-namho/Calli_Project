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
import lombok.Setter;

@Table(name = "question")
@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class QuestionEntity {
	
	@ManyToOne(fetch = FetchType.LAZY,optional = false)
	@JoinColumn(name = "user_id",nullable = false)
	private UserEntity user;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer qid;
	
	@Column(name = "q_title",nullable = false)
	private String qtitle;
	
	@Column(name = "q_content", nullable = false)
	private String qcontent;
	@Column(name = "q_category", nullable = false)
	private String qcategory;
	private String answer;
	@Builder.Default
	private String status = "WAIT";
	@Column(name = "q_at", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime qat;
	@Column(name = "a_at")
	private LocalDateTime aat;
}
