package com.ccc.www.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.SchoolBean;

public class SelectSchoolAdapter extends BaseAdapter {

	private List<SchoolBean> lists = new ArrayList<SchoolBean>();
	private LayoutInflater inflater;

	public SelectSchoolAdapter(Context context, List<SchoolBean> lists) {
		super();
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
			view = inflater.inflate(R.layout.school_lv_item, null);
			holder.tv_school_name = (TextView) view
					.findViewById(R.id.tv_school_name);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		SchoolBean s = lists.get(index);
		holder.tv_school_name.setText(s.getSchool_name());

		return view;
	}

	class ViewHolder {
		TextView tv_school_name;
	}

}
