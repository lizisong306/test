package entity;

/**
 * Created by 13520 on 2016/11/19.
 */

public class Category {
    public String ename;
    public String evalue;
    public String isChoice;
    public  Category(){}

    public Category(String ename, String evalue) {
        this.ename = ename;
        this.evalue = evalue;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getEvalue() {
        return evalue;
    }

    public void setEvalue(String evalue) {
        this.evalue = evalue;
    }

    @Override
    public String toString() {
        return "Category{" +
                "ename='" + ename + '\'' +
                ", evalue='" + evalue + '\'' +
                '}';
    }
}
