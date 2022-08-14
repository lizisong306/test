package dao.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import dao.Cpllecthelp;
import dao.dbentity.CollectionEntity;
import dao.dbentity.PulseData;

/**
 * Created by lizisong on 2016/12/28.
 */

public class PulseCondtion {

    private static PulseCondtion instance;
    private Cpllecthelp dbHelper;

    public PulseCondtion(Context context){
        dbHelper = Cpllecthelp.getInstance(context);
    }


    /**
     * 获取把脉条件数据的单例模式
     * @param context
     * @return
     */
    public static  PulseCondtion getInstance(Context context){
        if(instance == null && context == null){
            throw new IllegalArgumentException("Argument context can't be null!!!");
        }else if(instance == null){
            instance = new PulseCondtion(context);
        }
        return instance;
    }

    /**
     * 添加数据入库
     * @param entity
     */
    public synchronized long insert(PulseData entity){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mid", entity.mid);
        values.put("evaluetop", entity.evaluetop);
        values.put("evalue", entity.evalue);
        values.put("typeid", entity.typeid);
        values.put("province", entity.province);
        values.put("category", entity.category);
        values.put("name", entity.name);
        values.put("updateTime", entity.updatatime);
        values.put("evalueTitle", entity.evalueTitle);
        values.put("pid", entity.pid);
        long id = writeDatabase.insert("maidian_condition", "id", values);

        return id;
    }
    /**
     * 根据主键删除一条数据
     * @param id
     * @return
     */
    public long delete(int id){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        int num = writeDatabase.delete("maidian_condition",  "id = ?", new String[] {
                String.valueOf(id)
        });

        return num;
    }

    /**
     * 根据mid删除一条数据
     * @param mid
     * @return
     */
    public long deletebyaid(String mid){

        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        int num = writeDatabase.delete("maidian_condition",  "mid = ?", new String[] {
                mid
        });

        return num;
    }
    /**
     * 根据mid删除一条数据
     * @param evaluetop
     * @return
     */
    public long deletebyevaluetop(String evaluetop){

        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        int num = writeDatabase.delete("maidian_condition",  "evaluetop = ?", new String[] {
                evaluetop
        });

        return num;
    }


    /**
     * 更新数据
     * @param entity
     * @return
     */
    public long updata(PulseData entity){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mid", entity.mid);
        values.put("evaluetop", entity.evaluetop);
        values.put("evalue", entity.evalue);
        values.put("typeid", entity.typeid);
        values.put("province", entity.province);
        values.put("category", entity.category);
        values.put("name", entity.name);
        values.put("updateTime", entity.updatatime);
        values.put("upFlag",entity.upFlag);
        values.put("evalueTitle", entity.evalueTitle);
        values.put("pid", entity.pid);
        int num = writeDatabase.update("maidian_condition", values, "id = ?", new String[] {
                String.valueOf(entity.id)});
        return num;
    }

    /**
     * 获取所有数据
     * @return
     */
    public ArrayList<PulseData> get(){
        ArrayList<PulseData> listData = new ArrayList<PulseData>();
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM maidian_condition  ORDER BY id  DESC"
                , null);
        while (cursor.moveToNext()){
            PulseData item = new PulseData();
            item.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            item.mid = cursor.getString(cursor.getColumnIndexOrThrow("mid"));
            item.evaluetop =  cursor.getString(cursor.getColumnIndexOrThrow("evaluetop"));
            item.evalue = cursor.getString(cursor.getColumnIndexOrThrow("evalue"));
            item.typeid = cursor.getString(cursor.getColumnIndexOrThrow("typeid"));
            item.province = cursor.getString(cursor.getColumnIndexOrThrow("province"));
            item.category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
            item.updatatime = cursor.getString(cursor.getColumnIndexOrThrow("updateTime"));
            item.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            item.upFlag = cursor.getInt(cursor.getColumnIndexOrThrow("upFlag"));
            item.evalueTitle = cursor.getString(cursor.getColumnIndexOrThrow("evalueTitle"));
            item.pid = cursor.getString(cursor.getColumnIndexOrThrow("pid"));
            listData.add(item);
        }
        cursor.close();
        return listData;
    }

    /**
     * 跟新PID 数据
     */
    public int updataPid(String evaluetop, String pid){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pid", pid);
        int num = writeDatabase.update("maidian_condition", values, "evaluetop = ?", new String[] {
                evaluetop});
        return num;
    }

    /**
     * 更新upFlag
     */
    public int updataUpFlag(String evaluetop, int UpFlag){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("upFlag", UpFlag);
        int num = writeDatabase.update("maidian_condition", values, "evaluetop = ?", new String[] {
                evaluetop});
        return num;

    }
    /**
     * 清空数据库表的数据
     */
    public void deleteData(){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        writeDatabase.execSQL("delete from maidian_condition");
    }


}
