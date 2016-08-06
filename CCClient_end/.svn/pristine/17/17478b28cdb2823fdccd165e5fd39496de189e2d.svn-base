package com.ccc.www.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ccc.ccclient_end.R;
import com.ccc.www.alipay.AlipayUtil;
import com.ccc.www.alipay.PayResult;
import com.ccc.www.bean.CouponBean;
import com.ccc.www.bean.GoodsBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GetBalanceInterface;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.ccc.www.view.NoScrollListView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 潮流数码订单提交界面
 * 
 * @author Administrator
 * 
 */
public class DigitalCartSubmitOrderActivity extends BaseActivity {

	String TAG = "DigitalCartSubmitOrderActivity";

	int userid;
	List<GoodsBean> allchoicesock = new ArrayList<GoodsBean>();
	List<CouponBean> allCouponBean = new ArrayList<CouponBean>();

	ImageButton ib_digital_goback;

	EditText pop_proxystocktip_name;
	EditText pop_proxystocktip_phone;
	EditText pop_proxystocktip_addr;
	TextView pop_proxystocktip_money;
	Button pop_proxystocktip_pay;
	TextView pop_proxystocktip_balance;

	NoScrollListView couponlist;
	TextView nocouponlist;

	CouponAdapter couponAdapter = new CouponAdapter();

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

	float balance = 0.0f;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent data = getIntent();
		userid = data.getIntExtra("userid", 0);
		allchoicesock = (List<GoodsBean>) data
				.getSerializableExtra("allchoicesock");

		if (allchoicesock == null) {
			allchoicesock = new ArrayList<GoodsBean>();
		}

		Log.v(TAG, "allchoicesock  " + allchoicesock.size());

		setContentView(R.layout.activity_privatesupermarketcartsubmitorder);
		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_digital_goback:
			DigitalCartSubmitOrderActivity.this.finish();
			break;
		case R.id.pop_proxystocktip_pay:

			get_goods_person_name = pop_proxystocktip_name.getText().toString()
					.trim();
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

			// 调支付宝之前的数据准备

			businessname = "";
			for (int i = 0; i < allchoicesock.size(); i++) {
				if (i == 0) {
					businessname = "(潮流数码)"
							+ allchoicesock.get(i).getGoods_name();
				} else {
					businessname = businessname + ","
							+ allchoicesock.get(i).getGoods_name();
				}
			}
			businessdesc = get_goods_person_name + "的购物订单";

			Log.v(TAG, "order_sum_money  " + order_sum_money);

