package com.ccc.www.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ccc.ccclient_end.R;
import com.ccc.www.alipay.AlipayUtil;
import com.ccc.www.alipay.PayResult;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.CheckUtil;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MyMoneyBagActivity extends BaseActivity {

	ImageButton ib_apply_shop_close;

	Button btn_add_money;
	Button btn_get_my_money;

	View rootview;

	int userid;

	String user_balance = "";

	float balance = 0.0f;

	TextView tv_my_money_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_money_bag);
		rootview = LayoutInflater.from(this).inflate(
				R.layout.activity_my_money_bag, null);

		userid = UserUtil.getuserid(this);

		initview();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_apply_shop_close:
			MyMoneyBagActivity.this.finish();
			break;
		case R.id.btn_add_money:
			chongzhi();
			break;
		case R.id.btn_get_my_money:
			tixian();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findviewWithId() {
		ib_apply_shop_close = (ImageButton) findViewById(R.id.ib_apply_shop_close);
		btn_add_money = (Button) findViewById(R.id.btn_add_money);
		btn_get_my_money = (Button) findViewById(R.id.btn_get_my_money);
		tv_my_money_number = (TextView) findViewById(R.id.tv_my_money_number);
	}

	@Override
	public void initListener() {
		ib_apply_shop_close.setOnClickListener(this);
		btn_add_money.setOnClickListener(this);
		btn_get_my_money.setOnClickListener(this);
	}

	/**
	 * 获取账户余额
	 */
	private void getbalance() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在获取账户余额");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id",
				String.valueOf(UserUtil.getuserid(this)));

		loadData(HttpMethod.POST, GlobalConstant.GET_USER_BALANCE, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub

						dismissProgressDialog();

						String returnstr2 = arg0.result;

						Log.v(TAG, "余额 " + returnstr2);

						try {
							JSONObject json = new JSONObject(returnstr2);
							balance = (float) json.getDouble("balance");

							tv_my_money_number.setText("￥" + balance + "元");

						} catch (JSONException e) {
							e.printStackTrace();
							showToast("获取余额失败，请稍后重试");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						dismissProgressDialog();
						Log.v(TAG, "onFailure " + arg1);

						showToast("获取余额失败，请稍后重试");

					}
				});

	}

	@Override
	public void initdata() {
		getbalance();
	}

	private void chongzhi() {
		View view1 = LayoutInflater.from(MyMoneyBagActivity.this).inflate(
				R.layout.pop_chongzhi, null);
		mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);

		final EditText pop_proxystocktip_name = (EditText) view1
				.findViewById(R.id.pop_proxystocktip_name);

		TextView pop_proxystocktip_cancel = (TextView) view1
				.findViewById(R.id.pop_proxystocktip_cancel);
		TextView pop_proxystocktip_pay = (TextView) view1
				.findViewById(R.id.pop_proxystocktip_pay);

		pop_proxystocktip_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dissmisspopwindow();
			}
		});
		pop_proxystocktip_pay.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				user_balance = pop_proxystocktip_name.getText().toString();

				if (TextUtils.isEmpty(user_balance)) {
					showToast("请输入充值金额");
					pop_proxystocktip_name.requestFocus();
					return;
				} else {
					boolean check = CheckUtil.isIntOrFloat(user_balance);
					if (!check) {
						showToast("请输入正确格式的金额");
						pop_proxystocktip_name.requestFocus();
						return;
					}
				}

				/**
				 * 调用支付宝
				 */

				boolean alipayinstall = AlipayUtil
						.checkAlipayInstall(MyMoneyBagActivity.this);
				if (alipayinstall) {
					checkalipay();
				} else {
					showToast("请先安装支付宝APP，并登录您的支付宝帐号");
				}

			}
		});
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
		mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
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
				PayTask payTask = new PayTask(MyMoneyBagActivity.this);
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

					dissmisspopwindow();

					Toast.makeText(MyMoneyBagActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();

					// 提交给服务端
					tochongzhi(user_balance);

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
					Toast.makeText(MyMoneyBagActivity.this, "支付失败",
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

	/**
	 * 调用支付宝
	 */
	private void callaliPay() {
		// 订单
		// String orderInfo = AlipayUtil.getOrderInfo(businessname,
		// businessdesc,
		// String.valueOf(allmoney));

		String orderInfo = AlipayUtil.getOrderInfo("充值", "用户充值", user_balance);

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
				PayTask alipay = new PayTask(MyMoneyBagActivity.this);

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

	private void tochongzhi(String user_balance) {
		// TODO Auto-generated method stub

		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在提交充值");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", String.valueOf(userid));
		params.addBodyParameter("user_balance", user_balance);
		loadData(HttpMethod.POST, GlobalConstant.ADD_BALANCE, params,
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
								String returnMsg = jsonob
										.getString("returnMsg");
								showToast(returnMsg);
								if (returnMsg.contains("成功")) {
									dissmisspopwindow();
									getbalance();
								}
							} catch (JSONException e) {
								showToast("提交失败");
								e.printStackTrace();
							}

						}
					}
				});

	}

	private void tixian() {
		View view1 = LayoutInflater.from(MyMoneyBagActivity.this).inflate(
				R.layout.pop_tixian, null);
		mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);

		final RadioButton pop_tixian_bank = (RadioButton) view1
				.findViewById(R.id.pop_tixian_bank);
		final RadioButton pop_tixian_alipay = (RadioButton) view1
				.findViewById(R.id.pop_tixian_alipay);

		final EditText pop_tixian_draw_blank_no = (EditText) view1
				.findViewById(R.id.pop_tixian_draw_blank_no);
		final EditText pop_tixian_draw_name = (EditText) view1
				.findViewById(R.id.pop_tixian_draw_name);
		final EditText pop_tixian_real_name = (EditText) view1
				.findViewById(R.id.pop_tixian_real_name);
		final EditText pop_tixian_draw_money = (EditText) view1
				.findViewById(R.id.pop_tixian_draw_money);
		TextView pop_tixian_cancel = (TextView) view1
				.findViewById(R.id.pop_tixian_cancel);
		TextView pop_tixian_submit = (TextView) view1
				.findViewById(R.id.pop_tixian_submit);

		pop_tixian_bank
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (pop_tixian_bank.isChecked()) {
							pop_tixian_draw_name.setText("");
							pop_tixian_draw_name.setEnabled(true);
						} else {
							pop_tixian_draw_name.setText("支付宝");
							pop_tixian_draw_name.setEnabled(false);
						}
					}
				});

		pop_tixian_alipay
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						if (pop_tixian_alipay.isChecked()) {
							pop_tixian_draw_name.setText("支付宝");
							pop_tixian_draw_name.setEnabled(false);
						} else {
							pop_tixian_draw_name.setText("");
							pop_tixian_draw_name.setEnabled(true);
						}
					}
				});

		pop_tixian_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dissmisspopwindow();
			}
		});
		pop_tixian_submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				String draw_blank_no = pop_tixian_draw_blank_no.getText()
						.toString().trim();
				String draw_name = pop_tixian_draw_name.getText().toString()
						.trim();
				String real_name = pop_tixian_real_name.getText().toString()
						.trim();
				String draw_money = pop_tixian_draw_money.getText().toString()
						.trim();

				if (TextUtils.isEmpty(draw_blank_no)) {
					showToast("请输入银行或支付宝账号");
					pop_tixian_draw_blank_no.requestFocus();
					return;
				}

				if (TextUtils.isEmpty(draw_name)) {
					showToast("请输入银行名称");
					pop_tixian_draw_name.requestFocus();
					return;
				}
				if (TextUtils.isEmpty(real_name)) {
					showToast("请输入真实姓名");
					pop_tixian_real_name.requestFocus();
					return;
				}
				if (TextUtils.isEmpty(draw_money)) {
					showToast("请输入提现金额");
					pop_tixian_draw_money.requestFocus();
					return;
				} else {

					Log.v(TAG, "draw_money  " + draw_money);

					boolean check = CheckUtil.isInt(draw_money);
					if (check) {

						Integer moneyint = Integer.parseInt(draw_money);

						Log.v(TAG, "moneyint  " + moneyint);
						Log.v(TAG, "balance  " + balance);

						if (moneyint > balance) {
							showToast("您的金额不足" + moneyint + "元");
							pop_tixian_draw_money.requestFocus();
							return;
						}
					} else {
						showToast("提现金额必须是大于0的整数");
						pop_tixian_draw_money.requestFocus();
						return;
					}
				}
				totixian(draw_blank_no, draw_name, real_name, draw_money);
			}
		});
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
		mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
	}

	PopupWindow mPopupWindow;

	private void dissmisspopwindow() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

	private void totixian(String draw_blank_no, String draw_name,
			String real_name, String draw_money) {

		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在提交提现请求");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", String.valueOf(userid));
		params.addBodyParameter("draw_blank_no", draw_blank_no);
		params.addBodyParameter("draw_name", draw_name);
		params.addBodyParameter("real_name", real_name);
		params.addBodyParameter("draw_money", draw_money);
		loadData(HttpMethod.POST, GlobalConstant.WIDTH_BALANCE, params,
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
						Log.v(TAG, "json  " + json);
						if (BaseUtils.isEmpty(json)) {
							showToast("提交失败");
						} else {
							try {
								JSONObject jsonob = new JSONObject(json);
								String returnMsg = jsonob
										.getString("returnMsg");

								String code = jsonob.getString("code");
								if (code.equalsIgnoreCase("1")) {
									dissmisspopwindow();
									getbalance();

									// 推送提现成功

								}
								showToast(returnMsg);
							} catch (JSONException e) {
								showToast("提交失败");
								e.printStackTrace();
							}
						}
					}
				});
	}

}
