package com.ccc.www.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper mInstance = null;

	/** 数据库名字 **/
	public static final String DATABASE_NAME = "ccclient.db";

	/** 数据库版本 **/
	private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static synchronized DatabaseHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DatabaseHelper(context);
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String proxystockcartsql = "create table proxystockcart ( id INTEGER PRIMARY KEY AUTOINCREMENT,userid  int, sockid  int  , sock_name  varchar(50) , sock_price  varchar(50), sock_category_id  int, sock_category_name  varchar(50),sock_log  varchar(500), sock_status int,count  int  ) ";

		String proxystockordersql = "create table proxystockorder ( id INTEGER PRIMARY KEY AUTOINCREMENT,proxy_order  varchar(1500) , rand_no  varchar(100)  , proxy_user_id   int , get_goods_person_name  varchar(500), get_goods_person_phone   varchar(500), get_goods_person_address  varchar(500),order_sum_money  varchar(500), submitstatus int  ) ";

		// 潮流数码购物车
		String digitalcartsql = "create table digitalcart ( id INTEGER PRIMARY KEY AUTOINCREMENT,goodsid int , shop_id  int , user_id   int , goods_name  varchar(500), goods_num   int,digital_goods_category_id int,shop_category_id  int, goods_price  varchar(500),goods_detail  varchar(1500), goods_log1 varchar(1500),goods_log2 varchar(1500),goods_log3 varchar(1500),goods_status  int ,count int  ) ";
		
		// 校园购物购物车
		String schoolbuycartsql = "create table schoolbuycart ( id INTEGER PRIMARY KEY AUTOINCREMENT,goodsid int , shop_id  int , user_id   int , goods_name  varchar(500), goods_num   int,digital_goods_category_id int,shop_category_id  int, goods_price  varchar(500),goods_detail  varchar(1500), goods_log1 varchar(1500),goods_log2 varchar(1500),goods_log3 varchar(1500),goods_status  int ,count int  ) ";

		//私人超市购物车
		String privatesupermarketcartsql = "create table privatesupermarketcart ( id INTEGER PRIMARY KEY AUTOINCREMENT,userid  int, goodsid  int  , supermaket_id  int , goods_name  varchar(500), goods_price   varchar(50), goods_category_id  int,goods_log  varchar(500), have_num int,goods_status  int,count  int  ) ";

		//二手购物车
		String secondarytradecartsql = "create table secondarytradecart ( id INTEGER PRIMARY KEY AUTOINCREMENT,userid  int, tradeid  int  , user_id  int , title  varchar(500), price   varchar(50), detail  varchar(500),log1  varchar(500), log2  varchar(500),log3   varchar(500),phone   varchar(500),status  int  ) ";

		
		//商铺购物车
		String shopcartsql = "create table shopcart ( id INTEGER PRIMARY KEY AUTOINCREMENT,userid  int, goodsid  int  ,goods_cate_id  int, shop_id  int ,user_id int, goods_name  varchar(500),goods_num  int, goods_price   varchar(50), goods_detail    varchar(500),goods_log1  varchar(500),goods_log2  varchar(500),goods_log3  varchar(500), goods_status  int,count  int  ) ";

		
		db.execSQL(proxystockcartsql);
		db.execSQL(proxystockordersql);
		db.execSQL(digitalcartsql);
		db.execSQL(schoolbuycartsql);
		db.execSQL(privatesupermarketcartsql);
		db.execSQL(secondarytradecartsql);
		db.execSQL(shopcartsql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// String relusersitesql =
		// "create table   IF NOT EXISTS  relusersite  ( id INTEGER PRIMARY KEY AUTOINCREMENT, siteid  int ,userid  int  ) ";
		// String jpushmsgsql =
		// "create table   IF NOT EXISTS  jpushmsg  ( id INTEGER PRIMARY KEY AUTOINCREMENT,myuid  int, title   varchar(1000) ,contenttype  varchar(1000),msgcontent varchar(1000),taskid int,time BIGINT ) ";
		// db.execSQL(relusersitesql);
		// db.execSQL(jpushmsgsql);
	}

}
