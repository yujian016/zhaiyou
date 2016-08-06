package com.ccc.www.activity;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class RedEnvelope2Activity extends BaseActivity {

	String TAG = "RedEnvelope2Activity";

	ImageButton ib_goods_detail_goback;

	int id = 0;
	int odds = 0;
	int redbagmoney = 0;
	int moneyNeedCount = 0;

	LinearLayout regbag;

	int user_id;

	View rootview;

	Timer timerRedBagStatus = new Timer();

	int timerRedBagStatuscount = 0;
	String start_time = "";

	// 红包
	boolean regbagover = true;

	// 分享控制
	UMSocialService mController;

	String shop_name = "";

	// 每个账户能中奖的次数
	int get_money_count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_redenvelope2);
		initview();

		registerReceiver(regBagClode, new IntentFilter(
				GlobalConstant.RegBagClode));

		mController = UMServiceFactory.getUMSocialService("com.umeng.share");

		user_id = UserUtil.getuserid(this);

		/**
		 * 红包摇动次数清零
		 */
		UserUtil.setredbagshakecount(this, 0);

		rootview = LayoutInflater.from(this).inflate(R.layout.act_redenvelope2,
				null);

		// 获取红包是否结束
		timerRedBagStatus.schedule(new TimerTask() {
			@Override
			public void run() {

				Log.v(TAG, "GET_REDBAG_IS_OVER  id " + id);

				if (id != 0) {

					RequestParams params = new RequestParams();
					params.addBodyParameter("red_packet_id", "" + id);
//					if (timerRedBagStatuscount == 0) {
//						params.addBodyParameter("start_type", "" + 1);
//					} else {
//						params.addBodyParameter("start_type", "" + 0);
//
//						if (!TextUtils.isEmpty(start_time)) {
//							params.addBodyParameter("start_time", start_time);
//						}
//
//					}
					loadData(HttpMethod.POST,
							GlobalConstant.GET_REDBAG_IS_OVER, params,
							new RequestCallBack<String>() {
								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									Log.v(TAG, "GET_REDBAG_IS_OVER  onFailure "
											+ arg1);
								}

								//
								@Override
								public void onSuccess(ResponseInfo<String> info) {
									String returnstr = info.result;
									Log.v(TAG, "returnstr  timer  " + returnstr);

									timerRedBagStatuscount++;

									if (BaseUtils.isEmpty(returnstr)) {
										//
									} else {
										try {
											JSONObject json = new JSONObject(
													returnstr);
											String code = json
													.getString("code");
											String msg = json.getString("msg");
											if (code.equalsIgnoreCase("0")
													|| code.equalsIgnoreCase("2")) {
												runOnUiThread(new Runnable() {
													@Override
													public void run() {
														popRegBagOver();
													}
												});
												showToast("本轮红包已结束！");

												RedEnvelope2Activity.this
														.finish();

											} else if (code
													.equalsIgnoreCase("1")) {
												// 红包金额没了 时间还没结束
												regbagover = true;
												regbag.setOnClickListener(RedEnvelope2Activity.this);
											} else if (code
													.equalsIgnoreCase("4")) {
												start_time = msg;
												regbagover = false;
												regbag.setOnClickListener(RedEnvelope2Activity.this);
											} else {
												regbagover = false;
												regbag.setOnClickListener(RedEnvelope2Activity.this);
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								}
							});
				}
			}
		}, 0, 5 * 1000);
	}

	/**
	 * 弹出红包活动结束
	 */
	private void popRegBagOver() {
		// TODO Auto-generated method stub

	}

	long lastclicktime = 0;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_goods_detail_goback:
			RedEnvelope2Activity.this.finish();
			break;
		case R.id.regbag:
//			showToast("点击抢红包");

			long nowtime = new Date().getTime();
			if (nowtime - lastclicktime < 2000) {
				return;
			}

			lastclicktime = nowtime;

			// 加载
			int redbagshakecount = UserUtil
					.getredbagshakecount(RedEnvelope2Activity.this);
			redbagshakecount++;
			UserUtil.setredbagshakecount(RedEnvelope2Activity.this,
					redbagshakecount);

			if (redbagshakecount >= moneyNeedCount) {
				// 请求红包真实请求
				getMoney(true);
			} else {
				// 请求红包--假请求
				getMoney(false);
			}
			break;

		default:
			break;
		}
	}

	int questregbagcount = 0;

	/**
	 * 请求中奖
	 */
	private void getMoney(boolean isreal) {

		// moneyNeedCount = 0;

		if (regbagover) {

//			showToast("247");

			// 假请求
			try {
				Thread.sleep(1300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			questregbagcount++;
			if (questregbagcount == 3) {
				questregbagcount = 0;
				// 弹出分享增加中奖概率
				popshare();
			} else {
				showToast("没有抢到红包喔，再接再厉！");
			}
			return;
		}

		if (isreal && !regbagover) {
			boolean zhongjiang = true;
			// 取出保存的用户id 红包id 次数
			String userid_redbagid_hadcount = UserUtil
					.getuserid_redbagid_hadcount(this);
			if (TextUtils.isEmpty(userid_redbagid_hadcount)) {
				UserUtil.setuserid_redbagid_hadcount(RedEnvelope2Activity.this,
						user_id + "_" + id + "_" + 1);
			} else {
				String useridstr = userid_redbagid_hadcount.split("_")[0];
				String redidstr = userid_redbagid_hadcount.split("_")[1];
				String countstr = userid_redbagid_hadcount.split("_")[2];
				// && redidstr.equalsIgnoreCase(""+ id)
				if (useridstr.equalsIgnoreCase("" + user_id)) {
					if (redidstr.equalsIgnoreCase("" + id)) {
						Integer countint = Integer.parseInt(countstr);
						if (countint < get_money_count) {
							countint++;
							UserUtil.setuserid_redbagid_hadcount(
									RedEnvelope2Activity.this, user_id + "_"
											+ id + "_" + countint);
						} else {
							// 不中奖
							zhongjiang = false;
						}
					} else {
						UserUtil.setuserid_redbagid_hadcount(
								RedEnvelope2Activity.this, user_id + "_" + id
										+ "_" + 1);
					}
				} else {
					UserUtil.setuserid_redbagid_hadcount(
							RedEnvelope2Activity.this, user_id + "_" + id + "_"
									+ 1);
				}
			}

//			showToast("302	" + zhongjiang);

			if (zhongjiang) {

				questregbagcount = 0;

				UserUtil.setredbagshakecount(this, 0);
				// 重新计算中奖次数

				if (odds == 1) {
					moneyNeedCount = 0;
				} else {
					moneyNeedCount = new Random().nextInt(odds) + 1;
				}
				// 中奖接口
				getmoney_server();
			} else {
				// 假请求
				try {
					Thread.sleep(1300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				questregbagcount++;
				if (questregbagcount == 3) {
					questregbagcount = 0;
					// 弹出分享增加中奖概率
					popshare();
				} else {
					showToast("没有抢到红包喔，再接再厉！");
				}
			}
		} else {
			// 假请求
			try {
				Thread.sleep(1300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			questregbagcount++;
			if (questregbagcount == 3) {
				questregbagcount = 0;
				// 弹出分享增加中奖概率
				popshare();
			} else {
				showToast("没有抢到红包喔，再接再厉！");
			}
		}
	}

	/**
	 * 弹出分享增加中奖概率
	 */
	private void popshare() {
		View view1 = LayoutInflater.from(RedEnvelope2Activity.this).inflate(
				R.layout.pop_redbag_share, null);
		mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);

		TextView pop_redbag_money_receive = (TextView) view1
				.findViewById(R.id.pop_redbag_money_receive);

		pop_redbag_money_receive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dissmisspopwindow();
				shareApp();
			}
		});

		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
		mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);

	}

	/**
	 * 中奖操作
	 */
	private void getmoney_server() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在加载");

		float red_packet_moeny = (float) (1 + Math.random()
				* (redbagmoney - 1 + 1)) / 10.0f;

		DecimalFormat df = new DecimalFormat("######0.00");

		final String red_packet_moenyStr = df.format(red_packet_moeny);

		RequestParams params = new RequestParams();
		params.addBodyParameter("red_packet_id", "" + id);
		params.addBodyParameter("user_id", "" + user_id);
		params.addBodyParameter("red_packet_moeny", "" + red_packet_moenyStr);
		loadData(HttpMethod.POST, GlobalConstant.GET_REDBAG_MONEY, params,
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
						Log.v(TAG, "returnstr getmoney_server  " + returnstr);
						if (BaseUtils.isEmpty(returnstr)) {
							showToast("加载失败");
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);
								id = json.getInt("id");
								odds = json.getInt("odds");
								redbagmoney = json
										.getInt("red_packet_sub_money");

								if (odds == 1) {
									moneyNeedCount = 0;
								} else {
									moneyNeedCount = new Random().nextInt(odds) + 1;
								}
								/**
								 * 弹出领红包界面
								 */
								popGetMoneyTip(red_packet_moenyStr);

								MediaPlayer player = MediaPlayer.create(
										RedEnvelope2Activity.this,
										R.raw.newdatatoast);
								player.setLooping(false);
								player.start();

							} catch (JSONException e) {
								showToast("加载失败");
								e.printStackTrace();
							}
						}
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findviewWithId() {
		ib_goods_detail_goback = (ImageButton) findViewById(R.id.ib_goods_detail_goback);
		regbag = (LinearLayout) findViewById(R.id.regbag);
	}

	@Override
	public void initListener() {
		ib_goods_detail_goback.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		// TODO Auto-generated method stub
		// 接口获取红包信息
		getRadBagInfo(true);
	}

	/**
	 * 接口获取红包信息
	 */
	private void getRadBagInfo(boolean showload) {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}
		if (showload) {
			showLoading2("正在加载红包信息");
		}

		RequestParams params = new RequestParams();
		loadData(HttpMethod.POST, GlobalConstant.GET_REDBAG_INFO, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String returnstr = info.result;
						Log.v(TAG, "returnstr  " + returnstr);
						if (BaseUtils.isEmpty(returnstr)) {
							if (returnstr.equalsIgnoreCase("null")) {
								showToast("抢红包活动暂未开始");
								return;
							}
						} else {
							if (returnstr.equalsIgnoreCase("null")) {
								showToast("抢红包活动暂未开始");
								return;
							}

							try {
								JSONObject json = new JSONObject(returnstr);
								id = json.getInt("id");
								odds = json.getInt("odds");
								redbagmoney = json
										.getInt("red_packet_sub_money");
								int status = json.getInt("status");
								if (status == 0) {
									showToast("抢红包活动暂未开始");

									RedEnvelope2Activity.this.finish();

									return;
								}

								if (json.has("shop_name")) {
									shop_name = json.getString("shop_name");
								}

								if (json.has("get_money_count")) {
									get_money_count = json
											.getInt("get_money_count");
								}

								if (odds == 1) {
									moneyNeedCount = 0;
								} else {
									moneyNeedCount = new Random().nextInt(odds) + 1;
								}

								Log.v(TAG, "odds  " + odds);
								Log.v(TAG, "redbagmoney  " + redbagmoney);
								Log.v(TAG, "moneyNeedCount  " + moneyNeedCount);

								checkover();

							} catch (JSONException e) {
								showToast("加载失败");
								e.printStackTrace();
							}
						}
					}
				});
	}

	private void checkover() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addBodyParameter("red_packet_id", "" + id);
