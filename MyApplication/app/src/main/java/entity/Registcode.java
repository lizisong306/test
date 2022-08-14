package entity;

/**
 * Created by 13520 on 2016/10/19.
 */
public class Registcode {
    private int code;
    private Registlog data;
    public Registcode(){}

    public Registcode(int code, Registlog data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Registlog getData() {
        return data;
    }

    public void setData(Registlog data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Registcode{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
