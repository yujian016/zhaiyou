package com.ccc.www.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.AdImagePagerAdapter;
import com.ccc.www.bean.AdBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class LoginActivity extends BaseActivity {

	String TAG = "LoginActivity";

	protected static final String LOGIN_BROAD = "login_ok";
	private Button loginBtn;
	private EditText et_login_name;
	private EditText et_login_pwd;

	private String loginName = "";
	private String loginPwd = "";
	private ImageButton ib_login_close;

	TextView tv_forget_pwd;
	TextView tv_reg_user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initview();
	}

	private void login() {
		loginName = et_login_name.getText().toString().trim();
		loginPwd = et_login_pwd.getText().toString().trim();
		int checkResult = checkEmpty(loginName, loginPwd);
		if (checkResult != 0) {
			Toast.makeText(LoginActivity.this, "账号密码不能为空", Toast.LENGTH_LONG)
					.show();
		} else {
			HttpUtils http = new HttpUtils();
			RequestParams params = new RequestParams();
			params.addBodyParameter("userName", loginName);
			params.addBodyParameter("userPwd", loginPwd);
			http.send(HttpMethod.POST, GlobalConstant.USER_ACTION_IS_LOGIN_URL,
					params, new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String msg) {
							Toast.makeText(LoginActivity.this, msg,
									Toast.LENGTH_SHORT).show();
							showToast("onFailure  连接服务器失败");
							// dismissProgressDialog();
						}

						@Override
						public void onSuccess(ResponseInfo<String> info) {
							try {
								String result = info.result;
								if (result.length() == 0) {
									showLoading2("正在登录,请稍后");
								} else {
									JSONObject resultJson = new JSONObject(
											result);
									int beanId = resultJson.getInt("beanId");
									String code = resultJson
											.getString("resultCode");
									String msg = resultJson
											.getString("resultMsg");
									System.out.println(resultJson);
									if (code.equals("0")) {
										Toast.makeText(LoginActivity.this, msg,
												Toast.LENGTH_SHORT).show();
									} else if (code.equals("1")) {
										Toast.makeText(LoginActivity.this, msg,
												Toast.LENGTH_SHORT).show();
									} else {
										UserUtil.setuserid(LoginActivity.this,
												beanId);

										SharedPreferences.Editor sp_editor = LoginActivity.this
												.getSharedPreferences(
														"login_session",
														MODE_PRIVATE).edit();
										sp_editor.putString("login_name",
												loginName);
										sp_editor.putInt("login_id", beanId);
										sp_editor.commit();

										addtoJpush(beanId);

									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		}
	}

	private void addtoJpush(int user_id) {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		String imei_key = JPushInterface.getRegistrationID(this);
		String user_idStr = String.valueOf(user_id);

		Log.v(TAG, "imei_key  " + imei_key);
		Log.v(TAG, "user_id  " + user_id);

		RequestParams params = new RequestParams();
		params.addBodyParameter("imei_key", imei_key);
		params.addBodyParameter("user_id", user_idStr);
		loadData(HttpMethod.POST, GlobalConstant.ADD_TO_JPUSH_USER, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String json = info.result;
						Log.v(TAG, "json	" + json);
						if (BaseUtils.isEmpty(json)) {
							showToast("加载失败");
						} else {
							try {
								JSONObject obj = new JSONObject(json);
								String code = obj.getString("code");
								if (code.equalsIgnoreCase("1")) {
									LoginActivity.this.finish();
								} else {
									showToast("加载失败");
								}
							} catch (JSONException e) {
								showToast("加载失败");
								e.printStackTrace();
							}
						}
					}
				});
	}

	private int checkEmpty(String loginName, String loginPwd) {
		if (loginName.length() == 0) {
			return 1;
		} else if (loginPwd.length() == 0) {
			return 2;
		}
		return 0;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
	}

	@Override
	public void findviewWithId() {
		loginBtn = (Button) findViewById(R.id.btn_login);
		et_login_name = (EditText) findViewById(R.id.et_login_name);
		et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
		ib_login_close = (ImageButton) findViewById(R.id.ib_login_close);

		tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
		tv_reg_user = (TextView) findViewById(R.id.tv_reg_user);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		loginBtn.setOnClickListener(this);
		ib_login_close.setOnClickListener(this);
		tv_forget_pwd.setOnClickListener(this);
		tv_reg_user.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:
			login();
			break;
		case R.id.ib_login_close:
			LoginActivity.this.finish();
			break;
		case R.id.tv_forget_pwd:
			Intent forget_pwd = new Intent();
			forget_pwd.setClass(LoginActivity.this, ForgetPwdActivity.class);
			startActivity(forget_pwd);
			break;
		case R.id.tv_reg_user:
			Intent register = new Intent();
			register.setClass(LoginActivity.this, RegisterActivity.class);
			startActivity(register);
			break;
		default:
			break;
		}
	}

	@Override
	public void initdata() {
		// TODO Auto-generated method stub

	}

}
