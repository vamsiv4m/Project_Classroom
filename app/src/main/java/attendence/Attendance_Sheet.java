package attendence;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.projectclassroom.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import adapter.ListViewAdapter;
import model.AttendanceMonthSheetModel;

public class Attendance_Sheet extends AppCompatActivity {
    RecyclerView recyclerView;
    List<AttendanceMonthSheetModel> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance__sheet);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Attendance");
        recyclerView = findViewById(R.id.listview);
        Intent i = getIntent();
        String classcode = i.getStringExtra("classcode");
        String lastdate = i.getStringExtra("lastdate");
        ListViewAdapter adapter = new ListViewAdapter(this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Attendance");
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.child(classcode).getChildren()) {
                    Log.d("datachild",dataSnapshot.getKey()+"");
                    String s=dataSnapshot.getKey();
                    AttendanceMonthSheetModel attendanceMonthSheetModel=new AttendanceMonthSheetModel(s,classcode,lastdate);
                    list.add(attendanceMonthSheetModel);
                    Log.d("orderdata12334455", dataSnapshot + "");
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}