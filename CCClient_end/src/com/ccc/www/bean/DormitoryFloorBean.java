package com.ccc.www.bean;

public class DormitoryFloorBean {
	int id;
	int hostel_id;
	String floor_name;
	int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHostel_id() {
		return hostel_id;
	}

	public void setHostel_id(int hostel_id) {
		this.hostel_id = hostel_id;
	}

	public String getFloor_name() {
		return floor_name;
	}

	public void setFloor_name(String floor_name) {
		this.floor_name = floor_name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public DormitoryFloorBean(int id, int hostel_id, String floor_name,
			int status) {
		super();
		this.id = id;
		this.hostel_id = hostel_id;
		this.floor_name = floor_name;
		this.status = status;
	}

}
