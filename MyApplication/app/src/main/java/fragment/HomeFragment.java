package fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLinearLayout;
import com.google.gson.Gson;
import com.maidiantech.DetailsActivity;
import com.maidiantech.R;
import com.maidiantech.ZixunDetailsActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.android.tpush.XGPushManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import Util.NetUtils;
import Util.OkHttpUtils;
import Util.UIHelper;
import adapter.LunboAdapters;
import adapter.PullrefreshAdapter;
import adapter.RecommendAdapter;
import application.MyApplication;
import dao.Sqlitetions;
import entity.ADS;
import entity.Codes;
import entity.Datas;
import entity.Posts;
import entity.Sqlhuacun;
import view.RefreshListView;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.maidiantech.R.id.listview;
import static dao.Sqlitetions.getInstance;


/**
 * 首页界面
 * Created by lizisong on 2016/12/8.
 */

public class HomeFragment extends BaseFragment{

    private View view;
    private ViewPager vp_pager;
    private String str;
    private Long TimeMillis;
    private String pubdate;
    private List<Sqlhuacun> findall;
    private List<Posts> postses;
    private FrameLayout layout;
    private RelativeLayout relayout;
    private  int screenWidth;
    private RefreshListView listView;
    private boolean flag = false;
    private View mHomePageHeaderView;
    private String channelName;
    private LinearLayout ll_dots;
    private ArrayList<ImageView> imageList;
    private OkHttpUtils Okhttp;
    public  List<ADS> adsListData = new ArrayList<>();
    private List<ADS> tuijian = new ArrayList<>();
    private List<ADS> zixun = new ArrayList<>();
    private List<Posts> postsListData = new ArrayList<>();
    private Datas data;
    private int Size = 10;
    private LunboAdapters lunboadapter;
    private PullrefreshAdapter MyitemAdapter;
    private String mytitle;
    private String jsons;
    private boolean mHasLoadedOnce;
    RecommendAdapter Recomment;
    int netWorkType = 0;
    private  String   ips;
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.home_fragment, null);
            //TypefaceUtil.replaceFont(getActivity(), "fonts/font.ttf");
        }
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        if (!flag) {
             flag = true;
             listView = (RefreshListView) view.findViewById(listview);
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
                //updatedata();
            } else {
                // UIHelper.showDialogForLoading(getActivity(), "正在加载...", true);
            }
            listView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);

                    return imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }


            });
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

                            updatedata();
                            Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                            listView.setPullDownToRefreshFinish();
//                        psc.onRefreshComplete();
                        } else {
                            adsListData.clear();
                            postsListData.clear();
                            pubdate = "";
                            getjsons(Size, pubdate);
//                        psc.onRefreshComplete();
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

//                        Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                        if (channelName == "推荐") {
                            postsListData.clear();
                            List<Posts> tjanpost = getInstance(getContext()).tj_findmore();
                            postsListData.addAll(tjanpost);
                        }
                        if (channelName == "资讯") {
                            postsListData.clear();
                            List<Posts> tuijianpost = getInstance(getContext()).tuijian_findmore();
                            postsListData.addAll(tuijianpost);
                        }
                        if (channelName == "政策") {
                            postsListData.clear();
                            List<Posts> zcpost = getInstance(getContext()).zhengce_findmore();
                            postsListData.addAll(zcpost);
                        }
                        if (channelName == "项目") {
                            postsListData.clear();
                            List<Posts> xmnpost = getInstance(getContext()).xm_findmore();
                            postsListData.addAll(xmnpost);
                        }
                        if (channelName == "设备") {
                            postsListData.clear();
                            List<Posts> shebeipost = getInstance(getContext()).shebei_findmore();
                            postsListData.addAll(shebeipost);
                        }
                        if (channelName == "研究所") {
                            postsListData.clear();
                            List<Posts> syspost = getInstance(getContext()).sys_findmore();
                            postsListData.addAll(syspost);
                        }
                        if (channelName == "专利") {
                            postsListData.clear();
                            List<Posts> zhuanlipost = getInstance(getContext()).zhuanli_findmore();
                            postsListData.addAll(zhuanlipost);
                        }
                        if (channelName == "专家") {
                            postsListData.clear();
                            List<Posts> rencaipost = getInstance(getContext()).rencai_findmore();
                            postsListData.addAll(rencaipost);
                            // Log.i("Pos", rencaipost.toString());
                        }

                        // lunboAdapters();
                        if(channelName == "推荐"){
                            if(Recomment==null){
                                Recomment=new RecommendAdapter(getActivity(), postsListData, channelName, mytitle);
                                listView.setAdapter(Recomment);
                            }else{

                                Recomment.notifyDataSetChanged();
                                listView.setPullDownToRefreshFinish();
                                listView.setPullUpToRefreshFinish();
                            }

                        }else{
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
                        }


