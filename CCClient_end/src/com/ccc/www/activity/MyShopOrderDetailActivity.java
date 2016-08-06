package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.OrderBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.ccc.www.util.Xutils;
import com.ccc.www.view.NoScrollListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的商品订单详情
 * 
 * @author Administrator
 * 
 */
public class MyShopOrderDetailActivity extends BaseActivity {
	String TAG = "MyShopOrderDetailActivity";

	List<OrderBean> allMyShopOrder = new ArrayList<OrderBean>();

	ImageButton ib_digital_goback;

	TextView myshoporderdetail_orderno;
	TextView myshoporderdetail_price;
	TextView myshoporderdetail_buytime;

	TextView myshoporderdetail_buyername;
	TextView myshoporderdetail_buyerphone;
	TextView myshoporderdetail_buyeraddr;
	TextView myshoporderdetail_delivergoods;
	NoScrollListView myshoporderdetail_goodslistview;

	LinearLayout myshoporderdetail_logistics_layout;
	EditText myshoporderdetail_logistics_no;
	EditText myshoporderdetail_logistics_companyname;

	int type = 0;

	int userid = 0;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		userid = UserUtil.getuserid(this);

		allMyShopOrder = (List<OrderBean>) getIntent().getSerializableExtra(
				"bean");
		type = allMyShopOrder.get(0).getType();
		setContentView(R.layout.activity_myshoporderdetail);

		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_digital_goback:
			MyShopOrderDetailActivity.this.finish();
			break;
		case R.id.myshoporderdetail_delivergoods:
			if (type != 0) {
				String kd_no = myshoporderdetail_logistics_no.getText()
						.toString().trim();
				String kd_company = myshoporderdetail_logistics_companyname
						.getText().toString().trim();

				if (TextUtils.isEmpty(kd_no)) {
					myshoporderdetail_logistics_no.requestFocus();
					showToast("请输入快递单号");
					return;
				}

				if (TextUtils.isEmpty(kd_company)) {
					myshoporderdetail_logistics_companyname.requestFocus();
					showToast("请输入快递公司名称");
					return;
				}
			}

			AlertDialog.Builder build = new Builder(
					MyShopOrderDetailActivity.this);

