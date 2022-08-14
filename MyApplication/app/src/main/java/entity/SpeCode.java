package entity;

import java.io.Serializable;

/**
 * Created by 13520 on 2017/2/27.
 */

public class SpeCode implements Serializable {
    public  int code;
    public String message;
    public String LastModifiedTime;
    public SpeData data;
    public SpeCode(){}

    public SpeCode(int code, String message, String lastModifiedTime, SpeData data) {
        this.code = code;
        this.message = message;
        LastModifiedTime = lastModifiedTime;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLastModifiedTime() {
        return LastModifiedTime;
    }

    public void setLastModifiedTime(String lastModifiedTime) {
        LastModifiedTime = lastModifiedTime;
    }

    public SpeData getData() {
        return data;
    }

    public void setData(SpeData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SpeCode{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", LastModifiedTime='" + LastModifiedTime + '\'' +
                ", data=" + data +
                '}';
    }
}
