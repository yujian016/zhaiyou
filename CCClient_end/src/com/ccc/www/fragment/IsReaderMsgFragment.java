package com.ccc.www.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.MsgAdapter;
import com.ccc.www.bean.MsgBean;

public class IsReaderMsgFragment extends BaseFragment {
	private Activity activity;
	public View rootView;
	private ListView lvMsg;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		activity = getActivity();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.msg_handle_fragment, container,
					false);
			initView();
			initData();
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}
	
	private void initView() {
		lvMsg=(ListView) rootView.findViewById(R.id.lv_handle_msg);
	}

	private void initData() {
		MsgBean m1=new MsgBean("1","恭喜你获取100个积分!");
		MsgBean m2=new MsgBean("2","为了账户安全，及时修改密码");
		MsgBean m3=new MsgBean("3","你的电话号码已经通过审核");
		MsgBean m4=new MsgBean("4","开店申请未通过审核");
		
		ArrayList<MsgBean> msgs=new ArrayList<MsgBean>();
		msgs.add(m1);
		msgs.add(m2);
		msgs.add(m3);
		msgs.add(m4);
		lvMsg.setAdapter(new MsgAdapter(activity,msgs,R.layout.msg_lv_item));
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

}
