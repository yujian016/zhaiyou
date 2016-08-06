package com.ccc.www.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ccc.www.bean.SupMarketCategoryBean;
import com.ccc.www.fragment.MySuperMarketFragment;

public class MySuperMarketIndicatorAdapter extends FragmentPagerAdapter {

	private List<SupMarketCategoryBean> cates;

	public MySuperMarketIndicatorAdapter(FragmentManager fm,
			List<SupMarketCategoryBean> cates) {
		super(fm);
		this.cates = cates;
	}

	@Override
	public Fragment getItem(int position) {
		MySuperMarketFragment fragment = new MySuperMarketFragment(position, cates);
		return fragment;
	}

	@Override
	public int getCount() {
		return cates.size();
	}

	public CharSequence getPageTitle(int position) {
		return cates.get(position).getSock_category_name();
	}
}
