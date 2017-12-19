package androidproject.pollingdevice.Service;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import androidproject.pollingdevice.dataBase.DBHelper;
import androidproject.pollingdevice.model.Characteristics;
import androidproject.pollingdevice.model.TypeDevice;

public class ServiceType {
    private final Context mCtx;
    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    final String LOG_TAG = "myLogs";
    Cursor cursor, cursor1;

    public ServiceType(Context ctx, DBHelper db) {
        mCtx = ctx;
        dbHelper = db;
    }

    public void serviceDbClose() {
        sqLiteDatabase.close();
    }

    public List<TypeDevice> allTypeDevices() {
        List<TypeDevice> listType = new ArrayList<>();
        List<Characteristics> characteristicsList = new ArrayList<>();
        String sqlQuery = null;
        try {
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sqlQuery = " select t." + DBHelper.TYPE_NAME + " , t." + DBHelper.TYPE_ID + " as _id, c." + DBHelper.CH_ID + " , c." + DBHelper.CH_NAME
                    + " from " + DBHelper.TABLE_TYPE_DEVICE + " as t, "
                    + DBHelper.TYPE_CHARACTERISTICS + " as tc, " + DBHelper.CHARACTERISTICS + " as c "
                    + " where t." + DBHelper.TYPE_ID + " = " + " tc." + DBHelper.TYPE_ID
                    + " and tc." + DBHelper.CH_ID + " = " + " c." + DBHelper.CH_ID
                    + " order by  " + DBHelper.ID
            ;

            cursor = sqLiteDatabase.rawQuery(sqlQuery, null);
            cursor1 = cursor;
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBHelper.ID);
                    int typeNameIndex = cursor.getColumnIndex(DBHelper.TYPE_NAME);
                    int chIdIndex = cursor.getColumnIndex(DBHelper.CH_ID);
                    int chNameIndex = cursor.getColumnIndex(DBHelper.CH_NAME);
                    long typeId = 0;
                    TypeDevice typeDevice;
                    Characteristics characteristics;
                    do {
                        typeDevice = new TypeDevice();
                        if (typeId != cursor.getLong(idIndex)) {
                            typeDevice.setTypeId(cursor.getLong(idIndex));
                            typeDevice.setTypeName(cursor.getString(typeNameIndex));
                            //
                            /*do {
                                characteristics = new Characteristics();
                                characteristics.setChId(cursor1.getLong(chIdIndex));
                                characteristics.setChName(cursor1.getString(chNameIndex));
                                characteristicsList.add(characteristics);
                                Log.d("mylog", "cursor.getLong(chIdIndex)" + cursor.getLong(chIdIndex));
                            } while (cursor1.moveToNext());*/
                            //
                            //typeDevice.setCharacteristicsList(characteristicsList);
                            //typeDevice.setCharacteristicsList(chList(cursor1, cursor.getLong(idIndex)));
                            //Log.d("mylog", "after = " + String.valueOf(cursor.getLong(idIndex)));

                            typeId = cursor.getLong(idIndex);
                        }
                        if (typeId == cursor.getLong(idIndex)){
                            characteristics = new Characteristics();
                            characteristics.setChId(cursor.getLong(chIdIndex));
                            characteristics.setChName(cursor.getString(chNameIndex));
                            //Log.d("mylog", "cursor.getLong(chIdIndex) = " + String.valueOf(cursor.getLong(chIdIndex)));
                            characteristicsList.add(characteristics);
                        }
                        if (typeId != cursor.getLong(idIndex)) {
                            typeDevice.setCharacteristicsList(characteristicsList);
                            listType.add(typeDevice);
                        }
                    } while (cursor.moveToNext());

                } else Log.d(LOG_TAG, "Cursor is null");
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            serviceDbClose();
        }
        return listType;
    }

}
