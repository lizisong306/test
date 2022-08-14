package entity;

import java.util.List;

/**
 * Created by 13520 on 2016/11/18.
 */

public class industryCode {

    private int code;
    private List<industrydata> data;

    public industryCode(){}

    public industryCode(List<industrydata> data, int code) {
        this.data = data;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<industrydata> getData() {
        return data;
    }

    public void setData(List<industrydata> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "industryCode{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
