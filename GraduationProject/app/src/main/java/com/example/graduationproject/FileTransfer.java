package com.example.graduationproject;

import android.database.Cursor;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

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

public class FileTransfer extends Thread {

    DataInputStream dis;
    DataOutputStream dos;

    FileInputStream fis;
    FileOutputStream fos;
    Socket socket;
    String host = "120.110.113.213";
    int port = 12350;
    File file = new File("/storage/emulated/0/Pictures/JPEG_20231012_210638_3527709298275771254.jpg");
    File infile = new File("/storage/emulated/0/Pictures/Output.jpg");

    //
    @Override
    public void run() {
        super.run();
        try {
            socket = new Socket(host, port);
            Log.v("YYY","Here is FileTransfer");
            if (socket != null) {
                Log.v("YYY", "if");
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                fos = new FileOutputStream(file);
                fis = new FileInputStream(file);

                BufferedInputStream bis = new BufferedInputStream(fis);
                //Get socket's output stream
                OutputStream os = socket.getOutputStream();
                //Read File Contents into contents array
                ByteArrayOutputStream baos = new ByteArrayOutputStream();


//                Long fileLength = file.length();
//                String str = fileLength.toString();
//                int final_fileLength = Integer.valueOf(str);
//
//                Log.v("YYY", ""+final_fileLength);
//
//                ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
//
//                buffer.put(buffer.array(),0, buffer.array().length);
//                Log.v("YYY", ""+buffer.array().length);
//                buffer.flip();
//
//                bis.read(buffer.array(), 0, buffer.array().length);
//                os.write(buffer.array(), 0, buffer.array().length);
//
//                os.flush();
                //File transfer done. Close the sock

//                int length = 0;
//                byte[] sendBytes = new byte[1024 * 3000];
//                while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0){
//                    baos.write(sendBytes, 0, length);
//                }
//                baos.flush();
//                PrintWriter pw = new PrintWriter(os);
//                pw.write(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
//                pw.flush();
//                socket.shutdownOutput();
//                InputStream is = socket.getInputStream();
////                Log.v("YYY", "socket.getInputStream()");
//                BufferedReader br = new BufferedReader(new InputStreamReader(is));
////                String info = br.readLine();
//                socket.close();
//                fis.close();
//                pw.close();
//                baos.close();
//                os.close();
//
//                dos.close();
////                dis.close();
//                fos.close();

                InputStream is = socket.getInputStream();
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                byte[] buffer = new byte[1024*20];
                int bytesRead;
                while ((bytesRead = is.read(buffer, 0, buffer.length)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                bos.close();
                fos.close();

            } else {
                Log.v("YYY", "else");
            }

            while (true) {
                Log.v("YYY", "while");
                String x = dis.readUTF();
                Log.v("YYY", "msg from socket:123 " + x);
            }
        } catch (IOException e) {
            Log.v("YYY", "catch");
//        } finally {
//            try {
//                dos.close();
//                dis.close();
//                fos.close();
//                fis.close();
//                socket.close();
//            } catch (IOException ioe) {
//
//            }
        }

    }

    private void receiveFile() {
        int bytes = 0;
        try {
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            long size = 10000;
            while (size > 0 && (bytes = dis.read(buffer, 0, buffer.length)) != -1) {
                // Here we write the file using write method
                fos.write(buffer, 0, bytes);
                size -= bytes; // read upto file size
            }
            System.out.println("File is Received");
            fos.close();
        } catch (Exception e) {
        }
        // Here we received file
    }

}

