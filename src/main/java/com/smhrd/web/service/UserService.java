package com.smhrd.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smhrd.web.dto.ChangePwDto;
import com.smhrd.web.dto.CreditResponseDto;
import com.smhrd.web.dto.QuestionDto;
import com.smhrd.web.dto.QuestionResponseDto;
import com.smhrd.web.dto.ReviewRequestDto;
import com.smhrd.web.dto.ReviewResponseDto;
import com.smhrd.web.dto.UpdateDto;
import com.smhrd.web.dto.UserRequestDto;
import com.smhrd.web.entity.DownloadEntity;
import com.smhrd.web.entity.QuestionEntity;
import com.smhrd.web.entity.ReviewEntity;
import com.smhrd.web.entity.UserEntity;
import com.smhrd.web.repository.DownloadRepository;
import com.smhrd.web.repository.QuestionRepository;
import com.smhrd.web.repository.ReviewRepository;
import com.smhrd.web.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final QuestionRepository questionRepository;
	private final PasswordEncoder encoder;
	private final UserRepository repo;
	private final ReviewRepository reviewRepository;
	private final DownloadRepository downloadRepository;	
	
	public int join(UserRequestDto dto) {  // 회원가입
		String loginId = dto.getLoginId(); // 입력받은 id 
		String name = dto.getUserName();
		System.out.println(dto);
		boolean chk =repo.existsByLoginId(loginId); // id 중복 체크 일반회원 
		
		if(chk) { // id가 중복이라면
			return 0;
		}
		String PwEncode = encoder.encode(dto.getLoginPw()); // 비밀번호 암호화
		
		UserEntity user= UserEntity.createLocal(loginId, PwEncode,  dto.getUserName(),dto.getUserEmail(), dto.getUserPhone());
		
		repo.save(user); // 회원가입
		return 1;
		
	}

	public String findid(String useremail) { // 아이디찾기
		UserEntity entity = repo.findByUserEmail(useremail);
		if(entity==null) {
			System.out.println("존재하지 않는 회원입니다");
			return null;
		}
		return entity.getLoginId();
		
		
	}
	@Transactional  // 트랜잭션 어노테이션이 있기때문에 save 명시안해도됌
	public void updateme(UpdateDto dto,Integer userId) {
		System.out.println(dto);
		 UserEntity user=repo.findById(userId)
				 		.orElseThrow(()-> new IllegalArgumentException("해당 유저를 찾을 수 없습니다"));
		boolean change = false;
		
		if(dto.getUserEmail()!= null && !dto.getUserEmail().isBlank()) {  // 수정할 이메일값 비어있나 검증
			user.setUserEmail(dto.getUserEmail());
			change = true;
		}
		if(dto.getUserPhone()!= null && !dto.getUserPhone().isBlank()) {  // 수정할 폰넘버 비어있나 검증
			user.setUserPhone(dto.getUserPhone());
			change =true;
		}
		if(dto.getLoginPw()!=null && !dto.getLoginPw().isBlank()) {
			user.setLoginPw(encoder.encode(dto.getLoginPw()));
			
			change =true;
		}
		if(!change) {  // 아무것도 입력 안했을 경우
			throw new IllegalArgumentException("수정할 값을 입력해주세요");
		}
		
	}
	
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> showreview() {  // 리뷰목록 불러오기

        // ✅ 1) DB에서 리뷰 엔티티 목록을 가져온다
        List<ReviewEntity> reviewEntities = reviewRepository.findAll();

        // ✅ 2) 화면에 필요한 DTO 리스트를 만든다
        List<ReviewResponseDto> result = new ArrayList<>();

        // ✅ 3) for문으로 하나씩 DTO로 변환해서 담는다 (람다 X)
        for (int i = 0; i < reviewEntities.size(); i++) {
            ReviewEntity r = reviewEntities.get(i);

            ReviewResponseDto dto = ReviewResponseDto.builder()
                    .reviewId(r.getReviewId())
                    .maskedUserName(maskName(r.getUser().getUserName()))
                    .rating(r.getReviewRating())
                    .content(r.getReviewContent())
                    .reviewAt(r.getReviewAt())
                    .build();

            result.add(dto);
        }

        // ✅ 4) DTO 리스트 반환
        return result;
    }

    private String maskName(String name) { // 마스킹 메서드
        if (name == null || name.isBlank()) return "익명";
        if (name.length() == 1) return name;
        if (name.length() == 2) return name.charAt(0) + "*";
        return "" + name.charAt(0) + "*" + name.charAt(name.length() - 1);
    }
    @Transactional
	public void write(Integer userid, ReviewRequestDto dto) {
		
		DownloadEntity downloadEntity=downloadRepository.findByImage_CalliId(dto.getCalliId())
										.orElseThrow(()-> new IllegalArgumentException("다운로드 내역이 존재하지 않습니다."));
		Integer downloder = downloadEntity.getUser().getUserId();
		if(!downloder.equals(userid)) {
			throw new IllegalArgumentException("본인이 다운로드한 사진만 리뷰를 작성할 수 있습니다.");
		}
		boolean exist = reviewRepository.existsByDownload_DownloadId(downloadEntity.getDownloadId());
		if(exist) {
			throw new IllegalArgumentException("한건의 이미지에 한번만 리뷰할 수 있습니다");
		}
		ReviewEntity reviewEntity = ReviewEntity.builder()
					   .user(downloadEntity.getUser())
					   .calli(downloadEntity.getImage())
					   .download(downloadEntity)
					   .reviewContent(dto.getContent())
					   .reviewRating(dto.getRating())
					   .build();
		reviewRepository.save(reviewEntity);
 	    }
	public boolean chkid(String loginId) {
		boolean chkid =repo.existsByLoginId(loginId);
		return chkid;
	}
	
	public UserEntity findme(Integer userId) {
		UserEntity user = repo.findById(userId).orElseThrow(()-> new IllegalArgumentException("회원 정보를 찾을 수 없습니다"));
		return user;
	}


	
	@Transactional
	public boolean chkpw(Integer userId,String loginPw) {
		UserEntity entity =repo.findById(userId).orElseThrow(()-> new IllegalArgumentException("회원 정보를 찾을 수 없습니다"));
		String pw = entity.getLoginPw();
		if(encoder.matches(loginPw, pw)) {
			return true;
		};
		return false;
		
	}
	
	@Transactional
	public void question(Integer userId, QuestionDto dto) {
		
		UserEntity user =  repo.getReferenceById(userId);
		QuestionEntity entity = QuestionEntity.builder()
								.user(user)
								.qcategory(dto.getQcategory())
								.qcontent(dto.getQcontent())
								.qtitle(dto.getQtitle())
								.build();
		questionRepository.save(entity);
		
	}

	public List<QuestionResponseDto> showquestion() {
		List<QuestionEntity> list=questionRepository.findAll();
		List<QuestionResponseDto> body = new ArrayList<>();
		
		for(QuestionEntity l : list) {
			QuestionResponseDto dto = QuestionResponseDto.builder()
									.qid(l.getQid())
									.qcategory(l.getQcategory())
									.qcontent(l.getQcontent())
									.qtitle(l.getQtitle())
									.answer(l.getAnswer())
									.qat(l.getQat())
									.aat(l.getAat()).build();
			body.add(dto);
		}
		return body;
	}
	@Transactional // 비밀번호 변경
	public void changepw(ChangePwDto dto) {
		UserEntity user =repo.findByLoginId(dto.getLoginId());
		if(!(user.getUserEmail().equals(dto.getUserEmail())) || !(user.getUserName().equals(dto.getUserName()))){
			throw new IllegalArgumentException("회원 정보가 일치하지 않습니다");
		}
//		if(encoder.matches(dto.getNewPw(), user.getLoginPw())) {
//			throw new IllegalArgumentException("비밀번호가 중복입니다");
//		};
		user.setLoginPw(encoder.encode(dto.getNewPw()));
		
	
	}

	

}
