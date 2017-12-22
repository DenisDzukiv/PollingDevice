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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import androidproject.pollingdevice.Adapters.TypeAdapter;
import androidproject.pollingdevice.R;
import androidproject.pollingdevice.Service.ServiceType;
import androidproject.pollingdevice.View.EditTextDevice;
import androidproject.pollingdevice.dataBase.DB;
import androidproject.pollingdevice.dataBase.DBHelper;
import androidproject.pollingdevice.model.Device;
import androidproject.pollingdevice.model.TypeDevice;

public class DevActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd, btnDel, btnEdit;
    EditText etName;
    TextView tv;
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
    ServiceType serviceType;
    List<TypeDevice> typeDevicesList;
    TypeAdapter typeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        spType = (Spinner) findViewById(R.id.spinner);
        spBit = (Spinner) findViewById(R.id.spBit);
        etName = (EditText) findViewById(R.id.etName);

        dbHelper = new DBHelper(this);
        serviceType = new ServiceType(this, dbHelper);

        typeDevicesList = serviceType.allTypeDevices();
        typeAdapter = new TypeAdapter(this, R.layout.item_type_spinner, typeDevicesList);
        spType.setSelection(2, true);
        spType.setAdapter(typeAdapter);



        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
    @Override
    public void onClick(View view) {

    }
}
