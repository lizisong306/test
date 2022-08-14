package Util;

import android.os.Environment;

/**
 * Created by dell-pc on 2016/9/13.
 */
public class AppUtils {
    /**
     * 判断sdcard是否存在
     *
     * @return
     */
    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}
