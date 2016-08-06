package com.ccc.www.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.ccc.www.bean.SockBean;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class ContextUtil extends Application {

	private String TAG = "ContextUtil";

	private static Context instance;

	// 临时存储代理进货商品数据
	private static List<SockBean> allSockBean;

	public static void setallSockBeannull() {
		allSockBean = null;
	}

	public static void setallSockBean(List<SockBean> allsock) {
		if (allSockBean == null) {
			allSockBean = new ArrayList<SockBean>();
		}
		for (int i = 0; i < allsock.size(); i++) {
			allSockBean.add(allsock.get(i));
		}
	}

	public static List<SockBean> getallSockBean() {
		if (allSockBean == null) {
			allSockBean = new ArrayList<SockBean>();
		}
		return allSockBean;
	}

	public static File filename, filename1;

	public static Context getInstance() {
		return ContextUtil.instance;
	}

	public static List<String> imgpaths;

	public static List<String> getImgpaths() {
		return imgpaths;
	}

	public static void setImgpaths(List<String> imgpaths) {
		ContextUtil.imgpaths = imgpaths;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		instance = getApplicationContext();

		File file_imgpath = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "imageloadercache");

		if (!file_imgpath.exists()) {
			try {
				file_imgpath.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// File cacheDir = StorageUtils.getOwnCacheDirectory(
		// getApplicationContext(), "imageloader/Cache");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				instance)
				// .memoryCacheExtraOptions(480, 800)
				// Can slow ImageLoader, use it carefully (Better don't use
				// it)/设置缓存的详细信息，最好不要设置这个
				.threadPoolSize(5)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				// You can pass your own memory cache
				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(100 * 1024 * 1024)
				.diskCacheSize(350 * 1024 * 1024)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCacheFileCount(300)
				// 缓存的文件数量
				.diskCache(new UnlimitedDiscCache(file_imgpath))
				// 自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(instance, 8 * 1000, 40 * 1000)) // connectTimeout
				.writeDebugLogs() // Remove for release app
				.build();// 开始构建

		ImageLoader.getInstance().init(config);

	}
}