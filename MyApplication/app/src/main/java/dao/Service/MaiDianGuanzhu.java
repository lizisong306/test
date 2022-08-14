package dao.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import dao.Cpllecthelp;
import dao.dbentity.CollectionEntity;

/**
 * Created by lizisong on 2017/1/6.
 */

public class MaiDianGuanzhu {
    private static MaiDianGuanzhu instance;

    private Cpllecthelp dbHelper;

    public MaiDianGuanzhu (Context context){
        dbHelper = Cpllecthelp.getInstance(context);
    }

    /**
     * 获取收藏数据的单例模式
     * @param context
     * @return
     */
    public static  MaiDianGuanzhu getInstance(Context context){
        if(instance == null && context == null){
            throw new IllegalArgumentException("Argument context can't be null!!!");
        }else if(instance == null){
            instance = new MaiDianGuanzhu(context);
        }
        return instance;
    }

    /**
     * 添加数据入库
     * @param entity
     */
    public synchronized long insert(CollectionEntity entity){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", entity.title);
        values.put("upFlag", entity.upFlag);
        values.put("aid", entity.aid);
        values.put("updateTime", entity.updateTime);
        values.put("type", entity.type);
        values.put("pic",entity.pic);
        values.put("iscollect",entity.iscollect);
        values.put("isAdd",entity.isAdd);
        long id = writeDatabase.insert("maidianguanzhu", "id", values);

        return id;
    }

    /**
     * 根据主键删除一条数据
     * @param id
     * @return
     */
    public synchronized long delete(int id){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        int num = writeDatabase.delete("maidianguanzhu",  "id = ?", new String[] {
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
        int num = writeDatabase.delete("maidianguanzhu",  "aid = ?", new String[] {
                aid
        });

        return num;
    }
    /**
     * 更新数据
     * @param entity
     * @return
     */
    public long updata(CollectionEntity entity){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", entity.title);
        values.put("upFlag", entity.upFlag);
        values.put("aid", entity.aid);
        values.put("updateTime", entity.updateTime);
        values.put("type", entity.type);
        values.put("pic",entity.pic);
        values.put("iscollect",entity.iscollect);
        values.put("isAdd",entity.isAdd);
        int num = writeDatabase.update("maidianguanzhu", values, "id = ?", new String[] {
                String.valueOf(entity.id)});
        return num;
    }

    /**
     * 获取所有数据
     * @return
     */
    public ArrayList<CollectionEntity> getcollect(){
        ArrayList<CollectionEntity> listData = new ArrayList<CollectionEntity>();
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM maidianguanzhu  group by aid ORDER BY id  DESC"
                , null);
        while (cursor.moveToNext()){
            CollectionEntity item = new CollectionEntity();
            item.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            item.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            item.type =  cursor.getString(cursor.getColumnIndexOrThrow("type"));
            item.aid = cursor.getString(cursor.getColumnIndexOrThrow("aid"));
            item.updateTime = cursor.getString(cursor.getColumnIndexOrThrow("updateTime"));
            item.upFlag = cursor.getInt(cursor.getColumnIndexOrThrow("upFlag"));
            item.pic = cursor.getString(cursor.getColumnIndexOrThrow("pic"));
            item.iscollect=cursor.getString(cursor.getColumnIndexOrThrow("iscollect"));
            item.isAdd = cursor.getInt(cursor.getColumnIndexOrThrow("isAdd"));

            listData.add(item);


        }
        cursor.close();
        return listData;
    }

    /**
     * 获取所有数据
     * @return
     */
    public ArrayList<CollectionEntity> get(){
        ArrayList<CollectionEntity> listData = new ArrayList<CollectionEntity>();
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM maidianguanzhu  group by aid ORDER BY id  DESC"
                , null);
        while (cursor.moveToNext()){
            CollectionEntity item = new CollectionEntity();
            item.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            item.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            item.type =  cursor.getString(cursor.getColumnIndexOrThrow("type"));
            item.aid = cursor.getString(cursor.getColumnIndexOrThrow("aid"));
            item.updateTime = cursor.getString(cursor.getColumnIndexOrThrow("updateTime"));
            item.upFlag = cursor.getInt(cursor.getColumnIndexOrThrow("upFlag"));
            item.pic = cursor.getString(cursor.getColumnIndexOrThrow("pic"));
            item.iscollect=cursor.getString(cursor.getColumnIndexOrThrow("iscollect"));
            item.isAdd = cursor.getInt(cursor.getColumnIndexOrThrow("isAdd"));
            listData.add(item);
        }
        cursor.close();
        return listData;
    }

    public synchronized  ArrayList<CollectionEntity> getperson(){
        ArrayList<CollectionEntity> listData = new ArrayList<CollectionEntity>();
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM maidianguanzhu  group by aid ORDER BY id  DESC"
                , null);
        while (cursor.moveToNext()){
            CollectionEntity item = new CollectionEntity();
            item.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            item.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            item.type =  cursor.getString(cursor.getColumnIndexOrThrow("type"));
            item.aid = cursor.getString(cursor.getColumnIndexOrThrow("aid"));
            item.updateTime = cursor.getString(cursor.getColumnIndexOrThrow("updateTime"));
            item.upFlag = cursor.getInt(cursor.getColumnIndexOrThrow("upFlag"));
            item.pic = cursor.getString(cursor.getColumnIndexOrThrow("pic"));
            item.iscollect=cursor.getString(cursor.getColumnIndexOrThrow("iscollect"));
            item.isAdd = cursor.getInt(cursor.getColumnIndexOrThrow("isAdd"));

            listData.add(item);


        }
        cursor.close();
        return listData;
    }
    /**
     * 清空数据库表的数据
     */
    public void deleteData(){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        writeDatabase.execSQL("delete from maidianguanzhu");
    }
}
