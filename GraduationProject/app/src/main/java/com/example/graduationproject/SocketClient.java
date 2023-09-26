package com.example.graduationproject;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

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
    Uri u ;

    MyHandler myHandler;

    String host = "120.110.113.213";
    int port = 12345;

    public SocketClient() {
    }

    public SocketClient(Search s) {
        search = (Search) s;
    }

//    @Override
//    protected Void doInBackground(String... params) {
//
//        try {
//            try {
//                Socket socket = new Socket("120.110.113.213",12345);
//                PrintWriter outToServer = new PrintWriter(
//                        new OutputStreamWriter(
//                                socket.getOutputStream()));
//                outToServer.print(params[0]);
//                outToServer.flush();
//
//
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        catch (Exception e) {
//            this.exception = e;
//            return null;
//        }
//        return null;
//    }

    @Override
    public void run() {
        super.run();

        Looper.prepare();

        myHandler = new MyHandler();


        file = new File("file:///storage/emulated/0/Pictures/JPEG_20230926_212305_1951428212526344974.jpg");

        try {
            socket = new Socket(host, port);
            if (socket != null) {
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                fos = new FileOutputStream(file);
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(socket.getInputStream());
                bytes = (byte[]) ois.readObject();
            } else {

            }
            while (true) {
                String x = dis.readUTF();
                Log.v("CCC", "msg from socket: " + x);
//                SocketClient sc = search.gson.fromJson(search.json, SocketClient.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("str", search.json);
//                Message msg = new Message();
//                msg.setData(bundle);
//                myHandler.sendMessage(msg);
//                File f = fis.rea
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

    public void sendImage(){
        if (fos != null){
            try {
                fos.write(bytes);
            }catch (Exception e){
                Log.v("CCC", "Send image fail!");
            }
        }
    }

}
