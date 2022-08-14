package com.maidiantech.common.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maidiantech.R;

import java.util.Date;

public class RefreshListView extends ListView {
    // 是否添加header，下拉刷新
    private final String AddHeader = "addHeader";
    private boolean addHeader = false;

    private final static int RELEASE_To_REFRESH = 0;
    private final static int PULL_To_REFRESH = 1;
    private final static int REFRESHING = 2;
    private final static int DONE = 3;

    // 实际的padding的距离与界面上偏移距离的比例
    private final static int RATIO = 3;

    private LayoutInflater inflater;

    private LinearLayout headView;

    private TextView tipsTextview;
    private TextView lastUpdatedTextView;
    private ImageView arrowImageView;
    private ProgressBar progressBar;
    private RelativeLayout tipsRelative;
    private TextView  tipText;

    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;

    private View mLoadingFooterView;
    private OnScrollListener mOnScrollListener;
    private boolean mPullUpToRefreshable = false;
    private boolean mPullDownToRefreshable = false;
    private int state;
    protected int firstItemIndex;
    private int headContentHeight;
    private int startY;
    private boolean isBack;
    private boolean isRecored;
    private boolean isShowTips = false;

    private int scrollState = OnScrollListener.SCROLL_STATE_IDLE;

    public RefreshListView(Context context) {
        super(context);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.addHeader = attrs.getAttributeBooleanValue(null, AddHeader, false);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addHeader = attrs.getAttributeBooleanValue(null, AddHeader, false);
        init(context);
    }

