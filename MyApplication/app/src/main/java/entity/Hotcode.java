package entity;

import java.util.List;

/**
 * Created by 13520 on 2016/11/3.
 */
public class Hotcode {
    private int code;
    private List<hotwords> data;

    public Hotcode(int code, List<hotwords> data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<hotwords> getData() {
        return data;
    }

    public void setData(List<hotwords> data) {
        this.data = data;
    }
}
