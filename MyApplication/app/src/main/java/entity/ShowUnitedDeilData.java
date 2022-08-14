package entity;

/**
 * Created by lizisong on 2018/1/15.
 */

public class ShowUnitedDeilData {
    public String id;//aid,pos数据
    public String aid;
    public String description;//详情
    public String litpic;//title图片
    public String title;//标题
    public String typename;//区分类别
    public String typeid;
    public String pubdate;//服务器上传数据的时间
    public String result="";//分页滚动的时候，用来判断是否还有数据
    public String channel;//判断是那个频道的数据
    public String username;//人才频道，标记人名字的
    public String body;//是人才频道用来描述人才信息的
    public String rank;//是那个方面的专家
    public Area_cate area_cate;//领域
    public String source; //来源
    public  String sortTime;//下拉和上拉刷新时间
    public String labels;
    public String tags="";//置顶和推荐
    public String zan;//点赞
    public String click;//关注
    public String unit; //发布
    public String url;
    public String state;//所处阶段
    public String spec; //型号
    public String imageState; //图片对应数量
    public ImageState image;
    public String is_academician;
    public int type;
    public boolean isshow;
    public boolean isshowmore;

    public String name;
    public String names;
    public String content;
}
