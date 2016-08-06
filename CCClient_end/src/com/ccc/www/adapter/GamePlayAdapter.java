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

import com.ccc.ccclient_end.R;
import com.ccc.www.activity.GamePlayDetailActivity;
import com.ccc.www.bean.Info_GamePlayBean;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GamePlayAdapter extends BaseAdapter {

	private ArrayList<Info_GamePlayBean> lists = new ArrayList<Info_GamePlayBean>();
	private LayoutInflater inflater;
	Context context;

	public GamePlayAdapter(Context context, ArrayList<Info_GamePlayBean> lists) {
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
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.item_gameplay, null);

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

		final Info_GamePlayBean et = lists.get(index);

		String img = et.getLog1();
		if (!img.startsWith("http")) {
			img = GlobalConstant.SERVER_URL + img;
		}

		ImageLoader.getInstance().displayImage(img,
				holder.item_educationtrain_img,
				ImageLoaderOption.getoption());

		holder.item_educationtrain_title.setText(et.getTitle());
		holder.item_educationtrain_content.setText(et.getDetail());
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int dpid = et.getId();
				Intent intent = new Intent(context,
						GamePlayDetailActivity.class);
				intent.putExtra("gameid", dpid);
				context.startActivity(intent);
			}
		});

		return view;
	}

	class ViewHolder {
		ImageView item_educationtrain_img;
		TextView item_educationtrain_title;
		TextView item_educationtrain_content;
	}

}
