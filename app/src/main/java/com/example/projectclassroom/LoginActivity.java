package com.example.projectclassroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public void createanaccount(View view) {
        Intent i=new Intent(this,CreateaccountActivity.class);
        startActivity(i);
    }

    public void login(View view) {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }
}