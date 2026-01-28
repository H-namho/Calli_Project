package com.smhrd.web.service;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.smhrd.web.dto.oauth.OauthGoogle;
import com.smhrd.web.dto.oauth.OauthNaver;
import com.smhrd.web.dto.oauth.OauthResponse;
import com.smhrd.web.dto.oauth.UserPrincipalDetails;
import com.smhrd.web.entity.OauthEntity;
import com.smhrd.web.entity.UserEntity;
import com.smhrd.web.repository.OauthRepository;
import com.smhrd.web.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Oatuh2UserService extends DefaultOAuth2UserService{
	
	private final UserRepository userRepository;
	private final OauthRepository oauthRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
	
		OAuth2User oauth2User =  super.loadUser(userRequest);
		System.out.println(oauth2User.getAttributes());
		String provider = userRequest.getClientRegistration().getRegistrationId(); // google, naver , kakao
		System.out.println(provider);
		
		Map<String, Object> attributes = oauth2User.getAttributes();
		
		OauthResponse oauthResponse = null;
		if(provider.equals("google")) {
			oauthResponse = new OauthGoogle(attributes);
		}else if(provider.equals("naver")) {
			oauthResponse = new OauthNaver(((Map<String,Object>)attributes.get("response")));
		}else {
			throw new OAuth2AuthenticationException("지원하지 않는 서비스입니다");
			
		}
		
		String providerId= oauthResponse.getproviderId();
		String loginId = provider+"_"+providerId;
		String email = oauthResponse.getEmail();
		String name = oauthResponse.getName();
		
		
		UserEntity userEntity = userRepository.findByLoginId(loginId);
		if(userEntity==null) {
			userEntity= UserEntity.createOAuth(loginId,name,email);
			userEntity=userRepository.save(userEntity);
			
			OauthEntity oauthEntity= OauthEntity.builder()
									.providerId(loginId)
									.provider(provider)
									.user(userEntity)
									.userName(name)
									.userEmail(email).build();
			oauthRepository.save(oauthEntity);
			 
			 System.out.println("회원가입 완료");
			
		}else{
			System.out.println("이미 존재하는 회원입니다");
		}
		
		return new UserPrincipalDetails(userEntity,attributes);
	}
}

