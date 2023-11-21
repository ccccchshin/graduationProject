package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Selectpics extends AppCompatActivity {

    Button bt_cancel, bt_select;
    Frag_search frag_search = new Frag_search();
    Search search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpics);

        bt_cancel = findViewById(R.id.bt_cancel);
        bt_select = findViewById(R.id.bt_select);

//        search = new Search();


        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("bt_cancel", "this is cancel button");
                finish();
            }
        });
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("bt_select", "this is select button");
                Intent it = new Intent(Selectpics.this, Search.class);
                startActivity(it);

            }
        });

    }
}