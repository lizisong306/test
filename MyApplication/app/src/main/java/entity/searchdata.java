package entity;

import java.io.Serializable;

/**
 * Created by 13520 on 2016/11/3.
 */
public class searchdata implements Serializable{
    private String description;
    private String id;
    private String keywords;
    private String litpic;
    private String pubdate;
    private String title;
    private String typeid;
    public  searchdata(){}

    public searchdata(String description, String id, String keywords, String litpic, String pubdate, String title, String typeid) {
        this.description = description;
        this.id = id;
        this.keywords = keywords;
        this.litpic = litpic;
        this.pubdate = pubdate;
        this.title = title;
        this.typeid = typeid;
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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
        return "searchdata{" +
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
