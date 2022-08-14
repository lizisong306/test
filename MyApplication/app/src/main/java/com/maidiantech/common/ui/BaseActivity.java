package com.maidiantech.common.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.chenantao.autolayout.AutoLayoutActivity;
import com.maidiantech.R;
import view.StyleUtils1;

/**
 * Created by lizisong on 2018/3/1.
 * 公用的Activity 将某些公共的部分进行封装
 */

public class BaseActivity extends AutoLayoutActivity {
    TextView title;
    ImageView left;
    ImageView right;
    ImageView close;
    public boolean isShowTilte = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    /**
     * 初始化界面头
     */
    private void initNavigationLayout() {
        title = (TextView) findViewById(R.id.titlecontent);
        left = (ImageView)findViewById(R.id.back);
        right = (ImageView)findViewById(R.id.right);
        close = (ImageView)findViewById(R.id.close);
        if (left != null) {
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if(isShowTilte){
           initNavigationLayout();
        }

    }
    /**
     * 设置标题头部
     */
    public void setTitle(String titlestr){
        if(title != null){
            if(titlestr != null) {
                title.setText(titlestr);
            }
        }
    }

    /**
     * 是否隐藏Title
     * @param state
     */
    public void setHideTitle(boolean state){
        isShowTilte = state;
    }

    /**
     * 隐藏右边图标
     */
    public void setRightIconHide(){
        if(right != null){
            right.setVisibility(View.GONE);
        }
    }
    /**
     * 关闭
     */
    public void setClose(){
        if(close != null){
            close.setVisibility(View.VISIBLE);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * 设置右边按钮的状态及按键事件
     * @param clickListener
     */

    public void setRightClickRes(View.OnClickListener clickListener,int resid){
        if(right != null && clickListener != null){
            right.setImageResource(resid);
            right.setOnClickListener(clickListener);
        }
    }

    /**
     * 设置左边按键的状态及按键事件
     * @param clickListener
     * @param resid
     */
    public void setLeftClickRes(View.OnClickListener clickListener, int resid){
        if(left != null && clickListener != null){
            left.setImageResource(resid);
            left.setOnClickListener(clickListener);
        }
    }
    /**
     * 设置状态栏
     */
    public void setSystemBar(){
        //设置状态栏半透明的状态
        StyleUtils1.initSystemBar(this);
        //设置状态栏是否沉浸
        StyleUtils1.setStyle(this);
    }
}
