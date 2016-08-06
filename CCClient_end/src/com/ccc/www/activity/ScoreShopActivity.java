package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.AdImagePagerAdapter;
import com.ccc.www.adapter.ScoreShopAdapter;
import com.ccc.www.alipay.AlipayUtil;
import com.ccc.www.bean.AdBean;
import com.ccc.www.bean.ScoreMarketGoodsBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.ccc.www.util.Xutils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ScoreShopActivity extends BaseActivity {

	String TAG = "ScoreShopActivity";

	private ImageButton ibGoback;
	private GridView gvGoods;

	LinearLayout ad_score_shop_dot_layout;
	ViewPager vp_score_shop_ad;

	View rootview;
	String businessdesc = "";
	String businessname = "";
	String get_goods_person_name = "";
	String get_goods_person_phone = "";
	String get_goods_person_address = "";

	int userid;

	private Gson gson = new Gson();
	private ArrayList<AdBean> adImages = new ArrayList<AdBean>();

	List<ScoreMarketGoodsBean> allScoreMarketGoodsBean = new ArrayList<ScoreMarketGoodsBean>();

	ExchangePoint exchangePoint = new ExchangePoint();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		userid = UserUtil.getuserid(this);

		registerReceiver(exchangePoint, new IntentFilter(
				GlobalConstant.ExchangePoint));

		setContentView(R.layout.activity_score_shop);

		rootview = LayoutInflater.from(this).inflate(
				R.layout.activity_score_shop, null);

		initview();
	}

	@Override
	public void findviewWithId() {
		ibGoback = (ImageButton) findViewById(R.id.ib_score_goback);
		gvGoods = (GridView) findViewById(R.id.gv_score_goods);
		ad_score_shop_dot_layout = (LinearLayout) findViewById(R.id.ad_score_shop_dot_layout);
		vp_score_shop_ad = (ViewPager) findViewById(R.id.vp_score_shop_ad);
	}

	@Override
	public void initListener() {
		ibGoback.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_score_goback:
			ScoreShopActivity.this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void initdata() {
		initAd();

		loaddata();

	}

	/**
	 * 加载积分商城商品
	 */
	private void loaddata() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		loadData(HttpMethod.POST, GlobalConstant.GET_INTEGRAL_GOODS, null,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String json = info.result;
						Log.v(TAG, "积分商品   " + json);
						if (BaseUtils.isEmpty(json)) {
							showToast("加载失败");
						} else {
							try {

								List<ScoreMarketGoodsBean> tempallScoreMarketGoodsBean = new ArrayList<ScoreMarketGoodsBean>();

								JSONArray array = new JSONArray(json);
								for (int i = 0; i < array.length(); i++) {

									JSONObject obj = array.getJSONObject(i);

									int id = obj.getInt("id");
									String goods_title = obj
											.getString("goods_title");
									int need_integral = obj
											.getInt("need_integral");
									int goods_num = obj.getInt("goods_num");
									String log = obj.getString("log");

									if (goods_num > 0) {
										ScoreMarketGoodsBean bean = new ScoreMarketGoodsBean(
												id, goods_title, need_integral,
												goods_num, log);
										tempallScoreMarketGoodsBean.add(bean);
									}
									gvGoods.setAdapter(new ScoreShopAdapter(
											ScoreShopActivity.this,
											tempallScoreMarketGoodsBean));
								}

							} catch (JSONException e) {
								showToast("加载失败");
								e.printStackTrace();
							}

						}
					}
				});
	}

	/**
	 * 初始化广告幻灯片
	 */
	private void initAd() {
		getRepairAdData();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						int currentPageIndex = vp_score_shop_ad
								.getCurrentItem();
						if (currentPageIndex == adImages.size() - 1) {
							vp_score_shop_ad.setCurrentItem(0);
						} else {
							currentPageIndex++;
							vp_score_shop_ad.setCurrentItem(currentPageIndex);
						}
						updateDot();
					}
				});
			}
		}, 4000, 4000);
	}

	/**
	 * 获取广告图片
	 */
	private void getRepairAdData() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("ad_type", "10");
		loadData(HttpMethod.POST, GlobalConstant.GET_AD, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String json = info.result;
						Log.v(TAG, json);
						if (BaseUtils.isEmpty(json)) {
							showToast("加载失败");
						} else {
							adImages = gson.fromJson(json,
									new TypeToken<ArrayList<AdBean>>() {
									}.getType());
							vp_score_shop_ad
									.setAdapter(new AdImagePagerAdapter(
											adImages, ScoreShopActivity.this));
							vp_score_shop_ad.setCurrentItem(0);
							initDot();
							updateDot();
						}
					}
				});
	}

	private void initDot() {
		for (int i = 0; i < adImages.size(); i++) {
			View dot_view = new View(ScoreShopActivity.this);
			LayoutParams params = new LayoutParams(10, 10);
			params.leftMargin = 5;
			params.rightMargin = 5;
			dot_view.setLayoutParams(params);
			dot_view.setBackgroundResource(R.drawable.selector_dot);
			ad_score_shop_dot_layout.addView(dot_view);
		}
	}

	private void updateDot() {
		int currentPageIndex = vp_score_shop_ad.getCurrentItem();
		for (int i = 0; i < ad_score_shop_dot_layout.getChildCount(); i++) {
			if (i == currentPageIndex) {
				ad_score_shop_dot_layout.getChildAt(i).setEnabled(true);
			} else {
				ad_score_shop_dot_layout.getChildAt(i).setEnabled(false);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	class ExchangePoint extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent data) {
			if (data != null) {
				int userid = UserUtil.getuserid(ScoreShopActivity.this);
				if (userid <= 0) {
					Intent intent = new Intent();
					intent.setClass(ScoreShopActivity.this, LoginActivity.class);
					startActivity(intent);
					showToast("请先登录");
					return;
				}

				ScoreMarketGoodsBean s = (ScoreMarketGoodsBean) data
						.getSerializableExtra("info");
				int Need_integral = s.getNeed_integral();
				getmypoint(Need_integral, s);
			}
		}
	}

	private void getmypoint(final int Need_integral,
			final ScoreMarketGoodsBean s) {
		if (!BaseUtils.isNetWork(ScoreShopActivity.this)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在获取我的积分");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id",
				String.valueOf(UserUtil.getuserid(ScoreShopActivity.this)));
		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.USER_ACTION_GET_USER_SCORE_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("获取失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> result) {
						dismissProgressDialog();
						String resultJson = result.result;
						try {
							JSONObject json = new JSONObject(resultJson);
							int score_num = json.getInt("score_num");
							if (score_num >= Need_integral) {
								payinfoConfirm(s);
							} else {
								showToast("您的积分为" + score_num + ",兑换需要"
										+ Need_integral + ",积分不足无法兑换");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

	}

	private void point(ScoreMarketGoodsBean s) {
		if (!BaseUtils.isNetWork(ScoreShopActivity.this)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在兑换");

		JsonArray jsonarr = new JsonArray();

		JsonObject jsonobj = new JsonObject();
		jsonobj.addProperty("goods_id", s.getId());
		jsonobj.addProperty("goods_number", 1);
		jsonarr.add(jsonobj);

		String integral_order = jsonarr.toString();

		String rand_no = AlipayUtil.getRandomString();

		JsonObject shop_buy_user_infoobj = new JsonObject();
		shop_buy_user_infoobj
				.addProperty("buy_user_id", String.valueOf(userid));
		shop_buy_user_infoobj.addProperty("get_goods_person_name",
				get_goods_person_name);
		shop_buy_user_infoobj.addProperty("get_goods_person_phone",
				get_goods_person_phone);
		shop_buy_user_infoobj.addProperty("get_goods_person_address",
				get_goods_person_address);
		shop_buy_user_infoobj.addProperty("order_sum_integtal",
				String.valueOf(s.getNeed_integral()));
		shop_buy_user_infoobj.addProperty("rand_no", rand_no);

		String integral_buy_user_info = shop_buy_user_infoobj.toString();

		RequestParams params = new RequestParams();
		params.addBodyParameter("integral_buy_user_info",
				integral_buy_user_info);
		params.addBodyParameter("integral_order", integral_order);
		Xutils.loadData(HttpMethod.POST, GlobalConstant.ADD_INTEGRAL_ORDER,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("兑换失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {

						dismissProgressDialog();
						String json = info.result;
						Log.v(TAG, json);
						if (BaseUtils.isEmpty(json)) {
							showToast("兑换失败");
						} else {
							try {
								JSONObject jsonob = new JSONObject(json);
								String resultMsg = jsonob
										.getString("resultMsg");
								showToast(resultMsg);

								dissmisspopwindow();

								loaddata();

							} catch (JSONException e) {
								showToast("兑换失败");
								e.printStackTrace();
							}

						}
					}
				});
	}

	PopupWindow mPopupWindow;

	private void dissmisspopwindow() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

	/**
	 * 支付信息确认
	 * 
	 * @param KaizenId
	 */
	private void payinfoConfirm(final ScoreMarketGoodsBean s) {
		String schoolname = UserUtil.getschoolname(ScoreShopActivity.this);
		String hostelname = UserUtil.gethostelname(ScoreShopActivity.this);
		String floorname = UserUtil.getfloorname(ScoreShopActivity.this);

		String useraddr = schoolname + hostelname + floorname;

		View view1 = LayoutInflater.from(ScoreShopActivity.this).inflate(
				R.layout.pop_exchangepointtip, null);
		mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);

		final EditText pop_proxystocktip_name = (EditText) view1
				.findViewById(R.id.pop_proxystocktip_name);
		final EditText pop_proxystocktip_phone = (EditText) view1
				.findViewById(R.id.pop_proxystocktip_phone);
		final EditText pop_proxystocktip_addr = (EditText) view1
				.findViewById(R.id.pop_proxystocktip_addr);
		TextView pop_proxystocktip_money = (TextView) view1
				.findViewById(R.id.pop_proxystocktip_money);
		TextView pop_proxystocktip_cancel = (TextView) view1
				.findViewById(R.id.pop_proxystocktip_cancel);
		TextView pop_proxystocktip_pay = (TextView) view1
				.findViewById(R.id.pop_proxystocktip_pay);

		pop_proxystocktip_addr.setText(useraddr);

		pop_proxystocktip_money.setText(s.getNeed_integral() + "");

		pop_proxystocktip_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dissmisspopwindow();
			}
		});
		pop_proxystocktip_pay.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				get_goods_person_name = pop_proxystocktip_name.getText()
						.toString().trim();
				get_goods_person_phone = pop_proxystocktip_phone.getText()
						.toString().trim();
				get_goods_person_address = pop_proxystocktip_addr.getText()
						.toString().trim();

				if (TextUtils.isEmpty(get_goods_person_name)) {
					showToast("请输入收货人姓名");
					pop_proxystocktip_name.requestFocus();
					return;
				}

				if (TextUtils.isEmpty(get_goods_person_phone)) {
					showToast("请输入收货人联系电话");
					pop_proxystocktip_phone.requestFocus();
					return;
				}

				if (TextUtils.isEmpty(get_goods_person_address)) {
					showToast("请输入收货地址");
					pop_proxystocktip_addr.requestFocus();
					return;
				}

				businessname = "(积分兑换)" + s.getGoods_title();

				businessdesc = get_goods_person_name + "的兑换订单";

				point(s);

			}
		});
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
		mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(exchangePoint);
		super.onDestroy();
	}
}
