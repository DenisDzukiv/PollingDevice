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

    public DB(Context ctx, DBHelper db) {
        mCtx = ctx;
        dbHelper = db;
    }

    public void dbOpen(DBHelper db){
        dbHelper = db;
    }

    public void dbClose (){
        sqLiteDatabase.close();
    }

    public Cursor getType(){
        String sqlQuery = null;
        try {
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sqlQuery = "select T." + DBHelper.TYPE_NAME + " , T." + DBHelper.TYPE_ID + " as _id "
                    + " from "+ DBHelper.TABLE_TYPE_DEVICE +" as T ";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }


    public Cursor getBit(){
        String sqlQuery = null;
        try {
            sqlQuery = "select T." + DBHelper.CHBIT_NAME + " , T." + DBHelper.CHBIT_ID + " as _id "
                    + " from "+ DBHelper.CHARACTERISTICS_BIT +" as T ";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }

    public Cursor getAllCharacteristics(){
        String sqlQuery = "select T." + DBHelper.CH_NAME + " , T." + DBHelper.CH_ID + " as _id "
                + " from "+ DBHelper.CHARACTERISTICS +" as T ";

        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }

    public void drop_database(){
        mCtx.deleteDatabase(DBHelper.DATABASE_NAME);
        Log.d("myLog", "----- drop_database  ----");
    }

    public Cursor getCharacteristics(long id){
        String sqlQuery = "select T." + DBHelper.TYPE_NAME + " , C." + DBHelper.CH_NAME + ", C." + DBHelper.CH_ID + " as _id "
                + " from "+ DBHelper.TYPE_CHARACTERISTICS +" as TC "
                + " INNER JOIN "+ DBHelper.TABLE_TYPE_DEVICE +" as T ON TC."+ DBHelper.TYPE_ID +" = T."+ DBHelper.TYPE_ID
                + " INNER JOIN "+ DBHelper.CHARACTERISTICS + " as C ON TC."+DBHelper.CH_ID + " = C."+DBHelper.CH_ID
                + " where T." + DBHelper.TYPE_ID + " = " + id
                + " order by C." + DBHelper.CH_ID;

        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }


}
