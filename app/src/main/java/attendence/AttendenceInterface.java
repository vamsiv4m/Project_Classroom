package attendence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.*;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.*;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.example.projectclassroom.R;
import com.google.firebase.database.*;

import org.w3c.dom.Text;

import java.util.*;

import adapter.AttendanceAdapter;
import database.SqliteHelperClass;
import fragments.EnrollFragment;
import fragments.MyCalender;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import model.Attendance_Model2;
import model.Attendance_model;

public class AttendenceInterface extends AppCompatActivity {
    Toolbar toolbar;
    public Button save, viewdata, okdismis;
    public TextView sectionheading, totalstudents, totalpresent, totalabsent;
    public ImageView close;
    AlertDialog.Builder builder;
    private static final String filename = "login";
    private static final String user = "username";
    String username, classcode, sec;
    SharedPreferences sharedPreferences;
    Intent i;
    DatabaseReference reference;
    int count = 1;
    int stucount = 0;
    int presentcount = 0;
    int absentcount = 0;
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
        close = findViewById(R.id.closeimg);
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        recyclerView.setAdapter(attendanceAdapter);
        loadData();
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
                ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
            }
            final ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position=viewHolder.getAdapterPosition();
                    if(direction==ItemTouchHelper.LEFT){
                        Intent i=new Intent(getApplicationContext(),Attendance_Sheet.class);
                        i.putExtra("classcode",classcode);
                        startActivity(i);
                        attendanceAdapter.notifyItemChanged(position);
                    }
                }
                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeLeftLabel("More Details")
                            .create()
                            .decorate();
                }
            };

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
            attendance_model2 = new Attendance_Model2(sno, name, status, section, subjectname,myCalender.getDate(),myCalender.getMonth(), myCalender.getLastDate());
            if (!status.equals("P")) status = "A";
            reference = FirebaseDatabase.getInstance().getReference("Attendance");
            reference.child(classcode).child(myCalender.getMonth()).child(name).child(myCalender.getDate()).setValue(attendance_model2);
            Log.d("statusdata123", name + " " + status + " " + myCalender.getDate());
        }
    }
    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        totalpresent = findViewById(R.id.totalpresent);
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.viewdetails) {
            loadData();
            builder = new AlertDialog.Builder(this);
            final View customView = getLayoutInflater().inflate(R.layout.viewmoredetails, null);
            builder.setView(customView);
            totalpresent = customView.findViewById(R.id.totalpresent);
            totalstudents = customView.findViewById(R.id.totalstudents);
            totalabsent = customView.findViewById(R.id.totalabsent);
            totalstudents.setText(stucount + "");
            totalpresent.setText(presentcount + "");
            totalabsent.setText(absentcount + "");
            Log.d("studentstotal", stucount + "");
            Log.d("presenttotal", presentcount + "");
            Log.d("absenttotal", absentcount + "");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog1 = builder.create();
            dialog1.show();
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
                stucount = 0;
                presentcount = 0;
                absentcount = 0;
                String fstatus;
                for (Attendance_model attendance_model : list) {

                    fstatus = (String) snapshot.child(classcode).child(myCalender.getMonth()).child(attendance_model.getStudent_name()).child(myCalender.getDate()).child("status").getValue();
                    Log.d("datasnapshot123456", fstatus + "");
                    stucount = (int) snapshot.child(classcode).child(myCalender.getMonth()).getChildrenCount();
                    if (fstatus == null) { }
                    else if (fstatus.equals("P")) {
                        presentcount += 1;
                        Log.d("presenttotal1", presentcount + "");
                    } else if (fstatus.equals("A")) {
                        absentcount += 1;
                        Log.d("absenttotal1", absentcount + "");
                    } else {
                        Log.d("notgetting", "not getting rey...");
                    }
                    Log.d("fstatus", fstatus + " : " + attendance_model.getSno());
                    if (fstatus == null) attendance_model.setStatus("");
                    else attendance_model.setStatus(fstatus);
                }
                Log.d("present1234", count + "");
                Log.d("totalstudents", count + "");
                attendanceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AttendenceInterface.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.attendance_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

}