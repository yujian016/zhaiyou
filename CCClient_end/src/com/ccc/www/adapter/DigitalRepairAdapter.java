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
import com.ccc.www.bean.Info_DigitalRepairBean;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DigitalRepairAdapter extends BaseAdapter {

	private ArrayList<Info_DigitalRepairBean> lists = new ArrayList<Info_DigitalRepairBean>();
	private LayoutInflater inflater;

	public DigitalRepairAdapter(Context context,
			ArrayList<Info_DigitalRepairBean> lists) {
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
			view = inflater.inflate(R.layout.item_digitalrepair, null);

			holder.item_digitalrepair_image = (ImageView) view
					.findViewById(R.id.item_digitalrepair_image);
			holder.item_digitalrepair_title = (TextView) view
					.findViewById(R.id.item_digitalrepair_title);
			holder.item_digitalrepair_content = (TextView) view
					.findViewById(R.id.item_digitalrepair_content);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		Info_DigitalRepairBean dp = lists.get(index);

		String log1 = dp.getCompany_log1();
		if (!log1.startsWith("http")) {
			log1 = GlobalConstant.SERVER_URL + log1;
		}

		ImageLoader.getInstance().displayImage(log1,
				holder.item_digitalrepair_image,
				ImageLoaderOption.getoption());

		holder.item_digitalrepair_title.setText(dp.getTitle());
		holder.item_digitalrepair_content.setText(dp.getDetail());

		return view;
	}

	class ViewHolder {
		ImageView item_digitalrepair_image;
		TextView item_digitalrepair_title;
		TextView item_digitalrepair_content;
	}

}
