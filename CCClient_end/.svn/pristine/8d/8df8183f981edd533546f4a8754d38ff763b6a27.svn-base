package com.ccc.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;

import com.ccc.ccclient_end.R;
import com.ccc.www.navigation_activity.NewMainActivity;
import com.ccc.www.util.UserUtil;

public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				boolean firstrun = UserUtil.getfirstrun(SplashActivity.this);
				if (firstrun) {
					Intent intent = new Intent(SplashActivity.this,
							GuideActivity.class);
					SplashActivity.this.startActivity(intent);
				} else {
					Intent intent = new Intent(SplashActivity.this,
							NewMainActivity.class);
					SplashActivity.this.startActivity(intent);
				}
				SplashActivity.this.finish();
			}
		}, 2000);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void findviewWithId() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
	}

	@Override
	public void initdata() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

}
