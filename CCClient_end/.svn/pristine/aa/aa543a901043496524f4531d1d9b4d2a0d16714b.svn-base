package com.ccc.www.navigation_activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ccc.ccclient_end.R;
import com.ccc.www.activity.ApplyOpenShopActivity;
import com.ccc.www.activity.BaseActivity;
import com.ccc.www.activity.FeedbackHelpActivity;
import com.ccc.www.activity.LoginActivity;
import com.ccc.www.activity.MessageCenterActivity;
import com.ccc.www.activity.MyMoneyBagActivity;
import com.ccc.www.activity.MyOpenShopActivity;
import com.ccc.www.activity.MyStoreActivity;
import com.ccc.www.activity.MySuperMarketActivity;
import com.ccc.www.activity.OpenRedBagActivity;
import com.ccc.www.activity.OrderActivity;
import com.ccc.www.activity.ProxyStockActivity;
import com.ccc.www.activity.SendCouponActivity;
import com.ccc.www.activity.SystemSettingActivity;
import com.ccc.www.adapter.UserCenterListAdapter;
import com.ccc.www.bean.JPushMessageBean;
import com.ccc.www.bean.UserCenterListBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.GlobalRequestCode;
import com.ccc.www.util.UserUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.readystatesoftware.viewbadger.BadgeView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class MyCenterActivity extends BaseActivity {

	String TAG = "MyCenterActivity";

	public Activity activity;
	private TextView tv_login_reg;

	private boolean islogin;
	private String loginName = "";
	private int loginId = 0;
	private TextView tv_login_detail;
	private Button btn_login_out;
	private TextView tv_score_number;
	private TextView tv_discount_coupon_number;

	ImageButton ib_header_msg;

	HttpUtils http = new HttpUtils();
	SharedPreferences sp = null;
	protected static final String LOGIN_BROAD = "login_ok";

	ArrayList<UserCenterListBean> lists = new ArrayList<UserCenterListBean>();

	ListView lvUserCenter;

	// 分享控制
	UMSocialService mController;

	RelativeLayout mycenter_login_layout;

	BadgeView badge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mController = UMServiceFactory.getUMSocialService("com.umeng.share");

		activity = MyCenterActivity.this;
		setContentView(R.layout.mycenter_fragment);
		initview();

		badge = new BadgeView(MyCenterActivity.this, ib_header_msg);
	}

	/**
	 * 更新未读消息数目
	 */
	private void updateNoReadMsgCount() {
		int userid = UserUtil.getuserid(this);
		if (userid <= 0) {

			if (badge != null) {
				badge.hide();
			}

			return;
		}

		RequestParams params = new RequestParams();
		params.addBodyParameter("t_uid", userid + "");

		loadData(HttpMethod.POST, GlobalConstant.Jpush_GET_MY_MSG, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {

						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr " + returnstr);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("加载失败");
						} else {
							try {
								List<JPushMessageBean> tempallJPushMessageBean = new ArrayList<JPushMessageBean>();

								JSONArray array = new JSONArray(returnstr);
								for (int i = 0; i < array.length(); i++) {
									JSONObject json = array.getJSONObject(i);

									int id = json.getInt("id");
									String jpshu_title = json
											.getString("jpshu_title");
									String jpush_context = json
											.getString("jpush_context");
									int from_user_id = json
											.getInt("from_user_id");
									int to_user_id = json.getInt("to_user_id");
									int status = json.getInt("status");

									JPushMessageBean bean = new JPushMessageBean(
											id, jpshu_title, jpush_context,
											from_user_id, to_user_id, status);

									if (status == 0) {
										tempallJPushMessageBean.add(bean);
									}
								}

								int allcount = tempallJPushMessageBean.size();
								if (badge != null) {
									badge.setText("" + allcount);
									badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
									badge.setTextSize(10);
									if (allcount > 0) {
										badge.show();
									} else {
										badge.hide();
									}
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}
				});

	}

	@Override
	public void onResume() {
		super.onResume();
		initview();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//		case R.id.ib_header_msg:
//			if (UserUtil.getuserid(activity) == 0) {
//				Intent intent = new Intent(activity, LoginActivity.class);
//				startActivityForResult(intent,
//						GlobalRequestCode.ACTIVITY_MYCENTER_LOGIN_REQUEST_CODE);
//			} else {
//				Intent intent = new Intent(activity,
//						MessageCenterActivity.class);
//				startActivityForResult(intent,
//						GlobalRequestCode.ACTIVITY_MYCENTER_LOGIN_REQUEST_CODE);
//			}
//			break;
		case R.id.tv_login_reg:
			if (tv_login_reg.getText().equals("登录/注册")) {
				Intent intent = new Intent(activity, LoginActivity.class);
				startActivityForResult(intent,
						GlobalRequestCode.ACTIVITY_MYCENTER_LOGIN_REQUEST_CODE);
			}
			break;
		case R.id.mycenter_login_layout:
			int userid = UserUtil.getuserid(activity);
			if (userid == 0) {
				Intent intent = new Intent(activity, LoginActivity.class);
				startActivityForResult(intent,
						GlobalRequestCode.ACTIVITY_MYCENTER_LOGIN_REQUEST_CODE);
			}
			break;
		case R.id.btn_login_out:
			UserUtil.setuserid(activity, 0);

			SharedPreferences.Editor editor = sp.edit();
			editor.clear();
			editor.commit();
			elementIsShow();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		elementIsShow();

		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (lists.size() - 1 >= position) {
			String name = lists.get(position).getList_name();
			if (name.equalsIgnoreCase("我的超市")) {
				// 我的超市
				if (isLogin()) {
					Intent mysupermarket = new Intent();
					mysupermarket.setClass(activity,
							MySuperMarketActivity.class);
					startActivity(mysupermarket);
				} else {
					intentLogin();
				}
			} else if (name.equalsIgnoreCase("代理进货")) {
				// 代理进货
				int userid = UserUtil.getuserid(activity);
				if (userid > 0) {
					Intent proxyStock = new Intent();
					proxyStock.setClass(activity, ProxyStockActivity.class);
					startActivity(proxyStock);
				} else {
					intentLogin();
				}
			} else if (name.equalsIgnoreCase("我的店铺")) {
				// 我的店铺
				if (isLogin()) {
					isOpenShop();
				} else {
					intentLogin();
				}
			} else if (name.equalsIgnoreCase("我的订单")) {
				// 我的订单
				if (isLogin()) {
					Intent myorder = new Intent();
					myorder.setClass(activity, OrderActivity.class);
					startActivity(myorder);
				} else {
					intentLogin();
				}
			} else if (name.equalsIgnoreCase("我的钱包")) {
				// 我的钱包
				if (isLogin()) {
					Intent myorder = new Intent();
					myorder.setClass(activity, MyMoneyBagActivity.class);
					startActivity(myorder);
				} else {
					intentLogin();
				}
			} else if (name.equalsIgnoreCase("反馈帮助")) {
				// 反馈帮助
				Intent feedback = new Intent();
				feedback.setClass(activity, FeedbackHelpActivity.class);
				startActivity(feedback);
			} else if (name.equalsIgnoreCase("分享APP")) {
				// 分享
				shareApp();
			} else if (name.equalsIgnoreCase("系统设置")) {
				// 系统设置
				Intent systemsetting = new Intent();
				systemsetting.setClass(activity, SystemSettingActivity.class);
				startActivity(systemsetting);
			} else if (name.equalsIgnoreCase("发布优惠")) {
				// 发布优惠
				isOpenShop_ShareCoupon();
			} else if (name.equalsIgnoreCase("开启红包")) {
				// 开启红包
				if (isLogin()) {
					isOpenShop_fbhb();
				} else {
					intentLogin();
				}
			}
		}
	}

	/**
	 * 分享APP
	 */
	private void shareApp() {
		// 分享
		String title = "上大学有宅友就够了";
		String content = "大学生线上创业孵化基地，想创业上宅友APP。";
		String targeturl = "http://m.pp.cn/detail.html?appid=7045573&ch_src=pp_pp_share&ch=qq";
		// 设置分享到QQ好友的内容
		QQShareContent qqsharecontent = new QQShareContent();
		qqsharecontent.setTitle(title);
		qqsharecontent.setTargetUrl(targeturl);
		qqsharecontent.setShareContent(content);
		qqsharecontent.setShareImage(new UMImage(MyCenterActivity.this,
				R.drawable.logo));
		mController.setShareMedia(qqsharecontent);

		// 设置分享到QQ空间的内容
		QZoneShareContent qzonesharecontent = new QZoneShareContent();
		qzonesharecontent.setTitle(title);
		qzonesharecontent.setTargetUrl(targeturl);
		qzonesharecontent.setShareContent(content);
		qzonesharecontent.setShareImage(new UMImage(MyCenterActivity.this,
				R.drawable.logo));
		mController.setShareMedia(qzonesharecontent);

		// 设置分享到微信还有
		WeiXinShareContent weixinsharecontent = new WeiXinShareContent();
		weixinsharecontent.setTitle(title);
		weixinsharecontent.setTargetUrl(targeturl);
		weixinsharecontent.setShareContent(content);
		weixinsharecontent.setShareImage(new UMImage(MyCenterActivity.this,
				R.drawable.logo));
		mController.setShareMedia(weixinsharecontent);

		// 设置分享到朋友圈
		CircleShareContent circlesharecontent = new CircleShareContent();
		circlesharecontent.setTitle(title);
		circlesharecontent.setTargetUrl(targeturl);
		circlesharecontent.setShareContent(content);
		circlesharecontent.setShareImage(new UMImage(MyCenterActivity.this,
				R.drawable.logo));
		mController.setShareMedia(circlesharecontent);

		String appID = "wx77a5cd57027cac1b";
		String appSecret = "3fde70a7985b5fd91d2a15d3f8fe037e";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(MyCenterActivity.this, appID,
				appSecret);
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(MyCenterActivity.this,
				appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// 分享给QQ好友
		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(MyCenterActivity.this,
				"1104738145", "dBCimScHR8IThP5E");
		qqSsoHandler.addToSocialSDK();

		// SSO（免登录）分享到QQ空间
		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
				MyCenterActivity.this, "1104738145", "dBCimScHR8IThP5E");
		qZoneSsoHandler.addToSocialSDK();

		// 新浪的注册key
		// App Key:3699447690
		// 976765ce47f666d9b9bd98c6ef4e8154

		// 分享

		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE);
		mController.openShare(MyCenterActivity.this, false);
	}

	private void isOpenShop() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", String.valueOf(loginId));
		http.send(HttpMethod.POST, GlobalConstant.SHOP_ACTION_IS_OPEN_SHOP,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String msg) {
						Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String result = info.result;
						try {
							JSONObject json = new JSONObject(result);
							System.out.println(json);
							int code = json.getInt("resultCode");
							int shopid = json.getInt("beanId");
							if (code == 0) {
								// 申请开通店铺 1.审核通过 2.审核未通过
								Intent intent = new Intent();
								intent.setClass(activity,
										ApplyOpenShopActivity.class);
								startActivity(intent);
							} else if (code == 2) {
								// 审核未通过
								Intent intent = new Intent(activity,
										MyOpenShopActivity.class);
								startActivity(intent);
							} else if (code == 1) {
								// 审核通过
								Intent intent = new Intent(activity,
										MyStoreActivity.class);
								intent.putExtra("shopId", shopid);
								startActivity(intent);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 开启红包
	 */
	private void isOpenShop_fbhb() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", String.valueOf(loginId));
		http.send(HttpMethod.POST, GlobalConstant.SHOP_ACTION_IS_OPEN_SHOP,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String msg) {
						Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String result = info.result;
						try {
							JSONObject json = new JSONObject(result);
							System.out.println(json);
							int code = json.getInt("resultCode");
							int shopid = json.getInt("beanId");
							if (code == 0) {
								// 申请开通店铺 1.审核通过 2.审核未通过
								Intent intent = new Intent();
								intent.setClass(activity,
										ApplyOpenShopActivity.class);
								startActivity(intent);
							} else if (code == 2) {
								// 审核未通过
								Intent intent = new Intent(activity,
										MyOpenShopActivity.class);
								startActivity(intent);
							} else if (code == 1) {
								// 审核通过
								Intent intent = new Intent(activity,
										OpenRedBagActivity.class);
								intent.putExtra("shopId", shopid);
								startActivity(intent);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 发布优惠检验是否开店
	 */
	private void isOpenShop_ShareCoupon() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", String.valueOf(loginId));
		http.send(HttpMethod.POST, GlobalConstant.SHOP_ACTION_IS_OPEN_SHOP,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String msg) {
						Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String result = info.result;
						try {
							JSONObject json = new JSONObject(result);
							System.out.println(json);

							int code = json.getInt("resultCode");
							int shopid = json.getInt("beanId");
							if (code == 0) {
								// 申请开通店铺 1.审核通过 2.审核未通过
								Intent intent = new Intent();
								intent.setClass(activity,
										ApplyOpenShopActivity.class);
								startActivity(intent);
							} else if (code == 2) {
								// 审核未通过
								Intent intent = new Intent(activity,
										MyOpenShopActivity.class);
								startActivity(intent);
							} else if (code == 1) {
								// 审核通过
								Intent intent = new Intent();
								intent.setClass(activity,
										SendCouponActivity.class);
								startActivity(intent);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void intentLogin() {
		Intent intentLogin = new Intent();
		intentLogin.setClass(activity, LoginActivity.class);
		startActivity(intentLogin);
	}

	@SuppressLint("CutPasteId")
	@Override
	public void findviewWithId() {
		// TODO Auto-generated method stub
		tv_login_reg = (TextView) findViewById(R.id.tv_login_reg);
		btn_login_out = (Button) findViewById(R.id.btn_login_out);
		tv_score_number = (TextView) findViewById(R.id.tv_score_number);
		tv_discount_coupon_number = (TextView) findViewById(R.id.tv_discount_coupon_number);

		mycenter_login_layout = (RelativeLayout) findViewById(R.id.mycenter_login_layout);

		lvUserCenter = (ListView) findViewById(R.id.lv_user_center);

//		ib_header_msg = (ImageButton) findViewById(R.id.ib_header_msg);

		elementIsShow();
	}

	@Override
	public void initListener() {
		btn_login_out.setOnClickListener(this);
		tv_login_reg.setOnClickListener(this);
		mycenter_login_layout.setOnClickListener(this);

		lvUserCenter.setOnItemClickListener(this);

//		ib_header_msg.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		initCenterListView();
		// 没登录的情况下
		if (lists == null || lists.size() == 0) {
			UserCenterListBean fkbz = new UserCenterListBean();
			fkbz.setList_name("反馈帮助");

			UserCenterListBean share = new UserCenterListBean();
			share.setList_name("分享APP");

			UserCenterListBean xtsz = new UserCenterListBean();
			xtsz.setList_name("系统设置");

			lists.add(fkbz);
			lists.add(share);
			lists.add(xtsz);

			lvUserCenter.setAdapter(new UserCenterListAdapter(
					MyCenterActivity.this, lists, R.layout.usercenter_lv_item));

			updateNoReadMsgCount();

		}
	}

	/**
	 * 根据用户的类型进行显示用户中心的列表数据
	 */
	private void initCenterListView() {
		if (isLogin()) {
			if (!BaseUtils.isNetWork(getApplicationContext())) {
				showToast("检查网络");
				return;
			}
			showLoading2("数据加载中");
			RequestParams params = new RequestParams();
			params.addBodyParameter("user_id", String.valueOf(loginId));
			loadData(HttpMethod.POST, GlobalConstant.GET_USER_CENTER_LIST,
					params, new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dismissProgressDialog();
							showToast("加载失败");
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							dismissProgressDialog();
							String response = info.result;

							android.util.Log.v(TAG, "response  " + response);

							if (TextUtils.isEmpty(response)) {
								showToast("加载失败");
							} else {
								Log.v(TAG, response);

								Gson gson = new Gson();
								lists = gson
										.fromJson(
												response,
												new TypeToken<ArrayList<UserCenterListBean>>() {
												}.getType());

								if (lists == null || lists.size() == 0) {
									lists = new ArrayList<UserCenterListBean>();
								}

								if (lists != null && lists.size() > 0) {
									lvUserCenter
											.setAdapter(new UserCenterListAdapter(
													MyCenterActivity.this,
													lists,
													R.layout.usercenter_lv_item));
								}

								getScoreAndDiscountCoupon();
								updateNoReadMsgCount();
							}
						}
					});
		}
	}

	private void getScoreAndDiscountCoupon() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", String.valueOf(loginId));
		http.send(HttpMethod.POST,
				GlobalConstant.USER_ACTION_GET_USER_SCORE_URL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(activity, arg1, Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> result) {
						String resultJson = result.result;

						Log.v(TAG, "resultJson  " + resultJson);

						if (resultJson.contains("-")) {
							String useropenshopidstr = resultJson.split("-")[1];
							int useropenshopid = Integer
									.parseInt(useropenshopidstr);
							UserUtil.setuseropenshopid(activity, useropenshopid);
						} else {
							UserUtil.setuseropenshopid(activity, 0);
						}

						try {
							JSONObject json = new JSONObject(resultJson);
							// System.out.println(resultJson);
							tv_score_number.setText(":"
									+ json.getString("score_num") + "分");
							tv_discount_coupon_number.setText(":"
									+ json.getString("coupon_num") + "张");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	private boolean isLogin() {
		sp = activity.getSharedPreferences("login_session",
				Context.MODE_PRIVATE);
		loginName = sp.getString("login_name", "");
		loginId = sp.getInt("login_id", 0);
		islogin = false;
		if (loginName.length() > 0) {
			islogin = true;
		}
		return islogin;
	}

	private void elementIsShow() {
		if (isLogin()) {
			tv_login_reg.setText(loginName);
			btn_login_out.setVisibility(View.VISIBLE);
			getScoreAndDiscountCoupon();
			updateNoReadMsgCount();
			tv_score_number.setVisibility(View.VISIBLE);
			tv_discount_coupon_number.setVisibility(View.VISIBLE);

		} else {
			tv_login_reg.setText("登录/注册");
			btn_login_out.setVisibility(View.GONE);
			tv_score_number.setVisibility(View.INVISIBLE);
			tv_discount_coupon_number.setVisibility(View.INVISIBLE);
		}
	}
}
