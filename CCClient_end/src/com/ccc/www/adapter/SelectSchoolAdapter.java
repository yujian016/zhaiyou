package com.ccc.www.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ccc.ccclient_end.R;
import com.ccc.www.activity.PrivateSupermarketDormitoryProxyActivity;
import com.ccc.www.bean.DormitoryBean;
import com.ccc.www.bean.SchoolBean;
import com.ccc.www.util.UserUtil;
import com.ccc.www.view.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectSchoolAdapter extends BaseAdapter {

    private List<SchoolBean> lists = new ArrayList<SchoolBean>();
    private LayoutInflater inflater;
    private Context context;

    public SelectSchoolAdapter(Context context, List<SchoolBean> lists) {
        super();
        this.lists = lists;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int itemId) {
        return itemId;
    }

    @Override
    public View getView(int index, View view, ViewGroup vgroup) {
        view = inflater.inflate(R.layout.school_lv_item, null);
        TextView tv_school_name= (TextView) view
                .findViewById(R.id.tv_school_name);
        MyGridView gridView = (MyGridView) view
                .findViewById(R.id.gridView);
        ImageView expandIv = (ImageView) view
                .findViewById(R.id.expandIv);

        final SchoolBean s = lists.get(index);
        tv_school_name.setText(s.getSchool_name());

        if(s.getDormitoryBeanList()!=null&&s.getDormitoryBeanList().size()>0){
            List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
            data_list = getData(s.getDormitoryBeanList());
            String[] from = {"text"};
            int[] to = {R.id.tv_dormitory_name};
            SimpleAdapter sim_adapter = new SimpleAdapter(context, data_list, R.layout.item_school_dormitory, from, to);
            //配置适配器
            gridView.setAdapter(sim_adapter);
            if(s.isExpand()){
                expandIv.setImageResource(R.drawable.expand_up);
                gridView.setVisibility(View.VISIBLE);
            }else{
                expandIv.setImageResource(R.drawable.expand_down);
                gridView.setVisibility(View.GONE);
            }
        }
        gridView.setTag(index+"");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                final int hostel_id = s.getDormitoryBeanList().get(position).getId();
                Intent intent = new Intent();
                intent.setClass(context,
                        PrivateSupermarketDormitoryProxyActivity.class);
                intent.putExtra("hostel_id", hostel_id);

                UserUtil.setprivatesmhotelid(context,
                        hostel_id);
                context.startActivity(intent);
            }
        });

        return view;
    }

    public List<Map<String, Object>> getData(List<DormitoryBean> allDormitory) {
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < allDormitory.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("text", allDormitory.get(i).getHostel_name());
            data_list.add(map);
        }
        return data_list;
    }

    public void notifyDataSetChanged(List<SchoolBean> lists){
        this.lists = lists;
        this.notifyDataSetChanged();
    }
}
