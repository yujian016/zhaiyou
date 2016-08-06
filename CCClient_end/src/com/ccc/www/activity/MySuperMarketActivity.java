package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.MySuperMarketIndicatorAdapter;
import com.ccc.www.bean.SupMarketCategoryBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.TabPageIndicator;

/**
 * 我的超市
 * 
 * @author Administrator
 * 
 */
public class MySuperMarketActivity extends BaseActivity {

	ImageButton ib_select_school_goback;

	String TAG = "MySuperMarketActivity";

	List<SupMarketCategoryBean> allSockCategoryBean = new ArrayList<SupMarketCategoryBean>();

	ViewPager pager;
	TabPageIndicator indicator;
	int userid;

	TextView mysp_setting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		userid = UserUtil.getuserid(this);

		setContentView(R.layout.activity_mysupermarket);
		initview();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_select_school_goback:
			MySuperMarketActivity.this.finish();
			break;
		case R.id.mysp_setting:

			Intent intent = new Intent();
			intent.setClass(MySuperMarketActivity.this,
					MySuperMarketSettingActivity.class);
			startActivity(intent);

			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findviewWithId() {
		ib_select_school_goback = (ImageButton) findViewById(R.id.ib_select_school_goback);
		mysp_setting = (TextView) findViewById(R.id.mysp_setting);

		pager = (ViewPager) findViewById(R.id.activity_proxysock_viewpager);
		// 实例化TabPageIndicator然后设置ViewPager与之关联
		indicator = (TabPageIndicator) findViewById(R.id.activity_proxysock_indicator);
	}

	@Override
	public void initListener() {
		ib_select_school_goback.setOnClickListener(this);
		mysp_setting.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		loadData(HttpMethod.GET, GlobalConstant.SUP_ACTION_GET_GOODS_CATEGORY,
				null, new RequestCallBack<String>() {
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
									allSockCategoryBean.add(bean);
								}

								Log.v(TAG, "allSockCategoryBean.size()  "
										+ allSockCategoryBean.size());

								if (allSockCategoryBean.size() > 0) {

									MySuperMarketIndicatorAdapter adapter = new MySuperMarketIndicatorAdapter(
											getSupportFragmentManager(),
											allSockCategoryBean);

									pager.setAdapter(adapter);

									Log.v(TAG, "pager  " + pager);
									Log.v(TAG, "indicator  " + indicator);

									indicator.setViewPager(pager);

									indicator.setVisibility(View.VISIBLE);

									// 如果我们要对ViewPager设置监听，用indicator设置就行了
									indicator
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

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}
				});
	}

}
