package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.chenantao.autolayout.AutoLinearLayout;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.mm.opensdk.utils.Log;
import com.umeng.analytics.MobclickAgent;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.PrefUtils;
import Util.SharedPreferencesUtil;
import adapter.LunboAdapters;
import adapter.RecommendAdapter;
import application.MyApplication;
import entity.ADS;
import entity.Codes;
import entity.Datas;
import entity.Posts;
import view.RefreshListView;
import view.StyleUtils;
import view.StyleUtils1;

import static dao.Sqlitetions.getInstance;




/**
 * Created by lizisong on 2017/3/2.
 */

public class DFInfoShow extends AutoLayoutActivity {
    ImageView back;
    RefreshListView list;
    private int screenWidth;
//    private ViewPager vp_pager;
//    private LinearLayout ll_dots;
    public  List<ADS> adsListData=new ArrayList<>();
    private FrameLayout layout;
//    private RelativeLayout relayout;
    int netWorkType = 0;
    private String channelName;
    private List<Posts> postsListData = new ArrayList<>();
    private String pubdate="";
    private OkHttpUtils Okhttp;
    private int Size = 10;
    private String jsons;
    private Datas data;
//    private LunboAdapters lunboadapter;
    private ArrayList<ImageView> imageList;
//    private View mHomePageHeaderView;
    RecommendAdapter Recomment;
    private String mytitle;
    private MyHandler handler = new MyHandler();
    String typeid,cityCode,cityName,prvName,prvCode,channelid ;
    private View view;
    TextView title_name;
    private ProgressBar progress;
    private  String   ips;
    private String city;
    private LinearLayout city_line;
    private TextView city_guojia,city_sd,city_dz,city_gz,city_lineline;
    private boolean  state=false;
    private boolean  state_sd=false;
    private boolean  state_dz=false;
    private boolean  state_gz=false;
    private String status="1";
    private String orders="0";
    private String nations="";
    private String provinces="";
    private String sortid="";

    HashMap<String, String > hashMap = new HashMap<>();
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
         requestWindowFeature(Window.FEATURE_PROGRESS);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
            setContentView(R.layout.dfinfoshow);

            typeid = getIntent().getStringExtra("typeid");
            cityCode = getIntent().getStringExtra("cityCode");
            if(cityCode == null){
                cityCode="";
            }
            cityName =getIntent().getStringExtra("cityName");
            city = getIntent().getStringExtra("city");
            prvName = getIntent().getStringExtra("prvName");
            prvCode = getIntent().getStringExtra("prvCode");

            channelid = getIntent().getStringExtra("channelid");
            WindowManager wm = (WindowManager) this
                    .getSystemService(Context.WINDOW_SERVICE);
            screenWidth = wm.getDefaultDisplay().getWidth();
            back = (ImageView)findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DFInfoShow.this.finish();
                }
            });
            list = (RefreshListView)findViewById(R.id.listview);
          city_line=(LinearLayout) findViewById(R.id.city_line);
        city_lineline = (TextView)findViewById(R.id.city_lineline);
        progress=(ProgressBar) findViewById(R.id.progress);
        city_guojia=(TextView) findViewById(R.id.city_guojia);
        city_sd=(TextView) findViewById(R.id.city_sd);

//        if(city.equals("郑州")){
//            city_sd.setText("河南省");
//        }else if(city.equals("全国")){
//            city_sd.setText("全国");
//        }
        if(city.equals("全国")){
            city_sd.setText("全国");
        }else{
            city_sd.setText(prvName);
        }
        city_dz=(TextView) findViewById(R.id.city_dz);
        city_gz=(TextView) findViewById(R.id.city_gz);
