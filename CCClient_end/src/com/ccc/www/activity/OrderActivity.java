package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.ViewPageAdapter;
import com.ccc.www.fragment.Order_Fragment1;
import com.ccc.www.fragment.Order_Fragment2;

public class OrderActivity extends BaseActivity {

	ImageButton ib_digital_goback;
	TextView viewpage_1;
	TextView viewpage_2;
	ImageView cursor;
	ViewPager order_viewpage;

	int screenW;
	int currIndex = 0;

	String orderNo = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		orderNo = getIntent().getStringExtra("orderNo");

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenW = dm.widthPixels;
		setContentView(R.layout.activity_order);
		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_digital_goback:
			OrderActivity.this.finish();
			break;
		case R.id.viewpage_1:
			order_viewpage.setCurrentItem(0);
			break;
		case R.id.viewpage_2:
			order_viewpage.setCurrentItem(1);
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
		viewpage_1 = (TextView) findViewById(R.id.viewpage_1);
		viewpage_2 = (TextView) findViewById(R.id.viewpage_2);
		cursor = (ImageView) findViewById(R.id.cursor);
		order_viewpage = (ViewPager) findViewById(R.id.order_viewpage);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		ib_digital_goback.setOnClickListener(this);
		viewpage_1.setOnClickListener(this);
		viewpage_2.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		List<Fragment> allfragment = new ArrayList<Fragment>();
		Fragment Order_Fragment1 = new Order_Fragment1();
		Fragment Order_Fragment2 = new Order_Fragment2();

		allfragment.add(Order_Fragment1);
		allfragment.add(Order_Fragment2);

		// 给ViewPager设置适配器
		order_viewpage.setAdapter(new ViewPageAdapter(
				getSupportFragmentManager(), allfragment));
		order_viewpage.setCurrentItem(0);// 设置当前显示标签页为第一页
		order_viewpage.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		private int one = screenW / 2;// 两个相邻页面的偏移量

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			Animation animation = new TranslateAnimation(currIndex * one,
					position * one, 0, 0);// 平移动画
			currIndex = position;
			animation.setFillAfter(true);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
			animation.setDuration(200);// 动画持续时间0.2秒
			cursor.startAnimation(animation);// 是用ImageView来显示动画的
			// int i = currIndex + 1;

			switch (position) {
			case 0:
				viewpage_1.setTextColor(getResources().getColor(
						R.color.topbar_bg));
				viewpage_2.setTextColor(getResources().getColor(R.color.black));
				break;
			case 1:
				viewpage_1.setTextColor(getResources().getColor(R.color.black));
				viewpage_2.setTextColor(getResources().getColor(
						R.color.topbar_bg));
				break;
			default:
				break;
			}
		}
	}

}
