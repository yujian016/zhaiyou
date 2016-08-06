package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.AdapterView.OnItemClickListener;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.SelectDormitoryAreaAdapter;
import com.ccc.www.bean.DormitoryArea;
import com.ccc.www.bean.DormitoryBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.CheckUtil;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.utils.Log;

/**
 * 我的超市设置
 * 
 * @author Administrator
 * 
 */
public class MySuperMarketSettingActivity extends BaseActivity {

	String TAG = "MySuperMarketSettingActivity";

	ImageButton ib_apply_proxy_close;
	RadioButton send_status_ssc;
	RadioButton send_status_sdlx;
	EditText start_send_money;
	RadioButton yy;
	RadioButton xx;
	Button activity_privatesupermarketsetting_savesetting;

	int user_id = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		user_id = UserUtil.getuserid(this);

		setContentView(R.layout.activity_privatesupermarketsetting);

		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_apply_proxy_close:
			MySuperMarketSettingActivity.this.finish();
			break;
		case R.id.activity_privatesupermarketsetting_savesetting:
			if (!BaseUtils.isNetWork(getApplicationContext())) {
				showToast("请检查您的网络");
				return;
			}
			String send_goods_status = "送上床";
			if (send_status_ssc.isChecked()) {
				send_goods_status = "送上床";
			} else {
				send_goods_status = "送到楼下";
			}

			String start_send_money_Str = start_send_money.getText().toString()
					.trim();

			if (TextUtils.isEmpty(start_send_money_Str)) {
				showToast("请输入起送金额");
				start_send_money.requestFocus();
				return;
			} else {
				boolean check = CheckUtil.isInt(start_send_money_Str);
				if (!check) {
					showToast("请输入整数");
					start_send_money.requestFocus();
					return;
				}
			}

			int open_status = 0;
			if (yy.isChecked()) {
				open_status = 0;
			} else {
				open_status = 1;
			}

			showLoading2("正在保存设置");

			RequestParams params = new RequestParams();
			params.addBodyParameter("user_id", user_id + "");
			params.addBodyParameter("send_goods_status", send_goods_status + "");
			params.addBodyParameter("start_send_money", start_send_money_Str
					+ "");
			params.addBodyParameter("open_status", open_status + "");

			loadData(HttpMethod.POST, GlobalConstant.EDIT_SM_STATUS, params,
					new RequestCallBack<String>() {
						@Override
						public void onSuccess(ResponseInfo<String> response) {
							// TODO Auto-generated method stub
							dismissProgressDialog();

							String returnstr = response.result;

							Log.v(TAG, "returnstr  " + returnstr);
							if (TextUtils.isEmpty(returnstr)) {
								showToast("保存设置失败");
							} else {
								try {
									JSONObject json = new JSONObject(returnstr);
									String code = json.getString("code");
									String msg = json.getString("msg");
									showToast(msg);
									if (code.equalsIgnoreCase("1")) {
										MySuperMarketSettingActivity.this
												.finish();
									}

								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dismissProgressDialog();
							showToast("保存设置失败");
						}
					});

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
		ib_apply_proxy_close = (ImageButton) findViewById(R.id.ib_apply_proxy_close);
		send_status_ssc = (RadioButton) findViewById(R.id.send_status_ssc);
		send_status_sdlx = (RadioButton) findViewById(R.id.send_status_sdlx);
		start_send_money = (EditText) findViewById(R.id.start_send_money);
		yy = (RadioButton) findViewById(R.id.yy);
		xx = (RadioButton) findViewById(R.id.xx);
		activity_privatesupermarketsetting_savesetting = (Button) findViewById(R.id.activity_privatesupermarketsetting_savesetting);
	}

	@Override
	public void initListener() {
		ib_apply_proxy_close.setOnClickListener(this);
		activity_privatesupermarketsetting_savesetting.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		// TODO Auto-generated method stub

		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", user_id + "");

		loadData(HttpMethod.POST, GlobalConstant.GET_MY_SM_INFO, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr  " + returnstr);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("加载失败");
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);
								String sup_market_name = json
										.getString("sup_market_name");
								String send_goods = json
										.getString("send_goods");
								int open_status = json.getInt("open_status");
								int start_send_moneyint = json
										.getInt("start_send_money");

								if (send_goods.equalsIgnoreCase("送上床")) {
									send_status_ssc.setChecked(true);
								} else {
									send_status_sdlx.setChecked(true);
								}

								start_send_money.setText(""
										+ start_send_moneyint);
								if (open_status == 0) {
									yy.setChecked(true);
								} else {
									xx.setChecked(true);
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}
				});

	}

}