//        if(city.equals("潍坊")){
//            city_dz.setText("潍坊市");
//        }else if(city.equals("德州")){
//            city_dz.setText("德州市");
//        }else if(city.equals("青岛") ){
//            city_dz.setText("青岛市");
//        }else if(city.equals("郑州")){
//            city_dz.setText("郑州市");
//        }else if(city.equals("聊城")){
//            city_dz.setText("聊城市");
//        }else if(city.equals("全国")){
//            city_dz.setText("全国");
//        }else if(city.equals("莱西")){
//            city_dz.setText("莱西市");
//        }else if(city.equals("即墨")){
//            city_dz.setText("即墨市");
//        }else{
//            city_dz.setText(city);
//        }
        city_dz.setText(cityName);
        city_guojia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setPullUpToRefreshable(true);

                if(state==false){
                    city_guojia.setTextColor(getResources().getColor(R.color.huangse));
                    progress.setVisibility(View.VISIBLE);
                    postsListData.clear();
                    hashMap.clear();
                    if(Recomment != null){
                       Recomment.notifyDataSetChanged();
                    }
                    state=true;
                    state_check();
//                    provinces = prvCode;


                    getjsons(Size, pubdate,orders,nations,provinces,status,sortid);

                }else{
                    boolean checked = checked();
                    if(!checked){
                        return;
                    }
                    state=false;

                    city_guojia.setTextColor(getResources().getColor(R.color.title));
                    progress.setVisibility(View.VISIBLE);
                    postsListData.clear();
                    hashMap.clear();
                    if(Recomment != null){
                        Recomment.notifyDataSetChanged();
                    }
                    state_check();
//                    provinces = "";
                    getjsons(Size, pubdate,orders,nations,provinces,status,sortid);

                }
            }
        });
        city_sd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setPullUpToRefreshable(true);

                if(state_sd==false){
                    city_sd.setTextColor(getResources().getColor(R.color.huangse));
                    progress.setVisibility(View.VISIBLE);
                    postsListData.clear();
                    hashMap.clear();
                    if(Recomment != null){
                        Recomment.notifyDataSetChanged();
                    }
                    state_sd=true;
                    state_check();
                    provinces=prvCode;
                    getjsons(Size, pubdate,orders,nations,provinces,status,sortid);

                }else{
                    boolean checked = checked();
                    if(!checked){
                        return;
                    }
                    state_sd=false;
                    city_sd.setTextColor(getResources().getColor(R.color.title));
                    progress.setVisibility(View.VISIBLE);
                    postsListData.clear();
                    hashMap.clear();
                    if(Recomment != null){
                        Recomment.notifyDataSetChanged();
                    }
                    state_check();
                    provinces="";
                    getjsons(Size, pubdate,orders,nations,provinces,status,sortid);

                }
            }
        });
        city_dz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setPullUpToRefreshable(true);

                if(state_dz==false){
                    city_dz.setTextColor(getResources().getColor(R.color.huangse));
                    progress.setVisibility(View.VISIBLE);
                    postsListData.clear();
                    hashMap.clear();
                    if(Recomment != null){
                        Recomment.notifyDataSetChanged();
                    }
                    state_dz=true;
                    state_check();
                    getjsons(Size, pubdate,orders,nations,provinces,status,sortid);
                }else{
                    boolean checked = checked();
                    if(!checked){
                        return;
                    }
                    state_dz=false;
                    city_dz.setTextColor(getResources().getColor(R.color.title));
                    progress.setVisibility(View.VISIBLE);
                    postsListData.clear();
                    hashMap.clear();
                    if(Recomment != null){
                        Recomment.notifyDataSetChanged();
                    }
                    state_check();
                    getjsons(Size, pubdate,orders,nations,provinces,status,sortid);
                }
            }
        });
        city_gz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setPullUpToRefreshable(true);
                if(state_gz==false){
                    city_gz.setTextColor(getResources().getColor(R.color.huangse));
                    progress.setVisibility(View.VISIBLE);
                    postsListData.clear();
                    hashMap.clear();
                    if(Recomment != null){
                        Recomment.notifyDataSetChanged();
                    }
                    state_gz=true;
                    state_check();
                    getjsons(Size, pubdate,orders,nations,provinces,status,sortid);
                }else{
                    state_gz=false;
                    city_gz.setTextColor(getResources().getColor(R.color.title));
                    progress.setVisibility(View.VISIBLE);
                    postsListData.clear();
                    hashMap.clear();
                    if(Recomment != null){
                        Recomment.notifyDataSetChanged();
                    }
                    state_check();
                    getjsons(Size, pubdate,orders,nations,provinces,status,sortid);
                }
            }
        });

        progress.setVisibility(View.VISIBLE);
