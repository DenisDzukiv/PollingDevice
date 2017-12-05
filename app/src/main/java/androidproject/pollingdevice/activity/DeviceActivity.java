package androidproject.pollingdevice.activity;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidproject.pollingdevice.R;
import androidproject.pollingdevice.View.EditTextDevice;
import androidproject.pollingdevice.dataBase.DB;
import androidproject.pollingdevice.dataBase.DBHelper;


public class DeviceActivity extends AppCompatActivity {
    Button btnAdd, btnDel, btnEdit;
    EditText etName;
    DB db;
    Cursor cursor, cursorCharacteristics, cursorAllCharacteristics;
    SimpleCursorAdapter scAdapter;
    Spinner spType;
    final String LOG_TAG = "myLogs";
    LinearLayout linearLayoutEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        spType = (Spinner) findViewById(R.id.spinner);
        db = new DB(this);
        db.dbOpen();
        cursor = db.getType();
        logCursor(cursor);
        String[] from = new String[]{DBHelper.TYPE_NAME, DBHelper.ID};
        scAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, cursor, from, new int[]{android.R.id.text1, android.R.id.text2});
        scAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(scAdapter);
        db.dbClose();
        //spType.setPrompt("Вид оборудования");

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                db = new DB(DeviceActivity.this);
                db.dbOpen();
                //cursorCharacteristics = db.getCharacteristics(spType.getSelectedItemId());
                cursorAllCharacteristics = db.getAllCharacteristics();
                logCursor(cursorAllCharacteristics);
                createEditTextView(cursorAllCharacteristics);

                cursorAllCharacteristics.close();
                db.dbClose();
                Log.d(LOG_TAG, "11l");


                //linearLayoutEditText = (LinearLayout) findViewById(R.id.EditText);

                // создание полей для параметров

                //Toast.makeText(getBaseContext(), "Position = " + cursorCharacteristics.getCount(), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    void createEditTextView(Cursor cursor) {
        Log.d(LOG_TAG, "----- 333 ----");
        if (cursor != null) {
            /*etType = (EditText) findViewById(R.id.etNe);
            etType.setVisibility(View.GONE);*/
            /*if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(DBHelper.ID);
                int nameIndex = cursor.getColumnIndex(DBHelper.CH_NAME);
                do {

                    EditText et = new EditText(DeviceActivity.this);
                    et.setLayoutParams(lEditParams);
                    et.setId(cursor.getInt(idIndex));

                    et.setHint(cursor.getString(nameIndex));

                    linearLayoutEditText.addView(et);
                    Log.d(LOG_TAG, "----- cursor.getString(nameIndex) ----");
                } while (cursor.moveToNext());
            } else Log.d(LOG_TAG, "Cursor is null");
            */


            linearLayoutEditText = (LinearLayout) findViewById(R.id.EditText);
            LinearLayout.LayoutParams lEditParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(DBHelper.ID);
                int nameIndex = cursor.getColumnIndex(DBHelper.CH_NAME);
                do {
                    EditTextDevice et = new EditTextDevice(DeviceActivity.this);
                    et.setLayoutParams(lEditParams);
                    et.setHint(cursor.getString(nameIndex));
                    et.setId(cursor.getInt(idIndex));
                    linearLayoutEditText.addView(et);
                    Log.d(LOG_TAG, "----- cursor.getString(nameIndex) ----");
                } while (cursor.moveToNext());
            } else Log.d(LOG_TAG, "Cursor is null");
        }
    }

    void logCursor(Cursor cursor) {
        Log.d(LOG_TAG, "----- logCursor1 ----");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String str;
                do {
                    str = "";
                    for (String cn : cursor.getColumnNames())
                        str = str.concat(cn + " = " + cursor.getString(cursor.getColumnIndex(cn)) + "; ");
                    Log.d(LOG_TAG, str);
                } while (cursor.moveToNext());
            } else Log.d(LOG_TAG, "Cursor is null");
        }
    }
}
