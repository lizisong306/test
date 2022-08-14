package dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 13520 on 2016/8/25.
 */
public class Cpllecthelp extends SQLiteOpenHelper {
    private static Cpllecthelp singleton = null;
    private static final String DATABASE_NAME = "maidian.db";
    private static final int DATABASE_VERSION = 7;
    private static Cpllecthelp instance;

    Cpllecthelp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table release_adds(id integer primary key autoincrement,trim varchar(20),realease varchar(20),introduction varchar(20),bitmap varchar(20))");
        db.execSQL("create table sql_maidianlunbo(id integer not null unique primary key autoincrement,lunbo_pic varchar(64),lunbo_title varchar(20),lunbo_pubdate varchar(20))");
        db.execSQL("create table sql_maidiantuijian(id integer primary key autoincrement,tuijian_pic varchar(64),tuijian_title varchar(20),tuijian_pubdate varchar(20),tuijian_result varchar(20),channel varchar(10),zx_descript varchar(20),zx_souse varchar(20),zx_zan varchar(10),zx_look varchar(10),zx_tags varchar(10),zx_img1 varchar(64),zx_img2 varchar(64),zx_img3 varchar(64),zx_imagestate varchar(10),zx_typename varchar(10))");
        db.execSQL("create table sql_maidianzclunbo(id integer  primary key autoincrement,zhengce_pic varchar(64),zhengce_title varchar(20),zhengce_pibdate varchar(20))");
        db.execSQL("create table sql_maidianzhengce(id integer primary key autoincrement,zhengce_title varchar(20),zhengce_pubdate varchar(20),zhengce_result varchar(20),zhengcechannel varchar(10),zc_descript varchar(30),zc_unit varchar(10),zc_look varchar(10),zc_tags varchar(10),zc_typename varchar(10))");
        db.execSQL("create table sql_maidianxmlunbo(id integer  primary key autoincrement,xm_pic varchar(64),xm_title varchar(20),xm_pubdate varchar(20))");
        db.execSQL("create table sql_maidianxm(id integer primary key autoincrement,xm_pic varchar(64),xm_title varchar(20),xm_pubdate varchar(20),xm_result varchar(20),xmchannel varchar(10),xm_look varchar(10),xm_tags varchar(10),xm_img1 varchar(64),xm_img2 varchar(64),xm_img3 varchar(64),xm_imagestate varchar(10),xm_typename varchar(10))");
        db.execSQL("create table sql_maidianshebei(id integer primary key autoincrement,shebei_pic varchar(64),shebei_title varchar(20),shebei_pubdate varchar(20),shebei_result varchar(20),shebeichannel varchar(10),shebei_tags varchar(10),shebei_zan varchar(10),shebei_look varchar(10),shebei_img1 varchar(64),shebei_img2 varchar(64),shebei_img3 varchar(64),shebei_imagestate varchar(10),shebei_typename varchar(10))");
        db.execSQL("create table sql_maidianzhuanli(id integer primary key autoincrement,zhuanli_title varchar(20),zhuanli_pubdate varchar(20),zhuanli_result varchar(20),zhuanlichannel varchar(10),zhuanli_state varchar(10),zhuanli_tags varchar(10),zhuanli_look varchar(10),zhuanli_typename varchar(10))");
        db.execSQL("create table sql_maidianrencai(id integer primary key autoincrement,rencai_pic varchar(64),rencai_username varchar(20),rencai_pubdate varchar(20),rencai_result varchar(20),rencaichannel varchar(10),rencai_typename varchar(20),rencai_body varchar(50),rc_rank varchar(10),rc_tags varchar(10),rc_zan varchar(10),rc_look varchar(10),rc_typename varchar(10))");
        db.execSQL("create table sql_searchhistory(id integer primary key autoincrement,text varchar(10))");
        db.execSQL("create table maidiancollection(id integer primary key autoincrement,type varchar(30),iscollect varchar(2),isAdd integer default(-1),pic varchar(64),title varchar(64),upFlag integer default(0),aid varchar(10),updateTime varchar(20),pid varchar(20),sc_img1 varchar(64),sc_img2 varchar(64),sc_img3 varchar(64),sc_imagestate varchar(10),sc_yuanshi varchar(10),sc_description varchar(64) ,sc_area_cate varchar(64),sc_click varchar(10),sc_zan varchar(10))");
//        ,sc_img1 varchar(64),sc_img2 varchar(64),sc_img3 varchar(64),sc_imagestate varchar(10),sc_yuanshi varchar(10)
        db.execSQL("create table maidianguanzhu(id integer primary key autoincrement,type varchar(30),iscollect varchar(2),isAdd integer default(-1),pic varchar(64),title varchar(64),upFlag integer default(0),aid varchar(10),updateTime varchar(20))");
        db.execSQL("create table maidian_condition (id integer primary key autoincrement, mid varchar(30),evaluetop varchar(128), pid varchar(20), evalue varchar(128), typeid varchar(128), category varchar(10), province varchar(10), name varchar(20), updateTime varchar(20),upFlag integer default(0), evalueTitle varchar(256))");
        db.execSQL("create table sql_maidiantj(id integer not null unique primary key autoincrement,lunbo_pic varchar(64),lunbo_title varchar(20),lunbo_pubdate varchar(20))");
        db.execSQL("create table sql_maidianshiyanshi(id integer primary key autoincrement,sys_pic varchar(64),sys_title varchar(20),sys_pubdate varchar(20),sys_result varchar(20),syschannel varchar(10),sys_tags varchar(10),sys_zan varchar(10),sys_look varchar(10),sys_img1 varchar(64),sys_img2 varchar(64),sys_img3 varchar(64),sys_imagestate varchar(10),sys_typename varchar(10))");

