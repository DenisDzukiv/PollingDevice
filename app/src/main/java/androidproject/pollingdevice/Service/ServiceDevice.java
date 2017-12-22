package androidproject.pollingdevice.Service;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidproject.pollingdevice.Dao.DaoDevice;
import androidproject.pollingdevice.R;
import androidproject.pollingdevice.dataBase.DBHelper;
import androidproject.pollingdevice.model.DeviceCharacteristicsValue;
import androidproject.pollingdevice.model.Device;
import androidproject.pollingdevice.model.TypeDevice;


public class ServiceDevice implements DaoDevice {

    private DBHelper dbHelper;
    private final Context mCtx;
    private SQLiteDatabase sqLiteDatabase;
    final String LOG_TAG = "myLogs";
    ContentValues contentValuesDev, contentCharacteristics;
    Cursor cursor, cursorCharacter;


    public ServiceDevice(Context ctx, DBHelper db) {
        mCtx = ctx;
        dbHelper = db;
    }

    public void serviceDbClose() {
        sqLiteDatabase.close();
    }

    @Override
    public void save(Device device) {
        try {
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            long lastId;
            contentValuesDev = new ContentValues();
            contentValuesDev.put(DBHelper.DEV_NAME, device.getDevName());
            contentValuesDev.put(DBHelper.TYPE_ID, device.getTypeId());
            lastId = sqLiteDatabase.insert(DBHelper.DEVICE, null, contentValuesDev);

            contentCharacteristics = new ContentValues();
            contentCharacteristics.clear();
            for (DeviceCharacteristicsValue ct : device.getCharacteristicsList()) {
                contentCharacteristics.put(DBHelper.DEV_ID, lastId);
                contentCharacteristics.put(DBHelper.CH_ID, ct.getChId());
                contentCharacteristics.put(DBHelper.CH_VALUE, ct.getChValue());
                sqLiteDatabase.insert(DBHelper.CHARACTERISTICS_VALUE, null, contentCharacteristics);
            }
            sqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e) {
                e.printStackTrace();
         } finally {
            sqLiteDatabase.endTransaction();
            serviceDbClose();
        }
    }

    public void update(Device device) {
        try {
        sqLiteDatabase = dbHelper.getWritableDatabase();

            Log.d(LOG_TAG, "dev_id" + String.valueOf(device.getDevId()));
        sqLiteDatabase.beginTransaction();
        contentValuesDev = new ContentValues();
        contentValuesDev.put(DBHelper.DEV_NAME, device.getDevName());
        contentValuesDev.put(DBHelper.TYPE_ID, device.getTypeId());
        sqLiteDatabase.update(DBHelper.DEVICE, contentValuesDev, DBHelper.DEV_ID + " = ?" , new String[] {String.valueOf(device.getDevId())});

        sqLiteDatabase.delete(DBHelper.CHARACTERISTICS_VALUE, DBHelper.DEV_ID + " = ?" , new String[] {String.valueOf(device.getDevId())});
            Log.d(LOG_TAG, "21");
            contentCharacteristics = new ContentValues();
            contentCharacteristics.clear();

        for (DeviceCharacteristicsValue ct : device.getCharacteristicsList()) {
            contentCharacteristics.put(DBHelper.DEV_ID, device.getDevId());
            contentCharacteristics.put(DBHelper.CH_ID, ct.getChId());
            contentCharacteristics.put(DBHelper.CH_VALUE, ct.getChValue());
            sqLiteDatabase.insert(DBHelper.CHARACTERISTICS_VALUE, null, contentCharacteristics);
        }
            Log.d(LOG_TAG, "dev_id" + String.valueOf(device.getDevId()));
        sqLiteDatabase.setTransactionSuccessful();
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
            serviceDbClose();
        }
    }

    public Cursor getCharValue() {
        String sqlQuery = "select T." + DBHelper.DEV_ID + " , T." + DBHelper.CH_ID + " as _id " + ", T." + DBHelper.CH_VALUE
                + " from " + DBHelper.CHARACTERISTICS_VALUE + " as T ";

        return sqLiteDatabase.rawQuery(sqlQuery, null);
    }

    public List<Device> devices() {
        List<Device> deviceList = new ArrayList<>();
        List<DeviceCharacteristicsValue> deviceCharacteristicsValueList = new ArrayList<>();
        String sqlQuery = null;
        try {
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sqlQuery = "select T." + DBHelper.DEV_ID + " as _id" + " , T." + DBHelper.DEV_NAME + " , T." + DBHelper.TYPE_ID
                    + " , TD." + DBHelper.TYPE_NAME
                    + " from " + DBHelper.DEVICE + " as T "
                    + " INNER JOIN " + DBHelper.TABLE_TYPE_DEVICE + " as TD ON TD." + DBHelper.TYPE_ID + " = T." + DBHelper.TYPE_ID
                    + " order by T." + DBHelper.DEV_ID
            ;
            cursor = sqLiteDatabase.rawQuery(sqlQuery, null);

            if (cursor != null) {
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
                        device.setImage(R.drawable.arrow1);
                        try {
                            Cursor cursor1 = sqLiteDatabase.query(DBHelper.CHARACTERISTICS_VALUE, null, DBHelper.DEV_ID + " = ?", new String[]{String.valueOf(cursor.getLong(idIndex))}, null, null, null);
                            if (cursor1 != null) {
                                if (cursor1.moveToFirst()) {
                                    int chId = cursor1.getColumnIndex(DBHelper.CH_ID);
                                    int chValue = cursor1.getColumnIndex(DBHelper.CH_VALUE);
                                    do {
                                        DeviceCharacteristicsValue deviceCharacteristicsValue = new DeviceCharacteristicsValue();
                                        deviceCharacteristicsValue.setChId(cursor1.getLong(chId));
                                        deviceCharacteristicsValue.setChValue(cursor1.getString(chValue));
                                        //Log.d(LOG_TAG, "name = " + cursor.getString(nameIndex) + "param = " + cursor1.getLong(chId));
                                        deviceCharacteristicsValueList.add(deviceCharacteristicsValue);
                                    } while (cursor1.moveToNext());
                                    cursor1.close();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        device.setCharacteristicsList(deviceCharacteristicsValueList);
                        deviceList.add(device);
                        deviceCharacteristicsValueList = new ArrayList<>();
                    } while (cursor.moveToNext());
                } else Log.d(LOG_TAG, "Cursor is null");
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            serviceDbClose();
        }
        return deviceList;
    }


    public void deleteDevice(List<Device> deviceList) {
        List<String> dev_id = new ArrayList<>();
        try {
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            for (Device d : deviceList) {
                dev_id.add(String.valueOf(d.getDevId()));
            }
            sqLiteDatabase.delete(DBHelper.DEVICE, DBHelper.DEV_ID + " = ?", dev_id.toArray(new String[dev_id.size()]));
            sqLiteDatabase.delete(DBHelper.CHARACTERISTICS_VALUE, DBHelper.DEV_ID + " = ? ", dev_id.toArray(new String[dev_id.size()]));
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

}
