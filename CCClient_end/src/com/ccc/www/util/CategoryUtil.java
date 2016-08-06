package com.ccc.www.util;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class CategoryUtil {

	private static HttpUtils httpUtils = new HttpUtils();
	private static Gson gson = new Gson();
	private Context activity = null;

	private void getCategory(Context context, String id) {
		activity = context;
		httpUtils.send(HttpMethod.GET, GlobalConstant.SHOP_ACTION_GET_MY_STORE,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException e, String msg) {
						Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> json) {
						String ss_result = json.result;
					}
				});
	}

}
