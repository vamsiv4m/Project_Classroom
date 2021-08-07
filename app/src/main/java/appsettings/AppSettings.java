package appsettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectclassroom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class AppSettings extends AppCompatActivity {
    TextInputLayout passwdtil;
    TextView settingspasswd,settingsuname,settingsemail;
    Button submit;
    DatabaseReference reference;
    SharedPreferences sharedPreferences;
    private static final String filename="login";
    private static final String user="username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        sharedPreferences=getSharedPreferences(filename,0);
        submit=findViewById(R.id.submit);
        settingsuname=findViewById(R.id.settingsuname);
        settingsemail=findViewById(R.id.settingsemail);
        String u=sharedPreferences.getString(user,"");
        reference=FirebaseDatabase.getInstance().getReference("users");
        reference.child(u).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String e=snapshot.child("email").getValue().toString();
                settingsuname.setText(u);
                settingsemail.setText(e);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        Button changepass=findViewById(R.id.changepasswd);
        settingspasswd=findViewById(R.id.settingspasswd);
        passwdtil=findViewById(R.id.passwdtil);
        changepass.setOnClickListener(view -> passwdtil.setEnabled(true));
        submit.setOnClickListener(view -> {
            String passwd=settingspasswd.getText().toString();
            String uname=sharedPreferences.getString(user,"");
            reference= FirebaseDatabase.getInstance().getReference("users");
            reference.child(uname).child("password").setValue(passwd).addOnCompleteListener(task -> {
                Toast.makeText(AppSettings.this, "Successfully Password Changed", Toast.LENGTH_SHORT).show();
                onBackPressed();
            });
        });
    }

    public void backed(View view) {
        onBackPressed();
    }
}