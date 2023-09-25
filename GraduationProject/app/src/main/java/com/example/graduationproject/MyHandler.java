package com.example.graduationproject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

public class MyHandler extends Handler {
    Search search;
    SocketClient socketClient;

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);

        Bundle bundle = msg.getData();
        search.keyword.setText(bundle.getString("str"));

    }
}