    private void init(Context context) {
        if (addHeader) {
            inflater = LayoutInflater.from(context);
            headView = (LinearLayout) inflater.inflate(
                    R.layout.refresh_listview_header, null);
            arrowImageView = (ImageView) headView
                    .findViewById(R.id.head_arrowImageView);
            progressBar = (ProgressBar) headView
                    .findViewById(R.id.head_progressBar);
            tipsTextview = (TextView) headView
                    .findViewById(R.id.head_tipsTextView);
            lastUpdatedTextView = (TextView) headView
                    .findViewById(R.id.head_lastUpdatedTextView);
            tipsRelative = (RelativeLayout)headView.findViewById(R.id.tips);
            tipText = (TextView)headView.findViewById(R.id.tips_txt);
            measureView(headView);
            headContentHeight = headView.getMeasuredHeight();
            headView.setPadding(0, -1 * headContentHeight, 0, 0);
            headView.invalidate();
            addHeaderView(headView, null, false);
            animation = new RotateAnimation(0, -180,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            animation.setInterpolator(new LinearInterpolator());
            animation.setDuration(250);
            animation.setFillAfter(true);
            reverseAnimation = new RotateAnimation(-180, 0,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            reverseAnimation.setInterpolator(new LinearInterpolator());
            reverseAnimation.setDuration(200);
            reverseAnimation.setFillAfter(true);
            state = DONE;
        }

        mPullDownToRefreshable = false;
        addFooterView(new View(getContext()), null, false);
        // 保证列表的最下方有线
        addFooterView(new View(getContext()), null, false);
        super.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                RefreshListView.this.scrollState = scrollState;
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                firstItemIndex = firstVisibleItem;
                if (mPullUpToRefreshable
                        && firstVisibleItem + visibleItemCount + 2 >= totalItemCount
                        && !isPullToRefreshing
                        && visibleItemCount < totalItemCount
                        && scrollState != SCROLL_STATE_IDLE

                        /*&& NetUtils.isNetConnected()*/) {

                    isPullToRefreshing = true;
                    if (mOnPullUpToRefreshListener != null) {
                        mOnPullUpToRefreshListener.pullUpToRefresh();
                    }
                    if (mLoadingFooterView == null) {
                        mLoadingFooterView = LayoutInflater
                                .from(getContext())
                                .inflate(R.layout.refresh_listview_footer, null);
                    } else {
                        if (getFooterViewsCount() > 0) {
                            removeFooterView(mLoadingFooterView);
                        }
                    }
                    addFooterView(mLoadingFooterView);
                }
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScroll(view, firstVisibleItem,
                            visibleItemCount, totalItemCount);
                }
            }
        });

    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if ((null != mPullInterceptListener)
                && (mPullInterceptListener.isPullIntercept())) {
            return false;
        }
        // 屏蔽多点触摸
        switch (ev.getAction()) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_POINTER_ID_MASK:
            case MotionEvent.ACTION_POINTER_ID_SHIFT:
                return true;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event) {

        if (addHeader && mPullDownToRefreshable && state != REFRESHING) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (firstItemIndex == 0 && !isRecored) {
                        isRecored = true;
                        startY = (int) event.getY();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    // 没有刷新
                    if (state != REFRESHING) {
                        if (state == DONE) {
                        }
                        // 没有拉到位置
                        if (state == PULL_To_REFRESH) {
                            state = DONE;
                            changeHeaderViewByState();
                        }
                        // 拉到位置，开始刷新
                        if (state == RELEASE_To_REFRESH) {
                            state = REFRESHING;
                            changeHeaderViewByState();
                            if (null != mOnPullDownToRefreshListener) {
                                mOnPullDownToRefreshListener.pullDownToRefresh();
                            }
                        }
                    }
                    isRecored = false;
                    isBack = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) event.getY();
                    // 没有ACTION_DOWN，记录第一个ACTION_MOVE位置
                    if (!isRecored && firstItemIndex == 0) {
                        isRecored = true;
                        startY = tempY;
                    }
                    if (state != REFRESHING && isRecored) {
                        // 松手刷新状态
                        if (state == RELEASE_To_REFRESH) {
                            setSelection(0);
                            if (((tempY - startY) / RATIO < headContentHeight)
                                    && (tempY - startY) > 0) {
                                state = PULL_To_REFRESH;
                                changeHeaderViewByState();
                            } else if (tempY - startY <= 0) {
                                state = DONE;
                                changeHeaderViewByState();
                            } else {
                            }
                        }
                        // 下拉刷新状态
                        if (state == PULL_To_REFRESH) {
                            setSelection(0);
                            if ((tempY - startY) / RATIO >= headContentHeight) {
                                state = RELEASE_To_REFRESH;
                                isBack = true;
                                changeHeaderViewByState();
                            } else if (tempY - startY <= 0) {
                                state = DONE;
                                changeHeaderViewByState();
                            }
                        }
                        // 恢复态
                        if (state == DONE) {
                            if (tempY - startY > 0) {
                                state = PULL_To_REFRESH;
                                changeHeaderViewByState();
                            }
                        }
                        if (state == PULL_To_REFRESH) {
                            headView.setPadding(0, -1 * headContentHeight
                                    + (tempY - startY) / RATIO, 0, 0);
                        }
                        if (state == RELEASE_To_REFRESH) {
                            headView.setPadding(0, (tempY - startY) / RATIO
                                    - headContentHeight, 0, 0);
                        }
                    }
                    break;
            }
        }
        // cache住此异常是为了解决java.lang.ArrayIndexOutOfBoundsException的异常
        try {
            return super.onTouchEvent(event);
        } catch (Exception e) {
            return true;
        }
    }

    private void changeHeaderViewByState() {
        switch (state) {
            case RELEASE_To_REFRESH:
                arrowImageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(animation);
                tipsTextview.setText("松开刷新");

                break;
            case PULL_To_REFRESH:
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.VISIBLE);
                // 是由RELEASE_To_REFRESH状态转变来的
                if (isBack) {
                    isBack = false;
                    arrowImageView.clearAnimation();
                    arrowImageView.startAnimation(reverseAnimation);
                }
                tipsTextview.setText("下拉刷新");
                break;

            case REFRESHING:
                headView.setPadding(0, 0, 0, 0);
                progressBar.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.GONE);
                tipsTextview.setText("正在刷新...");
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                break;
            case DONE:
                headView.setPadding(0, -1 * headContentHeight, 0, 0);
                progressBar.setVisibility(View.GONE);
                arrowImageView.clearAnimation();
                arrowImageView.setImageResource(R.mipmap.ic_pulltorefresh_arrow);
                tipsTextview.setText("下拉刷新");
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                if(isShowTips){
                    handler.sendEmptyMessageDelayed(0,500);
                }
                break;
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                isShowTips = false;
                tipsRelative.setVisibility(VISIBLE);
                if(tipsRelative.getVisibility() == View.VISIBLE){
                    sendEmptyMessageDelayed(1,2000);
                }
            }else if(msg.what == 1){
                tipsRelative.setVisibility(GONE);
            }
        }
    };

    private PullInterceptListener mPullInterceptListener;

    public interface PullInterceptListener {
        public boolean isPullIntercept();
    }

    public void setPullInterceptListener(PullInterceptListener ls) {
        mPullInterceptListener = ls;
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }

    public interface OnPullUpToRefreshListener {
        void pullUpToRefresh();
    }

    public interface OnPullDownToRefreshListener {
        void pullDownToRefresh();
    }

    private OnPullUpToRefreshListener mOnPullUpToRefreshListener;
    private OnPullDownToRefreshListener mOnPullDownToRefreshListener;

    public void setOnPullUpToRefreshListener(OnPullUpToRefreshListener listener) {
        mOnPullUpToRefreshListener = listener;
    }

    public void setOnPullDownToRefreshListener(
            OnPullDownToRefreshListener listener) {
        mOnPullDownToRefreshListener = listener;
    }

    public void setPullUpToRefreshable(boolean refreshable) {
        mPullUpToRefreshable = refreshable;
    }

    public void setPullDownToRefreshable(boolean refreshable) {
        mPullDownToRefreshable = refreshable;
    }

    private boolean isPullToRefreshing = false;

    public void setPullUpToRefreshFinish() {
        isPullToRefreshing = false;
        if (getFooterViewsCount() > 0 && mLoadingFooterView != null) {
            removeFooterView(mLoadingFooterView);
        }
    }

    public void setPullDownToRefreshFinish() {
        if (addHeader) {
            state = DONE;
            lastUpdatedTextView.setText("最近更新:" + new Date().toLocaleString());
            changeHeaderViewByState();
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (addHeader) {
            lastUpdatedTextView.setText("最近更新:" + new Date().toLocaleString());
        }
        super.setAdapter(adapter);
    }

    // cache住此异常是为了解决java.lang.ArrayIndexOutOfBoundsException的异常
    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (Exception e) {
        }
    }

    public void showTipTitle(String txt, OnClickListener listener){
        isShowTips = true;
        tipText.setText(txt);
        tipText.setOnClickListener(listener);
    }
}
