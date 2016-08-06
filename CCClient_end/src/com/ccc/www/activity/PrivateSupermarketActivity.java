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
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.AdImagePagerAdapter;
import com.ccc.www.adapter.PrivateSuperMarketIndicatorAdapter;
import com.ccc.www.bean.AdBean;
import com.ccc.www.bean.PrivateSuperMarketGoodsBean;
import com.ccc.www.bean.SupMarketCategoryBean;
import com.ccc.www.db.DBUtil;
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
import com.readystatesoftware.viewbadger.BadgeView;
import com.viewpagerindicator.TabPageIndicator;

/**
 * 私人超市
 * 
 * @author Administrator
 * 
 */
public class PrivateSupermarketActivity extends BaseActivity {

	private TabPageIndicator activity_supmarket_indicator;

	private Gson gson = new Gson();
	private String TAG = "PrivateSupermarketActivity";

	private ArrayList<AdBean> adImages = new ArrayList<AdBean>();
	private ViewPager vp_supmarket_ad;
	private ViewPager activity_supmarket_viewpager;
	private LinearLayout ad_supmarket_dot_layout;
	private ImageButton ib_supmarket_goback;

	int proxyshopid = 0;

	List<SupMarketCategoryBean> allSupMarketCategoryBean = new ArrayList<SupMarketCategoryBean>();

	ImageView iv_privatesupermarketcartcount;

	int userid;

	UpdatePrivateSuperMarketCartCount updatePrivateSuperMarketCartCount = new UpdatePrivateSuperMarketCartCount();
	PaySuccessToMyOrder paySuccessToMyOrder = new PaySuccessToMyOrder();

	BadgeView badge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		proxyshopid = getIntent().getIntExtra("proxyshopid", 0);

		userid = UserUtil.getuserid(this);

		registerReceiver(updatePrivateSuperMarketCartCount, new IntentFilter(
				GlobalConstant.UpdatePrivateSuperMarketCartCount));
		registerReceiver(paySuccessToMyOrder, new IntentFilter(
				GlobalConstant.PaySuccessToMyOrder));

		setContentView(R.layout.activity_privatesupermarket);
		initview();

		badge = new BadgeView(PrivateSupermarketActivity.this,
				iv_privatesupermarketcartcount);

