package entity;

import java.io.Serializable;

/**
 * Created by 13520 on 2017/2/27.
 */

public class spec_post implements Serializable {

    public String notename;
    public String id;
    public String title;
    public String typename;
    public String litpic;
    public String source;
    public String description;
    public String click;
    public String zan;
    public String pubdate;
    public Boolean state;
    public  String unit;
    public String username;
    public String rank;
    public Area_cate area_cate;
    public String imageState; //图片对应数量
    public ImageState image;

    public spec_post(){}

    public spec_post(String id, String title, String litpic, String typename, String source, String description, String click, String zan, String pubdate) {
        this.id = id;
        this.title = title;
        this.litpic = litpic;
        this.typename = typename;
        this.source = source;
        this.description = description;
        this.click = click;
        this.zan = zan;
        this.pubdate = pubdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getZan() {
        return zan;
    }

    public void setZan(String zan) {
        this.zan = zan;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }
}
