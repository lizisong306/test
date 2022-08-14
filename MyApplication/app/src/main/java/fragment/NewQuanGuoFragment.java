package fragment;

import android.animation.Animator;
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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.maidiantech.ActiveActivity;
import com.maidiantech.AppointmentSpecialist;
import com.maidiantech.DFInfoShow;
import com.maidiantech.DetailsActivity;
import com.maidiantech.LocalCity;
import com.maidiantech.LookMoreActivity;
import com.maidiantech.MainActivity;
import com.maidiantech.MyloginActivity;
import com.maidiantech.NewJingPinProject;
import com.maidiantech.NewProJect;
import com.maidiantech.NewProjectActivity;
import com.maidiantech.NewSearchHistory;
import com.maidiantech.NewXuQiu;
import com.maidiantech.NewZhuanliActivity;
import com.maidiantech.PatentActivity;
import com.maidiantech.PersonActivity;
import com.maidiantech.PolicyActivity;
import com.maidiantech.QingBaoActivity;
import com.maidiantech.QingBaoDeilActivity;
import com.maidiantech.R;
import com.maidiantech.SheBeiActivity;
import com.maidiantech.UnitedStatesActivity;
import com.maidiantech.WebViewActivity;
import com.maidiantech.WriteXuQiu;
import com.maidiantech.XiangMuDuiJieActivity;
import com.maidiantech.XinFanAnCeShi;
import com.maidiantech.ZhuanLiShenQing;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import Util.FileHelper;
import Util.NetUtils;
import Util.SharedPreferencesUtil;
import Util.TimeUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import db.ChannelDb;
import entity.ADS;
import entity.ColumnData;
import entity.HomeEntity;
import entity.PlaceChannel;
import entity.PostData;
import entity.PostsData;
import view.BTAlertDialog;
import view.MyClickListener;
import view.RefreshListView;
import view.RoundImageView;
import view.ScrollBanner;
import view.SystemBarTintManager;
import view.ZQImageViewRoundOval;

/**
 * Created by Administrator on 2018/7/17.
 */

public class NewQuanGuoFragment extends BaseFragment {
    private View view;
    private View HeaderView;
    RefreshListView listview;
    ProgressBar progress;
    LinearLayout title;
    ImageView nodata,quanguo,lay_yin;
    HomeEntity homedata;
    String code;
    public List<PostsData> listData = new ArrayList<>();
    public  List<ADS> adsListData=new ArrayList<>();
    public List<ADS> advListData = new ArrayList<>();

    public List<String> zixuanlist = new ArrayList<>();
    public HashMap<String, String> zixuanaid = new HashMap<>();
    private DisplayImageOptions options;
    String city ="";
    private int screenWidth;
    boolean  isanimone = false;
    public String sortid;
    ReceiverBroad receiverBroad;
    NewQuanGuoFragmentAdapter adapter;
    ImageView nice_spinner1,jia,jia1,yinying;
    RelativeLayout search_lay,lay,search_lay1,viewPagerContainer;
    ViewPager viewpager;
    boolean isherat = true;
    LinearLayout four_cloum1;
    RelativeLayout four_cloum1_item1,four_cloum1_item2,four_cloum1_item3,four_cloum1_item4,four_cloum1_item5;
    ImageView four_cloum1_img1,four_cloum1_img2,four_cloum1_img3,four_cloum1_img4,four_cloum1_img5;
    TextView four_cloum1_text_item1,four_cloum1_text_item2,four_cloum1_text_item3,four_cloum1_text_item4,four_cloum1_text_item5;
    MyAdapter myPagerAdapter;
    PlaceChannel currentChannel;
    String channelid,type;
    ColumnData currentColumnData;
    int current =0;
    private  boolean isEnd = false;
    private int state = 0;
    boolean isAnuationStaring = false;
    SystemBarTintManager tintManager ;
    int heiht = 0;
    int lastindex = -1;
    LinearLayout title_lay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.newquanguofragment, null);
            options = ImageLoaderUtils.initOptions();
            city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
            city = "全国";
            nice_spinner1 = (ImageView)view.findViewById(R.id.nice_spinner1);
            title_lay = (LinearLayout)view.findViewById(R.id.title);
            jia = (ImageView)view.findViewById(R.id.jia);
            search_lay = (RelativeLayout)view.findViewById(R.id.search_lay);

            listview = (RefreshListView)view.findViewById(R.id.listview);
            progress = (ProgressBar)view.findViewById(R.id.progress);
            nodata   = (ImageView)view.findViewById(R.id.nodata);
            lay_yin = (ImageView) view.findViewById(R.id.lay_yin);
            HeaderView = getLayoutInflater(savedInstanceState).inflate(R.layout.newquanguofragmentheart, null);
            viewPagerContainer = (RelativeLayout)HeaderView.findViewById(R.id.viewPagerContainer);
            lay = (RelativeLayout)HeaderView.findViewById(R.id.lay);
            search_lay1 = (RelativeLayout)HeaderView.findViewById(R.id.search_lay1);
            quanguo = (ImageView)HeaderView.findViewById(R.id.quanguo);
            quanguo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LocalCity.class);
                    startActivity(intent);
                }
            });
            jia1 = (ImageView)HeaderView.findViewById(R.id.jia1);
            viewpager = (ViewPager)HeaderView.findViewById(R.id.viewpager);
            four_cloum1 = (LinearLayout)HeaderView.findViewById(R.id.four_cloum1);
            four_cloum1_img1 = (ImageView)HeaderView.findViewById(R.id.four_cloum1_img1);
            four_cloum1_img2 = (ImageView)HeaderView.findViewById(R.id.four_cloum1_img2);
            four_cloum1_img3 = (ImageView)HeaderView.findViewById(R.id.four_cloum1_img3);
            four_cloum1_img4 = (ImageView)HeaderView.findViewById(R.id.four_cloum1_img4);
            four_cloum1_img5 = (ImageView)HeaderView.findViewById(R.id.four_cloum1_img5);
            four_cloum1_text_item1 = (TextView)HeaderView.findViewById(R.id.four_cloum1_text_item1);
            four_cloum1_text_item2 = (TextView)HeaderView.findViewById(R.id.four_cloum1_text_item2);
            four_cloum1_text_item3 = (TextView)HeaderView.findViewById(R.id.four_cloum1_text_item3);
            four_cloum1_text_item4 = (TextView)HeaderView.findViewById(R.id.four_cloum1_text_item4);
            four_cloum1_text_item5 = (TextView)HeaderView.findViewById(R.id.four_cloum1_text_item5);
            four_cloum1_item1 = (RelativeLayout)HeaderView.findViewById(R.id.four_cloum1_item1);
            four_cloum1_item2 = (RelativeLayout)HeaderView.findViewById(R.id.four_cloum1_item2);
            four_cloum1_item3 = (RelativeLayout)HeaderView.findViewById(R.id.four_cloum1_item3);
            four_cloum1_item4 = (RelativeLayout)HeaderView.findViewById(R.id.four_cloum1_item4);
            four_cloum1_item5 = (RelativeLayout)HeaderView.findViewById(R.id.four_cloum1_item5);
        }

        nice_spinner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocalCity.class);
                startActivity(intent);
            }
        });
        search_lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewSearchHistory.class);
                startActivity(intent);
            }
        });
        search_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewSearchHistory.class);
                startActivity(intent);
            }
        });
        jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE,"");
                if(state.equals("1")){
                    Intent intent = new Intent(getActivity(), NewXuQiu.class);
                    intent.putExtra("typeid", "0");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), MyloginActivity.class);
                    startActivity(intent);
                }
            }
        });
        jia1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE,"");
                if(state.equals("1")){
                    Intent intent = new Intent(getActivity(), NewXuQiu.class);
                    intent.putExtra("typeid", "0");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), MyloginActivity.class);
                    startActivity(intent);
                }
            }
        });

        listview.setOnScrollListener(new AbsListView.OnScrollListener(){
            private SparseArray recordSp = new SparseArray(0);
            private int mCurrentfirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mCurrentfirstVisibleItem = firstVisibleItem;
                View firstView = view.getChildAt(0);
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = listview.getChildAt(0);
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
                    if(h >120){
                        int cha = h-120;
                        float alpha = cha/100f;
                        if(alpha < 1.0f){
                            title_lay.setAlpha(alpha);
                        }else{
                            title_lay.setAlpha(1.0f);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                            if(tintManager != null){
                                tintManager.setStatusBarTintEnabled(true);
                                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                tintManager.setStatusBarAlpha(1);
                            }
                        }
                        title_lay.setVisibility(View.VISIBLE);
                        lay_yin.setVisibility(View.VISIBLE);

                    }else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//1
                            if(tintManager != null){
                                tintManager.setStatusBarTintEnabled(true);
                                tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
                                tintManager.setStatusBarAlpha(0);
                            }
                        }
                        title_lay.setVisibility(View.GONE);
                        lay_yin.setVisibility(View.GONE);
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
            }

            class ItemRecod {
                int height = 0;
                int top = 0;
            }
        } );

//        if(MainActivity.hasSoftKeys(getActivity().getWindowManager())){
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listview.getLayoutParams();
//            layoutParams.bottomMargin=MyApplication.navigationbar+150;
//            listview.setLayoutParams(layoutParams);
//        }
        nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    for(int i = 0; i< MainActivity.columnButton.size(); i++){
                        PlaceChannel item = MainActivity.columnButton.get(i);
                        if(item.nativeplace.contains(city)){
                            currentChannel = item;
                            channelid = item.channelid;
                            code=item.code;
                            break;
                        }
                    }
                    handlerCase();
                    getJson();
                }catch (Exception e){

                }
            }
        });

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        heiht = (screenWidth-72)*30/65;
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.VISIBLE);
            for(int i = 0; i< MainActivity.columnButton.size(); i++){
                PlaceChannel item = MainActivity.columnButton.get(i);
                if(item.nativeplace.contains(city)){
                    currentChannel = item;
                    channelid = item.channelid;
                    code=item.code;
                    break;
                }
            }
        }
        return view;
    }

//    public int getScrollY() {
//        View c = listview.getChildAt(0);
//        if (c == null) {
//            return 0;
//        }
//        int firstVisiblePosition = listview.getFirstVisiblePosition();
//        int top = c.getTop();
//        return -top + firstVisiblePosition * c.getHeight() ;
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tintManager = new SystemBarTintManager(getActivity());
        initViewPager();
        receiverBroad = new ReceiverBroad();
        IntentFilter oninfilter = new IntentFilter();
        oninfilter.addAction("downok");
        oninfilter.addAction("changetitles");
        oninfilter.addAction("changelistviewheihgt");
        getActivity().registerReceiver(receiverBroad, oninfilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiverBroad);
        handler.removeCallbacksAndMessages(null);
    }

    private void initViewPager() {
        viewpager.setClipChildren(false);
        //父容器一定要设置这个，否则看不出效果
        viewPagerContainer.setClipChildren(false);
        viewpager.setPageTransformer(true,new view.ScaleInTransformer());
        //设置预加载数量
        viewpager.setOffscreenPageLimit(3);
        viewpager.setCurrentItem(Integer.MAX_VALUE);
    }

    @Override
    protected void lazyLoad() {

    }
    public void getJson(){
        String url ="http://"+MyApplication.ip+"/api/getHomeRecommendData.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("channelid", "4");
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                progress.setVisibility(View.GONE);
                if(msg.what == 1){
                    String ret = (String )msg.obj;
                    FileHelper.d("lizisong", ret);
                    Gson gson = new Gson();
                    homedata = gson.fromJson(ret, HomeEntity.class);
                    if(homedata != null && homedata.code.equals("1")){
                        listData.clear();
                        if(homedata.data.ads!=null){
                            for (int i=0;i<homedata.data.ads.size();i++){
                                ADS ads = homedata.data.ads.get(i);
                                adsListData.add(ads);
                            }
                        }
                        myPagerAdapter = new MyAdapter();
                        viewpager.setAdapter(myPagerAdapter);
                        viewpager.setCurrentItem(100000);
                        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                current = position;
                            }

                            @Override
                            public void onPageSelected(int position) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });
                        handlerCase();
                        four_cloum1_item1.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                            @Override
                            public void oneClick() {
                                currentColumnData= currentChannel.column1.get(0);
                                type=currentColumnData.type;
                                channelid = currentColumnData.channelid;
                                handleronClick(type, currentColumnData,channelid);
                            }

                            @Override
                            public void doubleClick() {

                            }
                        }));
//                        four_cloum1_item1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                currentColumnData= currentChannel.column1.get(0);
//                                type=currentColumnData.type;
//                                channelid = currentColumnData.channelid;
//                                handleronClick(type, currentColumnData,channelid);
//                            }
//                        });
                        four_cloum1_item2.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                            @Override
                            public void oneClick() {
                                currentColumnData= currentChannel.column1.get(1);
                                type=currentColumnData.type;
                                channelid = currentColumnData.channelid;
                                handleronClick(type, currentColumnData,channelid);
                            }

                            @Override
                            public void doubleClick() {

                            }
                        }));
