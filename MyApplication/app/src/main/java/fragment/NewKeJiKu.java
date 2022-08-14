package fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maidiantech.ActiveActivity;
import com.maidiantech.AppointmentSpecialist;
import com.maidiantech.BackHandledFragment;
import com.maidiantech.DFBaoTouMore;
import com.maidiantech.DFInfoShow;
import com.maidiantech.DetailsActivity;
import com.maidiantech.MyloginActivity;
import com.maidiantech.NewJingPinProject;
import com.maidiantech.NewProJect;
import com.maidiantech.NewProjectActivity;
import com.maidiantech.NewSearchHistory;
import com.maidiantech.NewZhengCeActivity;
import com.maidiantech.NewZhuanliActivity;
import com.maidiantech.PatentActivity;
import com.maidiantech.PersonActivity;
import com.maidiantech.PolicyActivity;
import com.maidiantech.ProJect;
import com.maidiantech.QingBaoActivity;
import com.maidiantech.R;
import com.maidiantech.SheBeiActivity;
import com.maidiantech.SpecialActivity;
import com.maidiantech.TopicInformation;
import com.maidiantech.UnitedStatesActivity;
import com.maidiantech.UnitedStatesDeilActivity;
import com.maidiantech.WebViewActivity;
import com.maidiantech.WriteXuQiu;
import com.maidiantech.XiangMuDuiJieActivity;
import com.maidiantech.XinFanAnCeShi;
import com.maidiantech.ZixunDetailsActivity;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Port.SollectInterface;
import Util.NetUtils;
import Util.PrefUtils;
import Util.SharedPreferencesUtil;
import Util.SortByLengthComparator;
import adapter.NewKeJiKuAdapter;
import adapter.kejikuAdapter;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.ADS;
import entity.Bannel_Data;
import entity.Codes;
import entity.ColumnData;
import entity.Datas;
import entity.Lab_Data;
import entity.NewKeJiKuEntity;
import entity.NewKeJiKuRes;
import entity.NewKejiKuShow;
import entity.Posts;
import entity.Project_Data;
import entity.Quick_Data;
import entity.Talent_Data;
import view.MyClickListener;
import view.RefreshListView;
import view.SystemBarTintManager;
import view.WordCloudView;
import view.ZQImageViewRoundOval;

import static dao.Sqlitetions.getInstance;

/**
 * Created by Administrator on 2019/11/11.
 */

