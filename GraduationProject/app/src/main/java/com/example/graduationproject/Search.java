package com.example.graduationproject;

import static com.example.graduationproject.MainActivity.CAMERA_REQUEST_CODE;
import static com.example.graduationproject.MainActivity.GALLERY_REQUEST_CODE;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Search extends AppCompatActivity {

    MainActivity main;
    ImageView iv_search;
    FrameLayout frameLayout;
    Frag_search frag_search;
    Frag_search_result frag_search_result;
    Button se_bt_photo, se_bt_pic;
    File photoFile,f;
    String currentPhotoPath;

    Uri uri;

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

        se_bt_photo = findViewById(R.id.se_bt_photo);
        se_bt_pic = findViewById(R.id.se_bt_picture);


        iv_search.setImageResource(R.drawable.search_icon);
//        Bitmap myBitmap = BitmapFactory.decodeFile(main.photoFile.getAbsolutePath());
//        iv_search.setImageBitmap(myBitmap);

        frag_search = new Frag_search(this);
        frag_search_result = new Frag_search_result(this);
        main = new MainActivity();

        Intent it = getIntent();
        String str = it.getStringExtra("path");
        uri = Uri.parse(str);

        Log.v("sss", "2ok");
        Log.v("sss", main.answer);
        if(str != null){
            Log.v("sss", "Path = " + str);
        }else{
            Log.v("sss", "8181");
        }
        if(uri != null){
            Log.v("sss", "Uri = " + uri);
        }else{
            Log.v("sss", "8181");
        }
        load();


//        Log.v("0914test", main.currentPhotoPath);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                t.replace(R.id.frame, frag_search_result).commit();


            }
        });
        se_bt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("bt_pho", "this is 拍照 button");
                try{
                    dispatchTakePictureIntent();
                }catch (Exception e){
                    Log.v("bt_pho", "can't");
                    e.printStackTrace();
                }
            }
        });
        se_bt_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Capture
        if(requestCode == CAMERA_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                f = new File(currentPhotoPath);
//
//                displayImg.setImageURI(Uri.fromFile(f));
//                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

            }
        }
        // Gallery
        if(requestCode == GALLERY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp +"."+getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " +  imageFileName);
//                displayImg.setImageURI(contentUri);


            }
        }

    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File dir = getExternalMediaDirs()[0];
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );


        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //開啟相機前的前置處理
    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent();
        takePictureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            photoFile = null;

            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                Log.e("error123",  ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.graduationproject.CameraEx", photoFile);

                Log.v("sss", "ok" );
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
//                Android/data/com.example.graduationproject/files/Pictures
            }
        }

    }

    public void load(){
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.frame, frag_search).commit();

    }

}