package com.example.projectclassroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import model.SessionManager;

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    String uname, passwd;
    TextInputLayout til1, til2;
    ProgressBar progressBar;
    private int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    public LoginActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        createRequest();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
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

    public void userlogin() {
        username = findViewById(R.id.usernameInput);
        password = findViewById(R.id.passwd);
        til1 = (TextInputLayout) findViewById(R.id.userInputLayout);
        til2 = (TextInputLayout) findViewById(R.id.passwordInputLayout);
        String uname = username.getText().toString();
        String passwd = password.getText().toString();
        progressBar = findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFcc0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkuser = reference.orderByChild("username").equalTo(uname);
        checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String pass = snapshot.child(uname).child("password").getValue(String.class);
                    if (pass.equals(passwd)) {
                        progressBar.setVisibility(View.GONE);
                        //get users
                        String fullname = snapshot.child(uname).child("username").getValue(String.class);
                        String passwd = snapshot.child(uname).child("password").getValue(String.class);
                        SessionManager sessionManager=new SessionManager(LoginActivity.this);
                        sessionManager.storeData(fullname);
                        startActivity(new Intent(getApplicationContext(),MainActivity2.class));
                        moveTomain();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        til2.setError("wrong password");
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    til1.setError("No user");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    private void moveTomain() {


        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void createanaccount(View view) {
        Intent i = new Intent(this, CreateaccountActivity.class);
        startActivity(i);
    }

    public void signInwithGoogle(View view) {
        signIn();
    }

    private void createRequest() {
// Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
//    public final void close(EditText username,EditText password){
//        EditText username = (EditText) findViewById(R.id.usernameInput);
//        EditText password = (EditText)findViewById(R.id.passwd);
//        username.setText("v");
//        password.setText("v");
//    }
}
