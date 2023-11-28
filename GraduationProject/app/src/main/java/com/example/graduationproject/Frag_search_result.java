package com.example.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;


public class Frag_search_result extends Fragment {

    Search search;

    Button bt_back, bt_home, bt_chat;
    ImageView resultImg;
    String path;


    public Frag_search_result(Search search, String s_) {
        this.search = search;
        path = s_;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("1021", "onCreate:");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_frag_search_result, container, false);

        bt_back = v.findViewById(R.id.fra_bt_back);
        bt_home = v.findViewById(R.id.fra_bt_home);
        bt_chat = v.findViewById(R.id.fra_bt_chat);
        resultImg = v.findViewById(R.id.result_img);

        Uri uri = Uri.parse(path);
        resultImg.setImageURI(uri);

        Log.v("1021", "onCreateview: resultImg : "+resultImg);


        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                search.load();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.remove(search.frag_search_result).commit();

                search.ll.setVisibility(View.VISIBLE);
                search.se_bt_photo.setVisibility(View.VISIBLE);
                search.se_bt_pic.setVisibility(View.VISIBLE);

            }
        });
        bt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(search, Chatbox.class);
                startActivity(it);
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