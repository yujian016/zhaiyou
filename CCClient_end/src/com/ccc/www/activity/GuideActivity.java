package com.ccc.www.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.ccc.ccclient_end.R;
import com.ccc.www.navigation_activity.NewMainActivity;

public class GuideActivity extends BaseActivity {

	private ViewPager vpGuide;
	private static final int[] guideImages = new int[] { R.drawable.guide_1,
			R.drawable.guide_2, R.drawable.guide_3 };
	private ArrayList<ImageView> imageList = new ArrayList<ImageView>();
	private LinearLayout guide_dot_layout;
	private Button btnGoMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initview();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_go_main:
			Intent intent = new Intent(GuideActivity.this,
					NewMainActivity.class);
			GuideActivity.this.startActivity(intent);
			this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void findviewWithId() {
		setContentView(R.layout.activity_guide);
		vpGuide = (ViewPager) findViewById(R.id.vp_guide);
		guide_dot_layout = (LinearLayout) findViewById(R.id.guide_dot_layout);
		btnGoMain = (Button) findViewById(R.id.btn_go_main);

	}

	@Override
	public void initListener() {
		btnGoMain.setOnClickListener(this);

		vpGuide.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				updateDot();
				if (position == guideImages.length - 1) {
					btnGoMain.setVisibility(View.VISIBLE);
				} else {
					btnGoMain.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	@Override
	public void initdata() {
		for (int i = 0; i < guideImages.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setBackgroundResource(guideImages[i]);
			imageList.add(iv);
		}
		vpGuide.setAdapter(new GuidePagerAdapter());
		initDot();
		updateDot();
	}

	public void initDot() {
		for (int i = 0; i < imageList.size(); i++) {
			View view = new View(this);
			LayoutParams params = new LayoutParams(10, 10);
			params.leftMargin = 10;
			view.setLayoutParams(params);
			view.setBackgroundResource(R.drawable.selector_dot);
			guide_dot_layout.addView(view);
		}
	}

	public void updateDot() {
		int currentIndex = vpGuide.getCurrentItem();
		for (int i = 0; i < guide_dot_layout.getChildCount(); i++) {
			if (currentIndex == i) {
				guide_dot_layout.getChildAt(i).setEnabled(true);
			} else {
				guide_dot_layout.getChildAt(i).setEnabled(false);
			}
		}
	}

	class GuidePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(imageList.get(position));
			return imageList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}
}
