package com.ccc.www.activity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ShakeListener;
import com.ccc.www.util.ShakeListener.OnShakeListener;
import com.ccc.www.util.UserUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 红包专区
 * 
 * @author Administrator
 * 
 */
public class RedEnvelopeActivity extends BaseActivity {

	String TAG = "RedEnvelopeActivity";

	ImageButton ib_goods_detail_goback;

	ShakeListener mShakeListener = null;
	Vibrator mVibrator;
	private RelativeLayout mImgUp;
	private RelativeLayout mImgDn;

	int id = 0;
	int odds = 0;
	int redbagmoney = 0;
	int moneyNeedCount = 0;

	int user_id;

	View rootview;

	Timer timerRedBagStatus = new Timer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		user_id = UserUtil.getuserid(this);

		/**
		 * 红包摇动次数清零
		 */
		UserUtil.setredbagshakecount(this, 0);

		setContentView(R.layout.activity_redenvelope);

		rootview = LayoutInflater.from(this).inflate(
				R.layout.activity_redenvelope, null);

		initview();

		// 获取红包是否结束
		timerRedBagStatus.schedule(new TimerTask() {
			@Override
			public void run() {

				Log.v(TAG, "GET_REDBAG_IS_OVER  id " + id);

				if (id != 0) {
					RequestParams params = new RequestParams();
					params.addBodyParameter("red_packet_id", "" + id);
					loadData(HttpMethod.POST,
							GlobalConstant.GET_REDBAG_IS_OVER, params,
							new RequestCallBack<String>() {
								@Override
								public void onFailure(HttpException arg0,
										String arg1) {

									Log.v(TAG, "GET_REDBAG_IS_OVER  onFailure "
											+ arg1);

								}

								@Override
								public void onSuccess(ResponseInfo<String> info) {
									String returnstr = info.result;
									Log.v(TAG, "returnstr  timer  " + returnstr);
									if (BaseUtils.isEmpty(returnstr)) {

									} else {
										try {
											JSONObject json = new JSONObject(
													returnstr);
											String code = json
													.getString("code");
											String msg = json.getString("msg");

											if (code.equalsIgnoreCase("0")) {
												
												runOnUiThread(new Runnable() {
													@Override
													public void run() {
														popRegBagOver();
													}
												});
												
												showToast(msg);
												RedEnvelopeActivity.this
														.finish();
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								}
							});
				}
			}
		}, 5 * 1000, 5 * 1000);
	}
	
	/**
	 * 弹出红包活动结束
	 */
	private void popRegBagOver() {
		// TODO Auto-generated method stub
  
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_goods_detail_goback:
			RedEnvelopeActivity.this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		if (mVibrator != null) {
			mVibrator.cancel();
		}
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
		if (timerRedBagStatus != null) {
			timerRedBagStatus.cancel();
			timerRedBagStatus = null;
		}
		super.onDestroy();
	}

	@Override
	public void onPause() {
		if (mVibrator != null) {
			mVibrator.cancel();
		}
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
		super.onPause();
	}

	@Override
	protected void onStart() {
		if (mShakeListener != null) {
			mShakeListener.start();
		}
		super.onStart();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findviewWithId() {
		ib_goods_detail_goback = (ImageButton) findViewById(R.id.ib_goods_detail_goback);
		mImgUp = (RelativeLayout) findViewById(R.id.shakeImgUp);
		mImgDn = (RelativeLayout) findViewById(R.id.shakeImgDown);
	}

	@Override
	public void initListener() {
		ib_goods_detail_goback.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		initAnimation();
		// 接口获取红包信息
		getRadBagInfo();
	}

	private void initAnimation() {
		mVibrator = (Vibrator) getApplication().getSystemService(
				VIBRATOR_SERVICE);
		mShakeListener = new ShakeListener(RedEnvelopeActivity.this);
		mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				// Toast.makeText(getApplicationContext(),
				// "抱歉，暂时没有找到在同一时刻摇一摇的人。\n再试一次吧！", Toast.LENGTH_SHORT).show();
				startAnim(); // 开始 摇一摇手掌动画
				mShakeListener.stop();

				startVibrato(); // 开始 震动
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						mVibrator.cancel();
						// 加载
						int redbagshakecount = UserUtil
								.getredbagshakecount(RedEnvelopeActivity.this);
						redbagshakecount++;
						UserUtil.setredbagshakecount(RedEnvelopeActivity.this,
								redbagshakecount);

						if (redbagshakecount >= moneyNeedCount) {
							// 请求红包真实请求
							getMoney(true);
						} else {
							// 请求红包--假请求
							getMoney(false);
						}
					}
				}, 2000);
			}
		});
	}

	/**
	 * 请求中奖
	 */
	private void getMoney(boolean isreal) {

		// moneyNeedCount = 0;

		if (isreal) {
			UserUtil.setredbagshakecount(this, 0);
			// 重新计算中奖次数
			moneyNeedCount = new Random().nextInt(odds) + 1;
			// 中奖接口
			getmoney_server();
			mShakeListener.start();
		} else {
			// 假请求
			try {
				Thread.sleep(1300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mShakeListener.start();
			showToast("没有抢到红包喔，再接再厉！");
		}
	}

	/**
	 * 接口获取红包信息
	 */
	private void getRadBagInfo() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}
		showLoading2("正在加载红包信息");
		RequestParams params = new RequestParams();
		loadData(HttpMethod.POST, GlobalConstant.GET_REDBAG_INFO, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
						if (mVibrator != null) {
							mVibrator.cancel();
						}
						if (mShakeListener != null) {
							mShakeListener.stop();
						}
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String returnstr = info.result;
						Log.v(TAG, "returnstr  " + returnstr);
						if (BaseUtils.isEmpty(returnstr)) {
							if (returnstr.equalsIgnoreCase("null")) {
								showToast("抢红包活动暂未开始");
								RedEnvelopeActivity.this.finish();
								return;
							}
						} else {
							if (returnstr.equalsIgnoreCase("null")) {
								showToast("抢红包活动暂未开始");
								RedEnvelopeActivity.this.finish();
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
									RedEnvelopeActivity.this.finish();
									return;
								}

								moneyNeedCount = new Random().nextInt(odds) + 1;

								Log.v(TAG, "odds  " + odds);
								Log.v(TAG, "redbagmoney  " + redbagmoney);
								Log.v(TAG, "moneyNeedCount  " + moneyNeedCount);

							} catch (JSONException e) {
								showToast("加载失败");
								if (mVibrator != null) {
									mVibrator.cancel();
								}
								if (mShakeListener != null) {
									mShakeListener.stop();
								}
								e.printStackTrace();
							}
						}
					}
				});
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
		RequestParams params = new RequestParams();
		params.addBodyParameter("red_packet_id", "" + id);
		params.addBodyParameter("user_id", "" + user_id);
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

								moneyNeedCount = new Random().nextInt(odds) + 1;
								/**
								 * 弹出领红包界面
								 */
								popGetMoneyTip();

								MediaPlayer player = MediaPlayer.create(
										RedEnvelopeActivity.this,
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
	private void popGetMoneyTip() {
		View view1 = LayoutInflater.from(RedEnvelopeActivity.this).inflate(
				R.layout.pop_redbag, null);
		mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);

		TextView pop_redbag_money = (TextView) view1
				.findViewById(R.id.pop_redbag_money);
		TextView pop_redbag_money_receive = (TextView) view1
				.findViewById(R.id.pop_redbag_money_receive);

		pop_redbag_money.setText(redbagmoney + "");

		pop_redbag_money_receive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showToast("红包已添加到您的余额");
				dissmisspopwindow();
			}
		});

		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
		mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);

	}

	public void startAnim() { // 定义摇一摇动画动画
		AnimationSet animup = new AnimationSet(true);
		TranslateAnimation mytranslateanimup0 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				-0.5f);
		mytranslateanimup0.setDuration(1000);
		TranslateAnimation mytranslateanimup1 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				+0.5f);
		mytranslateanimup1.setDuration(1000);
		mytranslateanimup1.setStartOffset(1000);
		animup.addAnimation(mytranslateanimup0);
		animup.addAnimation(mytranslateanimup1);
		mImgUp.startAnimation(animup);

		AnimationSet animdn = new AnimationSet(true);
		TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				+0.5f);
		mytranslateanimdn0.setDuration(1000);
		TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
				-0.5f);
		mytranslateanimdn1.setDuration(1000);
		mytranslateanimdn1.setStartOffset(1000);
		animdn.addAnimation(mytranslateanimdn0);
		animdn.addAnimation(mytranslateanimdn1);
		mImgDn.startAnimation(animdn);
	}

	public void startVibrato() {
		MediaPlayer player;
		player = MediaPlayer.create(this, R.raw.shake_sound);
		player.setLooping(false);
		player.start();

		// 定义震动
		mVibrator.vibrate(new long[] { 500, 200, 500, 200 }, -1);
		// 第一个｛｝里面是节奏数组，
		// 第二个参数是重复次数，-1为不重复，非-1俄日从pattern的指定下标开始重复
	}

}
