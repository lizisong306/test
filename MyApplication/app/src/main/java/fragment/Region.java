package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLinearLayout;
import com.google.gson.Gson;
import com.maidiantech.BejingCeShi;
import com.maidiantech.DFInfoShow;
import com.maidiantech.DetailsActivity;
import com.maidiantech.EarlyRef;
import com.maidiantech.MyloginActivity;
import com.maidiantech.R;
import com.maidiantech.SpecialActivity;
import com.maidiantech.WebViewActivity;
import com.maidiantech.XingquActivity;
import com.maidiantech.ZixunDetailsActivity;
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
import Util.WeatherUtils;
import adapter.LunboAdapters;
import adapter.RecommendAdapter;
import application.MyApplication;
import dao.Sqlitetions;
import db.ChannelDb;
import entity.ADS;
import entity.Codes;
import entity.Datas;
import entity.Posts;
import view.BTAlertDialog;
import view.RefreshListView;

import static application.MyApplication.currentCity;
import static application.MyApplication.heights;
import static application.MyApplication.widths;
import static dao.Sqlitetions.getInstance;

/**
 * Created by 13520 on 2017/2/23.
 */

public class Region extends BaseFragment implements View.OnClickListener{
    private View view;
    private RefreshListView listView;
    private boolean flag = false;
    private View mHomePageHeaderView;
    public  List<ADS> adsListData=new ArrayList<>();
    private RelativeLayout relayout;
    private RelativeLayout difang_select, difang_city;
    private ViewPager pager;
    private LinearLayout layout,region;

    private TextView regionInfo;
    private TextView regionPolicy;
    private TextView regionActivity;
    private TextView cityTxt;
    private String sortid="";
    private int Size = 20;
    private String pubdate;
    private String channelName;
    private String mytitle;
    private boolean mHasLoadedOnce;
    private OkHttpUtils Okhttp;
    private String jsons;
    private Datas data;
    private LunboAdapters lunboadapter;
    private ArrayList<ImageView> imageList;
    private  int screenWidth;
    private List<Posts> postsListData = new ArrayList<>();
    RecommendAdapter Recomment;
    int netWorkType;
    private ProgressBar progress;
    HashMap<String, String > hashMap = new HashMap<>();
    private  String   ips;
    private String city="";


    private ImageView beijing;
    private ImageView qingdao;
    private ImageView zhengzhou;
    private ImageView dezhou;
    private ImageView liaocheng;
    private ImageView shaoxing;
    private ImageView weifang;
    private TextView region_activity;
    private int width= widths;
    private int height=MyApplication.heights;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.local_city, null);
        }
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();

        if (!flag) {
            flag = true;
            listView = (RefreshListView) view.findViewById(R.id.listview);
            progress=(ProgressBar) view.findViewById(R.id.progress);

            progress.setVisibility(View.VISIBLE);
            difang_select = (RelativeLayout)view.findViewById(R.id.difang_select);
            difang_city   = (RelativeLayout)view.findViewById(R.id.difang_city);
            cityTxt = (TextView)view.findViewById(R.id.city);
            cityTxt.setText(MyApplication.currentCity);
            initCityView();
            city =SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "");
            if(city.equals("")){
                difang_select.setVisibility(View.VISIBLE);
                difang_city.setVisibility(View.GONE);
            }else{
                difang_select.setVisibility(View.GONE);
                difang_city.setVisibility(View.VISIBLE);
            }

            try {
                WeatherUtils.parseWeather(city,"0", null);
            }catch (Exception e){

            }
            /**
             * 轮播图布局
             */
            mHomePageHeaderView = getLayoutInflater(savedInstanceState).inflate(R.layout.local_lunbo, null);
            initView();

            beijing();
        }
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isVisible || mHasLoadedOnce) {
            return;
        }
         netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
