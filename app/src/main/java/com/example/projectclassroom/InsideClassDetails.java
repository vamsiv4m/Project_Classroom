package com.example.projectclassroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import fragments.ChatFragment;
import fragments.ShareFilesFragment;

public class InsideClassDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView textView, textView2;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigation;
    ActionBarDrawerToggle actionBarDrawerToggle;
    private static final String filename = "login";
    private static final String user = "username";
    private static final String email = "email";
    private static final String classcode="code";
    private static final String subject="sub";
    private static final String section="sec";
    private static final String profname="prof";

    @Override
    protected void onStart() {
        super.onStart();
        navigation = findViewById(R.id.navigationbar);
        View navi = navigation.getHeaderView(0);
        SharedPreferences sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
        String a = sharedPreferences.getString(user, "");
        String m = sharedPreferences.getString(email, "");
        TextView unameheader = navi.findViewById(R.id.unameheader);
        TextView emailheader = navi.findViewById(R.id.emailheader);
        unameheader.setText(a);
        emailheader.setText(m);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView imageView = findViewById(R.id.imgview);
        setContentView(R.layout.activity_class_details);
        SharedPreferences sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationDrawer();

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayoutinsideclass, new ShareFilesFragment()).commit();

        Intent intent = getIntent();
        String sub = intent.getStringExtra("subject");
        String sec = intent.getStringExtra("section");
        String prof = intent.getStringExtra("profname");
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(section,sec);
        editor.putString(subject,sub);
        editor.putString(profname,prof);
        editor.apply();
        String imgurl = intent.getStringExtra("imglink");
        String cc=intent.getStringExtra("code");
        editor.putString(classcode,cc);
        editor.apply();
        Log.d("img", imgurl + "");
        bottomnav();

    }

    private void bottomnav() {
        ChipNavigationBar chipNavigationBar = findViewById(R.id.chipNavigationBar);
        chipNavigationBar.setItemSelected(R.id.files, true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.files:
                        fragment = new ShareFilesFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayoutinsideclass, fragment).commit();
                        break;
                    case R.id.chat:
                        fragment = new ChatFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayoutinsideclass, fragment).addToBackStack(null).commit();
                        chipNavigationBar.setVisibility(View.GONE);
                        toolbar.setTitle("Chatroom");
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.classmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void navigationDrawer() {
        navigation = findViewById(R.id.navigationbar);
        navigation.bringToFront();
        View nav = navigation.getHeaderView(0);
        navigation.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.createclassmenu:
                i = new Intent(this, CreateClass.class);
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
        if (item.getItemId() == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            //Logout
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }
        drawerLayout = findViewById(R.id.drawer);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        drawerLayout = findViewById(R.id.drawer);
        ChipNavigationBar chipNavigationBar = findViewById(R.id.chipNavigationBar);
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (chipNavigationBar.getSelectedItemId()==R.id.files) {
                finish();
            } else {
                toolbar.setTitle("KoalaBear");
                chipNavigationBar.setVisibility(View.VISIBLE);
                chipNavigationBar.setItemSelected(R.id.files, true);
            }
        }
    }
}