package entity;

import java.util.List;

/**
 * Created by 13520 on 2016/11/18.
 */

public class industrydata {
    public int id;
    public int evalue;
    private String ename;
    public String  typeid;
    public String province;
    public String category;
    public String udate;
    public List<Category> sonCate;
    public List<SelectType> select_type;
    public industrydata(){}

    public industrydata(int evalue, String ename, List<Category> sonCate) {
        this.evalue = evalue;
        this.ename = ename;
        this.sonCate = sonCate;
    }

    public int getEvalue() {
        return evalue;
    }

    public void setEvalue(int evalue) {
        this.evalue = evalue;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public List<Category> getSonCate() {
        return sonCate;
    }

    public void setSonCate(List<Category> sonCate) {
        this.sonCate = sonCate;
    }

    @Override
    public String toString() {
        return "industrydata{" +
                "evalue=" + evalue +
                ", ename='" + ename + '\'' +
                ", sonCate=" + sonCate +
                '}';
    }
}
