package entity;

/**
 * Created by 13520 on 2016/9/8.
 */
public class checks {
    private String name;
    private int  img;

    public checks(){}
    public checks(int img, String name) {
        this.img = img;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int  getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "checks{" +
                "name='" + name + '\'' +
                ", img=" + img +
                '}';
    }
}