public class NewKeJiKu extends BackHandledFragment implements SollectInterface {
    private View view;
    private RefreshListView listView;
    private boolean flag = false;
    private View mHomePageHeaderView;
    private List<Posts> postsListData = new ArrayList<>();
    int netWorkType;
    private int Size = 10;
    private String channelName;
    private String mytitle;
    private String jsons;
    private Datas data;
    private RelativeLayout la,search_heart;
    private ProgressBar progress;
    private String pubdate;
    private String sortid="";
    boolean  isanimone = true;
    boolean isEnd = false;
    boolean isherat = true;
    int heiht = 0;
    SystemBarTintManager tintManager ;
    kejikuAdapter Recomment;
    HashMap<String, String > hashMap = new HashMap<>();
    TextView xiangm_info,rencai_info,shebei_info,shengyanshe_info,zhuanli_info;
    TextView search;
    private  String   ips;
    RelativeLayout search_lay;
    ImageView dingwei_id,yingyin,nodata;
    LinearLayout dingwei_lay,id;
    TextView tip1,tip2,tip3;
    int socllstate = 0;
    int count=0;
    int lastindex = -1;
    ViewPager viewpager;
    RelativeLayout viewPagerContainer;
    public  List<ADS> adsListData=new ArrayList<>();
    public List<NewKejiKuShow> showList = new ArrayList<>();
    public List<Bannel_Data> bannelList = new ArrayList<>();
    public List<String> hotcilist = new ArrayList<>();
    private DisplayImageOptions options;
    MyAdapter myPagerAdapter;
    NewKeJiKuAdapter adapter;
    WordCloudView wcv;
    TextView title;
    String titletxt;
    ConstraintLayout kkk;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.newkejiku, null);
        }
        if (!flag) {
            flag = true;
            listView = (RefreshListView) view.findViewById(R.id.listview);
            options = ImageLoaderUtils.initOptions();
            progress = (ProgressBar) view.findViewById(R.id.progress);
            search_lay = (RelativeLayout)view.findViewById(R.id.search_lay);
            dingwei_id = (ImageView)view.findViewById(R.id.dingwei_id);
            dingwei_lay = (LinearLayout)view.findViewById(R.id.dingwei_lay);
            la = (RelativeLayout)view.findViewById(R.id.la);
            yingyin = (ImageView)view.findViewById(R.id.yingyin);
            nodata = (ImageView)view.findViewById(R.id.nodata);
            nodata.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pubdate = "";
                    sortid="";
                    getJson(false);
//                    getjsons(Size, pubdate,true);
                    handler.sendEmptyMessageDelayed(2, 5000);
                }
            });
            progress.setVisibility(View.VISIBLE);
            WindowManager wm = (WindowManager) getContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            int screenWidth = wm.getDefaultDisplay().getWidth();
            heiht = (screenWidth-72)*30/65;
            /**
             * 轮播图布局
             */
            mHomePageHeaderView = getLayoutInflater(savedInstanceState).inflate(R.layout.newkejikuheart, null);
            initView();
            initViewPager();
            listView.setInterface(this);
            listView.setOnScrollListener(new AbsListView.OnScrollListener(){
                private SparseArray recordSp = new SparseArray(0);
                private int mCurrentfirstVisibleItem = 0;
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

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
                            la.setVisibility(View.VISIBLE);
                            yingyin.setVisibility(View.VISIBLE);

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
                }
                class ItemRecod {
                    int height = 0;
                    int top = 0;
                }
            });
        }

        return view;
    }
    @Override
    protected boolean onBackPressed() {
        return false;
    }
    private void initView() {
        xiangm_info = (TextView) mHomePageHeaderView.findViewById(R.id.xiangm_info);
        rencai_info = (TextView)mHomePageHeaderView.findViewById(R.id.rencai_info);
        viewPagerContainer = (RelativeLayout)mHomePageHeaderView.findViewById(R.id.viewPagerContainer);
        viewpager = (ViewPager)mHomePageHeaderView.findViewById(R.id.viewpager);
        tip1 = (TextView)mHomePageHeaderView.findViewById(R.id.tip1);
        tip2 = (TextView)mHomePageHeaderView.findViewById(R.id.tip2);
        tip3 = (TextView)mHomePageHeaderView.findViewById(R.id.tip3);
        wcv = mHomePageHeaderView.findViewById(R.id.wcv);
        kkk = mHomePageHeaderView.findViewById(R.id.kkk);
        title = mHomePageHeaderView.findViewById(R.id.title);
        shebei_info = (TextView)mHomePageHeaderView.findViewById(R.id.shebei_info);
        shengyanshe_info = (TextView)mHomePageHeaderView.findViewById(R.id.shengyanshe_info);
        zhuanli_info = (TextView)mHomePageHeaderView.findViewById(R.id.zhuanli_info);
        search_heart = (RelativeLayout)mHomePageHeaderView.findViewById(R.id.search_heart);
        search_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewSearchHistory.class);
                startActivity(intent);
            }
        });
        search_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewSearchHistory.class);
                startActivity(intent);
            }
        });
//        search  = (TextView) mHomePageHeaderView.findViewById(R.id.search);
//        search.setOnClickListener(new NoDoubleClickListener() {
//            @Override
//            protected void onNoDoubleClick(View v) {
//                Intent intent = new Intent(getActivity(), NewSearchHistory.class);
//                startActivity(intent);
//            }
//        });


        xiangm_info.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
            @Override
            public void oneClick() {
                WriteXuQiu.entry_address = 4;
                Intent intent = new Intent(getActivity(), NewProJect.class);
                startActivity(intent);
            }

            @Override
            public void doubleClick() {

            }
        }));

        rencai_info.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
            @Override
            public void oneClick() {
                WriteXuQiu.entry_address = 3;
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                startActivity(intent);
            }

            @Override
            public void doubleClick() {

            }
        }));

        shebei_info.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
            @Override
            public void oneClick() {
                WriteXuQiu.entry_address = 5;
                Intent intent = new Intent(getActivity(), SheBeiActivity.class);
                startActivity(intent);

            }

            @Override
            public void doubleClick() {

            }
        }));

        shengyanshe_info.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
            @Override
            public void oneClick() {
                Intent intent = new Intent(getActivity(), UnitedStatesActivity.class);
                startActivity(intent);
            }

            @Override
            public void doubleClick() {

            }
        }));

        zhuanli_info.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
            @Override
            public void oneClick() {
                Intent intent = new Intent(getActivity(), PatentActivity.class);
                startActivity(intent);
            }

            @Override
            public void doubleClick() {
            }
        }));

