package com.ccc.www.bean;

/**
 * 商品评价
 * 
 * @author Administrator
 * 
 */
public class GoodsDetailCommentBean {

	int id;
	int shop_id;
	int supermaket_id;
	int goods_id;
	String good_comment;
	String bad_comment;
	String comment_time;
	int status;

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

	public int getSupermaket_id() {
		return supermaket_id;
	}

	public void setSupermaket_id(int supermaket_id) {
		this.supermaket_id = supermaket_id;
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public String getGood_comment() {
		return good_comment;
	}

	public void setGood_comment(String good_comment) {
		this.good_comment = good_comment;
	}

	public String getComment_time() {
		return comment_time;
	}

	public void setComment_time(String comment_time) {
		this.comment_time = comment_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBad_comment() {
		return bad_comment;
	}

	public void setBad_comment(String bad_comment) {
		this.bad_comment = bad_comment;
	}

	public GoodsDetailCommentBean(int id, int shop_id, int supermaket_id,
			int goods_id, String good_comment, String bad_comment,
			String comment_time, int status) {
		super();
		this.id = id;
		this.shop_id = shop_id;
		this.supermaket_id = supermaket_id;
		this.goods_id = goods_id;
		this.good_comment = good_comment;
		this.bad_comment = bad_comment;
		this.comment_time = comment_time;
		this.status = status;
	}

}
