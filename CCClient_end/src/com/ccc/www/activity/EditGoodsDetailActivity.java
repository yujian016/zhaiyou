package com.ccc.www.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.GoodsBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 编辑商品信息
 * 
 * @author Administrator
 * 
 */
public class EditGoodsDetailActivity extends BaseActivity {

	String TAG = "EditGoodsDetailActivity";

	public static int EditSuccess = 520;

	int id = 0;
	GoodsBean goodsBean = null;

	EditText et_upload_goods_name1;
	EditText et_upload_goods_num1;
	EditText et_upload_goods_price1;
	EditText et_upload_goods_detail1;
	CheckBox checkbox_goods_kd1;
	CheckBox checkbox_goods_kd2;
	Button btn_edit_goods;

	ImageButton ib_upload_goods_close;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		id = getIntent().getIntExtra("id", 0);
		goodsBean = (GoodsBean) getIntent().getSerializableExtra("goodsBean");

		setContentView(R.layout.activity_editgoodsdetail);

		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_edit_goods:
			String goods_name = et_upload_goods_name1.getText().toString()
					.trim();
			String goods_num = et_upload_goods_num1.getText().toString().trim();
			String goods_price = et_upload_goods_price1.getText().toString()
					.trim();
			String goods_detail = et_upload_goods_detail1.getText().toString()
					.trim();
			String goods_kd1 = "0";
			String goods_kd2 = "0";

			if (checkbox_goods_kd1.isChecked()) {
				goods_kd1 = "包邮";
			}
			if (checkbox_goods_kd2.isChecked()) {
				goods_kd2 = "送货上门";
			}

			if (!BaseUtils.isNetWork(getApplicationContext())) {
				showToast("请检查您的网络");
				return;
			}

			showLoading2("正在加载");

			RequestParams params = new RequestParams();

			String goods_id = String.valueOf(id);

			params.addBodyParameter("goods_name", goods_name);
			params.addBodyParameter("goods_num", goods_num);
			params.addBodyParameter("goods_price", goods_price);
			params.addBodyParameter("goods_detail", goods_detail);
			params.addBodyParameter("goods_kd1", goods_kd1);
			params.addBodyParameter("goods_kd2", goods_kd2);
			params.addBodyParameter("goods_id", goods_id);

			loadData(HttpMethod.POST, GlobalConstant.EDIT_SHOP_GOODS, params,
					new RequestCallBack<String>() {

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							dismissProgressDialog();

							String returnstr = arg0.result;

							Log.v(TAG, "onSuccess	 " + returnstr);

							try {
								JSONObject json = new JSONObject(returnstr);
								int resultCode = json.getInt("resultCode");
								String resultMsg = json.getString("resultMsg");
								showToast(resultMsg);
								if (resultCode == 1) {
									setResult(EditSuccess);
									EditGoodsDetailActivity.this.finish();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							dismissProgressDialog();
							Log.v(TAG, "onFailure	 " + arg1);
							showToast("修改失败");
						}
					});

			break;
		case R.id.ib_upload_goods_close:
			EditGoodsDetailActivity.this.finish();
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
		et_upload_goods_name1 = (EditText) findViewById(R.id.et_upload_goods_name1);
		et_upload_goods_num1 = (EditText) findViewById(R.id.et_upload_goods_num1);
		et_upload_goods_price1 = (EditText) findViewById(R.id.et_upload_goods_price1);
		et_upload_goods_detail1 = (EditText) findViewById(R.id.et_upload_goods_detail1);
		checkbox_goods_kd1 = (CheckBox) findViewById(R.id.checkbox_goods_kd1);
		checkbox_goods_kd2 = (CheckBox) findViewById(R.id.checkbox_goods_kd2);
		btn_edit_goods = (Button) findViewById(R.id.btn_edit_goods);
		ib_upload_goods_close = (ImageButton) findViewById(R.id.ib_upload_goods_close);
	}

	@Override
	public void initListener() {
		btn_edit_goods.setOnClickListener(this);
		ib_upload_goods_close.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		if (goodsBean != null) {
			et_upload_goods_name1.setText(goodsBean.getGoods_name());
			et_upload_goods_num1.setText(goodsBean.getGoods_num() + "");
			et_upload_goods_price1.setText(goodsBean.getGoods_price() + "");
			et_upload_goods_detail1.setText(goodsBean.getGoods_detail());

			String kd1 = goodsBean.getGoods_kd1();
			String kd2 = goodsBean.getGoods_kd2();

			if (kd1.equalsIgnoreCase("包邮")) {
				checkbox_goods_kd1.setChecked(true);
			} else {
				checkbox_goods_kd1.setChecked(false);
			}

			if (kd2.equalsIgnoreCase("送货上门")) {
				checkbox_goods_kd2.setChecked(true);
			} else {
				checkbox_goods_kd2.setChecked(false);
			}
		}
	}

}
