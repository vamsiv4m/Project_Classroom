package attendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectclassroom.CreateClass;
import com.example.projectclassroom.JoinClass;
import com.example.projectclassroom.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import adapter.AttendanceAdapter;
import database.SqliteHelperClass;
import fragments.MyCalender;
import model.Attendance_Model2;
import model.Attendance_model;

public class AttendenceInterface extends AppCompatActivity {
    Toolbar toolbar;
    Button save, viewdata;
    TextView sectionheading;
    UserDataHandler handler;
    private static final String filename = "login";
    private static final String user = "username";
    String username, classcode, sec;
    SharedPreferences sharedPreferences;
    Intent i;
    int count = 1;
    private MyCalender myCalender = new MyCalender();

    RecyclerView recyclerView;
    SqliteHelperClass sqliteHelperClass;
    List<Attendance_model> list = new ArrayList<>();
    List<Attendance_Model2> list1 = new ArrayList<>();
    Attendance_model attendance_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_interface);
        save = findViewById(R.id.save);
        viewdata = findViewById(R.id.viewdata);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setPadding(5, 5, 5, 5);
        toolbar.setTitle("Attendance");
        toolbar.setSubtitle(myCalender.getDate());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sectionheading = findViewById(R.id.sectionheading);
        //Database
        sqliteHelperClass = new SqliteHelperClass(this);
        loadData();
        //getting username from sharedpreferences.
        sharedPreferences = getSharedPreferences(filename, 0);
        username = sharedPreferences.getString(user, "");

        //getting classcode from intent.
        i = getIntent();
        classcode = i.getStringExtra("code");
        sec = i.getStringExtra("sec");

        sectionheading.setText(sec + " Attendance");
        Cursor cursor = sqliteHelperClass.getClassTable();

        //recyclerview
        recyclerView = findViewById(R.id.recyclerAttendance);
        recyclerView.setHasFixedSize(true);
        AttendanceAdapter attendanceAdapter = new AttendanceAdapter(getApplicationContext(), list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setAdapter(attendanceAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {

                    Map<Object, Object> code = (Map<Object, Object>) dataSnapshot1.getValue();
                    String TAG = "tag234";

                    if (code.get("joinclass") == null) {
                        continue;
                    }
                    Map<String, Object> code1 = (Map<String, Object>) code.get("joinclass");
                    Map<String, Object> code2 = (Map<String, Object>) code1.get(classcode);
                    Log.d("joinclass123", code2 + "");
                    if (code2 == null) {
                        continue;
                    }
                    if (classcode.equals(code2.get("class_code"))) {
                        if (count >= 1 && count < 10) {
                            attendance_model = new Attendance_model(String.valueOf("0" + count++), (String) code.get("username"));

                        } else {
                            attendance_model = new Attendance_model(String.valueOf(count++), (String) code.get("username"));
                        }
                        list.add(attendance_model);
                    }

                }
                attendanceAdapter.notifyDataSetChanged();
                Log.d("snapshot123", snapshot + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder stringBuilder = new StringBuilder();
                while (cursor.moveToNext()) {
                    stringBuilder.append("Sno : " + cursor.getColumnName(0) + "\nSname : " + cursor.getColumnName(1) + "\nStatus : " + cursor.getColumnName(2));
                }
                Toast.makeText(AttendenceInterface.this, stringBuilder + "", Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "null";
                String status = "null";
                String sno = "null";
                for (Attendance_model attendance_model : list) {
                    sno = attendance_model.getSno();
                    name = attendance_model.getStudent_name();
                    status = attendance_model.getStatus();
                    if (!status.equals("P")) {
                        status = "A";
                    }
                    try {
                        sqliteHelperClass.insertdata(sno, name, status);
                    } catch (Exception e) {
                        Log.d("Exception raised", "Exception : " + e.getMessage());
                    }

                    Log.d("status123", status + "");
                    Log.d("status123", sno + "");
                    Log.d("status123", name + "");
                }


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.changedate) {
            showCalender();

        }
        return true;
    }

    private void showCalender() {
        myCalender.show(getSupportFragmentManager(), "");
        myCalender.setOnCalenderOkClickListener(this::onCalenderClicked);
    }

    private void onCalenderClicked(int year, int month, int day) {
        myCalender.setDate(year, month, day);
        toolbar.setSubtitle(myCalender.getDate());
    }

    public void loadData() {
        Cursor cursor = sqliteHelperClass.getClassTable();
        list1.clear();
        while (cursor.moveToNext()) {
            String studentname = cursor.getString(cursor.getColumnIndex(SqliteHelperClass.studentnamekey));
            String status = cursor.getString(cursor.getColumnIndex(SqliteHelperClass.statuskey));
            list1.add(new Attendance_Model2(studentname, status));
            Log.d("sqldata123", studentname + "  :  " + status);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.attendance_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

}