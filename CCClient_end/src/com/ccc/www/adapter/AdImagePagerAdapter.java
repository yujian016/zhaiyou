package com.ccc.www.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ccc.ccclient_end.R;
import com.ccc.www.bean.AdBean;
import com.ccc.www.bean.HomeAdImage;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AdImagePagerAdapter extends PagerAdapter {

	private ArrayList<AdBean> adImages = new ArrayList<AdBean>();
	private Activity activity;

	public AdImagePagerAdapter(ArrayList<AdBean> adImages, Activity activity) {
		super();
		this.adImages = adImages;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return adImages.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		View view = View.inflate(activity, R.layout.ad_image, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_ad);

		ImageLoader.getInstance().displayImage(
				adImages.get(position).getAd_image(), imageView,
				ImageLoaderOption.getoption());

		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
