package fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maidiantech.ActiveActivity;
import com.maidiantech.AppointmentSpecialist;
import com.maidiantech.ChengZhangHuoBanDeil;
import com.maidiantech.DFBaoTouMore;
import com.maidiantech.DFInfoShow;
import com.maidiantech.DetailsActivity;
import com.maidiantech.EquipmentActivity;
import com.maidiantech.LocalCity;
import com.maidiantech.LookMoreActivity;
import com.maidiantech.MainActivity;
import com.maidiantech.MyloginActivity;
import com.maidiantech.NewJingPinProject;
import com.maidiantech.NewProJect;
import com.maidiantech.NewProjectActivity;
import com.maidiantech.NewXuQiu;
import com.maidiantech.PatentActivity;
import com.maidiantech.PersonActivity;
import com.maidiantech.PolicyActivity;
import com.maidiantech.ProJect;
import com.maidiantech.QingBaoActivity;
import com.maidiantech.R;
import com.maidiantech.SearchCommentPage;
import com.maidiantech.SheBeiActivity;
import com.maidiantech.TianXuQiu;
import com.maidiantech.UnitedStatesActivity;
import com.maidiantech.WebViewActivity;
import com.maidiantech.WriteXuQiu;
import com.maidiantech.XiangMuDuiJieActivity;
import com.maidiantech.XuQiuXiangqQing;
import com.maidiantech.ZhuanLiShenQing;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import Port.SollectInterface;
import Util.NetUtils;
import Util.WeatherUtils;
import application.ImageLoaderUtils;
import application.MyApplication;
import db.ChannelDb;
import entity.ADS;
import entity.BaiduData;
import entity.ColumnData;
import entity.CommHomeData;
import entity.DFBaoTaoEntity;
import entity.DeviceShow;
import entity.HomeEntity;
import entity.PlaceChannel;
import entity.PostDFData;
import entity.PostData;
import entity.WeatherBaidu;
import entity.Weather_Data;
import entity.XInZhiNow;
import entity.XinzhiTianqi;
import entity.XinzhiTianqiData;
import view.BTAlertDialog;
import view.RefreshListView;
import view.RoundImageView;
import view.ScrollBanner;
import view.VarietyImageView;
import view.ZQImageViewRoundOval;
import Util.SharedPreferencesUtil;

/**
 * Created by Administrator on 2018/7/26.
 */

public class DFBaoTaoFragment extends BaseFragment implements SollectInterface {

    private View view;
    RefreshListView listview;
    ImageView nice_spinner1,zhuanjias,xuqius,tianqi,zhuanjiayin,zhuan,xuqiuyin,xuqiu,nodata;
    RelativeLayout search_lay,search,wendulay,lay,search_lay1,wendulowlay,zhuanjia,zhuanjialay,xuqiulay;
    TextView low,heihgt,yueyue,yueyuedes,yueyuecount,yueyuetxt,xuqiutxt,xuqiucoun,xuqiucount,xuqiudes,nowwendu;
    ProgressBar progress;
    String city;
    PlaceChannel currentChannel;
    String channelid,type;
    public String sortid;
    private int screenWidth;
    public List<PostDFData> listData = new ArrayList<>();
    public List<ADS> advListData = new ArrayList<>();
    public List<ADS> advJingPinData = new ArrayList<>();
    public HashMap<String, String> zixuanaid = new HashMap<>();
    public List<String> zixuanlist = new ArrayList<>();
    public List<DeviceShow> deviceShows = new ArrayList<>();
    boolean isAnuationStaring = false;
    private DisplayImageOptions options;
    boolean  isanimone = false;
    ReceiverBroad receiverBroad;
    boolean isEnd =false;
    boolean isherat =true;
    private int state = 0;
    int heiht;
    DFBaoTaoAdapter dfBaoTaoAdapter ;
    MyXiangMuAdapter xiangMuAdapter = new MyXiangMuAdapter();
    MyDeviceAdapter  myDeviceAdapter = new MyDeviceAdapter();
    String newtem="", heihttem="",lowtem="",newtxt;
    boolean isFirst = false;
    int  height = 0;
    private float mFirstY;
    private float mCurrentY;
    int mCurrentfirstVisibleItem=-1;
    boolean  isStopAntional = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.dfbaotaofragment, null);
            nice_spinner1 = (ImageView)view.findViewById(R.id.nice_spinner1);
            zhuanjias = (ImageView)view.findViewById(R.id.zhuanjias);
            xuqius = (ImageView)view.findViewById(R.id.xuqius);
            tianqi = (ImageView)view.findViewById(R.id.tianqi);
            zhuanjiayin = (ImageView)view.findViewById(R.id.zhuanjiayin);
            zhuan = (ImageView)view.findViewById(R.id.zhuan);
            xuqiuyin = (ImageView)view.findViewById(R.id.xuqiuyin);
            xuqiu = (ImageView)view.findViewById(R.id.xuqiu);
            search_lay = (RelativeLayout)view.findViewById(R.id.search_lay);
            search = (RelativeLayout)view.findViewById(R.id.search);
            wendulay = (RelativeLayout)view.findViewById(R.id.wendulay);
            lay = (RelativeLayout)view.findViewById(R.id.lay);
            search_lay1 = (RelativeLayout)view.findViewById(R.id.search_lay1);
            wendulowlay = (RelativeLayout)view.findViewById(R.id.wendulowlay);
            zhuanjia = (RelativeLayout)view.findViewById(R.id.zhuanjia);
            zhuanjialay = (RelativeLayout)view.findViewById(R.id.zhuanjialay);
            xuqiulay = (RelativeLayout)view.findViewById(R.id.xuqiulay);
            low = (TextView)view.findViewById(R.id.low);
            heihgt = (TextView)view.findViewById(R.id.heihgt);
            yueyue = (TextView)view.findViewById(R.id.yueyue);
            yueyuedes = (TextView)view.findViewById(R.id.yueyuedes);
            yueyuecount = (TextView)view.findViewById(R.id.yueyuecount);
            yueyuetxt = (TextView)view.findViewById(R.id.yueyuetxt);
            xuqiutxt = (TextView)view.findViewById(R.id.xuqiutxt);
            xuqiucoun = (TextView)view.findViewById(R.id.xuqiucoun);
            xuqiucount = (TextView)view.findViewById(R.id.xuqiucount);
            xuqiudes = (TextView)view.findViewById(R.id.xuqiudes);
            progress = (ProgressBar)view.findViewById(R.id.progress);
            nodata = (ImageView)view.findViewById(R.id.nodata);
            nowwendu = (TextView)view.findViewById(R.id.nowwendu);
            low = (TextView)view.findViewById(R.id.low);
            listview = (RefreshListView)view.findViewById(R.id.listid);
            isFirst = false;



        }
        listview.setOnScrollListener(new AbsListView.OnScrollListener(){
            private SparseArray recordSp = new SparseArray(0);

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
//                        ScrollBanner.isStop = true;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                        ScrollBanner.isStop = false;
                        if(mCurrentfirstVisibleItem == 0 && isherat){
                            showAnimatorfor();
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//                        ScrollBanner.isStop = true;
                        break;
                }
            }

            public int getScrollY1() {
                View c = listview.getChildAt(0);
                if (c == null) {
                    return 0;
                }
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                int top = c.getTop();
                return -top + firstVisiblePosition * c.getHeight() ;
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
//                    if(h >25  && !isherat && firstVisibleItem != 0){
//                        hideAnimatorfor();
//                    }else {
//                        if(isherat && firstVisibleItem == 0){
//                             showAnimatorfor();
//                        }
//                    }
//                    if(isAnuationStaring){
//                        return;
//                    }

                    if(firstVisibleItem == 0 ){
                        showAnimatorfor();
                    }else {
                        if(firstVisibleItem > 1){
                            hideAnimatorfor();
                        }
                    }
                    h=0;
//                    Log.d("lizisong", "mCurrentfirstVisibleItem:"+mCurrentfirstVisibleItem);

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
                    return 29;
                }

//                    return 0;
            }
            class ItemRecod {
                int height = 0;
                int top = 0;
            }
        } );

        zhuanjias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteXuQiu.entry_address=11;
                Intent intent = new Intent(getActivity(), AppointmentSpecialist.class);
                startActivity(intent);
            }
        });

        xuqius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteXuQiu.entry_address=12;
                Intent intent = new Intent(getActivity(), NewXuQiu.class);
                intent.putExtra("typeid", "0");
                startActivity(intent);
            }
        });
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
                Intent intent = new Intent(getActivity(), SearchCommentPage.class);
                startActivity(intent);
            }
        });

        search_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchCommentPage.class);
                startActivity(intent);
            }
        });
        zhuanjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AppointmentSpecialist.class);
                startActivity(intent);
            }
        });
        xuqiulay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewXuQiu.class);
                intent.putExtra("typeid", "0");
                startActivity(intent);
            }
        });



        city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
        nodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ICONJson();
                getJson();
            }
        });
        receiverBroad = new ReceiverBroad();
        options = ImageLoaderUtils.initOptions();

