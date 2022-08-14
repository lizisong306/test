package com.maidiantech;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.chenantao.autolayout.AutoLayoutActivity;

import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/12/29.
 */

public class Myattention extends AutoLayoutActivity {
    ImageView idback;
    ListView myattentionlv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_attention);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        initView();
        idback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        idback = (ImageView) findViewById(R.id._back);
        myattentionlv = (ListView) findViewById(R.id.my_attention_lv);
    }
}
