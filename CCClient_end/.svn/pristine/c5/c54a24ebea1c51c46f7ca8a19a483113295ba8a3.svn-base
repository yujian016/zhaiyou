package com.ccc.www.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ccc.ccclient_end.R;
import com.ccc.www.alipay.AlipayUtil;
import com.ccc.www.alipay.PayResult;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.CheckUtil;
import com.ccc.www.util.GetBalanceInterface;
import com.ccc.www.util.GlobalConstant;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class OpenRedBagActivity extends BaseActivity {

	ImageButton ib_digital_goback;

	EditText act_openredbag_red_packet_sum_money;
	EditText act_openredbag_red_packet_sub_money;
	EditText act_openredbag_get_money_count;
	EditText act_openredbag_shop_name;
	EditText act_openredbag_valid_time;
	EditText act_openredbag_odds;
	Button act_openredbag_open;

	String red_packet_sum_money = "";

	String red_packet_sub_money = "";
	String valid_time = "";
	String shop_name = "";
	String get_money_count = "";
	String odds = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_openredbag);
		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_digital_goback:
			OpenRedBagActivity.this.finish();
			break;
		case R.id.act_openredbag_open:
			red_packet_sum_money = act_openredbag_red_packet_sum_money
					.getText().toString().trim();
			red_packet_sub_money = act_openredbag_red_packet_sub_money
					.getText().toString().trim();
			valid_time = act_openredbag_valid_time.getText().toString().trim();
			shop_name = act_openredbag_shop_name.getText().toString().trim();
			get_money_count = act_openredbag_get_money_count.getText()
					.toString().trim();
			odds = act_openredbag_odds.getText().toString().trim();

			if (TextUtils.isEmpty(red_packet_sum_money)) {
				showToast("请输入红包总金额,大于0的整数");
				act_openredbag_red_packet_sum_money.requestFocus();
				return;
			} else {
				boolean aa = CheckUtil.isInt(red_packet_sum_money);
				if (!aa) {
					showToast("请输入红包总金额,大于0的整数");
					act_openredbag_red_packet_sum_money.requestFocus();
					return;
				}
			}

			if (TextUtils.isEmpty(red_packet_sub_money)) {
				showToast("请输入中奖金额,大于0的整数");
				act_openredbag_red_packet_sub_money.requestFocus();
				return;
			} else {
				boolean aa = CheckUtil.isInt(red_packet_sub_money);
				if (!aa) {
					showToast("请输入中奖金额,大于0的整数");
					act_openredbag_red_packet_sub_money.requestFocus();
					return;
				}
			}

			if (TextUtils.isEmpty(get_money_count)) {
				showToast("请输入限制中奖次数,大于0的整数");
				act_openredbag_get_money_count.requestFocus();
				return;
			} else {
				boolean aa = CheckUtil.isInt(get_money_count);
				if (!aa) {
					showToast("请输入限制中奖次数,大于0的整数");
					act_openredbag_get_money_count.requestFocus();
					return;
				}
			}

			if (TextUtils.isEmpty(shop_name)) {
				showToast("请输入赞助商名称");
				act_openredbag_shop_name.requestFocus();
				return;
			}

			if (TextUtils.isEmpty(valid_time)) {
				showToast("请输入有效时间,大于0的整数");
				act_openredbag_valid_time.requestFocus();
				return;
			} else {
				boolean aa = CheckUtil.isInt(valid_time);
				if (!aa) {
					showToast("请输入有效时间,大于0的整数");
					act_openredbag_valid_time.requestFocus();
					return;
				}
			}

			if (TextUtils.isEmpty(odds)) {
				showToast("请输入中奖概率,大于0的整数");
				act_openredbag_odds.requestFocus();
				return;
			} else {
				boolean aa = CheckUtil.isInt(odds);
				if (!aa) {
					showToast("请输入中奖概率,大于0的整数");
					act_openredbag_odds.requestFocus();
					return;
				}
			}

