package com.maidiantech;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import fragment.Fragment_menumsg;
import fragment.Fragment_mine;

/**
 * Created by 13520 on 2016/9/22.
 */
public class MymessageActivity extends Myautolayout {
    ImageView mymsgback;
    RadioButton rbmines;
    RadioButton rbmsgs;
    RadioGroup rgs;
    ViewPager mymessagevi;
    private  ImageView image;
    private int currIndex;//当前页卡编号
    private int bmpW;//横线图片宽度
    private int offset;//图片移动的偏移量
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_messages);
        initView();
        mymessagevi.setAdapter(new MymsgAdapter(getSupportFragmentManager()));
    }
    private void initView() {
        image=(ImageView) findViewById(R.id.cursor);
        mymsgback = (ImageView) findViewById(R.id.my_msg_back);
        rbmines = (RadioButton) findViewById(R.id.rb_mines);
        rbmsgs = (RadioButton) findViewById(R.id.rb_msgs);
        rgs = (RadioGroup) findViewById(R.id.rgs);
        mymessagevi = (ViewPager) findViewById(R.id.my_message_vi);
        bmpW = BitmapFactory.decodeResource(getResources(), R.mipmap.my_information_selected).getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (screenW/2 - bmpW)/2;
        //imgageview设置平移，使下划线平移到初始位置（平移一个offset）
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        image.setImageMatrix(matrix);
        mymsgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rgs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_mines:
                        mymessagevi.setCurrentItem(0);
                        break;
                    case R.id.rb_msgs:
                        mymessagevi.setCurrentItem(1);
                        break;
                }
            }
        });
        mymessagevi.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int one = offset *2 +bmpW;//两个相邻页面的偏移量
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               switch (position){
                    case 0:
                        rbmines.setTextColor(Color.parseColor("#4FA5FC"));
                        rbmsgs.setTextColor(Color.parseColor("#737373"));
                        break;
                    case 1:
                        rbmines.setTextColor(Color.parseColor("#737373"));
                        rbmsgs.setTextColor(Color.parseColor("#4FA5FC"));
                        break;
                }
            }
            @Override
            public void onPageSelected(int position) {
                Animation animation = new TranslateAnimation(currIndex*one,position*one,0,0);//平移动画
                currIndex = position;
                animation.setFillAfter(true);//动画终止时停留在最后一帧，不然会回到没有执行前的状态
                animation.setDuration(200);//动画持续时间0.2秒
                image.startAnimation(animation);//是用ImageView来显示动画的
                int i = currIndex + 1;
                Toast.makeText(MymessageActivity.this, "您选择了第" + i + "个页卡", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    class MymsgAdapter extends FragmentPagerAdapter {
        public MymsgAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment f=null;
            switch (position){
                case 0:
                    f=new Fragment_mine();
                    break;
                case 1:
                    f=new Fragment_menumsg();
                    break;
            }
            return f;
        }
        @Override
        public int getCount() {
            return 2;
        }
    }
}
