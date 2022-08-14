package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.maidiantech.R;


/**
 * Created by meitu on 2017/7/3.
 */

public class VarietyImageView extends ImageView {
    final String TAG="lizisong";
    private int mImageViewStyle=1;   //ImageView的风格  1为圆形 2为圆角形
    private int mRoundRectRadius=100;  //ImageView的风格为圆角形时,圆角的半径
    //设置在圆角矩形风格下,左上角是否为圆角
    private boolean mLeftTopRound;
    //设置在圆角矩形风格下，左下角是否为圆角
    private boolean mLeftBottomRound;
    //设置在圆角矩形风格下，右上角是否为圆角
    private boolean mRightTopRound;
    //设置在圆角矩形风格下，右下角是否为圆角
    private boolean mRightBottomRound;
    private Paint mPaint;
    private BitmapShader mBitmapShader;
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private int mCircleRadius;//用于存储圆形风格下控件的宽
    private int mViewWidth;
    private int mViewHieght;
    private RectF rectF;
    public VarietyImageView(Context context) {
        this(context,null);
    }
    public VarietyImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        mPaint=new Paint();
        mPaint.setAntiAlias(true);  //抗锯齿设置
        mMatrix=new Matrix();
    }

    public VarietyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.VarietyImageView);
        mImageViewStyle=ta.getInt(R.styleable.VarietyImageView_ImageViewStyle,1);
        mRoundRectRadius=ta.getInt(R.styleable.VarietyImageView_RoundRectRadius,100);
        mLeftTopRound=ta.getBoolean(R.styleable.VarietyImageView_LeftTopRound,true);
        mLeftBottomRound=ta.getBoolean(R.styleable.VarietyImageView_LeftBottomRound,true);
        mRightTopRound=ta.getBoolean(R.styleable.VarietyImageView_RightTopRound,true);
        mRightBottomRound=ta.getBoolean(R.styleable.VarietyImageView_RightBottomRound,true);
        ta.recycle();
        mBitmap=((BitmapDrawable)getDrawable()).getBitmap();  //drawable转换成Bitmap类型
        if(mImageViewStyle==1){
            int offset;
            if (mBitmap.getWidth() < mBitmap.getHeight()) {
                offset = (mBitmap.getHeight() - mBitmap.getWidth()) / 2;
                mBitmap = Bitmap.createBitmap(mBitmap,0, offset, mBitmap.getWidth(), mBitmap.getWidth() + offset);
            } else if (mBitmap.getWidth() > mBitmap.getHeight()) {
                offset = (mBitmap.getWidth() - mBitmap.getHeight()) / 2;
                mBitmap = Bitmap.createBitmap(mBitmap,offset, 0, mBitmap.getHeight() + offset, mBitmap.getHeight());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth=MeasureSpec.getSize(widthMeasureSpec);
        mViewHieght=MeasureSpec.getSize(heightMeasureSpec);
        Log.e(TAG, "onMeasure: "+heightMeasureSpec );
        if(mImageViewStyle==1){
            mCircleRadius=Math.min(getMeasuredWidth(),getMeasuredHeight());
            setMeasuredDimension(mCircleRadius,mCircleRadius);
        }
    }

    /*初始化BitmapShader*/
    private void setUpShader() {
        if (getDrawable() == null) {
            return;
        }
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (mImageViewStyle== 1) {
            // 拿到bitmap宽或高的小值
            int bSize = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
            scale = mCircleRadius * 1.0f / bSize;
        } else if (mImageViewStyle == 2) {
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；
            // 为了实现四个角的圆角效果，缩放后的图片的宽高，需要大于view的宽高；所以这里取大值，从而按照相差较小的边缩放；
            scale = Math.max(getWidth() * 1.0f / mBitmap.getWidth(), getHeight() * 1.0f / mBitmap.getHeight());
            //PS:这个缩放规则不是必须的，可视自己项目情况而修改
        }
        // shader的变换矩阵，用于放大或者缩小
        mMatrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mPaint.setShader(mBitmapShader);
        rectF = new RectF(new Rect(0, 0, getWidth(), getHeight()));
        Log.e(TAG, "setUpShader: "+getWidth()+" "+getHeight());
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if(getDrawable()!=null){
           setUpShader();
            if(mImageViewStyle==1){
                canvas.drawCircle(mCircleRadius / 2, mCircleRadius/ 2, mCircleRadius/ 2, mPaint);
            }else if(mImageViewStyle==2){
                //圆角形
                canvas.drawRoundRect(rectF, mRoundRectRadius, mRoundRectRadius, mPaint);
                if(!mLeftTopRound){   //如果左上角不设置为圆角，则画出左上角的矩形，下同；
                    canvas.drawRect(0, 0, mRoundRectRadius, mRoundRectRadius, mPaint);
                }
                if(!mLeftBottomRound){
                    canvas.drawRect(0, mViewHieght-mRoundRectRadius, mRoundRectRadius, mViewHieght, mPaint);
                    Log.e(TAG, "onDraw: "+mViewHieght );
                }
                if(!mRightTopRound){
                    canvas.drawRect(mViewWidth-mRoundRectRadius, 0, mViewWidth, mRoundRectRadius, mPaint);
                }
                if(!mRightBottomRound){
                    canvas.drawRect(mViewWidth-mRoundRectRadius, mViewHieght-mRoundRectRadius,mViewWidth, mViewHieght, mPaint);
                }
            }
        }else{
            super.onDraw(canvas);
        }
    }

}
