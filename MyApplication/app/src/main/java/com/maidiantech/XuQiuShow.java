package com.maidiantech;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import Util.OkHttpUtils;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Flow;
import entity.Flows;
import entity.XuQiu;
import entity.XuQiuEntiy;
import view.RefreshListView;
import view.RoundImageView;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/7/5.
 */

public class XuQiuShow extends AutoLayoutActivity {
    ImageView about_backs;
    TextView senddata,xuqiutype,description,buchong;
    ImageView xm_img;
    RoundImageView rc_img;
    TextView xm_title,xm_linyu,xm_description;
    TextView jingjiren;
    ImageView phone_call;
    RefreshListView listview;
    xuqiuadapter adapter;
    ProgressBar progressBar;
    String id;
    String json;
    XuQiuEntiy data;
    public List<Flow> flow;
    public  LinearLayout con,xingqing;
    public RelativeLayout phone;

    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    String tel="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xuqiushow);
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);

        options = ImageLoaderUtils.initOptions();
        imageLoader = ImageLoader.getInstance();
        about_backs = (ImageView)findViewById(R.id.about_backs);
        about_backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        senddata = (TextView)findViewById(R.id.senddata);
        xuqiutype = (TextView)findViewById(R.id.xuqiutype);
        buchong   = (TextView)findViewById(R.id.buchong);
        xm_img  =(ImageView)findViewById(R.id.xm_img);
        rc_img = (RoundImageView)findViewById(R.id.rc_img);
        xm_title = (TextView)findViewById(R.id.xm_title);
        xm_linyu = (TextView)findViewById(R.id.xm_linyu);
        xm_description = (TextView)findViewById(R.id.xm_description);
        description =(TextView)findViewById(R.id.description);
        jingjiren = (TextView)findViewById(R.id.jingjiren);
        phone_call = (ImageView)findViewById(R.id.phone_call);
        listview = (RefreshListView)findViewById(R.id.listview);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        con = (LinearLayout)findViewById(R.id.content);
        xingqing = (LinearLayout)findViewById(R.id.xingqing);
        phone = (RelativeLayout)findViewById(R.id.phone);
        xingqing.setVisibility(View.GONE);
        phone.setVisibility(View.GONE);
        id = getIntent().getStringExtra("id");
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XuQiuShow.this, DetailsActivity.class);
                intent.putExtra("id", data.data.basic.aid);
                if(data.data.basic.typename.equals("专家")){
                    intent.putExtra("name", "专家");
                }else{
                    intent.putExtra("name", data.data.basic.typename);
                }
                intent.putExtra("pic",data.data.basic.litpic);
                startActivity(intent);
            }
        });
        xm_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(XuQiuShow.this, DetailsActivity.class);
                intent.putExtra("id", data.data.basic.aid);
                if(data.data.basic.typename.equals("专家")){
                    intent.putExtra("name", "专家");
                }else{
                    intent.putExtra("name", data.data.basic.typename);
                }

                intent.putExtra("pic",data.data.basic.litpic);
                startActivity(intent);
            }
        });

        phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  if(tel == null || tel.equals("")) {
                      Toast.makeText(XuQiuShow.this, "暂无经纪人电话", Toast.LENGTH_SHORT).show();
                  }else{
                      try {
                          Intent intent = new Intent(Intent.ACTION_DIAL);
                          Uri data = Uri.parse("tel:" + tel);
                          intent.setData(data);
                          startActivity(intent);
                      }catch (Exception e){

                      }
                  }
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        getData();

    }

    class xuqiuadapter extends BaseAdapter{

        @Override
        public int getCount() {
            return flow.size();
        }

        @Override
        public Object getItem(int position) {
            return flow.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHold hold;
            if(convertView == null){
                hold = new ViewHold();
                convertView = View.inflate(XuQiuShow.this, R.layout.xuqiudeail,null);
                hold.icon = (ImageView) convertView.findViewById(R.id.start_dian);
                hold.date = (TextView) convertView.findViewById(R.id.deail_date);
                hold.content = (TextView)convertView.findViewById(R.id.deail);
                hold.xian = (TextView)convertView.findViewById(R.id.xian);
                hold.heihgt = (LinearLayout)convertView.findViewById(R.id.heihgt);

                convertView.setTag(hold);
            }else{
                hold = (ViewHold) convertView.getTag();
            }
            Flow item = flow.get(position);
            hold.date.setText(item.time);
            hold.content.setText(item.content.replaceAll("</br>","\n"));
            if(position == 0){
                hold.icon.setBackgroundResource(R.mipmap.start_dian);
            }else {
                hold.icon.setBackgroundResource(R.mipmap.start_point);
            }

            int h= hold.content.getHeight()+hold.date.getHeight();
            if(h != 0){
                LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) hold.xian.getLayoutParams();
                params.height=h+50;
                hold.xian.setLayoutParams(params);
            }
            return convertView;
        }
        class ViewHold{
            public  ImageView icon;
            public  TextView date;
            public  TextView content;
            public  TextView xian;
            public  LinearLayout heihgt;

        }
    }
    private void getData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
               String url= "http://"+ MyApplication.ip+"/api/require.php?id="+id+"&method=detail";
                json = OkHttpUtils.loaudstringfromurl(url);
                if(json != null){
                    Message msg = Message.obtain();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                if(msg.what == 1){
                    Gson gson = new Gson();
                    data = gson.fromJson(json,XuQiuEntiy.class);
                    progressBar.setVisibility(View.GONE);
                    if(data.code.equals("1")){

                        senddata.setText(data.data.basic.pubdate);
                        xuqiutype.setText("需求类型:"+data.data.basic.typename);
                        description.setText(data.data.basic.content);
                        if(data.data.basic.typename.equals("专家")){
                            buchong.setText("需求专家");
                            xm_img.setVisibility(View.GONE);
                            rc_img.setVisibility(View.VISIBLE);
                            if(data.data.basic.litpic != null && !data.data.basic.litpic.equals("")){
                                ImageLoader.getInstance().displayImage(data.data.basic.litpic
                                        , rc_img, options);
                            }else{
                                rc_img.setVisibility(View.GONE);
                            }

                        }else if(data.data.basic.typename.equals("项目")){
                            buchong.setText("需求项目");
                            xm_img.setVisibility(View.VISIBLE);
                            rc_img.setVisibility(View.GONE);
                            if(data.data.basic.litpic != null && !data.data.basic.litpic.equals("")){
                                ImageLoader.getInstance().displayImage(data.data.basic.litpic
                                        , xm_img, options);
                            }else{
                                xm_img.setVisibility(View.GONE);
                            }
                        }else if(data.data.basic.typename.equals("设备")){
                            buchong.setText("需求设备");
                            xm_img.setVisibility(View.VISIBLE);
                            rc_img.setVisibility(View.GONE);
                            if(data.data.basic.litpic != null && !data.data.basic.litpic.equals("")){
                                ImageLoader.getInstance().displayImage(data.data.basic.litpic
                                        , xm_img, options);
                            }else{
                                xm_img.setVisibility(View.GONE);
                            }
                        }else{
                            buchong.setText("");
                        }
                        flow= data.data.flow;
                        xm_title.setText(data.data.basic.title);
                        xm_linyu.setText(data.data.basic.area_cates);
                        xm_description.setText(data.data.basic.description);
                        tel = data.data.basic.tel;
                        if(data.data.basic.uname==null){
                            jingjiren.setText("专属经济人:"+"匹配中");
                        }else{
                            jingjiren.setText("专属经济人:"+data.data.basic.uname);
                        }

                        if(flow != null && flow.size() >0){
                            adapter = new xuqiuadapter();
                            listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                        if(data.data.basic.typeid.equals("0")){
                            con.setVisibility(View.GONE);
                        }else{
                            con.setVisibility(View.VISIBLE);
                        }
                        xingqing.setVisibility(View.VISIBLE);
                        phone.setVisibility(View.VISIBLE);

                    }else{
                        Toast.makeText(XuQiuShow.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }

                }
            }catch (Exception e){

            }

        }
    };
}
