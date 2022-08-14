package entity;

/**
 * Created by 13520 on 2016/9/26.
 */
public class Details {
    private recode data;
    private int code;
    public String message;
    public Details(){}

    public Details(int code, recode data) {
        this.code = code;
        this.data = data;
    }

    public recode getData() {
        return data;
    }

    public void setData(recode data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Details{" +
                "data=" + data +
                ", code=" + code +
                '}';
    }
}
