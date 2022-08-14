package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
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
import com.chenantao.autolayout.AutoLinearLayout;
import com.google.gson.Gson;
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

import static com.maidiantech.R.id.listview;
import static dao.Sqlitetions.getInstance;


/**
 * Created by lizisong on 2017/3/21.
 */

public class LaboratoryActivity extends AutoLayoutActivity {
    private  int screenWidth;
    private boolean flag = false;
    public  List<ADS> adsListData=new ArrayList<>();
    private RefreshListView listView;
    private View mHomePageHeaderView;
    private ViewPager vp_pager;
    private LinearLayout ll_dots;
    private FrameLayout layout;
    private RelativeLayout relayout;
    int netWorkType = 0;
    private List<Posts> postsListData = new ArrayList<>();
    private String pubdate="";
    private boolean mHasLoadedOnce;
    private String channelName = "研究所";
//    private OkHttpUtils Okhttp;
    private int Size = 15;
    private String jsons;
    private Datas data;
    private LunboAdapters lunboadapter;
    private ArrayList<ImageView> imageList;
    private RecommendAdapter MyitemAdapter;
    private String mytitle;
    private ProgressBar progress;
    ImageView image,search;
    TextView title;
    private int num=1;
    HashMap<String, String > hashMap = new HashMap<>();
    private  String   ips;
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
        title.setText(channelName);
        progress.setVisibility(View.VISIBLE);
        /**
         * 轮播图布局
         */
        mHomePageHeaderView = getLayoutInflater().inflate(R.layout.my_lunbo, null);
        vp_pager =(ViewPager) mHomePageHeaderView.findViewById(R.id.pager);
        ll_dots = (LinearLayout) mHomePageHeaderView.findViewById(R.id.layout);
        layout = new FrameLayout(this);
        relayout = (RelativeLayout) mHomePageHeaderView.findViewById(R.id.relayout);
        listView.addHeaderView(mHomePageHeaderView, null, false);

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
                Intent intent = new Intent(LaboratoryActivity.this, SearchCommentPage.class);
                intent.putExtra("type", "shiyanshi");
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

//        listView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//
//                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//            }
//
//
//        });
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
//                    if(postsListData.size() > 0 ){
//                        listView.setPullDownToRefreshFinish();
//                        return;
//                    }
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
//                        Log.d("lizisong", "setOnRefreshListener netWorkType");
                    updatedata();
                    Toast.makeText(LaboratoryActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    listView.setPullDownToRefreshFinish();
//                        psc.onRefreshComplete();
                } else {
//                        Log.d("lizisong", "setOnRefreshListener else");
//                    hashMap.clear();
//                    postsListData.clear();
//                    pubdate = "";
                    num=1;
                    getjsons(Size, pubdate,num);
//                        psc.onRefreshComplete();
                    handler.sendEmptyMessageDelayed(2, 5000);
                }


                String LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                if(LoginState.equals("0")){
                    listView.showTipTitle("登录后可获取更多精准信息。",new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(LaboratoryActivity.this, MyloginActivity.class);
                            startActivity(intent);
                        }
                    });
                }else{
                    int count = SharedPreferencesUtil.getInt(SharedPreferencesUtil.XINGQUCOUNT, 0);
                    if( count == 0){
                        listView.showTipTitle("选择兴趣领域获取精准信息。",new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(LaboratoryActivity.this, XingquActivity.class);                 intent.putExtra("id", "0");
                                startActivity(intent);
                            }
                        });
                    }
                }

            }
        });
        listView.setPullUpToRefreshable(true);
        listView.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
//                    Log.d("lizisong","pullUpToRefresh");
//                    Log.d("lizisong", "onPullUpToRefresh");
                netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {

//                        Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                    if (channelName == "研究所") {
                        postsListData.clear();
                        List<Posts> syspost = getInstance(LaboratoryActivity.this).sys_findmore();
                        postsListData.addAll(syspost);
                    }


                    if (MyitemAdapter == null) {
                        MyitemAdapter = new RecommendAdapter(LaboratoryActivity.this, postsListData, channelName, mytitle);
                        listView.setAdapter(MyitemAdapter);
                        listView.setPullUpToRefreshFinish();
//                            psc.onRefreshComplete();
                    } else {
                        MyitemAdapter.notifyDataSetChanged();
                        listView.setPullUpToRefreshFinish();
//                            psc.onRefreshComplete();
                    }

