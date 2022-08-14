package fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.maidiantech.BackHandledFragment;
import com.maidiantech.MainActivity;
import com.maidiantech.MyloginActivity;
import com.maidiantech.NewResultActivity;
import com.maidiantech.PulseActivity;
import com.maidiantech.R;
import com.maidiantech.ResultActivity;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import Util.DeviceUtils;
import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import application.MyApplication;
import dao.Service.PulseCondtion;
import dao.dbentity.PulseData;
import entity.Ret;
import entity.RetPulseData;
import view.BTAlertDialog;

/**
 * 欢迎进入把脉界面
 * Created by lizisong on 2016/12/12.
 */

public class WelcomePulse extends BackHandledFragment {

    private ViewPager mViewPager;
    private RelativeLayout mViewPagerContainer;

    public PulseCondtion pulseCondtion;
    public List<PulseData> listData;
    public static boolean welcome_state = false;
    public static int current_index = 0;
    public SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");

    public static PulseData mItem = null;
    public static boolean mFirst = false;
    public static final String resultBroaderAction = "CHANGE_BACKGROUD";
    ResultBroader mBroader;
    MyPagerAdapter myPagerAdapter;
    private OkHttpUtils Okhttp;
    private String loginState;
    private  String   ips;
    public static  boolean LoginState = false;

