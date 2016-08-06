package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.ProxyShopBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 列出宿舍的全部代理
 * 
 * @author Administrator
 * 
 */
public class PrivateSupermarketDormitoryProxyActivity extends BaseActivity {

	String TAG = "PrivateSupermarketDormitoryProxyActivity";

	int hostel_id = 0;

	ImageButton ib_select_dormitory_goback;
	TextView apply_proxy;
	ListView proxy_listview;

	int userid;

	List<ProxyShopBean> allProxyShopBean = new ArrayList<ProxyShopBean>();

	Adapter adapter = new Adapter();

	TextView clear_choose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		userid = UserUtil.getuserid(this);

		hostel_id = getIntent().getIntExtra("hostel_id", 0);
		setContentView(R.layout.activity_select_privatesupermarketdormitoryproxy);
		initview();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_select_dormitory_goback:
			PrivateSupermarketDormitoryProxyActivity.this.finish();
			break;
		case R.id.clear_choose:
			UserUtil.setprivatesmhotelid(
					PrivateSupermarketDormitoryProxyActivity.this, 0);
			showToast("清除成功");
			break;
		case R.id.apply_proxy:
			getproxyshopcount();
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
		ib_select_dormitory_goback = (ImageButton) findViewById(R.id.ib_select_dormitory_goback);
		apply_proxy = (TextView) findViewById(R.id.apply_proxy);
		clear_choose = (TextView) findViewById(R.id.clear_choose);
		proxy_listview = (ListView) findViewById(R.id.proxy_listview);

