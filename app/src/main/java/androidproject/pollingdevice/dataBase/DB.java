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
        sqLiteDatabase.close();
        //DBHelper = new DBHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getType(){
        String sqlQuery = "select T." + DBHelper.TYPE_NAME + " , T." + DBHelper.TYPE_ID + " as _id "
                + " from "+ DBHelper.TABLE_TYPE_DEVICE +" as T ";

        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }


    public Cursor getBit(){
        String sqlQuery = "select T." + DBHelper.CHBIT_NAME + " , T." + DBHelper.CHBIT_ID + " as _id "
                + " from "+ DBHelper.CHARACTERISTICS_BIT +" as T ";

        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }

    public Cursor getAllCharacteristics(){
        String sqlQuery = "select T." + DBHelper.CH_NAME + " , T." + DBHelper.CH_ID + " as _id "
                + " from "+ DBHelper.CHARACTERISTICS +" as T ";

        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }

    public void drop_database(){
        mCtx.deleteDatabase(DATABASE_NAME);
        Log.d("myLog", "----- drop_database  ----");
    }


    public Cursor getCharacteristics(long id){
        Log.d("myLog", "----- logCursor2  ----");
        String sqlQuery = "select T." + DBHelper.TYPE_NAME + " , C." + DBHelper.CH_NAME + ", C." + DBHelper.CH_ID + " as _id "
                + " from "+ DBHelper.TYPE_CHARACTERISTICS +" as TC "
                + " INNER JOIN "+ DBHelper.TABLE_TYPE_DEVICE +" as T ON TC."+ DBHelper.TYPE_ID +" = T."+ DBHelper.TYPE_ID
                + " INNER JOIN "+ DBHelper.CHARACTERISTICS + " as C ON TC."+DBHelper.CH_ID+" = C."+DBHelper.CH_ID
                + " where T." + DBHelper.TYPE_ID + " = " + id
                + " order by C." + DBHelper.CH_ID;

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
