package com.smhrd.web.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smhrd.web.entity.DownloadEntity;
import com.smhrd.web.entity.UserEntity;

public interface DownloadRepository extends JpaRepository<DownloadEntity, Integer> {

	DownloadEntity findByUser_UserId(Integer userid);

	

	List<DownloadEntity> findByUser_UserIdOrderByDownloadedAt(Integer userId);



	int countByUser_UserIdAndImage_CalliId(Integer userId, Integer calliId);



	   // ✅ "이미지당 1줄" 요약형 조회를 위한 Projection (DTO로 바로 못 박아도 됨)
    interface DownloadHistoryRow {
        Integer getCalliId();
        String getCalliPath();
        LocalDateTime getLastDownloadedAt();
        Long getDownloadCount();
    }

    /**
     * ✅ [핵심] DB에서 GROUP BY로 "이미지당 1줄" 집계
     * - 같은 calliId 다운로드가 3번이면 1줄로 내려오고 downloadCount=3
     * - lastDownloadedAt은 max(downloadedAt)으로 가장 최근 시간
     */
    @Query("""
        select
            d.image.calliId as calliId,
            d.image.calliPath as calliPath,
            max(d.downloadedAt) as lastDownloadedAt,
            count(d) as downloadCount
        from DownloadEntity d
        where d.user.userId = :userId
          and d.image.calliPath is not null
          and d.image.status = com.smhrd.web.enumm.ImageStatus.SUCCESS
        group by d.image.calliId, d.image.calliPath
        order by max(d.downloadedAt) desc
    """)
    List<DownloadHistoryRow> findDownloadHistorySummary(@Param("userId") Integer userId);



	Optional<DownloadEntity> findByImage_CalliId(Integer calliId);

   
	
}
