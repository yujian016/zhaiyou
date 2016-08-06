package com.ccc.www.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.ccc.ccclient_end.R;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.CheckUtil;
import com.ccc.www.util.FileUtil;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.GlobalSaveFileName;
import com.ccc.www.util.PhotoUtil;
import com.ccc.www.util.UserUtil;
import com.ccc.www.view.SelectPicPopuWindow;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class SendCouponActivity extends BaseActivity {

	String TAG = "SendCouponActivity";

	ImageButton ib_coupon_close;
	EditText et_coupon_price;
	EditText et_coupon_num;
	EditText et_coupon_info;
	ImageView iv_coupon_upload_log;
	Button btn_publish_second_info;

	EditText et_use_coupon_money;

	View rootview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_coupon_info);

		rootview = LayoutInflater.from(this).inflate(
				R.layout.activity_send_coupon_info, null);

		initview();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.ib_coupon_close:
			SendCouponActivity.this.finish();
			break;
		case R.id.btn_publish_second_info:
			sendinfo();
			break;
		case R.id.iv_coupon_upload_log:
			menuWindow = new SelectPicPopuWindow(SendCouponActivity.this,
					itemsOnClick);
			menuWindow.showAtLocation(rootview, Gravity.BOTTOM
					| Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		default:
			break;
		}
	}

	String templogobitmappath = "";
	SelectPicPopuWindow menuWindow;
	private static final int TAKE_PIC = 200;
	private static final int TAKE_PHOTO = 100;
	private File file;
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
		case 100:

			if (resultCode == RESULT_OK) {
				bitmap = PhotoUtil.CompresPhoto(templogobitmappath); // 根据刚刚保存的文件,进行图片压缩

				file = FileUtil.BitMapToFile(bitmap,
						GlobalSaveFileName.APPLY_SHOP_TAKE_PIC);
				iv_coupon_upload_log.setImageBitmap(bitmap);
				iv_coupon_upload_log.setScaleType(ScaleType.FIT_XY);

			}

			break;
		case 200:
			if (resultCode == RESULT_OK) {
				Uri takePicUri = intent.getData();
				try {
					bitmap = MediaStore.Images.Media.getBitmap(resolve,
							takePicUri);

					Bitmap newbitmap = PhotoUtil.CompresPhoto_bitmap(bitmap);

					file = FileUtil.BitMapToFile(newbitmap,
							GlobalSaveFileName.APPLY_SHOP_TAKE_PIC);
					iv_coupon_upload_log.setImageBitmap(bitmap);
					iv_coupon_upload_log.setScaleType(ScaleType.FIT_XY);

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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public void findviewWithId() {
		ib_coupon_close = (ImageButton) findViewById(R.id.ib_coupon_close);
		et_coupon_price = (EditText) findViewById(R.id.et_coupon_price);
		et_coupon_num = (EditText) findViewById(R.id.et_coupon_num);
		et_coupon_info = (EditText) findViewById(R.id.et_coupon_info);
		et_use_coupon_money = (EditText) findViewById(R.id.et_use_coupon_money);
		iv_coupon_upload_log = (ImageView) findViewById(R.id.iv_coupon_upload_log);
		btn_publish_second_info = (Button) findViewById(R.id.btn_publish_second_info);
	}

	@Override
	public void initListener() {
		ib_coupon_close.setOnClickListener(this);
		btn_publish_second_info.setOnClickListener(this);
		iv_coupon_upload_log.setOnClickListener(this);
	}

	@Override
	public void initdata() {

	}

	private void sendinfo() {
		// TODO Auto-generated method stub

		String coupon_price = et_coupon_price.getText().toString().trim();
		String coupon_num = et_coupon_num.getText().toString().trim();
		String detail = et_coupon_info.getText().toString().trim();
		String use_coupon_money = et_use_coupon_money.getText().toString()
				.trim();

		if (TextUtils.isEmpty(coupon_price)) {
			showToast("请输入优惠券金额");
			et_coupon_price.requestFocus();
			return;
		} else {
			if (!CheckUtil.isIntOrFloat(coupon_price)) {
				showToast("请输入正确格式的优惠券金额");
				et_coupon_price.requestFocus();
				return;
			}
		}

		if (TextUtils.isEmpty(coupon_num)) {
			showToast("请输入优惠券数量");
			et_coupon_num.requestFocus();
			return;
		} else {
			if (!CheckUtil.isInt(coupon_num)) {
				showToast("请输入正确格式的优惠券数量");
				et_coupon_num.requestFocus();
				return;
			}
		}

		if (TextUtils.isEmpty(detail)) {
			showToast("请输入优惠券信息");
			et_coupon_info.requestFocus();
			return;
		}

		if (file == null) {
			showToast("请添加封面");
			return;
		}

		if (TextUtils.isEmpty(use_coupon_money)) {
			use_coupon_money = "0";
		} else {
			if (!CheckUtil.isInt(coupon_price)) {
				showToast("请输入整数的优惠券使用限制");
				et_use_coupon_money.requestFocus();
				return;
			}
		}

		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		getshopid(coupon_price, coupon_num, detail, use_coupon_money);

	}

	private void getshopid(final String coupon_price, final String coupon_num,
			final String detail, final String use_coupon_money) {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在获取店铺信息");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id",
				String.valueOf(UserUtil.getuserid(SendCouponActivity.this)));

		loadData(HttpMethod.POST, GlobalConstant.SHOP_ACTION_IS_OPEN_SHOP,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("获取失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {

						dismissProgressDialog();
						String jsonstr = info.result;
						Log.v(TAG, jsonstr);
						if (BaseUtils.isEmpty(jsonstr)) {
							showToast("获取失败");
						} else {
							try {
								JSONObject json = new JSONObject(jsonstr);
								System.out.println(json);
								int code = json.getInt("resultCode");
								int shopid = json.getInt("beanId");
								if (code == 0) {
									// 申请开通店铺 1.审核通过 2.审核未通过
									showToast("您还未申请开店");
								} else if (code == 2) {
									// 审核未通过
									showToast("您的店铺暂未审核通过");
								} else if (code == 1) {
									// 审核通过

									add(coupon_price, coupon_num, detail,
											shopid, use_coupon_money);

								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	private void add(String coupon_price, String coupon_num, String detail,
			int shop_id, String use_coupon_money) {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在发布优惠券");

		RequestParams params = new RequestParams();
		params.addBodyParameter("coupon_price", coupon_price);
		params.addBodyParameter("coupon_num", coupon_num);
		params.addBodyParameter("coupon_log", file);
		params.addBodyParameter("detail", detail);
		params.addBodyParameter("shop_id", shop_id + "");
		params.addBodyParameter("use_coupon_money", use_coupon_money);
		loadData(HttpMethod.POST, GlobalConstant.ADD_COUPON, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("发布失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {

						dismissProgressDialog();
						String json = info.result;
						Log.v(TAG, json);
						if (BaseUtils.isEmpty(json)) {
							showToast("发布失败");
						} else {
							try {
								JSONObject jsonob = new JSONObject(json);
								String resultMsg = jsonob
										.getString("resultMsg");
								showToast(resultMsg);

								int resultCode = jsonob.getInt("resultCode");

								if (resultCode == 1) {
									SendCouponActivity.this.finish();
								}

							} catch (JSONException e) {
								showToast("发布失败");
								e.printStackTrace();
							}

						}
					}
				});

	}

}
