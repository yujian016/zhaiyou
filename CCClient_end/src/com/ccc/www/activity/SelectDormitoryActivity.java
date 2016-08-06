package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.DormitoryBean;
import com.ccc.www.bean.DormitoryFloorBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.utils.Log;

public class SelectDormitoryActivity extends BaseActivity {

	String TAG = "SelectDormitoryActivity";

	private ImageButton ibGoback;

	ListView left_listview;
	ListView right_listview;

	int schoolid = 0;
	String schoolname = "";

	List<DormitoryFloorBean> allDormitoryFloor = new ArrayList<DormitoryFloorBean>();
	List<DormitoryBean> allDormitory = new ArrayList<DormitoryBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		schoolid = getIntent().getIntExtra("schoolid", 0);
		schoolname = getIntent().getStringExtra("schoolname");

		setContentView(R.layout.activity_select_dormitory);
		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_select_dormitory_goback:
			SelectDormitoryActivity.this.finish();
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
		ibGoback = (ImageButton) findViewById(R.id.ib_select_dormitory_goback);
		left_listview = (ListView) findViewById(R.id.left_listview);
		right_listview = (ListView) findViewById(R.id.right_listview);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		ibGoback.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("school_id", schoolid + "");

		loadData(HttpMethod.POST, GlobalConstant.GET_SCHOOL_HOSTEL, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("加载失败");
						} else {
							try {
								JSONArray json = new JSONArray(returnstr);
								for (int i = 0; i < json.length(); i++) {
									JSONObject temp = json.getJSONObject(i);
									int id = temp.getInt("id");
									int school_id = temp.getInt("school_id");
									String hostel_name = temp
											.getString("hostel_name");
									int is_proxy = temp.getInt("is_proxy");
									DormitoryBean bean = new DormitoryBean(id,
											school_id, hostel_name, is_proxy);
									allDormitory.add(bean);
								}

								SelectDormitoryAdapter adapter = new SelectDormitoryAdapter();

								left_listview.setAdapter(adapter);

								if (allDormitory.size() > 0) {
									int id = allDormitory.get(0).getId();
									String hostelname = allDormitory.get(0)
											.getHostel_name();
									loadfloor(id, hostelname);
								}

								left_listview
										.setOnItemClickListener(new OnItemClickListener() {
											@Override
											public void onItemClick(
													AdapterView<?> arg0,
													View arg1, int position,
													long arg3) {

												int id = allDormitory.get(
														position).getId();
												String hostelname = allDormitory
														.get(position)
														.getHostel_name();
												loadfloor(id, hostelname);
											}
										});
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}
				});

	}

	class SelectDormitoryAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allDormitory.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allDormitory.get(arg0);
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
				view = LayoutInflater.from(SelectDormitoryActivity.this)
						.inflate(R.layout.item_privatesupermarketdormitory,
								null);
				holder.tv_dormitory_name = (TextView) view
						.findViewById(R.id.tv_dormitory_name);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.tv_dormitory_name.setText(allDormitory.get(position)
					.getHostel_name());

			return view;
		}

		class ViewHolder {
			TextView tv_dormitory_name;
		}
	}

	/**
	 * 加载宿舍楼层
	 * 
	 * @param id
	 */
	private void loadfloor(final int hostelid, final String hostelname) {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");
		
		Log.v(TAG, "hostel_id  " + hostelid);
		Log.v(TAG, "hostelname  " + hostelname);

		RequestParams params = new RequestParams();
		params.addBodyParameter("hostel_id", hostelid + "");
		params.addBodyParameter("school_id", schoolid + "");

		loadData(HttpMethod.POST, GlobalConstant.GET_SCHOOL_HOSTEL_FLOOR,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub
						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("加载失败");
						} else {
							try {
								allDormitoryFloor.clear();
								JSONArray json = new JSONArray(returnstr);
								for (int i = 0; i < json.length(); i++) {
									JSONObject temp = json.getJSONObject(i);
									int id = temp.getInt("id");
									int school_id = temp.getInt("school_id");
									int hostel_id = temp.getInt("hostel_id");
									String floor_name = temp
											.getString("floor_name");
									int status = temp.getInt("status");

									DormitoryFloorBean bean = new DormitoryFloorBean(
											school_id, hostel_id, floor_name,
											status);
									allDormitoryFloor.add(bean);
								}

								FloorAdapter adapter = new FloorAdapter();

								right_listview.setAdapter(adapter);

								right_listview
										.setOnItemClickListener(new OnItemClickListener() {
											@Override
											public void onItemClick(
													AdapterView<?> arg0,
													View arg1, int position,
													long arg3) {
												// 回首页
												DormitoryFloorBean bean = allDormitoryFloor
														.get(position);

												int floorid = bean.getId();
												String floorname = bean
														.getFloor_name();

												UserUtil.setschoolid(
														SelectDormitoryActivity.this,
														schoolid);
												UserUtil.setschoolname(
														SelectDormitoryActivity.this,
														schoolname);

												UserUtil.sethostelid(
														SelectDormitoryActivity.this,
														hostelid);
												UserUtil.sethostelname(
														SelectDormitoryActivity.this,
														hostelname);

												UserUtil.setfloorid(
														SelectDormitoryActivity.this,
														floorid);
												UserUtil.setfloorname(
														SelectDormitoryActivity.this,
														floorname);

												setResult(SelectSchoolActivity.CloseResultCode);

												SelectDormitoryActivity.this
														.finish();

											}
										});

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}
				});
	}

	class FloorAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allDormitoryFloor.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allDormitoryFloor.get(arg0);
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
				view = LayoutInflater.from(SelectDormitoryActivity.this)
						.inflate(
								R.layout.item_privatesupermarketdormitoryfloor,
								null);
				holder.tv_dormitory_floor_name = (TextView) view
						.findViewById(R.id.tv_dormitory_floor_name);

				view.setTag(holder);

			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.tv_dormitory_floor_name.setText(allDormitoryFloor.get(
					position).getFloor_name());

			return view;
		}

		class ViewHolder {
			TextView tv_dormitory_floor_name;
		}

	}
}
