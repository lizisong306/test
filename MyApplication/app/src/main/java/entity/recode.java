package entity;

import java.util.List;

/**
 * Created by 13520 on 2016/9/26.
 */
public class recode {
    private String body;
    private String id;
    private String typeid;
    private String source;
    private String title;
    private String litpic;

    private String pubdate;

    private String writer;
    private Areas area;

    private String post_title;
    private String rank;
    public String reward;
    public Area_cate area_cate;

    public String opus;

    private String study_area;
    private String username;
    private String spec;
    private String factory;
    private String country;
    private String performance;
    private String functional;
    private String buy_date;
    private String price;
    private String device_cate;
    private String unit;
    private String laboratory_research;
    private String operator;
    private String charge_standard;
    private String stage;
    private String cooperation;
    private String keywords;
    //人才
    private  String research_project;
    private String lab_type;
    private String department;

    public List<Interation> interpretation;
    public List<Interation> tech_resources;
    public String leaderette;
    private String is_academician;
    public String click;
    public String description;
    public String imageState; //图片对应数量
    public ImageState image;


    public Area_cate getArea_cate() {
        return area_cate;
    }

    public void setArea_cate(Area_cate area_cate) {
        this.area_cate = area_cate;
    }

    public recode(){}

    public String getLab_type() {
        return lab_type;
    }

    public void setLab_type(String lab_type) {
        this.lab_type = lab_type;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public recode(String body, String id, String litpic, String pubdate, String title, String writer, Areas area, String typeid, String post_title, String rank, String reward, String source, String study_area, String username) {
        this.body = body;
        this.id = id;
        this.litpic = litpic;
        this.pubdate = pubdate;
        this.title = title;
        this.writer = writer;
        this.area = area;
        this.typeid = typeid;
        this.post_title = post_title;
        this.rank = rank;
        this.reward = reward;
        this.source = source;
        this.study_area = study_area;
        this.username = username;
    }

    public List<Interation> getInterpretation() {
        return interpretation;
    }

    public void setInterpretation(List<Interation> interpretation) {
        this.interpretation = interpretation;
    }

    public String getLeaderette() {
        return leaderette;
    }

    public void setLeaderette(String leaderette) {
        this.leaderette = leaderette;
    }

    public String getIs_academician() {
        return is_academician;
    }

    public void setIs_academician(String is_academician) {
        this.is_academician = is_academician;
    }

    public String getResearch_project() {
        return research_project;
    }

    public void setResearch_project(String research_project) {
        this.research_project = research_project;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getCooperation() {
        return cooperation;
    }

    public void setCooperation(String cooperation) {
        this.cooperation = cooperation;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getFunctional() {
        return functional;
    }

    public void setFunctional(String functional) {
        this.functional = functional;
    }

    public String getBuy_date() {
        return buy_date;
    }

    public void setBuy_date(String buy_date) {
        this.buy_date = buy_date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDevice_cate() {
        return device_cate;
    }

    public void setDevice_cate(String device_cate) {
        this.device_cate = device_cate;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLaboratory_research() {
        return laboratory_research;
    }

    public void setLaboratory_research(String laboratory_research) {
        this.laboratory_research = laboratory_research;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCharge_standard() {
        return charge_standard;
    }

    public void setCharge_standard(String charge_standard) {
        this.charge_standard = charge_standard;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStudy_area() {
        return study_area;
    }

    public void setStudy_area(String study_area) {
        this.study_area = study_area;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public Areas getArea() {
        return area;
    }

    public void setArea(Areas area) {
        this.area = area;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    @Override
    public String toString() {
        return "recode{" +
                "body='" + body + '\'' +
                ", id='" + id + '\'' +
                ", litpic='" + litpic + '\'' +
                ", pubdate='" + pubdate + '\'' +
                ", title='" + title + '\'' +
                ", writer='" + writer + '\'' +
                '}';
    }
}
