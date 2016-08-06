package com.ccc.www.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 消息详情
 * 
 * @author Administrator
 * 
 */
public class MessageCenterDetailActivity extends BaseActivity {

	ImageButton ib_digital_goback;
	TextView act_messagecenterdetail_title;
	TextView act_messagecenterdetail_content;

	int msg_id = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		msg_id = getIntent().getIntExtra("msg_id", 0);

		setContentView(R.layout.act_messagecenterdetail);
		initview();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_digital_goback:
			MessageCenterDetailActivity.this.finish();
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
		ib_digital_goback = (ImageButton) findViewById(R.id.ib_digital_goback);

		act_messagecenterdetail_title = (TextView) findViewById(R.id.act_messagecenterdetail_title);
		act_messagecenterdetail_content = (TextView) findViewById(R.id.act_messagecenterdetail_content);
	}

	@Override
	public void initListener() {
		ib_digital_goback.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		loadmsg();
	}

	private void loadmsg() {
		// TODO Auto-generated method stub
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("msg_id", msg_id + "");

		loadData(HttpMethod.POST, GlobalConstant.Jpush_GET_MY_MSG_DETAIL,
				params, new RequestCallBack<String>() {
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
								JSONObject json = new JSONObject(returnstr);

								String jpshu_title = json
										.getString("jpshu_title");
								String jpush_context = json
										.getString("jpush_context");

								act_messagecenterdetail_title
										.setText(jpshu_title);
								act_messagecenterdetail_title
										.setText(jpush_context);

								Intent intent = new Intent();
								intent.putExtra("msg_id", msg_id);
								intent.setAction(GlobalConstant.MESSAGEREAD);
								sendBroadcast(intent);

								setIsRead();

							} catch (JSONException e) {
								e.printStackTrace();
								showToast("加载失败");
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

	private void setIsRead() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("msg_id", msg_id + "");
		loadData(HttpMethod.POST, GlobalConstant.Jpush_Set_Msg_Read, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub

						String returnstr = response.result;

						Log.v(TAG, "returnstr " + returnstr);

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
					}
				});

	}

}
