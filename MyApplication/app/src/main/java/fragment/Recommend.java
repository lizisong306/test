package fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLinearLayout;
import com.google.gson.Gson;
import com.maidiantech.DetailsActivity;
import com.maidiantech.EarlyRef;
import com.maidiantech.MainActivity;
import com.maidiantech.My_shezhi;
import com.maidiantech.MyloginActivity;
import com.maidiantech.QingBaoDeilActivity;
import com.maidiantech.R;
import com.maidiantech.ShowFristGuideActivity;
import com.maidiantech.SpecialActivity;
import com.maidiantech.WebViewActivity;
import com.maidiantech.XingquActivity;
import com.maidiantech.ZixunDetailsActivity;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Port.SollectInterface;
import Util.KeySort;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.PrefUtils;
import Util.SharedPreferencesUtil;
import adapter.LunboAdapters;
import adapter.RecommendAdapter;
import adapter.TouTiaoAdapter;
import application.MyApplication;
import dao.Sqlitetions;
import entity.ADS;
import entity.Codes;
import entity.Datas;
import entity.PlaceChannel;
import entity.Posts;
import view.NoDobleClickListener;
import view.RefreshListView;
import view.SystemBarTintManager;
import view.T;

import static com.maidiantech.R.id.listview;
import static com.maidiantech.R.id.tip2;
import static dao.Sqlitetions.getInstance;



/**
 * Created by 13520 on 2017/2/6.
 */

public class Recommend extends BaseFragment implements SollectInterface{
    private final int TAB_SHOW = 0; //显示
    private final int TAB_HIDE = 1; //隐藏
    private ObjectAnimator mAnimator;
    private View view;
    private int screenWidth;
    private boolean flag = false;
    private RefreshListView listView;

    TextView kejikutitile;
    private View mHomePageHeaderView;
//    private ViewPager vp_pager;
//    private LinearLayout ll_dots;
    private FrameLayout layout;
    private RelativeLayout la;
    public  List<ADS> adsListData=new ArrayList<>();
    int netWorkType = 0;
    private List<Posts> postsListData = new ArrayList<>();
    private String pubdate;
    private boolean mHasLoadedOnce;
    private String channelName = "推荐";
    private OkHttpUtils Okhttp;
    private int Size = 20;
    TouTiaoAdapter recomment;
    private String jsons;
    private Datas data;
    public static String aids="";
    private LunboAdapters lunboadapter;
    private ArrayList<ImageView> imageList;
    private Receiver receiver = new Receiver();
    private String mytitle;
    private ProgressBar progress;
    Posts topItem,zhiding1,zhiding2,shuaxin;
    private boolean ispushdownstate = false;
    private List<Posts> zhiding = new ArrayList<>();
    HashMap<String, String > hashMap = new HashMap<>();
    HashMap<String ,Posts> hashData = new HashMap<>();
    int widthPixels;
    int heightPixels;
    String sortid = "";
    SystemBarTintManager tintManager ;
    private  String   ips;
    RelativeLayout pop;
    LinearLayout dingwei_lay,tiplay,id;
    ImageView dingwei_id,dingwei_id1,yingyin;
    TextView tip1,tip2,tip3;
    private GestureDetector gesture;
    boolean  isanimone = false;
    boolean istopanimone = false;
//    OnScrollABCListener listenerabc;
    private float mFirstY;
    private float mCurrentY;
    private int lastindex=-1;
    private int old;
    boolean isherat = true;
    private  boolean isEnd = false;
    private int state = 0;
    int count;
    boolean isAnuationStaring = false;
    public static  String channelid = "";
    ImageView nodata;
    Posts zixun1,zixun2;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.home_fragment_new, null);
            DisplayMetrics metrics=new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            widthPixels=metrics.widthPixels;
            heightPixels=metrics.heightPixels;
            //TypefaceUtil.replaceFont(getActivity(), "fonts/font.ttf");
        }

        hashMap.clear();
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        if (!flag) {
            flag = true;
            listView = (RefreshListView) view.findViewById(R.id.listview);
            pop = (RelativeLayout)view.findViewById(R.id.pop);
            pop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pop.setVisibility(View.GONE);
                }
            });
