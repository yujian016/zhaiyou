package com.ccc.www.bean;

public class PointOrderBean {

	int id;
	String order_no;
	int buy_user_id;
	String get_goods_person_name;
	String get_goods_person_phone;
	String get_goods_person_address;
	double order_sum_integtal;
	int goods_id;
	String pay_time;
	String rand_no;
	int goods_number;
	int status;

	String goods_title;
	String log;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
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

	public double getOrder_sum_integtal() {
		return order_sum_integtal;
	}

	public void setOrder_sum_integtal(double order_sum_integtal) {
		this.order_sum_integtal = order_sum_integtal;
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public String getPay_time() {
		return pay_time;
	}

	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}

	public String getRand_no() {
		return rand_no;
	}

	public void setRand_no(String rand_no) {
		this.rand_no = rand_no;
	}

	public int getGoods_number() {
		return goods_number;
	}

	public void setGoods_number(int goods_number) {
		this.goods_number = goods_number;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getGoods_title() {
		return goods_title;
	}

	public void setGoods_title(String goods_title) {
		this.goods_title = goods_title;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public PointOrderBean(int id, String order_no, int buy_user_id,
			String get_goods_person_name, String get_goods_person_phone,
			String get_goods_person_address, double order_sum_integtal,
			int goods_id, String pay_time, String rand_no, int goods_number,
			int status, String goods_title, String log) {
		super();
		this.id = id;
		this.order_no = order_no;
		this.buy_user_id = buy_user_id;
		this.get_goods_person_name = get_goods_person_name;
		this.get_goods_person_phone = get_goods_person_phone;
		this.get_goods_person_address = get_goods_person_address;
		this.order_sum_integtal = order_sum_integtal;
		this.goods_id = goods_id;
		this.pay_time = pay_time;
		this.rand_no = rand_no;
		this.goods_number = goods_number;
		this.status = status;
		this.goods_title = goods_title;
		this.log = log;
	}

}
