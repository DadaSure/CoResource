package com.shuo.coresource;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shuo.coresource.data.Card;
import com.shuo.coresource.data.CardDAO;
import com.shuo.coresource.data.DbOpenHelper;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardsFragment extends Fragment {
    private ListView lv_cards;
    private DbOpenHelper dbOpenHelper;
    private CardDAO cardDAO;
    private String userName;
    private List<Card> list;


    public CardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View content = inflater.inflate(R.layout.fragment_cards, container, false);

        lv_cards = content.findViewById(R.id.lv_cards);
        dbOpenHelper=new DbOpenHelper(getContext());
        cardDAO=new CardDAO();

        SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        userName=sharedPreferences.getString("username",null);
        Log.i("login","current user: "+userName);

        return content;
    }

    @Override
    public void onResume() {
        super.onResume();

        list=cardDAO.readCards(dbOpenHelper, userName);
        Log.i("render cards", "list size: "+list.size());

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
                LayoutInflater layoutInflater=LayoutInflater.from(getContext());
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

        lv_cards.setAdapter(adapter);

        lv_cards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getActivity(), ResourceDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("cardId",list.get(position).getCard_id());
                bundle.putInt(("getPotato"),list.get(position).getCard_count());
                intent.putExtras(bundle);
                Log.i("passing id","position: "+ position +" cardId: " + list.get(position).getCard_id());
                startActivity(intent);
            }
        });
    }
}
