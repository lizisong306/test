package fragment;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.maidiantech.BackHandledFragment;
import com.maidiantech.DetailsActivity;
import com.maidiantech.EquipmentActivity;
import com.maidiantech.MainActivity;
import com.maidiantech.NewProJect;
import com.maidiantech.NewProjectActivity;
import com.maidiantech.NewRenCaiDetail;
import com.maidiantech.NewRenCaiTail;
import com.maidiantech.NewSearchHistory;
import com.maidiantech.NewZhengCeActivity;
import com.maidiantech.NewZhuanliActivity;
import com.maidiantech.PatentActivity;
import com.maidiantech.PersonActivity;
import com.maidiantech.PolicyActivity;
import com.maidiantech.R;
import com.maidiantech.SheBeiActivity;
import com.maidiantech.SpecialActivity;
import com.maidiantech.UnitedStatesActivity;
import com.maidiantech.UnitedStatesDeilActivity;
import com.maidiantech.WriteXuQiu;
import com.maidiantech.XinFanAnCeShi;
import com.maidiantech.ZixunDetailsActivity;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import Port.SollectInterface;
import Util.NetUtils;
import Util.PrefUtils;
import adapter.kejikuAdapter;
import application.MyApplication;
import entity.Codes;
import entity.Datas;
import entity.Posts;
import view.MyClickListener;
import view.RefreshListView;
import view.SystemBarTintManager;

import static dao.Sqlitetions.getInstance;

/**
 * Created by lizisong on 2017/3/20.
 */

public class kejiku extends BackHandledFragment implements SollectInterface{
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
    SystemBarTintManager tintManager ;
    kejikuAdapter Recomment;
    HashMap<String, String > hashMap = new HashMap<>();
    TextView xiangm_info,zhengce_info,rencai_info,shebei_info,shengyanshe_info,zhuanli_info;
    TextView search;
    private  String   ips;
    RelativeLayout search_lay;
    ImageView dingwei_id,yingyin,nodata;
    LinearLayout dingwei_lay,id;
    TextView tip1,tip2,tip3;
    int socllstate = 0;
    int count=0;
    int lastindex = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.kejiku_frame, null);
        }
        if (!flag) {
            flag = true;
            listView = (RefreshListView) view.findViewById(R.id.listview);
//            if(MainActivity.hasSoftKeys(getActivity().getWindowManager())){
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) listView.getLayoutParams();
//                layoutParams.bottomMargin=MyApplication.navigationbar+150;
//                listView.setLayoutParams(layoutParams);
//            }
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
                    getjsons(Size, pubdate,true);
                    handler.sendEmptyMessageDelayed(2, 5000);
                }
            });
            progress.setVisibility(View.VISIBLE);
            /**
             * 轮播图布局
             */
            mHomePageHeaderView = getLayoutInflater(savedInstanceState).inflate(R.layout.keji_header, null);
            initView();
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

//                    return 0;
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
        zhengce_info = (TextView)mHomePageHeaderView.findViewById(R.id.zhengce_info);
        rencai_info = (TextView)mHomePageHeaderView.findViewById(R.id.rencai_info);

        tip1 = (TextView)mHomePageHeaderView.findViewById(R.id.tip1);
        tip2 = (TextView)mHomePageHeaderView.findViewById(R.id.tip2);
        tip3 = (TextView)mHomePageHeaderView.findViewById(R.id.tip3);

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
        search  = (TextView) mHomePageHeaderView.findViewById(R.id.search);
        search.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                Intent intent = new Intent(getActivity(), NewSearchHistory.class);
                startActivity(intent);
            }
        });

//        xiangm_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
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

//        zhengce_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), PolicyActivity.class);
//                startActivity(intent);
//            }
//        });
        zhengce_info.setOnTouchListener(new MyClickListener(new MyClickListener.MyClickCallBack() {
            @Override
            public void oneClick() {
                Intent intent = new Intent(getActivity(), PolicyActivity.class);
                startActivity(intent);
            }

            @Override
            public void doubleClick() {

            }
        }));

