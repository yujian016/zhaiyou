package com.ccc.www.fragment;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.activity.MySchoolBuyOrderDetailActivity;
import com.ccc.www.activity.MySecondOrderDetailActivity;
import com.ccc.www.activity.MyShopOrderDetailActivity;
import com.ccc.www.bean.MySchoolBuyOrderBean;
import com.ccc.www.bean.OrderBean;
import com.ccc.www.bean.SecondHandOrderBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.ccc.www.util.XListViewUtil;
import com.ccc.www.util.Xutils;
import com.ccc.www.view.XListView;
import com.ccc.www.view.XListView.IXListViewListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.utils.Log;

public class Order_Fragment2 extends BaseFragment {

	protected static final String TAG = "Order_Fragment2";
	View rootView;
	Activity activity;

	TextView fragment_order2_myorder1;
	TextView fragment_order2_myorder2;
	TextView fragment_order2_myorder3;
	XListView order_fragment_order2_xlistview1;
	XListView order_fragment_order2_xlistview2;
	XListView order_fragment_order2_xlistview3;

	int userid = 0;

	Adapter1 adapter1 = new Adapter1();
	Adapter2 adapter2 = new Adapter2();
	Adapter3 adapter3 = new Adapter3();

	List<List<OrderBean>> allMyShopOrder = new ArrayList<List<OrderBean>>();

	List<SecondHandOrderBean> allMySecondOrder = new ArrayList<SecondHandOrderBean>();

	List<List<OrderBean>> allSchoolShopOrder = new ArrayList<List<OrderBean>>();

	RefeshMyShopOrder refeshMyShopOrder = new RefeshMyShopOrder();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		activity = getActivity();

		activity.registerReceiver(refeshMyShopOrder, new IntentFilter(
				GlobalConstant.RefeshMyShopOrder));

		userid = UserUtil.getuserid(activity);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_order2, container,
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
		fragment_order2_myorder1 = (TextView) rootView
				.findViewById(R.id.fragment_order2_myorder1);
		fragment_order2_myorder2 = (TextView) rootView
				.findViewById(R.id.fragment_order2_myorder2);
		fragment_order2_myorder3 = (TextView) rootView
				.findViewById(R.id.fragment_order2_myorder3);
		order_fragment_order2_xlistview1 = (XListView) rootView
				.findViewById(R.id.order_fragment_order2_xlistview1);
		order_fragment_order2_xlistview2 = (XListView) rootView
				.findViewById(R.id.order_fragment_order2_xlistview2);
		order_fragment_order2_xlistview3 = (XListView) rootView
				.findViewById(R.id.order_fragment_order2_xlistview3);

		order_fragment_order2_xlistview1.setPullLoadEnable(false);
		order_fragment_order2_xlistview1.setPullRefreshEnable(true);
		order_fragment_order2_xlistview1
				.setXListViewListener(new IXListViewListener() {
					@Override
					public void onRefresh() {
						getmyshoporder();
					}

					@Override
					public void onLoadMore() {
						// TODO Auto-generated method stub
						XListViewUtil.endload(order_fragment_order2_xlistview1);
					}
				});
		order_fragment_order2_xlistview1.setAdapter(adapter1);

		order_fragment_order2_xlistview2.setPullLoadEnable(false);
		order_fragment_order2_xlistview2.setPullRefreshEnable(true);
		order_fragment_order2_xlistview2
				.setXListViewListener(new IXListViewListener() {
					@Override
					public void onRefresh() {
						getsecondaryorder();
					}

					@Override
					public void onLoadMore() {
						XListViewUtil.endload(order_fragment_order2_xlistview2);
					}
				});
		order_fragment_order2_xlistview2.setAdapter(adapter2);

