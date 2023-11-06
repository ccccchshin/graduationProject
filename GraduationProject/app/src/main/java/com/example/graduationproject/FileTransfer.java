package com.example.graduationproject;

import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;

import java.io.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileTransfer {
    File backImg;
    String backimg_path = "123";
    String file_path;
    public FileTransfer(){

    }
    public FileTransfer(String path){
        this.file_path = path;
    }

    public void run() {

        try {
            StringClient sc = new StringClient();
            sc.run();
            FileClient fc = new FileClient();
            fc.run();
        } catch (Exception e) {
            System.out.println("Exception!");
        }
    }
    class StringClient  {
        Socket socket = null;
        DataOutputStream dos = null;
        DataInputStream dis = null;
        String host = "120.110.113.213";

        public void run() {
            try {
                socket = new Socket(host, 12355);

                dos = new DataOutputStream(socket.getOutputStream()); //給server送data
                dis = new DataInputStream(socket.getInputStream()); //接收server送來的data

                File file = new File(file_path);
                dos.writeUTF("" + file.length()); // 傳圖片長度給server
                String server_string = dis.readUTF(); // 讀server傳進來的字串
                System.out.println(server_string);
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println("Socket Error!");
            } finally {
                try {
                    dos.close();
                    dis.close();
                    socket.close();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println("finally error!");
                }
            }
        }

    }

    class FileClient {
        Socket socket = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        String host = "120.110.113.213";


        public void run() {
            try {
                socket = new Socket(host, 12350);

                bos = new BufferedOutputStream(socket.getOutputStream());//給server送data
                bis = new BufferedInputStream(socket.getInputStream());//接收server送來的data


                send_file();
                Thread.sleep(1000);
                receive_file();


            } catch (Exception e) {
                System.out.println("Socket Error!");
            } finally {
                try {
                    Thread.sleep(1000);
                    bos.close();
                    bis.close();
                    socket.close();
                } catch (Exception e) {
                    System.out.println("finally error!");
                }
            }
        }

        public void receive_file() {
            byte[] buffer = new byte[8192];
            try {
                int count;
//                Log.v("1019", "check the length");
                backImg = new File(createImageFile().getAbsolutePath());
                backimg_path = backImg.getAbsolutePath();
                Log.v("1021", backImg.getAbsolutePath());

//                File server_img = new File("/storage/emulated/0/Pictures/server.jpg");
//                Log.v("1019", "check the server_img");

                try (FileOutputStream fos = new FileOutputStream(backImg)) {
//                    Log.v("1019", "check try");
                    while ((count = bis.read(buffer, 0, buffer.length)) > 0) {
//                        Log.v("1019", "check the while loop");
//                    System.out.println("send to server");
                        fos.write(buffer, 0, count);
//                        Log.v("1019", "check the fos");
                    }
                    Thread.sleep(500);
                    fos.close();
                    Log.v("SSS","Server file received");
                    System.out.println("Server file received");
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println(" receive exception!");
                Log.v("1019",e.toString());
            } finally {

            }
        }

        public void send_file() {
            try {
                byte[] eof = {0x00, 0x00, 0x00};

                int count = 0;
                byte[] buffer = new byte[8192]; // or 4096, or more

//                try (FileInputStream input = new FileInputStream("/storage/emulated/0/Pictures/JPEG_20231012_211316_514444503908695507.jpg")) {
                try (FileInputStream input = new FileInputStream(file_path)) {

                    while ((count = input.read(buffer, 0, buffer.length)) > 0) {
                        bos.write(buffer, 0, count); //寫入字串in輸出流
                        bos.flush(); //把資料寫出給server，確保buffer乾淨
                    }
                    Thread.sleep(500);
                    input.close();
                }
                Thread.sleep(500);
                System.out.println("End of file!");
//            bos.write(eof, 0, 3);
            } catch (Exception e) {
                System.out.println("read file error!");
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );


        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }


}

