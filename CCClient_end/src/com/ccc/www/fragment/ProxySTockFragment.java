package com.ccc.www.fragment;

import java.util.ArrayList;
import java.util.List;

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
import com.ccc.www.adapter.ProxyStockAdapter;
import com.ccc.www.bean.SockBean;
import com.ccc.www.bean.SupMarketCategoryBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.ContextUtil;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.ccc.www.util.Xutils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ProxySTockFragment extends BaseFragment {

	String TAG = "SockFragment";

	private Activity activity;
	public View rootView;
	private ListView lvSockEr;

	private Gson gson;
	private int index = 0;
	private List<SupMarketCategoryBean> sockList;
	private TextView tv_no_sock;

	public ProxySTockFragment(int position, List<SupMarketCategoryBean> sockList) {
		this.index = position;
		this.sockList = sockList;
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
			rootView = inflater.inflate(R.layout.proxysock_fragment, container,
					false);
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
		lvSockEr = (ListView) rootView.findViewById(R.id.lv_sock_er);
		tv_no_sock = (TextView) rootView.findViewById(R.id.tv_no_sock);
	}

	public void initData() {
		fillSockData();
	}

	/**
	 * 根据分类ID获取超市商品
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
		params.addBodyParameter("goods_cate_id", sockcateid);
		Xutils.loadData(HttpMethod.POST, GlobalConstant.SUP_ACTION_GET_GOODS,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {

						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr  " + returnstr);

						if (TextUtils.isEmpty(returnstr)) {
							tv_no_sock.setVisibility(View.VISIBLE);
						} else {
							if (returnstr.equalsIgnoreCase("null")) {
								tv_no_sock.setVisibility(View.VISIBLE);
								return;
							}

							gson = new Gson();
							ArrayList<SockBean> sockList = gson.fromJson(
									returnstr,
									new TypeToken<ArrayList<SockBean>>() {
									}.getType());

							ContextUtil.setallSockBean(sockList);

							for (int i = 0; i < sockList.size(); i++) {
								sockList.get(i).setCount(0);
								// 更新本地购物车的商品信息
								DBUtil.updateProxyStockCartInfo(activity,
										sockList.get(i), sockList.get(i)
												.getId());
							}

							// 取出本地存储的购物车
							int userid = UserUtil.getuserid(activity);
							List<SockBean> allsock = DBUtil.getProxyStockCart(
									activity, userid);
							if (allsock.size() > 0) {
								for (int i = 0; i < allsock.size(); i++) {
									SockBean sock = allsock.get(i);
									for (int j = 0; j < sockList.size(); j++) {
										if (sockList.get(j).getId() == sock
												.getId()) {
											sockList.get(j).setCount(
													sock.getCount());
										}
									}
								}
							}

							lvSockEr.setAdapter(new ProxyStockAdapter(activity,
									sockList));
							tv_no_sock.setVisibility(View.GONE);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						Toast.makeText(activity, "服务器繁忙,稍后再试...",
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
