package editclass;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectclassroom.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.LongFunction;

import fragments.TeachFragment;

public class EditClass extends AppCompatActivity {
    TextView classcode;
    TextInputLayout textInputLayout1, textInputLayout2, textInputLayout3;
    private final static String filename = "login";
    private final static String user = "username";
//    private final static String data = "data";
//    private final static String title = "title";
//    private final static String subject = "subject";
//    private final static String section = "section";
//    private final static String code = "code";
//    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editclass);
        Intent i = getIntent();
        String t = i.getStringExtra("name");
        String sub = i.getStringExtra("sub");
        String sec = i.getStringExtra("sec");
        String code = i.getStringExtra("code");
        classcode = findViewById(R.id.classcode);
        TextInputLayout textInputLayout1 = findViewById(R.id.textInputLayout);
        TextInputLayout textInputLayout2 = findViewById(R.id.textInputLayout2);
        TextInputLayout textInputLayout3 = findViewById(R.id.textInputLayout3);
        textInputLayout1.getEditText().setText(t);
        textInputLayout2.getEditText().setText(sub);
        textInputLayout3.getEditText().setText(sec);
        classcode.setText(code);
    }


    public void backbtn(View view) {
        onBackPressed();
        TextInputLayout textInputLayout1 = findViewById(R.id.textInputLayout);
        TextInputLayout textInputLayout2 = findViewById(R.id.textInputLayout2);
        TextInputLayout textInputLayout3 = findViewById(R.id.textInputLayout3);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textInputLayout1.getEditText().getText().clear();
                textInputLayout2.getEditText().getText().clear();
                textInputLayout3.getEditText().getText().clear();
            }
        }, 500);
        finish();
    }

    public void save(View view) {
        SharedPreferences sharedata = getSharedPreferences(filename, MODE_PRIVATE);
        String username = sharedata.getString(user, "");
        Intent i = getIntent();

        String classcode = i.getStringExtra("code");
        textInputLayout1 = findViewById(R.id.textInputLayout);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        textInputLayout3 = findViewById(R.id.textInputLayout3);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(username).child("class").child(classcode);
        reference.child("classname").setValue(textInputLayout1.getEditText().getText().toString());
        reference.child("subject").setValue(textInputLayout2.getEditText().getText().toString());
        reference.child("section").setValue(textInputLayout3.getEditText().getText().toString());

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //ap helps us to iterate the objects..
                    Map<String, Object> code = (Map<String, Object>) dataSnapshot.getValue();
                    Log.d("datacode", "" + code);
                    assert code != null;
                    if (code.get("joinclass") == null) {
                        continue;
                    }
                    Map<String, Object> code1 = (Map<String, Object>) code.get("joinclass");

                    code1.forEach(new BiConsumer<Object, Object>() {
                        @Override
                        public void accept(Object o, Object o2) {

                            Log.d("classcode123", o + "");
                            if(classcode.equals(o)){

                                Log.d("oname",code.get("username")+"");
                                // text input data
                                String classname = textInputLayout1.getEditText().getText().toString().substring(0, 1).toUpperCase() + textInputLayout1.getEditText().getText().toString().substring(1);
                                String subject = textInputLayout2.getEditText().getText().toString().substring(0, 1).toUpperCase() + textInputLayout2.getEditText().getText().toString().substring(1);
                                String section = textInputLayout3.getEditText().getText().toString().toUpperCase();

                                // updating the class details in all users like updating classname,subject,section etc...
                                // the details will update in all users who are joined in that class
                                // for example if rajesh is joined in a class with code of 123abc. If the creator of the class is updated details of the
                                //123abc class then rajesh will also updated automatically.
                                reference1.child((String) Objects.requireNonNull(code.get("username"))).child("joinclass").child(classcode).child("classname").setValue(classname);
                                reference1.child((String) Objects.requireNonNull(code.get("username"))).child("joinclass").child(classcode).child("subject").setValue(subject);
                                reference1.child((String) Objects.requireNonNull(code.get("username"))).child("joinclass").child(classcode).child("section").setValue(section);
                            }
                        }


                        @Override
                        public BiConsumer<Object, Object> andThen(BiConsumer<? super Object, ? super Object> after) {
                            return null;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditClass.this, error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}