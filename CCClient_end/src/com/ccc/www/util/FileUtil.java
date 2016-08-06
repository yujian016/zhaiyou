package com.ccc.www.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

public class FileUtil {

	/**
	 * 将bitmap转换成file,存放在手机的SD卡里面
	 * 
	 * @param bitmap
	 * @param fileName
	 * @return
	 */
	public static File BitMapToFile(Bitmap bitmap, String fileName) {
		String savePath = Environment.getExternalStorageDirectory()
				+ "/cc_images";
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		file = new File(savePath, fileName);
		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
}
