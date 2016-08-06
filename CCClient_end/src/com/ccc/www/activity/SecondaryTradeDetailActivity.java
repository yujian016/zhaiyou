package com.ccc.www.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ccc.ccclient_end.R;
import com.ccc.www.alipay.AlipayUtil;
import com.ccc.www.alipay.PayResult;
import com.ccc.www.bean.SecondaryTradeBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GetBalanceInterface;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.ImageUtil;
import com.ccc.www.util.UserUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SecondaryTradeDetailActivity extends BaseActivity {

	private ImageButton ib_goods_detail_goback;

	String TAG = "SecondaryTradeDetailActivity";

	TextView tv_goods_detail_title;
	TextView activity_digitalrepair_detail_title;
	TextView activity_digitalrepair_detail_detail;
	TextView activity_digitalrepair_detail_phone;
	Button activity_digitalrepair_detail_call;

	ImageView event_pic1;
	ImageView event_pic2;
	ImageView event_pic3;

	SecondaryTradeBean bean = null;

	TextView add_secondarytradecart;

	int userid;

	View rootview;

	// 收货人姓名
	String get_goods_person_name = "";
	// 收货人联系电话
	String get_goods_person_phone = "";
	// 收货人地址
	String get_goods_person_address = "";
	// 订单总金额
	float order_sum_money = 0.0f;
	// 随机码
	String rand_no = "";

	// 商品名称
	String businessname = "";
	// 商品描述
	String businessdesc = "";

	PaySuccessToMyOrder paySuccessToMyOrder = new PaySuccessToMyOrder();

	@Override
	protected void onDestroy() {
		unregisterReceiver(paySuccessToMyOrder);
		super.onDestroy();
	}

	/**
	 * 支付成功后关闭页面
	 * 
	 * @author Administrator
	 * 
	 */
	class PaySuccessToMyOrder extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {

			SecondaryTradeDetailActivity.this
					.setResult(SecondaryTradeActivity.CloseSecondaryTrade);

			SecondaryTradeDetailActivity.this.finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		registerReceiver(paySuccessToMyOrder, new IntentFilter(
				GlobalConstant.PaySuccessToMyOrder));

		rootview = LayoutInflater.from(this).inflate(
				R.layout.activity_secondarytrade_detail, null);

		bean = (SecondaryTradeBean) getIntent().getSerializableExtra("bean");

		userid = UserUtil.getuserid(this);

		setContentView(R.layout.activity_secondarytrade_detail);
		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_goods_detail_goback:
			this.finish();
			break;
		case R.id.add_secondarytradecart:
			// 验证自己能否购买该二手物品
			checkCanBuy();

			// boolean isexist = DBUtil.CheckSecondaryTradeCartGoodsExist(
			// SecondaryTradeDetailActivity.this, bean.getId(), userid);
			// if (isexist) {
			// showToast("该商品已经在购物车了");
			// } else {
			// DBUtil.insertSecondaryTradeCart(
			// SecondaryTradeDetailActivity.this, bean, userid);
			// showToast("加入成功");
			//
			// Intent intent = new Intent();
			// intent.setAction(GlobalConstant.UpdateSecondaryTradeCart);
			// sendBroadcast(intent);
			//
			// }
			break;
		case R.id.activity_digitalrepair_detail_call:
			if (!TextUtils.isEmpty(bean.getPhone())) {
				// intent启动拨打电话
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ bean.getPhone()));
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * //验证自己能否购买该二手物品
	 */
	private void checkCanBuy() {
		if (bean != null) {
			if (!BaseUtils.isNetWork(getApplicationContext())) {
				showToast("请检查您的网络");
				return;
			}

			showLoading2("正在加载");

			String user_id = String.valueOf(userid);
			String goods_id = String.valueOf(bean.getId());

			RequestParams params = new RequestParams();
			params.addBodyParameter("goods_id", goods_id);
			params.addBodyParameter("user_id", user_id);

			Log.v(TAG, "goods_id  " + goods_id);
			Log.v(TAG, "user_id  " + user_id);

			loadData(HttpMethod.POST, GlobalConstant.GET_SECOND_IS_MY_GOOD,
					params, new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dismissProgressDialog();
							showToast("加载失败");

							Log.v(TAG, "onFailure  " + arg1);
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							dismissProgressDialog();
							String json = info.result;
							Log.v(TAG, "json  " + json);
							if (BaseUtils.isEmpty(json)) {
								showToast("加载失败");
							} else {
								try {
									JSONObject obj = new JSONObject(json);
									String code = obj.getString("code");
									String msg = obj.getString("msg");

									if (code.equalsIgnoreCase("1")) {
										showToast(msg);
									} else {
										// 获取账户余额
										getAccountBalance(new GetBalanceInterface() {
											@Override
											public void Callback(
													String returnstr) {
												if (TextUtils
														.isEmpty(returnstr)) {
													showToast("获取余额失败，请稍后重试");
												} else {
													try {
														JSONObject json = new JSONObject(
																returnstr);
														float balance = (float) json
																.getDouble("balance");

														// 检查收获地址是否正确
														String schoolname = UserUtil
																.getschoolname(SecondaryTradeDetailActivity.this);
														String hostelname = UserUtil
																.gethostelname(SecondaryTradeDetailActivity.this);
														String floorname = UserUtil
																.getfloorname(SecondaryTradeDetailActivity.this);

														String useraddr = schoolname
																+ hostelname
																+ floorname;

														payinfoConfirm(
																useraddr,
																balance);

													} catch (JSONException e) {
														e.printStackTrace();
														showToast("获取余额失败，请稍后重试");
													}
												}
											}
										});
									}
								} catch (JSONException e) {
									showToast("加载失败");
									e.printStackTrace();
								}

							}
						}
					});

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findviewWithId() {
		ib_goods_detail_goback = (ImageButton) findViewById(R.id.ib_goods_detail_goback);

		tv_goods_detail_title = (TextView) findViewById(R.id.tv_goods_detail_title);
		activity_digitalrepair_detail_title = (TextView) findViewById(R.id.activity_digitalrepair_detail_title);
		activity_digitalrepair_detail_detail = (TextView) findViewById(R.id.activity_digitalrepair_detail_detail);
		activity_digitalrepair_detail_phone = (TextView) findViewById(R.id.activity_digitalrepair_detail_phone);
		activity_digitalrepair_detail_call = (Button) findViewById(R.id.activity_digitalrepair_detail_call);

		event_pic1 = (ImageView) findViewById(R.id.event_pic1);
		event_pic2 = (ImageView) findViewById(R.id.event_pic2);
		event_pic3 = (ImageView) findViewById(R.id.event_pic3);

		add_secondarytradecart = (TextView) findViewById(R.id.add_secondarytradecart);
	}

	@Override
	public void initListener() {
		ib_goods_detail_goback.setOnClickListener(this);
		activity_digitalrepair_detail_call.setOnClickListener(this);
		add_secondarytradecart.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		String company_log1 = GlobalConstant.SERVER_URL + bean.getLog1();

		String company_log2 = GlobalConstant.SERVER_URL + bean.getLog2();
		String company_log3 = GlobalConstant.SERVER_URL + bean.getLog3();

		final String[] urls = new String[3];
		urls[0] = company_log1;
		urls[1] = company_log2;
		urls[2] = company_log3;

		if (!TextUtils.isEmpty(company_log1)) {
			event_pic1.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(company_log1, event_pic1,
					ImageLoaderOption.getoption());

			event_pic1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					ImageUtil.toShowImages(SecondaryTradeDetailActivity.this,
							urls, 0);
				}
			});

		} else {
			event_pic1.setVisibility(View.INVISIBLE);
		}

		if (!TextUtils.isEmpty(company_log2)) {
			event_pic2.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(company_log2, event_pic2,
					ImageLoaderOption.getoption());
			event_pic2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					ImageUtil.toShowImages(SecondaryTradeDetailActivity.this,
							urls, 1);
				}
			});

		} else {
			event_pic2.setVisibility(View.INVISIBLE);
		}

		if (!TextUtils.isEmpty(company_log3)) {
			event_pic3.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(company_log3, event_pic3,
					ImageLoaderOption.getoption());

			event_pic3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					ImageUtil.toShowImages(SecondaryTradeDetailActivity.this,
							urls, 2);
				}
			});
		} else {
			event_pic3.setVisibility(View.INVISIBLE);
		}

		activity_digitalrepair_detail_title.setText(bean.getTitle());
		activity_digitalrepair_detail_detail.setText("\t" + bean.getDetail());
		activity_digitalrepair_detail_phone.setText("联系电话：" + bean.getPhone());
	}

	PopupWindow mPopupWindow;

	private void dissmisspopwindow() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

	/**
	 * 计算总金额
	 */
	private float calculateTotalMoney() {
		float allmoney = bean.getPrice();
		return allmoney;
	}

	/**
	 * 支付信息确认
	 * 
	 * @param KaizenId
	 */
	private void payinfoConfirm(String useraddr, final float balance) {

		View view1 = LayoutInflater.from(SecondaryTradeDetailActivity.this)
				.inflate(R.layout.pop_proxystocktip, null);
		mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);

		final EditText pop_proxystocktip_name = (EditText) view1
				.findViewById(R.id.pop_proxystocktip_name);
		final EditText pop_proxystocktip_phone = (EditText) view1
				.findViewById(R.id.pop_proxystocktip_phone);
		final EditText pop_proxystocktip_addr = (EditText) view1
				.findViewById(R.id.pop_proxystocktip_addr);
		TextView pop_proxystocktip_money = (TextView) view1
				.findViewById(R.id.pop_proxystocktip_money);
		TextView pop_proxystocktip_cancel = (TextView) view1
				.findViewById(R.id.pop_proxystocktip_cancel);
		TextView pop_proxystocktip_pay = (TextView) view1
				.findViewById(R.id.pop_proxystocktip_pay);

		TextView pop_proxystocktip_balance = (TextView) view1
				.findViewById(R.id.pop_proxystocktip_balance);
		pop_proxystocktip_balance.setText(balance + "元");

		pop_proxystocktip_addr.setText(useraddr);

		order_sum_money = calculateTotalMoney();
		pop_proxystocktip_money.setText(order_sum_money + "元");

		pop_proxystocktip_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dissmisspopwindow();
			}
		});
		pop_proxystocktip_pay.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				get_goods_person_name = pop_proxystocktip_name.getText()
						.toString().trim();
				get_goods_person_phone = pop_proxystocktip_phone.getText()
						.toString().trim();
				get_goods_person_address = pop_proxystocktip_addr.getText()
						.toString().trim();

				if (TextUtils.isEmpty(get_goods_person_name)) {
					showToast("请输入收货人姓名");
					pop_proxystocktip_name.requestFocus();
					return;
				}

				if (TextUtils.isEmpty(get_goods_person_phone)) {
					showToast("请输入收货人联系电话");
					pop_proxystocktip_phone.requestFocus();
					return;
				}

				if (TextUtils.isEmpty(get_goods_person_address)) {
					showToast("请输入收货地址");
					pop_proxystocktip_addr.requestFocus();
					return;
				}

				businessname = "(二手交易)" + bean.getTitle();

				businessdesc = get_goods_person_name + "的购物订单";

				// 判断余额是否足够
				if (balance >= order_sum_money) {
					// 使用余额支付
					/**
					 * 
					 */
					submitorder(0);
				} else {
					/**
					 * 调用支付宝
					 */
					boolean alipayinstall = AlipayUtil
							.checkAlipayInstall(SecondaryTradeDetailActivity.this);
					if (alipayinstall) {
						checkalipay();
					} else {
						showToast("请先安装支付宝APP，并登录您的支付宝帐号");
					}
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
				PayTask payTask = new PayTask(SecondaryTradeDetailActivity.this);
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
				PayTask alipay = new PayTask(SecondaryTradeDetailActivity.this);

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

					Toast.makeText(SecondaryTradeDetailActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();

					// 清空当前用户的代理购物车
					DBUtil.deleteProxyStockCart(
							SecondaryTradeDetailActivity.this, userid);
					// 发广播修改代理进货界面数量变化
					Intent updateProxyStockCartCount = new Intent();
					updateProxyStockCartCount
							.setAction(GlobalConstant.UpdateProxyStockCartCount);
					sendBroadcast(updateProxyStockCartCount);

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
					Toast.makeText(SecondaryTradeDetailActivity.this, "支付失败",
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
					callaliPay(businessname, businessdesc, order_sum_money);
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
	 * 支付成功后提交订单给服务器 pay_type 0 余额 1 支付宝
	 */
	private void submitorder(int pay_type) {

		rand_no = AlipayUtil.getRandomString();

		JsonArray jsonarr = new JsonArray();

		JsonObject jsonobj = new JsonObject();
		jsonobj.addProperty("user_id", bean.getUser_id());
		jsonobj.addProperty("goods_id", bean.getId());
		jsonobj.addProperty("goods_number", 1);
		jsonarr.add(jsonobj);

		String second_order = jsonarr.toString();

		JsonObject second_buy_user_infoobj = new JsonObject();
		second_buy_user_infoobj.addProperty("buy_user_id", userid);
		second_buy_user_infoobj.addProperty("get_goods_person_name",
				get_goods_person_name);
		second_buy_user_infoobj.addProperty("get_goods_person_phone",
				get_goods_person_phone);
		second_buy_user_infoobj.addProperty("get_goods_person_address",
				get_goods_person_address);
		second_buy_user_infoobj.addProperty("order_sum_money", order_sum_money);
		second_buy_user_infoobj.addProperty("rand_no", rand_no);

		String second_buy_user_info = second_buy_user_infoobj.toString();

		// 掉接口
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在提交");

		RequestParams params = new RequestParams();
		params.addBodyParameter("second_buy_user_info", second_buy_user_info);

		params.addBodyParameter("second_order", second_order);
		params.addBodyParameter("pay_type", String.valueOf(pay_type));

		Log.v(TAG, "second_order   " + second_order);
		Log.v(TAG, "second_buy_user_info   " + second_buy_user_info);
		Log.v(TAG, "pay_type   " + pay_type);

		loadData(HttpMethod.POST, GlobalConstant.ADD_ERSHOU_ORDER, params,
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
								String resultMsg = json.getString("resultMsg");

								if (resultMsg.contains("失败")) {
									// 失败
									AlertDialog.Builder build = new Builder(
											SecondaryTradeDetailActivity.this);
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
									JpushGetIMEI_BY_UID__Push(
											bean.getUser_id(), "您有二手交易新订单",
											"您有二手交易新订单,请查看我的二手订单");

									JpushAddMsg(userid, bean.getUser_id(),
											"您有二手交易新订单", "您有二手交易新订单,请查看我的二手订单");

									// 成功
									final String orderNo = json
											.getString("orderNo");
									AlertDialog.Builder build = new Builder(
											SecondaryTradeDetailActivity.this);
									build.setTitle("提示");
									build.setMessage(resultMsg);
									build.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {

													dissmisspopwindow();

													// 删除二手购物车
													DBUtil.deleteSecondaryTradeCart(
															SecondaryTradeDetailActivity.this,
															userid);

													// 发送支付成功的广播，跳转去我的订单页面
													Intent PaySuccessToMyOrder = new Intent();
													PaySuccessToMyOrder
															.setAction(GlobalConstant.PaySuccessToMyOrder);
													sendBroadcast(PaySuccessToMyOrder);

													Intent UpdateSecondaryTradeCart = new Intent();
													UpdateSecondaryTradeCart
															.setAction(GlobalConstant.UpdateSecondaryTradeCart);
													sendBroadcast(UpdateSecondaryTradeCart);

													Intent intent = new Intent();
													intent.putExtra("orderNo",
															orderNo);
													intent.setClass(
															SecondaryTradeDetailActivity.this,
															OrderActivity.class);
													startActivity(intent);
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
}
