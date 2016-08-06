package com.ccc.www.bean;

import java.io.Serializable;

public class ShopBean implements Serializable {

	private int id;
	private String shop_name;
	private String shop_info;
	private String shop_category_id;
	private int user_id;
	private String shop_log;
	private String shop_status;
	private int have_goods;
	private int sell_num;
	private int good_comment_num;
	private int bad_comment_num;
	private String shop_category;

	public int getGood_comment_num() {
		return good_comment_num;
	}

	public void setGood_comment_num(int good_comment_num) {
		this.good_comment_num = good_comment_num;
	}

	public int getBad_comment_num() {
		return bad_comment_num;
	}

	public void setBad_comment_num(int bad_comment_num) {
		this.bad_comment_num = bad_comment_num;
	}

	public int getSell_num() {
		return sell_num;
	}

	public void setSell_num(int sell_num) {
		this.sell_num = sell_num;
	}

	public int getHave_goods() {
		return have_goods;
	}

	public void setHave_goods(int have_goods) {
		this.have_goods = have_goods;
	}

	public String getCategory_name() {
		return shop_category;
	}

	public void setCategory_name(String category_name) {
		this.shop_category = category_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShop_info() {
		return shop_info;
	}

	public void setShop_info(String shop_info) {
		this.shop_info = shop_info;
	}

	public String getShop_category_id() {
		return shop_category_id;
	}

	public void setShop_category_id(String shop_category_id) {
		this.shop_category_id = shop_category_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getShop_log() {
		return shop_log;
	}

	public void setShop_log(String shop_log) {
		this.shop_log = shop_log;
	}

	public String getShop_status() {
		return shop_status;
	}

	public void setShop_status(String shop_status) {
		this.shop_status = shop_status;
	}

	public String getShop_category() {
		return shop_category;
	}

	public void setShop_category(String shop_category) {
		this.shop_category = shop_category;
	}

	public ShopBean(int id, String shop_name, String shop_info,
			String shop_category_id, int user_id, String shop_log,
			String shop_status, int have_goods, int sell_num,
			int good_comment_num, int bad_comment_num, String shop_category) {
		super();
		this.id = id;
		this.shop_name = shop_name;
		this.shop_info = shop_info;
		this.shop_category_id = shop_category_id;
		this.user_id = user_id;
		this.shop_log = shop_log;
		this.shop_status = shop_status;
		this.have_goods = have_goods;
		this.sell_num = sell_num;
		this.good_comment_num = good_comment_num;
		this.bad_comment_num = bad_comment_num;
		this.shop_category = shop_category;
	}

}