//        listView.addHeaderView(mHomePageHeaderView, null, false);
        netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            progress.setVisibility(View.GONE);
//            updatedata();
        } else {
            Load();
        }
        listView.setPullDownToRefreshable(false);
        listView.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            @Override
            public void pullDownToRefresh() {
//                netWorkType = NetUtils.getNetWorkType(MyApplication
//                        .getContext());
//                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
////                    updatedata();
//                    Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
//                    listView.setPullDownToRefreshFinish();
//
//                } else {
//                    pubdate = "";
//                    sortid="";
//                    getjsons(Size, pubdate,true);
//                    handler.sendEmptyMessageDelayed(2, 5000);
//                }
            }
        });

        listView.setInterface(this);
        listView.setPullUpToRefreshable(false);
        listView.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
//                netWorkType = NetUtils.getNetWorkType(MyApplication
//                        .getContext());
//                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
//                    postsListData.clear();
//                    List<Posts> tjanpost = getInstance(getContext()).kejiku_findmore();
//                    postsListData.addAll(tjanpost);
//
//                    if(Recomment==null){
//                        Recomment=new kejikuAdapter(getActivity(), postsListData, channelName, mytitle);
//                        listView.setAdapter(Recomment);
//                    }else{
//                        Recomment.notifyDataSetChanged();
//                    }
//                }else {
//
//                    if(postsListData.size()==0){
//
//                    }else{
//                        if (postsListData.get(postsListData.size() - 1).getResult() != null && postsListData.get(postsListData.size() - 1).getResult().equals("no")) {
//                            listView.setPullUpToRefreshFinish();
//                            listView.setPullUpToRefreshable(false);
//                            Toast.makeText(getActivity(), "已是最后一条数据",Toast.LENGTH_SHORT).show();
//                            Message msgs = Message.obtain();
//                            msgs.what = 2;
//                            dismissDialog.sendMessageDelayed(msgs, 2000);
//                            return;
//                        }
//                        pubdate = postsListData.get(postsListData.size() - 1).getSortTime();
//                        sortid = postsListData.get(postsListData.size() - 1).getId();
//                        getjsons(Size, pubdate,false);
//                        Message msgs=Message.obtain();
//                        msgs.what=2;
//                        dismissDialog.sendMessageDelayed(msgs,5000);
//                    }
//                }
            }
        });

    }

    public void getjsons(final int Size, final String pubdate,final boolean state) {
        try {
            ips = MyApplication.ip;
            String url = "http://"+ips+"/api/technologyLibrary.php";
            NetworkCom networkCom = NetworkCom.getNetworkCom();
            HashMap<String,String> map = new HashMap<>();
            map.put("pageSize",Size+"");
            map.put("LastModifiedTime",pubdate+"");
            map.put("sortid", sortid);
            if(state){
                networkCom.getJson(url,map,handler,3,1);
            }else{
                networkCom.getJson(url,map,handler,3,0);
            }
            Message message = Message.obtain();
            message.what = 0;
            dismissDialog.sendMessageDelayed(message, 500);
        }catch (Exception e){
            Message msg=Message.obtain();
            msg.what=1;
            dismissDialog.sendMessage(msg);
        }

    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                //称动时的y坐标

                break;
            case MotionEvent.ACTION_UP:
                socllstate = 0;

                break;
        }

    }

    private class MyHandler extends Handler {
        WeakReference<NewKeJiKu> weakReference;

        public MyHandler(NewKeJiKu fragment) {
            weakReference = new WeakReference<NewKeJiKu>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NewKeJiKu frament=weakReference.get();
            if(frament == null){
                return;
            }
            try {
                if(msg.what == 4){
                    Gson gs = new Gson();
                    jsons = (String) msg.obj;
                    NewKeJiKuEntity newKeJiKuData = gs.fromJson(jsons, NewKeJiKuEntity.class);
                    if(newKeJiKuData != null){
                         if(newKeJiKuData.code.equals("1")){
                             if(newKeJiKuData.data != null){
                                 if(newKeJiKuData.data.bannel_data != null && newKeJiKuData.data.bannel_data.size() > 0){
                                     bannelList.clear();
                                     for(int i=0;i<newKeJiKuData.data.bannel_data.size();i++){
                                         Bannel_Data bannel = newKeJiKuData.data.bannel_data.get(i);
                                         bannelList.add(bannel);
                                     }
                                     myPagerAdapter = new MyAdapter();
                                     viewpager.setAdapter(myPagerAdapter);
                                     viewpager.setCurrentItem(100000);
                                     viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){

                                         @Override
                                         public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                         }

                                         @Override
                                         public void onPageSelected(int position) {

                                         }

                                         @Override
                                         public void onPageScrollStateChanged(int state) {

                                         }
                                     });
                                 }
                             }
                         tip1.setText("钛领科技库为您整合了 ");
                         Log.d("lizisong", "数据解析");
                        if(newKeJiKuData.data.techlib_count != 0){
                            tip2.setText(addComma(newKeJiKuData.data.techlib_count+"")+"");
                        }
                        tip3.setText(" 项顶尖科技资源");
                             Log.d("lizisong", "数据解析");
                             if(newKeJiKuData.data.result != null && newKeJiKuData.data.result.size()>0){
                                 showList.clear();
                                 for(int i=0;i<newKeJiKuData.data.result.size();i++){
                                     NewKeJiKuRes res = newKeJiKuData.data.result.get(i);
                                     if(!res.typename.equals("hot_search")){
                                         NewKejiKuShow nams = new NewKejiKuShow();
                                         nams.type=1;
                                         nams.typename = res.typename;
                                         nams.nav_name = res.nav_name;
                                         showList.add(nams);
                                     }
                                     if(res.typename.equals("hot_search")){
//                                         NewKejiKuShow pos = new NewKejiKuShow();
//                                         pos.typename=res.typename;
//                                         pos.nav_name=res.nav_name;
//                                         pos.hot_search_data = res.hot_search_data;
//                                         showList.add(pos);
                                         titletxt = res.nav_name;
                                         for(int j=0;j<res.hot_search_data.size();j++){
                                             hotcilist.add(res.hot_search_data.get(j));
                                         }
                                         Collections.sort(hotcilist, new SortByLengthComparator());

                                     }else if(res.typename.equals("talent_list")){
                                        if(res.talent_data != null && res.talent_data.size()>0){
                                             for(int j=0;j<res.talent_data.size();j++){
                                                 Talent_Data data = res.talent_data.get(j);
                                                 NewKejiKuShow pos = new NewKejiKuShow();
                                                 pos.typename=res.typename;
                                                 pos.nav_name=res.nav_name;
                                                 pos.talent_data = data;
                                                 showList.add(pos);
                                             }
                                            NewKejiKuShow na = new NewKejiKuShow();
                                            na.type=2;
                                            na.typename=res.typename;
                                            na.nav_name = res.nav_name;
                                            showList.add(na);
                                        }

                                     }else if(res.typename.equals("project_list")){
                                            if(res.project_data != null && res.project_data.size()>0){
                                                Log.d("lizisong", "res.project_data:"+res.project_data.size());
                                                NewKejiKuShow pos = new NewKejiKuShow();
                                                pos.typename=res.typename;
                                                pos.nav_name=res.nav_name;
                                                pos.project_data = res.project_data;
                                                showList.add(pos);
//                                                for(int j=0;j<res.project_data.size();j++){
//                                                    Project_Data data =res.project_data.get(j);
//                                                    NewKejiKuShow pos = new NewKejiKuShow();
//                                                    pos.lab_data=res.lab_data;
//                                                    pos.nav_name=res.nav_name;
//                                                    pos.project_data = data;
//                                                    showList.add(pos);
//                                                }
                                                NewKejiKuShow na = new NewKejiKuShow();
                                                na.type=2;
                                                na.typename=res.typename;

                                                na.nav_name = res.nav_name;
                                                showList.add(na);
                                            }
                                     }else if(res.typename.equals("laboratory_list")){
                                         if(res.lab_data != null && res.lab_data.size()>0){
                                             for(int j=0;j<res.lab_data.size();j++){
                                                 Lab_Data item = res.lab_data.get(j);
                                                 NewKejiKuShow pos = new NewKejiKuShow();
                                                 pos.typename=res.typename;
                                                 pos.nav_name=res.nav_name;
                                                 pos.lab_data = item;
                                                 showList.add(pos);
                                             }
                                         }
                                         NewKejiKuShow na = new NewKejiKuShow();
                                         na.type=2;
                                         na.typename=res.typename;
                                         na.nav_name = res.nav_name;
                                         showList.add(na);
                                     }else if(res.typename.equals("quick_link_list")){
                                         if(res.quick_data != null && res.quick_data.size()>0){
                                             for(int j=0;j<res.quick_data.size();j++){
                                                 Quick_Data data = res.quick_data.get(j);
                                                 NewKejiKuShow pos = new NewKejiKuShow();
                                                 pos.typename=res.typename;
                                                 pos.nav_name=res.nav_name;
                                                 pos.quick_data=data;
                                                 showList.add(pos);
                                             }
                                         }
                                     }

                                 }
                                 NewKejiKuShow nams = new NewKejiKuShow();
                                 nams.type=-1;
                                 nams.typename="";
                                 showList.add(nams);

                             }
                             progress.setVisibility(View.GONE);
                             if(showList.size() >0){
                                 nodata.setVisibility(View.GONE);
                             }
                             if(hotcilist.size()>0){
                                 kkk.setVisibility(View.VISIBLE);
                                 title.setVisibility(View.VISIBLE);
                                 title.setText(titletxt);
                                 int weight =18;
                                 int off = 2;
                                 for(int i=0;i<hotcilist.size();i++){
                                     String txt = hotcilist.get(i);
                                     wcv.addTextView(txt, weight);
                                     if(--off == 0) {
                                         off = 2;
                                         if(weight >8) weight=weight-1;
                                     }
                                 }

                             }else{
                                 kkk.setVisibility(View.GONE);
                                 title.setVisibility(View.GONE);
                             }
                             if(adapter == null){
                                 adapter = new NewKeJiKuAdapter(getContext(), showList);
                                 listView.addHeaderView(mHomePageHeaderView);
                                 listView.setAdapter(adapter);
                             }else {
                                 adapter.notifyDataSetChanged();
                             }
                         }
                    }
                }

