package appsettings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projectclassroom.R;
import com.google.android.material.textfield.TextInputLayout;

public class AppSettings extends AppCompatActivity {
    TextInputLayout passwdtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        Button changepass=findViewById(R.id.changepasswd);
        passwdtil=findViewById(R.id.passwdtil);
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwdtil.setEnabled(true);
            }
        });
    }

    public void backed(View view) {
        onBackPressed();
    }
}