//        if(MainActivity.hasSoftKeys(getActivity().getWindowManager())){
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listview.getLayoutParams();
//            layoutParams.bottomMargin= MyApplication.navigationbar+150;
//            listview.setLayoutParams(layoutParams);
//        }

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
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
                    break;
                }
            }
        }
        nowwendu.setTextSize(38);
        WeatherUtils.xinzhitianqi1("包头",handler);
        WeatherUtils.xinzhitianqi2("包头",handler);
//        int h= lay.getLayoutParams().height;
        lay.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.
                OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                lay.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                 height = lay.getHeight();
            }
        });
        listview.setInterface(this);
        progress.setVisibility(View.VISIBLE);

        heiht = (screenWidth-60)*461/1326;
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        receiverBroad = new ReceiverBroad();
        IntentFilter oninfilter = new IntentFilter();
        oninfilter.addAction("downok");
        oninfilter.addAction("changetitles");
        oninfilter.addAction("changelistviewheihgt");
        getActivity().registerReceiver(receiverBroad, oninfilter);

    }


    @Override
    public void onResume() {
        super.onResume();
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
            String currentcity = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "北京");
            if(currentcity != null && !currentcity.equals("包头")){
               MainActivity.changePindao();
            }

        }catch (Exception e){

        }
//        getJson();
//        listview.setAdapter(dfBaoTaoAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiverBroad);
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void lazyLoad() {

    }
    ScrollBanner mScrollBanner;

    @Override
    public void onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFirstY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                isStopAntional=false;
