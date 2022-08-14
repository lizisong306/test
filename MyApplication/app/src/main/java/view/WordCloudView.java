package view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.maidiantech.NewSearchContent;
import com.maidiantech.NewSearchHistory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Created by chao on 2019/2/15.
 */

public class WordCloudView extends FrameLayout implements View.OnClickListener {

    Random random = new Random();
    String[] words;
    HashSet<View> placed = new HashSet<>();
    int maxW =0,maxH=0;

    public WordCloudView(@NonNull Context context) {
        this(context, null);
    }

    public WordCloudView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WordCloudView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int n = getChildCount();
        maxW = getMeasuredWidth()-50;
        maxH = getMeasuredHeight()-50;
        Log.d("chakan", "w:"+getMeasuredWidth()+"，h:"+getMeasuredHeight());
        for(int i = 0; i < n; i++) {
            View v = getChildAt(i);
            if(placed.contains(v)) {
                continue;
            }
            int w = v.getMeasuredWidth()+20;
            int h = v.getMeasuredHeight()+20;
//            Log.d("lizisong", "getWidth():"+getWidth());
//            int pivotX = random.nextInt(maxW-w)+w;
//            int pivotY = random.nextInt(maxH-h)+h;

//            int pivotX = getMeasuredWidth() / 3 + random.nextInt(getMeasuredWidth() / 3);
//            int pivotY = getMeasuredHeight() / 3 + random.nextInt((getMeasuredHeight()) / 3);
//            int pivotX = random.nextInt((getMeasuredWidth()-w));
//            int pivotY = random.nextInt((getMeasuredHeight()-h));
            List<Point> spiral = generateSpiral();
            for(Point p : spiral) {
                int pivotX = random.nextInt(maxW-w)+(w/2);
                int pivotY = random.nextInt(maxH-h)+(h/2);
                pivotX += p.x;
                pivotY += p.y;

                Log.d("lizisong", "place " + pivotX + "," + pivotY);
                Rect r1 = getVisualRect(pivotX, pivotY, w, h, v.getRotation());
                boolean isOverlap = false;
                for(View pv : placed) {
                    Rect r2 = getVisualRect(pv);

                    if(isOverlap(r1, r2)) {
                        isOverlap = true;
                        break;
                    }
                }
                if(isOverlap) {

                } else {

                    Rect r = getRect(pivotX, pivotY, w, h);
                    Log.d("lizisong", "placed");
//                    if(r.right> maxW) {
//                        r.right=maxW;
//                    }
//                    if(r.bottom> maxH){
//                        r.bottom=maxH;
//                    }
                    if(((r.bottom)<h) || (r.right-r.left)< w){
                        continue;
                    }

                    v.layout(r.left, r.top, r.right, r.bottom);
                    Log.d("chakan", "label:"+((TextView)v).getText().toString()+r.left+","+ r.top+"," +r.right+","+ r.bottom+","+w+","+h);
                    break;
                }
            }
            placed.add(v);
        }
    }

    public Rect getRect(int pivotX, int pivotY, int width, int height) {
        return new Rect(
                pivotX - (width)/2 ,
                pivotY - (height) /2,
                pivotX + (width)/2 ,
                pivotY + (height)/2
        );
    }

    public Rect getVisualRect(int pivotX, int pivotY, int width, int height, float rotation) {
        if(rotation != 0) {
            int temp = width;
            width = height;
            height = temp;
        }
        return getRect(pivotX, pivotY, width, height);
    }

    public Rect getVisualRect(View v) {
        return getVisualRect(
                    (v.getRight() + v.getLeft()) / 2,
                    (v.getBottom() + v.getTop()) / 2,
                (v.getMeasuredWidth()+20),
                (v.getMeasuredHeight()+20),
                    v.getRotation()
                );
    }

    public static boolean isOverlap(Rect r1, Rect r2) {
        return r1.right >= r2.left && r2.right >= (r1.left)
                && r1.bottom >= r2.top && r2.bottom >= (r1.top);
    }

//    public void setWords(String[] words) {
//        this.words = words;
//        placed.clear();
//        removeAllViews();
//        for(final String word : words) {
//            addTextView(word);
//        }
//    }

    LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    float[] rotates = {
            0f,90f,270f
    };

    public void addTextView(String word, int weight) {
        TextView tv = new TextView(getContext());
        tv.setText(word);
        tv.setTextSize(weight);
        tv.setRotation(/*rotates[random.nextInt(rotates.length)]*/0);
        tv.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        if(weight<=21 && weight>=18){
            tv.setTextColor(0xff1431c8);
        }else if(weight<18 && weight>=15){
            tv.setTextColor(0xff4c59d9);
        }else if(weight<15 && weight>=10){
            tv.setTextColor(0xff6c9af7);
        }else{
            tv.setTextColor(0xffb1d0ff);
        }
        tv.setOnClickListener(this);
        addView(tv, params);
    }

    TextView lastText;

    @Override
    public void onClick(View v) {
        if(v instanceof TextView) {
//            Log.e("lizisong", "click " + ((TextView) v).getText());
//            ((TextView) v).setTextColor(Color.RED);
            if(lastText != null) {
//                lastText.setTextColor(Color.BLACK);
            }
            lastText = (TextView) v;
            Intent intent = new Intent(getContext(), NewSearchContent.class);
            intent.putExtra("hot", lastText.getText().toString());
            intent.putExtra("typeid", "");
            getContext().startActivity(intent);
        }
    }

    private List<Point> generateSpiral() {
//        List<Point> res = new ArrayList<>();
//        int A = 4;
//        int w = 2;
//        double sita = Math.PI;
//        for(double t = 0; t < 10 * Math.PI; t+=0.2) {
//            int x = Double.valueOf(A * Math.sin(w * t + sita)).intValue();
//            int y =  Double.valueOf(A * Math.cos(w * t + sita)).intValue();
//            A += 1;
//            res.add(new Point(x, y));
//            Log.e("lizisong", x + ", " + y);
//        }
//        return res;
        List<Point> res = new ArrayList<>();
        int A = 4;
        int w = 4;
        double sita = Math.PI;
        for(double t = 0; t < 10 * Math.PI; t+=0.1) {
            int x = Double.valueOf(A * Math.cos(w * t + sita)).intValue();
            int y = Double.valueOf(A * Math.sin(w * t + sita)).intValue();
            A += 1;
            res.add(new Point(x, y));
            Log.e("chao", x + ", " + y);
        }
        return res;
    }
}
