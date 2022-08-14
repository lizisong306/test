package com.maidiantech;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

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

import com.baidu.mapapi.map.Text;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.Date;
import java.util.HashMap;

import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.TestDate;
import Util.TimeUtil;
import Util.TimeUtils;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.Flow;
import entity.Flows;
import entity.QingBaoUpdate;
import entity.Ret;
import entity.XuQiuEntiyBase;
import entity.XuQiuXiangQingEntry;
import view.BTAlertDialog;
import view.RefreshListView;
import view.RoundImageView;
import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/9/5.
 */

public class XuQiuXiangqQing extends AutoLayoutActivity {
    ImageView back;
    RefreshListView listview;
    ProgressBar prgressBar;
    String id;
    String json,str;
    XuQiuXiangQingEntry xiangqing;
    View heard;
    String dianstate = "0";
    TextView senddata,xuqiutype,description,buchong, xm_rank;
    ImageView xm_img, need_add;
    RoundImageView rc_img;
    TextView xm_title,xm_linyu,xm_description,line;
    TextView jingjiren;
    ImageView phone_call;
    String state= "";
    TextView add_xuqiu, add_cenel,add_kefu;
    public XuQiuEntiyBase data;

    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    LinearLayout con,more_info;
    String tel;
    XuQiuXiang adapter;
    String user;
    String resid="";
    String ret;
    String gson ="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xuqiuxiangqing);
        StyleUtils1.initSystemBar(this);
//        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        options = ImageLoaderUtils.initOptions();
        imageLoader = ImageLoader.getInstance();
        back = (ImageView)findViewById(R.id.about_backs);
        listview = (RefreshListView) findViewById(R.id.listview);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        id = getIntent().getStringExtra("id");
        prgressBar = (ProgressBar)findViewById(R.id.progress) ;
        heard =  View.inflate(XuQiuXiangqQing.this, R.layout.xuqiuxiangqingheart,null);
        senddata = (TextView)heard.findViewById(R.id.senddata);
        line = (TextView)heard.findViewById(R.id.line);
        xuqiutype = (TextView)heard.findViewById(R.id.xuqiutype);
        buchong   = (TextView)heard.findViewById(R.id.buchong);
        xm_img  =(ImageView)heard.findViewById(R.id.xm_img);
        rc_img = (RoundImageView)heard.findViewById(R.id.rc_img);
        xm_title = (TextView)heard.findViewById(R.id.xm_title);
        xm_linyu = (TextView)heard.findViewById(R.id.xm_linyu);
        xm_description = (TextView)heard.findViewById(R.id.xm_description);
        description =(TextView)heard.findViewById(R.id.description);
        jingjiren = (TextView)heard.findViewById(R.id.jingjiren);
        phone_call = (ImageView)heard.findViewById(R.id.phone_call);
        xm_rank  = (TextView)heard.findViewById(R.id.xm_rank);
        con = (LinearLayout) heard.findViewById(R.id.content);
        need_add = (ImageView)findViewById(R.id.need_add);

