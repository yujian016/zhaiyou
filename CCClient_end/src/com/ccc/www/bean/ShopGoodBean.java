package com.ccc.www.bean;

import java.io.Serializable;

public class ShopGoodBean implements Serializable {
	int id;
	int shop_id;
	int user_id;
	String goods_name;
	int goods_num;
	float goods_price;
	String goods_detail;
	String goods_log1;
	String goods_log2;
	String goods_log3;

	String goods_d1;
	String goods_d2;
	String goods_d3;
	String goods_d4;
	String goods_d5;
	String goods_d6;

	int goods_status;

	int goods_cate_id;

	private int group_buy_person_num;
	private int group_info_id;
	private float group_buy_price;
	private int group_buy_status;
	
	
	
	
	public int getGroup_info_id() {
		return group_info_id;
	}

	public void setGroup_info_id(int group_info_id) {
		this.group_info_id = group_info_id;
	}

	public int getGroup_buy_person_num() {
		return group_buy_person_num;
	}

	public void setGroup_buy_person_num(int group_buy_person_num) {
		this.group_buy_person_num = group_buy_person_num;
	}

	public float getGroup_buy_price() {
		return group_buy_price;
	}

	public void setGroup_buy_price(float group_buy_price) {
		this.group_buy_price = group_buy_price;
	}

	public int getGroup_buy_status() {
		return group_buy_status;
	}

	public void setGroup_buy_status(int group_buy_status) {
		this.group_buy_status = group_buy_status;
	}

	public int getGoods_cate_id() {
		return goods_cate_id;
	}

	public void setGoods_cate_id(int goods_cate_id) {
		this.goods_cate_id = goods_cate_id;
	}

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

	public int getShop_id() {
		return shop_id;
	}

	public void setShop_id(int shop_id) {
		this.shop_id = shop_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public int getGoods_num() {
		return goods_num;
	}

	public void setGoods_num(int goods_num) {
		this.goods_num = goods_num;
	}

	public float getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(float goods_price) {
		this.goods_price = goods_price;
	}

	public String getGoods_detail() {
		return goods_detail;
	}

	public void setGoods_detail(String goods_detail) {
		this.goods_detail = goods_detail;
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

	public ShopGoodBean(int id, int shop_id, int user_id, String goods_name,
			int goods_num, float goods_price, String goods_detail,
			String goods_log1, String goods_log2, String goods_log3,
			int goods_status) {
		super();
		this.id = id;
		this.shop_id = shop_id;
		this.user_id = user_id;
		this.goods_name = goods_name;
		this.goods_num = goods_num;
		this.goods_price = goods_price;
		this.goods_detail = goods_detail;
		this.goods_log1 = goods_log1;
		this.goods_log2 = goods_log2;
		this.goods_log3 = goods_log3;
		this.goods_status = goods_status;
	}

	public String getGoods_d1() {
		return goods_d1;
	}

	public void setGoods_d1(String goods_d1) {
		this.goods_d1 = goods_d1;
	}

	public String getGoods_d2() {
		return goods_d2;
	}

	public void setGoods_d2(String goods_d2) {
		this.goods_d2 = goods_d2;
	}

	public String getGoods_d3() {
		return goods_d3;
	}

	public void setGoods_d3(String goods_d3) {
		this.goods_d3 = goods_d3;
	}

	public String getGoods_d4() {
		return goods_d4;
	}

	public void setGoods_d4(String goods_d4) {
		this.goods_d4 = goods_d4;
	}

	public String getGoods_d5() {
		return goods_d5;
	}

	public void setGoods_d5(String goods_d5) {
		this.goods_d5 = goods_d5;
	}

	public String getGoods_d6() {
		return goods_d6;
	}

	public void setGoods_d6(String goods_d6) {
		this.goods_d6 = goods_d6;
	}

}