//            if(MainActivity.hasSoftKeys(getActivity().getWindowManager())){
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listView.getLayoutParams();
//                layoutParams.bottomMargin=MyApplication.navigationbar+150;
//                listView.setLayoutParams(layoutParams);
//            }
            progress = (ProgressBar) view.findViewById(R.id.progress);
            yingyin = (ImageView)view.findViewById(R.id.yingyin);
            nodata = (ImageView)view.findViewById(R.id.nodata);
            nodata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pubdate = "";
                    sortid = "";
                    aids="";
                    ispushdownstate = true;
                    listView.setPullUpToRefreshable(true);
                    SharedPreferencesUtil.putLong(SharedPreferencesUtil.SHUAXIN_SHIJIAN,System.currentTimeMillis());
                    getjsons(Size, pubdate,false);
                    handler.sendEmptyMessageDelayed(2, 5000);
                }
            });
            progress.setVisibility(View.VISIBLE);
            /**
             * 轮播图布局
             */
            mHomePageHeaderView = getLayoutInflater(savedInstanceState).inflate(R.layout.my_lunbo, null);
            dingwei_id = (ImageView)mHomePageHeaderView.findViewById(R.id.dingwei_id);
            dingwei_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    toolbarAnimal(TAB_SHOW);
                }
            });
            dingwei_lay = (LinearLayout)mHomePageHeaderView.findViewById(R.id.dingwei_lay);
            dingwei_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    toolbarAnimal(TAB_HIDE);
                }
            });

            tip1 = (TextView)mHomePageHeaderView.findViewById(R.id.tip1);
            tip2 = (TextView)mHomePageHeaderView.findViewById(R.id.tip2);
            tip3 = (TextView)mHomePageHeaderView.findViewById(R.id.tip3);
            tiplay = (LinearLayout)mHomePageHeaderView.findViewById(R.id.tiplay);


            layout = new FrameLayout(getActivity());
            la = (RelativeLayout)view.findViewById(R.id.la);

            netWorkType = NetUtils.getNetWorkType(MyApplication
                    .getContext());
            if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                progress.setVisibility(View.GONE);
            }

            listView.setPullDownToRefreshable(true);
            listView.setInterface(this);
            listView.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
                /**
                 *
                 */
                @Override
                public void pullDownToRefresh() {
                    netWorkType = NetUtils.getNetWorkType(MyApplication
                            .getContext());

                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                        listView.setPullDownToRefreshFinish();
                    } else {
                        pubdate = "";
                        sortid = "";
                        aids="";
                        ispushdownstate = true;
                        listView.setPullUpToRefreshable(true);
                        SharedPreferencesUtil.putLong(SharedPreferencesUtil.SHUAXIN_SHIJIAN,System.currentTimeMillis());
                        getjsons(Size, pubdate,false);
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
                        if (channelName == "推荐") {
                            postsListData.clear();
                            List<Posts> tjanpost = getInstance(getContext()).tj_findmore();
                            postsListData.addAll(tjanpost);
                        }

                        if (channelName == "推荐") {
                            if (recomment == null) {
                                recomment = new TouTiaoAdapter(getActivity(), postsListData, channelName, mytitle,pop,listView,la);
                                listView.setAdapter(recomment);
                            } else {

                                recomment.notifyDataSetChanged();
                            }

                        }
                    } else {

                        if (postsListData.size() == 0) {

                        } else {
                            pubdate = postsListData.get(postsListData.size() - 1).getSortTime();
                            sortid = postsListData.get(postsListData.size() - 1).getId();
                            getjsons(Size, pubdate,true);
                            Message msgs = Message.obtain();
                            msgs.what = 2;
                            dismissDialog.sendMessageDelayed(msgs, 5000);
                        }
                    }
                }
            });

            /**
             * 根据滑动状态，改变轮播的状态
             */
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                private SparseArray recordSp = new SparseArray(0);
                private int mCurrentfirstVisibleItem = 0;
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                            if(lastindex ==0){
//                                showAnimatorfor();
//                            }

                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (totalItemCount >= postsListData.size() && (netWorkType == NetUtils.NETWORKTYPE_INVALID)) {
                        listView.setPullUpToRefreshFinish();
                    }
                    mCurrentfirstVisibleItem = firstVisibleItem;
                    View firstView = view.getChildAt(0);
                    if (firstVisibleItem == 0) {
                        View firstVisibleItemView = listView.getChildAt(0);
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
                        int h = getScrollY();//滚动距离
                        Log.d("lizisong", "h:"+h);
                        if(h >120){
                            int cha = h-120;
                            float alpha = cha/100f;
                            if(alpha < 1.0f){
                                la.setAlpha(alpha);
                            }else{
                                la.setAlpha(1.0f);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                                if(tintManager != null){
                                    tintManager.setStatusBarTintEnabled(true);
                                    tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                    tintManager.setStatusBarAlpha(1);
                                }
                            }
                            yingyin.setVisibility(View.VISIBLE);
                            la.setVisibility(View.VISIBLE);


                        }else {
//                            title.getBackground().setAlpha(290-25-h);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                                if(tintManager != null){
                                    tintManager.setStatusBarTintEnabled(true);
                                    tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                    tintManager.setStatusBarAlpha(0);
                                }
                            }
                            la.setVisibility(View.GONE);
                            yingyin.setVisibility(View.GONE);
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

//                    return 0;
                }
                class ItemRecod {
                    int height = 0;
                    int top = 0;
                }
            });


        }
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
//            updatedata();
            Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            getjsons(Size, pubdate, false);
        }
