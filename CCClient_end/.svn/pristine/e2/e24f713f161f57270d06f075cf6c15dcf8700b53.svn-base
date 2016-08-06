package com.ccc.www.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.ccc.ccclient_end.R;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.CheckUtil;
import com.ccc.www.util.GlobalConstant;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ApplyProxyActivity extends BaseActivity {

	String TAG = "ApplyProxyActivity";

	private HttpUtils http;

	private EditText et_input_stu_class_proxy;

	private EditText et_input_stucard_no_proxy;

	private EditText et_input_idcard_no_proxy;

	private EditText et_input_stu_class_shopname;

	private Button btn_apply_proxy;

	private ImageButton ib_apply_proxy_close;

	RadioButton send_status_ssc;
	RadioButton send_status_sdlx;
	EditText start_send_money;

	private int loginId;

	private String hostel_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_proxy);
		initview();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void findviewWithId() {
		ib_apply_proxy_close = (ImageButton) findViewById(R.id.ib_apply_proxy_close);
		et_input_idcard_no_proxy = (EditText) findViewById(R.id.et_input_idcard_no_proxy);
		et_input_stucard_no_proxy = (EditText) findViewById(R.id.et_input_stucard_no_proxy);
		et_input_stu_class_proxy = (EditText) findViewById(R.id.et_input_stu_class_proxy);
		et_input_stu_class_shopname = (EditText) findViewById(R.id.et_input_stu_class_shopname);
		btn_apply_proxy = (Button) findViewById(R.id.btn_apply_proxy);

		send_status_ssc = (RadioButton) findViewById(R.id.send_status_ssc);
		send_status_sdlx = (RadioButton) findViewById(R.id.send_status_sdlx);
		start_send_money = (EditText) findViewById(R.id.start_send_money);
	}

	@Override
	public void initListener() {
		btn_apply_proxy.setOnClickListener(this);
		ib_apply_proxy_close.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_apply_proxy_close:
			ApplyProxyActivity.this.finish();
			break;
		case R.id.btn_apply_proxy:
			if (isEmpty()) {
				httpUtilsSend();
			}
			break;
		default:
			break;
		}
	}

	private void httpUtilsSend() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查网络");
			return;
		}

		showLoading2("正在提交申请");

		http = new HttpUtils();
		RequestParams params = new RequestParams();
		String idcard_no = et_input_idcard_no_proxy.getText().toString().trim();
		String stucard_no = et_input_stucard_no_proxy.getText().toString()
				.trim();
		String stu_class = et_input_stu_class_proxy.getText().toString().trim();

		String proxy_shop_name = et_input_stu_class_shopname.getText()
				.toString().trim();

		String start_send_money_Str = start_send_money.getText().toString()
				.trim();

		String send_goods_status = "送上床";
		if (send_status_ssc.isChecked()) {
			send_goods_status = "送上床";
		} else {
			send_goods_status = "送到楼下";
		}

		params.addBodyParameter("user_id", loginId + "");
		params.addBodyParameter("hostel_id", hostel_id);
		params.addBodyParameter("idcard_no", idcard_no);
		params.addBodyParameter("stucard_no", stucard_no);
		params.addBodyParameter("stu_class", stu_class);
		params.addBodyParameter("proxy_shop_name", proxy_shop_name);
		params.addBodyParameter("send_goods_status", send_goods_status);
		params.addBodyParameter("start_send_money", start_send_money_Str);
		
		Log.v(TAG, "send_goods_status  "  + send_goods_status);
		Log.v(TAG, "start_send_money  "  + start_send_money_Str);

		http.send(HttpMethod.POST, GlobalConstant.APPLY_SUPMARKET_PROXY,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException e, String msg) {

						Log.v(TAG, "onFailure  " + msg);

						showToast("提交失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String returnResult = info.result;

						Log.v(TAG, "returnResult" + returnResult);

						try {
							JSONObject object = new JSONObject(returnResult);
							String code = object.getString("resultCode");
							String msg = object.getString("resultMsg");
							if (Integer.parseInt(code) > 0) {
								Dialog alertDialog = new AlertDialog.Builder(
										ApplyProxyActivity.this, 1)
										.setTitle("提示")
										.setMessage(msg)
										.setPositiveButton(
												"确定",
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														// 跳转
														Intent intent = new Intent(
																ApplyProxyActivity.this,
																MyApplyProxyActivity.class);
														intent.putExtra(
																"supmaket_id",
																hostel_id);
														startActivity(intent);
														finish();
													}
												}).show();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	private boolean isEmpty() {
		String idcard_no = et_input_idcard_no_proxy.getText().toString().trim();
		String stucard_no = et_input_stucard_no_proxy.getText().toString()
				.trim();
		String stu_class = et_input_stu_class_proxy.getText().toString().trim();

		String shopname = et_input_stu_class_shopname.getText().toString()
				.trim();
		String start_send_moneyStr = start_send_money.getText().toString()
				.trim();

		boolean isOk = false;
		if (idcard_no.length() < 15 || idcard_no.length() > 18) {
			this.showToast("身份证号码不正确");
			et_input_idcard_no_proxy.requestFocus();
		} else if (stucard_no.length() == 0) {
			this.showToast("请输入学生证号码");
			et_input_stucard_no_proxy.requestFocus();
		} else if (stu_class.length() == 0) {
			this.showToast("请输入手机号码");
			et_input_stu_class_proxy.requestFocus();
		} else if (shopname.length() == 0) {
			this.showToast("请输入店铺名称");
			et_input_stu_class_shopname.requestFocus();
		} else if (start_send_moneyStr.length() == 0) {
			this.showToast("请输入起送金额");
			start_send_money.requestFocus();
		} else if (!CheckUtil.isInt(start_send_moneyStr)) {
			this.showToast("起送金额请输入整数");
			start_send_money.requestFocus();
		} else {
			isOk = true;
		}
		return isOk;
	}

	@Override
	public void initdata() {
		Intent intent = ApplyProxyActivity.this.getIntent();
		loginId = intent.getIntExtra("user_id", 0);
		hostel_id = String.valueOf(intent.getIntExtra("hostel_id", 0));
	}
}
