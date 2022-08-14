package entity;

/**
 * Created by lizisong on 2017/1/13.
 */

public class YuJian {
    public String des;
    public String meetaddress;
    public String meetdate;
    public String meetname;
    public String meettheme;
    public String mobile;
    public String typeid;
    public String addtime;
    public String id;//aid,pos数据
    public String aid;
    public String description;//详情
    public String litpic;//title图片
    public String title;//标题
    public String typename;//区分类别
    public String pubdate;//服务器上传数据的时间
    public String result="";//分页滚动的时候，用来判断是否还有数据
    public String channel;//判断是那个频道的数据
    public String username;//人才频道，标记人名字的
    public String body;//是人才频道用来描述人才信息的
    public String rank;//是那个方面的专家
    public Area_cate area_cate;//领域
    public String source; //来源
    public  String sortTime;//下拉和上拉刷新时间

    public String tags="";//置顶和推荐
    public String zan;//点赞
    public String click;//关注
    public String unit; //发布
    public String state;//所处阶段
    public String spec; //型号
    public String imageState; //图片对应数量
    public ImageState image;
    public int imageid;
    public String is_academician;


}
