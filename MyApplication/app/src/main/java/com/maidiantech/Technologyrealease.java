package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.chenantao.autolayout.AutoLayoutActivity;

import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/9/19.
 */
public class Technologyrealease extends AutoLayoutActivity {
    ImageView technologyback;
    ImageView technologyadd;
    ListView mytechnologylv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_technology);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        initView();
    }
    private void initView() {
        technologyback = (ImageView) findViewById(R.id.technology_back);
        technologyadd = (ImageView) findViewById(R.id.technology_add);
        mytechnologylv = (ListView) findViewById(R.id.my_technology_lv);
        technologyadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Technologyrealease.this,TechnologyAdd.class);
                startActivity(intent);
            }
        });
        technologyback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
