package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
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
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.PrefUtils;
import Util.SharedPreferencesUtil;
import adapter.RecommendAdapter;
import application.MyApplication;
import entity.ADS;
import entity.Codes;
import entity.Datas;
import entity.Posts;
import view.ExpandTabView;
import view.RefreshListView;
import view.StyleUtils;
import view.StyleUtils1;
import view.ViewMiddle;

import static com.maidiantech.R.id.listview;
import static dao.Sqlitetions.getInstance;


/**
 * Created by lizisong on 2017/3/21.
 */

public class PolicyActivity extends AutoLayoutActivity {
    private  int screenWidth;
    public  List<ADS> adsListData=new ArrayList<>();
    private boolean flag = false;
    private RefreshListView listView;
    private LinearLayout top;
    private FrameLayout layout;
    int netWorkType = 0;
    private List<Posts> postsListData = new ArrayList<>();
    private String pubdate="";
    private String sortid="";
    private boolean mHasLoadedOnce;
    private String channelName = "政策";
    private int Size = 10;
    private String jsons;
    private Datas data;
    private ArrayList<ImageView> imageList;
    private RecommendAdapter MyitemAdapter;
    private String mytitle;
    private ProgressBar progress;
    ImageView image,search;
    TextView title;
    private int num=1;
    HashMap<String, String > hashMap = new HashMap<>();
    private  String   ips;
    private ExpandTabView expandTabView;
    TextView bottmon_line;
    private ArrayList<View> mViewArray = new ArrayList<View>();
    private TextView zc_guanzhu;
    private ViewMiddle viewMiddle;
    private boolean  zc_state=false;
    private String place="";
    private String der="0";
    private String value="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_activity);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        listView = (RefreshListView) findViewById(listview);
        progress=(ProgressBar) findViewById(R.id.progress);
        title = (TextView)findViewById(R.id.titledes);
        bottmon_line = (TextView)findViewById(R.id.bottmon_line);
        bottmon_line.setVisibility(View.GONE);
        title.setText(channelName);
        title.setTextColor(0xfff7b773);
        progress.setVisibility(View.VISIBLE);
        top=(LinearLayout) findViewById(R.id.top);
        top.setVisibility(View.VISIBLE);
        expandTabView = (ExpandTabView) findViewById(R.id.expandtab_view);
        zc_guanzhu=(TextView) findViewById(R.id.zc_guanzhu);
        viewMiddle = new ViewMiddle(this);
        initVaule();
        initListener();
        zc_guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(zc_state==false){
                    zc_guanzhu.setTextColor(getResources().getColor(R.color.lansecolor));
                    progress.setVisibility(View.VISIBLE);
//                    postsListData.clear();
//                    hashMap.clear();
                    if(MyitemAdapter != null){
                       MyitemAdapter.notifyDataSetChanged();
                    }
                    zc_state=true;

                    if(zc_state==false){
                        der="0";
                        sortid="";
                        pubdate="";
                        num=1;
                    }else{
                        der="1";
                        pubdate="";
                        num=1;
                        sortid="";
                    }
                    getjsons(Size, pubdate,num,place,der,sortid,true);
                }else{
                    zc_state=false;
                    zc_guanzhu.setTextColor(getResources().getColor(R.color.title));
                    progress.setVisibility(View.VISIBLE);
//                    postsListData.clear();
//                    hashMap.clear();
                    if(MyitemAdapter != null){
                        MyitemAdapter.notifyDataSetChanged();
                    }
                    if(zc_state==false){
                        der="0";
                        sortid="";
                        pubdate="";
                        num=1;
                    }else{
                        der="1";
                        pubdate="";
                        sortid="";
                        num=1;
                    }
                    listView.setPullUpToRefreshable(true);
                    getjsons(Size, pubdate,num,place,der,sortid,true);
                }
            }
        });
        layout = new FrameLayout(this);
        image = (ImageView)findViewById(R.id.shezhi_backs);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search = (ImageView)findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PolicyActivity.this, NewSearchHistory.class);
                intent.putExtra("typeid", "6");
                startActivity(intent);
            }
        });
       try {
           netWorkType = NetUtils.getNetWorkType(MyApplication
                   .getContext());
           if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
               // TypefaceUtil.replaceFont(getActivity(), "fonts/font.ttf");
               progress.setVisibility(View.GONE);
               updatedata();
           } else {
               lazyLoad();
           }
       }catch (Exception e){}


        listView.setPullDownToRefreshable(true);
        listView.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            /**
             *
             */
            @Override
            public void pullDownToRefresh() {
                listView.setPullUpToRefreshFinish();
                netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());

                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                   updatedata();
                    Toast.makeText(PolicyActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    listView.setPullDownToRefreshFinish();

                } else {
//                    hashMap.clear();
                    num = 1;
//                    postsListData.clear();
                    pubdate = "";
                    sortid="";
                    listView.setPullUpToRefreshable(true);
                    if(MyitemAdapter != null){
                        MyitemAdapter.notifyDataSetChanged();
                    }
                    listView.setPullUpToRefreshable(true);
                    getjsons(Size, pubdate,num,place,der,sortid,true);
                    handler.sendEmptyMessageDelayed(2, 5000);
                }


