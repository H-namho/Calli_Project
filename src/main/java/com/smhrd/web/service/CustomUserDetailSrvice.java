package com.smhrd.web.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smhrd.web.dto.oauth.UserPrincipalDetails;
import com.smhrd.web.entity.UserEntity;
import com.smhrd.web.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailSrvice implements UserDetailsService {

	private final UserRepository repo;
	 

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user =repo.findByLoginId(username);
		if(user==null) {
			throw new UsernameNotFoundException("회원정보가 일치하지 않습니다");
		}
		System.out.println("로그인 성공");
		return new UserPrincipalDetails(user);
	}
	

}
