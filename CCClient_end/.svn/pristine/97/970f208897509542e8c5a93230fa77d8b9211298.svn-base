package com.ccc.www.adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccc.ccclient_end.R;
import com.ccc.www.activity.LoginActivity;
import com.ccc.www.activity.SubmitGroupBuyOrderActivity;
import com.ccc.www.bean.GoodsBean;
import com.ccc.www.bean.ShopGoodBean;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GroupBuyGoodsAdapter extends BaseAdapter {

	List<ShopGoodBean> shopCategorys;

	Context context;

	public GroupBuyGoodsAdapter(Context context,
			List<ShopGoodBean> shopCategorys) {
		this.context = context;
		this.shopCategorys = shopCategorys;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return shopCategorys.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return shopCategorys.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holer = null;
		if (view == null) {
			holer = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.group_buy_goods_lv_item, null);
			holer.iv_gb_goods_logo = (ImageView) view.findViewById(R.id.iv_gb_goods_logo);
			holer.tv_gb_goods_name=(TextView) view.findViewById(R.id.tv_gb_goods_name);
			holer.tv_gb_goods_person=(TextView) view.findViewById(R.id.tv_gb_goods_person);
			holer.tv_gb_goods_price=(TextView) view.findViewById(R.id.tv_gb_goods_price);
			holer.btn_add_rb=(Button) view.findViewById(R.id.btn_add_rb);
			view.setTag(holer);
		} else {
			holer = (ViewHolder) view.getTag();
		}
		
		
		final ShopGoodBean goods = shopCategorys.get(position);
		
		String log = goods.getGoods_log1();
		if (!log.startsWith("http")) {
			log = GlobalConstant.SERVER_URL + log;
		}
		ImageLoader.getInstance().displayImage(log, holer.iv_gb_goods_logo,
				ImageLoaderOption.getoption());
		holer.tv_gb_goods_name.setText(goods.getGoods_name());
		holer.tv_gb_goods_person.setText("参团率%"+goods.getGroup_buy_person_num());
		holer.tv_gb_goods_price.setText("￥"+goods.getGroup_buy_price());
		if(goods.getGroup_buy_status()==1){
			holer.btn_add_rb.setText("立即购买");
		}else if(goods.getGroup_buy_status()==0){
			holer.btn_add_rb.setText("暂未开团");
		}

		
		holer.btn_add_rb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// 领取优惠
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("bean", (Serializable) goods);
				intent.putExtras(bundle);
				intent.setAction(GlobalConstant.BuyGroupGoods);
				context.sendBroadcast(intent);

			}
		});
		
		return view;
	}

	class ViewHolder {
		ImageView iv_gb_goods_logo;
		TextView tv_gb_goods_name;
		TextView tv_gb_goods_person;
		TextView tv_gb_goods_price;
		Button btn_add_rb;
	}

	
	
	
}
