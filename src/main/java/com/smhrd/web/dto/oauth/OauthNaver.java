package com.smhrd.web.dto.oauth;

import java.util.Map;

public class OauthNaver implements OauthResponse{
	
	private final Map<String,Object> attributes;
	public OauthNaver(Map<String,Object> attributes) {
		this.attributes=attributes;
	}

	@Override
	public String getprovider() {
		// TODO Auto-generated method stub
		return "naver";
	}

	@Override
	public String getproviderId() {
		// TODO Auto-generated method stub
		return attributes.get("id").toString();
	}

	@Override
	public String getEmail() {
		// TODO Auto-generated method stub
		return attributes.get("email").toString();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return attributes.get("name").toString();
	}



}