//                        listView.setPullUpToRefreshable(false);

                    } else {
                        // TimeMillis = System.currentTimeMillis();
                        if(postsListData.size()==0){
//                            psc.onRefreshComplete();
                        }else{

                            if (postsListData.get(postsListData.size() - 1).getResult() != null && postsListData.get(postsListData.size() - 1).getResult().equals("no")) {

//                                Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
//                                listView.setPullUpToRefreshable(false);
                                listView.setPullUpToRefreshFinish();
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
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {

                        String name = postsListData.get(position-2).getTypename();
                        if(name != null && (name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                            Intent intent = new Intent(getActivity(), ZixunDetailsActivity.class);
                            intent.putExtra("id", postsListData.get(position-2).getId());
                            intent.putExtra("name", postsListData.get(position-2).getTypename());
                            intent.putExtra("pic", postsListData.get(position-2).getLitpic());
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(getActivity(), DetailsActivity.class);
                            intent.putExtra("id", postsListData.get(position-2).getId());
                            intent.putExtra("name", postsListData.get(position-2).getTypename());
                            intent.putExtra("pic", postsListData.get(position-2).getLitpic());
                            startActivity(intent);
                        }


                    }catch (IndexOutOfBoundsException ex){

                    }catch (Exception e){
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
//                            MyitemAdapter.setDisplayImage(false);
                            if(channelName == "资讯"){
                                stopLunBo();
                            }else if(channelName == "推荐"){
                                stopLunBo();
                            }

                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

//                            ImageLoader.getInstance().clearDiskCache();
//                            ImageLoader.getInstance().clearMemoryCache();
//                            MyitemAdapter.setDisplayImage(true);
                           ImageLoader.getInstance().resume();


                            if(channelName == "资讯"){
                                startLunBo();
                            }else if(channelName == "推荐"){
                                startLunBo();
                            }
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:

//                            ImageLoader.getInstance().pause();
                            if(channelName == "资讯"){
                                stopLunBo();
                            }else if(channelName == "推荐"){
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
//        MobclickAgent.onPageEnd("MainScreen");
    }
    protected void lazyLoad(){
        if (!isVisible || mHasLoadedOnce) {
            return;
        }
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            updatedata();
            Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            UIHelper.hideDialogForLoading();
            UIHelper.showDialogForLoading(getActivity(),"", true);
            getjsons(Size, pubdate);
        }
    }

    /**
     * 跟新数据
     */
    private void updatedata() {
        try {

            if (channelName == "推荐") {
                adsListData.clear();
                List<ADS> tjList = getInstance(getContext()).tjfindalls();
                adsListData.addAll(tjList);
                postsListData.clear();
                List<Posts> tjpost = getInstance(getContext()).tj_findall();
                postsListData.addAll(tjpost);

            }
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
            if (channelName == "政策") {
                adsListData.clear();
                List<ADS> zcList = getInstance(getContext()).zcfindlunbo();
                adsListData.addAll(zcList);
                postsListData.clear();
                List<Posts> zcpost = getInstance(getContext()).zhengce_findall();
                postsListData.addAll(zcpost);
                // Log.i("Pos", zcpost.toString());
            }
            if (channelName == "项目") {
                adsListData.clear();
                List<ADS> xmList = getInstance(getContext()).xmfindalls();
                adsListData.addAll(xmList);

                postsListData.clear();
                List<Posts> xmnpost = getInstance(getContext()).xm_findall();
                postsListData.addAll(xmnpost);
            }
            if (channelName == "设备") {
                postsListData.clear();
                List<Posts> shebeipost = getInstance(getContext()).shebei_findall();
                postsListData.addAll(shebeipost);
                //Log.i("Pos", shebeipost.toString());
            }
            if (channelName == "研究所") {
                postsListData.clear();
                List<Posts> syspost = getInstance(getContext()).sys_findall();
                postsListData.addAll(syspost);
                //Log.i("Pos", shebeipost.toString());
            }
            if (channelName == "专家") {
                postsListData.clear();
                List<Posts> rencaipost = getInstance(getContext()).rencai_findall();
                postsListData.addAll(rencaipost);
                //Log.i("Pos", rencaipost.toString());
            }
            if (channelName == "专利") {
                postsListData.clear();
                List<Posts> zhuanlipost = getInstance(getContext()).zhuanli_findall();
                postsListData.addAll(zhuanlipost);

            }

            if (adsListData.size() == 0) {
                relayout.setVisibility(View.GONE);
            } else {
                relayout.setVisibility(View.VISIBLE);
            }
            if (lunboadapter == null) {

                lunboadapter = new LunboAdapters(getActivity(), handler, adsListData);
                vp_pager.setAdapter(lunboadapter);
            } else {
                lunboadapter.notifyDataSetChanged();
            }
            if(channelName=="推荐"){
                if(Recomment==null){
                    Recomment=new RecommendAdapter(getActivity(), postsListData, channelName, mytitle);
                    listView.setAdapter(Recomment);
                }else{

                    Recomment.notifyDataSetChanged();
                    listView.setPullDownToRefreshFinish();
                    listView.setPullUpToRefreshFinish();
                }
            }
            else if (MyitemAdapter == null) {
                MyitemAdapter = new PullrefreshAdapter(getActivity(), postsListData, channelName, mytitle);
                //vp_pager.setOffscreenPageLimit(1);
                listView.setAdapter(MyitemAdapter);
            } else {
                MyitemAdapter.notifyDataSetChanged();
                listView.setPullDownToRefreshFinish();
            }
        } catch (Exception e) {
        }
    }


    /**
     * handler更新UI
     */
    private HomeFragment.MyHandler handler = new HomeFragment.MyHandler(this);
    private Handler dismissDialog= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UIHelper.hideDialogForLoading();
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
        WeakReference<HomeFragment> weakReference;

        public MyHandler(HomeFragment fragment) {
            weakReference = new WeakReference<HomeFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            try {

                if (weakReference.get() != null) {
                    if (msg.what == 0 & adsListData.size() != 1) {
                        //获取当前所在页面索引值

                        int item = vp_pager.getCurrentItem();
                        item++;
                        vp_pager.setCurrentItem(item);
                        handler.removeCallbacksAndMessages(null);
                        handler.sendEmptyMessageDelayed(0, 3000);
                    }
                    if (msg.what == 3) {
                        Gson gs = new Gson();
                        jsons = (String) msg.obj;
                        Codes codes = gs.fromJson(jsons, Codes.class);
                        data = codes.getData();
                        adsListData = data.getAds();
                        if (lunboadapter == null) {
                            lunboadapter = new LunboAdapters(getActivity(), handler, adsListData);
                            vp_pager.setAdapter(lunboadapter);
                            if (adsListData.size() != 1) {
                                initPaint();
                                vp_pager.setCurrentItem(adsListData.size() * 5000);
                            }
                        } else {
                            lunboadapter.notifyDataSetChanged();
                        }
                        postsListData.addAll(data.getPosts());
                        if (adsListData.size() == 0) {
                            relayout.setVisibility(View.GONE);
                        } else {
                            relayout.setVisibility(View.VISIBLE);
                        }
                        if(channelName=="推荐"){

                            if(Recomment==null){
//                                Log.i("lizisong","@@@@@@@@@@@@@@@@@");
//                                for(int i=0; i<postsListData.size();i++){
//                                    Posts item = postsListData.get(i);
//                                    if(item.getTypename().equals("推荐")){
//                                        postsListData.remove(item);
//                                    }
//                                }
                                Recomment=new RecommendAdapter(getActivity(), postsListData, channelName, mytitle);
                                listView.setAdapter(Recomment);
                            }else{
//                                Log.i("lizisong","@uuuuuuuu");
//                                for(int i=0; i<postsListData.size();i++){
//                                    Posts item = postsListData.get(i);
//                                    if(item.getTypename().equals("推荐")){
//                                        postsListData.remove(item);
//                                    }
//                                }
                                Recomment.notifyDataSetChanged();
                                listView.setPullDownToRefreshFinish();
                                listView.setPullUpToRefreshFinish();
                            }
                        }else{

                            if (MyitemAdapter == null) {
                                MyitemAdapter = new PullrefreshAdapter(getActivity(), postsListData, channelName, mytitle);
                                listView.setAdapter(MyitemAdapter);
                            } else {
                                MyitemAdapter.notifyDataSetChanged();
                            }
                        }
                        if (msg.what == 2) {
                            MyitemAdapter.notifyDataSetChanged();
                        }
                        if(channelName=="推荐"){
                            for (int i = 0; i < adsListData.size(); i++) {
                                ADS s = new ADS(adsListData.get(i).getAid(), adsListData.get(i).getPicUrl(), adsListData.get(i).getTitle(), adsListData.get(i).getPubdate());
                                getInstance(getContext()).collect_tj(s);
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
                                p.setUnit(postsListData.get(i).getUnit());
                                p.setState(postsListData.get(i).getState());
                                p.setTypename(postsListData.get(i).getTypename());
                                p.setChannel("tj");
                                Sqlitetions.getInstance(getContext()).tj_adds(p);

                            }
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
                                p.setChannel("tuijian");
                                Sqlitetions.getInstance(getContext()).tuijian_adds(p);
                                //  Log.i("sql_title",p.toString());
                            }
                        }
                        if (channelName == "政策") {
                            for (int z = 0; z < adsListData.size(); z++) {
                                ADS s1 = new ADS(adsListData.get(z).getAid(), adsListData.get(z).getPicUrl(), adsListData.get(z).getTitle(), adsListData.get(z).getPubdate());
                                getInstance(getContext()).zc_addlunbo(s1);
                                //  Log.i("sqli",s1.toString());
                            }
                            for (int j = 0; j < postsListData.size(); j++) {
                                Posts p1 = new Posts();
//                                postsListData.get(j).getId(), postsListData.get(j).getLitpic(), postsListData.get(j).getTitle(), postsListData.get(j).getPubdate(),postsListData.get(j).getDescription(),postsListData.get(j).getSource()
                                p1.setId(postsListData.get(j).getId());
                                p1.setTitle(postsListData.get(j).getTitle());
                                p1.setSortTime(postsListData.get(j).getSortTime());
                                p1.setDescription(postsListData.get(j).getDescription());
                                p1.setUnit(postsListData.get(j).getUnit());
                                p1.setClick(postsListData.get(j).getClick());
                                p1.setTags(postsListData.get(j).getTags());
                                p1.setChannel("zhengce");
                                getInstance(getContext()).zhengce_adds(p1);
                                // Log.i("sql",p1.toString());
                            }
                        }
                        if (channelName == "项目") {
                            for (int j = 0; j < adsListData.size(); j++) {
                                ADS xm = new ADS(adsListData.get(j).getAid(), adsListData.get(j).getPicUrl(), adsListData.get(j).getTitle(), adsListData.get(j).getPubdate());
                                getInstance(getContext()).xm_addlunbo(xm);
                                //  Log.i("xmlunbo",xm.toString());
                            }
                            for (int j = 0; j < postsListData.size(); j++) {
                                Posts p2 = new Posts();
//                                postsListData.get(j).getId(), postsListData.get(j).getLitpic(), postsListData.get(j).getTitle(), postsListData.get(j).getPubdate(),postsListData.get(j).getDescription(),postsListData.get(j).getSource()
                                p2.setId(postsListData.get(j).getId());
                                p2.setLitpic(postsListData.get(j).getLitpic());
                                p2.setTitle(postsListData.get(j).getTitle());
                                p2.setSortTime(postsListData.get(j).getSortTime());
                                p2.setClick(postsListData.get(j).getClick());
                                p2.setTags(postsListData.get(j).getTags());
                                p2.setChannel("xiangmu");
                                getInstance(getContext()).xm_adds(p2);

                            }
                        }
                        if (channelName == "设备") {
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
                                p4.setChannel("shebei");
                                getInstance(getContext()).shebei_adds(p4);
                                // Log.i("p4",p4.toString());
                            }
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
                                p4.setChannel("shiyanshi");
                                getInstance(getContext()).sys_add(p4);
                                // Log.i("p4",p4.toString());
                            }
                        }
                        if (channelName == "专利") {
                            for (int j = 0; j < postsListData.size(); j++) {
                                Posts p5 = new Posts();
//                                postsListData.get(j).getId(), postsListData.get(j).getLitpic(), postsListData.get(j).getTitle(), postsListData.get(j).getPubdate(),postsListData.get(j).getDescription(),postsListData.get(j).getSource()
                                p5.setId(postsListData.get(j).getId());
                                p5.setTitle(postsListData.get(j).getTitle());
                                p5.setSortTime(postsListData.get(j).getSortTime());
                                p5.setState(postsListData.get(j).getState());
                                p5.setTags(postsListData.get(j).getTags());
                                p5.setClick(postsListData.get(j).getClick());
                                p5.setChannel("zhuanli");
                                getInstance(getContext()).zhuanli_adds(p5);

                            }
                        }
                        if (channelName == "专家") {
                            for (int j = 0; j < postsListData.size(); j++) {
                                Posts p3 = new Posts();
                                p3.setId(postsListData.get(j).getId());
                                p3.setLitpic(postsListData.get(j).getLitpic());
                                p3.setUsername(postsListData.get(j).getUsername());
                                p3.setSortTime(postsListData.get(j).getSortTime());
                                p3.setResult(postsListData.get(j).getResult());
                                p3.setChannel(postsListData.get(j).getChannel());
                                p3.setTypename(postsListData.get(j).getTypename());
                                p3.setDescription(postsListData.get(j).getDescription());
                                p3.setRank(postsListData.get(j).getRank());
                                p3.setTags(postsListData.get(j).getTags());
                                p3.setZan(postsListData.get(j).getZan());
                                p3.setClick(postsListData.get(j).getClick());
                                p3.setChannel("rencai");
                                getInstance(getContext()).rencai_adds(p3);
                                //  Log.i("p5",p3.toString());
                            }
                        }

                        lunboadapter.notifyDataSetChanged();

                        MyitemAdapter.notifyDataSetChanged();
                        listView.setPullDownToRefreshFinish();
                        listView.setPullUpToRefreshFinish();
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
     * 从接口获取数据
     * @param Size
     * @param pubdate
     */
    public void getjsons(final int Size, final String pubdate) {

        Okhttp =OkHttpUtils.getInstancesOkHttp();
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

                try {
                    if(channelName=="推荐"){
                       jsons = Okhttp.loaudstringfromurl("http://"+ips+"/api/arc_index.php?pageSize=" + Size + "&LastModifiedTime=" + pubdate + "&typeid=9");

                    }
                    if (channelName == "资讯") {
                        jsons = Okhttp.loaudstringfromurl("http://"+ips+"/api/arc_index.php?pageSize=" + Size + "&LastModifiedTime=" + pubdate + "&typeid=1");
                        // Log.i("jsons",jsons);
                    }
                    if (channelName == "政策") {
                        jsons = Okhttp.Myokhttpclient("http://"+ips+"/api/arc_index.php?pageSize=" + Size + "&LastModifiedTime=" + pubdate + "&typeid=6");
                    }
                    if (channelName == "项目") {
                        jsons = Okhttp.Myokhttpclient("http://"+ips+"/api/arc_index.php?pageSize=" + Size + "&LastModifiedTime=" + pubdate + "&typeid=2");

                    }
                    if (channelName == "专家") {
                        jsons = Okhttp.Myokhttpclient("http://"+ips+"/api/arc_index.php?pageSize=" + Size + "&LastModifiedTime=" + pubdate + "&typeid=4");

                    }
                    if (channelName == "设备") {
                        jsons = Okhttp.Myokhttpclient("http://"+ips+"/api/arc_index.php?pageSize=" + Size + "&LastModifiedTime=" + pubdate + "&typeid=7");
                    }
                    if (channelName == "专利") {
                        jsons = Okhttp.Myokhttpclient("http://"+ips+"/api/arc_index.php?pageSize=" + Size + "&LastModifiedTime=" + pubdate + "&typeid=5");
                    }
                    if(channelName=="研究所"){
                        jsons = Okhttp.Myokhttpclient("http://"+ips+"/api/arc_index.php?pageSize=" + Size + "&LastModifiedTime=" + pubdate + "&typeid=8");
                    }
                    mHasLoadedOnce = true;
                    //关闭对话框
                    // Log.i("jsons",jsons);
                    Message msg = new Message();
                    msg.obj = jsons;
                    msg.what = 3;

//                    Thread.sleep(1000);
//                    UIHelper.hideDialogForLoading();
                     handler.sendMessage(msg);
                     Message message = Message.obtain();
                     message.what = 0;
                     dismissDialog.sendMessageDelayed(message, 1000);

                } catch (Exception e) {
                    Message msg=Message.obtain();
                    msg.what=1;
                    dismissDialog.sendMessage(msg);
                }
            }
        }.start();
    }
    @Override
    public void onResume() {
        super.onResume();
        XGPushManager.onActivityStarted(getActivity());
//        psc.onRefreshComplete();
        handler.sendEmptyMessageDelayed(0, 5000);
        listView.setPullDownToRefreshFinish();
        XGPushManager.onActivityStarted(getActivity());
//        MobclickAgent.onPageStart("MainScreen"); //统计页面，"MainScreen"为页面名称，可自定义
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
    /**
     * 消除有可能出现下拉刷新的界面
     */
    public void canelDownRefrsh(){
        listView.setPullDownToRefreshFinish();
    }
}
