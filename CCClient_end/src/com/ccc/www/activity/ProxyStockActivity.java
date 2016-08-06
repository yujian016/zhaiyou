package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.ProxySockIndicatorAdapter;
import com.ccc.www.bean.SockBean;
import com.ccc.www.bean.SupMarketCategoryBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.ContextUtil;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.readystatesoftware.viewbadger.BadgeView;
import com.viewpagerindicator.TabPageIndicator;

public class ProxyStockActivity extends BaseActivity {

	ImageButton ib_select_school_goback;

	String TAG = "ProxySockActivity";

	List<SupMarketCategoryBean> allSockCategoryBean = new ArrayList<SupMarketCategoryBean>();

	ViewPager pager;
	TabPageIndicator indicator;

	ImageView iv_proxysockcartcount;

	int userid;

	UpdateProxyStockCartCount updateProxyStockCartCount = new UpdateProxyStockCartCount();
	PaySuccessToMyOrder paySuccessToMyOrder = new PaySuccessToMyOrder();

	BadgeView badge;

	/**
	 * 支付成功后关闭页面
	 * 
	 * @author Administrator
	 * 
	 */
	class PaySuccessToMyOrder extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			ProxyStockActivity.this.finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ContextUtil.setallSockBeannull();

		registerReceiver(updateProxyStockCartCount, new IntentFilter(
				GlobalConstant.UpdateProxyStockCartCount));
		registerReceiver(paySuccessToMyOrder, new IntentFilter(
				GlobalConstant.PaySuccessToMyOrder));

		userid = UserUtil.getuserid(this);

		setContentView(R.layout.activity_proxysock);
		initview();

		badge = new BadgeView(ProxyStockActivity.this, iv_proxysockcartcount);

		updateCartCount();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_select_school_goback:
			ProxyStockActivity.this.finish();
			break;
		case R.id.iv_proxysockcartcount:
			// 先判断购买的商品是否达到了起批量
			boolean okstartcount = true;

			String startcountgoodsname = "";

			// 判断购买的商品是否达到了单次进货需要满足的总金额
			boolean okstartmoney = true;

			String startmoneygoodsname = "";

			List<SockBean> allSockBean = ContextUtil.getallSockBean();

			List<SockBean> allsock = DBUtil.getProxyStockCart(this, userid);

			for (int i = 0; i < allsock.size(); i++) {
				for (int j = 0; j < allSockBean.size(); j++) {
					if (allsock.get(i).getId() == allSockBean.get(j).getId()) {
						int count = allsock.get(i).getCount();
						int needcount = allSockBean.get(j).getSock_startcount();
						if (count < needcount) {
							okstartcount = false;
							startcountgoodsname = allsock.get(i).getSock_name();
						}
					}
				}
			}

			for (int i = 0; i < allsock.size(); i++) {
				for (int j = 0; j < allSockBean.size(); j++) {
					if (allsock.get(i).getId() == allSockBean.get(j).getId()) {
						int count = allsock.get(i).getCount();
						float needmoney = allsock.get(i).getSock_price()
								* count;
						if (needmoney < allsock.get(i).getOrder_sum_money()) {
							okstartmoney = false;
							startmoneygoodsname = allsock.get(i).getSock_name();
						}
					}
				}
			}
			if (okstartcount && okstartmoney) {
				// 去购物车界面
				Intent proxyStockCart = new Intent();
				proxyStockCart.setClass(ProxyStockActivity.this,
						ProxyStockCartActivity.class);
				startActivity(proxyStockCart);
			} else {
				if (!okstartcount) {
					showToast("您购买的商品中" + startcountgoodsname + "未达到起批量");
				} else if (!okstartmoney) {
					showToast("您购买的商品中" + startmoneygoodsname
							+ "未达到单次进货需要满足的总金额");
				}
			}

			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findviewWithId() {
		ib_select_school_goback = (ImageButton) findViewById(R.id.ib_select_school_goback);
		iv_proxysockcartcount = (ImageView) findViewById(R.id.iv_proxysockcartcount);

		pager = (ViewPager) findViewById(R.id.activity_proxysock_viewpager);
		// 实例化TabPageIndicator然后设置ViewPager与之关联
		indicator = (TabPageIndicator) findViewById(R.id.activity_proxysock_indicator);
	}

	@Override
	public void initListener() {
		ib_select_school_goback.setOnClickListener(this);
		iv_proxysockcartcount.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		loadData(HttpMethod.GET, GlobalConstant.SUP_ACTION_GET_GOODS_CATEGORY,
				null, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {

						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("加载失败");
						} else {
							try {
								JSONArray json = new JSONArray(returnstr);
								for (int i = 0; i < json.length(); i++) {
									JSONObject temp = json.getJSONObject(i);
									int id = temp.getInt("id");
									String sock_category_name = temp
											.getString("sock_category_name");

									SupMarketCategoryBean bean = new SupMarketCategoryBean(
											id, sock_category_name);
									allSockCategoryBean.add(bean);
								}

								Log.v(TAG, "allSockCategoryBean.size()  "
										+ allSockCategoryBean.size());

								if (allSockCategoryBean.size() > 0) {

									ProxySockIndicatorAdapter adapter = new ProxySockIndicatorAdapter(
											getSupportFragmentManager(),
											allSockCategoryBean);

									pager.setAdapter(adapter);

									Log.v(TAG, "pager  " + pager);
									Log.v(TAG, "indicator  " + indicator);

									indicator.setViewPager(pager);

									indicator.setVisibility(View.VISIBLE);

									// 如果我们要对ViewPager设置监听，用indicator设置就行了
									indicator
											.setOnPageChangeListener(new OnPageChangeListener() {

												@Override
												public void onPageSelected(
														int arg0) {

												}

												@Override
												public void onPageScrolled(
														int arg0, float arg1,
														int arg2) {

												}

												@Override
												public void onPageScrollStateChanged(
														int arg0) {

												}
											});
								} else {
									showToast("无商品分类");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}
				});
	}

	/**
	 * 更新购物车数目广播
	 * 
	 * @author Administrator
	 * 
	 */
	class UpdateProxyStockCartCount extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			updateCartCount();
		}
	}

	/**
	 * 更新购物车数目
	 */
	private void updateCartCount() {
		// 取出数据库中count>0的货物
		List<SockBean> allSockBean = DBUtil.getProxyStockCart(
				ProxyStockActivity.this, userid);

		int allcount = 0;
		for (int i = 0; i < allSockBean.size(); i++) {
			int count = allSockBean.get(i).getCount();
			allcount = allcount + count;
		}

		Log.v(TAG, "allSockBean  " + allSockBean.size());

		badge.setText("" + allcount);
		badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badge.setTextSize(10);
		if (allcount > 0) {
			badge.show();
		} else {
			badge.hide();
		}

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(updateProxyStockCartCount);
		unregisterReceiver(paySuccessToMyOrder);

		super.onDestroy();
	}
}
