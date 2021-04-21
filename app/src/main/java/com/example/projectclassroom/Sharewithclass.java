package com.example.projectclassroom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import fragments.EnrollFragment;
import fragments.ShareFilesFragment;
import model.Sharefilemodel;

public class Sharewithclass extends AppCompatActivity {
    DatabaseReference reference;
    FirebaseStorage storage;
    FirebaseDatabase database;
    Button attachments, share;
    StorageReference storageReference;
    private static final String filename = "login";
    private static final String user = "username";
    private static final String classcode = "code";
    ProgressDialog progressDialog;
    Uri uri; // uri are actually urls that are meant for local storage.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharewithclass);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        attachments = findViewById(R.id.attachments);
        attachments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Sharewithclass.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectFile();
                } else {
                    ActivityCompat.requestPermissions(Sharewithclass.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }
            }
        });

        share = findViewById(R.id.shareid);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri != null) {
                    EditText sharetext = findViewById(R.id.sharetext);
                    String s = sharetext.getText().toString();
                    if (!sharetext()) {
                        return;
                    } else {
                        Log.d("sharetext", s + "");
                        uploadFile(uri, s);
                    }

                } else
                    Toast.makeText(Sharewithclass.this, "please select a file", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean sharetext() {
        TextInputLayout sharetxt = findViewById(R.id.sharetxt);
        String s = sharetxt.getEditText().getText().toString();
        if (s.isEmpty()) {
            sharetxt.setError("field cannot empty");
            return false;
        } else {
            sharetxt.setError(null);
            return true;
        }
    }

    private void uploadFile(Uri uri, String s) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file");
        progressDialog.setProgress(0);
        progressDialog.show();

        SharedPreferences sharedPreferences = getSharedPreferences(filename, MODE_PRIVATE);
        String users = sharedPreferences.getString(user, "");
        String cc = sharedPreferences.getString(classcode, "");
        Log.d("classcode", users + "");
        Date date = new Date();
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        Log.d("mydate", df.format("MM dd, yyyy", date) + "");

        String filename = System.currentTimeMillis() + "";

        storageReference = storage.getReference();
        storageReference.child("Uploads").child(filename).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String url = taskSnapshot.getUploadSessionUri().toString();
                Log.d("u", url + "");
                reference = database.getReference("users"); //return the path to root.
                Log.d("r", reference + "");
                String rancode = UUID.randomUUID().toString();
                reference.child("group").child(cc).child("files").child(rancode.substring(0, 13)).child("msg").setValue(s);
                reference.child("group").child(cc).child("files").child(rancode.substring(0, 13)).child("username").setValue(users);
                reference.child("group").child(cc).child("files").child(rancode.substring(0, 13)).child("date").setValue(df.format("MMMM dd, yyyy hh:mm", date));
                reference.child("group").child(cc).child("files").child(rancode.substring(0, 13)).child(filename).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(Sharewithclass.this, "file successfully uploaded...", Toast.LENGTH_SHORT).show();

                        } else
                            Toast.makeText(Sharewithclass.this, "Not uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Sharewithclass.this, "Not uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                //track the progress of our upload
                int currentprogress = (int) (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressDialog.setProgress(currentprogress);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 9 | grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectFile();
        } else {
            Toast.makeText(this, "plz provide permission", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectFile() {
        //to offer user to select a file using file manager
        //we will be use an intent
        Intent i = new Intent();
        i.setType("*/*");
        i.setAction(Intent.ACTION_GET_CONTENT); //It Helps to fetch the files
        startActivityForResult(i, 42);//we can put any number in requestCode

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //this will check whether user successfully selected a file or not.

        if (requestCode == 42 && resultCode == RESULT_OK && data != null) {

            uri = data.getData();  //return the uri of selected file.
            Toast.makeText(this, "File is selected", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "please select a file.", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void cancel(View view) {
        onBackPressed();
    }

}