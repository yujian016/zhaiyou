package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.SelectDormitoryAdapter;
import com.ccc.www.adapter.SelectDormitoryAreaAdapter;
import com.ccc.www.bean.DormitoryArea;
import com.ccc.www.bean.DormitoryBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.socialize.utils.Log;

public class PrivateSupermarketSelectDormitoryActivity extends BaseActivity {

	String TAG = "PrivateSupermarketSelectDormitoryActivity";

	private ImageButton ibGoback;

	ListView left_listview;
	ListView right_listview;

	int schoolid = 0;

	List<DormitoryBean> allDormitory = new ArrayList<DormitoryBean>();

	private int status = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		schoolid = getIntent().getIntExtra("schoolid", 0);
		setContentView(R.layout.activity_select_privatesupermarketdormitory);
		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_select_dormitory_goback:
			PrivateSupermarketSelectDormitoryActivity.this.finish();
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

								for (int i = 0; i < allDormitory.size() - 1; i++) {
									for (int j = 1; j < allDormitory.size() - i; j++) {
										DormitoryBean a;

										String hostel_name_j_1 = allDormitory
												.get(j - 1).getHostel_name();
										// String numStr_j_1 = hostel_name_j_1
										// .substring(0, hostel_name_j_1
										// .length() - 1);
										// int num_j_1 = Integer
										// .parseInt(numStr_j_1);

										String regEx = "[^0-9]";
										Pattern p = Pattern.compile(regEx);
										Matcher m = p.matcher(hostel_name_j_1);

										System.out.println(m.replaceAll("")
												.trim());

										int num_j_1 = Integer.parseInt(m
												.replaceAll("").trim());

										String hostel_name_j = allDormitory
												.get(j).getHostel_name();
//										String numStr_j = hostel_name_j.substring(
//												0, hostel_name_j.length() - 1);
//										int num_j = Integer.parseInt(numStr_j);
										
										
									 
										int num_j = Integer.parseInt(p.matcher(hostel_name_j).replaceAll("")
												.trim());

										if (num_j_1 > num_j) {
											a = allDormitory.get(j - 1);
											allDormitory.set((j - 1),
													allDormitory.get(j));
											allDormitory.set(j, a);
										}
									}
								}

								List<DormitoryBean> testallDormitory = new ArrayList<DormitoryBean>();

								testallDormitory.addAll(allDormitory);

								final List<DormitoryArea> allDormitorylist = new ArrayList<DormitoryArea>();

								int size = testallDormitory.size();
								if (size > 0) {
									int count = size / 12;
									int yushu = size % 12;
									if (yushu > 0) {
										count++;
									}

									for (int i = 0; i < count; i++) {

										String name = "";

										List<DormitoryBean> temp = null;
										if (i != count - 1) {
											int start = 12 * i;
											int end = 12 * (i + 1);

											temp = testallDormitory.subList(
													start, end);

											List<Integer> allnum = new ArrayList<Integer>();
											for (int j = 0; j < temp.size(); j++) {
												String hostel_name = temp
														.get(j)
														.getHostel_name();
												String numStr = hostel_name
														.substring(
																0,
																hostel_name
																		.length() - 1);
												
												String regEx = "[^0-9]";
												Pattern p = Pattern.compile(regEx);
												Matcher m = p.matcher(numStr);

												System.out.println(m.replaceAll("")
														.trim());

												int num = Integer.parseInt(m
														.replaceAll("").trim());
												
//												int num = Integer
//														.parseInt(numStr);
												allnum.add(num);
											}

											int min = allnum.get(0);
											int max = allnum.get(allnum.size() - 1);

											for (int m = 0; m < allnum.size(); m++) {
												if (min > allnum.get(m)) {
													min = allnum.get(m);
												}

												if (max < allnum.get(m)) {
													max = allnum.get(m);
												}
											}

											Log.v(TAG, "min  " + min);
											Log.v(TAG, "max  " + max);

											name = min + "栋—" + max + "栋";
										} else {
											temp = testallDormitory.subList(
													12 * i,
													testallDormitory.size());
											int start = 12 * i;
											int end = testallDormitory.size();

											temp = testallDormitory.subList(
													start, end);

											List<Integer> allnum = new ArrayList<Integer>();
											for (int j = 0; j < temp.size(); j++) {
												String hostel_name = temp
														.get(j)
														.getHostel_name();
												String numStr = hostel_name
														.substring(
																0,
																hostel_name
																		.length() - 1);
												
												String regEx = "[^0-9]";
												Pattern p = Pattern.compile(regEx);
												Matcher m = p.matcher(numStr);

												System.out.println(m.replaceAll("")
														.trim());

												int num = Integer.parseInt(m
														.replaceAll("").trim());
												
//												int num = Integer
//														.parseInt(numStr);
												allnum.add(num);
											}

											int min = allnum.get(0);
											int max = allnum.get(allnum.size() - 1);

											for (int m = 0; m < allnum.size(); m++) {
												if (min > allnum.get(m)) {
													min = allnum.get(m);
												}

												if (max < allnum.get(m)) {
													max = allnum.get(m);
												}
											}

											Log.v(TAG, "min  " + min);
											Log.v(TAG, "max  " + max);

											name = min + "栋—" + max + "栋";

										}

										DormitoryArea area = new DormitoryArea(
												name, temp);

										allDormitorylist.add(area);
									}
								}

								if (allDormitorylist.size() > 0) {

									SelectDormitoryAreaAdapter adapterleft = new SelectDormitoryAreaAdapter(
											PrivateSupermarketSelectDormitoryActivity.this,
											allDormitorylist,
											R.layout.item_dormitory_left);

									left_listview.setAdapter(adapterleft);

									left_listview
											.setOnItemClickListener(new OnItemClickListener() {
												@Override
												public void onItemClick(
														AdapterView<?> arg0,
														View arg1,
														int position, long arg3) {
													// TODO Auto-generated
													// method
													// stub

													loadrightdata(allDormitorylist
															.get(position)
															.getAllDormitory());

												}
											});

									Log.v(TAG, "testallDormitory  "
											+ testallDormitory.size());
									Log.v(TAG, "allDormitorylist  "
											+ allDormitorylist.size());

									// 加载右边的数据
									loadrightdata(allDormitorylist.get(0)
											.getAllDormitory());
								}
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

	void loadrightdata(final List<DormitoryBean> allDormitory) {

		Log.v(TAG, "allDormitory  " + allDormitory.size());

		SelectDormitoryAdapter adapter = new SelectDormitoryAdapter(
				PrivateSupermarketSelectDormitoryActivity.this, allDormitory,
				R.layout.item_dormitory);

		right_listview.setAdapter(adapter);

		right_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				int Is_proxy = allDormitory.get(position).getIs_proxy();
				final int hostel_id = allDormitory.get(position).getId();
				Intent intent = new Intent();
				intent.setClass(PrivateSupermarketSelectDormitoryActivity.this,
						PrivateSupermarketDormitoryProxyActivity.class);
				intent.putExtra("hostel_id", hostel_id);

				UserUtil.setprivatesmhotelid(
						PrivateSupermarketSelectDormitoryActivity.this,
						hostel_id);

				startActivity(intent);
			}
		});

	}
}
