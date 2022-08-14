package entity;

/**
 * Created by 13520 on 2017/6/12.
 */

public class city_index {
    public String name;
    public String value;

    public city_index(String name, String value) {
        this.name = name;
        this.value = value;
    }
    public city_index(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "city_index{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
