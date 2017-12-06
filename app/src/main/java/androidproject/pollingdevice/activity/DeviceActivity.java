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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidproject.pollingdevice.R;
import androidproject.pollingdevice.View.EditTextDevice;
import androidproject.pollingdevice.dataBase.DB;
import androidproject.pollingdevice.dataBase.DBHelper;


public class DeviceActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd, btnDel, btnEdit;
    EditText etName;
    DB db;
    Cursor cursor, cursorCharacteristics, cursorAllCharacteristics, cursorBit;
    SimpleCursorAdapter scAdapter, scAdapterBit;
    Spinner spType, spBit;
    final String LOG_TAG = "myLogs";
    LinearLayout linearLayoutEditText;
    List<EditTextDevice> param = new ArrayList<>();
    List<Integer> characteristicsId = new LinkedList<>();
    EditTextDevice et;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        spType = (Spinner) findViewById(R.id.spinner);
        spBit = (Spinner) findViewById(R.id.spBit);
        db = new DB(this);
        db.dbOpen();
        cursor = db.getType();
        String[] from = new String[]{DBHelper.TYPE_NAME, DBHelper.ID};
        int[] to = new int[]{R.id.text, R.id.id};
        scAdapter = new SimpleCursorAdapter(this, R.layout.item_type_spinner, cursor, from, to /*new int[]{android.R.id.text1, android.R.id.text2}*/    );
        scAdapter.setDropDownViewResource(R.layout.item_type_spinner);
        spType.setAdapter(scAdapter);

        cursorBit = db.getBit();
        String[] fromBit = new String[]{DBHelper.CHBIT_NAME, DBHelper.ID};
        int[] toBit = new int[]{R.id.textBit, R.id.idBit};
        scAdapterBit = new SimpleCursorAdapter(this, R.layout.item_bit, cursorBit, fromBit, toBit);
        spBit.setAdapter(scAdapterBit);
        spBit.setSelection(2);
        db.dbClose();
        //spType.setPrompt("Вид оборудования");


        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
//                db = new DB(DeviceActivity.this);
                db.dbOpen();
                cursorCharacteristics = db.getCharacteristics(spType.getSelectedItemId());
               /* String[] from = new String[] {DBHelper.ID, DBHelper.CH_NAME};
                int[] to = new int[] {R.id.tvId, R.id.edParam};

                scAdapter = new SimpleCursorAdapter(DeviceActivity.this, R.layout.layer_param, cursorCharacteristics, from, to);
                lvDevice = (ListView) findViewById(R.id.lvDevice);
                lvDevice.setAdapter(scAdapter);*/

                //cursorAllCharacteristics = db.getAllCharacteristics();
                //logCursor(cursorAllCharacteristics);

                createEditTextView(cursorCharacteristics);
                cursorCharacteristics.close();
                db.dbClose();
                //linearLayoutEditText = (LinearLayout) findViewById(R.id.EditText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spBit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    void createEditTextView(Cursor cursor) {
        if (cursor != null) {
            linearLayoutEditText = (LinearLayout) findViewById(R.id.EditText);
            linearLayoutEditText.removeAllViews();
            LinearLayout.LayoutParams lEditParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(DBHelper.ID);
                int nameIndex = cursor.getColumnIndex(DBHelper.CH_NAME);
                spBit = (Spinner) findViewById(R.id.spBit);
                spBit.setVisibility(View.GONE);
                characteristicsId.clear();
                param.clear();

                do {
                    characteristicsId.add(cursor.getInt(idIndex));
                    if(cursor.getInt(idIndex) == 6) {
                        spBit.setVisibility(View.VISIBLE);
                    }
                    else {
                        et = new EditTextDevice(DeviceActivity.this);
                        et.setLayoutParams(lEditParams);
                        et.setHint(cursor.getString(nameIndex));
                        et.setId(cursor.getInt(idIndex));
                        linearLayoutEditText.addView(et);
                        param.add(et);
                    }

                } while (cursor.moveToNext());
            } else Log.d(LOG_TAG, "Cursor is null");
        }
    }

    @Override
    public void onClick(View v) {
        etName = (EditText) findViewById(R.id.etName);
        spType = (Spinner) findViewById(R.id.spinner);
        for(EditTextDevice ett:param){
            if (ett.getText().length() == 0){
                Log.d(LOG_TAG, "is null");
            }

        }
    }
}
