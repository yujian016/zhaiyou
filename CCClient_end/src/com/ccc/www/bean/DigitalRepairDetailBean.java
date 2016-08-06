package com.ccc.www.bean;

public class DigitalRepairDetailBean {

	int id;
	String title;
	String detail;
	String company_name;
	String company_address;
	String company_phone;
	int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getCompany_address() {
		return company_address;
	}

	public void setCompany_address(String company_address) {
		this.company_address = company_address;
	}

	public String getCompany_phone() {
		return company_phone;
	}

	public void setCompany_phone(String company_phone) {
		this.company_phone = company_phone;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public DigitalRepairDetailBean(int id, String title, String detail,
			String company_name, String company_address, String company_phone,
			int status) {
		super();
		this.id = id;
		this.title = title;
		this.detail = detail;
		this.company_name = company_name;
		this.company_address = company_address;
		this.company_phone = company_phone;
		this.status = status;
	}

}