        db.execSQL("create table sql_maidiansqlite(id integer primary key autoincrement,tj_pic varchar(64),tj_title varchar(20),tj_pubdate varchar(20),tj_result varchar(20),tj_channel varchar(10),tj_descript varchar(20),tj_souse varchar(20),tj_zan varchar(10),tj_look varchar(10),tj_tags varchar(10),tj_unit varchar(10),tj_state varchar(10),tj_spec varchar(10),tj_typename varchar(10),tj_sortname varchar(10),tj_img1 varchar(64),tj_img2 varchar(64),tj_img3 varchar(64),tj_imagestate varchar(10))");
        db.execSQL("create table maidianyuyue(id integer primary key autoincrement, meetTime varchar(32),meetPost varchar(64), meetAdress varchar(64),meetMen varchar(10),meetTel varchar(20), meetTitle varchar(64),mid varchar(20), aid varchar(20), loginFlag varchar(32), typeid varchar(20),pic varchar(20),title varchar(32),rank varchar(32),lingyu varchar(32),model varchar(32),upFlag integer default(0),updateTime varchar(20))");

        db.execSQL("create table maidianzan(id integer primary key autoincrement,type varchar(30),iscollect varchar(2),isAdd integer default(-1),pic varchar(64),title varchar(64),upFlag integer default(0),aid varchar(10),updateTime varchar(20))");
        db.execSQL("create table maidianicon(id integer primary key autoincrement, mid varchar(20), icon BLOB)");
        db.execSQL("create table sql_dianfang(id integer primary key autoincrement,tj_pic varchar(64),tj_title varchar(20),tj_pubdate varchar(20),tj_result varchar(20),tj_channel varchar(10),tj_descript varchar(20),tj_souse varchar(20),tj_zan varchar(10),tj_look varchar(10),tj_tags varchar(10),tj_unit varchar(10),tj_state varchar(10),tj_spec varchar(10),tj_typename varchar(10),tj_sortname varchar(10),tj_img1 varchar(64),tj_img2 varchar(64),tj_img3 varchar(64),tj_imagestate varchar(10))");
        db.execSQL("create table sql_dianfanglunbo(id integer not null unique primary key autoincrement,lunbo_pic varchar(64),lunbo_title varchar(20),lunbo_pubdate varchar(20))");
        db.execSQL("create table sql_kejiku(id integer primary key autoincrement,tj_pic varchar(64),tj_title varchar(20),tj_pubdate varchar(20),tj_result varchar(20),tj_channel varchar(10),tj_descript varchar(20),tj_souse varchar(20),tj_zan varchar(10),tj_look varchar(10),tj_tags varchar(10),tj_unit varchar(10),tj_state varchar(10),tj_spec varchar(10),tj_typename varchar(10),tj_sortname varchar(10),tj_img1 varchar(64),tj_img2 varchar(64),tj_img3 varchar(64),tj_imagestate varchar(10))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          if(oldVersion == 1){
              db.execSQL("drop table maidianyuyue");
              db.execSQL("create table maidianyuyue(id integer primary key autoincrement, meetTime varchar(32),meetPost varchar(64), meetAdress varchar(64),meetMen varchar(10),meetTel varchar(20), meetTitle varchar(64),mid varchar(20), aid varchar(20), loginFlag varchar(32), typeid varchar(20),pic varchar(20),title varchar(32),rank varchar(32),lingyu varchar(32),model varchar(32),upFlag integer default(0),updateTime varchar(20))");
              db.execSQL("create table sql_maidiantj(id integer not null unique primary key autoincrement,lunbo_pic varchar(64),lunbo_title varchar(20),lunbo_pubdate varchar(20))");
              db.execSQL("create table maidianzan(id integer primary key autoincrement,type varchar(30),iscollect varchar(2),isAdd integer default(-1),pic varchar(64),title varchar(64),upFlag integer default(0),aid varchar(10),updateTime varchar(20))");
              db.execSQL("create table maidianicon(id integer primary key autoincrement, mid varchar(20), icon BLOB)");
          }
          if(oldVersion <3){
              boolean state = checkColumnExists(db, "maidiancollection", "pid");
              if(!state){
                  db.execSQL("alter table maidiancollection add pid varchar(20)");
              }
          }

