package com.smhrd.web.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smhrd.web.dto.AdminResponseDto;
import com.smhrd.web.dto.AnswerDto;
import com.smhrd.web.entity.QuestionEntity;
import com.smhrd.web.entity.UserEntity;
import com.smhrd.web.repository.QuestionRepository;
import com.smhrd.web.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	
	private final UserRepository userRepository;
	private final QuestionRepository questionRepository;
	@Transactional(readOnly = true)
	public List<AnswerDto> showq() {
		
		List<QuestionEntity> entities =questionRepository.findAll();
		
		List<AnswerDto> list = new ArrayList<>();
		for(QuestionEntity q : entities) {
			
			AnswerDto dto = AnswerDto.builder()
							.qid(q.getQid())
							.qcategory(q.getQcategory())
							.qcontent(q.getQcontent())
							.qtitle(q.getQtitle())
							.writer(q.getUser().getUserName())
							.answer(q.getAnswer())
							.status(q.getStatus())
							.qat(q.getQat())
							.aat(q.getAat())
							.build();
			list.add(dto);
		}
		return list;
	}
	@Transactional
	public void answer(Integer qId, String answer) {
		
		QuestionEntity entity =questionRepository.findById(qId).orElseThrow(()-> new IllegalArgumentException("해당 게시물이 존재하지 않습니다"));
		entity.setAnswer(answer);
		entity.setStatus("completed");
		
	}
	public List<AdminResponseDto> manage() {
		
		List<UserEntity> entities =userRepository.findAll();
		List<AdminResponseDto> list = new ArrayList<>();
		LocalDate today = LocalDate.now();
		for(UserEntity e : entities) {
			
			LocalDateTime lastat= e.getUpdateAt();
			long humanday;
			if(lastat==null) {
				humanday = Long.MAX_VALUE;
			}else {
				humanday = ChronoUnit.DAYS.between(lastat.toLocalDate(), today); // 마지막날짜랑 오늘 날짜 자동계산
			}
			String status = humanday >= 90 ? "휴먼" : "정상";
			   // ✅ DTO 생성
			  // ✅ 휴면 처리 날짜(원하면 null로 둬도 됨)
//            LocalDateTime humanAt = null;
//            if ("휴면".equals(status) && lastat != null) {
//                humanAt = lastat.plusDays(90);
//            }

            // ✅ DTO 생성
            AdminResponseDto dto = AdminResponseDto.builder()
                    .userId(e.getUserId())
                    .userName(e.getUserName())
                    .userEmail(e.getUserEmail())
                    .status(status)
                    .humanAt(humanday)
                    .lastAt(lastat)
                    .build();
            
            list.add(dto);

		}
		return list;
	}

}
