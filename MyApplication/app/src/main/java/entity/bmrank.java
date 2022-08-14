package entity;

import java.io.Serializable;

/**
 * Created by 13520 on 2016/11/16.
 */

public class bmrank implements Serializable{
    private int img;
    private String name;
    public boolean flag;
    private boolean isChecked=false;
    private int eavule;

    public bmrank(){

    }

    public bmrank(int img, String name,int eavule) {
        this.img = img;
        this.name = name;
        this.eavule=eavule;
    }

    public int getEavule() {
        return eavule;
    }

    public void setEavule(int eavule) {
        this.eavule = eavule;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "bmrank{" +
                "img=" + img +
                ", name='" + name + '\'' +
                '}';
    }
}
