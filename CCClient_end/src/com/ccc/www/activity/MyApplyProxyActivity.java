package com.ccc.www.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ccc.ccclient_end.R;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MyApplyProxyActivity extends BaseActivity {

	
	String TAG="MyApplyProxyActivity";
	
	private ImageButton ib_my_apply_proxy_close;
	private TextView tv_my_apply_proxy_title;
	private TextView tv_my_apply_proxy_info;
	private TextView tv_apply_proxy_status;
	private String supmaket_id;

	private TextView tv_my_apply_proxy_hostel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_apply_proxy);
		initview();
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ib_my_apply_proxy_close:
			finish();
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
		ib_my_apply_proxy_close = (ImageButton) findViewById(R.id.ib_my_apply_proxy_close);
		tv_my_apply_proxy_title = (TextView) findViewById(R.id.tv_my_apply_proxy_title);
		tv_my_apply_proxy_info = (TextView) findViewById(R.id.tv_my_apply_proxy_info);
		tv_my_apply_proxy_hostel = (TextView) findViewById(R.id.tv_my_apply_proxy_hostel);
		tv_apply_proxy_status = (TextView) findViewById(R.id.tv_apply_proxy_status);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		ib_my_apply_proxy_close.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		Intent intent=this.getIntent();
		supmaket_id = intent.getStringExtra("supmaket_id");
		fillData();
	}

	/**
	 * 根据宿舍栋编号获取宿舍信息
	 */
	private void fillData() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("supmaket_id", supmaket_id);
		
		loadData(HttpMethod.POST, GlobalConstant.GET_APPLY_SUPMARKET_PROXY_HOSTEL, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				dismissProgressDialog();
				showToast("加载失败");
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				dismissProgressDialog();
				try {
					JSONObject object=new JSONObject(info.result);
					String schoolName=object.getString("school_name");
					String hostelName=object.getString("hostel_name");
					tv_my_apply_proxy_title.setText("申请代理信息：");
					tv_my_apply_proxy_info.setText("学校："+schoolName);
					tv_my_apply_proxy_hostel.setText("宿舍："+hostelName);
					tv_apply_proxy_status.setText("审核状态：审核中");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
