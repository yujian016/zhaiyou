package com.ccc.www.navigation_activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.activity.BaseActivity;
import com.ccc.www.activity.CouponActivity;
import com.ccc.www.activity.DigitalActivity2;
import com.ccc.www.activity.DigitalRepairActivity;
import com.ccc.www.activity.EducationTrainActivity;
import com.ccc.www.activity.GamePlayActivity;
import com.ccc.www.activity.GroupBuyActivity;
import com.ccc.www.activity.LimitTimeSaleGoodsDetailActivity;
import com.ccc.www.activity.LoginActivity;
import com.ccc.www.activity.MsgActivity;
import com.ccc.www.activity.PrivateSupermarketDormitoryProxyActivity;
import com.ccc.www.activity.PrivateSupermarketSelectSchoolActivity;
import com.ccc.www.activity.RedEnvelope2Activity;
import com.ccc.www.activity.SchoolBuyGoodsActivity2;
import com.ccc.www.activity.ScoreShopActivity;
import com.ccc.www.activity.SecondaryTradeActivity;
import com.ccc.www.activity.SelectSchoolActivity;
import com.ccc.www.adapter.AdImagePagerAdapter;
import com.ccc.www.bean.AdBean;
import com.ccc.www.bean.HomeNoticeBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.ccc.www.util.Xutils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends BaseActivity {
	protected static final String TAG = "HomeActivity";
	Activity activity;

	private ViewPager vp_home_ad;
	private LinearLayout ad_home_dot_layout;
	int page = 1;
	private ArrayList<AdBean> adImages = new ArrayList<AdBean>();
	private View mySuperMarket_ll;
	private View digital_ll;
	private View schoolShop_ll;
	private View digital_edit_ll;
	private View education_ll;
	private View ershou_ll;
	private View game_ll;
	private View getYouHui_ll;
	private View scoreShop_ll;

	ImageButton ib_one;
	ImageButton ib_two;
	ImageButton ib_three;
	ImageButton ib_four;
	ImageButton ib_five;
	ImageButton ib_six;
	ImageButton ib_seven;
	ImageButton ib_eight;
	ImageButton ib_nine;

	private ImageButton ibGetSchool;

	ImageView home_redbag;
	ImageView home_tg_img;

	TextView tv_header_city;

	Main_Select_School_Dormitory_Broadcast main_Select_School_Dormitory_Broadcast = new Main_Select_School_Dormitory_Broadcast();

	List<HomeNoticeBean> allHomeNoticeBean = new ArrayList<HomeNoticeBean>();

	TextView homenotice_textview;

	Timer timerRedBagStatus = new Timer();
	Gson gson = new Gson();

	// 限时抢购
	TextView home_flash_sale_time;
	HorizontalScrollView home_flash_sale;
	LinearLayout home_flash_sale_linearlayout;

	TextView limittime_titme;

	View rootview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = HomeActivity.this;

		rootview = LayoutInflater.from(activity).inflate(
				R.layout.home_fragment, null);

		registerReceiver(main_Select_School_Dormitory_Broadcast,
				new IntentFilter(GlobalConstant.Main_Select_School_Dormitory));
		setContentView(R.layout.home_fragment);

		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_son_one_one:
			// 私人超市
			// Intent sockIntent = new Intent("SockReceiver");
			// activity.sendBroadcast(sockIntent);

			int hotelid = UserUtil.getprivatesmhotelid(HomeActivity.this);
			if (hotelid == 0) {
				Intent intent = new Intent();
				intent.setClass(HomeActivity.this,
						PrivateSupermarketSelectSchoolActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.setClass(HomeActivity.this,
						PrivateSupermarketDormitoryProxyActivity.class);
				intent.putExtra("hostel_id", hotelid);
				startActivity(intent);
			}

			break;
		case R.id.ib_one:
			// 私人超市
			// Intent sockIntent = new Intent("SockReceiver");
			// activity.sendBroadcast(sockIntent);

			int hotelid2 = UserUtil.getprivatesmhotelid(HomeActivity.this);
			if (hotelid2 == 0) {
				Intent intent = new Intent();
				intent.setClass(HomeActivity.this,
						PrivateSupermarketSelectSchoolActivity.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.setClass(HomeActivity.this,
						PrivateSupermarketDormitoryProxyActivity.class);
				intent.putExtra("hostel_id", hotelid2);
				startActivity(intent);
			}

			break;
		case R.id.ll_son_one_two:
			// 潮流数码
			Intent one_two_intent = new Intent(activity, DigitalActivity2.class);
			startActivity(one_two_intent);
			break;
		case R.id.ib_two:
			// 潮流数码
			Intent one_two_intent2 = new Intent(activity,
					DigitalActivity2.class);
			startActivity(one_two_intent2);
			break;
		case R.id.ll_son_one_three:
			// 校园购物
			Intent one_three_intent = new Intent(activity,
					SchoolBuyGoodsActivity2.class);
			startActivity(one_three_intent);
			break;
		case R.id.ib_three:
			// 校园购物
			Intent one_three_intent2 = new Intent(activity,
					SchoolBuyGoodsActivity2.class);
			startActivity(one_three_intent2);
			break;
		case R.id.ll_son_two_one:
			// 数码快修
			Intent shumakuaixiu = new Intent();
			shumakuaixiu.setClass(activity, DigitalRepairActivity.class);
			startActivity(shumakuaixiu);
			break;
		case R.id.ib_four:
			// 数码快修
			Intent shumakuaixiu2 = new Intent();
			shumakuaixiu2.setClass(activity, DigitalRepairActivity.class);
			startActivity(shumakuaixiu2);
			break;
		case R.id.ll_son_two_two:
			// 二手交易
			Intent ershou = new Intent();
			ershou.setClass(activity, SecondaryTradeActivity.class);
			startActivity(ershou);
			break;
		case R.id.ib_five:
			// 二手交易
			Intent ershou2 = new Intent();
			ershou2.setClass(activity, SecondaryTradeActivity.class);
			startActivity(ershou2);
			break;
		case R.id.ll_son_two_three:
			// 教育培训
			Intent jiaoyu = new Intent();
			jiaoyu.setClass(activity, EducationTrainActivity.class);
			startActivity(jiaoyu);
			break;
		case R.id.ib_six:
			// 教育培训
			Intent jiaoyu2 = new Intent();
			jiaoyu2.setClass(activity, EducationTrainActivity.class);
			startActivity(jiaoyu2);
			break;
		case R.id.ll_son_three_one:
			// 游戏代练
			Intent son_three_one_intent = new Intent(activity,
					GamePlayActivity.class);
			startActivity(son_three_one_intent);
			break;
		case R.id.ib_seven:
			// 游戏代练
			Intent son_three_one_intent2 = new Intent(activity,
					GamePlayActivity.class);
			startActivity(son_three_one_intent2);
			break;
		case R.id.ll_son_three_two:
			// 领取优惠
			Intent son_three_two_intent = new Intent(activity,
					CouponActivity.class);
			startActivity(son_three_two_intent);
			break;
		case R.id.ib_eight:
			// 领取优惠
			Intent son_three_two_intent2 = new Intent(activity,
					CouponActivity.class);
			startActivity(son_three_two_intent2);
			break;
		case R.id.ll_son_three_three:
			// 积分商城
			Intent son_three_three_intent = new Intent(activity,
					ScoreShopActivity.class);
			startActivity(son_three_three_intent);
			break;
//		case R.id.ib_nine:
//			// 积分商城
//			Intent son_three_three_intent2 = new Intent(activity,
//					ScoreShopActivity.class);
//			startActivity(son_three_three_intent2);
//			break;
		case R.id.ib_header_city:
			// 选择学校
			Intent select_school_intent = new Intent(activity,
					SelectSchoolActivity.class);
			startActivity(select_school_intent);
			activity.overridePendingTransition(
					R.anim.selectschool_activity_come_in, 0);
			break;
		case R.id.tv_header_city:
			// 选择学校
			Intent select_school_intent2 = new Intent(activity,
					SelectSchoolActivity.class);
			startActivity(select_school_intent2);
			activity.overridePendingTransition(
					R.anim.selectschool_activity_come_in, 0);
			break;
//		case R.id.ib_header_msg:
//			// 查看信息
//			Intent msg_intent = new Intent(activity, MsgActivity.class);
//			startActivity(msg_intent);
//			activity.overridePendingTransition(R.anim.msg_activity_come_in, 0);
//			break;
		case R.id.home_redbag:
			// 红包专区
			int useruid = UserUtil.getuserid(activity);
			if (useruid > 0) {
				Intent red_envelope = new Intent(activity,
						RedEnvelope2Activity.class);
				startActivity(red_envelope);
				activity.overridePendingTransition(R.anim.msg_activity_come_in,
						0);
			} else {
				Intent login = new Intent(activity, LoginActivity.class);
				startActivity(login);
				activity.overridePendingTransition(R.anim.msg_activity_come_in,
						0);
			}
			break;
		case R.id.home_tg_img://校园团购
			Intent group = new Intent(activity, GroupBuyActivity.class);
			startActivity(group);
			activity.overridePendingTransition(R.anim.msg_activity_come_in,
					0);
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
		// TODO Auto-generated method stub
		vp_home_ad = (ViewPager) findViewById(R.id.vp_home_ad);
		ad_home_dot_layout = (LinearLayout) findViewById(R.id.ad_home_dot_layout);

		mySuperMarket_ll = findViewById(R.id.ll_son_one_one);
		digital_ll = findViewById(R.id.ll_son_one_two);
		schoolShop_ll = findViewById(R.id.ll_son_one_three);

		digital_edit_ll = findViewById(R.id.ll_son_two_one);
		ershou_ll = findViewById(R.id.ll_son_two_two);
		education_ll = findViewById(R.id.ll_son_two_three);

		game_ll = findViewById(R.id.ll_son_three_one);
		getYouHui_ll = findViewById(R.id.ll_son_three_two);
		scoreShop_ll = findViewById(R.id.ll_son_three_three);

		ibGetSchool = (ImageButton) findViewById(R.id.ib_header_city);

		tv_header_city = (TextView) findViewById(R.id.tv_header_city);

		home_redbag = (ImageView) findViewById(R.id.home_redbag);
		home_tg_img = (ImageView) findViewById(R.id.home_tg_img);

		homenotice_textview = (TextView) findViewById(R.id.homenotice_textview);

		home_flash_sale_time = (TextView) findViewById(R.id.home_flash_sale_time);
		limittime_titme = (TextView) findViewById(R.id.limittime_titme);
//		home_flash_sale = (HorizontalScrollView) findViewById(R.id.home_flash_sale);
//		home_flash_sale_linearlayout = (LinearLayout) findViewById(R.id.home_flash_sale_linearlayout);

		ib_one = (ImageButton) findViewById(R.id.ib_one);
		ib_two = (ImageButton) findViewById(R.id.ib_two);
		ib_three = (ImageButton) findViewById(R.id.ib_three);
		ib_four = (ImageButton) findViewById(R.id.ib_four);
		ib_five = (ImageButton) findViewById(R.id.ib_five);
		ib_six = (ImageButton) findViewById(R.id.ib_six);
		ib_seven = (ImageButton) findViewById(R.id.ib_seven);
		ib_eight = (ImageButton) findViewById(R.id.ib_eight);
//		ib_nine = (ImageButton) findViewById(R.id.ib_nine);

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		mySuperMarket_ll.setOnClickListener(this);
		digital_ll.setOnClickListener(this);
		schoolShop_ll.setOnClickListener(this);

		digital_edit_ll.setOnClickListener(this);
		ershou_ll.setOnClickListener(this);
		education_ll.setOnClickListener(this);

		game_ll.setOnClickListener(this);
		getYouHui_ll.setOnClickListener(this);
		scoreShop_ll.setOnClickListener(this);

		ibGetSchool.setOnClickListener(this);

		tv_header_city.setOnClickListener(this);

		home_redbag.setOnClickListener(this);
		home_tg_img.setOnClickListener(this);

		ib_one.setOnClickListener(this);
		ib_two.setOnClickListener(this);
		ib_three.setOnClickListener(this);
		ib_four.setOnClickListener(this);
		ib_five.setOnClickListener(this);
		ib_six.setOnClickListener(this);
		ib_seven.setOnClickListener(this);
		ib_eight.setOnClickListener(this);
//		ib_nine.setOnClickListener(this);
	}

	@Override
	public void initdata() {
		/**
		 * 加载显示本地选择好的学校和宿舍
		 */
		String schoolname = UserUtil.getschoolname(HomeActivity.this);
		String hostelname = UserUtil.gethostelname(HomeActivity.this);
		String floorname = UserUtil.getfloorname(HomeActivity.this);
		String showcityname = schoolname + hostelname + floorname;
		if (!TextUtils.isEmpty(showcityname)) {
			tv_header_city.setText(schoolname + hostelname + floorname);
		} else {
			// 选择学校
			Intent select_school_intent2 = new Intent(activity,
					SelectSchoolActivity.class);
			startActivity(select_school_intent2);
			activity.overridePendingTransition(
					R.anim.selectschool_activity_come_in, 0);
		}

		getRepairAdData();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						int currentPageIndex = vp_home_ad.getCurrentItem();

						if (adImages.size() > 0) {
							if (currentPageIndex == adImages.size() - 1) {
								vp_home_ad.setCurrentItem(0);
							} else {
								currentPageIndex++;
								vp_home_ad.setCurrentItem(currentPageIndex);
							}
							updateDot();
						}
					}
				});

			}
		}, 4000, 4000);

		/**
		 * 加载公告数据
		 */
		loadnotice();

		/**
		 * 获取红包状态
		 */
		getRegBagStatus();

		/**
		 * 获取限时抢购
		 */
		getFlashSaleData();

		getFlashSaleTitle();

		/**
		 * 检查更新
		 */
		checkUpdate();

	}

	/**
	 * 检查更新
	 */
	private void checkUpdate() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		String url = GlobalConstant.CheckUpdate_URL;

		Log.v(TAG, "url  " + url);

		Xutils.loadData(HttpMethod.POST, url, null,
				new RequestCallBack<String>() {
					public void onSuccess(ResponseInfo<String> response) {
						dismissProgressDialog();
						String returnstr = response.result;
						Log.v(TAG, "checkUpdate  returnstr	 " + returnstr);
						if (TextUtils.isEmpty(returnstr)) {

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
											.getVersionCode(HomeActivity.this);

									if (version_code > versionCode) {
										// 有新版本
										popnewversion(version_name,
												down_load_url, update_date,
												update_context);

									}
								} catch (Exception e) {
									e.printStackTrace();

								}
							} catch (JSONException e) {
								e.printStackTrace();

							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();

					}
				});
	}

	private void popnewversion(String version_name, final String down_load_url,
			String update_date, String update_context) {

		View view2 = LayoutInflater.from(HomeActivity.this).inflate(
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

	/**
	 * 获取红包状态
	 */
	private void getRegBagStatus() {
		// home_redbag.setVisibility(View.VISIBLE);
		// if (true) {
		// return;
		// }

		// 获取红包是否结束
		timerRedBagStatus.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (!BaseUtils.isNetWork(getApplicationContext())) {
							return;
						}
						RequestParams params = new RequestParams();
						loadData(HttpMethod.POST,
								GlobalConstant.GET_REDBAG_INFO, params,
								new RequestCallBack<String>() {
									@Override
									public void onFailure(HttpException arg0,
											String arg1) {
									}

									@Override
									public void onSuccess(
											ResponseInfo<String> info) {
										String returnstr = info.result;
										Log.v(TAG, "returnstr  " + returnstr);
										if (BaseUtils.isEmpty(returnstr)) {
											if (returnstr
													.equalsIgnoreCase("null")) {
												home_redbag
														.setVisibility(View.GONE);
												timerRedBagStatuscount = 0;

												Intent intent = new Intent();
												intent.setAction(GlobalConstant.RegBagClode);
												sendBroadcast(intent);

												return;
											}
										} else {
											if (returnstr
													.equalsIgnoreCase("null")) {
												home_redbag
														.setVisibility(View.GONE);
												timerRedBagStatuscount = 0;

												Intent intent = new Intent();
												intent.setAction(GlobalConstant.RegBagClode);
												sendBroadcast(intent);

												return;
											}

											try {
												JSONObject json = new JSONObject(
														returnstr);
												int id = json.getInt("id");
												int odds = json.getInt("odds");
												int redbagmoney = json
														.getInt("red_packet_sub_money");
												int status = json
														.getInt("status");

												if (status == 0 || status == 2) {
													home_redbag
															.setVisibility(View.GONE);
												} else if (status == 1) {
													home_redbag
															.setVisibility(View.VISIBLE);

													currentredbagid = id;

													checkredbagover();
												}
											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
									}
								});
					}
				});
			}
		}, 5 * 1000, 5 * 1000);
	}

	int timerRedBagStatuscount = 0;
	String start_time = "";
	Timer timerRedBagOver = new Timer();

	int currentredbagid = 0;

	// 获取红包是否结束
	private void checkredbagover() {

		Log.v(TAG, "GET_REDBAG_IS_OVER  id " + currentredbagid);

		if (currentredbagid != 0) {

			RequestParams params = new RequestParams();
			params.addBodyParameter("red_packet_id", "" + currentredbagid);
			// if (timerRedBagStatuscount == 0) {
			// params.addBodyParameter("start_type", "" + 1);
			// } else {
			// params.addBodyParameter("start_type", "" + 0);
			// if (!TextUtils.isEmpty(start_time)) {
			// params.addBodyParameter("start_time", start_time);
			// }
			// }
			loadData(HttpMethod.POST, GlobalConstant.GET_REDBAG_IS_OVER,
					params, new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							Log.v(TAG, "GET_REDBAG_IS_OVER  onFailure " + arg1);
						}

						//
						@Override
						public void onSuccess(ResponseInfo<String> info) {
							String returnstr = info.result;
							Log.v(TAG, "returnstr  timer  " + returnstr);

							timerRedBagStatuscount++;

							if (BaseUtils.isEmpty(returnstr)) {
								//
							} else {
								try {
									JSONObject json = new JSONObject(returnstr);
									String code = json.getString("code");
									String msg = json.getString("msg");
									if (code.equalsIgnoreCase("0")
											|| code.equalsIgnoreCase("2")) {

										home_redbag.setVisibility(View.GONE);

									} else if (code.equalsIgnoreCase("1")) {
										// 红包金额没了 时间还没结束

									} else if (code.equalsIgnoreCase("4")) {
										start_time = msg;
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}
					});
		}

	}

	/**
	 * 获取广告图片
	 */
	private void getRepairAdData() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		params.addBodyParameter("ad_type", "1");
		loadData(HttpMethod.POST, GlobalConstant.GET_AD, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String json = info.result;
						Log.v(TAG, json);
						if (BaseUtils.isEmpty(json)) {
							showToast("加载失败");
						} else {
							adImages = gson.fromJson(json,
									new TypeToken<ArrayList<AdBean>>() {
									}.getType());

							if (adImages == null) {
								adImages = new ArrayList<AdBean>();
							}

							vp_home_ad.setAdapter(new AdImagePagerAdapter(
									adImages, HomeActivity.this));
							vp_home_ad.setCurrentItem(0);
							initDot();
							updateDot();
						}
					}
				});
	}

	private void initDot() {
		for (int i = 0; i < adImages.size(); i++) {
			View dot_view = new View(activity);
			LayoutParams params = new LayoutParams(10, 10);
			params.leftMargin = 5;
			params.rightMargin = 5;
			dot_view.setLayoutParams(params);
			dot_view.setBackgroundResource(R.drawable.selector_dot);
			ad_home_dot_layout.addView(dot_view);
		}
	}

	private void updateDot() {
		int currentPageIndex = vp_home_ad.getCurrentItem();
		for (int i = 0; i < ad_home_dot_layout.getChildCount(); i++) {
			if (i == currentPageIndex) {
				ad_home_dot_layout.getChildAt(i).setEnabled(true);
			} else {
				ad_home_dot_layout.getChildAt(i).setEnabled(false);
			}
		}
	}

	/**
	 * 首页左上角选择宿舍广播
	 */
	class Main_Select_School_Dormitory_Broadcast extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			String schoolname = UserUtil.getschoolname(HomeActivity.this);
			String hostelname = UserUtil.gethostelname(HomeActivity.this);
			String floorname = UserUtil.getfloorname(HomeActivity.this);

			tv_header_city.setText(schoolname + hostelname + floorname);

			Log.v(TAG, "tv_header_city.setText  " + schoolname + hostelname
					+ floorname);

		}
	}

	/**
	 * 公告自动切换
	 */

	int noticeshowposition = 0;

	private void autoChangeNotice() {

		initAnimation();

		mAnimatorSet = new AnimatorSet();
		mAnimatorSet.play(mFadeOutObjectAnimator).before(mFadeInObjectAnimator);

		mAnimatorSet.start();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mAnimatorSet.cancel();

						mAnimatorSet.start();
					}
				});
			}
		}, 5 * 1000, 5 * 1000);
	}

	/**
	 * 公告动画
	 */
	private ObjectAnimator mFadeInObjectAnimator;
	private ObjectAnimator mFadeOutObjectAnimator;
	private AnimatorSet mAnimatorSet;

	private void initAnimation() {
		mFadeOutObjectAnimator = ObjectAnimator.ofFloat(homenotice_textview,
				"translationY", 0, -80);
		mFadeOutObjectAnimator.setDuration(500);
		mFadeOutObjectAnimator
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						// float value = (float) animation.getAnimatedValue();
						homenotice_textview.setAlpha(1 + 10.0f / 150);
					}
				});
		mFadeOutObjectAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {

				if (noticeshowposition == allHomeNoticeBean.size()) {
					noticeshowposition = 0;
				}

				if (allHomeNoticeBean.size() > noticeshowposition) {
					String content = allHomeNoticeBean.get(noticeshowposition)
							.getNotice_context();
					homenotice_textview.setText(content);
				}
				noticeshowposition++;
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});

		mFadeInObjectAnimator = ObjectAnimator.ofFloat(homenotice_textview,
				"translationY", 80, 0);
		mFadeInObjectAnimator.setDuration(500);
		mFadeInObjectAnimator.setStartDelay(100);
		mFadeInObjectAnimator
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						// float value = (float) animation.getAnimatedValue();
						homenotice_textview.setAlpha(1 - 10.0f / 150);
					}
				});

	}

	/**
	 * 加载公告
	 */
	private void loadnotice() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		loadData(HttpMethod.POST, GlobalConstant.GET_NOTICE, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String json = info.result;
						Log.v(TAG, "json  " + json);
						if (BaseUtils.isEmpty(json)) {
							showToast("加载失败");
						} else {
							try {

								allHomeNoticeBean.removeAll(allHomeNoticeBean);

								JSONArray array = new JSONArray(json);
								for (int i = 0; i < array.length(); i++) {
									JSONObject obj = array.getJSONObject(i);
									int id = obj.getInt("id");
									String notice_context = obj
											.getString("notice_context");
									String handle_time = obj
											.getString("handle_time");
									int status = obj.getInt("status");

									if (status == 0) {
										HomeNoticeBean bean = new HomeNoticeBean(
												id, notice_context,
												handle_time, status);
										allHomeNoticeBean.add(bean);
									}
								}

								if (allHomeNoticeBean.size() > 0) {
									autoChangeNotice();
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(main_Select_School_Dormitory_Broadcast);
		super.onDestroy();
	}

	/**
	 * 获取限时抢购标题
	 */

	private void getFlashSaleTitle() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		loadData(HttpMethod.POST, GlobalConstant.GET_ALL_LIMITSALE_Title,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String json = info.result;
						Log.v(TAG, "getFlashSaleTitle  json  " + json);

						try {
							JSONArray array = new JSONArray(json);
							for (int i = 0; i < array.length(); i++) {
								JSONObject obj = array.getJSONObject(0);
								String tltitle = obj.getString("tltitle");
								limittime_titme.setText(tltitle);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				});
	}

	/**
	 * 获取限时抢购
	 */

	List<LimitSaleGoodsInfo> allLimitSaleGoodsInfo = new ArrayList<LimitSaleGoodsInfo>();

	private void getFlashSaleData() {
		if (!BaseUtils.isNetWork(getApplicationContext())) {
			showToast("请检查您的网络");
			return;
		}

		showLoading2("正在加载");

		RequestParams params = new RequestParams();
		loadData(HttpMethod.POST, GlobalConstant.GET_ALL_LIMITSALE_GOODS,
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						dismissProgressDialog();
						showToast("加载失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						dismissProgressDialog();
						String json = info.result;
						Log.v(TAG, "getFlashSaleData  json  " + json);
						if (BaseUtils.isEmpty(json)) {
							showToast("加载失败");
						} else {
							try {
								JSONArray array = new JSONArray(json);
								for (int i = 0; i < array.length(); i++) {
									JSONObject obj = array.getJSONObject(i);
									int id = obj.getInt("id");
									int shop_id = obj.getInt("shop_id");
									String goods_log1 = obj
											.getString("goods_log1");

									float goods_price = (float) obj
											.getDouble("goods_price");

									LimitSaleGoodsInfo goodsinfo = new LimitSaleGoodsInfo();

									goodsinfo.id = id;
									goodsinfo.shop_id = shop_id;
									goodsinfo.goods_log1 = goods_log1;
									goodsinfo.goods_price = goods_price;
									allLimitSaleGoodsInfo.add(goodsinfo);
								}

								HSVAdapter adapter = new HSVAdapter(activity,
										allLimitSaleGoodsInfo);

								for (int i = 0; i < adapter.getCount(); i++) {

									final int position = i;

									View view = adapter.getView(i, null, null);
									view.setPadding(2, 0, 2, 0);
									view.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											LimitSaleGoodsInfo info = allLimitSaleGoodsInfo
													.get(position);
											Intent intent = new Intent();
											intent.putExtra("id", info.id);
											intent.putExtra("shopid",
													info.shop_id);
											intent.setClass(
													activity,
													LimitTimeSaleGoodsDetailActivity.class);
											startActivity(intent);
										}
									});
//									home_flash_sale_linearlayout.addView(view,
//											new LinearLayout.LayoutParams(280,
//													340));
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				});
	}

	class LimitSaleGoodsInfo {
		int id;
		int shop_id;
		String goods_log1;
		float goods_price;
	}

	class HSVAdapter extends BaseAdapter {

		private List<LimitSaleGoodsInfo> list;
		private Context context;

		public HSVAdapter(Context context, List<LimitSaleGoodsInfo> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int location) {
			return list.get(location);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int location, View view, ViewGroup arg2) {

			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(context).inflate(
						R.layout.image_home_flash_sale, null);
				holder.image = (ImageView) view.findViewById(R.id.image);
				holder.goods_price = (TextView) view
						.findViewById(R.id.goods_price);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			LimitSaleGoodsInfo info = list.get(location);
			String log = info.goods_log1;
			if (!log.startsWith("http")) {
				log = GlobalConstant.SERVER_URL + log;
			}
			ImageLoader.getInstance().displayImage(log, holder.image,
					ImageLoaderOption.getoption());

			holder.goods_price.setText("￥" + info.goods_price);

			return view;
		}

		class ViewHolder {
			ImageView image;
			TextView goods_price;
		}

	}

}
