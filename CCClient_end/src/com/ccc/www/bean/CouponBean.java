package com.ccc.www.bean;

import java.io.Serializable;

public class CouponBean implements Serializable {

	int id;
	int coupon_type;
	float coupon_price;
	int coupon_num;
	int shop_id;
	String coupon_log;
	String detail;
	int status;
	int use_coupon_money;

	boolean check;

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public int getUse_coupon_money() {
		return use_coupon_money;
	}

	public void setUse_coupon_money(int use_coupon_money) {
		this.use_coupon_money = use_coupon_money;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCoupon_type() {
		return coupon_type;
	}

	public void setCoupon_type(int coupon_type) {
		this.coupon_type = coupon_type;
	}

	public float getCoupon_price() {
		return coupon_price;
	}

	public void setCoupon_price(float coupon_price) {
		this.coupon_price = coupon_price;
	}

	public int getCoupon_num() {
		return coupon_num;
	}

	public void setCoupon_num(int coupon_num) {
		this.coupon_num = coupon_num;
	}

	public int getShop_id() {
		return shop_id;
	}

	public void setShop_id(int shop_id) {
		this.shop_id = shop_id;
	}

	public String getCoupon_log() {
		return coupon_log;
	}

	public void setCoupon_log(String coupon_log) {
		this.coupon_log = coupon_log;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public CouponBean(int id, int coupon_type, float coupon_price,
			int coupon_num, int shop_id, String coupon_log, String detail,
			int status) {
		super();
		this.id = id;
		this.coupon_type = coupon_type;
		this.coupon_price = coupon_price;
		this.coupon_num = coupon_num;
		this.shop_id = shop_id;
		this.coupon_log = coupon_log;
		this.detail = detail;
		this.status = status;
	}

}
