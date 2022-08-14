package entity;

import java.io.Serializable;

/**
 * Created by 13520 on 2016/12/23.
 */

public class Shiyanshi implements Serializable {

    private Area_cate area_cate;
    private String click;
    private String description;
    private String id;
    private String litpic;
    private String pubdate;
    private String title;
    private String typename;
    public String num;
    private String zan;
    public  Shiyanshi(){}

    public Shiyanshi(Area_cate area_cate, String click, String description, String id, String pubdate, String litpic, String title, String typename, String zan) {
        this.area_cate = area_cate;
        this.click = click;
        this.description = description;
        this.id = id;
        this.pubdate = pubdate;
        this.litpic = litpic;
        this.title = title;
        this.typename = typename;
        this.zan = zan;
    }

    public Area_cate getArea_cate() {
        return area_cate;
    }

    public void setArea_cate(Area_cate area_cate) {
        this.area_cate = area_cate;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getZan() {
        return zan;
    }

    public void setZan(String zan) {
        this.zan = zan;
    }
}
