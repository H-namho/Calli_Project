package com.smhrd.web.enumm;

public enum MinusAction {
	GENERATE(5, "이미지 생성"), 
	DOWNLOAD(20, "이미지 다운로드");

	private final int cost;
	private final String description;

	MinusAction(int cost, String description) {
		this.cost = cost;
		this.description = description;
	}

	public int cost() {
		return cost;
	}

	public String description() {
		return description;
	}
	public enum PayType {
      FREE_TOKEN,  // 무료토큰으로 결제됨
      BALANCE      // 잔액(크레딧)으로 결제됨
	 }
}
