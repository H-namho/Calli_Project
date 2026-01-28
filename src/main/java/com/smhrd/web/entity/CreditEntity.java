package com.smhrd.web.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.smhrd.web.enumm.CreditType;
import com.smhrd.web.enumm.MinusAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name="credit")
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class CreditEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credit_id")
    private Integer creditId;

    // ✅ FK 연관관계는 객체로 매핑해야 함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CreditType type;

    @Column(name = "description")
    private String description;
    /**
     * ✅ 결제 수단 (환불 분기용)
     * - description으로 분기하지 말고 "코드값"으로 분기
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "pay_type", nullable = false)
    private MinusAction.PayType payType;

    /**
     * ✅ 어떤 작업(refId) 때문에 발생한 크레딧 로그인지 연결
     * - 이미지 생성: calliId
     * - 다운로드: downloadHistoryId 등
     */
    @Column(name = "ref_id")
    private Integer refId;

    /**
     * ✅ 중복 환불 방지 플래그 (비동기 재시도/중복 호출 대비)
     */
    @Builder.Default
    @Column(name = "refunded", nullable = false)
    private boolean refunded=false;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    public void markRefunded() {
        this.refunded = true;
    }
}


