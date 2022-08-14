package entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 13520 on 2017/2/27.
 */

public class SpeData implements Serializable {
    public String id;
    public String title;
    public String typename;
    public String litpic;
    public String description;
    public List<Spec_list> special_list;

    public SpeData(){}

    public SpeData(String id, String title, String litpic, String typename, String description, List<Spec_list> special_list) {
        this.id = id;
        this.title = title;
        this.litpic = litpic;
        this.typename = typename;
        this.description = description;
        this.special_list = special_list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Spec_list> getSpecial_list() {
        return special_list;
    }

    public void setSpecial_list(List<Spec_list> special_list) {
        this.special_list = special_list;
    }

    @Override
    public String toString() {
        return "SpeData{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", typename='" + typename + '\'' +
                ", litpic='" + litpic + '\'' +
                ", description='" + description + '\'' +
                ", special_list=" + special_list +
                '}';
    }
}
