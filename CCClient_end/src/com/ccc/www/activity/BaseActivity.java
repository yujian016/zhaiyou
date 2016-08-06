package com.ccc.www.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.CloseActivityClass;
import com.ccc.www.util.GetBalanceInterface;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.MD5;
import com.ccc.www.util.UserUtil;
import com.ccc.www.util.Xutils;
import com.ccc.www.view.LoadingProgress;
import com.google.gson.JsonObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends FragmentActivity implements
		OnClickListener, OnItemClickListener {

	String TAG = "BaseActivity";

	protected LoadingProgress mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		CloseActivityClass.activityList.add(this);

		super.onCreate(savedInstanceState);
	}

	public void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void showLoading2(String msg) {
		if (mDialog == null) {
			mDialog = LoadingProgress.createDialog(this);
			/* mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); */
			mDialog.setOwnerActivity(this);
			mDialog.setCancelable(true);
			mDialog.setCanceledOnTouchOutside(true);
		}
		mDialog.setMessage(msg);
		if (!mDialog.isShowing()) {
			mDialog.show();
		}
	}

	public void dismissProgressDialog() {
		if (null != mDialog && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	public void onResume() {
		MobclickAgent.onResume(this);
		JPushInterface.onResume(this);
		super.onResume();
	}

	public void onPause() {
		MobclickAgent.onPause(this);
		JPushInterface.onPause(this);
		super.onPause();
	}

	public abstract void findviewWithId();

	public abstract void initListener();

	public abstract void initdata();

	public void initview() {
		findviewWithId();
		initListener();
		initdata();
	}

	/**
	 * ��������
	 * 
	 * @param method
	 * @param url
	 * @param params
	 * @param callBack
	 */

	public void loadData(HttpRequest.HttpMethod method, String url,
			RequestParams params, RequestCallBack<String> callBack) {
		HttpUtils httpUtils = new HttpUtils();
		// 30秒超时
		httpUtils.configCurrentHttpCacheExpiry(1000 * 30);
		// httpUtils.configCurrentHttpCacheExpiry(0);
		if (params == null) {
			params = new RequestParams();
		}
		httpUtils.send(method, url, params, callBack);
	}

	/**
	 * 获取账户余额
	 */
	public void getAccountBalance(final GetBalanceInterface callback) {

		// 掉接口
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在获取账户余额");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id",
				String.valueOf(UserUtil.getuserid(this)));

		loadData(HttpMethod.POST, GlobalConstant.GET_USER_BALANCE, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub

						dismissProgressDialog();

						String returnstr2 = arg0.result;

						callback.Callback(returnstr2);

						Log.v(TAG, "returnstr " + returnstr2);

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						dismissProgressDialog();
						Log.v(TAG, "onFailure " + arg1);

						callback.Callback("");

					}
				});

	}

	/**
	 * 推送接口
	 */
	public void JpushAddMsg(int f_uid, int t_uid, String msg_title,
			String msg_context) {
		// 掉接口
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		RequestParams params = new RequestParams();

		params.addBodyParameter("f_uid", String.valueOf(f_uid));
		params.addBodyParameter("t_uid", String.valueOf(t_uid));
		params.addBodyParameter("msg_title", msg_title);
		params.addBodyParameter("msg_context", msg_context);

		loadData(HttpMethod.POST, GlobalConstant.Jpush_ADD_MSG, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub

						dismissProgressDialog();

						String returnstr2 = arg0.result;

						Log.v(TAG, "returnstr " + returnstr2);

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						dismissProgressDialog();
						Log.v(TAG, "onFailure " + arg1);

					}
				});

	}

	/**
	 * 推送接口
	 */
	public void JpushGetIMEI_BY_UID__Push(int user_id, final String title,
			final String content) {
		// 掉接口
		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", "" + user_id);

		Xutils.loadData(HttpMethod.POST, GlobalConstant.Jpush_GET_REGID_BY_UID,
				params, new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String returnstr = arg0.result;

						try {
							JSONObject json = new JSONObject(returnstr);
							String imei_key = json.getString("imei_key");

							sendMsgByImei(imei_key, title, content);

						} catch (JSONException e) {
							e.printStackTrace();
						}

						Log.v(TAG, "Jpush_GET_REGID_BY_UID returnstr  "
								+ returnstr);

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.v(TAG, "Jpush_GET_REGID_BY_UID  onFailure  " + arg1);
					}
				});
	}

	private void sendMsgByImei(String imei, String title, String content) {

		JsonObject jsonobj = new JsonObject();
		jsonobj.addProperty("n_title", title);
		jsonobj.addProperty("n_content", content);

		RequestParams params = new RequestParams();
		params.addBodyParameter("sendno", "" + 100000);
		params.addBodyParameter("app_key", "867efab78ddabaf552c5c62a");
		params.addBodyParameter("receiver_type", "5");
		params.addBodyParameter("receiver_value", imei);

		String verification_code_Str = "1000005" + imei
				+ "703d2031624cb5f23f4f8e29";

		verification_code_Str = MD5.md5(verification_code_Str).toUpperCase();

		params.addBodyParameter("verification_code", verification_code_Str);
		params.addBodyParameter("msg_type", "1");

		params.addBodyParameter("msg_content", jsonobj.toString());
		params.addBodyParameter("platform", "android,ios");
		params.addBodyParameter("time_to_live", "864000");

		Xutils.loadData(HttpMethod.POST, GlobalConstant.Jpush_V2_URL,
				params, new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String returnstr = arg0.result;

						 

						Log.v(TAG, "sendMsgByImei returnstr  "
								+ returnstr);

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.v(TAG, "sendMsgByImei  onFailure  " + arg1);
					}
				});
	}

}
