package com.ccc.www.alipay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.umeng.socialize.utils.Log;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 支付宝工具类
 * 
 * @author Administrator
 * 
 */
public class AlipayUtil {
	static String TAG = "AlipayUtil";

	// 商户PID
	public static final String PARTNER = "2088221219977734";
	// 商户收款账号
	public static final String SELLER = "206969653@qq.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAM23GTG6c5lOQrHfKsJhAySxHKzhsUfb9sHMZ6t8cbwazWTYjmNnqULcvBwnCd/il8and7aP35vfAAQKISXhtlacTZp6qH+RB5KX1PF60fr53aRBm11LwUZCYHKLdetIBI8McPKaLPVGhgWJAAaJCNtIZCOB4dd/ZAmdcJy+K/E3AgMBAAECgYBT3MnjkOwhWlssIhtPZLcgVSeoftWDwvNkbAzzRuci1RZYf0QE2DSTcmMpYuzOnZRcQtc10I9K/u0FuI9s3wgyqYHYxXlNtpNKGdOtxmeHeYPY5ditvouj8K6mL1ZZKb5+uN7qSKhli2+zKVC+dEvZePC6cFRH9iNK3Vbo5X0xoQJBAO7Y9F2LLuliYTzHSfX0e460Cr8gWoaf4HDnBsE71R0vNppLBgV78/0zzNGDMRXSiEVRdnWJ7hg42NLaW9QNvmcCQQDcfQfHc5RJATORZ3DTG7Rlhnaur5msZ8qZBYdxiBaRoaWHZQxePY9oLqNfdaFhrKTaZ8dl3rKeguwtxX/uUtSxAkEAnylD7fIJ2MwOMAo9ZwD8NSQU5TgSGxP+5D2PasUMRt5nbHoCETWAaYJTQmpCE2CwHkEix0CawNlg42FPQAfIkQJBAJFOPLAu9UxfZFKw7uPRYEOAJ/rbctTx2cyanjYtz2HOO75M06fPvhCUm8ovRVmPY10kA36ZEvHvqiJIBBVVtSECQE1D8TW+YGU5UHzefwAlmBdWefhO4DXYJMcVgl6et1whbjE1QNEYqYyBrlxNouqCd+VpYeX48YYZ8eF7hwYcmG4=";

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public static String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public static String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public static String getSignType() {
		return "sign_type=\"RSA\"";
	}

	/**
	 * 生成随机码
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString() { // length表示生成字符串的长度
		int length = 16;
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 判断支付宝是否安装
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean checkAlipayInstall(Context mContext) {
		PackageManager manager = mContext.getPackageManager();
		List<PackageInfo> pkgList = manager.getInstalledPackages(0);

		Log.v(TAG, "pkgList   " + pkgList.size());

		List<ApplicationInfo> pkgList1 = manager.getInstalledApplications(0);

		Log.v(TAG, "pkgList1   " + pkgList1.size());

		for (int i = 0; i < pkgList.size(); i++) {
			PackageInfo pI = pkgList.get(i);

			Log.v(TAG, "pI.packageNam   " + pI.packageName);

			if (pI.packageName.equalsIgnoreCase("com.alipay.android.app")|| pI.packageName.equalsIgnoreCase("com.eg.android.AlipayGphone")  )
				return true;
		}
	 
		return false;

	}

}