//                 if(mCurrentfirstVisibleItem == 0 && isherat){//当head可见时，强制显示头部，要不会有一段空白
//                   listview.setSelection(0);
//                }
//                isAnuationStaring = true;
//                isAnuationStaring = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentY = event.getY();
                if(mCurrentfirstVisibleItem == 0 || mCurrentfirstVisibleItem == 1){
                    if((Math.abs(mCurrentY -mFirstY)) <50 ){
                        isStopAntional = true;
                    }else{
                        isStopAntional = false;
                    }
                }

                break;
        }

    }

    class DFBaoTaoAdapter extends BaseAdapter{

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
            HoldView holdView;
            if(convertView == null){
                holdView = new HoldView();
                convertView = View.inflate(getActivity(), R.layout.dfbaotaoadapter,null);
                holdView.tianxuqiu = (LinearLayout)convertView.findViewById(R.id.tianxuqiu);
                holdView.sb_demographic = (ScrollBanner)convertView.findViewById(R.id.sb_demographic);
                holdView.santuu = (LinearLayout)convertView.findViewById(R.id.santuu);
                holdView.san = (LinearLayout)convertView.findViewById(R.id.san);
                holdView.er = (LinearLayout)convertView.findViewById(R.id.er);
                holdView.yi = (LinearLayout)convertView.findViewById(R.id.yi);
                holdView.tupian1 = (ZQImageViewRoundOval)convertView.findViewById(R.id.tupian1);
                holdView.tupian1yin = (ImageView)convertView.findViewById(R.id.tupian1yin);
                holdView.tupian2 = (ZQImageViewRoundOval)convertView.findViewById(R.id.tupian2);
                holdView.tupian3 = (ZQImageViewRoundOval)convertView.findViewById(R.id.tupian3);
                holdView.er1 = (ZQImageViewRoundOval)convertView.findViewById(R.id.er1);
                holdView.er2 = (ZQImageViewRoundOval)convertView.findViewById(R.id.er2);
                holdView.yi1 = (ZQImageViewRoundOval)convertView.findViewById(R.id.yi1);
                holdView.jingping = (LinearLayout)convertView.findViewById(R.id.jingping);
                holdView.xiangmu_title = (TextView)convertView.findViewById(R.id.xiangmu_title);
                holdView.hangye_more = (TextView)convertView.findViewById(R.id.hangye_more);
                holdView.viewpager = (ViewPager)convertView.findViewById(R.id.viewpager);
                holdView.viewpager.setPageMargin(20);
                holdView.baotou = (LinearLayout)convertView.findViewById(R.id.baotou);
                holdView.baotou_title = (TextView)convertView.findViewById(R.id.baotou_title);
                holdView.baotou_more = (TextView)convertView.findViewById(R.id.baotou_more);
                holdView.baotouviewpager = (ViewPager)convertView.findViewById(R.id.baotouviewpager);
                holdView.baotouviewpager.setPageMargin(20);
                holdView.tupian1.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdView.tupian1.setRoundRadius(20);
//                holdView.tupian1yin.setType(ZQImageViewRoundOval.TYPE_ROUND);
//                holdView.tupian1yin.setRoundRadius(20);
                holdView.tupian2.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdView.tupian2.setRoundRadius(20);
                holdView.tupian3.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdView.tupian3.setRoundRadius(20);
                holdView.er1.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdView.er1.setRoundRadius(20);
                holdView.er2.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdView.er2.setRoundRadius(20);
                holdView.yi1.setType(ZQImageViewRoundOval.TYPE_ROUND);
                holdView.yi1.setRoundRadius(20);
                ViewGroup.LayoutParams lp = holdView.yi1.getLayoutParams();
                lp.height = heiht;
                holdView.yi1.setLayoutParams(lp);
                holdView.zhuanli = (LinearLayout)convertView.findViewById(R.id.zhuanli);
                holdView.zhuanli_title = (TextView)convertView.findViewById(R.id.zhuanli_title);
                holdView.zhuanli_change = (TextView)convertView.findViewById(R.id.zhuanli_change);
                holdView.zhuanli_lay1 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay1);
                holdView.zhuanli_lay2 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay2);
                holdView.zhuanli_lay3 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay3);
                holdView.zhuanli_lay4 = (RelativeLayout)convertView.findViewById(R.id.zhuanli_lay4);
                holdView.zhuanli_line1 = (TextView)convertView.findViewById(R.id.zhuanli_line1);
                holdView.zhuanli_line2 = (TextView)convertView.findViewById(R.id.zhuanli_line2);
                holdView.zhuanli_line3 = (TextView)convertView.findViewById(R.id.zhuanli_line3);
                holdView.zhuanli_line4 = (TextView)convertView.findViewById(R.id.zhuanli_line4);
                holdView.zhuanli_title1 = (TextView)convertView.findViewById(R.id.zhuanli_title1);
                holdView.zhuanli_title2 = (TextView)convertView.findViewById(R.id.zhuanli_title2);
                holdView.zhuanli_title3 = (TextView)convertView.findViewById(R.id.zhuanli_title3);
                holdView.zhuanli_title4 = (TextView)convertView.findViewById(R.id.zhuanli_title4);
                holdView.zhuanli_lingyu1 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu1);
                holdView.zhuanli_lingyu2 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu2);
                holdView.zhuanli_lingyu3 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu3);
                holdView.zhuanli_lingyu4 = (TextView)convertView.findViewById(R.id.zhuanli_lingyu4);
                holdView.zhuanli_more = (TextView)convertView.findViewById(R.id.zhuanli_more);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView)convertView.getTag();
            }
            final PostDFData item = listData.get(position);
            if(item.typename.equals("zixun")){
                holdView.tianxuqiu.setVisibility(View.VISIBLE);
                holdView.santuu.setVisibility(View.GONE);
                holdView.jingping.setVisibility(View.GONE);
                holdView.baotou.setVisibility(View.GONE);
                holdView.zhuanli.setVisibility(View.GONE);
                holdView.sb_demographic.setIcon(R.mipmap.toutiao);
                mScrollBanner =  holdView.sb_demographic;
                holdView.sb_demographic.setList(zixuanlist);
                holdView.sb_demographic.stopScroll();
                holdView.sb_demographic.startScroll();
                holdView.sb_demographic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转
                        Intent intent = new Intent();
                        intent.setAction("action_toxuqiu");
                        Recommend.aids = zixuanaid.get(mScrollBanner.getCurrent() + "");
                        getActivity().sendBroadcast(intent);
                    }
                });

            }else if(item.typename.equals("adv")){
                holdView.tianxuqiu.setVisibility(View.GONE);
                holdView.santuu.setVisibility(View.VISIBLE);
                holdView.zhuanli.setVisibility(View.GONE);
                holdView.jingping.setVisibility(View.GONE);
                holdView.baotou.setVisibility(View.GONE);
                holdView.zhuanli.setVisibility(View.GONE);
                final ADS item1, item2, item3;
                if(item.adv.size() == 3){
                    holdView.san.setVisibility(View.VISIBLE);
                    holdView.yi.setVisibility(View.GONE);
                    holdView.er.setVisibility(View.GONE);
                    item1 = item.adv.get(0);
                    item2 = item.adv.get(1);
                    item3 = item.adv.get(2);
                    if(!isAnuationStaring){
                        Log.d("lizisong", "isAnuationStaring："+isAnuationStaring);
                        ImageLoader.getInstance().displayImage(item1.imgUrl
                                , holdView.tupian1, options);
                        ImageLoader.getInstance().displayImage(item2.imgUrl
                                , holdView.tupian2, options);
                        ImageLoader.getInstance().displayImage(item3.imgUrl
                                , holdView.tupian3, options);
                    }
                    holdView.tupian1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ADS ads = item1;
                            handlernewclick(ads);

                        }
                    });
                    holdView.tupian2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ADS ads = item2;
                            handlernewclick(ads);

                        }
                    });
                    holdView.tupian3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ADS ads = item3;
                            handlernewclick(ads);
                        }
                    });
                }else if(item.adv.size() == 2){
                    holdView.san.setVisibility(View.GONE);
                    holdView.yi.setVisibility(View.GONE);
                    holdView.er.setVisibility(View.VISIBLE);
                    item1 = item.adv.get(0);
                    item2 = item.adv.get(1);

                    if(!isAnuationStaring){
                        ImageLoader.getInstance().displayImage(item1.imgUrl
                                , holdView.er1, options);
                        ImageLoader.getInstance().displayImage(item2.imgUrl
                                , holdView.er2, options);

                    }
                    //                    }
                    holdView.er1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ADS ads = item1;
                            handlernewclick(ads);

                        }
                    });
                    holdView.er2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ADS ads = item2;
                            handlernewclick(ads);

                        }
                    });
                }else if(item.adv.size() == 1){
                    holdView.san.setVisibility(View.GONE);
                    holdView.yi.setVisibility(View.VISIBLE);
                    holdView.er.setVisibility(View.GONE);
                    item1 = item.adv.get(0);
                    if(!isAnuationStaring){
//                        Log.d("lizisong", "isAnuationStaring："+isAnuationStaring);
                        ImageLoader.getInstance().displayImage(item1.imgUrl
                                , holdView.yi1, options);

                    }
                    holdView.yi1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ADS ads = item1;
                            handlernewclick(ads);

                        }
                    });
                }

            }else if(item.typename.equals("device_list")){ //包头设备 device_list
//                Log.d("lizisong", "device_list");
                holdView.tianxuqiu.setVisibility(View.GONE);
                holdView.santuu.setVisibility(View.GONE);
                holdView.jingping.setVisibility(View.GONE);
                holdView.zhuanli.setVisibility(View.GONE);
                holdView.baotou.setVisibility(View.VISIBLE);
                holdView.baotou_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DFBaoTouMore.class);
                        intent.putExtra("typeid", "7");
                        intent.putExtra("cityCode","");
                        intent.putExtra("cityName","");
                        intent.putExtra("prvName","");
                        intent.putExtra("name", "");
                        intent.putExtra("type","");
                        intent.putExtra("channelid", "9");

                        intent.putExtra("city", "包头");

                        startActivity(intent);
                    }
                });
                if(!isAnuationStaring || !isFirst){
                    isFirst =true;
                   holdView.baotouviewpager.setAdapter(myDeviceAdapter);
                }

            }else if(item.typename.equals("foot")){// 精品项目 foot
//                Log.d("lizisong", "foot");
                holdView.tianxuqiu.setVisibility(View.GONE);
                holdView.santuu.setVisibility(View.GONE);
                holdView.zhuanli.setVisibility(View.GONE);
                holdView.jingping.setVisibility(View.VISIBLE);
                holdView.baotou.setVisibility(View.GONE);
                holdView.hangye_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NewProJect.class);
                        startActivity(intent);
