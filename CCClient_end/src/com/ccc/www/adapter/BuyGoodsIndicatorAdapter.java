package com.ccc.www.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ccc.www.bean.ShopCategoryBean;
import com.ccc.www.fragment.BuyGoodsFragment;

public class BuyGoodsIndicatorAdapter extends FragmentPagerAdapter {

	private ArrayList<ShopCategoryBean> cates;

	public BuyGoodsIndicatorAdapter(FragmentManager fm,
			ArrayList<ShopCategoryBean> cates) {
		super(fm);
		this.cates = cates;
	}

	@Override
	public Fragment getItem(int position) {
		BuyGoodsFragment fragment = new BuyGoodsFragment(cates, position);
		return fragment;
	}

	@Override
	public int getCount() {
		return cates.size();
	}

	public CharSequence getPageTitle(int position) {
		return cates.get(position).getShop_category();
	}
}