    MyOnPageChangeListener pageChangeListener = new MyOnPageChangeListener();
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test, null);
        mViewPager= (ViewPager) view.findViewById(R.id.viewpager);
        mViewPagerContainer= (RelativeLayout) view.findViewById(R.id.viewPagerContainer);
        pulseCondtion = PulseCondtion.getInstance(getContext());
        listData = pulseCondtion.get();
        Okhttp = OkHttpUtils.getInstancesOkHttp();
        ips = MyApplication.ip;
        loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
        if(!loginState.equals("1")||listData.size() == 0){
            welcome_state = true;
            PulseData item = new PulseData();
            item.typeid ="-1";
            listData.add(item);
            mItem = item;
        }else{
            welcome_state = false;
        }
        mBroader = new ResultBroader();
        myPagerAdapter = new MyPagerAdapter();
        initViewPager();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFirst = false;
        IntentFilter filter = new IntentFilter();
        filter.addAction(resultBroaderAction);
        getActivity().registerReceiver(mBroader, filter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mBroader);
    }

    private void initViewPager() {
        //设置ViewPager的布局
        RelativeLayout.LayoutParams params;
        if(!welcome_state){
             params=new RelativeLayout.LayoutParams(
                    DeviceUtils.getWindowWidth(getActivity())*6/10,
                    DeviceUtils.getWindowHeight(getActivity())*6/10);
        }else{
            params=new RelativeLayout.LayoutParams(
                    DeviceUtils.getWindowWidth(getActivity())*6/8,
                    DeviceUtils.getWindowHeight(getActivity())*6/8-60);
        }
        /**** 重要部分  ******/
        //clipChild用来定义他的子控件是否要在他应有的边界内进行绘制。 默认情况下，clipChild被设置为true。 也就是不允许进行扩展绘制。
        mViewPager.setClipChildren(false);
        //父容器一定要设置这个，否则看不出效果
        mViewPagerContainer.setClipChildren(false);

        mViewPager.setLayoutParams(params);
        //为ViewPager设置PagerAdapter
        mViewPager.setAdapter(myPagerAdapter);
        //设置ViewPager切换效果，即实现画廊效果
        if(!welcome_state){
             mViewPager.setPageTransformer(true, new WelcomePulse.ZoomOutPageTransformer());
        }else{
            mViewPager.setPageTransformer(true,null);
        }
        //设置预加载数量
        mViewPager.setOffscreenPageLimit(2);
        //设置每页之间的左右间隔
        if(Build.MODEL.equals("SM-G9287") || Build.MODEL.equals("SM-N9500")){
            mViewPager.setPageMargin(120);
        }else{
            mViewPager.setPageMargin(80);
        }


//        mViewPager.setCurrentItem(5000);
        mViewPager.setCurrentItem(0);
        //将容器的触摸事件反馈给ViewPager
        mViewPagerContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // dispatch the events to the ViewPager, to solve the problem that we can swipe only the middle view.
                return mViewPager.dispatchTouchEvent(event);
            }

        });
        mViewPager.setOnPageChangeListener(pageChangeListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
        current_index = 0;
           if(MainActivity.current == 2){
                listData = pulseCondtion.get();
                if(listData.size() == 0 || !loginState.equals("1")){
                    listData.clear();
                    welcome_state = true;
                    PulseData item = new PulseData();
                    item.typeid ="-1";
                    listData.add(item);
                    initViewPager();
                    ((MainActivity)getActivity()).setBackGround("-1", welcome_state);

                }else{
                    welcome_state = false;
                    initViewPager();
                    if(listData.size() == 1){
                        PulseData item =listData.get(0);
                        ((MainActivity)getActivity()).setBackGround(item.evaluetop, welcome_state);
                    }else {
                        PulseData item =listData.get(current_index);
                        ((MainActivity)getActivity()).setBackGround(item.evaluetop, welcome_state);

                    }

                }
           }
        MobclickAgent.onPageStart("把脉"); //统计页面，"MainScreen"为页面名称，可自定义

    }

    @Override
    protected boolean onBackPressed() {
        return false;
    }



    class MyPagerAdapter extends PagerAdapter {
//        int [] str={R.mipmap.pic_1,R.mipmap.pic_2,R.mipmap.pic_3,R.mipmap.pic_4,R.mipmap.pic_6,R.mipmap.pic_7};
        WeakReference<View> weakReference;
        LinkedList<View> mCaches = new LinkedList<View>();

        @Override
        public int getCount() {
            if(welcome_state || listData.size() == 1){
                return 1;
            }else{
                return listData.size();
            }

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

//           final PulseData item = listData.get(position%listData.size());
            final PulseData item = listData.get(position);
            mItem = item;
            View holder = null;
            ImageView icon = null,icon1;
            TextView title_txt;
            TextView title_info;
            ImageView arrow;
            TextView data;
            RelativeLayout delete;
            TextView start;
            if(welcome_state){
                if(Build.MODEL.equals("SM-G9287")|| Build.MODEL.equals("SM-N9500")|| MyApplication.maxHeight >MyApplication.MINHEIGHT){
                    holder = View.inflate(getContext(),R.layout.pulse_layout, null);
                }else {
                    holder = View.inflate(getContext(),R.layout.pulse_no_adapter, null);
                }
                weakReference = new WeakReference<View>(holder);
                holder = weakReference.get();
                start = (TextView)holder.findViewById(R.id.bottmon);
                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                        if(!loginState.equals("1")){
                            Intent intent=new Intent(getActivity(),MyloginActivity.class);
                            //getActivity().startActivity(intent);
                            //m.animations();
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getActivity(), PulseActivity.class);
                            startActivity(intent);
                        }

                    }
                });
            }else{
                if(Build.MODEL.equals("SM-G9287") || Build.MODEL.equals("SM-N9500") || MyApplication.maxHeight >1980){
                    holder = View.inflate(getContext(),R.layout.pulse_adapter_height, null);
                }else {
                    holder = View.inflate(getContext(),R.layout.pulse_adapter, null);
                }
                weakReference = new WeakReference<View>(holder);
                holder = weakReference.get();
                icon = (ImageView) holder.findViewById(R.id.icon);

                title_txt = (TextView)holder.findViewById(R.id.title_txt);
                title_info = (TextView)holder.findViewById(R.id.title_info);
                arrow = (ImageView)holder.findViewById(R.id.arrow);
                delete = (RelativeLayout)holder.findViewById(R.id.bottom);
                data = (TextView)holder.findViewById(R.id.data);

                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ResultActivity.evalueTitle = item.evalueTitle;
                        if(item.evalue != null &&item.evalue.length() > 0){
                            String temp = item.evalue.substring(item.evalue.length()-1,item.evalue.length());
                            if(temp.equals(",")){
                                ResultActivity.evalue = item.evalue;
                            }
                            else{
                                ResultActivity.evalue = item.evalue+",";
                            }
                        }

                        ResultActivity.province = item.province;
                        ResultActivity.evaluetop = Integer.parseInt(item.evaluetop);
                        if(item.typeid.length() > 0){
                            String temp = item.typeid.substring(item.typeid.length()-1,item.typeid.length());
                            if(temp.equals(",")){
                                ResultActivity.typeid = item.typeid;
                            }else{
                                ResultActivity.typeid = item.typeid+",";
                            }
                        }
//                        ResultActivity.typeid = item.typeid;
                        ResultActivity.category = item.category;
                        ResultActivity.mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                        Intent intent = new Intent(getActivity(), NewResultActivity.class);
                        intent.putExtra("evaluetop",ResultActivity.evaluetop+"");
                        intent.putExtra("title",item.name);
                        intent.putExtra("id",item.pid);
                        startActivity(intent);

//                        Intent intent = new Intent(getActivity(), ResultActivity.class);
//                        intent.putExtra("title", item.name);
//                        intent.putExtra("from", "WelcomePulse");
//                        startActivity(intent);
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                      final BTAlertDialog dialog = new BTAlertDialog(getActivity());
                        dialog.setTitle("您确认要删除吗？");
                        dialog.setNegativeButton("取消", null);
                        dialog.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                listData.remove(item);
                                pulseCondtion.delete(item.id);
                                if(listData.size() == 0){
                                    welcome_state = true;
                                    PulseData item = new PulseData();
                                    item.typeid ="-1";
                                    listData.add(item);
                                    mItem = item;
                                    initViewPager();
                                    ((MainActivity)getActivity()).setBackGround("-1", welcome_state);
                                }else{
                                    PulseData item =listData.get(0);
                                    initViewPager();
                                    ((MainActivity)getActivity()).setBackGround(item.evaluetop, welcome_state);
                                }

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String timestamp = System.currentTimeMillis()+"";
                                        String sign="";
                                        ArrayList<String> sort = new ArrayList<String>();
                                        sort.add("action"+"del");
                                        sort.add("mid"+item.mid);
                                        sort.add("id"+item.pid);
                                        sort.add("timestamp"+timestamp);
                                        sign= KeySort.keyScort(sort);
                                        String data = Okhttp.loaudstringfromurl("http://"+ips+"/api/setBaMaiApi.php?action=del&mid="+item.mid+"&id="+item.pid+"&sign="+sign+"&timestamp="+timestamp);
                                        Gson gs = new Gson();
                                        Ret item =gs.fromJson(data, Ret.class);
                                        if(item.code.equals("1")){
                                        }
                                    }
                                }).start();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    }
                });

                arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ResultActivity.evalueTitle = item.evalueTitle;
                        if(item.evalue.length() > 0){
                           String temp = item.evalue.substring(item.evalue.length()-1,item.evalue.length());
                            if(temp.equals(",")){
                                ResultActivity.evalue = item.evalue;
                            }
                            else{
                                ResultActivity.evalue = item.evalue+",";
                            }
                        }

                        ResultActivity.province = item.province;
                        ResultActivity.evaluetop = Integer.parseInt(item.evaluetop);
                        if(item.typeid.length() > 0){
                            String temp = item.typeid.substring(item.typeid.length()-1,item.typeid.length());
                            if(temp.equals(",")){
                                ResultActivity.typeid = item.typeid;
                            }else{
                                ResultActivity.typeid = item.typeid+",";
                            }
                        }

                        ResultActivity.category = item.category;
                        ResultActivity.mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                        Intent intent = new Intent(getActivity(), NewResultActivity.class);
                        intent.putExtra("evaluetop",ResultActivity.evaluetop+"");
                        intent.putExtra("title",item.name);
                        intent.putExtra("id", item.pid);
                        startActivity(intent);
