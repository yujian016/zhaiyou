package com.ccc.www.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.MySuperMarketGoodsBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.CheckUtil;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.Xutils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MySuperMarketAdapter extends BaseAdapter {

	String TAG = "MySuperMarketAdapter";

	private List<MySuperMarketGoodsBean> lists = new ArrayList<MySuperMarketGoodsBean>();
	private LayoutInflater inflater;
	private Context context;

	View rootView;

	public MySuperMarketAdapter(View rootView, Context context,
			List<MySuperMarketGoodsBean> lists) {
		super();
		this.rootView = rootView;
		this.context = context;
		this.lists = lists;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int itemId) {
		return itemId;
	}

	@Override
	public View getView(int index, View view, ViewGroup vgroup) {
		ViewHolder holder = null;

		if (view == null) {
			holder = new ViewHolder();

			view = inflater.inflate(R.layout.item_mysupermarket, null);

			holder.item_mysupermarket_img = (ImageView) view
					.findViewById(R.id.item_mysupermarket_img);
			holder.item_mysupermarket_name = (TextView) view
					.findViewById(R.id.item_mysupermarket_name);
			holder.item_mysupermarket_num = (TextView) view
					.findViewById(R.id.item_mysupermarket_num);
			holder.item_mysupermarket_price = (TextView) view
					.findViewById(R.id.item_mysupermarket_price);

			holder.item_mysupermarket_cancle_sup_goods = (TextView) view
					.findViewById(R.id.item_mysupermarket_cancle_sup_goods);

			view.setTag(holder);

		} else {
			holder = (ViewHolder) view.getTag();
		}

		final MySuperMarketGoodsBean sock = lists.get(index);

		final int goodstatus = sock.getGoods_status();

		Log.v(TAG, "	goodstatus 	" + goodstatus);

		ImageLoader.getInstance().displayImage(sock.getGoods_log(),
				holder.item_mysupermarket_img,
				ImageLoaderOption.getoption());

		holder.item_mysupermarket_name.setText(sock.getGoods_name());
		holder.item_mysupermarket_num.setText("库存:" + sock.getHave_num());

		holder.item_mysupermarket_price.setText("￥" + sock.getGoods_price()
				+ "元");

		if (goodstatus == 3) {
			holder.item_mysupermarket_cancle_sup_goods
					.setVisibility(View.VISIBLE);
			holder.item_mysupermarket_cancle_sup_goods.setText("下架");
		} else if (goodstatus == 4) {
			holder.item_mysupermarket_cancle_sup_goods
					.setVisibility(View.VISIBLE);
			holder.item_mysupermarket_cancle_sup_goods.setText("上架");
		} else {
			holder.item_mysupermarket_cancle_sup_goods
					.setVisibility(View.INVISIBLE);
		}

		holder.item_mysupermarket_cancle_sup_goods
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {

						if (goodstatus == 3) {
							AlertDialog.Builder build = new Builder(context);
							build.setTitle("提示");
							build.setMessage("您确定要下架 " + sock.getGoods_name()
									+ " 吗？");
							build.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
											xiajia(sock.getId());
										}
									});
							build.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub

										}
									});
							build.show();
						} else if (goodstatus == 4) {

							popChangePrice(sock);

						}
					}
				});

		return view;
	}

	/**
	 * 弹出修改价格
	 * 
	 */
	private void popChangePrice(final MySuperMarketGoodsBean bean) {

		View view1 = LayoutInflater.from(context).inflate(
				R.layout.pop_putaway_changeprice, null);
		mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setFocusable(true);

		final EditText pop_putaway_changeprice_newprice = (EditText) view1
				.findViewById(R.id.pop_putaway_changeprice_newprice);

		TextView pop_putaway_changeprice_cancel = (TextView) view1
				.findViewById(R.id.pop_putaway_changeprice_cancel);
		TextView pop_putaway_changeprice_confirm = (TextView) view1
				.findViewById(R.id.pop_putaway_changeprice_confirm);

		pop_putaway_changeprice_newprice.setText(bean.getGoods_price() + "");

		pop_putaway_changeprice_cancel
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dissmisspopwindow();
					}
				});
		pop_putaway_changeprice_confirm
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						final String newprice = pop_putaway_changeprice_newprice
								.getText().toString().trim();
						if (TextUtils.isEmpty(newprice)) {
							Toast.makeText(context, "请输入价格", Toast.LENGTH_SHORT)
									.show();
							return;
						}
						boolean ischeckok = CheckUtil.isIntOrFloat(newprice);
						if (!ischeckok) {
							Toast.makeText(context, "请输入整数或小数",
									Toast.LENGTH_SHORT).show();
							return;
						}

						AlertDialog.Builder build = new Builder(context);
						build.setTitle("提示");
						build.setMessage("您确认上架吗？");
						build.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										confirmPutaway(bean, newprice);
									}
								});
						build.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
									}
								});
						build.show();

					}
				});
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
		mPopupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
	}

	/**
	 * 确认上架
	 */
	private void confirmPutaway(final MySuperMarketGoodsBean bean,
			final String priceStr) {
		if (!BaseUtils.isNetWork(context)) {
			Toast.makeText(context, "请检查您的网络", Toast.LENGTH_SHORT).show();
			return;
		}

		RequestParams params = new RequestParams();
		params.addBodyParameter("goods_id", bean.getId() + "");
		params.addBodyParameter("new_price", priceStr);

		Xutils.loadData(HttpMethod.POST,
				GlobalConstant.UPDATE_PROXY_GOODS_TOSUP, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> response) {
						// TODO Auto-generated method stub

						String returnstr = response.result;

						Log.v(TAG, response.result);
						if (TextUtils.isEmpty(returnstr)) {
							Toast.makeText(context, "上架失败", Toast.LENGTH_SHORT)
									.show();
						} else {
							// {"resultCode":1,"beanId":1,"resultMsg":"上架成功"}
							try {
								JSONObject json = new JSONObject(returnstr);
								int resultCode = json.getInt("resultCode");
								String resultMsg = json.getString("resultMsg");
								if (resultCode == 1) {
									bean.setGoods_status(3);
									bean.setGoods_price(Float
											.parseFloat(priceStr));
									notifyDataSetChanged();
									dissmisspopwindow();

								}
								Toast.makeText(context, resultMsg,
										Toast.LENGTH_SHORT).show();

							} catch (JSONException e) {
								e.printStackTrace();
								Toast.makeText(context, "上架失败",
										Toast.LENGTH_SHORT).show();
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(context, "上架失败", Toast.LENGTH_SHORT)
								.show();

					}
				});
	}

	PopupWindow mPopupWindow;

	private void dissmisspopwindow() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

	private void xiajia(final int goods_id) {
		if (!BaseUtils.isNetWork(context)) {
			Toast.makeText(context, "请检查您的网络", Toast.LENGTH_SHORT).show();
			return;
		}

		Log.v(TAG, "goods_id  " + goods_id);

		RequestParams params = new RequestParams();
		params.addBodyParameter("goods_id", "" + goods_id);
		Xutils.loadData(HttpMethod.POST, GlobalConstant.CANCEL_SUP_GOODS,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(context, "下架失败，请稍后重试",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String returnstr = info.result;
						Log.v(TAG, "returnstr  " + returnstr);
						try {
							JSONObject json = new JSONObject(returnstr);
							String code = json.getString("code");
							String msg = json.getString("msg");
							Toast.makeText(context, msg, Toast.LENGTH_SHORT)
									.show();
							if (code.equalsIgnoreCase("1")) {
								for (int i = 0; i < lists.size(); i++) {
									if (lists.get(i).getId() == goods_id) {
										lists.get(i).setGoods_status(4);
										notifyDataSetChanged();
										break;
									}
								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	class ViewHolder {
		ImageView item_mysupermarket_img;
		TextView item_mysupermarket_name;
		TextView item_mysupermarket_num;
		TextView item_mysupermarket_price;
		TextView item_mysupermarket_cancle_sup_goods;
	}

}
