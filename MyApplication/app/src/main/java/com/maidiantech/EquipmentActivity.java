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

public class EquipmentActivity extends AutoLayoutActivity {
    private  int screenWidth;
    private boolean flag = false;
    public  List<ADS> adsListData=new ArrayList<>();
    private RefreshListView listView;
//    private View mHomePageHeaderView;
//    private ViewPager vp_pager;
//    private LinearLayout ll_dots;
    private FrameLayout layout;
//    private RelativeLayout relayout;
    int netWorkType = 0;
    public static boolean is_finish= false;
    private List<Posts> postsListData = new ArrayList<>();
    private String pubdate="";
    private boolean mHasLoadedOnce;
    private String channelName = "设备";
//    private OkHttpUtils Okhttp;
    private int Size = 10;
    private String jsons;
    private Datas data;
    private int num=1;
    private LunboAdapters lunboadapter;
    private ArrayList<ImageView> imageList;
    private RecommendAdapter MyitemAdapter;
    private String mytitle;
    private ProgressBar progress;
    ImageView image,search;
    TextView title;
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
        is_finish = false;
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        listView = (RefreshListView) findViewById(listview);
        progress=(ProgressBar) findViewById(R.id.progress);
        title = (TextView)findViewById(R.id.titledes);
        title.setText(channelName);
        title.setTextColor(0xffffc1a5);
        progress.setVisibility(View.VISIBLE);
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
                Intent intent = new Intent(EquipmentActivity.this, NewSearchHistory.class);
                intent.putExtra("typeid", "7");
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
                    Toast.makeText(EquipmentActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                    listView.setPullDownToRefreshFinish();
                } else {

                    num=1;
                    getjsons(Size, pubdate,num);
                    handler.sendEmptyMessageDelayed(2, 5000);
                }


                String LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                if(LoginState.equals("0")){
                    listView.showTipTitle("登录后可获取更多精准信息。",new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(EquipmentActivity.this, MyloginActivity.class);
                            startActivity(intent);
                        }
                    });
                }else{
                    int count = SharedPreferencesUtil.getInt(SharedPreferencesUtil.XINGQUCOUNT, 0);
                    if( count == 0){
                        listView.showTipTitle("选择兴趣领域获取精准信息。",new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(EquipmentActivity.this, XingquActivity.class);
                                intent.putExtra("id", "0");
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
                    if (channelName == "设备") {
                        postsListData.clear();
                        List<Posts> shebeipost = getInstance(EquipmentActivity.this).shebei_findmore();
                        postsListData.addAll(shebeipost);
                    }


                    if (MyitemAdapter == null) {
                        MyitemAdapter = new RecommendAdapter(EquipmentActivity.this, postsListData, channelName, mytitle);
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

                                Toast.makeText(EquipmentActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
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

    }
    private  void lazyLoad() {
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(EquipmentActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
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
        MobclickAgent.onPageEnd("设备");
        MobclickAgent.onPause(this);
    }


    /**
     * 跟新数据
     */
    private void updatedata() {
        try {
//            Log.d("lizisong", "updatedata");
            if (channelName == "设备") {
                postsListData.clear();
                List<Posts> shebeipost = getInstance(EquipmentActivity.this).shebei_findall();
                postsListData.addAll(shebeipost);
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
        ips = MyApplication.ip;
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        String url ="http://"+ips+"/api/arc_index.php";
        HashMap<String,String> map = new HashMap<>();

        try {
            if (channelName == "设备") {
                String evalue = getXingQu();
                map.put("pageSize", Size+"");
                map.put("LastModifiedTime", pubdate+"");
                map.put("typeid", "7");
                map.put("rand","1");
//                if(!evalue.equals("")){
//                     map.put("evalue",evalue);
//                   }
                 }
                mHasLoadedOnce = true;
                networkCom.getJson(url,map,handler,3,0);
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

                if (msg.what == 3) {
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
                        }
                    }

                    if (MyitemAdapter == null) {
                        MyitemAdapter = new RecommendAdapter(EquipmentActivity.this, postsListData, channelName, mytitle);
                        listView.setAdapter(MyitemAdapter);
                    } else {
                        MyitemAdapter.notifyDataSetChanged();
                    }
                    if (msg.what == 2) {
                        MyitemAdapter.notifyDataSetChanged();
                    }
//                    if (channelName == "设备") {
//
//                        for (int j = 0; j < postsListData.size(); j++) {
//                            Posts p4 = new Posts();
////                                postsListData.get(j).getId(), postsListData.get(j).getLitpic(), postsListData.get(j).getTitle(), postsListData.get(j).getPubdate(),postsListData.get(j).getDescription(),postsListData.get(j).getSource()
//                            p4.setId(postsListData.get(j).getId());
//                            p4.setLitpic(postsListData.get(j).getLitpic());
//                            p4.setTitle(postsListData.get(j).getTitle());
//                            p4.setSortTime(postsListData.get(j).getSortTime());
//                            p4.setTags(postsListData.get(j).getTags());
//                            p4.setZan(postsListData.get(j).getZan());
//                            p4.setClick(postsListData.get(j).getClick());
//                            p4.setImageState(postsListData.get(j).imageState);
//                            p4.setImage(postsListData.get(j).image);
//                            p4.setTypename(postsListData.get(j).getTypename());
//                            p4.setChannel("shebei");
//                            getInstance(EquipmentActivity.this).shebei_adds(p4);
//
//                        }
//                    }
//                    lunboadapter.notifyDataSetChanged();
//                        Log.d("lizisong", "notifyDataSetChanged");
                    MyitemAdapter.notifyDataSetChanged();
                    listView.setPullDownToRefreshFinish();
                    listView.setPullUpToRefreshFinish();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                PrefUtils.setString(EquipmentActivity.this, postsListData.get(position-1).getId(), postsListData.get(position-1).getId());
                                if(postsListData.get(position-1).getTypename().equals("专题")){
                                    Intent intent = new Intent(EquipmentActivity.this, SpecialActivity.class);
                                    intent.putExtra("id", postsListData.get(position-1).getId());

                                    startActivity(intent);
                                }else {
                                    String name = postsListData.get(position - 1).getTypename();
                                    if(name != null && (name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                        Intent intent = new Intent(EquipmentActivity.this, ZixunDetailsActivity.class);
                                        intent.putExtra("id", postsListData.get(position - 1).getId());
                                        intent.putExtra("name", postsListData.get(position - 1).getTypename());
                                        intent.putExtra("pic", postsListData.get(position - 1).getLitpic());
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(EquipmentActivity.this, DetailsActivity.class);
                                        intent.putExtra("id", postsListData.get(position - 1).getId());
                                        intent.putExtra("name", postsListData.get(position - 1).getTypename());
                                        intent.putExtra("pic", postsListData.get(position - 1).getLitpic());
                                        startActivity(intent);
                                    }
//                        Log.d("lizisong", "position:"+position);
//                        Log.i("possition", postsListData.get(position-2).getId() + "................");
//                                    Intent intent = new Intent(EquipmentActivity.this, DetailsActivity.class);
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
        if(is_finish){
            is_finish = false;
            finish();
            return;
        }
//        psc.onRefreshComplete();
        if(MyitemAdapter != null){
            MyitemAdapter.notifyDataSetChanged();
        }
        handler.sendEmptyMessageDelayed(0, 5000);
        listView.setPullDownToRefreshFinish();
        XGPushManager.onActivityStarted(this);
        MobclickAgent.onPageStart("设备"); //统计页面，"MainScreen"为页面名称，可自定义
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
//            ll_dots.addView(imageView, params);
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
