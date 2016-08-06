package com.ccc.www.adapter;

import java.util.ArrayList;

import com.ccc.www.bean.DigitalCategoryBean;
import com.ccc.www.fragment.DigitalFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class DigitalIndicatorAdapter extends FragmentPagerAdapter {

	private ArrayList<DigitalCategoryBean> cates;
	public DigitalIndicatorAdapter(FragmentManager fm,ArrayList<DigitalCategoryBean> cates) {
		super(fm);
		this.cates=cates;
	}

	@Override
	public Fragment getItem(int position) {
		DigitalFragment fragment=new DigitalFragment(cates,position);
		return fragment;
	}

	@Override
	public int getCount() {
		return cates.size();
	}
	
	public CharSequence getPageTitle(int position) {
		return cates.get(position).getDigital_goods_category_name();
    }
}
