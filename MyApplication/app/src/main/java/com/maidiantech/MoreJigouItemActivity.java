package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.NetUtils;
import adapter.LookMoreAdapter;
import adapter.MoreJigouItemAdapter;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.ADS;
import entity.JigouItemEntity;
import entity.LookMoreEntry;
import entity.PostData;
import entity.Posts;
import view.RefreshListView;
import view.RoundImageView;
import view.StyleUtils1;
import Util.OkHttpUtils;
import Util.KeySort;
/**
 * Created by lizisong on 2018/1/19.
 */

public class MoreJigouItemActivity extends AutoLayoutActivity{

    private int screenWidth;
    private boolean flag = false;
    private RefreshListView listView;

    public List<ADS> adsListData=new ArrayList<>();
    int netWorkType = 0;
    private List<Posts> postsListData = new ArrayList<>();
    private String pubdate="";
    private String channelName;
    private OkHttpUtils Okhttp;
    private int Size = 20;
    MoreJigouItemAdapter adapter;
    private ProgressBar progress;
    HashMap<String, String > hashMap = new HashMap<>();
    int widthPixels;
    int heightPixels;
    private String ips,jsons;
    ImageView back;
    TextView titlecontent;

    String title;
    int page=1;
    JigouItemEntity data;
    String typeid="";
    String aid="";
    String lastModifiedTime="";

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
        typeid = getIntent().getStringExtra("typeid");
        aid = getIntent().getStringExtra("aid");
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
                    Toast.makeText(MoreJigouItemActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    listView.setPullDownToRefreshFinish();
                } else {
                    postsListData.clear();
                    pubdate = "";
                    page =1;
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
                    Toast.makeText(MoreJigouItemActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                } else {
                    if (postsListData.size() == 0) {

                    } else {
                        pubdate = postsListData.get(postsListData.size() - 1).getSortTime();
                        page++;
                        getjsons(pubdate);
                        Message msgs = Message.obtain();
                        msgs.what = 2;
                        dismissDialog.sendMessageDelayed(msgs, 5000);
                    }
                }
            }
        });
        getjsons("");
    }
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
//                    MyApplication.setAccessid();
//                    String timestamp = System.currentTimeMillis()+"";
//                    String sign="";
//                    ArrayList<String> sort = new ArrayList<String>();
//                    sort.add("version"+MyApplication.version);
//                    sort.add("accessid"+MyApplication.deviceid);
//                    sort.add("timestamp"+timestamp);
//                    sort.add("pageSize"+"20");
//                    sort.add("typeid"+typeid);
//                    sort.add("LastModifiedTime"+sortid);

