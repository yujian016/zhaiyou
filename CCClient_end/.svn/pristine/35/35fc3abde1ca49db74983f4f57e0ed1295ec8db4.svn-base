package com.ccc.www.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.umeng.socialize.utils.Log;

public class SuggestionFeedbackActivity extends BaseActivity {

	String TAG="SuggestionFeedbackActivity";
	
	
	ImageButton ib_digital_goback;
	TextView tv_digital_header_submit;
	EditText activity_suggestfeedback_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_suggestfeedback);
		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_digital_goback:
			SuggestionFeedbackActivity.this.finish();
			break;
		case R.id.tv_digital_header_submit:
			String content = activity_suggestfeedback_content.getText()
					.toString().trim();
			if (TextUtils.isEmpty(content)) {
				showToast("请输入您的意见或建议");
				activity_suggestfeedback_content.requestFocus();
				return;
			}
			subMitData();
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
		tv_digital_header_submit = (TextView) findViewById(R.id.tv_digital_header_submit);
		activity_suggestfeedback_content = (EditText) findViewById(R.id.activity_suggestfeedback_content);
	}

	@Override
	public void initListener() {
		ib_digital_goback.setOnClickListener(this);
		tv_digital_header_submit.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		// TODO Auto-generated method stub

	}
	
	
	//提交操作
	private void subMitData(){
		
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在获取账户余额");
		
		
		RequestParams params=new RequestParams();
		params.addBodyParameter("user_id",String.valueOf(UserUtil.getuserid(this)));
		params.addBodyParameter("idea_context",activity_suggestfeedback_content.getText().toString().trim());
		loadData(HttpMethod.POST, GlobalConstant.ADD_IDEA, params, new RequestCallBack<String>() {
			
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				dismissProgressDialog();
				String result=arg0.result;
				Log.v(TAG,result);
				
				try {
					JSONObject json = new JSONObject(result);
					int code = json.getInt("code");
					String msg=json.getString("msg");
					if(code>0){
						AlertDialog.Builder build = new Builder(SuggestionFeedbackActivity.this);
						build.setTitle("提示");
						build.setMessage(msg);
						build.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								SuggestionFeedbackActivity.this.finish();
							}
						}).show();
					}else{
						showToast(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				dismissProgressDialog();
				Log.v(TAG, "onFailure " + arg1);

				showToast("获取余额失败，请稍后重试");
			}
		});
	}

}
