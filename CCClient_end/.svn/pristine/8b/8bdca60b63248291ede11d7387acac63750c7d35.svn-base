package com.ccc.www.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class ForgetPwdActivity extends BaseActivity {

	String TAG = "ForgetPwdActivity";

	ImageButton ib_forgetpwd_close;
	EditText et_forgetpwd_name;
	EditText et_forgetpwd_code;
	TextView tv_forget_getcode;
	EditText et_forgetpwd_pwd;
	EditText et_forgetpwd_repwd;
	Button btn_forgetpwd_submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_forgetpwd);

		initview();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_forgetpwd_close:
			ForgetPwdActivity.this.finish();
			break;
		case R.id.tv_forget_getcode:
			getcode();
			break;
		case R.id.btn_forgetpwd_submit:
			submit();
			break;
		default:
			break;
		}
	}

	/**
	 * 获取验证码
	 */
	private void getcode() {
		String name = et_forgetpwd_name.getText().toString().trim();
		if (TextUtils.isEmpty(name)) {
			showToast("请输入手机号码");
			et_forgetpwd_name.requestFocus();
			return;
		} else {
			boolean check = CheckUtil.isMobileNO(name);
			if (!check) {
				showToast("请输入正确格式的手机号码");
				et_forgetpwd_name.requestFocus();
				return;
			} else {
				// 发送短信验证码
				get_reg_code(name);
			}
		}

		CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				tv_forget_getcode.setText(millisUntilFinished / 1000 + "S");
				tv_forget_getcode.setOnClickListener(null);
			}

			@Override
			public void onFinish() {
				tv_forget_getcode.setOnClickListener(ForgetPwdActivity.this);
				tv_forget_getcode.setText("获取验证码");
			}
		};
		timer.start();

	}

	int smsResult = 0;

	private void get_reg_code(String mobileNo) {
		final int num = SendSmsUtil.getRandomNumber();
		// 【宅友APP】欢迎您使用宅友app，您重置密码的随机码是：#code#，10分钟内有效。感谢您的使用！
		String text = "【宅友APP】欢迎您使用宅友app，您重置密码的随机码是：" + num
				+ "，10分钟内有效。感谢您的使用！";
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

	/**
	 * 提交数据
	 */
	private void submit() {
		String name = et_forgetpwd_name.getText().toString().trim();
		String code = et_forgetpwd_code.getText().toString().trim();
		String pwd = et_forgetpwd_pwd.getText().toString().trim();
		String repwd = et_forgetpwd_repwd.getText().toString().trim();

		if (TextUtils.isEmpty(name)) {
			showToast("请输入手机号码");
			et_forgetpwd_name.requestFocus();
			return;
		} else {
			boolean check = CheckUtil.isMobileNO(name);
			if (!check) {
				showToast("请输入正确格式的手机号码");
				et_forgetpwd_name.requestFocus();
				return;
			}
		}
		if (TextUtils.isEmpty(code)) {
			showToast("请输入验证码");
			et_forgetpwd_code.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(pwd)) {
			showToast("请输入密码");
			et_forgetpwd_pwd.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(repwd)) {
			showToast("请重复输入密码");
			et_forgetpwd_repwd.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(name)) {
			showToast("请输入用户名");
			et_forgetpwd_name.requestFocus();
			return;
		}

		if (!pwd.equalsIgnoreCase(repwd)) {
			showToast("两次输入的密码不一致");
			et_forgetpwd_repwd.requestFocus();
			return;
		}

		if (!CheckUtil.isIntAll(code)) {
			showToast("验证码不正确");
			et_forgetpwd_code.requestFocus();
			return;
		}

		if (Integer.parseInt(code) != smsResult) {
			showToast("验证码不正确");
			et_forgetpwd_code.requestFocus();
			return;
		}

		showLoading2("正在重置密码");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_name", name);
		params.addBodyParameter("user_pwd", pwd.toLowerCase());
		loadData(HttpMethod.POST, GlobalConstant.GORGET_USER_PWD, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("重置密码失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {

						dismissProgressDialog();
						String json = info.result;
						Log.v(TAG, json);
						if (BaseUtils.isEmpty(json)) {
							showToast("服务器异常");
						} else {
							try {
								JSONObject jsonob = new JSONObject(json);
								String returnMsg = jsonob
										.getString("returnMsg");
								showToast(returnMsg);
								if (returnMsg.contains("成功")) {
									ForgetPwdActivity.this.finish();
								} else {
									showToast(returnMsg);
								}

							} catch (JSONException e) {
								showToast("重置密码异常");
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
		ib_forgetpwd_close = (ImageButton) findViewById(R.id.ib_forgetpwd_close);
		et_forgetpwd_name = (EditText) findViewById(R.id.et_forgetpwd_name);
		et_forgetpwd_code = (EditText) findViewById(R.id.et_forgetpwd_code);
		tv_forget_getcode = (TextView) findViewById(R.id.tv_forget_getcode);
		et_forgetpwd_pwd = (EditText) findViewById(R.id.et_forgetpwd_pwd);
		et_forgetpwd_repwd = (EditText) findViewById(R.id.et_forgetpwd_repwd);
		btn_forgetpwd_submit = (Button) findViewById(R.id.btn_forgetpwd_submit);
	}

	@Override
	public void initListener() {
		ib_forgetpwd_close.setOnClickListener(this);
		tv_forget_getcode.setOnClickListener(this);
		btn_forgetpwd_submit.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		// TODO Auto-generated method stub

	}

}
