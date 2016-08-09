package com.ccc.www.activity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ccc.ccclient_end.R;
import com.ccc.www.alipay.AlipayUtil;
import com.ccc.www.alipay.PayResult;
import com.ccc.www.bean.CouponBean;
import com.ccc.www.bean.PrivateSuperMarketGoodsBean;
import com.ccc.www.db.DBUtil;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GetBalanceInterface;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.util.ImageLoaderOption;
import com.ccc.www.util.UserUtil;
import com.ccc.www.view.FlowRadioGroup;
import com.ccc.www.view.NoScrollListView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 私人超市的购物车
 *
 * @author Administrator
 */
public class PrivateSupermarketCartActivity extends BaseActivity {

    String TAG = "PrivateSupermarketCartActivity";

    ImageButton ib_select_school_goback;
    ListView activity_proxystockcart_lv;

    int userid;
    int supermaket_id;

    List<PrivateSuperMarketGoodsBean> allsock = new ArrayList<PrivateSuperMarketGoodsBean>();

    Adapter adapter = new Adapter();

    TextView mycart_allmoney;
    TextView mycart_createorder;

    EditText pop_proxystocktip_name, pop_proxystocktip_phone, pop_proxystocktip_addr;
    TextView goodsCount, price, marketName;
    ImageView expandIv;
    RadioButton rbZhifubao, rbBalance, rbWeixin, rbCash;

    NoScrollListView couponlist;

    View rootview;

    // 收货人姓名
    String get_goods_person_name = "";
    // 收货人联系电话
    String get_goods_person_phone = "";
    // 收货人地址
    String get_goods_person_address = "";
    // 订单总金额
    double order_sum_money = 0.0f;
    // 随机码
    String rand_no = "";

    // 商品名称
    String businessname = "";
    // 商品描述
    String businessdesc = "";

    PaySuccessToMyOrder paySuccessToMyOrder = new PaySuccessToMyOrder();

    List<CouponBean> allCouponBean = new ArrayList<CouponBean>();

    float balance = 0.0f;

    String useraddr;//地址

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    /**
     * 支付成功后关闭页面
     *
     * @author Administrator
     */
    class PaySuccessToMyOrder extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            PrivateSupermarketCartActivity.this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(paySuccessToMyOrder);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supermaket_id = getIntent().getIntExtra("supermaket_id", 0);

        Log.v(TAG, "supermaket_id  " + supermaket_id);

        userid = UserUtil.getuserid(this);

        registerReceiver(paySuccessToMyOrder, new IntentFilter(
                GlobalConstant.PaySuccessToMyOrder));

        rootview = LayoutInflater.from(this).inflate(
                R.layout.activity_proxystockcart, null);

        setContentView(R.layout.activity_privatesupermarketcart);

        initview();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_select_school_goback:
                PrivateSupermarketCartActivity.this.finish();
                break;
            case R.id.expandIv:
                if (activity_proxystockcart_lv.getVisibility() == View.VISIBLE) {
                    activity_proxystockcart_lv.setVisibility(View.GONE);
                    expandIv.setImageResource(R.drawable.expand_list_down);
                } else {
                    activity_proxystockcart_lv.setVisibility(View.VISIBLE);
                    expandIv.setImageResource(R.drawable.expand_list_up);
                }
                break;
            case R.id.mycart_createorder:

                get_goods_person_name = pop_proxystocktip_name.getText().toString()
                        .trim();
                get_goods_person_phone = pop_proxystocktip_phone.getText()
                        .toString().trim();
                get_goods_person_address = pop_proxystocktip_addr.getText()
                        .toString().trim();

                if (TextUtils.isEmpty(get_goods_person_name)) {
                    showToast("请输入收货人姓名");
                    pop_proxystocktip_name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(get_goods_person_phone)) {
                    showToast("请输入收货人联系电话");
                    pop_proxystocktip_phone.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(get_goods_person_address)) {
                    showToast("请输入收货地址");
                    pop_proxystocktip_addr.requestFocus();
                    return;
                }

                // 调支付宝之前的数据准备

