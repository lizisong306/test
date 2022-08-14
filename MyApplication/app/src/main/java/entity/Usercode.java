package entity;

/**
 * Created by 13520 on 2016/10/19.
 */
public class Usercode {
    private int  code;
    private String message;
    private userlogin data;
    public  Usercode(){};

    public Usercode(int code, String message, userlogin data) {
        this.code = code;
        this.message = message;
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

    public userlogin getData() {
        return data;
    }

    public void setData(userlogin data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Usercode{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
