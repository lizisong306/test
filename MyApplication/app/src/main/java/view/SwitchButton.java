package view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.maidiantech.R;

/**
 * Created by lizisong on 2017/2/23.
 */

public class SwitchButton extends View implements View.OnClickListener{
    private Bitmap mSwitchBottom, mSwitchThumb,mSwitchThumb_on, mSwitchFrame, mSwitchMask;
    private float mCurrentX = 0;

    private int mMoveLength;//最大移动距离
    private float mLastX = 0;//第一次按下的有效区域

    private Rect mDest = null;//绘制的目标区域大小
    private Rect mSrc = null;//截取源图片的大小
    private int mDeltX = 0;//移动的偏移量
    private Paint mPaint = null;
    private OnChangeListener mListener = null;
    private boolean mFlag = false;
    private Context context;
    private boolean NowChoose = true;

    public SwitchButton(Context context) {
        this(context, null);
        //init(context);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        //init(context);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 初始化相关资源
     */
    public void init(Context context) {
        this.context = context;
        mSwitchBottom = BitmapFactory.decodeResource(getResources(),
                 R.mipmap.switch_bottom);
        mSwitchThumb = BitmapFactory.decodeResource(getResources(),
                R.mipmap.switch_btn_pressed);
        mSwitchThumb_on= BitmapFactory.decodeResource(getResources(),
                R.mipmap.switch_btn_unpressed);
        mSwitchFrame = BitmapFactory.decodeResource(getResources(),
                R.mipmap.switch_frame);
        mSwitchMask = BitmapFactory.decodeResource(getResources(),
                R.mipmap.switch_mask);
        setOnClickListener(this);
        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

//		Log.e("mSwitchBottom.getWidth()", mSwitchBottom.getWidth()+"");//123
//		Log.e("mSwitchFrame.getWidth()", mSwitchFrame.getWidth()+"");//78
        mMoveLength = mSwitchBottom.getWidth() - mSwitchFrame.getWidth()-3;
//		Log.e("mMoveLength", mMoveLength+"");//45
        mDest = new Rect(0, 0, mSwitchFrame.getWidth(), mSwitchFrame.getHeight());
        mSrc = new Rect();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(255);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSwitchFrame.getWidth(), mSwitchFrame.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (mDeltX > 0 || mDeltX == 0 && !NowChoose) {

            mSrc.set(mMoveLength - mDeltX, 0, mSwitchBottom.getWidth()- mDeltX, mSwitchFrame.getHeight());

        } else if(mDeltX < 0 || mDeltX == 0 && NowChoose){
            mSrc.set(-mDeltX, 0, mSwitchFrame.getWidth() - mDeltX,mSwitchFrame.getHeight());

        }
        //	<SPAN style="WHITE-SPACE: pre">		</SPAN>//这儿是离屏缓冲，自己感觉类似双缓冲机制吧
        int count = canvas.saveLayer(new RectF(mDest), null, Canvas.MATRIX_SAVE_FLAG
                | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
                | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                | Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        canvas.drawBitmap(mSwitchBottom, mSrc, mDest, null);
        if(!NowChoose)
            canvas.drawBitmap(mSwitchThumb, mSrc, mDest, null);
        else
            canvas.drawBitmap(mSwitchThumb_on, mSrc, mDest, null);
        canvas.drawBitmap(mSwitchFrame, 0, 0, null);
        canvas.drawBitmap(mSwitchMask, 0, 0, mPaint);
        canvas.restoreToCount(count);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getX();
                mDeltX = (int) (mCurrentX - mLastX);
                // 如果开关开着向左滑动，或者开关关着向右滑动（这时候是不需要处理的）
                if ((!NowChoose && mDeltX < 0) || (NowChoose && mDeltX > 0)) {
                    mFlag = true;
                    mDeltX = 0;
                }

                if (Math.abs(mDeltX) > mMoveLength) {
                    mDeltX = mDeltX > 0 ? mMoveLength : - mMoveLength;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(mDeltX) > 0 && Math.abs(mDeltX) < mMoveLength / 2) {
                    mDeltX = 0;
				/*if(mListener != null) {
					mListener.onChange(this, NowChoose);
				}*/
                    invalidate();
                    return true;
                } else if (Math.abs(mDeltX) >= mMoveLength / 2) {
                    mDeltX = mDeltX > 0 ? mMoveLength : -mMoveLength;
                    NowChoose = !NowChoose;
//				Const.mSwitchOn = !Const.mSwitchOn;
                    if(mListener != null) {
                        mListener.onChange(this, NowChoose);
                    }
                    invalidate();
                    mDeltX = 0;
                    return true;
                } else if(mDeltX == 0 && mFlag) {
                    //这时候得到的是不需要进行处理的，因为已经move过了
                    mDeltX = 0;
                    mFlag = false;
                    break;//return true;
                }
                return super.onTouchEvent(event);
            case MotionEvent.ACTION_CANCEL:
                if((Math.abs(mDeltX) >=mMoveLength/2) ){
                    NowChoose = !NowChoose;
//				mDeltX=mDeltX>0?mMoveLength:-mMoveLength;
                    if(mListener != null) {
                        mListener.onChange(this, NowChoose);
                    }
                    mDeltX=0;
                    invalidate();
                    return true;
                }else
                {
                    mDeltX=0;
                    invalidate();
                }
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    public void setOnChangeListener(OnChangeListener listener) {
        mListener = listener;
    }

    public interface OnChangeListener {
        public void onChange(SwitchButton sb, boolean state);
    }

    @Override
    public void onClick(View v) {
        mDeltX = NowChoose ? mMoveLength : -mMoveLength;

        NowChoose = !NowChoose;
//		Tools.setSwitchFlag();
//		if(NowChoose){//
//			UmengSingleton.getUmengSingleton().onUmengEvent(context, UmengSingleton.SETTINGS_SWITCH_OFF);
//		}else{
//			UmengSingleton.getUmengSingleton().onUmengEvent(context, UmengSingleton.SETTINGS_SWITCH_ON);
//		}
        if(mListener != null) {
            mListener.onChange(this, NowChoose);
        }
        invalidate();
        mDeltX = 0;
    }

    public void setChecked(boolean fl) {
        NowChoose = fl;
        invalidate();
    }
    public boolean getChecked()
    {
        return NowChoose;
    }

}
