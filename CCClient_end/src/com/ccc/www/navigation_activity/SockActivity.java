package com.ccc.www.navigation_activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.ccc.ccclient_end.R;
import com.ccc.www.activity.BaseActivity;
import com.ccc.www.adapter.SockIndicatorAdapter;
import com.ccc.www.bean.SupMarketCategoryBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.viewpagerindicator.TabPageIndicator;

public class SockActivity extends BaseActivity {

	Activity activity;
	private ViewPager vpSock;
	private TabPageIndicator indicator;

	private HttpUtils http;
	private Gson gson;

	String TAG = "SockActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = SockActivity.this;
		setContentView(R.layout.activity_sock);
		initview();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findviewWithId() {
		// TODO Auto-generated method stub
		vpSock = (ViewPager) findViewById(R.id.vp_sock);
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initdata() {
		getSockCategory();
	}

	/**
	 * 获取零食分类数据
	 */
	private void getSockCategory() {
		boolean netIsOk = BaseUtils.isMobileConnected(SockActivity.this);
		if (netIsOk) {
			showLoading2("数据加载中...");
			http = new HttpUtils(60 * 1000);
			http.send(HttpMethod.GET,
					GlobalConstant.SUP_ACTION_GET_GOODS_CATEGORY,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dismissProgressDialog();
							showToast("服务器繁忙,稍后再试....");
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {

							Log.v(TAG, "info.result  " + info.result);

							gson = new Gson();
							ArrayList<SupMarketCategoryBean> sockCategorys = gson
									.fromJson(
											info.result,
											new TypeToken<ArrayList<SupMarketCategoryBean>>() {
											}.getType());
							fillDate(sockCategorys);
						}
					});
		} else {
			showToast("网络不可用...");
		}
	}

	/**
	 * 填充零食分类数据
	 * 
	 * @param cates
	 */
	private void fillDate(final ArrayList<SupMarketCategoryBean> cates) {
		if (cates != null) {
			vpSock.setAdapter(new SockIndicatorAdapter(
					getSupportFragmentManager(), cates));
			indicator.setViewPager(vpSock, 0);
			indicator.setVisibility(View.VISIBLE);
		}
		dismissProgressDialog();
	}
}
