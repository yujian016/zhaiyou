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
import com.ccc.www.bean.Info_EducationTrainBean;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EducationTrainAdapter extends BaseAdapter {

	private ArrayList<Info_EducationTrainBean> lists = new ArrayList<Info_EducationTrainBean>();
	private LayoutInflater inflater;

	public EducationTrainAdapter(Context context,
			ArrayList<Info_EducationTrainBean> lists) {
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
			view = inflater.inflate(R.layout.item_educationtrain, null);

			holder.item_educationtrain_img = (ImageView) view
					.findViewById(R.id.item_educationtrain_img);
			holder.item_educationtrain_title = (TextView) view
					.findViewById(R.id.item_educationtrain_title);
			holder.item_educationtrain_content = (TextView) view
					.findViewById(R.id.item_educationtrain_content);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		Info_EducationTrainBean et = lists.get(index);

		String img = et.getCompany_log1();
		if (!img.startsWith("http")) {
			img = GlobalConstant.SERVER_URL + img;
		}

		ImageLoader.getInstance().displayImage(img,
				holder.item_educationtrain_img,
				ImageLoaderOption.getoption());

		holder.item_educationtrain_title.setText(et.getTitle());
		holder.item_educationtrain_content.setText(et.getDetail());

		return view;
	}

	class ViewHolder {
		ImageView item_educationtrain_img;
		TextView item_educationtrain_title;
		TextView item_educationtrain_content;
	}

}
