package com.example.graduationproject;
//https://thumbb13555.pixnet.net/blog/post/338106045-chatgpt

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Chatbox extends AppCompatActivity {
    public static final String API_KEY = "";
    public static final String URL = "https://api.openai.com/v1/completions";

    TextView tvAnswer, tvQuestion;
    Button send, ch_back, bt_strOCR;
    EditText input;
    String msg_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        tvAnswer = findViewById(R.id.textView_Answer);
        send = findViewById(R.id.button_Send);
        input = findViewById(R.id.edittext_Input);
        tvQuestion = findViewById(R.id.textView_Question);
        ch_back = findViewById(R.id.bt_chat_back);
        bt_strOCR = findViewById(R.id.bt_strOCR);

        Intent intent = getIntent();
        msg_dialog = intent.getStringExtra("OCRresult");

        ch_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Chatbox.this, MainActivity.class);
                startActivity(it);
            }
        });

//        bt_strOCR.setOnClickListener((v -> {setNormalAlertDialog();}));
        bt_strOCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNormalAlertDialog();
            }
        });

        send.setOnClickListener(view -> {
            String question = input.getText().toString();
            if(question.isEmpty()) return;
            tvQuestion.setText(question);
            tvAnswer.setText("請稍後..");
            //設置Header中的Content-Type
            MediaType mediaType = MediaType.parse("application/json");
            //寫入body
            String content = new Gson().toJson(makeRequest(question));
            RequestBody requestBody = RequestBody.create(mediaType, content);
            //發送請求
            new HttpRequest().sendPOST(URL, requestBody, new HttpRequest.OnCallback() {
                @Override
                public void onOKCall(String respond) {
                    Log.d("TAG", "onOKCall: "+respond);
                    ChatGPTRespond chatGPTRespond = new Gson().fromJson(respond,ChatGPTRespond.class);
                    Log.v("chatGPTRespond", "yes");
                    System.out.println(chatGPTRespond);
                    runOnUiThread(()->{
                        Log.v("runOnUiThread", "in");
                        Log.v("getChoices", String.valueOf(chatGPTRespond));
                        tvAnswer.setText(chatGPTRespond.getChoices().get(0).getText());
                    });
                }
                @Override
                public void onFailCall(String error) {
                    Log.e("TAG", "onFailCall: "+error);
                    tvAnswer.setText(error);
                }
            });
        });

    }
    private void setNormalAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Chatbox.this);
        alertDialog.setTitle("辨識結果");
        alertDialog.setMessage(msg_dialog);
        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "確定", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNeutralButton("取消",(dialog, which) -> {
            Toast.makeText(getBaseContext(), "取消", Toast.LENGTH_SHORT).show();
        });
//        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private WeakHashMap<String,Object> makeRequest(String input) {
        WeakHashMap<String,Object> weakHashMap = new WeakHashMap<>();
        weakHashMap.put("model","gpt-3.5-turbo-instruct");
        weakHashMap.put("prompt",input);
        weakHashMap.put("temperature",0.3);
        weakHashMap.put("max_tokens",1000);
        weakHashMap.put("top_p",1);
        weakHashMap.put("frequency_penalty",0);
        weakHashMap.put("presence_penalty",0);
        return weakHashMap;
    }

}