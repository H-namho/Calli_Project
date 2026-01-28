package com.smhrd.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smhrd.web.entity.QuestionEntity;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer>{

}
