package com.ccc.www.adapter;

import java.util.ArrayList;
import java.util.List;

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
import com.ccc.www.bean.PrivateSuperMarketGoodsBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.utils.Log;

public class PrivateSuperMarketAdapter extends BaseAdapter {

	String TAG = "PrivateSuperMarketAdapter";

	private List<PrivateSuperMarketGoodsBean> lists = new ArrayList<PrivateSuperMarketGoodsBean>();
	private LayoutInflater inflater;
	private Context context;

	public PrivateSuperMarketAdapter(Context context,
			List<PrivateSuperMarketGoodsBean> lists) {
		super();
		this.context = context;
		this.lists = lists;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (lists != null && lists.size() > 0) {
			UserUtil.setsupermaket_id(context, lists.get(0).getSupermaket_id());
		}

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

			view = inflater.inflate(R.layout.item_privatesupermarket, null);

			holder.item_privatesupermarket_img = (ImageView) view
					.findViewById(R.id.item_privatesupermarket_img);
			holder.item_privatesupermarket_name = (TextView) view
					.findViewById(R.id.item_privatesupermarket_name);
			holder.item_privatesupermarket_num = (TextView) view
					.findViewById(R.id.item_privatesupermarket_num);
			holder.item_privatesupermarket_price = (TextView) view
					.findViewById(R.id.item_privatesupermarket_price);

			holder.tv_privatesupermarket_count = (TextView) view
					.findViewById(R.id.tv_privatesupermarket_count);
			holder.iv_privatesupermarket_minus = (ImageView) view
					.findViewById(R.id.iv_privatesupermarket_minus);
			holder.iv_privatesupermarket_plus = (ImageView) view
					.findViewById(R.id.iv_privatesupermarket_plus);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		final PrivateSuperMarketGoodsBean sock = lists.get(index);

		ImageLoader.getInstance().displayImage(sock.getGoods_log(),
				holder.item_privatesupermarket_img,
				ImageLoaderOption.getoption());

		holder.item_privatesupermarket_name.setText(sock.getGoods_name());
		holder.item_privatesupermarket_num.setText("库存:" + sock.getHave_num());

		holder.item_privatesupermarket_price.setText("￥"
				+ sock.getGoods_price() + "元");

		if(sock.getCount()==0){
			holder.iv_privatesupermarket_minus.setImageResource(R.drawable.btn_minus_gray);

		}else{
			holder.iv_privatesupermarket_minus.setImageResource(R.drawable.btn_minus_blue);
		}

		if(sock.getCount()==sock.getHave_num()){
			holder.iv_privatesupermarket_plus.setImageResource(R.drawable.btn_plus_gray);
		}else{
			holder.iv_privatesupermarket_plus.setImageResource(R.drawable.btn_plus_blue);
		}

		holder.tv_privatesupermarket_count.setText(sock.getCount() + "");

		holder.iv_privatesupermarket_minus
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						int count = sock.getCount();
						if (count > 0) {
							count--;
						}
						sock.setCount(count);
						notifyDataSetChanged();

						// 更新到数据库
						update_db(sock);
					}
				});

		holder.iv_privatesupermarket_plus
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						int count = sock.getCount();

						if (count < sock.getHave_num()) {
							count++;
							sock.setCount(count);

							notifyDataSetChanged();

							// 更新到数据库
							update_db(sock);
						}
					}
				});
		return view;
	}

	class ViewHolder {
		ImageView item_privatesupermarket_img;
		TextView item_privatesupermarket_name;
		TextView item_privatesupermarket_num;
		TextView item_privatesupermarket_price;

		TextView tv_privatesupermarket_count;
		ImageView iv_privatesupermarket_minus;
		ImageView iv_privatesupermarket_plus;
	}

	private void update_db(PrivateSuperMarketGoodsBean sock) {
		int userid = UserUtil.getuserid(context);
		// 判断这个sock是否已经存在数据库
		boolean exist = DBUtil.CheckPrivateSupermarketCartGoodsExist(context,
				sock.getId(), userid);
		if (exist) {

			Log.v(TAG,
					"  存在     id " + sock.getId() + "  count  "
							+ sock.getCount());

			// 存在，更新操作
			DBUtil.updatePrivateSupermarketCartCount(context, sock, userid);
		} else {
			// 不存在，新增

			Log.v(TAG,
					"  不存在，新增       id " + sock.getId() + "  count  "
							+ sock.getCount());

			DBUtil.insertPrivateSupermarketCart(context, sock, userid);
		}
		// 发广播修改私人超市界面数量变化
		Intent updateProxyStockCartCount = new Intent();
		updateProxyStockCartCount
				.setAction(GlobalConstant.UpdatePrivateSuperMarketCartCount);
		context.sendBroadcast(updateProxyStockCartCount);

	}
}
