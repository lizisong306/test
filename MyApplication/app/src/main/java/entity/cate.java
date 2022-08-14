package entity;

import java.io.Serializable;

/**
 * Created by 13520 on 2016/8/22.
 */
public class cate implements Serializable{
    private String title;
    public cate(){}
    public cate(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "cate{" +
                "title='" + title + '\'' +
                '}';
    }
}