//            mHomePageHeaderView = getLayoutInflater().inflate(R.layout.my_lunbo, null);
            title_name = (TextView)findViewById(R.id.title_name);
            if(typeid.equals("1") ){
                title_name.setText(city+"-资讯");
                city_line.setVisibility(View.GONE);
                city_lineline.setVisibility(View.GONE);
            }else if(typeid.equals("6")){
                title_name.setText(city+"-政策");
                city_line.setVisibility(View.VISIBLE);
                title_name.setTextColor(getResources().getColor(R.color.huangse));
            }else if(typeid.equals("11")){
                title_name.setText(city+"-活动");
                city_line.setVisibility(View.GONE);
                city_lineline.setVisibility(View.GONE);
            }
            else if(typeid.equals("xr")){
                title_name.setText(city+"-资源");
                city_line.setVisibility(View.GONE);
                city_lineline.setVisibility(View.GONE);
            }
//            vp_pager =(ViewPager) mHomePageHeaderView.findViewById(R.id.pager);
//            ll_dots = (LinearLayout) mHomePageHeaderView.findViewById(R.id.layout);
            layout = new FrameLayout(this);
//            relayout = (RelativeLayout) mHomePageHeaderView.findViewById(R.id.relayout);
//            list.addHeaderView(mHomePageHeaderView, null, false);
//            list.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//
//                    return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//                }
//
//        });
        list.setPullDownToRefreshable(true);
        list.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            /**
             *
             */
            @Override
            public void pullDownToRefresh() {
                netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());

                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(DFInfoShow.this, "网络不给力", Toast.LENGTH_SHORT).show();
                } else {
//                    postsListData.clear();
//                    hashMap.clear();
                    if(Recomment!=null){
                        Recomment.notifyDataSetChanged();
                    }
                    pubdate = "";
                    sortid="";
                    getjsons(Size, pubdate,orders,nations,provinces,status,sortid);
                    handler.sendEmptyMessageDelayed(2, 5000);
                }
//                    listView.showTipTitle("跟新了10条数据");



            }
        });
        list.setPullUpToRefreshable(true);
        list.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
                netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(DFInfoShow.this, "网络不给力", Toast.LENGTH_SHORT).show();

                } else {

                    if(postsListData.size()==0){
//                            psc.onRefreshComplete();
                    }else{
//                        if (postsListData.get(postsListData.size() - 1).getResult() != null && postsListData.get(postsListData.size() - 1).getResult().equals("no")) {
//                            list.setPullUpToRefreshFinish();
//                            list.setPullUpToRefreshable(false);
//                            Toast.makeText(DFInfoShow.this, "已是最后一条数据",Toast.LENGTH_SHORT).show();
//                            Message msgs = Message.obtain();
//                            msgs.what = 2;
//                            dismissDialog.sendMessageDelayed(msgs, 2000);
//                            return;
//
//                        }
                        pubdate = postsListData.get(postsListData.size() - 1).getSortTime();
//                        if( state_gz==false){
//                            sortid="";
//                        }else{
//                            sortid=postsListData.get(postsListData.size() - 1).getId();
//                            Log.d("lizisong","id:"+postsListData.get(postsListData.size() - 1).getId());
//                        }
                        sortid=postsListData.get(postsListData.size() - 1).getId();
                        Log.d("lizisong","id:"+postsListData.get(postsListData.size() - 1).getId());
                        getjsons(Size, pubdate,orders,nations,provinces,status,sortid);

                        Message msgs=Message.obtain();
                        msgs.what=2;
                        dismissDialog.sendMessageDelayed(msgs,5000);

                    }
                }
            }
        });
        /**
         * 根据滑动状态，改变轮播的状态
         */
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
//                        ImageLoader.getInstance().pause();
                        stopLunBo();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                        ImageLoader.getInstance().resume();
                        startLunBo();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        stopLunBo();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(totalItemCount >= postsListData.size() &&  (netWorkType == NetUtils.NETWORKTYPE_INVALID)){
                    list.setPullUpToRefreshFinish();
                }
            }
        });

        /**
         * 页面滑动设置相对应的小圆点的改变
         *
         */
