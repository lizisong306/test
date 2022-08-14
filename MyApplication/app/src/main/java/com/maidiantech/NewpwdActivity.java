package com.maidiantech;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.chenantao.autolayout.AutoLayoutActivity;

import view.StyleUtils;
import view.StyleUtils1;

/**
 * Created by 13520 on 2016/9/12.
 */
public class NewpwdActivity extends AutoLayoutActivity {
    ImageView newback;
    EditText newpwd;
    EditText rightpwd;
    Button newqueding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpwd);
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
        initView();
    }
    private void initView() {
        newback = (ImageView) findViewById(R.id.new_back);
        newpwd = (EditText) findViewById(R.id.new_pwd);
        rightpwd = (EditText) findViewById(R.id.right_pwd);
        newqueding = (Button) findViewById(R.id.new_queding);
        newback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
