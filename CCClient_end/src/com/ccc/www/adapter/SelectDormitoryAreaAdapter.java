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
import com.ccc.www.bean.DormitoryArea;

public class SelectDormitoryAreaAdapter extends BaseAdapter {

	private List<DormitoryArea> lists = new ArrayList<DormitoryArea>();
	private int resource;
	private LayoutInflater inflater;
	private Context context;

	public SelectDormitoryAreaAdapter(Context context,
			List<DormitoryArea> lists, int resource) {
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

			holder.linearlayout_dormitory_left = (LinearLayout) view
					.findViewById(R.id.linearlayout_dormitory_left);
			holder.tv_dormitory_name_left = (TextView) view
					.findViewById(R.id.tv_dormitory_name_left);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		DormitoryArea s = lists.get(index);

		holder.tv_dormitory_name_left.setText(s.getName());

		if (index % 2 != 0) {
			holder.linearlayout_dormitory_left.setBackgroundColor(context
					.getResources().getColor(R.color.white));
		} else {
			holder.linearlayout_dormitory_left.setBackgroundColor(context
					.getResources().getColor(R.color.col_238));
		}

		return view;
	}

	class ViewHolder {
		LinearLayout linearlayout_dormitory_left;
		TextView tv_dormitory_name_left;
	}

}
