package com.smhrd.web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smhrd.web.entity.UserEntity;

import jakarta.persistence.LockModeType;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer>{

	boolean existsByLoginId(String loginId);

	UserEntity findByLoginId(String username);

	UserEntity findByUserEmail(String useremail);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select u from UserEntity u where u.userId = :userId")
	UserEntity findByUserIdForUpdate(Integer userId);
}
