package entity;

import java.util.List;

/**
 * Created by 13520 on 2016/11/3.
 */
public class hotData {
  private List<hotwords> hotWords;
    public hotData(){}

    public hotData(List<hotwords> hotWords) {
        this.hotWords = hotWords;
    }

    public List<hotwords> getHotWords() {
        return hotWords;
    }

    public void setHotWords(List<hotwords> hotWords) {
        this.hotWords = hotWords;
    }

    @Override
    public String toString() {
        return "hotData{" +
                "hotWords=" + hotWords +
                '}';
    }
}
