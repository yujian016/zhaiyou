package com.ccc.www.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.activity.ExpressActivity;
import com.ccc.www.bean.MyConsumeOrder;
import com.ccc.www.bean.MySchoolBuyOrderBean;
import com.ccc.www.bean.PointOrderBean;
import com.ccc.www.bean.ProxyGoodsBean;
import com.ccc.www.bean.SecondHandOrderBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.CheckUtil;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.ccc.www.util.XListViewUtil;
import com.ccc.www.util.Xutils;
import com.ccc.www.view.NoScrollListView;
import com.ccc.www.view.XListView;
import com.ccc.www.view.XListView.IXListViewListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.utils.Log;

public class Order_Fragment1 extends BaseFragment {

	protected static final String TAG = "Order_Fragment1";
	View rootView;
	Activity activity;

	TextView fragment_order1_myorder1;
	TextView fragment_order1_myorder2;
	// 店铺购物
	TextView fragment_order1_myorder3;
	// 潮流数码订单
	TextView fragment_order1_myorder4;
	// 二手交易订单
	TextView fragment_order1_myorder5;
	// 积分商城订单
	TextView fragment_order1_myorder6;
	// 校园购物订单
	TextView fragment_order1_myorder7;

	XListView order_fragment_order1_xlistview1;
	XListView order_fragment_order1_xlistview2;
	XListView order_fragment_order1_xlistview3;
	XListView order_fragment_order1_xlistview4;
	XListView order_fragment_order1_xlistview5;
	XListView order_fragment_order1_xlistview6;
	XListView order_fragment_order1_xlistview7;

	// 0 正在发货了 1 已经发货
	List<ProxyGoodsBean> allProxyGoodsBean = new ArrayList<ProxyGoodsBean>();
	// 私人超市订单
	List<PrivateSM> allMyConsumeOrder = new ArrayList<PrivateSM>();

	class PrivateSM {
		boolean isexp;
		List<MyConsumeOrder> allorerbean;
	}

	// 店铺购物订单
	List<MySchoolBuyOrder> allMySchoolBuyOrderBean = new ArrayList<MySchoolBuyOrder>();

	// 校园购物订单
	List<MySchoolBuyOrder> allMySSOrderBean = new ArrayList<MySchoolBuyOrder>();

	class MySchoolBuyOrder {
		boolean isexp;
		List<MySchoolBuyOrderBean> allorerbean;
	}

	// 潮流数码订单
	List<DiditalOrderBean> allDigitalOrderBean = new ArrayList<DiditalOrderBean>();

	class DiditalOrderBean {
		boolean isexp;
		List<MySchoolBuyOrderBean> allorerbean;
	}

	// 二手交易订单
	List<SecondHandOrderBean> allSecondHandOrderBean = new ArrayList<SecondHandOrderBean>();

	// 积分商城订单
	List<PointOrderBean> allPointOrderBean = new ArrayList<PointOrderBean>();

	Adapter1 adapter1 = new Adapter1();
	Adapter2 adapter2 = new Adapter2();
	Adapter3 adapter3 = new Adapter3();
	Adapter4 adapter4 = new Adapter4();
	Adapter5 adapter5 = new Adapter5();
	Adapter6 adapter6 = new Adapter6();
	Adapter7 adapter7 = new Adapter7();

