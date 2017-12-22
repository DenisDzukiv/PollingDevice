package androidproject.pollingdevice;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidproject.pollingdevice.Adapters.DeviceAdapter;
import androidproject.pollingdevice.Service.ServiceDevice;
import androidproject.pollingdevice.Service.ServiceType;
import androidproject.pollingdevice.activity.DevActivity;
import androidproject.pollingdevice.activity.DeviceActivity;
import androidproject.pollingdevice.activity.QuizActivity;
import androidproject.pollingdevice.dataBase.DB;
import androidproject.pollingdevice.dataBase.DBHelper;
import androidproject.pollingdevice.model.Device;
import androidproject.pollingdevice.model.TypeDevice;

import android.widget.AdapterView.OnItemSelectedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DeviceAdapter.CalbackDevice,  View.OnClickListener   {

    static final String LOG_TAG = "myLogs";
    ListView lvMain;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;

    DBHelper dbHelper;
    TextView textDevice;
    Intent intent;
    String[] names;

    ServiceDevice serviceDevice;
    ServiceType serviceType;
    List<Device> deviceList = new ArrayList<>();
    DeviceAdapter deviceAdapter;
    Button btnQuiz, btnDelDevice;
    Image img;
    List<TypeDevice> typeDeviceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //lvMain.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        dbHelper = new DBHelper(this);
        serviceDevice = new ServiceDevice(this, dbHelper);
        deviceList = serviceDevice.devices();

        /*serviceType = new ServiceType(this, dbHelper);
        typeDeviceList = serviceType.allTypeDevices();*/


        deviceAdapter = new DeviceAdapter(this, deviceList);
        deviceAdapter.setCallback(this);
        lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setAdapter(deviceAdapter);

        btnQuiz = (Button) findViewById(R.id.quiz);
        btnQuiz.setOnClickListener(this);

        btnDelDevice = (Button) findViewById(R.id.delDevice);
        btnDelDevice.setOnClickListener(this);

        /*cursor = serviceDevice.getAllDevice();
        logCursor(cursor);
        startManagingCursor(cursor); // сам закрывает курсор

        String[] from = new String[] {DBHelper.ID, DBHelper.DEV_NAME};
        int[] to = new int[] {R.id.tvType, R.id.tvText};
        scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to);
        serviceDevice.serviceDbClose(); // закрытие БД , делать только после использования курсора
        lvMain.setAdapter(scAdapter);
        registerForContextMenu(lvMain);*/

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {


                String result1 = "Удаленные устройства:";
                /*for (Device p : deviceAdapter.gettBox()) {
//                    if (p.getBox())
                    result1 += "\n" + p.getDevName();
                }*/
               // Toast.makeText(this, result1, Toast.LENGTH_LONG).show();
             //   Log.d(LOG_TAG, "lvMain.setOnItemClickListener");
                Toast.makeText(getApplicationContext(),  "toast" ,
                        Toast.LENGTH_SHORT).show();
            }
        });




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quiz:
                // Подключение произошло успешно
                if(connectDevice()) {
                    if (quizDevice()) {
                        //опрос произошел успешно
                        intent = new Intent(MainActivity.this, QuizActivity.class);
                        intent.putExtra(QuizActivity.DEVICE ,deviceAdapter.gettBox());
                        startActivity(intent);
                    } else
                        Toast.makeText(this, "При опросе устройств произошла ошибка", Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(this, "При подключении к устройству произошла ошибка", Toast.LENGTH_LONG).show();
                break;
            case R.id.delDevice:
                String result1 = "Удаленные устройства:";
                for (Device p : deviceAdapter.gettBox()) {
//                    if (p.getBox())
                        result1 += "\n" + p.getDevName();
                }
                Toast.makeText(this, result1, Toast.LENGTH_LONG).show();
                break;
        }
    }

    void logCursor(Cursor cursor){
        Log.d(LOG_TAG, "----- logCursor2  ----");
        if(cursor != null) {
            if(cursor.moveToFirst()){
                String str;
                do{
                    str = "";
                    for (String cn:cursor.getColumnNames())
                        str = str.concat(cn + " = " + cursor.getString(cursor.getColumnIndex(cn)) + "; ");
                    Log.d(LOG_TAG, str);
                } while (cursor.moveToNext());
            } else Log.d(LOG_TAG, "Cursor is null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                finish();
                //onBackPressed();
                break;
            case R.id.addDevice:
                callbackCall(new Device());
               /* intent = new Intent(MainActivity.this, DeviceActivity.class);
                startActivity(intent);*/
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public Boolean connectDevice(){
        return true;
    }
    public Boolean quizDevice() {
        return true;
    }


    @Override
    public void callbackCall(Device device) {
        intent = new Intent(MainActivity.this, DeviceActivity.class);
        intent.putExtra(DeviceActivity.DEVICE, device);
        startActivity(intent);
    }
}