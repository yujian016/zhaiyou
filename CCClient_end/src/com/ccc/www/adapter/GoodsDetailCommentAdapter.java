package com.ccc.www.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.GoodsDetailCommentBean;

public class GoodsDetailCommentAdapter extends BaseAdapter {

	private List<GoodsDetailCommentBean> lists = new ArrayList<GoodsDetailCommentBean>();
	private int resource;
	private LayoutInflater inflater;

	public GoodsDetailCommentAdapter(Context context,
			List<GoodsDetailCommentBean> lists, int resource) {
		super();
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
			ImageView ivlog = (ImageView) view
					.findViewById(R.id.iv_goods_comment_user_log);
			TextView tvtitle = (TextView) view
					.findViewById(R.id.tv_goods_comment_contxt);

			GoodsDetailCommentBean s = lists.get(index);

			String goodcomment = s.getGood_comment();
			String badcomment = s.getBad_comment();

			String showcoment = goodcomment;
			if (TextUtils.isEmpty(goodcomment)) {
				showcoment = badcomment;
			}

			tvtitle.setText(showcoment);
		}
		return view;
	}

}
