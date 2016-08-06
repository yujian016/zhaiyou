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
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Spinner;
import android.widget.Toast;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.DigitalCategoryBean;
import com.ccc.www.bean.ShopBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.CheckUtil;
import com.ccc.www.util.FileUtil;
import com.ccc.www.util.GlobalConstant;
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

public class UploadGoodsActivity extends BaseActivity {

	String TAG = "UploadGoodsActivity";

	private ImageButton ib_upload_goods_close;
	private ImageView iv_upload_goods_log1;
	private ImageView iv_upload_goods_log2;
	private ImageView iv_upload_goods_log3;

	private ImageView iv_upload_goods_d1;
	private ImageView iv_upload_goods_d2;
	private ImageView iv_upload_goods_d3;
	private ImageView iv_upload_goods_d4;
	private ImageView iv_upload_goods_d5;
	private ImageView iv_upload_goods_d6;

	private EditText et_upload_goods_name1;
	private EditText et_upload_goods_num1;
	private EditText et_upload_goods_price1;
	private EditText et_upload_goods_goods_tab;
	private EditText et_upload_goods_detail1;
	private Button btn_upload_goods;
	private int shopId;
	private int userId;
	protected final int TAKE_PHOTO = 100;
	protected final int TAKE_PIC = 200;
	private SelectPicPopuWindow picMenu;

	File files1;
	File files2;
	File files3;

	File d_files1;
	File d_files2;
	File d_files3;
	File d_files4;
	File d_files5;
	File d_files6;

	private HttpUtils httpUtils;
	private List<DigitalCategoryBean> cates;
	final List<String> spinnerData = new ArrayList<String>();
	private Gson gson;

	private int isDigital = 0;// 是不是属于数码产品
	private int index = 0;// 选择spinner的下标

	private File file;
	private Spinner upload_goods_spinner;

