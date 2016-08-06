package com.ccc.www.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.util.UserUtil;

/**
 * 关于
 * 
 * @author Administrator
 * 
 */
public class AboutActivity extends BaseActivity {

	ImageButton ib_digital_goback;
	TextView act_about_version;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_about);

		initview();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_digital_goback:
			AboutActivity.this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void findviewWithId() {
		ib_digital_goback = (ImageButton) findViewById(R.id.ib_digital_goback);
		act_about_version = (TextView) findViewById(R.id.act_about_version);
	}

	@Override
	public void initListener() {
		ib_digital_goback.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		try {
			String VersionName = UserUtil.getVersionName(this).toUpperCase();
			if (VersionName.contains("V") || VersionName.contains("v")) {
				act_about_version.setText("版本信息:" + VersionName);
			} else {
				act_about_version.setText("版本信息:V" + VersionName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}
}
