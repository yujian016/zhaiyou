package com.ccc.www.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.ScoreMarketGoodsBean;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ScoreShopAdapter extends BaseAdapter {

	private List<ScoreMarketGoodsBean> lists = new ArrayList<ScoreMarketGoodsBean>();
	private LayoutInflater inflater;

	Context context;

	public ScoreShopAdapter(Context context, List<ScoreMarketGoodsBean> lists) {
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
			view = inflater.inflate(R.layout.scoregoods_gv_item, null);

			holder.tv_scoregoods_title = (TextView) view
					.findViewById(R.id.tv_scoregoods_title);
			holder.iv_scoregoods_log = (ImageView) view
					.findViewById(R.id.iv_scoregoods_log);
			holder.tv_scoregoods_score = (TextView) view
					.findViewById(R.id.tv_scoregoods_score);
			holder.tv_scoregoods_num = (TextView) view
					.findViewById(R.id.tv_scoregoods_num);
			holder.btn_scoregoods_exchange = (Button) view
					.findViewById(R.id.btn_scoregoods_exchange);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		final ScoreMarketGoodsBean s = lists.get(index);

		ImageLoader.getInstance().displayImage(s.getLog(),
				holder.iv_scoregoods_log,
				ImageLoaderOption.getoption());

		holder.tv_scoregoods_score.setText("" + s.getNeed_integral());
		holder.tv_scoregoods_num.setText(s.getGoods_num() + "份");
		holder.tv_scoregoods_title.setText(s.getGoods_title());

		holder.btn_scoregoods_exchange
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {

						Intent intent = new Intent();
						intent.setAction(GlobalConstant.ExchangePoint);
						Bundle bundle = new Bundle();
						bundle.putSerializable("info", (Serializable) s);
						intent.putExtras(bundle);

						context.sendBroadcast(intent);
					}
				});

		return view;
	}

	class ViewHolder {
		TextView tv_scoregoods_title;
		ImageView iv_scoregoods_log;
		TextView tv_scoregoods_score;
		TextView tv_scoregoods_num;
		Button btn_scoregoods_exchange;
	}

}