        add_xuqiu = (TextView)findViewById(R.id.add_xuqiu);
        add_xuqiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more_info.setVisibility(View.GONE);
                Intent intent = new Intent(XuQiuXiangqQing.this, NewXuQiu.class);
                intent.putExtra("typeid", "0");
                startActivity(intent);
            }
        });
        add_cenel = (TextView)findViewById(R.id.add_cenel);
        add_cenel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more_info.setVisibility(View.GONE);
                final BTAlertDialog dialog = new BTAlertDialog(XuQiuXiangqQing.this);
                dialog.setTitle("您的需求正在快速解决中！是否取消？");
                dialog.setNegativeButton("否", null);
                dialog.setPositiveButton("是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url= "http://"+ MyApplication.ip+"/api/require_cancel.php";
                        HashMap<String,String> map = new HashMap<String, String>();
                        map.put("method","cancel");
                        map.put("id",resid);
                        NetworkCom networkCom = NetworkCom.getNetworkCom();
                        networkCom.getJson(url,map,handler,2,0);
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                String url = "http://"+MyApplication.ip+"/api/require_cancel.php?method=cancel&id="+resid;
//                                 str = OkHttpUtils.loaudstringfromurl(url);
//                                if(str != null){
//                                    Message msg = Message.obtain();
//                                    msg.what = 2;
//                                    handler.sendMessage(msg);
//                                }
//
//                            }
//                        }).start();
                    }});
                dialog.show();
            }
        });
        add_kefu  = (TextView)findViewById(R.id.add_kefu);
        add_kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more_info.setVisibility(View.GONE);
                final BTAlertDialog dialog = new BTAlertDialog(XuQiuXiangqQing.this);
                dialog.setTitle("客服电话:");
                dialog.setTitle1("400-8530-500");
                dialog.setNegativeButton("取消", null);
                dialog.setPositiveButton("呼叫", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            Uri data = Uri.parse("tel:" + "4008818083");
                            intent.setData(data);
                            startActivity(intent);
                        }catch (Exception e){

                        }
                    }});
                dialog.show();

            }
        });
        more_info = (LinearLayout)findViewById(R.id.more_info);
        need_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(more_info.getVisibility() == View.VISIBLE){
                    more_info.setVisibility(View.GONE);
                }else if(more_info.getVisibility() == View.GONE){
                    more_info.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.removeMessages(10);
        MobclickAgent.onPageStart("需求详情");
        prgressBar.setVisibility(View.VISIBLE);
        QingBaoUpDate();
        getData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("需求详情");
    }

    private void getData(){
        String url= "http://"+ MyApplication.ip+"/api/require_new.php";
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("id",id);
        map.put("method", "detail");
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String url= "http://"+ MyApplication.ip+"/api/require_new.php?id="+id+"&method=detail";
//                json = OkHttpUtils.loaudstringfromurl(url);
//                if(json != null){
//                    Message msg = Message.obtain();
//                    msg.what = 1;
//                    handler.sendMessage(msg);
//                }
//            }
//        }).start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                prgressBar.setVisibility(View.GONE);
                Gson gson = new Gson();
                json = (String)msg.obj;
                xiangqing   = gson.fromJson(json, XuQiuXiangQingEntry.class);

                if(xiangqing != null && xiangqing.data != null){
                    resid = xiangqing.data.basic.id;
                    state = xiangqing.data.basic.state;
                    data = xiangqing.data;
                    listview.removeHeaderView(heard);
                    getheard();
                    listview.addHeaderView(heard, null, false);
                }
                if(xiangqing != null && xiangqing.data.flow!= null){
                    adapter = new XuQiuXiang();
                    listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }



            }
            if(msg.what == 2){
                Gson gson = new Gson();
                str = (String)msg.obj;
                Ret ret = gson.fromJson(str, Ret.class);
                if(ret != null){
                    if(ret.code.equals("1")){
                        getData();
                    }
                    Toast.makeText(XuQiuXiangqQing.this, ret.message.replaceAll("<br>","\n"), Toast.LENGTH_SHORT).show();
                }
            }
            if(msg.what == 3){
                Gson gs = new Gson();
                QingBaoUpdate ret=  gs.fromJson(gson, QingBaoUpdate.class);
                gson = (String)msg.obj;
                if(ret != null){
                    if(ret.code.equals("1")){
                        if(ret.data != null){
                            dianstate = ret.data.update;
                        }
                    }
                }
            }
            if(msg.what == 10){
                   if(adapter.oldtime > 1000){
                       adapter. oldtime=adapter.oldtime-1000;
                   }else{
                     handler.removeMessages(10);
                   }
                adapter.current = formatTime(adapter.oldtime);
                 Log.d("lizisong",adapter.current);
                 try {
                     if(adapter.viewText == null){
                         Log.d("lizisong","1");
                         adapter.viewText =  (TextView)listview.getChildAt(adapter.pos).findViewById(R.id.fuza_paytime);
                     }else{
                         Log.d("lizisong","2");
                         adapter.viewText.setText("支付剩余时间:"+adapter.current);
                     }

                 }catch (Exception e){

                 }


                 Message message = Message.obtain();
                 message.what =10;
                 handler.sendMessageDelayed(message, 1000);
            }
        }
    };

    public void getheard(){
        senddata.setText("发布时间:"+TimeUtils.getStrXieXIANTime(data.basic.pubdate));
        xuqiutype.setText("需求类型:"+data.basic.typename);
        description.setText(data.basic.content);
        buchong.setVisibility(View.GONE);
        if(data.basic.typename.equals("专家")){
            buchong.setText("需求专家");
            xm_img.setVisibility(View.GONE);
            rc_img.setVisibility(View.VISIBLE);
            if(data.basic.litpic != null && !data.basic.litpic.equals("")){
                ImageLoader.getInstance().displayImage(data.basic.litpic
                        , rc_img, options);
            }else{
                rc_img.setVisibility(View.VISIBLE);
                rc_img.setImageResource(R.mipmap.head_1);
            }

        }else if(data.basic.typename.equals("项目")){
            buchong.setText("需求项目");
            xm_img.setVisibility(View.VISIBLE);
            rc_img.setVisibility(View.GONE);
            if(data.basic.litpic != null && !data.basic.litpic.equals("")){
                ImageLoader.getInstance().displayImage(data.basic.litpic
                        , xm_img, options);
            }else{
                xm_img.setVisibility(View.VISIBLE);
                xm_img.setImageResource(R.mipmap.information_placeholder);
            }
        }else if(data.basic.typename.equals("设备")){
            buchong.setText("需求设备");
            xm_img.setVisibility(View.VISIBLE);
            rc_img.setVisibility(View.GONE);
            if(data.basic.litpic != null && !data.basic.litpic.equals("")){
                ImageLoader.getInstance().displayImage(data.basic.litpic
                        , xm_img, options);
            }else{
                xm_img.setVisibility(View.GONE);
            }
        }else{
            buchong.setText("");
        }
        try {
            if(data.basic.aid == null || data.basic.aid.equals("") ||data.basic.aid.equals("0") ){
                con.setVisibility(View.GONE);
                line.setVisibility(View.VISIBLE);
                xm_description.setVisibility(View.GONE);
            }else{
                line.setVisibility(View.GONE);
                con.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(XuQiuXiangqQing.this, DetailsActivity.class);
//                        intent.putExtra("id", data.basic.aid);
//                        if(data.basic.typename.equals("专家")){
//                            intent.putExtra("name", "专家");
//                        }else{
//                            intent.putExtra("name", data.basic.typename);
//                        }
//                        intent.putExtra("pic",data.basic.litpic);
//                        startActivity(intent);

                        if(data.basic.typename.equals("专家")){

                            Intent intent = new Intent(XuQiuXiangqQing.this, NewRenCaiTail.class);
                            intent.putExtra("aid", data.basic.aid);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(XuQiuXiangqQing.this, DetailsActivity.class);
                            intent.putExtra("id", data.basic.aid);
                            if(data.basic.typename.equals("专家")){
                                intent.putExtra("name", "专家");
                            }else{
                                intent.putExtra("name", data.basic.typename);
                            }
                            intent.putExtra("pic",data.basic.litpic);
                            startActivity(intent);
                        }

                    }
                });
                xm_description.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(data.basic.typename.equals("专家")){
                            Intent intent = new Intent(XuQiuXiangqQing.this, NewRenCaiTail.class);
                            intent.putExtra("aid", data.basic.aid);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(XuQiuXiangqQing.this, DetailsActivity.class);
                            intent.putExtra("id", data.basic.aid);
                            if(data.basic.typename.equals("专家")){
                                intent.putExtra("name", "专家");
                            }else{
                                intent.putExtra("name", data.basic.typename);
                            }

                            intent.putExtra("pic",data.basic.litpic);
                            startActivity(intent);
                        }


                    }
                });

            }
        }catch (Exception e){
               con.setVisibility(View.GONE);
               line.setVisibility(View.VISIBLE);
        }

        xm_rank.setText(data.basic.ranks);
        xm_title.setText(data.basic.title);
        xm_linyu.setText(data.basic.area_cate);
        xm_description.setText(data.basic.description);
        tel = data.basic.tel;
        if(data.basic.uname==null){
            jingjiren.setText("专属经济人:"+"匹配中");
        }else{
            jingjiren.setText("专属经济人:"+data.basic.uname);
        }


        phone_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tel == null || tel.equals("")) {
                    Toast.makeText(XuQiuXiangqQing.this, "暂无经纪人电话", Toast.LENGTH_SHORT).show();
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

    }
     public void tiaozhuan(String name, String price, String rank, String lingyu,String conect,String tel,
                           String datetime, String meetadress, String youhuiprice, String litpic,String typeid,String aid,String id,double x,double y){
         Intent intent = new Intent(this, PayConfirmActivity.class);
         intent.putExtra("name", name);
         intent.putExtra("price", price);
         intent.putExtra("rank", rank);
         intent.putExtra("lingyu", lingyu);
         intent.putExtra("conect", conect);
         intent.putExtra("tel", tel);
         intent.putExtra("datetime", datetime);
         intent.putExtra("meetadress", meetadress);
         intent.putExtra("youhuiprice", youhuiprice);
         intent.putExtra("litpic", litpic);
         intent.putExtra("typeid", typeid);
         intent.putExtra("aid", aid);
         intent.putExtra("id", id);
         intent.putExtra("y", y);
         intent.putExtra("x", x);
         startActivity(intent);
     }


     class XuQiuXiang extends BaseAdapter{
         public String mobile,tel;

        @Override
        public int getCount() {
            return xiangqing.data.flow.size();
        }

        @Override
        public Object getItem(int position) {
            return xiangqing.data.flow.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView holdView;
            if(convertView == null){
                holdView = new HoldView();
                convertView = View.inflate(XuQiuXiangqQing.this, R.layout.xuqiuxiangadapter, null);
                holdView.state_fuza = (LinearLayout)convertView.findViewById(R.id.state_fuza);
                holdView.fuza_time  = (TextView)convertView.findViewById(R.id.fuza_time);
                holdView.fuza_pinlunrel  = (RelativeLayout)convertView.findViewById(R.id.fuza_pinlunrel);
                holdView.fuza_pinlunrel_content = (TextView)convertView.findViewById(R.id.fuza_pinlunrel_content);
                holdView.fuza_pinlunrel_yuanyin = (TextView)convertView.findViewById(R.id.fuza_pinlunrel_yuanyin);
                holdView.fuza_pinlun_img = (ImageView)convertView.findViewById(R.id.fuza_pinlun_img);
                holdView.content=(LinearLayout)convertView.findViewById(R.id.content);
                holdView.xm_img=(ImageView)convertView.findViewById(R.id.xm_img);
                holdView.rc_img=(RoundImageView)convertView.findViewById(R.id.rc_img);
                holdView.fuza_paytime = (TextView)convertView.findViewById(R.id.fuza_paytime);
                holdView.xm_title = (TextView)convertView.findViewById(R.id.xm_title);
                holdView.xm_linyu = (TextView)convertView.findViewById(R.id.xm_linyu);
                holdView.xm_rank = (TextView)convertView.findViewById(R.id.xm_rank);
                holdView.fuza_time_lay=(LinearLayout)convertView.findViewById(R.id.fuza_time_lay);
                holdView.fuza_public_time=(TextView)convertView.findViewById(R.id.fuza_public_time);
                holdView.fuza_adress_lay=(LinearLayout)convertView.findViewById(R.id.fuza_adress_lay);
                holdView.fuza_adress=(TextView)convertView.findViewById(R.id.fuza_adress);
                holdView.dian = (ImageView)convertView.findViewById(R.id.dian);
                holdView.fuza_connect_lay=(LinearLayout)convertView.findViewById(R.id.fuza_connect_lay);
                holdView.fuza_connect=(TextView)convertView.findViewById(R.id.fuza_connect);
                holdView.fuza_anniu  = (RelativeLayout)convertView.findViewById(R.id.fuza_anniu);
                holdView.faza_share=(TextView)convertView.findViewById(R.id.faza_share);
                holdView.fuza_pay=(TextView)convertView.findViewById(R.id.fuza_pay);
                holdView.fuza_jieshou=(LinearLayout)convertView.findViewById(R.id.fuza_jieshou);
                holdView.fuza_agree=(TextView)convertView.findViewById(R.id.fuza_agree);
                holdView.fuza_cenel=(TextView)convertView.findViewById(R.id.fuza_cenel);
                holdView.fuza_line = (TextView)convertView.findViewById(R.id.fuza_line);
                holdView.fuzi_tag = (LinearLayout)convertView.findViewById(R.id.fuzi_tag);
                holdView.fuza_tag1=(TextView)convertView.findViewById(R.id.fuza_tag1);
                holdView.fuza_tag2=(TextView)convertView.findViewById(R.id.fuza_tag2);
                holdView.fuza_tag3=(TextView)convertView.findViewById(R.id.fuza_tag3);
                holdView.fuza_tag4=(TextView)convertView.findViewById(R.id.fuza_tag4);
                holdView.fuza_tag5=(TextView)convertView.findViewById(R.id.fuza_tag5);
                holdView.fuza_tag6=(TextView)convertView.findViewById(R.id.fuza_tag6);
                holdView.fuza_tag7=(TextView)convertView.findViewById(R.id.fuza_tag7);
                holdView.fuza_tag8=(TextView)convertView.findViewById(R.id.fuza_tag8);
                holdView.fuza_tag9=(TextView)convertView.findViewById(R.id.fuza_tag9);
                holdView.fuza_tag10=(TextView)convertView.findViewById(R.id.fuza_tag10);
                holdView.state_jingjiren=(LinearLayout)convertView.findViewById(R.id.state_jingjiren);
                holdView.jingjiren_time=(TextView)convertView.findViewById(R.id.jingjiren_time);
                holdView.jingjiren_name=(TextView)convertView.findViewById(R.id.jingjiren_name);
                holdView.jingjiren_tel=(TextView)convertView.findViewById(R.id.jingjiren_tel);
                holdView.faza_pingjia = (TextView)convertView.findViewById(R.id.faza_pingjia);
                holdView.state_xinxi=(LinearLayout)convertView.findViewById(R.id.state_xinxi);
                holdView.xinxi_time=(TextView)convertView.findViewById(R.id.xinxi_time);
                holdView.xinxi_context=(TextView)convertView.findViewById(R.id.xinxi_context);
                holdView.fuza_tishi_lay =(LinearLayout)convertView.findViewById(R.id.fuza_tishi_lay);
                holdView.fuza_jiage = (TextView) convertView.findViewById(R.id.fuza_jiage);

                holdView.state_cenel = (LinearLayout) convertView.findViewById(R.id.state_cenel);
                holdView.state_time = (TextView)convertView.findViewById(R.id.state_time) ;
                holdView.state_pinglun =(ImageView)convertView.findViewById(R.id.state_pinglun);
                holdView.state_pipei = (TextView)convertView.findViewById(R.id.state_pipei);
                holdView.state_tel   = (TextView)convertView.findViewById(R.id.state_tel);
                convertView.setTag(holdView);
            }else{
                holdView = (HoldView) convertView.getTag();
            }
            try {

                  final Flow item = xiangqing.data.flow.get(position);
//               if(item.state.equals("2")){//去支付
                   holdView.state_fuza.setVisibility(View.GONE);
                   holdView.fuza_paytime.setVisibility(View.GONE);
                   holdView.state_jingjiren.setVisibility(View.GONE);
                   holdView.state_xinxi.setVisibility(View.GONE);
                   holdView.fuza_tag1.setVisibility(View.GONE);
                   holdView.fuza_tag2.setVisibility(View.GONE);
                   holdView.fuza_tag3.setVisibility(View.GONE);
                   holdView.fuza_tag4.setVisibility(View.GONE);
                   holdView.fuza_tag5.setVisibility(View.GONE);
                   holdView.fuza_tag6.setVisibility(View.GONE);
                   holdView.fuza_tag7.setVisibility(View.GONE);
                   holdView.fuza_tag8.setVisibility(View.GONE);
                   holdView.fuza_tag9.setVisibility(View.GONE);
                   holdView.fuza_tag10.setVisibility(View.GONE);
                   holdView.fuza_anniu.setVisibility(View.GONE);
                   holdView.faza_pingjia.setVisibility(View.GONE);
                   holdView.state_cenel.setVisibility(View.GONE);
                   holdView.fuza_pinlunrel_yuanyin.setVisibility(View.GONE);
                   holdView.fuza_line.setVisibility(View.GONE);
                   holdView.fuza_pay.setVisibility(View.GONE);
//                   holdView.fuza_time.setVisibility(View.GONE);
                   if(item.price != null && item.datetime != null){
                       holdView.fuza_tishi_lay.setVisibility(View.VISIBLE);
                       holdView.fuza_jiage.setText(item.price+"￥ / "+item.datetime);
                   }else{
                       holdView.fuza_tishi_lay.setVisibility(View.GONE);
                   }

                   if((item.str != null && !item.str.equals("")) || (item.remark != null && !item.remark.equals(""))){
                       holdView.fuza_pinlunrel.setVisibility(View.VISIBLE);
                       if((item.str != null && !item.str.equals(""))){
                            holdView.fuza_pinlunrel_content.setVisibility(View.VISIBLE);
                            holdView.fuza_pinlunrel_content.setText(item.str);
                       }else{
                           holdView.fuza_pinlunrel_content.setVisibility(View.GONE);
                       }
                       if(item.remark != null && !item.remark.equals("")){
                           holdView.fuza_pinlunrel_yuanyin.setVisibility(View.VISIBLE);
                           holdView.fuza_pinlunrel_yuanyin.setText(item.remark);
                           holdView.fuza_pinlunrel_yuanyin.setVisibility(View.GONE);

                       }else{
                           holdView.fuza_pinlunrel_yuanyin.setVisibility(View.GONE);
                       }
                       if(dianstate.equals("1")){
                           holdView.dian.setVisibility(View.VISIBLE);
                       }else {
                           holdView.dian.setVisibility(View.GONE);
                       }
                       holdView.fuza_pinlun_img.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               Intent intent = new Intent(XuQiuXiangqQing.this, PingLunActivity.class);
                               intent.putExtra("rank", item.ranks);
                               intent.putExtra("price", item.price);
                               intent.putExtra("str", item.str);
                               intent.putExtra("adress", item.unit);
                               intent.putExtra("xm_title", item.title);
                               intent.putExtra("linyu", item.area_cate);
                               intent.putExtra("typeid", item.typeid);
                               intent.putExtra("img", item.litpic);
                               intent.putExtra("rid", item.rid);
                               intent.putExtra("y", item.longitude);
                               intent.putExtra("x", item.latitude);
                               startActivity(intent);
                           }
                       });
                   }else{
                       holdView.fuza_pinlunrel.setVisibility(View.GONE);
                   }