//                        four_cloum1_item2.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                currentColumnData= currentChannel.column1.get(1);
//                                type=currentColumnData.type;
//                                channelid = currentColumnData.channelid;
//                                handleronClick(type, currentColumnData,channelid);
//                            }
//                        });
                        four_cloum1_item3.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                            @Override
                            public void oneClick() {
                                currentColumnData= currentChannel.column1.get(2);
                                type=currentColumnData.type;
                                channelid = currentColumnData.channelid;
                                handleronClick(type, currentColumnData,channelid);
                            }

                            @Override
                            public void doubleClick() {

                            }
                        }));
//                        four_cloum1_item3.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                currentColumnData= currentChannel.column1.get(2);
//                                type=currentColumnData.type;
//                                channelid = currentColumnData.channelid;
//                                handleronClick(type, currentColumnData,channelid);
//                            }
//                        });
                        four_cloum1_item4.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                            @Override
                            public void oneClick() {
                                currentColumnData= currentChannel.column1.get(3);
                                type=currentColumnData.type;
                                channelid = currentColumnData.channelid;
                                handleronClick(type, currentColumnData,channelid);
                            }

                            @Override
                            public void doubleClick() {

                            }
                        }));

//                        four_cloum1_item4.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                currentColumnData= currentChannel.column1.get(3);
//                                type=currentColumnData.type;
//                                channelid = currentColumnData.channelid;
//                                handleronClick(type, currentColumnData,channelid);
//                            }
//                        });
                        four_cloum1_item5.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                            @Override
                            public void oneClick() {
                                currentColumnData= currentChannel.column1.get(4);
                                type=currentColumnData.type;
                                channelid = currentColumnData.channelid;
                                handleronClick(type, currentColumnData,channelid);
                            }

                            @Override
                            public void doubleClick() {

                            }
                        }));

//                        four_cloum1_item5.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                currentColumnData= currentChannel.column1.get(4);
//                                type=currentColumnData.type;
//                                channelid = currentColumnData.channelid;
//                                handleronClick(type, currentColumnData,channelid);
//                            }
//                        });

                        if(homedata.data.adv!=null){
                            for (int i=0;i<homedata.data.adv.size();i++){
                                ADS ads = homedata.data.adv.get(i);
                                advListData.add(ads);
                            }
                        }

                            PostsData zixuan = new PostsData();
                            zixuan.typename ="zixun";
                            zixuan.zixun = homedata.data.posts.zixun;
                            listData.add(zixuan);
                            String pos = "";
                            String aids="";

                            int p=0;
                            for(int i=0; i<zixuan.zixun.size(); i++){
                                PostData posdata = zixuan.zixun.get(i);
                                String type="【"+posdata.typename+"】";

                                if(i % 2 ==0){
                                    pos="";
                                    aids="";
                                    pos = type+posdata.title.replace("\r\n","").replace("\n", "");
                                    aids=posdata.id+",";

                                }else if(i %2 == 1){
                                    pos =pos + "\n"+type+posdata.title.replace("\r\n", "").replace("\n", "");
                                    aids=aids+posdata.id;
                                    zixuanaid.put(p+"",aids);
                                    p++;
                                    zixuanlist.add(pos);
                                }
                            }

                        for(int i=0; i<zixuan.zixun.size(); i++){
                            PostData posdata = zixuan.zixun.get(i);
                            String type="【"+posdata.typename+"】";

                            if(i % 2 ==0){
                                pos="";
                                aids="";
                                pos = type+posdata.title.replace("\r\n","").replace("\n", "");
                                aids=posdata.id+",";

                            }else if(i %2 == 1){
                                pos =pos + "\n"+type+posdata.title.replace("\r\n", "").replace("\n", "");
                                aids=aids+posdata.id;
                                zixuanaid.put(p+"",aids);
                                p++;
                                zixuanlist.add(pos);
                            }
                        }

                            if(homedata.data.adv!=null && homedata.data.adv.size() >0){
                                PostsData guanggao = new PostsData();
                                guanggao.typename ="guanggao";
                                listData.add(guanggao);

                            }

                            PostsData newsan = new PostsData();
                            newsan.typename ="newsan";
                            listData.add(newsan);
                            if(homedata.data.posts.xiangmu != null && homedata.data.posts.xiangmu.size()>0 ){
                                PostsData xiangmu = new PostsData();
                                xiangmu.typename ="xiangmu";
                                xiangmu.xiangmu = homedata.data.posts.xiangmu;
                                listData.add(xiangmu);
                            }
                           if(homedata.data.posts.rencai != null && homedata.data.posts.rencai.size()>0){
                                PostsData rencai = new PostsData();
                                rencai.typename ="rencai";
                                rencai.rencai = homedata.data.posts.rencai;
                                listData.add(rencai);
                           }
                           if(homedata.data.posts.qingbao != null && homedata.data.posts.qingbao.size()>0){
                                PostsData qingbao = new PostsData();
                                qingbao.typename ="qingbao";
                                qingbao.qingbao = homedata.data.posts.qingbao;
                                listData.add(qingbao);
                           }
                           if(homedata.data.posts.zhuanli != null && homedata.data.posts.zhuanli.size()>0){
                                PostsData zhuanli = new PostsData();
                                zhuanli.typename ="zhuanli";
                                zhuanli.zhuanli = homedata.data.posts.zhuanli;
                                listData.add(zhuanli);
                           }
                           if(homedata.data.posts.offline != null &&homedata.data.posts.offline.size()>0 ){
                                PostsData offline = new PostsData();
                                offline.typename ="offline";
                                offline.offline = homedata.data.posts.offline;
                                listData.add(offline);
                           }

                            if(adapter == null){
                                listview.addHeaderView(HeaderView);
                                Message message = Message.obtain();
                                msg.what = 100;
                                handler.sendMessageDelayed(message, 4000);
                                adapter = new NewQuanGuoFragmentAdapter();
                                listview.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }else {
                                adapter.notifyDataSetChanged();
                            }
                            nodata.setVisibility(View.GONE);
                        }
                }


                if(msg.what == 100){
                    Message message =Message.obtain();
                    message.what=100;
                    if(viewpager != null){
                            int item = viewpager.getCurrentItem();
                            item++;
                            viewpager.setCurrentItem(item);

                    }
                    handler.sendMessageDelayed(message, 4000);
                }

                if(msg.what == 3){
                    progress.setVisibility(View.GONE);
                    Gson gson = new Gson();
                     String json = (String)msg.obj;
                    homedata = gson.fromJson(json, HomeEntity.class);
                    if(homedata != null && homedata.code.equals("1")){
                        if(homedata.data.posts.xiangmu != null && homedata.data.posts.xiangmu.size() >0){
                            for(int i =0;i<listData.size();i++){
                                PostsData item = listData.get(i);
                                if(item.typename.equals("xiangmu")){
                                    item.xiangmu = homedata.data.posts.xiangmu;
                                    adapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                }

                if(msg.what == 4){
                    progress.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    String json = (String)msg.obj;
                    homedata = gson.fromJson(json, HomeEntity.class);
                    if(homedata != null && homedata.code.equals("1")){
                        if(homedata.data.posts.rencai != null && homedata.data.posts.rencai.size() >0){
                            for(int i =0;i<listData.size();i++){
                                PostsData item = listData.get(i);
                                if(item.typename.equals("rencai")){
                                    item.rencai = homedata.data.posts.rencai;
                                    adapter.notifyDataSetChanged();
                                    break;
                                }
                            }

                        }

                    }
                }

                if(msg.what == 120){
                    isAnuationStaring = false;
                    if(mScrollBanner != null){
                        mScrollBanner.setStop(false);
                        mScrollBanner.startScroll();
                    }

                }

                if(msg.what == 5){
                    progress.setVisibility(View.GONE);
                    Gson gson = new Gson();
                    String json = (String)msg.obj;
                    homedata = gson.fromJson(json, HomeEntity.class);
                    if(homedata != null && homedata.code.equals("1")){
                        if(homedata.data.posts.zhuanli != null && homedata.data.posts.zhuanli.size() >0){
                            for(int i =0;i<listData.size();i++){
                                PostsData item = listData.get(i);
                                if(item.typename.equals("zhuanli")){
                                    item.zhuanli = homedata.data.posts.zhuanli;
                                    adapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                }
            }catch (Exception e){
                nodata.setVisibility(View.VISIBLE);
            }
        }
    };
    ScrollBanner mScrollBanner;
    class NewQuanGuoFragmentAdapter extends BaseAdapter{

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
            HoldView holdview = null;
            if(convertView == null){
                holdview = new HoldView();
                convertView = View.inflate(getActivity(),R.layout.newquanguofragmentadapter,null);
                holdview.tianxuqiu = (LinearLayout)convertView.findViewById(R.id.tianxuqiu);
                holdview.scrollBanner = (ScrollBanner)convertView.findViewById(R.id.sb_demographic);
                holdview.xiangmu = (LinearLayout)convertView.findViewById(R.id.xiangmu);
                holdview.xiangmu_lay1 = (RelativeLayout)convertView.findViewById(R.id.xiangmu_lay1);
                holdview.xiangmu_lay2 = (RelativeLayout)convertView.findViewById(R.id.xiangmu_lay2);
                holdview.xiangmu_lay3 = (RelativeLayout)convertView.findViewById(R.id.xiangmu_lay3);
                holdview.xiangmu_line1 = (TextView)convertView.findViewById(R.id.xiangmu_line1);
                holdview.xiangmu_line2 = (TextView)convertView.findViewById(R.id.xiangmu_line2);
                holdview.xiangmu_line3 = (TextView)convertView.findViewById(R.id.xiangmu_line3);
                holdview.xiangmu_title = (TextView)convertView.findViewById(R.id.xiangmu_title);
                holdview.xiangmu_change = (TextView)convertView.findViewById(R.id.xiangmu_change);
                holdview.source1 = (TextView)convertView.findViewById(R.id.source1);
                holdview.source2 = (TextView)convertView.findViewById(R.id.source2);
                holdview.source3 = (TextView)convertView.findViewById(R.id.source3);
                holdview.xianmgmu1 = (ImageView)convertView.findViewById(R.id.xianmgmu1);
                holdview.xianmgmu2 = (ImageView)convertView.findViewById(R.id.xianmgmu2);
                holdview.xianmgmu3 = (ImageView)convertView.findViewById(R.id.xianmgmu3);
                holdview.xmtitle1 = (TextView)convertView.findViewById(R.id.xmtitle1);
                holdview.xmtitle2 = (TextView)convertView.findViewById(R.id.xmtitle2);
                holdview.xmtitle3 = (TextView)convertView.findViewById(R.id.xmtitle3);
                holdview.lanyuan1 = (TextView)convertView.findViewById(R.id.lanyuan1);
                holdview.lanyuan2 = (TextView)convertView.findViewById(R.id.lanyuan2);
                holdview.lanyuan3 = (TextView)convertView.findViewById(R.id.lanyuan3);
                holdview.xm_more = (TextView)convertView.findViewById(R.id.xm_more);
                holdview.zhuanjia = (LinearLayout)convertView.findViewById(R.id.zhuanjia);
                holdview.rencai_lay1 = (RelativeLayout)convertView.findViewById(R.id.rencai_lay1);
                holdview.rencai_lay2 = (RelativeLayout)convertView.findViewById(R.id.rencai_lay2);
                holdview.rencai_lay3 = (RelativeLayout)convertView.findViewById(R.id.rencai_lay3);
                holdview.rencai_lay4 = (RelativeLayout)convertView.findViewById(R.id.rencai_lay4);

                holdview.rencai_line1 = (TextView)convertView.findViewById(R.id.rencai_line1);
                holdview.rencai_line2 = (TextView)convertView.findViewById(R.id.rencai_line2);
                holdview.rencai_line3 = (TextView)convertView.findViewById(R.id.rencai_line3);
                holdview.rencai_line4 = (TextView)convertView.findViewById(R.id.rencai_line4);

                holdview.zhuangjia_change = (TextView)convertView.findViewById(R.id.zhuangjia_change);
                holdview.rencai_img1 = (RoundImageView)convertView.findViewById(R.id.rencai_img1);
                holdview.rencai_img2 = (RoundImageView)convertView.findViewById(R.id.rencai_img2);
                holdview.rencai_img3 = (RoundImageView)convertView.findViewById(R.id.rencai_img3);
                holdview.rencai_img4 = (RoundImageView)convertView.findViewById(R.id.rencai_img4);
                holdview.rencai_title1 = (TextView)convertView.findViewById(R.id.rencai_title1);
                holdview.rencai_title2 = (TextView)convertView.findViewById(R.id.rencai_title2);
                holdview.rencai_title3 = (TextView)convertView.findViewById(R.id.rencai_title3);
                holdview.rencai_title4 = (TextView)convertView.findViewById(R.id.rencai_title4);
                holdview.rencai_lingyu1 = (TextView)convertView.findViewById(R.id.rencai_lingyu1);
                holdview.rencai_lingyu2 = (TextView)convertView.findViewById(R.id.rencai_lingyu2);
                holdview.rencai_lingyu3 = (TextView)convertView.findViewById(R.id.rencai_lingyu3);
                holdview.rencai_lingyu4 = (TextView)convertView.findViewById(R.id.rencai_lingyu4);
                holdview.rank1 = (TextView)convertView.findViewById(R.id.rank1);
                holdview.er =(LinearLayout)convertView.findViewById(R.id.er);
                holdview.er1 = (ZQImageViewRoundOval)convertView.findViewById(R.id.er1);
                holdview.er2 = (ZQImageViewRoundOval)convertView.findViewById(R.id.er2);
                holdview.yi  = (LinearLayout)convertView.findViewById(R.id.yi);
                holdview.yi1 = (ZQImageViewRoundOval)convertView.findViewById(R.id.yi1);
                holdview.rank2 = (TextView)convertView.findViewById(R.id.rank2);
                holdview.rank3 = (TextView)convertView.findViewById(R.id.rank3);
                holdview.rank4 = (TextView)convertView.findViewById(R.id.rank4);
                holdview.zhuanjia_more = (TextView)convertView.findViewById(R.id.zhuanjia_more);
                holdview.hangye = (LinearLayout)convertView.findViewById(R.id.hangye);
                holdview.hangye_title = (TextView)convertView.findViewById(R.id.hangye_title);
                holdview.hangye_more = (TextView)convertView.findViewById(R.id.hangye_more);
                holdview.qingbaoitem = (RelativeLayout)convertView.findViewById(R.id.qingbaoitem);
                holdview.qingicon = (ImageView)convertView.findViewById(R.id.qingicon);
                holdview.title =(TextView)convertView.findViewById(R.id.title);
                holdview.price = (TextView)convertView.findViewById(R.id.price);
                holdview.san = (LinearLayout)convertView.findViewById(R.id.san);
                holdview.description = (TextView)convertView.findViewById(R.id.description);
                holdview.timeupdate = (TextView)convertView.findViewById(R.id.timeupdate);
                holdview.updatadescription=(TextView)convertView.findViewById(R.id.updatadescription);
                holdview.type = (ImageView)convertView.findViewById(R.id.type);
                holdview.zhuanli = (LinearLayout)convertView.findViewById(R.id.zhuanli);
                holdview.zhuanli_title = (TextView)convertView.findViewById(R.id.zhuanli_title);
                holdview.zhuanli_change = (TextView)convertView.findViewById(R.id.zhuanli_change);
                holdview.zhuanli_lay1 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay1);
                holdview.zhuanli_lay2 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay2);
                holdview.zhuanli_lay3 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay3);
                holdview.zhuanli_lay4 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay4);
                holdview.zhuanli_line1 = (TextView)convertView.findViewById(R.id.zhuanli_line1);
                holdview.zhuanli_line2 = (TextView)convertView.findViewById(R.id.zhuanli_line2);
                holdview.zhuanli_line3 = (TextView)convertView.findViewById(R.id.zhuanli_line3);
                holdview.zhuanli_line4 = (TextView)convertView.findViewById(R.id.zhuanli_line4);
                holdview.zhuanli_title1 = (TextView)convertView.findViewById(R.id.zhuanli_title1);
                holdview.zhuanli_title2 = (TextView)convertView.findViewById(R.id.zhuanli_title2);
                holdview.zhuanli_title3 = (TextView)convertView.findViewById(R.id.zhuanli_title3);
                holdview.zhuanli_title4 = (TextView)convertView.findViewById(R.id.zhuanli_title4);
                holdview.zhuanli_lingyu1 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu1);
                holdview.zhuanli_lingyu2 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu2);
                holdview.zhuanli_lingyu3 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu3);
                holdview.zhuanli_lingyu4 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu4);
                holdview.zhuanli_more = (TextView)convertView.findViewById(R.id.zhuanli_more);
                holdview.xianxiafuwu = (LinearLayout)convertView.findViewById(R.id.xianxiafuwu);
                holdview.icon_left = (ImageView) convertView.findViewById(R.id.icon_left);
                holdview.icon_right = (ImageView) convertView.findViewById(R.id.icon_right);

                holdview.santuu = (LinearLayout)convertView.findViewById(R.id.santuu);
                holdview.tupian1 = (ZQImageViewRoundOval) convertView.findViewById(R.id.tupian1);
                holdview.tupian2 = (ZQImageViewRoundOval) convertView.findViewById(R.id.tupian2);
                holdview.tupian3 = (ZQImageViewRoundOval) convertView.findViewById(R.id.tupian3);
                holdview.tupian1.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdview.tupian1.setRoundRadius(20);
                holdview.tupian2.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdview.tupian2.setRoundRadius(20);
                holdview.tupian3.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdview.tupian3.setRoundRadius(20);
                holdview.er1.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdview.er1.setRoundRadius(20);
                holdview.er2.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdview.er2.setRoundRadius(20);
                holdview.yi1.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdview.yi1.setRoundRadius(20);
                int heiht=0;
                heiht = (screenWidth-60)*461/1326;
                ViewGroup.LayoutParams lp = holdview.yi1.getLayoutParams();
                lp.height = heiht;
                holdview.yi1.setLayoutParams(lp);
                convertView.setTag(holdview);
            }else {
                holdview = (HoldView) convertView.getTag();
            }
            try{

                    final PostsData item = listData.get(position);
                    holdview.tianxuqiu.setVisibility(View.GONE);
                    holdview.santuu.setVisibility(View.GONE);
                    holdview.xiangmu.setVisibility(View.GONE);
                    holdview.zhuanjia.setVisibility(View.GONE);
                    holdview.hangye.setVisibility(View.GONE);
                    holdview.zhuanli.setVisibility(View.GONE);
                    holdview.xianxiafuwu.setVisibility(View.GONE);
                    holdview.qingbaoitem.setVisibility(View.GONE);
                    if (item.typename.equals("zixun")) {
                        holdview.santuu.setVisibility(View.GONE);
                        holdview.tianxuqiu.setVisibility(View.VISIBLE);
                        holdview.xiangmu.setVisibility(View.GONE);
                        holdview.zhuanjia.setVisibility(View.GONE);
                        holdview.hangye.setVisibility(View.GONE);
                        holdview.zhuanli.setVisibility(View.GONE);
                        holdview.xianxiafuwu.setVisibility(View.GONE);
                        holdview.qingbaoitem.setVisibility(View.GONE);
                        mScrollBanner =  holdview.scrollBanner;
                        holdview.scrollBanner.setList(zixuanlist);
                        holdview.scrollBanner.stopScroll();
                        holdview.scrollBanner.startScroll();
                        holdview.scrollBanner.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //跳转
                                Intent intent = new Intent();
                                intent.setAction("action_toxuqiu");
                                Recommend.aids = zixuanaid.get(mScrollBanner.getCurrent() + "");
                                getActivity().sendBroadcast(intent);
                            }
                        });
                    } else if (item.typename.equals("newsan")) {

                        holdview.xiangmu.setVisibility(View.GONE);
                        holdview.zhuanjia.setVisibility(View.GONE);
                        holdview.hangye.setVisibility(View.GONE);
                        holdview.zhuanli.setVisibility(View.GONE);
                        holdview.qingbaoitem.setVisibility(View.GONE);
                        holdview.xianxiafuwu.setVisibility(View.GONE);
                        holdview.santuu.setVisibility(View.VISIBLE);

                        final ADS item1, item2, item3;
                        if(advListData.size() == 3){
                            holdview.san.setVisibility(View.VISIBLE);
                            holdview.yi.setVisibility(View.GONE);
                            holdview.er.setVisibility(View.GONE);
                            item1 = advListData.get(0);
                            item2 = advListData.get(1);
                            item3 = advListData.get(2);

                            if(!isAnuationStaring){
//                                Log.d("lizisong", "isAnuationStaring："+isAnuationStaring);
                                ImageLoader.getInstance().displayImage(item1.imgUrl
                                        , holdview.tupian1, options);
                                ImageLoader.getInstance().displayImage(item2.imgUrl
                                        , holdview.tupian2, options);
                                ImageLoader.getInstance().displayImage(item3.imgUrl
                                        , holdview.tupian3, options);
                                }
    //                    }
//                            holdview.tupian1.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    ADS ads = item1;
//                                    handlernewclick(ads);
//
//                                }
//                            });
                            holdview.tupian1.setClickable(true);
                            holdview.tupian1.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                                @Override
                                public void oneClick() {
                                    ADS ads = item1;
                                    handlernewclick(ads);
                                }

                                @Override
                                public void doubleClick() {

                                }
                            }));
                            holdview.tupian2.setClickable(true);
                            holdview.tupian2.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                                @Override
                                public void oneClick() {
                                    ADS ads = item2;
                                    handlernewclick(ads);
                                }

                                @Override
                                public void doubleClick() {

                                }
                            }));
