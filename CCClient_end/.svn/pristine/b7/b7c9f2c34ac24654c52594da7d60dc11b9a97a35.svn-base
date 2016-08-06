package com.ccc.www.adapter;

import java.io.Serializable;
import java.util.ArrayList;

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
import com.ccc.www.activity.MyShopGoodsDetailActivity;
import com.ccc.www.bean.GoodsBean;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EntityStoreGoodsAdapter extends BaseAdapter {

	private ArrayList<GoodsBean> lists = new ArrayList<GoodsBean>();
	private LayoutInflater inflater;
	private Context context = null;

	public EntityStoreGoodsAdapter(Context context, ArrayList<GoodsBean> lists) {
		this.lists = lists;
		this.context = context;
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
			view = inflater.inflate(R.layout.entity_store_goods_gv_item, null);
			holder.iv_entity_log = (ImageView) view
					.findViewById(R.id.iv_entity_log);
			holder.tv_entity_title = (TextView) view
					.findViewById(R.id.tv_entity_title);
			holder.tv_entity_price = (TextView) view
					.findViewById(R.id.tv_entity_price);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		final GoodsBean s = lists.get(index);

		String log = s.getGoods_log1();
		if (!log.startsWith("http")) {
			log = GlobalConstant.SERVER_URL + log;
		}
		ImageLoader.getInstance().displayImage(log, holder.iv_entity_log,
				ImageLoaderOption.getoption());
		holder.tv_entity_title.setText(s.getGoods_name());
		holder.tv_entity_price.setText("￥" + s.getGoods_price() + "元");

		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("id", s.getId());
				Bundle bundle = new Bundle();
				bundle.putSerializable("goodsBean", (Serializable) s);
				intent.putExtras(bundle);
				intent.setClass(context, MyShopGoodsDetailActivity.class);
				context.startActivity(intent);
			}
		});

		return view;
	}

	class ViewHolder {
		ImageView iv_entity_log;
		TextView tv_entity_price;
		TextView tv_entity_title;
	}

}
