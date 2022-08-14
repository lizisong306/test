package com.maidiantech;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.chenantao.autolayout.AutoLayoutActivity;

import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/12/29.
 */

public class FirmActivity extends AutoLayoutActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firms);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        Button yz_next=(Button) findViewById(R.id.yz_next);
        yz_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FirmActivity.this,Perfectinformation.class);
                startActivity(intent);
            }
        });
    }
}