//                            holdview.tupian2.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//
//                                }
//                            });
                            holdview.tupian3.setClickable(true);
                            holdview.tupian3.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
                                @Override
                                public void oneClick() {
                                    ADS ads = item3;
                                    handlernewclick(ads);
                                }

                                @Override
                                public void doubleClick() {

                                }
                            }));
//                            holdview.tupian3.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
                        }else if(advListData.size() == 2){
                            holdview.san.setVisibility(View.GONE);
                            holdview.yi.setVisibility(View.GONE);
                            holdview.er.setVisibility(View.VISIBLE);
                            item1 = advListData.get(0);
                            item2 = advListData.get(1);
                            if(!isAnuationStaring){
//                                Log.d("lizisong", "isAnuationStaring："+isAnuationStaring);
                                ImageLoader.getInstance().displayImage(item1.imgUrl
                                        , holdview.er1, options);
                                ImageLoader.getInstance().displayImage(item2.imgUrl
                                        , holdview.er2, options);

                            }
                            //                    }
                            holdview.er1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ADS ads = item1;
                                    handlernewclick(ads);

                                }
                            });
                            holdview.er2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ADS ads = item2;
                                    handlernewclick(ads);

                                }
                            });

                        }else if(advListData.size() == 1){
                            holdview.san.setVisibility(View.GONE);
                            holdview.yi.setVisibility(View.VISIBLE);
                            holdview.er.setVisibility(View.GONE);
                            item1 = advListData.get(0);
                            if(!isAnuationStaring){
                                ImageLoader.getInstance().displayImage(item1.imgUrl
                                        , holdview.yi1, options);
                            }
                            holdview.yi1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ADS ads = item1;
                                    handlernewclick(ads);
                                }
                            });
                        }


                    } else if (item.typename.equals("xiangmu")) {
                        holdview.xiangmu.setVisibility(View.VISIBLE);
                        holdview.zhuanjia.setVisibility(View.GONE);
                        holdview.hangye.setVisibility(View.GONE);
                        holdview.zhuanli.setVisibility(View.GONE);
                        holdview.qingbaoitem.setVisibility(View.GONE);
                        holdview.xianxiafuwu.setVisibility(View.GONE);
                        holdview.santuu.setVisibility(View.GONE);
                        int count = item.xiangmu.size();
                        PostData xiangmu1 = null, xiangmu2 = null, xiangmu3 = null;
                        if (count == 1) {
                            holdview.xiangmu_lay1.setVisibility(View.VISIBLE);
                            holdview.xiangmu_line1.setVisibility(View.VISIBLE);
                            holdview.xiangmu_lay2.setVisibility(View.GONE);
                            holdview.xiangmu_line2.setVisibility(View.GONE);
                            holdview.xiangmu_lay3.setVisibility(View.GONE);
                            holdview.xiangmu_line3.setVisibility(View.GONE);
                            xiangmu1 = item.xiangmu.get(0);
                                boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                                if (state) {
                                    NetUtils.NetType type = NetUtils.getNetType();
                                    if (type == NetUtils.NetType.NET_WIFI && xiangmu1.litpic != null && !xiangmu1.litpic.equals("")) {
                                        if(!isAnuationStaring){
                                        ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                                , holdview.xianmgmu1, options);
                                        }

                                    } else {
                                        holdview.xianmgmu1.setBackgroundResource(R.mipmap.information_placeholder);
                                    }
                                } else {
                                    if(!isAnuationStaring){
                                        ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                            , holdview.xianmgmu1, options);
                                    }
                                }

                            holdview.xmtitle1.setText(xiangmu1.title);
                            holdview.lanyuan1.setText(xiangmu1.area_cate);
                            if(xiangmu1.labels != null && (!xiangmu1.labels.equals(""))){
                                holdview.source1.setVisibility(View.VISIBLE);
                                holdview.source1.setText(xiangmu1.labels);
                                if(xiangmu1.labels.equals("精品项目")){
                                    holdview.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                                }else if(xiangmu1.labels.equals("钛领推荐")){
                                    holdview.source1.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                                }else if(xiangmu1.labels.equals("国家科学基金")){
                                    holdview.source1.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                                }else{
                                    holdview.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                                }
                            }else{
                                holdview.source1.setVisibility(View.GONE);
                            }

                        } else if (count == 2) {
                            xiangmu1 = item.xiangmu.get(0);
                            xiangmu2 = item.xiangmu.get(1);
                            holdview.xiangmu_lay1.setVisibility(View.VISIBLE);
                            holdview.xiangmu_line1.setVisibility(View.VISIBLE);
                            holdview.xiangmu_lay2.setVisibility(View.VISIBLE);
                            holdview.xiangmu_line2.setVisibility(View.VISIBLE);
                            holdview.xiangmu_lay3.setVisibility(View.GONE);
                            holdview.xiangmu_line3.setVisibility(View.GONE);
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);

                                if (state) {
                                    NetUtils.NetType type = NetUtils.getNetType();
                                    if (type == NetUtils.NetType.NET_WIFI && xiangmu1.litpic != null && !xiangmu1.litpic.equals("")) {
                                        if(!isAnuationStaring){
                                        ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                                , holdview.xianmgmu1, options);
                                        }

                                    } else {
                                        holdview.xianmgmu1.setBackgroundResource(R.mipmap.information_placeholder);
                                    }
                                } else {
                                    if(!isAnuationStaring){
                                    ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                            , holdview.xianmgmu1, options);
                                    }
                                }

                            holdview.xmtitle1.setText(xiangmu1.title);
                            holdview.lanyuan1.setText(xiangmu1.area_cate);
                            if(xiangmu1.labels != null && (!xiangmu1.labels.equals(""))){
                                holdview.source1.setVisibility(View.VISIBLE);
                                holdview.source1.setText(xiangmu1.labels);
                                if(xiangmu1.labels.equals("精品项目")){
                                    holdview.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                                }else if(xiangmu1.labels.equals("钛领推荐")){
                                    holdview.source1.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                                }else if(xiangmu1.labels.equals("国家科学基金")){
                                    holdview.source1.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                                }else{
                                    holdview.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                                }
                            }else{
                                holdview.source1.setVisibility(View.GONE);
                            }
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI && xiangmu2.litpic != null && !xiangmu2.litpic.equals("")) {
                                    if(!isAnuationStaring){
                                        ImageLoader.getInstance().displayImage(xiangmu2.litpic
                                                , holdview.xianmgmu2, options);
                                    }

                                } else {
                                    holdview.xianmgmu2.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                if(!isAnuationStaring){
                                ImageLoader.getInstance().displayImage(xiangmu2.litpic
                                        , holdview.xianmgmu2, options);
                                }
                            }
                            holdview.xmtitle2.setText(xiangmu2.title);
                            holdview.lanyuan2.setText(xiangmu2.area_cate);
                            if(xiangmu2.labels != null && (!xiangmu2.labels.equals(""))){
                                holdview.source2.setVisibility(View.VISIBLE);
                                holdview.source2.setText(xiangmu2.labels);
                                if(xiangmu2.labels.equals("精品项目")){
                                    holdview.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                                }else if(xiangmu2.labels.equals("钛领推荐")){
                                    holdview.source2.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                                }else if(xiangmu2.labels.equals("国家科学基金")){
                                    holdview.source2.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                                }else{
                                    holdview.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                                }
                            }else{
                                holdview.source2.setVisibility(View.GONE);
                            }


                        } else if (count >= 3) {
                            xiangmu1 = item.xiangmu.get(0);
                            xiangmu2 = item.xiangmu.get(1);
                            xiangmu3 = item.xiangmu.get(2);
                            holdview.xiangmu_lay1.setVisibility(View.VISIBLE);
                            holdview.xiangmu_line1.setVisibility(View.VISIBLE);
                            holdview.xiangmu_lay2.setVisibility(View.VISIBLE);
                            holdview.xiangmu_line2.setVisibility(View.VISIBLE);
                            holdview.xiangmu_lay3.setVisibility(View.VISIBLE);
                            holdview.xiangmu_line3.setVisibility(View.VISIBLE);

                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);

                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI && xiangmu1.litpic != null && !xiangmu1.litpic.equals("")) {
                                    if(!isAnuationStaring){
                                    ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                            , holdview.xianmgmu1, options);
                                    }

                                } else {
                                    holdview.xianmgmu1.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                if(!isAnuationStaring){
                                ImageLoader.getInstance().displayImage(xiangmu1.litpic
                                        , holdview.xianmgmu1, options);
                                }
                            }
                            holdview.xmtitle1.setText(xiangmu1.title);
                            holdview.lanyuan1.setText(xiangmu1.area_cate);
                            if(xiangmu1.labels != null && (!xiangmu1.labels.equals(""))){
                                holdview.source1.setVisibility(View.VISIBLE);
                                holdview.source1.setText(xiangmu1.labels);
                                if(xiangmu1.labels.equals("精品项目")){
                                    holdview.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                                }else if(xiangmu1.labels.equals("钛领推荐")){
                                    holdview.source1.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                                }else if(xiangmu1.labels.equals("国家科学基金")){
                                    holdview.source1.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                                }else{
                                    holdview.source1.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                                }
                            }else{
                                holdview.source1.setVisibility(View.GONE);
                            }

                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI && xiangmu2.litpic != null && !xiangmu2.litpic.equals("")) {
                                    if(!isAnuationStaring){
                                    ImageLoader.getInstance().displayImage(xiangmu2.litpic
                                            , holdview.xianmgmu2, options);
                                    }

                                } else {
                                    holdview.xianmgmu2.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                if(!isAnuationStaring){
                                ImageLoader.getInstance().displayImage(xiangmu2.litpic
                                        , holdview.xianmgmu2, options);
                                }
                            }


                            holdview.xmtitle2.setText(xiangmu2.title);
                            holdview.lanyuan2.setText(xiangmu2.area_cate);
                            if(xiangmu2.labels != null && (!xiangmu2.labels.equals(""))){
                                holdview.source2.setVisibility(View.VISIBLE);
                                holdview.source2.setText(xiangmu2.labels);
                                if(xiangmu2.labels.equals("精品项目")){
                                    holdview.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                                }else if(xiangmu2.labels.equals("钛领推荐")){
                                    holdview.source2.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                                }else if(xiangmu2.labels.equals("国家科学基金")){
                                    holdview.source2.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                                }else{
                                    holdview.source2.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                                }
                            }else{
                                holdview.source2.setVisibility(View.GONE);
                            }
                            if(!isAnuationStaring){
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI && xiangmu3.litpic != null && !xiangmu3.litpic.equals("")) {
                                    if(!isAnuationStaring){
                                    ImageLoader.getInstance().displayImage(xiangmu3.litpic
                                            , holdview.xianmgmu3, options);
                                    }

                                } else {
                                    holdview.xianmgmu3.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                if(!isAnuationStaring){
                                ImageLoader.getInstance().displayImage(xiangmu3.litpic
                                        , holdview.xianmgmu3, options);
                                }
                            }
                            }
                            holdview.xmtitle3.setText(xiangmu3.title);
                            holdview.lanyuan3.setText(xiangmu3.area_cate);
                            if(xiangmu3.labels != null && (!xiangmu3.labels.equals(""))){
                                holdview.source3.setVisibility(View.VISIBLE);
                                holdview.source3.setText(xiangmu3.labels);
                                if(xiangmu3.labels.equals("精品项目")){
                                    holdview.source3.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                                }else if(xiangmu3.labels.equals("钛领推荐")){
                                    holdview.source3.setBackgroundResource(R.drawable.shape_draw_maidiantuijian);
                                }else if(xiangmu3.labels.equals("国家科学基金")){
                                    holdview.source3.setBackgroundResource(R.drawable.shape_draw_guojiakexuejijin);
                                }else{
                                    holdview.source3.setBackgroundResource(R.drawable.shape_draw_jingpinxiangmu);
                                }
                            }else{
                                holdview.source3.setVisibility(View.GONE);
                            }


                        }
                        holdview.xiangmu_lay1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    String labels = item.xiangmu.get(0).labels;
                                    if(labels != null){
                                        if(labels.contains("精品项目") || (item.xiangmu.get(0).typeid.equals("2") && item.xiangmu.get(0).typename.equals("html"))){
                                            Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                            intent.putExtra("title", item.xiangmu.get(0).title);
                                            intent.putExtra("url", item.xiangmu.get(0).url);
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                            intent.putExtra("aid", item.xiangmu.get(0).id);
                                            startActivity(intent);
                                        }
                                    }else {
                                        if((item.xiangmu.get(0).typeid.equals("2") && item.xiangmu.get(0).typename.equals("html"))){
                                            Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                            intent.putExtra("title", item.xiangmu.get(0).title);
                                            intent.putExtra("url", item.xiangmu.get(0).url);
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                            intent.putExtra("aid", item.xiangmu.get(0).id);
                                            startActivity(intent);
                                        }
                                    }
                                } catch (IndexOutOfBoundsException ex) {

                                } catch (Exception e) {

                                }

                            }
                        });
                        holdview.xiangmu_lay2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    String labels = item.xiangmu.get(1).labels;
                                    if(labels != null){
                                        if(labels.contains("精品项目")|| (item.xiangmu.get(1).typeid.equals("2") && item.xiangmu.get(1).typename.equals("html"))){
                                            Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                            intent.putExtra("title", item.xiangmu.get(1).title);
                                            intent.putExtra("url", item.xiangmu.get(1).url);
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                            intent.putExtra("aid", item.xiangmu.get(1).id);
                                            startActivity(intent);
                                        }
                                    }else {
                                        if((item.xiangmu.get(1).typeid.equals("2") && item.xiangmu.get(1).typename.equals("html"))){
                                            Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                            intent.putExtra("title", item.xiangmu.get(1).title);
                                            intent.putExtra("url", item.xiangmu.get(1).url);
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                            intent.putExtra("aid", item.xiangmu.get(1).id);
                                            startActivity(intent);
                                        }
                                    }

                                } catch (IndexOutOfBoundsException ex) {

                                } catch (Exception e) {

                                }

                            }
                        });
                        holdview.xiangmu_lay3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    String labels = item.xiangmu.get(2).labels;
                                    if(labels != null){
                                        if(labels.contains("精品项目") || (item.xiangmu.get(2).typeid.equals("2") && item.xiangmu.get(2).typename.equals("html"))){
                                            Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                            intent.putExtra("title", item.xiangmu.get(2).title);
                                            intent.putExtra("url", item.xiangmu.get(2).url);
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                            intent.putExtra("aid", item.xiangmu.get(2).id);
                                            startActivity(intent);
                                        }
                                    }else {
                                        if((item.xiangmu.get(2).typeid.equals("2") && item.xiangmu.get(2).typename.equals("html"))){
                                            Intent intent=new Intent(getActivity(), ActiveActivity.class);
                                            intent.putExtra("title", item.xiangmu.get(2).title);
                                            intent.putExtra("url", item.xiangmu.get(2).url);
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                            intent.putExtra("aid", item.xiangmu.get(2).id);
                                            startActivity(intent);
                                        }
                                    }
                                } catch (IndexOutOfBoundsException ex) {

                                } catch (Exception e) {

                                }



                            }
                        });
                        holdview.xiangmu_change.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progress.setVisibility(View.VISIBLE);
                                sortid = item.xiangmu.get(item.xiangmu.size() - 1).id;
                                ChangeXumu();
                            }
                        });
                        holdview.xm_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), NewProJect.class);
                                startActivity(intent);
