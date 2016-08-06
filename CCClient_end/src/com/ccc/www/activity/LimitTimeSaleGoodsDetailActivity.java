package com.ccc.www.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.GoodsDetailCommentAdapter;
import com.ccc.www.alipay.AlipayUtil;
import com.ccc.www.alipay.PayResult;
import com.ccc.www.bean.GoodsDetailCommentBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GetBalanceInterface;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.ccc.www.view.XListView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LimitTimeSaleGoodsDetailActivity extends BaseActivity {

	String TAG = "ShopGoodsDetailActivity";

	private ViewPager vpgoods;
	private ImageButton goBackBtn;

	List<String> alltopimg = new ArrayList<String>();

	private LinearLayout ad_goods_dot_layout;
	private XListView xlvComment;
	private View headview;

	TextView tv_entity_goods_name;
	TextView tv_entity_goods_info;
	ImageView goods_img1;
	ImageView goods_img2;
	ImageView goods_img3;
	ImageView goods_img4;
	ImageView goods_img5;
	ImageView goods_img6;

	LinearLayout btn_add_to_shopcar_layout;
	TextView btn_buy_now;
	LinearLayout contact_saler;
	LinearLayout goto_shop;

	List<GoodsDetailCommentBean> allGoodsCommentBean = new ArrayList<GoodsDetailCommentBean>();

	int id = 0;
	int shopid = 0;

	float goods_price = 0.0f;
	int goods_cate_id = 0;
	String goods_name = "";

	int userid = 0;
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

	LinearLayout layout_add_to_collect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		id = getIntent().getIntExtra("id", 0);
		shopid = getIntent().getIntExtra("shopid", 0);

		userid = UserUtil.getuserid(this);
		rootview = LayoutInflater.from(this).inflate(
				R.layout.activity_goods_detail_limittimesale, null);

		headview = LayoutInflater.from(this).inflate(
				R.layout.activity_goods_detail_header, null);

		setContentView(R.layout.activity_goods_detail_limittimesale);

		initview();

		// 判断属否是自己店铺的，是的话不显示加入到购物车
		int useropenshopid = UserUtil
				.getuseropenshopid(LimitTimeSaleGoodsDetailActivity.this);
		if (useropenshopid == shopid) {
			btn_add_to_shopcar_layout.setVisibility(View.GONE);
		} else {
			btn_add_to_shopcar_layout.setVisibility(View.VISIBLE);
		}
	}

	private void getSalerPhone(int shopid) {
		// TODO Auto-generated method stub
		// 掉接口
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("shop_id", String.valueOf(shopid));

		loadData(HttpMethod.POST, GlobalConstant.GET_SALE_PHONE, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub

						dismissProgressDialog();

						String returnstr2 = arg0.result;

						Log.v(TAG, "onSuccess   " + returnstr2);

						try {
							JSONObject json = new JSONObject(returnstr2);
							String telephone = json.getString("telephone");
							if (!TextUtils.isEmpty(telephone)) {
								// intent启动拨打电话
								Intent intent = new Intent(Intent.ACTION_CALL,
										Uri.parse("tel:" + telephone));
								startActivity(intent);
							}
						} catch (JSONException e) {
							showToast("加载失败");
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						dismissProgressDialog();
						Log.v(TAG, "onFailure " + arg1);

						showToast("加载失败");

					}
				});
	}
	
	private void collectshop(int shopid) {
		// TODO Auto-generated method stub
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在添加收藏");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", String.valueOf(UserUtil.getuserid(this)));
		params.addBodyParameter("shop_id", String.valueOf(shopid));
		params.addBodyParameter("status", String.valueOf(1));

		loadData(HttpMethod.POST, GlobalConstant.Add_collect_shop, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub

						dismissProgressDialog();

						String returnstr2 = arg0.result;

						Log.v(TAG, "onSuccess   " + returnstr2);

						try {
							JSONObject json = new JSONObject(returnstr2);
							String telephone = json.getString("telephone");
							
							
							 
						} catch (JSONException e) {
							showToast("添加收藏失败");
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						dismissProgressDialog();
						Log.v(TAG, "onFailure " + arg1);

						showToast("添加收藏失败");

					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_goods_detail_goback:
			LimitTimeSaleGoodsDetailActivity.this.finish();
			break;
		case R.id.goto_shop:
			Intent intentSHOP = new Intent();
			intentSHOP.setClass(LimitTimeSaleGoodsDetailActivity.this,
					EntityStoreActivity.class);
			intentSHOP.putExtra("shopid", shopid);
			startActivity(intentSHOP);
			break;
		case R.id.layout_add_to_collect:
			collectshop(shopid);
			break;
		case R.id.contact_saler:
			/**
			 * 根据shopid获取联系电话
			 */
			getSalerPhone(shopid);

			break;
		case R.id.btn_buy_now:
			if (goods_price > 0) {
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
										.getschoolname(LimitTimeSaleGoodsDetailActivity.this);
								String hostelname = UserUtil
										.gethostelname(LimitTimeSaleGoodsDetailActivity.this);
								String floorname = UserUtil
										.getfloorname(LimitTimeSaleGoodsDetailActivity.this);

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
			}
			break;
		default:
			break;
		}
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

		View view1 = LayoutInflater.from(LimitTimeSaleGoodsDetailActivity.this)
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

		order_sum_money = goods_price;
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

				businessname = "(店铺购物-限时抢购)" + goods_name;

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
							.checkAlipayInstall(LimitTimeSaleGoodsDetailActivity.this);
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
				PayTask payTask = new PayTask(
						LimitTimeSaleGoodsDetailActivity.this);
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
						LimitTimeSaleGoodsDetailActivity.this);

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

					Toast.makeText(LimitTimeSaleGoodsDetailActivity.this,
							"支付成功", Toast.LENGTH_SHORT).show();

					// 清空当前用户的代理购物车
					DBUtil.deleteSchoolBuyCart(LimitTimeSaleGoodsDetailActivity.this);
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
					Toast.makeText(LimitTimeSaleGoodsDetailActivity.this,
							"支付失败", Toast.LENGTH_SHORT).show();
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
		jsonobj.addProperty("shop_id", shopid);
		jsonobj.addProperty("goods_id", id);
		jsonobj.addProperty("goods_cate_id", goods_cate_id);
		jsonobj.addProperty("goods_number", 1);
		jsonarr.add(jsonobj);

		String shop_order = jsonarr.toString();

		JsonObject private_buy_user_infoobj = new JsonObject();
		private_buy_user_infoobj.addProperty("buy_user_id",
				String.valueOf(userid));

		private_buy_user_infoobj.addProperty("get_goods_person_name",
				get_goods_person_name);
		private_buy_user_infoobj.addProperty("get_goods_person_phone",
				get_goods_person_phone);
		private_buy_user_infoobj.addProperty("get_goods_person_address",
				get_goods_person_address);
		private_buy_user_infoobj
				.addProperty("order_sum_money", order_sum_money);
		private_buy_user_infoobj.addProperty("rand_no", rand_no);

		String shop_buy_user_info = private_buy_user_infoobj.toString();

		// 掉接口
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在提交");

		RequestParams params = new RequestParams();

		params.addBodyParameter("shop_buy_user_info", shop_buy_user_info);
		params.addBodyParameter("shop_order", shop_order);
		params.addBodyParameter("pay_type", String.valueOf(pay_type));

		int is_coupon = 0;
		int coupon_id = 0;

		params.addBodyParameter("is_coupon", "" + is_coupon);
		params.addBodyParameter("coupon_id", "" + coupon_id);

		String url = GlobalConstant.ADD_SCHOOL_SHOP_ORDER;

		Log.v(TAG, "url  " + url);
		Log.v(TAG, "shop_buy_user_info  " + shop_buy_user_info);
		Log.v(TAG, "shop_order  " + shop_order);

		Log.v(TAG, "is_coupon  " + is_coupon);
		Log.v(TAG, "coupon_id  " + coupon_id);

		loadData(HttpMethod.POST, url, params, new RequestCallBack<String>() {
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
						final JSONObject json = new JSONObject(returnstr);
						String resultMsg = json.getString("resultMsg");

						if (resultMsg.contains("失败")) {
							// 失败
							AlertDialog.Builder build = new Builder(
									LimitTimeSaleGoodsDetailActivity.this);
							build.setTitle("提示");
							build.setMessage(resultMsg);
							build.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											// TODO Auto-generated
											// method stub

										}
									});
							build.show();
						} else {
							// 成功
							final String orderNo = json.getString("orderNo");
							AlertDialog.Builder build = new Builder(
									LimitTimeSaleGoodsDetailActivity.this);
							build.setTitle("提示");
							build.setMessage(resultMsg);
							build.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											DBUtil.updateProxyStockOrderStatus(
													LimitTimeSaleGoodsDetailActivity.this,
													rand_no, userid);

											// 删除代理进货购物车
											DBUtil.deleteProxyStockCart(
													LimitTimeSaleGoodsDetailActivity.this,
													userid);

											// 发送支付成功的广播，跳转去我的订单页面

											Intent PaySuccessToMyOrder = new Intent();
											PaySuccessToMyOrder
													.setAction(GlobalConstant.PaySuccessToMyOrder);
											sendBroadcast(PaySuccessToMyOrder);

											Intent intent = new Intent();
											intent.putExtra("orderNo", orderNo);
											intent.setClass(
													LimitTimeSaleGoodsDetailActivity.this,
													OrderActivity.class);
											startActivity(intent);

											LimitTimeSaleGoodsDetailActivity.this
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

	@Override
	public void findviewWithId() {
		ad_goods_dot_layout = (LinearLayout) headview
				.findViewById(R.id.ad_goods_dot_layout);
		vpgoods = (ViewPager) headview.findViewById(R.id.vp_goods_detail);

		tv_entity_goods_name = (TextView) headview
				.findViewById(R.id.tv_entity_goods_name);
		tv_entity_goods_info = (TextView) headview
				.findViewById(R.id.tv_entity_goods_info);
		goods_img1 = (ImageView) headview.findViewById(R.id.goods_img1);
		goods_img2 = (ImageView) headview.findViewById(R.id.goods_img2);
		goods_img3 = (ImageView) headview.findViewById(R.id.goods_img3);
		goods_img4 = (ImageView) headview.findViewById(R.id.goods_img4);
		goods_img5 = (ImageView) headview.findViewById(R.id.goods_img5);
		goods_img6 = (ImageView) headview.findViewById(R.id.goods_img6);

		goBackBtn = (ImageButton) findViewById(R.id.ib_goods_detail_goback);
		xlvComment = (XListView) findViewById(R.id.xlv_entity_goods_comment);
		btn_buy_now = (TextView) findViewById(R.id.btn_buy_now);
		btn_add_to_shopcar_layout = (LinearLayout) findViewById(R.id.btn_add_to_shopcar_layout);
		layout_add_to_collect = (LinearLayout) findViewById(R.id.layout_add_to_collect);
		contact_saler = (LinearLayout) findViewById(R.id.contact_saler);
		goto_shop = (LinearLayout) findViewById(R.id.goto_shop);

		xlvComment.addHeaderView(headview);

		xlvComment.setPullRefreshEnable(false);
		xlvComment.setPullLoadEnable(false);
	}

	@Override
	public void initListener() {
		goBackBtn.setOnClickListener(this);
		btn_buy_now.setOnClickListener(this);
		goto_shop.setOnClickListener(this);
		contact_saler.setOnClickListener(this);
		layout_add_to_collect.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		getgoodsdetail();
	}

	/**
	 * 获取详情
	 */
	private void getgoodsdetail() {

		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();

		String idstr = String.valueOf(id);

		params.addBodyParameter("goods_id", idstr);
		loadData(HttpMethod.POST, GlobalConstant.GET_SHOPGOODSINFO, params,
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
						Log.v(TAG, "returnstr " + returnstr);
						if (BaseUtils.isEmpty(returnstr)) {
							showToast("加载失败");
						} else {

							if (returnstr.contains("}-[")) {

								int position = returnstr.indexOf("}-[");
								String goodinfostr = returnstr.substring(0,
										position + 1);
								String commentstr = returnstr.substring(
										position + 2, returnstr.length());

								Log.v(TAG, "goodinfostr  " + goodinfostr);
								Log.v(TAG, "commentstr  " + commentstr);

								try {

									JSONObject goodinfo = new JSONObject(
											goodinfostr);

									int shop_id = goodinfo.getInt("shop_id");
									int user_id = goodinfo.getInt("user_id");
									goods_name = goodinfo
											.getString("goods_name");
									int goods_num = goodinfo
											.getInt("goods_num");
									goods_price = (float) goodinfo
											.getDouble("goods_price");
									String goods_detail = goodinfo
											.getString("goods_detail");
									String goods_log1 = goodinfo
											.getString("goods_log1");
									String goods_log2 = goodinfo
											.getString("goods_log2");
									String goods_log3 = goodinfo
											.getString("goods_log3");
									int goods_status = goodinfo
											.getInt("goods_status");
									goods_cate_id = goodinfo
											.getInt("shop_category_id");

									String goods_d1 = goodinfo
											.getString("goods_d1");
									String goods_d2 = goodinfo
											.getString("goods_d2");
									String goods_d3 = goodinfo
											.getString("goods_d3");
									String goods_d4 = goodinfo
											.getString("goods_d4");
									String goods_d5 = goodinfo
											.getString("goods_d5");
									String goods_d6 = goodinfo
											.getString("goods_d6");

									JSONArray comment = new JSONArray(
											commentstr);

									for (int i = 0; i < comment.length(); i++) {
										JSONObject jsoncomment = comment
												.getJSONObject(i);

										int id = jsoncomment.getInt("id");
										int shop_id_comment = jsoncomment
												.getInt("shop_id");
										int supermaket_id = jsoncomment
												.getInt("supermaket_id");
										int goods_id = jsoncomment
												.getInt("goods_id");
										String good_comment = "";
										if (jsoncomment.has("good_comment")) {
											good_comment = jsoncomment
													.getString("good_comment");
										}

										String bad_comment = "";
										if (jsoncomment.has("bad_comment")) {
											bad_comment = jsoncomment
													.getString("bad_comment");
										}
										String comment_time = jsoncomment
												.getString("comment_time");
										int status = jsoncomment
												.getInt("status");
										GoodsDetailCommentBean bean = new GoodsDetailCommentBean(
												id, shop_id_comment,
												supermaket_id, goods_id,
												good_comment, bad_comment,
												comment_time, status);

										allGoodsCommentBean.add(bean);
									}

									if (!TextUtils.isEmpty(goods_log1)) {
										if (!goods_log1.startsWith("http")) {
											goods_log1 = GlobalConstant.SERVER_URL
													+ goods_log1;
										}
										alltopimg.add(goods_log1);
									}
									if (!TextUtils.isEmpty(goods_log2)) {
										if (!goods_log2.startsWith("http")) {
											goods_log2 = GlobalConstant.SERVER_URL
													+ goods_log2;
										}
										alltopimg.add(goods_log2);
									}
									if (!TextUtils.isEmpty(goods_log3)) {
										if (!goods_log3.startsWith("http")) {
											goods_log3 = GlobalConstant.SERVER_URL
													+ goods_log3;
										}
										alltopimg.add(goods_log3);
									}

									if (!TextUtils.isEmpty(goods_d1)) {
										if (!goods_d1.startsWith("http")) {
											goods_d1 = GlobalConstant.SERVER_URL
													+ goods_d1;
										}
										ImageLoader.getInstance().displayImage(
												goods_d1, goods_img1,
												ImageLoaderOption.getoption());
									} else {
										goods_img1.setVisibility(View.GONE);
									}

									if (!TextUtils.isEmpty(goods_d2)) {
										if (!goods_d2.startsWith("http")) {
											goods_d2 = GlobalConstant.SERVER_URL
													+ goods_d2;
										}
										ImageLoader.getInstance().displayImage(
												goods_d2, goods_img2,
												ImageLoaderOption.getoption());
									} else {
										goods_img2.setVisibility(View.GONE);
									}

									if (!TextUtils.isEmpty(goods_d3)) {
										if (!goods_d3.startsWith("http")) {
											goods_d3 = GlobalConstant.SERVER_URL
													+ goods_d3;
										}
										ImageLoader.getInstance().displayImage(
												goods_d3, goods_img3,
												ImageLoaderOption.getoption());
									} else {
										goods_img3.setVisibility(View.GONE);
									}

									if (!TextUtils.isEmpty(goods_d4)) {
										if (!goods_d4.startsWith("http")) {
											goods_d4 = GlobalConstant.SERVER_URL
													+ goods_d4;
										}
										ImageLoader.getInstance().displayImage(
												goods_d4, goods_img4,
												ImageLoaderOption.getoption());
									} else {
										goods_img4.setVisibility(View.GONE);
									}

									if (!TextUtils.isEmpty(goods_d5)) {
										if (!goods_d5.startsWith("http")) {
											goods_d5 = GlobalConstant.SERVER_URL
													+ goods_d5;
										}
										ImageLoader.getInstance().displayImage(
												goods_d5, goods_img5,
												ImageLoaderOption.getoption());
									} else {
										goods_img5.setVisibility(View.GONE);
									}

									if (!TextUtils.isEmpty(goods_d6)) {
										if (!goods_d6.startsWith("http")) {
											goods_d6 = GlobalConstant.SERVER_URL
													+ goods_d6;
										}
										ImageLoader.getInstance().displayImage(
												goods_d6, goods_img6,
												ImageLoaderOption.getoption());
									} else {
										goods_img6.setVisibility(View.GONE);
									}

									vpgoods.setAdapter(new GoodsViewPager());

									xlvComment
											.setAdapter(new GoodsDetailCommentAdapter(
													LimitTimeSaleGoodsDetailActivity.this,
													allGoodsCommentBean,
													R.layout.goods_detail_comment_lv_item));

									initDot();
									updateDot();
									viewPagerListener();

									/**
									 * UI赋值
									 */
									tv_entity_goods_name.setText(goods_name);
									tv_entity_goods_info.setText(goods_detail);

								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}
					}
				});
	}

	class GoodsViewPager extends PagerAdapter {
		@Override
		public int getCount() {
			return alltopimg.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			View view = LayoutInflater.from(
					LimitTimeSaleGoodsDetailActivity.this).inflate(
					R.layout.item_viewpageimg, null);
			ImageView imageview = (ImageView) view
					.findViewById(R.id.item_viewpageimg_image);

			ImageLoader.getInstance().displayImage(alltopimg.get(position),
					imageview, ImageLoaderOption.getoption());

			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	public void initDot() {
		for (int i = 0; i < alltopimg.size(); i++) {
			View view = new View(this);
			LayoutParams params = new LayoutParams(10, 10);
			params.leftMargin = 10;
			view.setLayoutParams(params);
			view.setBackgroundResource(R.drawable.selector_dot);
			ad_goods_dot_layout.addView(view);
		}
	}

	public void updateDot() {
		int currentIndex = vpgoods.getCurrentItem();
		for (int i = 0; i < ad_goods_dot_layout.getChildCount(); i++) {
			if (currentIndex == i) {
				ad_goods_dot_layout.getChildAt(i).setEnabled(true);
			} else {
				ad_goods_dot_layout.getChildAt(i).setEnabled(false);
			}
		}
	}

	private void viewPagerListener() {
		vpgoods.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				updateDot();
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}
}
