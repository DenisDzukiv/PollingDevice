package androidproject.pollingdevice;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidproject.pollingdevice.Service.ServiceDevice;
import androidproject.pollingdevice.activity.DeviceActivity;
import androidproject.pollingdevice.activity.QuizActivity;
import androidproject.pollingdevice.dataBase.DB;
import androidproject.pollingdevice.dataBase.DBHelper;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs";
    ListView lvMain;
    SimpleCursorAdapter scAdapter;
    Cursor cursor;
    ServiceDevice serviceDevice;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvMain = (ListView) findViewById(R.id.lvMain);
        dbHelper = new DBHelper(this);
        serviceDevice = new ServiceDevice(this, dbHelper);
        cursor = serviceDevice.getAllDevice();
        logCursor(cursor);
        startManagingCursor(cursor); // сам закрывает курсор

        String[] from = new String[] {DBHelper.ID, DBHelper.DEV_NAME};
        int[] to = new int[] {R.id.tvType, R.id.tvText};
        scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, from, to);
        serviceDevice.serviceDbClose(); // закрытие БД , делать только после использования курсора
        lvMain.setAdapter(scAdapter);
        registerForContextMenu(lvMain);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
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
            case R.id.addDevice:
                Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
                startActivity(intent);
            case R.id.quiz:
                Intent intentQuiz = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intentQuiz);
        }


        return super.onOptionsItemSelected(item);
    }
}