//                holdView.fuza_pinlunrel_yuanyin.setVisibility(View.VISIBLE);
                   holdView.content.setBackgroundResource(R.color.content_bg);
                   if(item.typeid != null){
                       holdView.content.setVisibility(View.VISIBLE);
                       if(item.typeid.equals("4")){
                           holdView.xm_rank.setVisibility(View.VISIBLE);
                           holdView.xm_img.setVisibility(View.GONE);
                           holdView.rc_img.setVisibility(View.VISIBLE);
                           if(item.litpic != null && !item.litpic.equals("")){
                               ImageLoader.getInstance().displayImage(item.litpic
                                       , holdView.rc_img, options);
                           }else{
                               holdView.xm_img.setVisibility(View.GONE);
                           }
                       }else if(item.typeid.equals("7") || item.typeid.equals("2")){
                           holdView.xm_rank.setVisibility(View.GONE);
                           holdView.xm_img.setVisibility(View.VISIBLE);
                           holdView.rc_img.setVisibility(View.GONE);
                           if(item.litpic != null && !item.litpic.equals("")){
                               ImageLoader.getInstance().displayImage(item.litpic
                                       , holdView.xm_img, options);
                           }else{
                               holdView.rc_img.setVisibility(View.GONE);
                               holdView.xm_img.setImageResource(R.mipmap.information_placeholder);
                           }
                       }
                   }else{
                       holdView.content.setVisibility(View.GONE);
                   }

                   holdView.xm_title.setText(item.title);
                   holdView.xm_linyu.setText(item.area_cate);
                   if(item.ranks != null && !item.ranks.equals("")){
                       holdView.xm_rank.setText(item.ranks);
                   }else{
                       holdView.xm_rank.setText("");
                   }

                   if(item.meetdate != null && !item.meetdate.equals("")){
                       holdView.fuza_time_lay.setVisibility(View.VISIBLE);
                       holdView.fuza_public_time.setText(TimeUtils.getStrMonthAndDataTime(item.meetdate));
                   }else{
                       holdView.fuza_time_lay.setVisibility(View.GONE);
                   }

                   if(item.unit != null && !item.unit.equals("")){
                       holdView.fuza_adress_lay.setVisibility(View.VISIBLE);
                       holdView.fuza_adress.setText(item.unit+"  ");
                       holdView.fuza_adress.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               if(item.longitude >1 && item.latitude >1){
                                   Intent intent =new Intent(XuQiuXiangqQing.this, BaseMapDemo.class);
                                   intent.putExtra("y", item.longitude);
                                   intent.putExtra("x", item.latitude);
                                   startActivity(intent);
                               }
                           }
                       });
                   }else{
                       holdView.fuza_adress_lay.setVisibility(View.GONE);
                   }

                   if(item.meetmobile != null && !item.meetmobile.equals("")){
                       holdView.fuza_connect_lay.setVisibility(View.VISIBLE);
                       try {
                           String [] mobileAndTel = item.meetmobile.split("/");
                           mobile = mobileAndTel[0];
                           tel    = mobileAndTel[1];
                       }catch (Exception e){

                       }

                       holdView.fuza_connect.setText(mobile+"("+tel+")");
                       holdView.fuza_connect.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {
                               try {
                                   Intent intent = new Intent(Intent.ACTION_DIAL);
                                   Uri data = Uri.parse("tel:" + tel);
                                   intent.setData(data);
                                   startActivity(intent);
                               }catch (Exception e){

                               }
                           }
                       });
                   }else{
                       holdView.fuza_connect_lay.setVisibility(View.GONE);
                   }


