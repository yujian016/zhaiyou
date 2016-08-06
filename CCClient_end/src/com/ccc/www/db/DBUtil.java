package com.ccc.www.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ccc.www.bean.GoodsBean;
import com.ccc.www.bean.PrivateSuperMarketGoodsBean;
import com.ccc.www.bean.ProxyStockOrderBean;
import com.ccc.www.bean.SecondaryTradeBean;
import com.ccc.www.bean.ShopGoodBean;
import com.ccc.www.bean.SockBean;

public class DBUtil {
	private static String TAG = "DBUtil";

	static DatabaseHelper mDbHelper = null;
	static SQLiteDatabase mDb = null;

	private static final String PROXYSTOCKCART = "proxystockcart";
	private static final String PROXYSTOCKORDER = "proxystockorder";
	private static final String DigitalCart = "digitalcart";
	private static final String SchoolBuyCart = "schoolbuycart";
	private static final String PrivateSupermarketCart = "privatesupermarketcart";
	private static final String SecondaryTradeCart = "secondarytradecart";
	private static final String ShopCart = "shopcart";

	/**
	 * 
	 * 新增代理商品购物车
	 * 
	 * @param context
	 * @param province
	 */

	public static void insertProxyStockCart(Context context, SockBean bean,
			int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("userid", userid);
		values.put("sockid", bean.getId());
		values.put("sock_name", bean.getSock_name());
		values.put("sock_price", bean.getSock_price() + "");
		values.put("sock_category_id", bean.getSock_category_id());
		values.put("sock_category_name", bean.getSock_category_name());
		values.put("sock_log", bean.getSock_log());
		values.put("sock_status", bean.getSock_status());
		values.put("count", bean.getCount());
		mDb.insert(PROXYSTOCKCART, null, values);
	}

	/**
	 * 更新代理商品数量
	 * 
	 * @param context
	 * @param bean
	 * @param userid
	 */
	public static void updateProxyStockCartCount(Context context,
			SockBean bean, int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("count", bean.getCount());
		mDb.update(PROXYSTOCKCART, values, "  userid=? and  sockid=?  ",
				new String[] { userid + "", bean.getId() + "" });
	}

	/**
	 * 更新代理商品信息
	 * 
	 * @param context
	 * @param bean
	 * @param userid
	 */
	public static void updateProxyStockCartInfo(Context context, SockBean bean,
			int sockid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("sock_name", bean.getSock_name());
		values.put("sock_price", bean.getSock_price());
		values.put("sock_category_id", bean.getSock_category_id());
		values.put("sock_category_name", bean.getSock_category_name());
		values.put("sock_log", bean.getSock_log());
		values.put("sock_status", bean.getSock_status());
		mDb.update(PROXYSTOCKCART, values, "     sockid=?  ",
				new String[] { bean.getId() + "" });
	}

	/**
	 * 取出当前用户的代理商品购物车
	 * 
	 * @param context
	 * @param userid
	 */
	public static List<SockBean> getProxyStockCart(Context context, int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getReadableDatabase();

		List<SockBean> allSockBean = new ArrayList<SockBean>();

		Cursor cursor = mDb.query(PROXYSTOCKCART, new String[] { "sockid",
				"sock_name", "sock_price", "sock_category_id",
				"sock_category_name", "sock_log", "sock_status", "count" },
				"  userid=?  ", new String[] { userid + "" }, null, null, null);
		while (cursor.moveToNext()) {
			int sockid = cursor.getInt(cursor.getColumnIndex("sockid"));
			String sock_name = cursor.getString(cursor
					.getColumnIndex("sock_name"));
			String sock_price = cursor.getString(cursor
					.getColumnIndex("sock_price"));
			int sock_category_id = cursor.getInt(cursor
					.getColumnIndex("sock_category_id"));

			String sock_category_name = cursor.getString(cursor
					.getColumnIndex("sock_category_name"));
			String sock_log = cursor.getString(cursor
					.getColumnIndex("sock_log"));

			int sock_status = cursor.getInt(cursor
					.getColumnIndex("sock_status"));
			int count = cursor.getInt(cursor.getColumnIndex("count"));

			if (count > 0) {
				SockBean bean = new SockBean(sockid, sock_name,
						Float.parseFloat(sock_price), sock_category_id,
						sock_category_name, sock_log, sock_status, count);
				allSockBean.add(bean);
			}
		}

		cursor.close();
		return allSockBean;
	}

