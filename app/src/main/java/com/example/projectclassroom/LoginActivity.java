package com.example.projectclassroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText username;
    EditText password;
    String uname, passwd;
    TextInputLayout til1, til2;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }
    //validation methods
    private Boolean validationUsername() {
        til1 = (TextInputLayout) findViewById(R.id.userInputLayout);
        username = findViewById(R.id.usernameInput);
        uname = username.getEditableText().toString();
        if (uname.isEmpty()) {
            til1.setError("Field cannot empty");
            return false;
        } else {
            til1.setError(null);
            return true;
        }
    }

    private Boolean validationpassword() {
        til2 = (TextInputLayout) findViewById(R.id.passwordInputLayout);
        password = findViewById(R.id.passwd);
        passwd = password.getText().toString();
        if (passwd.isEmpty()) {
            til2.setError("Field cannot empty");
            return false;
        } else {
            til2.setError(null);
            return true;
        }
    }

    public void login(View view) {
        if (!validationUsername() | !validationpassword()) {
            return;
        } else {
            userlogin();
        }
    }

    private void userlogin() {
        username = findViewById(R.id.usernameInput);
        password = findViewById(R.id.passwd);
        til1 = (TextInputLayout) findViewById(R.id.userInputLayout);
        til2 = (TextInputLayout) findViewById(R.id.passwordInputLayout);
        String uname = username.getText().toString();
        String passwd = password.getText().toString();
        progressBar=findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable ().setColorFilter (0xFFcc0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkuser=reference.orderByChild("name").equalTo(uname);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String pass=snapshot.child(uname).child("password").getValue(String.class);
                    if(pass.equals(passwd)){
                        progressBar.setVisibility(View.GONE);
                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }
                    else{
                        til2.setError("wrong password");
                    }
                }
                else{
                    til1.setError("No user");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void createanaccount(View view) {
        Intent i = new Intent(this, CreateaccountActivity.class);
        startActivity(i);
    }

}