                businessname = "";
                for (int i = 0; i < allsock.size(); i++) {
                    if (i == 0) {
                        businessname = "(私人超市)"
                                + allsock.get(i).getGoods_name();
                    } else {
                        businessname = businessname + ","
                                + allsock.get(i).getGoods_name();
                    }
                }
                businessdesc = get_goods_person_name + "的购物订单";

                if (rbBalance.isChecked()) {
                    // 判断余额是否足够
                    if (balance < order_sum_money) {
                        showToast("您的余额为" + balance + "元不足以支付，请选择其他支付方式");
                    } else {
                        submitorder(0);
                    }
                } else {
                    /**
                     * 调用支付宝
                     */
                    boolean alipayinstall = AlipayUtil
                            .checkAlipayInstall(PrivateSupermarketCartActivity.this);
                    if (alipayinstall) {
                        checkalipay();
                    } else {
                        showToast("请先安装支付宝APP，并登录您的支付宝帐号");
                    }
                }

//                List<PrivateSuperMarketGoodsBean> allchoicesock = new ArrayList<PrivateSuperMarketGoodsBean>();
//
//                boolean havecheck = false;
//                for (int i = 0; i < allsock.size(); i++) {
//                    PrivateSuperMarketGoodsBean sock = allsock.get(i);
//                    boolean ischeck = sock.isCheck();
//                    if (ischeck) {
//                        if (sock.getCount() > 0) {
//                            havecheck = true;
//                            break;
//                        }
//                    }
//                }
//
//                for (int i = 0; i < allsock.size(); i++) {
//                    PrivateSuperMarketGoodsBean sock = allsock.get(i);
//                    boolean ischeck = sock.isCheck();
//                    if (ischeck) {
//                        if (sock.getCount() > 0) {
//                            allchoicesock.add(sock);
//                        }
//                    }
//                }
//
//                if (havecheck && allchoicesock.size() > 0) {
//                    Intent tocartorder = new Intent();
//                    tocartorder.putExtra("userid", userid);
//                    tocartorder.putExtra("supermaket_id", supermaket_id);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("allchoicesock",
//                            (Serializable) allchoicesock);
//                    tocartorder.putExtras(bundle);
//
//                    tocartorder.setClass(PrivateSupermarketCartActivity.this,
//                            PrivateSupermarketCartSubmitOrderActivity.class);
//                    startActivity(tocartorder);
//                } else {
//                    showToast("请选择要购买商品并且商品数目大于0");
//                }

                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void findviewWithId() {
        // TODO Auto-generated method stub
        ib_select_school_goback = (ImageButton) findViewById(R.id.ib_select_school_goback);
        activity_proxystockcart_lv = (ListView) findViewById(R.id.activity_proxystockcart_lv);
        activity_proxystockcart_lv.setAdapter(adapter);

        mycart_allmoney = (TextView) findViewById(R.id.mycart_allmoney);
        mycart_createorder = (TextView) findViewById(R.id.mycart_createorder);

        pop_proxystocktip_name = (EditText) findViewById(R.id.pop_proxystocktip_name);
        pop_proxystocktip_phone = (EditText) findViewById(R.id.pop_proxystocktip_phone);
        pop_proxystocktip_addr = (EditText) findViewById(R.id.pop_proxystocktip_addr);

        rbZhifubao = (RadioButton) findViewById(R.id.rbZhifubao);
        rbBalance = (RadioButton) findViewById(R.id.rbBalance);
        rbWeixin = (RadioButton) findViewById(R.id.rbWeixin);
        rbCash = (RadioButton) findViewById(R.id.rbCash);

        goodsCount = (TextView) findViewById(R.id.goodsCount);
        price = (TextView) findViewById(R.id.price);
        marketName = (TextView) findViewById(R.id.marketName);
        expandIv = (ImageView) findViewById(R.id.expandIv);
        couponlist = (NoScrollListView) findViewById(R.id.couponlist);
        couponlist.setAdapter(couponAdapter);
    }

    @Override
    public void initListener() {
        ib_select_school_goback.setOnClickListener(this);
        mycart_createorder.setOnClickListener(this);
        expandIv.setOnClickListener(this);
        rbWeixin.setOnCheckedChangeListener(onCheckedChangeListener);
        rbCash.setOnCheckedChangeListener(onCheckedChangeListener);

    }

