package com.smhrd.web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smhrd.web.entity.WishlistEntity;

public interface WishlistRepository extends JpaRepository<WishlistEntity, Integer> {

	List<WishlistEntity> findByUser_UserId(Integer userId);

	boolean existsByUser_UserIdAndCalli_CalliId(Integer userid,Integer callidid);

	void deleteByUser_UserIdAndCalli_CalliId(Integer userid, Integer callid);

}
