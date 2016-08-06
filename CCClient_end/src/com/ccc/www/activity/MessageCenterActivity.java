package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.JPushMessageBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.ccc.www.util.XListViewUtil;
import com.ccc.www.view.XListView;
import com.ccc.www.view.XListView.IXListViewListener;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 消息中心
 * 
 * @author Administrator
 * 
 */
public class MessageCenterActivity extends BaseActivity {

	ImageButton ib_digital_goback;
	XListView act_messagecenter_list;

	int userid = 0;

	List<JPushMessageBean> allJPushMessageBean = new ArrayList<JPushMessageBean>();
	Adapter adapter = new Adapter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userid = UserUtil.getuserid(this);
		setContentView(R.layout.act_messagecenter);
		initview();

		registerReceiver(mESSAGEREAD, new IntentFilter(
				GlobalConstant.MESSAGEREAD));

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_digital_goback:
			MessageCenterActivity.this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findviewWithId() {
		ib_digital_goback = (ImageButton) findViewById(R.id.ib_digital_goback);
		act_messagecenter_list = (XListView) findViewById(R.id.act_messagecenter_list);

		act_messagecenter_list.setPullLoadEnable(false);
		act_messagecenter_list.setPullRefreshEnable(true);

		act_messagecenter_list.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				loadmsg();
			}

			@Override
			public void onLoadMore() {
				XListViewUtil.endload(act_messagecenter_list);
			}
		});

		act_messagecenter_list.setAdapter(adapter);
	}

	@Override
	public void initListener() {
		ib_digital_goback.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		loadmsg();
	}

	private void loadmsg() {
		// TODO Auto-generated method stub
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("t_uid", userid + "");

		loadData(HttpMethod.POST, GlobalConstant.Jpush_GET_MY_MSG, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {

						XListViewUtil.endload(act_messagecenter_list);

						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, "returnstr " + returnstr);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("加载失败");
						} else {
							try {

								List<JPushMessageBean> tempallJPushMessageBean = new ArrayList<JPushMessageBean>();

								JSONArray array = new JSONArray(returnstr);
								for (int i = 0; i < array.length(); i++) {
									JSONObject json = array.getJSONObject(i);

									int id = json.getInt("id");
									String jpshu_title = json
											.getString("jpshu_title");
									String jpush_context = json
											.getString("jpush_context");
									int from_user_id = json
											.getInt("from_user_id");
									int to_user_id = json.getInt("to_user_id");
									int status = json.getInt("status");

									JPushMessageBean bean = new JPushMessageBean(
											id, jpshu_title, jpush_context,
											from_user_id, to_user_id, status);

									tempallJPushMessageBean.add(bean);
								}

								allJPushMessageBean = tempallJPushMessageBean;

								adapter.notifyDataSetChanged();
							} catch (JSONException e) {
								e.printStackTrace();
								showToast("加载失败");
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();

						XListViewUtil.endload(act_messagecenter_list);

						showToast("加载失败");
					}
				});
	}

	class Adapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allJPushMessageBean.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allJPushMessageBean.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup arg2) {

			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(MessageCenterActivity.this).inflate(
						R.layout.item_jpushmsg, null);
				holder.item_jpushmsg_content = (TextView) view
						.findViewById(R.id.item_jpushmsg_content);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final JPushMessageBean bean = allJPushMessageBean.get(position);

			String content = "";
			int status = bean.getStatus();
			if (status == 0) {
				content = "[未读]" + bean.getJpshu_title();
			} else {
				content = "[已读]" + bean.getJpshu_title();
			}

			holder.item_jpushmsg_content.setText(content);

			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.putExtra("msg_id", bean.getId());
					intent.setClass(MessageCenterActivity.this,
							MessageCenterDetailActivity.class);
					startActivity(intent);
				}
			});
			return view;
		}

		class ViewHolder {
			TextView item_jpushmsg_content;
		}
	}

	MESSAGEREAD mESSAGEREAD = new MESSAGEREAD();

	class MESSAGEREAD extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			int msg_id = arg1.getIntExtra("msg_id", 0);
			for (int i = 0; i < allJPushMessageBean.size(); i++) {
				if (allJPushMessageBean.get(i).getId() == msg_id) {
					allJPushMessageBean.get(i).setStatus(1);
					break;
				}
			}
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mESSAGEREAD);
		super.onDestroy();
	}

}
