package com.ccc.www.activity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.PrivateSuperMarketGoodsBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 私人超市的购物车
 * 
 * @author Administrator
 * 
 */
public class PrivateSupermarketCartActivity extends BaseActivity {

	String TAG = "PrivateSupermarketCartActivity";

	ImageButton ib_select_school_goback;
	ListView activity_proxystockcart_lv;

	int userid;
	int supermaket_id;

	List<PrivateSuperMarketGoodsBean> allsock = new ArrayList<PrivateSuperMarketGoodsBean>();

	Adapter adapter = new Adapter();

	TextView mycart_selelctcount;
	TextView mycart_allmoney;
	TextView mycart_createorder;

	View rootview;

	// 收货人姓名
	String get_goods_person_name = "";
	// 收货人联系电话
	String get_goods_person_phone = "";
	// 收货人地址
	String get_goods_person_address = "";
	// 订单总金额
	double order_sum_money = 0.0f;
	// 随机码
	String rand_no = "";

	// 商品名称
	String businessname = "";
	// 商品描述
	String businessdesc = "";

	PaySuccessToMyOrder paySuccessToMyOrder = new PaySuccessToMyOrder();

	/**
	 * 支付成功后关闭页面
	 * 
	 * @author Administrator
	 * 
	 */
	class PaySuccessToMyOrder extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			PrivateSupermarketCartActivity.this.finish();
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(paySuccessToMyOrder);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		supermaket_id = getIntent().getIntExtra("supermaket_id", 0);

		Log.v(TAG, "supermaket_id  " + supermaket_id);

		userid = UserUtil.getuserid(this);

		registerReceiver(paySuccessToMyOrder, new IntentFilter(
				GlobalConstant.PaySuccessToMyOrder));

		rootview = LayoutInflater.from(this).inflate(
				R.layout.activity_proxystockcart, null);

		setContentView(R.layout.activity_privatesupermarketcart);

		initview();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_select_school_goback:
			PrivateSupermarketCartActivity.this.finish();
			break;
		case R.id.mycart_createorder:

			List<PrivateSuperMarketGoodsBean> allchoicesock = new ArrayList<PrivateSuperMarketGoodsBean>();

			boolean havecheck = false;
			for (int i = 0; i < allsock.size(); i++) {
				PrivateSuperMarketGoodsBean sock = allsock.get(i);
				boolean ischeck = sock.isCheck();
				if (ischeck) {
					if (sock.getCount() > 0) {
						havecheck = true;
						break;
					}
				}
			}

			for (int i = 0; i < allsock.size(); i++) {
				PrivateSuperMarketGoodsBean sock = allsock.get(i);
				boolean ischeck = sock.isCheck();
				if (ischeck) {
					if (sock.getCount() > 0) {
						allchoicesock.add(sock);
					}
				}
			}

			if (havecheck && allchoicesock.size() > 0) {
				Intent tocartorder = new Intent();
				tocartorder.putExtra("userid", userid);
				tocartorder.putExtra("supermaket_id", supermaket_id);
				Bundle bundle = new Bundle();
				bundle.putSerializable("allchoicesock",
						(Serializable) allchoicesock);
				tocartorder.putExtras(bundle);

				tocartorder.setClass(PrivateSupermarketCartActivity.this,
						PrivateSupermarketCartSubmitOrderActivity.class);
				startActivity(tocartorder);
			} else {
				showToast("请选择要购买商品并且商品数目大于0");
			}

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
		ib_select_school_goback = (ImageButton) findViewById(R.id.ib_select_school_goback);
		activity_proxystockcart_lv = (ListView) findViewById(R.id.activity_proxystockcart_lv);
		activity_proxystockcart_lv.setAdapter(adapter);

