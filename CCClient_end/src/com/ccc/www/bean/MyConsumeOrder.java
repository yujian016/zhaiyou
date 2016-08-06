package com.ccc.www.bean;

/**
 * 私人超市订单
 * 
 * @author Administrator
 * 
 */
public class MyConsumeOrder {
	String sock_name;
	String sock_log;
	float goods_price;
	int id;
	String order_no;
	int goods_id;
	int goods_number;
	String pay_time;
	double order_sum_money;
	int status;

	int supermaket_id;
	int shop_id;

	public float getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(float goods_price) {
		this.goods_price = goods_price;
	}

	public int getSupermaket_id() {
		return supermaket_id;
	}

	public void setSupermaket_id(int supermaket_id) {
		this.supermaket_id = supermaket_id;
	}

	public int getShop_id() {
		return shop_id;
	}

	public void setShop_id(int shop_id) {
		this.shop_id = shop_id;
	}

	public String getSock_name() {
		return sock_name;
	}

	public void setSock_name(String sock_name) {
		this.sock_name = sock_name;
	}

	public String getSock_log() {
		return sock_log;
	}

	public void setSock_log(String sock_log) {
		this.sock_log = sock_log;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public double getOrder_sum_money() {
		return order_sum_money;
	}

	public void setOrder_sum_money(double order_sum_money) {
		this.order_sum_money = order_sum_money;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public MyConsumeOrder(String sock_name, String sock_log, int id,
			int goods_id, int goods_number, String pay_time,
			double order_sum_money, int status, int supermaket_id, int shop_id) {
		super();
		this.sock_name = sock_name;
		this.sock_log = sock_log;
		this.id = id;
		this.goods_id = goods_id;
		this.goods_number = goods_number;
		this.pay_time = pay_time;
		this.order_sum_money = order_sum_money;
		this.status = status;
		this.supermaket_id = supermaket_id;
		this.shop_id = shop_id;
	}

}