//        showAnimator();
//        showAlpha();
        return view;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tintManager = new SystemBarTintManager(getActivity());
        IntentFilter oninfilter = new IntentFilter();
        oninfilter.addAction("action_toxuqiu");
        oninfilter.addAction("action_gogogo");
        getActivity().registerReceiver(receiver, oninfilter);
    }

    @Override
    protected void lazyLoad() {
        if (!isVisible || mHasLoadedOnce) {
            return;
        }
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            getjsons(Size, pubdate, false);
        }
    }

    public void getjsons(final int Size, final String pubdate,final boolean state) {

        try {
            String url ="http://"+MyApplication.ip+"/api/arc_index.php";
            NetworkCom networkCom = NetworkCom.getNetworkCom();
            HashMap<String,String> map = new HashMap<>();
            map.put("pageSize",Size+"");
            map.put("typeid","9");
            map.put("ids",aids);
            map.put("sortid",sortid);
            map.put("channelid",channelid);
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
            Message msg = Message.obtain();
            msg.what = 1;
            dismissDialog.sendMessage(msg);
        }
    }

    /**
     * 停止轮播图刷新
     */
    public void stopLunBo() {
        handler.removeCallbacksAndMessages(null);
    }


    /**
     * handler更新UI
     */
    private Recommend.MyHandler handler = new Recommend.MyHandler(this);
    private Recommend.DismissDialog dismissDialog = new Recommend.DismissDialog(this);

    @Override
    public void onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //点下去的起始y坐标
                mFirstY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                state = 0;
                break;
        }

    }

    private class DismissDialog extends Handler{
        WeakReference<Recommend> weakReference;
        public DismissDialog (Recommend fragment){
            weakReference = new WeakReference<Recommend>(fragment);
        }
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

    }

    private class MyHandler extends Handler {
        WeakReference<Recommend> weakReference;

        public MyHandler(Recommend fragment) {
            weakReference = new WeakReference<Recommend>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                if (weakReference.get() != null) {
                    if (msg.what == 0 ) {
                        handler.removeCallbacksAndMessages(null);
                    }
                    if (msg.what == 3) {
                        listView.setPullDownToRefreshFinish();
                        listView.setPullUpToRefreshFinish();

                        Gson gs = new Gson();
                        jsons = (String) msg.obj;
                        Codes codes = gs.fromJson(jsons, Codes.class);
                        data = codes.getData();
                        if(data.getAds() != null){
                            adsListData = data.getAds();
                        }

                        if( codes.code == 1 &&data.getPosts().size() == 0){
                            listView.setPullUpToRefreshFinish();
                            listView.setPullUpToRefreshable(false);
                            Toast.makeText(getActivity(),"没有数据了",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(postsListData != null){
                            List<Posts> post =data.getPosts();
                            if(msg.arg1 == 0){
                                if(post != null){
                                    if(shuaxin != null){
                                        hashMap.remove(shuaxin.getId());
                                        postsListData.remove(shuaxin);
                                    }
                                    try {
                                        if(aids != null && !aids.equals("")){
                                            String []id =aids.split(",");
                                            hashMap.remove(id[0]);
                                            hashMap.remove(id[1]);
                                        }
                                    }catch (Exception e){

                                    }
                                    Posts item1 = new Posts();
                                    item1.setTypename("刷新");
                                    item1.setId("aaa");
                                    post.add(item1);
                                    shuaxin = item1;
                                    for(int i=post.size()-1; i>=0; i--){
                                        Posts item = post.get(i);
                                        String title =  hashMap.get(item.getId());
                                        if(item.getTags() != null && item.getTags().equals("置顶")){
                                            topItem = item;
                                            postsListData.remove(item);
                                        }
                                        if(aids != null && !aids.equals("")
                                                ){
                                            if(aids.contains(item.getId())){
                                                if(zhiding1 == null){
                                                    zhiding1 = item;
                                                }else{
                                                    zhiding2 = item;
                                                }
                                                postsListData.remove(item);
                                                continue;
                                            }
                                        }
                                        if(title == null){
                                          postsListData.add(0,item);
                                          hashMap.put(item.getId(),item.getId());
                                          hashData.put(item.getId(),item);
                                        }else{
                                            postsListData.remove(hashData.get(item.getId()));
                                            postsListData.add(0,item);
                                        }

                                    }
                                        for(int i=0; i<postsListData.size();i++){
                                            Posts item = postsListData.get(i);
                                            if(topItem != null){
                                                if(item.getId().equals(topItem.getId())){
                                                    postsListData.remove(i);
                                                }
                                            }

                                            if(zhiding1 != null){
                                                if(item.getId().equals(zhiding1.getId())){
                                                    postsListData.remove(i);
                                                }
                                            }

                                            if(zhiding2 != null){
                                                if(item.getId().equals(zhiding2.getId())){
                                                    postsListData.remove(i);
                                                }
                                            }
                                        }
                                        if(topItem != null){
                                            postsListData.add(0,topItem);
                                        }
                                        if(zhiding2 != null){
                                            postsListData.add(0,zhiding2);
                                        }
                                        if(zhiding1 != null){
                                            postsListData.add(0,zhiding1);
                                        }
                                        topItem = null;
                                        zhiding1=null;
                                        zhiding2=null;
                                    }
                                    isanimone=true;
                                    tip2.setText(" "+(post.size()-1)+" ");
                            }else if(msg.arg1 == 1){
                                if(post != null){
                                    for(int i=0;i<post.size();i++){
                                        Posts item = post.get(i);
                                        String title =  hashMap.get(item.getId());
                                        if(title == null){
                                            postsListData.add(item);
                                        }else{

                                        }
                                    }
                                    tip2.setText(" "+(post.size()-1)+" ");
                                }
                            }

                        }

                        nodata.setVisibility(View.GONE);
                        if (channelName == "推荐") {
                            if (recomment == null) {
                                listView.addHeaderView(mHomePageHeaderView);
                                recomment = new TouTiaoAdapter(getActivity(), postsListData, channelName, mytitle,pop,listView,la);
                                listView.setAdapter(recomment);
                            } else {
                                recomment.notifyDataSetChanged();
                                listView.setPullDownToRefreshFinish();
                                listView.setPullUpToRefreshFinish();
                            }
                        }
                    }

                    if(msg.what == 100){
                        isAnuationStaring = false;
                        if(recomment != null){
                            recomment.setIsAnuation(isAnuationStaring);
                        }
                    }
                }

            } catch (Exception e) {
                nodata.setVisibility(View.VISIBLE);
            }
        }
    }



    /**
     * 启动轮播图
     */
    public void startLunBo() {
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        XGPushManager.onActivityStarted(getActivity());
//        psc.onRefreshComplete();
        handler.sendEmptyMessageDelayed(0, 5000);
        listView.setPullDownToRefreshFinish();
        if(recomment!=null){
            recomment.notifyDataSetChanged();
        }
        XGPushManager.onActivityStarted(getActivity());
        MobclickAgent.onPageStart("推荐"); //统计页面，"MainScreen"为页面名称，可自定义
        String lognin = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "");

        if(lognin.equals("1")){
            tip1.setVisibility(View.VISIBLE);
            tip2.setVisibility(View.VISIBLE);
            tip3.setVisibility(View.VISIBLE);
            tip1.setText("尊敬的"+SharedPreferencesUtil.getString(SharedPreferencesUtil.NICK_NAME, "0")+"，钛领云服务为您推荐");
            tip3.setText("条资讯");
            tip1.setOnClickListener(null);
            tip2.setOnClickListener(null);
            tip3.setOnClickListener(null);
        }else{
            tip2.setVisibility(View.VISIBLE);
            tip3.setVisibility(View.VISIBLE);
            tip3.setText("条资讯");
            tip1.setText("尊敬的钛领用户，钛领云服务为您推荐");
        }
        tip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lognin = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "");
                if(!lognin.equals("1")){
                    Intent intent = new Intent(getActivity(), MyloginActivity.class);
                    startActivity(intent);
                }
            }
        });
        tip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lognin = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "");
                if(!lognin.equals("1")){
                    Intent intent = new Intent(getActivity(), MyloginActivity.class);
                    startActivity(intent);
                }
            }
        });
        tip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lognin = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "");
                if(!lognin.equals("1")){
                    Intent intent = new Intent(getActivity(), MyloginActivity.class);
                    startActivity(intent);
                }
            }
        });

