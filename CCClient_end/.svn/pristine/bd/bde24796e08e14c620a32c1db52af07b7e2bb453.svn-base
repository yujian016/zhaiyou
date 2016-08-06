package com.ccc.www.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.ShopCategoryBean;
import com.ccc.www.util.FileUtil;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.GlobalSaveFileName;
import com.ccc.www.util.PhotoUtil;
import com.ccc.www.view.SelectPicPopuWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ApplyOpenShopActivity extends BaseActivity {

	String TAG = "ApplyOpenShopActivity";

	private ImageButton ib_apply_shop_close;
	private EditText et_apply_shop_name;
	private EditText et_apply_shop_info;
	private Spinner apply_shop_spinner;
	private ImageView iv_apply_select_shop_log;
	private Button btn_apply_open_shop;
	private String shopName;
	private String shopInfo;

	SelectPicPopuWindow menuWindow;
	private static final int TAKE_PIC = 200;
	private static final int TAKE_PHOTO = 100;
	private File file;
	private HttpUtils http;
	final List<String> spinnerData = new ArrayList<String>();
	private int spinnerPosition;
	private Gson gson = new Gson();
	private List<ShopCategoryBean> cates;
	private EditText et_input_idcard_no;
	private EditText et_input_stucard_no;
	private EditText et_input_stu_class;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_open_shop);
		initview();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void findviewWithId() {
		ib_apply_shop_close = (ImageButton) findViewById(R.id.ib_apply_shop_close);
		et_apply_shop_name = (EditText) findViewById(R.id.et_apply_shop_name);
		et_apply_shop_info = (EditText) findViewById(R.id.et_apply_shop_info);
		et_input_idcard_no = (EditText) findViewById(R.id.et_input_idcard_no);
		et_input_stucard_no = (EditText) findViewById(R.id.et_input_stucard_no);
		et_input_stu_class = (EditText) findViewById(R.id.et_input_stu_class);
		apply_shop_spinner = (Spinner) findViewById(R.id.apply_shop_spinner);
		iv_apply_select_shop_log = (ImageView) findViewById(R.id.iv_apply_select_shop_log);
		btn_apply_open_shop = (Button) findViewById(R.id.btn_apply_open_shop);
	}

	@Override
	public void initListener() {
		ib_apply_shop_close.setOnClickListener(this);
		btn_apply_open_shop.setOnClickListener(this);
		iv_apply_select_shop_log.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_apply_shop_close:
			ApplyOpenShopActivity.this.finish();
			break;
		case R.id.btn_apply_open_shop:
			if (isEmpty()) {
				httpUtilsSend();
			}
			break;
		case R.id.iv_apply_select_shop_log:
			menuWindow = new SelectPicPopuWindow(ApplyOpenShopActivity.this,
					itemsOnClick);
			menuWindow.showAtLocation(
					ApplyOpenShopActivity.this.findViewById(R.id.ll_apply),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		default:
			break;
		}
	}

	private void httpUtilsSend() {
		showLoading2("正在提交");
		http = new HttpUtils();
		RequestParams params = new RequestParams();

		SharedPreferences sp = getSharedPreferences("login_session",
				Context.MODE_PRIVATE);
		String loginId = String.valueOf(sp.getInt("login_id", 0));

		String category_id = String.valueOf(cates.get(spinnerPosition).getId());
		String shop_name = et_apply_shop_name.getText().toString().trim();
		String shop_info = et_apply_shop_info.getText().toString().trim();

		String idcard_no = et_input_idcard_no.getText().toString().trim();
		String stucard_no = et_input_stucard_no.getText().toString().trim();
		String stu_class = et_input_stu_class.getText().toString().trim();

		params.addBodyParameter("shopImageFile", file);
		params.addBodyParameter("category_id", category_id);
		params.addBodyParameter("shop_name", shop_name);
		params.addBodyParameter("shop_info", shop_info);
		params.addBodyParameter("user_id", loginId);

		params.addBodyParameter("idcard_no", idcard_no);
		params.addBodyParameter("stucard_no", stucard_no);
		params.addBodyParameter("stu_class", stu_class);

		http.send(HttpMethod.POST, GlobalConstant.SHOP_ACTION_OPEN_SHOP,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException e, String msg) {
						dismissProgressDialog();
						Toast.makeText(ApplyOpenShopActivity.this, msg,
								Toast.LENGTH_SHORT).show();
					}

					@SuppressLint("NewApi")
					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String returnResult = info.result;

						Log.v(TAG, "returnResult  " + returnResult);

						try {
							JSONObject object = new JSONObject(returnResult);
							String code = object.getString("resultCode");
							String msg = object.getString("resultMsg");
							if (Integer.parseInt(code) > 0) {
								Dialog alertDialog = new AlertDialog.Builder(
										ApplyOpenShopActivity.this, 1)
										.setTitle("提示")
										.setMessage(msg)
										.setPositiveButton(
												"确定",
												new DialogInterface.OnClickListener() {
													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														// 跳转
														Intent intent = new Intent(
																ApplyOpenShopActivity.this,
																MyOpenShopActivity.class);
														startActivity(intent);
														finish();
													}
												}).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	private boolean isEmpty() {
		shopName = et_apply_shop_name.getText().toString().trim();
		shopInfo = et_apply_shop_info.getText().toString().trim();

		String idcard_no = et_input_idcard_no.getText().toString().trim();
		String stucard_no = et_input_stucard_no.getText().toString().trim();
		String stu_class = et_input_stu_class.getText().toString().trim();

		boolean isOk = false;
		if (shopName.length() == 0) {
			Toast.makeText(this, "输入店铺名称", Toast.LENGTH_SHORT).show();
		} else if (shopInfo.length() == 0) {
			Toast.makeText(this, "店铺简介不能为空", Toast.LENGTH_SHORT).show();
		} else if (file == null) {
			Toast.makeText(this, "选择作为店招的图片", Toast.LENGTH_SHORT).show();
		} else if (idcard_no.length() < 15 || idcard_no.length() > 18) {
			this.showToast("身份真号码不正确");
		} else if (stucard_no.length() == 0) {
			this.showToast("学生证号码不正确");
		} else if (stu_class.length() == 0) {
			this.showToast("输入班级");
		} else {
			isOk = true;
		}
		return isOk;
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
		case 100:
			if (resultCode == RESULT_OK) {
				bitmap = PhotoUtil.CompresPhoto(templogobitmappath); // 根据刚刚保存的文件,进行图片压缩

				file = FileUtil.BitMapToFile(bitmap,
						GlobalSaveFileName.APPLY_SHOP_TAKE_PIC);
				iv_apply_select_shop_log.setImageBitmap(bitmap);
				iv_apply_select_shop_log.setScaleType(ScaleType.FIT_XY);

			}

			break;
		case 200:
			if (resultCode == RESULT_OK) {
				Uri takePicUri = intent.getData();
				try {
					bitmap = MediaStore.Images.Media.getBitmap(resolve,
							takePicUri);

					bitmap = PhotoUtil.CompresPhoto_bitmap(bitmap);

					file = FileUtil.BitMapToFile(bitmap,
							GlobalSaveFileName.APPLY_SHOP_TAKE_PIC);
					iv_apply_select_shop_log.setImageBitmap(bitmap);
					iv_apply_select_shop_log.setScaleType(ScaleType.FIT_XY);

					// uploadimage(file.getAbsolutePath());

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
	public void initdata() {
		getDataWithHttp();
		apply_shop_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						spinnerPosition = position;
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						spinnerPosition = 0;
					}
				});
	}

	private void getDataWithHttp() {
		http = new HttpUtils();
		http.send(HttpMethod.GET, GlobalConstant.SHOP_ACTION_SHOP_CATEGORY,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException e, String msg) {
						Toast.makeText(ApplyOpenShopActivity.this, msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> json) {
						String ss_result = json.result;
						Log.v(TAG, ss_result);
						cates = gson.fromJson(ss_result,
								new TypeToken<List<ShopCategoryBean>>() {
								}.getType());
						for (ShopCategoryBean scb : cates) {
							spinnerData.add(scb.getShop_category());
						}
						ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
								ApplyOpenShopActivity.this,
								R.layout.shop_category_spinner_item,
								spinnerData);
						apply_shop_spinner.setAdapter(spinnerAdapter);
					}
				});
	}

	void uploadimage(String path) {

		showLoading2("正在上传图片");

		Log.v(TAG, "path  " + path);

		// QiNiuUpLoadUtils.upLoadToQiuNiu1(path, new QiNiuUploadInterface() {
		// @Override
		// public void returnkey(String key) {
		//
		// Log.v(TAG, "key  " + key);
		//
		// dismissProgressDialog();
		//
		// }
		// });
	}

}
