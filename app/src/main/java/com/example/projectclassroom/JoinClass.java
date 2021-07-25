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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

import model.Createuserdatapojo;
import model.SessionManager;

public class JoinClass extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String filename="join";
    public static final String classname="classname";
    public static final String subject="subject";
    public static final String section="section";
    private String img3="https://firebasestorage.googleapis.com/v0/b/chat-prattle.appspot.com/o/Background_Images%2FDownload%20wallpapers%20in%20zip%20file%20Click%20Here%20or%20mirror.png?alt=media&token=791ebdf6-f32b-49ca-aa21-e967be2b2b0f";
    private String img2="https://firebasestorage.googleapis.com/v0/b/chat-prattle.appspot.com/o/Background_Images%2FUntitled.jpg?alt=media&token=b41b4de7-1a6a-4921-89d0-027466eab27e";
    private String img1="https://firebasestorage.googleapis.com/v0/b/chat-prattle.appspot.com/o/Background_Images%2Fbg.jpg?alt=media&token=0b6797b4-4e3e-4a00-8804-8c95df6c5c16";
    private String img4="https://firebasestorage.googleapis.com/v0/b/chat-prattle.appspot.com/o/Background_Images%2Fbgimg.png?alt=media&token=c0622115-b922-4318-9fc9-6dcde86551f2";
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

        List<String> list=new ArrayList<>();
        list.add(img2);
        list.add(img3);
        list.add(img4);
        list.add(img1);
        EditText joinclass=(EditText)findViewById(R.id.editText);
        String text=joinclass.getText().toString();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        SecureRandom secureRandom=new SecureRandom();
        int index=secureRandom.nextInt(list.size());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("d",""+dataSnapshot);
                    Map<Object,Object> code= (Map<Object, Object>) dataSnapshot.getValue();
                    String TAG="t";
                    Log.d(TAG, code+"");
                    if(code.get("class")==null){
                        continue;
                    }
                    Map<String,Object> code1=(Map<String, Object>) code.get("class");
                    Log.d("c1",""+code1);
                    assert code1 != null;
                    code1.forEach(new BiConsumer<String, Object>() {
                        @Override
                        public void accept(String s, Object o) {
                            if(s.equals(text)){
                                Log.d("s",""+s);
                                Log.d("o",""+o);
                                Map<String,Object> obj= (Map<String, Object>) o;
                                Toast.makeText(JoinClass.this, ""+obj.get("subject"), Toast.LENGTH_SHORT).show();
                                Createuserdatapojo createuserdatapojo=new Createuserdatapojo(user,(String)obj.get("classname"),(String)obj.get("section"),(String)obj.get("room"),(String)obj.get("subject"),(String)obj.get("class_code"),list.get(index));
                                database=FirebaseDatabase.getInstance();
                                assert user != null;
                                myref=database.getReference("users").child(user);
                                myref.child("joinclass").child((String) Objects.requireNonNull(obj.get("class_code"))).setValue(createuserdatapojo);
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