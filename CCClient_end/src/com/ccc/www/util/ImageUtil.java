package com.ccc.www.util;

import android.app.Activity;
import android.content.Intent;

import com.ccc.www.activity.ImagePagerActivity;

public class ImageUtil {

	public static void toShowImages(Activity activity, String[] urls,
			int position) {
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		Intent intent = new Intent(activity, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		activity.startActivity(intent);
	}

}
