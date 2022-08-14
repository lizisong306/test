package dao.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import dao.Cpllecthelp;
import dao.dbentity.CollectionEntity;

/**
 * Created by lizisong on 2017/1/13.
 */

public class MainDianIcon {

    private static MainDianIcon instance;

    private Cpllecthelp dbHelper;

    public MainDianIcon(Context context){
        dbHelper = Cpllecthelp.getInstance(context);
    }

    /**
     * 获取收藏数据的单例模式
     * @param context
     * @return
     */
    public static  MainDianIcon getInstance(Context context){
        if(instance == null && context == null){
            throw new IllegalArgumentException("Argument context can't be null!!!");
        }else if(instance == null){
            instance = new MainDianIcon(context);
        }
        return instance;
    }

    /**
     * 添加数据入库
     * @param
     */
    public long insert(byte[] img, String mid)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("mid", mid);
        cv.put("icon", img);
        long result = db.insert("maidianicon", null, cv);
        return result;
    }

    /**
     * 根据主键删除一条数据
     * @param mid
     * @return
     */
    public synchronized long delete(String mid){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        int num = writeDatabase.delete("maidianicon",  "mid = ?", new String[] {
                mid
        });
        return num;
    }

    public void updata(Bitmap bm, String mid){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        byte[] out = null;
        Bitmap bitmap = get(mid);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        out = baos.toByteArray();
        if(bitmap == null){
            insert(out, mid);
        }else{
            ContentValues values = new ContentValues();
            values.put("icon", out);
            values.put("mid", mid);

            writeDatabase.update("maidianicon", values, "mid = ?", new String[] {
                    mid});
        }
    }


    /**
     * 更新数据
     * @param
     * @return
     */
    public void updata(byte[] img, String mid){
        SQLiteDatabase writeDatabase = dbHelper.getWritableDatabase();
        Bitmap bitmap = get(mid);
        if(bitmap == null){
            insert(img, mid);
        }else{
            ContentValues values = new ContentValues();
            values.put("icon", img);
            values.put("mid", mid);

            writeDatabase.update("maidianicon", values, "mid = ?", new String[] {
                    mid});
        }

    }


    /**
     * 获取所有数据
     * @return
     */
    public Bitmap get(String mid){
        SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
        Bitmap bitmap = null;
        byte[] in = null;
        String midStr;
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM maidianicon"
                , null);
        while (cursor.moveToNext()){
            midStr = cursor.getString(cursor.getColumnIndexOrThrow("mid"));
            if(midStr.equals(mid)){
                in = cursor.getBlob(cursor.getColumnIndexOrThrow("icon"));
                break;
            }

        }
        cursor.close();
        if(in != null){
             bitmap = BitmapFactory.decodeByteArray(in, 0, in.length, null);

        }
        return bitmap;
    }


}
