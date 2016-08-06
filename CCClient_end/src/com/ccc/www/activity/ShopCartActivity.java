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
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.ShopGoodBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 商铺购物车
 * 
 * @author Administrator
 * 
 */
public class ShopCartActivity extends BaseActivity {

	String TAG = "ShopCartActivity";

	ImageButton ib_select_school_goback;
	ListView activity_proxystockcart_lv;

	int userid;
	int shopid;

	List<ShopGoodBean> allsock = new ArrayList<ShopGoodBean>();

	Adapter adapter = new Adapter();

	TextView mycart_selelctcount;
	TextView mycart_allmoney;
	TextView mycart_createorder;

	View rootview;

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
			ShopCartActivity.this.finish();
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

		userid = UserUtil.getuserid(this);

		shopid = getIntent().getIntExtra("shopid", 0);

		registerReceiver(paySuccessToMyOrder, new IntentFilter(
				GlobalConstant.PaySuccessToMyOrder));

		rootview = LayoutInflater.from(this).inflate(
				R.layout.activity_shopcart, null);

		setContentView(R.layout.activity_shopcart);

		initview();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_select_school_goback:
			ShopCartActivity.this.finish();
			break;
		case R.id.mycart_createorder:

			List<ShopGoodBean> allchoicesock = new ArrayList<ShopGoodBean>();

			boolean havecheck = false;
			for (int i = 0; i < allsock.size(); i++) {
				ShopGoodBean sock = allsock.get(i);
				boolean ischeck = sock.isCheck();
				if (ischeck) {
					if (sock.getCount() > 0) {
						havecheck = true;
						break;
					}
				}
			}

			for (int i = 0; i < allsock.size(); i++) {
				ShopGoodBean sock = allsock.get(i);
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
				tocartorder.putExtra("shopid", shopid);
				Bundle bundle = new Bundle();
				bundle.putSerializable("allchoicesock",
						(Serializable) allchoicesock);
				tocartorder.putExtras(bundle);

				tocartorder.setClass(ShopCartActivity.this,
						ShopCartSubmitOrderActivity.class);
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

		float allmoney = 0.0f;

		allsock = DBUtil.getShopCart(this, userid, shopid);
		for (int i = 0; i < allsock.size(); i++) {

			Log.v(TAG, "allsock  getGoods_cate_id  "
					+ allsock.get(i).getGoods_cate_id());

			allsock.get(i).setCheck(true);
			float money = allsock.get(i).getCount()
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
				view = LayoutInflater.from(ShopCartActivity.this).inflate(
						R.layout.item_privatesupermarketcart, null);
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

			final ShopGoodBean sock = allsock.get(position);

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

			ImageLoader.getInstance().displayImage(
					GlobalConstant.SERVER_URL + sock.getGoods_log1(),
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
					}

					// 修改数据库数量
					DBUtil.updateShopCartCount(ShopCartActivity.this, sock);

					// 发广播修改代理进货界面数量变化
					Intent updateProxyStockCartCount = new Intent();
					updateProxyStockCartCount
							.setAction(GlobalConstant.UpdateShopCart);
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
					DBUtil.updateShopCartCount(ShopCartActivity.this, sock);
					// 发广播修改代理进货界面数量变化
					Intent updateProxyStockCartCount = new Intent();
					updateProxyStockCartCount
							.setAction(GlobalConstant.UpdateShopCart);
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
	private float calculateTotalMoney() {
		float allmoney = 0.0f;
		for (int i = 0; i < allsock.size(); i++) {
			if (allsock.get(i).isCheck()) {
				float money = allsock.get(i).getCount()
						* allsock.get(i).getGoods_price();
				allmoney = allmoney + money;
			}
		}

		DecimalFormat df = new DecimalFormat("######0.00");

		String allmoneyStr = df.format(allmoney);

		mycart_allmoney.setText("需支付:" + allmoneyStr + "元");

		return Float.parseFloat(allmoneyStr);
	}

}
