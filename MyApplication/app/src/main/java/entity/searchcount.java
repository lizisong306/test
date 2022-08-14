package entity;

import java.io.Serializable;

/**
 * Created by 13520 on 2016/11/4.
 */
public class searchcount implements Serializable {
    private Rencai rencai;
    private Shebei shebei;
    private Xiangmu xiangmu;
    private Zhengce zhengce;
    private Zhuanli zhuanli;
    private Zhuanti zhuanti;
    private Zixun zixun;
    public Shiyanshi shiyanshi;
    public searchcount(){}

    public searchcount(Rencai rencai, Shebei shebei, Xiangmu xiangmu, Zhengce zhengce, Zhuanli zhuanli, Zhuanti zhuanti, Zixun zixun) {
        this.rencai = rencai;
        this.shebei = shebei;
        this.xiangmu = xiangmu;
        this.zhengce = zhengce;
        this.zhuanli = zhuanli;
        this.zhuanti = zhuanti;
        this.zixun = zixun;
    }

    public Rencai getRencai() {
        return rencai;
    }

    public void setRencai(Rencai rencai) {
        this.rencai = rencai;
    }

    public Shebei getShebei() {
        return shebei;
    }

    public void setShebei(Shebei shebei) {
        this.shebei = shebei;
    }

    public Xiangmu getXiangmu() {
        return xiangmu;
    }

    public void setXiangmu(Xiangmu xiangmu) {
        this.xiangmu = xiangmu;
    }

    public Zhengce getZhengce() {
        return zhengce;
    }

    public void setZhengce(Zhengce zhengce) {
        this.zhengce = zhengce;
    }

    public Zhuanli getZhuanli() {
        return zhuanli;
    }

    public void setZhuanli(Zhuanli zhuanli) {
        this.zhuanli = zhuanli;
    }

    public Zhuanti getZhuanti() {
        return zhuanti;
    }

    public void setZhuanti(Zhuanti zhuanti) {
        this.zhuanti = zhuanti;
    }

    public Zixun getZixun() {
        return zixun;
    }

    public void setZixun(Zixun zixun) {
        this.zixun = zixun;
    }

    @Override
    public String toString() {
        return "searchcount{" +
                "rencai=" + rencai +
                ", shebei=" + shebei +
                ", xiangmu=" + xiangmu +
                ", zhengce=" + zhengce +
                ", zhuanli=" + zhuanli +
                ", zhuanti=" + zhuanti +
                ", zixun=" + zixun +
                '}';
    }
}