//            UIHelper.hideDialogForLoading();
//            UIHelper.showDialogForLoading(getActivity(),"", true);
            city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY,"");
            if(!city.equals("")){
                getjsons(Size, pubdate,false);
            }

        }
    }

    private void beijing(){
        if(city != null){
            if(city.equals("北京")){
                region.setVisibility(View.GONE);
                regionInfo.setVisibility(View.GONE);
                regionPolicy.setVisibility(View.GONE);
                regionActivity.setVisibility(View.GONE);
            }else{
                regionInfo.setVisibility(View.VISIBLE);
                regionPolicy.setVisibility(View.VISIBLE);
                regionActivity.setVisibility(View.VISIBLE);
                region.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initView() {
        relayout = (RelativeLayout) mHomePageHeaderView.findViewById(R.id.relayout);
        pager = (ViewPager) mHomePageHeaderView.findViewById(R.id.pager);
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(widths, heights);

        layout = (LinearLayout) mHomePageHeaderView.findViewById(R.id.layout);
        region =(LinearLayout) mHomePageHeaderView.findViewById(R.id.region);
        regionInfo = (TextView) mHomePageHeaderView.findViewById(R.id.region_info);
        region_activity = (TextView)mHomePageHeaderView.findViewById(R.id.region_activity);
        regionInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", "1");
                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "");
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });

        regionPolicy = (TextView) mHomePageHeaderView.findViewById(R.id.region_policy);
        regionPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                intent.putExtra("typeid", "6");
                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "");
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });

        regionActivity = (TextView) mHomePageHeaderView.findViewById(R.id.region_activity);
        regionActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DFInfoShow.class);
                String currentcity = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "");
                if(currentcity.equals("潍坊")){
                    intent.putExtra("typeid", "xr");
                }else{
                    intent.putExtra("typeid", "11");
                }
                String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "");
                intent.putExtra("city", city);
                startActivity(intent);


            }
        });
        listView.addHeaderView(mHomePageHeaderView, null, false);
         netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            // TypefaceUtil.replaceFont(getActivity(), "fonts/font.ttf");
            progress.setVisibility(View.GONE);
            updatedata();
        } else {
            // UIHelper.showDialogForLoading(getActivity(), "正在加载...", true);
        }
