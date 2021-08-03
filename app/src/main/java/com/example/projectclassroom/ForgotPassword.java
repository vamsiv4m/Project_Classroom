package com.example.projectclassroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgotPassword extends AppCompatActivity {
    TextInputLayout username, newpassword, confirmpassword;
    EditText forgotuname,forgotnewpass,forgotconfirmpass;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        username = findViewById(R.id.username);
        forgotuname=findViewById(R.id.forgotuname);
        forgotnewpass=findViewById(R.id.forgotnewpass);
        forgotconfirmpass=findViewById(R.id.forgotconfirmpass);
        newpassword = findViewById(R.id.newpassword);
        confirmpassword = findViewById(R.id.confirmpassword);
        save=findViewById(R.id.savebtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validationUsername() | !validationnewpassword() | !validationconfirmpassword()) {
                    return;
                } else {
                    forgotpassword();
                }
            }
        });
    }

    private boolean validationnewpassword() {
        newpassword = (TextInputLayout) findViewById(R.id.newpassword);
        String newpasswd = forgotnewpass.getText().toString();
        if (newpasswd.isEmpty()) {
            newpassword.setError("Field cannot empty");
            return false;
        } else {
            newpassword.setError(null);
            return true;
        }
    }

    private void forgotpassword() {
        String confirmpasswd = forgotconfirmpass.getText().toString();
        String uname = forgotuname.getText().toString();
        String newpasswd = forgotnewpass.getText().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query query = reference.orderByChild("username").equalTo(uname);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (newpasswd.equals(confirmpasswd)) {
                        reference.child(uname).child("password").setValue(confirmpasswd);
                        Toast.makeText(ForgotPassword.this, "Successfully Password Changed", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(ForgotPassword.this, "Confirm password is not same as New password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPassword.this, "No user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private boolean validationconfirmpassword() {
        confirmpassword = (TextInputLayout) findViewById(R.id.confirmpassword);
        String confirmpasswd = forgotconfirmpass.getText().toString();
        if (confirmpasswd.isEmpty()) {
            confirmpassword.setError("Field cannot empty");
            return false;
        } else {
            confirmpassword.setError(null);
            return true;
        }
    }

    private Boolean validationUsername() {
        username = (TextInputLayout) findViewById(R.id.username);
        String uname = forgotuname.getText().toString();
        if (uname.isEmpty()) {
            username.setError("Field cannot empty");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }
}