//                                Intent intent = new Intent(getActivity(), LookMoreActivity.class);
//                                intent.putExtra("typeid", 2);
//                                intent.putExtra("title", "更多项目");
//                                intent.putExtra("channel", "项目");
//                                startActivity(intent);
                            }
                        });
                    } else if (item.typename.equals("rencai")) {
                        holdview.xiangmu.setVisibility(View.GONE);
                        holdview.zhuanjia.setVisibility(View.VISIBLE);
                        holdview.qingbaoitem.setVisibility(View.GONE);
                        holdview.hangye.setVisibility(View.GONE);
                        holdview.zhuanli.setVisibility(View.GONE);
                        holdview.xianxiafuwu.setVisibility(View.GONE);
                        holdview.santuu.setVisibility(View.GONE);

                        int count = item.rencai.size();
                        PostData rencai1, rencai2, rencai3, rencai4;
                        if (count == 1) {
                            rencai1 = item.rencai.get(0);
                            holdview.rencai_lay1.setVisibility(View.VISIBLE);
                            holdview.rencai_line1.setVisibility(View.VISIBLE);
                            holdview.rencai_lay2.setVisibility(View.GONE);
                            holdview.rencai_line2.setVisibility(View.GONE);
                            holdview.rencai_lay3.setVisibility(View.GONE);
                            holdview.rencai_line3.setVisibility(View.GONE);
                            holdview.rencai_lay4.setVisibility(View.GONE);
                            holdview.rencai_line4.setVisibility(View.GONE);
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);

                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI && rencai1.litpic != null && !rencai1.litpic.equals("")) {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai1.litpic
                                            , holdview.rencai_img1, options);}

                                } else {
                                    holdview.rencai_img1.setBackgroundResource(R.mipmap.information_placeholder);
                                }
                            } else {
                                if (rencai1.litpic == null || rencai1.litpic.equals("")) {
                                    holdview.rencai_img1.setImageResource(R.mipmap.touxiangzhanwei);
                                } else {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai1.litpic
                                            , holdview.rencai_img1, options);
                                    }
                                }

                            }


                            holdview.rencai_title1.setText(rencai1.title);
                            holdview.rencai_lingyu1.setText(rencai1.unit);
                            holdview.rank1.setText(rencai1.rank);


                        } else if (count == 2) {
                            rencai1 = item.rencai.get(0);
                            rencai2 = item.rencai.get(1);
                            holdview.rencai_lay1.setVisibility(View.VISIBLE);
                            holdview.rencai_line1.setVisibility(View.VISIBLE);
                            holdview.rencai_lay2.setVisibility(View.VISIBLE);
                            holdview.rencai_line2.setVisibility(View.VISIBLE);
                            holdview.rencai_lay3.setVisibility(View.GONE);
                            holdview.rencai_line3.setVisibility(View.GONE);
                            holdview.rencai_lay4.setVisibility(View.GONE);
                            holdview.rencai_line4.setVisibility(View.GONE);
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);

                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI && rencai1.litpic != null && !rencai1.litpic.equals("")) {
                                    if(true){
                                     ImageLoader.getInstance().displayImage(rencai1.litpic
                                            , holdview.rencai_img1, options);
                                    }

                                } else {
                                    holdview.rencai_img1.setBackgroundResource(R.mipmap.touxiangzhanwei);
                                }
                            } else {
                                if (rencai1.litpic == null || rencai1.litpic.equals("")) {
                                    holdview.rencai_img1.setImageResource(R.mipmap.touxiangzhanwei);
                                } else {
                                    if(true){
                                        ImageLoader.getInstance().displayImage(rencai1.litpic
                                                , holdview.rencai_img1, options);
                                    }
                                }

                            }
                            holdview.rencai_title1.setText(rencai1.title);
                            holdview.rencai_lingyu1.setText(rencai1.unit);
                            holdview.rank1.setText(rencai1.rank);


                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI && rencai2.litpic != null && !rencai2.litpic.equals("")) {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai2.litpic
                                            , holdview.rencai_img2, options);
                                    }

                                } else {

                                    holdview.rencai_img2.setBackgroundResource(R.mipmap.touxiangzhanwei);
                                }
                            } else {
                                if (rencai2.litpic == null || rencai2.litpic.equals("")) {
                                    holdview.rencai_img2.setImageResource(R.mipmap.touxiangzhanwei);
                                } else {
                                    if(true){
                                        ImageLoader.getInstance().displayImage(rencai2.litpic
                                                , holdview.rencai_img2, options);
                                    }
                                }
                            }
                            holdview.rencai_title2.setText(rencai2.title);
                            holdview.rencai_lingyu2.setText(rencai2.unit);
                            holdview.rank2.setText(rencai2.rank);


                        } else if (count == 3) {
                            rencai1 = item.rencai.get(0);
                            rencai2 = item.rencai.get(1);
                            rencai3 = item.rencai.get(2);
                            holdview.rencai_lay1.setVisibility(View.VISIBLE);
                            holdview.rencai_line1.setVisibility(View.VISIBLE);
                            holdview.rencai_lay2.setVisibility(View.VISIBLE);
                            holdview.rencai_line2.setVisibility(View.VISIBLE);
                            holdview.rencai_lay3.setVisibility(View.VISIBLE);
                            holdview.rencai_line3.setVisibility(View.VISIBLE);
                            holdview.rencai_lay4.setVisibility(View.GONE);
                            holdview.rencai_line4.setVisibility(View.GONE);

                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);

                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI && rencai1.litpic != null && !rencai1.litpic.equals("")) {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai1.litpic
                                            , holdview.rencai_img1, options);
                                    }

                                } else {
                                    holdview.rencai_img1.setBackgroundResource(R.mipmap.touxiangzhanwei);
                                }
                            } else {
                                if (rencai1.litpic == null || rencai1.litpic.equals("")) {
                                    holdview.rencai_img1.setImageResource(R.mipmap.touxiangzhanwei);
                                } else {
                                    if(true){
                                        ImageLoader.getInstance().displayImage(rencai1.litpic
                                                , holdview.rencai_img1, options);
                                    }
                                }
                            }
                            holdview.rencai_title1.setText(rencai1.title);
                            holdview.rencai_lingyu1.setText(rencai1.unit);
                            holdview.rank1.setText(rencai1.rank);


                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI && rencai2.litpic != null && !rencai2.litpic.equals("")) {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai2.litpic
                                            , holdview.rencai_img2, options);}

                                } else {
                                    holdview.rencai_img2.setBackgroundResource(R.mipmap.touxiangzhanwei);
                                }
                            } else {
                                if (rencai2.litpic == null || rencai2.litpic.equals("")) {
                                    holdview.rencai_img2.setImageResource(R.mipmap.touxiangzhanwei);
                                } else {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai2.litpic
                                            , holdview.rencai_img2, options);}
                                }
                            }
                            holdview.rencai_title2.setText(rencai2.title);
                            holdview.rencai_lingyu2.setText(rencai2.unit);
                            holdview.rank2.setText(rencai2.rank);

                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI && rencai3.litpic != null && !rencai3.litpic.equals("")) {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai3.litpic
                                            , holdview.rencai_img3, options);
                                    }

                                } else {
                                    holdview.rencai_img3.setBackgroundResource(R.mipmap.touxiangzhanwei);
                                }
                            } else {
                                if (rencai3.litpic == null || rencai3.litpic.equals("")) {
                                    holdview.rencai_img3.setImageResource(R.mipmap.touxiangzhanwei);
                                } else {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai3.litpic
                                            , holdview.rencai_img3, options);}
                                }
                            }
                            holdview.rencai_title3.setText(rencai3.title);
                            holdview.rencai_lingyu3.setText( rencai3.unit);
                            holdview.rank3.setText(rencai3.rank);


                        } else if (count >= 4) {
                            rencai1 = item.rencai.get(0);
                            rencai2 = item.rencai.get(1);
                            rencai3 = item.rencai.get(2);
                            rencai4 = item.rencai.get(3);

                            holdview.rencai_lay1.setVisibility(View.VISIBLE);
                            holdview.rencai_line1.setVisibility(View.VISIBLE);
                            holdview.rencai_lay2.setVisibility(View.VISIBLE);
                            holdview.rencai_line2.setVisibility(View.VISIBLE);
                            holdview.rencai_lay3.setVisibility(View.VISIBLE);
                            holdview.rencai_line3.setVisibility(View.VISIBLE);
                            holdview.rencai_lay4.setVisibility(View.VISIBLE);
                            holdview.rencai_line4.setVisibility(View.VISIBLE);
                            boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI && rencai1.litpic != null && !rencai1.litpic.equals("")) {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai1.litpic
                                            , holdview.rencai_img1, options);}

                                } else {
                                    holdview.rencai_img1.setBackgroundResource(R.mipmap.touxiangzhanwei);
                                }
                            } else {
                                if (rencai1.litpic == null || rencai1.litpic.equals("")) {
                                    holdview.rencai_img1.setImageResource(R.mipmap.touxiangzhanwei);
                                } else {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai1.litpic
                                            , holdview.rencai_img1, options);}
                                }
                            }
                            holdview.rencai_title1.setText(rencai1.title);
                            holdview.rencai_lingyu1.setText(rencai1.unit);
                            holdview.rank1.setText(rencai1.rank);


                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI && rencai2.litpic != null && !rencai2.litpic.equals("")) {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai2.litpic
                                            , holdview.rencai_img2, options);}

                                } else {
                                    holdview.rencai_img2.setBackgroundResource(R.mipmap.touxiangzhanwei);
                                }
                            } else {
                                if (rencai2.litpic == null || rencai2.litpic.equals("")) {
                                    holdview.rencai_img2.setImageResource(R.mipmap.touxiangzhanwei);
                                } else {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai2.litpic
                                            , holdview.rencai_img2, options);}
                                }
                            }
                            holdview.rencai_title2.setText(rencai2.title);
                            holdview.rencai_lingyu2.setText(rencai2.unit);
                            holdview.rank2.setText(rencai2.rank);

                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI && rencai3.litpic != null && !rencai3.litpic.equals("")) {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai3.litpic
                                            , holdview.rencai_img3, options);}

                                } else {
                                    holdview.rencai_img3.setBackgroundResource(R.mipmap.touxiangzhanwei);
                                }
                            } else {
                                if (rencai3.litpic == null || rencai3.litpic.equals("")) {
                                    holdview.rencai_img3.setImageResource(R.mipmap.touxiangzhanwei);
                                } else {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai3.litpic
                                            , holdview.rencai_img3, options);}
                                }
                            }
                            holdview.rencai_title3.setText(rencai3.title);
                            holdview.rencai_lingyu3.setText(rencai3.unit);
                            holdview.rank3.setText(rencai3.rank);

                            if (state) {
                                NetUtils.NetType type = NetUtils.getNetType();
                                if (type == NetUtils.NetType.NET_WIFI && rencai4.litpic != null && !rencai4.litpic.equals("")) {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai4.litpic
                                            , holdview.rencai_img4, options);}

                                } else {
                                    holdview.rencai_img4.setBackgroundResource(R.mipmap.touxiangzhanwei);
                                }
                            } else {
                                if (rencai4.litpic == null || rencai4.litpic.equals("")) {
                                    holdview.rencai_img4.setImageResource(R.mipmap.touxiangzhanwei);
                                } else {
                                    if(true){
                                    ImageLoader.getInstance().displayImage(rencai4.litpic
                                            , holdview.rencai_img4, options);}
                                }
                            }
                            holdview.rencai_title4.setText(rencai4.title);
                            holdview.rencai_lingyu4.setText(rencai4.unit);
                            holdview.rank4.setText(rencai4.rank);

                        }
                        holdview.rencai_lay1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                                intent.putExtra("id", item.rencai.get(0).id);
