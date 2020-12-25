package com.example.projectclassroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import model.Userdetailspojo;

public class CreateaccountActivity extends AppCompatActivity {
    EditText name,email,code,phoneno,password;
    Button createbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        name=(EditText)findViewById(R.id.name);
        email=findViewById(R.id.email);
        code=findViewById(R.id.code);
        phoneno=findViewById(R.id.phone);
        password=findViewById(R.id.passwd);
    }
    public void createbtn(View view) {
        String username=name.getText().toString();
        String emailid=email.getText().toString();
        String c=code.getText().toString();
        String passwd=password.getText().toString();
        String phone=phoneno.getText().toString();
        String uniqueId= UUID.randomUUID().toString().substring(0,8);
        Userdetailspojo userdetailspojo=new Userdetailspojo(username,emailid,c,phone,passwd);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.child(username).setValue(userdetailspojo);
    }
}