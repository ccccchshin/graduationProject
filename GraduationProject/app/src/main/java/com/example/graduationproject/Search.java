package com.example.graduationproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class Search extends AppCompatActivity {

    MainActivity main;
    ImageView iv_search;
    FrameLayout frameLayout;
    Frag_search frag_search;
    Frag_search_result frag_search_result;

    public Search(){

    }
    public Search(Context c_){
        main = (MainActivity) c_;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        iv_search = findViewById(R.id.iv_search);
        frameLayout = findViewById(R.id.frame);


        iv_search.setImageResource(R.drawable.search_icon);

        frag_search = new Frag_search(this);
        frag_search_result = new Frag_search_result(this);
        main = new MainActivity();

        Log.v("sss", "2ok");
        Log.v("sss", main.answer);
        load();

//        Log.v("0914test", main.currentPhotoPath);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.replace(R.id.frame, frag_search_result).commit();

            }
        });

    }

    public void load(){
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.frame, frag_search).commit();

    }
    
}