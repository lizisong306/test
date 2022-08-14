package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.maidiantech.R;

/**
 * Created by lizisong on 2017/2/22.
 */

public class ProgressWebView extends WebView {
    private ProgressBar progressBar;
    //刷新时候不显示进度条，默认不是在刷新，显示进度条
    private boolean isRefresh = false;
    private LoadFinishListener mFinishListener;
    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressBar = (ProgressBar) LayoutInflater.from(context).inflate(
                R.layout.progress_horizontal, null);
        progressBar.setMax(100);
        progressBar.setProgress(0);
        addView(progressBar, LayoutParams.FILL_PARENT, 8);
        setWebChromeClient(new WebChromeClient());
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setVisibility(GONE);
                if (mFinishListener != null) {

                    mFinishListener.OnLoadfinish();
                }
                //加载完成恢复默认状态
                isRefresh=false;
            } else {
                if (!isRefresh) {//不是刷新时候显示

                    if (progressBar.getVisibility() == GONE)
                        progressBar.setVisibility(VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
    //加载完成接口调用，通知wipeRefreshLayout刷新完成
    public interface LoadFinishListener {
        public void OnLoadfinish();
    }

    public void setOnLoadFinishListener(LoadFinishListener loadFinishListener) {
        mFinishListener = loadFinishListener;
    }
    public void isRefresh(boolean is) {
        isRefresh=is;
    }
}