//                        Intent intent = new Intent(getActivity(), DFBaoTouMore.class);
//                        intent.putExtra("typeid", "2");
//                        intent.putExtra("cityCode","");
//                        intent.putExtra("cityName","");
//                        intent.putExtra("prvName","");
//                        intent.putExtra("name", "");
//                        intent.putExtra("type","");
//                        intent.putExtra("channelid", "9");
//                        intent.putExtra("city", "包头");
//                        startActivity(intent);
                    }
                });
                if(!isAnuationStaring){
                     holdView.viewpager.setAdapter(xiangMuAdapter);
                }
            }else if(item.typename.equals("zhuanli")){
                holdView.tianxuqiu.setVisibility(View.GONE);
                holdView.santuu.setVisibility(View.GONE);
                holdView.zhuanli.setVisibility(View.VISIBLE);
                holdView.jingping.setVisibility(View.GONE);
                holdView.baotou.setVisibility(View.GONE);
                PostData zhuanli1,zhuanli2,zhuanli3,zhuanli4;
                int count = item.zhuanli.size();
                if(count == 1){
                    zhuanli1 = item.zhuanli.get(0);
                    holdView.zhuanli_lay1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay2.setVisibility(View.GONE);
                    holdView.zhuanli_line2.setVisibility(View.GONE);
                    holdView.zhuanli_lay3.setVisibility(View.GONE);
                    holdView.zhuanli_line3.setVisibility(View.GONE);
                    holdView.zhuanli_lay4.setVisibility(View.GONE);
                    holdView.zhuanli_line4.setVisibility(View.GONE);
                    holdView.zhuanli_title1.setText(zhuanli1.title);
                    holdView.zhuanli_lingyu1.setText("所属领域:"+zhuanli1.area_cate);

                }else if(count == 2){
                    zhuanli1 = item.zhuanli.get(0);
                    zhuanli2 = item.zhuanli.get(1);
                    holdView.zhuanli_lay1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay2.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line2.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay3.setVisibility(View.GONE);
                    holdView.zhuanli_line3.setVisibility(View.GONE);
                    holdView.zhuanli_lay4.setVisibility(View.GONE);
                    holdView.zhuanli_line4.setVisibility(View.GONE);
                    holdView.zhuanli_title1.setText(zhuanli1.title);
                    holdView.zhuanli_lingyu1.setText("所属领域:"+zhuanli1.area_cate);
                    holdView.zhuanli_title2.setText(zhuanli2.title);
                    holdView.zhuanli_lingyu2.setText("所属领域:"+zhuanli2.area_cate);

                }else if(count == 3){
                    zhuanli1 = item.zhuanli.get(0);
                    zhuanli2 = item.zhuanli.get(1);
                    zhuanli3 = item.zhuanli.get(2);
                    holdView.zhuanli_lay1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay2.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line2.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay3.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line3.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay4.setVisibility(View.GONE);
                    holdView.zhuanli_line4.setVisibility(View.GONE);
                    holdView.zhuanli_title1.setText(zhuanli1.title);
                    holdView.zhuanli_lingyu1.setText("所属领域:"+zhuanli1.area_cate);
                    holdView.zhuanli_title2.setText(zhuanli2.title);
                    holdView.zhuanli_lingyu2.setText("所属领域:"+zhuanli2.area_cate);
                    holdView.zhuanli_title3.setText(zhuanli3.title);
                    holdView.zhuanli_lingyu3.setText("所属领域:"+zhuanli3.area_cate);

                }else if(count >= 4){
                    zhuanli1 = item.zhuanli.get(0);
                    zhuanli2 = item.zhuanli.get(1);
                    zhuanli3 = item.zhuanli.get(2);
                    zhuanli4 = item.zhuanli.get(3);

                    holdView.zhuanli_lay1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line1.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay2.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line2.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay3.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line3.setVisibility(View.VISIBLE);
                    holdView.zhuanli_lay4.setVisibility(View.VISIBLE);
                    holdView.zhuanli_line4.setVisibility(View.VISIBLE);
                    holdView.zhuanli_title1.setText(zhuanli1.title);
                    holdView.zhuanli_lingyu1.setText("所属领域:"+zhuanli1.area_cate);
                    holdView.zhuanli_title2.setText(zhuanli2.title);
                    holdView.zhuanli_lingyu2.setText("所属领域:"+zhuanli2.area_cate);
                    holdView.zhuanli_title3.setText(zhuanli3.title);
                    holdView.zhuanli_lingyu3.setText("所属领域:"+zhuanli3.area_cate);
                    holdView.zhuanli_title4.setText(zhuanli4.title);
                    holdView.zhuanli_lingyu4.setText("所属领域:"+zhuanli4.area_cate);
                }
                holdView.zhuanli_lay1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra("id", item.zhuanli.get(0).id);
                        intent.putExtra("name", item.zhuanli.get(0).typename);
                        intent.putExtra("pic", item.zhuanli.get(0).litpic);
                        startActivity(intent);
                    }
                });
                holdView.zhuanli_lay2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra("id", item.zhuanli.get(1).id);
                        intent.putExtra("name", item.zhuanli.get(1).typename);
                        intent.putExtra("pic", item.zhuanli.get(1).litpic);
                        startActivity(intent);
                    }
                });
                holdView.zhuanli_lay3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra("id", item.zhuanli.get(2).id);
                        intent.putExtra("name", item.zhuanli.get(2).typename);
                        intent.putExtra("pic", item.zhuanli.get(2).litpic);
                        startActivity(intent);
                    }
                });
                holdView.zhuanli_lay4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra("id", item.zhuanli.get(3).id);
                        intent.putExtra("name", item.zhuanli.get(3).typename);
                        intent.putExtra("pic", item.zhuanli.get(3).litpic);
                        startActivity(intent);
                    }
                });
                holdView.zhuanli_change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progress.setVisibility(View.VISIBLE);
                        sortid = item.zhuanli.get(item.zhuanli.size()-1).id;
                        ChangeZhuanli();
                    }
                });
                holdView.zhuanli_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), LookMoreActivity.class);
                        intent.putExtra("typeid",5);
                        intent.putExtra("title","更多专利");
                        intent.putExtra("channel","专利");
                        startActivity(intent);
                    }
                });
            }

            return convertView;
        }
        class HoldView{
            public LinearLayout tianxuqiu;
            public ScrollBanner sb_demographic;

            public LinearLayout santuu;
            public LinearLayout san,er,yi;
            public ZQImageViewRoundOval tupian1,tupian2,tupian3,er1,er2,yi1;
            public ImageView tupian1yin;

            public LinearLayout jingping;
            public TextView xiangmu_title,hangye_more;
            public ViewPager viewpager;

            public LinearLayout baotou;
            public TextView baotou_title;
            public TextView baotou_more;
            public ViewPager baotouviewpager;

            //专利推荐
            public LinearLayout zhuanli;
            public RelativeLayout zhuanli_lay1,zhuanli_lay2,zhuanli_lay3,zhuanli_lay4;
            public TextView zhuanli_line1,zhuanli_line2,zhuanli_line3,zhuanli_line4;
            public TextView zhuanli_title,zhuanli_change;
            public TextView zhuanli_title1,zhuanli_title2,zhuanli_title3,zhuanli_title4;
            public TextView zhuanli_lingyu1,zhuanli_lingyu2,zhuanli_lingyu3,zhuanli_lingyu4,zhuanli_more;

        }
    }
    private void getJson(){
//        city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "全国");
//        if(city.equals("全国")){
//            city = "包头";
//        }
//        if(currentChannel != null){
//            if(!currentChannel.nativeplace.equals(city)){
//                for(int i = 0; i< MainActivity.columnButton.size(); i++){
//                    PlaceChannel item = MainActivity.columnButton.get(i);
//                    if(item.nativeplace.contains(city)){
//                        currentChannel = item;
//                        channelid = item.channelid;
//                        break;
//                    }
//                }
//            }
//        }
        listData.clear();
        String url="http://"+MyApplication.ip+"/api/getHomeRecommendData.php";
        HashMap<String ,String> map = new HashMap<>();
        map.put("channelid","9");
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);

    }
    DFBaoTaoEntity data;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{
                progress.setVisibility(View.GONE);
                if(msg.what == 1){
                    Gson gson = new Gson();
                    String ret = (String)msg.obj;
                    data = gson.fromJson(ret, DFBaoTaoEntity.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            if(data.data.head != null){
                                for(int i=0; i<data.data.head.size();i++){
                                    ADS ads = data.data.head.get(i);
                                    advListData.add(ads);
                                }
                            }

                            if(advListData.size() >0){
                                ADS item1,item2;
                                item1 = advListData.get(0);
                                item2 = advListData.get(1);
                               if(!isAnuationStaring) {
                                    ImageLoader.getInstance().displayImage(item1.imgUrl_yinying
                                            ,zhuanjiayin , options);
                                    ImageLoader.getInstance().displayImage(item1.imgUrl
                                            ,zhuan , options);

                                    ImageLoader.getInstance().displayImage(item2.imgUrl_yinying
                                            ,xuqiuyin , options);
                                    ImageLoader.getInstance().displayImage(item2.imgUrl
                                            ,xuqiu , options);
                               }

                                yueyue.setText(item1.name);
                                xuqiutxt.setText(item2.name);
                                String temp = toUtf8(item1.description);
                                String[] tem = temp.split(";");
                                temp = toUtf8(item2.description);
                                String[] tem1 = temp.split(";");

                                yueyuedes.setText(tem[0]);
                                yueyuecount.setText(item1.description_num);
                                yueyuetxt.setText(tem[1]);

                                xuqiucoun.setText(tem1[0]);
                                xuqiucount.setText(item2.description_num);
                                xuqiudes.setText(tem1[1]);

                            }

                           if(data.data.zixun != null){
                               PostDFData zixuan = new PostDFData();
                               zixuan.typename ="zixun";
                               zixuan.zixun = data.data.zixun;
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
                           }

                           if(data.data.adv != null){
                               PostDFData guanggao = new PostDFData();
                               guanggao.typename ="adv";
                               guanggao.adv = data.data.adv;
                               listData.add(guanggao);
                           }

                            if(data.data.foot != null){
                                PostDFData foot = new PostDFData();
                                foot.typename = "foot";
                                foot.foot = data.data.foot;
                                listData.add(foot);
                                if(foot.foot != null){
                                    for (int i=0; i<foot.foot.size();i++){
                                        ADS item = foot.foot.get(i);
                                        advJingPinData.add(item);
                                    }
                                }
                            }

                           if(data.data.device_list != null){
                               PostDFData device = new PostDFData();
                               device.typename = "device_list";
                               device.device_list = data.data.device_list;
                               listData.add(device);
                               if(device.device_list != null){
                                   DeviceShow deviceShow = null;
                                   for (int i=0;i<device.device_list.size();i++){
                                       int index = i %3;
                                       PostData item = device.device_list.get(i);
                                       if(index == 0){
                                           deviceShow = new DeviceShow();
                                           deviceShow.id1 = item.id;
                                           deviceShow.title1 = item.title;
                                           deviceShow.typeid1 = item.typeid;
                                           deviceShow.litpic1 = item.litpic;
                                           deviceShow.area_cate1 = item.area_cate;
                                       }else if(index == 1){
                                           deviceShow.id2 = item.id;
                                           deviceShow.title2 = item.title;
                                           deviceShow.typeid2 = item.typeid;
                                           deviceShow.litpic2 = item.litpic;
                                           deviceShow.area_cate2 = item.area_cate;
                                       }else if(index == 2){
                                           deviceShow.id3 = item.id;
                                           deviceShow.title3 = item.title;
                                           deviceShow.typeid3 = item.typeid;
                                           deviceShow.litpic3 = item.litpic;
                                           deviceShow.area_cate3 = item.area_cate;
                                           deviceShows.add(deviceShow);
                                           deviceShow = null;
                                       }
                                       if(index == device.device_list.size() -1){
                                           if(deviceShow != null)
                                               deviceShows.add(deviceShow);
                                       }

                                   }
                               }

                           }

                            if(data.data.zhuanli != null){
                                PostDFData zhuanli = new PostDFData();
                                zhuanli.typename ="zhuanli";
                                zhuanli.zhuanli = data.data.zhuanli;
                                listData.add(zhuanli);
                            }

                            if(dfBaoTaoAdapter == null){
                                showAnimator();
//                                TextView tv = new TextView(getActivity());
////                                tv.getLayoutParams().height=6;
//                                tv.setBackgroundColor(0xfff6f6f6);
//                                listview.addHeaderView(tv);

                                dfBaoTaoAdapter = new DFBaoTaoAdapter();
                                listview.setAdapter(dfBaoTaoAdapter);
                            }else{
                                dfBaoTaoAdapter.notifyDataSetChanged();
                            }
                            if(lowtem != null){
                                low.setText(lowtem);
                            }
                            if(heihttem != null){
                                heihgt.setText(heihttem);
                            }
                           if(newtem != null){
                                nowwendu.setText(newtem);
                           }
                           if(newtxt != null){
                            if(newtxt.contains("晴转多云")){
                                tianqi.setImageResource(R.mipmap.duoyue);
                            }else if(newtxt.contains("雷阵雨")){
                                tianqi.setImageResource(R.mipmap.leizhenyun);
                            }else if(newtxt.contains("多云")){
                                tianqi.setImageResource(R.mipmap.duoyue);
                            }else if(newtxt.contains("雨夹雪")){
                                tianqi.setImageResource(R.mipmap.yujiaxue);
                            }else if(newtxt.contains("浮尘")){
                                tianqi.setImageResource(R.mipmap.fuchen);
                            }else if(newtxt.contains("沙")){
                                tianqi.setImageResource(R.mipmap.fuchen);
                            }else if(newtxt.contains("晴")){
                                tianqi.setImageResource(R.mipmap.qing);
                            }else if(newtxt.contains("阴")){
                                tianqi.setImageResource(R.mipmap.yin);
                            }else if(newtxt.contains("雪")){
                                tianqi.setImageResource(R.mipmap.xue);
                            }else if(newtxt.contains("雨")){
                                tianqi.setImageResource(R.mipmap.yue1);
                            }else if(newtxt.contains("霾")){
                                tianqi.setImageResource(R.mipmap.li);
                            }
                           }
                        }
                    }
                }
                if(msg.what == 200){
                    try {
                       String json1 = (String)msg.obj;
                        JSONObject jsonObject = new JSONObject(json1);
                        int code = jsonObject.getInt("code");
                        if (code == 1) {
                            String body = jsonObject.getString("data");
                            JSONObject json = new JSONObject(body);
                            JSONArray acolumnButton = json.getJSONArray("columnButton");
                            for (int i = 0; i < acolumnButton.length(); i++) {
                                PlaceChannel item = new PlaceChannel();
                                JSONObject obj = acolumnButton.getJSONObject(i);
                                item.channelid = obj.getString("channelid");
                                item.code = obj.getString("code");
                                item.englishName = obj.getString("englishName");
                                item.nativeplace = obj.getString("nativeplace");
                                item.isOpen = obj.getString("isOpen");
                                item.selectPicUrl = obj.getString("selectPicUrl");
                                MyApplication.cityId.add(item.nativeplace);
                                item.picUrl = obj.getString("picUrl");
                                JSONArray column = obj.getJSONArray("column");
                                item.column1 = new ArrayList<>();
                                item.column2 = new ArrayList<>();
                                for (int j = 0; j < column.length(); j++) {
                                    JSONArray pos = column.getJSONArray(j);

                                    for (int z = 0; z < pos.length(); z++) {
                                        JSONObject object = pos.getJSONObject(z);
                                        ColumnData data = new ColumnData();
                                        data.imgUrl = object.getString("imgUrl");
                                        data.name = object.getString("name");
                                        data.type = object.getString("type");
                                        try {
                                            data.jumpUrl = object.getString("jumpUrl");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }

                                        try {
                                            data.channelid = object.getString("channelid");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }

                                        try {
                                            data.cityCode = object.getString("cityCode");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }

                                        try {
                                            data.cityName = object.getString("cityName");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }
                                        try {
                                            data.tag = object.getString("tag");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }

                                        try {
                                            data.prvName = object.getString("prvName");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }
                                        try {
                                            data.userName = object.getString("userName");
                                        }catch (JSONException a){

                                        }catch (Exception e){

                                        }

                                        try {
                                            data.path = object.getString("path");
                                        }catch (JSONException a){

                                        }catch (Exception e){

                                        }
                                        try {
                                            data.miniProgramType = object.getString("miniProgramType");
                                        }catch (JSONException a){

                                        }catch (Exception e){

                                        }

                                        try {
                                            data.typeid = object.getString("typeid");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }

                                        try {
                                            data.title = object.getString("title");
                                        } catch (JSONException a) {

                                        } catch (Exception e) {

                                        }


                                        if (j == 0) {
                                            item.column1.add(data);
                                        } else if (j == 1) {
                                            item.column2.add(data);
                                        }

                                    }
                                }
                                MainActivity.columnButton.add(item);

                            }
                            Intent intent = new Intent();
                            intent.setAction("downok");
                            getActivity().sendBroadcast(intent);

                        }
                    } catch (Exception e) {

                    }
                }
                if(msg.what == 120){
                    isAnuationStaring = false;
                    mCurrentfirstVisibleItem=-1;
//                    isherat=false;
                   if( mCurrentfirstVisibleItem == 0){
                       listview.setSelection(0);
                   }
                    if(mScrollBanner != null){
                        mScrollBanner.setStop(false);
                        mScrollBanner.startScroll();
                    }
//                    if(dfBaoTaoAdapter != null){
//                        dfBaoTaoAdapter.notifyDataSetChanged();
//                    }
//                    xiangMuAdapter.notifyDataSetChanged();
//                    myDeviceAdapter.notifyDataSetChanged();

                }

                if(msg.what == 501){
                    Gson gson = new Gson();
                    String ret = (String) msg.obj;
                    XinzhiTianqi data = gson.fromJson(ret, XinzhiTianqi.class);
                    if(data != null){
                        XinzhiTianqiData now = data.results.get(0);
                        XInZhiNow hlwendu =now.daily.get(0);
                        if(hlwendu != null){
                            heihttem = hlwendu.high;
                            lowtem = hlwendu.low;
                            low.setText(lowtem);
                            heihgt.setText(heihttem);
                        }
                    }
                }
                if(msg.what == 500){
                    Gson gson = new Gson();
                    String ret = (String) msg.obj;
                    XinzhiTianqi data = gson.fromJson(ret, XinzhiTianqi.class);
                    if(data != null){
                        try {
                            XinzhiTianqiData now = data.results.get(0);
                            newtem = now.now.temperature;
                            newtxt = now.now.text;
                            nowwendu.setText(newtem);
                            if(newtxt.contains("晴转多云")){
                                tianqi.setImageResource(R.mipmap.duoyue);
                            }else if(newtxt.contains("雷阵雨")){
                                tianqi.setImageResource(R.mipmap.leizhenyun);
                            }else if(newtxt.contains("多云")){
                                tianqi.setImageResource(R.mipmap.duoyue);
                            }else if(newtxt.contains("雨夹雪")){
                                tianqi.setImageResource(R.mipmap.yujiaxue);
                            }else if(newtxt.contains("浮尘")){
                                tianqi.setImageResource(R.mipmap.fuchen);
                            }else if(newtxt.contains("沙")){
                                tianqi.setImageResource(R.mipmap.fuchen);
                            }else if(newtxt.contains("晴")){
                                tianqi.setImageResource(R.mipmap.qing);
                            }else if(newtxt.contains("阴")){
                                tianqi.setImageResource(R.mipmap.yin);
                            }else if(newtxt.contains("雪")){
                                tianqi.setImageResource(R.mipmap.xue);
                            }else if(newtxt.contains("雨")){
                                tianqi.setImageResource(R.mipmap.yue1);
                            }else if(newtxt.contains("霾")){
                                tianqi.setImageResource(R.mipmap.li);
                            }

                        }catch (Exception e){

                        }

                    }
                }
                if(msg.what == 5){
                    Gson gson = new Gson();
                    String json = (String)msg.obj;
                    HomeEntity data= gson.fromJson(json, HomeEntity.class);
                    if(data != null){
                        List<PostData> zhuanli = data.data.posts.zhuanli;
                        if(zhuanli != null){
                            for(int i =0;i<listData.size();i++){
                                PostDFData item = listData.get(i);
                                if(item.typename.equals("zhuanli")){
                                    item.zhuanli = zhuanli;
                                    dfBaoTaoAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                }

            }catch (Exception e){

            }

        }
    };
    private void tongji(final String id){
        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"");
        HashMap<String, String> map = new HashMap<>();
        String url="http://"+MyApplication.ip+"/api/banner_behavior.php";
        map.put("id", id);
        map.put("mid", mid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,-1,0);

    }
    private void handlernewclick(ADS item){
        tongji(item.id);
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
                intent.putExtra("cityCode",item.cityCode);
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
                intent.putExtra("cityCode",item.cityCode);
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
                intent.putExtra("cityCode",item.cityCode);
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
                intent.putExtra("cityCode",item.cityCode);
                intent.putExtra("cityName",item.cityName);
                intent.putExtra("prvName",item.prvName);
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

    class ReceiverBroad extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("downok")){
//                for(int i = 0; i< MainActivity.columnButton.size(); i++){
//                    PlaceChannel item = MainActivity.columnButton.get(i);
//                    if(item.nativeplace.contains(city)){
//                        currentChannel = item;
//                        channelid = item.channelid;
//                        break;
//                    }
//                }
//                getJson();

               getJson();
            }

        }
    }

    class MyXiangMuAdapter extends PagerAdapter{
        View view =null;
        WeakReference<View> weakReference;
        LinkedList<View> mCaches = new LinkedList<View>();

        @Override
        public int getCount() {
            return advJingPinData.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View converView = null;
            try{
                HoldView mHolder = null;
                final ADS ads = advJingPinData.get(position);
                if(mCaches.size() == 0){
                    converView = View.inflate(getActivity(),R.layout.baotouviewpageradapter, null);
                    mHolder = new HoldView();
                    mHolder.iv = (ZQImageViewRoundOval)converView.findViewById(R.id.iv);
                    mHolder.iv.setType(ZQImageViewRoundOval.TYPE_ROUND);
                    mHolder.iv.setRoundRadius(20);
                    mHolder.iv.setRoundJiao(false,false,true,true);
                    mHolder.rencai_lay1 = (RelativeLayout)converView.findViewById(R.id.rencai_lay1);
                    mHolder.rencai_img1 = (RoundImageView)converView.findViewById(R.id.rencai_img1);
                    mHolder.rencai_title1 = (TextView) converView.findViewById(R.id.rencai_title1);
                    mHolder.rank = (TextView) converView.findViewById(R.id.rank);
                    mHolder.rencai_lingyu1 = (TextView) converView.findViewById(R.id.rencai_lingyu1);
                    mHolder.rank1 = (ImageView) converView.findViewById(R.id.rank1);
                    converView.setTag(mHolder);
                }else{
                    converView = (View)mCaches.removeFirst();
                    mHolder = (HoldView)converView.getTag();
                }



                mHolder.rencai_title1.setText(ads.arc_title);
                mHolder.rank.setText(ads.ranks);
                mHolder.rencai_lingyu1.setText(ads.area_cate);
                mHolder.iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent=new Intent(getActivity(), ActiveActivity.class);
//                        intent.putExtra("title", ads.title);
//                        intent.putExtra("url", ads.jumpUrl);
//                        startActivity(intent);
                        handlernewclick(ads);


                    }
                });
                mHolder.rank1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String  loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                        if(loginState.equals("1")){
                            Intent intent = new Intent(getActivity(), TianXuQiu.class);
                            intent.putExtra("aid", ads.aid);
                            intent.putExtra("typeid", ads.typeid);
                            startActivity(intent);
                        } else{
                            Intent intent = new Intent(getActivity(), MyloginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                mHolder.rencai_lay1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WriteXuQiu.entry_address =13;
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra("id", ads.aid);
                        intent.putExtra("name", "专家");
                        intent.putExtra("typeid", ads.typeid);
                        intent.putExtra("pic", ads.litpic);
                        startActivity(intent);
                    }
                });
