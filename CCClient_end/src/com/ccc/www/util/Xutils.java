package com.ccc.www.util;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class Xutils {
	
	public static void loadData(HttpMethod method, String url, RequestParams params,
			RequestCallBack<String> callBack) {
		HttpUtils httpUtils = new HttpUtils();
		// 30秒超市
		httpUtils.configCurrentHttpCacheExpiry(1000 * 30);
		if (params == null) {
			params = new RequestParams();
		} 
		httpUtils.send(method, url, params, callBack);
	}

}
