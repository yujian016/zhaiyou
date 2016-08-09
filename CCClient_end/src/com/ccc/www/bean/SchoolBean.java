package com.ccc.www.bean;

import java.util.List;

public class SchoolBean {
	int id;

	private String school_name;
	private String school_address;
	private boolean isExpand=false;
	private List<DormitoryBean> dormitoryBeanList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSchool_name() {
		return school_name;
	}

	public void setSchool_name(String school_name) {
		this.school_name = school_name;
	}

	public String getSchool_address() {
		return school_address;
	}

	public void setSchool_address(String school_address) {
		this.school_address = school_address;
	}

	public boolean isExpand() {
		return isExpand;
	}

	public void setIsExpand(boolean isExpand) {
		this.isExpand = isExpand;
	}

	public List<DormitoryBean> getDormitoryBeanList() {
		return dormitoryBeanList;
	}

	public void setDormitoryBeanList(List<DormitoryBean> dormitoryBeanList) {
		this.dormitoryBeanList = dormitoryBeanList;
	}

	public SchoolBean(int id, String school_name, String school_address) {
		super();
		this.id = id;
		this.school_name = school_name;
		this.school_address = school_address;
	}

}
