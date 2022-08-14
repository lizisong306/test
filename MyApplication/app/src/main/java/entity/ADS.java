package entity;

import dao.dbentity.PulseData;

/**
 * Created by 13520 on 2016/9/23.
 */
public class ADS {
    public  String aid;
    public String picUrl;
    public String title;
    public String pubdate;
    private String typename;
    public String type;
    public String typeid;
    public String cates;
    public String url;
    public String img;
    public String id;
    public ImageState image;
    public String channelid;
    public String description;
    public String imgUrl_yinying;
    public String cate;
    public String imgUrl;
    public String jumpUrl;
    public String litpic;
    public String arc_title;
    public String createdate;
    public String is_show;
    public String name;
    public String code;
    public String prvCode;
    public String ranks;
    public String area_cate;
    public String prvName;
    public String cityName;
    public String cityCode;
    public String nativeplace;
    public String suoshu_channelid;
    public String place_channelid;
    public WeatherData weatherData;
    public String description_num;

    public ADS(){}

    public ADS(String aid,String picUrl, String title,String pubdate) {
        this.picUrl = picUrl;
        this.title = title;
        this.aid=aid;
        this.pubdate=pubdate;

    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ADS{" +
                "aid='" + aid + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
