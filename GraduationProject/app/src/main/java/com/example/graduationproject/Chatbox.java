package com.example.graduationproject;
//https://thumbb13555.pixnet.net/blog/post/338106045-chatgpt

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class Chatbox extends AppCompatActivity {
    public static final String API_KEY = "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";
    public static final String URL = "https://api.openai.com/v1/completions";

    TextView tvAnswer, tvQuestion;
    Button send;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox);

        tvAnswer = findViewById(R.id.textView_Answer);
        send = findViewById(R.id.button_Send);
        input = findViewById(R.id.edittext_Input);
        tvQuestion = findViewById(R.id.textView_Question);

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

    private WeakHashMap<String,Object> makeRequest(String input) {
        WeakHashMap<String,Object> weakHashMap = new WeakHashMap<>();
        weakHashMap.put("model","gpt-3.5-turbo-instruct");
        weakHashMap.put("prompt",input);
        weakHashMap.put("temperature",0.5);
        weakHashMap.put("max_tokens",1000);
        weakHashMap.put("top_p",1);
        weakHashMap.put("frequency_penalty",0);
        weakHashMap.put("presence_penalty",0);
        return weakHashMap;
    }

}