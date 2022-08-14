package com.maidiantech;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import view.SystemBarTintManager;

/**
 * Created by lizisong on 2017/5/17.
 */

public class BaseActivity extends FragmentActivity  implements BackHandledInterface{

    private BackHandledFragment mBackHandedFragment;
    private int mBackKeyPressedTimes = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.white);//通知栏所需颜色
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//        }



//        setStatusBarTransparent(true);
    }

    /**
     * 设置屏幕顶部导航栏背景是否透明
     *
     * @param
     */
    @SuppressWarnings("ResourceType")
    public void setStatusBarTransparent(boolean transparent) {
        if (transparent) {
            if (Build.VERSION.SDK_INT >= 19) {
                getWindow().addFlags(0x04ffffff);

                // 小米，魅族等手机的通知栏是全透明，白色背景通知栏显示不明显，特殊处理，加一个阴影背景
                Window window = getWindow();
                if (window != null) {
                    ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, (int) getResources().getDimension(R.dimen.title_padding_top));
                    params.gravity = Gravity.TOP;
                    View statusBarView = new View(this);
                    statusBarView.setLayoutParams(params);
                    statusBarView.setBackgroundResource(R.drawable.status_bar_bg);
                    statusBarView.setId(1);
                    decorViewGroup.addView(statusBarView);
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= 19) {
                getWindow().clearFlags(0x04ffffff);

                Window window = getWindow();
                if (window != null) {
                    ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
                    View statusBarView = decorViewGroup.findViewById(1);
                    if (statusBarView != null) {
                        decorViewGroup.removeView(statusBarView);
                    }
                }
            }
        }
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }
    @Override
    public void onBackPressed() {
    if(mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()){
        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
            if (mBackKeyPressedTimes == 0) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mBackKeyPressedTimes = 1;
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            mBackKeyPressedTimes = 0;
                        }
                    }
                }.start();
                return;
            } else {
                this.finish();
            }
            super.onBackPressed();
        }else{
            getSupportFragmentManager().popBackStack();
        }
    }
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


}