	CheckBox checkbox_goods_kd1;
	CheckBox checkbox_goods_kd2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_goods);
		initview();
	}

	int imgtype = 0;
	int d_imgtype = 0;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_upload_goods_close:
			finish();
			break;
		case R.id.btn_upload_goods:
			uploadGoods(shopId, userId);
			break;
		case R.id.iv_upload_goods_log1:
			imgtype = 1;
			d_imgtype = 0;
			picMenu = new SelectPicPopuWindow(UploadGoodsActivity.this,
					itemOnClick);
			picMenu.setType(1);
			picMenu.showAtLocation(
					UploadGoodsActivity.this.findViewById(R.id.ll_upload_goods),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.iv_upload_goods_log2:
			imgtype = 2;
			d_imgtype = 0;
			picMenu = new SelectPicPopuWindow(UploadGoodsActivity.this,
					itemOnClick);
			picMenu.setType(2);
			picMenu.showAtLocation(
					UploadGoodsActivity.this.findViewById(R.id.ll_upload_goods),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.iv_upload_goods_log3:
			imgtype = 3;
			d_imgtype = 0;
			picMenu = new SelectPicPopuWindow(UploadGoodsActivity.this,
					itemOnClick);
			picMenu.setType(3);
			picMenu.showAtLocation(
					UploadGoodsActivity.this.findViewById(R.id.ll_upload_goods),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.iv_upload_goods_d1:// 上传商品详情
			d_imgtype = 1;
			imgtype = 0;
			picMenu = new SelectPicPopuWindow(UploadGoodsActivity.this,
					itemOnClick);
			picMenu.setType(1);
			picMenu.showAtLocation(
					UploadGoodsActivity.this.findViewById(R.id.ll_upload_goods),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;

		case R.id.iv_upload_goods_d2:// 上传商品详情
			d_imgtype = 2;
			imgtype = 0;
			picMenu = new SelectPicPopuWindow(UploadGoodsActivity.this,
					itemOnClick);
			picMenu.setType(2);
			picMenu.showAtLocation(
					UploadGoodsActivity.this.findViewById(R.id.ll_upload_goods),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;

		case R.id.iv_upload_goods_d3:// 上传商品详情
			d_imgtype = 3;
			imgtype = 0;
			picMenu = new SelectPicPopuWindow(UploadGoodsActivity.this,
					itemOnClick);
			picMenu.setType(3);
			picMenu.showAtLocation(
					UploadGoodsActivity.this.findViewById(R.id.ll_upload_goods),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;

		case R.id.iv_upload_goods_d4:// 上传商品详情
			d_imgtype = 4;
			imgtype = 0;
			picMenu = new SelectPicPopuWindow(UploadGoodsActivity.this,
					itemOnClick);
			picMenu.setType(4);
			picMenu.showAtLocation(
					UploadGoodsActivity.this.findViewById(R.id.ll_upload_goods),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;

		case R.id.iv_upload_goods_d5:// 上传商品详情
			d_imgtype = 5;
			imgtype = 0;
			picMenu = new SelectPicPopuWindow(UploadGoodsActivity.this,
					itemOnClick);
			picMenu.setType(5);
			picMenu.showAtLocation(
					UploadGoodsActivity.this.findViewById(R.id.ll_upload_goods),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;

		case R.id.iv_upload_goods_d6:// 上传商品详情
			d_imgtype = 6;
			imgtype = 0;
			picMenu = new SelectPicPopuWindow(UploadGoodsActivity.this,
					itemOnClick);
			picMenu.setType(6);
			picMenu.showAtLocation(
					UploadGoodsActivity.this.findViewById(R.id.ll_upload_goods),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;

		default:
			break;
		}
	}

	String templogobitmappath1 = "";
	String templogobitmappath2 = "";
	String templogobitmappath3 = "";

	String templogobitmappath_d1 = "";
	String templogobitmappath_d2 = "";
	String templogobitmappath_d3 = "";
	String templogobitmappath_d4 = "";
	String templogobitmappath_d5 = "";
	String templogobitmappath_d6 = "";

	private OnClickListener itemOnClick = new OnClickListener() {
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

				File file = new File(path1, System.currentTimeMillis() + "_"
						+ picMenu.getType() + ".png");

				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if (imgtype == 1) {
					templogobitmappath1 = file.getAbsolutePath();
				} else if (imgtype == 2) {
					templogobitmappath2 = file.getAbsolutePath();
				} else if (imgtype == 3) {
					templogobitmappath3 = file.getAbsolutePath();
				} else if (d_imgtype == 1) {
					templogobitmappath_d1 = file.getAbsolutePath();
				} else if (d_imgtype == 2) {
					templogobitmappath_d2 = file.getAbsolutePath();
				} else if (d_imgtype == 3) {
					templogobitmappath_d3 = file.getAbsolutePath();
				} else if (d_imgtype == 4) {
					templogobitmappath_d4 = file.getAbsolutePath();
				} else if (d_imgtype == 5) {
					templogobitmappath_d5 = file.getAbsolutePath();
				} else if (d_imgtype == 6) {
					templogobitmappath_d6 = file.getAbsolutePath();
				}

				Uri mOutPutFileUri = Uri.fromFile(file);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
				startActivityForResult(intent, TAKE_PHOTO);

				picMenu.dismiss();

				break;
			case R.id.btn_take_pic:
				Intent picIntent = new Intent(Intent.ACTION_GET_CONTENT);
				picIntent.setType("image/*");
				startActivityForResult(picIntent, TAKE_PIC);
				picMenu.dismiss();
				break;
			default:
				break;
			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		ContentResolver cr = getContentResolver();
		Bitmap bitMap = null;
		switch (requestCode) {
		case 100:
			// 从相机选择
			if (resultCode == RESULT_OK) {

				Log.v(TAG, "templogobitmappath1  " + templogobitmappath1);
				Log.v(TAG, "templogobitmappath2  " + templogobitmappath2);
				Log.v(TAG, "templogobitmappath3  " + templogobitmappath3);

				if (imgtype == 1) {
					bitMap = PhotoUtil.CompresPhoto(templogobitmappath1);
				} else if (imgtype == 2) {
					bitMap = PhotoUtil.CompresPhoto(templogobitmappath2);
				} else if (imgtype == 3) {
					bitMap = PhotoUtil.CompresPhoto(templogobitmappath3);
				} else if (d_imgtype == 1) {
					bitMap = PhotoUtil.CompresPhoto(templogobitmappath_d1);
				} else if (d_imgtype == 2) {
					bitMap = PhotoUtil.CompresPhoto(templogobitmappath_d2);
				} else if (d_imgtype == 3) {
					bitMap = PhotoUtil.CompresPhoto(templogobitmappath_d3);
				} else if (d_imgtype == 4) {
					bitMap = PhotoUtil.CompresPhoto(templogobitmappath_d4);
				} else if (d_imgtype == 5) {
					bitMap = PhotoUtil.CompresPhoto(templogobitmappath_d5);
				} else if (d_imgtype == 6) {
					bitMap = PhotoUtil.CompresPhoto(templogobitmappath_d6);
				}

				String fileName = userId + "_" + shopId + "_goods_"
						+ System.currentTimeMillis() + "_" + picMenu.getType()
						+ ".png";
				file = FileUtil.BitMapToFile(bitMap, fileName);

				if (imgtype == 1) {
					files1 = file;
					iv_upload_goods_log1.setImageBitmap(bitMap);
					iv_upload_goods_log1.setScaleType(ScaleType.FIT_XY);
				} else if (imgtype == 2) {
					files2 = file;
					iv_upload_goods_log2.setImageBitmap(bitMap);
					iv_upload_goods_log2.setScaleType(ScaleType.FIT_XY);
				} else if (imgtype == 3) {
					files3 = file;
					iv_upload_goods_log3.setImageBitmap(bitMap);
					iv_upload_goods_log3.setScaleType(ScaleType.FIT_XY);
				} else if (d_imgtype == 1) {
					d_files1 = file;
					iv_upload_goods_d1.setImageBitmap(bitMap);
					iv_upload_goods_d1.setScaleType(ScaleType.FIT_XY);
				} else if (d_imgtype == 2) {
					d_files2 = file;
					iv_upload_goods_d2.setImageBitmap(bitMap);
					iv_upload_goods_d2.setScaleType(ScaleType.FIT_XY);
				} else if (d_imgtype == 3) {
					d_files3 = file;
					iv_upload_goods_d3.setImageBitmap(bitMap);
					iv_upload_goods_d3.setScaleType(ScaleType.FIT_XY);
				} else if (d_imgtype == 4) {
					d_files4 = file;
					iv_upload_goods_d4.setImageBitmap(bitMap);
					iv_upload_goods_d4.setScaleType(ScaleType.FIT_XY);
				} else if (d_imgtype == 5) {
					d_files5 = file;
					iv_upload_goods_d5.setImageBitmap(bitMap);
					iv_upload_goods_d5.setScaleType(ScaleType.FIT_XY);
				} else if (d_imgtype == 6) {
					d_files6 = file;
					iv_upload_goods_d6.setImageBitmap(bitMap);
					iv_upload_goods_d6.setScaleType(ScaleType.FIT_XY);
				}
			}

			break;
		case 200:
			// 从图库选择
			if (resultCode == RESULT_OK) {
				Uri picUri = intent.getData();
				try {
					bitMap = MediaStore.Images.Media.getBitmap(cr, picUri);
					String fileName = userId + "_" + shopId + "_goods_"
							+ System.currentTimeMillis() + "_"
							+ picMenu.getType() + ".png";
					file = FileUtil.BitMapToFile(bitMap, fileName);

					if (imgtype == 1) {
						files1 = file;
						iv_upload_goods_log1.setImageBitmap(bitMap);
						iv_upload_goods_log1.setScaleType(ScaleType.FIT_XY);
					} else if (imgtype == 2) {
						files2 = file;
						iv_upload_goods_log2.setImageBitmap(bitMap);
						iv_upload_goods_log2.setScaleType(ScaleType.FIT_XY);
					} else if (imgtype == 3) {
						files3 = file;
						iv_upload_goods_log3.setImageBitmap(bitMap);
						iv_upload_goods_log3.setScaleType(ScaleType.FIT_XY);
					} else if (d_imgtype == 1) {
						d_files1 = file;
						iv_upload_goods_d1.setImageBitmap(bitMap);
						iv_upload_goods_d1.setScaleType(ScaleType.FIT_XY);
					} else if (d_imgtype == 2) {
						d_files2 = file;
						iv_upload_goods_d2.setImageBitmap(bitMap);
						iv_upload_goods_d2.setScaleType(ScaleType.FIT_XY);
					} else if (d_imgtype == 3) {
						d_files3 = file;
						iv_upload_goods_d3.setImageBitmap(bitMap);
						iv_upload_goods_d3.setScaleType(ScaleType.FIT_XY);
					} else if (d_imgtype == 4) {
						d_files4 = file;
						iv_upload_goods_d4.setImageBitmap(bitMap);
						iv_upload_goods_d4.setScaleType(ScaleType.FIT_XY);
					} else if (d_imgtype == 5) {
						d_files5 = file;
						iv_upload_goods_d5.setImageBitmap(bitMap);
						iv_upload_goods_d5.setScaleType(ScaleType.FIT_XY);
					} else if (d_imgtype == 6) {
						d_files6 = file;
						iv_upload_goods_d6.setImageBitmap(bitMap);
						iv_upload_goods_d6.setScaleType(ScaleType.FIT_XY);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	};

	/**
	 * 调用上传
	 * 
	 * @param shopid
	 * @param userid
	 */
	private void uploadGoods(int shopid, int userid) {
		if (checkEmpty()) {
			// 上传数据
			if (checkIsNumber()) {
				uploadData(shopid, userid);
			} else {
				this.showToast("商品图片不能少于3张，详情图片不能少于6张,库存,售价需要是数字");
			}
		} else {
			this.showToast("商品的库存,售价,简介,标签不允许为空");
		}
	}

	/**
	 * 检查库存价格是否是数字
	 * 
	 * @return
	 */
	private boolean checkIsNumber() {
		String num = et_upload_goods_num1.getText().toString().trim();
		String price = et_upload_goods_price1.getText().toString().trim();

		if (files1 == null || files2 == null || files3 == null
				|| d_files1 == null || d_files2 == null || d_files3 == null
				|| d_files4 == null || d_files5 == null || d_files6 == null) {
			return false;
		}
		boolean checknum = CheckUtil.isInt(num);
		boolean checkprice = CheckUtil.isIntOrFloat(price);

		if (checknum && checkprice) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 上传商品
	 * 
	 * @param shopId
	 * @param userId
	 */
	private void uploadData(int shopId, int userId) {
		String goodsName = et_upload_goods_name1.getText().toString().trim();
		if (goodsName.length() > 30) {
			showToast("商品名称需小于30个字符");
			et_upload_goods_name1.requestFocus();
			return;
		}

		httpUtils = new HttpUtils(60 * 1000);
		RequestParams params = initParams(userId, shopId);
		boolean isconnected = BaseUtils
				.isMobileConnected(UploadGoodsActivity.this);
		if (isconnected) {
			showLoading2("数据加载中");
			httpUtils.send(HttpMethod.POST,
					GlobalConstant.GOODS_ACTION_UPLOAD_GOODS, params,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String msg) {
							showToast(msg);
							dismissProgressDialog();

							Log.v(TAG, "onFailure ");
						}

						@SuppressLint("NewApi")
						@Override
						public void onSuccess(ResponseInfo<String> jsonInfo) {
							String result = jsonInfo.result;

							Log.v(TAG, "onSuccess  result  " + result);

							dismissProgressDialog();
							try {
								JSONObject json = new JSONObject(result);
								String code = json.getString("resultCode");
								String msg = json.getString("resultMsg");
								System.out.println(code);
								if (code.equals("1")) {
									new AlertDialog.Builder(
											UploadGoodsActivity.this, 1)
											.setTitle("提示")
											.setMessage(msg)
											.setPositiveButton(
													"是",
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															// 清空一些控件的值
															clearValue();
														}
													})
											.setNegativeButton(
													"否",
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															finish();
														}
													}).show();
								} else {
									showToast(msg);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		} else {
			showToast("网络不可用...");
		}
	}

	/**
	 * 初始化上传参数
	 * 
	 * @param userid
	 * @param shopid
	 * @return
	 */
	private RequestParams initParams(int userid, int shopid) {
		RequestParams params = new RequestParams();
		// if (isDigital == 1) {
		// // 数码产品
		// getSelectSpinnerId();
		// int categoryId = cates.get(index).getId();
		//
		// params.addBodyParameter("isDigital", String.valueOf(isDigital));
		// params.addBodyParameter("digitalCategoryId",
		// String.valueOf(categoryId));
		// params.addBodyParameter("shopId", String.valueOf(shopid));
		// params.addBodyParameter("userId", String.valueOf(userid));
		//
		// params.addBodyParameter("files1", files1);
		// params.addBodyParameter("files2", files2);
		// params.addBodyParameter("files3", files3);
		//
		// params.addBodyParameter("d_files1", d_files1);
		// params.addBodyParameter("d_files2", d_files2);
		// params.addBodyParameter("d_files3", d_files3);
		// params.addBodyParameter("d_files4", d_files4);
		// params.addBodyParameter("d_files5", d_files5);
		// params.addBodyParameter("d_files6", d_files6);
		//
		// String goodsName = et_upload_goods_name1.getText().toString()
		// .trim();
		// String goodsNum = et_upload_goods_num1.getText().toString().trim();
		// String goodsPrice = et_upload_goods_price1.getText().toString()
		// .trim();
		// String goodsDetial = et_upload_goods_detail1.getText().toString()
		// .trim();
		//
		// params.addBodyParameter("goodsName", goodsName);
		// params.addBodyParameter("goodsNum", goodsNum);
		// params.addBodyParameter("goodsPrice", goodsPrice);
		// params.addBodyParameter("goodsDetial", goodsDetial);
		// } else if (isDigital == 0) {
		// 非数码产品
		System.out.println("非数码....");
		// params.addBodyParameter("isDigital", String.valueOf(isDigital));
		params.addBodyParameter("shopId", String.valueOf(shopid));
		params.addBodyParameter("userId", String.valueOf(userid));

		params.addBodyParameter("files1", files1);
		params.addBodyParameter("files2", files2);
		params.addBodyParameter("files3", files3);

		params.addBodyParameter("d_files1", d_files1);
		params.addBodyParameter("d_files2", d_files2);
		params.addBodyParameter("d_files3", d_files3);
		params.addBodyParameter("d_files4", d_files4);
		params.addBodyParameter("d_files5", d_files5);
		params.addBodyParameter("d_files6", d_files6);

		String goodsName = et_upload_goods_name1.getText().toString().trim();
		String goodsNum = et_upload_goods_num1.getText().toString().trim();
		String goodsPrice = et_upload_goods_price1.getText().toString().trim();
		String goods_tab = et_upload_goods_goods_tab.getText().toString()
				.trim();
		String goodsDetial = et_upload_goods_detail1.getText().toString()
				.trim();

		params.addBodyParameter("goodsName", goodsName);
		params.addBodyParameter("goodsNum", goodsNum);
		params.addBodyParameter("goodsPrice", goodsPrice);
		params.addBodyParameter("goods_tab", goods_tab);
		params.addBodyParameter("goodsDetial", goodsDetial);

		String goods_kd1 = "0";
		String goods_kd2 = "0";
		if (checkbox_goods_kd1.isChecked()) {
			goods_kd1 = "包邮";
		}
		if (checkbox_goods_kd2.isChecked()) {
			goods_kd2 = "送货上门";
		}

		params.addBodyParameter("goods_kd1", goods_kd1);
		params.addBodyParameter("goods_kd2", goods_kd2);

		// }
		return params;
	}

	// 获取选择的spinner项的ID
	public void getSelectSpinnerId() {
		upload_goods_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						index = position;
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						index = 0;
					}
				});
	}

	@SuppressLint("NewApi")
	private void clearValue() {
		// TODO Auto-generated method stub
		Resources r = UploadGoodsActivity.this.getResources();
		Drawable d = r.getDrawable(R.drawable.apply_open_shop_edit_bg);
		iv_upload_goods_log1.setBackground(d);
		iv_upload_goods_log1.setImageResource(R.drawable.pg1);
		iv_upload_goods_log1.setScaleType(ScaleType.CENTER_INSIDE);

		iv_upload_goods_log2.setBackground(d);
		iv_upload_goods_log2.setImageResource(R.drawable.pg1);
		iv_upload_goods_log2.setScaleType(ScaleType.CENTER_INSIDE);

		iv_upload_goods_log3.setBackground(d);
		iv_upload_goods_log3.setImageResource(R.drawable.pg1);
		iv_upload_goods_log3.setScaleType(ScaleType.CENTER_INSIDE);

		iv_upload_goods_d1.setBackground(d);
		iv_upload_goods_d1.setImageResource(R.drawable.pg1);
		iv_upload_goods_d1.setScaleType(ScaleType.CENTER_INSIDE);

		iv_upload_goods_d2.setBackground(d);
		iv_upload_goods_d2.setImageResource(R.drawable.pg1);
		iv_upload_goods_d2.setScaleType(ScaleType.CENTER_INSIDE);

		iv_upload_goods_d3.setBackground(d);
		iv_upload_goods_d3.setImageResource(R.drawable.pg1);
		iv_upload_goods_d3.setScaleType(ScaleType.CENTER_INSIDE);

		iv_upload_goods_d4.setBackground(d);
		iv_upload_goods_d4.setImageResource(R.drawable.pg1);
		iv_upload_goods_d4.setScaleType(ScaleType.CENTER_INSIDE);

		iv_upload_goods_d5.setBackground(d);
		iv_upload_goods_d5.setImageResource(R.drawable.pg1);
		iv_upload_goods_d5.setScaleType(ScaleType.CENTER_INSIDE);

		iv_upload_goods_d6.setBackground(d);
		iv_upload_goods_d6.setImageResource(R.drawable.pg1);
		iv_upload_goods_d6.setScaleType(ScaleType.CENTER_INSIDE);

		et_upload_goods_name1.setText("");
		et_upload_goods_num1.setText("");
		et_upload_goods_price1.setText("");
		et_upload_goods_goods_tab.setText("");
		et_upload_goods_detail1.setText("");

		files1 = null;
		files2 = null;
		files3 = null;

		d_files1 = null;
		d_files2 = null;
		d_files3 = null;
		d_files4 = null;
		d_files5 = null;
		d_files6 = null;

		templogobitmappath1 = "";
		templogobitmappath2 = "";
		templogobitmappath3 = "";

		templogobitmappath_d1 = "";
		templogobitmappath_d2 = "";
		templogobitmappath_d3 = "";
		templogobitmappath_d4 = "";
		templogobitmappath_d5 = "";
		templogobitmappath_d6 = "";
	}

	private boolean checkEmpty() {
		boolean isok = true;
		if (et_upload_goods_name1.getText().toString().trim().length() == 0) {
			isok = false;
		} else if (et_upload_goods_num1.getText().toString().trim().length() == 0) {
			isok = false;
		} else if (et_upload_goods_detail1.getText().toString().trim().length() == 0) {
			isok = false;
		} else if (et_upload_goods_goods_tab.getText().toString().trim().length() == 0) {
			isok = false;
		}
		return isok;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void findviewWithId() {
		ib_upload_goods_close = (ImageButton) findViewById(R.id.ib_upload_goods_close);
		iv_upload_goods_log1 = (ImageView) findViewById(R.id.iv_upload_goods_log1);
		iv_upload_goods_log2 = (ImageView) findViewById(R.id.iv_upload_goods_log2);
		iv_upload_goods_log3 = (ImageView) findViewById(R.id.iv_upload_goods_log3);

		iv_upload_goods_d1 = (ImageView) findViewById(R.id.iv_upload_goods_d1);
		iv_upload_goods_d2 = (ImageView) findViewById(R.id.iv_upload_goods_d2);
		iv_upload_goods_d3 = (ImageView) findViewById(R.id.iv_upload_goods_d3);
		iv_upload_goods_d4 = (ImageView) findViewById(R.id.iv_upload_goods_d4);
		iv_upload_goods_d5 = (ImageView) findViewById(R.id.iv_upload_goods_d5);
		iv_upload_goods_d6 = (ImageView) findViewById(R.id.iv_upload_goods_d6);

		et_upload_goods_name1 = (EditText) findViewById(R.id.et_upload_goods_name1);
		et_upload_goods_num1 = (EditText) findViewById(R.id.et_upload_goods_num1);
		et_upload_goods_price1 = (EditText) findViewById(R.id.et_upload_goods_price1);
		et_upload_goods_goods_tab = (EditText) findViewById(R.id.et_upload_goods_goods_tab);
		et_upload_goods_detail1 = (EditText) findViewById(R.id.et_upload_goods_detail1);
		btn_upload_goods = (Button) findViewById(R.id.btn_upload_goods);
		upload_goods_spinner = (Spinner) findViewById(R.id.upload_goods_spinner);

		checkbox_goods_kd1 = (CheckBox) findViewById(R.id.checkbox_goods_kd1);
		checkbox_goods_kd2 = (CheckBox) findViewById(R.id.checkbox_goods_kd2);
	}

	@Override
	public void initListener() {
		ib_upload_goods_close.setOnClickListener(this);
		btn_upload_goods.setOnClickListener(this);
		iv_upload_goods_log1.setOnClickListener(this);
		iv_upload_goods_log2.setOnClickListener(this);
		iv_upload_goods_log3.setOnClickListener(this);

		iv_upload_goods_d1.setOnClickListener(this);
		iv_upload_goods_d2.setOnClickListener(this);
		iv_upload_goods_d3.setOnClickListener(this);
		iv_upload_goods_d4.setOnClickListener(this);
		iv_upload_goods_d5.setOnClickListener(this);
		iv_upload_goods_d6.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		Intent intent = getIntent();
		shopId = intent.getIntExtra("shopId", 0);
		userId = intent.getIntExtra("userId", 0);
		getShopCategory();
		getSelectSpinnerId();
	}

	private void getShopCategory() {
		httpUtils = new HttpUtils();
		gson = new Gson();
		RequestParams params = new RequestParams();
		params.addBodyParameter("shop_id", String.valueOf(shopId));
		httpUtils.send(HttpMethod.POST,
				GlobalConstant.SHOP_ACTION_GET_MY_STORE, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException e, String msg) {
						Toast.makeText(UploadGoodsActivity.this, msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> json) {
						ShopBean shop = gson.fromJson(json.result,
								ShopBean.class);
						int index = shop.getCategory_name().indexOf("数码");
						if (index >= 0) {
							upload_goods_spinner.setVisibility(View.VISIBLE);
							isDigital = 1;// 待标属于数码产品
							getDataWithHttp();
						} else {
							upload_goods_spinner.setVisibility(View.GONE);
						}
					}
				});
	}

	// 填充spinner
	private void getDataWithHttp() {
		httpUtils = new HttpUtils();
		gson = new Gson();
		httpUtils.send(HttpMethod.GET,
				GlobalConstant.SHOP_ACTION_DIGITAL_CATEGORY,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException e, String msg) {
						Toast.makeText(UploadGoodsActivity.this, msg,
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> json) {
						String ss_result = json.result;
						cates = gson.fromJson(ss_result,
								new TypeToken<List<DigitalCategoryBean>>() {
								}.getType());
						for (DigitalCategoryBean scb : cates) {
							spinnerData.add(scb
									.getDigital_goods_category_name());
						}
						ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
								UploadGoodsActivity.this,
								R.layout.shop_category_spinner_item,
								spinnerData);
						upload_goods_spinner.setAdapter(spinnerAdapter);
					}
				});
	}
}
