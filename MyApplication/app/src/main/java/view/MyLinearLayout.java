package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;



import java.util.Hashtable;

/**
 * Created by Administrator on 2018/10/17.
 */

public class MyLinearLayout extends LinearLayout {
    int mLeft, mRight, mTop, mBottom;
    Hashtable map = new Hashtable();
    boolean rowindex =false;
    int shengyu = 0;
    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, int horizontalSpacing, int verticalSpacing) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
//        Log.d("lizisong", "widthMeasureSpec:"+mWidth);
        int mCount = getChildCount();
        int mX = 0;
        int mY = 0;
        mLeft = 0;
        mRight = 0;
        mTop = 5;
        mBottom = 0;
        int j = 0;

        View lastview = null;
        for (int i = 0; i < mCount; i++) {
            final View child = getChildAt(i);
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            // 此处增加onlayout中的换行判断，用于计算所需的高度
            int childw = child.getMeasuredWidth();
            int childh = child.getMeasuredHeight();
            mX += childw; // 将每次子控件宽度进行统计叠加，如果大于设定的高度则需要换行，高度即Top坐标也需重新设置

            Position position = new Position();
            mLeft = getPosition(i - j, i);
//            Log.d("lizisong", "i:"+i+",j:"+j);
            mRight = mLeft + child.getMeasuredWidth();
            if (mX+60 >= (mWidth-56)) {
                mX = childw;
                mY += childh;
                j = i;
                mLeft = 0;
                mRight = mLeft + child.getMeasuredWidth();
                mTop = mY + 35;
                if(childh *3 < mTop){
                    rowindex = true;
                    shengyu =   mWidth-56-mX;
                }else{
                    rowindex = false;
                }


                // PS：如果发现高度还是有问题就得自己再细调了
            }
            mBottom = mTop + child.getMeasuredHeight();
            mY = mTop; // 每次的高度必须记录 否则控件会叠加到一起
            position.left = mLeft;
            position.top = mTop + 5;
            position.right = mRight;
            position.bottom = mBottom;
            map.put(child, position);
        }
        setMeasuredDimension(mWidth, mBottom);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(0, 0); // default of 1px spacing
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            Position pos = (Position) map.get(child);
            if (pos != null) {
                child.layout(pos.left, pos.top, pos.right, pos.bottom);
            } else {
            }
        }
    }

    private class Position {
        int left, top, right, bottom;
    }

    public int getPosition(int IndexInRow, int childIndex) {
        if (IndexInRow > 0) {
            return getPosition(IndexInRow - 1, childIndex - 1) + getChildAt(childIndex - 1).getMeasuredWidth() + 40;
        }
        return getPaddingLeft();
    }
    public boolean getRows(){
        return rowindex;
    }
    public int shengyu(){
       return  shengyu;
    }
}
