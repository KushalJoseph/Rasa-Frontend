package com.example.rasafrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void english(View view){
        Intent intent = new Intent(getApplicationContext(), VoiceChat.class);
        intent.putExtra("lang", "en");
        startActivity(intent);
    }
    public void spanish(View view){
        Intent intent = new Intent(getApplicationContext(), VoiceChat.class);
        intent.putExtra("lang", "es");
        startActivity(intent);
    }


}