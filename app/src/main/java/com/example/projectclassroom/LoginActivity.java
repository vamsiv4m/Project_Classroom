package com.example.projectclassroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button login;
    String uname, passwd;
    TextInputLayout til1, til2;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    private int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final String filename = "login";
    private static final String user="username";
    private static final String email="email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        createRequest();

        sharedPreferences=getSharedPreferences(filename, Context.MODE_PRIVATE);
        login= (Button) findViewById(R.id.loginBtn);

        if(sharedPreferences.contains(user)){
            Intent i=new Intent(this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validationUsername() | !validationpassword()) {
                    return;
                } else {
                    userlogin();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent exit=new Intent(Intent.ACTION_MAIN);
        exit.addCategory(Intent.CATEGORY_HOME);
        exit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(exit);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
    }

    //validation methods
    private Boolean validationUsername() {
        til1 = (TextInputLayout) findViewById(R.id.sharetxt);
        username = findViewById(R.id.sharetext);
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


    public void userlogin() {
        username = findViewById(R.id.sharetext);
        password = findViewById(R.id.passwd);
        til1 = (TextInputLayout) findViewById(R.id.sharetxt);
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
                        String fullname = snapshot.child(uname).child("username").getValue(String.class);
                        String emailid = snapshot.child(uname).child("email").getValue(String.class);
                        Log.d("f",""+fullname);
                        Log.d("m",""+emailid);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString(user,fullname);
                        editor.putString(email,emailid);
                        editor.apply();
                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();

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
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        else{

        }
    }

}
