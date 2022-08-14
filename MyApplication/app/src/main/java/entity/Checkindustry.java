package entity;

/**
 * Created by 13520 on 2016/11/21.
 */

public class Checkindustry {
    private String nmae;
    private String title;
    public  Checkindustry(){}

    public Checkindustry(String nmae, String title) {
        this.nmae = nmae;
        this.title = title;
    }

    public String getNmae() {
        return nmae;
    }

    public void setNmae(String nmae) {
        this.nmae = nmae;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Checkindustry{" +
                "nmae='" + nmae + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
