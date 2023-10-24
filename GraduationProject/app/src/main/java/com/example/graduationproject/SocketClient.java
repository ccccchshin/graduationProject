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

    private Exception exception;
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;



    MyHandler myHandler;

    Search search;
    String host = "120.110.113.213";
    int port = 12345;

    public SocketClient(Search search) {
        this.search = search;
    }



    @Override
    public void run() {
        super.run();

        Looper.prepare();

        myHandler = new MyHandler();


        try {
            Log.v("CCC", "1005test22222");
            socket = new Socket(host, port);
            Log.v("CCC", "1005test123456");
            if (socket != null) {
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());

            } else {
                Log.v("CCC", "1005test");
            }
            while (true) {
                String x = dis.readUTF();
                Log.v("CCC", "msg from socket: " + x);

                if (x != null) {
                    Log.v("CCC", "response from python server");
                    FileTransfer ft = new FileTransfer(search.file_path);
                    ft.run();
                    Log.v("1021","path: "+ft.backimg_path);

                    search.showresult(ft.backimg_path);
                }
            }

        } catch (IOException e) {

        } finally {
            try {
                dos.close();
                dis.close();
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

}
