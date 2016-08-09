package com.ccc.www.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class UserUtil {

	public static String TAG = "UserUtil";

	public static String getVersionName(Context context) throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		String version = packInfo.versionName;
		return version;
	}

	public static int getVersionCode(Context context) throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		int versionCode = packInfo.versionCode;
		return versionCode;
	}

	/**
	 * 保存schoolid
	 * 
	 * @param context
	 * @param CompanyName
	 */
	public static void setschoolid(Context context, int schoolid) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt("schoolid", schoolid);
		edit.commit();
	}

	/**
	 * 取出schoolid
	 * 
	 * @param context
	 * @return
	 */
	public static int getschoolid(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		int schoolid = sp.getInt("schoolid", 0);
		return schoolid;
	}

	/**
	 * 保存schoolname
	 * 
	 * @param context
	 * @param CompanyName
	 */
	public static void setschoolname(Context context, String schoolname) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("schoolname", schoolname);
		edit.commit();
	}

	/**
	 * 取出schoolname
	 * 
	 * @param context
	 * @return
	 */
	public static String getschoolname(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		String schoolname = sp.getString("schoolname", "");
		return schoolname;
	}

	/**
	 * 保存hostelid
	 * 
	 * @param context
	 * @param CompanyName
	 */
	public static void sethostelid(Context context, int hostelid) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt("hostelid", hostelid);
		edit.commit();
	}

	/**
	 * 取出hostelid
	 * 
	 * @param context
	 * @return
	 */
	public static int gethostelid(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		int hostelid = sp.getInt("hostelid", 0);
		return hostelid;
	}

	/**
	 * 保存hostelname
	 * 
	 * @param context
	 * @param CompanyName
	 */
	public static void sethostelname(Context context, String hostelname) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("hostelname", hostelname);
		edit.commit();
	}

	/**
	 * 取出hostelname
	 * 
	 * @param context
	 * @return
	 */
	public static String gethostelname(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		String hostelname = sp.getString("hostelname", "");
		return hostelname;
	}

	/**
	 * 保存floorid
	 * 
	 * @param context
	 * @param CompanyName
	 */
	public static void setfloorid(Context context, int floorid) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt("floorid", floorid);
		edit.commit();
	}

	/**
	 * 取出floorid
	 * 
	 * @param context
	 * @return
	 */
	public static int getfloorid(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		int floorid = sp.getInt("floorid", 0);
		return floorid;
	}

	/**
	 * 保存floorname
	 * 
	 * @param context
	 * @param CompanyName
	 */
	public static void setfloorname(Context context, String floorname) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("floorname", floorname);
		edit.commit();
	}

	/**
	 * 取出floorname
	 * 
	 * @param context
	 * @return
	 */
	public static String getfloorname(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		String floorname = sp.getString("floorname", "");
		return floorname;
	}

	/**
	 * 保存userid
	 * 
	 * @param context
	 * @param CompanyName
	 */
	public static void setuserid(Context context, int userid) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt("userid", userid);
		edit.commit();
	}

	/**
	 * 取出userid
	 * 
	 * @param context
	 * @return
	 */
	public static int getuserid(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		int userid = sp.getInt("userid", 0);
		return userid;
	}

	/**
	 * 保存useropenshopid
	 * 
	 * @param context
	 * @param CompanyName
	 */
	public static void setuseropenshopid(Context context, int useropenshopid) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt("useropenshopid", useropenshopid);
		edit.commit();
	}

	/**
	 * 取出useropenshopid
	 * 
	 * @param context
	 * @return
	 */
	public static int getuseropenshopid(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		int useropenshopid = sp.getInt("useropenshopid", 0);
		return useropenshopid;
	}

	/**
	 * 保存supermaket_id
	 * 
	 * @param context
	 * @param CompanyName
	 */
	public static void setsupermaket_id(Context context, int supermaket_id) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt("supermaket_id", supermaket_id);
		edit.commit();
	}

	/**
	 * 取出supermaket_id
	 * 
	 * @param context
	 * @return
	 */
	public static int getsupermaket_id(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		int supermaket_id = sp.getInt("supermaket_id", 0);
		return supermaket_id;
	}

	/**
	 * 保存firstrun
	 * 
	 * @param context
	 * @param CompanyName
	 */
	public static void setfirstrun(Context context, boolean firstrun) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean("firstrun", firstrun);
		edit.commit();
	}

	/**
	 * 取出firstrun
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getfirstrun(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		boolean firstrun = sp.getBoolean("firstrun", true);
		return firstrun;
	}

	/**
	 * 保存redbagshakecount
	 * 
	 * @param context
	 * @param CompanyName
	 */
	public static void setredbagshakecount(Context context, int redbagshakecount) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt("redbagshakecount", redbagshakecount);
		edit.commit();
	}

	/**
	 * 取出redbagshakecount
	 * 
	 * @param context
	 * @return
	 */
	public static int getredbagshakecount(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		int redbagshakecount = sp.getInt("redbagshakecount", 0);
		return redbagshakecount;
	}

	/**
	 * 保存enterpsmid
	 * 
	 * @param context
	 * @param CompanyName
	 */
	public static void setenterpsmid(Context context, int enterpsmid) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt("enterpsmid", enterpsmid);
		edit.commit();
	}

	/**
	 * 取出enterpsmid
	 * 
	 * @param context
	 * @return
	 */
	public static int getenterpsmid(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		int enterpsmid = sp.getInt("enterpsmid", 0);
		return enterpsmid;
	}
	/**
	 * 保存market_name
	 *
	 * @param context
	 * @param market_name
	 */
	public static void setMarketName(Context context, String market_name) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("market_name", market_name);
		edit.commit();
	}

	/**
	 * 取出market_name
	 *
	 * @param context
	 * @return
	 */
	public static String getMarketName(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		String market_name = sp.getString("market_name", "");
		return market_name;
	}

	
	
	
	
	/**
	 * 保存私人超市起送金额
	 * 
	 * @param context
	 * @param CompanyName
	 */
	public static void setenterpsm_startmoney(Context context, int psm_startmoney) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt("psm_startmoney", psm_startmoney);
		edit.commit();
	}

	/**
	 * 取出私人超市起送金额
	 * 
	 * @param context
	 * @return
	 */
	public static int getpsm_startmoney(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		int psm_startmoney = sp.getInt("psm_startmoney", 0);
		return psm_startmoney;
	}
	
	

	/**
	 * 保存私人超市的记忆hotelid
	 * 
	 * @param context
	 * @param CompanyName
	 */
	public static void setprivatesmhotelid(Context context, int privatesmhotelid) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putInt("privatesmhotelid", privatesmhotelid);
		edit.commit();
	}

	/**
	 * 取出私人超市的记忆hotelid
	 * 
	 * @param context
	 * @return
	 */
	public static int getprivatesmhotelid(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		int privatesmhotelid = sp.getInt("privatesmhotelid", 0);
		return privatesmhotelid;
	}

	/**
	 * 保存userid_redbagid_hadcount
	 * 
	 * @param context
	 * @param CompanyName
	 */
	public static void setuserid_redbagid_hadcount(Context context,
			String userid_redbagid_hadcount) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("userid_redbagid_hadcount", userid_redbagid_hadcount);
		edit.commit();
	}

	/**
	 * 取出userid_redbagid_hadcount
	 * 
	 * @param context
	 * @return
	 */
	public static String getuserid_redbagid_hadcount(Context context) {
		SharedPreferences sp = context.getSharedPreferences("app",
				Context.MODE_PRIVATE);
		String userid_redbagid_hadcount = sp.getString(
				"userid_redbagid_hadcount", "");
		return userid_redbagid_hadcount;
	}

}
