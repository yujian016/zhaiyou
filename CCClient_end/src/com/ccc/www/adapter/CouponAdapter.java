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
import com.ccc.www.bean.CouponBean;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CouponAdapter extends BaseAdapter {

	String TAG = "CouponAdapter";

	private List<CouponBean> lists = new ArrayList<CouponBean>();
	private LayoutInflater inflater;
	private Context context;

	public CouponAdapter(Context context, List<CouponBean> lists) {
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
			view = inflater.inflate(R.layout.item_coupon, null);

			holder.item_coupon_img = (ImageView) view
					.findViewById(R.id.item_coupon_img);
			holder.item_coupon_price = (TextView) view
					.findViewById(R.id.item_coupon_price);
			holder.item_coupon_detail = (TextView) view
					.findViewById(R.id.item_coupon_detail);
			holder.item_coupon_get = (TextView) view
					.findViewById(R.id.item_coupon_get);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		final CouponBean goods = lists.get(index);

		ImageLoader.getInstance().displayImage(goods.getCoupon_log(),
				holder.item_coupon_img,
				ImageLoaderOption.getoption());

		holder.item_coupon_price.setText("面额：" + goods.getCoupon_price() + "元");
		holder.item_coupon_detail.setText("说明：" + goods.getDetail());

		holder.item_coupon_get.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 领取优惠
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("bean", (Serializable) goods);
				intent.putExtras(bundle);
				intent.setAction(GlobalConstant.GetMyCoupon);
				context.sendBroadcast(intent);
			}
		});
		return view;
	}

	class ViewHolder {
		ImageView item_coupon_img;
		TextView item_coupon_price;
		TextView item_coupon_detail;
		TextView item_coupon_get;
	}

}
