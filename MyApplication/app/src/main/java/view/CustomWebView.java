package view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by lizisong on 2017/5/23.
 */

public class CustomWebView extends WebView {
    ScrollInterface web;
    public CustomWebView(Context context) {
        super(context);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        web.onSChanged(l, t, oldl, oldt);
    }

    public void setOnCustomScroolChangeListener(ScrollInterface t) {
        this.web = t;
    }

    /**
     * 定义滑动接口
     */
    public interface ScrollInterface {

        public void onSChanged(int l, int t, int oldl, int oldt);
    }
}