//                if((System.currentTimeMillis() - Long.parseLong(item.time)*1000) > 3*24*60*60*1000){
//                    holdView.fuza_time.setText("  "+TimeUtils.getStrMonthAndDataTime(item.time));
//                }else{
//                    String data = TimeUtils.getStandardDate(item.time);
//                    holdView.fuza_time.setText("  "+data);
//                }

                holdView.fuza_time.setText("  " + TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));
                if(item.state.equals("50") || item.state.equals("100")){
                    holdView.fuza_line.setVisibility(View.GONE);
                    holdView.state_fuza.setVisibility(View.VISIBLE);
                    holdView.fuza_anniu.setVisibility(View.GONE);
                    holdView.faza_pingjia.setVisibility(View.GONE);
                    holdView.fuza_pay.setVisibility(View.GONE);
                    holdView.faza_share.setVisibility(View.GONE);
                    holdView.fuza_jieshou.setVisibility(View.GONE);
                    holdView.content.setBackgroundResource(R.drawable.shap_content);
                    holdView.faza_pingjia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(XuQiuXiangqQing.this,CommentActivity.class);
                            intent.putExtra("typeid", item.typeid);
                            intent.putExtra("img", item.litpic);
                            intent.putExtra("title", item.title);
                            intent.putExtra("rank", item.ranks);
                            intent.putExtra("y", item.longitude);
                            intent.putExtra("x", item.latitude);
                            intent.putExtra("linyu", item.area_cate);
                            intent.putExtra("time", item.meetdate);
                            intent.putExtra("adress", item.unit);
                            intent.putExtra("resource_id", item.id);
                            startActivity(intent);
                        }
                    });

                    holdView.content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(item.typeid.equals("4")){
                                Intent intent = new Intent(XuQiuXiangqQing.this, NewRenCaiTail.class);
                                intent.putExtra("aid", item.aid);
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(XuQiuXiangqQing.this, DetailsActivity.class);
                                intent.putExtra("id", item.aid);
//                            if(data.basic.typename.equals("专家")){
//                                intent.putExtra("name", "专家");
//                            }else{
//                                intent.putExtra("name", data.basic.typename);
//                            }
                                if(item.typeid.equals("2")){
                                    intent.putExtra("name", "项目");
                                }else if(item.typeid.equals("7")){
                                    intent.putExtra("name", "设备");
                                }else if(item.typeid.equals("4")){
                                    intent.putExtra("name", "专家");
                                }
                                intent.putExtra("pic",item.litpic);
                                startActivity(intent);
                            }

                        }
                    });

                }else if(item.state.equals("2")){//去支付
                    holdView.state_jingjiren.setVisibility(View.GONE);
                    holdView.state_xinxi.setVisibility(View.GONE);
                    holdView.fuza_paytime.setVisibility(View.VISIBLE);
                    holdView.fuza_anniu.setVisibility(View.VISIBLE);
                    holdView.fuza_pay.setVisibility(View.VISIBLE);
                    if(state.equals("13") || state.equals("14") || state.equals("50") || state.equals("100")){
                        holdView.fuza_anniu.setVisibility(View.GONE);
                    }
                    holdView.fuza_pay.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           viewText = null;
                           handler.removeMessages(10);
                           // 去支付流程
                           tiaozhuan(item.title, item.price, item.ranks, item.area_cate,mobile, tel,
                                   item.meetdate,item.unit, null, item.litpic,item.typeid,item.aid,item.id,item.latitude,item.longitude);
                       }
                    });

                    holdView.content.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(item.typeid.equals("4")){
                                Intent intent = new Intent(XuQiuXiangqQing.this, NewRenCaiTail.class);
                                intent.putExtra("aid", item.aid);
                                startActivity(intent);

                            }else{
                                Intent intent = new Intent(XuQiuXiangqQing.this, DetailsActivity.class);
                                intent.putExtra("id", item.aid);
//                            if(data.basic.typename.equals("专家")){
//                                intent.putExtra("name", "专家");
//                            }else{
//                                intent.putExtra("name", data.basic.typename);
//                            }
                                if(item.typeid.equals("2")){
                                    intent.putExtra("name", "项目");
                                }else if(item.typeid.equals("7")){
                                    intent.putExtra("name", "设备");
                                }else if(item.typeid.equals("4")){
                                    intent.putExtra("name", "专家");
                                }
                                intent.putExtra("pic",item.litpic);
                                startActivity(intent);
                            }

                        }
                    });


                    holdView.faza_share.setVisibility(View.GONE);
                    holdView.fuza_jieshou.setVisibility(View.GONE);
                    holdView.state_fuza.setVisibility(View.VISIBLE);