//                                intent.putExtra("name", item.rencai.get(0).typename);
//                                intent.putExtra("pic", item.rencai.get(0).litpic);
//                                startActivity(intent);
                                Intent intent = new Intent(getActivity(), XinFanAnCeShi.class);
                                intent.putExtra("aid", item.rencai.get(0).id);
                                startActivity(intent);
                            }
                        });
                        holdview.rencai_lay2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                                intent.putExtra("id", item.rencai.get(1).id);
//                                intent.putExtra("name", item.rencai.get(1).typename);
//                                intent.putExtra("pic", item.rencai.get(1).litpic);
//                                startActivity(intent);
                                Intent intent = new Intent(getActivity(), XinFanAnCeShi.class);
                                intent.putExtra("aid", item.rencai.get(1).id);
                                startActivity(intent);
                            }
                        });
                        holdview.rencai_lay3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                                intent.putExtra("id", item.rencai.get(2).id);
//                                intent.putExtra("name", item.rencai.get(2).typename);
//                                intent.putExtra("pic", item.rencai.get(2).litpic);
//                                startActivity(intent);

                                Intent intent = new Intent(getActivity(), XinFanAnCeShi.class);
                                intent.putExtra("aid", item.rencai.get(2).id);
                                startActivity(intent);
                            }
                        });
                        holdview.rencai_lay4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                                intent.putExtra("id", item.rencai.get(3).id);
