package entity;

import java.util.List;

/**
 * Created by 13520 on 2016/11/24.
 */
public class LevelCate {
    private List<LevelCount> count;
    private String ename;
    private int evalue;
    public  LevelCate(){}

    public LevelCate(List<LevelCount> count) {
        this.count = count;
    }

    public List<LevelCount> getCount() {
        return count;
    }

    public void setCount(List<LevelCount> count) {
        this.count = count;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public int getEvalue() {
        return evalue;
    }

    public void setEvalue(int evalue) {
        this.evalue = evalue;
    }

    @Override
    public String toString() {
        return "LevelCate{" +
                "count=" + count +
                '}';
    }
}
