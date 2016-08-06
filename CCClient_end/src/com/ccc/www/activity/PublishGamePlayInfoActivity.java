package com.ccc.www.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ccc.ccclient_end.R;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.CheckUtil;
import com.ccc.www.util.FileUtil;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.PhotoUtil;
import com.ccc.www.util.UserUtil;
import com.ccc.www.view.SelectPicPopuWindow;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class PublishGamePlayInfoActivity extends BaseActivity {

	String TAG = "PublishSecondaryInfoActivity";

	ImageButton ib_pub_second_close;
	EditText et_second_info_name;
	EditText et_second_shop_info;
	ImageView iv_seond_upload_log1;
	ImageView iv_seond_upload_log2;
	ImageView iv_seond_upload_log3;
	EditText et_input_phone_price;
	EditText et_input_phone_no;
	Button btn_publish_second_info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_gameplay_info);
		initview();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void findviewWithId() {
		ib_pub_second_close = (ImageButton) findViewById(R.id.ib_pub_second_close);
		et_second_info_name = (EditText) findViewById(R.id.et_second_info_name);
		et_second_shop_info = (EditText) findViewById(R.id.et_second_shop_info);
		iv_seond_upload_log1 = (ImageView) findViewById(R.id.iv_seond_upload_log1);
		iv_seond_upload_log2 = (ImageView) findViewById(R.id.iv_seond_upload_log2);
		iv_seond_upload_log3 = (ImageView) findViewById(R.id.iv_seond_upload_log3);
		et_input_phone_price = (EditText) findViewById(R.id.et_input_phone_price);
		et_input_phone_no = (EditText) findViewById(R.id.et_input_phone_no);
		btn_publish_second_info = (Button) findViewById(R.id.btn_publish_second_info);
	}

	@Override
	public void initListener() {
		ib_pub_second_close.setOnClickListener(this);
		iv_seond_upload_log1.setOnClickListener(this);
		iv_seond_upload_log2.setOnClickListener(this);
		iv_seond_upload_log3.setOnClickListener(this);
		btn_publish_second_info.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_pub_second_close:
			PublishGamePlayInfoActivity.this.finish();
			break;
		case R.id.iv_seond_upload_log1:
			choicepic = 1;
			choicepic();
			break;
		case R.id.iv_seond_upload_log2:
			choicepic = 2;
			choicepic();
			break;
		case R.id.iv_seond_upload_log3:
			choicepic = 3;
			choicepic();
			break;
		case R.id.btn_publish_second_info:
			submitinfo();
			break;

		default:
			break;
		}
	}

	@Override
	public void initdata() {
		// TODO Auto-generated method stub

	}

	int choicepic = 1;

	private File file1;
	private File file2;
	private File file3;
	SelectPicPopuWindow menuWindow;
	private static final int TAKE_PIC = 200;
	private static final int TAKE_PHOTO = 100;

	private void choicepic() {
		menuWindow = new SelectPicPopuWindow(PublishGamePlayInfoActivity.this,
				itemsOnClick);
		menuWindow.showAtLocation(
				PublishGamePlayInfoActivity.this.findViewById(R.id.ll_apply),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	String templogobitmappath = "";
	private OnClickListener itemsOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_take_photo:
				// Intent photoIntent = new Intent(
				// MediaStore.ACTION_IMAGE_CAPTURE, null);
				// startActivityForResult(photoIntent, TAKE_PHOTO);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 文件夹aaaa
				String path = Environment.getExternalStorageDirectory()
						.toString() + "/zhaiyou";
				File path1 = new File(path);
				if (!path1.exists()) {
					path1.mkdirs();
				}
				File file = new File(path1, System.currentTimeMillis() + ".png");
				templogobitmappath = file.getAbsolutePath();
				Uri mOutPutFileUri = Uri.fromFile(file);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
				startActivityForResult(intent, TAKE_PHOTO);
				menuWindow.dismiss();
				break;
			case R.id.btn_take_pic:
				Intent picIntent = new Intent(Intent.ACTION_GET_CONTENT);
				picIntent.setType("image/*");
				startActivityForResult(picIntent, TAKE_PIC);
				menuWindow.dismiss();
				break;
			default:
				break;
			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		ContentResolver resolve = getContentResolver();
		Bitmap bitmap;
		switch (requestCode) {
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				bitmap = PhotoUtil.CompresPhoto(templogobitmappath); // 根据刚刚保存的文件,进行图片压缩

				if (choicepic == 1) {
					file1 = FileUtil.BitMapToFile(bitmap,
							"file" + new Date().getTime() + ".png");
					iv_seond_upload_log1.setImageBitmap(bitmap);
				} else if (choicepic == 2) {
					file2 = FileUtil.BitMapToFile(bitmap,
							"file" + new Date().getTime() + ".png");
					iv_seond_upload_log2.setImageBitmap(bitmap);
				} else if (choicepic == 3) {
					file3 = FileUtil.BitMapToFile(bitmap,
							"file" + new Date().getTime() + ".png");
					iv_seond_upload_log3.setImageBitmap(bitmap);
				}

			}

			break;
		case TAKE_PIC:
			if (resultCode == RESULT_OK) {
				Uri takePicUri = intent.getData();
				try {
					bitmap = MediaStore.Images.Media.getBitmap(resolve,
							takePicUri);

					Bitmap newbitmap = PhotoUtil.CompresPhoto_bitmap(bitmap);

					if (choicepic == 1) {
						file1 = FileUtil.BitMapToFile(newbitmap, "file"
								+ new Date().getTime() + ".png");

						iv_seond_upload_log1.setImageBitmap(newbitmap);

					} else if (choicepic == 2) {
						file2 = FileUtil.BitMapToFile(newbitmap, "file"
								+ new Date().getTime() + ".png");
						iv_seond_upload_log2.setImageBitmap(newbitmap);
					} else if (choicepic == 3) {
						file3 = FileUtil.BitMapToFile(newbitmap, "file"
								+ new Date().getTime() + ".png");
						iv_seond_upload_log3.setImageBitmap(newbitmap);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				return;
			}
			break;
		default:
			break;
		}
	}

	private void submitinfo() {
		String user_id = String.valueOf(UserUtil.getuserid(this));
		String title = et_second_info_name.getText().toString().trim();
		String price = et_input_phone_price.getText().toString().trim();
		String detail = et_second_shop_info.getText().toString().trim();
		String phone = et_input_phone_no.getText().toString().trim();

		if (TextUtils.isEmpty(title)) {
			showToast("请输入标题");
			et_second_info_name.requestFocus();
			return;
		}

		if (TextUtils.isEmpty(detail)) {
			showToast("请输入描述");
			et_second_shop_info.requestFocus();
			return;
		}

		if (file1 == null || file2 == null || file3 == null) {
			showToast("请上传三张图片");
		}

		if (TextUtils.isEmpty(price)) {
			showToast("请输入价格");
			et_input_phone_price.requestFocus();
			return;
		} else {
			boolean check = CheckUtil.isIntOrFloat(price);
			if (!check) {
				showToast("请输入正确格式的价格");
				et_input_phone_price.requestFocus();
				return;
			}
		}

		if (TextUtils.isEmpty(phone)) {
			showToast("请输入联系手机号码");
			et_input_phone_no.requestFocus();
			return;
		} else {
			boolean check = CheckUtil.isMobileNO(phone);
			if (!check) {
				showToast("请输入正确格式的手机号码");
				et_input_phone_no.requestFocus();
				return;
			}
		}

		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在提交");

		try {
			title = new String(title.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			detail = new String(detail.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", user_id);
		params.addBodyParameter("title", title);
		params.addBodyParameter("price", price);
		params.addBodyParameter("detail", detail);
		params.addBodyParameter("phone", phone);

		params.addBodyParameter("file1", file1);
		params.addBodyParameter("file2", file2);
		params.addBodyParameter("file3", file3);

		Log.v(TAG, "user_id  " + user_id);
		Log.v(TAG, "title  " + title);
		Log.v(TAG, "price  " + price);
		Log.v(TAG, "detail  " + detail);
		Log.v(TAG, "phone  " + phone);

		Log.v(TAG, "file1   " + new File(file1.getAbsolutePath()));
		Log.v(TAG, "file2   " + new File(file2.getAbsolutePath()));
		Log.v(TAG, "file3   " + new File(file3.getAbsolutePath()));

		loadData(HttpMethod.POST, GlobalConstant.ADD_SECOND_HAND_GOOD, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {

						Log.v(TAG, "onFailure  " + arg1);

						dismissProgressDialog();
						showToast("提交失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String jsonStr = info.result;
						Log.v(TAG, "jsonStr " + jsonStr);
						if (BaseUtils.isEmpty(jsonStr)) {
							showToast("提交失败");
						} else {
							try {
								JSONObject json = new JSONObject(jsonStr);
								final int resultCode = json
										.getInt("resultCode");
								String resultMsg = json.getString("resultMsg");

								AlertDialog.Builder build = new Builder(
										PublishGamePlayInfoActivity.this);
								build.setTitle("提示");
								build.setMessage(resultMsg);
								build.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												if (resultCode == 1) {

													PublishGamePlayInfoActivity.this
															.finish();
												}
											}
										});
								build.show();
							} catch (JSONException e) {
								showToast("提交失败");
								e.printStackTrace();
							}
						}
					}
				});

	}

}
