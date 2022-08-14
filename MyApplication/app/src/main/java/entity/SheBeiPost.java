package entity;

import android.media.Image;
import android.os.ParcelUuid;

import java.util.List;

/**
 * Created by Administrator on 2018/12/12.
 */

public class SheBeiPost {
    public String id;
    public String title;
    public String typeid;
    public String typename;
    public String litpic;
    public String largeType;
    public String serviceCount;
    public String count;
    public String imageState;
    public ImageState image;
    public String url;
    public List<DeviceData> deviceServiceList;
    public String pubdate;
}
