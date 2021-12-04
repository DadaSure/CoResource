package com.shuo.coresource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shuo.coresource.data.CardDAO;
import com.shuo.coresource.data.DbOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AddResourceActivity extends AppCompatActivity {
    private EditText et_create_title;
    private EditText et_create_description;
    private EditText et_create_count;
    private DbOpenHelper dbOpenHelper;
    private CardDAO cardDAO;
    private Button btn_create_submit;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("添加物资");

        et_create_title=findViewById(R.id.et_create_title);
        et_create_description=findViewById(R.id.et_create_description);
        et_create_count=findViewById(R.id.et_create_count);
        btn_create_submit=findViewById(R.id.btn_create_submit);

        dbOpenHelper = new DbOpenHelper(this);
        cardDAO = new CardDAO();

        btn_create_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createcard();
            }
        });

        SharedPreferences sharedPreferences=getSharedPreferences("config", Context.MODE_PRIVATE);
        userName=sharedPreferences.getString("username","null");
        Log.i("login","current user: "+userName);
    }


    private void createcard(){
        String cardName=et_create_title.getText().toString();
        String cardDescription=et_create_description.getText().toString();
        int cardcount;

        if(et_create_count.getText().toString().length()>5){
            cardcount=-1;
        }else {
            cardcount=Integer.parseInt(et_create_count.getText().toString());
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        String cardDate = simpleDateFormat.format(date);

        boolean isCompleted=false;


        boolean iscardNameValid=true;
        boolean iscardDescriptionValid=true;
        boolean iscardcountValid=true;

        if(cardName.contains("*") || cardName.contains("#") || cardName.contains("!") || cardName.contains("@") || cardName.contains("$") || cardName.contains(" ") || cardName.contains("&") || cardName.length()==0 || userName.length()>30){
            iscardNameValid=false;
        }

        if(cardDescription.contains("*") || cardDescription.contains("#") || cardDescription.contains("!") || cardDescription.contains("@") || cardDescription.contains("$") || cardDescription.contains(" ") || cardDescription.contains("&") || cardDescription.length()==0 || cardDescription.length()>30){
            iscardDescriptionValid=false;
        }

        if(cardcount>99999||cardcount<0){
            iscardcountValid=false;
        }

        Log.i("create card",cardName);
        Log.i("create card",cardDescription);
        Log.i("create card",String.valueOf(cardcount));
        Log.i("create card",cardDate);


        boolean isValid=(iscardNameValid&&iscardDescriptionValid&&iscardcountValid);

        if(isValid){
            Toast.makeText(this,"添加物资成功！",new Integer(2)).show();

            cardDAO.createCard(dbOpenHelper,userName, cardName, cardDescription, cardDate, cardcount, 0);

            Log.i("create card","success");


            TimerTask card=new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(AddResourceActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            };

            Timer timer=new Timer();
            timer.schedule(card,2100);

        }else {
            Toast.makeText(this,"输入格式有误！",new Integer(2)).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
