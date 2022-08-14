package entity;

/**
 * Created by 13520 on 2016/11/24.
 */
public class LevelCount {
    private String number;
    private String typeid;
    private String typename;
    public LevelCount(){}

    public LevelCount(String number, String typeid, String typename) {
        this.number = number;
        this.typeid = typeid;
        this.typename = typename;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    @Override
    public String toString() {
        return "LevelCount{" +
                "number='" + number + '\'' +
                ", typeid='" + typeid + '\'' +
                ", typename='" + typename + '\'' +
                '}';
    }
}
