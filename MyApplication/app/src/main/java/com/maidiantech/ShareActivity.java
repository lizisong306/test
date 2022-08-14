package com.maidiantech;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.umeng.socialize.media.UMWeb;

import application.MyApplication;

/**
 * Created by lizisong on 2018/4/11.
 */

public class ShareActivity extends AutoLayoutActivity {
    public RelativeLayout weixin;
    public RelativeLayout pengyouquan;
    public RelativeLayout qq;
    public RelativeLayout qqkongjian;
    public TextView cencel;
    private UMShareAPI mShareAPI;
    private ShareAction action;
    private UMImage image;
    private String title, txt,Tarurl,imageurl;
    RelativeLayout bg;
    boolean isXiaoChengx =false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.shareactivity);
        Window window=getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.alpha=0.95f;
        window.setAttributes(wl);
        mShareAPI = UMShareAPI.get(this);

        weixin = (RelativeLayout)findViewById(R.id.weixin);

        pengyouquan = (RelativeLayout)findViewById(R.id.pengyouquan);

        qq = (RelativeLayout)findViewById(R.id.qq);
        qqkongjian = (RelativeLayout)findViewById(R.id.qqkongjian);
        cencel = (TextView)findViewById(R.id.cencel);
        title = getIntent().getStringExtra("title");
        txt =  getIntent().getStringExtra("txt");
        Tarurl = getIntent().getStringExtra("Tarurl");
        imageurl = getIntent().getStringExtra("imageurl");
        image = new UMImage(this, imageurl);
        cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareqqonclick(txt,title,Tarurl);
            }
        });
        qqkongjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareqzoneonclick(txt,title,Tarurl);
            }
        });

        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareweixinonclick(txt,title,Tarurl);
            }
        });
        pengyouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareweixincircleonclick(txt,title,Tarurl);
            }
        });



    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
            finish();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ShareActivity.this, platform + "分享失败", Toast.LENGTH_SHORT).show();
            if (t != null) {
            }
            finish();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            //Toast.makeText(DetailsActivity.this,platform +"分享取消", Toast.LENGTH_SHORT).show();
            finish();
        }
    };

    public void shareqqonclick(String txt,String title, String Tarurl) {
        try {

            UMWeb web = new UMWeb(Tarurl);
            web.setTitle(title);//标题
            web.setThumb(image);  //缩略图
            web.setDescription(txt);//描述
            action = new ShareAction(this);
            action.setPlatform(SHARE_MEDIA.QQ)//传入平台
                    .withText(txt)//分享内容
                    .withSubject(title)
                    .withMedia(web)
                    .setCallback(umShareListener)//回调监听器
                    .share();
//            action.setPlatform(SHARE_MEDIA.QQ)
//                    .withText(txt)
//                    .withTitle(title)
//                    .withTargetUrl(Tarurl)
//                    .withMedia(image)
//                    .setCallback(umShareListener)
//                    .share();
        } catch (Exception e) {
        }
    }

    public void shareqzoneonclick(String txt,String title, String Tarurl) {
        try {
            UMWeb web = new UMWeb(Tarurl);
            web.setTitle(title);//标题
            web.setThumb(image);  //缩略图
            web.setDescription(txt);//描述
            action = new ShareAction(this);
            action.setPlatform(SHARE_MEDIA.QZONE)//传入平台
                    .withText(txt)//分享内容
                    .withSubject(title)
                    .withMedia(web)
                    .setCallback(umShareListener)//回调监听器
                    .share();
//            action.setPlatform(SHARE_MEDIA.QZONE)
//                    .withText(txt)
//                    .withTitle(title)
//                    .withTargetUrl(Tarurl)
//                    .withMedia(image)
//                    .setCallback(umShareListener)
//                    .share();

        } catch (Exception e) {
        }
    }

    public void shareweixinonclick(String txt,String title, String Tarurl) {
        try {
            UMWeb web = new UMWeb(Tarurl);
            web.setTitle(title);//标题
            web.setThumb(image);  //缩略图
            web.setDescription(txt);//描述
            action = new ShareAction(this);
            action.setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
                    .withText(txt)//分享内容
                    .withSubject(title)
                    .withMedia(web)
                    .setCallback(umShareListener)//回调监听器
                    .share();
//            action.setPlatform(SHARE_MEDIA.WEIXIN)
//                    .withText(txt)
//                    .withTitle(title)
//                    .withTargetUrl(Tarurl)
//                    .withMedia(image)
//                    .setCallback(umShareListener)
//                    .share();

        } catch (Exception e) {
        }
    }

    public void shareweixincircleonclick(String txt,String title, String Tarurl) {
        try {
            UMWeb web = new UMWeb(Tarurl);
            web.setTitle(title);//标题
            web.setThumb(image);  //缩略图
            web.setDescription(txt);//描述

            action = new ShareAction(this);
            action.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                    .withText(txt)//分享内容
                    .withSubject(title)
                    .withMedia(web)
                    .setCallback(umShareListener)//回调监听器
                    .share();
//            action.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
//                    .withText(txt)
//                    .withTitle(title)
//                    .withTargetUrl(Tarurl)
//                    .withMedia(image)
//                    .setCallback(umShareListener)
//                    .share();

        } catch (Exception e) {
        }
    }

    public void shareweixinxiaochengxuonclick(String txt,String title, String Tarurl){
        try {
            UMMin umMin = new UMMin(Tarurl);
            umMin.setThumb(image);
            umMin.setTitle(title);
            umMin.setDescription(txt);
            umMin.setPath(Tarurl);
            umMin.setUserName("gh_3d4e463c9439");
            action = new ShareAction(this);
            action.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//传入平台
                    .withText(txt)//分享内容
                    .withSubject(title)
                    .withMedia(umMin)
                    .setCallback(umShareListener)//回调监听器
                    .share();
        }catch (Exception e){

        }
    }


}
