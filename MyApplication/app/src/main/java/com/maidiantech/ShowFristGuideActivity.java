package com.maidiantech;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.chenantao.autolayout.AutoLinearLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import application.ImageLoaderUtils;
import application.MyApplication;
import entity.ADS;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import view.ZQImageViewRoundOval;

/**
 * Created by lizisong on 2017/6/5.
 */

public class ShowFristGuideActivity extends Activity {
    ImageView img,close;
    int width, height;
    boolean state = false;
    /**
     * ViewPager
     */
    private ViewPager viewPager;
    private LinearLayout group;

    private ArrayList<ImageView> tips;
    /**
     * 图片资源id
     */
    private DisplayImageOptions options;
    public static List<ADS> data = new ArrayList<>();
    MyAdapter adapter;
    int len;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_showguide);
        Log.d("lizisong", "ShowFristGuideActivity");
        options = ImageLoaderUtils.initOptions();
        group = (LinearLayout)findViewById(R.id.viewGroup);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setClipChildren(false);
        frameLayout = (FrameLayout)findViewById(R.id.fragment);
//        frameLayout.setAlpha(0.5f);
//        if(MyApplication.model.equals("ZK-R322")){
            ViewGroup.LayoutParams params1 = frameLayout.getLayoutParams();
            params1.height=( MyApplication.width*2/3)*4/3;
            params1.width = MyApplication.width*2/3;
            frameLayout.setLayoutParams(params1);
//        }

        close = (ImageView)findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        len = data.size();
//        img = (ImageView)findViewById(R.id.flow_item_img);
//        img1 = (ImageView)findViewById(R.id.flow_bottom_img);
//        img1.setVisibility(View.GONE);
        WindowManager wm = (WindowManager)
                getSystemService(Context.WINDOW_SERVICE);
        height =  wm.getDefaultDisplay().getHeight();
        width = wm.getDefaultDisplay().getWidth();
        //载入图片资源ID

        //将点点加入到ViewGroup中
        tips = new ArrayList<ImageView>();
          if(len >1){
                for (int i = 0; i < len; i++) {
                    //画圆点
                    ImageView imageView = new ImageView(this);
                    if (i == 0) {
                        imageView.setImageResource(R.drawable.dot_focused);
                    } else {
                        imageView.setImageResource(R.drawable.dot_normal);
                    }
                    tips.add(imageView);
                    if(width<=480){
                        AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(10, 10);
                        params.setMargins(5, 0, 5, 0);
                        group.addView(imageView, params);

                    }else{
                        AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(15, 15);
                        params.setMargins(5, 0, 5,5);
                        group.addView(imageView, params);
                    }
            }
          }

        adapter = new MyAdapter();
        viewPager.setPageMargin(10);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setImageBackground(position%data.size());

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                img.setVisibility(View.GONE);
//                img1.setVisibility(View.VISIBLE);
//            }
//        });
//        img1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        state = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        int x, y;
//        x = (int) event.getX();
//        y = (int) event.getY();
//        Log.d("lizisong", "x="+x+"y="+y);
//        if(event.getAction() == MotionEvent.ACTION_UP){
//            if(!state){
//                state = true;
//                img.setVisibility(View.GONE);
//                img1.setVisibility(View.VISIBLE);
//            }else {
//                finish();
//            }
//        }
////        if(x > 130 && x < 350 && y >10 && y< 150){
////            img.setVisibility(View.GONE);
////            img1.setVisibility(View.VISIBLE);
////        }
////        if(x> 85 && x <285 && y>height-200 && y<height ){
////            finish();
////        }
        return super.onTouchEvent(event);

    }



    class MyAdapter extends PagerAdapter {
        View view =null;
        @Override
        public int getCount() {
            if(len == 1){
                return len;
            }

            return Integer.MAX_VALUE;
        }

        /**
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            try {
                int index =0;
                if(data.size() == 0){
                    index = 0;
                }else{
                    index = position % data.size();
                }

               final ADS ads = data.get(index);

                view = View.inflate(ShowFristGuideActivity.this,R.layout.showround, null);
                view.setBackgroundResource(R.drawable.test_image);
                ZQImageViewRoundOval img = (ZQImageViewRoundOval) view.findViewById(R.id.item);
                img.setType(ZQImageViewRoundOval.TYPE_ROUND);
                img.setRoundRadius(30);

                ImageLoader.getInstance().displayImage(ads.img
                        ,img , options);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ads != null && ads.type != null && ads.type.equals("html")
                                && ads.url != null && !ads.url.equals("")){
                            Intent intent=new Intent(ShowFristGuideActivity.this, ActiveActivity.class);
                            intent.putExtra("url", ads.url);
                            intent.putExtra("title", ads.getTitle());
                            startActivity(intent);
                        }else{
                            Intent intent=new Intent(ShowFristGuideActivity.this,ZixunDetailsActivity.class);
                            intent.putExtra("id",ads.getAid());
                            intent.putExtra("name",ads.getTypename());
                            intent.putExtra("pic",ads.getPicUrl());
                            startActivity(intent);
                        }
                        tongji(ads.id);
                    }
                });

                ViewGroup p = (ViewGroup) container.getParent();

                if(p != null){
                    container.removeView(view);
                }

                container.addView(view);
//                container.setBackgroundResource(R.drawable.test_image);
//                container.setBackgroundResource(R.drawable.test_image);


            }catch (IllegalStateException exx){}
            catch (NumberFormatException ex){}
            catch ( Exception e){}
           return  view;

        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
//            container.removeView(view);
        }
        //        @Override
//        public void onPageSelected(int arg0) {
//            setImageBackground(arg0 % imgIdArray.length);
//        }





    }
    /**
     * 设置选中的tip的背景
     * @param selectItems
     */
    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.size(); i++){
            if(i == selectItems){
                tips.get(i).setImageResource(R.drawable.dot_focused);
            }else{
                tips.get(i).setImageResource( R.drawable.dot_normal);
            }
        }
    }
    private void tongji(final String id){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID,"");
                        String url ="http://"+MyApplication.ip+"/api/banner_behavior.php?id ="+id+"&mid="+mid+"&accessid="+MyApplication.deviceid;
                        OkHttpUtils.loaudstringfromurl(url);

                    }
                }
        ).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("广告页");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("广告页");
    }
}
