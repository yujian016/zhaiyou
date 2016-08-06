package com.ccc.www.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.sdk.app.PayTask;
import com.ccc.ccclient_end.R;
import com.ccc.www.alipay.AlipayUtil;
import com.ccc.www.alipay.PayResult;
import com.ccc.www.bean.GoodsBean;
import com.ccc.www.bean.ShopGoodBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GetBalanceInterface;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SubmitGroupBuyOrderActivity extends BaseActivity {

	String TAG = "SubmitGroupBuyOrderActivity";
	
	
	ShopGoodBean goodsBean=null;
	
	ImageButton ib_gbo_goback;

	EditText et_gbo_person_name;
	EditText et_gbo_person_phone;
	EditText et_gbo_person_addr;
	TextView tv_gbo_balance;
	Button btn_gbo_pay_now;
	TextView tv_gbo_pay_money;
	
	int uid=0;
	
	// 收货人姓名
	String order_person_name = "";
	// 收货人联系电话
	String order_person_phone = "";
	// 收货人地址
	String order_address = "";
	// 订单总金额
	float order_money = 0.0f;
	float balance = 0.0f;
	
	
	private String businessname="";
	private String businessdesc="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submitgroupbuyorder);
		Intent data = getIntent();
		goodsBean = (ShopGoodBean) data
				.getSerializableExtra("bean");
		order_money=goodsBean.getGroup_buy_price();
		uid = UserUtil.getuserid(this);
		initview();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_gbo_goback:
			SubmitGroupBuyOrderActivity.this.finish();
			break;
		case R.id.btn_gbo_pay_now:
			order_person_name = et_gbo_person_name.getText().toString().trim();
			order_person_phone = et_gbo_person_phone.getText().toString().trim();
			order_address = et_gbo_person_addr.getText().toString().trim();
		
			if (TextUtils.isEmpty(order_person_name)) {
				showToast("请输入收货人姓名");
				et_gbo_person_name.requestFocus();
				return;
			}
		
			if (TextUtils.isEmpty(order_person_phone)) {
				showToast("请输入收货人联系电话");
				et_gbo_person_phone.requestFocus();
				return;
			}
		
			if (TextUtils.isEmpty(order_address)) {
				showToast("请输入收货地址");
				et_gbo_person_addr.requestFocus();
				return;
			}
			
			// 调支付宝之前的数据准备
			businessname="[团购]订单";
			businessdesc = "[团购]"+order_person_name + "的购物订单";
							
			if(balance>=order_money){
				// 使用余额支付
				submitorder(0);
			}else{
				//调用支付宝
				boolean alipayinstall = AlipayUtil
						.checkAlipayInstall(SubmitGroupBuyOrderActivity.this);
				if (alipayinstall) {
					checkalipay();
				} else {
					showToast("请先安装支付宝APP，并登录您的支付宝帐号");
				}
			}
			
			break;
		}
	}

	
	
	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void checkalipay() {
		Runnable checkRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(
						SubmitGroupBuyOrderActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}
	
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;

	private Handler mHandler = new Handler() {


		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {

					Toast.makeText(SubmitGroupBuyOrderActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					// 提交给服务端
					submitorder(1);

				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					// if (TextUtils.equals(resultStatus, "8000")) {
					// Toast.makeText(ProxyStockCartActivity.this, "支付结果确认中",
					// Toast.LENGTH_SHORT).show();
					//
					// } else {
					// // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					// Toast.makeText(ProxyStockCartActivity.this, "支付失败",
					// Toast.LENGTH_SHORT).show();
					// }
					Toast.makeText(SubmitGroupBuyOrderActivity.this, "支付失败",
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				// Toast.makeText(ProxyStockCartActivity.this, "检查结果为：" +
				// msg.obj,
				// Toast.LENGTH_SHORT).show();

				boolean isexist = Boolean.parseBoolean(msg.obj.toString());
				if (isexist) {
					callaliPay(businessname, businessdesc, order_money);
				} else {
					showToast("请先登录支付宝APP");
				}
				break;
			}
			default:
				break;
			}
		};
	};

	/**
	 * 调用支付宝
	 */
	private void callaliPay(String businessname, String businessdesc,
			float allmoney) {
		// 订单
		String orderInfo = AlipayUtil.getOrderInfo(businessname, businessdesc,
				String.valueOf(allmoney));

		// String orderInfo = AlipayUtil.getOrderInfo(businessname,
		// businessdesc,
		// "0.01");

		// 对订单做RSA 签名
		String sign = AlipayUtil.sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ AlipayUtil.getSignType();

		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(
						SubmitGroupBuyOrderActivity.this);

				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	
	/**
	 * 使用余额支付 pay_type 0 余额 1 支付宝
	 */
	private void submitorder(int pay_type) {
		
		//调用接口接口
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在提交");

		RequestParams params = new RequestParams();

		params.addBodyParameter("uid", uid+"");

		params.addBodyParameter("group_buy_info_id", goodsBean.getGroup_info_id()+"");

		params.addBodyParameter("goods_id", String.valueOf(goodsBean.getId()));
		params.addBodyParameter("order_person_name", String.valueOf(order_person_name));
		params.addBodyParameter("order_person_phone", String.valueOf(order_person_phone));
		params.addBodyParameter("order_address", String.valueOf(order_address));
		params.addBodyParameter("order_money", String.valueOf(order_money));
		params.addBodyParameter("pay_type", String.valueOf(pay_type));

		loadData(HttpMethod.POST, GlobalConstant.Add_group_buy_order, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "onSuccess  " + response.result);

						if (TextUtils.isEmpty(returnstr)) {
							showToast("提交失败");
						} else {
							try {
								final JSONObject json = new JSONObject(
										returnstr);
								String resultMsg = json.getString("msg");

								if (resultMsg.contains("失败")) {
									// 失败
									AlertDialog.Builder build = new Builder(
											SubmitGroupBuyOrderActivity.this);
									build.setTitle("提示");
									build.setMessage(resultMsg);
									build.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													// TODO Auto-generated
													// method stub

												}
											});
									build.show();
								} else {
									// 成功
									AlertDialog.Builder build = new Builder(
											SubmitGroupBuyOrderActivity.this);
									build.setTitle("提示");
									build.setMessage(resultMsg);
									build.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													//支付成功关闭订单页面
													SubmitGroupBuyOrderActivity.this.finish();
												}
											});
									build.show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
								showToast("提交失败");
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Log.v(TAG, "onFailure  ");
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
		// TODO Auto-generated method stub
		 ib_gbo_goback=(ImageButton) findViewById(R.id.ib_gbo_goback);

		 et_gbo_person_name=(EditText) findViewById(R.id.et_gbo_person_name);
		 et_gbo_person_phone=(EditText) findViewById(R.id.et_gbo_person_phone);
		 et_gbo_person_addr=(EditText) findViewById(R.id.et_gbo_person_addr);
		 tv_gbo_balance=(TextView) findViewById(R.id.tv_gbo_balance);
		 btn_gbo_pay_now=(Button) findViewById(R.id.btn_gbo_pay_now);
		 tv_gbo_pay_money=(TextView) findViewById(R.id.tv_gbo_pay_money);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		ib_gbo_goback.setOnClickListener(this);
		btn_gbo_pay_now.setOnClickListener(this);
	}

	
	
	@Override
	public void initdata() {
		// 获取账户余额
				getAccountBalance(new GetBalanceInterface() {
					@Override
					public void Callback(String returnstr) {
						if (TextUtils.isEmpty(returnstr)) {
							showToast("获取余额失败，请稍后重试");
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);
								balance = (float) json.getDouble("balance");

								// 检查收获地址是否正确
								String schoolname = UserUtil
										.getschoolname(SubmitGroupBuyOrderActivity.this);
								String hostelname = UserUtil
										.gethostelname(SubmitGroupBuyOrderActivity.this);
								String floorname = UserUtil
										.getfloorname(SubmitGroupBuyOrderActivity.this);

								String useraddr = schoolname + hostelname + floorname;

								payinfoConfirm(useraddr, balance);

							} catch (JSONException e) {
								e.printStackTrace();
								showToast("获取余额失败，请稍后重试");
							}
						}
					}
				});
	}
	
	/**
	 * 支付信息确认
	 * 
	 * @param KaizenId
	 */
	private void payinfoConfirm(String useraddr, final float balance) {
		tv_gbo_balance.setText(balance + "元");
		et_gbo_person_addr.setText(useraddr);
		tv_gbo_pay_money.setText(order_money + "元");
	}
	
	public void add_group_buy_order(ShopGoodBean bean){
		
		
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid", uid+"");
		params.addBodyParameter("group_buy_info_id", bean.getGroup_info_id()+"");
		params.addBodyParameter("goods_id", bean.getId()+"");
		
		params.addBodyParameter("order_person_name", bean.getGroup_info_id()+"");
		params.addBodyParameter("order_person_phone", bean.getGroup_info_id()+"");
		params.addBodyParameter("order_address", bean.getGroup_info_id()+"");
		params.addBodyParameter("order_money", bean.getGroup_buy_price()+"");
		params.addBodyParameter("pay_type", bean.getGroup_info_id()+"");

		Log.v(TAG, "Group_info_id  " + bean.getGroup_info_id());
		Log.v(TAG, "user_id  " + uid);
		
	}

}
