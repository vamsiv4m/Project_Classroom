package com.example.projectclassroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import model.Createuserdatapojo;
import model.SessionManager;

public class CreateClass extends AppCompatActivity {
    EditText classname, section, room, subject;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ProgressBar progressBar;
    TextInputLayout til1, til2, til3, til4;
    private String img1="https://firebasestorage.googleapis.com/v0/b/chat-prattle.appspot.com/o/Background_Images%2FDownload%20wallpapers%20in%20zip%20file%20Click%20Here%20or%20mirror.png?alt=media&token=791ebdf6-f32b-49ca-aa21-e967be2b2b0f";
    private String img2="https://firebasestorage.googleapis.com/v0/b/chat-prattle.appspot.com/o/Background_Images%2FUntitled.jpg?alt=media&token=b41b4de7-1a6a-4921-89d0-027466eab27e";
    private String img3="https://firebasestorage.googleapis.com/v0/b/chat-prattle.appspot.com/o/Background_Images%2Fbg.jpg?alt=media&token=0b6797b4-4e3e-4a00-8804-8c95df6c5c16";
    private String img4="https://firebasestorage.googleapis.com/v0/b/chat-prattle.appspot.com/o/Background_Images%2Fbgimg.png?alt=media&token=c0622115-b922-4318-9fc9-6dcde86551f2";
    List<String> list = new ArrayList<>();
    // add 5 element in ArrayList


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        classname = findViewById(R.id.classname);
        section = findViewById(R.id.section);
        room = findViewById(R.id.room);
        subject = findViewById(R.id.subject);
        progressBar = (ProgressBar) findViewById(R.id.createclassprogressbar);
        progressBar.setVisibility(View.GONE);
        list.add(img1);
        list.add(img2);
        list.add(img3);
        list.add(img4);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean validationclass() {
        til1 = (TextInputLayout) findViewById(R.id.class_name);

        if (classname.getText().toString().isEmpty()) {
            til1.setError("Field should not be empty");
            return false;
        } else {
            til1.setError(null);
            return true;
        }
    }

    public boolean validationsection() {
        til2 = (TextInputLayout) findViewById(R.id.section_til);

        if (section.getText().toString().isEmpty()) {
            til2.setError("Field should not be empty");
            return false;
        } else {
            til2.setError(null);
            return true;
        }
    }

    public boolean validationroom() {
        til3 = (TextInputLayout) findViewById(R.id.room_til);
        if (room.getText().toString().isEmpty()) {
            til3.setError("Field should not be empty");
            return false;
        } else {
            til3.setError(null);
            return true;
        }
    }

    public boolean validationsubject() {
        til4 = (TextInputLayout) findViewById(R.id.subject_til);
        if (subject.getText().toString().isEmpty()) {
            til4.setError("Field should not be empty");
            return false;
        } else {
            til4.setError(null);
            return true;
        }
    }


    public void create(View view) {
        if (!validationclass() | !validationsection() | !validationroom() | !validationsubject()) {
            return;
        } else {
            createclassintofirebase();
        }
    }

    private void createclassintofirebase() {
        //store the edittext data into variables...
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userdetails = sessionManager.getUserSession();
        String user = userdetails.get(SessionManager.Username);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        progressBar.findViewById(R.id.createclassprogressbar).setVisibility(View.VISIBLE);
        Query checkuser = reference.orderByChild("username").equalTo(user);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //list

                    SecureRandom random=new SecureRandom();
                    int index=random.nextInt(list.size());
                    Log.d("l",list.get(index)+"");

                    String cn = classname.getText().toString().substring(0, 1).toUpperCase()+classname.getText().toString().substring(1);
                    String sec = section.getText().toString().toUpperCase();
                    String r = room.getText().toString();
                    String sub = subject.getText().toString().substring(0, 1).toUpperCase()+subject.getText().toString().substring(1);
                    String class_code = UUID.randomUUID().toString().substring(0, 6);
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("users").child(user);
                    Createuserdatapojo createuserdatapojo = new Createuserdatapojo(user,cn, sec, r, sub, class_code, (String)list.get(index));
                    myRef.child("class").child(class_code).setValue(createuserdatapojo);
                    Toast.makeText(CreateClass.this, "Class is created", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CreateClass.this, "Class is not created", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                        Toast.makeText(CreateClass.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void back(View view) {
        onBackPressed();
    }
}