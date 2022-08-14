package entity;

import java.util.List;

/**
 * Created by 13520 on 2016/11/24.
 */
public class LevelData {
    private String ename;
    private int evalue;
    private List<LevelCate> sonCate;
    public  LevelData(){}

    public LevelData(String ename, int evalue, List<LevelCate> sonCate) {
        this.ename = ename;
        this.evalue = evalue;
        this.sonCate = sonCate;
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

    public List<LevelCate> getSonCate() {
        return sonCate;
    }

    public void setSonCate(List<LevelCate> sonCate) {
        this.sonCate = sonCate;
    }

    @Override
    public String toString() {
        return "LevelData{" +
                "ename='" + ename + '\'' +
                ", evalue=" + evalue +
                ", sonCate=" + sonCate +
                '}';
    }
}
