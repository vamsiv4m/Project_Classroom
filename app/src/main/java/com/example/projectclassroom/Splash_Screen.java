 package com.example.projectclassroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import com.airbnb.lottie.LottieAnimationView;

import static android.content.res.Resources.*;

 public class Splash_Screen extends AppCompatActivity {
    ImageView logo;
    LottieAnimationView lottieAnimationView;
    int time=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        logo=findViewById(R.id.imageView3);
        lottieAnimationView=findViewById(R.id.lottie);
        logo.animate().translationY(100).setDuration(800).start();
        new Handler().postDelayed(new Runnable() { 
            @Override
            public void run() {
                Intent i=new Intent(Splash_Screen.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        },time);
    }

 }