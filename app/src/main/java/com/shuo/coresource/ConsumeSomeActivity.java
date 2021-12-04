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

import com.shuo.coresource.data.Card;
import com.shuo.coresource.data.CardDAO;
import com.shuo.coresource.data.DbOpenHelper;

import java.util.List;

public class ConsumeSomeActivity extends AppCompatActivity {

    private int cardId;
    private int getCount;
    private String userName;

    private DbOpenHelper dbOpenHelper;
    private CardDAO cardDAO;

    private EditText et_completeSome_title;
    private EditText et_completeSome_description;
    private EditText et_completeSome_date;
    private EditText et_completeSome_Count;

    private Button btn_detail_completeSome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("消耗部分物资");

        Intent intent = getIntent();
        cardId=intent.getIntExtra("cardId",-1);
        Log.i("passing id","get cardId="+cardId);
        getCount=intent.getIntExtra("getCount",0);

        SharedPreferences sharedPreferences=getSharedPreferences("config", Context.MODE_PRIVATE);
        userName=sharedPreferences.getString("username",null);
        Log.i("login","current user: "+userName);

        dbOpenHelper = new DbOpenHelper(this);
        cardDAO = new CardDAO();

        et_completeSome_title=findViewById(R.id.et_consumeSome_title);
        et_completeSome_description=findViewById(R.id.et_consumeSome_description);
        et_completeSome_date=findViewById(R.id.et_consumeSome_date);
        et_completeSome_Count=findViewById(R.id.et_consumeSome_Count);
        btn_detail_completeSome=findViewById(R.id.btn_completeSome_complete);

        et_completeSome_title.setKeyListener(null);
        et_completeSome_description.setKeyListener(null);
        et_completeSome_date.setKeyListener(null);

        btn_detail_completeSome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete_some();
            }
        });
        fillIn();
    }

    private void complete_some(){

//        int consumeCount = et_completeSome_Count.;
//
//        cardDAO.deletecard(dbOpenHelper, cardId);
//        Toast.makeText(this,"物资已删除！",new Integer(2)).show();
//
//        Log.i("card detail","card deleted");
//
//        Timercard card=new Timercard() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(ConsumeSomeActivity.this,MainActivity.class);
//                startActivity(intent);
//            }
//        };
//
//        Timer timer=new Timer();
//        timer.schedule(card,2100);
    }

    private void fillIn(){
        List<Card> list = cardDAO.readIdCard(dbOpenHelper, cardId);
        Card card = list.get(0);

        et_completeSome_title.setText(card.getCard_name());
        et_completeSome_description.setText(card.getCard_description());
        et_completeSome_date.setText(card.getCard_date());
        et_completeSome_Count.setText(card.getCard_count()+"");
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
