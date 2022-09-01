package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class BgView extends View {
    int width,height;
    int viewWidth,viewHeight;
    Paint paint = new Paint();
    public BgView(Context context) {
        super(context);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3.0f);
    }

    public BgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3.0f);
    }

    public BgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3.0f);
    }

    public BgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        paint.setColor(Color.RED);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        viewWidth=w;
        viewHeight=h;
        Log.d("lizisong","viewWidth:"+viewWidth+"viewHeight:"+viewHeight);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(Color.alpha(0));
        width = canvas.getWidth();
        height=canvas.getHeight();
        canvas.drawLine(0, 0, 80, 80, paint);
        canvas.drawLine(80, 0, 0, 80, paint);

        Log.d("lizisong","width:"+width+"height:"+height);

    }
}
