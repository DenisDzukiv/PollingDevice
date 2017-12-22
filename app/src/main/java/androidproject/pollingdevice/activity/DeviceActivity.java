package androidproject.pollingdevice.activity;


import android.content.Intent;
import android.database.Cursor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidproject.pollingdevice.MainActivity;
import androidproject.pollingdevice.R;
import androidproject.pollingdevice.Service.ServiceDevice;
import androidproject.pollingdevice.Validation.Validation;
import androidproject.pollingdevice.View.EditTextDevice;
import androidproject.pollingdevice.dataBase.DB;
import androidproject.pollingdevice.dataBase.DBHelper;
import androidproject.pollingdevice.model.Device;
import androidproject.pollingdevice.model.DeviceCharacteristicsValue;


public class DeviceActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd, btnEdit;
    EditText etName;
    TextView tv, tvDevId;
    DB db;
    DBHelper dbHelper;
    Cursor cursor, cursorCharacteristics, cursorBit, cursorDevice;
    SimpleCursorAdapter scAdapter, scAdapterBit;
    Spinner spType, spBit;
    final String LOG_TAG = "myLogs";
    LinearLayout linearLayoutEditText;
    List<EditTextDevice> param = new ArrayList<>();
    List<Integer> characteristicsId = new LinkedList<>();
    EditTextDevice et;
    final long chIdBit = 6;
    String bitName, nameBit;
    public static final String DEVICE = "device";
    Device device;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(this);

        spType = (Spinner) findViewById(R.id.spinner);
        spBit = (Spinner) findViewById(R.id.spBit);
        etName = (EditText) findViewById(R.id.etName);

        tvDevId = (TextView) findViewById(R.id.devId);

        //TODO Передалать доступ к БД, и выводом данных как для Device  в MainActivity
        dbHelper = new DBHelper(this);
        db = new DB(this, dbHelper);

        cursor = db.getType();
        startManagingCursor(cursor);
        String[] from = new String[]{DBHelper.TYPE_NAME, DBHelper.ID};
        int[] to = new int[]{R.id.text, R.id.id};
        scAdapter = new SimpleCursorAdapter(this, R.layout.item_type_spinner, cursor, from, to);
        scAdapter.setDropDownViewResource(R.layout.item_type_spinner);
        spType.setAdapter(scAdapter);
        //редактирование
        device = (Device) getIntent().getSerializableExtra(DEVICE);


        if ((device.getDevName() != null)) {
           // Log.d("mylog", String.valueOf(device.getDevId()));
            tvDevId.setText(String.valueOf(device.getDevId()));
            etName.setText(device.getDevName());
            spType.setSelection(((int) device.getTypeDevice().getTypeId()) - 1);
            btnAdd.setVisibility(View.GONE);
            btnEdit.setVisibility(View.VISIBLE);
        }

        registerForContextMenu(spType);

        cursorBit = db.getBit();
        startManagingCursor(cursorBit);
        String[] fromBit = new String[]{DBHelper.CHBIT_NAME, DBHelper.ID};
        int[] toBit = new int[]{R.id.textBit, R.id.idBit};
        scAdapterBit = new SimpleCursorAdapter(this, R.layout.item_bit, cursorBit, fromBit, toBit);
        spBit.setAdapter(scAdapterBit);
        registerForContextMenu(spBit);

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                cursorCharacteristics = db.getCharacteristics(spType.getSelectedItemId());
                createEditTextView(cursorCharacteristics, device);
                cursorCharacteristics.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spBit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                tv = (TextView) view.findViewById(R.id.textBit);
                bitName = String.valueOf(tv.getText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    void createEditTextView(Cursor cursor, Device device) {
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
                nameBit = null;
                do {
                    characteristicsId.add(cursor.getInt(idIndex));
                    if (cursor.getInt(idIndex) == chIdBit) {
                        spBit.setVisibility(View.VISIBLE);

                        if ((device.getDevName() != null)) {
                            for (DeviceCharacteristicsValue dv : device.getCharacteristicsList()) {
                                if (dv.getChId() == chIdBit)
                                    spBit.setSelection(((int) dv.getChId()));
                            }
                        } else spBit.setSelection(2);
                        nameBit = bitName;
                    } else {
                        et = new EditTextDevice(DeviceActivity.this);
                        et.setLayoutParams(lEditParams);
                        et.setHint(cursor.getString(nameIndex));
                        et.setId(cursor.getInt(idIndex));
                        if ((device.getDevName() != null)) {
                            for (DeviceCharacteristicsValue dv : device.getCharacteristicsList()) {
                                if (dv.getChId() == cursor.getInt(idIndex)) {
                                    et.setText(dv.getChValue());
                                }
                            }
                        }
                        linearLayoutEditText.addView(et);
                        param.add(et);
                    }
                } while (cursor.moveToNext());
            } else Log.d(LOG_TAG, "Cursor is null");
        }
    }

    @Override
    public void onClick(View v) {
        DeviceCharacteristicsValue characteristics;
        List<DeviceCharacteristicsValue> characteristicsList = new ArrayList<>();
        Device deviceAdd = new Device();
        ServiceDevice serviceDevice = new ServiceDevice(DeviceActivity.this, dbHelper);


        //etName = (EditText) findViewById(R.id.etName);
        deviceAdd.setDevName(etName.getText().toString());


        spType = (Spinner) findViewById(R.id.spinner);
        deviceAdd.setTypeId(spType.getSelectedItemId());

        if (nameBit != null) {
            characteristics = new DeviceCharacteristicsValue();
            characteristics.setChId(chIdBit);
            characteristics.setChValue(bitName);
            characteristicsList.add(characteristics);
        }

        for (EditTextDevice ett : param) {
            characteristics = new DeviceCharacteristicsValue();
            characteristics.setChId(ett.getId());
            characteristics.setChValue(ett.getText().toString());
            characteristicsList.add(characteristics);
        }
        deviceAdd.setCharacteristicsList(characteristicsList);
        if (!Validation.validDevice(deviceAdd)) {
            Toast.makeText(getBaseContext(), "Данные введены не корректно!", Toast.LENGTH_SHORT).show();
        } else {
            switch (v.getId()) {
                case R.id.btnAdd:
                    serviceDevice.save(deviceAdd);
                    Toast.makeText(getBaseContext(), "Устройство добавлено", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnEdit:
                    deviceAdd.setDevId(Long.parseLong(String.valueOf(tvDevId.getText())));
                    serviceDevice.update(deviceAdd);
                    break;
                    //cursor = serviceDevice.getCharValue();
                    //cursor.close();
            }

            Intent intent = new Intent(DeviceActivity.this, MainActivity.class);
            startActivity(intent);

        }

    }

    void logCursor(Cursor cursor) {
        Log.d(LOG_TAG, "----- logCursor3  ----");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.dbClose();
    }
}
