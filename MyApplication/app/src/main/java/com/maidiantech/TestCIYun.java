package com.maidiantech;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import com.chenantao.autolayout.AutoLayoutActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Random;
import view.WordCloudView;
import view.WordGroupView;

/**
 * Created by Administrator on 2019/11/8.
 */

public class TestCIYun extends AutoLayoutActivity {
     WordCloudView wordCloudView ;
    WordGroupView wordGroupView;
    Random random = new Random();
    int weight = 30;
    int off = 2;
    int count=0;
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
        setContentView(R.layout.testciyun);
        wordGroupView = (WordGroupView)findViewById(R.id.wcv);
        ArrayList<String> words = new ArrayList<>();
        words.add("快点康复阶段来看看");
        words.add("噢噢噢噢");
        words.add("啦啦啦啦");
        words.add("口袋空空的课");
        words.add("扩大开放咔咔咔咔咔咔");
        words.add("啦啦啦啦啦d1");
        words.add("啦啦啦啦啦d2");
        words.add("啦啦啦啦啦d3");
        words.add("啦啦啦啦啦d4");
        words.add("啦啦啦啦啦d5");
        words.add("啦啦啦啦啦d6");
        words.add("啦啦啦啦啦d7");
        wordGroupView.setWords(words);
        EventBus.getDefault().register(this);
        EventBus.getDefault().unregister(this);
//        Handler handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                String s = String.valueOf(System.currentTimeMillis());
//                s = s.substring(random.nextInt(11));
//                wordCloudView.addTextView(s, weight);
//                if(--off == 0) {
//                    off = 3;
//                    if(weight > 3) weight=weight-3;
////                    weight--;
////                    if(weight == 5) return;
//                }
//                count++;
//                if(count<20){
//                    sendEmptyMessageDelayed(0, 100);
//                }
//            }
//        };
//        handler.sendEmptyMessage(0);
    }
}
