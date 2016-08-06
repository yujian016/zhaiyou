package com.ccc.www.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.DormitoryBean;

public class SelectDormitoryAdapter extends BaseAdapter {

	private List<DormitoryBean> lists = new ArrayList<DormitoryBean>();
	private int resource;
	private LayoutInflater inflater;

	Context context;

	public SelectDormitoryAdapter(Context context, List<DormitoryBean> lists,
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
			holder.tv_dormitory_name = (TextView) view
					.findViewById(R.id.tv_dormitory_name);
			holder.tv_dormitory_type = (TextView) view
					.findViewById(R.id.tv_dormitory_type);
			holder.linearlayout_dormitory = (LinearLayout) view
					.findViewById(R.id.linearlayout_dormitory);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (index % 2 != 0) {
			holder.linearlayout_dormitory.setBackgroundColor(context
					.getResources().getColor(R.color.col_250));
		} else {
			holder.linearlayout_dormitory.setBackgroundColor(context
					.getResources().getColor(R.color.white));
		}

		DormitoryBean s = lists.get(index);
		holder.tv_dormitory_name.setText(s.getHostel_name());
		int Is_proxy = s.getIs_proxy();
		if (Is_proxy == 0) {
			holder.tv_dormitory_type.setText("可申请");
			holder.tv_dormitory_type
					.setBackgroundResource(R.drawable.corners_selectdormitory_bg2);
		} else {
			holder.tv_dormitory_type.setText("营业中");
			holder.tv_dormitory_type
					.setBackgroundResource(R.drawable.corners_selectdormitory_bg1);
		}

		return view;
	}

	class ViewHolder {
		LinearLayout linearlayout_dormitory;
		TextView tv_dormitory_name;
		TextView tv_dormitory_type;
	}

}
