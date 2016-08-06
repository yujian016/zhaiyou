package com.ccc.www.bean;

/**
 * 校园购物订单
 * 
 * @author Administrator
 * 
 */
public class MySchoolBuyOrderBean {
	int id;
	String order_no;
	int buy_user_id;
	int shop_id;
	String get_goods_person_name;
	String get_goods_person_phone;
	String get_goods_person_address;
	double order_sum_money;
	int goods_id;
	int goods_cate_id;
	int goods_number;
	String pay_time;
	int status;
	String kd_no;
	String kd_company;

	public String getKd_no() {
		return kd_no;
	}

	public void setKd_no(String kd_no) {
		this.kd_no = kd_no;
	}

	public String getKd_company() {
		return kd_company;
	}

	public void setKd_company(String kd_company) {
		this.kd_company = kd_company;
	}

	String goods_name;
	String goods_detail;
	String goods_kd1;
	String goods_kd2;
	String goods_log1;
	String goods_log2;
	String goods_log3;
	int goods_status;
	double goods_price;

	public double getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(double goods_price) {
		this.goods_price = goods_price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBuy_user_id() {
		return buy_user_id;
	}

	public void setBuy_user_id(int buy_user_id) {
		this.buy_user_id = buy_user_id;
	}

	public int getShop_id() {
		return shop_id;
	}

	public void setShop_id(int shop_id) {
		this.shop_id = shop_id;
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

	public void setOrder_sum_money(double order_sum_money) {
		this.order_sum_money = order_sum_money;
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public int getGoods_cate_id() {
		return goods_cate_id;
	}

	public void setGoods_cate_id(int goods_cate_id) {
		this.goods_cate_id = goods_cate_id;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_log1() {
		return goods_log1;
	}

	public void setGoods_log1(String goods_log1) {
		this.goods_log1 = goods_log1;
	}

	public String getGoods_log2() {
		return goods_log2;
	}

	public void setGoods_log2(String goods_log2) {
		this.goods_log2 = goods_log2;
	}

	public String getGoods_log3() {
		return goods_log3;
	}

	public void setGoods_log3(String goods_log3) {
		this.goods_log3 = goods_log3;
	}

	public int getGoods_status() {
		return goods_status;
	}

	public void setGoods_status(int goods_status) {
		this.goods_status = goods_status;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getGoods_detail() {
		return goods_detail;
	}

	public void setGoods_detail(String goods_detail) {
		this.goods_detail = goods_detail;
	}

	public String getGoods_kd1() {
		return goods_kd1;
	}

	public void setGoods_kd1(String goods_kd1) {
		this.goods_kd1 = goods_kd1;
	}

	public String getGoods_kd2() {
		return goods_kd2;
	}

	public void setGoods_kd2(String goods_kd2) {
		this.goods_kd2 = goods_kd2;
	}

	public MySchoolBuyOrderBean(int id, String order_no, int buy_user_id,
			int shop_id, String get_goods_person_name,
			String get_goods_person_phone, String get_goods_person_address,
			double order_sum_money, int goods_id, int goods_cate_id,
			int goods_number, String pay_time, int status, String goods_name,
			String goods_detail, String goods_kd1, String goods_kd2,
			String goods_log1, String goods_log2, String goods_log3,
			int goods_status, double goods_price) {
		super();
		this.id = id;
		this.order_no = order_no;
		this.buy_user_id = buy_user_id;
		this.shop_id = shop_id;
		this.get_goods_person_name = get_goods_person_name;
		this.get_goods_person_phone = get_goods_person_phone;
		this.get_goods_person_address = get_goods_person_address;
		this.order_sum_money = order_sum_money;
		this.goods_id = goods_id;
		this.goods_cate_id = goods_cate_id;
		this.goods_number = goods_number;
		this.pay_time = pay_time;
		this.status = status;
		this.goods_name = goods_name;
		this.goods_detail = goods_detail;
		this.goods_kd1 = goods_kd1;
		this.goods_kd2 = goods_kd2;
		this.goods_log1 = goods_log1;
		this.goods_log2 = goods_log2;
		this.goods_log3 = goods_log3;
		this.goods_status = goods_status;
		this.goods_price = goods_price;
	}

}
