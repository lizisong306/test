package entity;

import java.io.Serializable;

/**
 * Created by 13520 on 2016/11/4.
 */
public class Xmlist implements Serializable{
    private String description;
    private String id;
    private String keywords;
    private String litpic;
    private String pubdate;
    private String title;
    private String typeid;
    private String typename;
    private String zan;
    private String click;
    private Area_cate area_cate;//领域
    public  Xmlist(){}

    public Area_cate getArea_cate() {
        return area_cate;
    }

    public void setArea_cate(Area_cate area_cate) {
        this.area_cate = area_cate;
    }

    public Xmlist(String description, String id, String keywords, String litpic, String pubdate, String title, String typeid) {
        this.description = description;
        this.id = id;
        this.keywords = keywords;
        this.litpic = litpic;
        this.pubdate = pubdate;
        this.title = title;
        this.typeid = typeid;
    }

    public String getZan() {
        return zan;
    }

    public void setZan(String zan) {
        this.zan = zan;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    @Override
    public String toString() {
        return "Renlist{" +
                "description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", keywords='" + keywords + '\'' +
                ", litpic='" + litpic + '\'' +
                ", pubdate='" + pubdate + '\'' +
                ", title='" + title + '\'' +
                ", typeid='" + typeid + '\'' +
                '}';
    }
}
