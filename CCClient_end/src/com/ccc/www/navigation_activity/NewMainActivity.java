package com.ccc.www.navigation_activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.ccc.ccclient_end.R;
import com.ccc.www.activity.PrivateSupermarketSelectSchoolActivity;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.ccc.www.util.Xutils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.utils.Log;
import com.umeng.update.UmengUpdateAgent;

public class NewMainActivity extends TabActivity implements OnClickListener {
	private String TAG = "MainActivity";

	public static TabHost mTabHost;
	LinearLayout group_top;

	RadioButton rb_home, rb_shop, rb_moneybag, rb_mycenter;

	public static boolean isForeground = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		// 标记不是第一次启动
		UserUtil.setfirstrun(this, false);

		// UmengUpdateAgent.update(getApplicationContext());
		setContentView(R.layout.activity_newmain);

		mTabHost = this.getTabHost();
		TabSpec homeSpec = mTabHost.newTabSpec("home");
		TabSpec shopSpec = mTabHost.newTabSpec("shop");
		TabSpec sockSpec = mTabHost.newTabSpec("sock");
		TabSpec mycenterSpec = mTabHost.newTabSpec("mycenter");

		homeSpec.setIndicator("home").setContent(
				new Intent(this, HomeActivity.class));
		shopSpec.setIndicator("shop").setContent(
				new Intent(this, ShopActivity.class));
		sockSpec.setIndicator("sock").setContent(
				new Intent(this, SuperMarketActivity.class));
		mycenterSpec.setIndicator("mycenter").setContent(
				new Intent(this, MyCenterActivity.class));
		mTabHost.addTab(homeSpec);
		mTabHost.addTab(shopSpec);
		mTabHost.addTab(sockSpec);
		mTabHost.addTab(mycenterSpec);
		mTabHost.setCurrentTab(0);

		group_top = (LinearLayout) findViewById(R.id.group_top);

		rb_home = (RadioButton) findViewById(R.id.rb_home);
		rb_shop = (RadioButton) findViewById(R.id.rb_shop);
		rb_moneybag = (RadioButton) findViewById(R.id.rb_moneybag);
		rb_mycenter = (RadioButton) findViewById(R.id.rb_mycenter);
		rb_home.setChecked(true);

		rb_home.setOnClickListener(this);
		rb_shop.setOnClickListener(this);
		rb_moneybag.setOnClickListener(this);
		rb_mycenter.setOnClickListener(this);

		rb_home.setChecked(true);

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String arg0) {
				if (arg0.equals("home")) {
					rb_home.setChecked(true);
				} else if (arg0.equals("shop")) {
					rb_shop.setChecked(true);
				} else if (arg0.equals("sock")) {
					rb_moneybag.setChecked(true);
				} else {
					rb_mycenter.setChecked(true);
				}
			}
		});

		/**
		 * 友盟检查更新
		 */
		// UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
		// @Override
		// public void onClick(int status) {
		// switch (status) {
		// case UpdateStatus.Update:
		// break;
		// default:
		// // 友盟自动更新目前还没有提供在代码里面隐藏/显示更新对话框的
		// // "以后再说"按钮的方式，所以在这里弹个Toast比较合适
		// Toast.makeText(NewMainActivity.this, "非常抱歉，您需要更新应用才能继续使用",
		// Toast.LENGTH_SHORT).show();
		// finish();
		// }
		// }
		// });
		// UmengUpdateAgent.setUpdateAutoPopup(false);
		// UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
		// @Override
		// public void onUpdateReturned(int updateStatus,
		// UpdateResponse updateInfo) {
		// switch (updateStatus) {
		// case UpdateStatus.Yes: // has update
		// UmengUpdateAgent.showUpdateDialog(NewMainActivity.this,
		// updateInfo);
		// break;
		// default:
		// break;
		// }
		// }
		// });
		// UmengUpdateAgent.forceUpdate(NewMainActivity.this);

		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);

		registerBoradcastReceiver();

		// 初始化 JPush
		initJpush();

	}

	// 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
	private void initJpush() {
		Log.v(TAG, "initJpush");
		JPushInterface.setDebugMode(true);
		JPushInterface.init(getApplicationContext());

		String regid = JPushInterface.getRegistrationID(this);

		int user_id = UserUtil.getuserid(NewMainActivity.this);
		if (user_id > 0) {
			addtoJpush(user_id);
		}

		Log.v(TAG, "  regid " + regid);
	}

	private void addtoJpush(int user_id) {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			Toast.makeText(NewMainActivity.this, "请检查您的网络", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		String imei_key = JPushInterface.getRegistrationID(this);
		String user_idStr = String.valueOf(user_id);

		Log.v(TAG, "imei_key  " + imei_key);
		Log.v(TAG, "user_id  " + user_id);

		RequestParams params = new RequestParams();
		params.addBodyParameter("imei_key", imei_key);
		params.addBodyParameter("user_id", user_idStr);
		Xutils.loadData(HttpMethod.POST, GlobalConstant.ADD_TO_JPUSH_USER,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(NewMainActivity.this, "设置推送失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						Log.v(TAG, "json	" + json);
						if (BaseUtils.isEmpty(json)) {
							Toast.makeText(NewMainActivity.this, "设置推送失败",
									Toast.LENGTH_SHORT).show();
						} else {
							try {
								JSONObject obj = new JSONObject(json);
								String code = obj.getString("code");
								if (code.equalsIgnoreCase("1")) {
									
								} else {
									Toast.makeText(NewMainActivity.this,
											"设置推送失败", Toast.LENGTH_SHORT)
											.show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	@Override
	protected void onResume() {
		isForeground = true;
		
		JPushInterface.onResume(this);
		
		super.onResume();
	}

	@Override
	protected void onPause() {
		
		JPushInterface.onPause(this);
		
		isForeground = false;
		super.onPause();
	}

	private BroadcastReceiver sockReceiver = new BroadcastReceiver() {
		public void onReceive(Context arg0, Intent intent) {
			String action = intent.getAction();
			if (action.equals("SockReceiver")) {
				mTabHost.setCurrentTab(2);
			}
		}
	};

	public void registerBoradcastReceiver() {
		IntentFilter IntentFilter = new IntentFilter();
		IntentFilter.addAction("SockReceiver");
		registerReceiver(sockReceiver, IntentFilter);
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(sockReceiver);
		super.onDestroy();
	}

	@Override
	public void onClick(View arg0) {
		int id = arg0.getId();
		if (id == R.id.rb_home) {
			mTabHost.setCurrentTab(0);
		} else if (id == R.id.rb_shop) {
			if (mTabHost.getCurrentTab() != 1) {
				mTabHost.setCurrentTab(1);
			}
		} else if (id == R.id.rb_moneybag) {
			if (mTabHost.getCurrentTab() != 2) {
				mTabHost.setCurrentTab(2);
			}
		} else {
			if (mTabHost.getCurrentTab() != 3) {
				mTabHost.setCurrentTab(3);
			}
		}
	}

	public boolean isRunningForeground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		if (currentPackageName != null
				&& currentPackageName.equals(getPackageName())) {
			return true;
		}
		return false;
	}

}
