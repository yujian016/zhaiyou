package com.ccc.www.activity;

import java.io.Serializable;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.AdImagePagerAdapter;
import com.ccc.www.adapter.DigitalCategoryAdapter;
import com.ccc.www.adapter.DigitalGoodsAdapter;
import com.ccc.www.bean.AdBean;
import com.ccc.www.bean.DigitalCategoryBean;
import com.ccc.www.bean.GoodsBean;
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

public class DigitalActivity2 extends BaseActivity {

	private ImageButton ibGoback;

	ArrayList<GoodsBean> digitalGoods;
	private Gson gson = new Gson();
	private HttpUtils http = null;
	private String TAG = "DigitalActivity2";

	private ArrayList<AdBean> adImages = new ArrayList<AdBean>();
	private ViewPager vp_digital_ad;
	private LinearLayout ad_digital_dot_layout;

	ListView schoolbug_categorylistview;
	ListView schoolbug_goodslistview;

	ImageView iv_proxysockcartcount;
	BadgeView badge;

	UpdateDigitalCartCount updateDigitalCartCount = new UpdateDigitalCartCount();

	/**
	 * 更新购物车数目广播
	 * 
	 * @author Administrator
	 * 
	 */
	class UpdateDigitalCartCount extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			updateCartCount();
		}
	}

	PaySuccessToMyOrder paySuccessToMyOrder = new PaySuccessToMyOrder();

	/**
	 * 支付成功后关闭页面
	 * 
	 * @author Administrator
	 * 
	 */
	class PaySuccessToMyOrder extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			DigitalActivity2.this.finish();
		}
	}

	/**
	 * 更新购物车数目
	 */
	private void updateCartCount() {
		// 取出数据库中count>0的货物

		List<GoodsBean> allgoods = DBUtil.getDigitalCart(DigitalActivity2.this);

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
		unregisterReceiver(updateDigitalCartCount);
		unregisterReceiver(paySuccessToMyOrder);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		registerReceiver(paySuccessToMyOrder, new IntentFilter(
				GlobalConstant.PaySuccessToMyOrder));

		registerReceiver(updateDigitalCartCount, new IntentFilter(
				GlobalConstant.UpdateDigitalCartCount));

		setContentView(R.layout.activity_digital2);

		initview();

		badge = new BadgeView(DigitalActivity2.this, iv_proxysockcartcount);

		DBUtil.deleteDigitalCart(this);
	}

	@Override
	public void findviewWithId() {
		ibGoback = (ImageButton) findViewById(R.id.ib_digital_goback);
		vp_digital_ad = (ViewPager) findViewById(R.id.vp_digital_ad);
		ad_digital_dot_layout = (LinearLayout) findViewById(R.id.ad_digital_dot_layout);
		iv_proxysockcartcount = (ImageView) findViewById(R.id.iv_proxysockcartcount);

		schoolbug_categorylistview = (ListView) findViewById(R.id.schoolbug_categorylistview);
		schoolbug_goodslistview = (ListView) findViewById(R.id.schoolbug_goodslistview);

	}

	@Override
	public void initListener() {
		ibGoback.setOnClickListener(this);
		iv_proxysockcartcount.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_digital_goback:
			this.finish();
			break;
		case R.id.iv_proxysockcartcount:

			Intent intent = new Intent();
			intent.setClass(DigitalActivity2.this, DigitalCartActivity.class);
			startActivity(intent);

			break;
		default:
			break;
		}
	}

	@Override
	public void initdata() {
		getDigitalCategory();
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
						int currentPageIndex = vp_digital_ad.getCurrentItem();
						if (currentPageIndex == adImages.size() - 1) {
							vp_digital_ad.setCurrentItem(0);
						} else {
							currentPageIndex++;
							vp_digital_ad.setCurrentItem(currentPageIndex);
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
		params.addBodyParameter("ad_type", "3");
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
							vp_digital_ad.setAdapter(new AdImagePagerAdapter(
									adImages, DigitalActivity2.this));
							vp_digital_ad.setCurrentItem(0);
							initDot();
							updateDot();
						}
					}
				});
	}

	private void initDot() {
		for (int i = 0; i < adImages.size(); i++) {
			View dot_view = new View(DigitalActivity2.this);
			LayoutParams params = new LayoutParams(10, 10);
			params.leftMargin = 5;
			params.rightMargin = 5;
			dot_view.setLayoutParams(params);
			dot_view.setBackgroundResource(R.drawable.selector_dot);
			ad_digital_dot_layout.addView(dot_view);
		}
	}

	private void updateDot() {
		int currentPageIndex = vp_digital_ad.getCurrentItem();
		for (int i = 0; i < ad_digital_dot_layout.getChildCount(); i++) {
			if (i == currentPageIndex) {
				ad_digital_dot_layout.getChildAt(i).setEnabled(true);
			} else {
				ad_digital_dot_layout.getChildAt(i).setEnabled(false);
			}
		}
	}

	private void getDigitalCategory() {
		boolean netIsOk = BaseUtils.isMobileConnected(DigitalActivity2.this);
		if (netIsOk) {
			showLoading2("数据加载中");
			http = new HttpUtils(60 * 1000);
			http.send(HttpMethod.GET,
					GlobalConstant.DIGITAL_ACTION_GET_DIGITALCATEGORY,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dismissProgressDialog();
							showToast("服务器繁忙,稍后再试");
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							dismissProgressDialog();
							// ArrayList<DigitalCategoryBean> digitalCategorys =
							// gson
							// .fromJson(
							// info.result,
							// new TypeToken<ArrayList<DigitalCategoryBean>>() {
							// }.getType());

							try {
								List<DigitalCategoryBean> allDigitalCategoryBean = new ArrayList<DigitalCategoryBean>();

								JSONArray array = new JSONArray(info.result);
								for (int i = 0; i < array.length(); i++) {
									JSONObject json = array.getJSONObject(i);

									int id = json.getInt("id");
									String digital_goods_category_name = json
											.getString("digital_goods_category_name");

									DigitalCategoryBean bean = new DigitalCategoryBean(
											id, digital_goods_category_name);
									allDigitalCategoryBean.add(bean);
								}

								getDate(allDigitalCategoryBean);

							} catch (JSONException e) {
								e.printStackTrace();
							}
							Log.v(TAG, "info.result  " + info.result);
						}
					});
		} else {
			showToast("网络不可用...");
		}
	}

	/**
	 * 获取数码分类数据
	 * 
	 * @param cates
	 */
	private void getDate(final List<DigitalCategoryBean> cates) {
		// vp_digital.setAdapter(new DigitalIndicatorAdapter(
		// getSupportFragmentManager(), cates));
		// digital_indicator.setViewPager(vp_digital, 0);
		// digital_indicator.setVisibility(View.VISIBLE);

		schoolbug_categorylistview.setAdapter(new DigitalCategoryAdapter(
				DigitalActivity2.this, cates));

		schoolbug_categorylistview
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						loadgoodsdata(cates.get(position));
					}
				});
		// 加载这个分类下面的商品
		if (cates.size() > 0) {
			loadgoodsdata(cates.get(0));
		}
	}

	private void loadgoodsdata(DigitalCategoryBean bean) {
		boolean netIsOk = BaseUtils.isMobileConnected(DigitalActivity2.this);
		if (netIsOk) {
			showLoading2("数据加载中");
			RequestParams params = new RequestParams();
			String cate_id = String.valueOf(bean.getId());
			params.addBodyParameter("digital_cate_id", cate_id);

			Log.v(TAG, "digital_cate_id  " + cate_id);

			http.send(HttpMethod.POST, GlobalConstant.GET_DIGITAL_GOODS,
					params, new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dismissProgressDialog();
							Toast.makeText(DigitalActivity2.this, "服务器繁忙，稍后再试",
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							dismissProgressDialog();

							String json = info.result;

							Log.v(TAG, "onSuccess  " + json);

							if (json.length() > 2) {
								gson = new Gson();
								digitalGoods = gson.fromJson(json,
										new TypeToken<ArrayList<GoodsBean>>() {
										}.getType());

								List<GoodsBean> allGoodsBean = DBUtil
										.getDigitalCart(DigitalActivity2.this);

								for (int i = 0; i < allGoodsBean.size(); i++) {
									for (int j = 0; j < digitalGoods.size(); j++) {
										if (allGoodsBean.get(i).getId() == digitalGoods
												.get(j).getId()) {
											digitalGoods.get(j).setCount(
													allGoodsBean.get(i)
															.getCount());
										}
									}
								}

								if (digitalGoods == null) {
									digitalGoods = new ArrayList<GoodsBean>();
								}

								schoolbug_goodslistview
										.setAdapter(new DigitalGoodsAdapter(
												DigitalActivity2.this,
												digitalGoods,
												R.layout.goods_lv_item));
								schoolbug_goodslistview
										.setOnItemClickListener(new OnItemClickListener() {
											@Override
											public void onItemClick(
													AdapterView<?> arg0,
													View arg1, int position,
													long arg3) {

												GoodsBean goodsbean = digitalGoods
														.get(position);

												int id = goodsbean.getId();

												Intent intent = new Intent();

												Bundle bundle = new Bundle();
												bundle.putSerializable(
														"goodsbean",
														(Serializable) goodsbean);
												intent.putExtras(bundle);

												intent.putExtra(
														"from",
														DigitalGoodsDetailActivity.FromDigital);
												intent.putExtra("id", id);
												intent.setClass(
														DigitalActivity2.this,
														DigitalGoodsDetailActivity.class);
												startActivity(intent);
											}
										});
							} else {
								schoolbug_goodslistview.setAdapter(null);
							}

						}
					});
		} else {
			showToast("网络不可用...");
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, ShopGoodsDetailActivity.class);
		startActivity(intent);
	}
}