			// 判断余额是否足够
			if (balance >= order_sum_money) {
				// 使用余额支付
				submitorder(0);
			} else {
				/**
				 * 调用支付宝
				 */
				boolean alipayinstall = AlipayUtil
						.checkAlipayInstall(DigitalCartSubmitOrderActivity.this);
				if (alipayinstall) {
					checkalipay();
				} else {
					showToast("请先安装支付宝APP，并登录您的支付宝帐号");
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
		ib_digital_goback = (ImageButton) findViewById(R.id.ib_digital_goback);

		pop_proxystocktip_name = (EditText) findViewById(R.id.pop_proxystocktip_name);
		pop_proxystocktip_phone = (EditText) findViewById(R.id.pop_proxystocktip_phone);
		pop_proxystocktip_addr = (EditText) findViewById(R.id.pop_proxystocktip_addr);
		pop_proxystocktip_money = (TextView) findViewById(R.id.pop_proxystocktip_money);
		pop_proxystocktip_pay = (Button) findViewById(R.id.pop_proxystocktip_pay);
		pop_proxystocktip_balance = (TextView) findViewById(R.id.pop_proxystocktip_balance);
		couponlist = (NoScrollListView) findViewById(R.id.couponlist);
		nocouponlist = (TextView) findViewById(R.id.nocouponlist);

		couponlist.setAdapter(couponAdapter);
	}

	class CouponAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allCouponBean.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allCouponBean.get(arg0);
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
				view = LayoutInflater.from(DigitalCartSubmitOrderActivity.this)
						.inflate(R.layout.item_order_choose_coupon, null);

				holder.item_order_choose_coupon_check = (CheckBox) view
						.findViewById(R.id.item_order_choose_coupon_check);
				holder.item_order_choose_coupon_img = (ImageView) view
						.findViewById(R.id.item_order_choose_coupon_img);
				holder.item_order_choose_coupon_detail = (TextView) view
						.findViewById(R.id.item_order_choose_coupon_detail);
				holder.item_order_choose_coupon_price = (TextView) view
						.findViewById(R.id.item_order_choose_coupon_price);
				holder.item_order_choose_coupon_desc = (TextView) view
						.findViewById(R.id.item_order_choose_coupon_desc);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final CouponBean bean = allCouponBean.get(position);

			if (bean.isCheck()) {
				holder.item_order_choose_coupon_check.setChecked(true);
			} else {
				holder.item_order_choose_coupon_check.setChecked(false);
			}

			String img = bean.getCoupon_log();
			if (!img.startsWith("http")) {
				img = GlobalConstant.SERVER_URL + img;
			}
			ImageLoader.getInstance().displayImage(img,
					holder.item_order_choose_coupon_img,
					ImageLoaderOption.getoption());

			holder.item_order_choose_coupon_detail.setText(bean.getDetail());

			holder.item_order_choose_coupon_price.setText("￥"
					+ bean.getCoupon_price());

			int money = bean.getUse_coupon_money();
			if (money > 0) {
				holder.item_order_choose_coupon_desc.setText("满" + money
						+ "元可用");
			} else {
				holder.item_order_choose_coupon_desc.setText("无使用限制");
			}

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					boolean beancheck = bean.isCheck();

					if (!beancheck) {
						for (int i = 0; i < allCouponBean.size(); i++) {
							allCouponBean.get(i).setCheck(false);
						}
						allCouponBean.get(position).setCheck(true);

						float price = allCouponBean.get(position)
								.getCoupon_price();

						order_sum_money = calculateTotalMoney();

						order_sum_money = order_sum_money - price;

						pop_proxystocktip_money.setText(order_sum_money + "元");

					} else {
						for (int i = 0; i < allCouponBean.size(); i++) {
							allCouponBean.get(i).setCheck(false);
						}
						allCouponBean.get(position).setCheck(false);

						order_sum_money = calculateTotalMoney();
						pop_proxystocktip_money.setText(order_sum_money + "元");
					}
					notifyDataSetChanged();
				}
			});

			holder.item_order_choose_coupon_check.setEnabled(false);

			return view;
		}

		class ViewHolder {
			CheckBox item_order_choose_coupon_check;
			ImageView item_order_choose_coupon_img;
			TextView item_order_choose_coupon_detail;
			TextView item_order_choose_coupon_price;
			TextView item_order_choose_coupon_desc;
		}

	}

	@Override
	public void initListener() {
		ib_digital_goback.setOnClickListener(this);
		pop_proxystocktip_pay.setOnClickListener(this);
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

						float allmoney = calculateTotalMoney();

						getCoupon(allmoney);

						// 检查收获地址是否正确
						String schoolname = UserUtil
								.getschoolname(DigitalCartSubmitOrderActivity.this);
						String hostelname = UserUtil
								.gethostelname(DigitalCartSubmitOrderActivity.this);
						String floorname = UserUtil
								.getfloorname(DigitalCartSubmitOrderActivity.this);

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
		pop_proxystocktip_balance.setText(balance + "元");
		pop_proxystocktip_addr.setText(useraddr);
		order_sum_money = calculateTotalMoney();
		pop_proxystocktip_money.setText(order_sum_money + "元");
	}

	/**
	 * 获取优惠券
	 */
	private void getCoupon(final float allmoney) {
		// 掉接口
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在获取优惠券");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", userid + "");

		Log.v(TAG, "user_id  " + userid);

		loadData(HttpMethod.POST, GlobalConstant.GET_Coupon, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub

						dismissProgressDialog();

						String returnstr2 = arg0.result;

						Log.v(TAG, "returnstr " + returnstr2);

						if (TextUtils.isEmpty(returnstr2)) {
							couponlist.setVisibility(View.GONE);
							nocouponlist.setVisibility(View.VISIBLE);
							return;
						}
						returnstr2 = returnstr2.substring(0,
								returnstr2.length() - 1);
						Log.v(TAG, "returnstr " + returnstr2);

						try {

							String[] sz = returnstr2.split("-");
							for (int i = 0; i < sz.length; i++) {
								Log.v(TAG, "  " + i + "  " + sz[i]);

								JSONArray array = new JSONArray(sz[i]);
								for (int m = 0; m < array.length(); m++) {
									JSONObject json = array.getJSONObject(m);

									int id = json.getInt("id");
									int coupon_type = json
											.getInt("coupon_type");
									float coupon_price = (float) json
											.getDouble("coupon_price");
									int coupon_num = json.getInt("coupon_num");
									int use_coupon_money = json
											.getInt("use_coupon_money");
									int shop_id = json.getInt("shop_id");
									String coupon_log = json
											.getString("coupon_log");
									String detail = json.getString("detail");
									int status = json.getInt("status");

									CouponBean bean = new CouponBean(id,
											coupon_type, coupon_price,
											coupon_num, shop_id, coupon_log,
											detail, status);

									bean.setCheck(false);
									bean.setUse_coupon_money(use_coupon_money);

									Log.v(TAG, "allmoney  " + allmoney
											+ "   use_coupon_money  "
											+ use_coupon_money);

									if (allmoney >= use_coupon_money * 1.0f) {
										allCouponBean.add(bean);
									}
								}
							}

							if (allCouponBean.size() > 0) {
								couponlist.setVisibility(View.VISIBLE);
								nocouponlist.setVisibility(View.GONE);
							} else {
								couponlist.setVisibility(View.GONE);
								nocouponlist.setVisibility(View.VISIBLE);
							}

							Log.v(TAG, "allCouponBean  " + allCouponBean.size());

							couponAdapter.notifyDataSetChanged();

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						dismissProgressDialog();
						Log.v(TAG, "onFailure " + arg1);

					}
				});
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
						DigitalCartSubmitOrderActivity.this);
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
				PayTask alipay = new PayTask(
						DigitalCartSubmitOrderActivity.this);

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

					Toast.makeText(DigitalCartSubmitOrderActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();

					// 清空当前用户的代理购物车
					DBUtil.deleteProxyStockCart(
							DigitalCartSubmitOrderActivity.this, userid);
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
					Toast.makeText(DigitalCartSubmitOrderActivity.this, "支付失败",
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
		for (int i = 0; i < allchoicesock.size(); i++) {
			GoodsBean sock = allchoicesock.get(i);
			boolean ischeck = sock.isCheck();
			if (ischeck) {
				if (sock.getCount() > 0) {

					JsonObject jsonobj = new JsonObject();
					jsonobj.addProperty("goods_id", sock.getId());
					jsonobj.addProperty("goods_cate_id",
							sock.getDigital_goods_category_id());
					jsonobj.addProperty("goods_number", sock.getCount());

					jsonarr.add(jsonobj);

				}
			}
		}

		String digital_order = jsonarr.toString();

		JsonObject digital_buy_user_infoobj = new JsonObject();
		digital_buy_user_infoobj.addProperty("buy_user_id",
				String.valueOf(userid));
		digital_buy_user_infoobj.addProperty("get_goods_person_name",
				get_goods_person_name);
		digital_buy_user_infoobj.addProperty("get_goods_person_phone",
				get_goods_person_phone);
		digital_buy_user_infoobj.addProperty("get_goods_person_address",
				get_goods_person_address);
		digital_buy_user_infoobj
				.addProperty("order_sum_money", order_sum_money);
		digital_buy_user_infoobj.addProperty("rand_no", rand_no);

		String digital_buy_user_info = digital_buy_user_infoobj.toString();

		// 掉接口
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在提交");

		RequestParams params = new RequestParams();

		params.addBodyParameter("digital_order", digital_order);

		params.addBodyParameter("digital_buy_user_info", digital_buy_user_info);

		params.addBodyParameter("pay_type", String.valueOf(pay_type));

		int is_coupon = 0;
		int coupon_id = 0;
		for (int i = 0; i < allCouponBean.size(); i++) {
			if (allCouponBean.get(i).isCheck()) {
				is_coupon = 1;
				coupon_id = allCouponBean.get(i).getId();
			}
		}

		params.addBodyParameter("is_coupon", "" + is_coupon);
		params.addBodyParameter("coupon_id", "" + coupon_id);

		Log.v(TAG, "digital_buy_user_info  " + digital_buy_user_info);
		Log.v(TAG, "digital_order  " + digital_order);
		Log.v(TAG, "is_coupon  " + is_coupon);
		Log.v(TAG, "coupon_id  " + coupon_id);

		loadData(HttpMethod.POST, GlobalConstant.ADD_DIGITAL_ORDER, params,
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
											DigitalCartSubmitOrderActivity.this);
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
									final String orderNo = json
											.getString("orderNo");
									AlertDialog.Builder build = new Builder(
											DigitalCartSubmitOrderActivity.this);
									build.setTitle("提示");
									build.setMessage(resultMsg);
									build.setPositiveButton(
											"确定",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													DBUtil.updateProxyStockOrderStatus(
															DigitalCartSubmitOrderActivity.this,
															rand_no, userid);

													// 删除潮流数码购物车
													DBUtil.deleteDigitalCart(DigitalCartSubmitOrderActivity.this);

													// 发广播修改 界面数量变化
													Intent updateProxyStockCartCount = new Intent();
													updateProxyStockCartCount
															.setAction(GlobalConstant.UpdateDigitalCartCount);
													sendBroadcast(updateProxyStockCartCount);

													// 发送支付成功的广播，跳转去我的订单页面

													Intent PaySuccessToMyOrder = new Intent();
													PaySuccessToMyOrder
															.setAction(GlobalConstant.PaySuccessToMyOrder);
													sendBroadcast(PaySuccessToMyOrder);

													Intent intent = new Intent();
													intent.putExtra("orderNo",
															orderNo);
													intent.setClass(
															DigitalCartSubmitOrderActivity.this,
															OrderActivity.class);
													startActivity(intent);

													DigitalCartSubmitOrderActivity.this
															.finish();
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

	/**
	 * 计算总金额
	 */
	private float calculateTotalMoney() {
		double allmoney = 0.0f;
		for (int i = 0; i < allchoicesock.size(); i++) {
			if (allchoicesock.get(i).isCheck()) {
				double money = allchoicesock.get(i).getCount()
						* allchoicesock.get(i).getGoods_price();
				allmoney = allmoney + money;
			}
		}

		DecimalFormat df = new DecimalFormat("######0.00");

		String allmoneyStr = df.format(allmoney);

		return Float.parseFloat(allmoneyStr);
	}

}
