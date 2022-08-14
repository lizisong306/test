package entity;

import java.io.Serializable;

/**
 * Created by 13520 on 2016/11/4.
 */
public class Rencai implements Serializable{
    public String num;
    public String typename;
    public Rencai(){}

    public Rencai(String num) {
        this.num = num;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Rencai{" +
                "num='" + num + '\'' +
                '}';
    }
}