//                if(!isAnuationStaring){
                    ImageLoader.getInstance().displayImage(ads.imgUrl
                            ,mHolder.iv , options);
                    ImageLoader.getInstance().displayImage(ads.litpic
                            ,mHolder.rencai_img1 , options);
//                }

                container.addView(converView);

            }catch (Exception e){

            }
            return  converView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(mCaches.size() >0){
                mCaches.clear();
            }
            container.removeView((View)object);
            mCaches.add((View)object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        class HoldView {
            public ZQImageViewRoundOval iv;
            public RelativeLayout rencai_lay1;
            public RoundImageView rencai_img1;
            public TextView rencai_title1,rank,rencai_lingyu1;
            public ImageView rank1;
        }
    }
    class MyDeviceAdapter  extends PagerAdapter{
        View view =null;
        WeakReference<View> weakReference;
        LinkedList<View> mCaches = new LinkedList<View>();
        @Override
        public int getCount() {
            return deviceShows.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View converView = null;
            try{
                HoldView mHolder = null;
              final  DeviceShow item  =deviceShows.get(position);
                if(mCaches.size() == 0){
                    converView = View.inflate(getActivity(),R.layout.baotoudevice, null);
                    mHolder = new HoldView();
                    mHolder.xiangmu_lay1 = (RelativeLayout)converView.findViewById(R.id.xiangmu_lay1);
                    mHolder.xiangmu_lay2 = (RelativeLayout)converView.findViewById(R.id.xiangmu_lay2);
                    mHolder.xiangmu_lay3 = (RelativeLayout)converView.findViewById(R.id.xiangmu_lay3);
                    mHolder.xianmgmu1 = (ImageView)converView.findViewById(R.id.xianmgmu1);
                    mHolder.xianmgmu2 = (ImageView)converView.findViewById(R.id.xianmgmu2);
                    mHolder.xianmgmu3 = (ImageView)converView.findViewById(R.id.xianmgmu3);
                    mHolder.xmtitle1  =(TextView)converView.findViewById(R.id.xmtitle1);
                    mHolder.xmtitle2  =(TextView)converView.findViewById(R.id.xmtitle2);
                    mHolder.xmtitle3  =(TextView)converView.findViewById(R.id.xmtitle3);
                    mHolder.lanyuan1 = (TextView)converView.findViewById(R.id.lanyuan1);
                    mHolder.lanyuan2 = (TextView)converView.findViewById(R.id.lanyuan2);
                    mHolder.lanyuan3 = (TextView)converView.findViewById(R.id.lanyuan3);
                    mHolder.yuyue1 = (ImageView)converView.findViewById(R.id.yuyue1);
                    mHolder.yuyue2 = (ImageView)converView.findViewById(R.id.yuyue2);
                    mHolder.yuyue3 = (ImageView)converView.findViewById(R.id.yuyue3);
                    converView.setTag(mHolder);
                }else{
                    converView = (View)mCaches.removeFirst();
                    mHolder = (HoldView)converView.getTag();
                }
                mHolder.xiangmu_lay1.setVisibility(View.GONE);
                mHolder.xiangmu_lay2.setVisibility(View.GONE);
                mHolder.xiangmu_lay3.setVisibility(View.GONE);
                if(item.id1 != null){
                    mHolder.xiangmu_lay1.setVisibility(View.VISIBLE);
                    if(!isAnuationStaring){
                        ImageLoader.getInstance().displayImage(item.litpic1
                                ,mHolder.xianmgmu1 , options);
                    }
                    mHolder.xmtitle1.setText(item.title1);
                    mHolder.lanyuan1.setText("所属领域："+item.area_cate1);

                }
                if(item.id2 != null){
                    mHolder.xiangmu_lay2.setVisibility(View.VISIBLE);
                    if(!isAnuationStaring){
                        ImageLoader.getInstance().displayImage(item.litpic2
                                ,mHolder.xianmgmu2 , options);
                    }
                    mHolder.xmtitle2.setText(item.title2);
                    mHolder.lanyuan2.setText("所属领域："+item.area_cate2);
                }

                if(item.id3 != null){
                    mHolder.xiangmu_lay3.setVisibility(View.VISIBLE);
                    if(!isAnuationStaring){
                        ImageLoader.getInstance().displayImage(item.litpic3
                                ,mHolder.xianmgmu3 , options);
                    }
                    mHolder.xmtitle3.setText(item.title3);
                    mHolder.lanyuan3.setText("所属领域："+item.area_cate3);
                }
                mHolder.xiangmu_lay1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra("id", item.id1);
                        intent.putExtra("name", "设备");
                        intent.putExtra("typeid", item.typeid1);
                        intent.putExtra("pic", item.litpic1);
                        startActivity(intent);
                    }
                });
                mHolder.xiangmu_lay2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra("id", item.id2);
                        intent.putExtra("name", "设备");
                        intent.putExtra("typeid", item.typeid2);
                        intent.putExtra("pic", item.litpic2);
                        startActivity(intent);
                    }
                });
                mHolder.xiangmu_lay3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                        intent.putExtra("id", item.id3);
                        intent.putExtra("name", "设备");
                        intent.putExtra("typeid", item.typeid3);
                        intent.putExtra("pic", item.litpic3);
                        startActivity(intent);
                    }
                });


                 mHolder.yuyue1.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          String  loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                          if(loginState.equals("1")){
                              Intent intent = new Intent(getActivity(), TianXuQiu.class);
                              intent.putExtra("aid", item.id1);
                              intent.putExtra("typeid", item.typeid1);
                              startActivity(intent);

                          } else{
                              Intent intent = new Intent(getActivity(), MyloginActivity.class);
                              startActivity(intent);
                          }
                      }
                  });
                mHolder.yuyue2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String  loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                        if(loginState.equals("1")){
                            Intent intent = new Intent(getActivity(), TianXuQiu.class);
                            intent.putExtra("aid", item.id2);
                            intent.putExtra("typeid", item.typeid2);
                            startActivity(intent);

                        } else{
                            Intent intent = new Intent(getActivity(), MyloginActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                mHolder.yuyue3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String  loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                        if(loginState.equals("1")){
                            Intent intent = new Intent(getActivity(), TianXuQiu.class);
                            intent.putExtra("aid", item.id3);
                            intent.putExtra("typeid", item.typeid3);
                            startActivity(intent);

                        } else{
                            Intent intent = new Intent(getActivity(), MyloginActivity.class);
                            startActivity(intent);
                        }
                    }
                });


                container.addView(converView);

            }catch (Exception e){

            }
            return  converView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(mCaches.size() >0){
                mCaches.clear();
            }
            container.removeView((View)object);
            mCaches.add((View)object);
        }
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }



        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
        //baotoudevice
        class HoldView{
            public RelativeLayout xiangmu_lay1,xiangmu_lay2,xiangmu_lay3;
            public ImageView xianmgmu1,xianmgmu2,xianmgmu3;
            public TextView xmtitle1,lanyuan1,xmtitle2,lanyuan2,xmtitle3,lanyuan3;
            public ImageView yuyue1,yuyue2,yuyue3;

        }
    }

    public String toUtf8(String str) {
              String result = null;
              try {
                   result = new String(str.getBytes("UTF-8"), "UTF-8");
              } catch (UnsupportedEncodingException e) {
                       // TODO Auto-generated catch block
                   e.printStackTrace();
              }
                return result;
            }


    public void showAnimatorfor(){
        if(height == 0){
            return;
        }
        if((lay.getVisibility() == View.INVISIBLE && !isEnd ) && !isAnuationStaring ){
            ScrollBanner.isStop = true;
            state = 1;
            if(listview != null){
             listview.setSelection(0);
//             dfBaoTaoAdapter.notifyDataSetChanged();
            }
            showAnimator();
            showAlpha();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isAnuationStaring = true;
                    ScrollBanner.isStop = true;
                    ValueAnimator va ;
                    va = ValueAnimator.ofInt(0,height);
                    va.setDuration(300);
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
//                            jia1.setVisibility(View.VISIBLE);
                            nowwendu.setTextSize(38);
                            listview.setSelection(0);
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
           Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale_in);
           Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale_out);
           nice_spinner1.startAnimation(animation);
           search_lay.startAnimation(animation1);
           wendulay.startAnimation(animation1);

           animation.setAnimationListener(new Animation.AnimationListener() {
               @Override
               public void onAnimationStart(Animation animation) {

               }

               @Override
               public void onAnimationEnd(Animation animation) {
//                   lay.setVisibility(View.VISIBLE);
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

//                   search_lay.setVisibility(View.VISIBLE);
//                   jia.setVisibility(View.VISIBLE);
               }

               @Override
               public void onAnimationRepeat(Animation animation) {

               }
           });

       }

    public void showAlpha(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_alpha_in);
        Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_appha_out);