//                        Intent intent = new Intent(getActivity(), ResultActivity.class);
//                        intent.putExtra("title", item.name);
//                        intent.putExtra("from", "WelcomePulse");
//                        startActivity(intent);
                    }
                });
//                data.setText("更新日期 : "+format.format(new Date(Long.parseLong(item.updatatime)))+"  ");
                if(item.evaluetop.equals("500")){  //电子信息
                    icon.setBackgroundResource(R.mipmap.pluse_dianzixinxi);
                    if(item.name != null && !item.name.equals("")){
                        title_txt.setText(item.name);
                    }else{
                        title_txt.setText("电子信息");
                    }
//                    item.evalueTitle = item.evalueTitle.substring(0,item.evalueTitle.length()-1);
                    title_info.setText(item.evalueTitle);
                    data.setText("更新日期 : "+format.format(new Date(Long.parseLong(item.updatatime)))+"  ");
                }else if(item.evaluetop.equals("1000")){ //新材料
                    icon.setBackgroundResource(R.mipmap.pluse_xincailiao);
                    if(item.name != null && !item.name.equals("")){
                        title_txt.setText(item.name);
                    }else{
                        title_txt.setText("新材料");
                    }

//                    item.evalueTitle = item.evalueTitle.substring(0,item.evalueTitle.length()-1);
                    title_info.setText(item.evalueTitle);
                    data.setText("更新日期 : "+format.format(new Date(Long.parseLong(item.updatatime)))+"  ");
                }else if(item.evaluetop.equals("3000")){ //节能环保
                    icon.setBackgroundResource(R.mipmap.pluse_huanbao);
                    if(item.name != null && !item.name.equals("")){
                        title_txt.setText(item.name);
                    }else{
                        title_txt.setText("节能环保");
                    }
//                    item.evalueTitle = item.evalueTitle.substring(0,item.evalueTitle.length()-1);
                    title_info.setText(item.evalueTitle);
                    data.setText("更新日期 : "+format.format(new Date(Long.parseLong(item.updatatime)))+"  ");
                }else if(item.evaluetop.equals("2500")){ //新能源
                    icon.setBackgroundResource(R.mipmap.pluse_xinnengyuan);
                    if(item.name != null && !item.name.equals("")){
                        title_txt.setText(item.name);
                    }else{
                        title_txt.setText("新能源");
                    }
//                    item.evalueTitle = item.evalueTitle.substring(0,item.evalueTitle.length()-1);
                    title_info.setText(item.evalueTitle);
                    data.setText("更新日期 : "+format.format(new Date(Long.parseLong(item.updatatime)))+"  ");
                }else if(item.evaluetop.equals("2000")){ //先进制造
                    icon.setBackgroundResource(R.mipmap.pluse_xinjinzhizhao);
                    if(item.name != null && !item.name.equals("")){
                        title_txt.setText(item.name);
                    }else{
                        title_txt.setText("先进制造");
                    }
                    title_info.setText(item.evalueTitle);
                    data.setText("更新日期 : "+format.format(new Date(Long.parseLong(item.updatatime)))+"  ");
                }else if(item.evaluetop.equals("1500")){ //生物技术
                    icon.setBackgroundResource(R.mipmap.pluse_shengwujishu);
                    if(item.name != null && !item.name.equals("")){
                        title_txt.setText(item.name);
                    }else{
                        title_txt.setText("生物技术");
                    }
//                    item.evalueTitle = item.evalueTitle.substring(0,item.evalueTitle.length()-1);
                    title_info.setText(item.evalueTitle);
                    data.setText("更新日期 : "+format.format(new Date(Long.parseLong(item.updatatime)))+"  ");
                }else if(item.evaluetop.equals("4000")){ //化学化工
                    icon.setBackgroundResource(R.mipmap.pluse_huaxuehuagong);
                    if(item.name != null && !item.name.equals("")){
                        title_txt.setText(item.name);
                    }else{
                        title_txt.setText("化学化工");
                    }
//                    title_txt.setText("化学化工");
//                    item.evalueTitle = item.evalueTitle.substring(0,item.evalueTitle.length()-1);
                    title_info.setText(item.evalueTitle);
                    data.setText("更新日期 : "+format.format(new Date(Long.parseLong(item.updatatime)))+"  ");
                }else if(item.evaluetop.equals("3500")){ //文化创意
                    icon.setBackgroundResource(R.mipmap.pluse_wenhuachuangyi);
                    if(item.name != null && !item.name.equals("")){
                        title_txt.setText(item.name);
                    }else{
                        title_txt.setText("文化创意");
                    }
//                    title_txt.setText("文化创意");
//                    item.evalueTitle = item.evalueTitle.substring(0,item.evalueTitle.length()-1);
                    title_info.setText(item.evalueTitle);
                    data.setText("更新日期 : "+format.format(new Date(Long.parseLong(item.updatatime)))+"  ");
                }else if(item.evaluetop.equals("4500")){ //其他
                    icon.setBackgroundResource(R.mipmap.pluse_qita);
                    if(item.name != null && !item.name.equals("")){
                        title_txt.setText(item.name);
                    }else{
                        title_txt.setText("其他");
                    }
//                    title_txt.setText("其他");
//                    item.evalueTitle = item.evalueTitle.substring(0,item.evalueTitle.length()-1);
                    title_info.setText(item.evalueTitle);
                    data.setText("更新日期 : "+format.format(new Date(Long.parseLong(item.updatatime)))+"  ");
                }
            }

            ViewGroup p = (ViewGroup) holder.getParent();
            if(p != null){
                container.removeView(holder);
//                if(icon != null)
//                  releaseImageViewResouce(icon);
            }
            ((ViewPager)container).addView(holder);
            return holder;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if(object != null){
               ((ViewPager)container).removeView((View)object);
            }
