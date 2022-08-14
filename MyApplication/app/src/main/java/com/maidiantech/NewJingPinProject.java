package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Util.NetUtils;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.DuiJieData;
import entity.XiangMuDuiJieEntity;
import view.RefreshListView;
import view.StyleUtils1;



/**
 * Created by Administrator on 2018/11/22.
 */

public class NewJingPinProject extends AutoLayoutActivity {
    ImageView back;
    RefreshListView listview;
    ProgressBar progressBar;
    XiangMuDuiJieEntity data;
    String ret;
    List<DuiJieData> listData = new ArrayList<>();
    String flag="";
    DisplayImageOptions options;
    Adapter adapter;
    String sortid="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiangmuduijieactivity);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        options = ImageLoaderUtils.initOptions();
        back = (ImageView)findViewById(R.id.information_back);
        listview = (RefreshListView)findViewById(R.id.listview);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listview.setPullUpToRefreshable(true);
        listview.setPullDownToRefreshable(true);
        listview.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            @Override
            public void pullDownToRefresh() {
               int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    listview.setPullDownToRefreshFinish();
                } else {
                    sortid="";
                    progressBar.setVisibility(View.VISIBLE);
                    getJson(sortid,true);
                }
            }
        });
        listview.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
               int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    listview.setPullDownToRefreshFinish();
                }else {
                    if(listData.size()==0){
                    }else{
                        sortid = listData.get(listData.size() - 1).id;
                        getJson(sortid,false);
                    }
                }
            }
        });
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
           Toast.makeText(NewJingPinProject.this, "网络异常",Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            sortid="";
            getJson(sortid,true);
        }
    }

    private void getJson(String sortid,boolean state){
        String url="http://"+ MyApplication.ip+"/api/getProjectDockingApi.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("pagesize", "20");
        map.put("sortid",sortid);
        if(state){
            networkCom.getJson(url,map,handler,1,1);
        }else{
            networkCom.getJson(url,map,handler,1,0);
        }
    }
//    JingPinEntity data;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                listview.setPullUpToRefreshFinish();
                listview.setPullDownToRefreshFinish();
                Gson gson = new Gson();
                String ret = (String )msg.obj;
                data = gson.fromJson(ret, XiangMuDuiJieEntity.class);
                if(msg.what == 1){
                    listData.clear();
                }

                if(data != null){

                    if(data.code == 1){

                        if(data.data!= null){

                            if(data.data.size() >0){
                                for(int i=0;i<data.data.size();i++){
                                    DuiJieData item = data.data.get(i);
                                    listData.add(item);
                                }
                                DuiJieData pos = new DuiJieData() ;
                                pos.typeid="-1";
                                listData.add(pos);
                            }
                        }
                        if(data.data.size() < 20){
                            listview.setPullUpToRefreshable(false);
                        }
                    }
                }

                progressBar.setVisibility(View.GONE);

                if(adapter == null){
                    adapter = new Adapter();
                    listview.setAdapter(adapter);
                }else{
                    adapter.notifyDataSetChanged();
                }


            }catch (Exception e){

            }
        }
    };

    class JingPinEntity{
        public int code;
        public String message;
        public List<JingPinData> data;
    }
    class JingPinData{
        public String id;
        public String title;
        public String typeid;
        public String typename;
        public String litpic;
        public String labels;
        public String picUrl;
        public String url;
        public String pubdate;
        public String sortTime;
    }
    class Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView holdView;
            if(convertView == null){
                holdView = new HoldView();
                convertView = View.inflate(NewJingPinProject.this, R.layout.jingpinadapter, null);

                holdView.yingyin = (ImageView)convertView.findViewById(R.id.yingyin);

                holdView.dixian = (RelativeLayout)convertView.findViewById(R.id.dixian);

                holdView.bg = (ImageView)convertView.findViewById(R.id.bg);

                convertView.setTag(holdView);
            }else {

                holdView = (HoldView)convertView.getTag();
            }
            final DuiJieData item = listData.get(position);
            if(item.typeid.equals("-1")){
                holdView.yingyin.setVisibility(View.GONE);
                holdView.dixian.setVisibility(View.VISIBLE);
                holdView.bg.setVisibility(View.GONE);

            }else {
                holdView.yingyin.setVisibility(View.VISIBLE);
                holdView.dixian.setVisibility(View.GONE);
                holdView.bg.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(item.picUrl
                        , holdView.bg, options);

                holdView.bg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(NewJingPinProject.this, ActiveActivity.class);
                        intent.putExtra("title", item.title);
                        intent.putExtra("url", item.url);
                        startActivity(intent);
                    }
                });
            }

            return convertView;
        }
        class HoldView{
            public ImageView bg;
            public ImageView yingyin;
            public RelativeLayout dixian;
        }
    }
}