//		if (timerRedBagStatuscount == 0) {
//			params.addBodyParameter("start_type", "" + 1);
//		} else {
//			params.addBodyParameter("start_type", "" + 0);
//
//			if (!TextUtils.isEmpty(start_time)) {
//				params.addBodyParameter("start_time", start_time);
//			}
//
//		}
		loadData(HttpMethod.POST, GlobalConstant.GET_REDBAG_IS_OVER, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {

						dismissProgressDialog();

						showToast("加载失败");
					}

					//
					@Override
					public void onSuccess(ResponseInfo<String> info) {

						dismissProgressDialog();

						String returnstr = info.result;
						Log.v(TAG, "returnstr  timer  " + returnstr);

						timerRedBagStatuscount++;

						if (BaseUtils.isEmpty(returnstr)) {
							//
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);
								String code = json.getString("code");
								String msg = json.getString("msg");
								if (code.equalsIgnoreCase("0")
										|| code.equalsIgnoreCase("2")) {
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											popRegBagOver();
										}
									});
									showToast("本轮红包已结束！");

									RedEnvelope2Activity.this.finish();

								} else if (code.equalsIgnoreCase("1")) {
									// 红包金额没了 时间还没结束
									regbagover = true;
									regbag.setOnClickListener(RedEnvelope2Activity.this);
								} else if (code.equalsIgnoreCase("4")) {
									start_time = msg;
									regbagover = false;
									regbag.setOnClickListener(RedEnvelope2Activity.this);
								} else {
									regbagover = false;
									regbag.setOnClickListener(RedEnvelope2Activity.this);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	/**
	 * 弹出中奖提示
	 */
	PopupWindow mPopupWindow;

	private void dissmisspopwindow() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

	/**
	 * 弹出领红包界面
	 */
	private void popGetMoneyTip(String red_packet_moeny) {
		View view1 = LayoutInflater.from(RedEnvelope2Activity.this).inflate(
				R.layout.pop_redbag, null);
		mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);

		TextView pop_redbag_money = (TextView) view1
				.findViewById(R.id.pop_redbag_money);
		TextView pop_redbag_money_receive = (TextView) view1
				.findViewById(R.id.pop_redbag_money_receive);
		TextView pop_redbag_from = (TextView) view1
				.findViewById(R.id.pop_redbag_from);

		pop_redbag_money.setText(red_packet_moeny + "");

		if (TextUtils.isEmpty(shop_name)) {
			pop_redbag_from.setText("红包到啦");
		} else {
			pop_redbag_from.setText("来自【" + shop_name + "】的红包");
		}

		pop_redbag_money_receive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showToast("红包已添加到您的钱包");
				dissmisspopwindow();
			}
		});

		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
		mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);

	}

	/**
	 * 分享APP
	 */
	private void shareApp() {
		// TODO Auto-generated method stub
		// 分享
		String title = "上大学有宅友就够了";
		String content = "下载宅友APP抢百万现金红包，可以提现哦~";
		String targeturl = "http://m.pp.cn/detail.html?appid=7045573&ch_src=pp_pp_share&ch=qq";
		// 设置分享到QQ好友的内容
		QQShareContent qqsharecontent = new QQShareContent();
		qqsharecontent.setTitle(title);
		qqsharecontent.setTargetUrl(targeturl);
		qqsharecontent.setShareContent(content);
		qqsharecontent.setShareImage(new UMImage(RedEnvelope2Activity.this,
				R.drawable.logo));
		mController.setShareMedia(qqsharecontent);

		// 设置分享到QQ空间的内容
		QZoneShareContent qzonesharecontent = new QZoneShareContent();
		qzonesharecontent.setTitle(title);
		qzonesharecontent.setTargetUrl(targeturl);
		qzonesharecontent.setShareContent(content);
		qzonesharecontent.setShareImage(new UMImage(RedEnvelope2Activity.this,
				R.drawable.logo));
		mController.setShareMedia(qzonesharecontent);

		// 设置分享到微信还有
		WeiXinShareContent weixinsharecontent = new WeiXinShareContent();
		weixinsharecontent.setTitle(title);
		weixinsharecontent.setTargetUrl(targeturl);
		weixinsharecontent.setShareContent(content);
		weixinsharecontent.setShareImage(new UMImage(RedEnvelope2Activity.this,
				R.drawable.logo));
		mController.setShareMedia(weixinsharecontent);

		// 设置分享到朋友圈
		CircleShareContent circlesharecontent = new CircleShareContent();
		circlesharecontent.setTitle(title);
		circlesharecontent.setTargetUrl(targeturl);
		circlesharecontent.setShareContent(content);
		circlesharecontent.setShareImage(new UMImage(RedEnvelope2Activity.this,
				R.drawable.logo));
		mController.setShareMedia(circlesharecontent);

		String appID = "wx77a5cd57027cac1b";
		String appSecret = "3fde70a7985b5fd91d2a15d3f8fe037e";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(RedEnvelope2Activity.this,
				appID, appSecret);
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(
				RedEnvelope2Activity.this, appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// 分享给QQ好友
		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(
				RedEnvelope2Activity.this, "1104738145", "dBCimScHR8IThP5E");
		qqSsoHandler.addToSocialSDK();

		// SSO（免登录）分享到QQ空间
		// 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
				RedEnvelope2Activity.this, "1104738145", "dBCimScHR8IThP5E");
		qZoneSsoHandler.addToSocialSDK();

		// 新浪的注册key
		// App Key:3699447690
		// 976765ce47f666d9b9bd98c6ef4e8154

		// 分享

		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE);
		mController.openShare(RedEnvelope2Activity.this, false);
	}

	@Override
	protected void onDestroy() {
		if (timerRedBagStatus != null) {
			timerRedBagStatus.cancel();
			timerRedBagStatus = null;
		}
		unregisterReceiver(regBagClode);
		super.onDestroy();
	}

	RegBagClode regBagClode = new RegBagClode();

	class RegBagClode extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			showToast("本轮红包已结束");
			RedEnvelope2Activity.this.finish();
		}
	}

}
