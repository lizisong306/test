package Util;

import android.content.Context;
import android.content.res.Resources;

import com.maidiantech.R;

/**
 * Created by lizisong on 2017/4/17.
 */

public class ADFilterTool {
    public static boolean hasAd(Context context, String url) {
        Resources res = context.getResources();
        String[] adUrls = res.getStringArray(R.array.adBlockUrl);
        for (String adUrl : adUrls) {
            if (url.contains(adUrl)) {
                return true;
            }
        }
        return false;
    }
}