    @Override
    public void initdata() {

        marketName.setText(UserUtil.getMarketName(this));

        double allmoney = 0.0f;

        allsock = DBUtil.getPrivateSupermarketCart(this, userid);
        for (int i = 0; i < allsock.size(); i++) {
            allsock.get(i).setCheck(true);
            double money = allsock.get(i).getCount()
                    * allsock.get(i).getGoods_price();
            allmoney = allmoney + money;
        }

        String schoolname = UserUtil
                .getschoolname(PrivateSupermarketCartActivity.this);
        String hostelname = UserUtil
                .gethostelname(PrivateSupermarketCartActivity.this);
        String floorname = UserUtil
                .getfloorname(PrivateSupermarketCartActivity.this);
        useraddr = schoolname + hostelname + floorname;
        pop_proxystocktip_addr.setText(useraddr);
        goodsCount.setText("共" + allsock.size() + "件商品，合计");

        order_sum_money = calculateTotalMoney();

        adapter.notifyDataSetChanged();

        getBalance();
        getCoupon(allmoney);
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            showToast("暂不支持");
            compoundButton.setChecked(false);
            return;
        }
    };

    class Adapter extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return allsock.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return allsock.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(PrivateSupermarketCartActivity.this)
                        .inflate(R.layout.item_privatesupermarketcart, null);
                holder.iv_sock_log = (ImageView) view
                        .findViewById(R.id.iv_sock_log);
                holder.tv_sock_name = (TextView) view
                        .findViewById(R.id.tv_sock_name);
                holder.tv_sock_price = (TextView) view
                        .findViewById(R.id.tv_sock_price);
                holder.tv_sock_count = (TextView) view
                        .findViewById(R.id.tv_sock_count);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            final PrivateSuperMarketGoodsBean sock = allsock.get(position);

            final boolean ischeck = sock.isCheck();

            Log.v(TAG, " position " + position + "  check  " + ischeck);


            ImageLoader.getInstance().displayImage(sock.getGoods_log(),
                    holder.iv_sock_log, ImageLoaderOption.getoption());

            holder.tv_sock_name.setText(sock.getGoods_name());
            holder.tv_sock_price.setText("￥" + sock.getGoods_price());

            holder.tv_sock_count.setText("×" + sock.getCount());

            return view;
        }

        class ViewHolder {
            ImageView iv_sock_log;
            TextView tv_sock_name;
            TextView tv_sock_price;
            TextView tv_sock_count;
        }
    }

    /**
     * 计算总金额
     */
    private double calculateTotalMoney() {
        double allmoney = 0.0f;
        for (int i = 0; i < allsock.size(); i++) {
            if (allsock.get(i).isCheck()) {
                double money = allsock.get(i).getCount()
                        * allsock.get(i).getGoods_price();
                allmoney = allmoney + money;
            }
        }

        DecimalFormat df = new DecimalFormat("######0.00");

        String allmoneyStr = df.format(allmoney);

        mycart_allmoney.setText("￥" + allmoneyStr);
        price.setText("￥" + allmoneyStr);

        return Float.parseFloat(allmoneyStr);

    }

    PopupWindow mPopupWindow;

    private void dissmisspopwindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }


    private void getBalance() {
        getAccountBalance(new GetBalanceInterface() {
            @Override
            public void Callback(String returnstr) {
                if (TextUtils.isEmpty(returnstr)) {
                    showToast("获取余额失败，请稍后重试");
                } else {
                    try {
                        JSONObject json = new JSONObject(returnstr);
                        balance = (float) json.getDouble("balance");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast("获取余额失败，请稍后重试");
                    }
                }
            }
        });
    }

    /**
     * 获取优惠券
     */
    private void getCoupon(final double allmoney) {
        // 掉接口
        if (!BaseUtils.isNetWork(getApplicationContext())) {
            showToast("请检查您的网络");
            return;
        }

        showLoading2("正在获取优惠券");

        RequestParams params = new RequestParams();
        params.addBodyParameter("user_id", userid + "");

        Log.v(TAG, "user_id  " + userid);

        loadData(HttpRequest.HttpMethod.POST, GlobalConstant.GET_Coupon, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub

                        dismissProgressDialog();

                        String returnstr2 = arg0.result;

                        Log.v(TAG, "returnstr " + returnstr2);

                        if (TextUtils.isEmpty(returnstr2)) {
                            couponlist.setVisibility(View.GONE);
                            return;
                        }
                        returnstr2 = returnstr2.substring(0,
                                returnstr2.length() - 1);
                        Log.v(TAG, "returnstr " + returnstr2);

                        try {

                            String[] sz = returnstr2.split("-");
                            for (int i = 0; i < sz.length; i++) {
                                Log.v(TAG, "  " + i + "  " + sz[i]);

                                JSONArray array = new JSONArray(sz[i]);
                                for (int m = 0; m < array.length(); m++) {
                                    JSONObject json = array.getJSONObject(m);

                                    int id = json.getInt("id");
                                    int coupon_type = json
                                            .getInt("coupon_type");
                                    float coupon_price = (float) json
                                            .getDouble("coupon_price");
                                    int coupon_num = json.getInt("coupon_num");
                                    int use_coupon_money = json
                                            .getInt("use_coupon_money");
                                    int shop_id = json.getInt("shop_id");
                                    String coupon_log = json
                                            .getString("coupon_log");
                                    String detail = json.getString("detail");
                                    int status = json.getInt("status");

                                    CouponBean bean = new CouponBean(id,
                                            coupon_type, coupon_price,
                                            coupon_num, shop_id, coupon_log,
                                            detail, status);

                                    bean.setCheck(false);
                                    bean.setUse_coupon_money(use_coupon_money);

                                    Log.v(TAG, "allmoney  " + allmoney
                                            + "   use_coupon_money  "
                                            + use_coupon_money);

                                    if (allmoney >= use_coupon_money * 1.0f) {
                                        allCouponBean.add(bean);
                                    }
                                }
                            }

                            if (allCouponBean.size() > 0) {
                                couponlist.setVisibility(View.VISIBLE);
                            } else {
                                couponlist.setVisibility(View.GONE);
                            }

                            Log.v(TAG, "allCouponBean  " + allCouponBean.size());

                            couponAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        dismissProgressDialog();
                        Log.v(TAG, "onFailure " + arg1);

                    }
                });
    }

    CouponAdapter couponAdapter = new CouponAdapter();

    class CouponAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return allCouponBean.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return allCouponBean.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup arg2) {

            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(
                        PrivateSupermarketCartActivity.this)
                        .inflate(R.layout.item_order_choose_coupon, null);

                holder.item_order_choose_coupon_check = (CheckBox) view
                        .findViewById(R.id.item_order_choose_coupon_check);
                holder.item_order_choose_coupon_detail = (TextView) view
                        .findViewById(R.id.item_order_choose_coupon_detail);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            final CouponBean bean = allCouponBean.get(position);

            if (bean.isCheck()) {
                holder.item_order_choose_coupon_check.setChecked(true);
            } else {
                holder.item_order_choose_coupon_check.setChecked(false);
            }

            String img = bean.getCoupon_log();
            if (!img.startsWith("http")) {
                img = GlobalConstant.SERVER_URL + img;
            }

            holder.item_order_choose_coupon_detail.setText("可使用优惠券抵扣" + bean.getCoupon_price() + "元");

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    boolean beancheck = bean.isCheck();

                    if (!beancheck) {
                        for (int i = 0; i < allCouponBean.size(); i++) {
                            allCouponBean.get(i).setCheck(false);
                        }
                        allCouponBean.get(position).setCheck(true);

                        float price = allCouponBean.get(position)
                                .getCoupon_price();

                        order_sum_money = calculateTotalMoney();

                        order_sum_money = order_sum_money - price;

                        mycart_allmoney.setText("￥" + order_sum_money);

                    } else {
                        for (int i = 0; i < allCouponBean.size(); i++) {
                            allCouponBean.get(i).setCheck(false);
                        }
                        allCouponBean.get(position).setCheck(false);

                        order_sum_money = calculateTotalMoney();
                        mycart_allmoney.setText("￥" + order_sum_money);
                    }
                    notifyDataSetChanged();
                }
            });

            holder.item_order_choose_coupon_check.setEnabled(false);

            return view;
        }

        class ViewHolder {
            CheckBox item_order_choose_coupon_check;
            TextView item_order_choose_coupon_detail;
        }

    }

    /**
     * 支付成功后提交订单给服务器 pay_type 0 余额 1 支付宝
     */
    private void submitorder(int pay_type) {

        rand_no = AlipayUtil.getRandomString();

        JsonArray jsonarr = new JsonArray();
        for (int i = 0; i < allsock.size(); i++) {
            PrivateSuperMarketGoodsBean sock = allsock.get(i);
            boolean ischeck = sock.isCheck();
            if (ischeck) {
                if (sock.getCount() > 0) {
                    JsonObject jsonobj = new JsonObject();
                    jsonobj.addProperty("goods_id", sock.getId());
                    jsonobj.addProperty("goods_cate_id",
                            sock.getGoods_category_id());
                    jsonobj.addProperty("goods_number", sock.getCount());
                    jsonarr.add(jsonobj);
                }
            }
        }

        String private_sup_order = jsonarr.toString();

        JsonObject private_buy_user_infoobj = new JsonObject();
        private_buy_user_infoobj.addProperty("buy_user_id",
                String.valueOf(userid));
        private_buy_user_infoobj.addProperty("supermaket_id",
                String.valueOf(supermaket_id));
        private_buy_user_infoobj.addProperty("get_goods_person_name",
                get_goods_person_name);
        private_buy_user_infoobj.addProperty("get_goods_person_phone",
                get_goods_person_phone);
        private_buy_user_infoobj.addProperty("get_goods_person_address",
                get_goods_person_address);
        private_buy_user_infoobj
                .addProperty("order_sum_money", order_sum_money);
        private_buy_user_infoobj.addProperty("rand_no", rand_no);
        String private_buy_user_info = private_buy_user_infoobj.toString();

        // 掉接口
        if (!BaseUtils.isNetWork(getApplicationContext())) {
            showToast("请检查您的网络");
            return;
        }

        showLoading2("正在提交");

        RequestParams params = new RequestParams();

        params.addBodyParameter("private_buy_user_info", private_buy_user_info);

        params.addBodyParameter("private_sup_order", private_sup_order);

        params.addBodyParameter("pay_type", String.valueOf(pay_type));

        int is_coupon = 0;
        int coupon_id = 0;
        for (int i = 0; i < allCouponBean.size(); i++) {
            if (allCouponBean.get(i).isCheck()) {
                is_coupon = 1;
                coupon_id = allCouponBean.get(i).getId();
            }
        }

        params.addBodyParameter("is_coupon", "" + is_coupon);
        params.addBodyParameter("coupon_id", "" + coupon_id);

        String url = GlobalConstant.ADD_PRIVATE_SUPMAKET_ORDER;

        Log.v(TAG, "url  " + url);
        Log.v(TAG, "private_buy_user_info  " + private_buy_user_info);
        Log.v(TAG, "private_sup_order  " + private_sup_order);
        Log.v(TAG, "is_coupon  " + is_coupon);
        Log.v(TAG, "coupon_id  " + coupon_id);

        loadData(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> response) {
                // TODO Auto-generated method stub
                dismissProgressDialog();

                String returnstr = response.result;

                Log.v(TAG, "onSuccess  " + response.result);

                if (TextUtils.isEmpty(returnstr)) {
                    showToast("提交失败");
                } else {
                    try {
                        final JSONObject json = new JSONObject(returnstr);
                        String resultMsg = json.getString("resultMsg");

                        if (resultMsg.contains("失败")) {
                            // 失败
                            AlertDialog.Builder build = new AlertDialog.Builder(
                                    PrivateSupermarketCartActivity.this);
                            build.setTitle("提示");
                            build.setMessage(resultMsg);
                            build.setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface arg0, int arg1) {
                                            // TODO Auto-generated
                                            // method stub

                                        }
                                    });
                            build.show();
                        } else {

                            JpushGetIMEI_BY_UID__Push(
                                    UserUtil.getenterpsmid(PrivateSupermarketCartActivity.this),
                                    "您有私人超市新订单", "您有私人超市新订单,请查看店铺购物订单");

                            JpushAddMsg(
                                    userid,
                                    UserUtil.getenterpsmid(PrivateSupermarketCartActivity.this),
                                    "您有私人超市新订单", "您有私人超市新订单,请查看店铺购物订单");

                            // 成功
                            final String orderNo = json.getString("orderNo");
                            AlertDialog.Builder build = new AlertDialog.Builder(
                                    PrivateSupermarketCartActivity.this);
                            build.setTitle("提示");
                            build.setMessage(resultMsg);
                            build.setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(
                                                DialogInterface arg0, int arg1) {
                                            DBUtil.updateProxyStockOrderStatus(
                                                    PrivateSupermarketCartActivity.this,
                                                    rand_no, userid);

                                            // 删除代理进货购物车
                                            DBUtil.deleteProxyStockCart(
                                                    PrivateSupermarketCartActivity.this,
                                                    userid);

                                            // 发送支付成功的广播，跳转去我的订单页面

                                            Intent PaySuccessToMyOrder = new Intent();
                                            PaySuccessToMyOrder
                                                    .setAction(GlobalConstant.PaySuccessToMyOrder);
                                            sendBroadcast(PaySuccessToMyOrder);

                                            Intent intent = new Intent();
                                            intent.putExtra("orderNo", orderNo);
                                            intent.setClass(
                                                    PrivateSupermarketCartActivity.this,
                                                    OrderActivity.class);
                                            startActivity(intent);

                                            PrivateSupermarketCartActivity.this
                                                    .finish();
                                        }
                                    });
                            build.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showToast("提交失败");
                    }
                }
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {

                Log.v(TAG, "onFailure  ");
                dismissProgressDialog();
                showToast("提交失败");
            }
        });

    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void checkalipay() {
        Runnable checkRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(
                        PrivateSupermarketCartActivity.this);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * 调用支付宝
     */
    private void callaliPay(String businessname, String businessdesc,
                            double allmoney) {
        // 订单
        String orderInfo = AlipayUtil.getOrderInfo(businessname, businessdesc,
                String.valueOf(allmoney));

//		String orderInfo = AlipayUtil.getOrderInfo(businessname, businessdesc,
//				"0.01");

        // 对订单做RSA 签名
        String sign = AlipayUtil.sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + AlipayUtil.getSignType();

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(
                        PrivateSupermarketCartActivity.this);

                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {

                        Toast.makeText(
                                PrivateSupermarketCartActivity.this,
                                "支付成功", Toast.LENGTH_SHORT).show();

                        // 清空当前用户的代理购物车
                        DBUtil.deleteProxyStockCart(
                                PrivateSupermarketCartActivity.this,
                                userid);
                        // 发广播修改代理进货界面数量变化
                        Intent updateProxyStockCartCount = new Intent();
                        updateProxyStockCartCount
                                .setAction(GlobalConstant.UpdateProxyStockCartCount);
                        sendBroadcast(updateProxyStockCartCount);

                        // 提交给服务端
                        submitorder(1);

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        // if (TextUtils.equals(resultStatus, "8000")) {
                        // Toast.makeText(ProxyStockCartActivity.this, "支付结果确认中",
                        // Toast.LENGTH_SHORT).show();
                        //
                        // } else {
                        // // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        // Toast.makeText(ProxyStockCartActivity.this, "支付失败",
                        // Toast.LENGTH_SHORT).show();
                        // }
                        Toast.makeText(
                                PrivateSupermarketCartActivity.this,
                                "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    // Toast.makeText(ProxyStockCartActivity.this, "检查结果为：" +
                    // msg.obj,
                    // Toast.LENGTH_SHORT).show();

                    boolean isexist = Boolean.parseBoolean(msg.obj.toString());
                    if (isexist) {
                        callaliPay(businessname, businessdesc, order_sum_money);
                    } else {
                        showToast("请先登录支付宝APP");
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    // /**
    // * 支付信息确认
    // *
    // * @param KaizenId
    // */
    // private void payinfoConfirm(String useraddr, final float balance) {
    //
    // View view1 = LayoutInflater.from(PrivateSupermarketCartActivity.this)
    // .inflate(R.layout.pop_proxystocktip, null);
    // mPopupWindow = new PopupWindow(view1, LayoutParams.MATCH_PARENT,
    // LayoutParams.MATCH_PARENT);
    // mPopupWindow.setFocusable(true);
    //
    // final EditText pop_proxystocktip_name = (EditText) view1
    // .findViewById(R.id.pop_proxystocktip_name);
    // final EditText pop_proxystocktip_phone = (EditText) view1
    // .findViewById(R.id.pop_proxystocktip_phone);
    // final EditText pop_proxystocktip_addr = (EditText) view1
    // .findViewById(R.id.pop_proxystocktip_addr);
    // TextView pop_proxystocktip_money = (TextView) view1
    // .findViewById(R.id.pop_proxystocktip_money);
    // TextView pop_proxystocktip_cancel = (TextView) view1
    // .findViewById(R.id.pop_proxystocktip_cancel);
    // TextView pop_proxystocktip_pay = (TextView) view1
    // .findViewById(R.id.pop_proxystocktip_pay);
    // TextView pop_proxystocktip_balance = (TextView) view1
    // .findViewById(R.id.pop_proxystocktip_balance);
    //
    // pop_proxystocktip_balance.setText(balance + "元");
    //
    // pop_proxystocktip_addr.setText(useraddr);
    //
    // order_sum_money = calculateTotalMoney();
    // pop_proxystocktip_money.setText(order_sum_money + "元");
    //
    // pop_proxystocktip_cancel.setOnClickListener(new OnClickListener() {
    // public void onClick(View v) {
    // dissmisspopwindow();
    // }
    // });
    // pop_proxystocktip_pay.setOnClickListener(new OnClickListener() {
    // public void onClick(View v) {
    //
    // get_goods_person_name = pop_proxystocktip_name.getText()
    // .toString().trim();
    // get_goods_person_phone = pop_proxystocktip_phone.getText()
    // .toString().trim();
    // get_goods_person_address = pop_proxystocktip_addr.getText()
    // .toString().trim();
    //
    // if (TextUtils.isEmpty(get_goods_person_name)) {
    // showToast("请输入收货人姓名");
    // pop_proxystocktip_name.requestFocus();
    // return;
    // }
    //
    // if (TextUtils.isEmpty(get_goods_person_phone)) {
    // showToast("请输入收货人联系电话");
    // pop_proxystocktip_phone.requestFocus();
    // return;
    // }
    //
    // if (TextUtils.isEmpty(get_goods_person_address)) {
    // showToast("请输入收货地址");
    // pop_proxystocktip_addr.requestFocus();
    // return;
    // }
    //
    // // 调支付宝之前的数据准备
    // List<PrivateSuperMarketGoodsBean> selectsocks = new
    // ArrayList<PrivateSuperMarketGoodsBean>();
    // for (int i = 0; i < allsock.size(); i++) {
    // PrivateSuperMarketGoodsBean sock = allsock.get(i);
    // boolean ischeck = sock.isCheck();
    // if (ischeck) {
    // if (sock.getCount() > 0) {
    // selectsocks.add(sock);
    // }
    // }
    // }
    //
    // businessname = "";
    // for (int i = 0; i < selectsocks.size(); i++) {
    // if (i == 0) {
    // businessname = "(私人超市)"
    // + selectsocks.get(i).getGoods_name();
    // } else {
    // businessname = businessname + ","
    // + selectsocks.get(i).getGoods_name();
    // }
    // }
    // businessdesc = get_goods_person_name + "的购物订单";
    //
    // // 判断余额是否足够
    // if (balance >= order_sum_money) {
    // // 使用余额支付
    // /**
    // *
    // */
    // submitorder(0);
    // } else {
    // /**
    // * 调用支付宝
    // */
    // boolean alipayinstall = AlipayUtil
    // .checkAlipayInstall(PrivateSupermarketCartActivity.this);
    // if (alipayinstall) {
    // checkalipay();
    // } else {
    // showToast("请先安装支付宝APP，并登录您的支付宝帐号");
    // }
    // }
    //
    // }
    // });
    // mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x90000000));
    // mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
    // }

}
