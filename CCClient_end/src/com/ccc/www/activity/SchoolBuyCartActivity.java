package com.ccc.www.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ccc.ccclient_end.R;
import com.ccc.www.alipay.AlipayUtil;
import com.ccc.www.alipay.PayResult;
import com.ccc.www.bean.GoodsBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GetBalanceInterface;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 校园购物的购物车
 * 
 * @author Administrator
 * 
 */
public class SchoolBuyCartActivity extends BaseActivity {

	String TAG = "SchoolBuyCartActivity";

	ImageButton ib_select_school_goback;
	ListView activity_proxystockcart_lv;

	int userid;

	List<GoodsBean> allGoodsBean = new ArrayList<GoodsBean>();

	Adapter adapter = new Adapter();

	TextView mycart_selelctcount;
	TextView mycart_allmoney;
	TextView mycart_createorder;

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

	/**
	 * 支付成功后关闭页面
	 * 
	 * @author Administrator
	 * 
	 */
	class PaySuccessToMyOrder extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			SchoolBuyCartActivity.this.finish();
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(paySuccessToMyOrder);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		registerReceiver(paySuccessToMyOrder, new IntentFilter(
				GlobalConstant.PaySuccessToMyOrder));

		userid = UserUtil.getuserid(this);

		rootview = LayoutInflater.from(this).inflate(
				R.layout.activity_schoolbuycart, null);

		setContentView(R.layout.activity_schoolbuycart);

		initview();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_select_school_goback:
			SchoolBuyCartActivity.this.finish();
			break;
		case R.id.mycart_createorder:

			boolean havecheck = false;
			for (int i = 0; i < allGoodsBean.size(); i++) {
				GoodsBean sock = allGoodsBean.get(i);
				boolean ischeck = sock.isCheck();
				if (ischeck) {
					if (sock.getCount() > 0) {
						havecheck = true;
						break;
					}
				}
			}

