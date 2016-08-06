package com.ccc.www.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.SecondHandOrderBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.ccc.www.util.Xutils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 我的二手订单详情
 * 
 * @author Administrator
 * 
 */
public class MySecondOrderDetailActivity extends BaseActivity {
	String TAG = "MySecondOrderDetailActivity";

	SecondHandOrderBean bean = null;

	ImageButton ib_digital_goback;

	ImageView myshoporderdetail_img;
	TextView myshoporderdetail_name;
	TextView myshoporderdetail_price;
	TextView myshoporderdetail_count;
	TextView myshoporderdetail_buytime;
	TextView myshoporderdetail_buyername;
	TextView myshoporderdetail_buyerphone;
	TextView myshoporderdetail_buyeraddr;
	TextView myshoporderdetail_delivergoods;

	EditText myshoporderdetail_logistics_no;
	EditText myshoporderdetail_logistics_companyname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		bean = (SecondHandOrderBean) getIntent().getSerializableExtra("bean");

		Log.v(TAG, "bean  " + bean.getOrder_sum_money());

		setContentView(R.layout.activity_mysecondorderdetail);

		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_digital_goback:
			MySecondOrderDetailActivity.this.finish();
			break;
		case R.id.myshoporderdetail_delivergoods:

			final String kd_no = myshoporderdetail_logistics_no.getText()
					.toString().trim();
			final String kd_company = myshoporderdetail_logistics_companyname
					.getText().toString().trim();

			if (TextUtils.isEmpty(kd_no)) {
				showToast("请输入快递单号");
				myshoporderdetail_logistics_no.requestFocus();
				return;
			}

			if (TextUtils.isEmpty(kd_company)) {
				showToast("请输入快递公司名称");
				myshoporderdetail_logistics_companyname.requestFocus();
				return;
			}

			AlertDialog.Builder build = new Builder(
					MySecondOrderDetailActivity.this);

			build.setTitle("提示");
			build.setMessage("您确定发货吗？");
			build.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							if (bean != null) {
								delivergoods(bean.getId(), kd_no, kd_company);
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
	 * 发货
	 */
	private void delivergoods(int id, String kd_no, String kd_company) {

		Log.v(TAG, "delivergoods");

		// TODO Auto-generated method stub
		if (!BaseUtils.isNetWork(MySecondOrderDetailActivity.this)) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在提交发货");

		RequestParams params = new RequestParams();

		String order_id = String.valueOf(id);

		Log.v(TAG, "order_id " + order_id);

		params.addBodyParameter("order_id", order_id);
		params.addBodyParameter("click_type", "1");
		params.addBodyParameter("kd_no", kd_no);
		params.addBodyParameter("kd_company", kd_company);
		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.GET_MY_SHOP_SEECOND_HAND_ORDER_SEND, params,
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

								if (code.equalsIgnoreCase("1")) {

									JpushAddMsg(
											UserUtil.getuserid(MySecondOrderDetailActivity.this),
											bean.getBuy_user_id(), "您购买的二手物品  "
													+ bean.getTitle() + " 已发货",
											"您购买的二手物品  " + bean.getTitle()
													+ " 已发货,请查看二手交易订单");

									MySecondOrderDetailActivity.this.finish();

									// 发广播刷新列表
									Intent intent = new Intent();
									intent.putExtra("type", 2);
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

		myshoporderdetail_img = (ImageView) findViewById(R.id.myshoporderdetail_img);
		myshoporderdetail_name = (TextView) findViewById(R.id.myshoporderdetail_name);
		myshoporderdetail_price = (TextView) findViewById(R.id.myshoporderdetail_price);
		myshoporderdetail_count = (TextView) findViewById(R.id.myshoporderdetail_count);
		myshoporderdetail_buytime = (TextView) findViewById(R.id.myshoporderdetail_buytime);
		myshoporderdetail_buyername = (TextView) findViewById(R.id.myshoporderdetail_buyername);
		myshoporderdetail_buyerphone = (TextView) findViewById(R.id.myshoporderdetail_buyerphone);
		myshoporderdetail_buyeraddr = (TextView) findViewById(R.id.myshoporderdetail_buyeraddr);
		myshoporderdetail_delivergoods = (TextView) findViewById(R.id.myshoporderdetail_delivergoods);

		myshoporderdetail_logistics_no = (EditText) findViewById(R.id.myshoporderdetail_logistics_no);
		myshoporderdetail_logistics_companyname = (EditText) findViewById(R.id.myshoporderdetail_logistics_companyname);

	}

	@Override
	public void initListener() {
		ib_digital_goback.setOnClickListener(this);
		myshoporderdetail_delivergoods.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		// TODO Auto-generated method stub

		if (bean != null) {
			String socklog = bean.getLog1();
			if (!socklog.startsWith("http")) {
				socklog = GlobalConstant.SERVER_URL + socklog;
			}
			ImageLoader.getInstance().displayImage(socklog,
					myshoporderdetail_img,
					ImageLoaderOption.getoption());

			myshoporderdetail_name.setText(bean.getTitle());

			myshoporderdetail_price.setText("￥" + bean.getOrder_sum_money());

			myshoporderdetail_count.setText("购买数量:" + bean.getGoods_number());

			myshoporderdetail_buytime.setText("下单时间:" + bean.getPay_time());

			myshoporderdetail_buyername.setText("收货人姓名:"
					+ bean.getGet_goods_person_name());
			myshoporderdetail_buyerphone.setText("收货人电话:"
					+ bean.getGet_goods_person_phone());
			myshoporderdetail_buyeraddr.setText("收货人地址:"
					+ bean.getGet_goods_person_address());
		}

	}

}
