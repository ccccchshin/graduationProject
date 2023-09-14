package com.example.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class Frag_search extends Fragment {

    MainActivity main;
    Search search;
    Button fra_bt_pho, fra_bt_pic;
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

        fra_bt_pho = v.findViewById(R.id.fra_bt_photo);
        fra_bt_pic = v.findViewById(R.id.fra_bt_picture);

        iv = v.findViewById(R.id.iv_fra_search);
        String str = search.main.currentPhotoPath;
        Uri temppath = Uri.parse(""+str);
        if(search.main.aa == null){
            Log.v("hello0914","hello null");
        }else{
            Log.v("hello0914","hello yes");
        }
        iv.setImageURI(search.main.aa);


        Log.v("sss",  "" +str);
        Log.v("sss", "3ok");
//        main = new MainActivity();

//        Log.v("0914321", search.string);
//        iv.setImageURI(main.aa);
//        Log.v("0914", search.main.aa.toString());

        fra_bt_pho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("bt_pho", "this is 拍照 button");
                try{
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        fra_bt_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(search, Selectpics.class);
                startActivity(it);
            }
        });



        return v;
    }


}