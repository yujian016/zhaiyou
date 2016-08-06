package com.ccc.www.util;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Toast;

public class BaseUtils {
	private static SimpleDateFormat sdf = new SimpleDateFormat();

	
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * ��ʽ���ַ�����
	 * 
	 * @param beforelate
	 *            ��ʽ��֮ǰ�����ڸ�ʽ
	 * @param afterlate
	 *            ��Ҫ��ʽ��������ڸ��?
	 * @param strDate
	 *            ��Ҫ��ʽ��������
	 * @return ��ʽ���������?
	 */
	public static String formatDate(String beforelate, String afterlate,
			String strDate) {
		try {
			sdf.applyPattern(beforelate);
			Date date = sdf.parse(strDate);
			sdf.applyPattern(afterlate);
			return sdf.format(date);
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
		return strDate;
	}

	@SuppressLint("SimpleDateFormat")
	public static String formatDate(String beforelate, Date mDate) {
		try {
			SimpleDateFormat mDateFormat = new SimpleDateFormat(beforelate);
			return mDateFormat.format(mDate);
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
		return mDate.toLocaleString();
	}

	public static void startActivity(Context ctx, Class<?> c) {
		Intent it = new Intent();
		it.setClass(ctx, c);
		ctx.startActivity(it);
	}

	public static void startActivity(Context ctx, Class<?> c, Bundle b) {
		Intent it = new Intent();
		it.setClass(ctx, c);
		it.putExtras(b);
		ctx.startActivity(it);
	}

	public static void startActivityForResult(Context ctx, Class<?> c,
			Bundle b, int requestCode) {
		Intent it = new Intent();
		it.setClass(ctx, c);
		it.putExtras(b);
		((Activity) ctx).startActivityForResult(it, requestCode);
	}

	public static void startActivityForResult(Context ctx, Class<?> c,
			int requestCode) {
		Intent it = new Intent();
		it.setClass(ctx, c);
		((Activity) ctx).startActivityForResult(it, requestCode);
	}

	/**
	 * 1���ж��Ƿ�����������
	 * 
	 * @param context
	 * @return
	 */
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 2���ж�WIFI�����Ƿ����?
	 * 
	 * @param context
	 * @return
	 */
	public boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 3���ж��ƶ����������Ƿ����?
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 4����ȡ��ǰ�������ӵ�������Ϣ
	 * 
	 * @param context
	 * @return
	 */
	public int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * 5���жϵ�ǰ�Ļ����Ƿ�������
	 * 
	 * @param TAG
	 * @param content
	 * @return
	 */
	public static boolean isNetWork(Context context) {
		BaseUtils nwu = new BaseUtils();
		boolean isNetWork = nwu.isNetworkConnected(context);
		if (isNetWork) {
			int netWorkCode = nwu.getConnectedType(context);
			String netWorkType = "";
			if (netWorkCode == 0) {
				netWorkType = "Mobile_NetWork";
				// BaseUtils.getToast(content,
				// "Mobile_NetWork="+nwu.isMobileConnected(content),
				// Toast.LENGTH_SHORT, 2);
				if (nwu.isMobileConnected(context)) {
					return true;
				} else {
					Toast.makeText(context, "当前网络不可用", Toast.LENGTH_SHORT)
							.show();
					return false;
				}
			} else if (netWorkCode == 1) {
				netWorkType = "WIFI_NetWork";
				// BaseUtils.getToast(content,
				// "WIFI_NetWork="+nwu.isMobileConnected(content),
				// Toast.LENGTH_SHORT, 2);
				if (nwu.isWifiConnected(context)) {
					return true;
				} else {
					Toast.makeText(context, "当前网络不可用", Toast.LENGTH_SHORT)
							.show();
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public static int getStudentId(Context mContext) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		return mPreferences.getInt("studentId", 0);
	}

	public static void setStudentId(Context mContext, int studentId) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putInt("studentId", studentId).commit();
	}

	public static String getToken(Context mContext) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		return mPreferences.getString("Token", "");
	}

	public static String getIdentity(Context mContext) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		return mPreferences.getString("identity", "");
	}

	public static void setIdentity(Context mContext, String identity) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putString("identity", identity).commit();
	}

	public static String getName(Context mContext) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		return mPreferences.getString("name", "");
	}

	public static void setPhone(Context mContext, String phone) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putString("phone", phone).commit();
	}

	public static String getPhone(Context mContext) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		return mPreferences.getString("phone", "");
	}

	public static void setName(Context mContext, String name) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putString("name", name).commit();
	}

	public static void setToken(Context mContext, String token) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putString("Token", token).commit();
	}

	public static void setCityName(Context mContext, String cityName) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putString("cityName", cityName).commit();
	}

	public static String getCityName(Context mContext) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		return mPreferences.getString("cityName", "");
	}

	public static void setCityId(Context mContext, int cityId) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putInt("cityId", cityId).commit();
	}

	public static int getCityId(Context mContext) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		return mPreferences.getInt("cityId", 0);
	}

	public static void setSchoolName(Context mContext, String schoolName) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putString("schoolName", schoolName).commit();
	}

	public static String getHeadImage(Context mContext) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		return mPreferences.getString("headImg", "");
	}

	public static void setHeadImg(Context mContext, String headImg) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putString("headImg", headImg).commit();
	}

	public static String getSchoolName(Context mContext) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		return mPreferences.getString("schoolName", "����ʦ����ѧ");
	}

	public static int dp2px(Context mContext, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				mContext.getResources().getDisplayMetrics());
	}

	public static boolean isEmpty(String x) {
		if (x == null || x.equals("") || x.equals("null")) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Collection<?> mList) {
		if (mList == null || mList.size() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isLogined(Context mContext) {
		System.out.println(isEmpty(getIdentity(mContext)) + "   "
				+ isEmpty(getToken(mContext)));
		return isEmpty(getIdentity(mContext)) || isEmpty(getToken(mContext));
	}

	/**
	 * ��֤����
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * ��֤�ֻ����?
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean checkMobileNumber(String mobileNumber) {
		boolean flag = false;
		try {
			Pattern regex = Pattern
					.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
			Matcher matcher = regex.matcher(mobileNumber);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public static void setCollection(Context mContext, String ids) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putString("ids", ids).commit();
	}

	public static String getCollection(Context mContext) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		return mPreferences.getString("ids", "");
	}

	public static void setMessageAble(Context mContext, boolean able) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putBoolean("msg", able).commit();
	}

	public static boolean getMessageAble(Context mContext) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		return mPreferences.getBoolean("msg", true);
	}

	public static boolean getInfoFull(Context mContext) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		return mPreferences.getBoolean("info_full", true);
	}

	public static void setInfoFull(Context mContext, boolean full) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"basedata", mContext.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putBoolean("info_full", full).commit();
	}
}