			if (havecheck) {
				// 获取账户余额
				getAccountBalance(new GetBalanceInterface() {
					@Override
					public void Callback(String returnstr) {

						if (TextUtils.isEmpty(returnstr)) {
							//
							showToast("获取余额失败，请稍后重试");
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);
								float balance = (float) json
										.getDouble("balance");

								// 检查收获地址是否正确
								String schoolname = UserUtil
										.getschoolname(SchoolBuyCartActivity.this);
								String hostelname = UserUtil
										.gethostelname(SchoolBuyCartActivity.this);
								String floorname = UserUtil
										.getfloorname(SchoolBuyCartActivity.this);

								String useraddr = schoolname + hostelname
										+ floorname;

								payinfoConfirm(useraddr, balance);

							} catch (JSONException e) {
								e.printStackTrace();
								showToast("获取余额失败，请稍后重试");
							}
						}
					}
				});

			} else {
				showToast("请选择要购买商品并且商品数目大于0");
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
		// TODO Auto-generated method stub
		ib_select_school_goback = (ImageButton) findViewById(R.id.ib_select_school_goback);
		activity_proxystockcart_lv = (ListView) findViewById(R.id.activity_proxystockcart_lv);
		activity_proxystockcart_lv.setAdapter(adapter);

		mycart_selelctcount = (TextView) findViewById(R.id.mycart_selelctcount);
		mycart_allmoney = (TextView) findViewById(R.id.mycart_allmoney);
		mycart_createorder = (TextView) findViewById(R.id.mycart_createorder);
	}

	@Override
	public void initListener() {
		ib_select_school_goback.setOnClickListener(this);
		mycart_createorder.setOnClickListener(this);
	}

	@Override
	public void initdata() {

		float allmoney = 0.0f;

		allGoodsBean = DBUtil.getSchoolBuyCart(this);
		for (int i = 0; i < allGoodsBean.size(); i++) {
			allGoodsBean.get(i).setCheck(true);

			float money = allGoodsBean.get(i).getCount()
					* allGoodsBean.get(i).getGoods_price();
			allmoney = allmoney + money;
		}

		mycart_selelctcount.setText(allGoodsBean.size() + "");

		calculateTotalMoney();

		adapter.notifyDataSetChanged();
	}

	class Adapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allGoodsBean.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allGoodsBean.get(arg0);
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
				view = LayoutInflater.from(SchoolBuyCartActivity.this).inflate(
						R.layout.item_proxysockcart, null);
				holder.item_proxysockcart_check = (ImageView) view
						.findViewById(R.id.item_proxysockcart_check);
				holder.iv_sock_log = (ImageView) view
						.findViewById(R.id.iv_sock_log);
				holder.tv_sock_name = (TextView) view
						.findViewById(R.id.tv_sock_name);
				holder.tv_sock_price = (TextView) view
						.findViewById(R.id.tv_sock_price);
				holder.iv_sock_minus = (ImageView) view
						.findViewById(R.id.iv_sock_minus);
				holder.iv_sock_plus = (ImageView) view
						.findViewById(R.id.iv_sock_plus);
				holder.tv_sock_count = (TextView) view
						.findViewById(R.id.tv_sock_count);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final GoodsBean sock = allGoodsBean.get(position);

			final boolean ischeck = sock.isCheck();

			Log.v(TAG, " position " + position + "  check  " + ischeck);

			if (ischeck) {
				holder.item_proxysockcart_check
						.setImageResource(R.drawable.icon_checked_01);
			} else {
				holder.item_proxysockcart_check
						.setImageResource(R.drawable.icon_checked_white_01);
			}

			holder.item_proxysockcart_check
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if (ischeck) {
								allGoodsBean.get(position).setCheck(false);
							} else {
								allGoodsBean.get(position).setCheck(true);
							}
							// 重新计算总价
							calculateTotalMoney();

							// 修改已选择的商品
							int selectcount = 0;
							for (int i = 0; i < allGoodsBean.size(); i++) {
								if (allGoodsBean.get(i).isCheck()) {
									selectcount++;
								}
							}
							mycart_selelctcount.setText(selectcount + "");

							notifyDataSetChanged();
						}
					});

			String img = sock.getGoods_log1();

			if (!img.startsWith("http")) {
				img = GlobalConstant.SERVER_URL + img;
			}

			ImageLoader.getInstance().displayImage(img, holder.iv_sock_log,
					ImageLoaderOption.getoption());

			holder.tv_sock_name.setText(sock.getGoods_name());
			holder.tv_sock_price.setText("￥" + sock.getGoods_price() + "元");

			holder.tv_sock_count.setText(sock.getCount() + "");

			holder.iv_sock_minus.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					int count = sock.getCount();
					if (count > 0) {
						count--;
					}
					sock.setCount(count);

					if (count == 0) {
						allGoodsBean.get(position).setCheck(false);
					}

					// 修改数据库数量
					DBUtil.updateSchoolBuyCartCount(SchoolBuyCartActivity.this,
							sock);
					// 发广播修改代理进货界面数量变化
					Intent updateProxyStockCartCount = new Intent();
					updateProxyStockCartCount
							.setAction(GlobalConstant.UpdateSchoolBuyCartCount);
					sendBroadcast(updateProxyStockCartCount);

					// 重新计算总价
					calculateTotalMoney();

					notifyDataSetChanged();
				}
			});

			holder.iv_sock_plus.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int count = sock.getCount();
					count++;
					sock.setCount(count);

					allGoodsBean.get(position).setCheck(true);

					// 修改数据库数量
					DBUtil.updateSchoolBuyCartCount(SchoolBuyCartActivity.this,
							sock);
					// 发广播修改代理进货界面数量变化
					Intent updateProxyStockCartCount = new Intent();
					updateProxyStockCartCount
							.setAction(GlobalConstant.UpdateSchoolBuyCartCount);
					sendBroadcast(updateProxyStockCartCount);

					// 重新计算总价
					calculateTotalMoney();

					notifyDataSetChanged();
				}
			});

			return view;
		}

		class ViewHolder {
			ImageView item_proxysockcart_check;
			ImageView iv_sock_log;
			TextView tv_sock_name;
			TextView tv_sock_price;
			ImageView iv_sock_minus;
			ImageView iv_sock_plus;
			TextView tv_sock_count;
		}
	}

	/**
	 * 计算总金额
	 */
	private float calculateTotalMoney() {
		float allmoney = 0.0f;
		for (int i = 0; i < allGoodsBean.size(); i++) {
			if (allGoodsBean.get(i).isCheck()) {
				float money = allGoodsBean.get(i).getCount()
						* allGoodsBean.get(i).getGoods_price();
				allmoney = allmoney + money;
			}
		}

		DecimalFormat df = new DecimalFormat("######0.00");

		String allmoneyStr = df.format(allmoney);

		mycart_allmoney.setText("需支付:" + allmoneyStr + "元");

		return Float.parseFloat(allmoneyStr);
	}

	PopupWindow mPopupWindow;

	private void dissmisspopwindow() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

	/**
	 * 支付信息确认
	 * 
	 * @param KaizenId
	 */
	private void payinfoConfirm(String useraddr, final float balance) {

		View view1 = LayoutInflater.from(SchoolBuyCartActivity.this).inflate(
				R.layout.pop_proxystocktip, null);
		mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);

		final EditText pop_proxystocktip_name = (EditText) view1
				.findViewById(R.id.pop_proxystocktip_name);
		final EditText pop_proxystocktip_phone = (EditText) view1
				.findViewById(R.id.pop_proxystocktip_phone);
		final EditText pop_proxystocktip_addr = (EditText) view1
				.findViewById(R.id.pop_proxystocktip_addr);
		TextView pop_proxystocktip_balance = (TextView) view1
				.findViewById(R.id.pop_proxystocktip_balance);
		TextView pop_proxystocktip_money = (TextView) view1
				.findViewById(R.id.pop_proxystocktip_money);
		TextView pop_proxystocktip_cancel = (TextView) view1
				.findViewById(R.id.pop_proxystocktip_cancel);
		TextView pop_proxystocktip_pay = (TextView) view1
				.findViewById(R.id.pop_proxystocktip_pay);

		pop_proxystocktip_addr.setText(useraddr);

		pop_proxystocktip_balance.setText(balance + "元");

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

				// 调支付宝之前的数据准备
				List<GoodsBean> selectsocks = new ArrayList<GoodsBean>();
				for (int i = 0; i < allGoodsBean.size(); i++) {
					GoodsBean sock = allGoodsBean.get(i);
					boolean ischeck = sock.isCheck();
					if (ischeck) {
						if (sock.getCount() > 0) {
							selectsocks.add(sock);
						}
					}
				}

				businessname = "";
				for (int i = 0; i < selectsocks.size(); i++) {
					if (i == 0) {
						businessname = "(校园购物)"
								+ selectsocks.get(i).getGoods_name();
					} else {
						businessname = businessname + ","
								+ selectsocks.get(i).getGoods_name();
					}
				}
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
							.checkAlipayInstall(SchoolBuyCartActivity.this);
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
				PayTask payTask = new PayTask(SchoolBuyCartActivity.this);
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
				PayTask alipay = new PayTask(SchoolBuyCartActivity.this);

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

					Toast.makeText(SchoolBuyCartActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();

					// 清空当前用户的代理购物车
					DBUtil.deleteSchoolBuyCart(SchoolBuyCartActivity.this);
					// 发广播修改代理进货界面数量变化
					Intent updateProxyStockCartCount = new Intent();
					updateProxyStockCartCount
							.setAction(GlobalConstant.UpdateSchoolBuyCartCount);
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
					Toast.makeText(SchoolBuyCartActivity.this, "支付失败",
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
		for (int i = 0; i < allGoodsBean.size(); i++) {
			GoodsBean sock = allGoodsBean.get(i);
			boolean ischeck = sock.isCheck();
			if (ischeck) {
				if (sock.getCount() > 0) {

					JsonObject jsonobj = new JsonObject();
					jsonobj.addProperty("shop_id", sock.getShop_id());
					jsonobj.addProperty("goods_id", sock.getId());
					jsonobj.addProperty("goods_cate_id",
							sock.getShop_category_id());
					jsonobj.addProperty("goods_number", sock.getCount());

					jsonobj.addProperty("order_sum_money", sock.getCount()
							* sock.getGoods_price());

					jsonarr.add(jsonobj);
				}
			}
		}

		String shop_order = jsonarr.toString();

		JsonObject shop_buy_user_infoobj = new JsonObject();
		shop_buy_user_infoobj
				.addProperty("buy_user_id", String.valueOf(userid));
		shop_buy_user_infoobj.addProperty("get_goods_person_name",
				get_goods_person_name);
		shop_buy_user_infoobj.addProperty("get_goods_person_phone",
				get_goods_person_phone);
		shop_buy_user_infoobj.addProperty("get_goods_person_address",
				get_goods_person_address);
		// shop_buy_user_infoobj.addProperty("order_sum_money",
		// String.valueOf(order_sum_money));
		shop_buy_user_infoobj.addProperty("rand_no", rand_no);

		String shop_buy_user_info = shop_buy_user_infoobj.toString();

		// 掉接口
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在提交");

		RequestParams params = new RequestParams();

		Log.v(TAG, "shop_buy_user_info  " + shop_buy_user_info);
		Log.v(TAG, "shop_order  " + shop_order);

		params.addBodyParameter("shop_buy_user_info", shop_buy_user_info);
		params.addBodyParameter("shop_order", shop_order);
		params.addBodyParameter("pay_type", String.valueOf(pay_type));

		loadData(HttpMethod.POST, GlobalConstant.ADD_SCHOOL_SHOP_ORDER_SHOP,
				params, new RequestCallBack<String>() {
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
											SchoolBuyCartActivity.this);
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

									List<Integer> alluserid = new ArrayList<Integer>();
									for (int i = 0; i < allGoodsBean.size(); i++) {
										GoodsBean sock = allGoodsBean.get(i);
										boolean ischeck = sock.isCheck();
										if (ischeck) {
											if (sock.getCount() > 0) {
												int userid = sock.getUser_id();

												boolean iscontain = false;
												for (int j = 0; j < alluserid
														.size(); j++) {
													if (alluserid.get(j) == userid) {
														iscontain = true;
														break;
													}
												}

												if (!iscontain) {
													alluserid.add(userid);
												}
											}
										}
									}

									for (int i = 0; i < alluserid.size(); i++) {
										JpushGetIMEI_BY_UID__Push(
												alluserid.get(i), "您有校园购物新订单",
												"您有校园购物新订单,请查看校园购物订单");
										JpushAddMsg(userid, alluserid.get(i),
												"您有校园购物新订单",
												"您有校园购物新订单,请查看校园购物订单");
									}

									// 成功
									final String orderNo = json
											.getString("orderNo");
									AlertDialog.Builder build = new Builder(
											SchoolBuyCartActivity.this);
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
															SchoolBuyCartActivity.this,
															rand_no, userid);

													// 删除潮流数码购物车
													DBUtil.deleteSchoolBuyCart(SchoolBuyCartActivity.this);

													// 发广播修改 界面数量变化
													Intent updateProxyStockCartCount = new Intent();
													updateProxyStockCartCount
															.setAction(GlobalConstant.UpdateSchoolBuyCartCount);
													sendBroadcast(updateProxyStockCartCount);

													// 发送支付成功的广播，修改校园购物商品的数量
													// for (int i = 0; i <
													// allGoodsBean.size(); i++)
													// {
													// GoodsBean sock =
													// allGoodsBean.get(i);
													// boolean ischeck =
													// sock.isCheck();
													// if (ischeck) {
													// if (sock.getCount() > 0)
													// {
													//
													//
													//
													//
													// JsonObject jsonobj = new
													// JsonObject();
													// jsonobj.addProperty("shop_id",
													// sock.getShop_id());
													// jsonobj.addProperty("goods_id",
													// sock.getId());
													// jsonobj.addProperty("goods_cate_id",
													// sock.getShop_category_id());
													// jsonobj.addProperty("goods_number",
													// sock.getCount());
													// jsonarr.add(jsonobj);
													// }
													// }
													// }

													// 发送支付成功的广播，跳转去我的订单页面
													Intent PaySuccessToMyOrder = new Intent();
													PaySuccessToMyOrder
															.setAction(GlobalConstant.PaySuccessToMyOrder);
													sendBroadcast(PaySuccessToMyOrder);

													Intent intent = new Intent();
													intent.putExtra("orderNo",
															orderNo);
													intent.setClass(
															SchoolBuyCartActivity.this,
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
