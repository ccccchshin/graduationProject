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

//        fra_bt_pho = v.findViewById(R.id.fra_bt_photo);
//        fra_bt_pic = v.findViewById(R.id.fra_bt_picture);

        iv = v.findViewById(R.id.iv_fra_search);

        iv.setImageURI(search.uri);

//        if(search.main.f != null) {
//            Log.v("sss", "3ok");
//            Bitmap myBitmap = BitmapFactory.decodeFile(search.main.f.getAbsolutePath());
//            iv.setImageBitmap(myBitmap);
//        }else{
//            Log.v("sss", "4ok");
//        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("sss", "");
            }
        });

//        String str = search.main.currentPhotoPath;
//
//        if(search.main.aa == null){
//            Log.v("hello0914","hello null");
//        }else{
//            Log.v("hello0914","hello yes");
//        }
//        iv.setImageURI(search.main.aa);


//        Log.v("sss",  "" +str);

//        main = new MainActivity();

//        Log.v("0914321", search.string);
//        iv.setImageURI(main.aa);
//        Log.v("0914", search.main.aa.toString());

//        fra_bt_pho.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.v("bt_pho", "this is 拍照 button");
//                try{
//                    Intent intent = new Intent();
//                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivity(intent);
//                    //觸發更新uri
//                }catch (Exception e){
//                    Log.v("bt_pho", "can't");
//                    e.printStackTrace();
//                }
//            }
//        });
//        fra_bt_pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent it = new Intent(search, Selectpics.class);
////                startActivity(it);
//                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivity(gallery);
////                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
//            }
//        });

        return v;
    }


}