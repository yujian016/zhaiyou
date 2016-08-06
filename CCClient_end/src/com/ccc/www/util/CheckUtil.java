package com.ccc.www.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {
	// 整数或者浮点小数
	public static boolean isIntOrFloat(String mobiles) {
		Pattern p = Pattern.compile("^[0-9]+(.[0-9]+)?$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	// 整数
	public static boolean isInt(String mobiles) {
		Pattern p = Pattern.compile("^[1-9]\\d*$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	// 整数
	public static boolean isIntAll(String mobiles) {
		Pattern p = Pattern.compile("^[0-9]\\d*$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^(1[0-9][0-9])\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

}
