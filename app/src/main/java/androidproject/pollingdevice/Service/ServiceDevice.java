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
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    public void dbClose (){
        sqLiteDatabase.close();
    }



    @Override
    public void save(Device device) {
        for(Characteristics c1:device.getCharacteristicsList()) {
            Log.d(LOG_TAG, "iddddd=" + String.valueOf(c1.getChId()) + " = " + c1.getChValue());
        }
        dbOpen();
        //sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            long lastId;
            contentValuesDev = new ContentValues();
            contentValuesDev.clear();
            contentValuesDev.put(DBHelper.DEV_NAME, device.getDevName());
            contentValuesDev.put(DBHelper.TYPE_ID, device.getTypeId());
            lastId = sqLiteDatabase.insert(DBHelper.DEVICE, null, contentValuesDev);

            contentCharacteristics = new ContentValues();
            contentCharacteristics.clear();
            for (Characteristics ct:device.getCharacteristicsList()){
                //Log.d(LOG_TAG, "iddddd=" + String.valueOf(ct.getChId()) + " = " + ct.getChValue());
                contentCharacteristics.put(DBHelper.DEV_ID, lastId);
                contentCharacteristics.put(DBHelper.CH_ID, ct.getChId());
                contentCharacteristics.put(DBHelper.CH_VALUE, ct.getChValue());
                sqLiteDatabase.insert(DBHelper.CHARACTERISTICS_VALUE, null, contentCharacteristics);
            }
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }

    }

    public Cursor getCharValue(){
        String sqlQuery = "select T." + DBHelper.DEV_ID + " , T." + DBHelper.CH_ID + " as _id " + ", T." + DBHelper.CH_VALUE
                + " from "+ DBHelper.CHARACTERISTICS_VALUE +" as T "
                ;

        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }

    /*public Cursor getAllDevice(){
        String sqlQuery = "select T." + DBHelper.DEV_ID  + " as _id " + " , T." + DBHelper.DEV_NAME  + ", T." + DBHelper.TYPE_ID
                + " from "+ DBHelper.DEVICE +" as T "
                + " INNER JOIN " + DBHelper.TABLE_TYPE_DEVICE + " as TD ON TD." + DBHelper.TYPE_ID + " = T."+ DBHelper.TYPE_ID
                + " INNER JOIN " + DBHelper.CHARACTERISTICS_VALUE + " as CV ON CV." +

                + " INNER JOIN "+ DBHelper.TABLE_TYPE_DEVICE +" as T ON TC."+ DBHelper.TYPE_ID +" = T."+ DBHelper.TYPE_ID
                ;

        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }*/


}