//        if(SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.TIPS_ZHEZHAO, false) == false){
//            SharedPreferencesUtil.putBoolean(SharedPreferencesUtil.TIPS_ZHEZHAO,true);
//            Intent intent = new Intent(getActivity(), ShowFristGuideActivity.class);
//            startActivity(intent);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getInstance(getContext()).guanrsd();
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        handler.removeCallbacksAndMessages(null);
        getActivity().unregisterReceiver(receiver);
    }


    /**
     * 消除有可能出现下拉刷新的界面
     */
    public void canelDownRefrsh() {
        listView.setPullDownToRefreshFinish();
    }

    @Override
    public void setArguments(Bundle bundle) {
        // TODO Auto-generated method stub
        // weburl = bundle.getString("weburl");
        // count=bundle.getInt("id");
        channelName = bundle.getString("name");
    }


    @Override
    public void onDestroyView() {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {//如果View已经添加到容器中，要进行删除，负责会报错n
            parent.removeView(view);
        }

        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        // handler.sendEmptyMessageDelayed(0, 3000);
        if (channelName == "推荐") {
            handler.sendEmptyMessageDelayed(0, 5000);
        }
        if (channelName == "政策") {
            handler.sendEmptyMessageDelayed(0, 5000);
        }
       /* if (channelName == "项目") {
            handler.sendEmptyMessageDelayed(0, 3000);
        }*/
        if (channelName == "设备") {
            handler.sendEmptyMessageDelayed(0, 5000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(getActivity());

        handler.removeCallbacksAndMessages(null);
        XGPushManager.onActivityStoped(getActivity());
        MobclickAgent.onPageEnd("推荐");
    }

    public int getScrollY() {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight() ;
    }

    class Receiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("action_toxuqiu")){
                try {
                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                        listView.setPullDownToRefreshFinish();

                    } else {
                        progress.setVisibility(View.VISIBLE);
                        pubdate = "";
                        ispushdownstate = true;
                        listView.setPullUpToRefreshable(true);
                        la.setVisibility(View.GONE);
                        listView.setSelection(0);
                        sortid="";
                        try{
                            String city1 = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY,"");
                            for(int i = 0; i< MainActivity.columnButton.size(); i++){
                                PlaceChannel item = MainActivity.columnButton.get(i);
                                if(item.nativeplace.contains(city1)){
                                    Recommend.channelid = item.channelid;
                                    break;
                                }
                            }
                        }catch (Exception e){

                        }
                        getjsons(Size, pubdate, false);
                        handler.sendEmptyMessageDelayed(2, 5000);
                    }
                }catch (Exception e){

                }
            }
            if(intent.getAction().equals("action_gogogo")){
                try {

                    listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                        private SparseArray recordSp = new SparseArray(0);
                        private int mCurrentfirstVisibleItem = 0;
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            switch (scrollState) {
                                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
//                                    ImageLoader.getInstance().pause();
//                                    if(lastindex ==0){
//                                        showAnimatorfor();
//                                    }
                                    break;
                                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                                    ImageLoader.getInstance().resume();
//                                    if(lastindex ==0){
//                                        showAnimatorfor();
//                                    }
                                    break;
                                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//                                    if(lastindex ==0){
//                                        showAnimatorfor();
//                                    }
                                    break;
                            }
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            mCurrentfirstVisibleItem = firstVisibleItem;
                            View firstView = view.getChildAt(0);
                            if (firstVisibleItem == 0) {
                                View firstVisibleItemView = listView.getChildAt(0);
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
                                int h = getScrollY();//滚动距离
                                Log.d("lizisong", "h:"+h);
                                if(h >120){
                                    int cha = h-120;
                                    float alpha = cha/100f;
                                    if(alpha < 1.0f){
                                        la.setAlpha(alpha);
                                    }else{
                                        la.setAlpha(1.0f);
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                                        if(tintManager != null){
                                            tintManager.setStatusBarTintEnabled(true);
                                            tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                            tintManager.setStatusBarAlpha(1);
                                        }
                                    }
                                    yingyin.setVisibility(View.VISIBLE);
                                    la.setVisibility(View.VISIBLE);

                                }else {
//                            title.getBackground().setAlpha(290-25-h);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                                        if(tintManager != null){
                                            tintManager.setStatusBarTintEnabled(true);
                                            tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                            tintManager.setStatusBarAlpha(0);
                                        }
                                    }
                                    la.setVisibility(View.GONE);
                                    yingyin.setVisibility(View.GONE);
                                }

                            }

