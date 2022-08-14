package entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 13520 on 2016/8/22.
 */
public class post implements Serializable{
   private String content;
    private Filds custom_fields;
    private String date;
    public post(){}

    public post(String content, Filds custom_fields, String date) {
        this.content = content;
        this.custom_fields = custom_fields;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Filds getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(Filds custom_fields) {
        this.custom_fields = custom_fields;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "post{" +
                "content='" + content + '\'' +
                ", custom_fields=" + custom_fields +
                ", date='" + date + '\'' +
                '}';
    }
}
