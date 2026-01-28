package com.smhrd.web.dto.oauth;

import java.util.Map;

public class OauthGoogle implements OauthResponse{
	
	private Map<String,Object> attributes;
	public OauthGoogle(Map<String,Object> attributes) {
		this.attributes=attributes;
	}
 
	@Override
	public String getprovider() {
		// TODO Auto-generated method stub
		return "google";
	}

	@Override
	public String getproviderId() {
		// TODO Auto-generated method stub
		return attributes.get("sub").toString();
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