//        vp_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageSelected(int arg0) {
//                try {
//                    if (lunboadapter.getList().size() != 1) {
//                        for (int i = 0; i < lunboadapter.getList().size(); i++) {
//                            if (i == arg0 % lunboadapter.getList().size()) {
//                                imageList.get(i).setImageResource(R.drawable.dot_focused);
//                            } else {
//                                imageList.get(i).setImageResource(R.drawable.dot_normal);
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//
//                }
//                  /*  //判断是否滑动到了第二个界面，假如是那么加载4个界面，前后各两个，随着postion递增
//                     if(arg0>=2){
//                         vp_pager.setOffscreenPageLimit(arg0);
//                            }*/
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                // TODO Auto-generated method stub
//                if (state == ViewPager.SCROLL_STATE_DRAGGING) {//在 拖拽 的时候移除消息回调
//                    handler.removeCallbacksAndMessages(null);
//                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {//在 松手复位 的时候重新发送消息
//                    handler.sendEmptyMessageDelayed(0, 3000);
//                } else if (state == ViewPager.SCROLL_STATE_IDLE) {//在 空闲 时候发送消息
//                    handler.sendEmptyMessageDelayed(0, 3000);
//                }
//            }
//        });
//        //自动轮播
//        //handler.sendEmptyMessageDelayed(0, 3000);
//        vp_pager.setFocusable(true);
//        vp_pager.setFocusableInTouchMode(true);
//        vp_pager.requestFocus();
           state_dz=false;
            city_dz.setTextColor(getResources().getColor(R.color.huangse));
            progress.setVisibility(View.VISIBLE);
            postsListData.clear();
            hashMap.clear();
            state_dz=true;
            state_check();