//			getAccountBalance(new GetBalanceInterface() {
//				@Override
//				public void Callback(String returnstr) {
//
//					if (TextUtils.isEmpty(returnstr)) {
//						//
//						showToast("获取余额失败，请稍后重试");
//					} else {
//						try {
//							JSONObject json = new JSONObject(returnstr);
//							float balance = (float) json.getDouble("balance");

							/**
							 * 调用支付宝
							 */

//							boolean alipayinstall = AlipayUtil
//									.checkAlipayInstall(OpenRedBagActivity.this);
//							if (alipayinstall) {
//								checkalipay();
//							} else {
//								showToast("请先安装支付宝APP，并登录您的支付宝帐号");
//							}
			
			tochongzhi(red_packet_sum_money);

//						} catch (JSONException e) {
//							e.printStackTrace();
//							showToast("获取余额失败，请稍后重试");
//						}
//					}
//				}
//			});

			break;

		default:
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
				PayTask payTask = new PayTask(OpenRedBagActivity.this);
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

					Toast.makeText(OpenRedBagActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();

					// 提交给服务端
					tochongzhi(red_packet_sum_money);

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
					Toast.makeText(OpenRedBagActivity.this, "支付失败",
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
					callaliPay();
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

	private void tochongzhi(String red_packet_sum_money) {
		// TODO Auto-generated method stub

		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在提交");

		RequestParams params = new RequestParams();
		params.addBodyParameter("red_packet_sum_money", red_packet_sum_money);
		params.addBodyParameter("red_packet_sub_money", red_packet_sub_money);
		params.addBodyParameter("valid_time", valid_time);
		params.addBodyParameter("shop_name", shop_name);
		params.addBodyParameter("get_money_count", get_money_count);
		params.addBodyParameter("odds", odds);
		loadData(HttpMethod.POST, GlobalConstant.Open_RedBag_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("提交失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {

						dismissProgressDialog();
						String json = info.result;
						Log.v(TAG, json);
						if (BaseUtils.isEmpty(json)) {
							showToast("提交失败");
						} else {
							try {
								JSONObject jsonob = new JSONObject(json);
								String msg = jsonob.getString("msg");
								String code = jsonob.getString("code");
								showToast(msg);
								if (code.contains("1")) {
									OpenRedBagActivity.this.finish();
								}
							} catch (JSONException e) {
								showToast("提交失败");
								e.printStackTrace();
							}
						}
					}
				});
	}

	/**
	 * 调用支付宝
	 */
	private void callaliPay() {
		// 订单
		// String orderInfo = AlipayUtil.getOrderInfo(businessname,
		// businessdesc,
		// String.valueOf(allmoney));

		// String orderInfo = AlipayUtil.getOrderInfo("充值", "用户充值",
		// red_packet_sum_money);

		String orderInfo = AlipayUtil.getOrderInfo("充值", "用户充值", "0.01");

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
				PayTask alipay = new PayTask(OpenRedBagActivity.this);

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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findviewWithId() {
		ib_digital_goback = (ImageButton) findViewById(R.id.ib_digital_goback);

		act_openredbag_red_packet_sum_money = (EditText) findViewById(R.id.act_openredbag_red_packet_sum_money);
		act_openredbag_red_packet_sub_money = (EditText) findViewById(R.id.act_openredbag_red_packet_sub_money);
		act_openredbag_get_money_count = (EditText) findViewById(R.id.act_openredbag_get_money_count);
		act_openredbag_shop_name = (EditText) findViewById(R.id.act_openredbag_shop_name);
		act_openredbag_valid_time = (EditText) findViewById(R.id.act_openredbag_valid_time);
		act_openredbag_odds = (EditText) findViewById(R.id.act_openredbag_odds);
		act_openredbag_open = (Button) findViewById(R.id.act_openredbag_open);
	}

	@Override
	public void initListener() {
		ib_digital_goback.setOnClickListener(this);
		act_openredbag_open.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		// TODO Auto-generated method stub

	}

}