//                            MainActivity.mFirstVisiableItem=firstVisibleItem;
//                            if (totalItemCount >= postsListData.size() && (netWorkType == NetUtils.NETWORKTYPE_INVALID)) {
//                                listView.setPullUpToRefreshFinish();
//                            }
//                            if(lastindex!= firstVisibleItem){
//                                lastindex= firstVisibleItem;
//                                int y = getScrollY();
//                                if(y >0){
//                                    hideAnimatorfor();
//                                }else{
//                                    showAnimatorfor();
//                                }
//                            }
//                                if(firstVisibleItem >= 1){
//
//
//                                    hideAnimatorfor();
////                                    donghua1();
//                                }else {
////                                    donghua2();
//                                    showAnimatorfor();
//                                }
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

//                    return 0;
                        }
                        class ItemRecod {
                            int height = 0;
                            int top = 0;
                        }
                    });
                }catch (Exception e){

                }
            }
        }
    }
    public void GunDongDing(){
        if(listView != null){
            if(recomment != null){
                recomment.shuaxin();
            }
//            listView.setPullUpToRefreshable(true);
//            listView.setSelection(0);

        }
    }
    public void GunDongDing1(){
        if(listView != null){
            if(recomment != null){
                la.setVisibility(View.GONE);
                recomment.shuaxin1();
            }
//            listView.setPullUpToRefreshable(true);
//            listView.setSelection(0);

        }
    }

