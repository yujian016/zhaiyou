package com.ccc.www.bean;

public class SupMarketCategoryBean {
	private int id;
	private String sock_category_name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSock_category_name() {
		return sock_category_name;
	}

	public void setSock_category_name(String sockCategoryName) {
		sock_category_name = sockCategoryName;
	}

	public SupMarketCategoryBean(int id, String sock_category_name) {
		super();
		this.id = id;
		this.sock_category_name = sock_category_name;
	}

}
