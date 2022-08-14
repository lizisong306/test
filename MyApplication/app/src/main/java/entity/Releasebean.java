package entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by 13520 on 2016/9/20.
 */
public class Releasebean  implements Serializable{
    private  int id;
    private String trim;
    private String realease;
    private String introduction;
    private String bitmap;
public Releasebean(){}
    public Releasebean(String trim, String realease, String introduction,String bitmap) {
        this.trim = trim;
        this.realease = realease;
        this.introduction = introduction;
        this.bitmap=bitmap;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrim() {
        return trim;
    }

    public void setTrim(String trim) {
        this.trim = trim;
    }

    public String getRealease() {
        return realease;
    }

    public void setRealease(String realease) {
        this.realease = realease;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getBitmap() {
        return bitmap;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "Releasebean{" +
                "trim='" + trim + '\'' +
                ", realease='" + realease + '\'' +
                ", introduction='" + introduction + '\'' +
                ", bitmap=" + bitmap +
                '}';
    }
}
