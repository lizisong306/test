package com.maidiantech;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.google.gson.Gson;
import com.lidroid.xutils.view.annotation.event.OnScroll;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;

import Util.KeySort;
import Util.OkHttpUtils;
import Util.SharedPreferencesUtil;
import Util.UIHelper;
import application.ImageLoaderUtils;
import application.MyApplication;
import entity.QingBaoDeilEntity;
import entity.QingBaoGetEntity;
import entity.UpdateTelig;
import view.RefreshListView;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/10/26.
 */

public class QingBaoDeilActivity extends AutoLayoutActivity {
    ProgressBar progress;
    TextView titlecontent;
    TextView shidu,dingyue,biaoti,description,jianjie,shiyirenqun,dingyuexieyi,no1,no2,no3,line1,line2,line3;
    ImageView back,icon,share;
    String teligId,telig_price,telig_unit,telig_type;
    String json;
    String descrip;
    QingBaoDeilEntity data;
    QingBaoGetEntity getQingBao;
    ScrollView scoll;
    private DisplayImageOptions options;
    RelativeLayout lingqu;
    ImageView dingyuesucfull;
    public static  boolean isShowSucessfull = false;
    UMShareAPI mShareAPI;
    private ShareAction action;
    private UMImage image;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qingbaodeilactivity);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        mShareAPI = UMShareAPI.get(this);
        isShowSucessfull = false;
        options = ImageLoaderUtils.initOptions();
        titlecontent = (TextView)findViewById(R.id.titlecontent);
        shidu = (TextView)findViewById(R.id.shidu);
        shidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(data.data.lastUpdateTelig != null && data.data.lastUpdateTelig.size() == 1){
                        UpdateTelig item = data.data.lastUpdateTelig.get(0);
                        Intent intent = new Intent(QingBaoDeilActivity.this,LookQingBaoActivity.class);
                        intent.putExtra("teligjournalid",item.telig_journal_id);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(QingBaoDeilActivity.this, FreeTryRead.class);
                        intent.putExtra("teligId", teligId);
                        intent.putExtra("isRess", data.data.isRess);
                        intent.putExtra("descrip",descrip);
                        intent.putExtra("unreadcount", data.data.unreadcount);
                        startActivity(intent);
                    }
                }catch (Exception e){

                }

            }
        });
        dingyue = (TextView)findViewById(R.id.dingyue);
        progress = (ProgressBar)findViewById(R.id.progress);
        back = (ImageView)findViewById(R.id.yujian_backs);
        icon = (ImageView)findViewById(R.id.icon);
        biaoti = (TextView)findViewById(R.id.biaoti);
        jianjie =(TextView)findViewById(R.id.jianjie);
        lingqu = (RelativeLayout)findViewById(R.id.lingqu);
        dingyuesucfull = (ImageView)findViewById(R.id.dingyuesucfull);
        shiyirenqun = (TextView)findViewById(R.id.shiyirenqun);
        dingyuexieyi = (TextView)findViewById(R.id.dingyuexieyi);
        description = (TextView)findViewById(R.id.description);
        no1 = (TextView) findViewById(R.id.no1);
        no2 = (TextView) findViewById(R.id.no2);
        no3 = (TextView) findViewById(R.id.no3);
        line1 = (TextView)findViewById(R.id.line1);
        line2 = (TextView)findViewById(R.id.line2);
        line3 = (TextView)findViewById(R.id.line3);
        scoll = (ScrollView)findViewById(R.id.scoll);
        share = (ImageView)findViewById(R.id.share);
        teligId = getIntent().getStringExtra("teligId");
        telig_price = getIntent().getStringExtra("telig_price");
        telig_unit = getIntent().getStringExtra("telig_unit");
        telig_type = getIntent().getStringExtra("telig_type");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progress.setVisibility(View.VISIBLE);
        lingqu.setVisibility(View.GONE);
        lingqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lingqu.setVisibility(View.GONE);
            }
        });
        dingyuesucfull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lingqu.setVisibility(View.GONE);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareonclick();
            }
        });

    }
    private void getJson(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                String mid= SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                MyApplication.setAccessid();
                String timestamp = System.currentTimeMillis()+"";
                String sign="";
                ArrayList<String> sort = new ArrayList<String>();
                sort.add("version"+MyApplication.version);
                sort.add("accessid"+MyApplication.deviceid);
                sort.add("timestamp"+timestamp);
                sort.add("mid"+mid);
                sort.add("teligId"+teligId);
                sign = KeySort.keyScort(sort);
                String url = "http://"+ MyApplication.ip+"/api/tellig/telig_list_detail.php?"+"mid="+mid+"&teligId="+teligId+"&version="+MyApplication.version+MyApplication.accessid+"&timestamp="+timestamp+"&sign"+sign;
                json = OkHttpUtils.loaudstringfromurl(url);
                if(json != null){
                    Message msg = Message.obtain();
                    msg.what=1;
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
                Gson gson = new Gson();
                data=gson.fromJson(json, QingBaoDeilEntity.class);
                if(data != null){
                    if(data.code.equals("1")){
                        progress.setVisibility(View.GONE);
                        ImageLoader.getInstance().displayImage(data.data.telig_cover
                                , icon, options);
                        biaoti.setText(data.data.telig_title);
                        description.setText(data.data.telig_slogan);
                        jianjie.setText(data.data.telig_slogan);
                        descrip = data.data.telig_description;
                        shiyirenqun.setText(data.data.telig_match_persion);
                        dingyuexieyi.setText(data.data.telig_takenotice);
                        if(data.data.lastUpdateTelig != null){
                            if(data.data.lastUpdateTelig.size()==1){
                                UpdateTelig item = data.data.lastUpdateTelig.get(0);
                                no1.setText(item.telig_journal_title);
                                no1.setVisibility(View.VISIBLE);
                                line1.setVisibility(View.VISIBLE);
                                no2.setVisibility(View.GONE);
                                line2.setVisibility(View.GONE);
                                no3.setVisibility(View.GONE);
                                line3.setVisibility(View.GONE);
                            }else if(data.data.lastUpdateTelig.size()==2){
                                UpdateTelig item = data.data.lastUpdateTelig.get(0);
                                no1.setText(item.telig_journal_title);
                                no1.setVisibility(View.VISIBLE);
                                line1.setVisibility(View.VISIBLE);
                                no2.setVisibility(View.VISIBLE);
                                line2.setVisibility(View.VISIBLE);
                                item = data.data.lastUpdateTelig.get(1);
                                no2.setText(item.telig_journal_title);
                                no3.setVisibility(View.GONE);
                                line3.setVisibility(View.GONE);
                            }else if(data.data.lastUpdateTelig.size()==3){
                                UpdateTelig item = data.data.lastUpdateTelig.get(0);
                                no1.setText(item.telig_journal_title);
                                no1.setVisibility(View.VISIBLE);
                                line1.setVisibility(View.VISIBLE);
                                no2.setVisibility(View.VISIBLE);
                                line2.setVisibility(View.VISIBLE);
                                item = data.data.lastUpdateTelig.get(1);
                                no2.setText(item.telig_journal_title);
                                no3.setVisibility(View.VISIBLE);
                                line3.setVisibility(View.VISIBLE);
                                item = data.data.lastUpdateTelig.get(2);
                                no3.setText(item.telig_journal_title);
                            }
                            no1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UpdateTelig item = data.data.lastUpdateTelig.get(0);
                                    if(item.telig_journal_free.equals("0")){
                                        Toast.makeText(QingBaoDeilActivity.this, "请先订阅后在阅读",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Intent intent = new Intent(QingBaoDeilActivity.this,LookQingBaoActivity.class);
                                        intent.putExtra("teligjournalid",item.telig_journal_id);
                                        startActivity(intent);
                                    }
                                }
                            });
                            no2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UpdateTelig item = data.data.lastUpdateTelig.get(1);
                                    if(item.telig_journal_free.equals("0")){
                                        Toast.makeText(QingBaoDeilActivity.this, "请先订阅后在阅读",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Intent intent = new Intent(QingBaoDeilActivity.this,LookQingBaoActivity.class);
                                        intent.putExtra("teligjournalid",item.telig_journal_id);
                                        startActivity(intent);
                                    }
                                }
                            });
                            no3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UpdateTelig item = data.data.lastUpdateTelig.get(2);
                                    if(item.telig_journal_free.equals("0")){
                                        Toast.makeText(QingBaoDeilActivity.this, "请先订阅后在阅读",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Intent intent = new Intent(QingBaoDeilActivity.this,LookQingBaoActivity.class);
                                        intent.putExtra("teligjournalid",item.telig_journal_id);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
//                        if(data.data.telig_journal_free.equals("0")){
//
//                        }
                        dingyue.setText("订阅："+telig_price+"元/"+telig_unit);
                     if(data.data.isRess.equals("1")){
                            shidu.setVisibility(View.GONE);
                            dingyue.setBackgroundColor(0xff3385ff);
                            dingyue.setText("立即查看");
                            if(data.data.unreadcount > 0){
                                shidu.setVisibility(View.GONE);
                                dingyue.setText("已订阅："+data.data.unreadcount+"篇未读");
                            }
                        }

                        if(data.data.isRess.equals("0") && telig_type != null && telig_type.equals("2")){
                            shidu.setVisibility(View.GONE);
                            dingyue.setText("免费领取");
                            dingyue.setBackgroundColor(0xffff6c58);
                        }

                        dingyue.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(data.data.isRess.equals("0") && telig_type != null && telig_type.equals("2")){
                                    String login = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                                    if(login.equals("0")){
                                        Intent intent = new Intent(QingBaoDeilActivity.this, MyloginActivity.class);
                                        startActivity(intent);
                                    }else{
                                        progress.setVisibility(View.VISIBLE);
                                        FreeLingQu();
                                    }
                                }else{
                                    if(data.data.isRess.equals("1")){
                                        if(data.data.lastUpdateTelig != null && data.data.lastUpdateTelig.size() == 1){
                                            UpdateTelig item = data.data.lastUpdateTelig.get(0);
                                            Intent intent = new Intent(QingBaoDeilActivity.this,LookQingBaoActivity.class);
                                            intent.putExtra("teligjournalid",item.telig_journal_id);
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(QingBaoDeilActivity.this, FreeTryRead.class);
                                            intent.putExtra("teligId", teligId);
                                            intent.putExtra("isRess", data.data.isRess);
                                            intent.putExtra("descrip",descrip);
                                            intent.putExtra("unreadcount", data.data.unreadcount);
                                            startActivity(intent);
                                        }

                                    }else{
                                        //跳转到支付
                                        String login = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_STATE, "0");
                                        if(login.equals("1")){
                                            Intent intent = new Intent(QingBaoDeilActivity.this, PaySDkCallActivity.class);
                                            intent.putExtra("aid", teligId);
                                            intent.putExtra("type",1);
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(QingBaoDeilActivity.this, MyloginActivity.class);
                                            startActivity(intent);
                                        }

                                    }
                                }


                            }
                        });
                    }
                }
                Message msgS = Message.obtain();
                msgS.what = 2;
                sendMessageDelayed(msgS,100);

            }
            if(msg.what == 2){
                int y = scoll.getScrollY();
                if(y>740){
                    titlecontent.setText(data.data.telig_title);
                }else{
                    titlecontent.setText(data.data.telig_title);
                }
                Message msgS = Message.obtain();
                msgS.what = 2;
                sendMessageDelayed(msgS,200);
            }
            if(msg.what == 3){
                progress.setVisibility(View.GONE);
                Gson gson = new Gson();
                getQingBao  =gson.fromJson(json, QingBaoGetEntity.class);
                if(getQingBao != null && getQingBao.code.equals("1")){
                    lingqu.setVisibility(View.VISIBLE);
                    if(Integer.parseInt(getQingBao.data.unreadcount) > 0){
                        dingyue.setBackgroundColor(0xff3385ff);
                        shidu.setVisibility(View.GONE);
                        dingyue.setText("已订阅："+data.data.unreadcount+"篇未读");
                    }else{
                        dingyue.setText("立即查看");
                    }
//                    progress.setVisibility(View.VISIBLE);
                    getJson();
                }else{
                    Toast.makeText(QingBaoDeilActivity.this, getQingBao.message,Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(isShowSucessfull){
            isShowSucessfull = false;
            lingqu.setVisibility(View.VISIBLE);
        }
        getJson();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(2);
    }
    private void FreeLingQu(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mid = SharedPreferencesUtil.getString(SharedPreferencesUtil.LOGIN_ID, "");
                String url = "http://"+MyApplication.ip+"/api/tellig/telig_ress.php?"+"&mid="+mid+"&teligId="+teligId;
                json = OkHttpUtils.loaudstringfromurl(url);
                if(json != null){
                    Message msg = Message.obtain();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void shareonclick() {
        try {

            Intent intent = new Intent(QingBaoDeilActivity.this, ShareActivity.class);
            intent.putExtra("title", data.data.telig_title);
            intent.putExtra("txt", data.data.telig_slogan);
            intent.putExtra("Tarurl", "http://www.zhongkechuangxiang.com/plus/activity/hd/Introduction_page.html?teligId="+teligId);
            intent.putExtra("imageurl", "http://www.zhongkechuangxiang.com/plus/activity/images/Introduction_page.jpg");
            startActivity(intent);

//            UIHelper.showDialogForLoading(QingBaoDeilActivity.this, "", true);
//
//            image = new UMImage(QingBaoDeilActivity.this, "http://www.zhongkechuangxiang.com/plus/activity/images/Introduction_page.jpg");
//
//
//            final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
//                    {
//                            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//                            SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
//                    };
//            action = new ShareAction(QingBaoDeilActivity.this);
//            UIHelper.hideDialogForLoading();
//
//            action.setDisplayList(displaylist)
//                    .withText(data.data.telig_slogan)
//                    .withTitle(data.data.telig_title)
//                    .withTargetUrl("http://www.zhongkechuangxiang.com/plus/activity/hd/Introduction_page.html?teligId="+teligId)
//                    .withMedia(image)
//                    .setCallback(umShareListener)
//                    .open();

        } catch (Exception e) {
        }
    }
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
            } else {
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(QingBaoDeilActivity.this, platform + "分享失败", Toast.LENGTH_SHORT).show();
            if (t != null) {
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            //Toast.makeText(DetailsActivity.this,platform +"分享取消", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mShareAPI.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
