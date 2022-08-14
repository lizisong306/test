package entity;

import java.io.Serializable;
import java.util.List;




public class searchresult implements Serializable {
    private List<Renlist> rencai;
    private List<Sblist> shebei;
    private List<Xmlist> xiangmu;
    private List<Zclist> zhengce;
    private List<Zllist> zhuanli;
    private List<Ztlist> zhuanti;
    private List<Zxlist> zixun;
    private List<Shiyanshi> shiyanshi;
    private List<Tuijian> tuijian;

    public searchresult(){}

    public searchresult(List<Renlist> rencai, List<Sblist> shebei, List<Xmlist> xiangmu, List<Zclist> zhengce, List<Zllist> zhuanli, List<Ztlist> zhuanti, List<Zxlist> zixun, List<Shiyanshi> shiyanshi) {
        this.rencai = rencai;
        this.shebei = shebei;
        this.xiangmu = xiangmu;
        this.zhengce = zhengce;
        this.zhuanli = zhuanli;
        this.zhuanti = zhuanti;
        this.zixun = zixun;
        this.shiyanshi = shiyanshi;
    }

    public List<Tuijian> getTuijian() {
        return tuijian;
    }

    public void setTuijian(List<Tuijian> tuijian) {
        this.tuijian = tuijian;
    }

    public List<Shiyanshi> getShiyanshi() {
        return shiyanshi;
    }

    public void setShiyanshi(List<Shiyanshi> shiyanshi) {
        this.shiyanshi = shiyanshi;
    }

    public List<Renlist> getRencai() {
        return rencai;
    }

    public void setRencai(List<Renlist> rencai) {
        this.rencai = rencai;
    }

    public List<Sblist> getShebei() {
        return shebei;
    }

    public void setShebei(List<Sblist> shebei) {
        this.shebei = shebei;
    }

    public List<Xmlist> getXiangmu() {
        return xiangmu;
    }

    public void setXiangmu(List<Xmlist> xiangmu) {
        this.xiangmu = xiangmu;
    }

    public List<Zclist> getZhengce() {
        return zhengce;
    }

    public void setZhengce(List<Zclist> zhengce) {
        this.zhengce = zhengce;
    }

    public List<Zllist> getZhuanli() {
        return zhuanli;
    }

    public void setZhuanli(List<Zllist> zhuanli) {
        this.zhuanli = zhuanli;
    }

    public List<Ztlist> getZhuanti() {
        return zhuanti;
    }

    public void setZhuanti(List<Ztlist> zhuanti) {
        this.zhuanti = zhuanti;
    }

    public List<Zxlist> getZixun() {
        return zixun;
    }

    public void setZixun(List<Zxlist> zixun) {
        this.zixun = zixun;
    }

    @Override
    public String toString() {
        return "searchresult{" +
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
