package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLinearLayout;
import com.google.gson.Gson;
import com.maidiantech.DetailsActivity;
import com.maidiantech.MyloginActivity;
import com.maidiantech.R;
import com.maidiantech.SpecialActivity;
import com.maidiantech.XingquActivity;
import com.maidiantech.ZixunDetailsActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.NetUtils;
import Util.OkHttpUtils;
import Util.PrefUtils;
import Util.SharedPreferencesUtil;
import adapter.LunboAdapters;
import adapter.PullrefreshAdapter;
import adapter.RecommendAdapter;
import application.MyApplication;
import dao.Sqlitetions;
import entity.ADS;
import entity.Codes;
import entity.Datas;
import entity.Posts;
import view.RefreshListView;

import static dao.Sqlitetions.getInstance;


/**
 * Created by 13520 on 2017/2/6.
 */

public class Information extends BaseFragment {
    private View view;
    private  int screenWidth;
    private boolean flag = false;
    private RefreshListView listView;
    private View mHomePageHeaderView;
    private ViewPager vp_pager;
    private LinearLayout ll_dots;
    private FrameLayout layout;
    private RelativeLayout relayout;
    public  List<ADS> adsListData=new ArrayList<>();
    int netWorkType = 0;
    private List<Posts> postsListData = new ArrayList<>();
    private String pubdate="";
    private boolean mHasLoadedOnce;
    private String channelName;
    private OkHttpUtils Okhttp;
    private int Size = 10;
    RecommendAdapter Recomment;
    private String jsons;
    private Datas data;
    private LunboAdapters lunboadapter;
    private ArrayList<ImageView> imageList;
    private PullrefreshAdapter MyitemAdapter;
    private String mytitle;
    private  ProgressBar progress;
    HashMap<String, String > hashMap = new HashMap<>();
    private  String   ips;
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.home_fragment, null);
            //TypefaceUtil.replaceFont(getActivity(), "fonts/font.ttf");
        }

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        if (!flag) {
            flag = true;
            listView = (RefreshListView) view.findViewById(R.id.listview);
             progress=(ProgressBar) view.findViewById(R.id.progress);

            progress.setVisibility(View.VISIBLE);
            /**
             * 轮播图布局
             */
            mHomePageHeaderView = getLayoutInflater(savedInstanceState).inflate(R.layout.my_lunbo, null);
            vp_pager =(ViewPager) mHomePageHeaderView.findViewById(R.id.pager);
            ll_dots = (LinearLayout) mHomePageHeaderView.findViewById(R.id.layout);
            layout = new FrameLayout(getActivity());
            relayout = (RelativeLayout) mHomePageHeaderView.findViewById(R.id.relayout);
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
//            listView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
//
//                    return imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
//                }
//
//
//            });
            listView.setPullDownToRefreshable(true);
            listView.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
                /**
                 *
                 */
                @Override
                public void pullDownToRefresh() {
                    netWorkType = NetUtils.getNetWorkType(MyApplication
                            .getContext());
//                    if(postsListData.size() > 0 ){
//                        listView.setPullDownToRefreshFinish();
//                        return;
//                    }
                    if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
//                        Log.d("lizisong", "setOnRefreshListener netWorkType");
                        updatedata();
                        Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                        listView.setPullDownToRefreshFinish();
//                        psc.onRefreshComplete();
                    } else {
//                        Log.d("lizisong", "setOnRefreshListener else");
                        hashMap.clear();
                        postsListData.clear();
                        pubdate = "";
                        getjsons(Size, pubdate);
//                        psc.onRefreshComplete();
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
                                    Intent intent = new Intent(getActivity(), XingquActivity.class);                           intent.putExtra("id", "0");
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
                        if (channelName == "资讯") {
                            postsListData.clear();
                            List<Posts> tuijianpost = getInstance(getContext()).tuijian_findmore();
                            postsListData.addAll(tuijianpost);
                        }


                        if (MyitemAdapter == null) {
                            MyitemAdapter = new PullrefreshAdapter(getActivity(), postsListData, channelName, mytitle);
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

                                listView.setPullUpToRefreshFinish();

                                listView.setPullUpToRefreshable(false);
                                Toast.makeText(getActivity(), "已是最后一条数据",Toast.LENGTH_SHORT).show();
                                Message msgs = Message.obtain();
                                msgs.what = 2;
                                dismissDialog.sendMessageDelayed(msgs, 2000);
                                return;
//                                psc.onRefreshComplete();
                            }
                            pubdate = postsListData.get(postsListData.size() - 1).getSortTime();
                            getjsons(Size, pubdate);
                            Message msgs=Message.obtain();
                            msgs.what=2;
                            dismissDialog.sendMessageDelayed(msgs,5000);
//                            psc.onRefreshComplete();
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
//                            Log.d("lizisong", "SCROLL_STATE_FLING");
                            ImageLoader.getInstance().pause();
//                            MyitemAdapter.setDisplayImage(false);
                            if(channelName == "推荐"){
                                stopLunBo();
                            }

                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                            Log.d("lizisong", "SCROLL_STATE_IDLE");
//                            ImageLoader.getInstance().clearDiskCache();
//                            ImageLoader.getInstance().clearMemoryCache();
//                            MyitemAdapter.setDisplayImage(true);
                            ImageLoader.getInstance().resume();


                            if(channelName == "资讯"){
                                startLunBo();
                            }
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//                            Log.d("lizisong", "SCROLL_STATE_TOUCH_SCROLL");
//                            ImageLoader.getInstance().pause();
                            if(channelName == "资讯"){
                                stopLunBo();
                            }
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

        return view;

    }
    @Override
    protected void lazyLoad() {
        if (!isVisible || mHasLoadedOnce) {
            return;
        }
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
//            updatedata();
            Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
//            UIHelper.hideDialogForLoading();
//            UIHelper.showDialogForLoading(getActivity(),"", true);
//            progress.setVisibility(View.VISIBLE);
            getjsons(Size, pubdate);
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
    public void getjsons(final int Size, final String pubdate) {
//        Log.d("lizisong", "getjsons");
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
                    if (channelName == "资讯") {
                        String evalue = getXingQu();
                        if(evalue.equals("")){
                            jsons = Okhttp.loaudstringfromurl("http://"+ips+"/api/arc_index.php?pageSize=" + Size /*+ "&LastModifiedTime=" + pubdate*/ + "&typeid=1&version=1.0&rand=0"+"&LastModifiedTime="+pubdate);
                        }else{
                            jsons = Okhttp.loaudstringfromurl("http://"+ips+"/api/arc_index.php?pageSize=" + Size /*+ "&LastModifiedTime=" + pubdate*/ + "&typeid=1&version=1.0&rand=0"+"&evalue="+evalue+"&LastModifiedTime="+pubdate);
                        }

                        // Log.i("jsons",jsons);
                    }

                    mHasLoadedOnce = true;
                    //关闭对话框
                    // Log.i("jsons",jsons);
                    Message msg = new Message();
                    msg.obj = jsons;
                    msg.what = 3;
//                    Log.d("lizisong", "send message");
//                    Thread.sleep(1000);
//                    UIHelper.hideDialogForLoading();
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
    private Information.MyHandler handler = new Information.MyHandler(this);
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
        WeakReference<Information> weakReference;

        public MyHandler(Information fragment) {
            weakReference = new WeakReference<Information>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            try {

                if (weakReference.get() != null) {
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



                        if (lunboadapter == null) {
//                            Log.d("lizisong", "create 2 lunboadapter");

                            lunboadapter = new LunboAdapters(getActivity(), handler, adsListData);
                            vp_pager.setAdapter(lunboadapter);
                            if (adsListData.size() != 1) {
                                initPaint();
                                vp_pager.setCurrentItem(adsListData.size() * 5000);
                            }
                        } else {
                            lunboadapter.notifyDataSetChanged();
                        }
                        if (adsListData.size() == 0) {
                            relayout.setVisibility(View.GONE);
                        } else {
                            relayout.setVisibility(View.VISIBLE);
                        }
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


                        if (MyitemAdapter == null) {
                            MyitemAdapter = new PullrefreshAdapter(getActivity(), postsListData, channelName, mytitle);
                            listView.setAdapter(MyitemAdapter);
                        } else {
                            MyitemAdapter.notifyDataSetChanged();
                        }
                        if (msg.what == 2) {
                            MyitemAdapter.notifyDataSetChanged();
                        }
                        if (channelName == "资讯") {
                            for (int i = 0; i < adsListData.size(); i++) {
                                ADS s = new ADS(adsListData.get(i).getAid(), adsListData.get(i).getPicUrl(), adsListData.get(i).getTitle(), adsListData.get(i).getPubdate());
                                getInstance(getContext()).collect_adds(s);
                                // Log.i("sqli",s.toString());
                            }
                            for (int i = 0; i < postsListData.size(); i++) {
                                Posts p = new Posts();
//                                postsListData.get(i).getId(), postsListData.get(i).getLitpic(), postsListData.get(i).getTitle(), postsListData.get(i).getPubdate(),postsListData.get(i).getDescription(),postsListData.get(i).getSource()
                                p.setId(postsListData.get(i).getId());
                                p.setLitpic(postsListData.get(i).getLitpic());
                                p.setTitle(postsListData.get(i).getTitle());
                                p.setSortTime(postsListData.get(i).getSortTime());
                                p.setDescription(postsListData.get(i).getDescription());
                                p.setSource(postsListData.get(i).getSource());
                                p.setZan(postsListData.get(i).getZan());
                                p.setClick(postsListData.get(i).getClick());
                                p.setTags(postsListData.get(i).getTags());
                                p.setImageState(postsListData.get(i).imageState);
                                p.setImage(postsListData.get(i).image);
                                p.setTypename(postsListData.get(i).getTypename());
                                p.setChannel("tuijian");
                                Sqlitetions.getInstance(getContext()).tuijian_adds(p);
                                //  Log.i("sql_title",p.toString());
                            }
                        }
                        lunboadapter.notifyDataSetChanged();
//                        Log.d("lizisong", "notifyDataSetChanged");
                        MyitemAdapter.notifyDataSetChanged();
                        listView.setPullDownToRefreshFinish();
                        listView.setPullUpToRefreshFinish();

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    PrefUtils.setString(getActivity(),postsListData.get(position - 2).getId(),postsListData.get(position - 2).getId());
                                    if(postsListData.get(position-2).getTypename().equals("专题")){
                                        Intent intent = new Intent(getActivity(), SpecialActivity.class);
                                        intent.putExtra("id", postsListData.get(position-2).getId());

                                        startActivity(intent);
                                    }else {
                                        String name = postsListData.get(position - 2).getTypename();
                                        if(name != null && (name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
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
//                        Log.d("lizisong", "position:"+position);
//                        Log.i("possition", postsListData.get(position-2).getId() + "................");
//                                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                                        intent.putExtra("id", postsListData.get(position - 2).getId());
//                                        intent.putExtra("name", postsListData.get(position - 2).getTypename());
//                                        intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
//                                        startActivity(intent);
                                    }
                                }catch (IndexOutOfBoundsException ex){

                                }catch (Exception e){
                                }
                            }
                        });
                    }
                }

            } catch (Exception e) {

            }
        }
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
    @Override
    public void onResume() {
        super.onResume();
        XGPushManager.onActivityStarted(getActivity());
//        psc.onRefreshComplete();
        handler.sendEmptyMessageDelayed(0, 5000);
        listView.setPullDownToRefreshFinish();
        if(MyitemAdapter!=null){
            MyitemAdapter.notifyDataSetChanged();
        }
        XGPushManager.onActivityStarted(getActivity());
        MobclickAgent.onPageStart("资讯"); //统计页面，"MainScreen"为页面名称，可自定义
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
        MobclickAgent.onPageEnd("资讯");
    }

    /**
     * 跟新数据
     */
    private void updatedata() {
//        try {
//            Log.d("lizisong", "updatedata");
            if (channelName == "资讯") {
                adsListData.clear();
                List<ADS> zxList = getInstance(getContext()).findalls();
                adsListData.addAll(zxList);
                //Log.i("adsListData", "" + adsListData.toString() + "........");
                postsListData.clear();
                List<Posts> tuijianpost = getInstance(getContext()).tuijian_findall();
                postsListData.addAll(tuijianpost);
                // Log.i("Postes", tuijianpost.toString());
            }


            if (adsListData.size() == 0) {
                relayout.setVisibility(View.GONE);
            } else {
                relayout.setVisibility(View.VISIBLE);
            }
            if (lunboadapter == null) {
//                Log.d("lizisong", "create lunboadapter");
                lunboadapter = new LunboAdapters(getActivity(), handler, adsListData);
                vp_pager.setAdapter(lunboadapter);
            } else {
                lunboadapter.notifyDataSetChanged();
            }
            if (MyitemAdapter == null) {
                MyitemAdapter = new PullrefreshAdapter(getActivity(), postsListData, channelName, mytitle);
                //vp_pager.setOffscreenPageLimit(1);
                listView.setAdapter(MyitemAdapter);
            } else {
                MyitemAdapter.notifyDataSetChanged();
                listView.setPullDownToRefreshFinish();
            }

//        } catch (Exception e) {
//        }
    }
}
