package entity;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by 13520 on 2016/8/23.
 */
public class Filds implements Serializable{
    private String[] area1;
    private String[] area2;
    private String[] describe;
    private String[] title;
    private String[] type;

    public Filds(){}

    public Filds(String[] areal, String[] area2, String[] describe, String[] title, String[] type) {
        this.area1 = areal;
        this.area2 = area2;
        this.describe = describe;
        this.title = title;
        this.type = type;

    }

    public String[] getAreal() {
        return area1;
    }

    public void setAreal(String[] areal) {
        this.area1 = areal;
    }

    public String[] getArea2() {
        return area2;
    }

    public void setArea2(String[] area2) {
        this.area2 = area2;
    }

    public String[] getDescribe() {
        return describe;
    }

    public void setDescribe(String[] describe) {
        this.describe = describe;
    }

    public String[] getTitle() {
        return title;
    }

    public void setTitle(String[] title) {
        this.title = title;
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }



    @Override
    public String toString() {
        return "Filds{" +
                "areal=" + Arrays.toString(area1) +
                ", area2=" + Arrays.toString(area2) +
                ", describe=" + Arrays.toString(describe) +
                ", title=" + Arrays.toString(title) +
                ", type=" + Arrays.toString(type) +
                 '\'' +
                '}';
    }
}
