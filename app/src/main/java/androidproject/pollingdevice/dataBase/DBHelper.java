package androidproject.pollingdevice.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    final String LOG_TAG = "myLogs";
    int[] typeId = {1, 2, 3};
    String[] typeName = {"DEVICETCP", "DEVICEUSB", "DEVICECOM"};

    int[] CHARACTERISTICSId = {1, 2, 3, 4, 5, 6, 7, 8};
    String[] CHARACTERISTICSName = {"адрес сервера", "порт", "идентификатор производителя", "идентификатор продукта", "скорость опроса", "бит четности", "databit", "stopbit"};

    int[] typeCharacter = {1, 1, 2, 3, 2, 2, 3, 3, 3, 3};
    int[] characterType = {1, 2, 2, 2, 3, 4, 5, 6, 7, 8};

    int[] chbit_id = {1, 2, 3};
    String[] chbit_name = {"четный", "нечетный", "не задано"};


    public static final String ID = "_id";
    public static final String TABLE_TYPE_DEVICE = "TYPE_DEVICE";
    public static final String TYPE_ID = "TYPE_ID";
    public static final String TYPE_NAME = "TYPE_NAME";


    public static final String CHARACTERISTICS = "CHARACTERISTICS";
    public static final String CH_ID = "CH_ID";
    public static final String CH_NAME = "CH_NAME";

    public static final String TYPE_CHARACTERISTICS = "TYPE_CHARACTERISTICS";

    public static final String DEVICE = "DEVICE";
    public static final String DEV_ID = "DEV_ID";
    public static final String DEV_NAME = "DEV_NAME";

    public static final String CHARACTERISTICS_VALUE = "CHARACTERISTICS_VALUE";
    public static final String CH_VALUE = "CH_VALUE";

    public static final String CHARACTERISTICS_BIT = "CHARACTERISTICS_BIT";
    public static final String CHBIT_ID = "CHBIT_ID";
    public static final String CHBIT_NAME = "CHBIT_NAME";


    public DBHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(LOG_TAG, "------Create table TYPE_DEVICE -------");
        ContentValues contentValues = new ContentValues();
        sqLiteDatabase.execSQL("create table if not exists " + TABLE_TYPE_DEVICE + " ("
                + TYPE_ID + "  integer primary key, "
                + TYPE_NAME + "  text"
                + ");");
        for (int i = 0; i < typeId.length; i++) {
            contentValues.clear();
            contentValues.put(TYPE_ID, typeId[i]);
            contentValues.put(TYPE_NAME, typeName[i]);
            sqLiteDatabase.insert("TYPE_DEVICE", null, contentValues);
        }

        Log.d(LOG_TAG, "------Create table CHARACTERISTICS_BIT -------");
        sqLiteDatabase.execSQL("create table if not exists " + CHARACTERISTICS_BIT + " ("
                + CHBIT_ID + "  integer primary key, "
                + CHBIT_NAME + "  text"
                + ");");
        for (int i = 0; i < chbit_id.length; i++) {
            contentValues.clear();
            contentValues.put(CHBIT_ID, chbit_id[i]);
            contentValues.put(CHBIT_NAME, chbit_name[i]);
            sqLiteDatabase.insert("CHARACTERISTICS_BIT", null, contentValues);
        }


        Log.d(LOG_TAG, "------Create table CHARACTERISTICS -------");
        sqLiteDatabase.execSQL("create table if not exists " + CHARACTERISTICS + " ("
                + CH_ID + " integer primary key, "
                + CH_NAME + "  text"
                + ");");
        for (int i = 0; i < CHARACTERISTICSId.length; i++) {
            contentValues.clear();
            contentValues.put(CH_ID, CHARACTERISTICSId[i]);
            contentValues.put(CH_NAME, CHARACTERISTICSName[i]);
            sqLiteDatabase.insert("CHARACTERISTICS", null, contentValues);
        }

        Log.d(LOG_TAG, "------Create table TYPE_CHARACTERISTICS -------");
        sqLiteDatabase.execSQL("create table if not exists " + TYPE_CHARACTERISTICS + "("
                + TYPE_ID + " integer, "
                + CH_ID + "  integer,"
                + " FOREIGN KEY(" + TYPE_ID + ") REFERENCES TYPE_DEVICE(" + TYPE_ID + "),"
                + " FOREIGN KEY(" + CH_ID + ") REFERENCES CHARACTERISTICS(" + CH_ID + ")"
                + ");");
        for (int i = 0; i < typeCharacter.length; i++) {
            contentValues.clear();
            contentValues.put(TYPE_ID, typeCharacter[i]);
            contentValues.put(CH_ID, characterType[i]);
            sqLiteDatabase.insert(TYPE_CHARACTERISTICS, null, contentValues);
        }


        Log.d(LOG_TAG, "------Create table DEVICE -------");
        sqLiteDatabase.execSQL("create table if not exists " + DEVICE + " ("
                + DEV_ID + "  integer primary key, "
                + DEV_NAME + "  text,"
                + TYPE_ID + "  integer,"
                + " FOREIGN KEY(" + TYPE_ID + ") REFERENCES TYPE_DEVICE(" + TYPE_ID + ")"
                + ");");

        Log.d(LOG_TAG, "------Create table CHARACTERISTICS_VALUE -------");
        sqLiteDatabase.execSQL("create table if not exists " + CHARACTERISTICS_VALUE + " ("
                + DEV_ID + "  integer,"
                + CH_ID + "  integer, "
                + CH_VALUE + " text,"
                + " FOREIGN KEY(" + CH_ID + ") REFERENCES CHARACTERISTICS(" + CH_ID + "),"
                + " FOREIGN KEY(" + DEV_ID + ") REFERENCES DEVICE(" + DEV_ID + ")"
                + ");");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
