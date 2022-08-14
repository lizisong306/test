package entity;

import java.util.List;

/**
 * Created by 13520 on 2016/9/23.
 */
public class Datas {
    private List<ADS> adPic;
    private List<ADS> ads;
    private  List<Posts>posts;
    public String mid;
    public String mtype;
    public String userid;
    public String uname;
    public String tel;
    public String email;
    public String face;
    public String nickname;
    public String logintime;
    public  String username;
    public  String company;
    public String leaderette;

    public String unit;
    public String address;
    public String linkman;
    public String mobile;
    public String product;
   public  String vocation;
    public String img;
    public String count;

    public Datas(){}

    public Datas(List<ADS> ads, List<Posts> posts) {
        this.ads = ads;
        this.posts = posts;
    }

    public List<ADS> getAdPic() {
        return adPic;
    }

    public void setAdPic(List<ADS> adPic) {
        this.adPic = adPic;
    }

    public List<ADS> getAds() {
        return ads;
    }

    public void setAds(List<ADS> ads) {
        this.ads = ads;
    }

    public List<Posts> getPosts() {
        return posts;
    }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }


    @Override
    public String toString() {
        return "Datas{" +
                "adPic=" + adPic +
                ", ads=" + ads +
                ", posts=" + posts +
                ", mid='" + mid + '\'' +
                ", mtype='" + mtype + '\'' +
                ", userid='" + userid + '\'' +
                ", uname='" + uname + '\'' +
                ", tel='" + tel + '\'' +
                ", email='" + email + '\'' +
                ", face='" + face + '\'' +
                ", logintime='" + logintime + '\'' +
                ", username='" + username + '\'' +
                ", company='" + company + '\'' +
                ", address='" + address + '\'' +
                ", linkman='" + linkman + '\'' +
                ", mobile='" + mobile + '\'' +
                ", product='" + product + '\'' +
                ", vocation='" + vocation + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
