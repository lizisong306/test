package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Util.NetUtils;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.PingLun;
import entity.PingLunEntry;
import entity.Ret;
import view.RefreshListView;
import view.RoundImageView;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/9/14.
 */

public class PingLunActivity extends AutoLayoutActivity {
    ImageView back;
    RefreshListView listview;
    ProgressBar progress;
    //头View
    View heart;
    TextView price, str,adress,xm_title,xm_rank,xm_linyu,sent;
    ImageView xm_img;
    RoundImageView rc_img;
    String priceStr,strStr,adressStr,xm_titleStr,xm_rankStr, xm_linyuStr,typeid, img;
    EditText pinglun;
    private DisplayImageOptions options;
    String mid;
    public final int pageSize = 20;
    String time = "";
    String rid;
    String ret;
    String x,y;
    PingLunEntry pinglundata;
    LinearLayout content;
    PingLunAdapter adapter;
    LinearLayout layout_jiage,layout_adress;
    int netWorkType;
    private List<PingLun> arrayList = new ArrayList<>();
    HashMap<String, String> hashmap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentactivity);
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        options = ImageLoaderUtils.initOptions();
        back = (ImageView)findViewById(R.id.shezhi_backs);
        heart =  View.inflate(PingLunActivity.this, R.layout.pinglunherat,null);
        layout_jiage = (LinearLayout)heart.findViewById(R.id.layout_jiage);
        layout_adress = (LinearLayout)heart.findViewById(R.id.layout_adress);
        price = (TextView)heart.findViewById(R.id.price);
        str   = (TextView)heart.findViewById(R.id.str);
        adress = (TextView)heart.findViewById(R.id.adress);
        xm_title = (TextView)heart.findViewById(R.id.xm_title);
        xm_rank =(TextView)heart.findViewById(R.id.xm_rank);
        xm_linyu = (TextView)heart.findViewById(R.id.xm_linyu);
        content = (LinearLayout) heart.findViewById(R.id.content);
        xm_img = (ImageView)heart.findViewById(R.id.xm_img);
        rc_img = (RoundImageView)heart.findViewById(R.id.rc_img);
        progress = (ProgressBar)findViewById(R.id.progress);
        xm_rankStr = getIntent().getStringExtra("rank");
        priceStr = getIntent().getStringExtra("price");
        strStr = getIntent().getStringExtra("str");
        adressStr = getIntent().getStringExtra("adress");
        xm_titleStr = getIntent().getStringExtra("xm_title");
        xm_linyuStr = getIntent().getStringExtra("linyu");
        typeid = getIntent().getStringExtra("typeid");
        img = getIntent().getStringExtra("img");
        rid = getIntent().getStringExtra("rid");
        y = getIntent().getStringExtra("y");
        x = getIntent().getStringExtra("x");

        if(priceStr == null || priceStr.equals("")){
            layout_jiage.setVisibility(View.GONE);
        }

        if(adressStr == null || adressStr.equals("")){
            layout_adress.setVisibility(View.GONE);
        }

        mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
        listview = (RefreshListView)findViewById(R.id.listview);
        sent = (TextView)findViewById(R.id.send);
        pinglun = (EditText)findViewById(R.id.pinglun);
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String txt = pinglun.getText().toString();
                if(txt != null && !txt.equals("")){
//                    progress.setVisibility(View.VISIBLE);
                    hashmap.put("mid", mid);
                    hashmap.put("rid", rid);
                    hashmap.put("comment", txt);
                    commitContent();
                }else {
                    Toast.makeText(PingLunActivity.this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        listview.setOnPullUpToRefreshListener(new RefreshListView.OnPullUpToRefreshListener() {
            @Override
            public void pullUpToRefresh() {
                netWorkType = NetUtils.getNetWorkType(MyApplication
                        .getContext());
                if (netWorkType == NetUtils.NETWORKTYPE_INVALID) {
                    Toast.makeText(PingLunActivity.this, "请检查网络",Toast.LENGTH_SHORT).show();
                }else{
                    if (arrayList.size() == 0) {

                    } else{
                        PingLun item = arrayList.get(arrayList.size()-1);
                        time = item.pubtime;
                        getData();
                    }
                }

            }});
        if(typeid != null){
            if(typeid.equals("4")){
                xm_img.setVisibility(View.GONE);
                rc_img.setVisibility(View.VISIBLE);
                if(img != null && !img.equals("")){
                    ImageLoader.getInstance().displayImage(img
                            , rc_img, options);
                }else{
                    xm_img.setVisibility(View.GONE);
                }
            }else if(typeid.equals("7") || typeid.equals("2")){

                xm_img.setVisibility(View.VISIBLE);
                rc_img.setVisibility(View.GONE);
                if(img != null && !img.equals("")){
                    ImageLoader.getInstance().displayImage(img
                            , xm_img, options);
                }else{
                    rc_img.setVisibility(View.GONE);
                }
            }
        }else{
            content.setVisibility(View.GONE);
        }

        price.setText(priceStr+"￥");
        str.setText(strStr);
        adress.setText(adressStr+"  ");
        adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(PingLunActivity.this, BaseMapDemo.class);
                intent.putExtra("y", y);
                intent.putExtra("x", x);
                startActivity(intent);
            }
        });
        xm_title.setText(xm_titleStr);
        xm_rank.setText(xm_rankStr);
        xm_linyu.setText(xm_linyuStr);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(adapter == null){
            adapter = new PingLunAdapter();
        }
        listview.setPullUpToRefreshable(true);
        listview.setPullDownToRefreshable(false);
        listview.addHeaderView(heart);
        listview.setAdapter(adapter);
        progress.setVisibility(View.VISIBLE);
        getData();

    }

    private void getData(){
         new Thread(new Runnable() {
             @Override
             public void run() {
                 String url = "http://"+ MyApplication.ip+"/api/getRequireComment.php?rid="+rid+"&mid="+mid+"&lastTime="+time+"&pageSize="+pageSize;
                 ret = OkHttpUtils.loaudstringfromurl(url);
                 if(ret != null){
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
            if(msg.what == 1){
                try {
                    progress.setVisibility(View.GONE);
                    listview.setPullUpToRefreshFinish();
                    Gson gson = new Gson();
                    pinglundata = gson.fromJson(ret, PingLunEntry.class);
                    if(pinglundata != null){
                        if(pinglundata.code.equals("1")){
                            List<PingLun> posList = pinglundata.data;
                            if(posList != null){
                                if(posList.size() > 0){
                                    for (int i=0; i < posList.size();i++){
                                        PingLun item = posList.get(i);
                                        arrayList.add(item);
                                    }
                                }
                                if(posList.size() < 20){
                                    listview.setPullUpToRefreshable(false);
                                }
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){

                }


            }

            if(msg.what == 2){
                try {
                    pinglun.setText("");
                    Gson gson = new Gson();
                    Ret ret1= gson.fromJson(ret, Ret.class);
                    if(ret1.code.equals("1")){
                        Toast.makeText(PingLunActivity.this, ret1.message, Toast.LENGTH_SHORT).show();
                        time="";
                        arrayList.clear();
                        getData();
                    }
                }catch (Exception e){

                }

            }
        }
    };

    class PingLunAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HolderView holder;
            if(convertView == null){
                holder = new HolderView();
                convertView = View.inflate(PingLunActivity.this, R.layout.pinglunadapter, null);
                holder.heart = (RoundImageView)convertView.findViewById(R.id.rc_img);
                holder.name  = (TextView)convertView.findViewById(R.id.name);
                holder.content = (TextView)convertView.findViewById(R.id.content);
                convertView.setTag(holder);
            }else{
                holder = (HolderView) convertView.getTag();
            }
            PingLun item =arrayList.get(position);

            try {
                holder.name.setText(item.username);
                holder.content.setText(item.comments);
                ImageLoader.getInstance().displayImage(item.headurl
                        , holder.heart, options);
            }catch (Exception e){

            }
            return convertView;
        }

        class HolderView{
            public RoundImageView heart;
            public TextView name;
            public TextView content;
        }
    }

    private void commitContent(){
        new Thread(new Runnable() {
            @Override
            public void run() {
              String url= "http://"+MyApplication.ip+"/api/require_addcomment.php";
              try {
                  ret = OkHttpUtils.post(url, hashmap);
                  if(ret != null){
                      Message msg = Message.obtain();
                      msg.what = 2;
                      handler.sendMessage(msg);
                  }
              }catch (Exception e){

              }

            }
        }).start();
    }
}
