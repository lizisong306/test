package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.maidiantech.common.ui.*;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import adapter.AddProjectadapter;
import adapter.AddShebeiAdapter;
import application.MyApplication;
import entity.Codes;
import entity.Datas;
import entity.ImageState;
import entity.Posts;
import entity.Renlist;
import entity.Sblist;
import view.RefreshListView;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/7/3.
 */

public class AddSheBei extends com.maidiantech.common.ui.BaseActivity {

    LinearLayout layout;
    RefreshListView list;
    TextView cenel,queren;
    RelativeLayout bottmon;
    AddShebeiAdapter adapter;
    private  String   ips;
    private String jsons;
    private String pubdate="";
    private int num=1;
    private int Size = 10;
    private Datas data;
    private ProgressBar progress;
    private List<Posts> postsListData = new ArrayList<>();
    HashMap<String, String > hashMap = new HashMap<>();
    public static Posts retdata;
    public static boolean isExit = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addshebei);
        setSystemBar();
        setRightIconHide();
        setTitle("选择设备");

        list = (RefreshListView)findViewById(R.id.listview);
        cenel = (TextView)findViewById(R.id.cenel);
        queren = (TextView)findViewById(R.id.queding);

        layout = (LinearLayout)findViewById(R.id.layout);
        bottmon = (RelativeLayout)findViewById(R.id.bottmon);
        progress = (ProgressBar)findViewById(R.id.progress);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSheBei.this, XuQiuSearch.class);
                intent.putExtra("type","shebei");
                startActivity(intent);
            }
        });
        cenel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottmon.setVisibility(View.GONE);
                adapter.setCurrent(-1);
                AddXuQiu.data = null;

            }
        });
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回数据
                AddXuQiu.data = retdata;
                Intent intent = new Intent(AddSheBei.this, TianXuQiu.class);
                intent.putExtra("aid", retdata.getId());
                intent.putExtra("typeid","7");
                startActivity(intent);
                finish();
            }
        });
        list.setPullDownToRefreshable(true);
        list.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            /**
             *
             */
            @Override
            public void pullDownToRefresh() {
                list.setPullUpToRefreshFinish();
                int  netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(AddSheBei.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    list.setPullDownToRefreshFinish();
                } else {
                    num = 1;
                    getjsons(Size, pubdate,num);
                    handler.sendEmptyMessageDelayed(2, 5000);
                }
            }
        });
        list.setPullUpToRefreshable(true);
        list.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
                int  netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(AddSheBei.this, "网络不给力", Toast.LENGTH_SHORT).show();
                } else {

                    if(postsListData.size()==0){
                    }else{
                        if (postsListData.get(postsListData.size() - 1).getResult() != null && postsListData.get(postsListData.size() - 1).getResult().equals("no")) {
                            Toast.makeText(AddSheBei.this, "没有更多数据", Toast.LENGTH_SHORT).show();
                            list.setPullUpToRefreshFinish();
                        }
                        pubdate = postsListData.get(postsListData.size() - 1).getSortTime();
                        ++num;
                        getjsons(Size, pubdate,num);
                        Message msgs=Message.obtain();
                        msgs.what=2;
                        dismissDialog.sendMessageDelayed(msgs,5000);
                    }
                }
            }
        });
        lazyLoad();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("需求-添加设备");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("需求-添加设备");
        MobclickAgent.onResume(this);
        if(isExit){
            finish();
        }
        try {
            if(adapter != null){

                if(postsListData != null){
                    if(XuQiuSearch.shebei != null){
                        if(XuQiuSearch.shebei.size() >0){
                            adapter.setCurrent(-1);
                            bottmon.setVisibility(View.GONE);
                            list.setPullDownToRefreshable(false);
                            list.setPullUpToRefreshable(false);
                            postsListData.clear();
                            for(int i=0; i<XuQiuSearch.shebei.size();i++){
                                Sblist temp = XuQiuSearch.shebei.get(i);
                                Posts item = new Posts();
                                item.setId(temp.getId());
                                item.setTypename(temp.getTypename());
                                item.setArea_cate(temp.getArea_cate());
                                item.setTitle(temp.getTitle());
                                item.setClick(temp.getClick());
                                item.setDescription(temp.getDescription());
                                item.setLitpic(temp.getLitpic());

                                ImageState imgstate = new ImageState();
                                imgstate.image1 = temp.getLitpic();
                                item.image = imgstate;
                                postsListData.add(item);
                            }

                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){

        }
    }

    public void getjsons(final int Size, final String pubdate, final int num) {
        ips = MyApplication.ip;
        String url ="http://"+ips+"/api/arc_index.php";
        HashMap<String,String> map = new HashMap<>();
        map.put("pageSize",Size+"");
        map.put("LastModifiedTime",pubdate);
        map.put("typeid","7");
        map.put("rand","1");
        map.put("page",num+"");
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,3,0);

    }

    public String getXingQu(){
        String value = "";
        String evalue = "";
        value = SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_DINAZIXINXIN,"0");
        if(!value.equals("0")){
            evalue = value;
        }
        value = SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_SHENGWUJISHU,"0");
        if(!value.equals("0")){
            evalue += ","+value;
        }
        value = SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_JIENENGHUANBAO,"0");
        if(!value.equals("0")){
            evalue += ","+value;
        }

        value = SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_XIANJINZHIZHAO,"0");
        if(!value.equals("0")){
            evalue += ","+value;
        }

        value = SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_XINNENGYUAN,"0");
        if(!value.equals("0")){
            evalue += ","+value;
        }

        value = SharedPreferencesUtil.getString(SharedPreferencesUtil.XINQU_HUAHUEHUAGONG,"0");
        if(!value.equals("0")){
            evalue += ","+value;
        }

        value = SharedPreferencesUtil.getString(SharedPreferencesUtil.XINQU_WENHUACHUANYI,"0");
        if(!value.equals("0")){
            evalue += ","+value;
        }

        value = SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_XINCAILIAO,"0");
        if(!value.equals("0")){
            evalue += ","+value;
        }

        value = SharedPreferencesUtil.getString(SharedPreferencesUtil.XINGQU_QITA,"0");
        if(!value.equals("0")){
            evalue += ","+value;
        }
        return evalue;
    }

    private  void lazyLoad() {
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(AddSheBei.this, "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            progress.setVisibility(View.VISIBLE);
            Message msg = Message.obtain();
            msg.what = 10;
            dismissDialog.sendMessageDelayed(msg, 5000);
            getjsons(Size, pubdate,num);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == 3){
                    progress.setVisibility(View.GONE);
                    Gson gs = new Gson();
                    jsons = (String) msg.obj;
                    Codes codes = gs.fromJson(jsons, Codes.class);
                    data = codes.getData();
                    if(postsListData != null){
                        List<Posts> post =data.getPosts();
                        if(post != null){
                            for(int i=0; i<post.size(); i++){
                                Posts item = post.get(i);
                                String title =  hashMap.get(item.getId());
                                if(title == null){
                                    postsListData.add(item);
                                    hashMap.put(item.getId(),item.getId());
                                }
                            }
                        }
                    }
                    if (adapter == null) {
                        adapter = new AddShebeiAdapter(AddSheBei.this, postsListData);
                        list.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }

                    adapter.notifyDataSetChanged();
                    list.setPullDownToRefreshFinish();
                    list.setPullUpToRefreshFinish();
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                adapter.setCurrent(position-1);
                                adapter.notifyDataSetChanged();
                                bottmon.setVisibility(View.VISIBLE);
                                retdata = postsListData.get(position-1);
                            }catch (IndexOutOfBoundsException ex){

                            }catch (Exception e){
                            }
                        }
                    });
                }

            }catch (Exception e){

            }

        }
    };

    Handler dismissDialog = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress.setVisibility(View.GONE);
            if(msg.what==1){
                list.setPullDownToRefreshFinish();
                list.setPullUpToRefreshFinish();
            }
            if(msg.what==2){
                list.setPullDownToRefreshFinish();
                list.setPullUpToRefreshFinish();
            }
            if(msg.what == 10){
                getjsons(Size, pubdate,num);
            }

        }
    };

}
