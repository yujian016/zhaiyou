package com.ccc.www.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.ccc.ccclient_end.R;
import com.ccc.www.adapter.SelectSchoolAdapter;
import com.ccc.www.bean.DormitoryBean;
import com.ccc.www.bean.SchoolBean;
import com.ccc.www.util.BaseUtils;
import com.ccc.www.util.GlobalConstant;
import com.ccc.www.view.MyGridView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class PrivateSupermarketSelectSchoolActivity extends BaseActivity {

    String TAG = "SelectSchoolActivity";

    private ImageButton ibGoback;
    private ListView lvSchool;

    List<SchoolBean> allSchoolBean = new ArrayList<SchoolBean>();
    List<SchoolBean> allOriginalSchoolBean = new ArrayList<SchoolBean>();

    EditText et_select_school_search;
    ImageButton btn_select_school_search;

    private SelectSchoolAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_school);
        initview();
    }

    @Override
    public void findviewWithId() {
        ibGoback = (ImageButton) findViewById(R.id.ib_select_school_goback);
        lvSchool = (ListView) findViewById(R.id.lv_select_school);
        et_select_school_search = (EditText) findViewById(R.id.et_select_school_search);
        btn_select_school_search = (ImageButton) findViewById(R.id.btn_select_school_search);

        et_select_school_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String school_name = et_select_school_search.getText()
                        .toString().trim();
                if (TextUtils.isEmpty(school_name)) {
                    allSchoolBean.clear();
                    allSchoolBean.addAll(allOriginalSchoolBean);
                    adapter = new SelectSchoolAdapter(
                            PrivateSupermarketSelectSchoolActivity.this,
                            allSchoolBean);
                    lvSchool.setAdapter(adapter);
                }
            }
        });
    }

    @Override
    public void initListener() {
        ibGoback.setOnClickListener(this);
        lvSchool.setOnItemClickListener(this);
        btn_select_school_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_select_school_goback:
                PrivateSupermarketSelectSchoolActivity.this.finish();
                break;
            case R.id.btn_select_school_search:

                String school_name = et_select_school_search.getText().toString()
                        .trim();
                if (TextUtils.isEmpty(school_name)) {
                    showToast("请输入关键字");
                    et_select_school_search.requestFocus();
                    return;
                }

                if (!BaseUtils.isNetWork(getApplicationContext())) {
                    showToast("请检查您的网络");
                    return;
                }

                showLoading2("正在搜索");

                RequestParams params = new RequestParams();
                params.addBodyParameter("school_name", school_name);

                loadData(HttpMethod.POST, GlobalConstant.SEARCH_SCHOOL, params,
                        new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> response) {
                                // TODO Auto-generated method stub
                                dismissProgressDialog();

                                String returnstr = response.result;

                                Log.v(TAG, response.result);
                                if (TextUtils.isEmpty(returnstr)) {
                                    showToast("搜索失败");
                                } else {
                                    try {
                                        allSchoolBean.clear();
                                        JSONArray json = new JSONArray(returnstr);
                                        for (int i = 0; i < json.length(); i++) {
                                            JSONObject temp = json.getJSONObject(i);
                                            int id = temp.getInt("id");
                                            String school_name = temp
                                                    .getString("school_name");
                                            String school_address = temp
                                                    .getString("school_address");

                                            SchoolBean bean = new SchoolBean(id,
                                                    school_name, school_address);
                                            allSchoolBean.add(bean);
                                        }

                                        adapter = new SelectSchoolAdapter(
                                                PrivateSupermarketSelectSchoolActivity.this,
                                                allSchoolBean);
                                        lvSchool.setAdapter(adapter);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(HttpException arg0, String arg1) {
                                dismissProgressDialog();
                                showToast("搜索失败");
                            }
                        });

                break;
            default:
                break;
        }
    }

    @Override
    public void initdata() {
        if (!BaseUtils.isNetWork(getApplicationContext())) {
            showToast("请检查您的网络");
            return;
        }

        showLoading2("正在加载");

        loadData(HttpMethod.POST, GlobalConstant.GET_ALL_SCHOOL, null,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> response) {
                        // TODO Auto-generated method stub
                        dismissProgressDialog();

                        String returnstr = response.result;

                        Log.v(TAG, response.result);
                        if (TextUtils.isEmpty(returnstr)) {
                            showToast("加载失败");
                        } else {
                            try {

                                allSchoolBean.clear();
                                allOriginalSchoolBean.clear();

                                JSONArray json = new JSONArray(returnstr);
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject temp = json.getJSONObject(i);
                                    int id = temp.getInt("id");
                                    String school_name = temp
                                            .getString("school_name");
                                    String school_address = temp
                                            .getString("school_address");

                                    SchoolBean bean = new SchoolBean(id,
                                            school_name, school_address);
                                    allSchoolBean.add(bean);
                                    allOriginalSchoolBean.add(bean);
                                }

                                adapter = new SelectSchoolAdapter(
                                        PrivateSupermarketSelectSchoolActivity.this,
                                        allSchoolBean);
                                lvSchool.setAdapter(adapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        dismissProgressDialog();
                        showToast("加载失败");
                    }
                });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        System.out.println("ppp:" + position + "，idddd:" + id);

        SchoolBean schoolBean=allSchoolBean.get(position);
        int schoolid = schoolBean.getId();
        boolean isExpand = schoolBean.isExpand();
        ImageView expandIv = (ImageView) view.findViewById(R.id.expandIv);
        MyGridView gridView = (MyGridView) view.findViewById(R.id.gridView);
        String tag=(String)gridView.getTag();
        if(tag.equals(position+"")){
            if (isExpand) {
                expandIv.setImageResource(R.drawable.expand_down);
                gridView.setVisibility(View.GONE);
                allSchoolBean.get(position).setIsExpand(false);
            } else {
                expandIv.setImageResource(R.drawable.expand_up);
                gridView.setVisibility(View.VISIBLE);
                allSchoolBean.get(position).setIsExpand(true);
                List<DormitoryBean> dormitoryBeanList=schoolBean.getDormitoryBeanList();
                if (dormitoryBeanList==null||dormitoryBeanList.size()==0) {
                    loadDormitoryData(schoolid, allSchoolBean.get(position));
                }
            }
        }
        Log.v(TAG, "schoolid " + schoolid);

//		Intent intent = new Intent();
//		intent.setClass(PrivateSupermarketSelectSchoolActivity.this,
//				PrivateSupermarketSelectDormitoryActivity.class);
//		intent.putExtra("schoolid", schoolid);
//		startActivity(intent);
    }

    private void loadDormitoryData(int schoolid,final SchoolBean schoolBean) {
        showLoading2("正在加载");
        final List<DormitoryBean> allDormitory = new ArrayList<DormitoryBean>();
        RequestParams params = new RequestParams();
        params.addBodyParameter("school_id", schoolid + "");

        loadData(HttpRequest.HttpMethod.POST, GlobalConstant.GET_SCHOOL_HOSTEL, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> response) {
                        // TODO Auto-generated method stub
                        dismissProgressDialog();
                        String returnstr = response.result;

                        if (TextUtils.isEmpty(returnstr)) {
                        } else {
                            try {
                                JSONArray json = new JSONArray(returnstr);
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject temp = json.getJSONObject(i);
                                    int id = temp.getInt("id");
                                    int school_id = temp.getInt("school_id");
                                    String hostel_name = temp
                                            .getString("hostel_name");
                                    int is_proxy = temp.getInt("is_proxy");
                                    DormitoryBean bean = new DormitoryBean(id,
                                            school_id, hostel_name, is_proxy);
                                    allDormitory.add(bean);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (allDormitory != null && allDormitory.size() > 0) {
                            schoolBean.setDormitoryBeanList(allDormitory);
                            adapter.notifyDataSetChanged(allSchoolBean);
                        }
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        dismissProgressDialog();
                    }
                });
    }

}