        if(oldVersion < 5){
            Log.d("lizisong", "升级数据库表0");
            db.execSQL("drop table sql_maidiansqlite");
            db.execSQL("create table sql_maidiansqlite(id integer primary key autoincrement,tj_pic varchar(64),tj_title varchar(20),tj_pubdate varchar(20),tj_result varchar(20),tj_channel varchar(10),tj_descript varchar(20),tj_souse varchar(20),tj_zan varchar(10),tj_look varchar(10),tj_tags varchar(10),tj_unit varchar(10),tj_state varchar(10),tj_spec varchar(10),tj_typename varchar(10),tj_sortname varchar(10),tj_img1 varchar(64),tj_img2 varchar(64),tj_img3 varchar(64),tj_imagestate varchar(10))");

            db.execSQL("drop table sql_maidianshebei");
            db.execSQL("create table sql_maidianshebei(id integer primary key autoincrement,shebei_pic varchar(64),shebei_title varchar(20),shebei_pubdate varchar(20),shebei_result varchar(20),shebeichannel varchar(10),shebei_tags varchar(10),shebei_zan varchar(10),shebei_look varchar(10),shebei_img1 varchar(64),shebei_img2 varchar(64),shebei_img3 varchar(64),shebei_imagestate varchar(10),shebei_typename varchar(10))");

            db.execSQL("drop table sql_maidiantuijian");
            db.execSQL("create table sql_maidiantuijian(id integer primary key autoincrement,tuijian_pic varchar(64),tuijian_title varchar(20),tuijian_pubdate varchar(20),tuijian_result varchar(20),channel varchar(10),zx_descript varchar(20),zx_souse varchar(20),zx_zan varchar(10),zx_look varchar(10),zx_tags varchar(10),zx_img1 varchar(64),zx_img2 varchar(64),zx_img3 varchar(64),zx_imagestate varchar(10),zx_typename varchar(10))");

            db.execSQL("drop table sql_maidianshiyanshi");
            db.execSQL("create table sql_maidianshiyanshi(id integer primary key autoincrement,sys_pic varchar(64),sys_title varchar(20),sys_pubdate varchar(20),sys_result varchar(20),syschannel varchar(10),sys_tags varchar(10),sys_zan varchar(10),sys_look varchar(10),sys_img1 varchar(64),sys_img2 varchar(64),sys_img3 varchar(64),sys_imagestate varchar(10),sys_typename varchar(10))");

            db.execSQL("drop table sql_maidianrencai");
            db.execSQL("create table sql_maidianrencai(id integer primary key autoincrement,rencai_pic varchar(64),rencai_username varchar(20),rencai_pubdate varchar(20),rencai_result varchar(20),rencaichannel varchar(10),rencai_typename varchar(20),rencai_body varchar(50),rc_rank varchar(10),rc_tags varchar(10),rc_zan varchar(10),rc_look varchar(10),rc_typename varchar(10))");

            db.execSQL("drop table sql_maidianxm");
            db.execSQL("create table sql_maidianxm(id integer primary key autoincrement,xm_pic varchar(64),xm_title varchar(20),xm_pubdate varchar(20),xm_result varchar(20),xmchannel varchar(10),xm_look varchar(10),xm_tags varchar(10),xm_img1 varchar(64),xm_img2 varchar(64),xm_img3 varchar(64),xm_imagestate varchar(10),xm_typename varchar(10))");

            db.execSQL("drop table sql_maidianzhuanli");
            db.execSQL("create table sql_maidianzhuanli(id integer primary key autoincrement,zhuanli_title varchar(20),zhuanli_pubdate varchar(20),zhuanli_result varchar(20),zhuanlichannel varchar(10),zhuanli_state varchar(10),zhuanli_tags varchar(10),zhuanli_look varchar(10),zhuanli_typename varchar(10))");

            db.execSQL("create table sql_dianfang(id integer primary key autoincrement,tj_pic varchar(64),tj_title varchar(20),tj_pubdate varchar(20),tj_result varchar(20),tj_channel varchar(10),tj_descript varchar(20),tj_souse varchar(20),tj_zan varchar(10),tj_look varchar(10),tj_tags varchar(10),tj_unit varchar(10),tj_state varchar(10),tj_spec varchar(10),tj_typename varchar(10),tj_sortname varchar(10),tj_img1 varchar(64),tj_img2 varchar(64),tj_img3 varchar(64),tj_imagestate varchar(10))");
            db.execSQL("create table sql_dianfanglunbo(id integer not null unique primary key autoincrement,lunbo_pic varchar(64),lunbo_title varchar(20),lunbo_pubdate varchar(20))");

        }
        if(oldVersion == 5){
            db.execSQL("drop table sql_maidianzhengce");
            db.execSQL("create table sql_maidianzhengce(id integer primary key autoincrement,zhengce_title varchar(20),zhengce_pubdate varchar(20),zhengce_result varchar(20),zhengcechannel varchar(10),zc_descript varchar(30),zc_unit varchar(10),zc_look varchar(10),zc_tags varchar(10),zc_typename varchar(10))");
            db.execSQL("create table sql_kejiku(id integer primary key autoincrement,tj_pic varchar(64),tj_title varchar(20),tj_pubdate varchar(20),tj_result varchar(20),tj_channel varchar(10),tj_descript varchar(20),tj_souse varchar(20),tj_zan varchar(10),tj_look varchar(10),tj_tags varchar(10),tj_unit varchar(10),tj_state varchar(10),tj_spec varchar(10),tj_typename varchar(10),tj_sortname varchar(10),tj_img1 varchar(64),tj_img2 varchar(64),tj_img3 varchar(64),tj_imagestate varchar(10))");
        }
        if(oldVersion == 6){
            Log.d("lizisong", "升级数据库表1");
            db.execSQL("drop table maidiancollection");
            db.execSQL("create table maidiancollection(id integer primary key autoincrement,type varchar(30),iscollect varchar(2),isAdd integer default(-1),pic varchar(64),title varchar(64),upFlag integer default(0),aid varchar(10),updateTime varchar(20),pid varchar(20),sc_img1 varchar(64),sc_img2 varchar(64),sc_img3 varchar(64),sc_imagestate varchar(10),sc_yuanshi varchar(10),sc_description varchar(64) ,sc_area_cate varchar(64),sc_click varchar(10),sc_zan varchar(10))");
        }
    }

    /**
     * 获取数据库的单例模式
     * @param context
     * @return
     */
    public static synchronized Cpllecthelp getInstance(Context context) {
        if (singleton == null) {
            singleton = new Cpllecthelp(context);
        }
        return singleton;
    }

    /**
     * 方法：检查表中某列是否存在
     * @param db
     * @param tableName 表名
     * @param columnName 列名
     * @return
     */
    private boolean checkColumnExists(SQLiteDatabase db, String tableName, String columnName) {
        boolean result = false ;
        Cursor cursor = null ;

        try{
            cursor = db.rawQuery( "select * from sqlite_master where name = ? and sql like ?"
                    , new String[]{tableName , "%" + columnName + "%"} );
            result = null != cursor && cursor.moveToFirst() ;
        }catch (Exception e){

        }finally{
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }

        return result ;
    }
}
