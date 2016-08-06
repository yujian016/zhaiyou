package com.ccc.www.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.ccc.ccclient_end.R;

/**
 * 快递查询
 * 
 * @author Administrator
 * 
 */
public class ExpressActivity extends BaseActivity {

	String TAG = "ExpressActivity";

	String kdno = "";
	String kdcompany = "";

	ImageButton ib_digital_goback;
	WebView act_express_webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		kdno = getIntent().getStringExtra("kdno");
		kdcompany = getIntent().getStringExtra("kdcompany");
		setContentView(R.layout.act_express);
		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_digital_goback:
			ExpressActivity.this.finish();
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
		act_express_webview = (WebView) findViewById(R.id.act_express_webview);
	}

	@Override
	public void initListener() {
		ib_digital_goback.setOnClickListener(this);
	}

	@Override
	public void initdata() {

		if (TextUtils.isEmpty(kdno) || TextUtils.isEmpty(kdcompany)) {
			return;
		}

		String kdcompanycode = "";

		if (kdcompany.contains("德邦")) {
			kdcompanycode = "debangwuliu";
		} else if (kdcompany.contains("dhl")) {
			kdcompanycode = "dhl";
		} else if (kdcompany.contains("d速")) {
			kdcompanycode = "dsukuaidi";
		} else if (kdcompany.contains("ems")) {
			kdcompanycode = "ems";
		} else if (kdcompany.contains("凤凰快递")) {
			kdcompanycode = "fenghuangkuaidi";
		} else if (kdcompany.contains("国通")) {
			kdcompanycode = "guotongkuaidi";
		} else if (kdcompany.contains("汇通")) {
			kdcompanycode = "huitongkuaidi";
		} else if (kdcompany.contains("佳吉")) {
			kdcompanycode = "jjwl";
		} else if (kdcompany.contains("快捷速递")) {
			kdcompanycode = "kuaijiesudi";
		} else if (kdcompany.contains("全晨")) {
			kdcompanycode = "quanchenkuaidi";
		} else if (kdcompany.contains("全峰")) {
			kdcompanycode = "quanfengkuaidi";
		} else if (kdcompany.contains("全日通快递")) {
			kdcompanycode = "quanritongkuaidi";
		} else if (kdcompany.contains("全一快递")) {
			kdcompanycode = "quanyikuaidi";
		} else if (kdcompany.contains("三态速递")) {
			kdcompanycode = "santaisudi";
		} else if (kdcompany.contains("申通")) {
			kdcompanycode = "shentong";
		} else if (kdcompany.contains("顺丰")) {
			kdcompanycode = "shunfeng";
		} else if (kdcompany.contains("速尔物流")) {
			kdcompanycode = "sue";
		} else if (kdcompany.contains("天天快递")) {
			kdcompanycode = "tiantian";
		} else if (kdcompany.contains("优速物流")) {
			kdcompanycode = "youshuwuliu";
		} else if (kdcompany.contains("邮政")) {
			kdcompanycode = "youzhengguonei";
		} else if (kdcompany.contains("圆通")) {
			kdcompanycode = "yuantong";
		} else if (kdcompany.contains("韵达")) {
			kdcompanycode = "yunda";
		} else if (kdcompany.contains("运通")) {
			kdcompanycode = "yuntongkuaidi";
		} else if (kdcompany.contains("宅急送")) {
			kdcompanycode = "zhaijisong";
		} else if (kdcompany.contains("中通")) {
			kdcompanycode = "zhongtong";
		} else if (kdcompany.contains("天天快递")) {
			kdcompanycode = "tiantian";
		}

		if (TextUtils.isEmpty(kdcompanycode)) {
			showToast("未识别快递公司");
			return;
		}

		// TODO Auto-generated method stub

		mysetting();

		WindowManager wm = getWindowManager();

		int width = wm.getDefaultDisplay().getWidth();

		Log.v(TAG, " width  " + width);
		// if (width == 1080) {
		// act_express_webview.setInitialScale(110);
		// } else {
		// act_express_webview.setInitialScale(75);
		// }

		String url = "http://wap.kuaidi100.com/wap_result.jsp?rand=20120517&id="
				+ kdcompanycode + "&fromWeb=null&&postid=" + kdno;

		act_express_webview.loadUrl(url);

	}

	void mysetting() {

		WebSettings webSettings = act_express_webview.getSettings();
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

		act_express_webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;

			}
		});

		// 支持javascript
		webSettings.setJavaScriptEnabled(true);
		// 设置可以支持缩放
		webSettings.setSupportZoom(true);
		// 设置出现缩放工具
		webSettings.setBuiltInZoomControls(true);

		// 设置默认缩放方式尺寸是far
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int mDensity = metrics.densityDpi;
		Log.d("maomao", "densityDpi = " + mDensity);
		if (mDensity == 240) {
			webSettings.setDefaultZoom(ZoomDensity.FAR);
		} else if (mDensity == 160) {
			webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
		} else if (mDensity == 120) {
			webSettings.setDefaultZoom(ZoomDensity.CLOSE);
		} else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
			webSettings.setDefaultZoom(ZoomDensity.FAR);
		} else if (mDensity == DisplayMetrics.DENSITY_TV) {
			webSettings.setDefaultZoom(ZoomDensity.FAR);
		} else {
			webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
		}

		// 扩大比例的缩放
		// act_express_webview.getSettings().setUseWideViewPort(true);
		// 自适应屏幕
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setLoadWithOverviewMode(true);
	}

}
