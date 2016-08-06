package com.ccc.www.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.MsgIndicatorAdapter;
import com.ccc.www.fragment.IsReaderMsgFragment;
import com.ccc.www.fragment.NotReaderMsgFragment;
import com.viewpagerindicator.TabPageIndicator;

public class MsgActivity extends BaseActivity{

	private ViewPager vpMsg;
	private TabPageIndicator msgIndicator;
	private ArrayList<Fragment> allMsgFragments;
	private ImageButton goBackBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg);
		initview();
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_msg_goback:
			MsgActivity.this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void findviewWithId() {
		vpMsg = (ViewPager)findViewById(R.id.vp_msg);
		msgIndicator = (TabPageIndicator)findViewById(R.id.msg_indicator);
		goBackBtn=(ImageButton) findViewById(R.id.ib_msg_goback);
	}

	@Override
	public void initListener() {
		goBackBtn.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		allMsgFragments = new ArrayList<Fragment>();
		allMsgFragments.add(new IsReaderMsgFragment());
		allMsgFragments.add(new NotReaderMsgFragment());
		vpMsg.setAdapter(new MsgIndicatorAdapter(getSupportFragmentManager(),allMsgFragments));
		msgIndicator.setViewPager(vpMsg);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

}
