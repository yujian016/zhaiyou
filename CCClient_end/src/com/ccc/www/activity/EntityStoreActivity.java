package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.ShopGoodAdapter;
import com.ccc.www.bean.ShopBean;
import com.ccc.www.bean.ShopGoodBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.viewbadger.BadgeView;

public class EntityStoreActivity extends BaseActivity {

	String TAG = "EntityStoreActivity";

	private ImageButton goBackBtn;

	GridView gv_store_goods;
	TextView tv_store_name;
	ImageView iv_store_log;

	TextView tv_store_desc;
	TextView tv_store_sellnum;
	TextView tv_store_goodcomment;
	TextView tv_store_badcomment;

	ShopBean shopBean;

	List<ShopGoodBean> allShopGoodBean = new ArrayList<ShopGoodBean>();

	ImageView iv_shopcartcount;

	BadgeView badge;

	int userid;
	int shopid;

	UpdateShopCartCount updateShopCartCount = new UpdateShopCartCount();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		registerReceiver(updateShopCartCount, new IntentFilter(
				GlobalConstant.UpdateShopCart));

		userid = UserUtil.getuserid(this);

		shopid = getIntent().getIntExtra("shopid", 0);

		setContentView(R.layout.activity_entity_store);
		initview();

		badge = new BadgeView(this, iv_shopcartcount);

		DBUtil.deleteShopCart(this, userid);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_shopcartcount:

			int userid = UserUtil.getuserid(EntityStoreActivity.this);
			if (userid > 0) {
				if (shopBean != null) {
					Intent intent = new Intent();
					intent.putExtra("shopid", shopBean.getId());
					intent.setClass(EntityStoreActivity.this,
							ShopCartActivity.class);
					startActivity(intent);
				}
			} else {
				showToast("请先登录");
				Intent login = new Intent();
				login.setClass(EntityStoreActivity.this, ShopCartActivity.class);
				startActivity(login);
			}

			break;
		case R.id.ib_store_goback:
			EntityStoreActivity.this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void findviewWithId() {
		goBackBtn = (ImageButton) findViewById(R.id.ib_store_goback);
		gv_store_goods = (GridView) findViewById(R.id.gv_store_goods);

		tv_store_name = (TextView) findViewById(R.id.tv_store_name);
		iv_store_log = (ImageView) findViewById(R.id.iv_store_log);

		tv_store_desc = (TextView) findViewById(R.id.tv_store_desc);
		tv_store_sellnum = (TextView) findViewById(R.id.tv_store_sellnum);
		tv_store_goodcomment = (TextView) findViewById(R.id.tv_store_goodcomment);
		tv_store_badcomment = (TextView) findViewById(R.id.tv_store_badcomment);

		iv_shopcartcount = (ImageView) findViewById(R.id.iv_shopcartcount);
	}

	@Override
	public void initListener() {
		goBackBtn.setOnClickListener(this);
		gv_store_goods.setOnItemClickListener(this);
		iv_shopcartcount.setOnClickListener(this);
	}

	private void initshopinfo() {
		// TODO Auto-generated method stub
		Log.v(TAG, "shopBean  " + shopBean);

		tv_store_name.setText(shopBean.getShop_name());

		String shoplog = shopBean.getShop_log();
		if (!shoplog.startsWith("http")) {
			shoplog = GlobalConstant.SERVER_URL + shoplog;
		}

		ImageLoader.getInstance().displayImage(shoplog, iv_store_log,
				ImageLoaderOption.getoption());

		int Good_comment_num = shopBean.getGood_comment_num();
		int Bad_comment_num = shopBean.getBad_comment_num();
		// tv_store_sellnum.setText("月销量:" + shopBean.getSell_num() + "");
		tv_store_desc.setText("店铺经营：" + shopBean.getShop_info());

		if (Good_comment_num > 0 || Bad_comment_num > 0) {
			float goodcom = (Good_comment_num * 1.00f)
					/ (Bad_comment_num * 1.00f + Good_comment_num * 1.00f)
					* 100;
			float badcom = (Bad_comment_num * 1.00f)
					/ (Bad_comment_num * 1.00f + Good_comment_num * 1.00f)
					* 100;
			tv_store_goodcomment.setText("好评" + (int) goodcom + "%");
			tv_store_badcomment.setText("差评" + (int) badcom + "%");
		} else {
			tv_store_goodcomment.setText("好评" + (int) 100 + "%");
			tv_store_badcomment.setText("差评" + (int) 0 + "%");
		}
		getSalerPhone(shopid);

	}

