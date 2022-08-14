package entity;

import java.util.List;

/**
 * Created by 13520 on 2016/10/19.
 */
public class userlogin {
    public int state;
    private String mid;
    private String username;
    private String userpwd;
    private String nickname;
    private String logintime;
    private String loginFlag;
    private String loginip;
    public String mtype;
    public int patent_sq;
    public String linkman;
    public String userid;
    public String address;
    public String mobile;
    public String email;
    public String face;
    public String product;
    public String tel;
    public String vocation;
    public String company;
    public String img;
    public String wq_num;
    public int num;
    public String ctype; //单位类型
    public String profession;//职位
    public List<Interest> interestArea;//感兴趣的领域
    public String jointime;
    public String infor_new;
    public String require_new_0;
    public String require_new_4;
    public String require_new_2;
    public String require_new_7;
    public userlogin(){}

    public userlogin(String username, String userpwd, String nickname, String logintime, String loginFlag) {
        this.username = username;
        this.userpwd = userpwd;
        this.nickname = nickname;
        this.logintime = logintime;
        this.loginFlag = loginFlag;
    }

    public String getLoginip() {
        return loginip;
    }

    public void setLoginip(String loginip) {
        this.loginip = loginip;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    @Override
    public String toString() {
        return "userlogin{" +
                "username='" + username + '\'' +
                ", userpwd='" + userpwd + '\'' +
                ", nickname='" + nickname + '\'' +
                ", logintime='" + logintime + '\'' +
                ", loginFlag='" + loginFlag + '\'' +
                '}';
    }
}