//        UIHelper.hideDialogForLoading();
//        UIHelper.showDialogForLoading(this,"", true);
        getjsons(Size, pubdate,orders,nations,provinces,status,sortid);
     }

    private void state_check() {

//        if(state){
//            provinces =
//        }

        if(state==false){
//            orders="0";
            nations="";
//            provinces="";
            pubdate="";
            sortid="";
        }else {
//            orders="0";
            nations="1";
//            provinces="";
            pubdate="";
            sortid="";
        }
        if( state_sd==false){
            sortid="";
            provinces="";
            pubdate="";
        }else{
//           if(city.equals("郑州")){
//               provinces="8500";
//           }else if(city.equals("全国")){
//               provinces="1000";
//           }else{
//               provinces="8000";
//           }
            provinces = prvCode;
            pubdate="";
            sortid="";
        }
        if(state_dz==false){
            status="0";
            pubdate="";
            sortid="";

        }else{
            status="1";
            pubdate="";
            sortid="";
        }
        if(state_gz==false){
            orders="0";
            pubdate="";
            sortid="";

        }else{
            orders="1";
            pubdate="";
            sortid="";
        }
    }

    public void getjsons(final int Size, final String pubdate,final String orders,final String nations,final String provinces ,final  String status,final String sortid) {

//        Okhttp = OkHttpUtils.getInstancesOkHttp();
        ips = MyApplication.ip;
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        String url = "http://"+ips+"/api/placeChannel.php";
        try {
            HashMap<String,String> map = new HashMap<>();
            map.put("pageSize",Size+"");
            map.put("LastModifiedTime",pubdate);
            map.put("typeid",typeid);
            if(typeid != null && !typeid.equals("1")){
                  map.put("channel",channelid);
            }
            map.put("code", cityCode);
            Log.d("lizisong", "cityCode:"+cityCode);
            map.put("order",orders);
            map.put("status",status);
            map.put("national",nations);
            map.put("province",provinces);
            map.put("sortid",sortid);
//            Log.d("lizisong", map.toString());
            networkCom.getJson(url,map,handler,3,0);

            Message message = Message.obtain();
            message.what = 1;
            dismissDialog.sendMessageDelayed(message, 500);

        } catch (Exception e) {
            Message msg=Message.obtain();
            msg.what=1;
            dismissDialog.sendMessage(msg);
        }

    }

    private class MyHandler extends Handler{
        public void handleMessage(Message msg) {
            try {
                if (msg.what == 0 & adsListData.size() != 1) {
                    //获取当前所在页面索引值
//                        Log.d("lizisong", "msg.what==="+0);
//                    int item = vp_pager.getCurrentItem();
//                    item++;
//                    vp_pager.setCurrentItem(item);
//                    handler.removeCallbacksAndMessages(null);
//                    handler.sendEmptyMessageDelayed(0, 3000);
                }
                if(msg.what == 3){
//                        Log.d("lizisong", "msg.what==="+3);
                    progress.setVisibility(View.GONE);
                    Gson gs = new Gson();
                    jsons = (String) msg.obj;
                    Codes codes = gs.fromJson(jsons, Codes.class);
                    data = codes.getData();
                    adsListData = data.getAds();



//                    if (lunboadapter == null) {
//                        lunboadapter = new LunboAdapters(DFInfoShow.this, handler, adsListData);
//                        vp_pager.setAdapter(lunboadapter);
//                        if (adsListData.size() != 1) {
//                            initPaint();
//                            vp_pager.setCurrentItem(adsListData.size() * 5000);
//                        }
//                    } else {
//                        lunboadapter.setLunBoAdapterList(adsListData);
//                        lunboadapter.notifyDataSetChanged();
//                    }

//                    postsListData.addAll(data.getPosts());
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
                            if(post.size() < 10){
                                list.setPullUpToRefreshFinish();
                                list.setPullUpToRefreshable(false);
                                Toast.makeText(DFInfoShow.this, "已是最后一条数据",Toast.LENGTH_SHORT).show();
                                Message msgs = Message.obtain();
                                msgs.what = 2;
                                dismissDialog.sendMessageDelayed(msgs, 2000);
                            }
                        }
                    }

//                    if (adsListData.size() == 0) {
//                        relayout.setVisibility(View.GONE);
//                    } else {
//                        relayout.setVisibility(View.VISIBLE);
//                    }
//                    if(typeid == 11){
//                        for(int i=0; i<postsListData.size();i++){
//                            postsListData.get(i).setTypename("资讯");
//                        }
//                    }


                        if(Recomment==null){
                            Recomment=new RecommendAdapter(DFInfoShow.this, postsListData, channelName, mytitle);
                            if(city.equals("潍坊")){
                                Recomment.setState(1);
                            }else{
                                Recomment.setState(0);
                            }

                            list.setAdapter(Recomment);
                        }else{
                            Recomment.notifyDataSetChanged();
                            list.setPullDownToRefreshFinish();
                            list.setPullUpToRefreshFinish();
                        }




                    list.setPullDownToRefreshFinish();
                    list.setPullUpToRefreshFinish();
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                PrefUtils.setString(DFInfoShow.this,postsListData.get(position - 1).getId(),postsListData.get(position - 1).getId());
                                if(postsListData.get(position-1).getTypename().equals("专题")){
                                    Intent intent = new Intent(DFInfoShow.this, SpecialActivity.class);
                                    intent.putExtra("id", postsListData.get(position-1).getId());
                                    startActivity(intent);
                                }else {

                                    String name = postsListData.get(position - 1).getTypename();
                                    if(name != null && (name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                        if(city.equals("北京")){
                                            Intent intent = new Intent(DFInfoShow.this, BejingCeShi.class);
                                            intent.putExtra("id", postsListData.get(position - 1).getId());
                                            intent.putExtra("name", postsListData.get(position - 1).getTypename());
                                            intent.putExtra("pic", postsListData.get(position - 1).getLitpic());
                                            startActivity(intent);

                                        }else if(postsListData.get(position - 1).getTypename().equals("html")|| postsListData.get(position-1).typeid.equals("10000")){
                                                Intent intent=new Intent(DFInfoShow.this, WebViewActivity.class);
                                                intent.putExtra("title", postsListData.get(position - 1).getTitle());
                                                intent.putExtra("url", postsListData.get(position - 1).detail);
                                                startActivity(intent);

                                        }else{
                                            Intent intent = new Intent(DFInfoShow.this, ZixunDetailsActivity.class);
                                            intent.putExtra("id", postsListData.get(position - 1).getId());
                                            intent.putExtra("name", postsListData.get(position - 1).getTypename());
                                            intent.putExtra("pic", postsListData.get(position - 1).getLitpic());
                                            startActivity(intent);
                                        }

                                    }else{
                                        if(city.equals("全国")){
                                            Intent intent = new Intent(DFInfoShow.this, BejingCeShi.class);
                                            intent.putExtra("id", postsListData.get(position - 1).getId());
                                            intent.putExtra("name", postsListData.get(position - 1).getTypename());
                                            intent.putExtra("pic", postsListData.get(position - 1).getLitpic());
                                            startActivity(intent);
                                        }else if(postsListData.get(position-1).typeid.equals("5")){
                                            Intent intent = new Intent(DFInfoShow.this, NewZhuanliActivity.class);
                                            intent.putExtra("aid", postsListData.get(position - 1).getId());
                                            startActivity(intent);
                                        }else if(postsListData.get(position-1).typeid.equals("6")){
                                            Intent intent = new Intent(DFInfoShow.this, NewZhengCeActivity.class);
                                            intent.putExtra("aid", postsListData.get(position - 1).getId());
                                            startActivity(intent);
                                        }else if(postsListData.get(position - 1).getTypename().equals("html")|| postsListData.get(position-1).typeid.equals("10000")){
                                            Intent intent=new Intent(DFInfoShow.this, WebViewActivity.class);
                                            intent.putExtra("title", postsListData.get(position - 1).getTitle());
                                            intent.putExtra("url", postsListData.get(position - 1).detail);
                                            startActivity(intent);

                                        }else{
                                            Intent intent = new Intent(DFInfoShow.this, DetailsActivity.class);
                                            intent.putExtra("id", postsListData.get(position - 1).getId());
                                            intent.putExtra("name", postsListData.get(position - 1).getTypename());
                                            intent.putExtra("pic", postsListData.get(position - 1).getLitpic());
                                            startActivity(intent);
                                        }

                                    }

                                }
                            }catch (IndexOutOfBoundsException ex){

                            }catch (Exception e){
                            }
                        }
                    });
                }
            }catch (Exception e){

            }
        }
    }

    private Handler dismissDialog= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            UIHelper.hideDialogForLoading();
