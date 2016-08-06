package com.ccc.www.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.activity.EntityStoreActivity;
import com.ccc.www.bean.ShopBean;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShopAdapter extends BaseAdapter {

	private ArrayList<ShopBean> lists = new ArrayList<ShopBean>();
	private LayoutInflater inflater;
	private Context context;

	public ShopAdapter(Context context, ArrayList<ShopBean> lists) {
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
			view = inflater.inflate(R.layout.shop_lv_item, null);
			holder.iv_shop_logo = (ImageView) view
					.findViewById(R.id.iv_shop_logo);
			holder.tv_shop_title = (TextView) view
					.findViewById(R.id.tv_shop_title);
			holder.tv_shop_category = (TextView) view
					.findViewById(R.id.tv_shop_category);
			holder.tv_shop_comment = (TextView) view
					.findViewById(R.id.tv_shop_comment);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		final ShopBean s = lists.get(index);

		ImageLoader.getInstance().displayImage(
				GlobalConstant.SERVER_URL + s.getShop_log(),
				holder.iv_shop_logo, ImageLoaderOption.getoption());

		holder.tv_shop_title.setText(s.getShop_name());
		holder.tv_shop_category.setText(s.getCategory_name());

		int Good_comment_num = s.getGood_comment_num();
		int Bad_comment_num = s.getBad_comment_num();

		if (Good_comment_num > 0 || Bad_comment_num > 0) {
			float goodcom = (Good_comment_num * 1.00f)
					/ (Bad_comment_num * 1.00f + Good_comment_num * 1.00f)
					* 100;
			float badcom = (Bad_comment_num * 1.00f)
					/ (Bad_comment_num * 1.00f + Good_comment_num * 1.00f)
					* 100;
			holder.tv_shop_comment.setText("好评率：" + (int) goodcom + "%");
		} else {
			holder.tv_shop_comment.setText("好评率：" + (int) 100 + "%");
		}
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent storeIntent = new Intent(context,
						EntityStoreActivity.class);
				storeIntent.putExtra("shopid", s.getId());
				context.startActivity(storeIntent);
			}
		});
		return view;
	}

	class ViewHolder {
		ImageView iv_shop_logo;
		TextView tv_shop_title;
		TextView tv_shop_category;
		TextView tv_shop_comment;
	}
}