		mycart_selelctcount = (TextView) findViewById(R.id.mycart_selelctcount);
		mycart_allmoney = (TextView) findViewById(R.id.mycart_allmoney);
		mycart_createorder = (TextView) findViewById(R.id.mycart_createorder);
	}

	@Override
	public void initListener() {
		ib_select_school_goback.setOnClickListener(this);
		mycart_createorder.setOnClickListener(this);
	}

	@Override
	public void initdata() {

		double allmoney = 0.0f;

		allsock = DBUtil.getPrivateSupermarketCart(this, userid);
		for (int i = 0; i < allsock.size(); i++) {
			allsock.get(i).setCheck(true);
			double money = allsock.get(i).getCount()
					* allsock.get(i).getGoods_price();
			allmoney = allmoney + money;
		}

		mycart_selelctcount.setText(allsock.size() + "");

		calculateTotalMoney();

		adapter.notifyDataSetChanged();
	}

	class Adapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allsock.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return allsock.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View view, ViewGroup arg2) {
			ViewHolder holder = null;
			if (view == null) {
				holder = new ViewHolder();
				view = LayoutInflater.from(PrivateSupermarketCartActivity.this)
						.inflate(R.layout.item_privatesupermarketcart, null);
				holder.item_proxysockcart_check = (ImageView) view
						.findViewById(R.id.item_proxysockcart_check);
				holder.iv_sock_log = (ImageView) view
						.findViewById(R.id.iv_sock_log);
				holder.tv_sock_name = (TextView) view
						.findViewById(R.id.tv_sock_name);
				holder.tv_sock_price = (TextView) view
						.findViewById(R.id.tv_sock_price);
				holder.iv_sock_minus = (ImageView) view
						.findViewById(R.id.iv_sock_minus);
				holder.iv_sock_plus = (ImageView) view
						.findViewById(R.id.iv_sock_plus);
				holder.tv_sock_count = (TextView) view
						.findViewById(R.id.tv_sock_count);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			final PrivateSuperMarketGoodsBean sock = allsock.get(position);

			final boolean ischeck = sock.isCheck();

			Log.v(TAG, " position " + position + "  check  " + ischeck);

			if (ischeck) {
				holder.item_proxysockcart_check
						.setImageResource(R.drawable.icon_checked_01);
			} else {
				holder.item_proxysockcart_check
						.setImageResource(R.drawable.icon_checked_white_01);
			}

			holder.item_proxysockcart_check
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if (ischeck) {
								allsock.get(position).setCheck(false);
							} else {
								allsock.get(position).setCheck(true);
							}
							// 重新计算总价
							calculateTotalMoney();

							// 修改已选择的商品
							int selectcount = 0;
							for (int i = 0; i < allsock.size(); i++) {
								if (allsock.get(i).isCheck()) {
									selectcount++;
								}
							}
							mycart_selelctcount.setText(selectcount + "");

							notifyDataSetChanged();
						}
					});

			ImageLoader.getInstance().displayImage(sock.getGoods_log(),
					holder.iv_sock_log, ImageLoaderOption.getoption());

			holder.tv_sock_name.setText(sock.getGoods_name());
			holder.tv_sock_price.setText("￥" + sock.getGoods_price() + "元");

			holder.tv_sock_count.setText(sock.getCount() + "");

			holder.iv_sock_minus.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					int count = sock.getCount();
					if (count > 0) {
						count--;
					}
					sock.setCount(count);

					if (count == 0) {
						allsock.get(position).setCheck(false);
						;
					}

					// 修改数据库数量
					DBUtil.updatePrivateSupermarketCartCount(
							PrivateSupermarketCartActivity.this, sock, userid);

					// 发广播修改代理进货界面数量变化
					Intent updateProxyStockCartCount = new Intent();
					updateProxyStockCartCount
							.setAction(GlobalConstant.UpdatePrivateSuperMarketCartCount);
					sendBroadcast(updateProxyStockCartCount);

					// 重新计算总价
					calculateTotalMoney();

					notifyDataSetChanged();
				}
			});

			holder.iv_sock_plus.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int count = sock.getCount();
					count++;
					sock.setCount(count);

					allsock.get(position).setCheck(true);

					// 修改数据库数量
					DBUtil.updatePrivateSupermarketCartCount(
							PrivateSupermarketCartActivity.this, sock, userid);
					// 发广播修改代理进货界面数量变化
					Intent updateProxyStockCartCount = new Intent();
					updateProxyStockCartCount
							.setAction(GlobalConstant.UpdatePrivateSuperMarketCartCount);
					sendBroadcast(updateProxyStockCartCount);

					// 重新计算总价
					calculateTotalMoney();

					notifyDataSetChanged();
				}
			});

			return view;
		}

		class ViewHolder {
			ImageView item_proxysockcart_check;
			ImageView iv_sock_log;
			TextView tv_sock_name;
			TextView tv_sock_price;
			ImageView iv_sock_minus;
			ImageView iv_sock_plus;
			TextView tv_sock_count;
		}
	}

	/**
	 * 计算总金额
	 */
	private double calculateTotalMoney() {
		double allmoney = 0.0f;
		for (int i = 0; i < allsock.size(); i++) {
			if (allsock.get(i).isCheck()) {
				double money = allsock.get(i).getCount()
						* allsock.get(i).getGoods_price();
				allmoney = allmoney + money;
			}
		}

		DecimalFormat df = new DecimalFormat("######0.00");

		String allmoneyStr = df.format(allmoney);

		mycart_allmoney.setText("需支付:" + allmoneyStr + "元");

		return Float.parseFloat(allmoneyStr);

	}

	PopupWindow mPopupWindow;

	private void dissmisspopwindow() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

	// /**
	// * 支付信息确认
	// *
	// * @param KaizenId
	// */
	// private void payinfoConfirm(String useraddr, final float balance) {
	//
	// View view1 = LayoutInflater.from(PrivateSupermarketCartActivity.this)
	// .inflate(R.layout.pop_proxystocktip, null);
	// mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
	// LayoutParams.MATCH_PARENT);
	// mPopupWindow.setFocusable(true);
	//
	// final EditText pop_proxystocktip_name = (EditText) view1
	// .findViewById(R.id.pop_proxystocktip_name);
	// final EditText pop_proxystocktip_phone = (EditText) view1
	// .findViewById(R.id.pop_proxystocktip_phone);
	// final EditText pop_proxystocktip_addr = (EditText) view1
	// .findViewById(R.id.pop_proxystocktip_addr);
	// TextView pop_proxystocktip_money = (TextView) view1
	// .findViewById(R.id.pop_proxystocktip_money);
	// TextView pop_proxystocktip_cancel = (TextView) view1
	// .findViewById(R.id.pop_proxystocktip_cancel);
	// TextView pop_proxystocktip_pay = (TextView) view1
	// .findViewById(R.id.pop_proxystocktip_pay);
	// TextView pop_proxystocktip_balance = (TextView) view1
	// .findViewById(R.id.pop_proxystocktip_balance);
	//
	// pop_proxystocktip_balance.setText(balance + "元");
	//
	// pop_proxystocktip_addr.setText(useraddr);
	//
	// order_sum_money = calculateTotalMoney();
	// pop_proxystocktip_money.setText(order_sum_money + "元");
	//
	// pop_proxystocktip_cancel.setOnClickListener(new OnClickListener() {
	// public void onClick(View v) {
	// dissmisspopwindow();
	// }
	// });
	// pop_proxystocktip_pay.setOnClickListener(new OnClickListener() {
	// public void onClick(View v) {
	//
	// get_goods_person_name = pop_proxystocktip_name.getText()
	// .toString().trim();
	// get_goods_person_phone = pop_proxystocktip_phone.getText()
	// .toString().trim();
	// get_goods_person_address = pop_proxystocktip_addr.getText()
	// .toString().trim();
	//
	// if (TextUtils.isEmpty(get_goods_person_name)) {
	// showToast("请输入收货人姓名");
	// pop_proxystocktip_name.requestFocus();
	// return;
	// }
	//
	// if (TextUtils.isEmpty(get_goods_person_phone)) {
	// showToast("请输入收货人联系电话");
	// pop_proxystocktip_phone.requestFocus();
	// return;
	// }
	//
	// if (TextUtils.isEmpty(get_goods_person_address)) {
	// showToast("请输入收货地址");
	// pop_proxystocktip_addr.requestFocus();
	// return;
	// }
	//
	// // 调支付宝之前的数据准备
	// List<PrivateSuperMarketGoodsBean> selectsocks = new
	// ArrayList<PrivateSuperMarketGoodsBean>();
	// for (int i = 0; i < allsock.size(); i++) {
	// PrivateSuperMarketGoodsBean sock = allsock.get(i);
	// boolean ischeck = sock.isCheck();
	// if (ischeck) {
	// if (sock.getCount() > 0) {
	// selectsocks.add(sock);
	// }
	// }
	// }
	//
	// businessname = "";
	// for (int i = 0; i < selectsocks.size(); i++) {
	// if (i == 0) {
	// businessname = "(私人超市)"
	// + selectsocks.get(i).getGoods_name();
	// } else {
	// businessname = businessname + ","
	// + selectsocks.get(i).getGoods_name();
	// }
	// }
	// businessdesc = get_goods_person_name + "的购物订单";
	//
	// // 判断余额是否足够
	// if (balance >= order_sum_money) {
	// // 使用余额支付
	// /**
	// *
	// */
	// submitorder(0);
	// } else {
	// /**
	// * 调用支付宝
	// */
	// boolean alipayinstall = AlipayUtil
	// .checkAlipayInstall(PrivateSupermarketCartActivity.this);
	// if (alipayinstall) {
	// checkalipay();
	// } else {
	// showToast("请先安装支付宝APP，并登录您的支付宝帐号");
	// }
	// }
	//
	// }
	// });
	// mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
	// mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
	// }

}
