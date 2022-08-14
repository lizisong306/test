package view;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.Calendar;

/**
 * Created by Administrator on 2018/8/22.
 */

public abstract class NoDobleClickListener implements AdapterView.OnItemClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;//这里设置不能超过多长时间
    private long lastClickTime = 0;
    protected abstract void onNoDoubleClick(AdapterView<?> parent, View view, int position, long id);
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            Log.d("lizisong", "onItemClick");
            lastClickTime = currentTime;
            onNoDoubleClick( parent, view,  position,id);
        }
    }
}
