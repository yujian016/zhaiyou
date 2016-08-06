package com.ccc.www.bean;

public class MsgBean {
	private String id;
	private String context;
	
	public MsgBean(String id, String context) {
		this.id = id;
		this.context = context;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	
	
}