//    public boolean dispatchTouchEvent(MotionEvent event){
//        return false;
//    }




    private void toolbarAnimal(final int flag) {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        if (flag == TAB_SHOW) {
            dingwei_lay.setVisibility(View.INVISIBLE);
            dingwei_id.setVisibility(View.INVISIBLE);
            mAnimator = ObjectAnimator.ofFloat(dingwei_lay, "translationY", dingwei_lay.getTranslationY(),0).setDuration(1000);
            mAnimator.start();
            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
//                    dingwei_id.setVisibility(View.INVISIBLE);
//                if(!mIsShow){
//                    layout_top.setVisibility(View.GONE);
//                }
//                    if(flag == TAB_SHOW){
//                        dingwei_lay.setVisibility(View.VISIBLE);
//                        dingwei_id.setVisibility(View.GONE);
//                    }else{
//                        dingwei_lay.setVisibility(View.GONE);
//                        dingwei_id.setVisibility(View.VISIBLE);
//                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
//                        mAnimator = ObjectAnimator.ofFloat(layout_top, "alpha", 1f, 0f);
        } else {
            mAnimator = ObjectAnimator.ofFloat(dingwei_lay, "scaleY", 1f, 0);
            mAnimator.start();



//            mAnimator = ObjectAnimator.ofFloat(layout_top, "alpha", 0f, 1f).setDuration(1000);
        }

    }

    private class MyOnGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("lizisong", "onFling");
            return false;
        }
    };

    private void showAnimator(){
//        mAnimator = ObjectAnimator.ofFloat(dingwei_lay, "scaleY", 1f,1f);
//        mAnimator.setDuration(1000);
        dingwei_lay.setVisibility(View.INVISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale_in);
        dingwei_id.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                  dingwei_lay.setVisibility(View.VISIBLE);
                  dingwei_id.setVisibility(View.VISIBLE);
//                  dingwei_id.setBackgroundResource(R.mipmap.miandiantoutiaoicon1);
                  isanimone=true;
                  isEnd = false;
//                dingwei_lay.getLayoutParams().height = 50;
//                  tiplay.getLayoutParams().height = 50;
//                Log.d("lizisong", old+"");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
    private void showAlpha(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_alpha_in);
        dingwei_lay.startAnimation(animation);

    }
    private void hideAlpha(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_appha_out);
        dingwei_lay.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                dingwei_lay.setVisibility(View.INVISIBLE);
                isEnd = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void hideAnimator(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale_out);
        dingwei_id.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                dingwei_lay.setVisibility(View.VISIBLE);