//                                intent.putExtra("name", item.rencai.get(3).typename);
//                                intent.putExtra("pic", item.rencai.get(3).litpic);
//                                startActivity(intent);

                                Intent intent = new Intent(getActivity(), XinFanAnCeShi.class);
                                intent.putExtra("aid", item.rencai.get(3).id);
                                startActivity(intent);
                            }
                        });

                        holdview.zhuangjia_change.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progress.setVisibility(View.VISIBLE);
                                sortid = item.rencai.get(item.rencai.size() - 1).id;
                                ChangeZhuanJia();
                            }
                        });
                        holdview.zhuanjia_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), LookMoreActivity.class);
                                intent.putExtra("typeid", 4);
                                intent.putExtra("title", "更多专家");
                                intent.putExtra("channel", "专家");
                                startActivity(intent);
                            }
                        });
                    } else if (item.typename.equals("qingbao")) {
                        final PostData qingbao = item.qingbao.get(0);
                        holdview.xiangmu.setVisibility(View.GONE);
                        holdview.qingbaoitem.setVisibility(View.VISIBLE);
                        holdview.zhuanjia.setVisibility(View.GONE);
                        holdview.hangye.setVisibility(View.VISIBLE);
                        holdview.zhuanli.setVisibility(View.GONE);
                        holdview.xianxiafuwu.setVisibility(View.GONE);
                        holdview.santuu.setVisibility(View.GONE);
                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && qingbao.telig_cover != null && !qingbao.telig_cover.equals("")) {
                                if(!isAnuationStaring){
                                ImageLoader.getInstance().displayImage(qingbao.telig_cover
                                        , holdview.qingicon, options);}

                            } else {
                                holdview.qingicon.setBackgroundResource(R.mipmap.information_placeholder);
                            }
                        } else {
                            if(!isAnuationStaring){
                            ImageLoader.getInstance().displayImage(qingbao.telig_cover
                                    , holdview.qingicon, options);}
                        }
                        holdview.title.setText(qingbao.telig_title);
                        holdview.description.setText(qingbao.telig_description);

                        if (qingbao.telig_type.equals("0")) {
                            holdview.type.setVisibility(View.GONE);
                        } else if (qingbao.telig_type.equals("2")) {
                            holdview.type.setVisibility(View.VISIBLE);
                            holdview.type.setBackgroundResource(R.mipmap.xianmian);
                        } else if (qingbao.telig_type.equals("1")) {
                            holdview.type.setVisibility(View.VISIBLE);
                            holdview.type.setBackgroundResource(R.mipmap.youhui);
                        }
                        holdview.updatadescription.setText(qingbao.telig_journal_title);//暂定
                        holdview.price.setText(qingbao.telig_price + "元/" + qingbao.telig_unit);
                        if (Float.parseFloat(qingbao.telig_price) == 0) {
                            holdview.price.setText("免费");
                        }
                        if (qingbao.telig_journal_pubdate != null && !qingbao.telig_journal_pubdate.equals("")) {
                            holdview.timeupdate.setText(TimeUtil.getTimeFormatText(new Date(Long.parseLong(qingbao.telig_journal_pubdate) * 1000), qingbao.telig_journal_pubdate) + "更新");
                        }

                        holdview.hangye_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), QingBaoActivity.class);
                                startActivity(intent);

                            }
                        });
                        holdview.hangye.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), QingBaoDeilActivity.class);
                                intent.putExtra("teligId", qingbao.telig_id);
                                intent.putExtra("telig_price", qingbao.telig_price);
                                intent.putExtra("telig_unit", qingbao.telig_unit);
                                intent.putExtra("telig_type", qingbao.telig_type);
                                startActivity(intent);

                            }
                        });
                    } else if (item.typename.equals("zhuanli")) {
                        PostData zhuanli1, zhuanli2, zhuanli3, zhuanli4;
                        holdview.xiangmu.setVisibility(View.GONE);
                        holdview.zhuanjia.setVisibility(View.GONE);
                        holdview.hangye.setVisibility(View.GONE);
                        holdview.zhuanli.setVisibility(View.VISIBLE);
                        holdview.xianxiafuwu.setVisibility(View.GONE);
                        holdview.qingbaoitem.setVisibility(View.GONE);
                        holdview.santuu.setVisibility(View.GONE);
                        int count = item.zhuanli.size();
                        if (count == 1) {
                            zhuanli1 = item.zhuanli.get(0);
                            holdview.zhuanli_lay1.setVisibility(View.VISIBLE);
                            holdview.zhuanli_line1.setVisibility(View.VISIBLE);
                            holdview.zhuanli_lay2.setVisibility(View.GONE);
                            holdview.zhuanli_line2.setVisibility(View.GONE);
                            holdview.zhuanli_lay3.setVisibility(View.GONE);
                            holdview.zhuanli_line3.setVisibility(View.GONE);
                            holdview.zhuanli_lay4.setVisibility(View.GONE);
                            holdview.zhuanli_line4.setVisibility(View.GONE);
                            holdview.zhuanli_title1.setText(zhuanli1.title);
                            holdview.zhuanli_lingyu1.setText("所属领域:" + zhuanli1.area_cate);

                        } else if (count == 2) {
                            zhuanli1 = item.zhuanli.get(0);
                            zhuanli2 = item.zhuanli.get(1);
                            holdview.zhuanli_lay1.setVisibility(View.VISIBLE);
                            holdview.zhuanli_line1.setVisibility(View.VISIBLE);
                            holdview.zhuanli_lay2.setVisibility(View.VISIBLE);
                            holdview.zhuanli_line2.setVisibility(View.VISIBLE);
                            holdview.zhuanli_lay3.setVisibility(View.GONE);
                            holdview.zhuanli_line3.setVisibility(View.GONE);
                            holdview.zhuanli_lay4.setVisibility(View.GONE);
                            holdview.zhuanli_line4.setVisibility(View.GONE);
                            holdview.zhuanli_title1.setText(zhuanli1.title);
                            holdview.zhuanli_lingyu1.setText("所属领域:" + zhuanli1.area_cate);
                            holdview.zhuanli_title2.setText(zhuanli2.title);
                            holdview.zhuanli_lingyu2.setText("所属领域:" + zhuanli2.area_cate);

                        } else if (count == 3) {
                            zhuanli1 = item.zhuanli.get(0);
                            zhuanli2 = item.zhuanli.get(1);
                            zhuanli3 = item.zhuanli.get(2);
                            holdview.zhuanli_lay1.setVisibility(View.VISIBLE);
                            holdview.zhuanli_line1.setVisibility(View.VISIBLE);
                            holdview.zhuanli_lay2.setVisibility(View.VISIBLE);
                            holdview.zhuanli_line2.setVisibility(View.VISIBLE);
                            holdview.zhuanli_lay3.setVisibility(View.VISIBLE);
                            holdview.zhuanli_line3.setVisibility(View.VISIBLE);
                            holdview.zhuanli_lay4.setVisibility(View.GONE);
                            holdview.zhuanli_line4.setVisibility(View.GONE);
                            holdview.zhuanli_title1.setText(zhuanli1.title);
                            holdview.zhuanli_lingyu1.setText("所属领域:" + zhuanli1.area_cate);
                            holdview.zhuanli_title2.setText(zhuanli2.title);
                            holdview.zhuanli_lingyu2.setText("所属领域:" + zhuanli2.area_cate);
                            holdview.zhuanli_title3.setText(zhuanli3.title);
                            holdview.zhuanli_lingyu3.setText("所属领域:" + zhuanli3.area_cate);

                        } else if (count >= 4) {
                            zhuanli1 = item.zhuanli.get(0);
                            zhuanli2 = item.zhuanli.get(1);
                            zhuanli3 = item.zhuanli.get(2);
                            zhuanli4 = item.zhuanli.get(3);

                            holdview.zhuanli_lay1.setVisibility(View.VISIBLE);
                            holdview.zhuanli_line1.setVisibility(View.VISIBLE);
                            holdview.zhuanli_lay2.setVisibility(View.VISIBLE);
                            holdview.zhuanli_line2.setVisibility(View.VISIBLE);
                            holdview.zhuanli_lay3.setVisibility(View.VISIBLE);
                            holdview.zhuanli_line3.setVisibility(View.VISIBLE);
                            holdview.zhuanli_lay4.setVisibility(View.VISIBLE);
                            holdview.zhuanli_line4.setVisibility(View.VISIBLE);
                            holdview.zhuanli_title1.setText(zhuanli1.title);
                            holdview.zhuanli_lingyu1.setText("所属领域:" + zhuanli1.area_cate);
                            holdview.zhuanli_title2.setText(zhuanli2.title);
                            holdview.zhuanli_lingyu2.setText("所属领域:" + zhuanli2.area_cate);
                            holdview.zhuanli_title3.setText(zhuanli3.title);
                            holdview.zhuanli_lingyu3.setText("所属领域:" + zhuanli3.area_cate);
                            holdview.zhuanli_title4.setText(zhuanli4.title);
                            holdview.zhuanli_lingyu4.setText("所属领域:" + zhuanli4.area_cate);
                        }
                        holdview.zhuanli_lay1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), NewZhuanliActivity.class);
                                intent.putExtra("aid", item.zhuanli.get(0).id);
                                startActivity(intent);

//                                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                                intent.putExtra("id", item.zhuanli.get(0).id);
//                                intent.putExtra("name", item.zhuanli.get(0).typename);
//                                intent.putExtra("pic", item.zhuanli.get(0).litpic);
//                                startActivity(intent);
                            }
                        });
                        holdview.zhuanli_lay2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), NewZhuanliActivity.class);
                                intent.putExtra("aid", item.zhuanli.get(1).id);
                                startActivity(intent);
//                                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                                intent.putExtra("id", item.zhuanli.get(1).id);
//                                intent.putExtra("name", item.zhuanli.get(1).typename);
//                                intent.putExtra("pic", item.zhuanli.get(1).litpic);
//                                startActivity(intent);
                            }
                        });
                        holdview.zhuanli_lay3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), NewZhuanliActivity.class);
                                intent.putExtra("aid", item.zhuanli.get(2).id);
                                startActivity(intent);
//                                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                                intent.putExtra("id", item.zhuanli.get(2).id);
//                                intent.putExtra("name", item.zhuanli.get(2).typename);
//                                intent.putExtra("pic", item.zhuanli.get(2).litpic);
//                                startActivity(intent);
                            }
                        });
                        holdview.zhuanli_lay4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), NewZhuanliActivity.class);
                                intent.putExtra("aid", item.zhuanli.get(3).id);
                                startActivity(intent);
//                                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                                intent.putExtra("id", item.zhuanli.get(3).id);
//                                intent.putExtra("name", item.zhuanli.get(3).typename);
//                                intent.putExtra("pic", item.zhuanli.get(3).litpic);
//                                startActivity(intent);
                            }
                        });
                        holdview.zhuanli_change.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progress.setVisibility(View.VISIBLE);
                                sortid = item.zhuanli.get(item.zhuanli.size() - 1).id;
                                ChangeZhuanli();
                            }
                        });
                        holdview.zhuanli_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), LookMoreActivity.class);
                                intent.putExtra("typeid", 5);
                                intent.putExtra("title", "更多专利");
                                intent.putExtra("channel", "专利");
                                startActivity(intent);
                            }
                        });

                    } else if (item.typename.equals("offline")) {
                        holdview.xiangmu.setVisibility(View.GONE);
                        holdview.zhuanjia.setVisibility(View.GONE);
                        holdview.hangye.setVisibility(View.GONE);
                        holdview.zhuanli.setVisibility(View.GONE);
                        holdview.qingbaoitem.setVisibility(View.GONE);
                        holdview.xianxiafuwu.setVisibility(View.VISIBLE);
                        holdview.santuu.setVisibility(View.GONE);
                        final PostData offline1, offline2;
                        offline1 = item.offline.get(0);
                        offline2 = item.offline.get(1);

                        boolean state = SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.WIFI_DOWN_STATE, false);
                        if (state) {
                            NetUtils.NetType type = NetUtils.getNetType();
                            if (type == NetUtils.NetType.NET_WIFI && offline1.litpic != null && !offline1.litpic.equals("")) {
//                                if(!isAnuationStaring){
                                ImageLoader.getInstance().displayImage(offline1.litpic
                                        , holdview.icon_left, options);
                                ImageLoader.getInstance().displayImage(offline2.litpic
                                        , holdview.icon_right, options);}

//                            } else {
//                                holdview.icon_left.setBackgroundResource(R.mipmap.information_placeholder);
//                                holdview.icon_right.setBackgroundResource(R.mipmap.information_placeholder);
//                            }
                        } else {
//                            if(!isAnuationStaring){
                            ImageLoader.getInstance().displayImage(offline1.litpic
                                    , holdview.icon_left, options);
                            ImageLoader.getInstance().displayImage(offline2.litpic
                                    , holdview.icon_right, options);}
