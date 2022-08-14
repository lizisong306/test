package entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 13520 on 2016/8/22.
 */
public class lunbo implements Serializable{
   private String[] content;
    public lunbo(){}

    public lunbo(String[] content) {
        this.content = content;
    }

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "lunbo{" +
                "content=" + Arrays.toString(content) +
                '}';
    }
}
