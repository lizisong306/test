package dao.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import dao.Cpllecthelp;
import dao.dbentity.CollectionEntity;
import entity.ImageState;

/**
 * 处理收藏的数据
 * Created by lizisong on 2016/12/5.
 */

public class MaiDianCollection {

    private static MaiDianCollection instance;

    private Cpllecthelp dbHelper;

    public MaiDianCollection(Context context){
        dbHelper = Cpllecthelp.getInstance(context);
    }

    /**
     * 获取收藏数据的单例模式
     * @param context
     * @return
     */
    public static  MaiDianCollection getInstance(Context context){
        if(instance == null && context == null){
            throw new IllegalArgumentException("Argument context can't be null!!!");
        }else if(instance == null){
            instance = new MaiDianCollection(context);
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
        values.put("pid",entity.pid);
        values.put("sc_click", entity.click);
        values.put("sc_zan", entity.zan);
        if(entity.image != null){
            if(entity.image.image1 != null){
                values.put("sc_img1",entity.image.image1);
            }
            if(entity.image.image2 != null){
                values.put("sc_img2",entity.image.image2);
            }
            if(entity.image.image3 != null){
                values.put("sc_img3",entity.image.image3);
            }
        }
        values.put("sc_description", entity.description);
        values.put("sc_area_cate", entity.area_cate);

        values.put("sc_imagestate",entity.imageState);
        values.put("sc_yuanshi",entity.is_academician);
        long id = writeDatabase.insert("maidiancollection", "id", values);

        return id;
    }

    /**
     * 根据主键删除一条数据
     * @param id
     * @return
     */
    public synchronized long delete(int id){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        int num = writeDatabase.delete("maidiancollection",  "id = ?", new String[] {
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
        int num = writeDatabase.delete("maidiancollection",  "aid = ?", new String[] {
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
        values.put("pid",entity.pid);
        values.put("sc_click", entity.click);
        values.put("sc_zan", entity.zan);

        if(entity.image != null){
            if(entity.image.image1 != null){
                values.put("sc_img1",entity.image.image1);
            }
            if(entity.image.image2 != null){
                values.put("sc_img2",entity.image.image2);
            }
            if(entity.image.image3 != null){
                values.put("sc_img3",entity.image.image3);
            }
        }
        values.put("sc_description", entity.description);
        values.put("sc_area_cate", entity.area_cate);

        values.put("sc_imagestate",entity.imageState);
        values.put("sc_yuanshi",entity.is_academician);
        int num = writeDatabase.update("maidiancollection", values, "id = ?", new String[] {
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
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM maidiancollection  group by aid ORDER BY id  DESC"
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

            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("sc_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("sc_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("sc_img3"));
            item.setImage(image);
            item.setImageState(cursor.getString(cursor.getColumnIndex("sc_imagestate")));
            item.is_academician=cursor.getString(cursor.getColumnIndex("sc_yuanshi"));
            item.pid=cursor.getString(cursor.getColumnIndexOrThrow("pid"));

            item.description = cursor.getString(cursor.getColumnIndex("sc_description"));
            item.area_cate   = cursor.getString(cursor.getColumnIndex("sc_area_cate"));
            item.click = cursor.getString(cursor.getColumnIndex("sc_click"));
            item.zan   = cursor.getString(cursor.getColumnIndex("sc_zan"));
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
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM maidiancollection  group by aid ORDER BY id  DESC"
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
            item.pid=cursor.getString(cursor.getColumnIndexOrThrow("pid"));
            item.description = cursor.getString(cursor.getColumnIndex("sc_description"));
            item.area_cate   = cursor.getString(cursor.getColumnIndex("sc_area_cate"));
            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("sc_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("sc_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("sc_img3"));
            item.setImage(image);
            item.setImageState(cursor.getString(cursor.getColumnIndex("sc_imagestate")));
            item.is_academician=cursor.getString(cursor.getColumnIndex("sc_yuanshi"));
            item.pid=cursor.getString(cursor.getColumnIndexOrThrow("pid"));
            item.click = cursor.getString(cursor.getColumnIndex("sc_click"));
            item.zan   = cursor.getString(cursor.getColumnIndex("sc_zan"));
            listData.add(item);
        }
        cursor.close();
        return listData;
    }

    public synchronized  ArrayList<CollectionEntity> getperson(){
        ArrayList<CollectionEntity> listData = new ArrayList<CollectionEntity>();
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM maidiancollection  group by aid ORDER BY id  DESC"
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
            item.pid=cursor.getString(cursor.getColumnIndexOrThrow("pid"));
            item.description = cursor.getString(cursor.getColumnIndex("sc_description"));
            item.area_cate   = cursor.getString(cursor.getColumnIndex("sc_area_cate"));
            ImageState image=new ImageState();
            image.image1=cursor.getString(cursor.getColumnIndex("sc_img1"));
            image.image2=cursor.getString(cursor.getColumnIndex("sc_img2"));
            image.image3=cursor.getString(cursor.getColumnIndex("sc_img3"));
            item.setImage(image);
            item.setImageState(cursor.getString(cursor.getColumnIndex("sc_imagestate")));
            item.is_academician=cursor.getString(cursor.getColumnIndex("sc_yuanshi"));
            item.pid=cursor.getString(cursor.getColumnIndexOrThrow("pid"));

            listData.add(item);


        }
        cursor.close();
        return listData;
    }


    public void deleteData(){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        writeDatabase.execSQL("delete from maidiancollection");
    }
}
