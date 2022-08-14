package view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by lizisong on 2017/10/23.
 */

public class HeaderListView extends ListView {
    // 放大的 ImageView
    private ImageView headerIV;
    private int height;
    private boolean isShow = false;
    private static final int SCROLLLIMIT = 40;

    public void setHeaderIV(ImageView headerIV) {
        this.headerIV = headerIV;
    }

    public HeaderListView(Context context) {
        super(context);
    }

    public HeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 当 view 依附到 activity 上面的时候回调
     * @param hasWindowFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        if (hasWindowFocus){
            try {
                height = this.headerIV.getHeight();
                if(height<446){
                    height=446;
                }
            }catch (Exception e){

            }

        }
    }
    /**
     *  当listview 滚动到顶部的时候，还要下拉，还要网上滚动，那么这时就会调用该方法
     * @param deltaX
     * @param deltaY
     * @param scrollX
     * @param scrollY
     * @param scrollRangeX
     * @param scrollRangeY
     * @param maxOverScrollX
     * @param maxOverScrollY
     * @param isTouchEvent
     * @return
     */
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        // 滑动过头的时候回调该方法
        // 控制 imageview 的高度逐渐增加------从而达到滚动图片放大的效果
        if(isShow){
        boolean isCollpse = resizeOverScrollBy((int)(deltaY*0.25));

        return isCollpse == false ? isCollpse: super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
        }else {
            return false;
        }
    }

    private boolean resizeOverScrollBy(int deltaY) {
        if(isShow){
        if (deltaY < 0){
            if (headerIV != null){
                // 当滑动到顶部的时候，还要网上滑动，就改变 imageview 的高度
                headerIV.getLayoutParams().height = headerIV.getHeight() - deltaY;
                headerIV.requestLayout();
            }
        } else {
            if (headerIV != null){
                headerIV.getLayoutParams().height = headerIV.getHeight() - deltaY;
                headerIV.requestLayout();
            }
        }}
        return false;
    }

    /**
     * 当listview 没有滑动到底部或顶部时调用
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // 还原图片，保持imageview 的初始化高度
        // 获取imageview 的父容器（RelativeLayout）
        try {
         if(isShow){

            ViewParent parent = this.headerIV.getParent();
            if (parent != null){
                View rootView = (View)parent;
                if (rootView.getTop() < 0 && headerIV .getHeight() > height){

                    headerIV.getLayoutParams().height = headerIV.getHeight() + rootView.getTop();

                    // 重新摆放子控件
                    rootView.layout(rootView.getLeft(), 0, rootView.getRight(), rootView.getBottom());

                    // 重新绘制
                    headerIV.requestLayout();
                }
            }
         }
        }catch (Exception e){

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 监听手势抬起
//        Log.d("lizisong", "isShow:"+isShow);
        if (ev.getAction() == MotionEvent.ACTION_UP){
            if(isShow){
                if(height < 446){
                    height =466;
                }
                if(headerIV == null){
                    return true;
                }
                MyAnimation animation = new MyAnimation(headerIV, height);
                animation.setDuration(500);
                this.headerIV.startAnimation(animation);
            }else{
//                this.headerIV.getLayoutParams().height=350;
            }
        }
        return super.onTouchEvent(ev);
    }
    public void setState(boolean state){
        this.isShow = state;
    }

    public class MyAnimation extends Animation {

        private ImageView imageView;
        // imageview 的原始高度
        private int targetHeight;
        // 当前 imageview 的高度
        private int currentHeight;
        // 高度差 当前的减去原始的
        private int extraHeight;

        public MyAnimation(ImageView imageView, int targetHeight){
            try {
                this.imageView = imageView;
                this.targetHeight = targetHeight;
                this.currentHeight = imageView.getHeight();
                this.extraHeight = this.currentHeight - this.targetHeight;
            }catch (Exception e){

            }

        }

        /**
         *  当动画在不断的执行的时候回调该方法（就是监听动画执行的过程）
         * @param interpolatedTime 值得范围 0.0 到 1.0，时间变化因子
         * @param t
         */
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
//            Log.d("lizisong", "动画的值:"+this.currentHeight+","+ this.targetHeight+","+ this.extraHeight+","+extraHeight * interpolatedTime);
          if(isShow){
               if(this.currentHeight
                       - extraHeight * interpolatedTime > targetHeight){
                   int height = (int)(this.currentHeight
                           - extraHeight * interpolatedTime);

                this.imageView.getLayoutParams().height = height;
               }else{
                   this.imageView.getLayoutParams().height=targetHeight;
               }
              this.imageView.requestLayout();
          }

        }
    }

    public void setHeight(int height){
        this.height = height;
    }


}
