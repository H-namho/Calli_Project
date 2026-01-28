package com.smhrd.web.dto.oauth;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.smhrd.web.entity.UserEntity;

import lombok.Data;


@Data
public class UserPrincipalDetails implements UserDetails,OAuth2User{
	
	private UserEntity entity;
	private Map<String,Object> attributes;
	public UserPrincipalDetails(UserEntity entity) {
		this.entity=entity;
	}
	
	public UserPrincipalDetails(UserEntity entity,Map<String,Object> attributes) {
		this.entity=entity;
		this.attributes=attributes;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_"+entity.getUserRole()));
	}

	@Override
	public @Nullable String getPassword() {

		return entity.getLoginPw();
	}

	@Override
	public String getUsername() {

		return entity.getLoginId();
	}

	@Override
	public String getName() {
		
		return entity.getUserName();
	}

	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return attributes == null? Map.of() : attributes;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