	/**
	 * 删除当前用户的代理商品购物车
	 * 
	 * @param context
	 * @param userid
	 */
	public static void deleteProxyStockCart(Context context, int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		mDb.delete(PROXYSTOCKCART, "  userid=? ", new String[] { userid + "" });
	}

	/**
	 * 检查代理商品是否存在
	 * 
	 * @param sockid
	 * @param userid
	 * @return
	 */
	public static boolean CheckProxyStockExist(Context context, int sockid,
			int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getReadableDatabase();
		boolean exist = false;
		Cursor cursor = mDb.query(PROXYSTOCKCART, new String[] { "sockid",
				"sock_name", "sock_price", "sock_category_id",
				"sock_category_name", "sock_log", "sock_status", "count" },
				"  userid=? and  sockid=? ", new String[] { userid + "",
						sockid + "" }, null, null, null);
		if (cursor.moveToNext()) {
			exist = true;
		}
		cursor.close();
		return exist;
	}

	/**
	 * 
	 * 新增代理商品订单
	 * 
	 * @param context
	 * @param province
	 */
	public static void insertProxyStockOrder(Context context,
			ProxyStockOrderBean bean) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("proxy_order", bean.getProxy_order());
		values.put("rand_no", bean.getRand_no());
		values.put("proxy_user_id", bean.getProxy_user_id());
		values.put("get_goods_person_name", bean.getGet_goods_person_name());
		values.put("get_goods_person_phone", bean.getGet_goods_person_phone());
		values.put("get_goods_person_address",
				bean.getGet_goods_person_address());
		values.put("order_sum_money", String.valueOf(bean.getOrder_sum_money()));
		values.put("submitstatus", bean.getSubmitstatus());
		mDb.insert(PROXYSTOCKORDER, null, values);
	}

	/**
	 * 更新代理商品数量
	 * 
	 * @param context
	 * @param bean
	 * @param userid
	 */
	public static void updateProxyStockOrderStatus(Context context,
			String rand_no, int proxy_user_id) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("submitstatus", 1);
		mDb.update(PROXYSTOCKORDER, values,
				"  rand_no=? and  proxy_user_id=?  ", new String[] { rand_no,
						proxy_user_id + "" });
	}

	/**
	 * 
	 * 新增潮流数码购物车
	 * 
	 * @param context
	 * @param province
	 */

	public static void insertDigitalCart(Context context, GoodsBean bean) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("goodsid", bean.getId());
		values.put("shop_id", bean.getShop_id());
		values.put("user_id", bean.getUser_id());
		values.put("goods_price", bean.getGoods_price());
		values.put("goods_name", bean.getGoods_name());
		values.put("goods_num", bean.getGoods_num());
		values.put("digital_goods_category_id",
				bean.getDigital_goods_category_id());
		values.put("shop_category_id", bean.getShop_category_id());
		values.put("goods_detail", bean.getGoods_detail());
		values.put("goods_log1", bean.getGoods_log1());
		values.put("goods_log2", bean.getGoods_log2());
		values.put("goods_log3", bean.getGoods_log3());
		values.put("goods_status", bean.getGoods_status());
		values.put("count", bean.getCount());
		mDb.insert(DigitalCart, null, values);
	}

	/**
	 * 更新潮流数码购物车数量
	 * 
	 * @param context
	 * @param bean
	 * @param userid
	 */
	public static void updateDigitalCartCount(Context context, GoodsBean bean) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("count", bean.getCount());
		mDb.update(DigitalCart, values, "  goodsid=? and  shop_id=?  ",
				new String[] { bean.getId() + "", bean.getShop_id() + "" });
	}

	/**
	 * 删除潮流数码购物车
	 * 
	 * @param context
	 * @param userid
	 */
	public static void deleteDigitalCart(Context context) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		mDb.delete(DigitalCart, null, null);
	}

	/**
	 * 取出当前用户的潮流数码商品购物车
	 * 
	 * @param context
	 * @param userid
	 */
	public static List<GoodsBean> getDigitalCart(Context context) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getReadableDatabase();

		List<GoodsBean> allGoodsBean = new ArrayList<GoodsBean>();

		Cursor cursor = mDb.query(DigitalCart, new String[] { "goodsid",
				"shop_id", "user_id", "goods_name", "goods_price", "goods_num",
				"digital_goods_category_id", "shop_category_id",
				"goods_detail", "goods_log1", "goods_log2", "goods_log3",
				"goods_status", "count" }, null, null, null, null, null);
		while (cursor.moveToNext()) {
			int count = cursor.getInt(cursor.getColumnIndex("count"));
			int id = cursor.getInt(cursor.getColumnIndex("goodsid"));
			int shop_id = cursor.getInt(cursor.getColumnIndex("shop_id"));
			int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
			int goods_num = cursor.getInt(cursor.getColumnIndex("goods_num"));
			int digital_goods_category_id = cursor.getInt(cursor
					.getColumnIndex("digital_goods_category_id"));
			int shop_category_id = cursor.getInt(cursor
					.getColumnIndex("shop_category_id"));

			String goods_detail = cursor.getString(cursor
					.getColumnIndex("goods_detail"));

			String goods_name = cursor.getString(cursor
					.getColumnIndex("goods_name"));
			String goods_log1 = cursor.getString(cursor
					.getColumnIndex("goods_log1"));
			String goods_log2 = cursor.getString(cursor
					.getColumnIndex("goods_log2"));
			String goods_log3 = cursor.getString(cursor
					.getColumnIndex("goods_log3"));
			String goods_priceStr = cursor.getString(cursor
					.getColumnIndex("goods_price"));

			float goods_price = Float.parseFloat(goods_priceStr);

			int goods_status = cursor.getInt(cursor
					.getColumnIndex("goods_status"));

			if (count > 0) {
				GoodsBean bean = new GoodsBean(id, shop_id, user_id,
						digital_goods_category_id, shop_category_id,
						goods_name, goods_num, goods_price, goods_detail,
						goods_log1, goods_log2, goods_log3, goods_status, count);
				allGoodsBean.add(bean);
			}
		}
		cursor.close();
		return allGoodsBean;
	}

	/**
	 * 检查潮流数码商品是否存在
	 * 
	 * @param sockid
	 * @param userid
	 * @return
	 */
	public static boolean CheckDigitalGoodsExist(Context context, int goodsid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getReadableDatabase();
		boolean exist = false;
		Cursor cursor = mDb.query(DigitalCart, new String[] { "shop_id",
				"user_id", "count" }, "  goodsid=?   ", new String[] { goodsid
				+ "" }, null, null, null);
		if (cursor.moveToNext()) {
			exist = true;
		}
		cursor.close();
		return exist;
	}

	/**
	 * 
	 * 新增校园购物购物车
	 * 
	 * @param context
	 * @param province
	 */

	public static void insertSchoolBuyCart(Context context, GoodsBean bean) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("goodsid", bean.getId());
		values.put("shop_id", bean.getShop_id());
		values.put("user_id", bean.getUser_id());
		values.put("goods_price", bean.getGoods_price());
		values.put("goods_name", bean.getGoods_name());
		values.put("goods_num", bean.getGoods_num());
		values.put("digital_goods_category_id",
				bean.getDigital_goods_category_id());
		values.put("shop_category_id", bean.getShop_category_id());
		values.put("goods_detail", bean.getGoods_detail());
		values.put("goods_log1", bean.getGoods_log1());
		values.put("goods_log2", bean.getGoods_log2());
		values.put("goods_log3", bean.getGoods_log3());
		values.put("goods_status", bean.getGoods_status());
		values.put("count", bean.getCount());
		mDb.insert(SchoolBuyCart, null, values);
	}

	/**
	 * 更新校园购物购物车数量
	 * 
	 * @param context
	 * @param bean
	 * @param userid
	 */
	public static void updateSchoolBuyCartCount(Context context, GoodsBean bean) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("count", bean.getCount());
		mDb.update(SchoolBuyCart, values, "  goodsid=? and  shop_id=?  ",
				new String[] { bean.getId() + "", bean.getShop_id() + "" });
	}

	/**
	 * 删除校园购物购物车
	 * 
	 * @param context
	 * @param userid
	 */
	public static void deleteSchoolBuyCart(Context context) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		mDb.delete(SchoolBuyCart, null, null);
	}

	/**
	 * 取出当前用户的校园购物购物车
	 * 
	 * @param context
	 * @param userid
	 */
	public static List<GoodsBean> getSchoolBuyCart(Context context) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getReadableDatabase();

		List<GoodsBean> allGoodsBean = new ArrayList<GoodsBean>();

		Cursor cursor = mDb.query(SchoolBuyCart, new String[] { "goodsid",
				"shop_id", "user_id", "goods_name", "goods_price", "goods_num",
				"digital_goods_category_id", "shop_category_id",
				"goods_detail", "goods_log1", "goods_log2", "goods_log3",
				"goods_status", "count" }, null, null, null, null, null);
		while (cursor.moveToNext()) {
			int count = cursor.getInt(cursor.getColumnIndex("count"));
			int id = cursor.getInt(cursor.getColumnIndex("goodsid"));
			int shop_id = cursor.getInt(cursor.getColumnIndex("shop_id"));
			int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
			int goods_num = cursor.getInt(cursor.getColumnIndex("goods_num"));
			int digital_goods_category_id = cursor.getInt(cursor
					.getColumnIndex("digital_goods_category_id"));
			int shop_category_id = cursor.getInt(cursor
					.getColumnIndex("shop_category_id"));

			String goods_detail = cursor.getString(cursor
					.getColumnIndex("goods_detail"));

			String goods_name = cursor.getString(cursor
					.getColumnIndex("goods_name"));
			String goods_log1 = cursor.getString(cursor
					.getColumnIndex("goods_log1"));
			String goods_log2 = cursor.getString(cursor
					.getColumnIndex("goods_log2"));
			String goods_log3 = cursor.getString(cursor
					.getColumnIndex("goods_log3"));
			String goods_priceStr = cursor.getString(cursor
					.getColumnIndex("goods_price"));

			float goods_price = Float.parseFloat(goods_priceStr);

			int goods_status = cursor.getInt(cursor
					.getColumnIndex("goods_status"));

			if (count > 0) {
				GoodsBean bean = new GoodsBean(id, shop_id, user_id,
						digital_goods_category_id, shop_category_id,
						goods_name, goods_num, goods_price, goods_detail,
						goods_log1, goods_log2, goods_log3, goods_status, count);
				allGoodsBean.add(bean);
			}
		}
		cursor.close();
		return allGoodsBean;
	}

	/**
	 * 检查校园购物商品是否存在
	 * 
	 * @param sockid
	 * @param userid
	 * @return
	 */
	public static boolean CheckSchoolBuyCartGoodsExist(Context context,
			int goodsid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getReadableDatabase();
		boolean exist = false;
		Cursor cursor = mDb.query(SchoolBuyCart, new String[] { "shop_id",
				"user_id", "count" }, "  goodsid=?   ", new String[] { goodsid
				+ "" }, null, null, null);
		if (cursor.moveToNext()) {
			exist = true;
		}
		cursor.close();
		return exist;
	}

	/**
	 * 检查私人超市购物车商品是否存在
	 * 
	 * @param sockid
	 * @param userid
	 * @return
	 */
	public static boolean CheckPrivateSupermarketCartGoodsExist(
			Context context, int goodsid, int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getReadableDatabase();
		boolean exist = false;
		Cursor cursor = mDb.query(PrivateSupermarketCart,
				new String[] { "supermaket_id" }, "  goodsid=?  and userid=? ",
				new String[] { goodsid + "", userid + "" }, null, null, null);
		if (cursor.moveToNext()) {
			exist = true;
		}
		cursor.close();
		return exist;
	}

	/**
	 * 更新私人超市购物车商品数量
	 * 
	 * @param context
	 * @param bean
	 * @param userid
	 */
	public static void updatePrivateSupermarketCartCount(Context context,
			PrivateSuperMarketGoodsBean bean, int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("count", bean.getCount());
		mDb.update(PrivateSupermarketCart, values,
				"   goodsid=?  and userid=?  ", new String[] {
						bean.getId() + "", userid + "" });
	}

	/**
	 * 
	 * 新增私人超市购物车商品购物车
	 * 
	 * @param context
	 * @param province
	 */

	public static void insertPrivateSupermarketCart(Context context,
			PrivateSuperMarketGoodsBean bean, int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("userid", userid);
		values.put("goodsid", bean.getId());
		values.put("supermaket_id", bean.getSupermaket_id());
		values.put("goods_name", bean.getGoods_name());
		values.put("goods_price", bean.getGoods_price() + "");
		values.put("goods_category_id", bean.getGoods_category_id());
		values.put("goods_log", bean.getGoods_log());
		values.put("have_num", bean.getHave_num());
		values.put("goods_status", bean.getGoods_status());
		values.put("count", bean.getCount());
		mDb.insert(PrivateSupermarketCart, null, values);
	}

	/**
	 * 取出当前用户的私人超市购物车
	 * 
	 * @param context
	 * @param userid
	 */
	public static List<PrivateSuperMarketGoodsBean> getPrivateSupermarketCart(
			Context context, int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getReadableDatabase();

		List<PrivateSuperMarketGoodsBean> allSockBean = new ArrayList<PrivateSuperMarketGoodsBean>();

		Cursor cursor = mDb.query(PrivateSupermarketCart, new String[] {
				"goodsid", "supermaket_id", "goods_name", "goods_price",
				"goods_category_id", "goods_log", "have_num", "goods_status",
				"count" }, "  userid=?  ", new String[] { userid + "" }, null,
				null, null);
		while (cursor.moveToNext()) {

			int id = cursor.getInt(cursor.getColumnIndex("goodsid"));
			int supermaket_id = cursor.getInt(cursor
					.getColumnIndex("supermaket_id"));
			int goods_category_id = cursor.getInt(cursor
					.getColumnIndex("goods_category_id"));
			int have_num = cursor.getInt(cursor.getColumnIndex("have_num"));
			int goods_status = cursor.getInt(cursor
					.getColumnIndex("goods_status"));

			String goods_name = cursor.getString(cursor
					.getColumnIndex("goods_name"));
			String goods_price = cursor.getString(cursor
					.getColumnIndex("goods_price"));
			String goods_log = cursor.getString(cursor
					.getColumnIndex("goods_log"));

			int count = cursor.getInt(cursor.getColumnIndex("count"));

			if (count > 0) {
				PrivateSuperMarketGoodsBean bean = new PrivateSuperMarketGoodsBean(
						id, supermaket_id, goods_name,
						Float.parseFloat(goods_price), goods_category_id,
						goods_log, have_num, goods_status);
				bean.setCount(count);
				allSockBean.add(bean);
			}
		}

		cursor.close();
		return allSockBean;
	}

	/**
	 * 删除当前用户的私人超市购物车
	 * 
	 * @param context
	 * @param userid
	 */
	public static void deletePrivateSupermarketCart(Context context, int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		mDb.delete(PrivateSupermarketCart, "  userid=? ", new String[] { userid
				+ "" });
	}

	/**
	 * 
	 * 新增二手商品购物车
	 * 
	 * @param context
	 * @param province
	 */

	public static void insertSecondaryTradeCart(Context context,
			SecondaryTradeBean bean, int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("userid", userid);
		values.put("tradeid", bean.getId());
		values.put("user_id", bean.getUser_id());
		values.put("title", bean.getTitle());
		values.put("price", bean.getPrice() + "");
		values.put("detail", bean.getDetail());
		values.put("log1", bean.getLog1());
		values.put("log2", bean.getLog2());
		values.put("log3", bean.getLog3());
		values.put("phone", bean.getPhone());
		values.put("status", bean.getStatus());
		mDb.insert(SecondaryTradeCart, null, values);
	}

	/**
	 * 删除当前用户的二手购物车
	 * 
	 * @param context
	 * @param userid
	 */
	public static void deleteSecondaryTradeCart(Context context, int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		mDb.delete(SecondaryTradeCart, "  userid=? ", new String[] { userid
				+ "" });
	}

	/**
	 * 取出当前用户的二手购物车
	 * 
	 * @param context
	 * @param userid
	 */
	public static List<SecondaryTradeBean> getSecondaryTradeCart(
			Context context, int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getReadableDatabase();

		List<SecondaryTradeBean> allSockBean = new ArrayList<SecondaryTradeBean>();

		Cursor cursor = mDb.query(SecondaryTradeCart, new String[] { "tradeid",
				"user_id", "title", "price", "detail", "log1", "log2", "log3",
				"phone", "status" }, "  userid=?  ",
				new String[] { userid + "" }, null, null, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("tradeid"));
			int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String pricestr = cursor.getString(cursor.getColumnIndex("price"));
			String detail = cursor.getString(cursor.getColumnIndex("detail"));

			float price = Float.parseFloat(pricestr);

			String log1 = cursor.getString(cursor.getColumnIndex("log1"));
			String log2 = cursor.getString(cursor.getColumnIndex("log2"));
			String log3 = cursor.getString(cursor.getColumnIndex("log3"));
			String phone = cursor.getString(cursor.getColumnIndex("phone"));
			int status = cursor.getInt(cursor.getColumnIndex("status"));

			SecondaryTradeBean bean = new SecondaryTradeBean(id, user_id,
					title, price, detail, log1, log2, log3, phone, status);

			allSockBean.add(bean);
		}

		cursor.close();
		return allSockBean;
	}

	/**
	 * 检查二手购物车商品是否存在
	 * 
	 * @param sockid
	 * @param userid
	 * @return
	 */
	public static boolean CheckSecondaryTradeCartGoodsExist(Context context,
			int tradeid, int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getReadableDatabase();
		boolean exist = false;
		Cursor cursor = mDb.query(SecondaryTradeCart, new String[] { "title" },
				"  tradeid=?  and userid=? ", new String[] { tradeid + "",
						userid + "" }, null, null, null);
		if (cursor.moveToNext()) {
			exist = true;
		}
		cursor.close();
		return exist;
	}

	/**
	 * 检查商铺购物车商品是否存在
	 * 
	 * @param sockid
	 * @param userid
	 * @return
	 */
	public static boolean CheckShopCartGoodsExist(Context context, int goodsid,
			int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getReadableDatabase();
		boolean exist = false;
		Cursor cursor = mDb.query(ShopCart, new String[] { "goodsid" },
				"  goodsid=?  and userid=? ", new String[] { goodsid + "",
						userid + "" }, null, null, null);
		if (cursor.moveToNext()) {
			exist = true;
		}
		cursor.close();
		return exist;
	}

	/**
	 * 取出当前用户的商铺购物车
	 * 
	 * @param context
	 * @param userid
	 */
	public static List<ShopGoodBean> getShopCart(Context context, int userid,
			int shop_id) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getReadableDatabase();

		List<ShopGoodBean> allSockBean = new ArrayList<ShopGoodBean>();

		Cursor cursor = mDb.query(ShopCart, new String[] { "goodsid",
				"shop_id", "user_id", "goods_name", "goods_num", "goods_price",
				"goods_detail", "goods_log1", "goods_log2", "goods_log3",
				"goods_status", "count", "goods_cate_id" },
				"  userid=? and shop_id=?  ", new String[] { userid + "",
						shop_id + "" }, null, null, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("goodsid"));
			int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
			int goods_cate_id = cursor.getInt(cursor
					.getColumnIndex("goods_cate_id"));
			int goods_num = cursor.getInt(cursor.getColumnIndex("goods_num"));
			String goods_name = cursor.getString(cursor
					.getColumnIndex("goods_name"));
			String goods_pricestr = cursor.getString(cursor
					.getColumnIndex("goods_price"));
			String goods_detail = cursor.getString(cursor
					.getColumnIndex("goods_detail"));
			String goods_log1 = cursor.getString(cursor
					.getColumnIndex("goods_log1"));
			String goods_log2 = cursor.getString(cursor
					.getColumnIndex("goods_log2"));
			String goods_log3 = cursor.getString(cursor
					.getColumnIndex("goods_log3"));

			Log.v(TAG, "goods_cate_id  " + goods_cate_id);

			int goods_status = cursor.getInt(cursor
					.getColumnIndex("goods_status"));
			int count = cursor.getInt(cursor.getColumnIndex("count"));

			float goods_price = Float.parseFloat(goods_pricestr);

			ShopGoodBean bean = new ShopGoodBean(id, shop_id, user_id,
					goods_name, goods_num, goods_price, goods_detail,
					goods_log1, goods_log2, goods_log3, goods_status);
			bean.setGoods_cate_id(goods_cate_id);

			if (count > 0) {
				bean.setCount(count);
				allSockBean.add(bean);
			}
		}
		cursor.close();
		return allSockBean;
	}

	/**
	 * 删除当前用户的商铺购物车
	 * 
	 * @param context
	 * @param userid
	 */
	public static void deleteShopCart(Context context, int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		mDb.delete(ShopCart, "  userid=? ", new String[] { userid + "" });
	}

	/**
	 * 删除当前用户的商铺购物车某个商品
	 * 
	 * @param context
	 * @param userid
	 */
	public static void deleteShopCart(Context context, int userid, int goodsid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		mDb.delete(ShopCart, "  userid=?  and goodsid=? ", new String[] {
				userid + "", goodsid + "" });
	}

	/**
	 * 
	 * 新增商铺商品购物车
	 * 
	 * @param context
	 * @param province
	 */

	public static void insertShopCart(Context context, ShopGoodBean bean,
			int userid) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("userid", userid);
		values.put("goodsid", bean.getId());
		values.put("shop_id", bean.getShop_id());
		values.put("user_id", bean.getUser_id());
		values.put("goods_name", bean.getGoods_name());
		values.put("goods_num", bean.getGoods_num());
		values.put("goods_price", bean.getGoods_price() + "");
		values.put("goods_detail", bean.getGoods_detail());
		values.put("goods_log1", bean.getGoods_log1());
		values.put("goods_log2", bean.getGoods_log2());
		values.put("goods_log3", bean.getGoods_log3());
		values.put("goods_status", bean.getGoods_status());
		values.put("goods_cate_id", bean.getGoods_cate_id());
		values.put("count", 1);
		mDb.insert(ShopCart, null, values);
	}

	/**
	 * 更新商铺购物车数量
	 * 
	 * @param context
	 * @param bean
	 * @param userid
	 */
	public static void updateShopCartCount(Context context, ShopGoodBean bean) {
		mDbHelper = DatabaseHelper.getInstance(context);
		mDb = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("count", bean.getCount());
		mDb.update(ShopCart, values, "  goodsid=? and  shop_id=?  ",
				new String[] { bean.getId() + "", bean.getShop_id() + "" });
	}

}
