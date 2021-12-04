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
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {
    private String userName;
    private Button btn_consumedCards;
    private TextView tv_me_username;


    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View content = inflater.inflate(R.layout.fragment_me, container, false);

        btn_consumedCards=content.findViewById(R.id.btn_consumedCards);
        tv_me_username=content.findViewById(R.id.tv_me_username);

        SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        userName=sharedPreferences.getString("username",null);
        Log.i("login","current user: "+userName);

        btn_consumedCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConsumedResourcesActivity.class);
                startActivity(intent);
            }
        });

        tv_me_username.setText(userName);

        return content;
    }

}
