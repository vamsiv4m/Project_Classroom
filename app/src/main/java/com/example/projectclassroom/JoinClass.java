package com.example.projectclassroom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import model.Createuserdatapojo;
import model.SessionManager;

public class JoinClass extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String filename="join";
    public static final String classname="classname";
    public static final String subject="subject";
    public static final String section="section";
    FirebaseDatabase database;
    DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_class);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public void back(View view) {
        onBackPressed();
    }

    private Boolean validationjoincode() {
        TextInputLayout til = (TextInputLayout) findViewById(R.id.til);
        EditText joinclass=(EditText)findViewById(R.id.editText);
        String text=joinclass.getText().toString();
        if (text.isEmpty()) {
            til.setError("Field cannot empty");
            return false;
        } else {
            til.setError(null);
            return true;
        }
    }
    public void join(View view) {
       if(!validationjoincode()) {
           return;
       }
       else joiningclass_method();
    }
    public void joiningclass_method() {

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userdetails = sessionManager.getUserSession();
        String user = userdetails.get(SessionManager.Username);


        EditText joinclass=(EditText)findViewById(R.id.editText);
        String text=joinclass.getText().toString();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("d",""+dataSnapshot);
                    Map<String,Object> code=(Map<String, Object>) dataSnapshot.getValue();
                    if(code.get("class")==null){
                        continue;
                    }
                    Map<String,Object> code1=(Map<String, Object>) code.get("class");
                    Log.d("c1",""+code);
                    code1.forEach(new BiConsumer<String, Object>() {
                        @Override
                        public void accept(String s, Object o) {
                            String c="class";
                            if(s.equals(text)){
                                Log.d("s",""+s);
                                Log.d("o",""+o);
                                Map<String,Object> obj= (Map<String, Object>) o;
                                Toast.makeText(JoinClass.this, ""+obj.get("subject"), Toast.LENGTH_SHORT).show();
                                Createuserdatapojo createuserdatapojo=new Createuserdatapojo((String)obj.get("classname"),(String)obj.get("section"),(String)obj.get("room"),(String)obj.get("subject"),(String)obj.get("class_code"));
                                database=FirebaseDatabase.getInstance();
                                myref=database.getReference("users").child(user);
                                myref.child("class").child((String)obj.get(subject)).setValue(createuserdatapojo);
                                try {
                                    Thread.sleep(500);
                                    finish();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public BiConsumer<String, Object> andThen(BiConsumer<? super String, ? super Object> after) {
                            return null;
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(JoinClass.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}