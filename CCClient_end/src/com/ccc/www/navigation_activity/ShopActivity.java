package com.ccc.www.navigation_activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ccc.ccclient_end.R;
import com.ccc.www.activity.BaseActivity;
import com.ccc.www.adapter.ShopAdapter;
import com.ccc.www.bean.ShopBean;
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

public class ShopActivity extends BaseActivity {
	private Activity activity;
	private XListView lvShop;

	EditText et_shopsearch;
	ImageButton btn_search;

	private HttpUtils http;
	private Gson gson = new Gson();

	String allshopreturnstr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = ShopActivity.this;

		setContentView(R.layout.shop_fragment);
		initview();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:
			String content = et_shopsearch.getText().toString().trim();

			if (TextUtils.isEmpty(content)) {
				showToast("请输入店铺名称");
				return;
			}

			boolean connected = BaseUtils.isMobileConnected(ShopActivity.this);
			if (connected) {
				showLoading2("数据加载中");

				RequestParams params = new RequestParams();
				params.addBodyParameter("shop_title", content);

				Xutils.loadData(HttpMethod.POST,
						GlobalConstant.SHOP_ACTION_GET_SHOP_SEARCH, params,
						new RequestCallBack<String>() {

							@Override
							public void onFailure(HttpException arg0,
									String error) {
								dismissProgressDialog();
								showToast("服务器繁忙,稍后再试");
							}

							@Override
							public void onSuccess(ResponseInfo<String> info) {
								dismissProgressDialog();
								System.out.println(info.result);
								if (info.result.length() > 0) {
									fillShopData(info.result);
								}
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public void findviewWithId() {
		// TODO Auto-generated method stub
		lvShop = (XListView) findViewById(R.id.lv_shop);
		lvShop.setPullLoadEnable(false);
		lvShop.setPullRefreshEnable(true);

		et_shopsearch = (EditText) findViewById(R.id.et_shopsearch);
		btn_search = (ImageButton) findViewById(R.id.btn_search);
	}

	@Override
	public void initListener() {
		lvShop.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				getAllShop();
			}

			@Override
			public void onLoadMore() {
				XListViewUtil.endload(lvShop);
			}
		});

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
					if (!TextUtils.isEmpty(allshopreturnstr)) {
						fillShopData(allshopreturnstr);
					}
				}
			}
		});
	}

	@Override
	public void initdata() {
		getAllShop();
	}

	private void getAllShop() {
		http = new HttpUtils(60 * 1000);
		boolean connected = BaseUtils.isMobileConnected(ShopActivity.this);
		if (connected) {
			showLoading2("数据加载中");
			http.send(HttpMethod.GET, GlobalConstant.SHOP_ACTION_GET_ALLSHOP,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String error) {
							dismissProgressDialog();
							XListViewUtil.endload(lvShop);
							showToast("服务器繁忙,稍后再试");
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							dismissProgressDialog();
							XListViewUtil.endload(lvShop);
							System.out.println(info.result);
							if (info.result.length() > 0) {
								allshopreturnstr = info.result;
								fillShopData(info.result);
							}
						}
					});
		} else {
			showToast("网络不可用");
		}
	}

	private void fillShopData(String resultJson) {
		ArrayList<ShopBean> shopList = gson.fromJson(resultJson,
				new TypeToken<List<ShopBean>>() {
				}.getType());
		System.out.println(shopList.size());
		lvShop.setAdapter(new ShopAdapter(activity, shopList));
	}

}