//                    if((System.currentTimeMillis() - Long.parseLong(item.time)*1000) > 3*24*60*60*1000){
//                        holdView.fuza_time.setText("  "+TimeUtils.getStrMonthAndDataTime(item.time));
//                    }else{
//                        String data = TimeUtils.getStandardDate(item.time);
//                        holdView.fuza_time.setText("  "+data);
//                    }
//                    mTextView =holdView.fuza_time;
                    holdView.fuza_time.setText(  TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));
                    oldtime =(Long.parseLong(item.time)*1000+172800000)-System.currentTimeMillis()  ;
                    if(oldtime >1000){
                        holdView.fuza_paytime.setText(current);
                        handler.removeMessages(10);
                        viewText= holdView.fuza_paytime;
                        Message msg = Message.obtain();
                        msg.what =10;
                        handler.sendMessage(msg);
                        pos = position;

                        Log.d("lizisong","send");
                        handler.sendMessage(msg);
                    }else{
                        holdView.fuza_paytime.setVisibility(View.VISIBLE);
                        viewText =  holdView.fuza_paytime;
                        current = "";
                        pos = 0;
                        handler.removeMessages(10);

                    }
                }else if(item.state.equals("1")){
                    holdView.fuza_pay.setVisibility(View.GONE);
                    holdView.state_jingjiren.setVisibility(View.VISIBLE);
                    holdView.state_fuza.setVisibility(View.GONE);
                    holdView.state_xinxi.setVisibility(View.GONE);
                    holdView.faza_pingjia.setVisibility(View.GONE);
//                    if((System.currentTimeMillis() - Long.parseLong(item.time)*1000) > 3*24*60*60*1000){
//                        holdView.jingjiren_time.setText("  "+TimeUtils.getStrMonthAndDataTime(item.time));
//                    }else{
//                         String data = TimeUtils.getStandardDate(item.time);
//                         holdView.jingjiren_time.setText("  "+data);
//                    }
                    holdView.jingjiren_time.setText( TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));
                    holdView.jingjiren_name.setText(item.uname);
                    holdView.jingjiren_tel.setText(item.tel);


                }else if(item.state.equals("0")){
                    holdView.state_jingjiren.setVisibility(View.GONE);
                    holdView.state_fuza.setVisibility(View.GONE);
                    holdView.state_xinxi.setVisibility(View.VISIBLE);
                    holdView.fuza_pay.setVisibility(View.GONE);
//                    if((System.currentTimeMillis() - Long.parseLong(item.time)*1000) > 3*24*60*60*1000){
//                        holdView.xinxi_time.setText("  "+TimeUtils.getStrMonthAndDataTime(item.time));
//                    }else{
//                        String data = TimeUtils.getStandardDate(item.time);
//                        holdView.xinxi_time.setText("  "+data);
//                    }
                    holdView.xinxi_time.setText( TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));

                    holdView.xinxi_context.setText(item.content);
                }else if(item.state.equals("12") || item.state.equals("13")){
                    holdView.fuza_pay.setVisibility(View.GONE);
                    holdView.state_cenel.setVisibility(View.VISIBLE);
//                    if((System.currentTimeMillis() - Long.parseLong(item.time)*1000) > 3*24*60*60*1000){
//                        holdView.state_time.setText("  "+TimeUtils.getStrMonthAndDataTime(item.time));
//                    }else{
//                        String data = TimeUtils.getStandardDate(item.time);
//                        holdView.state_time.setText("  "+data);
//                    }
                    holdView.state_time.setText( TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));
                    holdView.state_pipei.setText(item.content);
                    holdView.state_tel.setText(item.tel);
                    holdView.state_tel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                Uri data = Uri.parse("tel:" + item.tel);
                                intent.setData(data);
                                startActivity(intent);
                            }catch (Exception e){

                            }
                        }
                    });

                }else if(item.state.equals("9")){
                    holdView.fuza_time.setText(  TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.time)*1000), item.time));
                    holdView.state_fuza.setVisibility(View.VISIBLE);
                    holdView.fuza_anniu.setVisibility(View.VISIBLE);
                    if(state.equals("13") || state.equals("14") || state.equals("50") || state.equals("100")){
                        holdView.fuza_anniu.setVisibility(View.GONE);
                    }

                    holdView.faza_share.setVisibility(View.GONE);
                    holdView.fuza_jieshou.setVisibility(View.VISIBLE);
                    holdView.fuza_pay.setVisibility(View.GONE);
                    holdView.fuza_time.setVisibility(View.VISIBLE);
                    holdView.faza_pingjia.setVisibility(View.GONE);
                    //同意
                    holdView.fuza_agree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            prgressBar.setVisibility(View.VISIBLE);
                            user ="1";
                            AgreeOrCenel(item.id);

                        }
                    });
                    //拒绝
                    holdView.fuza_cenel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            user ="0";
                            AgreeOrCenel(item.id);
                        }
                    });

                }else if(item.state.equals("7")){
                    holdView.state_fuza.setVisibility(View.VISIBLE);
                    holdView.fuza_anniu.setVisibility(View.VISIBLE);
                    holdView.faza_pingjia.setVisibility(View.VISIBLE);
                    if(state.equals("13") || state.equals("14") || state.equals("50") || state.equals("100")){
                        holdView.fuza_anniu.setVisibility(View.GONE);
                    }
                    holdView.faza_share.setVisibility(View.GONE);
                    holdView.fuza_pay.setVisibility(View.GONE);
                    holdView.fuza_jieshou.setVisibility(View.GONE);

                    holdView.fuza_pay.setVisibility(View.GONE);
                    holdView.fuza_paytime.setVisibility(View.GONE);

                    holdView.faza_pingjia.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(XuQiuXiangqQing.this,CommentActivity.class);
                            intent.putExtra("typeid", item.typeid);
                            intent.putExtra("img", item.litpic);
                            intent.putExtra("title", item.title);
                            intent.putExtra("rank", item.ranks);
                            intent.putExtra("y", item.longitude);
                            intent.putExtra("x", item.latitude);
                            intent.putExtra("linyu", item.area_cate);
                            intent.putExtra("time", item.meetdate);
                            intent.putExtra("adress", item.unit);
                            intent.putExtra("resource_id", item.id);
                            startActivity(intent);
                        }
                    });
                }else if(item.state.equals("3") || item.state.equals("5") || item.state.equals("6") || item.state.equals("4")
                        || item.state.equals("8")|| item.state.equals("10")){
                    holdView.state_fuza.setVisibility(View.VISIBLE);
                    holdView.fuza_anniu.setVisibility(View.GONE);
                    holdView.fuza_pay.setVisibility(View.GONE);
                }else if(item.state.equals("11")){
                    holdView.state_fuza.setVisibility(View.VISIBLE);
                    holdView.fuza_anniu.setVisibility(View.VISIBLE);
                    holdView.faza_pingjia.setVisibility(View.GONE);
                    holdView.faza_share.setVisibility(View.VISIBLE);
                    holdView.fuza_pay.setVisibility(View.GONE);
                    holdView.fuza_time.setVisibility(View.VISIBLE);
                    holdView.faza_pingjia.setVisibility(View.GONE);

                    if(state.equals("13") || state.equals("14") || state.equals("50") || state.equals("100")){
                        holdView.fuza_anniu.setVisibility(View.GONE);
                    }
                    holdView.faza_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(XuQiuXiangqQing.this, "此功能暂未开放",Toast.LENGTH_SHORT).show();
                        }
                    });
                    holdView.fuza_pay.setVisibility(View.GONE);
                    holdView.fuza_jieshou.setVisibility(View.GONE);
                    if(state.equals("13") || state.equals("14") || state.equals("50") || state.equals("100")){
                        holdView.fuza_anniu.setVisibility(View.GONE);
                    }
                }else{
                    holdView.fuza_anniu.setVisibility(View.GONE);
                }
                if(item.end != null && item.end.size() >0){
                    holdView.fuzi_tag.setVisibility(View.VISIBLE);
                    holdView.fuza_line.setVisibility(View.VISIBLE);
                    for(int i=0; i<item.end.size(); i++){
                        String data = null;
                        Flows pos = item.end.get(i);

                        if((System.currentTimeMillis() - Long.parseLong(item.time)*1000) > 3*24*60*60*1000){
                            data = TimeUtils.getStrMonthAndDataTime(pos.time);
                        }else{
                            data = TimeUtils.getStandardDate(pos.time);
                        }

                        if(i == 0){
                            holdView.fuza_tag1.setVisibility(View.VISIBLE);
                            holdView.fuza_tag1.setText( "  "+data+":"+pos.content);
                        }else if(i == 1){
                            holdView.fuza_tag2.setVisibility(View.VISIBLE);
                            holdView.fuza_tag2.setText("  "+data+":"+pos.content);
                        }else if(i == 2){
                            holdView.fuza_tag3.setVisibility(View.VISIBLE);
                            holdView.fuza_tag3.setText("  "+data+":"+pos.content);
                        }else if(i == 3){
                            holdView.fuza_tag4.setVisibility(View.VISIBLE);
                            holdView.fuza_tag4.setText("  "+data+":"+pos.content);
                        }else if(i == 4){
                            holdView.fuza_tag5.setVisibility(View.VISIBLE);
                            holdView.fuza_tag5.setText("  "+data+":"+pos.content);
                        }else if(i == 5){
                            holdView.fuza_tag6.setVisibility(View.VISIBLE);
                            holdView.fuza_tag6.setText("  "+data+":"+pos.content);
                        }else if(i == 6){
                            holdView.fuza_tag7.setVisibility(View.VISIBLE);
                            holdView.fuza_tag7.setText("  "+data+":"+pos.content);
                        }else if(i == 7){
                            holdView.fuza_tag8.setVisibility(View.VISIBLE);
                            holdView.fuza_tag8.setText("  "+data+":"+pos.content);
                        }else if(i == 8){
                            holdView.fuza_tag9.setVisibility(View.VISIBLE);
                            holdView.fuza_tag9.setText("  "+data+":"+pos.content);
                        }else if(i == 9){
                            holdView.fuza_tag10.setVisibility(View.VISIBLE);
                            holdView.fuza_tag10.setText("  "+data+":"+pos.content);
                        }
                    }
                }else{
                    holdView.fuza_line.setVisibility(View.GONE);
                    holdView.fuzi_tag.setVisibility(View.GONE);
                }





                holdView.content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(item.typeid.equals("4")){
                            Intent intent = new Intent(XuQiuXiangqQing.this, NewRenCaiTail.class);
                            intent.putExtra("aid", item.aid);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(XuQiuXiangqQing.this, DetailsActivity.class);
                            intent.putExtra("id", item.aid);
//                            if(data.basic.typename.equals("专家")){
//                                intent.putExtra("name", "专家");
//                            }else{
//                                intent.putExtra("name", data.basic.typename);
//                            }
                            if(item.typeid.equals("2")){
                                intent.putExtra("name", "项目");
                            }else if(item.typeid.equals("7")){
                                intent.putExtra("name", "设备");
                            }else if(item.typeid.equals("4")){
                                intent.putExtra("name", "专家");
                            }
                            intent.putExtra("pic",item.litpic);
                            startActivity(intent);
                        }

                    }
                });



            }catch (Exception e){

            }

            return convertView;
        }

        private String currentTimeStr(String time){

           return null;
        }

        class HoldView{
           // 复杂情况
            public LinearLayout state_fuza;
            public TextView  fuza_time;
            //评论
            public RelativeLayout fuza_pinlunrel;
            public TextView  fuza_pinlunrel_content;
            public TextView fuza_pinlunrel_yuanyin;
            public ImageView fuza_pinlun_img,dian;
            //人才项目
            public LinearLayout content;
            public ImageView  xm_img;
            public RoundImageView rc_img;
            public TextView xm_title;
            public TextView xm_linyu;
            public TextView xm_rank;
            //提示
            public LinearLayout fuza_tishi_lay;
            public TextView fuza_jiage;
            //时间
            public LinearLayout fuza_time_lay;
            public TextView fuza_public_time;
            public TextView fuza_paytime;
            public LinearLayout fuza_adress_lay;
            public TextView fuza_adress;
            public  LinearLayout fuza_connect_lay;
            public TextView fuza_connect;
            public RelativeLayout fuza_anniu;
            public TextView faza_share;
            public TextView fuza_pay;
            public LinearLayout fuza_jieshou,fuzi_tag;
            public TextView fuza_agree;
            public TextView fuza_cenel;
            public TextView fuza_tag1;
            public TextView fuza_tag2;
            public TextView fuza_tag3;
            public TextView fuza_tag4;
            public TextView fuza_tag5;
            public TextView fuza_tag6;
            public TextView fuza_tag7;
            public TextView fuza_tag8;
            public TextView fuza_tag9;
            public TextView fuza_tag10;
            public TextView faza_pingjia;
            public TextView fuza_line;
            //经纪人
            public LinearLayout state_jingjiren;
            public TextView jingjiren_time;
            public TextView jingjiren_name;
            public TextView jingjiren_tel;
            //简单匹配
            public LinearLayout state_xinxi;
            public TextView xinxi_time;
            public TextView xinxi_context;

            //11,12状态
            public LinearLayout state_cenel;
            public TextView state_time;
            public ImageView state_pinglun;
            public TextView state_pipei;
            public TextView state_tel;
        }

         public TextView viewText=null;
         public String current;
         public   View mView;
         public   long oldtime = 0;
         public    int pos;
