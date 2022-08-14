package entity;

/**
 * Created by 13520 on 2016/10/19.
 */
public class Registlog
{
    private String tel;
    private String pwd;
    private String uname;
    private String mid;

    public Registlog(){}

    public Registlog(String tel, String pwd, String uname) {
        this.tel = tel;
        this.pwd = pwd;
        this.uname = uname;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "Registlog{" +
                "tel='" + tel + '\'' +
                ", pwd='" + pwd + '\'' +
                ", uname='" + uname + '\'' +
                '}';
    }
}
