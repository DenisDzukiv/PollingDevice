package androidproject.pollingdevice.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

import androidproject.pollingdevice.MainActivity;
import androidproject.pollingdevice.R;
import androidproject.pollingdevice.model.Device;




public class QuizActivity extends AppCompatActivity implements  View.OnClickListener{

    Toolbar toolbar;
    public static final String DEVICE = "devices";
    public static final int typeTCP = 1;
    public static final int typeUSB = 2;
    public static final int typeCOM = 3;
    ListView lvQuiz;
    SharedPreferences sPref;
    ArrayAdapter<String> adapter;
    Button btnSave, btnLoad;
    int circle;
    public static final String COUNTQUIZ = "COUNTQUIZ";
    ArrayList<String> devQuizList;
    GenerateDeviceCom threadDeviceCom;
    Intent intent;



    class GenerateDeviceCom extends Thread {
        private ArrayList<String> devTypeComList;
        String name;
        public GenerateDeviceCom(String devName){
            this.name = devName;
        }
        @Override
        public void run()	//Этот метод будет выполнен в побочном потоке
        {
            devTypeComList = new ArrayList<>();
            for (int i = 1; i < 13; i++) {
                devTypeComList.add(name + "DeviceCOM" + "_val" + i);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            devQuizList.addAll(devTypeComList);
            System.out.println("Привет из побочного потока!");
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);



        btnSave = (Button) findViewById(R.id.save);
        btnSave.setOnClickListener(this);

        btnLoad = (Button) findViewById(R.id.load);
        btnLoad.setOnClickListener(this);

        //Device device = (Device) getIntent().getSerializableExtra(DEVICE);
        ArrayList<Device> deviceList = (ArrayList<Device>) getIntent().getSerializableExtra(DEVICE);
        devQuizList = new ArrayList<>();
        //формирование параметров опроса
        for (Device d : deviceList) {
            if (d.getTypeDevice().getTypeId() == typeTCP) {
                for (int i = 1; i < 6; i++) {
                    devQuizList.add(d.getDevName() + "DeviceTCP" + "_val" + i);
                }
            } else if (d.getTypeDevice().getTypeId() == typeUSB) {
                for (int i = 1; i < 3; i++) {
                    devQuizList.add(d.getDevName() + "DeviceUSB" + "_val" + i);
                }
            } else if (d.getTypeDevice().getTypeId() == typeCOM) {
                //создаю потом для фомриования с задержкой
                threadDeviceCom = new GenerateDeviceCom(d.getDevName());
                threadDeviceCom.start();
                try {
                    threadDeviceCom.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /*for (int i = 1; i < 13; i++) {
                    devQuizList.add(d.getDevName() + "DeviceCOM" + "_val" + i);
                }*/

            }
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, devQuizList);
        loadQuiz();
        lvQuiz = (ListView) findViewById(R.id.lvQuiz);
        lvQuiz.setAdapter(adapter);
        sPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(R.string.quiz);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*intent = new Intent(QuizActivity.this, MainActivity.class);
                startActivity(intent);*/
                onBackPressed();// возврат на предыдущий activity
            }
        });
    }



    @Override
    public void onClick(View view) {
        Log.d("mylog", "in ");

        switch (view.getId()){
            case R.id.save:
                saveQuiz();
                break;
            case R.id.load:
                loadQuiz();
                break;
        }
    }

    private void saveQuiz(){
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.clear();
        for(int i=0; i < adapter.getCount(); i++){
            editor.putString(String.valueOf(i), adapter.getItem(i));
        }
        circle++;
        editor.putInt(COUNTQUIZ, circle);
        Log.d("mylog", "save " + String.valueOf(adapter.getCount()));
        editor.commit();
    }

    private void loadQuiz(){
        sPref = getPreferences(MODE_PRIVATE);
        circle = sPref.getInt(COUNTQUIZ, 0);
        Log.d("mylog", "circle " + circle);

        if(circle < 3) {
            for (int i = 0; ; ++i) {
                final String str = sPref.getString(String.valueOf(i), "");
                if (!str.equals("")) {
                    adapter.add(str);
                } else {
                    break;
                }
            }
        }
        else {
            Log.d("mylog", "clear ");
            clear();
        }
    }

    private void clear(){
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        circle = 0;
        editor.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveQuiz();
    }



}