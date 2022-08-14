package com.maidiantech;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import Util.PrefUtils;
import application.ImageLoaderUtils;
import entity.data;
import view.RefreshListView;
import view.RoundImageView;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2016/12/27.
 */

public class MoreInfoActivity extends AutoLayoutActivity {

    private TextView title_name;
    private ImageView back;
    private RefreshListView listView;
    String title;
    List<data> listData;
    MoreInfoAdapter moreAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_info);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_name = (TextView)findViewById(R.id.title_name);
        listView = (RefreshListView)findViewById(R.id.listview);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        title_name.setText(title);
        if(title.equals("项目")){
            listData = ResultActivity.more_xiangmu;
        }else if(title.equals("人才") || title.equals("专家")){
            listData = ResultActivity.more_rencai;
        }else if(title.equals("设备")){
            listData = ResultActivity.more_shebei;
        }else if(title.equals("科研机构")){
            listData = ResultActivity.more_shiyanshi;
        }else if(title.equals("政策")){
            listData = ResultActivity.more_zhenci;
        }else if(title.equals("资讯")){
            listData = ResultActivity.more_zixun;
        }else if(title.equals("专利")){
            listData = ResultActivity.more_zhuanli;
        }
        if(listData == null){
            listData = new ArrayList<>();
        }
        moreAdapter = new MoreInfoAdapter();
        listView.setAdapter(moreAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(moreAdapter != null){
            moreAdapter.notifyDataSetChanged();
        }
        MobclickAgent.onPageStart("把脉更多"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("把脉更多"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    class MoreInfoAdapter extends BaseAdapter{

       private ImageLoader imageLoader;
       private DisplayImageOptions options;
       public  MoreInfoAdapter(){
           options = ImageLoaderUtils.initOptions();
           imageLoader = ImageLoader.getInstance();
       }

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
           ViewHold holder = null;
           final data item = listData.get(position);
           if(convertView == null){
               holder = new ViewHold();
               convertView = View.inflate(MoreInfoActivity.this, R.layout.more_info_adapter, null);
               holder.content = (LinearLayout) convertView.findViewById(R.id.rc);
               holder.content_lay = (LinearLayout) convertView.findViewById(R.id.content);
               holder.cont_imgs = (RoundImageView)convertView.findViewById(R.id.cont_imgs);
               holder.cont_imgs2 = (ImageView)convertView.findViewById(R.id.cont_imgs2);
               holder.my_rencainame = (TextView)convertView.findViewById(R.id.my_rencainame);
               holder.zhicheng = (TextView)convertView.findViewById(R.id.zhicheng);
               holder.lingyu =(TextView)convertView.findViewById(R.id.lingyu);
               holder.my_linyu = (TextView)convertView.findViewById(R.id.my_linyu);
               holder.my_xianqin = (TextView)convertView.findViewById(R.id.my_xianqin);
               holder.time = (TextView)convertView.findViewById(R.id.time);
               holder.xiangqing = (LinearLayout)convertView.findViewById(R.id.xiangqing);

               holder.rc_look =(TextView)convertView.findViewById(R.id.rc_look);
               holder.shiyanse_imgs=(ImageView) convertView.findViewById(R.id.shiyanse_imgs);
               holder.lingyu_info = (LinearLayout)convertView.findViewById(R.id.lingyu_info);
               convertView.setTag(holder);
           }else{
               holder = (ViewHold)convertView.getTag();
           }

           holder.lingyu_info.setVisibility(View.INVISIBLE);
           holder.zhicheng.setVisibility(View.GONE);
           if(item.typeid.equals("4")){
               holder.cont_imgs.setVisibility(View.VISIBLE);
               holder.cont_imgs2.setVisibility(View.GONE);
               holder.shiyanse_imgs.setVisibility(View.GONE);
               if( item.litpic == null || item.litpic.equals("")){
                   holder.cont_imgs.setVisibility(View.GONE);
               }
               imageLoader.displayImage(item.litpic
                       , holder.cont_imgs, options);
               holder.my_rencainame.setText(item.username);
               String pid=PrefUtils.getString(MoreInfoActivity.this,item.id,"");
               if(pid.equals("")){
                   holder.my_rencainame.setTextColor(Color.parseColor("#181818"));
               }else{
                   holder.my_rencainame.setTextColor(Color.parseColor("#777777"));
               }
               holder.lingyu_info.setVisibility(View.VISIBLE);
               holder.my_linyu.setText(item.area_cate.getArea_cate1());
               holder.time.setText("专家");
               holder.zhicheng.setText(item.rank);
               holder.zhicheng.setVisibility(View.VISIBLE);

           }/*else if(item.typeid.equals("8")){
               holder.cont_imgs.setVisibility(View.GONE);
               holder.lingyu_info.setVisibility(View.GONE);
               holder.my_xianqin.setVisibility(View.GONE);
               holder.cont_imgs2.setVisibility(View.GONE);
               holder.shiyanse_imgs.setVisibility(View.VISIBLE);
               if( item.litpic == null || item.litpic.equals("")){
                   holder.shiyanse_imgs.setVisibility(View.GONE);
               }
               imageLoader.displayImage(item.litpic
                       , holder.shiyanse_imgs, options);
               holder.my_rencainame.setText(item.title);
               holder.time.setText("实验室");
               String pid=PrefUtils.getString(MoreInfoActivity.this,item.id,"");
               if(pid.equals("")){
                   holder.my_rencainame.setTextColor(Color.parseColor("#181818"));
               }else{
                   holder.my_rencainame.setTextColor(Color.parseColor("#777777"));
               }
           }8*/
           else{
               holder.cont_imgs.setVisibility(View.GONE);
               holder.lingyu_info.setVisibility(View.VISIBLE);
               holder.my_xianqin.setVisibility(View.VISIBLE);
               holder.cont_imgs2.setVisibility(View.VISIBLE);
               holder.shiyanse_imgs.setVisibility(View.GONE);
               if( item.litpic == null || item.litpic.equals("")){
                   holder.cont_imgs2.setVisibility(View.GONE);
               }
               imageLoader.displayImage(item.litpic
                       , holder.cont_imgs2, options);
               holder.my_rencainame.setText(item.title);
//               holder.my_xianqin.setText(item.description);
               String pid=PrefUtils.getString(MoreInfoActivity.this,item.id,"");
               if(pid.equals("")){
                   holder.my_rencainame.setTextColor(Color.parseColor("#181818"));
               }else{
                   holder.my_rencainame.setTextColor(Color.parseColor("#777777"));
               }
           }

           if(item.description == null || item.description.equals("")){
               holder.xiangqing.setVisibility(View.GONE);
           }else {
               holder.xiangqing.setVisibility(View.VISIBLE);
               holder.my_xianqin.setText(item.description.replaceAll("\r\n", " "));
           }
           try {
               holder.my_linyu.setText(item.area_cate.getArea_cate1());
           }catch (Exception e){

           }

           if(item.typeid.equals("2")){
               holder.time.setText("项目");
           }if(item.typeid.equals("7")){
               holder.time.setText("设备");
           }else if(item.typeid.equals("6")){
               holder.lingyu_info.setVisibility(View.GONE);
               holder.time.setText("政策");
           }else if(item.typeid .equals("1")){
               holder.time.setText("资讯");
               holder.xiangqing.setVisibility(View.GONE);
               holder.lingyu_info.setVisibility(View.GONE);
           }else if(item.typeid .equals("5")){
               holder.time.setText("专利");
           }else if(item.typeid .equals("8")){
               holder.time.setText("科研机构");
           }

           holder.rc_look.setText(item.click);

           holder.content_lay.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   PrefUtils.setString(MoreInfoActivity.this,item.id,item.id);
                   if(item.typeid != null &&(item.typeid.equals("1"))){
                       Intent intent = new Intent(MoreInfoActivity.this, ZixunDetailsActivity.class);
                       intent.putExtra("id", item.id);
                       intent.putExtra("name", title);
                       intent.putExtra("pic", item.litpic);
                       startActivity(intent);
                   }else if(item.typeid != null &&(item.typeid.equals("5"))){
                       Intent intent = new Intent(MoreInfoActivity.this, NewZhuanliActivity.class);
                       intent.putExtra("aid",item.id);
                       startActivity(intent);
                   }else if(item.typeid != null &&(item.typeid.equals("6"))){
                       Intent intent = new Intent(MoreInfoActivity.this, NewZhengCeActivity.class);
                       intent.putExtra("aid",item.id);
                       startActivity(intent);
                   }else if(item.typeid != null && item.typeid.equals("8")){
                       Intent intent = new Intent(MoreInfoActivity.this, UnitedStatesDeilActivity.class);
                       intent.putExtra("aid",item.id);
                       startActivity(intent);

                   }else if(item.typeid != null && item.typeid.equals("4")){
                       Intent intent = new Intent(MoreInfoActivity.this, XinFanAnCeShi.class);
                       intent.putExtra("aid", item.id);
                       startActivity(intent);
                   }else if(item.typeid != null && item.typeid.equals("2")){
                       Intent intent = new Intent(MoreInfoActivity.this, NewProjectActivity.class);
                       intent.putExtra("aid", item.id);
                       startActivity(intent);
                   }else {
                       Intent intent = new Intent(MoreInfoActivity.this, DetailsActivity.class);
                       intent.putExtra("id", item.id);
                       intent.putExtra("pic", item.litpic);
                       intent.putExtra("name", title);
                       startActivity(intent);
                   }
               }
           });
           return convertView;
       }
       class ViewHold{
           LinearLayout content;
           LinearLayout xiangqing;
           RoundImageView cont_imgs;
           ImageView cont_imgs2;
           TextView my_rencainame;
           TextView zhicheng;
           TextView lingyu;
           TextView my_linyu;
           LinearLayout lingyu_info;
           TextView my_xianqin;
           TextView time;
           LinearLayout content_lay;
           TextView rc_look;
           ImageView shiyanse_imgs;

       }
   }

}
