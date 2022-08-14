package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import adapter.RecommendAdapter;
import application.MyApplication;
import entity.Posts;
import entity.XiangMuDuiJieListEntity;
import view.RefreshListView;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/9/27.
 */

public class XiangMuListActivity extends AutoLayoutActivity {
    ImageView back;
    RefreshListView listview;
    ProgressBar progressBar;
    String ret;
    RecommendAdapter adapter;
    private List<Posts> postsListData = new ArrayList<>();
    XiangMuDuiJieListEntity data;
    String id,LastModifiedTime="";
    int netWorkType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiangmuduijieactivity);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        id = getIntent().getStringExtra("id");
        back = (ImageView)findViewById(R.id.information_back);
        listview = (RefreshListView)findViewById(R.id.listview);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        listview.setPullDownToRefreshable(true);
        listview.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            /**
             *
             */
            @Override
            public void pullDownToRefresh() {
                netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());

                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(XiangMuListActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    listview.setPullDownToRefreshFinish();
                } else {
                    LastModifiedTime = "";
                    listview.setPullUpToRefreshable(true);
                    getJson();
                    handler.sendEmptyMessageDelayed(2, 5000);
                }
            }
        });

        listview.setPullUpToRefreshable(true);

        listview.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
                netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(XiangMuListActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                } else {
                    if (postsListData.size() == 0) {

                    } else {
                        LastModifiedTime = postsListData.get(postsListData.size() - 1).getSortTime();
                        getJson();
                        Message msgs = Message.obtain();
                        msgs.what = 2;
                        dismissDialog.sendMessageDelayed(msgs, 5000);
                    }
                }
            }
        });
        getJson();

    }
    private void getJson(){
        String url = "http://"+ MyApplication.ip+"/api/getProjectDocking.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("action", "list");
        map.put("id", id);
        map.put("pageSize","20");
        map.put("LastModifiedTime",LastModifiedTime);
        networkCom.getJson(url,map,handler,1,0);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    MyApplication.setAccessid();
//                    String timestamp = System.currentTimeMillis()+"";
//                    String sign="";
//                    ArrayList<String> sort = new ArrayList<String>();
//                    sort.add("version"+MyApplication.version);
//                    sort.add("accessid"+MyApplication.deviceid);
//                    sort.add("timestamp"+timestamp);
//                    sort.add("action"+"list");
//                    sort.add("id"+id);
//                    sort.add("pageSize"+"20");
//                    sort.add("LastModifiedTime"+LastModifiedTime);
//                    sign = KeySort.keyScort(sort);
//                    String url = "http://"+ MyApplication.ip+"/api/getProjectDocking.php?action=list&id="+id+"&pageSize=20&LastModifiedTime="+LastModifiedTime+"&version="+MyApplication.version+MyApplication.deviceid+"&timestamp="+timestamp+"&sign="+sign;
//                    ret = OkHttpUtils.loaudstringfromurl(url);
//                    if(ret != null){
//                        Message msg = Message.obtain();
//                        msg.what =1;
//                        handler.sendMessage(msg);
//                    }
//                }catch (Exception e){
//
//                }
//
//            }
//        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 1){
                    progressBar.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    ret = (String)msg.obj;
                    data = gson.fromJson(ret, XiangMuDuiJieListEntity.class);
                    if(data != null && data.code.equals("1")){
                        if(data.data != null && data.data.size() >0 ){
                            for(int i=0; i<data.data.size();i++){
                                Posts item = data.data.get(i);
                                postsListData.add(item);
                            }
                            if(data.data.size() < 20){
                                listview.setPullUpToRefreshable(false);
                            }
                        }else{
                            listview.setPullUpToRefreshable(false);
                        }
                        if(adapter == null){
                            adapter = new RecommendAdapter(XiangMuListActivity.this, postsListData, "项目", "");
                            listview.setAdapter(adapter);
                        }else {
                            adapter.notifyDataSetChanged();
                        }
                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Posts posts = postsListData.get(position-1);
                                if(posts != null){
                                    if(posts.typeid.equals("2")){
                                        Intent intent = new Intent(XiangMuListActivity.this, NewProjectActivity.class);
                                        intent.putExtra("aid", postsListData.get(position-1).getId());
                                        startActivity(intent);
                                        return;
                                    }
                                }

                                Intent intent = new Intent(XiangMuListActivity.this, DetailsActivity.class);
                                intent.putExtra("id", postsListData.get(position-1).getId());
                                intent.putExtra("name", postsListData.get(position-1).getTypename());
                                intent.putExtra("pic", postsListData.get(position-1).getLitpic());
                                startActivity(intent);
                            }
                        });
                    }else{
                        listview.setPullUpToRefreshable(false);
                    }

                }
            }catch (Exception e){

            }

        }
    };

    private Handler dismissDialog = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            UIHelper.hideDialogForLoading();
            progressBar.setVisibility(View.GONE);
            if (msg.what == 1) {
                listview.setPullDownToRefreshFinish();
                listview.setPullUpToRefreshFinish();
            }
            if (msg.what == 2) {
                listview.setPullDownToRefreshFinish();
                listview.setPullUpToRefreshFinish();
            }
        }
    };
}
