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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.ccc.ccclient_end.R;
import com.ccc.www.activity.CouponActivity.GetCoupon;
import com.ccc.www.adapter.AdImagePagerAdapter;
import com.ccc.www.adapter.GroupBuyGoodsAdapter;
import com.ccc.www.adapter.GroupBuyInfoAdapter;
import com.ccc.www.bean.AdBean;
import com.ccc.www.bean.CouponBean;
import com.ccc.www.bean.GroupBuyGoodsBean;
import com.ccc.www.bean.GroupBuyInfoBean;
import com.ccc.www.bean.ShopGoodBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class GroupBuyActivity extends BaseActivity {

	
	String TAG = "GroupBuyActivity";
	private Gson gson = new Gson();
	private HttpUtils http = null;
	
	
	
	ImageButton ib_group_buy_goback;
	LinearLayout ad_group_buy_dot_layout;
	ViewPager vp_group_buy_ad;
	
	ListView group_buy_changci_listview;
	ListView group_buy_goods_listview;
	int uid=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.group_buy_activity);
		initview();
		registerReceiver(buyGroupGoods,
				new IntentFilter(GlobalConstant.BuyGroupGoods));
		uid=UserUtil.getuserid(GroupBuyActivity.this);
	}
	
	
	
	BuyGroupGoods buyGroupGoods = new BuyGroupGoods();

	class BuyGroupGoods extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent data) {

			Log.v(TAG, "BuyGroupGoods  ");

			if (data != null) {
				ShopGoodBean bean = (ShopGoodBean) data
						.getSerializableExtra("bean");

				checkhaveget(bean);

			}
		}
	}
	
	
	/**
	 * 验证是团购过该商品
	 * 
	 * @param bean
	 */
	private void checkhaveget(final ShopGoodBean bean) {
		
		if(uid<=0){
			Intent intent = new Intent();
			intent.setClass(GroupBuyActivity.this, LoginActivity.class);
			startActivity(intent);
			showToast("请先登录");
			return;
		}
		
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", uid + "");
		params.addBodyParameter("group_buy_info_id", bean.getGroup_info_id() + "");
		params.addBodyParameter("goods_id", bean.getId() + "");

		Log.v(TAG, "group_buy_info_id  " + bean.getGroup_info_id());
		Log.v(TAG, "goods_id  " + bean.getId());

		loadData(HttpMethod.POST, GlobalConstant.Get_group_buy_goods_by_uid, params,
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
									Intent intent = new Intent();
									Bundle bundle = new Bundle();
									bundle.putSerializable("bean", (Serializable) bean);
									intent.putExtras(bundle);
									intent.setClass(GroupBuyActivity.this, SubmitGroupBuyOrderActivity.class);
									startActivity(intent);
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
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.ib_group_buy_goback:
			GroupBuyActivity.this.finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findviewWithId() {
		// TODO Auto-generated method stub
		ib_group_buy_goback=(ImageButton) findViewById(R.id.ib_group_buy_goback);
		
		vp_group_buy_ad = (ViewPager) findViewById(R.id.vp_group_buy_ad);
		ad_group_buy_dot_layout = (LinearLayout) findViewById(R.id.ad_group_buy_dot_layout);

		group_buy_changci_listview = (ListView) findViewById(R.id.group_buy_changci_listview);
		group_buy_goods_listview = (ListView) findViewById(R.id.group_buy_goods_listview);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		ib_group_buy_goback.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		// TODO Auto-generated method stub
		initAd();
		getGroupBuyInfoBean();
	}

	/**
	 * 获取团购场次数据
	 */
	private void getGroupBuyInfoBean() {
		boolean netIsOk = BaseUtils.isMobileConnected(GroupBuyActivity.this);
		if (netIsOk) {
			showLoading2("数据加载中");
			http = new HttpUtils(60 * 1000);
			http.send(HttpMethod.GET,
					GlobalConstant.Get_group_buy_info,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dismissProgressDialog();
							showToast("服务器繁忙,稍后再试");
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							dismissProgressDialog();
							try {
								List<GroupBuyInfoBean> allGroupBuyInfoBean = new ArrayList<GroupBuyInfoBean>();

								JSONArray array = new JSONArray(info.result);
								for (int i = 0; i < array.length(); i++) {
									JSONObject json = array.getJSONObject(i);

									int id = json.getInt("id");
									String group_buy_time = json
											.getString("group_buy_time");

									GroupBuyInfoBean bean = new GroupBuyInfoBean(
											id, group_buy_time);
									allGroupBuyInfoBean.add(bean);
								}

								getDate(allGroupBuyInfoBean);

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
	 * 填充团购场次
	 * 
	 * @param cates
	 */
	private void getDate(final List<GroupBuyInfoBean> cates) {

		group_buy_changci_listview.setAdapter(new GroupBuyInfoAdapter(
				GroupBuyActivity.this, cates));

		group_buy_changci_listview
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
	
	
	ArrayList<ShopGoodBean> digitalGoods;
	private void loadgoodsdata(GroupBuyInfoBean bean) {
		boolean netIsOk = BaseUtils.isMobileConnected(GroupBuyActivity.this);
		if (netIsOk) {
			showLoading2("数据加载中");
			RequestParams params = new RequestParams();
			String cate_id = String.valueOf(bean.getId());
			params.addBodyParameter("group_info_id", cate_id);

			Log.v(TAG, "group_info_id  " + cate_id);

			http.send(HttpMethod.POST, GlobalConstant.Get_group_buy_goods,
					params, new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dismissProgressDialog();
							Toast.makeText(GroupBuyActivity.this, "服务器繁忙，稍后再试",
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
										new TypeToken<ArrayList<ShopGoodBean>>() {
										}.getType());
								if (digitalGoods == null) {
									digitalGoods = new ArrayList<ShopGoodBean>();
								}

								group_buy_goods_listview
										.setAdapter(new GroupBuyGoodsAdapter(
												GroupBuyActivity.this,
												digitalGoods));
							} else {
								group_buy_goods_listview.setAdapter(null);
							}

						}
					});
		} else {
			showToast("网络不可用...");
		}

	}
	
	private ArrayList<AdBean> adImages = new ArrayList<AdBean>();

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
						int currentPageIndex = vp_group_buy_ad
								.getCurrentItem();
						if (currentPageIndex == adImages.size() - 1) {
							vp_group_buy_ad.setCurrentItem(0);
						} else {
							currentPageIndex++;
							vp_group_buy_ad
									.setCurrentItem(currentPageIndex);
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
		params.addBodyParameter("ad_type", "5");
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
							vp_group_buy_ad
									.setAdapter(new AdImagePagerAdapter(
											adImages,
											GroupBuyActivity.this));
							vp_group_buy_ad.setCurrentItem(0);
							initDot();
							updateDot();
						}
					}
				});
	}
	
	
	private void initDot() {
		for (int i = 0; i < adImages.size(); i++) {
			View dot_view = new View(GroupBuyActivity.this);
			LayoutParams params = new LayoutParams(10, 10);
			params.leftMargin = 5;
			params.rightMargin = 5;
			dot_view.setLayoutParams(params);
			dot_view.setBackgroundResource(R.drawable.selector_dot);
			ad_group_buy_dot_layout.addView(dot_view);
		}
	}

	private void updateDot() {
		int currentPageIndex = vp_group_buy_ad.getCurrentItem();
		for (int i = 0; i < ad_group_buy_dot_layout.getChildCount(); i++) {
			if (i == currentPageIndex) {
				ad_group_buy_dot_layout.getChildAt(i).setEnabled(true);
			} else {
				ad_group_buy_dot_layout.getChildAt(i).setEnabled(false);
			}
		}
	}
}