//                        }
                        holdview.icon_left.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), ActiveActivity.class);
                                intent.putExtra("url", offline1.url);
                                startActivity(intent);
                            }
                        });

                        holdview.icon_right.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), ActiveActivity.class);
                                intent.putExtra("url", offline2.url);
                                startActivity(intent);
                            }
                        });
                    }

            }catch (Exception e){

            }
            return convertView;
        }
        class HoldView{
            //<!-- 新的首页-->
            public LinearLayout tianxuqiu;

            //滚动
            public ScrollBanner scrollBanner;
            //项目
            public LinearLayout xiangmu;
            public RelativeLayout xiangmu_lay1,xiangmu_lay2,xiangmu_lay3;
            public TextView xiangmu_line1,xiangmu_line2,xiangmu_line3;
            public TextView xiangmu_title;
            public TextView xiangmu_change;
            public ImageView xianmgmu1,xianmgmu2,xianmgmu3;
            public TextView xmtitle1,xmtitle2,xmtitle3;
            public TextView lanyuan1,lanyuan2,lanyuan3;
            public TextView xm_more;
            public TextView source1,source2,source3;
            //专家推荐
            public LinearLayout zhuanjia;
            public RelativeLayout rencai_lay1,rencai_lay2,rencai_lay3,rencai_lay4;
            public TextView rencai_line1,rencai_line2,rencai_line3,rencai_line4;
            public TextView zhuangjia_change;
            public RoundImageView rencai_img1,rencai_img2,rencai_img3,rencai_img4;
            public TextView rencai_title1,rencai_title2,rencai_title3,rencai_title4;
            public TextView rencai_lingyu1,rencai_lingyu2,rencai_lingyu3,rencai_lingyu4;
            public TextView rank1,rank2,rank3,rank4;
            public TextView zhuanjia_more;
            //行业解析
            public LinearLayout hangye;
            public RelativeLayout qingbaoitem;
            public TextView hangye_title,hangye_more;
            public ImageView qingicon,type;
            public TextView title;
            public TextView description,timeupdate,price,updatadescription;
            //专利推荐
            public LinearLayout zhuanli;
            public RelativeLayout zhuanli_lay1,zhuanli_lay2,zhuanli_lay3,zhuanli_lay4;
            public TextView zhuanli_line1,zhuanli_line2,zhuanli_line3,zhuanli_line4;
            public TextView zhuanli_title,zhuanli_change;
            public TextView zhuanli_title1,zhuanli_title2,zhuanli_title3,zhuanli_title4;
            public TextView zhuanli_lingyu1,zhuanli_lingyu2,zhuanli_lingyu3,zhuanli_lingyu4,zhuanli_more;
            //线下服务
            public LinearLayout xianxiafuwu;
            public ImageView icon_left,icon_right;
            //三张图
            public LinearLayout santuu,san,er,yi;
            public ZQImageViewRoundOval tupian1,tupian2,tupian3,er1,er2, yi1;


        }
    }

    private void ChangeXumu(){
        String url ="http://"+ MyApplication.ip+"/api/getHomeRecommendData.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize", "3");
        map.put("typeid", "2");
        map.put("tag", "1");
        map.put("sortid", sortid);
        networkCom.getJson(url,map,handler,3,0);

    }
    private void ChangeZhuanJia(){
        String url ="http://"+MyApplication.ip+"/api/getHomeRecommendData.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize", "4");
        map.put("typeid", "4");
        map.put("tag", "1");
        map.put("sortid", sortid);
        networkCom.getJson(url,map,handler,4,0);
    }

    private void ChangeZhuanli(){
        String url = "http://"+ MyApplication.ip+"/api/getHomeRecommendData.php";
        HashMap<String, String> map = new HashMap<>();
        map.put("pageSize","4");
        map.put("typeid","5");
        map.put("tag","1");
        map.put("sortid",sortid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,5,0);
    }


    class MyAdapter extends PagerAdapter{

        View view =null;
        WeakReference<View> weakReference;
        LinkedList<View> mCaches = new LinkedList<View>();

        @Override
        public int getCount() {
            if(adsListData.size() == 1){
                return 1;
            }
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View converView = null;
            try{
                int index =0;
                if(adsListData.size() == 0){
                    index = 0;
                }else{
                    index = position % adsListData.size();
                }
                final ADS ads = adsListData.get(index);
                ViewHolder mHolder = null;
                if(mCaches.size() == 0){
                    converView = View.inflate(getActivity(),R.layout.newquanguoquanguo, null);
                    mHolder = new ViewHolder();
                    mHolder.imageView = (ZQImageViewRoundOval) converView.findViewById(R.id.item);
                    mHolder.imageView.setType(ZQImageViewRoundOval.TYPE_ROUND);
                    mHolder.imageView.setRoundRadius(25);
                    ViewGroup.LayoutParams lp = mHolder.imageView.getLayoutParams();
                    lp.height = heiht;
                    Log.d("lizisong", "heiht:"+heiht);
                    mHolder.imageView.setLayoutParams(lp);
                    converView.setTag(mHolder);
                }else{
                    converView = (View)mCaches.removeFirst();
                    mHolder = (ViewHolder)converView.getTag();
                }
                ImageLoader.getInstance().displayImage(ads.imgUrl
                        ,mHolder.imageView , options);
                mHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handlernewclick(ads);
                    }

                });
//                mHolder.imageView.setOnClickListener(new NoDoubleClickListener() {
//                    @Override
//                    protected void onNoDoubleClick(View v) {
//                        handlernewclick(ads);
//                    }
//                });
//                mHolder.imageView.setClickable(true);
//                mHolder.imageView.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
//                    @Override
//                    public void oneClick() {
//                        handlernewclick(ads);
//                    }
//
//                    @Override
//                    public void doubleClick() {
//
//                    }
//                }));

                mHolder.imageView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            handler.removeMessages(100);
