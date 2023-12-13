package com.example.graduationproject;
//https://thumbb13555.pixnet.net/blog/post/338106045-chatgpt  # openai
//https://thumbb13555.pixnet.net/blog/post/310777160  # dialog

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Chatbox extends AppCompatActivity {
    public static final String API_KEY = "";
    public static final String URL = "https://api.openai.com/v1/completions";
//    public static final String URL = "https://api.openai.com/v1/chat/completions";

    TextView tvAnswer, tvQuestion;
    Button send, ch_back, bt_strOCR;
    EditText input;
    String msg_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        tvQuestion = findViewById(R.id.textView_Question);
        tvQuestion.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvAnswer = findViewById(R.id.textView_Answer);
        send = findViewById(R.id.button_Send);
        input = findViewById(R.id.edittext_Input);
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
            String question = msg_dialog + input.getText().toString();
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

//            input.getText().clear();
        });

    }
    private void setNormalAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Chatbox.this);
        alertDialog.setTitle("辨識結果");
//        alertDialog.setMessage(msg_dialog);

        // 創建一個TextView來顯示文字
        TextView textView = new TextView(Chatbox.this);
        textView.setText(msg_dialog);
        textView.setClickable(true);
        textView.setFocusable(true);

        // 添加TextView的點擊事件，將文字複製到剪貼板
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = ((TextView) v).getText().toString() ;
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", messageText);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getBaseContext(), "文字已複製", Toast.LENGTH_SHORT).show();
            }
        });

        // 將TextView添加到AlertDialog中
        alertDialog.setView(textView);

        // 確定按鈕
        alertDialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "確定", Toast.LENGTH_SHORT).show();
            }
        });
        // 取消按鈕
        alertDialog.setNeutralButton("取消",(dialog, which) -> {
            Toast.makeText(getBaseContext(), "取消", Toast.LENGTH_SHORT).show();
        });
//        alertDialog.setCancelable(false);  // 點擊空白處能否使dialog消失
        alertDialog.show();
    }

    private WeakHashMap<String,Object> makeRequest(String input) {
        WeakHashMap<String,Object> weakHashMap = new WeakHashMap<>();
        weakHashMap.put("model","gpt-3.5-turbo-instruct");
//        weakHashMap.put("model","gpt-4");
        weakHashMap.put("prompt",input);
        weakHashMap.put("temperature",0.3);
        weakHashMap.put("max_tokens",1000);
        weakHashMap.put("top_p",1);
        weakHashMap.put("frequency_penalty",0);
        weakHashMap.put("presence_penalty",0);
        return weakHashMap;
    }

}