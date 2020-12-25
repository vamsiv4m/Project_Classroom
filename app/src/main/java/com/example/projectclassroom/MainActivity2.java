package com.example.projectclassroom;

import androidx.appcompat.app.AppCompatActivity;
import java.util.UUID;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Createuserdatapojo;

public class MainActivity2 extends AppCompatActivity {
   Button create;
    EditText classname,section,room,subject;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ProgressBar progressBar;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        classname=findViewById(R.id.classname);
        section=findViewById(R.id.section);
        room=findViewById(R.id.room);
        subject=findViewById(R.id.subject);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);

    }
    public void back(View view) {
        onBackPressed();
    }


    public void create(View view) {
        //progress bar
//         progressBar.setVisibility(View.VISIBLE);
        //store the edittext data into variables...
        String cn=classname.getText().toString();
        String sec=section.getText().toString();
        String r=room.getText().toString();
        String sub=subject.getText().toString();
        String uniqueId=UUID.randomUUID().toString().substring(0,8);
        String class_code=UUID.randomUUID().toString().substring(0,5);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("create_class");
        Createuserdatapojo createuserdatapojo=new Createuserdatapojo(cn,sec,r,sub,class_code);
        myRef.child(class_code).setValue(createuserdatapojo);
    }
}