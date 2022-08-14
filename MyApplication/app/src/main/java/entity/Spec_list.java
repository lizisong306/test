package entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 13520 on 2017/2/27.
 */

public class Spec_list implements Serializable {
    public String notename;
    public List<spec_post> posts;
    public Spec_list(){}

    public Spec_list(String notename, List<spec_post> posts) {
        this.notename = notename;
        this.posts = posts;
    }

    public String getNotename() {
        return notename;
    }

    public void setNotename(String notename) {
        this.notename = notename;
    }

    public List<spec_post> getPosts() {
        return posts;
    }

    public void setPosts(List<spec_post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "Spec_list{" +
                "notename='" + notename + '\'' +
                ", posts=" + posts +
                '}';
    }
}