//                if(msg.what == 3){
//                    if(msg.arg1 == 1){
//                        hashMap.clear();
//                        postsListData.clear();
//                    }
//                    Gson gs = new Gson();
//                    jsons = (String) msg.obj;
//                    Codes codes = gs.fromJson(jsons, Codes.class);
//                    data = codes.getData();
////                    adsListData.clear();
////                    adsListData = data.getAds();
//                    if(postsListData != null){
//                        List<Posts> post =data.getPosts();
//                        if(post != null){
//                            for(int i=0; i<post.size(); i++){
//                                Posts item = post.get(i);
//                                String title =  hashMap.get(item.getId());
//                                if(title == null){
//                                    postsListData.add(item);
//                                    hashMap.put(item.getId(),item.getId());
//                                }
//                            }
//                        }
//                        tip1.setText("钛领科技库为您整合了 ");
//                        if(data.count != null){
//                            tip2.setText(addComma(data.count)+"");
//                        }
//                        tip3.setText(" 项顶尖科技资源");
//                    }
//
//                    nodata.setVisibility(View.GONE);
//
//                    if(Recomment==null){
//                        Recomment=new kejikuAdapter(getActivity(), postsListData, channelName, mytitle);
//                        listView.setAdapter(Recomment);
//                    }else{
//                        Recomment.notifyDataSetChanged();
//                        listView.setPullDownToRefreshFinish();
//                        listView.setPullUpToRefreshFinish();
//                    }
//
//
//
//                    listView.setPullDownToRefreshFinish();
//                    listView.setPullUpToRefreshFinish();
//
//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            try {
//                                PrefUtils.setString(getActivity(),postsListData.get(position - 2).getId(),postsListData.get(position - 2).getId());
//                                if (postsListData.get(position - 2).getTypename().equals("专题")) {
//                                    Intent intent = new Intent(getActivity(), SpecialActivity.class);
//                                    intent.putExtra("id", postsListData.get(position - 2).getId());
//                                    startActivity(intent);
//                                }else {
//                                    String name = postsListData.get(position - 2).getTypename();
//                                    String typeid = postsListData.get(position - 2).typeid;
//                                    if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
//                                        Intent intent = new Intent(getActivity(), ZixunDetailsActivity.class);
//                                        intent.putExtra("id", postsListData.get(position - 2).getId());
//                                        intent.putExtra("name", postsListData.get(position - 2).getTypename());
//                                        intent.putExtra("typeid", postsListData.get(position - 2).typeid);
//                                        intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
//                                        startActivity(intent);
//
//                                    }else if(typeid.equals("8")){
//                                        Intent intent = new Intent(getActivity(), UnitedStatesDeilActivity.class);
//                                        intent.putExtra("aid",postsListData.get(position - 2).getId() );
//                                        startActivity(intent);
//                                    }else if(typeid.equals("4")){
//                                        Intent intent = new Intent(getActivity(), XinFanAnCeShi.class);
//                                        intent.putExtra("aid", postsListData.get(position - 2).getId());
//                                        startActivity(intent);
//                                    }else if(typeid.equals("2")){
//                                        Intent intent = new Intent(getActivity(), NewProjectActivity.class);
//                                        intent.putExtra("aid", postsListData.get(position - 2).getId());
//                                        startActivity(intent);
//                                    }else if(typeid.equals("5")){
//                                        Intent intent = new Intent(getActivity(), NewZhuanliActivity.class);
//                                        intent.putExtra("aid", postsListData.get(position - 2).getId());
//                                        startActivity(intent);
//                                    }else if(typeid.equals("6")){
//                                        Intent intent = new Intent(getActivity(), NewZhengCeActivity.class);
//                                        intent.putExtra("aid", postsListData.get(position - 2).getId());
//                                        startActivity(intent);
//                                    }else{
//                                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                                        intent.putExtra("id", postsListData.get(position - 2).getId());
//                                        intent.putExtra("typeid", postsListData.get(position - 2).typeid);
//                                        intent.putExtra("name", postsListData.get(position - 2).getTypename());
//                                        intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
//                                        startActivity(intent);
//                                    }
//                                }
//                            } catch (IndexOutOfBoundsException ex) {
//                                nodata.setVisibility(View.VISIBLE);
//
//                            } catch (Exception e) {
//                                nodata.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    });
//                }
            }catch (Exception e){

            }

        }
    }

    MyHandler handler = new MyHandler(this);
    DismissDialog dismissDialog = new DismissDialog(this);
    private class DismissDialog extends Handler{
        WeakReference<NewKeJiKu> weakReference;
        public DismissDialog (NewKeJiKu fragment){
            weakReference = new WeakReference<NewKeJiKu>(fragment);
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
    @Override
    public void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(getActivity());
        handler.removeCallbacksAndMessages(null);
        XGPushManager.onActivityStoped(getActivity());
        MobclickAgent.onPageEnd("科技库");
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
    /**
     * 消除有可能出现下拉刷新的界面
     */
    public void canelDownRefrsh(){
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
    public void onDestroy() {
        super.onDestroy();
        getInstance(getContext()).guanrsd();
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        handler.removeCallbacksAndMessages(null);
    }
    @Override
    public void onResume() {
        super.onResume();
        xiangm_info.setClickable(true);
        rencai_info.setClickable(true);
        shebei_info.setClickable(true);
        shengyanshe_info.setClickable(true);
        zhuanli_info.setClickable(true);
        XGPushManager.onActivityStarted(getActivity());
        handler.sendEmptyMessageDelayed(0, 5000);
        listView.setPullDownToRefreshFinish();
        if(Recomment!=null){
            Recomment.notifyDataSetChanged();
        }
        XGPushManager.onActivityStarted(getActivity());
        MobclickAgent.onPageStart("科技库");
    }

    protected void Load() {

        netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            getJson(false);
        }
    }

    public abstract class NoDoubleClickListener implements View.OnClickListener {
        public static final int MIN_CLICK_DELAY_TIME = 1000;//这里设置不能超过多长时间
        private long lastClickTime = 0;
        protected abstract void onNoDoubleClick(View v);
        @Override
        public void onClick(View v) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                onNoDoubleClick(v);
            }
        }
    }

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
                isEnd= false;
