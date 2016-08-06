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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.AdImagePagerAdapter;
import com.ccc.www.bean.AdBean;
import com.ccc.www.bean.SecondaryTradeBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.ccc.www.util.XListViewUtil;
import com.ccc.www.view.XListView;
import com.ccc.www.view.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.viewbadger.BadgeView;

public class SecondaryTradeActivity extends BaseActivity {

	ImageButton ib_digital_goback;
	XListView activity_secondarytrade_listview;
	Gson gson = new Gson();
	Adapter adapter = new Adapter();

	LinearLayout ad_second_dot_layout;
	ViewPager vp_second_ad;
	TextView tv_send_second_info;

	ImageView iv_proxysockcartcount;

	EditText et_shopsearch;
	ImageButton btn_search;

	BadgeView badge;

	List<SecondaryTradeBean> allSecondaryTradeBean = new ArrayList<SecondaryTradeBean>();

	List<SecondaryTradeBean> allSecondaryTradeBean_save = new ArrayList<SecondaryTradeBean>();

	String TAG = "SecondaryTradeActivity";

	int userid;

	UpdateSecondaryTradeCart updateSecondaryTradeCart = new UpdateSecondaryTradeCart();

	public static int CloseSecondaryTrade = 888;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_secondarytrade);
		initview();

		badge = new BadgeView(SecondaryTradeActivity.this,
				iv_proxysockcartcount);

		userid = UserUtil.getuserid(this);

		registerReceiver(updateSecondaryTradeCart, new IntentFilter(
				GlobalConstant.UpdateSecondaryTradeCart));

		DBUtil.deleteSecondaryTradeCart(this, userid);
	}

	@Override
	public void findviewWithId() {
		ib_digital_goback = (ImageButton) findViewById(R.id.ib_digital_goback);
		activity_secondarytrade_listview = (XListView) findViewById(R.id.activity_secondarytrade_listview);

		vp_second_ad = (ViewPager) findViewById(R.id.vp_second_ad);
		ad_second_dot_layout = (LinearLayout) findViewById(R.id.ad_second_dot_layout);

		tv_send_second_info = (TextView) findViewById(R.id.tv_send_second_info);
		iv_proxysockcartcount = (ImageView) findViewById(R.id.iv_proxysockcartcount);

		et_shopsearch = (EditText) findViewById(R.id.et_shopsearch);
		btn_search = (ImageButton) findViewById(R.id.btn_search);

		activity_secondarytrade_listview.setPullLoadEnable(false);
		activity_secondarytrade_listview.setPullRefreshEnable(true);

		activity_secondarytrade_listview.setAdapter(adapter);

		activity_secondarytrade_listview
				.setXListViewListener(new IXListViewListener() {

					@Override
					public void onRefresh() {
						loaddata();
					}

					@Override
					public void onLoadMore() {
						// TODO Auto-generated method stub
						XListViewUtil.endload(activity_secondarytrade_listview);
					}
				});

	}

	@Override
	public void initListener() {
		ib_digital_goback.setOnClickListener(this);
		tv_send_second_info.setOnClickListener(this);
		iv_proxysockcartcount.setOnClickListener(this);
		btn_search.setOnClickListener(this);

		et_shopsearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				String school_name = et_shopsearch.getText().toString().trim();
				if (TextUtils.isEmpty(school_name)) {

					allSecondaryTradeBean.removeAll(allSecondaryTradeBean);
					allSecondaryTradeBean.addAll(allSecondaryTradeBean_save);

					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_digital_goback:
			SecondaryTradeActivity.this.finish();
			break;
		case R.id.btn_search:
			String content = et_shopsearch.getText().toString().trim();
			if (TextUtils.isEmpty(content)) {
				showToast("请输入搜索标题");
				return;
			}

			// 搜索的结果赋值 dps

			break;
		case R.id.tv_send_second_info:

			int userid = UserUtil.getuserid(this);
			if (userid > 0) {
				Intent intent = new Intent();
				intent.setClass(SecondaryTradeActivity.this,
						PublishSecondaryInfoActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.setClass(SecondaryTradeActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}

			break;
		case R.id.iv_proxysockcartcount:
			if (UserUtil.getuserid(this) > 0) {
				Intent cart = new Intent();
				cart.setClass(SecondaryTradeActivity.this,
						SecondaryTradeCartActivity.class);
				startActivity(cart);
			} else {
				Intent intent = new Intent();
				intent.setClass(SecondaryTradeActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}
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
						int currentPageIndex = vp_second_ad.getCurrentItem();
						if (currentPageIndex == adImages.size() - 1) {
							vp_second_ad.setCurrentItem(0);
						} else {
							currentPageIndex++;
							vp_second_ad.setCurrentItem(currentPageIndex);
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
		params.addBodyParameter("ad_type", "6");
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
							vp_second_ad.setAdapter(new AdImagePagerAdapter(
									adImages, SecondaryTradeActivity.this));
							vp_second_ad.setCurrentItem(0);
							initDot();
							updateDot();
						}
					}
				});
	}

	private void initDot() {
		for (int i = 0; i < adImages.size(); i++) {
			View dot_view = new View(SecondaryTradeActivity.this);
			LayoutParams params = new LayoutParams(10, 10);
			params.leftMargin = 5;
			params.rightMargin = 5;
			dot_view.setLayoutParams(params);
			dot_view.setBackgroundResource(R.drawable.selector_dot);
			ad_second_dot_layout.addView(dot_view);
		}
	}

	private void updateDot() {
		int currentPageIndex = vp_second_ad.getCurrentItem();
		for (int i = 0; i < ad_second_dot_layout.getChildCount(); i++) {
			if (i == currentPageIndex) {
				ad_second_dot_layout.getChildAt(i).setEnabled(true);
			} else {
				ad_second_dot_layout.getChildAt(i).setEnabled(false);
			}
		}
	}

	class Adapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allSecondaryTradeBean.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return allSecondaryTradeBean.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(SecondaryTradeActivity.this)
						.inflate(R.layout.item_secondarytrade, null);

				holder.item_secondarytrade_image = (ImageView) view
						.findViewById(R.id.item_secondarytrade_image);
				holder.item_secondarytrade_title = (TextView) view
						.findViewById(R.id.item_secondarytrade_title);
				holder.item_secondarytrade_desc = (TextView) view
						.findViewById(R.id.item_secondarytrade_desc);
				holder.item_secondarytrade_price = (TextView) view
						.findViewById(R.id.item_secondarytrade_price);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final SecondaryTradeBean bean = allSecondaryTradeBean.get(position);

			ImageLoader.getInstance().displayImage(
					GlobalConstant.SERVER_URL + bean.getLog1(),
					holder.item_secondarytrade_image,
					ImageLoaderOption.getoption());

			holder.item_secondarytrade_title.setText(bean.getTitle());

			holder.item_secondarytrade_desc.setText(bean.getDetail());

			holder.item_secondarytrade_price.setText("￥" + bean.getPrice()
					+ "元");

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putSerializable("bean", (Serializable) bean);
					intent.putExtras(bundle);
					intent.setClass(SecondaryTradeActivity.this,
							SecondaryTradeDetailActivity.class);
					startActivityForResult(intent, CloseSecondaryTrade);
				}
			});

			return view;
		}

		class ViewHolder {
			ImageView item_secondarytrade_image;
			TextView item_secondarytrade_title;
			TextView item_secondarytrade_desc;
			TextView item_secondarytrade_price;
		}

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0 == CloseSecondaryTrade && arg1 == CloseSecondaryTrade) {
			SecondaryTradeActivity.this.finish();
		}
		super.onActivityResult(arg0, arg1, arg2);
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

		// RequestParams params = new RequestParams();
		// params.addBodyParameter("ad_type", "6");
		loadData(HttpMethod.POST, GlobalConstant.GET_SECOND_HAND_GOOD, null,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();

						XListViewUtil.endload(activity_secondarytrade_listview);

						showToast("加载失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String json = info.result;
						Log.v(TAG, json);
						if (BaseUtils.isEmpty(json)) {
							showToast("加载失败");
							XListViewUtil
									.endload(activity_secondarytrade_listview);

						} else {
							try {
								List<SecondaryTradeBean> tempallSecondaryTradeBean = new ArrayList<SecondaryTradeBean>();

								JSONArray array = new JSONArray(json);
								for (int i = 0; i < array.length(); i++) {

									JSONObject obj = array.getJSONObject(i);

									int id = obj.getInt("id");
									int user_id = obj.getInt("user_id");
									String title = obj.getString("title");
									float price = (float) obj
											.getDouble("price");
									String detail = obj.getString("detail");
									String log1 = obj.getString("log1");
									String log2 = obj.getString("log2");
									String log3 = obj.getString("log3");
									String phone = obj.getString("phone");
									int status = obj.getInt("status");

									SecondaryTradeBean bean = new SecondaryTradeBean(
											id, user_id, title, price, detail,
											log1, log2, log3, phone, status);
									tempallSecondaryTradeBean.add(bean);
								}
								allSecondaryTradeBean_save = tempallSecondaryTradeBean;

								allSecondaryTradeBean = tempallSecondaryTradeBean;

								adapter.notifyDataSetChanged();
								XListViewUtil
										.endload(activity_secondarytrade_listview);

							} catch (JSONException e) {
								showToast("加载失败");
								XListViewUtil
										.endload(activity_secondarytrade_listview);
								e.printStackTrace();
							}
						}
					}
				});
	}

	/**
	 * 更新购物车数量
	 * 
	 * @author Administrator
	 * 
	 */
	class UpdateSecondaryTradeCart extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			List<SecondaryTradeBean> allSecondaryTradeBean = DBUtil
					.getSecondaryTradeCart(SecondaryTradeActivity.this, userid);

			int allcount = allSecondaryTradeBean.size();

			Log.v(TAG, "allcount  " + allcount);

			badge.setText("" + allcount);
			badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
			badge.setTextSize(10);
			if (allcount > 0) {
				badge.show();
			} else {
				badge.hide();
			}
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(updateSecondaryTradeCart);
		super.onDestroy();
	}

}
