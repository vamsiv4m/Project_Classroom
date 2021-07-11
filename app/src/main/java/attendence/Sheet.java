package attendence;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.projectclassroom.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import model.AttendanceMonthSheetModel2;
import model.SheetModel;

public class Sheet extends AppCompatActivity {
    TextView textView;
    TableRow tableRow;
    String sno1 = "";
    HashMap<String, Object> map;
    List<SheetModel> list = new ArrayList<>();
    List<AttendanceMonthSheetModel2> list2 = new ArrayList<AttendanceMonthSheetModel2>();
    TreeMap<String, String> treeSet = new TreeMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);
        map();

    }

    @SuppressLint("SetTextI18n")
    public void sheetdata(TreeMap<String, String> treeSet) {
        TableLayout prices = (TableLayout) findViewById(R.id.tablelayout);
        prices.setStretchAllColumns(true);
        prices.bringToFront();
        Log.d("checklist123", treeSet + "");
        TableRow tr = new TableRow(this);
        TextView c1 = new TextView(this);
        c1.setText("sno");
        for (int i = 0; i < treeSet.size(); i++) {
            c1.setText((CharSequence) treeSet.get("sno"));
            tr.addView(c1);
            prices.addView(tr);
        }
    }

    public void map() {
        Intent i = getIntent();
        String s = i.getStringExtra("monthyear");
        ArrayList<String> values=new ArrayList<String>();
        HashSet<String> hashSet=new HashSet<String>();
        String classcode = i.getStringExtra("classcode");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Attendance");
        reference.child(classcode).child(s).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("referncedata", snapshot.getValue() + "");
                HashMap<String, Object> map = (HashMap<String, Object>) snapshot.getValue();
                Log.d("referncedata", map + "");
                assert map != null;
                map.forEach(new BiConsumer<String, Object>() {
                    @Override
                    public void accept(String s1, Object o) {
//                        Query reference= (Query) FirebaseDatabase.getInstance().getReference("Attendance").child(classcode).child(s).child(s1);
//                        reference.addChildEventListener(new ChildEventListener() {
//                            @Override
//                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                                String[] tokens = snapshot.getRef().toString().split("/");
//                                String parentKey = tokens[tokens.length - 1];
//
//                            }
//
//                            @Override
//                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                            }
//
//                            @Override
//                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                            }
//
//                            @Override
//                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                        reference.orderByValue().addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                    String r=snapshot.toString();
//                                    Log.d("refer123",r+"");
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
                        reference.child(classcode).child(s).child(s1).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String date1, monthyear1, status1;

                                Log.d("mysnapshot2",snapshot.getChildrenCount()+"");
                                for (int i = 0; i <= snapshot.getChildrenCount(); i++) {
                                    if (i>=1 && i<10){
                                        Log.d("mysnapshot",snapshot.child("0"+i).getValue()+"");
                                    }
                                }
//                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
//                                    HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot1.getValue();
//                                    Log.d("thisismap2", map.get("date") + "");
//                                    date1 = map.get("date").toString();
//                                    monthyear1 = map.get("monthyear").toString();
//                                    String section1 = map.get("section").toString();
//                                    String sno1 = map.get("sno").toString() + "";
//                                    status1 = map.get("status").toString();
//                                    String studentname1 = map.get("studentname").toString();
//                                    String subjectname1 = map.get("subjectname").toString();
//
//                                    values.add(studentname1);
//
//                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Log.d("zandubalm",values+"");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}

