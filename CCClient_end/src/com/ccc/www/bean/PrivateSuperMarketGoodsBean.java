package com.ccc.www.bean;

import java.io.Serializable;

public class PrivateSuperMarketGoodsBean implements Serializable {

	int id;
	int supermaket_id;
	String goods_name;
	double goods_price;
	int goods_category_id;
	String goods_log;
	int have_num;
	int goods_status;

	int count;
	boolean check;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSupermaket_id() {
		return supermaket_id;
	}

	public void setSupermaket_id(int supermaket_id) {
		this.supermaket_id = supermaket_id;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public double getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(double goods_price) {
		this.goods_price = goods_price;
	}

	public int getGoods_category_id() {
		return goods_category_id;
	}

	public void setGoods_category_id(int goods_category_id) {
		this.goods_category_id = goods_category_id;
	}

	public String getGoods_log() {
		return goods_log;
	}

	public void setGoods_log(String goods_log) {
		this.goods_log = goods_log;
	}

	public int getHave_num() {
		return have_num;
	}

	public void setHave_num(int have_num) {
		this.have_num = have_num;
	}

	public int getGoods_status() {
		return goods_status;
	}

	public void setGoods_status(int goods_status) {
		this.goods_status = goods_status;
	}

	public PrivateSuperMarketGoodsBean(int id, int supermaket_id,
			String goods_name, double goods_price, int goods_category_id,
			String goods_log, int have_num, int goods_status) {
		super();
		this.id = id;
		this.supermaket_id = supermaket_id;
		this.goods_name = goods_name;
		this.goods_price = goods_price;
		this.goods_category_id = goods_category_id;
		this.goods_log = goods_log;
		this.have_num = have_num;
		this.goods_status = goods_status;
	}

}
