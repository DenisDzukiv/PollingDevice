package androidproject.pollingdevice.activity;


import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidproject.pollingdevice.MainActivity;
import androidproject.pollingdevice.R;


public class QuizActivity extends AppCompatActivity  {

    Toolbar toolbar;
    String user = "ЖЫвотное";
    Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);



        user = getIntent().getExtras().getString("username");
        Log.d("myLog", user);



        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(R.string.quiz);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                intent = new Intent(QuizActivity.this, MainActivity.class);
                startActivity(intent);
               //onBackPressed();// возврат на предыдущий activity
            }
        });
    }




}