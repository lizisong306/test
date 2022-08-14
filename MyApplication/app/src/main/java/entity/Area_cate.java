package entity;

import java.io.Serializable;

/**
 * Created by 13520 on 2016/12/15.
 */

public class Area_cate implements Serializable{
    private  String area_cate1="";
    public  Area_cate(){}

    public Area_cate(String area_cate1) {
        this.area_cate1 = area_cate1;
    }

    public String getArea_cate1() {
        return area_cate1;
    }

    public void setArea_cate1(String area_cate1) {
        this.area_cate1 = area_cate1;
    }
}
