package com.ccc.www.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.ccc.www.adapter.MySuperMarketAdapter;
import com.ccc.www.bean.MySuperMarketGoodsBean;
import com.ccc.www.bean.SupMarketCategoryBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.ccc.www.util.Xutils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

@SuppressLint("ValidFragment")
public class MySuperMarketFragment extends BaseFragment {

	String TAG = "MySuperMarketFragment";

	private Activity activity;
	public View rootView;
	private ListView lvSockEr;

	private int index = 0;
	private List<SupMarketCategoryBean> sockList;
	private TextView tv_no_sock;

	public MySuperMarketFragment(int position,
			List<SupMarketCategoryBean> sockList) {
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
		params.addBodyParameter("cate_id", sockcateid);
		params.addBodyParameter("user_id", UserUtil.getuserid(activity) + "");
		Xutils.loadData(HttpMethod.POST, GlobalConstant.GET_MY_SUP_GOODS,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {

						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, response.result);
						if (TextUtils.isEmpty(returnstr)) {
							tv_no_sock.setVisibility(View.VISIBLE);
						} else {

							List<MySuperMarketGoodsBean> allMySuperMarketGoodsBean = new ArrayList<MySuperMarketGoodsBean>();
							try {
								JSONArray array = new JSONArray(returnstr);

								for (int i = 0; i < array.length(); i++) {
									JSONObject json = array.getJSONObject(i);

									int id = json.getInt("id");
									int supermaket_id = json
											.getInt("supermaket_id");
									String goods_name = json
											.getString("goods_name");
									float goods_price = (float) json
											.getDouble("goods_price");
									int goods_category_id = json
											.getInt("goods_category_id");
									String goods_log = json
											.getString("goods_log");
									int have_num = json.getInt("have_num");
									int goods_status = json
											.getInt("goods_status");

									MySuperMarketGoodsBean bean = new MySuperMarketGoodsBean(
											id, supermaket_id, goods_name,
											goods_price, goods_category_id,
											goods_log, have_num, goods_status);
									allMySuperMarketGoodsBean.add(bean);
								}

								lvSockEr.setAdapter(new MySuperMarketAdapter(
										rootView, activity,
										allMySuperMarketGoodsBean));
								tv_no_sock.setVisibility(View.GONE);
							} catch (JSONException e) {
								e.printStackTrace();
							}
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
