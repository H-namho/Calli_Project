package com.smhrd.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smhrd.web.entity.ImageGenEntity;

public interface ImageRepository extends JpaRepository<ImageGenEntity, Integer>{

	Optional<ImageGenEntity> findByCalliIdAndUser_UserId(Integer calliId, Integer userId);

	

}
