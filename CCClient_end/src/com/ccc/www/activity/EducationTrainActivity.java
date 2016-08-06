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

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.AdImagePagerAdapter;
import com.ccc.www.adapter.EducationTrainAdapter;
import com.ccc.www.bean.AdBean;
import com.ccc.www.bean.Info_EducationTrainBean;
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

public class EducationTrainActivity extends BaseActivity {

	String TAG = "EducationTrainActivity";

	ImageButton ib_digital_goback;
	XListView activity_educationtrain_listview;
	LinearLayout ad_edu_dot_layout;
	ViewPager vp_edu_ad;

	EditText et_shopsearch;
	ImageButton btn_search;

	private HttpUtils http;
	private Gson gson = new Gson();
	ArrayList<Info_EducationTrainBean> dps = new ArrayList<Info_EducationTrainBean>();
	ArrayList<Info_EducationTrainBean> dps_save = new ArrayList<Info_EducationTrainBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_educationtrain);
		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_digital_goback:
			EducationTrainActivity.this.finish();
			break;
		case R.id.btn_search:
			String content = et_shopsearch.getText().toString().trim();
			if (TextUtils.isEmpty(content)) {
				showToast("请输入搜索标题");
				return;
			}

			// 结果赋值 dps

			boolean netIsOk = BaseUtils
					.isMobileConnected(EducationTrainActivity.this);
			if (netIsOk) {
				showLoading2("数据加载中");

				RequestParams params = new RequestParams();
				params.addBodyParameter("et_title", content);

				Xutils.loadData(HttpMethod.POST,
						GlobalConstant.GET_EducationTrain_SEARCH, params,
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

								Log.v(TAG, "info.result  " + info.result);

								gson = new Gson();
								dps = gson
										.fromJson(
												info.result,
												new TypeToken<ArrayList<Info_EducationTrainBean>>() {
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
		activity_educationtrain_listview = (XListView) findViewById(R.id.activity_educationtrain_listview);
		vp_edu_ad = (ViewPager) findViewById(R.id.vp_edu_ad);
		ad_edu_dot_layout = (LinearLayout) findViewById(R.id.ad_edu_dot_layout);

		et_shopsearch = (EditText) findViewById(R.id.et_shopsearch);
		btn_search = (ImageButton) findViewById(R.id.btn_search);

		activity_educationtrain_listview.setPullLoadEnable(false);
		activity_educationtrain_listview.setPullRefreshEnable(true);
		activity_educationtrain_listview
				.setXListViewListener(new IXListViewListener() {

					@Override
					public void onRefresh() {
						// TODO Auto-generated method stub
						XListViewUtil.endload(activity_educationtrain_listview);
					}

					@Override
					public void onLoadMore() {
						// TODO Auto-generated method stub
						XListViewUtil.endload(activity_educationtrain_listview);
					}
				});
	}

	@Override
	public void initListener() {
		ib_digital_goback.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		activity_educationtrain_listview.setOnItemClickListener(this);

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

					activity_educationtrain_listview
							.setAdapter(new EducationTrainAdapter(
									EducationTrainActivity.this, dps));
				}
			}
		});
	}

	@Override
	public void initdata() {
		// TODO Auto-generated method stub
		getetData();
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
						int currentPageIndex = vp_edu_ad.getCurrentItem();
						if (currentPageIndex == adImages.size() - 1) {
							vp_edu_ad.setCurrentItem(0);
						} else {
							currentPageIndex++;
							vp_edu_ad.setCurrentItem(currentPageIndex);
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
		params.addBodyParameter("ad_type", "7");
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
							vp_edu_ad.setAdapter(new AdImagePagerAdapter(
									adImages, EducationTrainActivity.this));
							vp_edu_ad.setCurrentItem(0);
							initDot();
							updateDot();
						}
					}
				});
	}

	private void initDot() {
		for (int i = 0; i < adImages.size(); i++) {
			View dot_view = new View(EducationTrainActivity.this);
			LayoutParams params = new LayoutParams(10, 10);
			params.leftMargin = 5;
			params.rightMargin = 5;
			dot_view.setLayoutParams(params);
			dot_view.setBackgroundResource(R.drawable.selector_dot);
			ad_edu_dot_layout.addView(dot_view);
		}
	}

	private void updateDot() {
		int currentPageIndex = vp_edu_ad.getCurrentItem();
		for (int i = 0; i < ad_edu_dot_layout.getChildCount(); i++) {
			if (i == currentPageIndex) {
				ad_edu_dot_layout.getChildAt(i).setEnabled(true);
			} else {
				ad_edu_dot_layout.getChildAt(i).setEnabled(false);
			}
		}
	}

	/**
	 * 获取数码快修数据
	 */
	private void getetData() {
		boolean netIsOk = BaseUtils
				.isMobileConnected(EducationTrainActivity.this);
		if (netIsOk) {
			showLoading2("数据加载中");
			http = new HttpUtils(60 * 1000);
			http.send(HttpMethod.GET, GlobalConstant.INFO_ACTION_GET_ALLET,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dismissProgressDialog();
							showToast("服务器繁忙,稍后再试");
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							dismissProgressDialog();

							Log.v(TAG, "info.result  " + info.result);

							gson = new Gson();
							dps = gson
									.fromJson(
											info.result,
											new TypeToken<ArrayList<Info_EducationTrainBean>>() {
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
	private void fillDate(ArrayList<Info_EducationTrainBean> ets) {
		activity_educationtrain_listview.setAdapter(new EducationTrainAdapter(
				EducationTrainActivity.this, ets));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		int dpid = dps.get(position - 1).getId();
		Intent intent = new Intent(this, EducationTrainDetailActivity.class);
		intent.putExtra("etid", dpid);
		startActivity(intent);
	}

}
