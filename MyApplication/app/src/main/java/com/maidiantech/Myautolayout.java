package com.maidiantech;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.chenantao.autolayout.AutoCardView;
import com.chenantao.autolayout.AutoFrameLayout;
import com.chenantao.autolayout.AutoLinearLayout;
import com.chenantao.autolayout.AutoRelativeLayout;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by 13520 on 2016/8/8.
 */
public class Myautolayout extends FragmentActivity implements BackHandledInterface {
    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    private BackHandledFragment mBackHandedFragment;
    private int mBackKeyPressedTimes = 0;
    private static final String CARD_VIEW = "android.support.v7.widget.CardView";
    private boolean hadIntercept;
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs)
    {
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT))
        {
            view = new AutoFrameLayout(context, attrs);
        }
        if (name.equals(LAYOUT_LINEARLAYOUT))
        {
            view = new AutoLinearLayout(context, attrs);
        }
        if (name.equals(LAYOUT_RELATIVELAYOUT))
        {
            view = new AutoRelativeLayout(context, attrs);
        }
        if (name.equals(CARD_VIEW))
        {
            view = new AutoCardView(context, attrs);
        }
        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
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
                          if(!isFinishing()){
                            super.onBackPressed();
                          }
                       }else{
                          getSupportFragmentManager().popBackStack();
                      }
                }
         }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 设置屏幕顶部导航栏背景是否透明
     *
     * @param 
     */
    public void setStatusBarTransparent(boolean transparent) {
        if (transparent) {
            if (Build.VERSION.SDK_INT >= 19) {
                getWindow().addFlags(0x04000000);

                // 小米，魅族等手机的通知栏是全透明，白色背景通知栏显示不明显，特殊处理，加一个阴影背景
                Window window = getWindow();
                if (window != null) {
                    ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(AutoCardView.LayoutParams.FILL_PARENT,  (int) getResources().getDimension(R.dimen.title_padding_top));
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
                getWindow().clearFlags(0x04000000);

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

}
