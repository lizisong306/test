package com.maidiantech;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.maidiantech.common.resquest.NetworkCom;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.MyXuQiuData;
import entity.MyXuQiuEntiry;
import entity.XuQiudetailsdata;
import view.AutoTextView;
import view.RefreshListView;
import view.RoundImageView;
import Util.TimeUtil;
import Util.*;
import view.ShapeImageView;

/**
 * Created by lizisong on 2017/9/26.
 */

public class MyXuqiuActivity extends AutoLayoutActivity {
    ImageView back;
    TextView title;
    String  titletxt;
    ImageView need_add;
    String mid ="";
    RelativeLayout welcome,datalist;
    RefreshListView listview;
    Button add_need;
//    XuqiuAdapter adapter;
    private OkHttpUtils Okhttp;
    private String jsons;
    private ProgressBar progress;
    private  String   ips;
    List<XuQiudetailsdata> postsListData;
    DisplayImageOptions options;
    List<MyXuQiuData> showList = new ArrayList<>();
    String type = "-1";
    Adapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.xuqiuframent);
        options = ImageLoaderUtils.initOptions();
        titletxt = "我的需求";
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title = (TextView)findViewById(R.id.titlecontent);
        need_add=(ImageView) findViewById(R.id.need_add);
        welcome = (RelativeLayout)findViewById(R.id.welcome);
        datalist = (RelativeLayout) findViewById(R.id.datalist);
        listview = (RefreshListView)findViewById(R.id.listview);
        listview.setPullDownToRefreshable(true);
        listview.setOnPullDownToRefreshListener(new RefreshListView.OnPullDownToRefreshListener() {
            @Override
            public void pullDownToRefresh() {
                getjsons();
            }
        });

        add_need = (Button)findViewById(R.id.add_need);
        add_need.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyXuqiuActivity.this, NewXuQiu.class);
                startActivity(intent);
            }
        });

        progress = (ProgressBar)findViewById(R.id.progress);


        need_add.setVisibility(View.VISIBLE);
        need_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyXuqiuActivity.this, NewXuQiu.class);
                startActivity(intent);
            }
        });
        type = getIntent().getStringExtra("type");
        if(type.equals("-1")){
            titletxt = "我的需求";
        }else if(type.equals("4")){
            titletxt = "专家预约";
        }else if(type.equals("2")){
            titletxt = "项目预约";
        }else if(type.equals("7")){
            titletxt = "设备预约";
        }
        title.setText(titletxt);
    }


    @Override
    public void onResume() {
        super.onResume();
        int netWorkType = NetUtils.getNetWorkType(MyApplication
                .getContext());
        if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
//            updatedata();
            Toast.makeText(MyXuqiuActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
        } else {
            progress.setVisibility(View.VISIBLE);
            getjsons();
        }
    }


    public void getjsons(){
        String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
        String url ="http://"+MyApplication.ip+"/api/new_require.php";
        HashMap<String ,String> map = new HashMap<>();
        map.put("method","list");
        map.put("identity","0");
        map.put("mid",mid);
        map.put("typeid", type);
        NetworkCom networkCom = NetworkCom.getNetworkCom();
        networkCom.getJson(url,map,handler,1,0);
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                try {
                    progress.setVisibility(View.GONE);
                    listview.setPullDownToRefreshFinish();
                    String ret = (String)msg.obj;
                    Gson gs = new Gson();
                    MyXuQiuEntiry data = gs.fromJson(ret, MyXuQiuEntiry.class);
                    if(data != null && data.code == 1){
                        showList.clear();
                        if(data.data != null && data.data.size() >0){
                            for (int i=0; i<data.data.size(); i++){
                                MyXuQiuData item = data.data.get(i);
                                showList.add(item);
                            }
                        }
                        if(adapter == null){
                            adapter = new Adapter();
                            listview.setAdapter(adapter);
                        }else{
                            adapter.notifyDataSetChanged();
                        }
                        if(showList.size() >0){
                            datalist.setVisibility(View.VISIBLE);
                            welcome.setVisibility(View.GONE);
                        }

                    }else{
                        datalist.setVisibility(View.GONE);
                        welcome.setVisibility(View.VISIBLE);
                    }

                }catch (Exception e){

                }

            }

        }
    };


    class Adapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup parent) {
            HoldView holdView = null;
            if(convertView == null){
                holdView = new HoldView();
                convertView = View.inflate(MyXuqiuActivity.this,R.layout.myxuqiuadapter, null);
                holdView.putongxuqiu = convertView.findViewById(R.id.putongxuqiu);
                holdView.date = convertView.findViewById(R.id.date);
                holdView.type = convertView.findViewById(R.id.type);
                holdView.content = convertView.findViewById(R.id.content);
                holdView.des = convertView.findViewById(R.id.des);
                holdView.jindu = convertView.findViewById(R.id.jindu);
                holdView.daiziyuan = convertView.findViewById(R.id.daiziyuan);
                holdView.ziyuandate = convertView.findViewById(R.id.ziyuandate);
                holdView.ziyuantype = convertView.findViewById(R.id.ziyuantype);
                holdView.ziyuancontent = convertView.findViewById(R.id.ziyuancontent);
                holdView.rencai_title = convertView.findViewById(R.id.rencai_title);
                holdView.rencai_lingyu = convertView.findViewById(R.id.rencai_lingyu);
                holdView.rank = convertView.findViewById(R.id.rank);
                holdView.xmtitle = convertView.findViewById(R.id.xmtitle);
                holdView.lanyuan = convertView.findViewById(R.id.lanyuan);
                holdView.unitname = convertView.findViewById(R.id.unitname);
                holdView.unitadress = convertView.findViewById(R.id.unitadress);
                holdView.ketizu_title = convertView.findViewById(R.id.ketizu_title);
                holdView.ketizu_lingyu = convertView.findViewById(R.id.ketizu_lingyu);
                holdView.wutu = convertView.findViewById(R.id.wutu);
                holdView.ketizurank = convertView.findViewById(R.id.ketizurank);
                holdView.ziyuanjindu = convertView.findViewById(R.id.ziyuanjindu);
                holdView.ziyuandes = convertView.findViewById(R.id.ziyuandes);
                holdView.ziyuanleixing = convertView.findViewById(R.id.ziyuanleixing);
                holdView.zhuanjia = convertView.findViewById(R.id.zhuanjia);
                holdView.xiangmulay = convertView.findViewById(R.id.xiangmulay);
                holdView.keyanjigou = convertView.findViewById(R.id.keyanjigou);
                holdView.ketizu = convertView.findViewById(R.id.ketizu);
                holdView.rencai_img = convertView.findViewById(R.id.rencai_img);
                holdView.xianmgmu = convertView.findViewById(R.id.xianmgmu);
                holdView.ziyuandeslay = convertView.findViewById(R.id.ziyuandeslay);
                holdView.img = convertView.findViewById(R.id.img);
                holdView.ketizu_img = convertView.findViewById(R.id.ketizu_img);
                holdView.deslay = convertView.findViewById(R.id.deslay);
                convertView.setTag(holdView);
            }else {
                holdView = (HoldView) convertView.getTag();
            }
            final MyXuQiuData item = showList.get(position);
            try {
                if(item.typeid.equals("0")){
                    holdView.putongxuqiu.setVisibility(View.VISIBLE);
                    holdView.daiziyuan.setVisibility(View.GONE);
                    holdView.ziyuanleixing.setVisibility(View.GONE);
                    long sys = (System.currentTimeMillis()-Long.parseLong(item.pubdate)*1000)/1000;
                    String time = TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.pubdate)*1000), sys+"");
                    holdView.date.setText(time);
                    holdView.type.setText("需求类型："+item.typename);
                    holdView.content.setText(item.content.replaceAll("\r\n", "").replaceAll("\n", ""));
                    if(item.is_click.equals("1")){
                        holdView.jindu.setVisibility(View.VISIBLE);
                    }else if(item.is_click.equals("0")){
                        holdView.jindu.setVisibility(View.GONE);
                    }
                    holdView.des.setText(item.str);
                    if(item.str == null || item.str.equals("")){
                        holdView.deslay.setVisibility(View.GONE);
                    }else{
                        holdView.deslay.setVisibility(View.VISIBLE);
                    }
                    holdView.putongxuqiu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MyXuqiuActivity.this, XuQiuLiuCheng.class);
                            intent.putExtra("id", item.id);
                            startActivity(intent);
                        }
                    });
                    holdView.putongxuqiu.setOnClickListener(new NoDoubleClick() {
                        @Override
                        public void Click(View v) {
                            Intent intent = new Intent(MyXuqiuActivity.this, XuQiuLiuCheng.class);
                            intent.putExtra("id", item.id);
                            startActivity(intent);
                        }
                    });
                }else{
                    holdView.putongxuqiu.setVisibility(View.GONE);
                    holdView.daiziyuan.setVisibility(View.VISIBLE);
                    holdView.ziyuanleixing.setVisibility(View.VISIBLE);
                    holdView.zhuanjia.setVisibility(View.GONE);
                    holdView.xiangmulay.setVisibility(View.GONE);
                    holdView.keyanjigou.setVisibility(View.GONE);
                    holdView.ketizu.setVisibility(View.GONE);
                    long sys = (System.currentTimeMillis()-Long.parseLong(item.pubdate)*1000)/1000;
                    String time = TimeUtil.getTimeFormatText(new Date(Long.parseLong(item.pubdate)*1000), sys+"");
                    holdView.ziyuandate.setText(time);
                    holdView.ziyuancontent.setText(item.content.replaceAll("\r\n", "").replaceAll("\n", "")+"\t\t");
                    holdView.ziyuantype.setText("需求类型："+item.typename);
                    if(item.typeid.equals("2")){
                        holdView.xiangmulay.setVisibility(View.VISIBLE);
                        if(item.litpic == null || item.litpic.equals("")){
                            holdView.xianmgmu.setImageResource(R.mipmap.information_placeholder);
                        }else{
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.xianmgmu, options);
                        }

                        holdView.xmtitle.setText(item.title);
                        holdView.lanyuan.setText(item.area_cate);

                        holdView.xiangmulay.setOnClickListener(new NoDoubleClick() {
                            @Override
                            public void Click(View v) {
                               Intent intent = new Intent(MyXuqiuActivity.this,NewProjectActivity.class);
                               intent.putExtra("aid",item.aid);
                               startActivity(intent);
                            }
                        });
                    }else if(item.typeid.equals("4")){
                        holdView.zhuanjia.setVisibility(View.VISIBLE);
                        if(item.litpic == null || item.litpic.equals("")){
                            holdView.rencai_img.setVisibility(View.GONE);
                            holdView.wutu.setVisibility(View.VISIBLE);
                            if(item.title != null && !item.title.equals("")){
                                String txt = item.title.substring(0,1);
                                holdView.wutu.setText(txt);
                            }
                        }else{
                            holdView.rencai_img.setVisibility(View.VISIBLE);
                            holdView.wutu.setVisibility(View.GONE);
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.rencai_img, options);
                        }
                        holdView.rencai_title.setText(item.title);
                        holdView.rencai_lingyu.setText(item.unit);
                        holdView.rank.setText(item.ranks);
                        holdView.zhuanjia.setOnClickListener(new NoDoubleClick() {
                            @Override
                            public void Click(View v) {
                                Intent intent = new Intent(MyXuqiuActivity.this,XinFanAnCeShi.class);
                                intent.putExtra("aid",item.aid);
                                startActivity(intent);
                            }
                        });
                    }else if(item.typeid.equals("7")){
                        holdView.xiangmulay.setVisibility(View.VISIBLE);
                        if(item.litpic == null || item.litpic.equals("")){
                            holdView.xianmgmu.setImageResource(R.mipmap.information_placeholder);
                        }else{
                            ImageLoader.getInstance().displayImage(item.litpic
                                    , holdView.xianmgmu, options);
                        }
                        holdView.xmtitle.setText(item.title);
                        holdView.lanyuan.setText(item.area_cate);
                        holdView.xiangmulay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MyXuqiuActivity.this, SheBeiDeilActivity.class);
                                intent.putExtra("aid", item.aid);
                                startActivity(intent);
                            }
                        });
                    }else if(item.typeid.equals("8") || item.typeid.equals("16")){
                        holdView.ketizu.setVisibility(View.VISIBLE);
                        holdView.keyanjigou.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(item.litpic
                                ,  holdView.img, options);
                        holdView.unitname.setText(item.title);
                        holdView.unitadress.setText(item.area_cate);
                    }
                    holdView.content.setText(item.content);
                    if(item.is_click.equals("1")){
                        holdView.ziyuanjindu.setVisibility(View.VISIBLE);
                    }else if(item.is_click.equals("0")){
                        holdView.ziyuanjindu.setVisibility(View.GONE);
                    }
                    holdView.ziyuandes.setText(item.str);
                    if(item.str == null || item.str.equals("")){
                        holdView.ziyuandeslay.setVisibility(View.GONE);
                    }else{
                        holdView.ziyuandeslay.setVisibility(View.VISIBLE);
                    }
                    holdView.daiziyuan.setOnClickListener(new NoDoubleClick() {
                        @Override
                        public void Click(View v) {
                            Intent intent = new Intent(MyXuqiuActivity.this, XuQiuLiuCheng.class);
                            intent.putExtra("id", item.id);
                            startActivity(intent);
                        }
                    });
                }
            }catch (Exception e){

            }
            return convertView;
        }
        class HoldView{
            TextView ziyuancontent,content;
            LinearLayout putongxuqiu;
            TextView date,type,des,jindu;
            LinearLayout daiziyuan;

            TextView ziyuandate,ziyuantype,rencai_title,rencai_lingyu,wutu,rank,xmtitle,lanyuan,unitname,unitadress,ketizu_title,ketizu_lingyu,ketizurank,ziyuanjindu,ziyuandes;
            RelativeLayout ziyuanleixing,zhuanjia,xiangmulay,keyanjigou,ketizu,ziyuandeslay,deslay;
            RoundImageView rencai_img;
            ImageView img,ketizu_img;
            ShapeImageView xianmgmu;
        }
    }
}
