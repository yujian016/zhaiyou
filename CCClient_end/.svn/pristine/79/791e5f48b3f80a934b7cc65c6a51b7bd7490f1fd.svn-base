package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
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
import com.ccc.www.bean.GoodsDetailCommentBean;
import com.ccc.www.bean.ShopGoodBean;
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

public class ShopGoodsDetailActivity extends BaseActivity {

	String TAG = "ShopGoodsDetailActivity";

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
	LinearLayout contact_saler;
	LinearLayout goto_shop;

	List<GoodsDetailCommentBean> allGoodsCommentBean = new ArrayList<GoodsDetailCommentBean>();

	// 从商铺进来
	public static int FromShop = 1;
	// 从校园购物进来
	public static int FromSchoolBuy = 2;

	int id = 0;
	int from = 0;

	ShopGoodBean shopgoodbean = null;
	
	
	LinearLayout layout_add_to_collect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		id = getIntent().getIntExtra("id", 0);
		from = getIntent().getIntExtra("from", 0);

		shopgoodbean = (ShopGoodBean) getIntent().getSerializableExtra(
				"shopgoodbean");

		headview = LayoutInflater.from(this).inflate(
				R.layout.activity_goods_detail_header, null);

		setContentView(R.layout.activity_goods_detail2);

		initview();
	}
	
	
	private void collectshop(int shopid) {
		// TODO Auto-generated method stub
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在添加收藏");

		RequestParams params = new RequestParams();
		params.addBodyParameter("user_id", String.valueOf(UserUtil.getuserid(this)));
		params.addBodyParameter("shop_id", String.valueOf(shopid));
		params.addBodyParameter("status", String.valueOf(1));

		loadData(HttpMethod.POST, GlobalConstant.Add_collect_shop, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub

						dismissProgressDialog();

						String returnstr2 = arg0.result;

						Log.v(TAG, "onSuccess   " + returnstr2);

						try {
							JSONObject json = new JSONObject(returnstr2);
							String telephone = json.getString("telephone");
							
							
							 
						} catch (JSONException e) {
							showToast("添加收藏失败");
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						dismissProgressDialog();
						Log.v(TAG, "onFailure " + arg1);

						showToast("添加收藏失败");

					}
				});
	}

	private void getSalerPhone(int shopid) {
		// TODO Auto-generated method stub
		// 掉接口
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("shop_id", String.valueOf(shopid));

		loadData(HttpMethod.POST, GlobalConstant.GET_SALE_PHONE, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub

						dismissProgressDialog();

						String returnstr2 = arg0.result;

						Log.v(TAG, "onSuccess   " + returnstr2);

						try {
							JSONObject json = new JSONObject(returnstr2);
							String telephone = json.getString("telephone");
							if (!TextUtils.isEmpty(telephone)) {
								// intent启动拨打电话
								Intent intent = new Intent(Intent.ACTION_CALL,
										Uri.parse("tel:" + telephone));
								startActivity(intent);
							}
						} catch (JSONException e) {
							showToast("加载失败");
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						dismissProgressDialog();
						Log.v(TAG, "onFailure " + arg1);

						showToast("加载失败");

					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_goods_detail_goback:
			ShopGoodsDetailActivity.this.finish();
			break;
		case R.id.goto_shop:
			ShopGoodsDetailActivity.this.finish();
			break;
		case R.id.contact_saler:
			/**
			 * 根据shopid获取联系电话
			 */
			getSalerPhone(shopgoodbean.getShop_id());
			break;
		case R.id.layout_add_to_collect:
			/**
			 * 收藏店铺
			 */
			if (shopgoodbean == null) {
				return;
			}
			collectshop(shopgoodbean.getShop_id());
			break;
		case R.id.btn_buy_now:
			int userid1 = UserUtil.getuserid(ShopGoodsDetailActivity.this);

			if (userid1 <= 0) {
				showToast("请先登录");
				Intent login = new Intent();
				login.setClass(ShopGoodsDetailActivity.this,
						ShopCartActivity.class);
				startActivity(login);
				return;
			}

			boolean exist1 = DBUtil
					.CheckShopCartGoodsExist(ShopGoodsDetailActivity.this,
							shopgoodbean.getId(), userid1);
			if (exist1) {
				Intent intent = new Intent();
				intent.setAction(GlobalConstant.UpdateShopCart);
				sendBroadcast(intent);

				Intent tocart = new Intent();
				tocart.putExtra("shopid", shopgoodbean.getShop_id());
				tocart.setClass(ShopGoodsDetailActivity.this,
						ShopCartActivity.class);
				startActivity(tocart);

			} else {

				DBUtil.deleteShopCart(ShopGoodsDetailActivity.this, userid1,
						shopgoodbean.getId());

				DBUtil.insertShopCart(ShopGoodsDetailActivity.this,
						shopgoodbean, userid1);

				Intent intent = new Intent();
				intent.setAction(GlobalConstant.UpdateShopCart);
				sendBroadcast(intent);

				Intent tocart = new Intent();
				tocart.putExtra("shopid", shopgoodbean.getShop_id());
				tocart.setClass(ShopGoodsDetailActivity.this,
						ShopCartActivity.class);
				startActivity(tocart);

			}
			break;
		case R.id.layout_add_to_shopcar:
			if (shopgoodbean == null) {
				return;
			}
			// 加入到购物车
			switch (from) {
			case 1:
				// 从商铺进来
				int userid = UserUtil.getuserid(ShopGoodsDetailActivity.this);

				if (userid <= 0) {
					showToast("请先登录");
					Intent login = new Intent();
					login.setClass(ShopGoodsDetailActivity.this,
							ShopCartActivity.class);
					startActivity(login);
					return;
				}

				Log.v(TAG, "id  " + id);

				Log.v(TAG, "shopgoodbean.getId()  " + shopgoodbean.getId());

				boolean exist = DBUtil.CheckShopCartGoodsExist(
						ShopGoodsDetailActivity.this, shopgoodbean.getId(),
						userid);
				if (exist) {
					Toast.makeText(ShopGoodsDetailActivity.this, "该商品已经在购物车了",
							Toast.LENGTH_SHORT).show();
				} else {

					DBUtil.deleteShopCart(ShopGoodsDetailActivity.this, userid,
							shopgoodbean.getId());

					DBUtil.insertShopCart(ShopGoodsDetailActivity.this,
							shopgoodbean, userid);

					Toast.makeText(ShopGoodsDetailActivity.this, "已添加到购物车",
							Toast.LENGTH_SHORT).show();

					Intent intent = new Intent();
					intent.setAction(GlobalConstant.UpdateShopCart);
					sendBroadcast(intent);
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
		btn_add_to_shopcar_layout = (LinearLayout) findViewById(R.id.btn_add_to_shopcar_layout);
		layout_add_to_collect = (LinearLayout) findViewById(R.id.layout_add_to_collect);
		btn_buy_now = (TextView) findViewById(R.id.btn_buy_now);
		contact_saler = (LinearLayout) findViewById(R.id.contact_saler);
		goto_shop = (LinearLayout) findViewById(R.id.goto_shop);

		xlvComment.addHeaderView(headview);

		xlvComment.setPullRefreshEnable(false);
		xlvComment.setPullLoadEnable(false);
	}

	@Override
	public void initListener() {
		goBackBtn.setOnClickListener(this);
		layout_add_to_shopcar.setOnClickListener(this);
		btn_buy_now.setOnClickListener(this);
		contact_saler.setOnClickListener(this);
		goto_shop.setOnClickListener(this);
		layout_add_to_collect.setOnClickListener(this);
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

									shopgoodbean = new ShopGoodBean(id,
											shop_id, user_id, goods_name,
											goods_num, goods_price,
											goods_detail, goods_log1,
											goods_log2, goods_log3,
											goods_status);

									shopgoodbean.setGoods_d1(goods_d1);
									shopgoodbean.setGoods_d2(goods_d2);
									shopgoodbean.setGoods_d3(goods_d3);
									shopgoodbean.setGoods_d4(goods_d4);
									shopgoodbean.setGoods_d5(goods_d5);
									shopgoodbean.setGoods_d6(goods_d6);

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
													ShopGoodsDetailActivity.this,
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
									int useropenshopid = UserUtil
											.getuseropenshopid(ShopGoodsDetailActivity.this);
									if (useropenshopid == shop_id) {
										btn_add_to_shopcar_layout
												.setVisibility(View.GONE);
									} else {
										btn_add_to_shopcar_layout
												.setVisibility(View.VISIBLE);
									}
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

			View view = LayoutInflater.from(ShopGoodsDetailActivity.this)
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
