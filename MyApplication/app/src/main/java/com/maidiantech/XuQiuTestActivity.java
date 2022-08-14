package com.maidiantech;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chenantao.autolayout.AutoLayoutActivity;

import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by lizisong on 2017/4/19.
 */

public class XuQiuTestActivity extends AutoLayoutActivity {
    ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xuqiutest);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        back = (ImageView)findViewById(R.id.about_backs);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuQiuTestActivity.this.finish();
            }
        });
    }
}