		order_fragment_order2_xlistview3.setPullLoadEnable(false);
		order_fragment_order2_xlistview3.setPullRefreshEnable(true);
		order_fragment_order2_xlistview3
				.setXListViewListener(new IXListViewListener() {
					@Override
					public void onRefresh() {
						getschoolshoporder();
					}

					@Override
					public void onLoadMore() {
						XListViewUtil.endload(order_fragment_order2_xlistview3);
					}
				});
		order_fragment_order2_xlistview3.setAdapter(adapter3);
	}

	private void initdata() {
		getmyshoporder();
	}

	class Adapter1 extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allMyShopOrder.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allMyShopOrder.get(arg0);
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
						R.layout.item_orderfragment2_shoporder, null);
				holder.item_orderfragment2_shoporder_orderno = (TextView) view
						.findViewById(R.id.item_orderfragment2_shoporder_orderno);
				holder.item_orderfragment2_shoporder_price = (TextView) view
						.findViewById(R.id.item_orderfragment2_shoporder_price);
				holder.item_orderfragment2_shoporder_paytime = (TextView) view
						.findViewById(R.id.item_orderfragment2_shoporder_paytime);
				holder.item_orderfragment2_shoporder_status = (TextView) view
						.findViewById(R.id.item_orderfragment2_shoporder_status);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final OrderBean bean = allMyShopOrder.get(position).get(0);

			holder.item_orderfragment2_shoporder_orderno.setText("订单编号："
					+ bean.getOrder_no());

			DecimalFormat df = new DecimalFormat("######0.00");

			String allmoneyStr = df.format(bean.getOrder_sum_money());

			holder.item_orderfragment2_shoporder_price.setText("订单金额："
					+ allmoneyStr + "元");

			holder.item_orderfragment2_shoporder_paytime.setText("下单时间："
					+ bean.getPay_time());

			final int status = bean.getStatus();
			if (status == 0) {
				holder.item_orderfragment2_shoporder_status.setText("未发货");
			} else if (status == 1) {
				holder.item_orderfragment2_shoporder_status.setText("已经发货");
			} else if (status == 2) {
				holder.item_orderfragment2_shoporder_status.setText("未评价");
			} else if (status == 3) {
				holder.item_orderfragment2_shoporder_status.setText("已评价");
			}

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (status == 0) {
						Intent intent = new Intent();

						intent.putExtra("type", allMyShopOrder.get(position)
								.get(0).getType());

						Bundle bundle = new Bundle();
						bundle.putSerializable("bean",
								(Serializable) allMyShopOrder.get(position));
						intent.putExtras(bundle);
						intent.setClass(activity,
								MyShopOrderDetailActivity.class);
						startActivity(intent);
					}
				}
			});

			return view;
		}

		class ViewHolder {
			TextView item_orderfragment2_shoporder_orderno;
			TextView item_orderfragment2_shoporder_price;
			TextView item_orderfragment2_shoporder_paytime;
			TextView item_orderfragment2_shoporder_status;
		}
	}

	/**
	 * 校园购物订单
	 * 
	 * @author Administrator
	 * 
	 */
	class Adapter3 extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allSchoolShopOrder.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allSchoolShopOrder.get(arg0);
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
						R.layout.item_orderfragment2_shoporder, null);
				holder.item_orderfragment2_shoporder_orderno = (TextView) view
						.findViewById(R.id.item_orderfragment2_shoporder_orderno);
				holder.item_orderfragment2_shoporder_price = (TextView) view
						.findViewById(R.id.item_orderfragment2_shoporder_price);
				holder.item_orderfragment2_shoporder_paytime = (TextView) view
						.findViewById(R.id.item_orderfragment2_shoporder_paytime);
				holder.item_orderfragment2_shoporder_status = (TextView) view
						.findViewById(R.id.item_orderfragment2_shoporder_status);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final OrderBean bean = allSchoolShopOrder.get(position).get(0);

			holder.item_orderfragment2_shoporder_orderno.setText("订单编号："
					+ bean.getOrder_no());

			float allmoney = 0.0f;
			List<OrderBean> tempallorder = allSchoolShopOrder.get(position);
			for (int i = 0; i < tempallorder.size(); i++) {
				allmoney = allmoney
						+ (float) (tempallorder.get(i).getGoods_number() * tempallorder
								.get(i).getGoods_price());
			}
			holder.item_orderfragment2_shoporder_price.setText("订单金额："
					+ allmoney + "元");

			holder.item_orderfragment2_shoporder_paytime.setText("下单时间："
					+ bean.getPay_time());

			final int status = bean.getStatus();
			if (status == 0) {
				holder.item_orderfragment2_shoporder_status.setText("未发货");
			} else if (status == 1) {
				holder.item_orderfragment2_shoporder_status.setText("已经发货");
			}

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (status == 0) {
						Intent intent = new Intent();

						intent.putExtra("type", 3);

						Bundle bundle = new Bundle();
						bundle.putSerializable("bean",
								(Serializable) allSchoolShopOrder.get(position));
						intent.putExtras(bundle);
						intent.setClass(activity,
								MySchoolBuyOrderDetailActivity.class);
						startActivity(intent);
					}
				}
			});

			return view;
		}

		class ViewHolder {
			TextView item_orderfragment2_shoporder_orderno;
			TextView item_orderfragment2_shoporder_price;
			TextView item_orderfragment2_shoporder_paytime;
			TextView item_orderfragment2_shoporder_status;
		}
	}

	class Adapter2 extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allMySecondOrder.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allMySecondOrder.get(arg0);
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
						R.layout.item_orderfragment2_shoporder, null);
				holder.item_orderfragment2_shoporder_orderno = (TextView) view
						.findViewById(R.id.item_orderfragment2_shoporder_orderno);
				holder.item_orderfragment2_shoporder_price = (TextView) view
						.findViewById(R.id.item_orderfragment2_shoporder_price);
				holder.item_orderfragment2_shoporder_paytime = (TextView) view
						.findViewById(R.id.item_orderfragment2_shoporder_paytime);
				holder.item_orderfragment2_shoporder_status = (TextView) view
						.findViewById(R.id.item_orderfragment2_shoporder_status);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final SecondHandOrderBean bean = allMySecondOrder.get(position);

			holder.item_orderfragment2_shoporder_orderno.setText("订单编号："
					+ bean.getOrder_no());
			holder.item_orderfragment2_shoporder_price.setText("订单金额："
					+ bean.getOrder_sum_money() + "元");

			holder.item_orderfragment2_shoporder_paytime.setText("下单时间："
					+ bean.getPay_time());

			final int status = bean.getStatus();
			if (status == 0) {
				holder.item_orderfragment2_shoporder_status.setText("未发货");
			} else if (status == 1) {
				holder.item_orderfragment2_shoporder_status.setText("已经发货");
			}

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (status == 0) {
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putSerializable("bean", (Serializable) bean);
						intent.putExtras(bundle);
						intent.setClass(activity,
								MySecondOrderDetailActivity.class);
						startActivity(intent);
					}
				}
			});
			return view;
		}

		class ViewHolder {
			TextView item_orderfragment2_shoporder_orderno;
			TextView item_orderfragment2_shoporder_price;
			TextView item_orderfragment2_shoporder_paytime;
			TextView item_orderfragment2_shoporder_status;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fragment_order2_myorder1:
			fragment_order2_myorder1.setTextColor(getResources().getColor(
					R.color.white));
			fragment_order2_myorder1.setBackgroundColor(getResources()
					.getColor(R.color.topbar_bg));

			fragment_order2_myorder2.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order2_myorder2.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order2_myorder3.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order2_myorder3.setBackgroundColor(getResources()
					.getColor(R.color.white));

			order_fragment_order2_xlistview1.setVisibility(View.VISIBLE);
			order_fragment_order2_xlistview2.setVisibility(View.GONE);
			order_fragment_order2_xlistview3.setVisibility(View.GONE);

			getmyshoporder();

			break;
		case R.id.fragment_order2_myorder2:

			fragment_order2_myorder2.setTextColor(getResources().getColor(
					R.color.white));
			fragment_order2_myorder2.setBackgroundColor(getResources()
					.getColor(R.color.topbar_bg));

			fragment_order2_myorder1.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order2_myorder1.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order2_myorder3.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order2_myorder3.setBackgroundColor(getResources()
					.getColor(R.color.white));

			order_fragment_order2_xlistview1.setVisibility(View.GONE);
			order_fragment_order2_xlistview2.setVisibility(View.VISIBLE);
			order_fragment_order2_xlistview3.setVisibility(View.GONE);

			getsecondaryorder();
			break;
		case R.id.fragment_order2_myorder3:

			fragment_order2_myorder3.setTextColor(getResources().getColor(
					R.color.white));
			fragment_order2_myorder3.setBackgroundColor(getResources()
					.getColor(R.color.topbar_bg));

			fragment_order2_myorder1.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order2_myorder1.setBackgroundColor(getResources()
					.getColor(R.color.white));

			fragment_order2_myorder2.setTextColor(getResources().getColor(
					R.color.col_72));
			fragment_order2_myorder2.setBackgroundColor(getResources()
					.getColor(R.color.white));

			order_fragment_order2_xlistview1.setVisibility(View.GONE);
			order_fragment_order2_xlistview2.setVisibility(View.GONE);
			order_fragment_order2_xlistview3.setVisibility(View.VISIBLE);

			getschoolshoporder();

			break;
		default:
			break;
		}
	}

	/**
	 * 我的商铺订单
	 */
	private void getmyshoporder() {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", userid + "");
		params.addBodyParameter("shop_handle_type", "1");

		Xutils.loadData(HttpMethod.POST, GlobalConstant.GET_ORDER_ACTION,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "onSuccess " + response.result);

						if (TextUtils.isEmpty(returnstr)) {
							allMyShopOrder.removeAll(allMyShopOrder);
							adapter1.notifyDataSetChanged();
							XListViewUtil
									.endload(order_fragment_order2_xlistview1);
						} else {

							List<OrderBean> tempallMyShopOrder = new ArrayList<OrderBean>();

							if (returnstr.contains("]-[")) {

								int position = returnstr.indexOf("]-[");
								String orderstr = returnstr.substring(0,
										position + 1);
								String sockstr = returnstr.substring(
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

										int id = json.getInt("id");
										String order_no = json
												.getString("order_no");
										int goods_id = json.getInt("goods_id");
										int goods_cate_id = json
												.getInt("goods_cate_id");
										int goods_number = json
												.getInt("goods_number");
										String pay_time = json
												.getString("pay_time");
										int buy_user_id = json
												.getInt("buy_user_id");

										int supermaket_id = 0;
										if (json.has("supermaket_id")) {
											supermaket_id = json
													.getInt("supermaket_id");
										}

										String get_goods_person_name = json
												.getString("get_goods_person_name");
										String get_goods_person_phone = json
												.getString("get_goods_person_phone");
										String get_goods_person_address = json
												.getString("get_goods_person_address");
										float order_sum_money = (float) json
												.getDouble("order_sum_money");
										String rand_no = json
												.getString("rand_no");
										int status = json.getInt("status");

										// 0宿舍代理 1 商铺
										int type = 0;

										String sock_name = "";
										if (sock.has("sock_name")) {
											sock_name = sock
													.getString("sock_name");
										}

										double goods_price = 0;
										if (sock.has("goods_price")) {
											goods_price = sock
													.getDouble("goods_price");
										}

										if (sock.has("goods_name")) {
											sock_name = sock
													.getString("goods_name");
										}

										String sock_log = "";
										if (sock.has("sock_log")) {
											sock_log = sock
													.getString("sock_log");
										}

										if (sock.has("goods_log1")) {
											sock_log = sock
													.getString("goods_log1");
										}

										if (supermaket_id == 0) {
											type = 1;
										}

										OrderBean bean = new OrderBean(id,
												order_no, goods_id,
												goods_cate_id, goods_number,
												pay_time, buy_user_id,
												supermaket_id,
												get_goods_person_name,
												get_goods_person_phone,
												get_goods_person_address,
												order_sum_money, rand_no,
												status);
										bean.setType(type);
										bean.setSock_name(sock_name);
										bean.setSock_log(sock_log);
										bean.setGoods_price(goods_price);

										tempallMyShopOrder.add(bean);
									}

									Map<String, List<OrderBean>> map = new HashMap<String, List<OrderBean>>();
									for (int i = 0; i < tempallMyShopOrder
											.size(); i++) {
										OrderBean bean = tempallMyShopOrder
												.get(i);
										String orderno = bean.getOrder_no();

										if (map.containsKey(orderno)) {

											List<OrderBean> tempOrder = map
													.get(orderno);
											tempOrder.add(bean);
											map.put(orderno, tempOrder);
										} else {
											List<OrderBean> tempOrder = new ArrayList<OrderBean>();
											tempOrder.add(bean);
											map.put(orderno, tempOrder);
										}
									}

									Log.v(TAG, "map  " + map.size());

									// 第四种
									System.out
											.println("通过Map.values()遍历所有的value，但不能遍历key");

									allMyShopOrder.removeAll(allMyShopOrder);
									for (List<OrderBean> v : map.values()) {
										Log.v(TAG, "v  " + v.size());
										allMyShopOrder.add(v);
									}
									adapter1.notifyDataSetChanged();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							XListViewUtil
									.endload(order_fragment_order2_xlistview1);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {

						Log.v(TAG, "onFailure  " + arg1);

						dismissProgressDialog();
						showToast("加载失败");
						XListViewUtil.endload(order_fragment_order2_xlistview1);
					}
				});
	}

	/**
	 * 二手交易订单
	 */
	private void getsecondaryorder() {

		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", userid + "");
		params.addBodyParameter("click_type", "0");

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.GET_MY_SHOP_SEECOND_HAND_ORDER, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr " + response.result);
						if (TextUtils.isEmpty(returnstr)) {
							adapter2.notifyDataSetChanged();
							XListViewUtil
									.endload(order_fragment_order2_xlistview2);
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
									allMySecondOrder = tempallMySecondOrder;
									adapter2.notifyDataSetChanged();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							XListViewUtil
									.endload(order_fragment_order2_xlistview2);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {

						Log.v(TAG, "onFailure  " + arg1);

						dismissProgressDialog();
						showToast("加载失败");
						XListViewUtil.endload(order_fragment_order2_xlistview2);
					}
				});

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListener() {
		fragment_order2_myorder1.setOnClickListener(this);
		fragment_order2_myorder2.setOnClickListener(this);
		fragment_order2_myorder3.setOnClickListener(this);
	}

	/**
	 * 我的店铺订单刷新
	 * 
	 * @author Administrator
	 * 
	 */
	class RefeshMyShopOrder extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// 1.店铺购物订单 2.二手 3.校园购物
			int type = arg1.getIntExtra("type", 0);
			if (type == 1) {
				fragment_order2_myorder1.setTextColor(getResources().getColor(
						R.color.white));
				fragment_order2_myorder1.setBackgroundColor(getResources()
						.getColor(R.color.topbar_bg));

				fragment_order2_myorder2.setTextColor(getResources().getColor(
						R.color.col_72));
				fragment_order2_myorder2.setBackgroundColor(getResources()
						.getColor(R.color.white));

				fragment_order2_myorder3.setTextColor(getResources().getColor(
						R.color.col_72));
				fragment_order2_myorder3.setBackgroundColor(getResources()
						.getColor(R.color.white));

				order_fragment_order2_xlistview1.setVisibility(View.VISIBLE);
				order_fragment_order2_xlistview2.setVisibility(View.GONE);
				order_fragment_order2_xlistview3.setVisibility(View.GONE);

				getmyshoporder();
			} else if (type == 2) {
				fragment_order2_myorder2.setTextColor(getResources().getColor(
						R.color.white));
				fragment_order2_myorder2.setBackgroundColor(getResources()
						.getColor(R.color.topbar_bg));

				fragment_order2_myorder1.setTextColor(getResources().getColor(
						R.color.col_72));
				fragment_order2_myorder1.setBackgroundColor(getResources()
						.getColor(R.color.white));

				fragment_order2_myorder3.setTextColor(getResources().getColor(
						R.color.col_72));
				fragment_order2_myorder3.setBackgroundColor(getResources()
						.getColor(R.color.white));

				order_fragment_order2_xlistview1.setVisibility(View.GONE);
				order_fragment_order2_xlistview2.setVisibility(View.VISIBLE);
				order_fragment_order2_xlistview3.setVisibility(View.GONE);

				getsecondaryorder();
			} else {

				fragment_order2_myorder3.setTextColor(getResources().getColor(
						R.color.white));
				fragment_order2_myorder3.setBackgroundColor(getResources()
						.getColor(R.color.topbar_bg));

				fragment_order2_myorder1.setTextColor(getResources().getColor(
						R.color.col_72));
				fragment_order2_myorder1.setBackgroundColor(getResources()
						.getColor(R.color.white));

				fragment_order2_myorder2.setTextColor(getResources().getColor(
						R.color.col_72));
				fragment_order2_myorder2.setBackgroundColor(getResources()
						.getColor(R.color.white));

				order_fragment_order2_xlistview1.setVisibility(View.GONE);
				order_fragment_order2_xlistview2.setVisibility(View.GONE);
				order_fragment_order2_xlistview3.setVisibility(View.VISIBLE);

				getschoolshoporder();

			}

		}
	}

	/**
	 * 校园购物订单
	 */
	private void getschoolshoporder() {
		if (!BaseUtils.isNetWork(activity)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", userid + "");

		Log.v(TAG, "getschoolshoporder   userid   " + userid);

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.GET_SCHOOL_SHOP_ORDER_ACTION, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "onSuccess " + response.result);

						if (TextUtils.isEmpty(returnstr)) {
							allSchoolShopOrder.removeAll(allSchoolShopOrder);
							adapter3.notifyDataSetChanged();
							XListViewUtil
									.endload(order_fragment_order2_xlistview3);
						} else {
							List<OrderBean> tempallMyShopOrder = new ArrayList<OrderBean>();

							if (returnstr.contains("]-[")) {

								int position = returnstr.indexOf("]-[");
								String orderstr = returnstr.substring(0,
										position + 1);
								String sockstr = returnstr.substring(
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

										int id = json.getInt("id");
										String order_no = json
												.getString("order_no");
										int goods_id = json.getInt("goods_id");
										int goods_cate_id = json
												.getInt("goods_cate_id");
										int goods_number = json
												.getInt("goods_number");
										String pay_time = json
												.getString("pay_time");
										int buy_user_id = json
												.getInt("buy_user_id");

										int supermaket_id = 0;
										if (json.has("supermaket_id")) {
											supermaket_id = json
													.getInt("supermaket_id");
										}

										String get_goods_person_name = json
												.getString("get_goods_person_name");
										String get_goods_person_phone = json
												.getString("get_goods_person_phone");
										String get_goods_person_address = json
												.getString("get_goods_person_address");
										float order_sum_money = (float) json
												.getDouble("order_sum_money");
										String rand_no = json
												.getString("rand_no");
										int status = json.getInt("status");

										// 0宿舍代理 1 商铺
										int type = 0;

										String sock_name = "";
										if (sock.has("sock_name")) {
											type = 0;
											sock_name = sock
													.getString("sock_name");
										}

										double goods_price = 0;
										if (sock.has("goods_price")) {
											goods_price = sock
													.getDouble("goods_price");
										}

										if (sock.has("goods_name")) {
											type = 1;
											sock_name = sock
													.getString("goods_name");
										}

										String sock_log = "";
										if (sock.has("sock_log")) {
											type = 0;
											sock_log = sock
													.getString("sock_log");
										}

										if (sock.has("goods_log1")) {
											type = 1;
											sock_log = sock
													.getString("goods_log1");
										}

										OrderBean bean = new OrderBean(id,
												order_no, goods_id,
												goods_cate_id, goods_number,
												pay_time, buy_user_id,
												supermaket_id,
												get_goods_person_name,
												get_goods_person_phone,
												get_goods_person_address,
												order_sum_money, rand_no,
												status);
										bean.setType(type);
										bean.setSock_name(sock_name);
										bean.setSock_log(sock_log);
										bean.setGoods_price(goods_price);

										tempallMyShopOrder.add(bean);
									}

									Map<String, List<OrderBean>> map = new HashMap<String, List<OrderBean>>();
									for (int i = 0; i < tempallMyShopOrder
											.size(); i++) {
										OrderBean bean = tempallMyShopOrder
												.get(i);
										String orderno = bean.getOrder_no();

										if (map.containsKey(orderno)) {

											List<OrderBean> tempOrder = map
													.get(orderno);
											tempOrder.add(bean);
											map.put(orderno, tempOrder);
										} else {
											List<OrderBean> tempOrder = new ArrayList<OrderBean>();
											tempOrder.add(bean);
											map.put(orderno, tempOrder);
										}
									}

									Log.v(TAG, "map  " + map.size());

									// 第四种
									System.out
											.println("通过Map.values()遍历所有的value，但不能遍历key");

									allSchoolShopOrder
											.removeAll(allSchoolShopOrder);
									for (List<OrderBean> v : map.values()) {
										Log.v(TAG, "v  " + v.size());
										allSchoolShopOrder.add(v);
									}
									adapter3.notifyDataSetChanged();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							XListViewUtil
									.endload(order_fragment_order2_xlistview3);
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {

						Log.v(TAG, "onFailure  " + arg1);

						dismissProgressDialog();
						showToast("加载失败");
						XListViewUtil.endload(order_fragment_order2_xlistview3);
					}
				});

	}

	@Override
	public void onDestroy() {
		activity.unregisterReceiver(refeshMyShopOrder);
		super.onDestroy();
	}
}