//            progress.setVisibility(View.GONE);
            if(msg.what==1){
                list.setPullDownToRefreshFinish();
                list.setPullUpToRefreshFinish();
            }
            if(msg.what==2){
                list.setPullDownToRefreshFinish();
                list.setPullUpToRefreshFinish();
            }
        }
    };


    /**
     * 初始化小圆点
     */
//    public void initPaint() {
//        imageList = new ArrayList<ImageView>();
//        // Log.i("adsListData",+"...................");
//        for (int i = 0; i < lunboadapter.getList().size(); i++) {
//            //画圆点
//            ImageView imageView = new ImageView(this);
//            if (i == 0) {
//                imageView.setImageResource(R.drawable.dot_focused);
//            } else {
//                imageView.setImageResource(R.drawable.dot_normal);
//            }
//            imageList.add(imageView);
//            AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(15, 15);
//            params.setMargins(5, 0, 5, 0);
//            ll_dots.addView(imageView, params);
//        }
//        if(lunboadapter != null){
//            int with= lunboadapter.getList().size()*15+(lunboadapter.getList().size()-1)*5+100;
//            lunboadapter.setWidth(screenWidth-with);
//        }
//    }

    /**
     * 停止轮播图刷新
     */
    public void stopLunBo(){
        handler.removeCallbacksAndMessages(null);
    }
    /**
     * 启动轮播图
     */
    public void startLunBo(){
        handler.sendEmptyMessageDelayed(0, 3000);
    }
    @Override
    public void onResume() {
        super.onResume();

        XGPushManager.onActivityStarted(this);
//        psc.onRefreshComplete();
        handler.sendEmptyMessageDelayed(0, 5000);
        list.setPullDownToRefreshFinish();
        if(Recomment!=null){
            Recomment.notifyDataSetChanged();
        }
        XGPushManager.onActivityStarted(this);
        MobclickAgent.onPageStart("地方频道列表"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getInstance(this).guanrsd();
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        handler.removeCallbacksAndMessages(null);
    }
    /**
     * 消除有可能出现下拉刷新的界面
     */
    public void canelDownRefrsh(){
        list.setPullDownToRefreshFinish();
    }



    @Override
    public void onStop() {
        super.onStop();

        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);

        handler.removeCallbacksAndMessages(null);
        XGPushManager.onActivityStoped(this);
        MobclickAgent.onPageEnd("地方频道列表");
    }

    private boolean checked(){
        boolean sdate;
         int count=0;
        if(state==true){
            count++;
        }
        if(state_sd==true){
            count++;
        }
        if(state_dz==true){
            count++;
        }
        if(count>1){
            sdate=true;
        }else{
            sdate=false;
        }
        return sdate;
    }


}
