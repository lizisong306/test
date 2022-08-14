package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import entity.ADS;
import entity.ImageState;
import entity.Posts;

/**
 * Created by 13520 on 2016/9/27.
 */
public class Sqlitetions {
    private SQLiteDatabase dbs;
    private List<ADS> list=new ArrayList<>();
    private  List<Posts> posts=new ArrayList<>();
    private String pubdate;
    private static Sqlitetions singleton = null;
    private SQLiteDatabase wsd = null;
    private SQLiteDatabase rsd = null;
    private static  Context mContext = null;
    private Sqlitetions() {
    }
    public static Sqlitetions getInstance(Context context) {
        mContext = context;
        if (singleton == null) {
            synchronized (Sqlitetions.class) {
                if (singleton == null) {
                    Cpllecthelp databaseHelper = Cpllecthelp.getInstance(context);
                    singleton = new Sqlitetions();
                    singleton.wsd = databaseHelper.getWritableDatabase();
                    singleton.rsd = databaseHelper.getReadableDatabase();
                }
            }
        }
        return singleton;
    }
    public void guanrsd(){
        rsd.close();
    }
    public void search_add(String sousuoid){
        ContentValues values=new ContentValues();
        values.put("text", sousuoid);
        rsd.replace("sql_searchhistory", null, values);
       // rsd.close();
    }
    public void delete(int id){
        rsd.delete("sql_searchhistory", "id=?", new String[]{id+""});
    }
    private List<String> search_list;
    public List<String> findall(){
        search_list=new ArrayList<>();
        Cursor  cursor = rsd.rawQuery("select * from sql_searchhistory group by text  order by id desc limit 3",null);
       // search_list.clear();
        while(cursor.moveToNext()){
            // s=new searchss();
            String cname=cursor.getString(cursor.getColumnIndex("text"));
            cursor.getInt(cursor.getColumnIndex("id"));
            search_list.add(cname);
        }
        cursor.close();
       // rsd.close();
        return search_list;
    }
//    sql_maidiantj
public void collect_tj(ADS s){
    // 一样的  先查询
     /* ADS olds= collect_queryByid(s.getAid());
        if(olds==null){*/
    rsd= Cpllecthelp.getInstance(mContext).getWritableDatabase();
    ContentValues values=new ContentValues();
    values.put("id",s.getAid());
    values.put("lunbo_pic",s.getPicUrl());
    values.put("lunbo_title",s.getTitle());
    values.put("lunbo_pubdate",s.getPubdate());
    rsd.replace("sql_maidiantj", null, values);
    // rsd.close();
}
    public void difang_collect(ADS s){
        // 一样的  先查询
     /* ADS olds= collect_queryByid(s.getAid());
        if(olds==null){*/
        rsd= Cpllecthelp.getInstance(mContext).getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("id",s.getAid());
        values.put("lunbo_pic",s.getPicUrl());
        values.put("lunbo_title",s.getTitle());
        values.put("lunbo_pubdate",s.getPubdate());
        rsd.replace("sql_dianfanglunbo", null, values);
        // rsd.close();
    }
    List<ADS> tjlist;
    public List<ADS> tjfindalls(){
        tjlist=new ArrayList<>();
        String sql="select * from sql_maidiantj order by id asc limit 3";
        rsd= Cpllecthelp.getInstance(mContext).getReadableDatabase();
        Cursor cursor = rsd.rawQuery(sql, null);
        // xmlist.clear();
        ADS s=null;
        while(cursor.moveToNext()){
            s=new ADS();
            s.setPicUrl(cursor.getString(cursor.getColumnIndex("lunbo_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("lunbo_title")));
            s.setAid(cursor.getString(cursor.getColumnIndex("id")));
            s.setPubdate(cursor.getString(cursor.getColumnIndex("lunbo_pubdate")));
            tjlist.add(s);

        }
        cursor.close();
        //  rsd.close();
        return tjlist;
    }
    List<ADS> dflist;
    public List<ADS> dffindalls(){
        dflist=new ArrayList<>();
        String sql="select * from sql_dianfanglunbo order by id asc limit 3";
        rsd= Cpllecthelp.getInstance(mContext).getReadableDatabase();
        Cursor cursor = rsd.rawQuery(sql, null);
        // xmlist.clear();
        ADS s=null;
        while(cursor.moveToNext()){
            s=new ADS();
            s.setPicUrl(cursor.getString(cursor.getColumnIndex("lunbo_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("lunbo_title")));
            s.setAid(cursor.getString(cursor.getColumnIndex("id")));
            s.setPubdate(cursor.getString(cursor.getColumnIndex("lunbo_pubdate")));
            dflist.add(s);
        }
        cursor.close();
        //  rsd.close();
        return dflist;
    }

    public void collect_adds(ADS s){
        // 一样的  先查询
     /* ADS olds= collect_queryByid(s.getAid());
        if(olds==null){*/
        rsd =  Cpllecthelp.getInstance(mContext).getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("id",s.getAid());
        values.put("lunbo_pic",s.getPicUrl());
        values.put("lunbo_title",s.getTitle());
        values.put("lunbo_pubdate",s.getPubdate());
        rsd.replace("sql_maidianlunbo", null, values);
       // rsd.close();
    }

    public void tuijian_adds(Posts p){
        rsd =  Cpllecthelp.getInstance(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", p.getId());
        values.put("tuijian_pic", p.getLitpic());
        values.put("tuijian_title", p.getTitle());
        values.put("tuijian_pubdate",p.getSortTime());
        values.put("tuijian_result",p.getResult());
        values.put("channel",p.getChannel());
        values.put("zx_descript",p.getDescription());
        values.put("zx_souse",p.getSource());
        values.put("zx_zan",p.getZan());
        values.put("zx_look",p.getClick());
        values.put("zx_tags",p.getTags());
        values.put("zx_typename",p.getTypename());
        values.put("zx_img1",p.image.image1);
        values.put("zx_img2",p.image.image2);
        values.put("zx_img3",p.image.image3);
        values.put("zx_imagestate",p.imageState);
        rsd.replace("sql_maidiantuijian", null, values);
       // rsd.close();
    }
    public void df_adds(Posts p){
        rsd =  Cpllecthelp.getInstance(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", p.getId());
        values.put("tj_pic", p.getLitpic());
        values.put("tj_title", p.getTitle());
        values.put("tj_pubdate",p.getPubdate());
        values.put("tj_result",p.getResult());
        values.put("tj_channel",p.getChannel());
        values.put("tj_descript",p.getDescription());
        values.put("tj_souse",p.getSource());
        values.put("tj_img1",p.image.image1);
        values.put("tj_img2",p.image.image2);
        values.put("tj_img3",p.image.image3);
        values.put("tj_imagestate",p.imageState);
        values.put("tj_zan",p.getZan());
        values.put("tj_look",p.getClick());
        values.put("tj_tags",p.getTags());
        values.put("tj_unit",p.getUnit());
        values.put("tj_state",p.getState());
        values.put("tj_typename",p.getTypename());
        values.put("tj_sortname",p.getSortTime());
        rsd.replace("sql_dianfang", null, values);
        // rsd.close();
    }

    public void kejiku_adds(Posts p){
        rsd =  Cpllecthelp.getInstance(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", p.getId());
        values.put("tj_pic", p.getLitpic());
        values.put("tj_title", p.getTitle());
        values.put("tj_pubdate",p.getPubdate());
        values.put("tj_result",p.getResult());
        values.put("tj_channel",p.getChannel());
        values.put("tj_descript",p.getDescription());
        values.put("tj_souse",p.getSource());

        values.put("tj_img1",p.image.image1);
        values.put("tj_img2",p.image.image2);
        values.put("tj_img3",p.image.image3);
        values.put("tj_imagestate",p.imageState);
        values.put("tj_zan",p.getZan());
        values.put("tj_look",p.getClick());
        values.put("tj_tags",p.getTags());
        values.put("tj_unit",p.getUnit());
        values.put("tj_state",p.getState());
        values.put("tj_typename",p.getTypename());
        values.put("tj_sortname",p.getSortTime());
        rsd.replace("sql_kejiku", null, values);
        // rsd.close();
    }
    public void tj_adds(Posts p){
        rsd =  Cpllecthelp.getInstance(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", p.getId());
        values.put("tj_pic", p.getLitpic());
        values.put("tj_title", p.getTitle());
        values.put("tj_pubdate",p.getPubdate());
        values.put("tj_result",p.getResult());
        values.put("tj_channel",p.getChannel());
        values.put("tj_descript",p.getDescription());
        values.put("tj_souse",p.getSource());
        values.put("tj_img1",p.image.image1);
        values.put("tj_img2",p.image.image2);
        values.put("tj_img3",p.image.image3);
        values.put("tj_imagestate",p.imageState);
        values.put("tj_zan",p.getZan());
        values.put("tj_look",p.getClick());
        values.put("tj_tags",p.getTags());
        values.put("tj_unit",p.getUnit());
        values.put("tj_state",p.getState());
        values.put("tj_typename",p.getTypename());
        values.put("tj_sortname",p.getSortTime());
        rsd.replace("sql_maidiansqlite", null, values);
        // rsd.close();
    }
    List<Posts> tjpost ;
    public List<Posts> tj_findall(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        tjpost =new ArrayList<>();
        String sql="select * from sql_maidiansqlite where tj_channel='tj'  order by tj_sortname desc limit 10";
        Cursor cursor = rsd.rawQuery(sql, null);
        //  mypost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
            s.setLitpic(cursor.getString(cursor.getColumnIndex("tj_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("tj_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("tj_sortname")));
            s.setPubdate(cursor.getString(cursor.getColumnIndex("tj_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("tj_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("tj_channel")));
            s.setDescription(cursor.getString(cursor.getColumnIndex("tj_descript")));
            s.setSource(cursor.getString(cursor.getColumnIndex("tj_souse")));
            s.setZan(cursor.getString(cursor.getColumnIndex("tj_zan")));
            s.setClick(cursor.getString(cursor.getColumnIndex("tj_look")));
            s.setTags(cursor.getString(cursor.getColumnIndex("tj_tags")));
            s.setUnit(cursor.getString(cursor.getColumnIndex("tj_unit")));
            s.setState(cursor.getString(cursor.getColumnIndex("tj_state")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("tj_typename")));
            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("tj_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("tj_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("tj_img3"));
            s.setImage(image);
            s.setImageState(cursor.getString(cursor.getColumnIndex("tj_imagestate")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            tjpost.add(s);
        }
        cursor.close();
        //rsd.close();
        return tjpost;
    }
    List<Posts> kejipost ;
    public List<Posts> kejiku_findall(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        kejipost =new ArrayList<>();
        String sql="select * from sql_kejiku where tj_channel='kejiku' order by tj_sortname desc limit 10";
        Cursor cursor = rsd.rawQuery(sql, null);
        //  mypost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
            s.setLitpic(cursor.getString(cursor.getColumnIndex("tj_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("tj_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("tj_sortname")));
            s.setPubdate(cursor.getString(cursor.getColumnIndex("tj_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("tj_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("tj_channel")));
            s.setDescription(cursor.getString(cursor.getColumnIndex("tj_descript")));
            s.setSource(cursor.getString(cursor.getColumnIndex("tj_souse")));
            s.setZan(cursor.getString(cursor.getColumnIndex("tj_zan")));
            s.setClick(cursor.getString(cursor.getColumnIndex("tj_look")));
            s.setTags(cursor.getString(cursor.getColumnIndex("tj_tags")));
            s.setUnit(cursor.getString(cursor.getColumnIndex("tj_unit")));
            s.setState(cursor.getString(cursor.getColumnIndex("tj_state")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("tj_typename")));
            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("tj_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("tj_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("tj_img3"));
            s.setImage(image);
            s.setImageState(cursor.getString(cursor.getColumnIndex("tj_imagestate")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            kejipost.add(s);
        }
        cursor.close();
        //rsd.close();
        return kejipost;
    }
    public List<Posts> df_findall(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        tjpost =new ArrayList<>();
        String sql="select * from sql_dianfang where tj_channel='df' order by tj_sortname desc limit 10";
        Cursor cursor = rsd.rawQuery(sql, null);
        //  mypost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
            s.setLitpic(cursor.getString(cursor.getColumnIndex("tj_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("tj_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("tj_sortname")));
            s.setPubdate(cursor.getString(cursor.getColumnIndex("tj_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("tj_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("tj_channel")));
            s.setDescription(cursor.getString(cursor.getColumnIndex("tj_descript")));
            s.setSource(cursor.getString(cursor.getColumnIndex("tj_souse")));
            s.setZan(cursor.getString(cursor.getColumnIndex("tj_zan")));
            s.setClick(cursor.getString(cursor.getColumnIndex("tj_look")));
            s.setTags(cursor.getString(cursor.getColumnIndex("tj_tags")));
            s.setUnit(cursor.getString(cursor.getColumnIndex("tj_unit")));
            s.setState(cursor.getString(cursor.getColumnIndex("tj_state")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("tj_typename")));
            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("tj_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("tj_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("tj_img3"));
            s.setImage(image);
            s.setImageState(cursor.getString(cursor.getColumnIndex("tj_imagestate")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            tjpost.add(s);
        }
        cursor.close();
        //rsd.close();
        return tjpost;
    }
    public List<Posts> tj_findmore(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        if(tjpost != null && tjpost.size() >1){
            String pubdate = tjpost.get(tjpost.size() - 1).getSortTime();
            Log.i("pubdate",pubdate+"...................");
            String sql="select * from sql_maidiansqlite where tj_sortname<";
            sql += pubdate;
            sql += " and tj_channel='tj' order by tj_pubdate desc limit 10";

            Cursor cursor = rsd.rawQuery(sql, null);
            // mypost.clear();
            Posts s=null;
            while(cursor.moveToNext()){
                s=new Posts();
                s.setLitpic(cursor.getString(cursor.getColumnIndex("tj_pic")));
                s.setTitle(cursor.getString(cursor.getColumnIndex("tj_title")));
                s.setSortTime(cursor.getString(cursor.getColumnIndex("tj_sortname")));
                s.setPubdate(cursor.getString(cursor.getColumnIndex("tj_pubdate")));
                s.setResult(cursor.getString(cursor.getColumnIndex("tj_result")));
                s.setChannel(cursor.getString(cursor.getColumnIndex("tj_channel")));
                s.setDescription(cursor.getString(cursor.getColumnIndex("tj_descript")));
                s.setSource(cursor.getString(cursor.getColumnIndex("tj_souse")));
                s.setZan(cursor.getString(cursor.getColumnIndex("tj_zan")));
                s.setClick(cursor.getString(cursor.getColumnIndex("tj_look")));
                s.setTags(cursor.getString(cursor.getColumnIndex("tj_tags")));
                s.setUnit(cursor.getString(cursor.getColumnIndex("tj_unit")));
                s.setState(cursor.getString(cursor.getColumnIndex("tj_state")));
                s.setTypename(cursor.getString(cursor.getColumnIndex("tj_typename")));
                ImageState image=new ImageState();
                image.image1=cursor.getString(cursor.getColumnIndex("tj_img1"));
                image.image2=cursor.getString(cursor.getColumnIndex("tj_img2"));
                image.image3=cursor.getString(cursor.getColumnIndex("tj_img3"));
                s.setImage(image);
                s.setImageState(cursor.getString(cursor.getColumnIndex("tj_imagestate")));
                s.setId(cursor.getString(cursor.getColumnIndex("id")));
                tjpost.add(s);
            }
            cursor.close();
        }
        //  rsd.close();
        return tjpost;
    }

    public List<Posts> kejiku_findmore(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        String pubdate = kejipost.get(kejipost.size() - 1).getSortTime();
        Log.i("pubdate",pubdate+"...................");
        String sql="select * from sql_kejiku where tj_sortname<";
        sql += pubdate;
        sql += " and tj_channel='kejiku' order by tj_pubdate desc limit 10";

        Cursor cursor = rsd.rawQuery(sql, null);
        // mypost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
            s.setLitpic(cursor.getString(cursor.getColumnIndex("tj_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("tj_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("tj_sortname")));
            s.setPubdate(cursor.getString(cursor.getColumnIndex("tj_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("tj_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("tj_channel")));
            s.setDescription(cursor.getString(cursor.getColumnIndex("tj_descript")));
            s.setSource(cursor.getString(cursor.getColumnIndex("tj_souse")));
            s.setZan(cursor.getString(cursor.getColumnIndex("tj_zan")));
            s.setClick(cursor.getString(cursor.getColumnIndex("tj_look")));
            s.setTags(cursor.getString(cursor.getColumnIndex("tj_tags")));
            s.setUnit(cursor.getString(cursor.getColumnIndex("tj_unit")));
            s.setState(cursor.getString(cursor.getColumnIndex("tj_state")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("tj_typename")));
            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("tj_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("tj_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("tj_img3"));
            s.setImage(image);
            s.setImageState(cursor.getString(cursor.getColumnIndex("tj_imagestate")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            kejipost.add(s);
        }
        cursor.close();
        //  rsd.close();
        return kejipost;
    }

    public List<Posts> df_findmore(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        String pubdate = tjpost.get(tjpost.size() - 1).getSortTime();
        Log.i("pubdate",pubdate+"...................");
        String sql="select * from sql_dianfang where tj_sortname<";
        sql += pubdate;
        sql += " and tj_channel='df' order by tj_pubdate desc limit 10";

        Cursor cursor = rsd.rawQuery(sql, null);
        // mypost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
            s.setLitpic(cursor.getString(cursor.getColumnIndex("tj_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("tj_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("tj_sortname")));
            s.setPubdate(cursor.getString(cursor.getColumnIndex("tj_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("tj_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("tj_channel")));
            s.setDescription(cursor.getString(cursor.getColumnIndex("tj_descript")));
            s.setSource(cursor.getString(cursor.getColumnIndex("tj_souse")));
            s.setZan(cursor.getString(cursor.getColumnIndex("tj_zan")));
            s.setClick(cursor.getString(cursor.getColumnIndex("tj_look")));
            s.setTags(cursor.getString(cursor.getColumnIndex("tj_tags")));
            s.setUnit(cursor.getString(cursor.getColumnIndex("tj_unit")));
            s.setState(cursor.getString(cursor.getColumnIndex("tj_state")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("tj_typename")));
            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("tj_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("tj_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("tj_img3"));
            s.setImage(image);
            s.setImageState(cursor.getString(cursor.getColumnIndex("tj_imagestate")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            tjpost.add(s);
        }
        cursor.close();
        //  rsd.close();
        return tjpost;
    }
    public void zhengce_adds(Posts p){
        rsd =  Cpllecthelp.getInstance(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", p.getId());
//        values.put("zhengce_pic", p.getLitpic());
        values.put("zhengce_title", p.getTitle());
        values.put("zhengce_pubdate",p.getSortTime());
        values.put("zhengce_result",p.getResult());
        values.put("zhengcechannel",p.getChannel());
        values.put("zc_descript",p.getDescription());
        values.put("zc_unit",p.getUnit());
        values.put("zc_look",p.getClick());
        values.put("zc_tags",p.getTags());
        values.put("zc_typename",p.getTypename());
        rsd.replace("sql_maidianzhengce", null, values);
       // rsd.close();
    }
    public void xm_addlunbo(ADS s){
        // 一样的  先查询
     /* ADS olds= collect_queryByid(s.getAid());
        if(olds==null){*/
        rsd =  Cpllecthelp.getInstance(mContext).getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("id",s.getAid());
        values.put("xm_pic",s.getPicUrl());
        values.put("xm_title",s.getTitle());
        values.put("xm_pubdate",s.getPubdate());
        rsd.replace("sql_maidianxmlunbo", null, values);
       // rsd.close();
    }
    public void zc_addlunbo(ADS s){
        // 一样的  先查询
     /* ADS olds= collect_queryByid(s.getAid());
        if(olds==null){*/
        rsd =  Cpllecthelp.getInstance(mContext).getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("id",s.getAid());
        values.put("zhengce_pic",s.getPicUrl());
        values.put("zhengce_title",s.getTitle());
        values.put("zhengce_pibdate",s.getPubdate());
        rsd.replace("sql_maidianzclunbo", null, values);
       // rsd.close();
    }
    public void rencai_adds(Posts p){
        rsd =  Cpllecthelp.getInstance(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", p.getId());
        values.put("rencai_pic", p.getLitpic());
        values.put("rencai_username", p.getUsername());
        values.put("rencai_pubdate",p.getSortTime());
        values.put("rencai_result",p.getResult());
        values.put("rencaichannel",p.getChannel());
        values.put("rencai_typename",p.getTypename());
        values.put("rencai_body",p.getDescription());
        values.put("rc_rank",p.getRank());
        values.put("rc_tags",p.getTags());
        values.put("rc_zan",p.getZan());
        values.put("rc_look",p.getClick());
        values.put("rc_typename",p.getTypename());
        rsd.replace("sql_maidianrencai", null, values);
       // rsd.close();
    }
    List<Posts> rencaipost ;
    public List<Posts> rencai_findall(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        rencaipost  =new ArrayList<>();
        String sql="select * from sql_maidianrencai where rencaichannel='rencai' order by rencai_pubdate desc limit 10";
        Cursor cursor = rsd.rawQuery(sql, null);
         //rencaipost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
            s.setLitpic(cursor.getString(cursor.getColumnIndex("rencai_pic")));
            s.setUsername(cursor.getString(cursor.getColumnIndex("rencai_username")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("rencai_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("rencai_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("rencaichannel")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("rencai_typename")));
            s.setDescription(cursor.getString(cursor.getColumnIndex("rencai_body")));
            s.setRank(cursor.getString(cursor.getColumnIndex("rc_rank")));
            s.setTags(cursor.getString(cursor.getColumnIndex("rc_tags")));
            s.setZan(cursor.getString(cursor.getColumnIndex("rc_zan")));
            s.setClick(cursor.getString(cursor.getColumnIndex("rc_look")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("rc_typename")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            rencaipost.add(s);
        }
        cursor.close();
      //  rsd.close();
        return rencaipost;
    }
    public List<Posts> rencai_findmore(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        String rencai = rencaipost.get(rencaipost.size() - 1).getSortTime();
        String sql="select * from sql_maidianrencai where rencai_pubdate<";
        sql+=rencai;
        sql+=" and rencaichannel='rencai' order by rencai_pubdate desc limit 10";
        Cursor cursor = rsd.rawQuery(sql, null);
       // rencaipost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
            s.setLitpic(cursor.getString(cursor.getColumnIndex("rencai_pic")));
            s.setUsername(cursor.getString(cursor.getColumnIndex("rencai_username")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("rencai_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("rencai_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("rencaichannel")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("rencai_typename")));
            s.setDescription(cursor.getString(cursor.getColumnIndex("rencai_body")));
            s.setRank(cursor.getString(cursor.getColumnIndex("rc_rank")));
            s.setTags(cursor.getString(cursor.getColumnIndex("rc_tags")));
            s.setZan(cursor.getString(cursor.getColumnIndex("rc_zan")));
            s.setClick(cursor.getString(cursor.getColumnIndex("rc_look")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("rc_typename")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            rencaipost.add(s);
        }
        cursor.close();
       // rsd.close();
        return rencaipost;
    }
    public void zhuanli_adds(Posts p){
        rsd =  Cpllecthelp.getInstance(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", p.getId());
//        values.put("zhuanli_pic", p.getLitpic());
        values.put("zhuanli_title", p.getTitle());
        values.put("zhuanli_pubdate",p.getSortTime());
        values.put("zhuanli_result",p.getResult());
        values.put("zhuanlichannel",p.getChannel());
        values.put("zhuanli_state",p.getState());
        values.put("zhuanli_tags",p.getTags());
        values.put("zhuanli_look",p.getClick());
        values.put("zhuanli_typename",p.getTypename());
        rsd.replace("sql_maidianzhuanli", null, values);
       // rsd.close();
    }
    List<Posts> zhuanlipost ;
    public List<Posts> zhuanli_findall(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        zhuanlipost  =new ArrayList<>();
        String sql="select * from sql_maidianzhuanli where zhuanlichannel='zhuanli' order by zhuanli_pubdate desc limit 10";
        Cursor cursor = rsd.rawQuery(sql, null);
       // zhuanlipost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
//            s.setLitpic(cursor.getString(cursor.getColumnIndex("zhuanli_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("zhuanli_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("zhuanli_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("zhuanli_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("zhuanlichannel")));
            s.setState(cursor.getString(cursor.getColumnIndex("zhuanli_state")));
            s.setTags(cursor.getString(cursor.getColumnIndex("zhuanli_tags")));
            s.setClick(cursor.getString(cursor.getColumnIndex("zhuanli_look")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("zhuanli_typename")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            zhuanlipost.add(s);
        }
        cursor.close();
       // rsd.close();
        return zhuanlipost;
    }
    public List<Posts> zhuanli_findmore(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        String zhuanli = zhuanlipost.get(zhuanlipost.size()- 1).getSortTime();
        String sql="select * from sql_maidianzhuanli where zhuanli_pubdate<";
        sql += zhuanli;
        sql += " and zhuanlichannel='zhuanli' order by zhuanli_pubdate desc limit 10";
        Cursor cursor = rsd.rawQuery(sql, null);
       // zhuanlipost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
//            s.setLitpic(cursor.getString(cursor.getColumnIndex("zhuanli_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("zhuanli_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("zhuanli_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("zhuanli_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("zhuanlichannel")));
            s.setState(cursor.getString(cursor.getColumnIndex("zhuanli_state")));
            s.setTags(cursor.getString(cursor.getColumnIndex("zhuanli_tags")));
            s.setClick(cursor.getString(cursor.getColumnIndex("zhuanli_look")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("zhuanli_typename")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            zhuanlipost.add(s);
        }
        cursor.close();
       // rsd.close();
        return zhuanlipost;
    }

//    sql_maidianshiyanshi
    public  void sys_add(Posts p){
        rsd =  Cpllecthelp.getInstance(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", p.getId());
        values.put("sys_pic", p.getLitpic());
        values.put("sys_title", p.getTitle());
        values.put("sys_pubdate",p.getSortTime());
        values.put("sys_result",p.getResult());
        values.put("syschannel",p.getChannel());
        values.put("sys_tags",p.getTags());
        values.put("sys_zan",p.getZan());
        values.put("sys_look",p.getClick());
        values.put("sys_img1",p.image.image1);
        values.put("sys_img2",p.image.image2);
        values.put("sys_img3",p.image.image3);
        values.put("sys_imagestate",p.imageState);
        values.put("sys_typename",p.getTypename());
        rsd.replace("sql_maidianshiyanshi", null, values);
    }
    List<Posts> syspost ;
    public List<Posts> sys_findall(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        syspost  =new ArrayList<>();
        String sql="select * from sql_maidianshiyanshi where syschannel='shiyanshi' order by sys_pubdate desc limit 10" ;
        Cursor cursor = rsd.rawQuery(sql, null);
        // shebeipost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
            s.setLitpic(cursor.getString(cursor.getColumnIndex("sys_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("sys_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("sys_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("sys_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("syschannel")));
            s.setTags(cursor.getString(cursor.getColumnIndex("sys_tags")));
            s.setZan(cursor.getString(cursor.getColumnIndex("sys_zan")));
            s.setClick(cursor.getString(cursor.getColumnIndex("sys_look")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("sys_typename")));
            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("sys_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("sys_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("sys_img3"));
            s.setImage(image);
            s.setImageState(cursor.getString(cursor.getColumnIndex("sys_imagestate")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            syspost.add(s);
        }
        cursor.close();
        // rsd.close();
        return syspost;
    }
    public List<Posts> sys_findmore(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        String sys = syspost.get(syspost.size() - 1).getSortTime();
        String sql="select * from sql_maidianshiyanshi where sys_pubdate<";
        sql += sys;
        sql += " and syschannel='shiyanshi' order by sys_pubdate desc limit 10";
        Cursor cursor = rsd.rawQuery(sql, null);
        //  shebeipost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
            s.setLitpic(cursor.getString(cursor.getColumnIndex("sys_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("sys_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("sys_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("sys_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("syschannel")));
            s.setTags(cursor.getString(cursor.getColumnIndex("sys_tags")));
            s.setZan(cursor.getString(cursor.getColumnIndex("sys_zan")));
            s.setClick(cursor.getString(cursor.getColumnIndex("sys_look")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("sys_typename")));
            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("sys_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("sys_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("sys_img3"));
            s.setImage(image);
            s.setImageState(cursor.getString(cursor.getColumnIndex("sys_imagestate")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            syspost.add(s);
        }
        cursor.close();
        // rsd.close();
        return syspost;
    }
    public void shebei_adds(Posts p){
        rsd =  Cpllecthelp.getInstance(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", p.getId());
        values.put("shebei_pic", p.getLitpic());
        values.put("shebei_title", p.getTitle());
        values.put("shebei_pubdate",p.getSortTime());
        values.put("shebei_result",p.getResult());
        values.put("shebeichannel",p.getChannel());
        values.put("shebei_tags",p.getTags());
        values.put("shebei_zan",p.getZan());
        values.put("shebei_look",p.getClick());
        values.put("shebei_img1",p.image.image1);
        values.put("shebei_img2",p.image.image2);
        values.put("shebei_img3",p.image.image3);
        values.put("shebei_imagestate",p.imageState);
        values.put("shebei_typename",p.getTypename());
        rsd.replace("sql_maidianshebei", null, values);

    }
    List<Posts> shebeipost ;
    public List<Posts> shebei_findall(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        shebeipost  =new ArrayList<>();
        String sql="select * from sql_maidianshebei where shebeichannel='shebei' order by shebei_pubdate desc limit 10" ;
        Cursor cursor = rsd.rawQuery(sql, null);
       // shebeipost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
            s.setLitpic(cursor.getString(cursor.getColumnIndex("shebei_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("shebei_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("shebei_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("shebei_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("shebeichannel")));
            s.setTags(cursor.getString(cursor.getColumnIndex("shebei_tags")));
            s.setZan(cursor.getString(cursor.getColumnIndex("shebei_zan")));
            s.setClick(cursor.getString(cursor.getColumnIndex("shebei_look")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("shebei_typename")));
            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("shebei_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("shebei_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("shebei_img3"));
            s.setImage(image);
            s.setImageState(cursor.getString(cursor.getColumnIndex("shebei_imagestate")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            shebeipost.add(s);
        }
        cursor.close();
       // rsd.close();
        return shebeipost;
    }
    public List<Posts> shebei_findmore(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        String shebei = shebeipost.get(shebeipost.size() - 1).getSortTime();
        String sql="select * from sql_maidianshebei where shebei_pubdate<";
        sql += shebei;
        sql += " and shebeichannel='shebei' order by shebei_pubdate desc limit 10";
        Cursor cursor = rsd.rawQuery(sql, null);
      //  shebeipost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
            s.setLitpic(cursor.getString(cursor.getColumnIndex("shebei_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("shebei_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("shebei_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("shebei_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("shebeichannel")));
            s.setTags(cursor.getString(cursor.getColumnIndex("shebei_tags")));
            s.setZan(cursor.getString(cursor.getColumnIndex("shebei_zan")));
            s.setClick(cursor.getString(cursor.getColumnIndex("shebei_look")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("shebei_typename")));
            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("shebei_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("shebei_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("shebei_img3"));
            s.setImage(image);
            s.setImageState(cursor.getString(cursor.getColumnIndex("shebei_imagestate")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            shebeipost.add(s);
        }
        cursor.close();
       // rsd.close();
        return shebeipost;
    }
    public void xm_adds(Posts p){
        rsd =  Cpllecthelp.getInstance(mContext).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", p.getId());
        values.put("xm_pic", p.getLitpic());
        values.put("xm_title", p.getTitle());
        values.put("xm_pubdate",p.getSortTime());
        values.put("xm_result",p.getResult());
        values.put("xmchannel",p.getChannel());
//        values.put("xm_area",p.getArea_cate().getArea_cate1());
        values.put("xm_look",p.getClick());
        values.put("xm_tags",p.getTags());
        values.put("xm_img1",p.image.image1);
        values.put("xm_img2",p.image.image2);
        values.put("xm_img3",p.image.image3);
        values.put("xm_imagestate",p.imageState);
        values.put("xm_typename",p.getTypename());
        rsd.replace("sql_maidianxm", null, values);
     //   rsd.close();
    }
    List<ADS> xmlist;
    public List<ADS> xmfindalls(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        xmlist=new ArrayList<>();
        String sql="select * from sql_maidianxmlunbo order by id asc limit 3";

        Cursor cursor = rsd.rawQuery(sql, null);
       // xmlist.clear();
        ADS s=null;
        while(cursor.moveToNext()){
            s=new ADS();
            s.setPicUrl(cursor.getString(cursor.getColumnIndex("xm_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("xm_title")));
            s.setAid(cursor.getString(cursor.getColumnIndex("id")));
            s.setPubdate(cursor.getString(cursor.getColumnIndex("xm_pubdate")));

            xmlist.add(s);

        }
        cursor.close();
      //  rsd.close();
        return xmlist;
    }
    List<Posts> xmpost ;
    public List<Posts> xm_findall(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        xmpost  =new ArrayList<>();
        String sql="select * from sql_maidianxm where xmchannel='xiangmu' order by xm_pubdate desc limit 10";
        Cursor cursor = rsd.rawQuery(sql, null);
       // xmpost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
            s.setLitpic(cursor.getString(cursor.getColumnIndex("xm_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("xm_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("xm_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("xm_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("xmchannel")));
            s.setClick(cursor.getString(cursor.getColumnIndex("xm_look")));
            s.setTags(cursor.getString(cursor.getColumnIndex("xm_tags")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("xm_typename")));
            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("xm_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("xm_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("xm_img3"));
            s.setImage(image);
            s.setImageState(cursor.getString(cursor.getColumnIndex("xm_imagestate")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            xmpost.add(s);
        }
        cursor.close();
      //  rsd.close();
        return xmpost;
    }
    public List<Posts> xm_findmore(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        String xm = xmpost.get(xmpost.size() - 1).getSortTime();
        String sql="select * from sql_maidianxm where xm_pubdate<";
        sql += xm;
        sql += " and xmchannel='xiangmu' order by xm_pubdate desc limit 10";
        Cursor cursor = rsd.rawQuery(sql, null);
       // xmpost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
            s.setLitpic(cursor.getString(cursor.getColumnIndex("xm_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("xm_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("xm_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("xm_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("xmchannel")));
            s.setClick(cursor.getString(cursor.getColumnIndex("xm_look")));
            s.setTags(cursor.getString(cursor.getColumnIndex("xm_tags")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("xm_typename")));
            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("xm_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("xm_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("xm_img3"));
            s.setImage(image);
            s.setImageState(cursor.getString(cursor.getColumnIndex("xm_imagestate")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            xmpost.add(s);
        }
        cursor.close();
      //  rsd.close();
        return xmpost;
    }
    List<Posts> zcpost ;
    public List<Posts> zhengce_findall(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        zcpost  =new ArrayList<>();
        String sql="select * from sql_maidianzhengce where zhengcechannel='zhengce' order by zhengce_pubdate desc limit 10";
        Cursor cursor = rsd.rawQuery(sql, null);
       // zcpost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
//            s.setLitpic(cursor.getString(cursor.getColumnIndex("zhengce_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("zhengce_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("zhengce_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("zhengce_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("zhengcechannel")));
            s.setDescription(cursor.getString(cursor.getColumnIndex("zc_descript")));
            s.setUnit(cursor.getString(cursor.getColumnIndex("zc_unit")));
            s.setClick(cursor.getString(cursor.getColumnIndex("zc_look")));
            s.setTags(cursor.getString(cursor.getColumnIndex("zc_tags")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("zc_typename")));
            zcpost.add(s);
        }
        cursor.close();
       // rsd.close();
        return zcpost;
    }
    public List<Posts> zhengce_findmore(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        String zc = zcpost.get(zcpost.size() - 1).getSortTime();
        String sql="select * from sql_maidianzhengce where zhengce_pubdate<";
        sql+= zc;
        sql+= " and zhengcechannel='zhengce' order by zhengce_pubdate desc limit 10";
        Cursor cursor = rsd.rawQuery(sql, null);
       // zcpost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
//            s.setLitpic(cursor.getString(cursor.getColumnIndex("zhengce_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("zhengce_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("zhengce_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("zhengce_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("zhengcechannel")));
            s.setDescription(cursor.getString(cursor.getColumnIndex("zc_descript")));
            s.setUnit(cursor.getString(cursor.getColumnIndex("zc_unit")));
            s.setClick(cursor.getString(cursor.getColumnIndex("zc_look")));
            s.setTags(cursor.getString(cursor.getColumnIndex("zc_tags")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("zc_typename")));
            zcpost.add(s);
        }
        cursor.close();
       // rsd.close();
        return zcpost;
    }
    private List<Posts> mypost;

    /**
     * @return
     */
    public List<Posts> tuijian_findall(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();

        mypost =new ArrayList<>();
        String sql="select * from sql_maidiantuijian where channel='tuijian'  order by tuijian_pubdate desc limit 10";
        Cursor cursor = rsd.rawQuery(sql, null);
      //  mypost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
            s.setLitpic(cursor.getString(cursor.getColumnIndex("tuijian_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("tuijian_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("tuijian_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("tuijian_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("channel")));
            s.setDescription(cursor.getString(cursor.getColumnIndex("zx_descript")));
            s.setSource(cursor.getString(cursor.getColumnIndex("zx_souse")));
            s.setZan(cursor.getString(cursor.getColumnIndex("zx_zan")));
            s.setClick(cursor.getString(cursor.getColumnIndex("zx_look")));
            s.setTags(cursor.getString(cursor.getColumnIndex("zx_tags")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("zx_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("zx_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("zx_img3"));
            s.setImage(image);
            s.setImageState(cursor.getString(cursor.getColumnIndex("zx_imagestate")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("zx_typename")));
            mypost.add(s);
        }
        cursor.close();
        //rsd.close();
        return mypost;
    }
    public List<Posts> tuijian_findmore(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        String pubdate = mypost.get(mypost.size() - 1).getSortTime();
        Log.i("pubdate",pubdate+"...................");
        String sql="select * from sql_maidiantuijian where tuijian_pubdate<";
        sql += pubdate;
        sql += " and channel='tuijian' order by tuijian_pubdate desc limit 10";

        Cursor cursor = rsd.rawQuery(sql, null);
       // mypost.clear();
        Posts s=null;
        while(cursor.moveToNext()){
            s=new Posts();
            s.setLitpic(cursor.getString(cursor.getColumnIndex("tuijian_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("tuijian_title")));
            s.setSortTime(cursor.getString(cursor.getColumnIndex("tuijian_pubdate")));
            s.setResult(cursor.getString(cursor.getColumnIndex("tuijian_result")));
            s.setChannel(cursor.getString(cursor.getColumnIndex("channel")));
            s.setDescription(cursor.getString(cursor.getColumnIndex("zx_descript")));
            s.setSource(cursor.getString(cursor.getColumnIndex("zx_souse")));
            s.setZan(cursor.getString(cursor.getColumnIndex("zx_zan")));
            s.setClick(cursor.getString(cursor.getColumnIndex("zx_look")));

            s.setTags(cursor.getString(cursor.getColumnIndex("zx_tags")));
            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("zx_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("zx_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("zx_img3"));
            s.setImage(image);
            s.setImageState(cursor.getString(cursor.getColumnIndex("zx_imagestate")));
            s.setTypename(cursor.getString(cursor.getColumnIndex("zx_typename")));
            s.setId(cursor.getString(cursor.getColumnIndex("id")));
            mypost.add(s);
        }
        cursor.close();
      //  rsd.close();
        return mypost;
    }
private List<ADS> zxlist;
    public List<ADS> findalls(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        zxlist=new ArrayList<>();
        String sql="select * from sql_maidianlunbo order by id asc limit 3";
        Cursor cursor = rsd.rawQuery(sql, null);
       // list.clear();
        ADS s=null;
        while(cursor.moveToNext()){
            s=new ADS();
            s.setPicUrl(cursor.getString(cursor.getColumnIndex("lunbo_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("lunbo_title")));
            s.setAid(cursor.getString(cursor.getColumnIndex("id")));
            s.setPubdate(cursor.getString(cursor.getColumnIndex("lunbo_pubdate")));
            zxlist.add(s);
        }
        cursor.close();
       // rsd.close();
        return zxlist;
    }
    List<ADS> zclist;
    public List<ADS> zcfindlunbo(){
        rsd =  Cpllecthelp.getInstance(mContext).getReadableDatabase();
        zclist=new ArrayList<>();
        String sql="select * from sql_maidianzclunbo order by id asc limit 3";
        Cursor cursor = rsd.rawQuery(sql, null);
       // zclist.clear();
        ADS s=null;
        while(cursor.moveToNext()){
            s=new ADS();
            s.setPicUrl(cursor.getString(cursor.getColumnIndex("zhengce_pic")));
            s.setTitle(cursor.getString(cursor.getColumnIndex("zhengce_title")));
            s.setAid(cursor.getString(cursor.getColumnIndex("id")));
            s.setPubdate(cursor.getString(cursor.getColumnIndex("zhengce_pibdate")));
            zclist.add(s);
        }
        cursor.close();
       // rsd.close();
        return zclist;
    }
    public void del() {
        String sql = "delete from sql_maidianlunbo;";
        rsd.rawQuery(sql,null);
    }
}
