package com.example.projectclassroom;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import model.SessionManager;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,PopupMenu.OnMenuItemClickListener {
    //variables
    Button menuicon;
    NavigationView navigation;
    DrawerLayout drawerLayout;

    ImageView create;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //To disable night mode view for our app ...
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        menuicon = findViewById(R.id.menuicon);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigation = findViewById(R.id.navigation);
        navigationDrawer();
        GoogleSignInAccount signInAccount= GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount!=null){
            View nav=navigation.getHeaderView(0);
            TextView googleusername=nav.findViewById(R.id.unameheader);
            TextView googlemailid=nav.findViewById(R.id.emailheader);
            googleusername.setText(signInAccount.getDisplayName());
            googlemailid.setText(signInAccount.getEmail());
        }
        else {
            View nav=navigation.getHeaderView(0);
            TextView googleusername=nav.findViewById(R.id.unameheader);
            TextView googlemailid=nav.findViewById(R.id.emailheader);
            googleusername.setText("");
            googlemailid.setText("Mail-Id");

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

    public void click(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.add_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return true;
    }

    public void create_classroom(MenuItem item) {
        Intent i = new Intent(this, MainActivity2.class);
        startActivity(i);
    }

    public void navigationDrawer(){
        navigation.bringToFront();
        View nav=navigation.getHeaderView(0);
        TextView ntext=nav.findViewById(R.id.unameheader);
//        SessionManager sessionManager=new SessionManager(MainActivity.this);
//        HashMap<String,String> userdetails=sessionManager.getuserDetails();
//        String user=userdetails.get(sessionManager.Key_name);

        navigation.setNavigationItemSelectedListener(this);
    }

    public void logout(MenuItem item) {
        //Logout
        FirebaseAuth.getInstance().signOut();
        Intent i= new Intent(getApplicationContext(),LoginActivity.class);
        View nav=navigation.getHeaderView(0);
        TextView googlename=nav.findViewById(R.id.unameheader);
        TextView googleemail=nav.findViewById(R.id.emailheader);
        googlename.setText("username");
        googleemail.setText("email-id");
        startActivity(i);
        finish();
    }

    public void joinClassroom(MenuItem item) {
        Intent intent=new Intent(this, JoinClass.class);
        startActivity(intent);
    }

    public void menu(View view) {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            drawerLayout.isDrawerVisible(GravityCompat.START);
        }
    }
}
