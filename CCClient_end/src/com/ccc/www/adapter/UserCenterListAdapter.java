package com.ccc.www.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.UserCenterListBean;

public class UserCenterListAdapter extends BaseAdapter {

	private ArrayList<UserCenterListBean> lists = new ArrayList<UserCenterListBean>();
	private int resource;
	private LayoutInflater inflater;
	private Context context;

	public UserCenterListAdapter(Context context,
			ArrayList<UserCenterListBean> lists, int resource) {
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
	public View getView(int position, View view, ViewGroup vgroup) {
		ViewHolder holder = null;

		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(resource, null);

			holder.mycenter_layout = (LinearLayout) view
					.findViewById(R.id.mycenter_layout);
			holder.iv_user_smail = (ImageView) view
					.findViewById(R.id.iv_user_smail);
			holder.tv_user_title = (TextView) view
					.findViewById(R.id.tv_user_title);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}
		UserCenterListBean bean = lists.get(position);
		if (position % 2 == 0) {
			holder.mycenter_layout.setBackgroundColor(context.getResources()
					.getColor(R.color.white));
		} else {
			holder.mycenter_layout.setBackgroundColor(context.getResources()
					.getColor(R.color.col_250));
		}

		String name = bean.getList_name();
		holder.tv_user_title.setText(name);

		if (name.contains("我的钱包")) {
			holder.iv_user_smail.setImageResource(R.drawable.icon_wdqb);
		} else if (name.contains("我的订单")) {
			holder.iv_user_smail.setImageResource(R.drawable.icon_wddd);
		} else if (name.contains("我的店铺")) {
			holder.iv_user_smail.setImageResource(R.drawable.icon_wddp);
		} else if (name.contains("反馈帮助")) {
			holder.iv_user_smail.setImageResource(R.drawable.icon_fkbz);
		} else if (name.contains("分享")) {
			holder.iv_user_smail.setImageResource(R.drawable.icon_share);
		} else if (name.contains("系统设置")) {
			holder.iv_user_smail
					.setImageResource(R.drawable.icon_systemsetting);
		} else if (name.contains("我的超市")) {
			holder.iv_user_smail.setImageResource(R.drawable.icon_wdcs);
		} else if (name.contains("代理进货")) {
			holder.iv_user_smail.setImageResource(R.drawable.icon_dljh);
		} else if (name.contains("发布优惠")) {
			holder.iv_user_smail.setImageResource(R.drawable.icon_fbyh);
		}

		return view;
	}

	class ViewHolder {
		LinearLayout mycenter_layout;
		ImageView iv_user_smail;
		TextView tv_user_title;
	}

}
