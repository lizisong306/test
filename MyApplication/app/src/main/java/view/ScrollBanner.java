package view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maidiantech.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.List;

import application.ImageLoaderUtils;
import application.MyApplication;

/**
 * Created by lizisong on 2017/9/19.
 */

public class ScrollBanner extends LinearLayout {
    private LinearLayout mBannerTV1;
    private LinearLayout mBannerTV2;
    private TextView tv1,tv2,tv3,tv4;
    private ImageView icon;
    private Handler handler;
    private boolean isShow=false;
    private int startY1, endY1, startY2, endY2;
    private Runnable runnable;
    private List<String> list;
    public  int  position = 0;
    public static boolean isStop = false;
    WeakReference<View> weakReference;
    private int offsetY = 140;
    private DisplayImageOptions options;
    public ScrollBanner(Context context) {
        this(context, null);
    }
    public ScrollBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ScrollBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_scroll_banner, this);

        weakReference = new WeakReference<View>(view);
        view = weakReference.get();
        mBannerTV1 = (LinearLayout) view.findViewById(R.id.tv_banner1);
        mBannerTV2 = (LinearLayout) view.findViewById(R.id.tv_banner2);
        tv1 = (TextView)view.findViewById(R.id.tv_1);
        tv2 = (TextView)view.findViewById(R.id.tv_2);
        tv3 = (TextView)view.findViewById(R.id.tv_3);
        tv4 = (TextView)view.findViewById(R.id.tv_4);
        options = ImageLoaderUtils.initOptions();
        icon = (ImageView) view.findViewById(R.id.icon);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(isStop){
                    return;
                }
                    isShow = !isShow;
                    if (position == list.size()-1) {
                        position = 0;
                    }
                try {
                    String text1="" ;
                    String text2="";
                    String[] arry;
                    if (isShow) {
                        text1 = list.get(++position);
                        arry=text1.split("\n");
                        tv1.setText(arry[0]);
                        tv2.setText(arry[1]);

//                            mBannerTV1.setText(text1);
                        text2 = list.get(position);
                        arry=text2.split("\n");
                        tv3.setText(arry[0]);
                        tv4.setText(arry[1]);
//                            mBannerTV2.setText(text2);
                    } else {
                        text1 = list.get(++position);
                        arry=text1.split("\n");
                        tv3.setText(arry[0]);
                        tv4.setText(arry[1]);

//                            mBannerTV2.setText(text1);
                        text2 = list.get(position);
                        arry=text2.split("\n");
                        tv1.setText(arry[0]);
                        tv2.setText(arry[1]);
//                            mBannerTV1.setText(text2);
                    }

                }catch (IndexOutOfBoundsException ex){
                    position = 0;
                }catch (Exception e){

                }
                if(list.size()>1){
                    startY1 = isShow ? 0 : offsetY;
                    endY1 = isShow ? -offsetY : 0;
                    ObjectAnimator.ofFloat(mBannerTV1, "translationY", startY1, endY1).setDuration(300).start();
                    startY2 = isShow ? offsetY : 0;
                    endY2 = isShow ? 0 : -offsetY;
                    ObjectAnimator.ofFloat(mBannerTV2, "translationY", startY2, endY2).setDuration(300).start();
                    handler.postDelayed(runnable, 6000);
                }
                }

        };
    }
    public List<String> getList() {
        return list;
    }
    public void setList(List<String> list) {
        this.list = list;
    }
    public void startScroll() {
        handler.post(runnable);
    }

    public void setStop(boolean isStop){
        this.isStop = isStop;
    }

    public void stopScroll() {
        handler.removeCallbacks(runnable);
    }
    public int  getTextWidth(){return mBannerTV1.getWidth();};
    public  int  getCurrent(){return position;};

    public void setIcon(int iconid){
        icon.setImageResource(iconid);
    }
    public void setIcon(String url){
        ImageLoader.getInstance().displayImage(url
                , icon, options);
    }

}
