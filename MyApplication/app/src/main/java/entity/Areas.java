package entity;

/**
 * Created by 13520 on 2016/10/14.
 */
public class Areas {
    private String area_cate1;
    private String area_cate2;
    private String area_cate3;

    public Areas(String area_cate1, String area_cate2, String area_cate3) {
        this.area_cate1 = area_cate1;
        this.area_cate2 = area_cate2;
        this.area_cate3 = area_cate3;
    }
    public Areas(){}

    public String getArea_cate1() {
        return area_cate1;
    }

    public void setArea_cate1(String area_cate1) {
        this.area_cate1 = area_cate1;
    }

    public String getArea_cate2() {
        return area_cate2;
    }

    public void setArea_cate2(String area_cate2) {
        this.area_cate2 = area_cate2;
    }

    public String getArea_cate3() {
        return area_cate3;
    }

    public void setArea_cate3(String area_cate3) {
        this.area_cate3 = area_cate3;
    }
}
