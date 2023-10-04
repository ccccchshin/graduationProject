package com.example.graduationproject;

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

public class FileTransfer extends Thread {

    DataInputStream dis;
    DataOutputStream dos;

    FileInputStream fis;
    FileOutputStream fos;
    Socket socket;
    String host = "120.110.113.213";
    int port = 12350;
    File file = new File("/storage/emulated/0/Pictures/JPEG_20230926_144651_6448690246035440926.jpg");
    File infile = new File("/storage/emulated/0/Pictures/Output.jpg");

    //
    @Override
    public void run() {
        super.run();
        try {
            socket = new Socket(host, port);
            if (socket != null) {
                dos = new DataOutputStream(socket.getOutputStream());
                dis = new DataInputStream(socket.getInputStream());
                fos = new FileOutputStream(file);
                fis = new FileInputStream(file);

                BufferedInputStream bis = new BufferedInputStream(fis);
                //Get socket's output stream
                OutputStream os = socket.getOutputStream();
                //Read File Contents into contents array
                long fileLength = file.length();
                byte[] buffer = new byte[(int) fileLength];
                bis.read(buffer, 0, buffer.length);
                os.write(buffer, 0, buffer.length);
                System.out.print("Sending file complete!");

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

