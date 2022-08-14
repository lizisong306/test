package view;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;



/**
 * Created by Administrator on 2018/8/22.
 */

public class MyClickListener implements View.OnTouchListener {
    private static int timeout=1000;//双击间四百毫秒延时
    private int clickCount = 0;//记录连续点击次数
    private static Handler handler;
    private MyClickCallBack myClickCallBack;
    private static boolean isClick = false;
    public interface MyClickCallBack{
        void oneClick();//点击一次的回调
        void doubleClick();//连续点击两次的回调

    }


    public MyClickListener(MyClickCallBack myClickCallBack) {
        this.myClickCallBack = myClickCallBack;
        if(handler == null){
             handler = new Handler();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
    if(event.getAction() == MotionEvent.ACTION_DOWN){
          return isClick;
    }else if (event.getAction() == MotionEvent.ACTION_UP ) {
//            ViewClick.setLastClickTime();
              if(!isClick){
                  isClick = true;
                  Log.d("lizisong", "isClick handler");
                  myClickCallBack.oneClick();
              }
             Log.d("lizisong", "isClick:"+isClick);
//            clickCount++;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isClick = false;
                    Log.d("lizisong", "postDelayed isClick:"+isClick);
//                    if (clickCount == 1) {
//                        Log.d("lizisong", "clickCount:"+clickCount);
//                        myClickCallBack.oneClick();
//                    }else if(clickCount==2){
//                        myClickCallBack.doubleClick();
//                    }
//                    handler.removeCallbacksAndMessages(null);
//                    //清空handler延时，并防内存泄漏
//                    clickCount = 0;//计数清零
                }
            },timeout);//延时timeout后执行run方法中的代码
        }
        return false;//让点击事件继续传播，方便再给View添加其他事件监听
    }
}