//                String LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
//                if(LoginState.equals("0")){
//                    listView.showTipTitle("登录后可获取更多精准信息。",new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(PolicyActivity.this, MyloginActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//                }else{
//                    int count = SharedPreferencesUtil.getInt(SharedPreferencesUtil.XINGQUCOUNT, 0);
//                    if( count == 0){
//                        listView.showTipTitle("选择兴趣领域获取精准信息。",new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(PolicyActivity.this, XingquActivity.class);              intent.putExtra("id", "0");
//                                startActivity(intent);
//                            }
//                        });
//                    }
//                }

            }
        });
        listView.setPullUpToRefreshable(true);
        listView.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
                netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    if (channelName == "政策") {
                        postsListData.clear();
                        List<Posts> xmnpost = getInstance(PolicyActivity.this).zhengce_findmore();
                        postsListData.addAll(xmnpost);
                    }

                    if (MyitemAdapter == null) {
                        MyitemAdapter = new RecommendAdapter(PolicyActivity.this, postsListData, channelName, mytitle);
                        listView.setAdapter(MyitemAdapter);
                        listView.setPullUpToRefreshFinish();

                    } else {
                        MyitemAdapter.notifyDataSetChanged();
                        listView.setPullUpToRefreshFinish();

                    }

                } else {

                    if(postsListData.size()==0){

                    }else{
                        if (postsListData.get(postsListData.size() - 1).getResult() != null && postsListData.get(postsListData.size() - 1).getResult().equals("no")) {
                            Toast.makeText(PolicyActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
                            listView.setPullUpToRefreshFinish();
                            listView.setPullUpToRefreshable(false);
                            return;
                        }
                        pubdate = postsListData.get(postsListData.size() - 1).getSortTime();
                        if(zc_state==false){
                            sortid="";
                        }else{
                            sortid=postsListData.get(postsListData.size() - 1).getId();
                        }

                        ++num;
                        getjsons(Size, pubdate,num,place,der,sortid,false);
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
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        ImageLoader.getInstance().pause();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        ImageLoader.getInstance().resume();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        ImageLoader.getInstance().pause();
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(totalItemCount >= postsListData.size() &&  (netWorkType == NetUtils.NETWORKTYPE_INVALID)){
                    listView.setPullUpToRefreshFinish();
                }
            }
        });
    }
    private  void lazyLoad() {
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(PolicyActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            getjsons(Size, pubdate,num,place,der,sortid,false);
        }
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
        MobclickAgent.onPageEnd("政策");
        MobclickAgent.onPause(this);
    }


    /**
     * 跟新数据
     */
    private void updatedata() {
        try {
//            Log.d("lizisong", "updatedata");
            if (channelName == "政策") {
//                adsListData.clear();
//                List<ADS> xmList = getInstance(this).zcfindlunbo();
//                adsListData.addAll(xmList);
//
                postsListData.clear();
                List<Posts> xmnpost = getInstance(this).zhengce_findall();
                postsListData.addAll(xmnpost);
            }


//            if (adsListData.size() == 0) {
//                relayout.setVisibility(View.GONE);
//            } else {
//                relayout.setVisibility(View.VISIBLE);
//            }
//            if (lunboadapter == null) {
////                Log.d("lizisong", "create lunboadapter");
//                lunboadapter = new LunboAdapters(this, handler, adsListData);
//                vp_pager.setAdapter(lunboadapter);
//            } else {
//                lunboadapter.notifyDataSetChanged();
//            }
            if (MyitemAdapter == null) {
                MyitemAdapter = new RecommendAdapter(this, postsListData, channelName, mytitle);
                //vp_pager.setOffscreenPageLimit(1);
                listView.setAdapter(MyitemAdapter);
            } else {
                MyitemAdapter.notifyDataSetChanged();
                listView.setPullDownToRefreshFinish();
            }

        } catch (Exception e) {
        }
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
    public void getjsons(final int Size, final String pubdate,final int num,final String place,final String der,final String sortid,final boolean state) {

        ips = MyApplication.ip;
        try {
            NetworkCom networkCom = NetworkCom.getNetworkCom();
            String url ="http://"+ips+"/api/arc_index.php";
            HashMap<String,String> map = new HashMap<>();

            if (channelName == "政策") {
                map.put("pageSize", Size+"");
                map.put("typeid", "6");
                map.put("rand","1");
                String evalue = getXingQu();
                if(evalue.equals("")){
                    if(sortid.equals("")){
                        if(der.equals("1")){
                            map.put("nativeplace",place);
                            map.put("order",der);
                            map.put("sortid",sortid);
                        }else{
                            map.put("nativeplace",place);
                            map.put("order","0");
                            map.put("sortid",sortid);
                            map.put("LastModifiedTime",pubdate);
                        }

                    }else{
                        map.put("nativeplace",place);
                        map.put("order",der);
                        map.put("sortid",sortid);
                    }
                }else{
                    map.put("evalue",evalue);
                    if(sortid.equals("")){
                        map.put("nativeplace",place);
                        map.put("order",der);
                        map.put("sortid",sortid);
                        map.put("LastModifiedTime",pubdate);
                    }else{
                        map.put("nativeplace",place);
                        map.put("order",der);
                        map.put("sortid",sortid);
                    }
                }

            }
            mHasLoadedOnce = true;
            if(state){
                networkCom.getJson(url,map,handler,3,1);
            }else{
                networkCom.getJson(url,map,handler,3,0);
            }
            Message message = Message.obtain();
            message.what = 0;
            dismissDialog.sendMessageDelayed(message, 500);

        } catch (Exception e) {
            Message msg=Message.obtain();
            msg.what=1;
            dismissDialog.sendMessage(msg);
        }


    }

    /**
     * 停止轮播图刷新
     */
    public void stopLunBo(){
        handler.removeCallbacksAndMessages(null);
    }


    /**
     * handler更新UI
     */
    private MyHandler handler = new MyHandler();
    private Handler dismissDialog= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            UIHelper.hideDialogForLoading();

            if(msg.what==1){
                listView.setPullDownToRefreshFinish();
                listView.setPullUpToRefreshFinish();
            }
            if(msg.what==2){
                listView.setPullDownToRefreshFinish();
                listView.setPullUpToRefreshFinish();
            }
        }
    };

    private class MyHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {
            try {
//                if (msg.what == 0 && adsListData.size() >0) {
//                    //获取当前所在页面索引值
////                        Log.d("lizisong", "msg.what==="+0);
//                    int item = vp_pager.getCurrentItem();
//                    item++;
//                    vp_pager.setCurrentItem(item);
//                    handler.removeCallbacksAndMessages(null);
//                    handler.sendEmptyMessageDelayed(0, 3000);
//                }
                if (msg.what == 3) {
                    listView.setPullDownToRefreshFinish();
                    listView.setPullUpToRefreshFinish();
//                        Log.d("lizisong", "msg.what==="+3);
                    if(msg.arg1 == 1){
                        hashMap.clear();
                        postsListData.clear();
                    }

                    Gson gs = new Gson();
                    jsons = (String) msg.obj;
                    Codes codes = gs.fromJson(jsons, Codes.class);
                    data = codes.getData();
                    adsListData = data.getAds();
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
                                listView.setPullUpToRefreshable(false);
                                Posts item = new Posts();
                                Log.d("lizisong", "add 底线");
                                item.setTypename("底线");
                                postsListData.add(item);
                            }
                        }else{
                            listView.setPullUpToRefreshable(false);
                            Posts item = new Posts();
                            Log.d("lizisong", "add 底线");
                            item.setTypename("底线");
                            postsListData.add(item);
                        }
                    }

                    progress.setVisibility(View.GONE);
                    if (MyitemAdapter == null) {
                        MyitemAdapter = new RecommendAdapter(PolicyActivity.this, postsListData, channelName, mytitle);
                        listView.setAdapter(MyitemAdapter);
                    } else {
                        MyitemAdapter.notifyDataSetChanged();
                    }
                    if (msg.what == 2) {
                        MyitemAdapter.notifyDataSetChanged();
                    }

                    MyitemAdapter.notifyDataSetChanged();
                    listView.setPullDownToRefreshFinish();
                    listView.setPullUpToRefreshFinish();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                PrefUtils.setString(PolicyActivity.this, postsListData.get(position-1).getId(), postsListData.get(position-1).getId());
                                if(postsListData.get(position-1).getTypename().equals("专题")){
                                    Intent intent = new Intent(PolicyActivity.this, SpecialActivity.class);
                                    intent.putExtra("id", postsListData.get(position-1).getId());
                                    startActivity(intent);
                                }else {

                                    Intent intent = new Intent(PolicyActivity.this, NewZhengCeActivity.class);
                                    intent.putExtra("aid", postsListData.get(position - 1).getId());
                                    startActivity(intent);
//                                    Intent intent = new Intent(PolicyActivity.this, DetailsActivity.class);
//                                    intent.putExtra("id", postsListData.get(position - 1).getId());
//                                    intent.putExtra("name", postsListData.get(position - 1).getTypename());
//                                    intent.putExtra("pic", postsListData.get(position - 1).getLitpic());
//                                    startActivity(intent);
                                }
                            }catch (IndexOutOfBoundsException ex){

                            }catch (Exception e){
                            }
                        }
                    });
                }


            } catch (Exception e) {

            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        XGPushManager.onActivityStarted(this);
//        psc.onRefreshComplete();
        if(MyitemAdapter != null){
            MyitemAdapter.notifyDataSetChanged();
        }
        handler.sendEmptyMessageDelayed(0, 5000);
        listView.setPullDownToRefreshFinish();
        XGPushManager.onActivityStarted(this);
        MobclickAgent.onPageStart("政策"); //统计页面，"MainScreen"为页面名称，可自定义
        MobclickAgent.onResume(this);
//        initListener();
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
     * 启动轮播图
     */
    public void startLunBo(){
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    private void initVaule() {

//		mViewArray.add(viewLeft);
        mViewArray.add(viewMiddle);


        ArrayList<String> mTextArray = new ArrayList<String>();
//		mTextArray.add("距离");
        mTextArray.add("区域筛选");

//        mTextArray.add("升级");
//		mTextArray.add("距离");
        expandTabView.setValue(mTextArray, mViewArray);
        expandTabView.text_color(0xff3e3e3e);
//		expandTabView.setTitle(viewLeft.getShowText(), 0);
//        expandTabView.setTitle(viewMiddle.getShowText(), 0);
//		expandTabView.setTitle(viewRight.getShowText(), 2);

    }

    private void initListener() {



        viewMiddle.setOnSelectListener(new ViewMiddle.OnSelectListener() {

            @Override
            public void getValue(String showText, String value) {

                onRefresh(viewMiddle,showText,value);

            }
        });



    }

    private void onRefresh(View view, String showText, String value) {

        expandTabView.onPressBack();
        int position = getPositon(view);

        if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
            expandTabView.setTitle(showText, position);
            expandTabView.setColor(getResources().getColor(R.color.lansecolor),position);
            expandTabView.setDrawableLeft(R.mipmap.arrows_h,position);


        }

        progress.setVisibility(View.VISIBLE);
//        postsListData.clear();
//        hashMap.clear();
        if(MyitemAdapter!=null){
            MyitemAdapter.notifyDataSetChanged();
        }
        place=value;
        sortid="";
        num=1;
        pubdate="";
        listView.setPullUpToRefreshable(true);
        getjsons(Size, pubdate,num,place,der,sortid,true);
//        Toast.makeText(PolicyActivity.this, showText+","+value, Toast.LENGTH_SHORT).show();

    }

    private int getPositon(View tView) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onBackPressed() {

        if (!expandTabView.onPressBack()) {
            finish();
        }

    }


}
