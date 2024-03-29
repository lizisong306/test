package com.maidiantech;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import adapter.LookMoreAdapter;
import adapter.LookMoreQingBaoAdapter;
import application.MyApplication;
import entity.ADS;
import entity.LookMoreEntry;
import entity.PostData;
import view.RefreshListView;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/9/29.
 */

public class LookMoreQingBao extends AutoLayoutActivity {
    private int screenWidth;
    private boolean flag = false;
    private RefreshListView listView;

    public List<ADS> adsListData=new ArrayList<>();
    int netWorkType = 0;
    private List<PostData> postsListData = new ArrayList<>();
    private String pubdate="";
    private String channelName;
    private OkHttpUtils Okhttp;
    private int Size = 20;
    LookMoreQingBaoAdapter adapter;
    private ProgressBar progress;
    HashMap<String, String > hashMap = new HashMap<>();
    int widthPixels;
    int heightPixels;
    private String ips,jsons;
    ImageView back;
    TextView titlecontent;
    int typeid;
    String title;
    LookMoreEntry data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookmoreactivity);
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        widthPixels=metrics.widthPixels;
        heightPixels=metrics.heightPixels;
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        listView = (RefreshListView) findViewById(R.id.listview);
        progress = (ProgressBar) findViewById(R.id.progress);
        back = (ImageView) findViewById(R.id.yujian_backs);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlecontent = (TextView)findViewById(R.id.titlecontent);
        titlecontent.setText("");
        typeid = getIntent().getIntExtra("typeid", 0);
        title  = getIntent().getStringExtra("title");
        channelName = getIntent().getStringExtra("channel");
        titlecontent.setText(title);
        progress.setVisibility(View.VISIBLE);
        listView.setPullDownToRefreshable(true);
        listView.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            /**
             *
             */
            @Override
            public void pullDownToRefresh() {
                netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());

                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(LookMoreQingBao.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    listView.setPullDownToRefreshFinish();
                } else {
                    postsListData.clear();
                    pubdate = "";
                    listView.setPullUpToRefreshable(true);
                    getjsons(pubdate);
                    handler.sendEmptyMessageDelayed(2, 5000);
                }
            }
        });

        listView.setPullUpToRefreshable(true);

        listView.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
                netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(LookMoreQingBao.this, "网络不给力", Toast.LENGTH_SHORT).show();
                } else {
                    if (postsListData.size() == 0) {

                    } else {
                        pubdate = postsListData.get(postsListData.size() - 1).sortTime;
                        getjsons(pubdate);
                        Message msgs = Message.obtain();
                        msgs.what = 2;
                        dismissDialog.sendMessageDelayed(msgs, 5000);
                    }
                }
            }
        });
        getjsons(pubdate);
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                listView.setPullDownToRefreshFinish();
                listView.setPullUpToRefreshFinish();
                progress.setVisibility(View.GONE);
                Gson gs = new Gson();
                data = gs.fromJson(jsons, LookMoreEntry.class);
                if(data != null && data.code.equals("1")){
                    if(data.data != null && data.data.size() > 0){
                        for (int i=0; i<data.data.size();i++){
                            PostData item = data.data.get(i);
                            postsListData.add(item);
                        }
                        if(data.data.size() < 20){
                            listView.setPullUpToRefreshable(false);
                        }
                    }else {
                        listView.setPullUpToRefreshable(false);
                    }
                }else{
                    listView.setPullUpToRefreshable(false);
                }

                if(adapter == null){
                    adapter = new LookMoreQingBaoAdapter(LookMoreQingBao.this, postsListData);
                    listView.setAdapter(adapter);
                }else{
                    adapter.notifyDataSetChanged();
                }
            }

        }
    };

    private Handler dismissDialog = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            UIHelper.hideDialogForLoading();
            progress.setVisibility(View.GONE);
            if (msg.what == 1) {
                listView.setPullDownToRefreshFinish();
                listView.setPullUpToRefreshFinish();
            }
            if (msg.what == 2) {
                listView.setPullDownToRefreshFinish();
                listView.setPullUpToRefreshFinish();
            }
        }
    };


    private void getjsons(final String sortid){
        Okhttp = OkHttpUtils.getInstancesOkHttp();
        ips = MyApplication.ip;
        new Thread() {
            /**
             *
             */
            @Override
            public void run() {
                super.run();
                // init();
                // 123.207.164.210  测试IP地址
                // 123.206.8.208:80 正式IP地址
//                Log.d("lizisong", "getjsons gogogo");
                try {
                    MyApplication.setAccessid();
                    String timestamp = System.currentTimeMillis()+"";
                    String sign="";
                    ArrayList<String> sort = new ArrayList<String>();
                    sort.add("version"+MyApplication.version);
                    sort.add("accessid"+MyApplication.deviceid);
                    sort.add("timestamp"+timestamp);
                    sort.add("pageSize"+"20");
                    sort.add("typeid"+typeid);
                    sort.add("LastModifiedTime"+sortid);
                    sign = KeySort.keyScort(sort);
                    String url ="http://"+ips+"/api/getHomeRecommendData.php?pageSize=20&typeid="+typeid+"&LastModifiedTime="+sortid+"&version="+MyApplication.version+MyApplication.accessid+"&timestamp="+timestamp+"&sign="+sign;
                    jsons = Okhttp.loaudstringfromurl(url);

                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (Exception e) {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    dismissDialog.sendMessage(msg);
                }
            }
        }.start();
    }
}
