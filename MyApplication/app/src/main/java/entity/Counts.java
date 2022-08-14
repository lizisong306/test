package entity;

import java.io.Serializable;


public class Counts implements Serializable{
     private searchcount count;
    private searchresult result;
    public  Counts(){}

    public Counts(searchcount count, searchresult result) {
        this.count = count;
        this.result = result;
    }

    public searchcount getCount() {
        return count;
    }

    public void setCount(searchcount count) {
        this.count = count;
    }

    public searchresult getResult() {
        return result;
    }

    public void setResult(searchresult result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Counts{" +
                "count=" + count +
                ", result=" + result +
                '}';
    }
}
