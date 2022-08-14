package entity;

/**
 * Created by 13520 on 2016/10/25.
 */
public class qqDate {
    public String id;
    public String nickname;
    public String email;
    public String img;
    public String loginFlag;
    public String loginip;
    public String logintime;
    public String mid;
    public String mtype;
    public String tel;
    public String username;
   public  qqDate(){}

    public qqDate(String id, String loginFlag, String logintime, String nickname, String username) {
        this.id = id;
        this.loginFlag = loginFlag;
        this.logintime = logintime;
        this.nickname = nickname;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "qqDate{" +
                "id='" + id + '\'' +
                ", loginFlag='" + loginFlag + '\'' +
                ", logintime='" + logintime + '\'' +
                ", nickname='" + nickname + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