	private void getSalerPhone(int shopid) {
		// TODO Auto-generated method stub
		// 掉接口
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("shop_id", String.valueOf(shopid));

		loadData(HttpMethod.POST, GlobalConstant.GET_SALE_PHONE, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub

						dismissProgressDialog();

						String returnstr2 = arg0.result;

						Log.v(TAG, "onSuccess   " + returnstr2);

						try {
							JSONObject json = new JSONObject(returnstr2);
							final String telephone = json
									.getString("telephone");
							tv_store_sellnum.setText("联系电话：" + telephone);
							// tv_store_sellnum
							// .setOnClickListener(new OnClickListener() {
							// @Override
							// public void onClick(View arg0) {
							// if (!TextUtils.isEmpty(telephone)) {
							// // intent启动拨打电话
							// Intent intent = new Intent(
							// Intent.ACTION_CALL,
							// Uri.parse("tel:"
							// + telephone));
							// startActivity(intent);
							// }
							// }
							// });
						} catch (JSONException e) {
							showToast("加载失败");
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						dismissProgressDialog();
						Log.v(TAG, "onFailure " + arg1);

						showToast("加载失败");

					}
				});
	}

	@Override
	public void initdata() {

		/*
		 * GoodsBean goods1=new
		 * GoodsBean(R.drawable.x1,"球鞋球鞋球鞋","耐克精品",123.98d,12); GoodsBean
		 * goods2=new GoodsBean(R.drawable.x1,"上衣","冬暖夏凉",63d,2); GoodsBean
		 * goods3=new GoodsBean(R.drawable.x2,"裤子","精品韩式",18d,24); GoodsBean
		 * goods4=new GoodsBean(R.drawable.x1,"球鞋","耐克精品",534d,5); GoodsBean
		 * goods5=new GoodsBean(R.drawable.x2,"裤子球鞋球鞋","精品韩式",198d,12);
		 * GoodsBean goods6=new GoodsBean(R.drawable.x1,"上衣","冬暖夏凉",98d,5);
		 * GoodsBean goods7=new GoodsBean(R.drawable.x2,"上衣","冬暖夏凉",98d,12);
		 * GoodsBean goods8=new GoodsBean(R.drawable.x2,"球鞋","耐克精品",23.98d,2);
		 * GoodsBean goods9=new
		 * GoodsBean(R.drawable.x1,"上衣球鞋球鞋","冬暖夏凉",123.98d,12);
		 * 
		 * ArrayList<GoodsBean> goods=new ArrayList<GoodsBean>();
		 * goods.add(goods1); goods.add(goods2); goods.add(goods3);
		 * goods.add(goods4); goods.add(goods5); goods.add(goods6);
		 * goods.add(goods7); goods.add(goods8); goods.add(goods9);
		 * gvEntityGoods.setAdapter(new
		 * EntityStoreGoodsAdapter(this,goods,R.layout
		 * .entity_store_goods_gv_item));
		 */

		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("shop_id", shopid + "");

		loadData(HttpMethod.POST, GlobalConstant.GET_SHOP_GOODS, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {

						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("请求失败");
						} else {
							try {

								if (returnstr.contains("}-[")) {
									int position = returnstr.indexOf("}-[");
									String shopinfostr = returnstr.substring(0,
											position + 1);
									String goodsstr = returnstr.substring(
											position + 2, returnstr.length());

									JSONObject shopinfo = new JSONObject(
											shopinfostr);
									String shop_name = shopinfo
											.getString("shop_name");
									String shop_info = shopinfo
											.getString("shop_info");
									int shop_category_id_int = shopinfo
											.getInt("shop_category_id");
									int have_goods = shopinfo
											.getInt("have_goods");
									int sell_num = shopinfo.getInt("sell_num");
									int good_comment_num = shopinfo
											.getInt("good_comment_num");
									int bad_comment_num = shopinfo
											.getInt("bad_comment_num");
									int shop_categoryint = shopinfo
											.getInt("shop_category");

									int shopid = shopinfo.getInt("id");
									String shop_category_idshop = shop_category_id_int
											+ "";
									int user_idshop = shopinfo
											.getInt("user_id");
									String shop_log = shopinfo
											.getString("shop_log");
									String shop_status = shopinfo
											.getInt("shop_status") + "";

									String shop_category = shop_categoryint
											+ "";

									shopBean = new ShopBean(shopid, shop_name,
											shop_info, shop_category_idshop,
											user_idshop, shop_log, shop_status,
											have_goods, sell_num,
											good_comment_num, bad_comment_num,
											shop_category);

									initshopinfo();

									List<ShopGoodBean> tempallShopGoodBean = new ArrayList<ShopGoodBean>();

									JSONArray array = new JSONArray(goodsstr);
									for (int i = 0; i < array.length(); i++) {

										JSONObject json = array
												.getJSONObject(i);

										int id = json.getInt("id");
										int shop_id = json.getInt("shop_id");
										int user_id = json.getInt("user_id");
										String goods_name = json
												.getString("goods_name");
										int goods_num = json
												.getInt("goods_num");
										float goods_price = (float) json
												.getDouble("goods_price");
										String goods_detail = json
												.getString("goods_detail");
										String goods_log1 = json
												.getString("goods_log1");
										String goods_log2 = json
												.getString("goods_log2");
										String goods_log3 = json
												.getString("goods_log3");
										int goods_status = json
												.getInt("goods_status");
										int shop_category_id = json
												.getInt("shop_category_id");

										ShopGoodBean bean = new ShopGoodBean(
												id, shop_id, user_id,
												goods_name, goods_num,
												goods_price, goods_detail,
												goods_log1, goods_log2,
												goods_log3, goods_status);
										bean.setGoods_cate_id(shop_category_id);
										tempallShopGoodBean.add(bean);

										Log.v(TAG, "shop_category_id  "
												+ shop_category_id);

									}

									allShopGoodBean = tempallShopGoodBean;

									gv_store_goods
											.setAdapter(new ShopGoodAdapter(
													EntityStoreActivity.this,
													allShopGoodBean));
								}
							} catch (JSONException e) {
								showToast("加载失败");
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}
				});

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	/**
	 * 更新购物车数目
	 */
	private void updateCartCount() {
		// 取出数据库中count>0的货物
		List<ShopGoodBean> allSockBean = DBUtil.getShopCart(this, userid,
				shopBean.getId());

		int allcount = 0;
		for (int i = 0; i < allSockBean.size(); i++) {
			int count = allSockBean.get(i).getCount();
			allcount = allcount + count;
		}

		badge.setText("" + allcount);
		badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badge.setTextSize(10);
		if (allcount > 0) {
			badge.show();
		} else {
			badge.hide();
		}
	}

	/**
	 * 更新商铺购物车
	 * 
	 * @author Administrator
	 * 
	 */
	class UpdateShopCartCount extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			updateCartCount();
		}
	}

	@Override
	protected void onDestroy() {

		unregisterReceiver(updateShopCartCount);

		super.onDestroy();
	}

}
