package com.example.projectclassroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.UUID;
import android.os.Bundle;
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

public class MainActivity2 extends AppCompatActivity {
    EditText classname, section, room, subject;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ProgressBar progressBar;
    TextInputLayout til1, til2, til3, til4;

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
                    String cn = classname.getText().toString();
                    String sec = section.getText().toString();
                    String r = room.getText().toString();
                    String sub = subject.getText().toString();
                    String class_code = UUID.randomUUID().toString().substring(0, 5);
                    database = FirebaseDatabase.getInstance();
                    myRef = database.getReference("users").child(user);
                    Createuserdatapojo createuserdatapojo = new Createuserdatapojo(cn, sec, r, sub, class_code);
                    myRef.child("class").child(class_code).setValue(createuserdatapojo);
                    Toast.makeText(MainActivity2.this, "Class is created", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity2.this, "Class is not created", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity2.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void back(View view) {
        onBackPressed();
    }
}