//                    sign = KeySort.keyScort(sort);
                    String url ="http://"+MyApplication.ip+"/api/laboratory_tavlant_project.php?typeid="+typeid+"&aid="+aid+"&pageSize=10"+"&page="+page+"&LastModifiedTime="+pubdate+"&version="+MyApplication.version;
                    Log.d("lizisong", "url:"+url);
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

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                listView.setPullDownToRefreshFinish();
                listView.setPullUpToRefreshFinish();
                progress.setVisibility(View.GONE);
                Gson gs = new Gson();
                data = gs.fromJson(jsons, JigouItemEntity.class);
                if(data != null && data.code.equals("1")){
                    if(data.data != null && data.data.size() > 0){
                        for (int i=0; i<data.data.size();i++){
                            Posts item = data.data.get(i);
                            postsListData.add(item);
                        }
//                        if(data.data.size() < 10){
//                            listView.setPullUpToRefreshable(false);
//                        }
                    }else {
                        listView.setPullUpToRefreshable(false);
                    }
                }else{
                    listView.setPullUpToRefreshable(false);
                }

                if(adapter == null){
                    adapter = new MoreJigouItemAdapter(MoreJigouItemActivity.this, postsListData, channelName);
                    listView.setAdapter(adapter);
                }else{
                    adapter.notifyDataSetChanged();
                }
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            Log.d("lizisong",postsListData.get(position - 1).getTypename()+"");
                            if(postsListData.get(position - 1).getTypename().equals("人才")|| postsListData.get(position - 1).getTypename().equals("专家")){
                                Intent intent = new Intent(MoreJigouItemActivity.this, NewRenCaiTail.class);
                                intent.putExtra("aid", postsListData.get(position - 1).getId());
                                startActivity(intent);
                            }else if(postsListData.get(position - 1).getTypename().equals("项目")){
                                Intent intent = new Intent(MoreJigouItemActivity.this, NewProjectActivity.class);
                                intent.putExtra("aid", postsListData.get(position - 1).getId());
                                startActivity(intent);
                            }else if(postsListData.get(position - 1).getTypename().equals("html") && postsListData.get(position - 1).typeid.equals("2")){
                                Intent intent = new Intent(MoreJigouItemActivity.this, ActiveActivity.class);
                                intent.putExtra("title", postsListData.get(position - 1).getTitle());
                                intent.putExtra("url", postsListData.get(position - 1).url);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(MoreJigouItemActivity.this, DetailsActivity.class);
                                intent.putExtra("id", postsListData.get(position - 1).getId());
                                intent.putExtra("name", postsListData.get(position - 1).getTypename());
                                intent.putExtra("pic", postsListData.get(position - 1).getLitpic());
                                startActivity(intent);
                            }


                        } catch (IndexOutOfBoundsException ex) {

                        } catch (Exception e) {
                        }
                    }
                });
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
//    ImageView yujian_backs;
//    TextView titlecontent;
//    RefreshListView listview;
//    ProgressBar progress;
//    String typeid="";
//    String aid="";
//    String lastModifiedTime="";
//    String json;
//    JigouItemEntity data;
//    List<Posts> showList = new ArrayList<>();
//    private DisplayImageOptions options;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.morejigouitemactivity);
//        StyleUtils1.initSystemBar(this);
////        //设置状态栏是否沉浸
//        StyleUtils1.setStyle(this);
//        options = ImageLoaderUtils.initOptions();
//        yujian_backs = (ImageView)findViewById(R.id.yujian_backs);
//        titlecontent = (TextView)findViewById(R.id.titlecontent);
//        listview = (RefreshListView)findViewById(R.id.listview);
//        progress = (ProgressBar)findViewById(R.id.progress);
//        yujian_backs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        progress.setVisibility(View.VISIBLE);
//        int  netWorkType = NetUtils.getNetWorkType(MyApplication
//                .getContext());
//        if(netWorkType == NetUtils.NETWORKTYPE_INVALID) {
//        }else{
//            getJson();
//        }
//    }
//    private void getJson(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String url="http://192.168.1.128/api/laboratory_tavlant_project.php?typeid="+typeid+"&aid="+aid+"&pageSize=10"+"&LastModifiedTime="+lastModifiedTime;
//                        json = OkHttpUtils.loaudstringfromurl(url);
//                        if(json != null){
//                            Message msg = Message.obtain();
//                            msg.what=1;
//                            handler.sendMessage(msg);
//                        }
//                    }
//                }).start();
//
//            }
//        }).start();
//    }
//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if(msg.what == 1){
//                progress.setVisibility(View.GONE);
//                Gson g = new Gson();
//                data=g.fromJson(json,JigouItemEntity.class);
//                if(data != null && data.code.equals("1")){
//                    if(data.data != null && data.data.size() > 0){
//                        for(int i=0; i<data.data.size();i++){
//                            Posts item = data.data.get(i);
//                            showList.add(item);
//                        }
//                    }
//                }
//            }
//        }
//    };
//    class MoreJigouItem extends BaseAdapter{
//
//        @Override
//        public int getCount() {
//            return showList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return showList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            HoldView holdView;
//            if(convertView == null){
//                holdView = new HoldView();
//                convertView = View.inflate(MoreJigouItemActivity.this,R.layout.morejigouitemadapter,null);
//
//                holdView.xiangmu_lay = (LinearLayout)convertView.findViewById(R.id.xiangmu_lay);
//                holdView.xiangmu_lay1 = (RelativeLayout)convertView.findViewById(R.id.xiangmu_lay1);
//                holdView.xianmgmu1 = (ImageView)convertView.findViewById(R.id.xianmgmu1);
//                holdView.xmtitle1 = (TextView)convertView.findViewById(R.id.xmtitle1);
//                holdView.lanyuan1 =(TextView)convertView.findViewById(R.id.lanyuan1);
//
//                holdView.rencai_lay=(LinearLayout)convertView.findViewById(R.id.rencai_lay);
//                holdView.rencai_lay1 = (RelativeLayout)convertView.findViewById(R.id.rencai_lay1);
//                holdView.rencai_img1 = (RoundImageView)convertView.findViewById(R.id.rencai_img1);
//                holdView.rencai_title1=(TextView)convertView.findViewById(R.id.rencai_title1);
//                holdView.rencai_lingyu1=(TextView)convertView.findViewById(R.id.rencai_lingyu1);
//                holdView.rank1 = (TextView)convertView.findViewById(R.id.rank1);
//                convertView.setTag(holdView);
//            }else{
//                holdView = (HoldView)convertView.getTag();
//                final Posts item = showList.get(position);
//                if(item.typeid.equals("4")){
//                    holdView.xiangmu_lay.setVisibility(View.GONE);
//                    holdView.rencai_lay.setVisibility(View.VISIBLE);
//                    ImageLoader.getInstance().displayImage(item.getLitpic()
//                            , holdView.rencai_img1, options);
//                    holdView.rencai_title1.setText(item.getTitle());
//                    holdView.rencai_lingyu1.setText("所属领域:"+item.getArea_cate().getArea_cate1());
//                    holdView.rank1.setText(item.getRank());
//                }else if(item.typeid.equals("2")){
//                    holdView.xiangmu_lay.setVisibility(View.VISIBLE);
//                    holdView.rencai_lay.setVisibility(View.GONE);
//                    ImageLoader.getInstance().displayImage(item.getLitpic()
//                            , holdView.xianmgmu1, options);
//                    holdView.xmtitle1.setText(item.getTitle());
//                    try {
//                        holdView.lanyuan1.setText("所属领域:"+item.getArea_cate().getArea_cate1());
//                    }catch (Exception e){
//
//                    }
//
//                }
//            }
//
//            return convertView;
//        }
//
//        class HoldView{
//            //项目
//            public LinearLayout xiangmu_lay;
//            public RelativeLayout xiangmu_lay1;
//            public ImageView xianmgmu1;
//            public TextView xmtitle1;
//            public TextView lanyuan1;
//
//            //人才
//            public LinearLayout rencai_lay;
//            public RelativeLayout rencai_lay1;
//            public RoundImageView rencai_img1;
//            public TextView rencai_title1;
//            public TextView rencai_lingyu1;
//            public TextView rank1;
//
//        }
//
//    }
}
