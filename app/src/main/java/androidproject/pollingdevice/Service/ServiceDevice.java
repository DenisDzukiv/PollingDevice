package androidproject.pollingdevice.Service;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidproject.pollingdevice.Dao.DaoDevice;
import androidproject.pollingdevice.dataBase.DBHelper;
import androidproject.pollingdevice.model.Characteristics;
import androidproject.pollingdevice.model.Device;


public class ServiceDevice implements DaoDevice{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mydb";
    private DBHelper dbHelper;
    private final Context mCtx;
    private SQLiteDatabase sqLiteDatabase;
    final String LOG_TAG = "myLogs";
    ContentValues contentValuesDev, contentCharacteristics;


    public ServiceDevice(Context ctx) {
        mCtx = ctx;
    }

    public void dbOpen(){
        dbHelper = new DBHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void dbClose (){
        sqLiteDatabase.close();
    }



    @Override
    public void save(Device device) {
        dbOpen();
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "1");
        //sqLiteDatabase.beginTransaction();
        Log.d(LOG_TAG, "2");
       /* try {*/
            long lasdId;
            contentValuesDev = new ContentValues();
            contentValuesDev.clear();
            contentValuesDev.put(DBHelper.DEV_NAME, device.getDevName());
            contentValuesDev.put(DBHelper.TYPE_ID, device.getTypeId());
            lasdId = sqLiteDatabase.insert(DBHelper.DEVICE, null, contentValuesDev);
            Log.d(LOG_TAG, "lasdId" + String.valueOf(lasdId));
            contentCharacteristics = new ContentValues();
            contentCharacteristics.clear();
            for (Characteristics ct:device.getCharacteristicsList()){
                contentCharacteristics.put(DBHelper.DEV_ID, lasdId);
                contentCharacteristics.put(DBHelper.CH_ID, ct.getChId());
                contentCharacteristics.put(DBHelper.CH_VALUE, ct.getChValue());
            }
            /*sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }*/
        dbClose();
    }

    private long getLastId(){
        String sqlQuery = "select T." + DBHelper.DEV_ID + " as _id "
                + " from "+ DBHelper.DEVICE +" as T "
                + " where rowid = last_insert_rowid()";
        long devId = Long.parseLong(null);
        Cursor cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
        if(cursor != null) {
            if(cursor.moveToFirst()){

                int idIndex = cursor.getColumnIndex(DBHelper.ID);
                do{
                    devId = cursor.getInt(idIndex);
                } while (cursor.moveToNext());
            }
        }
        Log.d(LOG_TAG,"lastId" +  String.valueOf(devId));
        return devId;
    }

    public void drop_database(){
        mCtx.deleteDatabase(DATABASE_NAME);
        Log.d("myLog", "----- drop_database  ----");
    }
}
