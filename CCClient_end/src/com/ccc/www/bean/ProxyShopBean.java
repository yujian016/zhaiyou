package com.ccc.www.bean;

/**
 * 私人超市
 * 
 * @author Administrator
 * 
 */
public class ProxyShopBean {
	int id;
	int hostel_id;
	String sup_market_name;
	int user_id;
	String apply_proxy_time;
	String send_goods;
	// 0---->营业中,1---->休息中
	int open_status;
	int start_send_money;

	// 1审核通过 0未审核
	int sup_market_status;

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

	public String getSup_market_name() {
		return sup_market_name;
	}

	public void setSup_market_name(String sup_market_name) {
		this.sup_market_name = sup_market_name;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getApply_proxy_time() {
		return apply_proxy_time;
	}

	public void setApply_proxy_time(String apply_proxy_time) {
		this.apply_proxy_time = apply_proxy_time;
	}

	public int getSup_market_status() {
		return sup_market_status;
	}

	public void setSup_market_status(int sup_market_status) {
		this.sup_market_status = sup_market_status;
	}

	public String getSend_goods() {
		return send_goods;
	}

	public void setSend_goods(String send_goods) {
		this.send_goods = send_goods;
	}

	public int getOpen_status() {
		return open_status;
	}

	public void setOpen_status(int open_status) {
		this.open_status = open_status;
	}

	public int getStart_send_money() {
		return start_send_money;
	}

	public void setStart_send_money(int start_send_money) {
		this.start_send_money = start_send_money;
	}

	public ProxyShopBean(int id, int hostel_id, String sup_market_name,
			int user_id, String apply_proxy_time, String send_goods,
			int open_status, int start_send_money, int sup_market_status) {
		super();
		this.id = id;
		this.hostel_id = hostel_id;
		this.sup_market_name = sup_market_name;
		this.user_id = user_id;
		this.apply_proxy_time = apply_proxy_time;
		this.send_goods = send_goods;
		this.open_status = open_status;
		this.start_send_money = start_send_money;
		this.sup_market_status = sup_market_status;
	}

}