//        rencai_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WriteXuQiu.entry_address = 3;
//                Intent intent = new Intent(getActivity(), PersonActivity.class);
//                startActivity(intent);
//            }
//        });
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
//        shebei_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                WriteXuQiu.entry_address = 5;
//                Intent intent = new Intent(getActivity(), EquipmentActivity.class);
//                startActivity(intent);
//            }
//        });
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
//        shengyanshe_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), UnitedStatesActivity.class);
//                startActivity(intent);
//            }
//        });
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
//        zhuanli_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), PatentActivity.class);
//                startActivity(intent);
//            }
//        });
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

        listView.addHeaderView(mHomePageHeaderView, null, false);
        netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
            // TypefaceUtil.replaceFont(getActivity(), "fonts/font.ttf");
            progress.setVisibility(View.GONE);
            updatedata();
        } else {
            Load();
        }
        listView.setPullDownToRefreshable(true);
        listView.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            @Override
            public void pullDownToRefresh() {
                netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    updatedata();
                    Toast.makeText(getActivity(), "网络不给力", Toast.LENGTH_SHORT).show();
                    listView.setPullDownToRefreshFinish();

                } else {
                    pubdate = "";
                    sortid="";
                    getjsons(Size, pubdate,true);
                    handler.sendEmptyMessageDelayed(2, 5000);
                }
            }
        });
        listView.setInterface(this);

        listView.setPullUpToRefreshable(true);
        listView.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
                netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    postsListData.clear();
                    List<Posts> tjanpost = getInstance(getContext()).kejiku_findmore();
                    postsListData.addAll(tjanpost);

                    if(Recomment==null){
                        Recomment=new kejikuAdapter(getActivity(), postsListData, channelName, mytitle);
                        listView.setAdapter(Recomment);
                    }else{
                        Recomment.notifyDataSetChanged();
                    }
                }else {

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
                        sortid = postsListData.get(postsListData.size() - 1).getId();
                        getjsons(Size, pubdate,false);
                        Message msgs=Message.obtain();
                        msgs.what=2;
                        dismissDialog.sendMessageDelayed(msgs,5000);
                    }
                }
            }
        });
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                switch (scrollState) {
//                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
//                        ImageLoader.getInstance().pause();
////                        if(lastindex == 0){
////                            setAniationShow();
////                        }
//                        break;
//                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
//                        ImageLoader.getInstance().resume();
////                        if(lastindex == 0){
////                            setAniationShow();
////                        }
//                        break;
//                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
////                        if(lastindex == 0){
////                            setAniationShow();
////                        }
//                        break;
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if(totalItemCount >= postsListData.size() &&  (netWorkType == NetUtils.NETWORKTYPE_INVALID)){
//                    listView.setPullUpToRefreshFinish();
//                }
////                if(lastindex != firstVisibleItem){
//                float y = getScrollY();
////                Log.d("lizisong", "firstVisibleItem"+firstVisibleItem);
//
////                if(lastindex != firstVisibleItem){
////                    Log.d("lizisong", "firstVisibleItem"+firstVisibleItem);
////                    lastindex = firstVisibleItem;
////                    if(y>0){
////                        setAniationHide();
////                    }else{
////                        setAniationShow();
////                    }
////                }
////                    if(firstVisibleItem >= 1){
////                        setAniationHide();
////                    }else {
////                        setAniationShow();
////                    }
////                }
//
//            }
//        });

    }
//    public int getScrollY() {
//        View c = listView.getChildAt(0);
//        if (c == null) {
//            return 0;
//        }
//        int firstVisiblePosition = listView.getFirstVisiblePosition();
//        int top = c.getTop();
//        return -top + firstVisiblePosition * c.getHeight() ;
//    }

    /**
         * 跟新数据
         */
    private void updatedata() {
        try {
            postsListData.clear();
            List<Posts> dfpost = getInstance(getContext()).kejiku_findall();
            postsListData.addAll(dfpost);
            if(Recomment==null){
                Recomment=new kejikuAdapter(getActivity(), postsListData, channelName, mytitle);
                listView.setAdapter(Recomment);
            }else{
                Recomment.notifyDataSetChanged();
                listView.setPullDownToRefreshFinish();
                listView.setPullUpToRefreshFinish();
            }

        } catch (Exception e) {
        }


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

    private class MyHandler extends Handler{
        WeakReference<kejiku> weakReference;

        public MyHandler(kejiku fragment) {
            weakReference = new WeakReference<kejiku>(fragment);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
             kejiku frament=weakReference.get();
             if(frament == null){
                 return;
             }
            try {
                if(msg.what == 3){
                    if(msg.arg1 == 1){
                        hashMap.clear();
                        postsListData.clear();
                    }
                    Gson gs = new Gson();
                    jsons = (String) msg.obj;
                    Codes codes = gs.fromJson(jsons, Codes.class);
                    data = codes.getData();
//                    adsListData.clear();
//                    adsListData = data.getAds();
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
                        tip1.setText("钛领科技库为您整合了 ");
                        if(data.count != null){
                           tip2.setText(addComma(data.count)+"");
                        }
                        tip3.setText(" 项顶尖科技资源");
                    }

                    nodata.setVisibility(View.GONE);

                    if(Recomment==null){
                        Recomment=new kejikuAdapter(getActivity(), postsListData, channelName, mytitle);
                        listView.setAdapter(Recomment);
                    }else{
                        Recomment.notifyDataSetChanged();
                        listView.setPullDownToRefreshFinish();
                        listView.setPullUpToRefreshFinish();
                    }
//                    isanimone = true;

//                    for (int i = 0; i < postsListData.size(); i++) {
//                        Posts p = new Posts();
//                        p.setId(postsListData.get(i).getId());
//                        p.setLitpic(postsListData.get(i).getLitpic());
//                        p.setImageState(postsListData.get(i).imageState);
//                        p.setImage(postsListData.get(i).image);
//                        p.setTitle(postsListData.get(i).getTitle());
//                        p.setSortTime(postsListData.get(i).getSortTime());
//                        p.setPubdate(postsListData.get(i).getPubdate());
//                        p.setDescription(postsListData.get(i).getDescription());
//                        p.setSource(postsListData.get(i).getSource());
//                        p.setZan(postsListData.get(i).getZan());
//                        p.setClick(postsListData.get(i).getClick());
//                        p.setTags(postsListData.get(i).getTags());
//                        p.setUnit(postsListData.get(i).getUnit());
//                        p.setState(postsListData.get(i).getState());
//                        p.setTypename(postsListData.get(i).getTypename());
//                        p.setChannel("kejiku");
//                        Sqlitetions.getInstance(getContext()).kejiku_adds(p);
////                                Log.i("tj",p.toString());
//                    }


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
                                }else {
                                    String name = postsListData.get(position - 2).getTypename();
                                    String typeid = postsListData.get(position - 2).typeid;
                                    if(name != null &&(name.equals("资讯")||name.equals("推荐轮播图")|| name.equals("推荐") || name.equals("活动")|| name.equals("知识"))){
                                        Intent intent = new Intent(getActivity(), ZixunDetailsActivity.class);
                                        intent.putExtra("id", postsListData.get(position - 2).getId());
                                        intent.putExtra("name", postsListData.get(position - 2).getTypename());
                                        intent.putExtra("typeid", postsListData.get(position - 2).typeid);
                                        intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
                                        startActivity(intent);

                                    }else if(typeid.equals("8")){
                                        Intent intent = new Intent(getActivity(), UnitedStatesDeilActivity.class);
                                        intent.putExtra("aid",postsListData.get(position - 2).getId() );
                                        startActivity(intent);
                                   }else if(typeid.equals("4")){
                                        Intent intent = new Intent(getActivity(), XinFanAnCeShi.class);
                                        intent.putExtra("aid", postsListData.get(position - 2).getId());
                                        startActivity(intent);
                                    }else if(typeid.equals("2")){
                                        Intent intent = new Intent(getActivity(), NewProjectActivity.class);
                                        intent.putExtra("aid", postsListData.get(position - 2).getId());
                                        startActivity(intent);
                                    }else if(typeid.equals("5")){
                                        Intent intent = new Intent(getActivity(), NewZhuanliActivity.class);
                                        intent.putExtra("aid", postsListData.get(position - 2).getId());
                                        startActivity(intent);
                                    }else if(typeid.equals("6")){
                                        Intent intent = new Intent(getActivity(), NewZhengCeActivity.class);
                                        intent.putExtra("aid", postsListData.get(position - 2).getId());
                                        startActivity(intent);
                                    }else{
                                        Intent intent = new Intent(getActivity(), DetailsActivity.class);
                                        intent.putExtra("id", postsListData.get(position - 2).getId());
                                        intent.putExtra("typeid", postsListData.get(position - 2).typeid);
                                        intent.putExtra("name", postsListData.get(position - 2).getTypename());
                                        intent.putExtra("pic", postsListData.get(position - 2).getLitpic());
                                        startActivity(intent);
                                    }
                                }
                            } catch (IndexOutOfBoundsException ex) {
                                nodata.setVisibility(View.VISIBLE);

                            } catch (Exception e) {
                                nodata.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }catch (Exception e){

            }

        }
    }

    MyHandler handler = new MyHandler(this);
    DismissDialog dismissDialog = new DismissDialog(this);
    private class DismissDialog extends Handler{
        WeakReference<kejiku> weakReference;
        public DismissDialog (kejiku fragment){
            weakReference = new WeakReference<kejiku>(fragment);
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
        zhengce_info.setClickable(true);
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
            getjsons(Size, pubdate,false);
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


}

