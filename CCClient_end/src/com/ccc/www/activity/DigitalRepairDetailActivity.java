package com.ccc.www.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.ImageUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DigitalRepairDetailActivity extends BaseActivity {

	private ImageButton ib_goods_detail_goback;

	int dpid;

	String company_phone = "";

	String TAG = "DigitalRepairDetailActivity";

	TextView activity_digitalrepair_detail_title;
	TextView activity_digitalrepair_detail_detail;
	TextView activity_digitalrepair_detail_companyname;
	TextView activity_digitalrepair_detail_address;
	TextView activity_digitalrepair_detail_phone;
	Button activity_digitalrepair_detail_call;

	ImageView event_pic1;
	ImageView event_pic2;
	ImageView event_pic3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		dpid = getIntent().getIntExtra("dpid", 0);

		setContentView(R.layout.activity_digitalrepair_detail);
		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_goods_detail_goback:
			this.finish();
			break;
		case R.id.activity_digitalrepair_detail_call:
			if (!TextUtils.isEmpty(company_phone)) {
				// intent启动拨打电话
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ company_phone));
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findviewWithId() {
		ib_goods_detail_goback = (ImageButton) findViewById(R.id.ib_goods_detail_goback);

		activity_digitalrepair_detail_title = (TextView) findViewById(R.id.activity_digitalrepair_detail_title);
		activity_digitalrepair_detail_detail = (TextView) findViewById(R.id.activity_digitalrepair_detail_detail);
		activity_digitalrepair_detail_companyname = (TextView) findViewById(R.id.activity_digitalrepair_detail_companyname);
		activity_digitalrepair_detail_address = (TextView) findViewById(R.id.activity_digitalrepair_detail_address);
		activity_digitalrepair_detail_phone = (TextView) findViewById(R.id.activity_digitalrepair_detail_phone);
		activity_digitalrepair_detail_call = (Button) findViewById(R.id.activity_digitalrepair_detail_call);

		event_pic1 = (ImageView) findViewById(R.id.event_pic1);
		event_pic2 = (ImageView) findViewById(R.id.event_pic2);
		event_pic3 = (ImageView) findViewById(R.id.event_pic3);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		ib_goods_detail_goback.setOnClickListener(this);
		activity_digitalrepair_detail_call.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("drepair_id", dpid + "");

		loadData(HttpMethod.POST, GlobalConstant.GET_DIGITAL_INFO_BY_ID,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {

						dismissProgressDialog();

						String returnstr = response.result;

						Log.v(TAG, response.result);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("加载失败");
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);

								int id = json.getInt("id");
								String title = json.getString("title");
								String detail = json.getString("detail");
								String company_name = json
										.getString("company_name");
								String company_address = json
										.getString("company_address");
								company_phone = json.getString("company_phone");
								int status = json.getInt("status");

								String company_log1 = json
										.getString("company_log1");
								String company_log2 = json
										.getString("company_log2");
								String company_log3 = json
										.getString("company_log3");

								final String[] urls = new String[3];
								urls[0] = company_log1;
								urls[1] = company_log2;
								urls[2] = company_log3;

								if (!TextUtils.isEmpty(company_log1)) {
									event_pic1.setVisibility(View.VISIBLE);
									ImageLoader.getInstance().displayImage(
											company_log1, event_pic1,
											ImageLoaderOption.getoption());

									event_pic1
											.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View arg0) {
													ImageUtil
															.toShowImages(
																	DigitalRepairDetailActivity.this,
																	urls, 0);
												}
											});
								} else {
									event_pic1.setVisibility(View.INVISIBLE);
								}

								if (!TextUtils.isEmpty(company_log2)) {
									event_pic2.setVisibility(View.VISIBLE);
									ImageLoader.getInstance().displayImage(
											company_log2, event_pic2,
											ImageLoaderOption.getoption());
									event_pic2
											.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View arg0) {
													ImageUtil
															.toShowImages(
																	DigitalRepairDetailActivity.this,
																	urls, 1);
												}
											});
								} else {
									event_pic2.setVisibility(View.INVISIBLE);
								}

								if (!TextUtils.isEmpty(company_log3)) {
									event_pic3.setVisibility(View.VISIBLE);
									ImageLoader.getInstance().displayImage(
											company_log3, event_pic3,
											ImageLoaderOption.getoption());
									event_pic3
											.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View arg0) {
													ImageUtil
															.toShowImages(
																	DigitalRepairDetailActivity.this,
																	urls, 2);
												}
											});
								} else {
									event_pic3.setVisibility(View.INVISIBLE);
								}

								activity_digitalrepair_detail_title
										.setText(title);
								activity_digitalrepair_detail_detail
										.setText("\t" + detail);
								activity_digitalrepair_detail_companyname
										.setText(company_name);
								activity_digitalrepair_detail_address
										.setText("地址:" + company_address);
								activity_digitalrepair_detail_phone
										.setText("预约电话：" + company_phone);

							} catch (JSONException e) {
								showToast("加载失败");
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
}
