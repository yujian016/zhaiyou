package com.ccc.www.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MsgIndicatorAdapter extends FragmentPagerAdapter {

	List<Fragment> allfragment = new ArrayList<Fragment>();
	private static final String[] CONTENT = new String[] { "已读", "未读"};
	public MsgIndicatorAdapter(FragmentManager fm, List<Fragment> allfragment) {
		super(fm);
		this.allfragment = allfragment;
	}

	@Override
	public Fragment getItem(int position) {
		return allfragment.get(position);
	}

	@Override
	public int getCount() {
		return allfragment.size();
	}
	
	public CharSequence getPageTitle(int position) {
        return CONTENT[position % CONTENT.length].toUpperCase();
    }
}
