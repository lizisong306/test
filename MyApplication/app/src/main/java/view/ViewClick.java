package view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;



/**
 * Created by Administrator on 2018/8/22.
 */

public class ViewClick {
    private static final int MIN_DELAY_TIME= 500;  // 两次点击间隔不能少于1000ms

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
//        Log.d("lizisong", "flag:"+flag);
        return flag;
    }


    /** 判断是否是快速点击 */
    private static long lastClickTime;

    public static void setLastClickTime(){
       lastClickTime = System.currentTimeMillis();
    }

    public static boolean isLowClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( timeD < 400) {
            return false;
        }
        lastClickTime = time;
        return true;
    }




    /**
     * 检查设备是否有导航栏
     * @param activity
     * @return
     */
    @SuppressLint("NewApi")
    public static boolean checkDeviceHasNavigationBar(Context activity) {
        boolean hasMenuKey = false;
        boolean hasBackKey = false;
        try {
            hasMenuKey = ViewConfiguration.get(activity).hasPermanentMenuKey();
        } catch (java.lang.NoSuchMethodError e) {

        }
        try {
            hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        } catch (java.lang.NoSuchMethodError e) {

        }

        if (!hasMenuKey && !hasBackKey) {
            return true;
        }
        return false;
    }


    /**
     * 获取导航栏高度 ,此方法不会检查导航栏是否存在，直接返回数值。所以可能手机没有显示导航栏，但是高度依然返回
     * @param activity
     * @return
     */
    public static int getNavigationBarHeight(Context activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 获取导航栏高度，此方法会根据手机是否存在导航栏，返回相应的数值
     * @param activity
     * @return
     */
    public static int getNavigationBarHeightEx(Context activity){
        if(checkDeviceHasNavigationBar(activity)){
            return getNavigationBarHeight(activity);
        }
        return 0;
    }

}
