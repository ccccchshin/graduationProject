package com.example.graduationproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

// AsyncTask<String, Void, Void>
public class SocketClient extends Thread {

    Search search;
    private Exception exception;
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    FileInputStream fis;
    FileOutputStream fos;

    ObjectInputStream ois;

    byte[] bytes;

    File file;

    MyHandler myHandler;


    String host = "120.110.113.213";
    int port = 12345;

    public SocketClient() {
    }

    public SocketClient(Search s) {
        search = (Search) s;
    }


    @Override
    public void run() {
        super.run();

        Looper.prepare();

        myHandler = new MyHandler();


//        file = new File("file:///storage/emulated/0/Pictures/JPEG_20230926_212305_1951428212526344974.jpg");
        file = new File("/storage/emulated/0/Pictures/JPEG_20230926_144651_6448690246035440926.jpg");
//        file = new File("\storage\emulated\0\Pictures\JPEG_20230926_212305_1951428212526344974.jpg");


        try {
            socket = new Socket(host, port);
            if (socket != null) {
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                fos = new FileOutputStream(file);
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(socket.getInputStream());
                bytes = (byte[]) ois.readObject();
//                ByteArrayOutputStream()
            } else {

            }
            while (true) {
                String x = dis.readUTF();
                if (x.equals("OK")) {
                    Log.v("CCC", "response from python server");
                    FileTransfer ft = new FileTransfer();
                    ft.start();
                }
                Log.v("CCC", "msg from socket: " + x);
            }
        } catch (IOException e) {

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                dos.close();
                dis.close();
                fos.close();
                fis.close();
                socket.close();
            } catch (IOException ioe) {

            }
        }

    }

    public void sendMessage(String str) {
        if (dos != null) {
            try {
                dos.writeUTF(str);
            } catch (Exception e) {
                Log.v("CCC", "Send msg fail!");
            }
        }
    }
    public void sendImgMsg(DataOutputStream out) throws IOException {
        Log.i("sendImg", "len: "+"1");
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
//        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(fis), R.drawable.search_icon);

        Log.i("sendImg", "len: "+"2");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

//        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bout);
//        Log.i("sendImg", "len: "+"3");

        long len = bout.size();
        Log.i("sendImg", "len: "+"4");
        out.write(bout.toByteArray());
    }

    public void sendImage(){
        byte[] sendbytes = new byte[1024];
        int length = 0;
        if (fos != null){
            try {
                while ((length = fis.read(sendbytes, 0, sendbytes.length)) > 0){
                    dos.write(sendbytes, 0, length);
                    dos.flush();
                }
            }catch (Exception e){
                Log.v("CCC", "Send image fail!");
            }
        }


//        if (fos != null){
//            try {
//                fos.write(bytes);
//            }catch (Exception e){
//                Log.v("CCC", "Send image fail!");
//            }
//        }
    }

}
