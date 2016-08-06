package com.ccc.www.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

public class PhotoUtil {

	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	public static Bitmap CompresPhoto(String filePath) {
		Bitmap bmp = BitmapFactory.decodeFile(filePath);
		double maxSize = 480.00;// 限制的文件大小
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		// 将字节换成KB
		double mid = b.length / 1024;
		// 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
		if (mid > maxSize) {
			// 获取bitmap大小 是允许最大大小的多少倍
			double i = mid / maxSize;
			// 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
			// （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
			bmp = zoomImage(bmp, bmp.getWidth() / Math.abs(i), bmp.getHeight()
					/ Math.abs(i));
		}
		return bmp;

	}

	public static Bitmap CompresPhoto_bitmap(Bitmap bmp ) {
		double maxSize = 480.00;// 限制的文件大小
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		// 将字节换成KB
		double mid = b.length / 1024;
		// 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
		if (mid > maxSize) {
			// 获取bitmap大小 是允许最大大小的多少倍
			double i = mid / maxSize;
			// 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
			// （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
			bmp = zoomImage(bmp, bmp.getWidth() / Math.abs(i), bmp.getHeight()
					/ Math.abs(i));
		}
		return bmp;

	}

	/** ѹ��ͼƬ */
	public static Bitmap CompresPhoto_2(String filePath, int width, int height) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		Bitmap bm = BitmapFactory.decodeFile(filePath, options);// ѹ��ͼƬ
		if (bm == null) {
			return null;
		}
		int degree = readPictureDegree(filePath);// ����ͼƬ��ת
		bm = rotateBitmap(bm, degree);
		int bytecount = bm.getRowBytes() * bm.getHeight(); // ��ȡ�ֽ���

		int optionyasuo = 80;
		while (bytecount > 2 * 1024 * 1024) {
			Log.v("optionyasuo  ", "optionyasuo    " + optionyasuo);

			if (optionyasuo == 10) {
				return null;
			} else {
				optionyasuo = optionyasuo - 10;
				ByteArrayOutputStream baos = null;
				try {
					baos = new ByteArrayOutputStream();
					bm.compress(Bitmap.CompressFormat.JPEG, optionyasuo, baos); // 70

					ByteArrayInputStream isBm = new ByteArrayInputStream(
							baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
					bm = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
					bytecount = bm.getRowBytes() * bm.getHeight(); // ��ȡ�ֽ���
				} finally {
					try {
						if (baos != null)
							baos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return bm;
	}

	// -------------------------------------------------------------------
	/** �鿴ͼƬ��ת�Ƕ� */
	private static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/** ��ȡѹ���� */
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}

	/** ����ת��ͼƬ�ָ���Ƕ� */
	private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
		if (bitmap == null)
			return null;

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		// Setting post rotate to 90
		Matrix mtx = new Matrix();
		mtx.postRotate(rotate);
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}

	/**
	 * ��Bitmap����ΪFile
	 * 
	 * @param bitmap
	 * @param file
	 */
	public static void SaveBitmapFile(Bitmap bitmap, File file) {
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��bitMap��һ���ļ����
	 * 
	 * @param filePath
	 * @param fileName
	 * @param bitmap
	 */
	public static void saveBitMapFile(String filePath, String fileName,
			Bitmap bitmap) {
		if (null == bitmap)
			return;
		File f = new File(filePath, fileName);
		if (f.exists()) {
			f.delete();
		}
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(f));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡbitmap
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitMap(String filePath) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}
}
