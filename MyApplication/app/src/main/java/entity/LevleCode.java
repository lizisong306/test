package entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 13520 on 2016/11/24.
 */

public class LevleCode {

    private int code;
    private List<LevelData> data;
    public  LevleCode(){}

    public LevleCode(int code, List<LevelData> data) {
        this.code = code;
        this.data = data;
    }

    public List<LevelData> getData() {
        return data;
    }

    public void setData(List<LevelData> data) {
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
        return "LevleCode{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
