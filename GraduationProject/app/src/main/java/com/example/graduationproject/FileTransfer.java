package com.example.graduationproject;

import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
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
    File file = new File("./storage/emulated/0/Pictures/JPEG_20231007_131700_2554008183218083671.jpg");
    File infile = new File("/storage/emulated/0/Pictures/Output.jpg");

    //
    @Override
    public void run() {
        super.run();
        try {
            socket = new Socket(host, port);
            Log.v("YYY","Here is FileTransfer");
            if (socket != null) {
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                fos = new FileOutputStream(file);
                fis = new FileInputStream(file);

                BufferedInputStream bis = new BufferedInputStream(fis);
                //Get socket's output stream
                OutputStream os = socket.getOutputStream();
                //Read File Contents into contents array


                Long fileLength = file.length();
                String str = fileLength.toString();
                int final_fileLength = Integer.parseInt(str);

                Log.v("YYY", ""+final_fileLength);

//                byte[] b = new byte[final_fileLength];
                ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
                if(buffer == null)
                    Log.v("YYY", "QAQ");
                else
                    Log.v("YYY", "YES");
                buffer.put(buffer.array(),0, buffer.array().length);
                Log.v("YYY", ""+buffer.array().length);
                buffer.flip();

                Uri uri = Uri.fromFile(file);
//                Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
                Log.v("YYY","Hello");
//                Log.v("YYY",Integer.toString(buffer.length));

                bis.read(buffer.array(), 0, buffer.array().length);
                os.write(buffer.array(), 0, buffer.array().length);

                os.flush();
                //File transfer done. Close the sock


            } else {

            }
            while (true) {

                String x = dis.readUTF();
                Log.v("CCC", "msg from socket: " + x);
            }
        } catch (
                IOException e) {

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

