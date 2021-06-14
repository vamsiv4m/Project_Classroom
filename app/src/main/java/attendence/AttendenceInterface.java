package attendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.*;

import android.annotation.SuppressLint;
import android.content.*;
import android.icu.number.LocalizedNumberFormatter;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.example.projectclassroom.R;
import com.google.firebase.database.*;

import java.util.*;

import adapter.AttendanceAdapter;
import database.SqliteHelperClass;
import fragments.MyCalender;
import model.Attendance_Model2;
import model.Attendance_model;

public class AttendenceInterface extends AppCompatActivity {
    Toolbar toolbar;
    Button save, viewdata;
    TextView sectionheading;
    private static final String filename = "login";
    private static final String user = "username";
    String username, classcode, sec;
    SharedPreferences sharedPreferences;
    Intent i;
    DatabaseReference reference;
    int count = 1;
    AttendanceAdapter attendanceAdapter;
    private final MyCalender myCalender = new MyCalender();
    RecyclerView recyclerView;
    SqliteHelperClass sqliteHelperClass;
    List<Attendance_model> list = new ArrayList<>();
    Attendance_model attendance_model = null;

    @SuppressLint("SetTextI18n")
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sectionheading = findViewById(R.id.sectionheading);
        //Database
        sqliteHelperClass = new SqliteHelperClass(this);

        //getting username from sharedpreferences.
        sharedPreferences = getSharedPreferences(filename, 0);
        username = sharedPreferences.getString(user, "");

        //getting classcode from intent.
        i = getIntent();
        classcode = i.getStringExtra("code");
        sec = i.getStringExtra("sec");
        sectionheading.setText(sec + " Attendance");
        attendanceAdapter = new AttendanceAdapter(this, list);
        //recyclerview
        recyclerView = findViewById(R.id.recyclerAttendance);
        recyclerView.setHasFixedSize(true);
        loadData();
//        count();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setAdapter(attendanceAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                    Map<Object, Object> code = (Map<Object, Object>) dataSnapshot1.getValue();
                    assert code != null;
                    if (code.get("joinclass") == null) continue;
                    Map<String, Object> code1 = (Map<String, Object>) code.get("joinclass");
                    assert code1 != null;
                    Map<String, Object> code2 = (Map<String, Object>) code1.get(classcode);
                    Log.d("joinclass123", code2 + "");
                    if (code2 == null) continue;

                    loadData();

                    if (classcode.equals(code2.get("class_code"))) {
                        if (count >= 1 && count < 10) {
                            attendance_model = new Attendance_model("0" + count++, (String) code.get("username"));
                        } else {
                            attendance_model = new Attendance_model(String.valueOf(count++), (String) code.get("username"));
                        }
                        list.add(attendance_model);
                    }
                }
                attendanceAdapter.notifyDataSetChanged();
                Log.d("snapshot123", snapshot + "");
                save.setOnClickListener(v -> savedata());
                viewdata.setOnClickListener(v -> showCalender());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Error in database firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savedata() {
        Intent i = getIntent();
        Attendance_Model2 attendance_model2;
        String sno, name, classcode, section, subjectname, status;
        for (Attendance_model attendance_model : list) {
            sno = attendance_model.getSno();
            name = attendance_model.getStudent_name();
            classcode = i.getStringExtra("code");
            section = i.getStringExtra("sec");
            subjectname = i.getStringExtra("sub");
            status = attendance_model.getStatus();
            attendance_model2 = new Attendance_Model2(sno, name, status, section, subjectname);
            if (!status.equals("P")) status = "A";
            reference = FirebaseDatabase.getInstance().getReference("Attendance");
            reference.child(classcode).child(myCalender.getDate()).child(name).setValue(attendance_model2);
            Log.d("statusdata123", name + " " + status + " " + myCalender.getDate());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        else if (item.getItemId()==R.id.changedate){
//            count();
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
        loadData();
    }

    public void loadData() {
        classcode = i.getStringExtra("code");

            reference = FirebaseDatabase.getInstance().getReference("Attendance");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int count=0;
                    String fstatus = null;
                    for (Attendance_model attendance_model : list) {
                        Log.d("datasnapshot123456", snapshot.child(classcode) + "");
                         fstatus = (String) snapshot.child(classcode).child(myCalender.getDate()).child(attendance_model.getStudent_name()).child("status").getValue();
                        count= (int) snapshot.child(classcode).child(myCalender.getDate()).getChildrenCount();
                        if (attendance_model.getStatus()=="P"){
                            count++;
                        }

                        Log.d("fstatus", fstatus + " : " + attendance_model.getStudent_name());
                        if (fstatus == null) attendance_model.setStatus("");
                        else attendance_model.setStatus(fstatus);
                    }
                    Log.d("present1234",count+"");
                    Log.d("totalstudents",count+"");
                    attendanceAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AttendenceInterface.this, "Database error", Toast.LENGTH_SHORT).show();
                }
            });
        }

//        private void count(){
//            int present_count=0;
//            int absent_count=0;
//            for (Attendance_model attendance_model:list){
//                if (attendance_model.getStatus()=="P"){
//                    present_count++;
//                }
//                else {
//                    absent_count++;
//                }
//            }
//            Log.d("presentcount123",present_count+"");
//            Log.d("presentcount123",absent_count+"");
//        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.attendance_items, menu);
        return super.onCreateOptionsMenu(menu);
    }
}