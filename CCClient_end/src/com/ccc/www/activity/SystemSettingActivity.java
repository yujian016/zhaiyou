package com.ccc.www.activity;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.UserUtil;
import com.ccc.www.util.Xutils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class SystemSettingActivity extends BaseActivity {

	ImageButton ib_digital_goback;
	TextView activity_systemsetting_currentversion;

	LinearLayout activity_systemsetting_clear;
	LinearLayout activity_systemsetting_aboutus;

	LinearLayout activity_systemsetting_currentversion_layout;

	View rootview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		rootview = LayoutInflater.from(this).inflate(
				R.layout.activity_systemsetting, null);

		setContentView(R.layout.activity_systemsetting);
		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_digital_goback:
			SystemSettingActivity.this.finish();
			break;
		case R.id.activity_systemsetting_clear:
			String path1;
			String path2;

			if (Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				path1 = "/mnt/sdcard/lts_images";
				path2 = "/mnt/sdcard/lts_images1";
			} else {
				path1 = getApplicationContext().getFilesDir().getPath()
						+ "/images";
				path2 = getApplicationContext().getFilesDir().getPath()
						+ "/images1";
			}

			File file1 = new File(path1);
			File file2 = new File(path2);
			DeleteFile(file1);
			DeleteFile(file2);

			showToast("清理缓存完成");

			break;
		case R.id.activity_systemsetting_aboutus:
			Intent aboutus = new Intent();
			aboutus.setClass(SystemSettingActivity.this, AboutActivity.class);
			startActivity(aboutus);
			break;
		case R.id.activity_systemsetting_currentversion_layout:
			checkUpdate();
			break;

		default:
			break;
		}
	}

	/**
	 * 检查更新
	 */
	private void checkUpdate() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在检查更新");

		String url = GlobalConstant.CheckUpdate_URL;

		Log.v(TAG, "url  " + url);

		Xutils.loadData(HttpMethod.POST, url, null,
				new RequestCallBack<String>() {
					public void onSuccess(ResponseInfo<String> response) {
						dismissProgressDialog();
						String returnstr = response.result;
						Log.v(TAG, "checkUpdate  returnstr	 " + returnstr);
						if (TextUtils.isEmpty(returnstr)) {
							showToast("当前已经是最新版本");
						} else {
							try {
								JSONObject json = new JSONObject(returnstr);
								int version_code = json.getInt("version_code");
								String version_name = json
										.getString("version_name");
								String down_load_url = json
										.getString("down_load_url");
								String update_date = json
										.getString("update_date");
								String update_context = json
										.getString("update_context");
								try {
									int versionCode = UserUtil
											.getVersionCode(SystemSettingActivity.this);

									if (version_code > versionCode) {
										// 有新版本
										popnewversion(version_name,
												down_load_url, update_date,
												update_context);
									} else {
										showToast("当前已经是最新版本");
									}
								} catch (Exception e) {
									e.printStackTrace();
									showToast("当前已经是最新版本");
								}
							} catch (JSONException e) {
								e.printStackTrace();
								showToast("当前已经是最新版本");
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("当前已经是最新版本");
					}
				});
	}

	private void popnewversion(String version_name, final String down_load_url,
			String update_date, String update_context) {

		View view2 = LayoutInflater.from(SystemSettingActivity.this).inflate(
				R.layout.pop_versionupdate, null);
		mPopupWindow = new PopupWindow(view2, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(false);

		TextView pop_versionupdate_close = (TextView) view2
				.findViewById(R.id.pop_versionupdate_close);
		TextView pop_versionupdate_tip = (TextView) view2
				.findViewById(R.id.pop_versionupdate_tip);
		TextView pop_versionupdate_download = (TextView) view2
				.findViewById(R.id.pop_versionupdate_download);

		pop_versionupdate_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dissmisspopwindow();
			}
		});

		if (update_context.contains("-")) {
			String tempstr = "";
			String[] sz = update_context.split("-");
			for (int i = 0; i < sz.length; i++) {
				if (i == 0) {
					tempstr = sz[i];
				} else {
					tempstr = tempstr + "\n" + sz[i];
				}
			}
			update_context = tempstr;
		}
		
		Log.v(TAG, "update_context		..	" + update_context);

		pop_versionupdate_tip.setText(update_context);

		pop_versionupdate_download.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dissmisspopwindow();

				String savepath = Environment.getExternalStorageDirectory()
						+ File.separator + "zhaiyou.apk";

				// String path =
				// "http://121.14.31.216:8080/mobser-gz/apps/android/gzrst_v1.02.apk";

				File file = new File(savepath);

				if (file.exists()) {
					file.delete();
				}

				String newdown_load_url = down_load_url;

				if (!newdown_load_url.startsWith("http")) {
					newdown_load_url = "http://" + newdown_load_url;
				}

				Log.v(TAG, "newdown_load_url	" + newdown_load_url);

				HttpUtils http = new HttpUtils();
				http.download(newdown_load_url, savepath, true, true,
						new RequestCallBack<File>() {

							@Override
							public void onStart() {
								showLoading2("正在下载，请稍候");
							}

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								// showLoading2("正在下载中..." + current + "/" +
								// total);
								// showLoading2("正在下载，请稍后");
							}

							@Override
							public void onFailure(HttpException error,
									String msg) {
								dismissProgressDialog();
								showToast("下载失败");
							}

							@Override
							public void onSuccess(
									ResponseInfo<File> responseInfo) {
								dismissProgressDialog();
								openFile(new File(responseInfo.result.getPath()));
							}
						});
			}
		});

		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
		mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
	}

	// 打开APK程序代码
	private void openFile(File file) {
		Log.e("OpenFile", file.getName());
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	PopupWindow mPopupWindow;

	private void dissmisspopwindow() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findviewWithId() {
		ib_digital_goback = (ImageButton) findViewById(R.id.ib_digital_goback);

		activity_systemsetting_clear = (LinearLayout) findViewById(R.id.activity_systemsetting_clear);
		activity_systemsetting_aboutus = (LinearLayout) findViewById(R.id.activity_systemsetting_aboutus);
		activity_systemsetting_currentversion_layout = (LinearLayout) findViewById(R.id.activity_systemsetting_currentversion_layout);

		activity_systemsetting_currentversion = (TextView) findViewById(R.id.activity_systemsetting_currentversion);
	}

	@Override
	public void initListener() {
		ib_digital_goback.setOnClickListener(this);
		activity_systemsetting_clear.setOnClickListener(this);
		activity_systemsetting_aboutus.setOnClickListener(this);
		activity_systemsetting_currentversion_layout.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		try {
			String versionname = UserUtil.getVersionName(this).toUpperCase();
			if (versionname.contains("v") || versionname.contains("V")) {
				activity_systemsetting_currentversion.setText(versionname);
			} else {
				activity_systemsetting_currentversion
						.setText("V" + versionname);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void DeleteFile(File file) {
		if (file.exists() == false) {
			return;
		} else {
			if (file.isFile()) {
				file.delete();
				return;
			}
			if (file.isDirectory()) {
				File[] childFile = file.listFiles();
				if (childFile == null || childFile.length == 0) {
					file.delete();
					return;
				}
				for (File f : childFile) {
					DeleteFile(f);
				}
				file.delete();
			}
		}
	}

}
