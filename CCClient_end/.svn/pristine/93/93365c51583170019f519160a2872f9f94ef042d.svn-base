package com.ccc.www.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.GoodsDetailCommentAdapter;
import com.ccc.www.bean.GoodsBean;
import com.ccc.www.bean.GoodsDetailCommentBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.view.XListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyShopGoodsDetailActivity extends BaseActivity {

	String TAG = "MyShopGoodsDetailActivity";

	private ViewPager vpgoods;
	private ImageButton goBackBtn;

	List<String> alltopimg = new ArrayList<String>();

	private LinearLayout ad_goods_dot_layout;
	private XListView xlvComment;
	private View headview;

	TextView tv_entity_goods_name;
	TextView tv_entity_goods_info;
	ImageView goods_img1;
	ImageView goods_img2;
	ImageView goods_img3;
	ImageView goods_img4;
	ImageView goods_img5;
	ImageView goods_img6;

	Button btn_unshelve;
	Button btn_editgoods;
	Button btn_add_limited_sale;

	List<GoodsDetailCommentBean> allGoodsCommentBean = new ArrayList<GoodsDetailCommentBean>();

	int id = 0;
	GoodsBean goodsBean = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		id = getIntent().getIntExtra("id", 0);

		goodsBean = (GoodsBean) getIntent().getSerializableExtra("goodsBean");

		headview = LayoutInflater.from(this).inflate(
				R.layout.activity_goods_detail_header, null);

		setContentView(R.layout.activity_myshop_goods_detail);

		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_goods_detail_goback:
			MyShopGoodsDetailActivity.this.finish();
			break;
		case R.id.btn_unshelve:
			AlertDialog.Builder build = new Builder(
					MyShopGoodsDetailActivity.this);
			build.setTitle("提示");
			build.setMessage("您确定要删除该商品吗？");
			build.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							deletegoods();
						}
					});
			build.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {

						}
					});
			build.show();

			break;
		case R.id.btn_editgoods:
			if (goodsBean == null) {
				return;
			}

			Intent intent = new Intent();

			Bundle bundle = new Bundle();
			bundle.putSerializable("goodsBean", (Serializable) goodsBean);
			intent.putExtras(bundle);

			intent.putExtra("id", id);

			intent.setClass(MyShopGoodsDetailActivity.this,
					EditGoodsDetailActivity.class);
			startActivityForResult(intent, EditGoodsDetailActivity.EditSuccess);

			break;
		case R.id.btn_add_limited_sale:
			if (goodsBean != null) {
				checklimit_goods_num();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0 == EditGoodsDetailActivity.EditSuccess
				&& arg1 == EditGoodsDetailActivity.EditSuccess) {
			getgoodsdetail();
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	/**
	 * 5、删除商品： goods_id------>商品ID
	 */
	private void deletegoods() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在删除");

		RequestParams params = new RequestParams();

		String goods_id = String.valueOf(id);

		Log.v(TAG, "goods_id	  " + goods_id);

		params.addBodyParameter("goods_id", goods_id);
		loadData(HttpMethod.POST, GlobalConstant.DELETE_GOODS, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						dismissProgressDialog();

						String returnstr = arg0.result;

						Log.v(TAG, "returnstr   " + returnstr);

						try {
							JSONObject json = new JSONObject(returnstr);
							String code = json.getString("code");
							String msg = json.getString("msg");
							showToast(msg);
							if (code.equalsIgnoreCase("1")) {

								Intent intent = new Intent();
								intent.setAction(GlobalConstant.ReloadMyStoreAllGoods);
								sendBroadcast(intent);

								MyShopGoodsDetailActivity.this.finish();
							}
						} catch (JSONException e) {
							e.printStackTrace();
							showToast("删除失败");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("删除失败");
						Log.v(TAG, "onFailure   " + arg1);

					}
				});

	}

	/**
	 * 3、判断店铺是否已经加入了2个商品到限时抢购，每家店铺只能加入2个商品：
	 */
	private void checklimit_goods_num() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();

		String shop_id = String.valueOf(goodsBean.getShop_id());

		Log.v(TAG, "shop_id	  " + shop_id);

		params.addBodyParameter("shop_id", shop_id);
		loadData(HttpMethod.POST, GlobalConstant.CHECK_GOODS_TimeLimit_NUM,
				params, new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						dismissProgressDialog();

						String returnstr = arg0.result;

						Log.v(TAG, "returnstr   " + returnstr);

						try {
							JSONObject json = new JSONObject(returnstr);
							String code = json.getString("code");
							String msg = json.getString("msg");

							if (code.equalsIgnoreCase("0")) {
								// 加入限时抢购
								joinlimitsale();
							} else {
								showToast(msg);
							}
						} catch (JSONException e) {
							e.printStackTrace();
							showToast("加载失败");
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
						Log.v(TAG, "onFailure   " + arg1);

					}
				});
	}

	/**
	 * 加入限时抢购
	 */
	private void joinlimitsale() {
		// TODO Auto-generated method stub
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();

		String goods_id = String.valueOf(id);

		Log.v(TAG, "goods_id	  " + goods_id);

		params.addBodyParameter("goods_id", goods_id);
		loadData(HttpMethod.POST, GlobalConstant.ADD_LIMIT_SALE, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						dismissProgressDialog();

						String returnstr = arg0.result;

						Log.v(TAG, "returnstr   " + returnstr);

						try {
							JSONObject json = new JSONObject(returnstr);
							String code = json.getString("code");
							String msg = json.getString("msg");

							if (code.equalsIgnoreCase("1")) {
								btn_add_limited_sale.setVisibility(View.GONE);
							}
							showToast(msg);
						} catch (JSONException e) {
							showToast("加载失败");
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
						Log.v(TAG, "onFailure   " + arg1);

					}
				});
	}

	@Override
	public void findviewWithId() {
		ad_goods_dot_layout = (LinearLayout) headview
				.findViewById(R.id.ad_goods_dot_layout);
		vpgoods = (ViewPager) headview.findViewById(R.id.vp_goods_detail);

		tv_entity_goods_name = (TextView) headview
				.findViewById(R.id.tv_entity_goods_name);
		tv_entity_goods_info = (TextView) headview
				.findViewById(R.id.tv_entity_goods_info);
		goods_img1 = (ImageView) headview.findViewById(R.id.goods_img1);
		goods_img2 = (ImageView) headview.findViewById(R.id.goods_img2);
		goods_img3 = (ImageView) headview.findViewById(R.id.goods_img3);
		goods_img4 = (ImageView) headview.findViewById(R.id.goods_img4);
		goods_img5 = (ImageView) headview.findViewById(R.id.goods_img5);
		goods_img6 = (ImageView) headview.findViewById(R.id.goods_img6);

		goBackBtn = (ImageButton) findViewById(R.id.ib_goods_detail_goback);
		xlvComment = (XListView) findViewById(R.id.xlv_entity_goods_comment);

		btn_unshelve = (Button) findViewById(R.id.btn_unshelve);
		btn_editgoods = (Button) findViewById(R.id.btn_editgoods);
		btn_add_limited_sale = (Button) findViewById(R.id.btn_add_limited_sale);

		xlvComment.addHeaderView(headview);

		xlvComment.setPullRefreshEnable(false);
		xlvComment.setPullLoadEnable(false);
	}

	@Override
	public void initListener() {
		goBackBtn.setOnClickListener(this);
		btn_unshelve.setOnClickListener(this);
		btn_editgoods.setOnClickListener(this);
		btn_add_limited_sale.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		getgoodsdetail();
		checkisaddlimitsale();
	}

	/**
	 * 获取该商品是否已经加入了限时抢购
	 */

	private void checkisaddlimitsale() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();

		String goods_id = String.valueOf(id);

		Log.v(TAG, "goods_id   " + goods_id);

		params.addBodyParameter("goods_id", goods_id);
		loadData(HttpMethod.POST, GlobalConstant.CHECK_GOODS_TimeLimit, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						dismissProgressDialog();

						String returnstr = arg0.result;

						Log.v(TAG, "returnstr   " + returnstr);

						try {
							JSONObject json = new JSONObject(returnstr);
							String code = json.getString("code");

							if (!code.equalsIgnoreCase("1")) {
								btn_add_limited_sale
										.setVisibility(View.VISIBLE);
							} else {
								btn_add_limited_sale.setVisibility(View.GONE);
							}
						} catch (JSONException e) {
							btn_add_limited_sale.setVisibility(View.GONE);
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();

						Log.v(TAG, "onFailure   " + arg1);

					}
				});

	}

	/**
	 * 获取详情
	 */
	private void getgoodsdetail() {

		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();

		String idstr = String.valueOf(id);

		params.addBodyParameter("goods_id", idstr);
		loadData(HttpMethod.POST, GlobalConstant.GET_SHOPGOODSINFO, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();

						showToast("加载失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String returnstr = info.result;
						Log.v(TAG, "returnstr " + returnstr);
						if (BaseUtils.isEmpty(returnstr)) {
							showToast("加载失败");
						} else {

							if (returnstr.contains("}-[")) {

								int position = returnstr.indexOf("}-[");
								String goodinfostr = returnstr.substring(0,
										position + 1);
								String commentstr = returnstr.substring(
										position + 2, returnstr.length());

								Log.v(TAG, "goodinfostr  " + goodinfostr);
								Log.v(TAG, "commentstr  " + commentstr);

								try {

									JSONObject goodinfo = new JSONObject(
											goodinfostr);

									int shop_id = goodinfo.getInt("shop_id");
									int user_id = goodinfo.getInt("user_id");
									String goods_name = goodinfo
											.getString("goods_name");
									int goods_num = goodinfo
											.getInt("goods_num");
									float goods_price = (float) goodinfo
											.getDouble("goods_price");
									String goods_detail = goodinfo
											.getString("goods_detail");
									String goods_log1 = goodinfo
											.getString("goods_log1");
									String goods_log2 = goodinfo
											.getString("goods_log2");
									String goods_log3 = goodinfo
											.getString("goods_log3");
									int goods_status = goodinfo
											.getInt("goods_status");
									int goods_cate_id = goodinfo
											.getInt("shop_category_id");

									String goods_d1 = goodinfo
											.getString("goods_d1");
									String goods_d2 = goodinfo
											.getString("goods_d2");
									String goods_d3 = goodinfo
											.getString("goods_d3");
									String goods_d4 = goodinfo
											.getString("goods_d4");
									String goods_d5 = goodinfo
											.getString("goods_d5");
									String goods_d6 = goodinfo
											.getString("goods_d6");

									String goods_kd1 = goodinfo
											.getString("goods_kd1");
									String goods_kd2 = goodinfo
											.getString("goods_kd2");

									goodsBean.setGoods_detail(goods_detail);
									goodsBean.setGoods_kd1(goods_kd1);
									goodsBean.setGoods_kd2(goods_kd2);
									goodsBean.setGoods_name(goods_name);
									goodsBean.setGoods_price(goods_price);

									JSONArray comment = new JSONArray(
											commentstr);

									for (int i = 0; i < comment.length(); i++) {
										JSONObject jsoncomment = comment
												.getJSONObject(i);

										int id = jsoncomment.getInt("id");
										int shop_id_comment = jsoncomment
												.getInt("shop_id");
										int supermaket_id = jsoncomment
												.getInt("supermaket_id");
										int goods_id = jsoncomment
												.getInt("goods_id");
										String good_comment = "";
										if (jsoncomment.has("good_comment")) {
											good_comment = jsoncomment
													.getString("good_comment");
										}

										String bad_comment = "";
										if (jsoncomment.has("bad_comment")) {
											bad_comment = jsoncomment
													.getString("bad_comment");
										}
										String comment_time = jsoncomment
												.getString("comment_time");
										int status = jsoncomment
												.getInt("status");
										GoodsDetailCommentBean bean = new GoodsDetailCommentBean(
												id, shop_id_comment,
												supermaket_id, goods_id,
												good_comment, bad_comment,
												comment_time, status);

										allGoodsCommentBean.add(bean);
									}

									if (!TextUtils.isEmpty(goods_log1)) {
										if (!goods_log1.startsWith("http")) {
											goods_log1 = GlobalConstant.SERVER_URL
													+ goods_log1;
										}
										alltopimg.add(goods_log1);
									}
									if (!TextUtils.isEmpty(goods_log2)) {
										if (!goods_log2.startsWith("http")) {
											goods_log2 = GlobalConstant.SERVER_URL
													+ goods_log2;
										}
										alltopimg.add(goods_log2);
									}
									if (!TextUtils.isEmpty(goods_log3)) {
										if (!goods_log3.startsWith("http")) {
											goods_log3 = GlobalConstant.SERVER_URL
													+ goods_log3;
										}
										alltopimg.add(goods_log3);
									}

									if (!TextUtils.isEmpty(goods_d1)) {
										if (!goods_d1.startsWith("http")) {
											goods_d1 = GlobalConstant.SERVER_URL
													+ goods_d1;
										}
										ImageLoader.getInstance().displayImage(
												goods_d1, goods_img1,
												ImageLoaderOption.getoption());
									} else {
										goods_img1.setVisibility(View.GONE);
									}

									if (!TextUtils.isEmpty(goods_d2)) {
										if (!goods_d2.startsWith("http")) {
											goods_d2 = GlobalConstant.SERVER_URL
													+ goods_d2;
										}
										ImageLoader.getInstance().displayImage(
												goods_d2, goods_img2,
												ImageLoaderOption.getoption());
									} else {
										goods_img2.setVisibility(View.GONE);
									}

									if (!TextUtils.isEmpty(goods_d3)) {
										if (!goods_d3.startsWith("http")) {
											goods_d3 = GlobalConstant.SERVER_URL
													+ goods_d3;
										}
										ImageLoader.getInstance().displayImage(
												goods_d3, goods_img3,
												ImageLoaderOption.getoption());
									} else {
										goods_img3.setVisibility(View.GONE);
									}

									if (!TextUtils.isEmpty(goods_d4)) {
										if (!goods_d4.startsWith("http")) {
											goods_d4 = GlobalConstant.SERVER_URL
													+ goods_d4;
										}
										ImageLoader.getInstance().displayImage(
												goods_d4, goods_img4,
												ImageLoaderOption.getoption());
									} else {
										goods_img4.setVisibility(View.GONE);
									}

									if (!TextUtils.isEmpty(goods_d5)) {
										if (!goods_d5.startsWith("http")) {
											goods_d5 = GlobalConstant.SERVER_URL
													+ goods_d5;
										}
										ImageLoader.getInstance().displayImage(
												goods_d5, goods_img5,
												ImageLoaderOption.getoption());
									} else {
										goods_img5.setVisibility(View.GONE);
									}

									if (!TextUtils.isEmpty(goods_d6)) {
										if (!goods_d6.startsWith("http")) {
											goods_d6 = GlobalConstant.SERVER_URL
													+ goods_d6;
										}
										ImageLoader.getInstance().displayImage(
												goods_d6, goods_img6,
												ImageLoaderOption.getoption());
									} else {
										goods_img6.setVisibility(View.GONE);
									}

									vpgoods.setAdapter(new GoodsViewPager());

									xlvComment
											.setAdapter(new GoodsDetailCommentAdapter(
													MyShopGoodsDetailActivity.this,
													allGoodsCommentBean,
													R.layout.goods_detail_comment_lv_item));

									initDot();
									updateDot();
									viewPagerListener();

									/**
									 * UI赋值
									 */
									tv_entity_goods_name.setText(goods_name);
									tv_entity_goods_info.setText(goods_detail);

									// 判断属否是自己店铺的，是的话不显示加入到购物车

								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}
					}
				});
	}

	class GoodsViewPager extends PagerAdapter {
		@Override
		public int getCount() {
			return alltopimg.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			View view = LayoutInflater.from(MyShopGoodsDetailActivity.this)
					.inflate(R.layout.item_viewpageimg, null);
			ImageView imageview = (ImageView) view
					.findViewById(R.id.item_viewpageimg_image);

			ImageLoader.getInstance().displayImage(alltopimg.get(position),
					imageview,
					ImageLoaderOption.getoption());

			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

	public void initDot() {
		for (int i = 0; i < alltopimg.size(); i++) {
			View view = new View(this);
			LayoutParams params = new LayoutParams(10, 10);
			params.leftMargin = 10;
			view.setLayoutParams(params);
			view.setBackgroundResource(R.drawable.selector_dot);
			ad_goods_dot_layout.addView(view);
		}
	}

	public void updateDot() {
		int currentIndex = vpgoods.getCurrentItem();
		for (int i = 0; i < ad_goods_dot_layout.getChildCount(); i++) {
			if (currentIndex == i) {
				ad_goods_dot_layout.getChildAt(i).setEnabled(true);
			} else {
				ad_goods_dot_layout.getChildAt(i).setEnabled(false);
			}
		}
	}

	private void viewPagerListener() {
		vpgoods.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				updateDot();
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}
}
