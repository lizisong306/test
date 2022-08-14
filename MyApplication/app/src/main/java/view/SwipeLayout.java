package view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class SwipeLayout extends FrameLayout {

    private Callback mCallback = new Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mFrontView) {
                if (left < -mBackWidth) {
                    left = -mBackWidth;
                } else if (left > 0) {
                    left = 0;
                }
            } else if (child == mBackView) {
                if (left < mWidth - mBackWidth) {
                    left = mWidth - mBackWidth;
                } else if (left > mWidth) {
                    left = mWidth;
                }
            }
            return left;
        };

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == mFrontView) {
                mBackView.offsetLeftAndRight(dx);
            } else if (changedView == mBackView) {
                mFrontView.offsetLeftAndRight(dx);
            }
            dispatchDragState(mFrontView.getLeft());
            invalidate();
        };

        public int getViewHorizontalDragRange(View child) {
            return mBackWidth;
        };

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (xvel == 0.0f && mFrontView.getLeft() < -mBackWidth * 0.5f) {
                open();
            } else if (xvel < 0) {
                open();
            } else {
                close();
            }
        };
    };
    private ViewDragHelper mDragHelper;
    private View mBackView;
    private View mFrontView;
    private int mWidth;
    private int mHeight;
    private int mBackWidth;

    public enum State {
        CLOSE, OPEN, DRAGGING
    }

    private State mState = State.CLOSE;

    public interface OnDragStateChangeListener {
        void onOpen(SwipeLayout layout);

        void onClose(SwipeLayout layout);

        void onDragging();

        void onStartOpen(SwipeLayout layout);

        void onStartClose(SwipeLayout layout);
    }

    private OnDragStateChangeListener mOnDragStateChangeListener;

    public State getState() {
        return mState;
    }

    protected void dispatchDragState(int left) {
        State preState = mState;
        mState = updateState(left);
        if (mOnDragStateChangeListener != null) {
            if (mState != preState) {
                if (mState == State.OPEN) {
                    mOnDragStateChangeListener.onOpen(this);
                } else if (mState == State.CLOSE) {
                    mOnDragStateChangeListener.onClose(this);
                } else if (mState == State.DRAGGING) {
                    if (preState == State.OPEN) {
                        mOnDragStateChangeListener.onStartClose(this);
                    } else if (preState == State.CLOSE) {
                        mOnDragStateChangeListener.onStartOpen(this);
                    }
                }
            } else {
                mOnDragStateChangeListener.onDragging();
            }
        }
    }

    private State updateState(int left) {
        if (left == -mBackWidth) {
            return State.OPEN;
        } else if (left == 0) {
            return State.CLOSE;
        } else {
            return State.DRAGGING;
        }
    }

    public void setState(State state) {
        mState = state;
    }

    public OnDragStateChangeListener getOnDragStateChangeListener() {
        return mOnDragStateChangeListener;
    }

    public void setOnDragStateChangeListener(OnDragStateChangeListener onDragStateChangeListener) {
        mOnDragStateChangeListener = onDragStateChangeListener;
    }

    public SwipeLayout(Context context) {
        this(context, null);
    }

    protected void open() {
        // mFrontView.layout(-mBackWidth, 0, -mBackWidth+mWidth, mHeight);
        // mBackView.layout(-mBackWidth+mWidth, 0, mWidth, mHeight);
        open(true);
    }

    public void open(boolean isSmooth) {
        if (isSmooth) {
            mDragHelper.smoothSlideViewTo(mFrontView, -mBackWidth, 0);
            invalidate();
        } else {
            layoutContent(true);
        }
    }

    protected void close() {
        close(true);
    }

    public void close(boolean isSmooth) {
        if (isSmooth) {
            mDragHelper.smoothSlideViewTo(mFrontView, 0, 0);
            invalidate();
        } else {
            layoutContent(false);
        }
    }

    private void layoutContent(boolean isOpen) {
        Rect frontRect = computeFrontRect(isOpen);
        Rect backRect = computeBackRectFromFront(frontRect);
        mFrontView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);
        mBackView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);
    }

    private Rect computeBackRectFromFront(Rect frontRect) {
        return new Rect(frontRect.right, frontRect.top, frontRect.right + mBackWidth,
                frontRect.bottom);
    }

    private Rect computeFrontRect(boolean isOpen) {
        int left = 0;
        if (isOpen) {
            left = -mBackWidth;
        }
        return new Rect(left, 0, left + mWidth, mHeight);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, mCallback);
    }

    // 在onLayout中"摆放"每个子View的位置
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mBackView.layout(mWidth, 0, mBackWidth + mWidth, mHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mBackView = getChildAt(0);
        mFrontView = getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mBackWidth = mBackView.getMeasuredWidth();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

}