//                            Log.d("lizisong", "ACTION_DOWN");
                        }else if(event.getAction() == MotionEvent.ACTION_UP){
//                            Log.d("lizisong", "ACTION_UP");
                            handler.removeMessages(100);
                            handler.sendEmptyMessageDelayed(100, 4000);
                        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
//                            Log.d("lizisong", "ACTION_MOVE");
                            handler.removeMessages(100);
                            handler.sendEmptyMessageDelayed(100, 4000);
                        }
                        return false;
                    }
                });
                container.addView(converView);

            }catch (Exception e){

            }
            return  converView;
        }

        private class ViewHolder{
            public ZQImageViewRoundOval imageView;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(mCaches.size() >0){
                mCaches.clear();
            }
            container.removeView((View)object);
            mCaches.add((View)object);
        }
    }


    class ReceiverBroad extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("downok")){
                for(int i = 0; i< MainActivity.columnButton.size(); i++){
                    PlaceChannel item = MainActivity.columnButton.get(i);
                    if(item.nativeplace.contains(city)){
                        currentChannel = item;
                        channelid = item.channelid;
                        code=item.code;
                        break;
                    }
                }
                handlerCase();
                getJson();
//                getJson();
            }

        }
    }
    int height =135;

    @Override
    public void onResume() {
        super.onResume();
        if(mScrollBanner != null){
            mScrollBanner.setList(zixuanlist);
        }
        four_cloum1_item1.setClickable(true);
        four_cloum1_item2.setClickable(true);
        four_cloum1_item3.setClickable(true);
        four_cloum1_item4.setClickable(true);
        four_cloum1_item5.setClickable(true);
        if(height == 0){
            lay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.
                    OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    lay.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    height = lay.getHeight();
                }
            });
        }
        try{
            for(int i = 0; i< MainActivity.columnButton.size(); i++){
                PlaceChannel item = MainActivity.columnButton.get(i);
                if(item.nativeplace.contains(city)){
                    currentChannel = item;
                    channelid = item.channelid;
                    code=item.code;
                    break;
                }
            }

            if(!SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.LOCAL_CITY_FRIST, false)){
                if(MyApplication.currentCity != null && !MyApplication.currentCity.equals("")){
                    for(int i=0; i<MyApplication.cityId.size();i++){
                        final String city1 = MyApplication.cityId.get(i);
                        if(MyApplication.currentCity.contains(city1)){
                            String current = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "北京");
                            if(!MyApplication.currentCity.contains(current)){
                                SharedPreferencesUtil.putBoolean(SharedPreferencesUtil.LOCAL_CITY_FRIST, true);
                                //弹出切换提示框
                                final BTAlertDialog dialog = new BTAlertDialog(getActivity());
                                dialog.setTitle("系统定位到"+MyApplication.currentCity+"，是否切回"+MyApplication.currentCity+"频道?");
                                dialog.setNegativeButton("取消", null);
                                dialog.setPositiveButton("确定", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //切换频道刷新数据
                                        SharedPreferencesUtil.putString(SharedPreferencesUtil.CITY,city1);
                                        city=city1;
                                        ChannelDb.changeChannel(city1);
                                        Intent intent1 = new Intent();
                                        intent1.setAction("changetitles");
                                        getActivity().sendBroadcast(intent1);
                                        city =SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                                        city = "全国";
                                        if(city != null && !city.equals("")){
                                            if(currentChannel != null){
                                                if(!currentChannel.nativeplace.equals(city)){
                                                    for(int i=0; i< MainActivity.columnButton.size();i++){
                                                        PlaceChannel item = MainActivity.columnButton.get(i);
                                                        if(item.nativeplace.contains(city)){
                                                            currentChannel = item;
                                                            channelid = item.channelid;
                                                            code=item.code;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                            break;
                        }
                    }
                }
            }
//            String nowcity = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
//            if(!city.contains("nowcity")){
//                city = nowcity;
//                 getJson();
//            }
        }catch (Exception e){

        }


    }

    public void handlerCase(){
        try{
            ColumnData item ;
            four_cloum1_item1.setVisibility(View.GONE);
            four_cloum1_item2.setVisibility(View.GONE);
            four_cloum1_item3.setVisibility(View.GONE);
            four_cloum1_item4.setVisibility(View.GONE);
            four_cloum1_item5.setVisibility(View.GONE);
            if(currentChannel != null){
                List<ColumnData> column = currentChannel.column1;
                if(column != null){
                    int count = column.size();
                    if(count == 3){
                        four_cloum1_item1.setVisibility(View.VISIBLE);
                        four_cloum1_item2.setVisibility(View.VISIBLE);
                        four_cloum1_item3.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img1,options);
                        four_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img2,options);
                        four_cloum1_text_item2.setText(item.name);

                        item = currentChannel.column1.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img3,options);
                        four_cloum1_text_item3.setText(item.name);
                    }else if(count == 4){
                        four_cloum1_item1.setVisibility(View.VISIBLE);
                        four_cloum1_item2.setVisibility(View.VISIBLE);
                        four_cloum1_item3.setVisibility(View.VISIBLE);
                        four_cloum1_item4.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img1,options);
                        four_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img2,options);
                        four_cloum1_text_item2.setText(item.name);

                        item = currentChannel.column1.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img3,options);
                        four_cloum1_text_item3.setText(item.name);

                        item = currentChannel.column1.get(3);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img4,options);
                        four_cloum1_text_item4.setText(item.name);
                    }else if(count == 5){
                        four_cloum1_item1.setVisibility(View.VISIBLE);
                        four_cloum1_item2.setVisibility(View.VISIBLE);
                        four_cloum1_item3.setVisibility(View.VISIBLE);
                        four_cloum1_item4.setVisibility(View.VISIBLE);
                        four_cloum1_item5.setVisibility(View.VISIBLE);
                        item = currentChannel.column1.get(0);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img1,options);
                        four_cloum1_text_item1.setText(item.name);

                        item = currentChannel.column1.get(1);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img2,options);
                        four_cloum1_text_item2.setText(item.name);

                        item = currentChannel.column1.get(2);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img3,options);
                        four_cloum1_text_item3.setText(item.name);

                        item = currentChannel.column1.get(3);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img4,options);
                        four_cloum1_text_item4.setText(item.name);

                        item = currentChannel.column1.get(4);
                        ImageLoader.getInstance().displayImage(item.imgUrl
                                ,four_cloum1_img5,options);
                        four_cloum1_text_item5.setText(item.name);
                    }
                }
            }

        }catch (Exception e){

        }
    }

    private void openMinpro(ADS item){
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = item.title; // 小程序原始id
        req.path = item.jumpUrl;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        if(item.prvCode.equals("0")){
            req.miniprogramType=0;
        }else if(item.prvCode.equals("1")){
            req.miniprogramType=1;
        }else if(item.prvCode.equals("2")){
            req.miniprogramType=2;
        }

        MyApplication.api.sendReq(req);
    }


    private void handlernewclick(ADS item){
        tongji(item.id);
        Log.d("lizisong", "item.type:"+item.type);
        if(item.type.equals("applypatent")){
            String  LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
            if(LoginState.equals("1")){
                Intent intent = new Intent(getActivity(), ZhuanLiShenQing.class);
                startActivity(intent);
            }else {
                Intent intent=new Intent(getActivity(), MyloginActivity.class);
                //getActivity().startActivity(intent);
                //m.animations();
                startActivity(intent);
            }

        }if(item.type.equals("openMiniProgram")){
            openMinpro(item);
        }else if(item.type.equals("QualityProject")){
            Intent intent = new Intent(getActivity(), NewJingPinProject.class);
            startActivity(intent);
        }else if(item.type.equals("htmlDetail")){
            Intent intent=new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("title",item.title);
            intent.putExtra("url", item.jumpUrl);
            startActivity(intent);
        }else if(item.type.equals("mdDetail")){
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("id", item.aid);
            if(item.typeid.equals("2")){
                intent.putExtra("name", "项目");
            }else if(item.typeid.equals("4")){
                intent.putExtra("name", "专家");
            }else if(item.typeid.equals("7")){
                intent.putExtra("name", "设备");
            }else if(item.typeid.equals("1")){
                intent.putExtra("name", "资讯");
            }else if(item.typeid.equals("8")){
                intent.putExtra("name", "研究院");
            }else if(item.typeid.equals("6")){
                intent.putExtra("name", "政策");
            }else if(item.typeid.equals("5")){
                intent.putExtra("name", "专利");
            }else if(item.typeid.equals("1")){
                intent.putExtra("name", "资讯");
            }
            intent.putExtra("pic", item.getPicUrl());
            startActivity(intent);

        }else if(item.type.equals("techList")){
            if(item.typeid.equals("2")){
                WriteXuQiu.entry_address = 4;
                Intent intent = new Intent(getActivity(), NewProJect.class);
                startActivity(intent);
            }else if(item.typeid.equals("4")){
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                startActivity(intent);
            }else if(item.typeid.equals("6")){
                Intent intent = new Intent(getActivity(), PolicyActivity.class);
                startActivity(intent);
            }else if(item.typeid.equals("7")){
                Intent intent = new Intent(getActivity(), SheBeiActivity.class);
                startActivity(intent);
            }else if(item.typeid.equals("5")){
                Intent intent = new Intent(getActivity(), PatentActivity.class);
                startActivity(intent);
            }else if(item.typeid.equals("8")){
                Intent intent = new Intent(getActivity(), UnitedStatesActivity.class);
                startActivity(intent);
            }

        }else if(item.type.equals("expertAppointment")){
            Intent intent = new Intent(getActivity(), AppointmentSpecialist.class);
            startActivity(intent);
        }else if(item.type.equals("html")){
            Intent intent=new Intent(getActivity(), ActiveActivity.class);
            intent.putExtra("title", item.title);
            intent.putExtra("url", item.jumpUrl);
            startActivity(intent);
        }else if(item.type.equals("intelligence")){
            Intent intent = new Intent(getActivity(), QingBaoActivity.class);
            startActivity(intent);
        }else if(item.type.equals("boutiqueProject")){
            Intent intent = new Intent(getActivity(), XiangMuDuiJieActivity.class);
            startActivity(intent);
        }else if(item.type.equals("place")){
            if(item.typeid.equals("1")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", item.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("cityName",item.cityName);
                intent.putExtra("prvName",item.prvName);
                intent.putExtra("name", item.name);
                intent.putExtra("type",item.type);
                intent.putExtra("channelid", item.channelid);
                String city = item.nativeplace;
                intent.putExtra("city", city);
                startActivity(intent);
            }else if(item.typeid.equals("6")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", item.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("cityName",item.cityName);
                intent.putExtra("prvName",item.prvName);
                intent.putExtra("name", item.name);
                intent.putExtra("type",item.type);
                intent.putExtra("channelid",  item.channelid);
                String city = item.nativeplace;
                intent.putExtra("city", city);
                startActivity(intent);
            }else if(item.typeid.equals("11")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", item.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("cityName",item.cityName);
                intent.putExtra("prvName",item.prvName);
                intent.putExtra("name", item.name);
                intent.putExtra("type",item.type);
                intent.putExtra("channelid",item.channelid);
                String city = item.nativeplace;
                intent.putExtra("city", city);
                startActivity(intent);
            }else if(item.typeid.equals("xr")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                String city =item.nativeplace;
                intent.putExtra("typeid", item.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("cityName",item.cityName);
                intent.putExtra("prvName",item.prvName);
                intent.putExtra("prvCode", item.prvCode);
                intent.putExtra("name", item.name);
                intent.putExtra("type",item.type);
                intent.putExtra("channelid",item.channelid);
                intent.putExtra("city", city);
                startActivity(intent);
            }
        }else{
            Toast.makeText(getActivity(),"功能建设中，请等待升级",Toast.LENGTH_SHORT).show();
        }
    }

    private void openMinPro(ColumnData data){
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = data.title; // 小程序原始id
        req.path = data.jumpUrl;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        if(data.prvCode.equals("0")){
            req.miniprogramType=0;
        }else if(data.prvCode.equals("1")){
            req.miniprogramType=1;
        }else if(data.prvCode.equals("2")){
            req.miniprogramType=2;
        }

        MyApplication.api.sendReq(req);

    }

    private void handleronClick(String type, ColumnData data,String channelid){
        if(type.equals("applypatent")){
            String  LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
            if(LoginState.equals("1")){
                Intent intent = new Intent(getActivity(), ZhuanLiShenQing.class);
                startActivity(intent);
            }else {
                Intent intent=new Intent(getActivity(), MyloginActivity.class);
                //getActivity().startActivity(intent);
                //m.animations();
                startActivity(intent);
            }

        }else if(data.type.equals("openMiniProgram")){
            openMinPro(data);
        }else if(type.equals("QualityProject")){
            Intent intent = new Intent(getActivity(), NewJingPinProject.class);
            startActivity(intent);
        }else if(type.equals("expertAppointment")){
            Intent intent = new Intent(getActivity(), AppointmentSpecialist.class);
            startActivity(intent);
        }else if(type.equals("html")){
            Intent intent=new Intent(getActivity(), ActiveActivity.class);
            intent.putExtra("title", data.title);
            intent.putExtra("url", data.jumpUrl);
            startActivity(intent);
        }else if(type.equals("intelligence")){
            Intent intent = new Intent(getActivity(), QingBaoActivity.class);
            startActivity(intent);
        }else if(type.equals("boutiqueProject")){
            Intent intent = new Intent(getActivity(), XiangMuDuiJieActivity.class);
            startActivity(intent);
        }else if(type.equals("place")){
            if(data.typeid.equals("1")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", data.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("cityName",data.cityName);
                intent.putExtra("prvName",data.prvName);
                intent.putExtra("name", data.name);
                intent.putExtra("type",data.type);
                intent.putExtra("channelid", channelid);
                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("city", city);

                startActivity(intent);
            }else if(data.typeid.equals("6")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", data.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("cityName",data.cityName);
                intent.putExtra("prvName",data.prvName);
                intent.putExtra("name", data.name);
                intent.putExtra("type",data.type);
                intent.putExtra("channelid", channelid);
                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("city", city);
                startActivity(intent);
            }else if(data.typeid.equals("11")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", data.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("cityName",data.cityName);
                intent.putExtra("prvName",data.prvName);
                intent.putExtra("name", data.name);
                intent.putExtra("type",data.type);
                intent.putExtra("channelid",channelid);

                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("city", city);
                startActivity(intent);
            }else if(data.typeid.equals("xr")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("typeid", data.typeid);
                intent.putExtra("cityCode",code);
                intent.putExtra("cityName",data.cityName);
                intent.putExtra("prvName",data.prvName);
                intent.putExtra("name", data.name);
                intent.putExtra("type",data.type);
                intent.putExtra("channelid",channelid);
                intent.putExtra("city", city);
                startActivity(intent);
            }
        }else{
            Toast.makeText(getActivity(),"功能建设中，请等待升级",Toast.LENGTH_SHORT).show();
        }
    }


    public void showAnimatorfor(){
//        Log.d("lizisong", "showAnimatorfor");
        if((lay.getVisibility() == View.GONE && !isEnd ) && !isAnuationStaring ){
            yinying.setVisibility(View.GONE);
            ScrollBanner.isStop = true;
            state = 1;
//            Log.d("lizisong", "showAnimatorfor1");
            showAnimator();
            showAlpha();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isAnuationStaring = true;
                    ScrollBanner.isStop = true;
                    ValueAnimator va ;
                    va = ValueAnimator.ofInt(0,135);
                    va.setDuration(700);
                    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            //获取当前的height值
                            int h =(Integer)valueAnimator.getAnimatedValue();
                            //动态更新view的高度
                            lay.getLayoutParams().height = h;
//                            dingwei_lay.invalidate();
                            lay.requestLayout();
                        }
                    });
                    va.addListener(new Animator.AnimatorListener(){

                        @Override
                        public void onAnimationStart(Animator animation) {
                            isEnd = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            lay.setVisibility(View.VISIBLE);
                            isEnd = false;
                            jia1.setVisibility(View.VISIBLE);
                            handler.sendEmptyMessageDelayed(120, 50);

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    //开始动画
                    va.start();

                }
            });
        }
    }


    public void showAnimator(){
        lay.setVisibility(View.INVISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale_in);
        Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale_out);
        nice_spinner1.startAnimation(animation);
        search_lay.startAnimation(animation1);
        jia.startAnimation(animation1);
        jia1.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lay.setVisibility(View.VISIBLE);
                nice_spinner1.setVisibility(View.VISIBLE);
                isanimone=true;
                isEnd = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isanimone=true;
                isEnd = false;

                search_lay.setVisibility(View.VISIBLE);
                jia.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void showAlpha(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_alpha_in);
         Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_appha_out);

        jia.startAnimation(animation1);
        search_lay.startAnimation(animation1);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isEnd = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isEnd = false;
                jia.setVisibility(View.INVISIBLE);
                search_lay.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void hideAnimator(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale_out);
        nice_spinner1.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void hideAlpha(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_appha_out);
        jia1.setVisibility(View.VISIBLE);
        lay.startAnimation(animation);
        Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_alpha_in);
        jia.startAnimation(animation1);
        search_lay.startAnimation(animation1);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isEnd = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isEnd = false;
                jia.setVisibility(View.VISIBLE);
                search_lay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void hideAnimatorfor(){

        if( !isEnd  && !isAnuationStaring){

            nice_spinner1.setVisibility(View.VISIBLE);
            yinying.setVisibility(View.VISIBLE);
            state = 1;
            if(lay.getVisibility() == View.VISIBLE){
                ScrollBanner.isStop = true;

                isEnd = true;
                lay.setVisibility(View.INVISIBLE);
                hideAnimator();
                hideAlpha();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isAnuationStaring = true;
                        ScrollBanner.isStop = true;
                        ValueAnimator va ;
                        va = ValueAnimator.ofInt(135,0);
                        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                //获取当前的height值
                                int h =(Integer)valueAnimator.getAnimatedValue();
                                //动态更新view的高度
                                lay.getLayoutParams().height = h;
                                lay.requestLayout();
                            }
                        });
                        va.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                isEnd = true;
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                lay.setVisibility(View.GONE);
                                isEnd = false;
                                handler.sendEmptyMessageDelayed(120, 50);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        va.setDuration(700);
                        //开始动画
                        va.start();
                    }
//                    }
                });


            }
        }
    }

    private void pingYiAmin(View iv) {
        TranslateAnimation animation;
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, screenWidth-jia.getLayoutParams().width-30, Animation.RELATIVE_TO_SELF,
                200);
        animation.setDuration(500);
        animation.setFillAfter(true);
        iv.startAnimation(animation);
    }


    /**
     * 动画移动view并摆放至相应的位置
     *
     * @param view               控件
     * @param xFromDeltaDistance x起始位置的偏移量
     * @param xToDeltaDistance   x终止位置的偏移量
     * @param yFromDeltaDistance y起始位置的偏移量
     * @param yToDeltaDistance   y终止位置的偏移量
     * @param duration           动画的播放时间
     * @param delay              延迟播放时间
     * @param isBack             是否需要返回到开始位置
     */
    public static void moveViewWithAnimation(final View view, final float xFromDeltaDistance, final float xToDeltaDistance, final float yFromDeltaDistance, final float yToDeltaDistance, int duration, int delay, final boolean isBack) {
        //创建位移动画
        TranslateAnimation ani = new TranslateAnimation(xFromDeltaDistance, xToDeltaDistance, yFromDeltaDistance, yToDeltaDistance);
        ani.setInterpolator(new OvershootInterpolator());//设置加速器
        ani.setDuration(duration);//设置动画时间
        ani.setStartOffset(delay);//设置动画延迟时间
        //监听动画播放状态
        ani.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int deltaX = (int) (xToDeltaDistance - xFromDeltaDistance);
                int deltaY = (int) (yToDeltaDistance - yFromDeltaDistance);
                int layoutX = view.getLeft();
                int layoutY = view.getTop();
                int tempWidth = view.getWidth();
                int tempHeight = view.getHeight();
                view.clearAnimation();
                if (isBack == false) {
                    layoutX += deltaX;
                    layoutY += deltaY;
                    view.layout(layoutX, layoutY, layoutX + tempWidth, layoutY + tempHeight);
                } else {
                    view.layout(layoutX, layoutY, layoutX + tempWidth, layoutY + tempHeight);
                }
            }
        });
        view.startAnimation(ani);
    }
    private void tongji(final String id){
        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"");
        HashMap<String, String> map = new HashMap<>();
        String url="http://"+MyApplication.ip+"/api/banner_behavior.php";
        map.put("id", id);
        map.put("mid", mid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,-1,0);

    }
    public boolean isVisible= false;
    public void setVisible(boolean state){
        isVisible=state;
    }
}