//                dingwei_id.setVisibility(View.VISIBLE);
//                dingwei_id.setBackgroundResource(R.mipmap.miandiantoutiaoicon);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }



    private void gaiaddbian(){
        old = 0;
        Message msg = Message.obtain();
        msg.what = 30;
        handler.sendMessage(msg);


    }
    private void gaireovebian(){
        old=50;
        Message msg = Message.obtain();
        msg.what = 31;
        handler.sendMessage(msg);
    }




    public void showAnimatorfor(){
        if((dingwei_lay.getVisibility() == View.GONE && !isEnd && state == 0)){
            state = 1;
            showAnimator();
            showAlpha();

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isAnuationStaring = true;
                    if(recomment != null){
                        recomment.setIsAnuation(isAnuationStaring);
                    }
                    ValueAnimator va ;
                    va = ValueAnimator.ofInt(0,100);
                    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            //获取当前的height值
                            int h =(Integer)valueAnimator.getAnimatedValue();
                            //动态更新view的高度
                            dingwei_lay.getLayoutParams().height = h;
//                            dingwei_lay.invalidate();
                            dingwei_lay.requestLayout();
                        }
                    });
                    va.addListener(new Animator.AnimatorListener(){

                        @Override
                        public void onAnimationStart(Animator animation) {
                            isEnd = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            dingwei_lay.setVisibility(View.VISIBLE);
                            isEnd = false;
                            handler.sendEmptyMessageDelayed(100, 10);

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    va.setDuration(500);
                    //开始动画
                    va.start();

                }
            });



        }
    }

    public void hideAnimatorfor(){
        dingwei_id.setVisibility(View.VISIBLE);
        if(isanimone && !isEnd ){
            state = 1;
            if(dingwei_lay.getVisibility() == View.VISIBLE){
                isanimone = false;
                isEnd = true;
                dingwei_lay.setVisibility(View.INVISIBLE);
                hideAnimator();
                hideAlpha();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isAnuationStaring = true;
                        if(recomment != null){
                            recomment.setIsAnuation(isAnuationStaring);
                        }
                        ValueAnimator va ;
                        va = ValueAnimator.ofInt(100,0);
                        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                //获取当前的height值
                                int h =(Integer)valueAnimator.getAnimatedValue();
                                //动态更新view的高度
                                dingwei_lay.getLayoutParams().height = h;
                                dingwei_lay.requestLayout();
                            }
                        });
                        va.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                isEnd = true;
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                dingwei_lay.setVisibility(View.GONE);
                                isEnd = false;
