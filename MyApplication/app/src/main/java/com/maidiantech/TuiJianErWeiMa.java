package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chenantao.autolayout.AutoLayoutActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import application.MyApplication;
import entity.data;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/10/20.
 */

public class TuiJianErWeiMa extends AutoLayoutActivity {
    ImageView back;
    private ShareAction action;
    UMShareAPI mShareAPI;
    private UMImage image;
    private TextView share;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuijianerweima);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        mShareAPI = UMShareAPI.get(this);
        back = (ImageView)findViewById(R.id.back);
        share = (TextView)findViewById(R.id.share);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareonclick();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mShareAPI.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

//    private UMShareListener umShareListener = new UMShareListener() {
//        @Override
//        public void onResult(SHARE_MEDIA platform) {
//            if (platform.name().equals("WEIXIN_FAVORITE")) {
//            } else {
//            }
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(TuiJianErWeiMa.this, platform + "分享失败", Toast.LENGTH_SHORT).show();
//            if (t != null) {
//            }
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA platform) {
//            //Toast.makeText(DetailsActivity.this,platform +"分享取消", Toast.LENGTH_SHORT).show();
//        }
//    };

    private void shareonclick() {
        try {
//            UIHelper.showDialogForLoading(QingBaoDeilActivity.this, "", true);
            Intent intent = new Intent(TuiJianErWeiMa.this, ShareActivity.class);
            intent.putExtra("title", "钛领科技，让科技变简单");
            intent.putExtra("txt", "助力企业解决技术难题，促进院企科技成果转移");
            intent.putExtra("Tarurl", "http://www.zhongkechuangxiang.com/plus/maidianm/#page1");
            intent.putExtra("imageurl", "http://"+ MyApplication.ip+"/uploads/logo/logo.png");
            startActivity(intent);

//            image = new UMImage(TuiJianErWeiMa.this, "http://"+ MyApplication.ip+"/uploads/logo/logo.png");
//
//
//            final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
//                    {
//                            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
//                            SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
//                    };
//            action = new ShareAction(TuiJianErWeiMa.this);
////            Util.UIHelper.hideDialogForLoading();
//
//            action.setDisplayList(displaylist)
//                    .withText("助力企业解决技术难题，促进院企科技成果转移")
//                    .withTitle("钛领科技，让科技变简单")
//                    .withTargetUrl("http://www.zhongkechuangxiang.com/plus/maidianm/#page1")
//                    .withMedia(image)
//                    .setCallback(umShareListener)
//                    .open();

        } catch (Exception e) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("推荐二维码");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("推荐二维码");
    }
}
