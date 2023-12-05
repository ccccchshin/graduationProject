package com.example.graduationproject;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;


    Button bt_pho, bt_pic, bt_ctrl,bt_chat;
    String currentPhotoPath;

    Search search;

    File photoFile, f;
    Long filelength;
    EditText main_et;

    int count1 = 0, count2 = 0;
    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_pho = findViewById(R.id.bt_photo);
        bt_pic = findViewById(R.id.bt_picture);
        bt_ctrl = findViewById(R.id.mode_CF);
        bt_chat = findViewById(R.id.mode_Chat);

        main_et = findViewById(R.id.main_et);

        search = new Search(this);


        main_et.setVisibility(View.INVISIBLE);
        bt_pho.setVisibility(View.INVISIBLE);
        bt_pic.setVisibility(View.INVISIBLE);


        bt_ctrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(main_et.getVisibility() == View.VISIBLE) {
//                    main_et.setVisibility(View.INVISIBLE);
//                    bt_pho.setVisibility(View.INVISIBLE);
//                    bt_pic.setVisibility(View.INVISIBLE);
//                    Toast.makeText(getBaseContext(), "按鈕已收起", Toast.LENGTH_SHORT).show();
//
//                }else{
//                    main_et.setVisibility(View.VISIBLE);
//                    bt_pho.setVisibility(View.VISIBLE);
//                    bt_pic.setVisibility(View.VISIBLE);
//                    Toast.makeText(getBaseContext(), "Ctrl+F 模式", Toast.LENGTH_SHORT).show();
//                }
                mode = 0;
                count2 = 0;
                if(count1 == 0) {
                    main_et.setVisibility(View.VISIBLE);
                    bt_pho.setVisibility(View.VISIBLE);
                    bt_pic.setVisibility(View.VISIBLE);
                    Toast.makeText(getBaseContext(), "Ctrl+F 模式", Toast.LENGTH_SHORT).show();
                    count1 = 1;
                }else{
                    main_et.setVisibility(View.INVISIBLE);
                    bt_pho.setVisibility(View.INVISIBLE);
                    bt_pic.setVisibility(View.INVISIBLE);
                    Toast.makeText(getBaseContext(), "按鈕已收起", Toast.LENGTH_SHORT).show();
                    count1 = 0;
                }
            }
        });

        bt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 1;
                count1 = 0;
                if(count2 == 0) {
                    main_et.setVisibility(View.INVISIBLE);
                    bt_pho.setVisibility(View.VISIBLE);
                    bt_pic.setVisibility(View.VISIBLE);
                    Toast.makeText(getBaseContext(), "ChatGPT 模式", Toast.LENGTH_SHORT).show();
                    count2 = 1;
                }else {
                    bt_pho.setVisibility(View.INVISIBLE);
                    bt_pic.setVisibility(View.INVISIBLE);
                    Toast.makeText(getBaseContext(), "按鈕已收起", Toast.LENGTH_SHORT).show();
                    count2 = 0;
                }
            }
        });


//        bt_pho.setBackgroundColor(getResources().getColor(R.color.baby_blue));
//        bt_pic.setBackground(getResources().getDrawable(R.drawable.button_shape));
        // 相機
        bt_pho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                askCameraPermissions();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if(!Environment.isExternalStorageManager()){
                        Intent getPermission = new Intent();
                        getPermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivity(getPermission);
                    }
                    verifyPermissions();
                }
            }
        });


        // 相簿
        bt_pic.setOnClickListener(new View.OnClickListener() {
            @Override //Environment.DIRECTORY_PICTURES
            public void onClick(View v) { //Media.EXTERNAL_CONTENT_URI
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

    }

    //存取權安全性驗證
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void verifyPermissions(){

        String[] permissions = {
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.CAMERA};

         if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED ){

                dispatchTakePictureIntent();

         }else{
             Log.v("ANSWER","what was that = "+ContextCompat.checkSelfPermission(this.getApplicationContext(),
                     permissions[0])+"!!!!");
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    permissions[0]) == PackageManager.PERMISSION_GRANTED){
                Log.v("answer0","yes");
            }else{
                Log.v("answer0","no");
            }
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    permissions[1]) == PackageManager.PERMISSION_GRANTED){
                Log.v("answer1","yes");
            }else{
                Log.v("answer1","no");
            }

            ActivityCompat.requestPermissions(this,
                    permissions,
                    CAMERA_PERM_CODE);
        }
    }

    //驗證
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 開啟相機之後的動作
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Capture
        if(requestCode == CAMERA_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                f = new File(currentPhotoPath);

                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                filelength = f.length();

                if(mode == 0) {
                    Intent it = new Intent(MainActivity.this, Search.class);
                    it.putExtra("path", currentPhotoPath);
                    it.putExtra("keyword", main_et.getText().toString());
                    startActivity(it);
                }else{
                    Intent it = new Intent(MainActivity.this, Search_chat.class);
                    it.putExtra("path", currentPhotoPath);
                    startActivity(it);
                }
            }
        }
        // Gallery
        if(requestCode == GALLERY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Uri contentUri = data.getData();
                getRealPathFromURI(contentUri);
//                Log.v("1026", "getRealPathFromURI: "+getRealPathFromURI(contentUri));
//                Log.v("1024", "gallery img: "+ contentUri.getPath());
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp +"."+getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " +  imageFileName);
//                displayImg.setImageURI(contentUri);

                if(mode == 0) {
                    Intent it = new Intent(MainActivity.this, Search.class);
                    it.putExtra("path", getRealPathFromURI(contentUri));
                    it.putExtra("keyword", main_et.getText().toString());
//                it.putExtra("path", "/storage/emulated/0/Pictures/"+imageFileName);
                    startActivity(it);
                }else{
                    Intent it = new Intent(MainActivity.this, Search_chat.class);
                    it.putExtra("path", getRealPathFromURI(contentUri));
                    startActivity(it);
                }
            }
        }

    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
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
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
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



}