//                                isAnuationStaring = false;
//                                if(recomment != null){
//                                    recomment.setIsAnuation(isAnuationStaring);
//                                }
                                handler.sendEmptyMessageDelayed(100, 10);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        va.setDuration(500);
                        //开始动画
                        va.start();



                        }
//                    }
                });


            }
        }
    }


    public void donghua1(){
        if(isanimone && !isEnd){
            isanimone = false;
            hideAnimator();
                        hideAlpha();
            ValueAnimator va ;
            va = ValueAnimator.ofInt(80,0);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    //获取当前的height值
                    int h =(Integer)valueAnimator.getAnimatedValue();
                    //动态更新view的高度
                    dingwei_lay.getLayoutParams().height = h;
                    dingwei_lay.invalidate();
                }
            });
            va.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isEnd = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    dingwei_lay.setVisibility(View.GONE);
                    isEnd = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            va.setDuration(500);
            //开始动画
            va.start();
        }
    }

    public void donghua2(){
        if(dingwei_lay.getVisibility() == View.GONE && !isEnd){
            showAnimator();
            showAlpha();
            ValueAnimator va ;
            va = ValueAnimator.ofInt(0,80);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    //获取当前的height值
                    int h =(Integer)valueAnimator.getAnimatedValue();
                    //动态更新view的高度
                    dingwei_lay.getLayoutParams().height = h;
                  dingwei_lay.invalidate();
                }
            });
            va.addListener(new Animator.AnimatorListener(){

                @Override
                public void onAnimationStart(Animator animation) {
                    isEnd = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    dingwei_lay.setVisibility(View.VISIBLE);
                    isEnd = false;


                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            va.setDuration(500);
            //开始动画
            va.start();

        }
    }

    public void ceshidonghua1(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale_xialai);
        dingwei_lay.startAnimation(animation);
    }

    public void ceshidonghua2(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale_shangqu);
        dingwei_lay.startAnimation(animation);
    }


}
