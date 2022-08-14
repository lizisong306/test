package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chenantao.autolayout.AutoLayoutActivity;

import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/3/9.
 */

public class ShopActivity extends AutoLayoutActivity{
    LinearLayout rencai_fuwu,jishu_fuwu,shebei_fuwu,zhishi_fuwu,ziyuan_fuwu,zhuanli_fuwu;
    ImageView back,banner_top,banner_bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);

        rencai_fuwu = (LinearLayout)findViewById(R.id.rencai_fuwu);
        jishu_fuwu  = (LinearLayout)findViewById(R.id.jishu_fuwu);
        shebei_fuwu = (LinearLayout)findViewById(R.id.shebei_fuwu);
        zhishi_fuwu = (LinearLayout)findViewById(R.id.zhishi_fuwu);
        ziyuan_fuwu = (LinearLayout)findViewById(R.id.ziyuan_fuwu);
        zhuanli_fuwu = (LinearLayout)findViewById(R.id.zhuanli_fuwu);

        back = (ImageView)findViewById(R.id.back);
        banner_top = (ImageView)findViewById(R.id.banner_top);
        banner_bottom = (ImageView)findViewById(R.id.banner_bottom);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopActivity.this.finish();
            }
        });

        banner_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, WebViewActivity.class);
                intent.putExtra("title", "企业成长伙伴");
                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/banner.html");
                startActivity(intent);
            }
        });
        banner_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                http://www.maidiantech.com/plus/an-service-mall/coupon.html
                Intent intent = new Intent(ShopActivity.this, WebViewActivity.class);
                intent.putExtra("title", "创新卷");
                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/coupon.html");
                startActivity(intent);
            }
        });

        rencai_fuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                http://www.maidiantech.com/plus/service-mall/talent-service.html
                Intent intent = new Intent(ShopActivity.this, WebViewActivity.class);
                intent.putExtra("title", "人才服务");
                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/talent-service.html");
                startActivity(intent);

            }
        });

        jishu_fuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                http://www.maidiantech.com/plus/an-service-mall/jishu-service.html
                Intent intent = new Intent(ShopActivity.this, WebViewActivity.class);
                intent.putExtra("title", "技术服务");
                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/jishu-service.html");
                startActivity(intent);
            }
        });
        shebei_fuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, SearchActivity.class);
//                intent.putExtra("title", "设备服务");
//                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/shebei-service.html");
                startActivity(intent);
            }
        });

        zhishi_fuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, WebViewActivity.class);
                intent.putExtra("title", "知识服务");
                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/zhishi-service.html");
                startActivity(intent);
            }
        });
        ziyuan_fuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, WebViewActivity.class);
                intent.putExtra("title", "资源服务");
                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/ziyuan-service.html");
                startActivity(intent);
            }
        });
        zhuanli_fuwu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, WebViewActivity.class);
                intent.putExtra("title", "专利服务");
                intent.putExtra("url", "http://www.maidiantech.com/plus/an-service-mall/zhuanli-service.html");
                startActivity(intent);
            }
        });

    }
}
