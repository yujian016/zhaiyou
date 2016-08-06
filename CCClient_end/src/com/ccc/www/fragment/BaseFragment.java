package com.ccc.www.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.Xutils;
import com.ccc.www.view.LoadingProgress;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseFragment extends Fragment implements OnClickListener,
		OnItemClickListener {

	String TAG = "BaseFragment";

	protected LoadingProgress mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public abstract void initListener();

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen"); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen");
	}

	public void showLoading2(String msg) {
		if (mDialog == null) {
			mDialog = LoadingProgress.createDialog(getActivity());
			/* mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); */
			mDialog.setOwnerActivity(getActivity());
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

	public void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 推送接口
	 */
	public void JpushAddMsg(int f_uid, int t_uid, String msg_title,
			String msg_context) {
		// 掉接口
		if (!BaseUtils.isNetWork(getActivity())) {
			showToast("请检查您的网络");
			return;
		}

		RequestParams params = new RequestParams();

		params.addBodyParameter("f_uid", String.valueOf(f_uid));
		params.addBodyParameter("t_uid", String.valueOf(t_uid));
		params.addBodyParameter("msg_title", msg_title);
		params.addBodyParameter("msg_context", msg_context);

		Xutils.loadData(HttpMethod.POST, GlobalConstant.Jpush_ADD_MSG, params,
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

}
