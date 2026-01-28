package com.smhrd.web.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smhrd.web.entity.CreditEntity;
import com.smhrd.web.entity.UserEntity;
import com.smhrd.web.enumm.CreditType;

@Repository
public interface CreditRepository extends JpaRepository<CreditEntity,Integer>{

	Optional<CreditEntity> findFirstByUser_UserIdAndRefIdAndTypeOrderByCreditIdDesc(Integer userId, Integer calliId, CreditType minus);

	List<CreditEntity> findAllByUser_UserIdOrderByCreatedAtDesc(Integer userId);

	Page<CreditEntity> findByUser_UserId(Integer userId, PageRequest pageable);

}
