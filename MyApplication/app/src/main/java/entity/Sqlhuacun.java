package entity;

import android.graphics.Bitmap;

/**
 * Created by 13520 on 2016/9/27.
 */
public class Sqlhuacun {
    private int id;
   private String sql_pic;
    private String lunbo_title;


    public Sqlhuacun(){

    }

    public Sqlhuacun(String sql_pic,String lunbo_title) {


        this.lunbo_title = lunbo_title;

        this.sql_pic = sql_pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getLunbo_title() {
        return lunbo_title;
    }

    public void setLunbo_title(String lunbo_title) {
        this.lunbo_title = lunbo_title;
    }


    public String getSql_pic() {
        return sql_pic;
    }

    public void setSql_pic(String sql_pic) {
        this.sql_pic = sql_pic;
    }

    @Override
    public String toString() {
        return "Sqlhuacun{" +
                "id=" + id +
                ", sql_pic='" + sql_pic + '\'' +
                ", lunbo_title='" + lunbo_title + '\'' +
                '}';
    }
}
