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
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
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

    ScaleGestureDetector scaleGestureDetector;
    float scaleFactor = 1.0f;
    float focusX, focusY;


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
        resultImg = v.findViewById(R.id.result_img);

        Uri uri = Uri.parse(path);
        resultImg.setImageURI(uri);

        Log.v("1021", "onCreateview: resultImg : "+resultImg);

        scaleGestureDetector = new ScaleGestureDetector(requireContext(), new ScaleListener());
        resultImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                focusX = scaleGestureDetector.getFocusX();
                focusY = scaleGestureDetector.getFocusY();
                return true;
            }
        });

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

        bt_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(search, MainActivity.class);
                startActivity(it);
            }
        });

        return v;

    }

    //    public boolean onTouchEvent(MotionEvent event){
//        scaleGestureDetector.onTouchEvent(event);
//        return true;
//    }
//
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f)); // 控制縮放的範圍

            // 計算放大縮小時的焦點位置偏移
            float offsetX = (1 - detector.getScaleFactor()) * focusX;
            float offsetY = (1 - detector.getScaleFactor()) * focusY;
//            float offsetX = detector.getScaleFactor()* focusX;
//            float offsetY = detector.getScaleFactor() * focusY;

            // 設置圖片的縮放和偏移
            resultImg.setScaleX(scaleFactor);
            resultImg.setScaleY(scaleFactor);
            resultImg.setTranslationX(offsetX);
            resultImg.setTranslationY(offsetY);

            return true;
        }
    }

}