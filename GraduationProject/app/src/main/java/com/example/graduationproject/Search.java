package com.example.graduationproject;

import static com.example.graduationproject.MainActivity.CAMERA_REQUEST_CODE;
import static com.example.graduationproject.MainActivity.GALLERY_REQUEST_CODE;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
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
    String se_currentPhotoPath;
    Uri uri;
    Uri contentUri;
    LinearLayout ll;
    EditText keyword;
    SocketClient client;
    String json = "";
    String inputStr = "", search_keyword;
    Gson gson = new Gson();
    Long filesize;

    String file_path;

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

        ll = findViewById(R.id.searchlayout);

        iv_search.setImageResource(R.drawable.search_icon);
        keyword = findViewById(R.id.keyword);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);




        frag_search = new Frag_search(this);

        Intent it = getIntent();
        file_path = it.getStringExtra("path");
        search_keyword = it.getStringExtra("keyword");
        Log.v("1024", "file_path: "+file_path);
        uri = Uri.parse(file_path);

        load();

        keyword.setText(search_keyword);

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getBaseContext(), "處理中..", Toast.LENGTH_LONG).show();
                se_bt_photo.setVisibility(View.GONE);
                se_bt_pic.setVisibility(View.GONE);
                ll.setVisibility(View.GONE);
                frag_search.iv.setVisibility(View.INVISIBLE);

                inputStr = keyword.getText().toString();
                json = gson.toJson(inputStr);

                Log.v("CCC", json.toString());

                client = new SocketClient(Search.this);
                client.start();
                while (!client.ready) {
                }
                client.sendMessage(json);

                keyword.getText().clear();


            }
        });
        se_bt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("bt_pho", "this camera in search_act");
                try{
                    dispatchTakePictureIntent();
                }catch (Exception e){
                    Log.v("bt_pho", "can't open");
                    e.printStackTrace();
                }

            }
        });
        se_bt_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Capture
        if(requestCode == CAMERA_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                f = new File(se_currentPhotoPath);

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                frag_search.iv.setImageURI(uri);

            }
        }
        // Gallery
        if(requestCode == GALLERY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Uri galleryUri = data.getData();
                getRealPathFromURI(galleryUri);
                Log.v("1026", "getRealPathFromURI in search: "+ getRealPathFromURI(galleryUri));
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp +"."+getFileExt(galleryUri);
                uri = Uri.parse(getRealPathFromURI(galleryUri));
                f = new File(uri.toString());
                frag_search.iv.setImageURI(uri);

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
        se_currentPhotoPath = image.getAbsolutePath();
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
                Log.v("CCC", "photoURI:" + photoURI.toString());

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
//                Android/data/com.example.graduationproject/files/Pictures
            }
            String path = photoFile.getAbsolutePath();
            Log.v("CCC", "Path: " + path);
            uri = Uri.parse(path);

        }

    }

    public void load(){
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.frame, frag_search).commit();

    }

    public void showresult(String path){
        frag_search_result = new Frag_search_result(this, path);

        Log.v("1021","frag_search_result: "+frag_search_result);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.add(R.id.frame, frag_search_result).commit();
        Log.v("1021","frag_search_result: "+frag_search_result);
        Log.v("1021", "frag_search_result: resultImg: "+frag_search_result.resultImg);

    }
//    String OCRstr;
//    public void StrfromOCR(String str){
//        OCRstr = str;
//    }

}