package com.kimhello.isolationtimer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Splash -> Main 넘어가도록
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
