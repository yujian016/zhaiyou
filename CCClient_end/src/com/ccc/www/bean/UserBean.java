package com.ccc.www.bean;

public class UserBean {

	private String id;
	private String userName;
	private String userPwd;
	
	
	
	public UserBean(String id, String userName, String userPwd) {
		this.id = id;
		this.userName = userName;
		this.userPwd = userPwd;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	
	
}
