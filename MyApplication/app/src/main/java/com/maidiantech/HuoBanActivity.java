package com.maidiantech;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import entity.Arrt_Dell_List;
import entity.Arrt_List;
import entity.HuoBanEntity;
import view.RefreshListView;

/**
 * Created by Administrator on 2019/11/28.
 */

public class HuoBanActivity extends AutoLayoutActivity {
    RefreshListView listview;
    LinearLayout title;
    ImageView back,heartback;
    TextView titledes,hearttitle,youxiaoqi,jingjiren;
    View heartView;
    HuoBanAdapter adapter;
    boolean isherat = true;
    String id;
    ProgressBar progress;
    List<Arrt_Dell_List> showList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.huobanactivity);
        id = getIntent().getStringExtra("id");
        listview = (RefreshListView)findViewById(R.id.listview);
        title = (LinearLayout) findViewById(R.id.title);
        back = (ImageView)findViewById(R.id.back);
        titledes = (TextView)findViewById(R.id.titledes);
        progress = (ProgressBar)findViewById(R.id.progress);
        heartView = View.inflate(this, R.layout.huobanheart,null);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        heartback = heartView.findViewById(R.id.heartback);
        heartback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hearttitle = heartView.findViewById(R.id.hearttitle);
        youxiaoqi = heartView.findViewById(R.id.youxiaoqi);
        jingjiren = heartView.findViewById(R.id.jingjiren);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            private SparseArray recordSp = new SparseArray(0);
            private int mCurrentfirstVisibleItem = 0;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mCurrentfirstVisibleItem = firstVisibleItem;
                View firstView = view.getChildAt(0);
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = listview.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isherat = true;
                    }
                }else{
                    isherat = false;
                }
                if (null != firstView) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                    if (null == itemRecord) {
                        itemRecord = new ItemRecod();
                    }
                    itemRecord.height = firstView.getHeight();
                    itemRecord.top = firstView.getTop();
                    recordSp.append(firstVisibleItem, itemRecord);
                    int h = getScrollY();
                    if(h >290-25){
                        int cha = h-(290-25);
                        float alpha = cha/100f;
                        if(alpha < 1.0f){
                            title.setAlpha(alpha);
                        }else{
                            title.setAlpha(1.0f);
                        }
                        title.setVisibility(View.VISIBLE);
                    }else {
                        title.setVisibility(View.GONE);
                    }
                }
            }
            private int getScrollY() {
                try {
                    int height = 0;
                    for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
                        ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
                        height += itemRecod.height;
                    }
                    ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
                    if (null == itemRecod) {
                        itemRecod = new ItemRecod();
                    }
                    return height - itemRecod.top;
                }catch (Exception e){
                    return 400;
                }

            }
            class ItemRecod {
                int height = 0;
                int top = 0;
            }
        });
        progress.setVisibility(View.VISIBLE);
        getJson();
    }
    class HuoBanAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return showList.size();
        }

        @Override
        public Object getItem(int position) {
            return showList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView holdView = null;
            if(convertView == null){
                holdView = new HoldView();
                convertView = View.inflate(HuoBanActivity.this, R.layout.huobanadapter, null);
                holdView.title = convertView.findViewById(R.id.title);
                holdView.title1 = convertView.findViewById(R.id.title1);
                holdView.title2 = convertView.findViewById(R.id.title2);
                holdView.count1 = convertView.findViewById(R.id.count1);
                holdView.count2 = convertView.findViewById(R.id.count2);
                holdView.dell   =convertView.findViewById(R.id.dell);
                holdView.item1   =convertView.findViewById(R.id.item1);
                holdView.item2   =convertView.findViewById(R.id.item2);
                holdView.end = convertView.findViewById(R.id.end);
                convertView.setTag(holdView);
            }else {
                holdView = (HoldView) convertView.getTag();
            }
            final Arrt_Dell_List item = showList.get(position);
            if(item.type == 1){
                holdView.title.setVisibility(View.VISIBLE);
                holdView.dell.setVisibility(View.GONE);
                holdView.end.setVisibility(View.GONE);
                holdView.title.setText(item.attr_name);
            }else if(item.type == 0){
                holdView.title.setVisibility(View.GONE);
                holdView.end.setVisibility(View.GONE);
                holdView.dell.setVisibility(View.VISIBLE);
                if(item.title1 != null){
                    holdView.item1.setVisibility(View.VISIBLE);
                    holdView.title1.setText(item.title1);
                    holdView.count1.setText(item.count1);
                }else{
                    holdView.item1.setVisibility(View.GONE);
                }

                if(item.title2 != null){
                    holdView.item2.setVisibility(View.VISIBLE);
                    holdView.title2.setText(item.title2);
                    holdView.count2.setText(item.count2);
                }else{
                    holdView.item2.setVisibility(View.GONE);
                }
            }else if(item.type == -1){
                holdView.title.setVisibility(View.GONE);
                holdView.dell.setVisibility(View.GONE);
                holdView.end.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
        class HoldView{
            TextView title,title1,title2,count1,count2,end;
            LinearLayout dell,item1,item2;
        }
    }
    private void getJson(){
        String url = "http://erp.zhongkechuangxiang.com/api/Product/MPdetail";
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 1){
                    progress.setVisibility(View.GONE);
                    String ret = (String)msg.obj;
                    Gson gson = new Gson();
                    HuoBanEntity data = gson.fromJson(ret, HuoBanEntity.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            titledes.setText(data.data.enterprise_name);
                            hearttitle.setText(data.data.enterprise_name);
                            youxiaoqi.setText("会员有效期至："+data.data.sign_time);
                            jingjiren.setText("技术经纪人： "+data.data.tech_agent_name);
                            final String phone = data.data.tech_agent_tel;
                            jingjiren.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    Uri data = Uri.parse("tel:" + phone);
                                    intent.setData(data);
                                    startActivity(intent);
                                }
                            });
                            if(data.data.product_arrt_list != null && data.data.product_arrt_list.size()>0){
                                for(int i=0; i<data.data.product_arrt_list.size(); i++){
                                    Arrt_List item = data.data.product_arrt_list.get(i);
                                    Arrt_Dell_List postitle = new Arrt_Dell_List();
                                    postitle.type=1;
                                    postitle.attr_name = item.attr_name;
                                    showList.add(postitle);
                                    boolean addstate =false;
                                    Arrt_Dell_List post = null;
                                    for(int j=0;j<item.arrt_list.size();j++){
                                        Arrt_Dell_List pos = item.arrt_list.get(j);
                                        int k = j % 2 ;
                                        if(k==0){
                                            addstate =false;
                                            post = new Arrt_Dell_List();
                                            post.title1 = pos.attr_name;
                                            post.count1 = pos.count_text;
                                        }else if(k == 1){
                                            addstate=true;
                                            post.title2 = pos.attr_name;
                                            post.count2 = pos.count_text;
                                            showList.add(post);
                                        }
                                        if(!addstate && (j == item.arrt_list.size()-1)){
                                            showList.add(post);
                                        }
                                    }
                                }
                                Arrt_Dell_List end = new Arrt_Dell_List();
                                end.type=-1;
                                showList.add(end);
                            }
                            if(adapter == null){
                                listview.addHeaderView(heartView);
                                adapter = new HuoBanAdapter();
                                listview.setAdapter(adapter);
                            }else{
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }catch (Exception e){

            }
        }
    };
}