		proxy_listview.setAdapter(adapter);
	}

	@Override
	public void initListener() {
		ib_select_dormitory_goback.setOnClickListener(this);
		apply_proxy.setOnClickListener(this);
		clear_choose.setOnClickListener(this);
	}

	@Override
	public void initdata() {

		getallproxyshop();

	}

	/**
	 * 获取该楼诚所有代理
	 */
	private void getallproxyshop() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查网络");
			return;
		}

		Log.v(TAG, "hostel_id " + hostel_id);

		showLoading2("正在加载");
		RequestParams params = new RequestParams();

		params.addBodyParameter("hostel_id", String.valueOf(hostel_id));
		loadData(HttpMethod.POST, GlobalConstant.GET_SP_HOSTELID, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String returnstr = info.result;

						Log.v(TAG, "returnstr  " + returnstr);

						if (TextUtils.isEmpty(returnstr)) {
							showToast("加载失败");
						} else {
							try {
								JSONArray array = new JSONArray(returnstr);
								for (int i = 0; i < array.length(); i++) {
									JSONObject json = array.getJSONObject(i);

									int id = json.getInt("id");
									int hostel_id = json.getInt("hostel_id");
									String sup_market_name = json
											.getString("sup_market_name");
									int user_id = json.getInt("user_id");
									String apply_proxy_time = json
											.getString("apply_proxy_time");
									// 1审核通过 0未审核
									int sup_market_status = json
											.getInt("sup_market_status");

									String send_goods = json
											.getString("send_goods");
									int open_status = json
											.getInt("open_status");
									int start_send_money = json
											.getInt("start_send_money");

									if (sup_market_status == 1) {
										ProxyShopBean bean = new ProxyShopBean(
												id, hostel_id, sup_market_name,
												user_id, apply_proxy_time,
												send_goods, open_status,
												start_send_money,
												sup_market_status);
										allProxyShopBean.add(bean);
									}
								}
								adapter.notifyDataSetChanged();
							} catch (JSONException e) {
								showToast("加载失败");
								e.printStackTrace();
							}
						}
					}
				});
	}

	class Adapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allProxyShopBean.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allProxyShopBean.get(arg0);
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
				view = LayoutInflater.from(
						PrivateSupermarketDormitoryProxyActivity.this).inflate(
						R.layout.item_privatesupermarketname, null);
				holder.item_privatesupermarketname_tv = (TextView) view
						.findViewById(R.id.item_privatesupermarketname_tv);

				holder.item_privatesupermarketname_status = (TextView) view
						.findViewById(R.id.item_privatesupermarketname_status);
				holder.item_privatesupermarketname_info = (TextView) view
						.findViewById(R.id.item_privatesupermarketname_info);
				holder.item_privatesupermarketname_send = (TextView) view
						.findViewById(R.id.item_privatesupermarketname_send);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final ProxyShopBean bean = allProxyShopBean.get(position);

			holder.item_privatesupermarketname_tv.setText(bean
					.getSup_market_name());

			final int open_status = bean.getOpen_status();
			if (open_status == 0) {
				holder.item_privatesupermarketname_status.setText("营业中");
				holder.item_privatesupermarketname_status
						.setBackgroundResource(R.drawable.corners_privatesupermarket_yyz_bg);
			} else {
				holder.item_privatesupermarketname_status.setText("休息中");
				holder.item_privatesupermarketname_status
						.setBackgroundResource(R.drawable.corners_privatesupermarket_xxz_bg);
			}

			int start_send_money = bean.getStart_send_money();
			holder.item_privatesupermarketname_info.setText(start_send_money
					+ "元起送");

			holder.item_privatesupermarketname_send.setText(bean
					.getSend_goods());

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (open_status == 0) {
						entershop(bean);
					} else {
						showToast("店铺休息中");
					}
				}
			});
			return view;
		}

		class ViewHolder {
			TextView item_privatesupermarketname_tv;
			TextView item_privatesupermarketname_status;
			TextView item_privatesupermarketname_info;
			TextView item_privatesupermarketname_send;
		}
	}

	/**
	 * 进入店铺
	 */
	private void entershop(final ProxyShopBean bean) {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查网络");
		} else {
			showLoading2("正在加载");
			RequestParams params = new RequestParams();
			params.addBodyParameter("user_id", String.valueOf(userid));
			loadData(HttpMethod.POST, GlobalConstant.USER_ISHAVE_SHOP, params,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dismissProgressDialog();
							showToast("加载失败");
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							dismissProgressDialog();
							if (TextUtils.isEmpty(info.result)) {
								showToast("加载失败");
							} else {
								String respJson = info.result;
								android.util.Log.v(TAG, respJson);
								JSONObject obj;
								try {
									obj = new JSONObject(respJson);
									int code = obj.getInt("code");
									if (code == 2) {
										showToast("你已是宿舍代理,不能在此购物");
									} else {

										UserUtil.setenterpsmid(
												PrivateSupermarketDormitoryProxyActivity.this,
												bean.getUser_id());

										UserUtil.setenterpsm_startmoney(
												PrivateSupermarketDormitoryProxyActivity.this,
												bean.getStart_send_money());

										Intent intent = new Intent();
										intent.putExtra("proxyshopid",
												bean.getId());
										intent.setClass(
												PrivateSupermarketDormitoryProxyActivity.this,
												PrivateSupermarketActivity.class);
										startActivity(intent);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}
					});
		}
	}

	/**
	 * 获取所有代理的总数
	 */
	private void getproxyshopcount() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查网络");
			return;
		}

		Log.v(TAG, "hostel_id " + hostel_id);

		showLoading2("正在加载");
		RequestParams params = new RequestParams();

		params.addBodyParameter("hostel_id", String.valueOf(hostel_id));
		loadData(HttpMethod.POST, GlobalConstant.GET_PROXY_COUNT_HOSTELID,
				params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String returnstr = info.result;

						Log.v(TAG, "returnstr  " + returnstr);

						if (TextUtils.isEmpty(returnstr)) {
							showToast("加载失败");
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);
								int proxy_count = json.getInt("proxy_count");
								if (proxy_count < 3) {
									// 申请代理
									applyproxy();
								} else {
									showToast("该楼层代理已满");
								}
							} catch (JSONException e) {
								showToast("加载失败");
								e.printStackTrace();
							}
						}
					}
				});
	}

	/**
	 * 申请代理
	 */
	private void applyproxy() {
		AlertDialog.Builder build = new Builder(
				PrivateSupermarketDormitoryProxyActivity.this);
		build.setTitle("提示");
		build.setMessage("申请代理吗？");
		build.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// 进入代理申请界面,需要先登录
				SharedPreferences sp = getSharedPreferences("login_session",
						Context.MODE_PRIVATE);
				int userid = sp.getInt("login_id", 0);
				if (userid > 0) {
					// 加入一个账号只能申请一次代理的限制
					checkUserStatus(userid);
				} else {
					Intent intent = new Intent();
					intent.setClass(
							PrivateSupermarketDormitoryProxyActivity.this,
							LoginActivity.class);
					startActivity(intent);
				}
			}

		});
		build.setNegativeButton("否", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO
				// Auto-generated
				// method
				// stub

			}
		});
		build.show();
	}

	/**
	 * 根据用户ID查询开店状态
	 * 
	 * @param userid
	 */
	private void checkUserStatus(final int userid) {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查网络");
			return;
		}
		showLoading2("正在加载");
		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", String.valueOf(userid));
		loadData(HttpMethod.POST, GlobalConstant.USER_ISHAVE_SHOP, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						if (TextUtils.isEmpty(info.result)) {
							showToast("加载失败");
						} else {
							String respJson = info.result;
							android.util.Log.v(TAG, respJson);
							JSONObject obj;
							try {
								obj = new JSONObject(respJson);
								int code = obj.getInt("code");
								if (code > 0) {
									showToast("你已是店主或者其他宿舍代理");
								} else {
									Intent intent = new Intent();
									intent.putExtra("hostel_id", hostel_id);
									intent.putExtra("user_id", userid);
									intent.setClass(
											PrivateSupermarketDormitoryProxyActivity.this,
											ApplyProxyActivity.class);
									startActivity(intent);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}
}
