package com.ccc.www.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.OrderBean;
import com.ccc.www.util.XListViewUtil;
import com.ccc.www.view.XListView;
import com.ccc.www.view.XListView.IXListViewListener;

public class Order_Fragment3 extends BaseFragment {

	protected static final String TAG = "Order_Fragment3";
	View rootView;
	Activity activity;

	XListView order_fragment_xlistview;


	Adapter adapter = new Adapter();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		activity = getActivity();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_order3, container,
					false);
			initview();
			initdata();
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	private void initview() {
		order_fragment_xlistview = (XListView) rootView
				.findViewById(R.id.order_fragment_xlistview);

		order_fragment_xlistview.setPullLoadEnable(false);
		order_fragment_xlistview.setPullRefreshEnable(true);
		order_fragment_xlistview.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				XListViewUtil.endload(order_fragment_xlistview);
			}

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				XListViewUtil.endload(order_fragment_xlistview);
			}
		});
	}

	private void initdata() {
		// TODO Auto-generated method stub
		order_fragment_xlistview.setAdapter(adapter);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	class Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 20;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view  = LayoutInflater.from(activity).inflate(R.layout.item_orderfragment1_myorder, null);
				holder. order_image = (ImageView) view.findViewById(R.id.order_image);
				holder. order_name = (TextView) view.findViewById(R.id.order_name);
				holder. order_buytime = (TextView) view.findViewById(R.id.order_buytime);
				holder. order_price = (TextView) view.findViewById(R.id.order_price);
				holder. order_status = (TextView) view.findViewById(R.id.order_status);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			return view;
		}

		class ViewHolder {
			ImageView order_image;
			TextView order_name;
			TextView order_buytime;
			TextView order_price;
			TextView order_status;
		}

	}

}
