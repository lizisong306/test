package com.maidiantech;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import Util.NoDoubleClick;
import com.chenantao.autolayout.AutoLayoutActivity;

/**
 * Created by Administrator on 2019/8/21.
 */

public class FaBuActivity extends AutoLayoutActivity {
    TextView done,bottom,back,lianx;
    String state;
    public static int states=0;
    String id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.fabuactivity);
        done = (TextView)findViewById(R.id.done);
        bottom = (TextView)findViewById(R.id.bottom);
        lianx = (TextView)findViewById(R.id.lianx);
        back = (TextView)findViewById(R.id.back);
        id = getIntent().getStringExtra("id");
        state = getIntent().getStringExtra("state");
        states =0;
        if(state.equals("0")){
            done.setText("完善需求");
        }else if(state.equals("1")){
            done.setText("完善企业信息");
            lianx.setText("需求补充成功");
            bottom.setText("完善企业信息，我们可以更精准的为您推荐资源");
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaBuActivity.this, MainActivity.class);
                intent.putExtra("state",3);
                startActivity(intent);
            }
        });
        done.setOnClickListener(new NoDoubleClick() {
            @Override
            public void Click(View v) {
                 if(states == 0){
                     Intent intent = new Intent(FaBuActivity.this, XuQiuBuChong.class);
                     intent.putExtra("id", id);
                     startActivity(intent);
                 }else {
                     Intent intent = new Intent(FaBuActivity.this, QiYeBuChong.class);
                     intent.putExtra("id", id);
                     startActivity(intent);
                 }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        state = getIntent().getStringExtra("state");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(states==0){
            done.setText("完善需求");
        }else if(states==1){
            done.setText("完善企业信息");
            lianx.setText("需求补充成功");
            bottom.setText("完善企业信息，我们可以更精准的为您推荐资源");
        }
    }
}
