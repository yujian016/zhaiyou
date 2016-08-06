package com.ccc.www.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.MsgBean;

public class MsgAdapter extends BaseAdapter {

	private ArrayList<MsgBean> lists=new ArrayList<MsgBean>();
	private int resource;
	private LayoutInflater inflater;
	
	public MsgAdapter(Context context,ArrayList<MsgBean> lists, int resource) {
		super();
		this.lists = lists;
		this.resource = resource;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		if(view==null){
			view=inflater.inflate(resource, null);
			TextView tvSchool=(TextView) view.findViewById(R.id.tv_msg_name);
			MsgBean s=lists.get(index);
			tvSchool.setText(s.getContext());
		}
		return view;
	}

}
