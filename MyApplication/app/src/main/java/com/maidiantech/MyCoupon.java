package com.maidiantech;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import Util.KeySort;

import android.widget.RelativeLayout;
import android.widget.TextView;
import Util.SharedPreferencesUtil;
import Util.OkHttpUtils;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.chenantao.autolayout.AutoLinearLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import application.ImageLoaderUtils;
import application.MyApplication;
import entity.ADS;
import entity.CouponEntity;
import entity.MyCouponEntity;
import Util.TimeUtils;
import view.RefreshListView;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/12/8.
 */

public class MyCoupon extends AutoLayoutActivity {
    ImageView yujian_backs;
    RefreshListView listview;
    ProgressBar progress;
    MyCouponAdapter adapter;
    String json;
    MyCouponEntity data;
    int width, height;
    List<ADS> adsList = new ArrayList<>();
    List<CouponEntity> dataList = new ArrayList<>();
    View heart;
    public FrameLayout fragment;
    public ViewPager viewPager;
    private DisplayImageOptions options;
    MyAdapter heartApdater;
    public LinearLayout viewGroup;
    ImageView nocontent;
    int current =0;
    private ArrayList<ImageView> tips;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        setContentView(R.layout.mycoupon);
        options = ImageLoaderUtils.initOptions();
        WindowManager wm = (WindowManager)
                getSystemService(Context.WINDOW_SERVICE);
        height =  wm.getDefaultDisplay().getHeight();
        width = wm.getDefaultDisplay().getWidth();
        yujian_backs = (ImageView)findViewById(R.id.yujian_backs);
        yujian_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listview = (RefreshListView)findViewById(R.id.listview);
        progress = (ProgressBar)findViewById(R.id.progress);
        nocontent = (ImageView)findViewById(R.id.nocontent);
        progress.setVisibility(View.VISIBLE);
        heart = View.inflate(MyCoupon.this, R.layout.mycouponadapterheart,null);
        fragment = (FrameLayout)heart.findViewById(R.id.fragment);
        viewPager = (ViewPager)heart.findViewById(R.id.viewPager);
        viewGroup = (LinearLayout)heart.findViewById(R.id.viewGroup);
        getJson();

    }


    class MyCouponAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView view=null;
            if(convertView == null){
                view = new HoldView();
                convertView = View.inflate(MyCoupon.this, R.layout.mycouponadapter, null);
                view.youhuijuan = (RelativeLayout) convertView.findViewById(R.id.youhuijuan);
                view.youhuijuan_title = (TextView)convertView.findViewById(R.id.youhuijuan_title);
                view.youhuijuan_date = (TextView)convertView.findViewById(R.id.youhuijuan_date);
                view.youhuijuan_price = (TextView)convertView.findViewById(R.id.youhuijuan_price);
                view.youhuijuan_fanwei = (TextView)convertView.findViewById(R.id.youhuijuan_fanwei);
                view.youhuijuan_overdue = (TextView)convertView.findViewById(R.id.youhuijuan_overdue);
                view.youhuijuan_danwei = (TextView)convertView.findViewById(R.id.youhuijuan_danwei);
                view.chakanmore = (TextView)convertView.findViewById(R.id.chakanmore);
                view.end = (RelativeLayout)convertView.findViewById(R.id.end);
                view.heart = (RelativeLayout)convertView.findViewById(R.id.heart);
                view.yuan = (ImageView)convertView.findViewById(R.id.yuan);
                convertView.setTag(view);
            }else{
                view = (HoldView)convertView.getTag();
            }
            final CouponEntity item =  dataList.get(position);
            view.youhuijuan.setVisibility(View.GONE);
            view.chakanmore.setVisibility(View.GONE);
            if(item.type == 1){
                if(item.isShow && item.state.equals("1")){
                    view.chakanmore.setVisibility(View.GONE);
                    view.youhuijuan.setVisibility(View.VISIBLE);
                    view.youhuijuan_danwei.setTextColor(0xffff6363);
                    view.youhuijuan_title.setTextColor(0xff3e3e3e);
                    view.youhuijuan_title.setText(item.title);
                    String date ="";
                    date = TimeUtils.getTimes(item.collectdate);
                    date = date+"至"+TimeUtils.getTimes(item.enddate);
                    view.youhuijuan_date.setText(date);
                    view.youhuijuan_date.setTextColor(0xff3e3e3e);
                    view.youhuijuan_price.setText(item.moneys);
                    view.youhuijuan_price.setTextColor(0xffff6363);
                    view.youhuijuan_fanwei.setText("使用范围:"+item.name);
                    view.youhuijuan_fanwei.setTextColor(0xff3e3e3e);
                    view.youhuijuan_overdue.setVisibility(View.GONE);
                    if(item.state.equals("-1")){
                        view.youhuijuan_overdue.setVisibility(View.VISIBLE);
                        view.youhuijuan_overdue.setTextColor(0xffff6363);
                        view.youhuijuan_overdue.setText("已过期");
                    }
                    if(item.color != null && item.color.equals("1")){
                        view.heart.setBackgroundResource(R.mipmap.youhuiquan_heart_1);
                        view.end.setBackgroundResource(R.mipmap.youhuiquan_end_1);
                        view.yuan.setBackgroundResource(R.mipmap.youhuiquan_yuan_1);
                        view.youhuijuan_title.setTextColor(0xffffc169);
                        view.youhuijuan_date.setTextColor(0xffff9d9d);
                        view.youhuijuan_price.setTextColor(0xffffc169);
                        view.youhuijuan_danwei.setTextColor(0xffffc169);
                        view.youhuijuan_fanwei.setTextColor(0xffff9d9d);

                    }else{
                        view.heart.setBackgroundResource(R.mipmap.youhuiquan_heart);
                        view.end.setBackgroundResource(R.mipmap.youhuiquan_end);
                        view.yuan.setBackgroundResource(R.mipmap.youhuiquan_yuan);
                        view.youhuijuan_title.setTextColor(0xff3e3e3e);
                        view.youhuijuan_date.setTextColor(0xff3e3e3e);
                        view.youhuijuan_price.setTextColor(0xffff6363);
                        view.youhuijuan_danwei.setTextColor(0xffff6363);
                        view.youhuijuan_fanwei.setTextColor(0xff3e3e3e);
                    }
                }else if(item.isShow && item.state.equals("2")){
                    view.chakanmore.setVisibility(View.GONE);
                    view.youhuijuan.setVisibility(View.VISIBLE);
                    view.youhuijuan_title.setTextColor(0xffbbbbbb);
                    view.youhuijuan_danwei.setTextColor(0xffbbbbbb);
                    view.youhuijuan_title.setText(item.title);
                    String date1 ="";
                    date1 = TimeUtils.getTimes(item.collectdate);
                    date1 = date1+"至"+TimeUtils.getTimes(item.enddate);
                    view.youhuijuan_date.setText(date1);
                    view.youhuijuan_date.setTextColor(0xffbbbbbb);
                    view.youhuijuan_price.setText(item.moneys);
                    view.youhuijuan_price.setTextColor(0xffbbbbbb);
                    view.youhuijuan_fanwei.setText("使用范围:"+item.name);
                    view.youhuijuan_fanwei.setTextColor(0xffbbbbbb);
                    view.youhuijuan_overdue.setTextColor(0xffbbbbbb);
                    view.youhuijuan_overdue.setVisibility(View.VISIBLE);
                    view.youhuijuan_overdue.setTextColor(0xffff6363);
                    view.youhuijuan_overdue.setText("已使用");
                }else if(item.isShow && item.state.equals("-1")){
                    view.chakanmore.setVisibility(View.GONE);
                    view.youhuijuan.setVisibility(View.VISIBLE);
                    view.youhuijuan_title.setTextColor(0xffbbbbbb);
                    view.youhuijuan_danwei.setTextColor(0xffbbbbbb);
                    view.youhuijuan_title.setText(item.title);
                    String date1 ="";
                    date1 = TimeUtils.getTimes(item.collectdate);
                    date1 = date1+"至"+TimeUtils.getTimes(item.enddate);
                    view.youhuijuan_date.setText(date1);
                    view.youhuijuan_date.setTextColor(0xffbbbbbb);
                    view.youhuijuan_price.setText(item.moneys);
                    view.youhuijuan_price.setTextColor(0xffbbbbbb);
                    view.youhuijuan_fanwei.setText("使用范围:"+item.name);
                    view.youhuijuan_fanwei.setTextColor(0xffbbbbbb);
                    view.youhuijuan_overdue.setTextColor(0xffbbbbbb);
                    view.youhuijuan_overdue.setVisibility(View.VISIBLE);
                    view.youhuijuan_overdue.setTextColor(0xffff6363);
                    view.youhuijuan_overdue.setText("已过期");
                }

            }else if(item.type == 2){
                view.youhuijuan.setVisibility(View.GONE);
                view.chakanmore.setVisibility(View.VISIBLE);
                view.chakanmore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int count = 0;
                        for (int i=0;i<dataList.size();i++){
                            CouponEntity item = dataList.get(i);
                            if(!item.isShow){
                                count++;
                            }
                            item.isShow = true;
                        }
                        if(dataList.size() >0){
                           listview.smoothScrollToPosition(dataList.size());

                        }
                        notifyDataSetChanged();
                        Message msg = Message.obtain();
                        msg.what = 10;
                        myhandler.sendMessage(msg);
                    }
                });
            }else if(item.type == 3){
                if(item.isShow){
                    view.youhuijuan.setVisibility(View.GONE);
                    view.chakanmore.setVisibility(View.GONE);
                }
            }


            return convertView;
        }
        class HoldView{
            //优惠卷
            public RelativeLayout youhuijuan;
            public TextView youhuijuan_title,youhuijuan_date,youhuijuan_price,youhuijuan_fanwei,youhuijuan_overdue,youhuijuan_danwei;
            //更多
            public TextView chakanmore;
            //line


            public ImageView yuan;
            public RelativeLayout end,heart;

        }
    }

    private void getJson(){
       new Thread(new Runnable() {
           @Override
           public void run() {
               String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
               MyApplication.setAccessid();
               String timestamp = System.currentTimeMillis()+"";
               String sign="";
               ArrayList<String> sort = new ArrayList<String>();
               sort.add("version"+MyApplication.version);
               sort.add("accessid"+MyApplication.deviceid);
               sort.add("timestamp"+timestamp);
               sort.add("mid"+mid);
               sign = KeySort.keyScort(sort);
               String url = "http://"+MyApplication.ip+"/api/discounts_coupon.php?mid="+mid+"&version="+MyApplication.version+"&accessid="+MyApplication.deviceid+"&timestamp="+timestamp+"&sign="+sign;
               json = OkHttpUtils.loaudstringfromurl(url);
               if(json != null){
                   Message msg = Message.obtain();
                   msg.what = 1;
                   myhandler.sendMessage(msg);
               }

           }
       }).start();
    }
    Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                progress.setVisibility(View.GONE);
               try {
                    Gson gson = new Gson();
                    data = gson.fromJson(json, MyCouponEntity.class);
                    if(data != null){
                        if(data.code.equals("1")){
                            if(data.data.ads != null){
                                for(int i=0;i<data.data.ads.size();i++){
                                    ADS item = data.data.ads.get(i);
                                    adsList.add(item);
                                }
                            }

                            if(adsList.size() > 0){
                                tips = new ArrayList<ImageView>();
                                if(adsList.size() > 1){
                                    for (int i = 0; i < adsList.size(); i++) {
                                        //画圆点
                                        ImageView imageView = new ImageView(MyCoupon.this);
                                        if (i == 0) {
                                            imageView.setImageResource(R.drawable.dot_focused);
                                        } else {
                                            imageView.setImageResource(R.drawable.dot_normal);
                                        }
                                        tips.add(imageView);
                                        if(width<=480){
                                            AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(10, 10);
                                            params.setMargins(5, 0, 5, 0);
                                            viewGroup.addView(imageView, params);

                                        }else{
                                            AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(15, 15);
                                            params.setMargins(5, 0, 5,5);
                                            viewGroup.addView(imageView, params);
                                        }
                                    }


                                }
                                heartApdater = new MyAdapter();
                                viewPager.setAdapter(heartApdater);
                                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                    @Override
                                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                        Log.d("lizisong", "position:"+position);
                                        current = position;
                                    }

                                    @Override
                                    public void onPageSelected(int position) {
                                        int index=position%tips.size();
                                        for(int i=0; i<tips.size(); i++){
                                            if(i == index){
                                                tips.get(i).setImageResource(R.drawable.dot_focused);
                                            }else{
                                                tips.get(i).setImageResource( R.drawable.dot_normal);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int state) {

                                    }
                                });
                                listview.addHeaderView(heart);
                                if(adsList.size() > 1){
                                    Message message = Message.obtain();
                                    msg.what = 100;
                                    myhandler.sendMessageDelayed(message, 4000);
                                }

                            }

                            if(data.data.list != null){
                                if(data.data.list.head != null && data.data.list.head.size() >0){
                                    for (int i=0; i<data.data.list.head.size();i++){
                                        CouponEntity item =data.data.list.head.get(i);
                                        item.isShow = true;
                                        item.type = 1;
                                        dataList.add(item);
                                    }
                                }
                                boolean state = false;
                                if(dataList.size() > 0){
                                    state = true;
                                }

                                if(data.data.list.foot != null && data.data.list.foot.size() >0){
                                     if(dataList.size() > 0){
                                        CouponEntity item1 = new CouponEntity();
                                        item1.type =2;
                                        item1.isShow=true;
                                        dataList.add(item1);
                                        CouponEntity item2 = new CouponEntity();
                                        item2.type =3;
                                        item2.isShow=false;
                                        dataList.add(item2);
                                     }
                                }

                                if(data.data.list.foot != null && data.data.list.foot.size() >0){
                                    for (int i=0; i<data.data.list.foot.size();i++){
                                        CouponEntity item =data.data.list.foot.get(i);
                                        if(state){
                                            item.isShow = false;
                                        }else {
                                            item.isShow = true;
                                        }

                                        item.type = 1;
                                        dataList.add(item);
                                    }
//                                    CouponEntity item1 = new CouponEntity();
//                                    item1.type =2;
//                                    item1.isShow=true;
//                                    dataList.add(item1);
//                                    CouponEntity item2 = new CouponEntity();
//                                    item2.type =3;
//                                    item2.isShow=false;
//                                    dataList.add(item2);

                                }else {
//                                    CouponEntity item1 = new CouponEntity();
//                                    item1.type =2;
//                                    item1.isShow=false;
//                                    dataList.add(item1);
//                                    CouponEntity item2 = new CouponEntity();
//                                    item2.type =3;
//                                    item2.isShow=false;
//                                    dataList.add(item2);
                                }
                                adapter = new MyCouponAdapter();
                                listview.setAdapter(adapter);
                                if(dataList.size()==0){
                                    nocontent.setVisibility(View.VISIBLE);
                                }else{
                                    nocontent.setVisibility(View.GONE);
                                }
                            }



                        }
                    }
               }catch (IndexOutOfBoundsException ex){

               }catch (Exception e){

               }
            }

            if(msg.what == 100){
                Message message =Message.obtain();
                message.what=100;
                if(heartApdater != null){
                    if(viewPager != null){
                        int item = viewPager.getCurrentItem();
                        item++;
                        viewPager.setCurrentItem(item);
                    }
                }
                myhandler.sendMessageDelayed(message, 4000);
            }
            if(msg.what == 10){
                listview.smoothScrollToPosition(dataList.size());
                adapter.notifyDataSetChanged();
            }
        }
    };


    class MyAdapter extends PagerAdapter {
        View view =null;
        @Override
        public int getCount() {
            if(adsList.size() == 1){
                return 1;
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
                if(adsList.size() == 0){
                    index = 0;
                }else{
                    index = position % adsList.size();
                }

                final ADS ads = adsList.get(index);

                view = View.inflate(MyCoupon.this,R.layout.showimage, null);
                ImageView img = (ImageView) view.findViewById(R.id.item);
                ImageLoader.getInstance().displayImage(ads.getPicUrl()
                        ,img , options);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ads != null && ads.type != null && ads.type.equals("html")
                                && ads.url != null && !ads.url.equals("")){

                            //增加mid参数 todoby wyy
                            String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "0");
                            if (!mid.equals("0")){

                                Intent intent=new Intent(MyCoupon.this, ActiveActivity.class);
                                intent.putExtra("url", ads.url+"?mid="+mid);
                                startActivity(intent);

                            } else {

                                Intent intent=new Intent(MyCoupon.this, ActiveActivity.class);
                                intent.putExtra("url", ads.url);
                                startActivity(intent);
                            }

                        }else{
                            Intent intent=new Intent(MyCoupon.this,ZixunDetailsActivity.class);
                            intent.putExtra("id",ads.getAid());
                            intent.putExtra("name",ads.getTypename());
                            intent.putExtra("pic",ads.getPicUrl());
                            startActivity(intent);
                        }
                    }
                });

                ViewGroup p = (ViewGroup) container.getParent();
                if(p != null){
                    container.removeView(view);
                }

                container.addView(view);



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


}
