package com.smhrd.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smhrd.web.entity.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity,Integer >{

	boolean existsByDownload_DownloadId(Integer downloadId);

}
