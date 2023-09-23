package com.example.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Frag_search_result extends Fragment {

    MainActivity main;
    Search search;

    Button bt_back, bt_home;

    public Frag_search_result() {
        // Required empty public constructor
    }

    public Frag_search_result(Context c_) {
        search = (Search) c_;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_frag_search_result, container, false);

        bt_back = v.findViewById(R.id.fra_bt_back);
        bt_home = v.findViewById(R.id.fra_bt_home);



        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.se_bt_photo.setVisibility(View.GONE);
                search.se_bt_pic.setVisibility(View.GONE);

            }
        });
        bt_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(search, MainActivity.class);
                startActivity(it);
            }
        });


        return v;
    }
}