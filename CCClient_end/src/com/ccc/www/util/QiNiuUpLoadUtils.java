package com.ccc.www.util;


import java.io.ByteArrayOutputStream;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.UrlSafeBase64;

public class QiNiuUpLoadUtils {
	private static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "UTF-8";
	private static String AccessKey = GlobalConstant.QINIU_AK;
	private static String SecretKey =  GlobalConstant.QINIU_SK;

//	public static String upLoadToQiuNiu(String filePath) {
//		return upLoadToQiuNiu(filePath, false);
//	}

//	public static String upLoadToQiuNiu(String filePath,
//			final boolean isUploadListener) {
//		String key = null;
//		try {
//			// 1 构造上传策略
//			JSONObject _json = new JSONObject();
//			long _dataline = System.currentTimeMillis() / 1000 + 3600;
//			_json.put("deadline", _dataline);// 有效时间为一个小时
//			_json.put("scope", "public");
//			String _encodedPutPolicy = UrlSafeBase64.encodeToString(_json
//					.toString().getBytes());
//			byte[] _sign = HmacSHA1Encrypt(_encodedPutPolicy, SecretKey);
//			String _encodedSign = UrlSafeBase64.encodeToString(_sign);
//			String _uploadToken = AccessKey + ':' + _encodedSign + ':'
//					+ _encodedPutPolicy;
//			// String SAVE_FILE_DIRECTORY = filePath;
//			Bitmap bmp = getBitmapFromNamePath(filePath, 480, 800,
//					Bitmap.Config.RGB_565);
//			byte[] data = Bitmap2Bytes(bmp);
//			bmp.recycle();
//			UUID uuid = UUID.randomUUID();
//			key = uuid.toString() + ".png";
//			UploadManager uploadManager = new UploadManager();
//			uploadManager.put(data, key, _uploadToken,
//					new UpCompletionHandler() {
//						@Override
//						public void complete(String key, ResponseInfo info,
//								JSONObject response) {
//							Log.e("qiniu============", info.toString());
//							if (!info.isOK()) {
//								key = null;
//							}
//							if (isUploadListener && info.isOK()) {
//								Intent intent = new Intent(
//										ACTION_UPLOAD_AVATAR_DONE);
//								LocalBroadcastManager.getInstance(
//										MyApplication.getInstance())
//										.sendBroadcast(intent);
//							}
//						}
//					}, null);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return key;
//	}
	
	
	public static void upLoadToQiuNiu1(String filePath,
			final QiNiuUploadInterface  loadface) {
		String key = null;
		try {
			// 1 构造上传策略
			JSONObject _json = new JSONObject();
			long _dataline = System.currentTimeMillis() / 1000 + 3600;
			_json.put("deadline", _dataline);// 有效时间为一个小时
			_json.put("scope", "public");
			String _encodedPutPolicy = UrlSafeBase64.encodeToString(_json
					.toString().getBytes());
			byte[] _sign = HmacSHA1Encrypt(_encodedPutPolicy, SecretKey);
			String _encodedSign = UrlSafeBase64.encodeToString(_sign);
			String _uploadToken = AccessKey + ':' + _encodedSign + ':'
					+ _encodedPutPolicy;
			// String SAVE_FILE_DIRECTORY = filePath;
			Bitmap bmp = getBitmapFromNamePath(filePath, 480, 800,
					Bitmap.Config.RGB_565);
			byte[] data = Bitmap2Bytes(bmp);
			bmp.recycle();
			UUID uuid = UUID.randomUUID();
			key = uuid.toString() + ".png";
			UploadManager uploadManager = new UploadManager();
			uploadManager.put(data, key, _uploadToken,
					new UpCompletionHandler() {
						@Override
						public void complete(String key, ResponseInfo info,
								JSONObject response) {
							Log.e("qiniu============", info.toString());
							if (!info.isOK()) {
								key = null;
							}else{
								loadface.returnkey(key);
							}
						}
					}, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 
	 * 这个签名方法找了半天 一个个对出来的、、、、程序猿辛苦啊、、、 使用 HMAC-SHA1 签名方法对对encryptText进行签名
	 * 
	 * @param encryptText
	 *            被签名的字符串
	 * @param encryptKey
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey)
			throws Exception {
		byte[] data = encryptKey.getBytes(ENCODING);
		// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
		SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
		// 生成一个指定 Mac 算法 的 Mac 对象
		Mac mac = Mac.getInstance(MAC_NAME);
		// 用给定密钥初始化 Mac 对象
		mac.init(secretKey);
		byte[] text = encryptText.getBytes(ENCODING);
		// 完成 Mac 操作
		return mac.doFinal(text);
	}

	private static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap getBitmapFromNamePath(String namePath, int width,
			int height, Bitmap.Config bitmapConfig) {
		if (!TextUtils.isEmpty(namePath)) {
			BitmapFactory.Options opts = null;
			if (width > 0 && height > 0) {
				opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				// BitmapFactory.decodeFile(namePath, opts);
				BitmapFactory.decodeFile(namePath, opts);
				// 计算图片缩放比例
				final int minSideLength = Math.min(width, height);
				opts.inSampleSize = computeSampleSize(opts, minSideLength,
						width * height);
				opts.inPreferredConfig = bitmapConfig;
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
				return BitmapFactory.decodeFile(namePath, opts);
			}
		}
		return null;
	}

	/**
	 * 计算图片采样大小，这段代码是从网上扒下来的。
	 * 参见网址http://orgcent.com/android-outofmemoryerror-load-big-image/
	 * 
	 * @param options
	 *            BitmapFactory的构建参数，该工参数中已经包含图片原始大小
	 * @param minSideLength
	 *            所需要的图片宽高度较小的那个
	 * @param maxNumOfPixels
	 *            所需要的图片最大像素
	 * @return 采样率
	 */
	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	/**
	 * 计算初始采样率 参见网址http://orgcent.com/android-outofmemoryerror-load-big-image/
	 * 
	 * @param options
	 *            BitmapFactory的构建参数，该工参数中已经包含图片原始大小
	 * @param minSideLength
	 *            所需要的图片宽高度较小的那个
	 * @param maxNumOfPixels
	 *            所需要的图片最大像素
	 * @return 采样率
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
