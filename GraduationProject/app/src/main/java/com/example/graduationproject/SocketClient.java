package com.example.graduationproject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
//    Gson gson = new Gson();

    MyHandler myHandler;

    String host = "120.110.113.213";
    int port = 12345;

    public SocketClient() {
    }
    public SocketClient(Search search) {
        this.search = (Search) search;
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

        try {
            socket = new Socket(host, port);
            if (socket != null) {
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
            } else {

            }while (true) {
                search.json = dis.readUTF();
//                SocketClient sc = search.gson.fromJson(search.json, SocketClient.class);
                Bundle bundle = new Bundle();
                bundle.putString("str", search.json);
                Message msg = new Message();
                msg.setData(bundle);
                myHandler.sendMessage(msg);
            }
        } catch (IOException e) {

        } finally {
            try {
                dos.close();
                dis.close();
                socket.close();
            } catch (IOException ioe){

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
