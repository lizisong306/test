package entity;

/**
 * Created by 13520 on 2016/8/25.
 */
public class Url {
    private String url;
    public Url(){}

    public Url(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Url{" +
                "url='" + url + '\'' +
                '}';
    }
}
