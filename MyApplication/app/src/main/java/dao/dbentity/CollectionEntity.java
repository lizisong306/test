package dao.dbentity;

import entity.Area_cate;
import entity.ImageState;

/**
 * Created by lizisong on 2016/12/5.
 */

public class CollectionEntity {
    public int id;//主键ID
    public String type;//用来记录数据字段
    public String title;//标题
    public int upFlag;//是否上传0：未上传；1：以上传
    public String aid;//详情页面id
    public String updateTime;//数据更新时间
    public String pic;//人物简介图
    public  String iscollect; //区分收藏和关注
    public int isAdd;//-1没有加过数据；0收藏；1关注；2收藏关注都加
    public String pid;

    public String is_academician;
    public String description;
    public String imageState; //图片对应数量
    public ImageState image;
    public  String  area_cate;
    public String zan;//点赞
    public String click;//关注
    public String getIs_academician() {
        return is_academician;
    }

    public void setIs_academician(String is_academician) {
        this.is_academician = is_academician;
    }

    public String getImageState() {
        return imageState;
    }

    public void setImageState(String imageState) {
        this.imageState = imageState;
    }

    public ImageState getImage() {
        return image;
    }

    public void setImage(ImageState image) {
        this.image = image;
    }
}
