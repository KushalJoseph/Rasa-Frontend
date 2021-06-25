package com.example.rasafrontend;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VoiceChat extends AppCompatActivity {

    EditText speechToText;
    String language = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_chat);
        speechToText = findViewById(R.id.speechToText);

        Intent intent = getIntent();
        language = intent.getStringExtra("lang");

        tts = new TextToSpeech(VoiceChat.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i == TextToSpeech.SUCCESS){
                    int res = tts.setLanguage(Locale.getDefault());
                }
            }
        });
    }

    public void getSpeechInput(View view){
        speechToText.setText("");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        if(language ==  "en"){
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        }else if(language == "es"){
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        }

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, 1);
        }else {
            Toast.makeText(this, "Not supported", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speechToText.setText(result.get(0));
                    String finalAnswer = callApi("en", result.get(0));
                    //Toast.makeText(this, finalAnswer, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    TextToSpeech tts;

    public String callApi(String lang, String message) {
        Retrofit retrofit = new Retrofit.Builder()
                
                .baseUrl("https://49884be9dfa1.ngrok.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);
        RequestObject requestObject = new RequestObject(lang, message);
        Log.i("test", requestObject.lang);
        Log.i("test", requestObject.message);
        Call<ResponseObject> call = api.getData(requestObject);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                Log.i("test", String.valueOf(response.body().text));
                Toast.makeText(VoiceChat.this, String.valueOf(response.body().text), Toast.LENGTH_LONG).show();
                String toSay = String.valueOf(response.body().text);

                ConvertTextToSpeech(toSay);

            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.i("test", "Wrong");
                //Toast.makeText(VoiceChat.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        return "";
    }

    private void ConvertTextToSpeech(String toSay) {
        Log.i("test", toSay);
        String answer = "";
        if(toSay == null || toSay.equals("")){
            answer = "Something went wrong";
        }
        tts.speak(answer, TextToSpeech.QUEUE_ADD, null);
    }
}