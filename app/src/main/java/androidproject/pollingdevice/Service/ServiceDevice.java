package androidproject.pollingdevice.Service;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidproject.pollingdevice.Dao.DaoDevice;
import androidproject.pollingdevice.dataBase.DBHelper;
import androidproject.pollingdevice.model.DeviceCharacteristicsValue;
import androidproject.pollingdevice.model.Device;
import androidproject.pollingdevice.model.TypeDevice;


public class ServiceDevice implements DaoDevice{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mydb";
    private DBHelper dbHelper;
    private final Context mCtx;
    private SQLiteDatabase sqLiteDatabase;
    final String LOG_TAG = "myLogs";
    ContentValues contentValuesDev, contentCharacteristics;
    Cursor cursor;


    public ServiceDevice(Context ctx, DBHelper db) {
        mCtx = ctx;
        dbHelper = db;
    }

    public void serviceDbClose (){
        sqLiteDatabase.close();
    }

    @Override
    public void save(Device device) {
        sqLiteDatabase = dbHelper.getWritableDatabase();
        try {
            sqLiteDatabase.beginTransaction();
            long lastId;
            contentValuesDev = new ContentValues();
            contentValuesDev.clear();
            contentValuesDev.put(DBHelper.DEV_NAME, device.getDevName());
            contentValuesDev.put(DBHelper.TYPE_ID, device.getTypeId());
            lastId = sqLiteDatabase.insert(DBHelper.DEVICE, null, contentValuesDev);

            contentCharacteristics = new ContentValues();
            contentCharacteristics.clear();
            for (DeviceCharacteristicsValue ct:device.getCharacteristicsList()){
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
        String sqlQuery = null;
        try {
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sqlQuery = "select T." + DBHelper.DEV_ID  + " as _id " + " , T." + DBHelper.DEV_NAME  + " , T." + DBHelper.TYPE_ID
                    + " from "+ DBHelper.DEVICE +" as T "
                    + " order by T." + DBHelper.DEV_ID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }*/



   /* public Cursor getAllDevice(){
        String sqlQuery = null;
        try {
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sqlQuery = "select T." + DBHelper.DEV_ID  + " as _id "  + " , T." + DBHelper.DEV_NAME  + " , T." + DBHelper.TYPE_ID
                    + " , TD." + DBHelper.TYPE_NAME + " , CV." + DBHelper.CH_ID + " , CV." + DBHelper.CH_VALUE
                    + " , CH." + DBHelper.CH_NAME
                    + " from "+ DBHelper.DEVICE +" as T "
                    + " INNER JOIN " + DBHelper.TABLE_TYPE_DEVICE + " as TD ON TD." + DBHelper.TYPE_ID + " = T."+ DBHelper.TYPE_ID
                    + " INNER JOIN " + DBHelper.CHARACTERISTICS_VALUE + " as CV ON CV." + DBHelper.DEV_ID + " = T." + DBHelper.DEV_ID
                    + " JOIN " + DBHelper.CHARACTERISTICS + " as CH ON CH." + DBHelper.CH_ID + " = CV." + DBHelper.CH_ID
                    + " order by T." + DBHelper.DEV_ID
                    ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }*/

    public List<Device> devices(){
        List<Device> dev = new ArrayList<>();
        String sqlQuery = null;
        try {
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sqlQuery = "select T." + DBHelper.DEV_ID  + " as _id" + " , T." + DBHelper.DEV_NAME  + " , T." + DBHelper.TYPE_ID
                    + " , TD." + DBHelper.TYPE_NAME
                    + " from "+ DBHelper.DEVICE +" as T "
                    + " INNER JOIN " + DBHelper.TABLE_TYPE_DEVICE + " as TD ON TD." + DBHelper.TYPE_ID + " = T."+ DBHelper.TYPE_ID
                    + " order by T." + DBHelper.DEV_ID
            ;
            cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
            if(cursor != null) {
                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBHelper.ID);
                    int nameIndex = cursor.getColumnIndex(DBHelper.DEV_NAME);
                    int typeIdIndex = cursor.getColumnIndex(DBHelper.TYPE_ID);
                    int typeNameIndex = cursor.getColumnIndex(DBHelper.TYPE_NAME);
                    do {
                        Device device = new Device();
                        TypeDevice typeDevice = new TypeDevice();

                        device.setDevId(cursor.getLong(idIndex));
                        device.setDevName(cursor.getString(nameIndex));

                        typeDevice.setTypeId(cursor.getLong(typeIdIndex));
                        typeDevice.setTypeName(cursor.getString(typeNameIndex));

                        device.setTypeDevice(typeDevice);
                        device.setBox(false);
                        dev.add(device);
                    } while (cursor.moveToNext());
                } else Log.d(LOG_TAG, "Cursor is null");
            }
            cursor.close();

            /*sqlQuery = "select T." + DBHelper.DEV_ID  + " , T." + DBHelper.DEV_NAME  + " , T." + DBHelper.TYPE_ID
                    + " , TD." + DBHelper.TYPE_NAME + " , CV." + DBHelper.CH_ID + " , CV." + DBHelper.CH_VALUE
                    + " , CH." + DBHelper.CH_NAME
                    + " from "+ DBHelper.DEVICE +" as T "
                    + " INNER JOIN " + DBHelper.TABLE_TYPE_DEVICE + " as TD ON TD." + DBHelper.TYPE_ID + " = T."+ DBHelper.TYPE_ID
                    + " INNER JOIN " + DBHelper.CHARACTERISTICS_VALUE + " as CV ON CV." + DBHelper.DEV_ID + " = T." + DBHelper.DEV_ID
                    + " JOIN " + DBHelper.CHARACTERISTICS + " as CH ON CH." + DBHelper.CH_ID + " = CV." + DBHelper.CH_ID
                    + " order by T." + DBHelper.DEV_ID
                    ;*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            sqLiteDatabase.close();
        }
        return dev;
    }

    public void deleteDevice(List<Device> deviceList){
        List<String> dev_id = new ArrayList<>();
        try {
            sqLiteDatabase = dbHelper.getWritableDatabase();
            for(Device d: deviceList){
                dev_id.add(String.valueOf(d.getDevId()));
            }
            sqLiteDatabase.delete(DBHelper.DEVICE, DBHelper.DEV_ID + " = ?" , dev_id.toArray(new String[dev_id.size()]) );
            sqLiteDatabase.delete(DBHelper.CHARACTERISTICS_VALUE, DBHelper.DEV_ID + " = ? ", dev_id.toArray(new String[dev_id.size()]) );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
