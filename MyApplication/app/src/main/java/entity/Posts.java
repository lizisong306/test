package entity;

/**
 * Created by 13520 on 2016/9/23.
 */
public class Posts {
    private String id;//aid,pos数据
    public String aid;
    public String description;//详情
    public String litpic;//title图片
    public String title;//标题
    public String typename;//区分类别
    public String typeid;
    private String pubdate;//服务器上传数据的时间
    private String result="";//分页滚动的时候，用来判断是否还有数据
    private String channel;//判断是那个频道的数据
    private String username;//人才频道，标记人名字的
    private String body;//是人才频道用来描述人才信息的
    public String rank;//是那个方面的专家
    private Area_cate area_cate;//领域
    public String areacate;
    public String source; //来源
    private  String sortTime;//下拉和上拉刷新时间
    public String detail;
    private String tags="";//置顶和推荐
    private String zan;//点赞
    private String click;//关注
    public String unit; //发布
    private String state;//所处阶段
    private String spec; //型号
    public String imageState; //图片对应数量
    public ImageState image;
    public String region_text;
    public int imageid;
    public String original_price;
    public String current_price;
    public String str;
    public String url;
    private String is_academician;
    public String ranks;
    public String labels;

    public int xianstate;


    public  Posts(){

    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getIs_academician() {
        return is_academician;
    }

    public void setIs_academician(String is_academician) {
        this.is_academician = is_academician;
    }

    public String getImageState() {
        return imageState;
    }

    public void setImageState(String imageState) {
        this.imageState = imageState;
    }

    public ImageState getImage() {
        return image;
    }

    public void setImage(ImageState image) {
        this.image = image;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getZan() {
        return zan;
    }

    public void setZan(String zan) {
        this.zan = zan;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSortTime() {
        return sortTime;
    }

    public void setSortTime(String sortTime) {
        this.sortTime = sortTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Area_cate getArea_cate() {
        return area_cate;
    }

    public void setArea_cate(Area_cate area_cate) {
        this.area_cate = area_cate;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Posts(String id, String litpic, String title, String pubdate,String description,String source) {
        this.id=id;
        this.litpic = litpic;
        this.title = title;
        this.pubdate =pubdate;
        this.description=description;
        this.source=source;
        this.zan=zan;
        this.click=click;


    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLitpic() {
        return litpic;
    }

    public void setLitpic(String litpic) {
        this.litpic = litpic;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "id='" + id + '\'' +
                ", litpic='" + litpic + '\'' +
                ", title='" + title + '\'' +
                ", typename='" + typename + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }


}