//            View view = weakReference.get();
//            if(view != null){
//                view = null;
//                weakReference = null;
//            }

        }
        public void releaseImageViewResouce(ImageView imageView) {
            try {
                if (imageView == null) return;
              Bitmap bitmap1 = ((BitmapDrawable)imageView.getBackground()).getBitmap();
                if (bitmap1 != null ) {
                    if (bitmap1 != null && !bitmap1.isRecycled()) {
                        bitmap1.recycle();
                        bitmap1=null;
                    }
                }
                System.gc();
            }catch (Exception e){

            }
        }
    }


    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            //这里做切换ViewPager时，底部RadioButton的操作
//            current_index = position%listData.size();
            current_index = position;
            PulseData item = listData.get(current_index);
            mItem = item;
            if(mFirst){
                mFirst = true;
                ((MainActivity)getActivity()).setBackGround(item.evaluetop, welcome_state);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    }

    /**
     * 实现的原理是，在当前显示页面放大至原来的MAX_SCALE
     * 其他页面才是正常的的大小MIN_SCALE
     */
    class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MAX_SCALE = 1.2f;
        private static final float MIN_SCALE = 1.0f;//0.85f

        @Override
        public void transformPage(View view, float position) {
            //setScaleY只支持api11以上
            if (position < -1){
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
            } else if (position <= 1) //a页滑动至b页 ； a页从 0.0 -1 ；b页从1 ~ 0.0
            { // [-1,1]
//				Log.e("TAG", view + " , " + position + "");
                float scaleFactor =  MIN_SCALE+(1-Math.abs(position))*(MAX_SCALE-MIN_SCALE);
                view.setScaleX(scaleFactor);
                //每次滑动后进行微小的移动目的是为了防止在三星的某些手机上出现两边的页面为显示的情况
                if(position>0){
                    view.setTranslationX(-scaleFactor*2);
                }else if(position<0){
                    view.setTranslationX(scaleFactor*2);
                }
                view.setScaleY(scaleFactor);

            } else
            { // (1,+Infinity]

                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);

            }
        }

    }
    PulseData Deldata = null ;
    class ResultBroader extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                String action = intent.getAction();
                if(action != null && action.equals(resultBroaderAction)){
                    String act = intent.getStringExtra("action");
                    if(act.equals("delete")){
                        String evaluetop = intent.getStringExtra("evaluetop");
                        listData = pulseCondtion.get();
                        for(int i =0;i<listData.size();i++){
                            PulseData pos = listData.get(i);
                            if(evaluetop.equals(pos.evaluetop)){
                                Deldata = pos;
                                break;
                            }
                        }
                        listData.remove(Deldata);
                        pulseCondtion.delete(Deldata.id);
                        if(listData.size() == 0){
                            welcome_state = true;
                            PulseData item = new PulseData();
                            item.typeid ="-1";
                            listData.add(item);
                            mItem = item;
                            initViewPager();
                            ((MainActivity)getActivity()).setBackGround("-1", welcome_state);
                        }else{
                            initViewPager();
                            ((MainActivity)getActivity()).setBackGround(Deldata.evaluetop, welcome_state);
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                String timestamp = System.currentTimeMillis()+"";
                                String sign="";
                                ArrayList<String> sort = new ArrayList<String>();
                                sort.add("action"+"del");
                                sort.add("mid"+Deldata.mid);
                                sort.add("id"+Deldata.pid);
                                sort.add("timestamp"+timestamp);
                                sort.add("version"+MyApplication.version);
                                sign= KeySort.keyScort(sort);

                                String data = Okhttp.loaudstringfromurl("http://"+ips+"/api/getAreaCateInfo.php?action=del&mid="+Deldata.mid+"&id="+Deldata.pid+"&sign="+sign+"&timestamp="+timestamp+"&version="+MyApplication.version);
                                Gson gs = new Gson();
                                RetPulseData item =gs.fromJson(data, RetPulseData.class);
                                if(item.code.equals("1")){

                                }
                            }
                        }).start();


                    }else{
                        try {
    //                        PulseData item = listData.get(current_index);
                            loginState = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                            if(!loginState.equals("1")){
                                welcome_state = true;
                                PulseData item1 = new PulseData();
                                item1.typeid ="-1";
                                listData.add(item1);
                                initViewPager();
                                ((MainActivity)getActivity()).setBackGround("-1", welcome_state);
                            }else{

                                listData = pulseCondtion.get();
                                if(listData.size() == 0 || !loginState.equals("1")){
                                    listData.clear();
                                    welcome_state = true;
                                    PulseData item = new PulseData();
                                    item.typeid ="-1";
                                    listData.add(item);
                                    initViewPager();
                                    ((MainActivity)getActivity()).setBackGround("-1", welcome_state);

                                }else{
                                    welcome_state = false;
                                    initViewPager();
    //                                if(listData.size() == 1){
                                        PulseData item =listData.get(0);
                                        ((MainActivity)getActivity()).setBackGround(item.evaluetop, welcome_state);
    //                                }else {
    //                                     PulseData item =listData.get(current_index);
    //                                     ((MainActivity)getActivity()).setBackGround(item.evaluetop, welcome_state);
    //
    //                                }

                                }
                            }

                        }catch (IndexOutOfBoundsException ex){

                        }catch (Exception e){

                        }
                    }
                }
            }catch(Exception e){

            }

        }
    }


    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("把脉");
    }

}