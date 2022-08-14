package entity;

/**
 * Created by 13520 on 2016/11/3.
 */
public class hotwords {
    private int id;
    private String keyword;
    public  hotwords(){}

    public hotwords(int id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "hotwords{" +
                "id=" + id +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
