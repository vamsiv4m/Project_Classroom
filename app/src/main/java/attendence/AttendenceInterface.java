package attendence;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projectclassroom.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AttendenceInterface extends AppCompatActivity {
    FloatingActionButton fab;
    Button cancel,ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_interface);
        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(AttendenceInterface.this);
                View view=getLayoutInflater().inflate(R.layout.floating_dialog,null);
                builder.setView(view);
                final AlertDialog alertDialog=builder.create();
                cancel=findViewById(R.id.cancel);
                ok=findViewById(R.id.ok);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }
}