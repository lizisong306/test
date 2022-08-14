package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;

import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2016/12/17.
 */

public class ServiceProvisionAcitivty extends AutoLayoutActivity {

    ImageView mImageBack;
    TextView  mTxtInfo,mTitle;
    int _res_id;
    ImageView service_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_provision);

        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        mImageBack = (ImageView)findViewById(R.id.back);
        mTxtInfo = (TextView)findViewById(R.id.info);
        service_back=(ImageView) findViewById(R.id.service_back);
        Intent intent = getIntent();
        _res_id = intent.getIntExtra("res_id",0);

        service_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTxtInfo.setText(Html.fromHtml(getResources().getText(_res_id).toString()));

    }
}
