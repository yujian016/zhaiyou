package com.ccc.www.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ccc.ccclient_end.R;

public class FeedbackHelpActivity extends BaseActivity {

	ImageButton ib_digital_goback;

	LinearLayout to_callservicephone;
	LinearLayout to_suggestfeedback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_digital_goback:
			FeedbackHelpActivity.this.finish();
			break;
		case R.id.to_suggestfeedback:
			Intent suggestfeedback = new Intent();
			suggestfeedback.setClass(FeedbackHelpActivity.this,
					SuggestionFeedbackActivity.class);
			startActivity(suggestfeedback);
			break;
		case R.id.to_callservicephone:
			AlertDialog.Builder build = new Builder(FeedbackHelpActivity.this);
			build.setTitle("提示");
			build.setMessage("您要拨打服务热线0797-8286555吗?");
			build.setPositiveButton("是", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// intent启动拨打电话
					Intent intent = new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + "07978286555"));
					startActivity(intent);
				}
			});
			build.setNegativeButton("否", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub

				}
			});
			build.show();

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
		to_suggestfeedback = (LinearLayout) findViewById(R.id.to_suggestfeedback);
		to_callservicephone = (LinearLayout) findViewById(R.id.to_callservicephone);
	}

	@Override
	public void initListener() {
		ib_digital_goback.setOnClickListener(this);
		to_suggestfeedback.setOnClickListener(this);
		to_callservicephone.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		// TODO Auto-generated method stub

	}

}
