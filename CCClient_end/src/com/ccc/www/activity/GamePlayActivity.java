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
import com.ccc.www.adapter.GamePlayAdapter;
import com.ccc.www.bean.AdBean;
import com.ccc.www.bean.Info_GamePlayBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
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

public class GamePlayActivity extends BaseActivity {

	private String TAG = "GamePlayActivity";

	private ImageButton ibGoback;
	private XListView lvGameplay;
	
	TextView  tv_send_second_info;

	LinearLayout ad_game_dot_layout;
	ViewPager vp_game_ad;

	EditText et_gamesearch;
	ImageButton btn_game_search;

	private Gson gson = new Gson();
	private ArrayList<AdBean> adImages = new ArrayList<AdBean>();

	private HttpUtils http;
	ArrayList<Info_GamePlayBean> dps = new ArrayList<Info_GamePlayBean>();

	ArrayList<Info_GamePlayBean> dps_save = new ArrayList<Info_GamePlayBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gameplay);
		initview();
	}

	@Override
	public void findviewWithId() {
		ibGoback = (ImageButton) findViewById(R.id.ib_game_goback);
		lvGameplay = (XListView) findViewById(R.id.lv_game_play);
		tv_send_second_info = (TextView) findViewById(R.id.tv_send_second_info);

		lvGameplay.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				XListViewUtil.endload(lvGameplay);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				XListViewUtil.endload(lvGameplay);
			}
		});

		lvGameplay.setPullLoadEnable(false);
		lvGameplay.setPullRefreshEnable(false);

		ad_game_dot_layout = (LinearLayout) findViewById(R.id.ad_game_dot_layout);
		vp_game_ad = (ViewPager) findViewById(R.id.vp_game_ad);

		et_gamesearch = (EditText) findViewById(R.id.et_gamesearch);
		btn_game_search = (ImageButton) findViewById(R.id.btn_game_search);

	}

	@Override
	public void initListener() {
		ibGoback.setOnClickListener(this);
		btn_game_search.setOnClickListener(this);
		lvGameplay.setOnItemClickListener(this);
		tv_send_second_info.setOnClickListener(this);

		et_gamesearch.addTextChangedListener(new TextWatcher() {
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
				String school_name = et_gamesearch.getText().toString().trim();
				if (TextUtils.isEmpty(school_name)) {

					dps.removeAll(dps);
					dps.addAll(dps_save);

					fillDate(dps);
				}
			}
		});

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ib_game_goback:
			this.finish();
			break;
		case R.id.tv_send_second_info:
			int userid = UserUtil.getuserid(this);
			if (userid > 0) {
				Intent intent = new Intent();
				intent.setClass(GamePlayActivity.this,
						PublishGamePlayInfoActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.setClass(GamePlayActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.btn_game_search:
			String content = et_gamesearch.getText().toString().trim();
			if (TextUtils.isEmpty(content)) {
				showToast("请输入代练信息");
				return;
			}

			// 赋值给dps

			boolean netIsOk = BaseUtils
					.isMobileConnected(GamePlayActivity.this);
			if (netIsOk) {
				showLoading2("数据加载中");

				RequestParams params = new RequestParams();
				params.addBodyParameter("gp_title", content);

				Xutils.loadData(HttpMethod.POST,
						GlobalConstant.GET_GAME_PLAY_SEARCH, params,
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
												new TypeToken<ArrayList<Info_GamePlayBean>>() {
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
	public void initdata() {

		getetData();
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
						int currentPageIndex = vp_game_ad.getCurrentItem();
						if (currentPageIndex == adImages.size() - 1) {
							vp_game_ad.setCurrentItem(0);
						} else {
							currentPageIndex++;
							vp_game_ad.setCurrentItem(currentPageIndex);
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
		params.addBodyParameter("ad_type", "8");
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
							vp_game_ad.setAdapter(new AdImagePagerAdapter(
									adImages, GamePlayActivity.this));
							vp_game_ad.setCurrentItem(0);
							initDot();
							updateDot();
						}
					}
				});
	}

	private void initDot() {
		for (int i = 0; i < adImages.size(); i++) {
			View dot_view = new View(GamePlayActivity.this);
			LayoutParams params = new LayoutParams(10, 10);
			params.leftMargin = 5;
			params.rightMargin = 5;
			dot_view.setLayoutParams(params);
			dot_view.setBackgroundResource(R.drawable.selector_dot);
			ad_game_dot_layout.addView(dot_view);
		}
	}

	private void updateDot() {
		int currentPageIndex = vp_game_ad.getCurrentItem();
		for (int i = 0; i < ad_game_dot_layout.getChildCount(); i++) {
			if (i == currentPageIndex) {
				ad_game_dot_layout.getChildAt(i).setEnabled(true);
			} else {
				ad_game_dot_layout.getChildAt(i).setEnabled(false);
			}
		}
	}

	/**
	 * 获取游戏代理数据
	 */
	private void getetData() {
		boolean netIsOk = BaseUtils.isMobileConnected(GamePlayActivity.this);
		if (netIsOk) {
			showLoading2("数据加载中");
			http = new HttpUtils(60 * 1000);
			http.send(HttpMethod.GET, GlobalConstant.GAME_PLAY_DATA,
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
											new TypeToken<ArrayList<Info_GamePlayBean>>() {
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
	 * 填充游戏代理数据
	 * 
	 * @param dps
	 */
	private void fillDate(ArrayList<Info_GamePlayBean> ets) {
		lvGameplay.setAdapter(new GamePlayAdapter(GamePlayActivity.this, ets));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}
}
