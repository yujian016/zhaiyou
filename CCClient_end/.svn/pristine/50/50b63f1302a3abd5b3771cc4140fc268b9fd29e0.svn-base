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
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.AdImagePagerAdapter;
import com.ccc.www.adapter.CouponAdapter;
import com.ccc.www.bean.AdBean;
import com.ccc.www.bean.CouponBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class CouponActivity extends BaseActivity {

	String TAG = "CouponActivity";

	private ImageButton ibGoback;

	TextView activity_coupan_type1;
	TextView activity_coupan_type2;
	ListView coupan_listview;

	LinearLayout ad_coupan_dot_layout;
	ViewPager vp_coupan_ad;

	List<CouponBean> allCouponBean = new ArrayList<CouponBean>();

	private Gson gson = new Gson();
	private ArrayList<AdBean> adImages = new ArrayList<AdBean>();

	int coupon_type = 1;

	int user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		user_id = UserUtil.getuserid(this);

		registerReceiver(getCoupon,
				new IntentFilter(GlobalConstant.GetMyCoupon));

		setContentView(R.layout.activity_coupan);
		initview();
	}

	@Override
	public void findviewWithId() {
		ibGoback = (ImageButton) findViewById(R.id.ib_coupan_goback);

		activity_coupan_type1 = (TextView) findViewById(R.id.activity_coupan_type1);
		activity_coupan_type2 = (TextView) findViewById(R.id.activity_coupan_type2);
		coupan_listview = (ListView) findViewById(R.id.coupan_listview);

		ad_coupan_dot_layout = (LinearLayout) findViewById(R.id.ad_coupan_dot_layout);
		vp_coupan_ad = (ViewPager) findViewById(R.id.vp_coupan_ad);
	}

	@Override
	public void initListener() {
		ibGoback.setOnClickListener(this);
		activity_coupan_type1.setOnClickListener(this);
		activity_coupan_type2.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_coupan_goback:
			CouponActivity.this.finish();
			break;
		case R.id.activity_coupan_type1:
			coupon_type = 1;
			loaddata();
			break;
		case R.id.activity_coupan_type2:
			coupon_type = 2;
			loaddata();
			break;

		default:
			break;
		}
	}

	@Override
	public void initdata() {

		initAd();

		coupon_type = 1;

		loaddata();
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
						int currentPageIndex = vp_coupan_ad.getCurrentItem();
						if (currentPageIndex == adImages.size() - 1) {
							vp_coupan_ad.setCurrentItem(0);
						} else {
							currentPageIndex++;
							vp_coupan_ad.setCurrentItem(currentPageIndex);
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
		params.addBodyParameter("ad_type", "9");
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
							vp_coupan_ad.setAdapter(new AdImagePagerAdapter(
									adImages, CouponActivity.this));
							vp_coupan_ad.setCurrentItem(0);
							initDot();
							updateDot();
						}
					}
				});
	}

	private void initDot() {
		for (int i = 0; i < adImages.size(); i++) {
			View dot_view = new View(CouponActivity.this);
			LayoutParams params = new LayoutParams(10, 10);
			params.leftMargin = 5;
			params.rightMargin = 5;
			dot_view.setLayoutParams(params);
			dot_view.setBackgroundResource(R.drawable.selector_dot);
			ad_coupan_dot_layout.addView(dot_view);
		}
	}

	private void updateDot() {
		int currentPageIndex = vp_coupan_ad.getCurrentItem();
		for (int i = 0; i < ad_coupan_dot_layout.getChildCount(); i++) {
			if (i == currentPageIndex) {
				ad_coupan_dot_layout.getChildAt(i).setEnabled(true);
			} else {
				ad_coupan_dot_layout.getChildAt(i).setEnabled(false);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	private void loaddata() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("coupon_type", coupon_type + "");
		loadData(HttpMethod.POST, GlobalConstant.GET_COUPON, params,
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
							try {

								List<CouponBean> tempallCouponBean = new ArrayList<CouponBean>();

								JSONArray array = new JSONArray(json);
								for (int i = 0; i < array.length(); i++) {
									JSONObject obj = array.getJSONObject(i);

									int id = obj.getInt("id");
									int coupon_type = obj.getInt("coupon_type");
									float coupon_price = (float) obj
											.getDouble("coupon_price");
									int coupon_num = obj.getInt("coupon_num");
									int shop_id = obj.getInt("shop_id");
									String coupon_log = obj
											.getString("coupon_log");
									String detail = obj.getString("detail");
									int status = obj.getInt("status");

									if (!coupon_log.startsWith("http")) {
										coupon_log = GlobalConstant.SERVER_URL
												+ coupon_log;
									}

									CouponBean bean = new CouponBean(id,
											coupon_type, coupon_price,
											coupon_num, shop_id, coupon_log,
											detail, status);

									tempallCouponBean.add(bean);
								}

								allCouponBean = tempallCouponBean;

								coupan_listview.setAdapter(new CouponAdapter(
										CouponActivity.this, allCouponBean));

							} catch (JSONException e) {
								showToast("加载失败");
								e.printStackTrace();
							}

						}
					}
				});

	}

	GetCoupon getCoupon = new GetCoupon();

	class GetCoupon extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent data) {

			Log.v(TAG, "GetCoupon  ");

			if (data != null) {
				CouponBean bean = (CouponBean) data
						.getSerializableExtra("bean");

				checkhaveget(bean);

			}
		}
	}

	/**
	 * 验证是否领取
	 * 
	 * @param bean
	 */
	private void checkhaveget(final CouponBean bean) {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("coupon_id", bean.getId() + "");
		params.addBodyParameter("user_id", user_id + "");

		Log.v(TAG, "coupon_id  " + bean.getId());
		Log.v(TAG, "user_id  " + user_id);

		loadData(HttpMethod.POST, GlobalConstant.CHECK_COUPON_IS_GET, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Log.v(TAG, "onFailure " + arg1);
						dismissProgressDialog();
						showToast("加载失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String json = info.result;
						Log.v(TAG, "onSuccess " + json);
						if (BaseUtils.isEmpty(json)) {
							showToast("加载失败");
						} else {
							try {
								JSONObject jsonobj = new JSONObject(json);
								String code = jsonobj.getString("code");
								String msg = jsonobj.getString("msg");
								if (code.equalsIgnoreCase("0")) {
									getTheCoupon(bean);
								} else {
									showToast(msg);
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
	 * 领取
	 * 
	 * @param bean
	 */
	private void getTheCoupon(final CouponBean bean) {
		int userid = UserUtil.getuserid(this);
		if (userid <= 0) {
			Intent intent = new Intent();
			intent.setClass(CouponActivity.this, LoginActivity.class);
			startActivity(intent);

			showToast("请先登录");
			return;
		}

		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在领取优惠");

		RequestParams params = new RequestParams();
		params.addBodyParameter("coupon_id", bean.getId() + "");
		params.addBodyParameter("user_id", user_id + "");

		Log.v(TAG, "coupon_id  " + bean.getId());
		Log.v(TAG, "user_id  " + user_id);

		loadData(HttpMethod.POST, GlobalConstant.GET_MY_COUPON, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Log.v(TAG, "onFailure " + arg1);
						dismissProgressDialog();
						showToast("领取失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String json = info.result;
						Log.v(TAG, "onSuccess " + json);
						if (BaseUtils.isEmpty(json)) {
							showToast("领取失败");
						} else {
							try {
								JSONObject obj = new JSONObject(json);
								int resultCode = obj.getInt("resultCode");
								String resultMsg = obj.getString("resultMsg");
								showToast(resultMsg);

								if (resultCode == 1) {
									for (int i = 0; i < allCouponBean.size(); i++) {
										if (bean.getId() == allCouponBean
												.get(i).getId()) {
											int num = allCouponBean.get(i)
													.getCoupon_num();
											num--;

											if (num > 0) {
												allCouponBean.get(i)
														.setCoupon_num(num);
											} else {
												allCouponBean.remove(i);
											}
											coupan_listview
													.setAdapter(new CouponAdapter(
															CouponActivity.this,
															allCouponBean));
											break;
										}
									}
								}

							} catch (JSONException e) {
								showToast("领取失败");
								e.printStackTrace();
							}
						}
					}
				});
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(getCoupon);
		super.onDestroy();
	}
}
