package com.ccc.www.bean;

import java.util.List;

public class DormitoryArea {

	String name;
	List<DormitoryBean> allDormitory;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DormitoryBean> getAllDormitory() {
		return allDormitory;
	}

	public void setAllDormitory(List<DormitoryBean> allDormitory) {
		this.allDormitory = allDormitory;
	}

	public DormitoryArea(String name, List<DormitoryBean> allDormitory) {
		super();
		this.name = name;
		this.allDormitory = allDormitory;
	}

}
