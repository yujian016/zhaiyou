package com.ccc.www.activity;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.EntityStoreGoodsAdapter;
import com.ccc.www.bean.GoodsBean;
import com.ccc.www.bean.ShopBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.GlobalRequestCode;
import com.ccc.www.util.ImageLoaderOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyStoreActivity extends BaseActivity {

	private ImageButton ib_my_store_goback;
	private ImageView iv_my_store_log;
	private TextView tv_my_store_name;
	private TextView tv_my_store_info;
	private TextView tv_my_store_category;
	private GridView gv_my_store_goods;
	private TextView tv_goods_upload_goods;

	private HttpUtils http;
	Gson gson = null;
	private int userId = 0;
	private int shopId = 0;

	TextView edit_mystore;

	ShopBean shop = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mystore);
		initview();

		registerReceiver(reloadMyStoreAllGoods, new IntentFilter(
				GlobalConstant.ReloadMyStoreAllGoods));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_my_store_goback:
			finish();
			break;
		case R.id.tv_goods_upload_goods:
			Intent intent = new Intent(MyStoreActivity.this,
					UploadGoodsActivity.class);
			intent.putExtra("userId", userId);
			intent.putExtra("shopId", shopId);
			startActivityForResult(intent,
					GlobalRequestCode.ACTIVITY_STORE_UPLOAD_REQUEST_CODE);
			break;
		case R.id.edit_mystore:
			if (shop == null) {
				showToast("店铺信息未加载成功");
				return;
			}

			Intent edit_mystore = new Intent(MyStoreActivity.this,
					EditShopActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("shop", (Serializable) shop);
			edit_mystore.putExtras(bundle);
			edit_mystore.putExtra("shopId", shopId);
			startActivityForResult(edit_mystore,
					GlobalRequestCode.ACTIVITY_STORE_EDITINFO_REQUEST_CODE);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		getShopData(shopId);
		// 获取店铺商品信息
		getShopGoods(shopId);
	}

	private void getShopGoods(int shopid) {
		http = new HttpUtils(60 * 1000);
		RequestParams params = new RequestParams();
		params.addBodyParameter("shopId", String.valueOf(shopid));
		http.send(HttpMethod.POST, GlobalConstant.GOODS_ACTION_GET_GOODS,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String msg) {
						showToast("服务器正忙,请稍后再试");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String goodsJson = info.result;

						Log.v(TAG, "goodsJson  " + goodsJson);

						if (goodsJson.length() > 0) {
							dismissProgressDialog();
							gson = new Gson();
							ArrayList<GoodsBean> goodsList = gson.fromJson(
									goodsJson,
									new TypeToken<ArrayList<GoodsBean>>() {
									}.getType());
							gv_my_store_goods
									.setAdapter(new EntityStoreGoodsAdapter(
											MyStoreActivity.this, goodsList));
						} else {
							showToast("商店没有商品,可以进行上传");
						}
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void findviewWithId() {
		ib_my_store_goback = (ImageButton) findViewById(R.id.ib_my_store_goback);
		iv_my_store_log = (ImageView) findViewById(R.id.iv_my_store_log);
		tv_my_store_name = (TextView) findViewById(R.id.tv_my_store_name);
		tv_my_store_info = (TextView) findViewById(R.id.tv_my_store_info);
		tv_my_store_category = (TextView) findViewById(R.id.tv_my_store_category);
		gv_my_store_goods = (GridView) findViewById(R.id.gv_my_store_goods);
		tv_goods_upload_goods = (TextView) findViewById(R.id.tv_goods_upload_goods);
		edit_mystore = (TextView) findViewById(R.id.edit_mystore);
	}

	@Override
	public void initListener() {
		ib_my_store_goback.setOnClickListener(this);
		tv_goods_upload_goods.setOnClickListener(this);
		edit_mystore.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		Intent intent = getIntent();
		int shopId = intent.getIntExtra("shopId", 0);
		boolean isconnected = BaseUtils.isMobileConnected(MyStoreActivity.this);
		if (isconnected) {
			showLoading2("加载中.....");
			getShopData(shopId);
			getShopGoods(shopId);
		} else {
			showToast("网络不可用...");
		}

	}

	private void getShopData(int shopId) {
		RequestParams params = new RequestParams();
		params.addBodyParameter("shop_id", String.valueOf(shopId));
		http = new HttpUtils(1000 * 60);
		http.send(HttpMethod.POST, GlobalConstant.SHOP_ACTION_GET_MY_STORE,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						showToast(arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						System.out.println("my store:" + info.result);
						fillShopInfoData(info.result);
					}
				});
	}

	protected void fillShopInfoData(String jsonData) {
		gson = new Gson();

		shop = gson.fromJson(jsonData, ShopBean.class);

		ImageLoader.getInstance().displayImage(
				GlobalConstant.SERVER_URL + shop.getShop_log(),
				iv_my_store_log, ImageLoaderOption.getoption());

		tv_my_store_name.setText(shop.getShop_name());
		tv_my_store_category.setText("经营范围：" + shop.getCategory_name());
		userId = shop.getUser_id();
		shopId = shop.getId();

		tv_my_store_info.setText("店铺信息：" + shop.getShop_info());
	}

	ReloadMyStoreAllGoods reloadMyStoreAllGoods = new ReloadMyStoreAllGoods();

	class ReloadMyStoreAllGoods extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			getShopGoods(shopId);
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(reloadMyStoreAllGoods);
		super.onDestroy();
	}
	
	

}
