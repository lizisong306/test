package com.maidiantech;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by lizisong on 2016/12/12.
 */

public class Rotate2Animation extends Animation {
//    /** 值为true时可明确查看动画的旋转方向。 */
//    public static final boolean DEBUG = false;
//    /** 沿Y轴正方向看，数值减1时动画逆时针旋转。 */
//    public static  int ROTATE_DECREASE = 1;
//    /** 沿Y轴正方向看，数值减1时动画顺时针旋转。 */
//    public static  int ROTATE_INCREASE = 2;
//    /** Z轴上最大深度。 */
//    public static final float DEPTH_Z = 310.0f;
//    /** 动画显示时长。 */
//    public static final long DURATION = 800l;
//    /** 图片翻转类型。 */
//    private  int type = 1;
//    private final float centerX;
//    private final float centerY;
//    private Camera camera;
//    /** 用于监听动画进度。当值过半时需更新txtNumber的内容。 */
   private InterpolatedTimeListener listener;
//
//
//
//
//
//    public Rotate2Animation(float cX, float cY, int type) {
//        centerX = cX;
//        centerY = cY;
//        this.type = type;
//        setDuration(DURATION);
//    }
//
//    public void initialize(int width, int height, int parentWidth, int parentHeight) {
//        // 在构造函数之后、getTransformation()之前调用本方法。
//        super.initialize(width, height, parentWidth, parentHeight);
//        camera = new Camera();
//    }
//
    public void setInterpolatedTimeListener(InterpolatedTimeListener listener) {
        this.listener = listener;
    }
//
//    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
//        // interpolatedTime:动画进度值，范围为[0.0f,10.f]
//        if (listener != null) {
//            listener.interpolatedTime(interpolatedTime);
//        }
//        float from = 0.0f, to = 0.0f;
//        if (type == ROTATE_DECREASE) {
//            Log.d("lizisong", "ROTATE_DECREASE");
//            from = 0.0f;
//            to = 180.0f;
//        } else if (type == ROTATE_INCREASE) {
//            Log.d("lizisong", "ROTATE_INCREASE");
//            from = 360.0f;
//            to = 180.0f;
//        }
//        float degree = from + (to - from) * interpolatedTime;
//        boolean overHalf = (interpolatedTime > 0.5f);
//        if (overHalf) {
//            // 翻转过半的情况下，为保证数字仍为可读的文字而非镜面效果的文字，需翻转180度。
//            degree = degree - 180;
//        }
//        // float depth = 0.0f;
//        float depth = (0.5f - Math.abs(interpolatedTime - 0.5f)) * DEPTH_Z;
//        final Matrix matrix = transformation.getMatrix();
//        camera.save();
//        camera.translate(0.0f, 0.0f, 0.5f);
//        Log.d("lizisong", "degree:"+degree);
//        camera.rotateY(degree);
//        camera.getMatrix(matrix);
//        camera.restore();
//        if (DEBUG) {
//            if (overHalf) {
//                matrix.preTranslate(-centerX * 2, -centerY);
//                matrix.postTranslate(centerX * 2, centerY);
//            }
//        } else {
//            //确保图片的翻转过程一直处于组件的中心点位置
//            matrix.preTranslate(-centerX, -centerY);
//            matrix.postTranslate(centerX, centerY);
//
//        }
//    }
//
    /** 动画进度监听器。 */
    public static interface InterpolatedTimeListener {
        public void interpolatedTime(float interpolatedTime);
    }

    // 开始角度
    private final float mFromDegrees;
    // 结束角度
    private final float mToDegrees;
    // X轴中心点
    private final float mCenterX;
    // Y轴中心点
    private final float mCenterY;
    // Z轴中心点
    private final float mDepthZ;
    //是否需要扭曲
    private final boolean mReverse;
    //摄像头
    private Camera mCamera;

    public Rotate2Animation(float fromDegrees, float toDegrees,
                    float centerX,float centerY,
                    float depthZ, boolean reverse) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mCenterX = centerX;
        mCenterY = centerY;
        mDepthZ = depthZ;
        mReverse = reverse;
    }

    @Override
    public void initialize(int width, int height,
                           int  parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth,
                parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float
                                               interpolatedTime, Transformation t) {
        if(listener != null){
            listener.interpolatedTime(interpolatedTime);
        }
        final float fromDegrees = mFromDegrees;
        // 生成中间角度
        float degrees = fromDegrees
                + ((mToDegrees - fromDegrees)
                * interpolatedTime);
        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Camera camera = mCamera;
        final Matrix matrix = t.getMatrix();//取得当前矩阵
        camera.save();
        if (mReverse) {
            camera.translate(0.0f, 0.0f, mDepthZ *
                    interpolatedTime);
        } else {
            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f -
                    interpolatedTime));
        }

        camera.rotateY(degrees);//翻转
        camera.getMatrix(matrix);// 取得变换后的矩阵
        camera.restore();
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}
