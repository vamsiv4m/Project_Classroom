package com.example.projectclassroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;
import java.util.List;

import adapter.ClassAdapter;
import appsettings.AppSettings;
import fragments.EnrollFragment;
import fragments.TeachFragment;
import model.FetchData;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener {
    //variables
    Toolbar toolbar;
    NavigationView navigation;
    DrawerLayout drawerLayout;
    ProgressBar progressBar;
    ChipNavigationBar chipNavigationBar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    SwipeRefreshLayout swipeRefreshLayout;
    private static final String filename = "login";
    private static final String user = "username";
    private static final String email = "email";
    private static final String codename = "classcode";
    List<FetchData> fetchDataList = new ArrayList<>();
    ClassAdapter adapter;

    @Override
    protected void onStart() {
        super.onStart();
        navigation = findViewById(R.id.navigation);
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
        setContentView(R.layout.navigation_activity_1);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        progressBar = findViewById(R.id.progressEnroll);
        adapter = new ClassAdapter(MainActivity.this, fetchDataList);
        //To disable night mode view for our app ...
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // bottomNavigationView = findViewById(R.id.bottomnav);
        //custom tool bar.
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        swipeRefreshLayout = findViewById(R.id.swipe);
        navigation = findViewById(R.id.navigation);
        navigationDrawer();
        runfragment();
        bottomnav();

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            View navi = navigation.getHeaderView(0);
            TextView googleusername = navi.findViewById(R.id.unameheader);
            TextView googlemailid = navi.findViewById(R.id.emailheader);
        }
    }

    private void bottomnav() {
        chipNavigationBar = findViewById(R.id.bottomnav);
        chipNavigationBar.setItemSelected(R.id.enroll, true);
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.enroll:
                        fragment = new EnrollFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;
                    case R.id.teach:
                        fragment = new TeachFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).addToBackStack(null).commit();
                        break;
                }
            }
        });
    }

    private void runfragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new EnrollFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            if (chipNavigationBar.getSelectedItemId() == R.id.enroll) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are You Sure to Exit?")
                        .setTitle("Exit").setIcon(R.drawable.ic_baseline_exit_to_app_24)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent exit = new Intent(Intent.ACTION_MAIN);
                                exit.addCategory(Intent.CATEGORY_HOME);
                                exit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(exit);
                                finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                chipNavigationBar.setItemSelected(R.id.enroll, true);
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //....Logout implementation.....
        SharedPreferences sharedPreferences = getSharedPreferences(filename, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (item.getItemId() == R.id.logout) {
            editor.clear();
            editor.apply();
            //Logout
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        } else if (item.getItemId()==R.id.a){
            Intent i=new Intent(getApplicationContext(), AppSettings.class);
            startActivity(i);
        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return true;
    }

    public void navigationDrawer() {
        navigation.bringToFront();
        View nav = navigation.getHeaderView(0);
        TextView ntext = nav.findViewById(R.id.unameheader);
        navigation.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
}