//        jia.startAnimation(animation1);
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
//                jia.setVisibility(View.GONE);
                search_lay.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void hideAnimatorfor(){
        if(height == 0){
            return;
        }
        if( !isEnd  && !isAnuationStaring){
            nice_spinner1.setVisibility(View.VISIBLE);
//            yinying.setVisibility(View.VISIBLE);
            state = 1;
            nice_spinner1.setVisibility(View.VISIBLE);
//            yinying.setVisibility(View.VISIBLE);
            state = 1;
            if(lay.getVisibility() == View.VISIBLE){
                ScrollBanner.isStop = true;
                isAnuationStaring = true;
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
                        va = ValueAnimator.ofInt(height,0);
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
                                lay.setVisibility(View.INVISIBLE);
                                isEnd = false;
                                handler.sendEmptyMessageDelayed(120, 50);
                                nowwendu.setTextSize(38);

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        va.setDuration(300);
                        //开始动画
                        va.start();
                    }
//                    }
                });
        }
        }
    }

    public void hideAnimator(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale_out);
        nice_spinner1.startAnimation(animation);
        wendulay.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                wendulay.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void hideAlpha(){
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_appha_out);
//        jia1.setVisibility(View.VISIBLE);
        lay.startAnimation(animation);
        Animation animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_alpha_in);
        wendulay.startAnimation(animation);
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
//                jia.setVisibility(View.VISIBLE);
                search_lay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void ICONJson() {
        if( MainActivity.columnButton.size() ==0){
            String url = "http://"+MyApplication.ip+"/api/indexChannel_2_5.php";
            NetworkCom networkCom = NetworkCom.getNetworkCom();
            networkCom.getJson(url,null,handler,200,0);
        }
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
    public boolean isVisible= false;
    public void setVisible(boolean state){
        isVisible=state;
    }

}
