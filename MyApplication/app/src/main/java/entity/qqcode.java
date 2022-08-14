package entity;

/**
 * Created by 13520 on 2016/10/25.
 */
public class qqcode {
    private int code;
    private qqDate data;
    private String message;
    public qqcode(){}
    public qqcode(int code, qqDate data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public qqDate getData() {
        return data;
    }

    public void setData(qqDate data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "qqcode{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