			build.setTitle("提示");
			build.setMessage("您确定发货吗？");
			build.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							if (allMyShopOrder != null) {

								if (type == 0) {
									proxydelivergoods(allMyShopOrder.get(0)
											.getOrder_no());
								} else {
									shopdelivergoods(allMyShopOrder.get(0)
											.getOrder_no());
								}
							}
						}
					});
			build.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub

						}
					});
			build.show();
			break;
		default:
			break;
		}
	}

	/**
	 * 商铺发货
	 */
	private void shopdelivergoods(String order_no) {

		Log.v(TAG, "shopdelivergoods");

		String kd_no = myshoporderdetail_logistics_no.getText().toString()
				.trim();
		String kd_company = myshoporderdetail_logistics_companyname.getText()
				.toString().trim();

		if (TextUtils.isEmpty(kd_no)) {
			myshoporderdetail_logistics_no.requestFocus();
			showToast("请输入快递单号");
			return;
		}

		if (TextUtils.isEmpty(kd_company)) {
			myshoporderdetail_logistics_companyname.requestFocus();
			showToast("请输入快递公司名称");
			return;
		}

		// TODO Auto-generated method stub
		if (!BaseUtils.isNetWork(MyShopOrderDetailActivity.this)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在提交发货");

		RequestParams params = new RequestParams();

		params.addBodyParameter("order_no", order_no);
		params.addBodyParameter("kd_no", kd_no);
		params.addBodyParameter("kd_company", kd_company);
		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.PRICATE_SHOP_SEND_GOODS, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "onSuccess " + response.result);

						if (TextUtils.isEmpty(returnstr)) {
							showToast("提交失败");
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);

								String code = json.getString("code");
								String msg = json.getString("msg");

								showToast(msg);

								if (!code.equalsIgnoreCase("0")) {
									
									// 推送代理进货发货
									int t_uid = allMyShopOrder.get(0)
											.getBuy_user_id();
									JpushAddMsg(userid, t_uid, "您的店铺购物订单已发货",
											"您的店铺购物订单已发货,请查看店铺购物订单");
									
									MyShopOrderDetailActivity.this.finish();

									// 发广播刷新列表
									Intent intent = new Intent();
									intent.putExtra("type", 1);
									intent.setAction(GlobalConstant.RefeshMyShopOrder);
									sendBroadcast(intent);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("提交失败");
						Log.v(TAG, "onFailure " + arg1);
					}
				});

	}

	/**
	 * 代理进货发货
	 */
	private void proxydelivergoods(String order_no) {

		Log.v(TAG, "proxydelivergoods");

		// TODO Auto-generated method stub
		if (!BaseUtils.isNetWork(MyShopOrderDetailActivity.this)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在提交发货");

		RequestParams params = new RequestParams();

		Log.v(TAG, "order_no " + order_no);

		params.addBodyParameter("order_no", order_no);
		Xutils.loadData(HttpMethod.POST, GlobalConstant.PRICATE_SUP_SEND_GOODS,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "onSuccess " + response.result);

						if (TextUtils.isEmpty(returnstr)) {
							showToast("提交失败");
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);

								String code = json.getString("code");
								String msg = json.getString("msg");

								showToast(msg);

								if (!code.equalsIgnoreCase("0")) {
									// 发广播刷新列表
									Intent intent = new Intent();
									intent.putExtra("type", 1);
									intent.setAction(GlobalConstant.RefeshMyShopOrder);
									sendBroadcast(intent);

									// 推送代理进货发货
									int t_uid = allMyShopOrder.get(0)
											.getBuy_user_id();
									JpushAddMsg(userid, t_uid, "您的私人超市订单已发货",
											"您的私人超市订单已发货,请查看私人超市订单");

									MyShopOrderDetailActivity.this.finish();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("提交失败");
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findviewWithId() {
		ib_digital_goback = (ImageButton) findViewById(R.id.ib_digital_goback);

		myshoporderdetail_orderno = (TextView) findViewById(R.id.myshoporderdetail_orderno);
		myshoporderdetail_price = (TextView) findViewById(R.id.myshoporderdetail_price);
		myshoporderdetail_buytime = (TextView) findViewById(R.id.myshoporderdetail_buytime);
		myshoporderdetail_buyername = (TextView) findViewById(R.id.myshoporderdetail_buyername);
		myshoporderdetail_buyerphone = (TextView) findViewById(R.id.myshoporderdetail_buyerphone);
		myshoporderdetail_buyeraddr = (TextView) findViewById(R.id.myshoporderdetail_buyeraddr);
		myshoporderdetail_delivergoods = (TextView) findViewById(R.id.myshoporderdetail_delivergoods);
		myshoporderdetail_goodslistview = (NoScrollListView) findViewById(R.id.myshoporderdetail_goodslistview);

		myshoporderdetail_logistics_layout = (LinearLayout) findViewById(R.id.myshoporderdetail_logistics_layout);
		myshoporderdetail_logistics_no = (EditText) findViewById(R.id.myshoporderdetail_logistics_no);
		myshoporderdetail_logistics_companyname = (EditText) findViewById(R.id.myshoporderdetail_logistics_companyname);

		if (type == 0) {
			myshoporderdetail_logistics_layout.setVisibility(View.GONE);
		} else {
			myshoporderdetail_logistics_layout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void initListener() {
		ib_digital_goback.setOnClickListener(this);
		myshoporderdetail_delivergoods.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		if (allMyShopOrder != null && allMyShopOrder.size() > 0) {

			myshoporderdetail_orderno.setText("订单编号:"
					+ allMyShopOrder.get(0).getOrder_no());

			myshoporderdetail_price.setText("￥"
					+ allMyShopOrder.get(0).getOrder_sum_money());

			myshoporderdetail_buytime.setText("下单时间:"
					+ allMyShopOrder.get(0).getPay_time());

			myshoporderdetail_buyername.setText("收货人姓名:"
					+ allMyShopOrder.get(0).getGet_goods_person_name());
			myshoporderdetail_buyerphone.setText("收货人电话:"
					+ allMyShopOrder.get(0).getGet_goods_person_phone());
			myshoporderdetail_buyeraddr.setText("收货人地址:"
					+ allMyShopOrder.get(0).getGet_goods_person_address());

			myshoporderdetail_goodslistview.setAdapter(new Adapter());

		}
	}

	class Adapter extends BaseAdapter {
		@Override
		public int getCount() {
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
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(MyShopOrderDetailActivity.this)
						.inflate(R.layout.item_order_goodsdetail, null);

				holder.item_order_goodsdetail_image = (ImageView) view
						.findViewById(R.id.item_order_goodsdetail_image);
				holder.item_order_goodsdetail_name = (TextView) view
						.findViewById(R.id.item_order_goodsdetail_name);
				holder.item_order_goodsdetail_price = (TextView) view
						.findViewById(R.id.item_order_goodsdetail_price);
				holder.item_order_goodsdetail_count = (TextView) view
						.findViewById(R.id.item_order_goodsdetail_count);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			OrderBean bean = allMyShopOrder.get(position);

			String img = bean.getSock_log();
			if (!img.startsWith("http")) {
				img = GlobalConstant.SERVER_URL + img;
			}

			ImageLoader.getInstance().displayImage(img,
					holder.item_order_goodsdetail_image,
					ImageLoaderOption.getoption());

			holder.item_order_goodsdetail_name.setText(bean.getSock_name());
			holder.item_order_goodsdetail_price.setText("￥"
					+ bean.getGoods_price());

			holder.item_order_goodsdetail_count.setText("购买数量："
					+ bean.getGoods_number());

			return view;
		}

		class ViewHolder {
			ImageView item_order_goodsdetail_image;
			TextView item_order_goodsdetail_name;
			TextView item_order_goodsdetail_price;
			TextView item_order_goodsdetail_count;
		}

	}

}
