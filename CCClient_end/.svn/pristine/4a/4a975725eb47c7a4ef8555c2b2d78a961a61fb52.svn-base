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
import com.ccc.www.bean.GoodsBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.utils.Log;

public class GoodsAdapter extends BaseAdapter {

	String TAG = "GoodsAdapter";

	private ArrayList<GoodsBean> lists = new ArrayList<GoodsBean>();
	private int resource;
	private LayoutInflater inflater;
	private Context context;

	public GoodsAdapter(Context context, ArrayList<GoodsBean> lists,
			int resource) {
		super();
		this.context = context;
		this.lists = lists;
		this.resource = resource;
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
			view = inflater.inflate(resource, null);

			holder.iv_goods_logo = (ImageView) view
					.findViewById(R.id.iv_goods_logo);
			holder.tv_goods_name = (TextView) view
					.findViewById(R.id.tv_goods_name);
			holder.tv_goods_detail = (TextView) view
					.findViewById(R.id.tv_goods_detail);
			holder.tv_goods_price = (TextView) view
					.findViewById(R.id.tv_goods_price);
			holder.tv_goods_sailnumber = (TextView) view
					.findViewById(R.id.tv_goods_sailnumber);

			holder.tv_goods_count = (TextView) view
					.findViewById(R.id.tv_goods_count);

			holder.iv_goods_minus = (ImageView) view
					.findViewById(R.id.iv_goods_minus);

			holder.iv_goods_plus = (ImageView) view
					.findViewById(R.id.iv_goods_plus);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}
		final GoodsBean goods = lists.get(index);

		ImageLoader.getInstance().displayImage(
				GlobalConstant.SERVER_URL + goods.getGoods_log1(),
				holder.iv_goods_logo, ImageLoaderOption.getoption());

		holder.tv_goods_name.setText(goods.getGoods_name());
		holder.tv_goods_detail.setText(goods.getGoods_detail());
		holder.tv_goods_price.setText("￥" + goods.getGoods_price() + "元");
		holder.tv_goods_sailnumber.setText("库存:" + goods.getGoods_num());

		holder.iv_goods_minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int count = goods.getCount();
				if (count > 0) {
					count--;
				}
				goods.setCount(count);
				notifyDataSetChanged();

				// 更新到数据库
				update_db(goods);
			}
		});

		holder.iv_goods_plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int count = goods.getCount();
				if (count < goods.getGoods_num()) {
					count++;
					goods.setCount(count);

					notifyDataSetChanged();

					// 更新到数据库
					update_db(goods);
				}
			}
		});

		return view;
	}

	class ViewHolder {
		ImageView iv_goods_logo;
		TextView tv_goods_name;
		TextView tv_goods_detail;
		TextView tv_goods_price;
		TextView tv_goods_sailnumber;
		TextView tv_goods_count;
		ImageView iv_goods_minus;
		ImageView iv_goods_plus;
	}

	private void update_db(GoodsBean goods) {
		int userid = UserUtil.getuserid(context);
		// 判断这个sock是否已经存在数据库
		boolean exist = DBUtil.CheckSchoolBuyCartGoodsExist(context,
				goods.getId());
		if (exist) {

			Log.v(TAG,
					"  存在     id " + goods.getId() + "  count  "
							+ goods.getCount());

			// 存在，更新操作
			DBUtil.updateSchoolBuyCartCount(context, goods);
		} else {
			// 不存在，新增

			Log.v(TAG, "  不存在，新增       id " + goods.getId() + "  count  "
					+ goods.getCount());

			DBUtil.insertSchoolBuyCart(context, goods);

		}
		// 发广播修改 界面数量变化
		Intent updateProxyStockCartCount = new Intent();
		updateProxyStockCartCount
				.setAction(GlobalConstant.UpdateSchoolBuyCartCount);
		context.sendBroadcast(updateProxyStockCartCount);

	}

}
