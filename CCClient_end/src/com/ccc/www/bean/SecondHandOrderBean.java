package com.ccc.www.bean;

import java.io.Serializable;

public class SecondHandOrderBean implements Serializable {
	int id;
	String order_no;
	int user_id;
	int buy_user_id;
	int goods_id;
	int goods_number;
	String pay_time;
	String get_goods_person_name;
	String get_goods_person_phone;
	String get_goods_person_address;
	double order_sum_money;
	String rand_no;
	int status;

	String title;
	String detail;
	String log1;
	String phone;
	double price;

	public void setOrder_sum_money(double order_sum_money) {
		this.order_sum_money = order_sum_money;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public int getGoods_number() {
		return goods_number;
	}

	public void setGoods_number(int goods_number) {
		this.goods_number = goods_number;
	}

	public String getPay_time() {
		return pay_time;
	}

	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}

	public int getBuy_user_id() {
		return buy_user_id;
	}

	public void setBuy_user_id(int buy_user_id) {
		this.buy_user_id = buy_user_id;
	}

	public String getGet_goods_person_name() {
		return get_goods_person_name;
	}

	public void setGet_goods_person_name(String get_goods_person_name) {
		this.get_goods_person_name = get_goods_person_name;
	}

	public String getGet_goods_person_phone() {
		return get_goods_person_phone;
	}

	public void setGet_goods_person_phone(String get_goods_person_phone) {
		this.get_goods_person_phone = get_goods_person_phone;
	}

	public String getGet_goods_person_address() {
		return get_goods_person_address;
	}

	public void setGet_goods_person_address(String get_goods_person_address) {
		this.get_goods_person_address = get_goods_person_address;
	}

	public double getOrder_sum_money() {
		return order_sum_money;
	}

	public String getRand_no() {
		return rand_no;
	}

	public void setRand_no(String rand_no) {
		this.rand_no = rand_no;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SecondHandOrderBean(int id, String order_no, int user_id,
			int buy_user_id, int goods_id, int goods_number, String pay_time,
			String get_goods_person_name, String get_goods_person_phone,
			String get_goods_person_address, double order_sum_money,
			String rand_no, int status, String title, String detail,
			String log1, String phone, double price) {
		super();
		this.id = id;
		this.order_no = order_no;
		this.user_id = user_id;
		this.buy_user_id = buy_user_id;
		this.goods_id = goods_id;
		this.goods_number = goods_number;
		this.pay_time = pay_time;
		this.get_goods_person_name = get_goods_person_name;
		this.get_goods_person_phone = get_goods_person_phone;
		this.get_goods_person_address = get_goods_person_address;
		this.order_sum_money = order_sum_money;
		this.rand_no = rand_no;
		this.status = status;
		this.title = title;
		this.detail = detail;
		this.log1 = log1;
		this.phone = phone;
		this.price = price;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
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

	public String getLog1() {
		return log1;
	}

	public void setLog1(String log1) {
		this.log1 = log1;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