	int userid = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		activity = getActivity();
		userid = UserUtil.getuserid(activity);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_order1, container,
					false);
			initview();
			initListener();
			initdata();
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	private void initview() {
		fragment_order1_myorder1 = (TextView) rootView
				.findViewById(R.id.fragment_order1_myorder1);
		fragment_order1_myorder2 = (TextView) rootView
				.findViewById(R.id.fragment_order1_myorder2);
		fragment_order1_myorder3 = (TextView) rootView
				.findViewById(R.id.fragment_order1_myorder3);
		fragment_order1_myorder4 = (TextView) rootView
				.findViewById(R.id.fragment_order1_myorder4);
		fragment_order1_myorder5 = (TextView) rootView
				.findViewById(R.id.fragment_order1_myorder5);
		fragment_order1_myorder6 = (TextView) rootView
				.findViewById(R.id.fragment_order1_myorder6);
		fragment_order1_myorder7 = (TextView) rootView
				.findViewById(R.id.fragment_order1_myorder7);

		order_fragment_order1_xlistview1 = (XListView) rootView
				.findViewById(R.id.order_fragment_order1_xlistview1);
		order_fragment_order1_xlistview2 = (XListView) rootView
				.findViewById(R.id.order_fragment_order1_xlistview2);
		order_fragment_order1_xlistview3 = (XListView) rootView
				.findViewById(R.id.order_fragment_order1_xlistview3);
		order_fragment_order1_xlistview4 = (XListView) rootView
				.findViewById(R.id.order_fragment_order1_xlistview4);
		order_fragment_order1_xlistview5 = (XListView) rootView
				.findViewById(R.id.order_fragment_order1_xlistview5);
		order_fragment_order1_xlistview6 = (XListView) rootView
				.findViewById(R.id.order_fragment_order1_xlistview6);
		order_fragment_order1_xlistview7 = (XListView) rootView
				.findViewById(R.id.order_fragment_order1_xlistview7);

		order_fragment_order1_xlistview1.setPullLoadEnable(false);
		order_fragment_order1_xlistview1.setPullRefreshEnable(true);
		order_fragment_order1_xlistview1
				.setXListViewListener(new IXListViewListener() {
					@Override
					public void onRefresh() {
						loadMyConsumeOrder();
					}

					@Override
					public void onLoadMore() {
						// TODO Auto-generated method stub
						XListViewUtil.endload(order_fragment_order1_xlistview1);
					}
				});
		order_fragment_order1_xlistview1.setAdapter(adapter1);

		order_fragment_order1_xlistview2.setPullLoadEnable(false);
		order_fragment_order1_xlistview2.setPullRefreshEnable(true);
		order_fragment_order1_xlistview2
				.setXListViewListener(new IXListViewListener() {
					@Override
					public void onRefresh() {
						loadProxyStockOrder();
					}

					@Override
					public void onLoadMore() {
						// TODO Auto-generated method stub
						XListViewUtil.endload(order_fragment_order1_xlistview2);
					}
				});
		order_fragment_order1_xlistview2.setAdapter(adapter2);

		// 店铺购物订单
		order_fragment_order1_xlistview3.setPullLoadEnable(false);
		order_fragment_order1_xlistview3.setPullRefreshEnable(true);
		order_fragment_order1_xlistview3
				.setXListViewListener(new IXListViewListener() {
					@Override
					public void onRefresh() {
						loadSchoolbuyOrder();
					}

					@Override
					public void onLoadMore() {
						// TODO Auto-generated method stub
						XListViewUtil.endload(order_fragment_order1_xlistview3);
					}
				});
		order_fragment_order1_xlistview3.setAdapter(adapter3);

		// 潮流数码订单
		order_fragment_order1_xlistview4.setPullLoadEnable(false);
		order_fragment_order1_xlistview4.setPullRefreshEnable(true);
		order_fragment_order1_xlistview4
				.setXListViewListener(new IXListViewListener() {
					@Override
					public void onRefresh() {
						loadDigitalOrder();
					}

					@Override
					public void onLoadMore() {
						// TODO Auto-generated method stub
						XListViewUtil.endload(order_fragment_order1_xlistview4);
					}
				});
		order_fragment_order1_xlistview4.setAdapter(adapter4);

		// 二手交易订单
		order_fragment_order1_xlistview5.setPullLoadEnable(false);
		order_fragment_order1_xlistview5.setPullRefreshEnable(true);
		order_fragment_order1_xlistview5
				.setXListViewListener(new IXListViewListener() {
					@Override
					public void onRefresh() {
						loadSecondOrder();
					}

					@Override
					public void onLoadMore() {
						// TODO Auto-generated method stub
						XListViewUtil.endload(order_fragment_order1_xlistview5);
					}
				});
		order_fragment_order1_xlistview5.setAdapter(adapter5);

		// 积分商城订单
		order_fragment_order1_xlistview6.setPullLoadEnable(false);
		order_fragment_order1_xlistview6.setPullRefreshEnable(true);
		order_fragment_order1_xlistview6
				.setXListViewListener(new IXListViewListener() {
					@Override
					public void onRefresh() {
						loadPointOrder();
					}

					@Override
					public void onLoadMore() {
						XListViewUtil.endload(order_fragment_order1_xlistview6);
					}
				});
		order_fragment_order1_xlistview6.setAdapter(adapter6);

		// 积分商城订单
		order_fragment_order1_xlistview7.setPullLoadEnable(false);
		order_fragment_order1_xlistview7.setPullRefreshEnable(true);
		order_fragment_order1_xlistview7
				.setXListViewListener(new IXListViewListener() {
					@Override
					public void onRefresh() {
						loadSSOrder();
					}

					@Override
					public void onLoadMore() {
						XListViewUtil.endload(order_fragment_order1_xlistview7);
					}
				});
		order_fragment_order1_xlistview7.setAdapter(adapter7);

	}

	private void initdata() {
		loadMyConsumeOrder();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_order1_myorder1:
			fragment_order1_myorder1.setTextColor(getResources().getColor(
					R.color.white));
			fragment_order1_myorder1.setBackgroundColor(getResources()
					.getColor(R.color.topbar_bg));

			fragment_order1_myorder2.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder2.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder3.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder3.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder4.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder4.setBackgroundColor(getResources()
					.getColor(R.color.white));
			fragment_order1_myorder5.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder5.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder6.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder6.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder7.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder7.setBackgroundColor(getResources()
					.getColor(R.color.white));

			order_fragment_order1_xlistview1.setVisibility(View.VISIBLE);
			order_fragment_order1_xlistview2.setVisibility(View.GONE);
			order_fragment_order1_xlistview3.setVisibility(View.GONE);
			order_fragment_order1_xlistview4.setVisibility(View.GONE);
			order_fragment_order1_xlistview5.setVisibility(View.GONE);
			order_fragment_order1_xlistview6.setVisibility(View.GONE);
			order_fragment_order1_xlistview7.setVisibility(View.GONE);

			loadMyConsumeOrder();

			break;
		case R.id.fragment_order1_myorder2:
			fragment_order1_myorder2.setTextColor(getResources().getColor(
					R.color.white));
			fragment_order1_myorder2.setBackgroundColor(getResources()
					.getColor(R.color.topbar_bg));

			fragment_order1_myorder1.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder1.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder3.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder3.setBackgroundColor(getResources()
					.getColor(R.color.white));
			fragment_order1_myorder4.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder4.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder5.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder5.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder6.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder6.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder7.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder7.setBackgroundColor(getResources()
					.getColor(R.color.white));

			Log.v(TAG, "fragment_order1_myorder2  ");

			order_fragment_order1_xlistview1.setVisibility(View.GONE);
			order_fragment_order1_xlistview2.setVisibility(View.VISIBLE);
			order_fragment_order1_xlistview3.setVisibility(View.GONE);
			order_fragment_order1_xlistview4.setVisibility(View.GONE);
			order_fragment_order1_xlistview5.setVisibility(View.GONE);
			order_fragment_order1_xlistview6.setVisibility(View.GONE);
			order_fragment_order1_xlistview7.setVisibility(View.GONE);

			loadProxyStockOrder();

			break;

		case R.id.fragment_order1_myorder3:
			fragment_order1_myorder3.setTextColor(getResources().getColor(
					R.color.white));
			fragment_order1_myorder3.setBackgroundColor(getResources()
					.getColor(R.color.topbar_bg));

			fragment_order1_myorder1.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder1.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder2.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder2.setBackgroundColor(getResources()
					.getColor(R.color.white));
			fragment_order1_myorder4.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder4.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder5.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder5.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder6.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder6.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder7.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder7.setBackgroundColor(getResources()
					.getColor(R.color.white));

			order_fragment_order1_xlistview1.setVisibility(View.GONE);
			order_fragment_order1_xlistview2.setVisibility(View.GONE);
			order_fragment_order1_xlistview3.setVisibility(View.VISIBLE);
			order_fragment_order1_xlistview4.setVisibility(View.GONE);
			order_fragment_order1_xlistview5.setVisibility(View.GONE);
			order_fragment_order1_xlistview6.setVisibility(View.GONE);
			order_fragment_order1_xlistview7.setVisibility(View.GONE);

			loadSchoolbuyOrder();

			break;

		case R.id.fragment_order1_myorder4:
			fragment_order1_myorder3.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder3.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder1.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder1.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder2.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder2.setBackgroundColor(getResources()
					.getColor(R.color.white));
			fragment_order1_myorder4.setTextColor(getResources().getColor(
					R.color.white));
			fragment_order1_myorder4.setBackgroundColor(getResources()
					.getColor(R.color.topbar_bg));

			fragment_order1_myorder5.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder5.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder6.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder6.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder7.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder7.setBackgroundColor(getResources()
					.getColor(R.color.white));

			order_fragment_order1_xlistview1.setVisibility(View.GONE);
			order_fragment_order1_xlistview2.setVisibility(View.GONE);
			order_fragment_order1_xlistview3.setVisibility(View.GONE);
			order_fragment_order1_xlistview4.setVisibility(View.VISIBLE);
			order_fragment_order1_xlistview5.setVisibility(View.GONE);
			order_fragment_order1_xlistview6.setVisibility(View.GONE);
			order_fragment_order1_xlistview7.setVisibility(View.GONE);

			loadDigitalOrder();

			break;

		case R.id.fragment_order1_myorder5:
			fragment_order1_myorder3.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder3.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder1.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder1.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder2.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder2.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder4.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder4.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder5.setTextColor(getResources().getColor(
					R.color.white));
			fragment_order1_myorder5.setBackgroundColor(getResources()
					.getColor(R.color.topbar_bg));

			fragment_order1_myorder6.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder6.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder7.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder7.setBackgroundColor(getResources()
					.getColor(R.color.white));

			order_fragment_order1_xlistview1.setVisibility(View.GONE);
			order_fragment_order1_xlistview2.setVisibility(View.GONE);
			order_fragment_order1_xlistview3.setVisibility(View.GONE);
			order_fragment_order1_xlistview4.setVisibility(View.GONE);
			order_fragment_order1_xlistview5.setVisibility(View.VISIBLE);
			order_fragment_order1_xlistview6.setVisibility(View.GONE);
			order_fragment_order1_xlistview7.setVisibility(View.GONE);

			loadSecondOrder();

			break;

		case R.id.fragment_order1_myorder6:

			fragment_order1_myorder3.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder3.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder1.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder1.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder2.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder2.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder4.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder4.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder5.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder5.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder6.setTextColor(getResources().getColor(
					R.color.white));
			fragment_order1_myorder6.setBackgroundColor(getResources()
					.getColor(R.color.topbar_bg));

			fragment_order1_myorder7.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder7.setBackgroundColor(getResources()
					.getColor(R.color.white));

			order_fragment_order1_xlistview1.setVisibility(View.GONE);
			order_fragment_order1_xlistview2.setVisibility(View.GONE);
			order_fragment_order1_xlistview3.setVisibility(View.GONE);
			order_fragment_order1_xlistview4.setVisibility(View.GONE);
			order_fragment_order1_xlistview5.setVisibility(View.GONE);
			order_fragment_order1_xlistview6.setVisibility(View.VISIBLE);
			order_fragment_order1_xlistview7.setVisibility(View.GONE);

			loadPointOrder();

			break;

		case R.id.fragment_order1_myorder7:

			fragment_order1_myorder3.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder3.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder1.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder1.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder2.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder2.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder4.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder4.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder5.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder5.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder6.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order1_myorder6.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order1_myorder7.setTextColor(getResources().getColor(
					R.color.white));
			fragment_order1_myorder7.setBackgroundColor(getResources()
					.getColor(R.color.topbar_bg));

			order_fragment_order1_xlistview1.setVisibility(View.GONE);
			order_fragment_order1_xlistview2.setVisibility(View.GONE);
			order_fragment_order1_xlistview3.setVisibility(View.GONE);
			order_fragment_order1_xlistview4.setVisibility(View.GONE);
			order_fragment_order1_xlistview5.setVisibility(View.GONE);
			order_fragment_order1_xlistview6.setVisibility(View.GONE);
			order_fragment_order1_xlistview7.setVisibility(View.VISIBLE);

			loadSSOrder();

			break;

		default:
			break;
		}

	}

	/**
	 * 载入私人超市订单
	 */
	private void loadMyConsumeOrder() {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", userid + "");

		Xutils.loadData(HttpMethod.POST, GlobalConstant.PRIVATE_SUP_ORDER,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr " + response.result);
						if (TextUtils.isEmpty(returnstr)) {
							allMyConsumeOrder.removeAll(allMyConsumeOrder);
							adapter1.notifyDataSetChanged();
							XListViewUtil
									.endload(order_fragment_order1_xlistview1);
						} else {
							List<MyConsumeOrder> tempallMyConsumeOrder = new ArrayList<MyConsumeOrder>();

							if (returnstr.contains("]-[")) {

								int position = returnstr.indexOf("]-[");
								String sockstr = returnstr.substring(0,
										position + 1);
								String orderstr = returnstr.substring(
										position + 2, returnstr.length());

								Log.v(TAG, "sockstr  " + sockstr);
								Log.v(TAG, "orderstr  " + orderstr);

								try {

									JSONArray array = new JSONArray(orderstr);

									JSONArray arraysock = new JSONArray(sockstr);

									for (int i = 0; i < array.length(); i++) {
										JSONObject json = array
												.getJSONObject(i);

										JSONObject sock = arraysock
												.getJSONObject(i);

										String sock_name = "";
										if (sock.has("goods_name")) {
											sock_name = sock
													.getString("goods_name");
										} else if (sock.has("sock_name")) {
											sock_name = sock
													.getString("sock_name");
										}

										String sock_log = "";
										if (sock.has("goods_log")) {
											sock_log = sock
													.getString("goods_log");
										} else if (sock.has("sock_log")) {
											sock_log = sock
													.getString("sock_log");
										}

										float goods_price = (float) sock
												.getDouble("goods_price");

										int id = json.getInt("id");
										String order_no = json
												.getString("order_no");
										int goods_id = json.getInt("goods_id");
										int goods_number = json
												.getInt("goods_number");
										String pay_time = json
												.getString("pay_time");
										double order_sum_money = json
												.getDouble("order_sum_money");
										int status = json.getInt("status");

										int supermaket_id = 0;
										if (json.has("supermaket_id")) {
											supermaket_id = json
													.getInt("supermaket_id");
										}
										int shop_id = 0;
										if (json.has("shop_id")) {
											shop_id = json.getInt("shop_id");
										}

										MyConsumeOrder order = new MyConsumeOrder(
												sock_name, sock_log, id,
												goods_id, goods_number,
												pay_time, order_sum_money,
												status, supermaket_id, shop_id);
										order.setOrder_no(order_no);

										order.setGoods_price(goods_price);

										tempallMyConsumeOrder.add(order);
									}

									Map<String, List<MyConsumeOrder>> map = new HashMap<String, List<MyConsumeOrder>>();
									for (int i = 0; i < tempallMyConsumeOrder
											.size(); i++) {
										MyConsumeOrder bean = tempallMyConsumeOrder
												.get(i);
										String orderno = bean.getOrder_no();

										if (map.containsKey(orderno)) {
											List<MyConsumeOrder> tempOrder = map
													.get(orderno);
											tempOrder.add(bean);
											map.put(orderno, tempOrder);
										} else {
											List<MyConsumeOrder> tempOrder = new ArrayList<MyConsumeOrder>();
											tempOrder.add(bean);
											map.put(orderno, tempOrder);
										}
									}

									Log.v(TAG, "map  " + map.size());

									// 第四种
									System.out
											.println("通过Map.values()遍历所有的value，但不能遍历key");

									allMyConsumeOrder
											.removeAll(allMyConsumeOrder);
									for (List<MyConsumeOrder> v : map.values()) {
										Log.v(TAG, "v  " + v.size());

										PrivateSM order = new PrivateSM();
										order.isexp = false;
										order.allorerbean = v;

										allMyConsumeOrder.add(order);
									}
									adapter1.notifyDataSetChanged();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							XListViewUtil
									.endload(order_fragment_order1_xlistview1);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
						XListViewUtil.endload(order_fragment_order1_xlistview1);
					}
				});

	}

	/**
	 * 载入代理进货订单
	 */
	private void loadProxyStockOrder() {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", userid + "");

		Log.v(TAG, "userid  " + userid);

		Xutils.loadData(HttpMethod.POST, GlobalConstant.GET_PROXY_GOODS,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, response.result);
						if (TextUtils.isEmpty(returnstr)) {
							allProxyGoodsBean.removeAll(allProxyGoodsBean);
							adapter2.notifyDataSetChanged();
							XListViewUtil
									.endload(order_fragment_order1_xlistview2);
						} else {
							List<ProxyGoodsBean> tempProxyGoodsBean = new ArrayList<ProxyGoodsBean>();
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

									ProxyGoodsBean bean = new ProxyGoodsBean(
											id, supermaket_id, goods_name,
											goods_price, goods_category_id,
											goods_log, have_num, goods_status);

									tempProxyGoodsBean.add(bean);
								}
								allProxyGoodsBean.removeAll(allProxyGoodsBean);
								allProxyGoodsBean.addAll(tempProxyGoodsBean);
								adapter2.notifyDataSetChanged();
							} catch (JSONException e) {
								e.printStackTrace();
							}
							XListViewUtil
									.endload(order_fragment_order1_xlistview2);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
						XListViewUtil.endload(order_fragment_order1_xlistview2);
					}
				});

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListener() {
		fragment_order1_myorder1.setOnClickListener(this);
		fragment_order1_myorder2.setOnClickListener(this);
		fragment_order1_myorder3.setOnClickListener(this);
		fragment_order1_myorder4.setOnClickListener(this);
		fragment_order1_myorder5.setOnClickListener(this);
		fragment_order1_myorder6.setOnClickListener(this);
		fragment_order1_myorder7.setOnClickListener(this);
	}

	class Adapter1 extends BaseAdapter {

		public int getCount() {
			// TODO Auto-generated method stub
			return allMyConsumeOrder.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allMyConsumeOrder.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View view, ViewGroup arg2) {

			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(activity).inflate(
						R.layout.item_myorder_private_sm, null);

				holder.item_myorder_orderno = (TextView) view
						.findViewById(R.id.item_myorder_orderno);
				holder.item_myorder_price = (TextView) view
						.findViewById(R.id.item_myorder_price);
				holder.item_myorder_paytime = (TextView) view
						.findViewById(R.id.item_myorder_paytime);
				holder.item_myorder_receivegoods = (TextView) view
						.findViewById(R.id.item_myorder_receivegoods);
				holder.item_myorder_goodslistview = (NoScrollListView) view
						.findViewById(R.id.item_myorder_goodslistview);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final MyConsumeOrder bean = allMyConsumeOrder.get(position).allorerbean
					.get(0);

			holder.item_myorder_orderno.setText("订单编号：" + bean.getOrder_no());

			// float allmoney = 0.0f;
			// for (int i = 0; i < allMyConsumeOrder.get(position).allorerbean
			// .size(); i++) {
			// allmoney = (float) (allmoney +
			// allMyConsumeOrder.get(position).allorerbean
			// .get(i).getGoods_number()
			// * allMyConsumeOrder.get(position).allorerbean.get(i)
			// .getGoods_price());
			// }

			holder.item_myorder_price.setText("￥" + bean.getOrder_sum_money());
			holder.item_myorder_paytime.setText("订单时间：" + bean.getPay_time());

			holder.item_myorder_goodslistview.setAdapter(new AdapterSMGoods(
					allMyConsumeOrder.get(position).allorerbean));
			if (allMyConsumeOrder.get(position).isexp) {
				holder.item_myorder_goodslistview.setVisibility(View.VISIBLE);
			} else {
				holder.item_myorder_goodslistview.setVisibility(View.GONE);
			}

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					boolean isexp = allMyConsumeOrder.get(position).isexp;
					if (isexp) {
						for (int i = 0; i < allMyConsumeOrder.size(); i++) {
							allMyConsumeOrder.get(i).isexp = false;
						}
						allMyConsumeOrder.get(position).isexp = false;
					} else {
						for (int i = 0; i < allMyConsumeOrder.size(); i++) {
							allMyConsumeOrder.get(i).isexp = false;
						}
						allMyConsumeOrder.get(position).isexp = true;
					}
					notifyDataSetChanged();
				}
			});

			int status = bean.getStatus();
			if (status == 1) {
				holder.item_myorder_receivegoods.setVisibility(View.VISIBLE);
				holder.item_myorder_receivegoods.setText("确认收货");
				holder.item_myorder_receivegoods
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {

								AlertDialog.Builder build = new Builder(
										activity);
								build.setTitle("提示");
								build.setMessage("您确认收货吗？");
								build.setPositiveButton("确认",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												myconsumeconfirmReceiving(
														Adapter1.this, bean);
											}
										});
								build.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
											}
										});
								build.show();

							}
						});
			} else if (status == 0) {
				holder.item_myorder_receivegoods.setVisibility(View.VISIBLE);
				holder.item_myorder_receivegoods.setText("取消订单");

				holder.item_myorder_receivegoods
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {

								AlertDialog.Builder build = new Builder(
										activity);
								build.setTitle("提示");
								build.setMessage("您确定要取消订单吗？");
								build.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												myconsumecancelorder(
														Adapter1.this, bean);
											}
										});
								build.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
											}
										});
								build.show();

							}
						});
			} else if (status == 4) {
				holder.item_myorder_receivegoods.setVisibility(View.VISIBLE);
				holder.item_myorder_receivegoods.setText("订单已取消");
				holder.item_myorder_receivegoods.setOnClickListener(null);
			}

			// ViewHolder holder = null;
			// if (view == null) {
			// holder = new ViewHolder();
			// view = LayoutInflater.from(activity).inflate(
			// R.layout.item_shoporder, null);
			//
			// holder.item_shoporder_img = (ImageView) view
			// .findViewById(R.id.item_shoporder_img);
			// holder.item_shoporder_name = (TextView) view
			// .findViewById(R.id.item_shoporder_name);
			// holder.item_shoporder_goodsstatus = (TextView) view
			// .findViewById(R.id.item_shoporder_goodsstatus);
			// holder.item_shoporder_price = (TextView) view
			// .findViewById(R.id.item_shoporder_price);
			// holder.item_shoporder_btn = (TextView) view
			// .findViewById(R.id.item_shoporder_btn);
			//
			// view.setTag(holder);
			// } else {
			// holder = (ViewHolder) view.getTag();
			// }
			//
			//
			//
			//
			//
			// ImageLoader.getInstance().displayImage(bean.getSock_log(),
			// holder.item_shoporder_img);
			//
			// holder.item_shoporder_price
			// .setText("￥" + bean.getOrder_sum_money());
			//
			// holder.item_shoporder_name.setText(bean.getSock_name());
			//
			// int status = bean.getStatus();
			// // 0->未发货
			// // 1->待收货
			// if (status == 0) {
			// holder.item_shoporder_goodsstatus.setText("待发货");
			// holder.item_shoporder_btn.setVisibility(View.GONE);
			// } else if (status == 1) {
			// holder.item_shoporder_btn.setVisibility(View.VISIBLE);
			//
			// holder.item_shoporder_goodsstatus.setText("待收货");
			//
			// holder.item_shoporder_btn.setText("确认收货");
			// holder.item_shoporder_btn
			// .setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View arg0) {
			// AlertDialog.Builder build = new Builder(
			// activity);
			// build.setTitle("提示");
			// build.setMessage("您确认收货吗？");
			// build.setPositiveButton("确认",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(
			// DialogInterface arg0,
			// int arg1) {
			// myconsumeconfirmReceiving(
			// Adapter1.this, bean);
			// }
			// });
			// build.setNegativeButton("取消",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(
			// DialogInterface arg0,
			// int arg1) {
			// }
			// });
			// build.show();
			// }
			// });
			// } else if (status == 2) {
			// holder.item_shoporder_goodsstatus.setText("已确认收货");
			// holder.item_shoporder_btn.setVisibility(View.GONE);
			// } else {
			// holder.item_shoporder_btn.setVisibility(View.GONE);
			// }
			return view;
		}

		class ViewHolder {
			TextView item_myorder_orderno;
			TextView item_myorder_price;
			TextView item_myorder_paytime;
			TextView item_myorder_receivegoods;
			NoScrollListView item_myorder_goodslistview;
		}
	}

	/**
	 * 私人超市商品适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class AdapterSMGoods extends BaseAdapter {

		List<MyConsumeOrder> allMyConsumeOrder = new ArrayList<MyConsumeOrder>();

		public AdapterSMGoods(List<MyConsumeOrder> allMyConsumeOrder) {
			this.allMyConsumeOrder = allMyConsumeOrder;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allMyConsumeOrder.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allMyConsumeOrder.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(activity).inflate(
						R.layout.item_privatesm_ordergoods, null);

				holder.item_shoporder_img = (ImageView) view
						.findViewById(R.id.item_shoporder_img);
				holder.item_shoporder_name = (TextView) view
						.findViewById(R.id.item_shoporder_name);
				holder.item_shoporder_goodsstatus = (TextView) view
						.findViewById(R.id.item_shoporder_goodsstatus);
				holder.item_shoporder_price = (TextView) view
						.findViewById(R.id.item_shoporder_price);
				// holder.item_shoporder_btn = (TextView) view
				// .findViewById(R.id.item_shoporder_btn);

				holder.item_shoporder_kdnolayout = (LinearLayout) view
						.findViewById(R.id.item_shoporder_kdnolayout);
				holder.item_shoporder_kdno = (TextView) view
						.findViewById(R.id.item_shoporder_kdno);
				holder.item_shoporder_kdcompanylayout = (LinearLayout) view
						.findViewById(R.id.item_shoporder_kdcompanylayout);
				holder.item_shoporder_kdcompany = (TextView) view
						.findViewById(R.id.item_shoporder_kdcompany);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final MyConsumeOrder bean = allMyConsumeOrder.get(position);

			String log = bean.getSock_log();
			if (!log.startsWith("http")) {
				log = GlobalConstant.SERVER_URL + log;
			}
			ImageLoader.getInstance().displayImage(log,
					holder.item_shoporder_img, ImageLoaderOption.getoption());

			holder.item_shoporder_price.setText("￥" + bean.getGoods_price()
					+ " X " + bean.getGoods_number());

			holder.item_shoporder_name.setText(bean.getSock_name());

			final String kdno = "";
			final String kdcompany = "";

			if (TextUtils.isEmpty(kdno) || TextUtils.isEmpty(kdcompany)) {
				holder.item_shoporder_kdnolayout.setVisibility(View.GONE);
				holder.item_shoporder_kdcompanylayout.setVisibility(View.GONE);
			} else {
				holder.item_shoporder_kdnolayout.setVisibility(View.VISIBLE);
				holder.item_shoporder_kdcompanylayout
						.setVisibility(View.VISIBLE);

				holder.item_shoporder_kdno.setText(kdno);
				holder.item_shoporder_kdcompany.setText(kdcompany);
				holder.item_shoporder_kdno
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Intent intent = new Intent();
								intent.setClass(activity, ExpressActivity.class);
								intent.putExtra("kdno", kdno);
								intent.putExtra("kdcompany", kdcompany);
								startActivity(intent);
							}
						});
			}

			int status = bean.getStatus();
			// 0->未发货
			// 1->待收货
			if (status == 0) {
				holder.item_shoporder_goodsstatus.setText("待发货");
				// holder.item_shoporder_btn.setVisibility(View.GONE);
			} else if (status == 1) {
				// holder.item_shoporder_btn.setVisibility(View.VISIBLE);

				holder.item_shoporder_goodsstatus.setText("待收货");

				// holder.item_shoporder_btn.setText("确认收货");
				// holder.item_shoporder_btn
				// .setOnClickListener(new OnClickListener() {
				// @Override
				// public void onClick(View arg0) {
				// AlertDialog.Builder build = new Builder(
				// activity);
				// build.setTitle("提示");
				// build.setMessage("您确认收货吗？");
				// build.setPositiveButton("确认",
				// new DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(
				// DialogInterface arg0,
				// int arg1) {
				// // myconsumeconfirmReceiving(
				// // Adapter1.this, bean);
				// }
				// });
				// build.setNegativeButton("取消",
				// new DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(
				// DialogInterface arg0,
				// int arg1) {
				// }
				// });
				// build.show();
				// }
				// });
			} else if (status == 2) {
				holder.item_shoporder_goodsstatus.setText("已确认收货");
				// holder.item_shoporder_btn.setVisibility(View.GONE);
			} else if (status == 3) {
				holder.item_shoporder_goodsstatus.setText("已确认收货");
				// holder.item_shoporder_btn.setVisibility(View.GONE);
			} else {
				holder.item_shoporder_goodsstatus.setVisibility(View.VISIBLE);
				holder.item_shoporder_goodsstatus.setText("已取消");
			}
			return view;
		}

		class ViewHolder {
			ImageView item_shoporder_img;
			TextView item_shoporder_name;
			TextView item_shoporder_goodsstatus;
			TextView item_shoporder_price;
			// TextView item_shoporder_btn;

			LinearLayout item_shoporder_kdnolayout;
			TextView item_shoporder_kdno;
			LinearLayout item_shoporder_kdcompanylayout;
			TextView item_shoporder_kdcompany;
		}
	}

	/**
	 * 私人超市订单，取消订单
	 */
	private void myconsumecancelorder(final Adapter1 adapter1,
			final MyConsumeOrder bean) {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在取消订单");

		String order_no = bean.getOrder_no();

		RequestParams params = new RequestParams();
		params.addBodyParameter("order_no", order_no);

		Log.v(TAG, "order_no " + order_no);

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.PRIVATE_SUP_ORDER_CancelOrder, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr取消订单	" + response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("取消订单失败");
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);
								String code = json.getString("code");
								String msg = json.getString("msg");

								int codeint = Integer.parseInt(code);
								if (codeint > 0) {
									for (int i = 0; i < allMyConsumeOrder
											.size(); i++) {
										if (bean.getOrder_no()
												.equalsIgnoreCase(
														allMyConsumeOrder
																.get(i).allorerbean
																.get(0)
																.getOrder_no())) {
											for (int j = 0; j < allMyConsumeOrder
													.get(i).allorerbean.size(); j++) {
												allMyConsumeOrder.get(i).allorerbean
														.get(j).setStatus(4);
											}
											break;
										}
									}
									adapter1.notifyDataSetChanged();
								}
								showToast(msg);
							} catch (JSONException e) {
								e.printStackTrace();
								showToast("取消订单失败");
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("取消订单失败");

						Log.v(TAG, "returnstr取消订单	" + arg1);
					}
				});

	}

	/**
	 * 私人超市订单，确认收货
	 */
	private void myconsumeconfirmReceiving(final Adapter1 adapter1,
			final MyConsumeOrder bean) {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在确认收货");

		String order_no = bean.getOrder_no();

		RequestParams params = new RequestParams();
		params.addBodyParameter("order_no", order_no);

		Log.v(TAG, "order_no " + order_no);

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.PRIVATE_SUP_ORDER_ReceiveGoods, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("确认收货失败");
						} else {
							// {"resultCode":1,"beanId":1,"resultMsg":"已确认收货"}
							try {
								JSONObject json = new JSONObject(returnstr);
								String code = json.getString("code");
								String msg = json.getString("msg");
								if (code.equalsIgnoreCase("1")) {

									for (int i = 0; i < allMyConsumeOrder
											.size(); i++) {
										if (bean.getOrder_no()
												.equalsIgnoreCase(
														allMyConsumeOrder
																.get(i).allorerbean
																.get(0)
																.getOrder_no())) {

											for (int j = 0; j < allMyConsumeOrder
													.get(i).allorerbean.size(); j++) {
												allMyConsumeOrder.get(i).allorerbean
														.get(j).setStatus(2);
											}
											break;
										}
									}

									adapter1.notifyDataSetChanged();

									// allMyConsumeOrder.get(0).allorerbean.get(0).
									// JpushAddMsg(userid, t_uid, msg_title,
									// msg_context);

									// popevaluate(bean);
								}
								showToast(msg);
							} catch (JSONException e) {
								e.printStackTrace();
								showToast("确认收货失败");
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("确认收货失败");
					}
				});
	}

	/**
	 * 私人超市订单弹出评价
	 */
	private void popevaluate(final MyConsumeOrder bean) {
		View view1 = LayoutInflater.from(activity).inflate(
				R.layout.pop_evaluate, null);
		mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);

		// final RadioButton pop_evaluate_good = (RadioButton) view1
		// .findViewById(R.id.pop_evaluate_good);
		// final RadioButton pop_evaluate_bad = (RadioButton) view1
		// .findViewById(R.id.pop_evaluate_bad);

		TextView pop_evaluate_cancel = (TextView) view1
				.findViewById(R.id.pop_evaluate_cancel);
		TextView pop_evaluate_submit = (TextView) view1
				.findViewById(R.id.pop_evaluate_submit);

		pop_evaluate_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dissmisspopwindow();

				submitevaluate(bean, "好评", 0, false);

			}
		});
		pop_evaluate_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int evastatus = 0;
				// 评价修改
				// submitevaluate(bean, content, evastatus, true);
			}
		});
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
		mPopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
	}

	/**
	 * 我的消费订单评价
	 * 
	 * @author Administrator
	 */
	private void submitevaluate(MyConsumeOrder bean, String content,
			int evastatus, final boolean issubmit) {
		if (!BaseUtils.isNetWork(activity)) {
			if (issubmit) {
				showToast("请检查您的网络");
			}
			return;
		}

		if (issubmit) {
			showLoading2("正在提交评价");
		}
		RequestParams params = new RequestParams();

		int supermaket_id = bean.getSupermaket_id();
		if (supermaket_id == 0) {
			// 校园购物
			params.addBodyParameter("handle_type", "2");
			params.addBodyParameter("shop_id", "" + bean.getShop_id());
			params.addBodyParameter("goods_id", "" + bean.getGoods_id());

			Log.v(TAG, "handle_type  2");
			Log.v(TAG, "shop_id   " + bean.getShop_id());
			Log.v(TAG, "goods_id  " + bean.getGoods_id());

		} else {
			params.addBodyParameter("handle_type", "1");
			params.addBodyParameter("supermaket_id",
					"" + bean.getSupermaket_id());
			params.addBodyParameter("goods_id", "" + bean.getGoods_id());

			Log.v(TAG, "handle_type  1");
			Log.v(TAG, "supermaket_id   " + bean.getSupermaket_id());
			Log.v(TAG, "goods_id  " + bean.getGoods_id());

		}
		params.addBodyParameter("comment_type", "" + evastatus);
		params.addBodyParameter("comment_context", content);
		params.addBodyParameter("handle_id", String.valueOf(bean.getId()));

		Log.v(TAG, "comment_type  " + evastatus);
		Log.v(TAG, "comment_context  " + content);

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.PRIVATE_ORDER_ADDCOMMENT, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr  " + response.result);
						if (TextUtils.isEmpty(returnstr)) {
							if (issubmit) {
								showToast("提交失败");
							}
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);
								String code = json.getString("code");
								String msg = json.getString("msg");
								if (code.equalsIgnoreCase("1")) {
									dissmisspopwindow();
									loadMyConsumeOrder();
								}
								if (issubmit) {
									showToast(msg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
								if (issubmit) {
									showToast("提交失败");
								}
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						if (issubmit) {
							showToast("提交失败");
						}
					}
				});

	}

	/*** 代理进货 ***/
	class Adapter2 extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allProxyGoodsBean.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allProxyGoodsBean.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(activity).inflate(
						R.layout.item_shoporder, null);

				holder.item_shoporder_img = (ImageView) view
						.findViewById(R.id.item_shoporder_img);
				holder.item_shoporder_name = (TextView) view
						.findViewById(R.id.item_shoporder_name);
				holder.item_shoporder_goodsstatus = (TextView) view
						.findViewById(R.id.item_shoporder_goodsstatus);
				holder.item_shoporder_price = (TextView) view
						.findViewById(R.id.item_shoporder_price);
				holder.item_shoporder_btn = (TextView) view
						.findViewById(R.id.item_shoporder_btn);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final ProxyGoodsBean bean = allProxyGoodsBean.get(position);

			ImageLoader.getInstance().displayImage(bean.getGoods_log(),
					holder.item_shoporder_img, ImageLoaderOption.getoption());

			holder.item_shoporder_price.setText("￥" + bean.getGoods_price()
					* bean.getHave_num());

			holder.item_shoporder_name.setText(bean.getGoods_name());

			int status = bean.getGoods_status();
			// 0->未发货
			// 1->待收货
			if (status == 0) {
				holder.item_shoporder_goodsstatus.setText("待发货");
				holder.item_shoporder_btn.setVisibility(View.GONE);
			} else if (status == 1) {
				holder.item_shoporder_btn.setVisibility(View.VISIBLE);

				holder.item_shoporder_goodsstatus.setText("待收货");

				holder.item_shoporder_btn.setText("确认收货");
				holder.item_shoporder_btn
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								AlertDialog.Builder build = new Builder(
										activity);
								build.setTitle("提示");
								build.setMessage("您确认收货吗？");
								build.setPositiveButton("确认",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												confirmReceiving(bean);
											}
										});
								build.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
											}
										});
								build.show();
							}
						});
			} else if (status == 2) {

				holder.item_shoporder_goodsstatus.setText("已确认收货");

				holder.item_shoporder_btn.setVisibility(View.VISIBLE);
				holder.item_shoporder_btn.setText("上架");
				holder.item_shoporder_btn
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								popChangePrice(bean);
							}
						});
			} else {
				holder.item_shoporder_btn.setVisibility(View.GONE);
			}

			return view;
		}

		class ViewHolder {
			ImageView item_shoporder_img;
			TextView item_shoporder_name;
			TextView item_shoporder_goodsstatus;
			TextView item_shoporder_price;
			TextView item_shoporder_btn;
		}
	}

	/**
	 * 代理进货确认收货
	 */
	private void confirmReceiving(final ProxyGoodsBean bean) {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在确认收货");

		RequestParams params = new RequestParams();
		params.addBodyParameter("goods_id", bean.getId() + "");

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.UPDATE_PROXY_GOODS_CONFIRM, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("确认收货失败");
						} else {
							// {"resultCode":1,"beanId":1,"resultMsg":"已确认收货"}
							try {
								JSONObject json = new JSONObject(returnstr);
								int resultCode = json.getInt("resultCode");
								String resultMsg = json.getString("resultMsg");
								if (resultCode == 1) {
									bean.setGoods_status(2);
									adapter2.notifyDataSetChanged();
								}

								showToast(resultMsg);
							} catch (JSONException e) {
								e.printStackTrace();
								showToast("确认收货失败");
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("确认收货失败");
					}
				});
	}

	/**
	 * 确认上架
	 */
	private void confirmPutaway(final ProxyGoodsBean bean, String priceStr) {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在上架");

		RequestParams params = new RequestParams();
		params.addBodyParameter("goods_id", bean.getId() + "");
		params.addBodyParameter("new_price", priceStr);

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.UPDATE_PROXY_GOODS_TOSUP, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("上架失败");
						} else {
							// {"resultCode":1,"beanId":1,"resultMsg":"上架成功"}
							try {
								JSONObject json = new JSONObject(returnstr);
								int resultCode = json.getInt("resultCode");
								String resultMsg = json.getString("resultMsg");
								if (resultCode == 1) {
									bean.setGoods_status(2);
									allProxyGoodsBean.remove(bean);
									adapter2.notifyDataSetChanged();

									dissmisspopwindow();

								}
								showToast(resultMsg);
							} catch (JSONException e) {
								e.printStackTrace();
								showToast("上架失败");
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("上架失败");
					}
				});
	}

	PopupWindow mPopupWindow;

	private void dissmisspopwindow() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

	/**
	 * 弹出修改价格
	 * 
	 */
	private void popChangePrice(final ProxyGoodsBean bean) {

		View view1 = LayoutInflater.from(activity).inflate(
				R.layout.pop_putaway_changeprice, null);
		mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);

		final EditText pop_putaway_changeprice_newprice = (EditText) view1
				.findViewById(R.id.pop_putaway_changeprice_newprice);

		TextView pop_putaway_changeprice_cancel = (TextView) view1
				.findViewById(R.id.pop_putaway_changeprice_cancel);
		TextView pop_putaway_changeprice_confirm = (TextView) view1
				.findViewById(R.id.pop_putaway_changeprice_confirm);

		pop_putaway_changeprice_newprice.setText(bean.getGoods_price() + "");

		pop_putaway_changeprice_cancel
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dissmisspopwindow();
					}
				});
		pop_putaway_changeprice_confirm
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						final String newprice = pop_putaway_changeprice_newprice
								.getText().toString().trim();
						if (TextUtils.isEmpty(newprice)) {
							showToast("请输入价格");
							return;
						}
						boolean ischeckok = CheckUtil.isIntOrFloat(newprice);
						if (!ischeckok) {
							showToast("请输入整数或小数");
							return;
						}

						AlertDialog.Builder build = new Builder(activity);
						build.setTitle("提示");
						build.setMessage("您确认上架吗？");
						build.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										confirmPutaway(bean, newprice);
									}
								});
						build.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
									}
								});
						build.show();

					}
				});
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
		mPopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
	}

	/****************** 店铺购物 ******************/

	private void loadSchoolbuyOrder() {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", userid + "");

		Xutils.loadData(HttpMethod.POST, GlobalConstant.GET_SCHOOLBUY_ORDER,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr " + response.result);
						if (TextUtils.isEmpty(returnstr)) {
							allMySchoolBuyOrderBean
									.removeAll(allMySchoolBuyOrderBean);
							adapter3.notifyDataSetChanged();
							XListViewUtil
									.endload(order_fragment_order1_xlistview3);
						} else {
							List<MySchoolBuyOrderBean> tempallMySchoolBuyOrderBean = new ArrayList<MySchoolBuyOrderBean>();

							if (returnstr.contains("]-[")) {

								int position = returnstr.indexOf("]-[");
								String orderstr = returnstr.substring(0,
										position + 1);
								String goodsstr = returnstr.substring(
										position + 2, returnstr.length());

								Log.v(TAG, "orderstr  " + orderstr);
								Log.v(TAG, "goodsstr  " + goodsstr);

								try {

									JSONArray array = new JSONArray(orderstr);

									JSONArray arraygood = new JSONArray(
											goodsstr);

									for (int i = 0; i < array.length(); i++) {
										JSONObject order = array
												.getJSONObject(i);
										JSONObject goods = arraygood
												.getJSONObject(i);

										int id = order.getInt("id");
										String order_no = order
												.getString("order_no");
										int buy_user_id = order
												.getInt("buy_user_id");
										int shop_id = order.getInt("shop_id");
										String get_goods_person_name = order
												.getString("get_goods_person_name");
										String get_goods_person_phone = order
												.getString("get_goods_person_phone");
										String get_goods_person_address = order
												.getString("get_goods_person_address");
										double order_sum_money = order
												.getDouble("order_sum_money");
										int goods_id = order.getInt("goods_id");
										int goods_cate_id = order
												.getInt("goods_cate_id");
										int goods_number = order
												.getInt("goods_number");
										String pay_time = order
												.getString("pay_time");
										int status = order.getInt("status");

										String goods_name = goods
												.getString("goods_name");
										String goods_detail = goods
												.getString("goods_detail");
										String goods_kd1 = goods
												.getString("goods_kd1");
										String goods_kd2 = goods
												.getString("goods_kd2");
										String goods_log1 = goods
												.getString("goods_log1");
										String goods_log2 = goods
												.getString("goods_log2");
										String goods_log3 = goods
												.getString("goods_log3");
										int goods_status = goods
												.getInt("goods_status");

										double goods_price = goods
												.getDouble("goods_price");

										MySchoolBuyOrderBean bean = new MySchoolBuyOrderBean(
												id, order_no, buy_user_id,
												shop_id, get_goods_person_name,
												get_goods_person_phone,
												get_goods_person_address,
												order_sum_money, goods_id,
												goods_cate_id, goods_number,
												pay_time, status, goods_name,
												goods_detail, goods_kd1,
												goods_kd2, goods_log1,
												goods_log2, goods_log3,
												goods_status, goods_price);
										tempallMySchoolBuyOrderBean.add(bean);
									}
									Map<String, List<MySchoolBuyOrderBean>> map = new HashMap<String, List<MySchoolBuyOrderBean>>();
									for (int i = 0; i < tempallMySchoolBuyOrderBean
											.size(); i++) {
										MySchoolBuyOrderBean bean = tempallMySchoolBuyOrderBean
												.get(i);
										String orderno = bean.getOrder_no();

										if (map.containsKey(orderno)) {
											List<MySchoolBuyOrderBean> tempOrder = map
													.get(orderno);
											tempOrder.add(bean);
											map.put(orderno, tempOrder);
										} else {
											List<MySchoolBuyOrderBean> tempOrder = new ArrayList<MySchoolBuyOrderBean>();
											tempOrder.add(bean);
											map.put(orderno, tempOrder);
										}
									}

									Log.v(TAG, "map  " + map.size());

									// 第四种
									System.out
											.println("通过Map.values()遍历所有的value，但不能遍历key");

									allMySchoolBuyOrderBean
											.removeAll(allMySchoolBuyOrderBean);
									for (List<MySchoolBuyOrderBean> v : map
											.values()) {
										Log.v(TAG, "v  " + v.size());

										MySchoolBuyOrder order = new MySchoolBuyOrder();
										order.isexp = false;
										order.allorerbean = v;

										allMySchoolBuyOrderBean.add(order);
									}

									adapter3.notifyDataSetChanged();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							XListViewUtil
									.endload(order_fragment_order1_xlistview3);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
						XListViewUtil.endload(order_fragment_order1_xlistview3);
					}
				});

	}

	class Adapter3 extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allMySchoolBuyOrderBean.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allMySchoolBuyOrderBean.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(activity).inflate(
						R.layout.item_myorder, null);

				holder.item_myorder_orderno = (TextView) view
						.findViewById(R.id.item_myorder_orderno);
				holder.item_myorder_price = (TextView) view
						.findViewById(R.id.item_myorder_price);
				holder.item_myorder_paytime = (TextView) view
						.findViewById(R.id.item_myorder_paytime);
				holder.item_myorder_goodslistview = (NoScrollListView) view
						.findViewById(R.id.item_myorder_goodslistview);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final MySchoolBuyOrderBean bean = allMySchoolBuyOrderBean
					.get(position).allorerbean.get(0);

			holder.item_myorder_orderno.setText("订单编号：" + bean.getOrder_no());
			holder.item_myorder_price.setText("￥" + bean.getOrder_sum_money());
			holder.item_myorder_paytime.setText("订单时间：" + bean.getPay_time());

			holder.item_myorder_goodslistview
					.setAdapter(new AdapterSchoolBuyGoods(
							allMySchoolBuyOrderBean.get(position).allorerbean));
			if (allMySchoolBuyOrderBean.get(position).isexp) {
				holder.item_myorder_goodslistview.setVisibility(View.VISIBLE);
			} else {
				holder.item_myorder_goodslistview.setVisibility(View.GONE);
			}

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					boolean isexp = allMySchoolBuyOrderBean.get(position).isexp;
					if (isexp) {
						for (int i = 0; i < allMySchoolBuyOrderBean.size(); i++) {
							allMySchoolBuyOrderBean.get(i).isexp = false;
						}
						allMySchoolBuyOrderBean.get(position).isexp = false;
					} else {
						for (int i = 0; i < allMySchoolBuyOrderBean.size(); i++) {
							allMySchoolBuyOrderBean.get(i).isexp = false;
						}
						allMySchoolBuyOrderBean.get(position).isexp = true;
					}
					notifyDataSetChanged();
				}
			});

			return view;
		}

		class ViewHolder {
			TextView item_myorder_orderno;
			TextView item_myorder_price;
			TextView item_myorder_paytime;
			NoScrollListView item_myorder_goodslistview;
		}
	}

	/**
	 * 获取校园购物订单
	 */
	private void loadSSOrder() {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", userid + "");

		Xutils.loadData(HttpMethod.POST, GlobalConstant.GET_SS_ORDER, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr " + response.result);
						if (TextUtils.isEmpty(returnstr)) {
							allMySSOrderBean.removeAll(allMySSOrderBean);
							adapter7.notifyDataSetChanged();
							XListViewUtil
									.endload(order_fragment_order1_xlistview7);
						} else {
							List<MySchoolBuyOrderBean> tempallMySchoolBuyOrderBean = new ArrayList<MySchoolBuyOrderBean>();

							if (returnstr.contains("]-[")) {

								int position = returnstr.indexOf("]-[");
								String orderstr = returnstr.substring(0,
										position + 1);
								String goodsstr = returnstr.substring(
										position + 2, returnstr.length());

								Log.v(TAG, "orderstr  " + orderstr);
								Log.v(TAG, "goodsstr  " + goodsstr);

								try {

									JSONArray array = new JSONArray(orderstr);

									JSONArray arraygood = new JSONArray(
											goodsstr);

									for (int i = 0; i < array.length(); i++) {
										JSONObject order = array
												.getJSONObject(i);
										JSONObject goods = arraygood
												.getJSONObject(i);

										int id = order.getInt("id");
										String order_no = order
												.getString("order_no");
										int buy_user_id = order
												.getInt("buy_user_id");
										int shop_id = order.getInt("shop_id");
										String get_goods_person_name = order
												.getString("get_goods_person_name");
										String get_goods_person_phone = order
												.getString("get_goods_person_phone");
										String get_goods_person_address = order
												.getString("get_goods_person_address");
										double order_sum_money = order
												.getDouble("order_sum_money");
										int goods_id = order.getInt("goods_id");
										int goods_cate_id = order
												.getInt("goods_cate_id");
										int goods_number = order
												.getInt("goods_number");
										String pay_time = order
												.getString("pay_time");
										int status = order.getInt("status");

										String kd_no = "";
										if (order.has("kd_no")) {
											kd_no = order.getString("kd_no");
										}

										String kd_company = "";
										if (order.has("kd_company")) {
											kd_company = order
													.getString("kd_company");
										}
										String goods_name = goods
												.getString("goods_name");
										String goods_detail = goods
												.getString("goods_detail");
										String goods_kd1 = goods
												.getString("goods_kd1");
										String goods_kd2 = goods
												.getString("goods_kd2");
										String goods_log1 = goods
												.getString("goods_log1");
										String goods_log2 = goods
												.getString("goods_log2");
										String goods_log3 = goods
												.getString("goods_log3");
										int goods_status = goods
												.getInt("goods_status");

										double goods_price = goods
												.getDouble("goods_price");

										MySchoolBuyOrderBean bean = new MySchoolBuyOrderBean(
												id, order_no, buy_user_id,
												shop_id, get_goods_person_name,
												get_goods_person_phone,
												get_goods_person_address,
												order_sum_money, goods_id,
												goods_cate_id, goods_number,
												pay_time, status, goods_name,
												goods_detail, goods_kd1,
												goods_kd2, goods_log1,
												goods_log2, goods_log3,
												goods_status, goods_price);

										bean.setKd_no(kd_no);
										bean.setKd_company(kd_company);

										tempallMySchoolBuyOrderBean.add(bean);
									}
									Map<String, List<MySchoolBuyOrderBean>> map = new HashMap<String, List<MySchoolBuyOrderBean>>();
									for (int i = 0; i < tempallMySchoolBuyOrderBean
											.size(); i++) {
										MySchoolBuyOrderBean bean = tempallMySchoolBuyOrderBean
												.get(i);
										String orderno = bean.getOrder_no();

										if (map.containsKey(orderno)) {
											List<MySchoolBuyOrderBean> tempOrder = map
													.get(orderno);
											tempOrder.add(bean);
											map.put(orderno, tempOrder);
										} else {
											List<MySchoolBuyOrderBean> tempOrder = new ArrayList<MySchoolBuyOrderBean>();
											tempOrder.add(bean);
											map.put(orderno, tempOrder);
										}
									}

									Log.v(TAG, "map  " + map.size());

									// 第四种
									System.out
											.println("通过Map.values()遍历所有的value，但不能遍历key");

									allMySSOrderBean
											.removeAll(allMySSOrderBean);
									for (List<MySchoolBuyOrderBean> v : map
											.values()) {
										Log.v(TAG, "v  " + v.size());

										MySchoolBuyOrder order = new MySchoolBuyOrder();
										order.isexp = false;
										order.allorerbean = v;

										allMySSOrderBean.add(order);
									}
									adapter7.notifyDataSetChanged();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							XListViewUtil
									.endload(order_fragment_order1_xlistview7);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
						XListViewUtil.endload(order_fragment_order1_xlistview7);
					}
				});
	}

	class Adapter7 extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allMySSOrderBean.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allMySSOrderBean.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(activity).inflate(
						R.layout.item_myorder, null);

				holder.item_myorder_orderno = (TextView) view
						.findViewById(R.id.item_myorder_orderno);
				holder.item_myorder_price = (TextView) view
						.findViewById(R.id.item_myorder_price);
				holder.item_myorder_paytime = (TextView) view
						.findViewById(R.id.item_myorder_paytime);
				holder.item_myorder_goodslistview = (NoScrollListView) view
						.findViewById(R.id.item_myorder_goodslistview);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final MySchoolBuyOrderBean bean = allMySSOrderBean.get(position).allorerbean
					.get(0);

			holder.item_myorder_orderno.setText("订单编号：" + bean.getOrder_no());

			float allmoney = 0.0f;
			for (int i = 0; i < allMySSOrderBean.get(position).allorerbean
					.size(); i++) {
				allmoney = (float) (allmoney + allMySSOrderBean.get(position).allorerbean
						.get(i).getGoods_number()
						* allMySSOrderBean.get(position).allorerbean.get(i)
								.getGoods_price());
			}

			holder.item_myorder_price.setText("￥" + allmoney);
			holder.item_myorder_paytime.setText("订单时间：" + bean.getPay_time());

			holder.item_myorder_goodslistview.setAdapter(new AdapterSSGoods(
					allMySSOrderBean.get(position).allorerbean));
			if (allMySSOrderBean.get(position).isexp) {
				holder.item_myorder_goodslistview.setVisibility(View.VISIBLE);
			} else {
				holder.item_myorder_goodslistview.setVisibility(View.GONE);
			}

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					boolean isexp = allMySSOrderBean.get(position).isexp;
					if (isexp) {
						for (int i = 0; i < allMySSOrderBean.size(); i++) {
							allMySSOrderBean.get(i).isexp = false;
						}
						allMySSOrderBean.get(position).isexp = false;
					} else {
						for (int i = 0; i < allMySSOrderBean.size(); i++) {
							allMySSOrderBean.get(i).isexp = false;
						}
						allMySSOrderBean.get(position).isexp = true;
					}
					notifyDataSetChanged();
				}
			});

			return view;
		}

		class ViewHolder {
			TextView item_myorder_orderno;
			TextView item_myorder_price;
			TextView item_myorder_paytime;
			NoScrollListView item_myorder_goodslistview;
		}
	}

	/**
	 * 校园购物商品适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class AdapterSSGoods extends BaseAdapter {

		List<MySchoolBuyOrderBean> allMySchoolBuyOrderBean = new ArrayList<MySchoolBuyOrderBean>();

		public AdapterSSGoods(List<MySchoolBuyOrderBean> allMySchoolBuyOrderBean) {
			this.allMySchoolBuyOrderBean = allMySchoolBuyOrderBean;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allMySchoolBuyOrderBean.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allMySchoolBuyOrderBean.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(activity).inflate(
						R.layout.item_schoolbuyordergoods, null);

				holder.item_shoporder_img = (ImageView) view
						.findViewById(R.id.item_shoporder_img);
				holder.item_shoporder_name = (TextView) view
						.findViewById(R.id.item_shoporder_name);
				holder.item_shoporder_goodsstatus = (TextView) view
						.findViewById(R.id.item_shoporder_goodsstatus);
				holder.item_shoporder_price = (TextView) view
						.findViewById(R.id.item_shoporder_price);
				holder.item_shoporder_btn = (TextView) view
						.findViewById(R.id.item_shoporder_btn);

				holder.item_shoporder_kdnolayout = (LinearLayout) view
						.findViewById(R.id.item_shoporder_kdnolayout);
				holder.item_shoporder_kdno = (TextView) view
						.findViewById(R.id.item_shoporder_kdno);
				holder.item_shoporder_kdcompanylayout = (LinearLayout) view
						.findViewById(R.id.item_shoporder_kdcompanylayout);
				holder.item_shoporder_kdcompany = (TextView) view
						.findViewById(R.id.item_shoporder_kdcompany);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final MySchoolBuyOrderBean bean = allMySchoolBuyOrderBean
					.get(position);

			String log = bean.getGoods_log1();
			if (!log.startsWith("http")) {
				log = GlobalConstant.SERVER_URL + log;
			}
			ImageLoader.getInstance().displayImage(log,
					holder.item_shoporder_img, ImageLoaderOption.getoption());

			holder.item_shoporder_price.setText("￥" + bean.getGoods_price()
					+ " X " + bean.getGoods_number());

			holder.item_shoporder_name.setText(bean.getGoods_name());

			final String kdno = bean.getKd_no();
			final String kdcompany = bean.getKd_company();

			if (TextUtils.isEmpty(kdno) || TextUtils.isEmpty(kdcompany)) {
				holder.item_shoporder_kdnolayout.setVisibility(View.GONE);
				holder.item_shoporder_kdcompanylayout.setVisibility(View.GONE);
			} else {
				holder.item_shoporder_kdnolayout.setVisibility(View.VISIBLE);
				holder.item_shoporder_kdcompanylayout
						.setVisibility(View.VISIBLE);

				holder.item_shoporder_kdno.setText(kdno);
				holder.item_shoporder_kdcompany.setText(kdcompany);

				holder.item_shoporder_kdno
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Intent intent = new Intent();
								intent.setClass(activity, ExpressActivity.class);
								intent.putExtra("kdno", kdno);
								intent.putExtra("kdcompany", kdcompany);
								startActivity(intent);
							}
						});
			}

			int status = bean.getStatus();
			// 0->未发货
			// 1->待收货
			if (status == 0) {
				holder.item_shoporder_goodsstatus.setText("待发货");
				holder.item_shoporder_btn.setVisibility(View.GONE);
			} else if (status == 1) {
				holder.item_shoporder_btn.setVisibility(View.VISIBLE);

				holder.item_shoporder_goodsstatus.setText("待收货");

				holder.item_shoporder_btn.setText("确认收货");
				holder.item_shoporder_btn
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								AlertDialog.Builder build = new Builder(
										activity);
								build.setTitle("提示");
								build.setMessage("您确认收货吗？");
								build.setPositiveButton("确认",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												SSConfirmReceiving(
														AdapterSSGoods.this,
														bean);
											}
										});
								build.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
											}
										});
								build.show();
							}
						});
			} else if (status == 2) {
				holder.item_shoporder_goodsstatus.setText("未评价");
				holder.item_shoporder_btn.setVisibility(View.VISIBLE);
				holder.item_shoporder_btn.setText("评价");
				holder.item_shoporder_btn
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								SSPopevaluate(bean);
							}
						});
			} else if (status == 3) {
				holder.item_shoporder_goodsstatus.setText("已评价");
				holder.item_shoporder_btn.setVisibility(View.GONE);
			} else {
				holder.item_shoporder_btn.setVisibility(View.GONE);
			}
			return view;
		}

		class ViewHolder {
			ImageView item_shoporder_img;
			TextView item_shoporder_name;
			TextView item_shoporder_goodsstatus;
			TextView item_shoporder_price;
			TextView item_shoporder_btn;

			LinearLayout item_shoporder_kdnolayout;
			TextView item_shoporder_kdno;
			LinearLayout item_shoporder_kdcompanylayout;
			TextView item_shoporder_kdcompany;
		}
	}

	/**
	 * 校园购物确认收货
	 */
	private void SSConfirmReceiving(final AdapterSSGoods adapterSSGoods,
			final MySchoolBuyOrderBean bean) {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在确认收货");

		RequestParams params = new RequestParams();
		params.addBodyParameter("order_id", bean.getId() + "");

		Xutils.loadData(HttpMethod.POST, GlobalConstant.SS_ORDER_RECEIVEGOODS,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {

						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr " + response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("确认收货失败");
						} else {
							// {"resultCode":1,"beanId":1,"resultMsg":"已确认收货"}
							try {
								JSONObject json = new JSONObject(returnstr);
								String code = json.getString("code");
								String msg = json.getString("msg");
								showToast(msg);
								if (code.equalsIgnoreCase("1")) {
									for (int i = 0; i < allMySSOrderBean.size(); i++) {
										MySchoolBuyOrder mySchoolBuyOrder = allMySSOrderBean
												.get(i);
										List<MySchoolBuyOrderBean> allorerbean = mySchoolBuyOrder.allorerbean;
										for (int j = 0; j < allorerbean.size(); j++) {
											if (allorerbean.get(j).getId() == bean
													.getId()) {
												allorerbean.get(j).setStatus(2);
											}
										}
									}

									adapterSSGoods.notifyDataSetChanged();
								}
							} catch (JSONException e) {
								e.printStackTrace();
								showToast("确认收货失败");
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("确认收货失败");
						android.util.Log.v(TAG, "onFailure " + arg1);
					}
				});
	}

	/**
	 * 弹出校园购物评价
	 */
	private void SSPopevaluate(final MySchoolBuyOrderBean bean) {
		View view1 = LayoutInflater.from(activity).inflate(
				R.layout.pop_evaluate, null);
		mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);

		final RatingBar room_ratingbar = (RatingBar) view1
				.findViewById(R.id.room_ratingbar);
		TextView pop_evaluate_cancel = (TextView) view1
				.findViewById(R.id.pop_evaluate_cancel);
		TextView pop_evaluate_submit = (TextView) view1
				.findViewById(R.id.pop_evaluate_submit);

		pop_evaluate_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dissmisspopwindow();
			}
		});
		pop_evaluate_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				int comment_star_num = (int) room_ratingbar.getRating();

				Log.v(TAG, "comment_star_num  " + comment_star_num);

				int evastatus = 0;
				// 评价修改
				submitSSEvaluate(bean, comment_star_num, evastatus, true);
			}
		});
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
		mPopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
	}

	/**
	 * 校园购物评价
	 */
	private void submitSSEvaluate(MySchoolBuyOrderBean bean,
			int comment_star_num, int evastatus, final boolean issubmit) {
		if (!BaseUtils.isNetWork(activity)) {
			if (issubmit) {
				showToast("请检查您的网络");
			}
			return;
		}

		if (issubmit) {
			showLoading2("正在提交评价");
		}
		RequestParams params = new RequestParams();

		// 校园购物
		params.addBodyParameter("shop_id", "" + bean.getShop_id());
		params.addBodyParameter("goods_id", "" + bean.getGoods_id());

		Log.v(TAG, "shop_id   " + bean.getShop_id());
		Log.v(TAG, "goods_id  " + bean.getGoods_id());

		params.addBodyParameter("comment_star_num", "" + comment_star_num);
		params.addBodyParameter("comment_type", "" + evastatus);
		params.addBodyParameter("handle_id", String.valueOf(bean.getId()));
		params.addBodyParameter("shop_order_type", "1");
		params.addBodyParameter("handle_type", "2");

		Log.v(TAG, "comment_star_num  " + comment_star_num);
		Log.v(TAG, "comment_type  " + evastatus);
		Log.v(TAG, "handle_id  " + String.valueOf(bean.getId()));

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.PRIVATE_ORDER_ADDCOMMENT, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr  " + response.result);
						if (TextUtils.isEmpty(returnstr)) {
							if (issubmit) {
								showToast("提交失败");
							}
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);
								String code = json.getString("code");
								String msg = json.getString("msg");
								if (code.equalsIgnoreCase("1")) {
									dissmisspopwindow();
									loadSchoolbuyOrder();
								}
								if (issubmit) {
									showToast(msg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
								if (issubmit) {
									showToast("提交失败");
								}
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();

						Log.v(TAG, "onFailure  " + arg1);

						if (issubmit) {
							showToast("提交失败");
						}
					}
				});
	}

	/**
	 * 店铺购物商品适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class AdapterSchoolBuyGoods extends BaseAdapter {

		List<MySchoolBuyOrderBean> allMySchoolBuyOrderBean = new ArrayList<MySchoolBuyOrderBean>();

		public AdapterSchoolBuyGoods(
				List<MySchoolBuyOrderBean> allMySchoolBuyOrderBean) {
			this.allMySchoolBuyOrderBean = allMySchoolBuyOrderBean;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allMySchoolBuyOrderBean.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allMySchoolBuyOrderBean.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(activity).inflate(
						R.layout.item_schoolbuyordergoods, null);

				holder.item_shoporder_img = (ImageView) view
						.findViewById(R.id.item_shoporder_img);
				holder.item_shoporder_name = (TextView) view
						.findViewById(R.id.item_shoporder_name);
				holder.item_shoporder_goodsstatus = (TextView) view
						.findViewById(R.id.item_shoporder_goodsstatus);
				holder.item_shoporder_price = (TextView) view
						.findViewById(R.id.item_shoporder_price);
				holder.item_shoporder_btn = (TextView) view
						.findViewById(R.id.item_shoporder_btn);

				holder.item_shoporder_kdnolayout = (LinearLayout) view
						.findViewById(R.id.item_shoporder_kdnolayout);
				holder.item_shoporder_kdno = (TextView) view
						.findViewById(R.id.item_shoporder_kdno);
				holder.item_shoporder_kdcompanylayout = (LinearLayout) view
						.findViewById(R.id.item_shoporder_kdcompanylayout);
				holder.item_shoporder_kdcompany = (TextView) view
						.findViewById(R.id.item_shoporder_kdcompany);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final MySchoolBuyOrderBean bean = allMySchoolBuyOrderBean
					.get(position);

			String log = bean.getGoods_log1();
			if (!log.startsWith("http")) {
				log = GlobalConstant.SERVER_URL + log;
			}
			ImageLoader.getInstance().displayImage(log,
					holder.item_shoporder_img, ImageLoaderOption.getoption());

			holder.item_shoporder_price.setText("￥" + bean.getGoods_price()
					+ " X " + bean.getGoods_number());

			holder.item_shoporder_name.setText(bean.getGoods_name());

			final String kdno = bean.getKd_no();
			final String kdcompany = bean.getKd_company();

			if (TextUtils.isEmpty(kdno) || TextUtils.isEmpty(kdcompany)) {
				holder.item_shoporder_kdnolayout.setVisibility(View.GONE);
				holder.item_shoporder_kdcompanylayout.setVisibility(View.GONE);
			} else {
				holder.item_shoporder_kdnolayout.setVisibility(View.VISIBLE);
				holder.item_shoporder_kdcompanylayout
						.setVisibility(View.VISIBLE);

				holder.item_shoporder_kdno.setText(kdno);
				holder.item_shoporder_kdcompany.setText(kdcompany);

				holder.item_shoporder_kdno
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Intent intent = new Intent();
								intent.setClass(activity, ExpressActivity.class);
								intent.putExtra("kdno", kdno);
								intent.putExtra("kdcompany", kdcompany);
								startActivity(intent);
							}
						});

			}

			int status = bean.getStatus();
			// 0->未发货
			// 1->待收货
			if (status == 0) {
				holder.item_shoporder_goodsstatus.setText("待发货");
				holder.item_shoporder_btn.setVisibility(View.GONE);
			} else if (status == 1) {
				holder.item_shoporder_btn.setVisibility(View.VISIBLE);

				holder.item_shoporder_goodsstatus.setText("待收货");

				holder.item_shoporder_btn.setText("确认收货");
				holder.item_shoporder_btn
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								AlertDialog.Builder build = new Builder(
										activity);
								build.setTitle("提示");
								build.setMessage("您确认收货吗？");
								build.setPositiveButton("确认",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {

												schoolBuyConfirmReceiving(
														AdapterSchoolBuyGoods.this,
														bean);
											}
										});
								build.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
											}
										});
								build.show();
							}
						});
			} else if (status == 2) {
				holder.item_shoporder_goodsstatus.setText("未评价");
				holder.item_shoporder_btn.setVisibility(View.VISIBLE);
				holder.item_shoporder_btn.setText("评价");
				holder.item_shoporder_btn
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								schoolBuyPopevaluate(bean);
							}
						});
			} else if (status == 3) {
				holder.item_shoporder_goodsstatus.setText("已评价");
				holder.item_shoporder_btn.setVisibility(View.GONE);
			} else {
				holder.item_shoporder_btn.setVisibility(View.GONE);
			}
			return view;
		}

		class ViewHolder {
			ImageView item_shoporder_img;
			TextView item_shoporder_name;
			TextView item_shoporder_goodsstatus;
			TextView item_shoporder_price;
			TextView item_shoporder_btn;

			LinearLayout item_shoporder_kdnolayout;
			TextView item_shoporder_kdno;
			LinearLayout item_shoporder_kdcompanylayout;
			TextView item_shoporder_kdcompany;
		}
	}

	/**
	 * 店铺购物确认收货
	 */
	private void schoolBuyConfirmReceiving(
			final AdapterSchoolBuyGoods adapterSchoolBuyGoods,
			final MySchoolBuyOrderBean bean) {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在确认收货");

		RequestParams params = new RequestParams();
		params.addBodyParameter("order_id", bean.getId() + "");

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.SCHOOLBUY_ORDER_RECEIVEGOODS, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {

						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr " + response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("确认收货失败");
						} else {
							// {"resultCode":1,"beanId":1,"resultMsg":"已确认收货"}
							try {
								JSONObject json = new JSONObject(returnstr);
								String code = json.getString("code");
								String msg = json.getString("msg");
								showToast(msg);
								if (code.equalsIgnoreCase("1")) {

									for (int i = 0; i < allMySchoolBuyOrderBean
											.size(); i++) {
										MySchoolBuyOrder mySchoolBuyOrder = allMySchoolBuyOrderBean
												.get(i);
										List<MySchoolBuyOrderBean> allorerbean = mySchoolBuyOrder.allorerbean;
										for (int j = 0; j < allorerbean.size(); j++) {
											if (allorerbean.get(j).getId() == bean
													.getId()) {
												allorerbean.get(j).setStatus(2);
											}
										}
									}

									adapterSchoolBuyGoods
											.notifyDataSetChanged();
								}
							} catch (JSONException e) {
								e.printStackTrace();
								showToast("确认收货失败");
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("确认收货失败");
						android.util.Log.v(TAG, "onFailure " + arg1);
					}
				});
	}

	/**
	 * 弹出店铺购物评价
	 */

	private void schoolBuyPopevaluate(final MySchoolBuyOrderBean bean) {
		View view1 = LayoutInflater.from(activity).inflate(
				R.layout.pop_evaluate, null);
		mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);

		final RatingBar room_ratingbar = (RatingBar) view1
				.findViewById(R.id.room_ratingbar);

		TextView pop_evaluate_cancel = (TextView) view1
				.findViewById(R.id.pop_evaluate_cancel);
		TextView pop_evaluate_submit = (TextView) view1
				.findViewById(R.id.pop_evaluate_submit);

		pop_evaluate_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dissmisspopwindow();
			}
		});
		pop_evaluate_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int evastatus = 0;
				// 评价修改
				int comment_star_num = (int) room_ratingbar.getRating();

				// 评价修改
				submitSchoolBuyEvaluate(bean, comment_star_num, evastatus, true);
			}
		});
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
		mPopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
	}

	/**
	 * 店铺购物评价
	 */
	private void submitSchoolBuyEvaluate(MySchoolBuyOrderBean bean,
			int comment_star_num, int evastatus, final boolean issubmit) {
		if (!BaseUtils.isNetWork(activity)) {
			if (issubmit) {
				showToast("请检查您的网络");
			}
			return;
		}

		if (issubmit) {
			showLoading2("正在提交评价");
		}
		RequestParams params = new RequestParams();

		// 校园购物
		params.addBodyParameter("handle_type", "2");
		params.addBodyParameter("shop_id", "" + bean.getShop_id());
		params.addBodyParameter("goods_id", "" + bean.getGoods_id());

		Log.v(TAG, "handle_type  2");
		Log.v(TAG, "shop_id   " + bean.getShop_id());
		Log.v(TAG, "goods_id  " + bean.getGoods_id());

		params.addBodyParameter("comment_type", "" + evastatus);
		params.addBodyParameter("comment_star_num", "" + comment_star_num);
		params.addBodyParameter("handle_id", String.valueOf(bean.getId()));
		params.addBodyParameter("shop_order_type", "2");

		Log.v(TAG, "comment_type  " + evastatus);

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.PRIVATE_ORDER_ADDCOMMENT, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr  " + response.result);
						if (TextUtils.isEmpty(returnstr)) {
							if (issubmit) {
								showToast("提交失败");
							}
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);
								String code = json.getString("code");
								String msg = json.getString("msg");
								if (code.equalsIgnoreCase("1")) {
									dissmisspopwindow();
									loadSchoolbuyOrder();
								}
								if (issubmit) {
									showToast(msg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
								if (issubmit) {
									showToast("提交失败");
								}
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						if (issubmit) {
							showToast("提交失败");
						}
					}
				});
	}

	/**
	 * 潮流数码
	 */

	private void loadDigitalOrder() {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", userid + "");

		Xutils.loadData(HttpMethod.POST, GlobalConstant.GET_DIGITAL_ORDER,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr " + response.result);
						if (TextUtils.isEmpty(returnstr)) {
							allDigitalOrderBean.removeAll(allDigitalOrderBean);
							adapter4.notifyDataSetChanged();
							XListViewUtil
									.endload(order_fragment_order1_xlistview4);
						} else {
							List<MySchoolBuyOrderBean> tempallMySchoolBuyOrderBean = new ArrayList<MySchoolBuyOrderBean>();

							if (returnstr.contains("]-[")) {

								int position = returnstr.indexOf("]-[");
								String orderstr = returnstr.substring(0,
										position + 1);
								String goodsstr = returnstr.substring(
										position + 2, returnstr.length());

								Log.v(TAG, "orderstr  " + orderstr);
								Log.v(TAG, "goodsstr  " + goodsstr);

								try {

									JSONArray array = new JSONArray(orderstr);

									JSONArray arraygood = new JSONArray(
											goodsstr);

									for (int i = 0; i < array.length(); i++) {
										JSONObject order = array
												.getJSONObject(i);
										JSONObject goods = arraygood
												.getJSONObject(i);

										int id = order.getInt("id");
										String order_no = order
												.getString("order_no");
										int buy_user_id = order
												.getInt("buy_user_id");
										String get_goods_person_name = order
												.getString("get_goods_person_name");
										String get_goods_person_phone = order
												.getString("get_goods_person_phone");
										String get_goods_person_address = order
												.getString("get_goods_person_address");
										double order_sum_money = order
												.getDouble("order_sum_money");
										int goods_id = order.getInt("goods_id");
										int goods_cate_id = order
												.getInt("goods_cate_id");
										int goods_number = order
												.getInt("goods_number");
										String pay_time = order
												.getString("pay_time");
										int status = order.getInt("status");

										String kd_no = "";
										if (order.has("kd_no")) {
											kd_no = order.getString("kd_no");
										}

										String kd_company = "";
										if (order.has("kd_company")) {
											kd_company = order
													.getString("kd_company");
										}

										String goods_name = goods
												.getString("goods_name");
										String goods_detail = goods
												.getString("goods_detail");
										String goods_log1 = goods
												.getString("goods_log1");
										String goods_log2 = goods
												.getString("goods_log2");
										String goods_log3 = goods
												.getString("goods_log3");
										String goods_kd1 = goods
												.getString("goods_kd1");
										String goods_kd2 = goods
												.getString("goods_kd2");
										int goods_status = goods
												.getInt("goods_status");
										double goods_price = goods
												.getDouble("goods_price");

										MySchoolBuyOrderBean bean = new MySchoolBuyOrderBean(
												id, order_no, buy_user_id, 0,
												get_goods_person_name,
												get_goods_person_phone,
												get_goods_person_address,
												order_sum_money, goods_id,
												goods_cate_id, goods_number,
												pay_time, status, goods_name,
												goods_detail, goods_kd1,
												goods_kd2, goods_log1,
												goods_log2, goods_log3,
												goods_status, goods_price);

										bean.setKd_no(kd_no);
										bean.setKd_company(kd_company);

										tempallMySchoolBuyOrderBean.add(bean);
									}

									Map<String, List<MySchoolBuyOrderBean>> map = new HashMap<String, List<MySchoolBuyOrderBean>>();
									for (int i = 0; i < tempallMySchoolBuyOrderBean
											.size(); i++) {
										MySchoolBuyOrderBean bean = tempallMySchoolBuyOrderBean
												.get(i);
										String orderno = bean.getOrder_no();

										if (map.containsKey(orderno)) {
											List<MySchoolBuyOrderBean> tempOrder = map
													.get(orderno);
											tempOrder.add(bean);
											map.put(orderno, tempOrder);
										} else {
											List<MySchoolBuyOrderBean> tempOrder = new ArrayList<MySchoolBuyOrderBean>();
											tempOrder.add(bean);
											map.put(orderno, tempOrder);
										}
									}

									Log.v(TAG, "map  " + map.size());

									// 第四种
									System.out
											.println("通过Map.values()遍历所有的value，但不能遍历key");

									allDigitalOrderBean
											.removeAll(allDigitalOrderBean);
									for (List<MySchoolBuyOrderBean> v : map
											.values()) {
										Log.v(TAG, "v  " + v.size());

										DiditalOrderBean order = new DiditalOrderBean();
										order.isexp = false;
										order.allorerbean = v;

										allDigitalOrderBean.add(order);
									}

									adapter4.notifyDataSetChanged();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							XListViewUtil
									.endload(order_fragment_order1_xlistview4);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
						XListViewUtil.endload(order_fragment_order1_xlistview4);
					}
				});
	}

	class Adapter4 extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allDigitalOrderBean.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allDigitalOrderBean.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View view, ViewGroup arg2) {

			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(activity).inflate(
						R.layout.item_myorder, null);

				holder.item_myorder_orderno = (TextView) view
						.findViewById(R.id.item_myorder_orderno);
				holder.item_myorder_price = (TextView) view
						.findViewById(R.id.item_myorder_price);
				holder.item_myorder_paytime = (TextView) view
						.findViewById(R.id.item_myorder_paytime);
				holder.item_myorder_goodslistview = (NoScrollListView) view
						.findViewById(R.id.item_myorder_goodslistview);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final MySchoolBuyOrderBean bean = allDigitalOrderBean.get(position).allorerbean
					.get(0);

			holder.item_myorder_orderno.setText("订单编号：" + bean.getOrder_no());
			holder.item_myorder_price.setText("￥" + bean.getOrder_sum_money());
			holder.item_myorder_paytime.setText("订单时间：" + bean.getPay_time());

			holder.item_myorder_goodslistview
					.setAdapter(new AdapterDigitalgoods(allDigitalOrderBean
							.get(position).allorerbean));
			if (allDigitalOrderBean.get(position).isexp) {
				holder.item_myorder_goodslistview.setVisibility(View.VISIBLE);
			} else {
				holder.item_myorder_goodslistview.setVisibility(View.GONE);
			}

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					boolean isexp = allDigitalOrderBean.get(position).isexp;
					if (isexp) {
						for (int i = 0; i < allDigitalOrderBean.size(); i++) {
							allDigitalOrderBean.get(i).isexp = false;
						}
						allDigitalOrderBean.get(position).isexp = false;
					} else {
						for (int i = 0; i < allDigitalOrderBean.size(); i++) {
							allDigitalOrderBean.get(i).isexp = false;
						}
						allDigitalOrderBean.get(position).isexp = true;
					}
					notifyDataSetChanged();
				}
			});

			return view;
		}

		class ViewHolder {
			TextView item_myorder_orderno;
			TextView item_myorder_price;
			TextView item_myorder_paytime;
			NoScrollListView item_myorder_goodslistview;
		}
	}

	/**
	 * 潮流数码商品适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class AdapterDigitalgoods extends BaseAdapter {

		List<MySchoolBuyOrderBean> allMySchoolBuyOrderBean = new ArrayList<MySchoolBuyOrderBean>();

		public AdapterDigitalgoods(
				List<MySchoolBuyOrderBean> allMySchoolBuyOrderBean) {
			this.allMySchoolBuyOrderBean = allMySchoolBuyOrderBean;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allMySchoolBuyOrderBean.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allMySchoolBuyOrderBean.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(activity).inflate(
						R.layout.item_schoolbuyordergoods, null);

				holder.item_shoporder_img = (ImageView) view
						.findViewById(R.id.item_shoporder_img);
				holder.item_shoporder_name = (TextView) view
						.findViewById(R.id.item_shoporder_name);
				holder.item_shoporder_goodsstatus = (TextView) view
						.findViewById(R.id.item_shoporder_goodsstatus);
				holder.item_shoporder_price = (TextView) view
						.findViewById(R.id.item_shoporder_price);
				holder.item_shoporder_btn = (TextView) view
						.findViewById(R.id.item_shoporder_btn);

				holder.item_shoporder_kdnolayout = (LinearLayout) view
						.findViewById(R.id.item_shoporder_kdnolayout);
				holder.item_shoporder_kdno = (TextView) view
						.findViewById(R.id.item_shoporder_kdno);
				holder.item_shoporder_kdcompanylayout = (LinearLayout) view
						.findViewById(R.id.item_shoporder_kdcompanylayout);
				holder.item_shoporder_kdcompany = (TextView) view
						.findViewById(R.id.item_shoporder_kdcompany);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final MySchoolBuyOrderBean bean = allMySchoolBuyOrderBean
					.get(position);

			String log = bean.getGoods_log1();
			if (!log.startsWith("http")) {
				log = GlobalConstant.SERVER_URL + log;
			}
			ImageLoader.getInstance().displayImage(log,
					holder.item_shoporder_img, ImageLoaderOption.getoption());

			holder.item_shoporder_price.setText("￥" + bean.getGoods_price()
					+ " X " + bean.getGoods_number());

			holder.item_shoporder_name.setText(bean.getGoods_name());

			final String kdno = bean.getKd_no();
			final String kdcompany = bean.getKd_company();

			if (TextUtils.isEmpty(kdno) || TextUtils.isEmpty(kdcompany)) {
				holder.item_shoporder_kdnolayout.setVisibility(View.GONE);
				holder.item_shoporder_kdcompanylayout.setVisibility(View.GONE);
			} else {
				holder.item_shoporder_kdnolayout.setVisibility(View.VISIBLE);
				holder.item_shoporder_kdcompanylayout
						.setVisibility(View.VISIBLE);

				holder.item_shoporder_kdno.setText(kdno);
				holder.item_shoporder_kdcompany.setText(kdcompany);

				holder.item_shoporder_kdno
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Intent intent = new Intent();
								intent.setClass(activity, ExpressActivity.class);
								intent.putExtra("kdno", kdno);
								intent.putExtra("kdcompany", kdcompany);
								startActivity(intent);
							}
						});
			}

			int status = bean.getStatus();
			// 0->未发货
			// 1->待收货
			if (status == 0) {
				holder.item_shoporder_goodsstatus.setText("待发货");

				holder.item_shoporder_btn.setText("取消订单");
				holder.item_shoporder_btn.setVisibility(View.VISIBLE);
				holder.item_shoporder_btn
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								AlertDialog.Builder build = new Builder(
										activity);
								build.setTitle("提示");
								build.setMessage("您确认取消订单吗？");
								build.setPositiveButton("确认",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												digitalCancelOrder(
														AdapterDigitalgoods.this,
														bean);
											}
										});
								build.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
											}
										});
								build.show();
							}
						});
			} else if (status == 1) {
				holder.item_shoporder_btn.setVisibility(View.VISIBLE);

				holder.item_shoporder_goodsstatus.setText("待收货");

				holder.item_shoporder_btn.setText("确认收货");
				holder.item_shoporder_btn
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								AlertDialog.Builder build = new Builder(
										activity);
								build.setTitle("提示");
								build.setMessage("您确认收货吗？");
								build.setPositiveButton("确认",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												digitalConfirmReceiving(
														AdapterDigitalgoods.this,
														bean);
											}
										});
								build.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
											}
										});
								build.show();
							}
						});
			} else if (status == 2) {
				holder.item_shoporder_btn.setText("未评价");
				holder.item_shoporder_goodsstatus.setText("未评价");
				holder.item_shoporder_btn.setVisibility(View.VISIBLE);
				holder.item_shoporder_btn
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								digitalPopevaluate(bean);
							}
						});
			} else if (status == 3) {
				holder.item_shoporder_btn.setText("已评价");
				holder.item_shoporder_goodsstatus.setText("已评价");
				holder.item_shoporder_btn.setVisibility(View.GONE);
			} else if (status == 4) {
				holder.item_shoporder_btn.setText("已取消");
				holder.item_shoporder_goodsstatus.setText("已取消");
				holder.item_shoporder_btn.setVisibility(View.GONE);
			}
			return view;
		}

		class ViewHolder {
			ImageView item_shoporder_img;
			TextView item_shoporder_name;
			TextView item_shoporder_goodsstatus;
			TextView item_shoporder_price;
			TextView item_shoporder_btn;

			LinearLayout item_shoporder_kdnolayout;
			TextView item_shoporder_kdno;
			LinearLayout item_shoporder_kdcompanylayout;
			TextView item_shoporder_kdcompany;
		}
	}

	/**
	 * 潮流数码取消订单
	 */
	private void digitalCancelOrder(
			final AdapterDigitalgoods adapterDigitalgoods,
			final MySchoolBuyOrderBean bean) {

		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在取消订单");

		RequestParams params = new RequestParams();
		params.addBodyParameter("order_no", bean.getOrder_no() + "");

		Log.v(TAG, " bean.getOrder_no()   " + bean.getOrder_no());

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.DIGITAL_ORDER_CancelOrder, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr	" + response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("取消订单失败");
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);
								String code = json.getString("code");
								String msg = json.getString("msg");
								showToast(msg);

								int codeint = Integer.parseInt(code);

								if (codeint > 0) {

									for (int i = 0; i < allDigitalOrderBean
											.size(); i++) {
										for (int j = 0; j < allDigitalOrderBean
												.get(i).allorerbean.size(); j++) {
											allDigitalOrderBean.get(i).allorerbean
													.get(j).setStatus(4);
										}
									}
									adapterDigitalgoods.notifyDataSetChanged();
								}
							} catch (JSONException e) {
								e.printStackTrace();
								showToast("取消订单失败");
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("取消订单失败");
					}
				});
	}

	/**
	 * 潮流数码确认收货
	 */
	private void digitalConfirmReceiving(
			final AdapterDigitalgoods adapterDigitalgoods,
			final MySchoolBuyOrderBean bean) {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在确认收货");

		RequestParams params = new RequestParams();
		params.addBodyParameter("order_id", bean.getId() + "");

		Log.v(TAG, " bean.getId()   " + bean.getId());

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.DIGITAL_ORDER_RECEIVEGOODS, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("确认收货失败");
						} else {
							// {"resultCode":1,"beanId":1,"resultMsg":"已确认收货"}
							try {
								JSONObject json = new JSONObject(returnstr);
								String code = json.getString("code");
								String msg = json.getString("msg");
								showToast(msg);
								if (code.equalsIgnoreCase("1")) {

									for (int i = 0; i < allDigitalOrderBean
											.size(); i++) {

										for (int j = 0; j < allDigitalOrderBean
												.get(i).allorerbean.size(); j++) {
											if (allDigitalOrderBean.get(i).allorerbean
													.get(j).getId() == bean
													.getId()) {
												allDigitalOrderBean.get(i).allorerbean
														.get(j).setStatus(2);
											}
										}
									}

									adapterDigitalgoods.notifyDataSetChanged();
								}
							} catch (JSONException e) {
								e.printStackTrace();
								showToast("确认收货失败");
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("确认收货失败");
					}
				});
	}

	/**
	 * 弹出潮流数码评价
	 */

	private void digitalPopevaluate(final MySchoolBuyOrderBean bean) {
		View view1 = LayoutInflater.from(activity).inflate(
				R.layout.pop_evaluate, null);
		mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);

		final RatingBar room_ratingbar = (RatingBar) view1
				.findViewById(R.id.room_ratingbar);

		TextView pop_evaluate_cancel = (TextView) view1
				.findViewById(R.id.pop_evaluate_cancel);
		TextView pop_evaluate_submit = (TextView) view1
				.findViewById(R.id.pop_evaluate_submit);

		pop_evaluate_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dissmisspopwindow();
			}
		});
		pop_evaluate_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int evastatus = 0;
				// 评价修改
				int comment_star_num = (int) room_ratingbar.getRating();

				submitDigitalEvaluate(bean, comment_star_num, evastatus, true);
			}
		});
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
		mPopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
	}

	/**
	 * 潮流数码评价
	 */
	private void submitDigitalEvaluate(MySchoolBuyOrderBean bean,
			int comment_star_num, int evastatus, final boolean issubmit) {
		if (!BaseUtils.isNetWork(activity)) {
			if (issubmit) {
				showToast("请检查您的网络");
			}
			return;
		}

		if (issubmit) {
			showLoading2("正在提交评价");
		}
		RequestParams params = new RequestParams();

		params.addBodyParameter("handle_type", "3");
		params.addBodyParameter("goods_id", "" + bean.getGoods_id());

		Log.v(TAG, "handle_type  3");
		Log.v(TAG, "goods_id  " + bean.getGoods_id());

		params.addBodyParameter("comment_type", "" + evastatus);
		params.addBodyParameter("comment_star_num", "" + comment_star_num);
		params.addBodyParameter("handle_id", String.valueOf(bean.getId()));

		Log.v(TAG, "comment_type  " + evastatus);
		Log.v(TAG, "handle_id  " + String.valueOf(bean.getId()));

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.PRIVATE_ORDER_ADDCOMMENT, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr  " + response.result);
						if (TextUtils.isEmpty(returnstr)) {
							if (issubmit) {
								showToast("提交失败");
							}
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);
								String code = json.getString("code");
								String msg = json.getString("msg");
								if (code.equalsIgnoreCase("1")) {
									dissmisspopwindow();
									loadDigitalOrder();
								}
								if (issubmit) {
									showToast(msg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
								if (issubmit) {
									showToast("提交失败");
								}
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						if (issubmit) {
							showToast("提交失败");
						}
					}
				});
	}

	/**
	 * 二手交易订单
	 */

	// 载入二手交易订单
	private void loadSecondOrder() {

		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", userid + "");
		params.addBodyParameter("click_type", "0");

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.GET_MY_SECOND_HAND_ORDER_SEND, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr " + response.result);
						if (TextUtils.isEmpty(returnstr)) {
							allSecondHandOrderBean
									.removeAll(allSecondHandOrderBean);
							adapter5.notifyDataSetChanged();
							XListViewUtil
									.endload(order_fragment_order1_xlistview5);
						} else {

							if (returnstr.contains("]-[")) {

								int position = returnstr.indexOf("]-[");
								String orderstr = returnstr.substring(0,
										position + 1);
								String goodsstr = returnstr.substring(
										position + 2, returnstr.length());

								Log.v(TAG, "orderstr  " + orderstr);
								Log.v(TAG, "goodsstr  " + goodsstr);

								try {

									List<SecondHandOrderBean> tempallMySecondOrder = new ArrayList<SecondHandOrderBean>();

									JSONArray array = new JSONArray(orderstr);

									JSONArray arraysock = new JSONArray(
											goodsstr);

									for (int i = 0; i < array.length(); i++) {
										JSONObject json = array
												.getJSONObject(i);

										JSONObject sock = arraysock
												.getJSONObject(i);

										int id = json.getInt("id");
										String order_no = json
												.getString("order_no");
										int user_id = json.getInt("user_id");
										int goods_id = json.getInt("goods_id");
										int buy_user_id = json
												.getInt("buy_user_id");

										int goods_number = json
												.getInt("goods_number");

										String pay_time = json
												.getString("pay_time");

										String get_goods_person_name = json
												.getString("get_goods_person_name");
										String get_goods_person_phone = json
												.getString("get_goods_person_phone");
										String get_goods_person_address = json
												.getString("get_goods_person_address");
										double order_sum_money = json
												.getDouble("order_sum_money");
										String rand_no = json
												.getString("rand_no");
										int status = json.getInt("status");

										String title = sock.getString("title");
										String detail = sock
												.getString("detail");
										String log1 = sock.getString("log1");
										String phone = sock.getString("phone");
										double price = sock.getDouble("price");

										SecondHandOrderBean bean = new SecondHandOrderBean(
												id, order_no, user_id,
												buy_user_id, goods_id,
												goods_number, pay_time,
												get_goods_person_name,
												get_goods_person_phone,
												get_goods_person_address,
												order_sum_money, rand_no,
												status, title, detail, log1,
												phone, price);

										tempallMySecondOrder.add(bean);
									}
									allSecondHandOrderBean = tempallMySecondOrder;
									adapter5.notifyDataSetChanged();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							XListViewUtil
									.endload(order_fragment_order1_xlistview5);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
						XListViewUtil.endload(order_fragment_order1_xlistview5);
					}
				});
	}

	//
	class Adapter5 extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allSecondHandOrderBean.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allSecondHandOrderBean.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(activity).inflate(
						R.layout.item_shoporder, null);

				holder.item_shoporder_img = (ImageView) view
						.findViewById(R.id.item_shoporder_img);
				holder.item_shoporder_name = (TextView) view
						.findViewById(R.id.item_shoporder_name);
				holder.item_shoporder_goodsstatus = (TextView) view
						.findViewById(R.id.item_shoporder_goodsstatus);
				holder.item_shoporder_price = (TextView) view
						.findViewById(R.id.item_shoporder_price);
				holder.item_shoporder_btn = (TextView) view
						.findViewById(R.id.item_shoporder_btn);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final SecondHandOrderBean bean = allSecondHandOrderBean
					.get(position);

			String log = bean.getLog1();
			if (!log.startsWith("http")) {
				log = GlobalConstant.SERVER_URL + log;
			}
			ImageLoader.getInstance().displayImage(log,
					holder.item_shoporder_img, ImageLoaderOption.getoption());

			holder.item_shoporder_price
					.setText("￥" + bean.getOrder_sum_money());

			holder.item_shoporder_name.setText(bean.getTitle());

			int status = bean.getStatus();
			// 0->未发货
			// 1->待收货
			if (status == 0) {
				holder.item_shoporder_goodsstatus.setText("待发货");
				holder.item_shoporder_btn.setVisibility(View.VISIBLE);
				holder.item_shoporder_btn.setText("取消订单");
				holder.item_shoporder_btn
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								AlertDialog.Builder build = new Builder(
										activity);
								build.setTitle("提示");
								build.setMessage("您确认取消订单吗？");
								build.setPositiveButton("确认",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												secondCancelOrder(
														Adapter5.this, bean);
											}
										});
								build.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
											}
										});
								build.show();
							}
						});

			} else if (status == 1) {
				holder.item_shoporder_btn.setVisibility(View.VISIBLE);

				holder.item_shoporder_goodsstatus.setText("待收货");

				holder.item_shoporder_btn.setText("确认收货");
				holder.item_shoporder_btn
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								AlertDialog.Builder build = new Builder(
										activity);
								build.setTitle("提示");
								build.setMessage("您确认收货吗？");
								build.setPositiveButton("确认",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												secondConfirmReceiving(bean);
											}
										});
								build.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
											}
										});
								build.show();
							}
						});
			} else if (status == 2) {
				holder.item_shoporder_goodsstatus.setText("已确认收货");
				holder.item_shoporder_btn.setVisibility(View.GONE);
			} else if (status == 4) {
				holder.item_shoporder_goodsstatus.setText("已取消订单");
				holder.item_shoporder_btn.setVisibility(View.GONE);
			}

			return view;
		}

		class ViewHolder {
			ImageView item_shoporder_img;
			TextView item_shoporder_name;
			TextView item_shoporder_goodsstatus;
			TextView item_shoporder_price;
			TextView item_shoporder_btn;
		}
	}

	/**
	 * 二手交易取消订单
	 */
	private void secondCancelOrder(final Adapter5 adapter5,
			final SecondHandOrderBean bean) {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在取消订单");

		RequestParams params = new RequestParams();
		params.addBodyParameter("order_id", bean.getId() + "");

		Xutils.loadData(HttpMethod.POST, GlobalConstant.SECOND_CANCEL_ORDER,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("取消订单失败");
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);
								String code = json.getString("code");
								String msg = json.getString("msg");
								showToast(msg);

								bean.setStatus(4);
								adapter5.notifyDataSetChanged();

							} catch (JSONException e) {
								e.printStackTrace();
								showToast("取消订单失败");
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("取消订单失败");
					}
				});
	}

	/**
	 * 二手交易确认收货
	 */
	private void secondConfirmReceiving(final SecondHandOrderBean bean) {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在确认收货");

		RequestParams params = new RequestParams();
		params.addBodyParameter("order_id", bean.getId() + "");
		params.addBodyParameter("click_type", "1");

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.GET_MY_SECOND_HAND_ORDER_RECEIVE, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("确认收货失败");
						} else {
							// {"resultCode":1,"beanId":1,"resultMsg":"已确认收货"}
							try {
								JSONObject json = new JSONObject(returnstr);
								String code = json.getString("code");
								String msg = json.getString("msg");
								showToast(msg);
								if (code.equalsIgnoreCase("1")) {
									loadSecondOrder();

									JpushAddMsg(userid, bean.getUser_id(),
											"您的二手交易订单确认收货了",
											"您的二手交易订单确认收货了,请查看我的二手订单");
								}
							} catch (JSONException e) {
								e.printStackTrace();
								showToast("确认收货失败");
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("确认收货失败");
					}
				});
	}

	/**
	 * 积分商城
	 * 
	 * @author Administrator
	 * 
	 */

	/**
	 * 加载积分商城订单
	 */
	private void loadPointOrder() {

		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", userid + "");
		params.addBodyParameter("click_type", "0");

		Xutils.loadData(HttpMethod.POST, GlobalConstant.GET_MY_POINT_ORDER,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr " + response.result);
						if (TextUtils.isEmpty(returnstr)) {
							allSecondHandOrderBean
									.removeAll(allSecondHandOrderBean);
							adapter6.notifyDataSetChanged();
							XListViewUtil
									.endload(order_fragment_order1_xlistview6);
						} else {

							if (returnstr.contains("]-[")) {

								int position = returnstr.indexOf("]-[");
								String orderstr = returnstr.substring(0,
										position + 1);
								String goodsstr = returnstr.substring(
										position + 2, returnstr.length());

								Log.v(TAG, "orderstr  " + orderstr);
								Log.v(TAG, "goodsstr  " + goodsstr);

								try {
									List<PointOrderBean> tempallPointOrderBean = new ArrayList<PointOrderBean>();

									JSONArray array = new JSONArray(orderstr);

									JSONArray arraysock = new JSONArray(
											goodsstr);

									for (int i = 0; i < array.length(); i++) {
										JSONObject json = array
												.getJSONObject(i);

										JSONObject sock = arraysock
												.getJSONObject(i);

										int id = json.getInt("id");
										String order_no = json
												.getString("order_no");
										int buy_user_id = json
												.getInt("buy_user_id");
										int goods_id = json.getInt("goods_id");

										int goods_number = json
												.getInt("goods_number");

										String pay_time = json
												.getString("pay_time");

										String get_goods_person_name = json
												.getString("get_goods_person_name");
										String get_goods_person_phone = json
												.getString("get_goods_person_phone");
										String get_goods_person_address = json
												.getString("get_goods_person_address");
										double order_sum_integtal = json
												.getDouble("order_sum_integtal");
										String rand_no = json
												.getString("rand_no");
										int status = json.getInt("status");

										String goods_title = sock
												.getString("goods_title");
										String log = sock.getString("log");

										PointOrderBean bean = new PointOrderBean(
												id, order_no, buy_user_id,
												get_goods_person_name,
												get_goods_person_phone,
												get_goods_person_address,
												order_sum_integtal, goods_id,
												pay_time, rand_no,
												goods_number, status,
												goods_title, log);

										tempallPointOrderBean.add(bean);
									}
									allPointOrderBean = tempallPointOrderBean;
									adapter6.notifyDataSetChanged();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							XListViewUtil
									.endload(order_fragment_order1_xlistview6);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
						XListViewUtil.endload(order_fragment_order1_xlistview6);
					}
				});

	}

	class Adapter6 extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allPointOrderBean.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allPointOrderBean.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(activity).inflate(
						R.layout.item_shoporder, null);

				holder.item_shoporder_img = (ImageView) view
						.findViewById(R.id.item_shoporder_img);
				holder.item_shoporder_name = (TextView) view
						.findViewById(R.id.item_shoporder_name);
				holder.item_shoporder_goodsstatus = (TextView) view
						.findViewById(R.id.item_shoporder_goodsstatus);
				holder.item_shoporder_price = (TextView) view
						.findViewById(R.id.item_shoporder_price);
				holder.item_shoporder_btn = (TextView) view
						.findViewById(R.id.item_shoporder_btn);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final PointOrderBean bean = allPointOrderBean.get(position);

			String log = bean.getLog();
			if (!log.startsWith("http")) {
				log = GlobalConstant.SERVER_URL + log;
			}
			ImageLoader.getInstance().displayImage(log,
					holder.item_shoporder_img, ImageLoaderOption.getoption());

			holder.item_shoporder_price.setText("积分:"
					+ bean.getOrder_sum_integtal());

			holder.item_shoporder_name.setText(bean.getGoods_title());

			int status = bean.getStatus();
			// 0->未发货
			// 1->待收货
			if (status == 0) {
				holder.item_shoporder_goodsstatus.setText("待发货");
				holder.item_shoporder_btn.setVisibility(View.GONE);
			} else if (status == 1) {
				holder.item_shoporder_btn.setVisibility(View.VISIBLE);

				holder.item_shoporder_goodsstatus.setText("待收货");

				holder.item_shoporder_btn.setText("确认收货");
				holder.item_shoporder_btn
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								AlertDialog.Builder build = new Builder(
										activity);
								build.setTitle("提示");
								build.setMessage("您确认收货吗？");
								build.setPositiveButton("确认",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												pointConfirmReceiving(bean);
											}
										});
								build.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
											}
										});
								build.show();
							}
						});
			} else if (status == 2) {
				holder.item_shoporder_goodsstatus.setText("已确认收货");
				holder.item_shoporder_btn.setVisibility(View.GONE);
			} else {
				holder.item_shoporder_btn.setVisibility(View.GONE);
			}
			return view;
		}

		class ViewHolder {
			ImageView item_shoporder_img;
			TextView item_shoporder_name;
			TextView item_shoporder_goodsstatus;
			TextView item_shoporder_price;
			TextView item_shoporder_btn;
		}
	}

	/**
	 * 积分商城确认收货
	 */
	private void pointConfirmReceiving(final PointOrderBean bean) {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在确认收货");

		RequestParams params = new RequestParams();
		params.addBodyParameter("order_id", bean.getId() + "");
		params.addBodyParameter("click_type", "1");

		Xutils.loadData(HttpMethod.POST, GlobalConstant.GET_MY_POINT_ORDER,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("确认收货失败");
						} else {
							// {"resultCode":1,"beanId":1,"resultMsg":"已确认收货"}
							try {
								JSONObject json = new JSONObject(returnstr);
								String code = json.getString("code");
								String msg = json.getString("msg");
								showToast(msg);
								if (code.equalsIgnoreCase("1")) {
									loadPointOrder();
								}
							} catch (JSONException e) {
								e.printStackTrace();
								showToast("确认收货失败");
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("确认收货失败");
					}
				});
	}

}
