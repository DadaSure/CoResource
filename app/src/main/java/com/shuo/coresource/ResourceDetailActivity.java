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

import com.shuo.coresource.data.Card;
import com.shuo.coresource.data.CardDAO;
import com.shuo.coresource.data.DbOpenHelper;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ResourceDetailActivity extends AppCompatActivity {
    private int cardId;
    private int getCount;
    private String userName;

    private DbOpenHelper dbOpenHelper;
    private CardDAO cardDAO;

    private EditText et_detail_title;
    private EditText et_detail_description;
    private EditText et_detail_date;
    private EditText et_detail_count;

    private Button btn_detail_complete;
    private Button btn_detail_delete;
    private Button btn_detail_completeSome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("物资详情");

        Intent intent = getIntent();
        cardId=intent.getIntExtra("cardId",-1);
        Log.i("passing id","get cardId="+cardId);
        getCount=intent.getIntExtra("getCount",0);

        SharedPreferences sharedPreferences=getSharedPreferences("config", Context.MODE_PRIVATE);
        userName=sharedPreferences.getString("username",null);
        Log.i("login","current user: "+userName);

        dbOpenHelper = new DbOpenHelper(this);
        cardDAO = new CardDAO();

        et_detail_title=findViewById(R.id.et_detail_title);
        et_detail_description=findViewById(R.id.et_detail_description);
        et_detail_date=findViewById(R.id.et_detail_date);
        et_detail_count=findViewById(R.id.et_detail_count);
        btn_detail_delete=findViewById(R.id.btn_detail_delete);
        btn_detail_complete=findViewById(R.id.btn_detail_complete);
        btn_detail_completeSome=findViewById(R.id.btn_detail_complete2);

        et_detail_title.setKeyListener(null);
        et_detail_count.setKeyListener(null);
        et_detail_description.setKeyListener(null);
        et_detail_date.setKeyListener(null);

        btn_detail_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete();
            }
        });

        btn_detail_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        btn_detail_completeSome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete_some();
            }
        });
        fillIn();
    }

    private void complete(){
        cardDAO.completeCard(dbOpenHelper, cardId, userName, getCount);
        Toast.makeText(this,"物资已全部消耗！",new Integer(2)).show();

        Log.i("card detail","card completed");

        TimerTask card=new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(ResourceDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        };

        Timer timer=new Timer();
        timer.schedule(card,2100);
    }

    private void complete_some(){
        Intent intent = new Intent(ResourceDetailActivity.this, ConsumeSomeActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("cardId",cardId);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void delete(){
        cardDAO.deleteCard(dbOpenHelper, cardId);
        Toast.makeText(this,"物资已删除！",new Integer(2)).show();

        Log.i("card detail","card deleted");

        TimerTask card=new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(ResourceDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        };

        Timer timer=new Timer();
        timer.schedule(card,2100);
    }

    private void fillIn(){
        List<Card> list = cardDAO.readIdCard(dbOpenHelper, cardId);
        Card card = list.get(0);

        et_detail_title.setText(card.getCard_name());
        et_detail_description.setText(card.getCard_description());
        et_detail_date.setText(card.getCard_date());
        et_detail_count.setText(card.getCard_count()+"");
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
