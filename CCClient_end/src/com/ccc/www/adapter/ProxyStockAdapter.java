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
import android.widget.Toast;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.SockBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.utils.Log;

public class ProxyStockAdapter extends BaseAdapter {

	String TAG = "ProxyStockAdapter";

	private ArrayList<SockBean> lists = new ArrayList<SockBean>();
	private LayoutInflater inflater;
	private Context context;

	public ProxyStockAdapter(Context context, ArrayList<SockBean> lists) {
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

		Log.v(TAG, "getView  " + index);

		if (view == null) {
			holder = new ViewHolder();

			view = inflater.inflate(R.layout.item_proxysock2, null);

			holder.item_proxysock2_img = (ImageView) view
					.findViewById(R.id.item_proxysock2_img);
			holder.item_proxysock2_name = (TextView) view
					.findViewById(R.id.item_proxysock2_name);
			holder.item_proxysock2_price = (TextView) view
					.findViewById(R.id.item_proxysock2_price);
			holder.iv_sock_minus = (ImageView) view
					.findViewById(R.id.iv_sock_minus);
			holder.iv_sock_plus = (ImageView) view
					.findViewById(R.id.iv_sock_plus);
			holder.tv_sock_count = (TextView) view
					.findViewById(R.id.tv_sock_count);

			holder.item_proxysock2_startcount = (TextView) view
					.findViewById(R.id.item_proxysock2_startcount);

			holder.item_proxysock2_suggestprice = (TextView) view
					.findViewById(R.id.item_proxysock2_suggestprice);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		final SockBean sock = lists.get(index);

		String img = sock.getSock_log();
		if (!img.startsWith("http")) {
			img = GlobalConstant.SERVER_URL + img;
		}

		ImageLoader.getInstance().displayImage(img, holder.item_proxysock2_img,
				ImageLoaderOption.getoption());

		holder.item_proxysock2_name.setText(sock.getSock_name());
		holder.item_proxysock2_price.setText("批发价:￥" + sock.getSock_price()
				+ "元");

		holder.tv_sock_count.setText(sock.getCount() + "");

		holder.item_proxysock2_startcount.setText("起批量:"
				+ sock.getSock_startcount());

		holder.item_proxysock2_suggestprice.setText("建议零售价:"
				+ sock.getSock_suggestprice() + "元");

		holder.iv_sock_minus.setOnClickListener(new OnClickListener() {
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

		holder.iv_sock_plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int count = sock.getCount();
				if (count < sock.getSock_total()) {
					count++;
					sock.setCount(count);
					notifyDataSetChanged();
					// 更新到数据库
					update_db(sock);
				} else {
					Toast.makeText(context, "已达到最大库存", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		return view;
	}

	class ViewHolder {
		ImageView item_proxysock2_img;
		TextView item_proxysock2_name;
		TextView item_proxysock2_price;

		ImageView iv_sock_minus;
		ImageView iv_sock_plus;
		TextView tv_sock_count;

		TextView item_proxysock2_startcount;
		TextView item_proxysock2_suggestprice;

	}

	private void update_db(SockBean sock) {
		int userid = UserUtil.getuserid(context);
		// 判断这个sock是否已经存在数据库
		boolean exist = DBUtil.CheckProxyStockExist(context, sock.getId(),
				userid);
		if (exist) {

			Log.v(TAG,
					"  存在     id " + sock.getId() + "  count  "
							+ sock.getCount());

			// 存在，更新操作
			DBUtil.updateProxyStockCartCount(context, sock, userid);
		} else {
			// 不存在，新增

			Log.v(TAG,
					"  不存在，新增       id " + sock.getId() + "  count  "
							+ sock.getCount());

			DBUtil.insertProxyStockCart(context, sock, userid);
		}
		// 发广播修改代理进货界面数量变化
		Intent updateProxyStockCartCount = new Intent();
		updateProxyStockCartCount
				.setAction(GlobalConstant.UpdateProxyStockCartCount);
		context.sendBroadcast(updateProxyStockCartCount);

	}

}