		DBUtil.deletePrivateSupermarketCart(this, userid);
	}

	/**
	 * 支付成功后关闭页面
	 * 
	 * @author Administrator
	 * 
	 */
	class PaySuccessToMyOrder extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			PrivateSupermarketActivity.this.finish();
		}
	}

	/**
	 * 更新私人超市购物车
	 * 
	 * @author Administrator
	 * 
	 */
	class UpdatePrivateSuperMarketCartCount extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			updateCartCount();
		}
	}

	/**
	 * 更新购物车数目
	 */
	private void updateCartCount() {
		// 取出数据库中count>0的货物
		List<PrivateSuperMarketGoodsBean> allSockBean = DBUtil
				.getPrivateSupermarketCart(PrivateSupermarketActivity.this,
						userid);

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

	@Override
	protected void onDestroy() {
		unregisterReceiver(updatePrivateSuperMarketCartCount);
		unregisterReceiver(paySuccessToMyOrder);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_supmarket_goback:
			PrivateSupermarketActivity.this.finish();
			break;
		case R.id.iv_privatesupermarketcartcount:

			int userid = UserUtil.getuserid(this);
			if (userid > 0) {

				int psm_startmoney = UserUtil.getpsm_startmoney(this);

				double allmoney = 0.0f;
				List<PrivateSuperMarketGoodsBean> allsock = DBUtil
						.getPrivateSupermarketCart(this, userid);
				for (int i = 0; i < allsock.size(); i++) {
					allsock.get(i).setCheck(true);
					double money = allsock.get(i).getCount()
							* allsock.get(i).getGoods_price();
					allmoney = allmoney + money;
				}

				if (allmoney < psm_startmoney) {
					showToast("必须达到起送价" + psm_startmoney + "元才可下单");
					return;
				}

				Intent privatesupermarketcart = new Intent();
				int supermaket_id = UserUtil.getsupermaket_id(this);
				privatesupermarketcart.putExtra("supermaket_id", supermaket_id);
				privatesupermarketcart.setClass(
						PrivateSupermarketActivity.this,
						PrivateSupermarketCartActivity.class);
				startActivity(privatesupermarketcart);
			} else {
				showToast("请先登录");
				Intent login = new Intent();
				login.setClass(PrivateSupermarketActivity.this,
						LoginActivity.class);
				startActivity(login);
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void findviewWithId() {
		activity_supmarket_indicator = (TabPageIndicator) findViewById(R.id.activity_supmarket_indicator);
		vp_supmarket_ad = (ViewPager) findViewById(R.id.vp_supmarket_ad);
		activity_supmarket_viewpager = (ViewPager) findViewById(R.id.activity_supmarket_viewpager);
		ad_supmarket_dot_layout = (LinearLayout) findViewById(R.id.ad_supmarket_dot_layout);
		ib_supmarket_goback = (ImageButton) findViewById(R.id.ib_supmarket_goback);
		iv_privatesupermarketcartcount = (ImageView) findViewById(R.id.iv_privatesupermarketcartcount);
	}

	@Override
	public void initListener() {
		ib_supmarket_goback.setOnClickListener(this);
		iv_privatesupermarketcartcount.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		initAd();
		getSupMarketCategory();
		Log.v(TAG, proxyshopid + "");
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
						int currentPageIndex = vp_supmarket_ad.getCurrentItem();
						if (currentPageIndex == adImages.size() - 1) {
							vp_supmarket_ad.setCurrentItem(0);
						} else {
							currentPageIndex++;
							vp_supmarket_ad.setCurrentItem(currentPageIndex);
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
		params.addBodyParameter("ad_type", "2");
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
							vp_supmarket_ad.setAdapter(new AdImagePagerAdapter(
									adImages, PrivateSupermarketActivity.this));
							vp_supmarket_ad.setCurrentItem(0);
							initDot();
							updateDot();
						}
					}
				});
	}

	private void initDot() {
		for (int i = 0; i < adImages.size(); i++) {
			View dot_view = new View(PrivateSupermarketActivity.this);
			LayoutParams params = new LayoutParams(10, 10);
			params.leftMargin = 5;
			params.rightMargin = 5;
			dot_view.setLayoutParams(params);
			dot_view.setBackgroundResource(R.drawable.selector_dot);
			ad_supmarket_dot_layout.addView(dot_view);
		}
	}

	private void updateDot() {
		int currentPageIndex = vp_supmarket_ad.getCurrentItem();
		for (int i = 0; i < ad_supmarket_dot_layout.getChildCount(); i++) {
			if (i == currentPageIndex) {
				ad_supmarket_dot_layout.getChildAt(i).setEnabled(true);
			} else {
				ad_supmarket_dot_layout.getChildAt(i).setEnabled(false);
			}
		}
	}

	// 获取超市物品分类数据
	private void getSupMarketCategory() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("数据加载中");
		loadData(HttpMethod.GET, GlobalConstant.SUP_ACTION_GET_GOODS_CATEGORY,
				null, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("服务器繁忙,稍后再试");
					}

					@Override
					public void onSuccess(ResponseInfo<String> response) {
						dismissProgressDialog();

						String returnstr = response.result;
						Log.v(TAG, response.result);

						if (TextUtils.isEmpty(returnstr)) {
							showToast("加载失败");
						} else {
							try {
								JSONArray json = new JSONArray(returnstr);
								for (int i = 0; i < json.length(); i++) {
									JSONObject temp = json.getJSONObject(i);
									int id = temp.getInt("id");
									String sock_category_name = temp
											.getString("sock_category_name");

									SupMarketCategoryBean bean = new SupMarketCategoryBean(
											id, sock_category_name);
									allSupMarketCategoryBean.add(bean);
								}

								if (allSupMarketCategoryBean.size() > 0) {

									PrivateSuperMarketIndicatorAdapter adapter = new PrivateSuperMarketIndicatorAdapter(
											getSupportFragmentManager(),
											allSupMarketCategoryBean,
											proxyshopid);

									activity_supmarket_viewpager
											.setAdapter(adapter);

									activity_supmarket_indicator
											.setViewPager(activity_supmarket_viewpager);

									activity_supmarket_indicator
											.setVisibility(View.VISIBLE);

									// 如果我们要对ViewPager设置监听，用indicator设置就行了
									activity_supmarket_indicator
											.setOnPageChangeListener(new OnPageChangeListener() {

												@Override
												public void onPageSelected(
														int arg0) {

												}

												@Override
												public void onPageScrolled(
														int arg0, float arg1,
														int arg2) {

												}

												@Override
												public void onPageScrollStateChanged(
														int arg0) {

												}
											});
								} else {
									showToast("无商品分类");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

}
