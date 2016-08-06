package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.AdImagePagerAdapter;
import com.ccc.www.adapter.BuyGoodsIndicatorAdapter;
import com.ccc.www.bean.AdBean;
import com.ccc.www.bean.GoodsBean;
import com.ccc.www.bean.ShopCategoryBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.readystatesoftware.viewbadger.BadgeView;
import com.viewpagerindicator.TabPageIndicator;

public class SchoolBuyGoodsActivity extends BaseActivity {

	String TAG = "BuyGoodsActivity";

	private ImageButton ibGoback;
	private TabPageIndicator buy_goods_indicator;
	private ViewPager vp_buy_goods;

	private Gson gson = new Gson();
	private HttpUtils http = null;

	private ViewPager vp_buygoods_ad;
	private LinearLayout ad_buygoods_dot_layout;
	private ArrayList<AdBean> adImages = new ArrayList<AdBean>();

	ImageView iv_proxysockcartcount;
	BadgeView badge;

	UpdateSchoolBuyCartCount updateSchoolBuyCartCount = new UpdateSchoolBuyCartCount();

	/**
	 * 更新购物车数目广播
	 * 
	 * @author Administrator
	 * 
	 */
	class UpdateSchoolBuyCartCount extends BroadcastReceiver {
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

		List<GoodsBean> allgoods = DBUtil.getSchoolBuyCart(this);
		int allcount = 0;
		for (int i = 0; i < allgoods.size(); i++) {
			int count = allgoods.get(i).getCount();
			allcount = allcount + count;
		}

		Log.v(TAG, "allSockBean  " + allgoods.size());

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
		unregisterReceiver(updateSchoolBuyCartCount);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		registerReceiver(updateSchoolBuyCartCount, new IntentFilter(
				GlobalConstant.UpdateSchoolBuyCartCount));

		setContentView(R.layout.activity_schoolbuygoods);
		initview();

		badge = new BadgeView(SchoolBuyGoodsActivity.this, iv_proxysockcartcount);

		DBUtil.deleteSchoolBuyCart(this);
	}

	@Override
	public void findviewWithId() {
		ibGoback = (ImageButton) findViewById(R.id.ib_goback);
		buy_goods_indicator = (TabPageIndicator) findViewById(R.id.buy_goods_indicator);
		vp_buy_goods = (ViewPager) findViewById(R.id.vp_buy_goods);

		vp_buygoods_ad = (ViewPager) findViewById(R.id.vp_buygoods_ad);
		ad_buygoods_dot_layout = (LinearLayout) findViewById(R.id.ad_buygoods_dot_layout);

		iv_proxysockcartcount = (ImageView) findViewById(R.id.iv_proxysockcartcount);
	}

	@Override
	public void initListener() {
		ibGoback.setOnClickListener(this);
		iv_proxysockcartcount.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_goback:
			SchoolBuyGoodsActivity.this.finish();
			break;
		case R.id.iv_proxysockcartcount:

			Intent intent = new Intent();
			intent.setClass(SchoolBuyGoodsActivity.this, SchoolBuyCartActivity.class);
			startActivity(intent);

			break;
		default:
			break;
		}
	}

	@Override
	public void initdata() {
		getGoodsCategory();
		initAd();
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
						int currentPageIndex = vp_buygoods_ad.getCurrentItem();
						if (currentPageIndex == adImages.size() - 1) {
							vp_buygoods_ad.setCurrentItem(0);
						} else {
							currentPageIndex++;
							vp_buygoods_ad.setCurrentItem(currentPageIndex);
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
		params.addBodyParameter("ad_type", "4");
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
							vp_buygoods_ad.setAdapter(new AdImagePagerAdapter(
									adImages, SchoolBuyGoodsActivity.this));
							vp_buygoods_ad.setCurrentItem(0);
							initDot();
							updateDot();
						}
					}
				});
	}

	private void initDot() {
		for (int i = 0; i < adImages.size(); i++) {
			View dot_view = new View(SchoolBuyGoodsActivity.this);
			LayoutParams params = new LayoutParams(10, 10);
			params.leftMargin = 5;
			params.rightMargin = 5;
			dot_view.setLayoutParams(params);
			dot_view.setBackgroundResource(R.drawable.selector_dot);
			ad_buygoods_dot_layout.addView(dot_view);
		}
	}

	private void updateDot() {
		int currentPageIndex = vp_buygoods_ad.getCurrentItem();
		for (int i = 0; i < ad_buygoods_dot_layout.getChildCount(); i++) {
			if (i == currentPageIndex) {
				ad_buygoods_dot_layout.getChildAt(i).setEnabled(true);
			} else {
				ad_buygoods_dot_layout.getChildAt(i).setEnabled(false);
			}
		}
	}

	/**
	 * 获取商品分类数据
	 */
	private void getGoodsCategory() {
		boolean netIsOk = BaseUtils.isMobileConnected(SchoolBuyGoodsActivity.this);
		if (netIsOk) {
			showLoading2("数据加载中...");
			http = new HttpUtils(60 * 1000);
			http.send(HttpMethod.GET, GlobalConstant.SHOP_ACTION_SHOP_CATEGORY,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							showToast("服务器繁忙,稍后再试....");
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							gson = new Gson();
							ArrayList<ShopCategoryBean> shopCategorys = gson
									.fromJson(
											info.result,
											new TypeToken<ArrayList<ShopCategoryBean>>() {
											}.getType());
							fillDate(shopCategorys);
						}

					});
		} else {
			showToast("网络不可用...");
		}
	}

	/**
	 * 填充商品分类数据
	 * 
	 * @param shopCategorys
	 */
	private void fillDate(ArrayList<ShopCategoryBean> shopCategorys) {
		vp_buy_goods.setAdapter(new BuyGoodsIndicatorAdapter(
				getSupportFragmentManager(), shopCategorys));
		buy_goods_indicator.setViewPager(vp_buy_goods, 0);
		buy_goods_indicator.setVisibility(View.VISIBLE);

		dismissProgressDialog();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, ShopGoodsDetailActivity.class);
		startActivity(intent);
	}
}
