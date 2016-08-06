package com.ccc.www.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.PrivateSuperMarketAdapter;
import com.ccc.www.bean.PrivateSuperMarketGoodsBean;
import com.ccc.www.bean.SupMarketCategoryBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.Xutils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

@SuppressLint("ValidFragment")
public class PrivateSuperMarketFragment extends BaseFragment {

	String TAG = "PrivateSuperMarketFragment";

	private Activity activity;
	public View rootView;
	private ListView lv_private_supmarket_goods;

	private int index = 0;
	private int proxyshopid = 0;
	private List<SupMarketCategoryBean> sockList;
	private TextView tv_no_goods;

	public PrivateSuperMarketFragment(int position,
			List<SupMarketCategoryBean> sockList, int proxyshopid) {
		this.index = position;
		this.sockList = sockList;
		this.proxyshopid = proxyshopid;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		activity = getActivity();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.private_supmarket_fragment,
					container, false);
			initView();
			initData();
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	private void initView() {
		lv_private_supmarket_goods = (ListView) rootView
				.findViewById(R.id.lv_private_supmarket_goods);
		tv_no_goods = (TextView) rootView.findViewById(R.id.tv_no_goods);
	}

	public void initData() {
		fillSockData();
	}

	/**
	 * 根据分类ID、宿舍ID获取超市商品
	 */
	private void fillSockData() {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		String sockcateid = String.valueOf(sockList.get(index).getId());
		Log.v(TAG, sockcateid);
		params.addBodyParameter("cate_id", sockcateid);
		params.addBodyParameter("sup_id", proxyshopid + "");
		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.GET_MY_SUP_GOODS_FORM_PRIVATESUP, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {

						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, response.result);

						if (TextUtils.isEmpty(returnstr)) {
							tv_no_goods.setVisibility(View.VISIBLE);
						} else {
							List<PrivateSuperMarketGoodsBean> allMySuperMarketGoodsBean = new ArrayList<PrivateSuperMarketGoodsBean>();
							Gson gson = new Gson();
							allMySuperMarketGoodsBean = gson
									.fromJson(
											returnstr,
											new TypeToken<ArrayList<PrivateSuperMarketGoodsBean>>() {
											}.getType());

							if (allMySuperMarketGoodsBean == null) {
								allMySuperMarketGoodsBean = new ArrayList<PrivateSuperMarketGoodsBean>();
							}

							lv_private_supmarket_goods
									.setAdapter(new PrivateSuperMarketAdapter(
											activity, allMySuperMarketGoodsBean));
							tv_no_goods.setVisibility(View.GONE);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {

						Log.v(TAG, "onFailure  " + arg1);

						dismissProgressDialog();
						Toast.makeText(activity, "服务器繁忙,稍后再试",
								Toast.LENGTH_SHORT).show();
					}
				});

	}

	@Override
	public void onClick(View arg0) {

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}
}
