package com.example.projectclassroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
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

import model.Userdetailspojo;

public class CreateaccountActivity extends AppCompatActivity {
    //variables
    EditText name,email,phoneno,password;
    ProgressBar progressBar;
    TextInputLayout til1;
    TextInputLayout til2;
    TextInputLayout til3;
    TextInputLayout til4;
    String uname;
    String passwd;
    String mail;
    String phonenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //view are FindviewById
        progressBar=findViewById(R.id.progressBar3);
        name=(EditText)findViewById(R.id.name);
        email=findViewById(R.id.email);
        phoneno=findViewById(R.id.phone);
        password=findViewById(R.id.passwd);
        progressBar.setVisibility(View.GONE);
    }
    private Boolean validationUsername() {
        til1 = (TextInputLayout) findViewById(R.id.createname);
        uname = name.getText().toString();
        String nowhitespace= "\\A\\w{4,20}\\z";
        if (uname.isEmpty()) {
            til1.setError("Field cannot empty");
            return false;
        } else if (uname.length() > 15) {
            til1.setError("Name should be less than 15 characters.");
            return false;
        }
        else if (!uname.matches(nowhitespace)) {
            til1.setError("whitespaces are not allowed");
            return false;
        }
        else {
            til1.setError(null);
            return true;
        }
    }
    private Boolean validationpassword() {
        til2 = (TextInputLayout) findViewById(R.id.createpasswd);
        passwd = password.getText().toString();
//        String passwordVal = "^" +
//                //"(?=.*[0-9])" +         //at least 1 digit
//                //"(?=.*[a-z])" +         //at least 1 lower case letter
//                //"(?=.*[A-Z])" +         //at least 1 upper case letter
//                "(?=.*[a-zA-Z])" +      //any letter
//                "(?=.*[@#$%^&+=])" +    //at least 1 special character
//                "(?=\\S+$)" +           //no white spaces
//                "(?=.*[a-zA-Z])" +               //at least 4 characters
//                "$";
        if (passwd.isEmpty()) {
            til2.setError("Field cannot empty");
            return false;
        }
        else if(passwd.length()<8){
            til2.setError("Password length must be greater than 8 characters.");
            return false;
        }
            else {
            til2.setError(null);
            til2.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validationemail() {
        til3 = (TextInputLayout) findViewById(R.id.createemail);
        mail = email.getText().toString();
        String emailpattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (mail.isEmpty()) {
            til3.setError("Field cannot empty");
            return false;
        }
        else if(!mail.matches(emailpattern)){
            til3.setError("Invalid Email Address");
            return false;
        }
            else {
            til3.setError(null);
            return true;
        }
    }
    private Boolean validationphone() {
        til4 = (TextInputLayout) findViewById(R.id.createphoneno);
        phonenum = phoneno.getText().toString();
        if (phonenum.isEmpty()) {
            til4.setError("Field cannot empty");
            return false;
        } else {
            til4.setError(null);
            return true;
        }
    }
    public void createbtn(View view) {
        if(!validationUsername() | !validationpassword() | !validationemail() | !validationphone()) {
            return;
        }
        else {
            createaccount();
        }
    }
    private void createaccount() {
        progressBar=findViewById(R.id.progressBar3);
        String username=til1.getEditText().getText().toString();
        String emailid=email.getText().toString();
        String passwd=password.getText().toString();
        String phone=phoneno.getText().toString();
        Userdetailspojo userdetailspojo=new Userdetailspojo(username,emailid,phone,passwd);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkuser = reference.orderByChild("username").equalTo(username);
        progressBar.setVisibility(View.VISIBLE);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    progressBar.setVisibility(View.GONE);
                    til1.setError("Username already Exists.Try Another name.");
                }
                else{
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("users");
                    myRef.child(username).setValue(userdetailspojo);
                    progressBar.setVisibility(View.GONE);
                    Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                    Toast.makeText(CreateaccountActivity.this, "Thank u for Register.Please Login.", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CreateaccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}