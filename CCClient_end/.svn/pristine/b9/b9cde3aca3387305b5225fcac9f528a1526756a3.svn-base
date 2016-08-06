package com.ccc.www.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.SockBean;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SockAdapter extends BaseAdapter {

	private ArrayList<SockBean> lists = new ArrayList<SockBean>();
	private int resource;
	private LayoutInflater inflater;
	private Context context;

	public SockAdapter(Context context, ArrayList<SockBean> lists, int resource) {
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
		if (view == null) {
			view = inflater.inflate(resource, null);

			ImageView ivlog = (ImageView) view.findViewById(R.id.iv_sock_log);
			TextView sockTitle = (TextView) view
					.findViewById(R.id.tv_sock_name);
			TextView sockPrice = (TextView) view
					.findViewById(R.id.tv_sock_price);

			SockBean sock = lists.get(index);

			ImageLoader.getInstance().displayImage(sock.getSock_log(), ivlog,
					ImageLoaderOption.getoption());

			sockTitle.setText(sock.getSock_name());
			sockPrice.setText("￥" + sock.getSock_price() + "元");

		}
		return view;
	}

}
