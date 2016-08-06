package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.GoodsDetailCommentAdapter;
import com.ccc.www.bean.GoodsBean;
import com.ccc.www.bean.GoodsDetailCommentBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.ccc.www.view.XListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DigitalGoodsDetailActivity extends BaseActivity {

	String TAG = "DigitalGoodsDetailActivity";

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

	LinearLayout btn_add_to_shopcar_layout;
	LinearLayout layout_add_to_shopcar;
	TextView btn_buy_now;

	List<GoodsDetailCommentBean> allGoodsCommentBean = new ArrayList<GoodsDetailCommentBean>();

	// 从数码进来
	public static int FromDigital = 3;

	int id = 0;
	int from = 0;

	GoodsBean goodsbean = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		id = getIntent().getIntExtra("id", 0);
		from = getIntent().getIntExtra("from", 0);

		goodsbean = (GoodsBean) getIntent().getSerializableExtra("goodsbean");

		headview = LayoutInflater.from(this).inflate(
				R.layout.activity_goods_detail_header, null);

		setContentView(R.layout.activity_digital_goods_detail);

		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_goods_detail_goback:
			DigitalGoodsDetailActivity.this.finish();
			break;
		case R.id.btn_buy_now:
			int userid1 = UserUtil.getuserid(DigitalGoodsDetailActivity.this);
			if (userid1 > 0) {
				boolean exist = DBUtil.CheckDigitalGoodsExist(
						DigitalGoodsDetailActivity.this, goodsbean.getId());
				if (exist) {
					// 发广播修改 界面数量变化
					Intent updateProxyStockCartCount = new Intent();
					updateProxyStockCartCount
							.setAction(GlobalConstant.UpdateDigitalCartCount);
					sendBroadcast(updateProxyStockCartCount);

					Intent intent = new Intent();
					intent.setClass(DigitalGoodsDetailActivity.this,
							DigitalCartActivity.class);
					startActivity(intent);

				} else {
					// 不存在，新增
					goodsbean.setCount(1);
					DBUtil.insertDigitalCart(DigitalGoodsDetailActivity.this,
							goodsbean);

					// 发广播修改 界面数量变化
					Intent updateProxyStockCartCount = new Intent();
					updateProxyStockCartCount
							.setAction(GlobalConstant.UpdateDigitalCartCount);
					sendBroadcast(updateProxyStockCartCount);

					Intent intent = new Intent();
					intent.setClass(DigitalGoodsDetailActivity.this,
							DigitalCartActivity.class);
					startActivity(intent);
				}
			} else {
				showToast("请先登录");
				Intent login = new Intent();
				login.setClass(DigitalGoodsDetailActivity.this,
						LoginActivity.class);
				startActivity(login);
			}

			break;
		case R.id.layout_add_to_shopcar:
			if (goodsbean == null) {
				return;
			}
			// 加入到购物车
			switch (from) {
			case 3:
				// 从数码进来
				int userid = UserUtil
						.getuserid(DigitalGoodsDetailActivity.this);

				if (userid > 0) {

					Log.v(TAG, "id  " + id);

					Log.v(TAG, "goodsbean.getId()  " + goodsbean.getId());

					boolean exist = DBUtil.CheckDigitalGoodsExist(
							DigitalGoodsDetailActivity.this, goodsbean.getId());
					if (exist) {
						Toast.makeText(DigitalGoodsDetailActivity.this,
								"该商品已经在购物车了", Toast.LENGTH_SHORT).show();
					} else {
						// 不存在，新增
						Toast.makeText(DigitalGoodsDetailActivity.this,
								"已添加到购物车", Toast.LENGTH_SHORT).show();
						goodsbean.setCount(1);
						DBUtil.insertDigitalCart(
								DigitalGoodsDetailActivity.this, goodsbean);

					}
					// 发广播修改 界面数量变化
					Intent updateProxyStockCartCount = new Intent();
					updateProxyStockCartCount
							.setAction(GlobalConstant.UpdateDigitalCartCount);
					sendBroadcast(updateProxyStockCartCount);
					sendBroadcast(updateProxyStockCartCount);
				} else {
					showToast("请先登录");
					Intent login = new Intent();
					login.setClass(DigitalGoodsDetailActivity.this,
							LoginActivity.class);
					startActivity(login);
				}
				break;
			default:
				break;
			}

			break;

		default:
			break;
		}
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
		layout_add_to_shopcar = (LinearLayout) findViewById(R.id.layout_add_to_shopcar);
		btn_buy_now = (TextView) findViewById(R.id.btn_buy_now);
		btn_add_to_shopcar_layout = (LinearLayout) findViewById(R.id.btn_add_to_shopcar_layout);

		xlvComment.addHeaderView(headview);

		xlvComment.setPullRefreshEnable(false);
		xlvComment.setPullLoadEnable(false);
	}

	@Override
	public void initListener() {
		goBackBtn.setOnClickListener(this);
		layout_add_to_shopcar.setOnClickListener(this);
		btn_buy_now.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		getgoodsdetail();
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

		params.addBodyParameter("id", idstr);
		loadData(HttpMethod.POST, GlobalConstant.GET_DIGITALGOODSINFO, params,
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

							if (returnstr.contains("]-[")) {

								int position = returnstr.indexOf("]-[");
								String goodinfostr = returnstr.substring(0,
										position + 1);
								String commentstr = returnstr.substring(
										position + 2, returnstr.length());

								Log.v(TAG, "goodinfostr  " + goodinfostr);
								Log.v(TAG, "commentstr  " + commentstr);

								try {

									JSONArray goodinfoaray = new JSONArray(
											goodinfostr);

									JSONObject goodinfo = goodinfoaray
											.getJSONObject(0);

									int digital_goods_category_id = goodinfo
											.getInt("digital_goods_category_id");

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

									goodsbean = new GoodsBean(id, 0, 0,
											digital_goods_category_id,
											digital_goods_category_id,
											goods_name, goods_num, goods_price,
											goods_detail, goods_log1,
											goods_log2, goods_log3,
											goods_status, 0);

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

										ImageLoader.getInstance().displayImage(
												goods_log2, goods_img2,
												ImageLoaderOption.getoption());
									} else {
										goods_img2.setVisibility(View.GONE);
									}
									if (!TextUtils.isEmpty(goods_log3)) {
										if (!goods_log3.startsWith("http")) {
											goods_log3 = GlobalConstant.SERVER_URL
													+ goods_log3;
										}
										alltopimg.add(goods_log3);

										ImageLoader.getInstance().displayImage(
												goods_log3, goods_img3,
												ImageLoaderOption.getoption());
									} else {
										goods_img3.setVisibility(View.GONE);
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
													DigitalGoodsDetailActivity.this,
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

			View view = LayoutInflater.from(DigitalGoodsDetailActivity.this)
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
