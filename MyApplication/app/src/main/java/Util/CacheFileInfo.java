package Util;

import java.io.File;

/**
 * Created by lizisong on 2017/1/18.
 */

public class CacheFileInfo {

    private long mCacheImageFileSize = 0;



    /**
     * 获取文件的大小
     * @param dir
     * @return
     */
    public static FolderInfo loadFolderInfo(String dir) {
        FolderInfo finfo = new FolderInfo();
        if ((null == dir) || (dir.length() <= 0)) {
            return finfo;
        }

        File fDir = new File(dir);
        File[] fFiles = fDir.listFiles();
        if (fFiles != null) {
            finfo.fileCnt = fFiles.length;
            for (File f : fFiles) {
                finfo.allFileSize += f.length();
            }
        }
        return finfo;
    }

    /**
     * 将字节表示的文件大小格式化为K/M/G这样的字符串形式
     *
     * @param lSize 文件大小(字节为单位)
     * @return 格式化后的字符串
     */
    public static String fileSize2String(float lSize) {
        if (lSize > 1024 * 1024 * 1024) {
            return String.format("%.1fG", lSize / 1024.0 / 1024.0 / 1024.0);
        }
        if (lSize > 1024 * 1024) {
            return String.format("%.1fM", lSize / 1024.0 / 1024.0);
        }
        if (lSize > 1024) {
            return String.format("%.1fK", lSize / 1024.0);
        }
        return String.format("%.1fB", lSize);
    }
}
