package com.smhrd.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smhrd.web.entity.OauthEntity;

public interface OauthRepository extends JpaRepository<OauthEntity, String>{

}
