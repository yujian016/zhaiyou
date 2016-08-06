package com.ccc.www.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.activity.ShopGoodsDetailActivity;
import com.ccc.www.bean.ShopGoodBean;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShopGoodAdapter extends BaseAdapter {

	private List<ShopGoodBean> lists = new ArrayList<ShopGoodBean>();
	private LayoutInflater inflater;
	Context context;

	public ShopGoodAdapter(Context context, List<ShopGoodBean> lists) {
		super();
		this.context = context;
		this.lists = lists;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int itemId) {
		return itemId;
	}

	@Override
	public View getView(int index, View view, ViewGroup vgroup) {

		ViewHolder holder = null;

		if (view == null) {
			holder = new ViewHolder();

			view = inflater.inflate(R.layout.item_shopgoods, null);

			holder.item_shopgoods_name = (TextView) view
					.findViewById(R.id.item_shopgoods_name);
			holder.item_shopgoods_img = (ImageView) view
					.findViewById(R.id.item_shopgoods_img);
			holder.item_shopgoods_price = (TextView) view
					.findViewById(R.id.item_shopgoods_price);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		final ShopGoodBean s = lists.get(index);

		holder.item_shopgoods_name.setText(s.getGoods_name());
		ImageLoader.getInstance().displayImage(
				GlobalConstant.SERVER_URL + s.getGoods_log1(),
				holder.item_shopgoods_img,
				ImageLoaderOption.getoption());

		// holder.item_shopgoods_num.setText("库存:" + s.getGoods_num());
		holder.item_shopgoods_price.setText("￥" + s.getGoods_price());

//		holder.item_shopgoods_buy.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//
//				int userid = UserUtil.getuserid(context);
//
//				boolean exist = DBUtil.CheckShopCartGoodsExist(context,
//						s.getId(), userid);
//				if (exist) {
//					Toast.makeText(context, "该商品已经在购物车了", Toast.LENGTH_SHORT)
//							.show();
//				} else {
//					DBUtil.insertShopCart(context, s, userid);
//					Toast.makeText(context, "已添加到购物车", Toast.LENGTH_SHORT)
//							.show();
//
//					Intent intent = new Intent();
//					intent.setAction(GlobalConstant.UpdateShopCart);
//					context.sendBroadcast(intent);
//				}
//			}
//		});

		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				int id = s.getId();

				Intent intent = new Intent();

				Bundle bundle = new Bundle();
				bundle.putSerializable("shopgoodbean", (Serializable) s);
				intent.putExtras(bundle);

				intent.putExtra("from", ShopGoodsDetailActivity.FromShop);
				intent.putExtra("id", id);
				intent.setClass(context, ShopGoodsDetailActivity.class);
				context.startActivity(intent);

			}
		});

		return view;
	}

	class ViewHolder {
		TextView item_shopgoods_name;
		ImageView item_shopgoods_img;
		// TextView item_shopgoods_num;
		TextView item_shopgoods_price;
		// Button item_shopgoods_buy;
	}

}
