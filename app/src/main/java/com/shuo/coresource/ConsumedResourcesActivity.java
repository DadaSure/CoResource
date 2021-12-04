package com.shuo.coresource;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shuo.coresource.data.Card;
import com.shuo.coresource.data.CardDAO;
import com.shuo.coresource.data.DbOpenHelper;

import java.util.List;

public class ConsumedResourcesActivity extends AppCompatActivity {
    private ListView lv_consumedCards;
    private DbOpenHelper dbOpenHelper;
    private CardDAO cardDAO;
    private String userName;
    private List<Card> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumed_cards);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("物资消耗记录");

        lv_consumedCards = findViewById(R.id.lv_consumedCards);
        dbOpenHelper=new DbOpenHelper(this);
        cardDAO=new CardDAO();

        SharedPreferences sharedPreferences=getSharedPreferences("config", Context.MODE_PRIVATE);
        userName=sharedPreferences.getString("username",null);
        Log.i("login","current user: "+userName);


        list=cardDAO.readCompletedCards(dbOpenHelper, userName);
        Log.i("render completedcards", "list size: "+list.size());

        BaseAdapter adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater layoutInflater=LayoutInflater.from(ConsumedResourcesActivity.this);
                View cardView=layoutInflater.inflate(R.layout.card, null);

                TextView tv_card_title=cardView.findViewById(R.id.tv_card_title);
                TextView tv_card_date=cardView.findViewById(R.id.tv_card_date);
                TextView tv_card_count=cardView.findViewById(R.id.tv_card_count);

                tv_card_title.setText(list.get(position).getCard_name());
                tv_card_date.setText(list.get(position).getCard_date());
                tv_card_count.setText(list.get(position).getCard_count()+"");

                return cardView;
            }
        };

        lv_consumedCards.setAdapter(adapter);
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