//                dingwei_lay.getLayoutParams().height = 50;
//                  tiplay.getLayoutParams().height = 50;

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
                dingwei_id.setBackgroundResource(R.mipmap.kejikub);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

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
//                dingwei_lay.setVisibility(View.GONE);
                isEnd= false;
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
    public static String addComma(String str) {
        DecimalFormat decimalFormat = new DecimalFormat(",###");
        return decimalFormat.format(Integer.parseInt(str));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tintManager = new SystemBarTintManager(getActivity());
    }

    public void setAniationShow(){
        if(dingwei_lay.getVisibility() == View.GONE && socllstate==0 && !isEnd ){
            isEnd = true;
            socllstate=1;
            showAnimator();
            showAlpha();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    ValueAnimator va ;
                    va = ValueAnimator.ofInt(0,90);
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
            });
        }
    }

    public void setAniationHide(){
        if(isanimone  && !isEnd){
            isanimone = false;
            isEnd = true;

            hideAnimator();
            hideAlpha();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dingwei_lay.setVisibility(View.INVISIBLE);
                    ValueAnimator va ;
                    va = ValueAnimator.ofInt(90,0);
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
    private void getJson(final boolean state){
        String url = "http://"+MyApplication.ip+"/api/techlibMainApi.php";
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        if(state){
            networkCom.getJson(url,null,handler,4,1);
        }else{
            networkCom.getJson(url,null,handler,4,0);
        }
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

    class MyAdapter extends PagerAdapter {

        View view =null;
        WeakReference<View> weakReference;
        LinkedList<View> mCaches = new LinkedList<View>();

        @Override
        public int getCount() {
            if(bannelList.size() == 1){
                return 1;
            }
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View converView = null;
            try{
                int index =0;
                if(bannelList.size() == 0){
                    index = 0;
                }else{
                    index = position % bannelList.size();
                }
                final Bannel_Data ads = bannelList.get(index);
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
                        handleronClick(ads);
                    }

                });


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


    private void handleronClick( Bannel_Data bannel_data){

        if(bannel_data.type.equals("tagplace")){
            Intent intent = new Intent(getActivity(), DFBaoTouMore.class);
            intent.putExtra("typeid", bannel_data.typeid);
            intent.putExtra("cityCode",bannel_data.code);
            intent.putExtra("cityName","");
            intent.putExtra("prvName","");
            intent.putExtra("name", "");
            intent.putExtra("type","");
//            intent.putExtra("tag",bannel_data.tag);
            intent.putExtra("channelid", bannel_data.channelid);
            intent.putExtra("city", bannel_data.name);
            startActivity(intent);
        }else if(bannel_data.type.equals("openMiniProgram")){
            openMinPro(bannel_data);
        }else if(bannel_data.type.equals("QualityProject")){
            Intent intent = new Intent(getActivity(), NewJingPinProject.class);
            startActivity(intent);
        }else if(bannel_data.type.equals("applypatent")){
            String  LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
            if(LoginState.equals("1")){
                Intent intent = new Intent(getActivity(), TopicInformation.class);
                intent.putExtra("typeid","2");
                startActivity(intent);
            }else {
                Intent intent=new Intent(getActivity(), MyloginActivity.class);
                //getActivity().startActivity(intent);
                //m.animations();
                startActivity(intent);
            }
        }else if(bannel_data.type.equals("htmlDetail")){
            Intent intent=new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("title",bannel_data.title);
            intent.putExtra("url", bannel_data.jumpUrl);
            startActivity(intent);
        }else if(bannel_data.type.equals("mdDetail")){
            if(bannel_data.type.equals("2")){
                Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                intent.putExtra("aid", bannel_data.aid);
                startActivity(intent);
            }else if(bannel_data.typeid.equals("4")){
                Intent intent = new Intent(getActivity(), XinFanAnCeShi.class);
                intent.putExtra("aid", bannel_data.aid);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("id", bannel_data.aid);
                if(bannel_data.typeid.equals("2")){
                    intent.putExtra("name", "项目");
                }else if(bannel_data.typeid.equals("4")){
                    intent.putExtra("name", "专家");
                    intent.putExtra("name", "设备");
                }else if(bannel_data.typeid.equals("1")){
                    intent.putExtra("name", "资讯");
                }else if(bannel_data.typeid.equals("8")){
                    intent.putExtra("name", "研究院");
                }else if(bannel_data.typeid.equals("6")){
                    intent.putExtra("name", "政策");
                }else if(bannel_data.typeid.equals("5")){
                    intent.putExtra("name", "专利");
                }else if(bannel_data.typeid.equals("1")){
                    intent.putExtra("name", "资讯");
                }
                intent.putExtra("pic", bannel_data.imgUrl);
                startActivity(intent);
            }


        }else if(bannel_data.type.equals("techList")){
            if(bannel_data.typeid.equals("2")){
                WriteXuQiu.entry_address = 4;
                Intent intent = new Intent(getActivity(), NewProJect.class);
                intent.putExtra("time_limit",bannel_data.time_limit);
                startActivity(intent);
            }else if(bannel_data.typeid.equals("4")){
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                startActivity(intent);
            }else if(bannel_data.typeid.equals("6")){
                Intent intent = new Intent(getActivity(), PolicyActivity.class);
                startActivity(intent);
            }else if(bannel_data.typeid.equals("7")){
                Intent intent = new Intent(getActivity(), SheBeiActivity.class);
                startActivity(intent);
            }else if(bannel_data.typeid.equals("5")){
                Intent intent = new Intent(getActivity(), PatentActivity.class);
                startActivity(intent);
            }else if(bannel_data.typeid.equals("8")){
                Intent intent = new Intent(getActivity(), UnitedStatesActivity.class);
                startActivity(intent);
            }

        }else if(bannel_data.type.equals("expertAppointment")){
            Intent intent = new Intent(getActivity(), AppointmentSpecialist.class);
            startActivity(intent);
        }else if(bannel_data.type.equals("html")){
            Intent intent=new Intent(getActivity(), ActiveActivity.class);
            intent.putExtra("title", bannel_data.title);
            intent.putExtra("url", bannel_data.jumpUrl);
            startActivity(intent);
        }else if(bannel_data.type.equals("intelligence")){
            Intent intent = new Intent(getActivity(), QingBaoActivity.class);
            startActivity(intent);
        }else if(bannel_data.type.equals("boutiqueProject")){
            Intent intent = new Intent(getActivity(), XiangMuDuiJieActivity.class);
//            intent.putExtra("flag", bannel_data.flag);
            intent.putExtra("channelid",bannel_data.channelid);
            startActivity(intent);
        }else if(bannel_data.type.equals("place")){
            if(bannel_data.typeid.equals("1")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", bannel_data.typeid);
                intent.putExtra("cityCode",bannel_data.code);
                intent.putExtra("prvCode",bannel_data.prvCode);
                intent.putExtra("cityName",bannel_data.name);
                intent.putExtra("prvCode", bannel_data.prvCode);
                intent.putExtra("prvName",bannel_data.prvName);
                intent.putExtra("name", bannel_data.name);
                intent.putExtra("type",bannel_data.type);
                intent.putExtra("channelid", bannel_data.channelid);
//                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("city", bannel_data.name);

                startActivity(intent);
            }else if(bannel_data.typeid.equals("6")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", bannel_data.typeid);
                intent.putExtra("cityCode",bannel_data.code);
                intent.putExtra("cityName",bannel_data.name);
                intent.putExtra("prvName",bannel_data.prvName);
                intent.putExtra("name", bannel_data.name);
                intent.putExtra("prvCode", bannel_data.prvCode);
                intent.putExtra("type",bannel_data.type);
                intent.putExtra("channelid", bannel_data.channelid);
//                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("city", bannel_data.name);
                startActivity(intent);
            }else if(bannel_data.typeid.equals("11")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", bannel_data.typeid);
                intent.putExtra("cityCode",bannel_data.code);
                intent.putExtra("cityName",bannel_data.name);
                intent.putExtra("prvName",bannel_data.prvName);
                intent.putExtra("prvCode", bannel_data.prvCode);
                intent.putExtra("name", bannel_data.name);
                intent.putExtra("type",bannel_data.type);
                intent.putExtra("channelid",bannel_data.channelid);

//                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("city", bannel_data.name);
                startActivity(intent);
            }else if(bannel_data.typeid.equals("xr")){
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
//                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
                intent.putExtra("typeid", bannel_data.typeid);
                intent.putExtra("cityCode", bannel_data.code);
                intent.putExtra("cityName",bannel_data.name);
                intent.putExtra("prvName",bannel_data.prvName);
                intent.putExtra("name", bannel_data.name);
                intent.putExtra("type",bannel_data.type);
                intent.putExtra("prvCode", bannel_data.prvCode);
                intent.putExtra("channelid",bannel_data.channelid);
                intent.putExtra("city", bannel_data.name);
                startActivity(intent);
            }
        }else{
            Toast.makeText(getActivity(),"功能建设中，请等待升级",Toast.LENGTH_SHORT).show();
        }
    }

    private void openMinPro(Bannel_Data data){
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
}
