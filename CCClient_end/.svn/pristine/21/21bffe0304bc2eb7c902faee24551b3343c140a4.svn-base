package com.ccc.www.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.SendSmsJsonBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.CheckUtil;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.SendSmsUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class RegisterActivity extends BaseActivity {

	String TAG = "RegisterActivity";

	EditText et_register_name;
	EditText et_register_pwd;
	EditText et_register_repwd;

	EditText et_register_code;
	TextView et_register_getcode;

	ImageButton ib_register_close;
	Button btn_register;

	CheckBox agree;
	TextView useragree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_register_close:
			RegisterActivity.this.finish();
			break;
		case R.id.btn_register:
			register();
			break;
		case R.id.useragree:

			Intent intent = new Intent();
			intent.setClass(RegisterActivity.this, UserAgreementActivity.class);
			startActivity(intent);

			break;
		case R.id.et_register_getcode:
			String name = et_register_name.getText().toString().trim();
			boolean check = CheckUtil.isMobileNO(name);
			if (TextUtils.isEmpty(name)) {
				showToast("请输入手机号码");
				et_register_name.requestFocus();
				return;
			} else if (!check) {
				showToast("请输入正确的手机号码");
				et_register_name.requestFocus();
				return;
			} else {
				// 发送短信验证码
				get_reg_code(name);
			}

			CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
				@Override
				public void onTick(long millisUntilFinished) {
					et_register_getcode.setText(millisUntilFinished / 1000
							+ "S");
					et_register_getcode.setOnClickListener(null);
				}

				@Override
				public void onFinish() {
					et_register_getcode
							.setOnClickListener(RegisterActivity.this);
					et_register_getcode.setText("获取验证码");
				}
			};
			timer.start();

			break;

		default:
			break;
		}
	}

	int smsResult = 0;

	private void get_reg_code(String mobileNo) {
		final int num = SendSmsUtil.getRandomNumber();
		// 【宅友APP】欢迎您注册宅友app，您的验证码是：#code#，验证码10分钟内有效。感谢您的使用！
		String text = "【宅友APP】欢迎您注册宅友app，您的验证码是：" + num + "，验证码10分钟内有效。感谢您的使用！";
		RequestParams params = new RequestParams();
		params.addBodyParameter("apikey", GlobalConstant.SEND_SMS_AK);
		params.addBodyParameter("text", text);
		params.addBodyParameter("mobile", mobileNo);
		loadData(HttpMethod.POST, GlobalConstant.SEND_SMS_URL, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.v(TAG, arg1);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String json = arg0.result;
						Log.v(TAG, json);
						Gson gson = new Gson();
						SendSmsJsonBean bean = gson.fromJson(json,
								SendSmsJsonBean.class);
						if (bean.getCode() == 0) {
							smsResult = num;
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
		et_register_name = (EditText) findViewById(R.id.et_register_name);
		et_register_pwd = (EditText) findViewById(R.id.et_register_pwd);
		et_register_repwd = (EditText) findViewById(R.id.et_register_repwd);

		ib_register_close = (ImageButton) findViewById(R.id.ib_register_close);
		btn_register = (Button) findViewById(R.id.btn_register);

		et_register_code = (EditText) findViewById(R.id.et_register_code);
		et_register_getcode = (TextView) findViewById(R.id.et_register_getcode);

		agree = (CheckBox) findViewById(R.id.agree);
		useragree = (TextView) findViewById(R.id.useragree);
	}

	@Override
	public void initListener() {
		ib_register_close.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		et_register_getcode.setOnClickListener(this);
		useragree.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		// TODO Auto-generated method stub

	}

	private void register() {
		String name = et_register_name.getText().toString().trim();
		String pwd = et_register_pwd.getText().toString().trim();
		String repwd = et_register_repwd.getText().toString().trim();
		String rep_code = et_register_code.getText().toString().trim();

		if (TextUtils.isEmpty(name)) {
			showToast("请输入手机号码");
			et_register_name.requestFocus();
			return;
		} else {
			boolean check = CheckUtil.isMobileNO(name);
			if (!check) {
				showToast("请输入正确的手机号码");
				et_register_name.requestFocus();
				return;
			}
		}
		if (TextUtils.isEmpty(pwd)) {
			showToast("请输入密码");
			et_register_pwd.requestFocus();
			return;
		} else {

		}
		if (TextUtils.isEmpty(repwd)) {
			showToast("请再次输入密码");
			et_register_repwd.requestFocus();
			return;
		}

		if (!pwd.equalsIgnoreCase(repwd)) {
			showToast("两次密码不一致");
			et_register_repwd.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(rep_code)) {
			showToast("请输入验证码");
			et_register_code.requestFocus();
			return;
		} else {
			if (!CheckUtil.isIntAll(rep_code)) {
				showToast("验证码不正确");
				et_register_code.requestFocus();
				return;
			}

			if (Integer.parseInt(rep_code) != smsResult) {
				showToast("验证码不正确");
				et_register_code.requestFocus();
				return;
			}
		}

		boolean isagree = agree.isChecked();
		if (!isagree) {
			showToast("请同意用户注册协议");
			return;
		}

		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在注册");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_name", name);
		params.addBodyParameter("user_pwd", pwd.toLowerCase());
		loadData(HttpMethod.POST, GlobalConstant.ADD_USER, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("注册失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {

						dismissProgressDialog();
						String json = info.result;
						Log.v(TAG, json);
						if (BaseUtils.isEmpty(json)) {
							showToast("注册失败");
						} else {
							try {
								JSONObject jsonob = new JSONObject(json);
								String returnMsg = jsonob
										.getString("returnMsg");
								showToast(returnMsg);
								if (returnMsg.contains("成功")) {
									RegisterActivity.this.finish();
								}

							} catch (JSONException e) {
								showToast("注册失败");
								e.printStackTrace();
							}

						}
					}
				});

	}

}