//        listView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
//
//                return imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
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
                 netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());

                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {

                    updatedata();
                    Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                    listView.setPullDownToRefreshFinish();

                } else {
                    try {
                        if(WeatherUtils.weatherData.city == null ){
                            city =SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "");
                            WeatherUtils.parseWeather(city,"0" ,handler);
                        }
                    }catch (Exception e){

                    }

                    pubdate = "";
                    sortid = "";
                    listView.setPullUpToRefreshable(true);
                    getjsons(Size, pubdate,true);
                    handler.sendEmptyMessageDelayed(2, 5000);
                }

               String LoginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                if(LoginState.equals("0")){
                    listView.showTipTitle("登录后可获取更多精准信息。",new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), MyloginActivity.class);
                            startActivity(intent);
                        }
                    });
                }else{
                   int count = SharedPreferencesUtil.getInt(SharedPreferencesUtil.XINGQUCOUNT, 0);
                    if( count == 0){
                        listView.showTipTitle("选择兴趣领域获取精准信息。",new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), XingquActivity.class);
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

               int netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                        postsListData.clear();
                        List<Posts> tjanpost = getInstance(getContext()).df_findmore();
                        postsListData.addAll(tjanpost);

                        if(Recomment==null){
                            Recomment=new RecommendAdapter(getActivity(), postsListData, channelName, mytitle);
                            listView.setAdapter(Recomment);
                        }else{
                            Recomment.notifyDataSetChanged();
                        }

                } else {

                    if(postsListData.size()==0){

                    }else{

                        if (postsListData.get(postsListData.size() - 1).getResult() != null && postsListData.get(postsListData.size() - 1).getResult().equals("no")) {
                            listView.setPullUpToRefreshFinish();
                            listView.setPullUpToRefreshable(false);
                            Toast.makeText(getActivity(), "已是最后一条数据",Toast.LENGTH_SHORT).show();
                            Message msgs = Message.obtain();
                            msgs.what = 2;
                            dismissDialog.sendMessageDelayed(msgs, 2000);
                            return;
                        }
                        pubdate = postsListData.get(postsListData.size() - 1).getSortTime();
                        city =SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "");
                        if(city.equals("北京"))
                        {
                            Log.d("lizisong", "beijing");
                            pubdate = "";
                            sortid =  postsListData.get(postsListData.size() - 1).getId();
                        }

                        getjsons(Size, pubdate,false);
                        Message msgs=Message.obtain();
                        msgs.what=2;
                        dismissDialog.sendMessageDelayed(msgs,5000);

                    }
                }
            }
        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                try {
//                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                    intent.putExtra("id", postsListData.get(position-2).getId());
//                    intent.putExtra("name", postsListData.get(position-2).getTypename());
//                    intent.putExtra("pic", postsListData.get(position-2).getLitpic());
//                    startActivity(intent);
//                }catch (IndexOutOfBoundsException ex){
//
//                }catch (Exception e){
//                }
//            }
//        });
        /**
         * 根据滑动状态，改变轮播的状态
         */
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        ImageLoader.getInstance().pause();
                        if(channelName == "推荐"){
                            stopLunBo();
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                            ImageLoader.getInstance().resume();
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
                    listView.setPullUpToRefreshFinish();
                }
            }
        });

        /**
         * 页面滑动设置相对应的小圆点的改变
         *
         */
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        pager.setFocusable(true);
        pager.setFocusableInTouchMode(true);
        pager.requestFocus();

    }

    public void getjsons(final int Size, final String pubdate, final boolean state) {
        beijing();
        Okhttp = OkHttpUtils.getInstancesOkHttp();
        ips = MyApplication.ip;
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {

                            String timestamp = System.currentTimeMillis()+"";
                            String sign="";
                            ArrayList<String> sort = new ArrayList<String>();
                            String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "");
                            String url="";
                            String accessid="";
                            String loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
//                            if(loginState.equals("1")){
//                                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"0");
//
//                                accessid = mid;
//                            }else{
                                accessid = MyApplication.deviceid;
//                            }
                            sort.add("accessid" + accessid);
                            MyApplication.setAccessid();
                            //   http://123.206.8.208:80/api/placeChannel.php?pageSize=12&LastModifiedTime=1478068151&channel=1
                            if(city.equals("潍坊")){
                                sort.add("pageSize"+Size);
                                sort.add("LastModifiedTime"+pubdate);
                                sort.add("timestamp"+timestamp);
                                sort.add("channel"+"1");
                                sort.add("version"+MyApplication.version);
                                sign = KeySort.keyScort(sort);
                                 url = "http://"+ips+"/api/placeChannel.php?pageSize="+Size+"&LastModifiedTime="+pubdate+"&channel="+"1"+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;
                            }else if(city.equals("青岛") || city.equals("即墨") || city.equals("莱西")){
                                sort.add("pageSize"+Size);
                                sort.add("LastModifiedTime"+pubdate);
                                sort.add("timestamp"+timestamp);
                                sort.add("channel"+"2");
                                sort.add("version"+MyApplication.version);
                                sign = KeySort.keyScort(sort);
                                url = "http://"+ips+"/api/placeChannel.php?pageSize="+Size+"&LastModifiedTime="+pubdate+"&channel="+"2"+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;
                            }else if(city.equals("德州")){
                                sort.add("pageSize"+Size);
                                sort.add("LastModifiedTime"+pubdate);
                                sort.add("timestamp"+timestamp);
                                sort.add("channel"+"3");
                                sort.add("version"+MyApplication.version);
                                sign = KeySort.keyScort(sort);
                                url = "http://"+ips+"/api/placeChannel.php?pageSize="+Size+"&LastModifiedTime="+pubdate+"&channel="+"3"+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;
                            }else if(city.equals("郑州")){
                                sort.add("pageSize"+Size);
                                sort.add("LastModifiedTime"+pubdate);
                                sort.add("timestamp"+timestamp);
                                sort.add("channel"+"5");
                                sort.add("version"+MyApplication.version);
                                sign = KeySort.keyScort(sort);
                                url = "http://"+ips+"/api/placeChannel.php?pageSize="+Size+"&LastModifiedTime="+pubdate+"&channel="+"5"+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid;
                            }
                            else if(city.equals("北京")){
                                sort.add("pageSize"+Size);
                                sort.add("LastModifiedTime"+pubdate);
                                sort.add("timestamp"+timestamp);
                                sort.add("channel"+"4");
                                sort.add("typeid"+"1");
                                sort.add("version"+MyApplication.version);
                                sort.add("sortid"+sortid);
                                sign = KeySort.keyScort(sort);
                                url = "http://"+ips+"/api/placeChannel.php?pageSize="+Size+"&LastModifiedTime="+pubdate+"&channel="+"4"+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version+MyApplication.accessid+"&sortid="+sortid+"&typeid=1";
//                                Log.d("lizisong", "url:"+url);
                            }

                             jsons = Okhttp.loaudstringfromurl(url);
                             mHasLoadedOnce = true;
                             Message msg = new Message();
                             msg.obj = jsons;
                             msg.what = 3;
                            if(state){
                                msg.arg1 =1;
                            }
                            handler.sendMessage(msg);

                            Message message = Message.obtain();
                            message.what = 0;
                            dismissDialog.sendMessageDelayed(message, 500);

                        }catch (Exception e){
                            Message msg=Message.obtain();
                            msg.what=1;
                            dismissDialog.sendMessage(msg);
                        }
                    }
                }
        ).start();

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {

                if (msg.what == 0) {
                    //获取当前所在页面索引值
//                        Log.d("lizisong", "msg.what==="+0);
//                    int item = pager.getCurrentItem();
//                    item++;
//                    pager.setCurrentItem(item);
//                    handler.removeCallbacksAndMessages(null);
//                    handler.sendEmptyMessageDelayed(0, 3000);
                }
                if(msg.what == 3){
                    if(msg.arg1 == 1){
                        hashMap.clear();
                        postsListData.clear();
                    }
                    Gson gs = new Gson();
                    jsons = (String) msg.obj;
                    Codes codes = gs.fromJson(jsons, Codes.class);
                    data = codes.getData();
                    adsListData.clear();
                    adsListData = data.getAds();
                    if(netWorkType != NetUtils.NETWORKTYPE_INVALID){
                        if(WeatherUtils.weatherData != null && WeatherUtils.weatherData.city != null
                                && !WeatherUtils.weatherData.city.equals("")){
                            ADS tianqi  = new ADS();
                            tianqi.setTypename("天气");
                            adsListData.add(0,tianqi);
                        }
                    }
                    try {
                        if (lunboadapter == null) {
                            lunboadapter = new LunboAdapters(getActivity(), handler, adsListData);
                            pager.setAdapter(lunboadapter);
                            if (adsListData.size() != 1) {
                                initPaint();
                                pager.setCurrentItem(adsListData.size() * 5000);
                            }
                        } else {
                            lunboadapter.setLunBoAdapterList(adsListData);
                            lunboadapter.notifyDataSetChanged();
                        }
                    }catch (IllegalStateException e ){

                    }catch(Exception e){

                    }

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
                        }
                    }
                    if (adsListData.size() == 0) {
                        relayout.setVisibility(View.GONE);
                    } else {
                        relayout.setVisibility(View.VISIBLE);
                    }
                    if(Recomment==null){
                        Recomment=new RecommendAdapter(getActivity(), postsListData, channelName, mytitle);
                        listView.setAdapter(Recomment);
                    }else{
                        Recomment.notifyDataSetChanged();
                        listView.setPullDownToRefreshFinish();
                        listView.setPullUpToRefreshFinish();
                    }
                    for (int i = 0; i < adsListData.size(); i++) {
                        ADS ads = adsListData.get(i);
                        if( ads.getTypename()!= null && ads.getTypename().equals("天气")){

                        }else{
                            ADS s = new ADS(adsListData.get(i).getAid(), adsListData.get(i).getPicUrl(), adsListData.get(i).getTitle(), adsListData.get(i).getPubdate());
                            getInstance(getContext()).difang_collect(s);
                        }
                        // Log.i("sqli",s.toString());
                    }
                    for (int i = 0; i < postsListData.size(); i++) {
                        Posts p = new Posts();
                        p.setId(postsListData.get(i).getId());
                        p.setLitpic(postsListData.get(i).getLitpic());
                        p.setImageState(postsListData.get(i).imageState);
                        p.setImage(postsListData.get(i).image);
                        p.setTitle(postsListData.get(i).getTitle());
                        p.setSortTime(postsListData.get(i).getSortTime());
                        p.setPubdate(postsListData.get(i).getPubdate());
                        p.setDescription(postsListData.get(i).getDescription());
                        p.setSource(postsListData.get(i).getSource());
                        p.setZan(postsListData.get(i).getZan());
                        p.setClick(postsListData.get(i).getClick());
                        p.setTags(postsListData.get(i).getTags());
                        p.setUnit(postsListData.get(i).getUnit());
                        p.setState(postsListData.get(i).getState());
                        p.setTypename(postsListData.get(i).getTypename());
                        p.setChannel("df");
                        Sqlitetions.getInstance(getContext()).df_adds(p);
//                                Log.i("tj",p.toString());
                    }
                    lunboadapter.notifyDataSetChanged();

                    listView.setPullDownToRefreshFinish();
                    listView.setPullUpToRefreshFinish();

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                PrefUtils.setString(getActivity(),postsListData.get(position - 2).getId(),postsListData.get(position - 2).getId());
                                if (postsListData.get(position - 2).getTypename().equals("专题")) {
                                    Intent intent = new Intent(getActivity(), SpecialActivity.class);
                                    intent.putExtra("id", postsListData.get(position - 2).getId());

                                    startActivity(intent);
                                } else {
                                    String name = postsListData.get(position - 2).getTypename();
                                    if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                        String city = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "");
                                        if(city.equals("北京")){
//                                            Intent intent = new Intent(getActivity(), BejingCeShi.class);
//                                            intent.putExtra("id", postsListData.get(position - 2).getId());
//                                            intent.putExtra("name", postsListData.get(position - 2).getTypename());
//                                            intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
//                                            startActivity(intent);
                                            try {
//                                     String read_ids = PrefUtils.getString(getActivity(), "read_ids", "");
//                                    String id1 = postsListData.get(position-2).getId();
//                                    if(!read_ids.contains(id1)){
//                                      read_ids=read_ids+id1+",";
//                                       PrefUtils.setString(getActivity(),"read_ids",read_ids);
//                    }

                                                PrefUtils.setString(getActivity(),postsListData.get(position - 2).getId(),postsListData.get(position - 2).getId());
                                                if(postsListData.get(position - 2).getTypename().equals("科讯早参")){
                                                    Intent intent = new Intent(getActivity(), EarlyRef.class);
                                                    intent.putExtra("id", postsListData.get(position - 2).getId());
                                                    startActivity(intent);

                                                }else if(postsListData.get(position - 2).getTypename().equals("html")){
                                                    Intent intent=new Intent(getActivity(), WebViewActivity.class);
                                                    intent.putExtra("title", " ");
                                                    intent.putExtra("url", "http://www.zhongkechuangxiang.com/plus/patent/advertisement.html");
                                                    startActivity(intent);
                                                }
                                                else if (postsListData.get(position - 2).getTypename().equals("专题")) {
                                                    Intent intent = new Intent(getActivity(), SpecialActivity.class);
                                                    intent.putExtra("id", postsListData.get(position - 2).getId());

                                                    startActivity(intent);
                                                } else {

                                                     name = postsListData.get(position - 2).getTypename();
                                                    if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                                        Intent intent = new Intent(getActivity(), ZixunDetailsActivity.class);
                                                        intent.putExtra("id", postsListData.get(position - 2).getId());
                                                        intent.putExtra("name", postsListData.get(position - 2).getTypename());
                                                        intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
                                                        startActivity(intent);
                                                    }else{
                                                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                                                        intent.putExtra("id", postsListData.get(position - 2).getId());
                                                        intent.putExtra("name", postsListData.get(position - 2).getTypename());
                                                        intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
                                                        startActivity(intent);
                                                    }

                                                }
                                            } catch (IndexOutOfBoundsException ex) {

                                            } catch (Exception e) {
                                            }
                                        }else{
                                            Intent intent = new Intent(getActivity(), ZixunDetailsActivity.class);
                                            intent.putExtra("id", postsListData.get(position - 2).getId());
                                            intent.putExtra("name", postsListData.get(position - 2).getTypename());
                                            intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
                                            startActivity(intent);
                                        }


                                    }else{
                                        if(city.equals("北京")){
//                                            Intent intent = new Intent(getActivity(), BejingCeShi.class);
//                                            intent.putExtra("id", postsListData.get(position - 2).getId());
//                                            intent.putExtra("name", postsListData.get(position - 2).getTypename());
//                                            intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
//                                            startActivity(intent);
                                            try {
//                                     String read_ids = PrefUtils.getString(getActivity(), "read_ids", "");
//                                    String id1 = postsListData.get(position-2).getId();
//                                    if(!read_ids.contains(id1)){
//                                      read_ids=read_ids+id1+",";
//                                       PrefUtils.setString(getActivity(),"read_ids",read_ids);
//                    }

                                                PrefUtils.setString(getActivity(),postsListData.get(position - 2).getId(),postsListData.get(position - 2).getId());
                                                if(postsListData.get(position - 2).getTypename().equals("科讯早参")){
                                                    Intent intent = new Intent(getActivity(), EarlyRef.class);
                                                    intent.putExtra("id", postsListData.get(position - 2).getId());
                                                    startActivity(intent);

                                                }else if(postsListData.get(position - 2).getTypename().equals("html")){
                                                    Intent intent=new Intent(getActivity(), WebViewActivity.class);
                                                    intent.putExtra("title", " ");
                                                    intent.putExtra("url", "http://www.zhongkechuangxiang.com/plus/patent/advertisement.html");
                                                    startActivity(intent);
                                                }
                                                else if (postsListData.get(position - 2).getTypename().equals("专题")) {
                                                    Intent intent = new Intent(getActivity(), SpecialActivity.class);
                                                    intent.putExtra("id", postsListData.get(position - 2).getId());

                                                    startActivity(intent);
                                                } else {

                                                    String name1 = postsListData.get(position - 2).getTypename();
                                                    if(name1 != null &&(name1.equals("资讯")||name1.equals("推荐轮播图")|| name1.equals("推荐") || name1.equals("活动")|| name1.equals("知识"))){
                                                        Intent intent = new Intent(getActivity(), ZixunDetailsActivity.class);
                                                        intent.putExtra("id", postsListData.get(position - 2).getId());
                                                        intent.putExtra("name", postsListData.get(position - 2).getTypename());
                                                        intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
                                                        startActivity(intent);
                                                    }else{
                                                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                                                        intent.putExtra("id", postsListData.get(position - 2).getId());
                                                        intent.putExtra("name", postsListData.get(position - 2).getTypename());
                                                        intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
                                                        startActivity(intent);
                                                    }

                                                }
                                            } catch (IndexOutOfBoundsException ex) {

                                            } catch (Exception e) {
                                            }
                                        }else{
                                            Intent intent = new Intent(getActivity(), DetailsActivity.class);
                                            intent.putExtra("id", postsListData.get(position - 2).getId());
                                            intent.putExtra("name", postsListData.get(position - 2).getTypename());
                                            intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
                                            startActivity(intent);
                                        }

                                    }


                                }
                            } catch (IndexOutOfBoundsException ex) {

                            } catch (Exception e) {
                            }
                        }
                    });
                }
                if(msg.what == 1000){
                    try {
                        if(lunboadapter != null){
                            if(netWorkType != NetUtils.NETWORKTYPE_INVALID){
                                if(WeatherUtils.weatherData != null && WeatherUtils.weatherData.city != null
                                        && !WeatherUtils.weatherData.city.equals("")){
                                    adsListData.clear();
                                    ADS tianqi  = new ADS();
                                    tianqi.setTypename("天气");
                                    adsListData.add(0,tianqi);
//                                    lunboadapter = new LunboAdapters(getActivity(), handler, adsListData);
                                    lunboadapter.setLunBoAdapterList(adsListData);
                                    lunboadapter.notifyDataSetChanged();
                                    pager.setAdapter(lunboadapter);

                                    if (adsListData.size() != 1) {
                                        initPaint();
                                        pager.setCurrentItem(adsListData.size() * 5000);
                                    }

                                }
                            }
//                            lunboadapter.notifyDataSetChanged();
                        }
                    }catch (IllegalStateException ex){

                    }
                    catch (Exception e){

                    }

                }
            }catch (Exception e){

            }


        }
    };

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

    /**
     * 停止轮播图刷新
     */
    public void stopLunBo(){
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
            ImageView imageView = new ImageView(getActivity());
            if (i == 0) {
                imageView.setImageResource(R.drawable.dot_focused);
            } else {
                imageView.setImageResource(R.drawable.dot_normal);
            }
            imageList.add(imageView);
            AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(15, 15);
            params.setMargins(5, 0, 5, 0);
            layout.addView(imageView, params);
        }
        if(lunboadapter != null){
            int with= lunboadapter.getList().size()*15+(lunboadapter.getList().size()-1)*5+100;
            lunboadapter.setWidth(screenWidth-with);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        XGPushManager.onActivityStarted(getActivity());
        handler.sendEmptyMessageDelayed(0, 5000);
        listView.setPullDownToRefreshFinish();


       String currentcity = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "");
        if(currentcity.equals("潍坊")){
            region_activity.setText("本地资源");
        }else{
            region_activity.setText("活动");
        }
        if(currentcity.equals("") || currentcity.equals(city)){
            if(Recomment!=null){
                Recomment.notifyDataSetChanged();
            }
        }else{
            changeChannel();
        }
        XGPushManager.onActivityStarted(getActivity());
        MobclickAgent.onPageStart("地方频道");

      if(!SharedPreferencesUtil.getBoolean(SharedPreferencesUtil.LOCAL_CITY_FRIST, false)){

        if(MyApplication.currentCity != null && !MyApplication.currentCity.equals("")){
                   for(int i=0; i<MyApplication.cityId.size();i++){
                      final String city1 = MyApplication.cityId.get(i);
                       if(MyApplication.currentCity.contains(city1)){
                           String current = SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "");
                           if(!MyApplication.currentCity.contains(current)){
                               SharedPreferencesUtil.putBoolean(SharedPreferencesUtil.LOCAL_CITY_FRIST, true);
                               //弹出切换提示框
                               final BTAlertDialog dialog = new BTAlertDialog(getActivity());
                               dialog.setTitle("是否切换到"+MyApplication.currentCity);
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
                                        changeChannel();
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
        beijing();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getInstance(getContext()).guanrsd();
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
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
        MobclickAgent.onPageEnd("地方频道");
    }
    /**
     * 跟新数据
     */
    private void updatedata() {
        try {
//            Log.d("lizisong", "updatedata");

            adsListData.clear();
            List<ADS> dfList = getInstance(getContext()).dffindalls();
            adsListData.addAll(dfList);
            postsListData.clear();
            List<Posts> dfpost = getInstance(getContext()).df_findall();
            postsListData.addAll(dfpost);
            if (adsListData.size() == 0) {
                relayout.setVisibility(View.GONE);
            } else {
                relayout.setVisibility(View.VISIBLE);
            }
            if (lunboadapter == null) {
//                Log.d("lizisong", "create lunboadapter");
                try {
                    lunboadapter = new LunboAdapters(getActivity(), handler, adsListData);
                    pager.setAdapter(lunboadapter);
                    if (adsListData.size() != 1) {
                        initPaint();
                        pager.setCurrentItem(adsListData.size() * 5000);
                    }
                }catch (IllegalStateException e){

                }

            } else {
                lunboadapter.notifyDataSetChanged();
                listView.setPullDownToRefreshFinish();
                listView.setPullUpToRefreshFinish();
            }
                if(Recomment==null){
                    Recomment=new RecommendAdapter(getActivity(), postsListData, channelName, mytitle);
                    listView.setAdapter(Recomment);
                }else{
//                    Log.i("lizisong","@uuuuuuuu");
                    Recomment.notifyDataSetChanged();
                    listView.setPullDownToRefreshFinish();
                    listView.setPullUpToRefreshFinish();
                }


       } catch (Exception e) {
        }
    }

    /**
     * 启动轮播图
     */
    public void startLunBo(){
        handler.sendEmptyMessageDelayed(0, 3000);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.beijing:
//                Toast.makeText(getActivity(), "此频道暂未开通", Toast.LENGTH_SHORT).show();
                difang_select.setVisibility(View.GONE);
                difang_city.setVisibility(View.VISIBLE);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.CITY,"北京");
                ChannelDb.changeChannel("北京");
                city = "北京";
                Intent intent4 = new Intent();
                intent4.setAction("changetitles");
                getActivity().sendBroadcast(intent4);
                region_activity.setText("活动");
                changeChannel();
                break;
            case R.id.qingdao:
//                Toast.makeText(getActivity(), "切换到青岛频道", Toast.LENGTH_SHORT).show();
                difang_select.setVisibility(View.GONE);
                difang_city.setVisibility(View.VISIBLE);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.CITY,"青岛");
                if(currentCity.equals("即墨")){
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.CITY,currentCity);
                }
                if(currentCity.equals("莱西")){
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.CITY,currentCity);
                }
                ChannelDb.changeChannel("青岛");
                if(currentCity.equals("即墨") || currentCity.equals("莱西")){
                    SharedPreferencesUtil.putString(SharedPreferencesUtil.CITY,currentCity);
                }
                city = "青岛";
                if(currentCity.equals("即墨") || currentCity.equals("莱西")){
                    city = currentCity;
                    ChannelDb.changeChannel(city);
                }
                Toast.makeText(getActivity(), "切换到"+city+"频道", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent();
                intent1.setAction("changetitles");
                getActivity().sendBroadcast(intent1);
                region_activity.setText("活动");
                changeChannel();
                break;
            case R.id.zhengzhou:
                Toast.makeText(getActivity(), "切换到郑州频道", Toast.LENGTH_SHORT).show();
                difang_select.setVisibility(View.GONE);
                difang_city.setVisibility(View.VISIBLE);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.CITY,"郑州");
                ChannelDb.changeChannel("郑州");
                city = "郑州";
                Intent intent3 = new Intent();
                intent3.setAction("changetitles");
                getActivity().sendBroadcast(intent3);
                region_activity.setText("活动");
                changeChannel();
                break;
            case R.id.dezhou:
                Toast.makeText(getActivity(), "切换到德州频道", Toast.LENGTH_SHORT).show();
                difang_select.setVisibility(View.GONE);
                difang_city.setVisibility(View.VISIBLE);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.CITY,"德州");
                ChannelDb.changeChannel("德州");
                city = "德州";
                Intent intent2 = new Intent();
                intent2.setAction("changetitles");
                getActivity().sendBroadcast(intent2);
                region_activity.setText("活动");
                changeChannel();
                break;
            case R.id.liaocheng:
                Toast.makeText(getActivity(), "此频道暂未开通", Toast.LENGTH_SHORT).show();
                break;
            case R.id.shaoxing:
                Toast.makeText(getActivity(), "此频道暂未开通", Toast.LENGTH_SHORT).show();
                break;
            case R.id.weifang:
                Toast.makeText(getActivity(), "切换到潍坊频道", Toast.LENGTH_SHORT).show();
                difang_select.setVisibility(View.GONE);
                difang_city.setVisibility(View.VISIBLE);
                SharedPreferencesUtil.putString(SharedPreferencesUtil.CITY,"潍坊");
                city = "潍坊";
                region_activity.setText("本地资源");
                ChannelDb.changeChannel("潍坊");
                Intent intent = new Intent();
                intent.setAction("changetitles");
                getActivity().sendBroadcast(intent);
                changeChannel();
                break;

        }
    }


    private void initCityView() {
        beijing = (ImageView) view.findViewById(R.id.beijing);
        qingdao = (ImageView) view.findViewById(R.id.qingdao);
        zhengzhou = (ImageView) view.findViewById(R.id.zhengzhou);
        dezhou = (ImageView) view.findViewById(R.id.dezhou);
        liaocheng = (ImageView) view.findViewById(R.id.liaocheng);
        shaoxing = (ImageView) view.findViewById(R.id.shaoxing);
        weifang  = (ImageView)view.findViewById(R.id.weifang);

        qingdao.setOnClickListener(this);
        zhengzhou.setOnClickListener(this);
        dezhou.setOnClickListener(this);
        liaocheng.setOnClickListener(this);
        shaoxing.setOnClickListener(this);
        beijing.setOnClickListener(this);
        weifang.setOnClickListener(this);
    }

    private void changeChannel(){
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {

            updatedata();
            Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
            listView.setPullDownToRefreshFinish();

        } else {
            try {
                city =SharedPreferencesUtil.getString(SharedPreferencesUtil.CITY, "");
                WeatherUtils.parseWeather(city,"0" ,handler);
            }catch (Exception e){

            }
            difang_select.setVisibility(View.GONE);
            difang_city.setVisibility(View.VISIBLE);
            progress.setVisibility(View.VISIBLE);
            hashMap.clear();
            postsListData.clear();
            listView.setPullUpToRefreshable(true);
            pubdate = "";
            getjsons(Size, pubdate,false);
            handler.sendEmptyMessageDelayed(2, 5000);
        }
    }


}
