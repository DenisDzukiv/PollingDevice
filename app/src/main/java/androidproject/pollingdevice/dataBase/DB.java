package androidproject.pollingdevice.dataBase;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DB {
    private DBHelper dbHelper;
    private final Context mCtx;
    SQLiteDatabase sqLiteDatabase;


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mydb";

    public DB(Context ctx) {
        mCtx = ctx;
    }

    public void dbOpen(){
        dbHelper = new DBHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = dbHelper.getWritableDatabase();

    }

    public void dbClose (){
        dbHelper = new DBHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getType(){
        String sqlQuery = "select T." + dbHelper.TYPE_NAME + " , T." + dbHelper.TYPE_ID + " as _id "
                + " from "+ dbHelper.TABLE_TYPE_DEVICE +" as T ";

        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }

    public Cursor getAllCharacteristics(){
        String sqlQuery = "select T." + dbHelper.CH_NAME + " , T." + dbHelper.CH_ID + " as _id "
                + " from "+ dbHelper.CHARACTERISTICS +" as T ";

        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }

    public void drop_database(){
        mCtx.deleteDatabase(DATABASE_NAME);
        Log.d("myLog", "----- drop_database  ----");
    }


   /* public Cursor getCharacteristics(){
        Log.d("myLog", "----- logCursor2  ----");
        String sqlQuery = "select T." + dbHelper.TYPE_NAME + " , C." + dbHelper.CH_NAME + ", C." + dbHelper.CH_ID + " as _id "
                + " from "+ dbHelper.TYPE_CHARACTERISTICS +" as TC "
                + " INNER JOIN "+ dbHelper.TABLE_TYPE_DEVICE +" as T ON TC."+ dbHelper.TYPE_ID +" = T."+ dbHelper.TYPE_ID
                + " INNER JOIN "+ dbHelper.CHARACTERISTICS + " as C ON TC."+dbHelper.CH_ID+" = C."+dbHelper.CH_ID;

        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }*/

    public Cursor getCharacteristics(long id){
        Log.d("myLog", "----- logCursor2  ----");
        String sqlQuery = "select T." + dbHelper.TYPE_NAME + " , C." + dbHelper.CH_NAME + ", C." + dbHelper.CH_ID + " as _id "
                + " from "+ dbHelper.TYPE_CHARACTERISTICS +" as TC "
                + " INNER JOIN "+ dbHelper.TABLE_TYPE_DEVICE +" as T ON TC."+ dbHelper.TYPE_ID +" = T."+ dbHelper.TYPE_ID
                + " INNER JOIN "+ dbHelper.CHARACTERISTICS + " as C ON TC."+dbHelper.CH_ID+" = C."+dbHelper.CH_ID
                + " where T." + dbHelper.TYPE_ID + " = " + id;

        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }

    public static List<String> getTypeString(Cursor cursor){
        List<String> list = new ArrayList<>();
        if(cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.TYPE_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.TYPE_NAME);
            do{
                list.add(cursor.getString(nameIndex));
            } while (cursor.moveToNext());
        }
        return list;
    }
}
