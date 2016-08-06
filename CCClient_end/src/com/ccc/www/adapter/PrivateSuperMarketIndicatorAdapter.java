package com.ccc.www.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ccc.www.bean.SupMarketCategoryBean;
import com.ccc.www.fragment.PrivateSuperMarketFragment;

public class PrivateSuperMarketIndicatorAdapter extends FragmentPagerAdapter {

	private List<SupMarketCategoryBean> cates;
	private int proxyshopid = 0;

	public PrivateSuperMarketIndicatorAdapter(FragmentManager fm,
			List<SupMarketCategoryBean> cates, int proxyshopid) {
		super(fm);
		this.cates = cates;
		this.proxyshopid = proxyshopid;
	}

	@Override
	public Fragment getItem(int position) {
		PrivateSuperMarketFragment fragment = new PrivateSuperMarketFragment(
				position, cates, proxyshopid);
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
