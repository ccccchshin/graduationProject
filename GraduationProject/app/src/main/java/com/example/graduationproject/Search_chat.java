package com.example.graduationproject;

import static com.example.graduationproject.MainActivity.CAMERA_REQUEST_CODE;
import static com.example.graduationproject.MainActivity.GALLERY_REQUEST_CODE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Search_chat extends AppCompatActivity {

    ImageView iv_chse, chse_img;
    Button chse_pho, chse_pic;
    String img_path;
    Uri img_uri, contentUri;
    File photoFile, f;
    String chse_currentPhotoPath, strOCR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_chat);

        iv_chse = findViewById(R.id.iv_chat_search);
        chse_img = findViewById(R.id.chat_img);
        chse_pho = findViewById(R.id.chse_pho);
        chse_pic = findViewById(R.id.chse_pic);

        iv_chse.setImageResource(R.drawable.search_icon);

        Intent it = getIntent();
        img_path = it.getStringExtra("path");
        img_uri = Uri.parse(img_path);

        chse_img.setImageURI(img_uri);

//        iv_chse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getBaseContext(), "處理中..", Toast.LENGTH_LONG).show();
//                OCR_StringClient ocr_sc = new OCR_StringClient();
//                Log.v("OCR_sc", "ocr_sc error");
//                ocr_sc.start();
//                Log.v("OCR_sc", "no ready");
//                while (!ocr_sc.ready) {
//                }
//                Log.v("OCR_sc", "over ready");
//                ocr_sc.run(img_path);
//                Rec_OCR recOcr = new Rec_OCR();
//                recOcr.run(img_path);
//                strOCR = recOcr.backOCR;
//                Log.v("strOCR: ", strOCR);
//                Intent it = new Intent(Search_chat.this, Chatbox.class);
//                it.putExtra("OCRresult", strOCR);
//                startActivity(it);
//            }
//        });
        iv_chse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "處理中..", Toast.LENGTH_LONG).show();

                // 使用 Thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OCR_StringClient ocr_sc = new OCR_StringClient();
                        ocr_sc.run(img_path);

                        Rec_OCR recOcr = new Rec_OCR();
                        recOcr.run(img_path);

                        // 獲取結果後更新 UI（可使用 runOnUiThread 方法）
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                strOCR = recOcr.backOCR;
                                Log.v("strOCR: ", strOCR);
                                Intent it = new Intent(Search_chat.this, Chatbox.class);
                                it.putExtra("OCRresult", strOCR);
                                startActivity(it);
                            }
                        });
                    }
                }).start();
            }
        });


        chse_pho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    dispatchTakePictureIntent();
                }catch (Exception e){
                    Log.v("bt_pho", "can't open");
                    e.printStackTrace();
                }
            }
        });
        chse_pic.setOnClickListener(new View.OnClickListener() {
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
                f = new File(chse_currentPhotoPath);

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                chse_img.setImageURI(img_uri);

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
                img_uri = Uri.parse(getRealPathFromURI(galleryUri));
                f = new File(img_uri.toString());
                chse_img.setImageURI(img_uri);

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
        chse_currentPhotoPath = image.getAbsolutePath();
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
            img_uri = Uri.parse(path);

        }

    }
    
}
class OCR_StringClient {
    Socket socket = null;
    DataOutputStream dos = null;
    DataInputStream dis = null;
    String host = "120.110.113.204";

    public void run(String path) {
        try {
            Log.v("OCR_sc", "is here?");
            socket = new Socket(host, 12370);
            Log.v("OCR_sc", "socket error");

            dos = new DataOutputStream(socket.getOutputStream()); //給server送data
            dis = new DataInputStream(socket.getInputStream()); //接收server送來的data
            Log.v("OCR_sc", "dis dos error");
            File file = new File(path);
            dos.writeUTF("" + file.length()); // 傳圖片長度給server
            Log.v("OCR_sc", "length error" + file.length());
            String OCR_server_string = dis.readUTF(); // 讀server傳進來的字串
            System.out.println(OCR_server_string);
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.v("OCR_sc", "Socket Error" + e);
            System.out.println("OCR_sc Socket Error!");
        } finally {
            try {
                dos.close();
                dis.close();
                socket.close();
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("OCR_sc finally error!");
            }
        }
    }

}

class Rec_OCR {
    String host = "120.110.113.204";
    String backOCR = "";

    Socket socket = null;
    BufferedOutputStream bos = null;
    BufferedInputStream bis = null;
    DataInputStream dis;

    public void run(String path){
        try {
            socket = new Socket(host, 12365);

            bos = new BufferedOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());

            send_file(path);
            Thread.sleep(1000);
            Log.v("rec_srtOCR", "sleep");
            receive_strOCR();


        } catch (Exception e) {
            System.out.println("Rec_OCR Socket Error!");
        } finally {
            try {
                Thread.sleep(1000);
                bos.close();
                dis.close();
                socket.close();
            } catch (Exception e) {
                System.out.println("Rec_OCR finally error!");
            }
        }
    }

    public void send_file(String path) {
        try {
            byte[] eof = {0x00, 0x00, 0x00};

            int count = 0;
            byte[] buffer = new byte[8192]; // or 4096, or more

//                try (FileInputStream input = new FileInputStream("/storage/emulated/0/Pictures/JPEG_20231012_211316_514444503908695507.jpg")) {
            try (FileInputStream input = new FileInputStream(path)) {

                while ((count = input.read(buffer, 0, buffer.length)) > 0) {
                    bos.write(buffer, 0, count); //寫入字串in輸出流
                    bos.flush(); //把資料寫出給server，確保buffer乾淨
                }
                input.close();
            }
            System.out.println("End of file!");
//            bos.write(eof, 0, 3);
        } catch (Exception e) {
            System.out.println("send_file read file error!");
        }
    }

    public void receive_strOCR() {
        try {
            Log.v("rec_srtOCR", "try");
//            String temp = dis.readUTF();
//            Log.v("rec_srtOCR", temp);
//            while (temp != null){
//                backOCR += temp;
//                temp = dis.readUTF();
//            }
            int bufferSize = 8192; // 設置緩衝區大小
            byte[] buffer = new byte[bufferSize];
            int bytesRead;

            // 使用 StringBuilder 來構建字符串
            StringBuilder stringBuilder = new StringBuilder();

            // 持續從 DataInputStream 中讀取字節
            while ((bytesRead = dis.read(buffer, 0, bufferSize)) != -1) {
                stringBuilder.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            }
            // 最終的字符串即為 backOCR
            backOCR = stringBuilder.toString();
            Log.v("rec_srtOCR backOCR: ", backOCR);
        }catch (Exception e){
            Log.v("receive exception", "receive exception: " + e);
            System.out.println(" receive exception! " + e);
        }finally {
            try {
                dis.close();
                socket.close();
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("rec_srtOCR finally error!");
            }
        }
    }

}