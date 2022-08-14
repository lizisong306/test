package dao;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2016/6/23.
 */
public class Collectionss {
    private SQLiteDatabase db;
  /*  private List<Releasebean> list;
    public Collectionss(Context context){
        db=new Cpllecthelp(context, "releases.db", null, 1).getReadableDatabase();
        list=new ArrayList<>();
    }

    public void collect_add(Releasebean s){
        ContentValues values=new ContentValues();
            values.put("trim",s.getTrim());
        values.put("realease",s.getRealease());
        values.put("introduction",s.getIntroduction());
        values.put("bitmap",s.getBitmap());
        db.insert("release_adds", null, values);

    }
    public List<Releasebean> findall(){

        Cursor cursor = db.query("release_adds", null, null, null, null, null, null);
        list.clear();
        Releasebean s=null;
        while(cursor.moveToNext()){
            s=new Releasebean();
            s.setTrim(cursor.getString(cursor.getColumnIndex("trim")));
            s.setRealease(cursor.getString(cursor.getColumnIndex("realease")));
            s.setIntroduction(cursor.getString(cursor.getColumnIndex("introduction")));
            s.setBitmap(cursor.getString(cursor.getColumnIndex("bitmap")));
            s.setId(cursor.getInt(cursor.getColumnIndex("id")));
            list.add(s);

        }
        return list;
    }
    *//**
     * 通过id删除，可以删除唯一的一条记录
     *//*
    public void deleteStuById(int id) {
       db.delete("release_adds","id=?",new String[]{id+""});
    }
*/
}

