package com.ccc.www.adapter;

import java.util.ArrayList;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.ShopCategoryBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SchoolBuyCategoryAdapter extends BaseAdapter {

	ArrayList<ShopCategoryBean> shopCategorys;

	Context context;

	public SchoolBuyCategoryAdapter(Context context,
			ArrayList<ShopCategoryBean> shopCategorys) {
		this.context = context;
		this.shopCategorys = shopCategorys;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return shopCategorys.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return shopCategorys.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holer = null;
		if (view == null) {
			holer = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.item_schoolbuycategory, null);
			holer.item_schoolbuycategory_name = (TextView) view
					.findViewById(R.id.item_schoolbuycategory_name);
			view.setTag(holer);
		} else {
			holer = (ViewHolder) view.getTag();
		}

		holer.item_schoolbuycategory_name.setText(shopCategorys.get(position)
				.getShop_category());

		return view;
	}

	class ViewHolder {
		TextView item_schoolbuycategory_name;
	}

}
