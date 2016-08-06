package com.ccc.www.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ccc.www.bean.SupMarketCategoryBean;
import com.ccc.www.fragment.SockFragment;

public class SockIndicatorAdapter extends FragmentPagerAdapter {

	private ArrayList<SupMarketCategoryBean> sockList = new ArrayList<SupMarketCategoryBean>();

	public SockIndicatorAdapter(FragmentManager fm,
			ArrayList<SupMarketCategoryBean> sockList) {
		super(fm);
		this.sockList = sockList;
	}

	@Override
	public Fragment getItem(int position) {
		SockFragment fragment = new SockFragment(position, sockList);
		return fragment;
	}

	@Override
	public int getCount() {
		return sockList.size();
	}

	public CharSequence getPageTitle(int position) {
		return sockList.get(position).getSock_category_name();
	}
}
