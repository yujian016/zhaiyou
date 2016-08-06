package com.ccc.www.bean;

public class UserCenterBean {
	private int smailImage;
	private String title;
	public int getSmailImage() {
		return smailImage;
	}
	public void setSmailImage(int smailImage) {
		this.smailImage = smailImage;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public UserCenterBean(int smailImage, String title) {
		super();
		this.smailImage = smailImage;
		this.title = title;
	}
	
	
}
