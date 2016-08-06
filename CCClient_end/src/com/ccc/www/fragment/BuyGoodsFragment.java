package com.ccc.www.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.SchoolBuyAdapter;
import com.ccc.www.bean.GoodsBean;
import com.ccc.www.bean.ShopCategoryBean;
import com.ccc.www.util.GlobalConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

@SuppressLint("ValidFragment")
public class BuyGoodsFragment extends BaseFragment {

	private Activity root_activity;
	private View root_view;
	private ListView lv_goods_fragment;

	private int type = 0;
	private ArrayList<ShopCategoryBean> cates;

	private HttpUtils http;
	private Gson gson;
	private TextView tv_goods_nogoods;

	public BuyGoodsFragment(ArrayList<ShopCategoryBean> cates, int index) {
		this.type = index;
		this.cates = cates;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		root_activity = getActivity();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (root_view == null) {
			root_view = inflater.inflate(R.layout.goods_fragment, container,
					false);
			initView();
			initData();
		}
		ViewGroup parent = (ViewGroup) root_view.getParent();
		if (parent != null) {
			parent.removeView(root_view);
		}
		return root_view;
	}

	private void initData() {
		fillDigitalData();
	}

	/**
	 * 获取数码分类数据
	 */
	private void fillDigitalData() {
		showLoading2("数据加载中...");
		http = new HttpUtils(60 * 1000);
		RequestParams params = new RequestParams();
		String cate_id = String.valueOf(cates.get(type).getId());
		params.addBodyParameter("goods_cate_id", cate_id);
		http.send(HttpMethod.POST, GlobalConstant.GOODS_ACTION_GET_CATE_GOODS,
				params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(root_activity, "服务器繁忙，稍后在试..",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						if (json.length() > 2) {
							gson = new Gson();
							ArrayList<GoodsBean> goods = gson.fromJson(json,
									new TypeToken<ArrayList<GoodsBean>>() {
									}.getType());
							lv_goods_fragment.setAdapter(new SchoolBuyAdapter(
									root_activity, goods
									 ));
							tv_goods_nogoods.setVisibility(View.GONE);
						} else {
							tv_goods_nogoods.setVisibility(View.VISIBLE);
						}
						dismissProgressDialog();
					}
				});
	}

	private void initView() {
		lv_goods_fragment = (ListView) root_view
				.findViewById(R.id.lv_goods_fragment);
		tv_goods_nogoods = (TextView) root_view
				.findViewById(R.id.tv_goods_nogoods);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void initListener() {

	}

}
