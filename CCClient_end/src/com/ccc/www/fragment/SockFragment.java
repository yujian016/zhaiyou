package com.ccc.www.fragment;

import java.util.ArrayList;

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
import com.ccc.www.adapter.SockAdapter;
import com.ccc.www.bean.SockBean;
import com.ccc.www.bean.SupMarketCategoryBean;
import com.ccc.www.util.BaseUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class SockFragment extends BaseFragment {

	String TAG = "SockFragment";

	private Activity activity;
	public View rootView;
	private ListView lvSockEr;

	private HttpUtils http;
	private Gson gson;
	private int index = 0;
	private ArrayList<SupMarketCategoryBean> sockList;
	private TextView tv_no_sock;

	public SockFragment(int position, ArrayList<SupMarketCategoryBean> sockList) {
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
			rootView = inflater.inflate(R.layout.sock_fragment, container,
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
		boolean connected = BaseUtils.isMobileConnected(activity);
		if (connected) {
			showLoading2("数据加载中....");
			http = new HttpUtils(60 * 1000);
			RequestParams params = new RequestParams();
			String sockcateid = String.valueOf(sockList.get(index).getId());
			params.addBodyParameter("sock_cate_id", sockcateid);
			http.send(HttpMethod.POST, "", params,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Toast.makeText(activity, "服务器繁忙,稍后再试...",
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							if (info.result.length() > 2) {
								String resultJson = info.result;
								gson = new Gson();
								ArrayList<SockBean> sockList = gson.fromJson(
										resultJson,
										new TypeToken<ArrayList<SockBean>>() {
										}.getType());
								lvSockEr.setAdapter(new SockAdapter(activity,
										sockList, R.layout.sock_lv_item));
								tv_no_sock.setVisibility(View.GONE);
							} else {
								tv_no_sock.setVisibility(View.VISIBLE);
							}
							dismissProgressDialog();
						}
					});
		} else {
			Toast.makeText(activity, "网络不可用...", Toast.LENGTH_SHORT).show();
		}
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
