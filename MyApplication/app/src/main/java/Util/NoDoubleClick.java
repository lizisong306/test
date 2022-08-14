package Util;

import android.view.View;

/**
 * Created by Administrator on 2019/6/27.
 */

public abstract class NoDoubleClick implements View.OnClickListener  {
    public abstract void Click(View v);
    @Override
    public void onClick(View v) {
        if (!NoDoubleClickUtils.isDoubleClick()) {
            Click(v);
        }
    }
}