//                        listView.setPullUpToRefreshable(false);

                } else {
                    // TimeMillis = System.currentTimeMillis();
                    if(postsListData.size()==0){
//                            psc.onRefreshComplete();
                    }else{

                        if (postsListData.get(postsListData.size() - 1).getResult() != null && postsListData.get(postsListData.size() - 1).getResult().equals("no")) {

                                Toast.makeText(LaboratoryActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
//                                listView.setPullUpToRefreshable(false);
                            listView.setPullUpToRefreshFinish();
//                                psc.onRefreshComplete();
                        }
                        pubdate = postsListData.get(postsListData.size() - 1).getSortTime();
                       ++num;
                        getjsons(Size, pubdate,num);
                        Message msgs=Message.obtain();
                        msgs.what=2;
                        dismissDialog.sendMessageDelayed(msgs,5000);
//                            psc.onRefreshComplete();
                    }
                }
            }
        });
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    try {
////                        Log.d("lizisong", "position:"+position);
////                        Log.i("possition", postsListData.get(position-2).getId() + "................");
//                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                        intent.putExtra("id", postsListData.get(position-2).getId());
//                        intent.putExtra("name", postsListData.get(position-2).getTypename());
//                        intent.putExtra("pic", postsListData.get(position-2).getLitpic());
//                        startActivity(intent);
//                    }catch (IndexOutOfBoundsException ex){
//
//                    }catch (Exception e){
//                    }
//                }
//            });
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

        /**
         * 页面滑动设置相对应的小圆点的改变
         *
         */
        vp_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                try {
                    if (lunboadapter.getList().size() != 1) {
                        for (int i = 0; i < lunboadapter.getList().size(); i++) {
                            if (i == arg0 % lunboadapter.getList().size()) {
                                imageList.get(i).setImageResource(R.drawable.dot_focused);
                            } else {
                                imageList.get(i).setImageResource(R.drawable.dot_normal);
                            }
                        }
                    }
                } catch (Exception e) {

                }
                  /*  //判断是否滑动到了第二个界面，假如是那么加载4个界面，前后各两个，随着postion递增
                     if(arg0>=2){
                         vp_pager.setOffscreenPageLimit(arg0);
                            }*/
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {//在 拖拽 的时候移除消息回调
                    handler.removeCallbacksAndMessages(null);
                } else if (state == ViewPager.SCROLL_STATE_SETTLING) {//在 松手复位 的时候重新发送消息
                    handler.sendEmptyMessageDelayed(0, 3000);
                } else if (state == ViewPager.SCROLL_STATE_IDLE) {//在 空闲 时候发送消息
                    handler.sendEmptyMessageDelayed(0, 3000);
                }
            }
        });
        //自动轮播
        //handler.sendEmptyMessageDelayed(0, 3000);
        vp_pager.setFocusable(true);
        vp_pager.setFocusableInTouchMode(true);
        vp_pager.requestFocus();
    }
    private  void lazyLoad() {
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(LaboratoryActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            getjsons(Size, pubdate,num);
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
        MobclickAgent.onPageEnd("研究所");
        MobclickAgent.onPause(this);
    }


    /**
     * 跟新数据
     */
    private void updatedata() {
        try {
//            Log.d("lizisong", "updatedata");
            if (channelName == "研究所") {
                postsListData.clear();
                List<Posts> syspost = getInstance(LaboratoryActivity.this).sys_findall();
                postsListData.addAll(syspost);
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
    public void getjsons(final int Size, final String pubdate,final int num) {
//        Log.d("lizisong", "getjsons");
        ips = MyApplication.ip;
//        Okhttp = OkHttpUtils.getInstancesOkHttp();
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
                MyApplication.setAccessid();
                try {
                    if (channelName == "研究所") {
                        String timestamp = System.currentTimeMillis()+"";
                        String sign="";
                        ArrayList<String> sort = new ArrayList<String>();

                        String evalue = getXingQu();
                        if(evalue.equals("")){
                            sort.add("pageSize"+Size);
                            sort.add("LastModifiedTime"+pubdate);
                            sort.add("typeid"+"8");
                            sort.add("version"+"1.0");
                            sort.add("rand"+"1");
//                            sort.add("page"+num);
                            sort.add("timestamp"+timestamp);
                            sort.add("version"+MyApplication.version);
                            String accessid="";
                            String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                            if(loginState.equals("1")){
                                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                                accessid = mid;
                            }else{
                                accessid = MyApplication.deviceid;
                            }
                            sort.add("accessid"+accessid);
                            sign = KeySort.keyScort(sort);
                            jsons = OkHttpUtils.loaudstringfromurl("http://"+ips+"/api/arc_index.php?pageSize="+ Size+"&LastModifiedTime="+pubdate+"&typeid=8&rand=1"+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                        }else{
                            sort.add("pageSize"+Size);
                            sort.add("LastModifiedTime"+pubdate);
                            sort.add("typeid"+"8");
                            sort.add("version"+"1.0");
                            sort.add("rand"+"1");
//                            sort.add("page"+num);
                            sort.add("evalue"+evalue);
                            sort.add("timestamp"+timestamp);
                            sort.add("version"+MyApplication.version);
                            String accessid="";
                            String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                            if(loginState.equals("1")){
                                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                                accessid = mid;
                            }else{
                                accessid = MyApplication.deviceid;
                            }
                            sort.add("accessid"+accessid);
                            sign = KeySort.keyScort(sort);
                            jsons = OkHttpUtils.loaudstringfromurl("http://"+ips+"/api/arc_index.php?pageSize="+ Size+ "&LastModifiedTime="+pubdate+"&typeid=8&rand=1"+"&evalue="+evalue+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid);
                        }

                    }
                    mHasLoadedOnce = true;
                    Message msg = new Message();
                    msg.obj = jsons;
                    msg.what = 3;
                    handler.sendMessage(msg);
                    Message message = Message.obtain();
                    message.what = 0;
                    dismissDialog.sendMessageDelayed(message, 500);

                } catch (Exception e) {
                    Message msg=Message.obtain();
                    msg.what=1;
                    dismissDialog.sendMessage(msg);
                }
            }
        }.start();
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
            progress.setVisibility(View.GONE);
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
                if (msg.what == 0 && adsListData.size() >0) {
                    //获取当前所在页面索引值
//                        Log.d("lizisong", "msg.what==="+0);
                    int item = vp_pager.getCurrentItem();
                    item++;
                    vp_pager.setCurrentItem(item);
                    handler.removeCallbacksAndMessages(null);
                    handler.sendEmptyMessageDelayed(0, 3000);
                }
                if (msg.what == 3) {
//                        Log.d("lizisong", "msg.what==="+3);

                    Gson gs = new Gson();
                    jsons = (String) msg.obj;
                    Codes codes = gs.fromJson(jsons, Codes.class);
                    data = codes.getData();
                    adsListData = data.getAds();



//                    if (lunboadapter == null) {
////                            Log.d("lizisong", "create 2 lunboadapter");
//
//                        lunboadapter = new LunboAdapters(LaboratoryActivity.this, handler, adsListData);
//                        vp_pager.setAdapter(lunboadapter);
//                        if (adsListData.size() != 1) {
//                            initPaint();
//                            vp_pager.setCurrentItem(adsListData.size() * 5000);
//                        }
//                    } else {
//                        lunboadapter.notifyDataSetChanged();
//                    }
//                        postsListData.addAll(data.getPosts());
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
                    if (adsListData.size() == 0) {
                        relayout.setVisibility(View.GONE);
                    } else {
                        relayout.setVisibility(View.VISIBLE);
                    }
                    if (MyitemAdapter == null) {
                        MyitemAdapter = new RecommendAdapter(LaboratoryActivity.this, postsListData, channelName, mytitle);
                        listView.setAdapter(MyitemAdapter);
                    } else {
                        MyitemAdapter.notifyDataSetChanged();
                    }
                    if (msg.what == 2) {
                        MyitemAdapter.notifyDataSetChanged();
                    }
                    if (channelName == "研究所") {

                        for (int j = 0; j < postsListData.size(); j++) {
                            Posts p4 = new Posts();
//                                postsListData.get(j).getId(), postsListData.get(j).getLitpic(), postsListData.get(j).getTitle(), postsListData.get(j).getPubdate(),postsListData.get(j).getDescription(),postsListData.get(j).getSource()
                            p4.setId(postsListData.get(j).getId());
                            p4.setLitpic(postsListData.get(j).getLitpic());
                            p4.setTitle(postsListData.get(j).getTitle());
                            p4.setSortTime(postsListData.get(j).getSortTime());
                            p4.setTags(postsListData.get(j).getTags());
                            p4.setZan(postsListData.get(j).getZan());
                            p4.setClick(postsListData.get(j).getClick());
                            p4.setImageState(postsListData.get(j).imageState);
                            p4.setImage(postsListData.get(j).image);
                            p4.setTypename(postsListData.get(j).getTypename());
                            p4.setChannel("shiyanshi");
                            getInstance(LaboratoryActivity.this).sys_add(p4);

                        }
                    }
//                    lunboadapter.notifyDataSetChanged();
//                        Log.d("lizisong", "notifyDataSetChanged");
                    MyitemAdapter.notifyDataSetChanged();
                    listView.setPullDownToRefreshFinish();
                    listView.setPullUpToRefreshFinish();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                PrefUtils.setString(LaboratoryActivity.this,postsListData.get(position - 2).getId(),postsListData.get(position - 2).getId());
                                if(postsListData.get(position-2).getTypename().equals("专题")){
                                    Intent intent = new Intent(LaboratoryActivity.this, SpecialActivity.class);
                                    intent.putExtra("id", postsListData.get(position-2).getId());

                                    startActivity(intent);
                                }else {
                                    String name = postsListData.get(position - 2).getTypename();
                                    if(name != null && (name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                        Intent intent = new Intent(LaboratoryActivity.this, ZixunDetailsActivity.class);
                                        intent.putExtra("id", postsListData.get(position - 2).getId());
                                        intent.putExtra("name", postsListData.get(position - 2).getTypename());
                                        intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(LaboratoryActivity.this, DetailsActivity.class);
                                        intent.putExtra("id", postsListData.get(position - 2).getId());
                                        intent.putExtra("name", postsListData.get(position - 2).getTypename());
                                        intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
                                        startActivity(intent);
                                    }
//                        Log.d("lizisong", "position:"+position);
//                        Log.i("possition", postsListData.get(position-2).getId() + "................");
//                                    Intent intent = new Intent(LaboratoryActivity.this, DetailsActivity.class);
//                                    intent.putExtra("id", postsListData.get(position - 2).getId());
//                                    intent.putExtra("name", postsListData.get(position - 2).getTypename());
//                                    intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
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
        MobclickAgent.onPageStart("实验室"); //统计页面，"MainScreen"为页面名称，可自定义
        MobclickAgent.onResume(this);
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
     * 初始化小圆点
     */
    public void initPaint() {
        imageList = new ArrayList<ImageView>();
        // Log.i("adsListData",+"...................");
        for (int i = 0; i < lunboadapter.getList().size(); i++) {
            //画圆点
            ImageView imageView = new ImageView(this);
            if (i == 0) {
                imageView.setImageResource(R.drawable.dot_focused);
            } else {
                imageView.setImageResource(R.drawable.dot_normal);
            }
            imageList.add(imageView);
            AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(15, 15);
            params.setMargins(5, 0, 5, 0);
            ll_dots.addView(imageView, params);
        }
        if(lunboadapter != null){
            int with= lunboadapter.getList().size()*15+(lunboadapter.getList().size()-1)*5+100;
            lunboadapter.setWidth(screenWidth-with);
        }
    }
    /**
     * 启动轮播图
     */
    public void startLunBo(){
        handler.sendEmptyMessageDelayed(0, 3000);
    }

}
