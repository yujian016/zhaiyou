package com.ccc.www.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.ShopBean;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyOpenShopActivity extends BaseActivity {

	private ImageButton ib_my_open_shop_close;
	private ImageView iv_my_open_shop_logo;
	private TextView tv_my_open_shop_title;
	private TextView tv_my_open_shop_category;
	private TextView tv_my_open_shop_audit;

	HttpUtils http;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_open_shop);
		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_my_open_shop_close:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findviewWithId() {
		ib_my_open_shop_close = (ImageButton) findViewById(R.id.ib_my_open_shop_close);
		iv_my_open_shop_logo = (ImageView) findViewById(R.id.iv_my_open_shop_logo);
		tv_my_open_shop_title = (TextView) findViewById(R.id.tv_my_open_shop_title);
		tv_my_open_shop_category = (TextView) findViewById(R.id.tv_my_open_shop_category);
		tv_my_open_shop_audit = (TextView) findViewById(R.id.tv_my_open_shop_audit);

		initListener();
	}

	@Override
	public void initListener() {
		ib_my_open_shop_close.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		SharedPreferences sp = this.getSharedPreferences("login_session",
				Context.MODE_PRIVATE);
		String loginName = sp.getString("login_name", "");
		if (loginName.length() > 0) {
			int loginId = sp.getInt("login_id", 0);
			getMyShop(String.valueOf(loginId));
		}
	}

	private void getMyShop(String loginId) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("login_id", loginId);
		http = new HttpUtils();
		http.send(HttpMethod.POST, GlobalConstant.SHOP_ACTION_GET_MY_SHOP,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String msg) {
						Toast.makeText(MyOpenShopActivity.this, msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						Gson gson = new Gson();
						String json = info.result;
						System.out.println(json);
						ShopBean shop = gson.fromJson(json, ShopBean.class);

						ImageLoader.getInstance().displayImage(
								GlobalConstant.SERVER_URL + shop.getShop_log(),
								iv_my_open_shop_logo,
								ImageLoaderOption.getoption());
						tv_my_open_shop_title.setText(shop.getShop_name());
						tv_my_open_shop_category.setText("经营范围："
								+ shop.getCategory_name());
						tv_my_open_shop_audit.setText("审核进度：正在审核");
					}
				});
	}

}
