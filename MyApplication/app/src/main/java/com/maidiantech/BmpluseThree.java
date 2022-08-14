package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.chenantao.autolayout.AutoRelativeLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import entity.BmpluseData;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/11/21.
 */

public class BmpluseThree extends AutoLayoutActivity {

    String title;
    RelativeLayout bg;
    TextView mNameTxt;
    GridView mGridView;
    GridBaseApter mGridBase;
    public static List<BmpluseData> showList;
    Button back, next;
    ImageView mImg;
    TextView checkAll;

    public static  boolean close_state = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pluse_three_next);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);


        showList = new ArrayList<>();
        //添加项目
        BmpluseData project = new BmpluseData();
        project.checkState = false;
        project.txt = "项目";
        project.tag = "2";
        showList.add(project);

        //添加人才
        BmpluseData personnel = new BmpluseData();
        personnel.checkState = false;
        personnel.txt = "专家";
        project.tag = "4";
        showList.add(personnel);

        //添加设备
        BmpluseData equipment = new BmpluseData();
        equipment.checkState = false;
        equipment.txt = "设备";
        project.tag = "7";
        showList.add(equipment);
        //添加实验室
        BmpluseData laboratory = new BmpluseData();
        laboratory.checkState = false;
        laboratory.txt = "研究所";
        project.tag = "8";
        showList.add(laboratory);
        //政策
        BmpluseData policy = new BmpluseData();
        policy.checkState = false;
        policy.txt = "政策";
        project.tag = "6";
        showList.add(policy);
        //咨询
        BmpluseData consultation = new BmpluseData();
        consultation.checkState = false;
        consultation.txt = "资讯";
        project.tag = "1";
        showList.add(consultation);
        //专利
        BmpluseData patent = new BmpluseData();
        patent.checkState=false;
        patent.txt="专利";
        project.tag = "5";
        showList.add(patent);

        bg = (RelativeLayout)findViewById(R.id.title);
        mNameTxt = (TextView)findViewById(R.id.name);
        mGridView = (GridView)findViewById(R.id.bamai_listview);
        back = (Button)findViewById(R.id.goback);
        next = (Button)findViewById(R.id.bamai_next_two);
        mImg = (ImageView)findViewById(R.id.img_close);
        checkAll = (TextView)findViewById(R.id.check_all);

        checkAll.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                for(int i=0; i<showList.size(); i++){
                    showList.get(i).checkState =true;
                    mGridBase.notifyDataSetChanged();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 0;
                for(int i=0; i<showList.size(); i++){
                    if(showList.get(i).checkState){
                        count++;
                    }
                }
                if(count == 0){
                    Toast.makeText(BmpluseThree.this, "请选择主题", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(BmpluseThree.this, pluse_four.class);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });
        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bmplusetwo.close_state = true;
                PulseActivity.posdata = 1;
                finish();
            }
        });
        mGridBase = new GridBaseApter();
        mGridView.setAdapter(mGridBase);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        if(title.equals("先进制造")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_xianjinzhizao);
            bg.setBackgroundResource(R.mipmap.bg_xianjinzhizao);
            mNameTxt.setText("先进制造");
        }else if(title.equals("电子信息")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_dianzixinx);
            bg.setBackgroundResource(R.mipmap.bg_dianzixinxi);
            mNameTxt.setText("电子信息");
        }else if(title.equals("新材料")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_xincailiao);
            bg.setBackgroundResource(R.mipmap.bg_xincailiao);
            mNameTxt.setText("新材料");
        }else if(title.equals("生物技术")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_shegnwujishu);
            bg.setBackgroundResource(R.mipmap.bg_shengwujishu);
            mNameTxt.setText("生物技术");
        }else if(title.equals("节能环保")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_jienenghuanbao);
            bg.setBackgroundResource(R.mipmap.bg_jienenghuanbao);
            mNameTxt.setText("节能环保");
        }else if(title.equals("文化创意")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_wenhuachuangyi);
            bg.setBackgroundResource(R.mipmap.bg_wenhuachuangyi);
            mNameTxt.setText("文化创意");

        }else if(title.equals("化学化工")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_huaxuehuagong);
            bg.setBackgroundResource(R.mipmap.bg_huaxuehuagong);
            mNameTxt.setText("化学化工");
        }else if(title.equals("新能源")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_xinnengyuan);
            bg.setBackgroundResource(R.mipmap.bg_xinnengyuan);
            mNameTxt.setText("新能源");
        }else if(title.equals("其他")){
            StyleUtils1.setStyleTitle(this, R.mipmap.status_bg_qita);
            bg.setBackgroundResource(R.mipmap.bg_qita);
            mNameTxt.setText("其他");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(close_state){
            close_state = false;
            finish();
        }
        MobclickAgent.onPageStart("选择主题"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("选择主题"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    class GridBaseApter extends BaseAdapter{

        @Override
        public int getCount() {
            return showList.size();
        }

        @Override
        public Object getItem(int position) {
            return showList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            BmpluseData item = showList.get(position);
            if(convertView == null){
                holder = new ViewHolder();
                convertView = View.inflate(getApplicationContext(), R.layout.bmpluse_three_adapter,null);
                holder.mTxt = (TextView)convertView.findViewById(R.id.bm_txt);
                holder.mImg = (ImageView)convertView.findViewById(R.id.bm_imgview);
                holder.bg = (AutoRelativeLayout)convertView.findViewById(R.id.bg);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            if(item.txt.equals("项目")){
                holder.mTxt.setText("项目");
                if(item.checkState){
                   holder.mImg.setBackgroundResource(R.mipmap.img_xiangmu_h);
                    holder.mTxt.setTextColor(getResources().getColor(R.color.lansecolor));
                }else {
                    holder.mImg.setBackgroundResource(R.mipmap.img_xiangmu);
                    holder.mTxt.setTextColor(getResources().getColor(R.color.text_gray));
                }

            }else if(item.txt.equals("专家")){

                holder.mTxt.setText("专家");
                if(item.checkState){
                    holder.mImg.setBackgroundResource(R.mipmap.img_rencai_h);
                    holder.mTxt.setTextColor(getResources().getColor(R.color.lansecolor));
                }else {
                    holder.mImg.setBackgroundResource(R.mipmap.img_rencai);
                    holder.mTxt.setTextColor(getResources().getColor(R.color.text_gray));
                }

            }else if(item.txt.equals("设备")){
                holder.mTxt.setText("设备");
                if(item.checkState){
                    holder.mImg.setBackgroundResource(R.mipmap.img_shebei_h);
                    holder.mTxt.setTextColor(getResources().getColor(R.color.lansecolor));
                }else {
                    holder.mImg.setBackgroundResource(R.mipmap.img_shebei);
                    holder.mTxt.setTextColor(getResources().getColor(R.color.text_gray));
                }

            }else if(item.txt.equals("研究所")){
                holder.mTxt.setText("研究所");
                if(item.checkState){
                    holder.mImg.setBackgroundResource(R.mipmap.img_shiyanshi_h);
                    holder.mTxt.setTextColor(getResources().getColor(R.color.lansecolor));
                }else {
                    holder.mImg.setBackgroundResource(R.mipmap.img_shiyanshi);
                    holder.mTxt.setTextColor(getResources().getColor(R.color.text_gray));
                }

            }else if(item.txt.equals("政策")){
                holder.mTxt.setText("政策");
                if(item.checkState){
                    holder.mImg.setBackgroundResource(R.mipmap.img_zhengce_h);
                    holder.mTxt.setTextColor(getResources().getColor(R.color.lansecolor));
                }else {
                    holder.mImg.setBackgroundResource(R.mipmap.img_zhengce);
                    holder.mTxt.setTextColor(getResources().getColor(R.color.text_gray));
                }

            }else if(item.txt.equals("资讯")){
                holder.mTxt.setText("资讯");
                if(item.checkState){
                    holder.mImg.setBackgroundResource(R.mipmap.img_zixun_h);
                    holder.mTxt.setTextColor(getResources().getColor(R.color.lansecolor));
                }else {
                    holder.mImg.setBackgroundResource(R.mipmap.img_zixun);
                    holder.mTxt.setTextColor(getResources().getColor(R.color.text_gray));
                }

            }else if(item.txt.equals("专利")){
                holder.mTxt.setText("专利");
                if(item.checkState){
                    holder.mImg.setBackgroundResource(R.mipmap.img_zhuanli_h);
                    holder.mTxt.setTextColor(getResources().getColor(R.color.lansecolor));
                }else {
                    holder.mImg.setBackgroundResource(R.mipmap.img_zhuanli);
                    holder.mTxt.setTextColor(getResources().getColor(R.color.text_gray));
                }

            }
            holder.bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(showList.get(position).checkState){
                        showList.get(position).checkState = false;
                    }else {
                        showList.get(position).checkState = true;
                    }
                    mGridBase.notifyDataSetChanged();

                }
            });

            return convertView;
        }

        class ViewHolder{
            TextView mTxt;
            ImageView mImg;
            AutoRelativeLayout bg;
        }
    }

}
