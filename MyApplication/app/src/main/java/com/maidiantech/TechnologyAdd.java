package com.maidiantech;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenantao.autolayout.AutoLayoutActivity;

import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/9/19.
 */
public class TechnologyAdd extends AutoLayoutActivity {
    private String [] techarry={"研发小试阶段","已有样品或样机","研发中试阶段","示范工程阶段","可量产阶段","产业化阶段"};
    private  TextView technology_chengshu_tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.technology_add);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
         LinearLayout technology_chengshu=(LinearLayout) findViewById(R.id.technology_chengshu);
         technology_chengshu_tv=(TextView) findViewById(R.id.technology_chengshu_tv);
        technology_chengshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(TechnologyAdd.this)
                        .setItems(techarry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                technology_chengshu_tv.setText(techarry[which]);
                            }
                        })
                        .show();
            }
        });
    }
}
