package entity;

/**
 * Created by 13520 on 2016/9/23.
 */
public class Codes {
    private String LastModifiedTime;
    public String message;
    private Datas data;
    public int code;
    public String img;
    public  Codes(){

    }
    public Codes(String lastModifiedTime, Datas data) {
        LastModifiedTime = lastModifiedTime;
        this.data = data;
    }

    public String getLastModifiedTime() {
        return LastModifiedTime;
    }

    public void setLastModifiedTime(String lastModifiedTime) {
        LastModifiedTime = lastModifiedTime;
    }

    public Datas getData() {
        return data;
    }

    public void setData(Datas data) {
        this.data = data;
    }

}
