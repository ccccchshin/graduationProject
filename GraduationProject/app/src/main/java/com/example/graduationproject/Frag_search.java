package com.example.graduationproject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Frag_search extends Fragment {

    public static final int GALLERY_REQUEST_CODE = 105;
    MainActivity main;
    Search search;
//    Button fra_bt_pho, fra_bt_pic;
    ImageView iv;

    public Frag_search() {
        // Required empty public constructor
    }

    public Frag_search(Context c_) {
        search = (Search) c_;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_frag_search, container, false);

        iv = v.findViewById(R.id.iv_fra_search);


        iv.setImageURI(search.uri);
        Log.v("imgtag", "uri:"+search.uri);


        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("sss", "");
            }
        });

        return v;
    }


}