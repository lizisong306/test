package entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 13520 on 2016/8/22.
 */
public class Hjson implements Serializable{
    private List<lunbo>  ads;
    private cate category;
    private List<post> posts;
    private int count;
    private int pages;
    private String status;
    public Hjson(){}

    public Hjson(List<lunbo> ads, cate category, List<post> posts, int count, int pages, String status) {
        this.ads = ads;
        this.category = category;
        this.posts = posts;
        this.count = count;
        this.pages = pages;
        this.status = status;
    }

    public List<lunbo> getAds() {
        return ads;
    }

    public cate getCategory() {
        return category;
    }

    public List<post> getPosts() {
        return posts;
    }

    public int getCount() {
        return count;
    }

    public int getPages() {
        return pages;
    }

    public String getStatus() {
        return status;
    }

    public void setAds(List<lunbo> ads) {
        this.ads = ads;
    }

    public void setCategory(cate category) {
        this.category = category;
    }

    public void setPosts(List<post> posts) {
        this.posts = posts;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Hjson{" +
                "ads=" + ads +
                ", category=" + category +
                ", posts=" + posts +
                ", count=" + count +
                ", pages=" + pages +
                ", status='" + status + '\'' +
                '}';
    }
}
