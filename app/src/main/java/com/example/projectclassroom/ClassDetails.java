 package com.example.projectclassroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

public class ClassDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextView textView, textView2;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigation;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private static final String filename = "login";
    private static final String user="username";
    private static final String email="email";

    @Override
    protected void onStart() {
        super.onStart();
        navigation=findViewById(R.id.navigation2);
        View navi = navigation.getHeaderView(0);
        SharedPreferences sharedPreferences=getSharedPreferences(filename,Context.MODE_PRIVATE);
        String a=sharedPreferences.getString(user,"");
        String m=sharedPreferences.getString(email,"");
        TextView unameheader=navi.findViewById(R.id.unameheader);
        TextView emailheader=navi.findViewById(R.id.emailheader);
        unameheader.setText(a);
        emailheader.setText(m);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView imageView=findViewById(R.id.imgview);
        setContentView(R.layout.navigation_activity_2);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout2);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationDrawer();
        Intent intent = getIntent();
        String sub = intent.getStringExtra("subject");
        String sec = intent.getStringExtra("section");
        String imgurl=intent.getStringExtra("imglink");
        Log.d("img",imgurl+"");
        textView = findViewById(R.id.sub);
        textView2 = findViewById(R.id.sec);
        textView.setText(sub);
        textView2.setText(sec);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.classmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void navigationDrawer() {
        navigation = findViewById(R.id.navigation2);
        navigation.bringToFront();
        View nav = navigation.getHeaderView(0);
        TextView ntext = nav.findViewById(R.id.unameheader);
        navigation.setNavigationItemSelectedListener( this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.createclassmenu:
                i = new Intent(this, MainActivity2.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;
            case R.id.joinclassroommenu:
                i = new Intent(this, JoinClass.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void share(View view) {
        Intent i = new Intent(getApplicationContext(), Sharewithclass.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){ SharedPreferences sharedPreferences=getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
        //Logout
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
        }
        drawerLayout = findViewById(R.id.drawerLayout2);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        drawerLayout = findViewById(R.id.drawerLayout2);
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
}