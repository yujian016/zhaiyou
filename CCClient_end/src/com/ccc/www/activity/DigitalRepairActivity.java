package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.AdImagePagerAdapter;
import com.ccc.www.adapter.DigitalRepairAdapter;
import com.ccc.www.bean.AdBean;
import com.ccc.www.bean.Info_DigitalRepairBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.XListViewUtil;
import com.ccc.www.util.Xutils;
import com.ccc.www.view.XListView;
import com.ccc.www.view.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class DigitalRepairActivity extends BaseActivity {

	String TAG = "DigitalRepairActivity";

	ImageButton ib_digital_goback;
	TextView activity_digitalrepair_publish;
	XListView activity_digitalrepair_listview;
	LinearLayout ad_digitalrepair_dot_layout;
	ViewPager vp_digitalrepair_ad;

	EditText et_shopsearch;
	ImageButton btn_search;

	private HttpUtils http;
	private Gson gson = new Gson();

	ArrayList<Info_DigitalRepairBean> dps = new ArrayList<Info_DigitalRepairBean>();

	ArrayList<Info_DigitalRepairBean> dps_save = new ArrayList<Info_DigitalRepairBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_digitalrepair);
		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_digital_goback:
			DigitalRepairActivity.this.finish();
			break;
		case R.id.btn_search:
			String content = et_shopsearch.getText().toString().trim();
			if (TextUtils.isEmpty(content)) {
				showToast("请输入搜索标题");
				return;
			}

			// 搜索的结果赋值 dps

			boolean netIsOk = BaseUtils
					.isMobileConnected(DigitalRepairActivity.this);
			if (netIsOk) {
				showLoading2("数据加载中");

				RequestParams params = new RequestParams();
				params.addBodyParameter("drepair_title", content);

				Xutils.loadData(HttpMethod.POST,
						GlobalConstant.GET_DIGITAL_INFO_SEARCH, params,
						new RequestCallBack<String>() {

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								dismissProgressDialog();
								showToast("服务器繁忙,稍后再试");
							}

							@Override
							public void onSuccess(ResponseInfo<String> info) {
								dismissProgressDialog();

								String jsondata = info.result;

								Log.v(TAG, "jsondata " + jsondata);

								gson = new Gson();
								dps = gson
										.fromJson(
												jsondata,
												new TypeToken<ArrayList<Info_DigitalRepairBean>>() {
												}.getType());
								fillDate(dps);
							}
						});

			} else {
				showToast("网络不可用");
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void findviewWithId() {
		ib_digital_goback = (ImageButton) findViewById(R.id.ib_digital_goback);
		activity_digitalrepair_listview = (XListView) findViewById(R.id.activity_digitalrepair_listview);

		vp_digitalrepair_ad = (ViewPager) findViewById(R.id.vp_digitalrepair_ad);
		ad_digitalrepair_dot_layout = (LinearLayout) findViewById(R.id.ad_digitalrepair_dot_layout);

		et_shopsearch = (EditText) findViewById(R.id.et_shopsearch);
		btn_search = (ImageButton) findViewById(R.id.btn_search);

		activity_digitalrepair_listview.setPullLoadEnable(false);
		activity_digitalrepair_listview.setPullRefreshEnable(true);
		activity_digitalrepair_listview
				.setXListViewListener(new IXListViewListener() {
					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						XListViewUtil.endload(activity_digitalrepair_listview);
					}

					@Override
					public void onLoadMore() {
						// TODO Auto-generated method stub
						XListViewUtil.endload(activity_digitalrepair_listview);
					}
				});
	}

	@Override
	public void initListener() {
		ib_digital_goback.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		activity_digitalrepair_listview.setOnItemClickListener(this);

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

					dps.removeAll(dps);
					dps.addAll(dps_save);

					activity_digitalrepair_listview
							.setAdapter(new DigitalRepairAdapter(
									DigitalRepairActivity.this, dps));
				}
			}
		});
	}

	@Override
	public void initdata() {
		getDigitalRepairData();
		initAd();
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
						int currentPageIndex = vp_digitalrepair_ad
								.getCurrentItem();
						if (currentPageIndex == adImages.size() - 1) {
							vp_digitalrepair_ad.setCurrentItem(0);
						} else {
							currentPageIndex++;
							vp_digitalrepair_ad
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
							vp_digitalrepair_ad
									.setAdapter(new AdImagePagerAdapter(
											adImages,
											DigitalRepairActivity.this));
							vp_digitalrepair_ad.setCurrentItem(0);
							initDot();
							updateDot();
						}
					}
				});
	}

	private void initDot() {
		for (int i = 0; i < adImages.size(); i++) {
			View dot_view = new View(DigitalRepairActivity.this);
			LayoutParams params = new LayoutParams(10, 10);
			params.leftMargin = 5;
			params.rightMargin = 5;
			dot_view.setLayoutParams(params);
			dot_view.setBackgroundResource(R.drawable.selector_dot);
			ad_digitalrepair_dot_layout.addView(dot_view);
		}
	}

	private void updateDot() {
		int currentPageIndex = vp_digitalrepair_ad.getCurrentItem();
		for (int i = 0; i < ad_digitalrepair_dot_layout.getChildCount(); i++) {
			if (i == currentPageIndex) {
				ad_digitalrepair_dot_layout.getChildAt(i).setEnabled(true);
			} else {
				ad_digitalrepair_dot_layout.getChildAt(i).setEnabled(false);
			}
		}
	}

	/**
	 * 获取数码快修数据
	 */
	private void getDigitalRepairData() {
		boolean netIsOk = BaseUtils
				.isMobileConnected(DigitalRepairActivity.this);
		if (netIsOk) {
			showLoading2("数据加载中");
			http = new HttpUtils(60 * 1000);
			http.send(HttpMethod.GET, GlobalConstant.INFO_ACTION_GET_ALLDP,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dismissProgressDialog();
							showToast("服务器繁忙,稍后再试");
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							dismissProgressDialog();

							String jsondata = info.result;

							Log.v(TAG, "jsondata " + jsondata);

							gson = new Gson();
							dps = gson
									.fromJson(
											jsondata,
											new TypeToken<ArrayList<Info_DigitalRepairBean>>() {
											}.getType());

							dps_save.removeAll(dps_save);
							for (int i = 0; i < dps.size(); i++) {
								dps_save.add(dps.get(i));
							}

							fillDate(dps);
						}
					});
		} else {
			showToast("网络不可用");
		}
	}

	/**
	 * 填充数码快修数据
	 * 
	 * @param dps
	 */
	private void fillDate(ArrayList<Info_DigitalRepairBean> dps) {
		activity_digitalrepair_listview.setAdapter(new DigitalRepairAdapter(
				DigitalRepairActivity.this, dps));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int dpid = dps.get(position - 1).getId();
		Intent intent = new Intent(this, DigitalRepairDetailActivity.class);
		intent.putExtra("dpid", dpid);
		startActivity(intent);
	}

}
