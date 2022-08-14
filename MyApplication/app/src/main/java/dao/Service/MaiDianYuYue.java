package dao.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import Util.SharedPreferencesUtil;
import dao.Cpllecthelp;

import entity.YuYueData;

/**
 * Created by lizisong on 2017/1/7.
 */

public class MaiDianYuYue {
    private static MaiDianYuYue instance;

    private Cpllecthelp dbHelper;

    public MaiDianYuYue (Context context){
        dbHelper = Cpllecthelp.getInstance(context);
    }

    /**
     * 获取收藏数据的单例模式
     * @param context
     * @return
     */
    public static  MaiDianYuYue getInstance(Context context){
        if(instance == null && context == null){
            throw new IllegalArgumentException("Argument context can't be null!!!");
        }else if(instance == null){
            instance = new MaiDianYuYue(context);
        }
        return instance;
    }

    /**
     * 添加数据入库
     * @param entity
     */
    public synchronized long insert(YuYueData entity){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("meetTime", entity.meetTime);
        values.put("upFlag", entity.upFlag);
        values.put("aid", entity.aid);
        values.put("pic", entity.pic);
        values.put("meetTime", entity.meetTime);
        values.put("typeid", entity.typeid);
        values.put("loginFlag", entity.loginFlag);
        values.put("mid", entity.mid);
        values.put("meetPost", entity.meetPost);
        values.put("meetMen",entity.meetMen);
        values.put("meetTel", entity.meetTel);
        values.put("meetTitle",entity.meetTitle);
        values.put("title", entity.title);
        values.put("rank", entity.rank);
        values.put("lingyu", entity.lingyu);
        values.put("model", entity.model);
        values.put("updateTime", entity.update);
        values.put("meetAdress", entity.meetAdress);//用来表示；

        long id = writeDatabase.insert("maidianyuyue", "id", values);

        return id;
    }

    /**
     * 根据主键删除一条数据
     * @param id
     * @return
     */
    public synchronized long delete(int id){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        int num = writeDatabase.delete("maidianyuyue",  "id = ?", new String[] {
                String.valueOf(id)
        });

        return num;
    }

    /**
     * 根据aid删除一条数据
     * @param aid
     * @return
     */
    public synchronized long deletebyaid(String aid){

        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        int num = writeDatabase.delete("maidianyuyue",  "aid = ?", new String[] {
                aid
        });

        return num;
    }
    /**
     * 更新数据
     * @param entity
     * @return
     */
    public long updata(YuYueData entity){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", entity.id);
        values.put("meetTime", entity.meetTime);
        values.put("upFlag", entity.upFlag);
        values.put("aid", entity.aid);
        values.put("pic", entity.pic);
        values.put("meetTime", entity.meetTime);
        values.put("typeid", entity.typeid);
        values.put("loginFlag", entity.loginFlag);
        values.put("mid", entity.mid);
        values.put("meetPost", entity.meetPost);
        values.put("meetMen",entity.meetMen);
        values.put("meetTel", entity.meetTel);
        values.put("meetTitle",entity.meetTitle);
        values.put("title", entity.title);
        values.put("rank", entity.rank);
        values.put("lingyu", entity.lingyu);
        values.put("model", entity.model);
        values.put("updateTime", entity.update);
        values.put("time", entity.meetTime);
        values.put("meetAdress", entity.meetAdress);//用来表示返回的ID；
        int num = writeDatabase.update("maidianyuyue", values, "id = ?", new String[] {
                String.valueOf(entity.id)});
        return num;
    }

    /**
     * 获取所有数据
     * @return
     */
    public ArrayList<YuYueData> get(){
        ArrayList<YuYueData> listData = new ArrayList<YuYueData>();
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM maidianyuyue"
                , null);

        while (cursor.moveToNext()){
            YuYueData item = new YuYueData();
            item.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            item.meetMen = cursor.getString(cursor.getColumnIndexOrThrow("meetMen"));
            item.typeid =  cursor.getString(cursor.getColumnIndexOrThrow("typeid"));
            item.aid = cursor.getString(cursor.getColumnIndexOrThrow("aid"));
            item.mid = cursor.getString(cursor.getColumnIndexOrThrow("mid"));
            item.upFlag = cursor.getInt(cursor.getColumnIndexOrThrow("upFlag"));
            item.pic = cursor.getString(cursor.getColumnIndexOrThrow("pic"));
            item.loginFlag = cursor.getString(cursor.getColumnIndexOrThrow("loginFlag"));
            item.meetPost = cursor.getString(cursor.getColumnIndexOrThrow("meetPost"));
            item.meetTel = cursor.getString(cursor.getColumnIndexOrThrow("meetTel"));
            item.meetPost = cursor.getString(cursor.getColumnIndexOrThrow("meetPost"));
            item.meetTitle = cursor.getString(cursor.getColumnIndexOrThrow("meetTitle"));
            item.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            item.model = cursor.getString(cursor.getColumnIndexOrThrow("model"));
            item.rank = cursor.getString(cursor.getColumnIndexOrThrow("rank"));
            item.lingyu = cursor.getString(cursor.getColumnIndexOrThrow("lingyu"));
            item.update = cursor.getString(cursor.getColumnIndexOrThrow("updateTime"));
            item.meetTime = cursor.getString(cursor.getColumnIndexOrThrow("meetTime"));
            item.meetAdress = cursor.getString(cursor.getColumnIndexOrThrow("meetAdress"));
            listData.add(item);
        }
        cursor.close();
        return listData;
    }

    public void deleteData(){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        writeDatabase.execSQL("delete from maidianyuyue");
    }

}