//       public   Handler handler1 = new Handler(){
//             @Override
//             public void handleMessage(Message msg) {
//                 super.handleMessage(msg);
//                 if(oldtime > 1000){
//                     oldtime=oldtime-1000;
//                 }else{
//                     handler1.removeMessages(1);
//                 }
//                 current = formatTime(oldtime);
//                 Log.d("lizisong",current);
//                 try {
//                     if(viewText == null){
//                         Log.d("lizisong","1");
//                         viewText =  (TextView)listview.getChildAt(pos).findViewById(R.id.fuza_paytime);
//                     }else{
//                         Log.d("lizisong","2");
//                         viewText.setText(current);
//                     }
//
//                 }catch (Exception e){
//
//                 }
//
//
//                 Message message = Message.obtain();
//                 message.what =1;
//                 handler1.sendMessageDelayed(message, 1000);
////                 adapter.notifyDataSetChanged();
//             }
//         };



    }



    /**
     * 同意或者拒绝的接口
     **/
    public void AgreeOrCenel(final String id1){
        String url = "http://"+ MyApplication.ip+"/api/require_user_feedback.php";
        HashMap<String,String> map = new HashMap<String, String>();
        map.put("id", id1);
        map.put("method", "feedback");
        map.put("userfk",user );
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,2,0);
//        new Thread(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        String url = "http://"+ MyApplication.ip+"/api/require_user_feedback.php?id="+id1+"&method=feedback&userfk="+user;
//                        str = OkHttpUtils.loaudstringfromurl(url);
//                        if(str != null){
//                            Message msg = Message.obtain();
//                            msg.what = 2;
//                            handler.sendMessage(msg);
//                        }
//                    }
//                }
//        ).start();
    }

    /*
 * 毫秒转化时分秒毫秒
 */
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

      //  StringBuffer sb = new StringBuffer();
        String ret="";

        if(day > 0) {
//            sb.append(day+"天");
            hour =hour+day*24;
        }
        if(hour > 0) {
            //sb.append(hour+":");
            ret = hour+"时";
        }else{
            ret ="00时";
        }
        if(minute > 0) {
           // sb.append(minute+":");
            ret=ret+minute+"分";
        }else{
            ret=ret+"00分";
        }
        if(second > 0) {
            //sb.append(second+":");
            ret = ret+second+"秒";
        }else{
            ret = ret+"00秒";
        }
//        if(milliSecond > 0) {
//            sb.append(milliSecond+"毫秒");
//        }
        return ret;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(10);
    }

    private void QingBaoUpDate(){
        String url = "http://"+MyApplication.ip+"/api/getCommentUpdate.php";
        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
        HashMap<String,String> map = new HashMap<>();
        map.put("rid",id);
        map.put("mid",mid);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.postJson(url,map,handler,3,0);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
//                String url = "http://"+MyApplication.ip+"/api/getCommentUpdate.php?"+"&rid="+id+"&mid="+mid;
//                gson = OkHttpUtils.loaudstringfromurl(url);
//                if(gson != null){
//                    Message message = Message.obtain();
//                    message.what = 3;
//                    handler.sendMessage(message);
//                }
//
//            }
//        }).start();
    }

}
