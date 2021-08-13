package attendence;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectclassroom.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public class Sheet extends AppCompatActivity {
    List<String> list = new ArrayList<>();
    List<Long> list2 = new ArrayList<>();
    List<String > list3 = new ArrayList<>();
    List<String > list4 = new ArrayList<>();
    LinkedHashSet<Long> linkedHashSet1;
    LinkedHashSet<String> linkedHashSet2;
    LinkedHashSet<String> linkedHashSet3;
    ScrollView scrollView;
    ArrayList<Long> arrayList1;
    ArrayList<String> arrayList2;
    ArrayList<String> arrayList3;
    String text="";
    String date="";
    String status="";
    String text1="";
    int c=0;
    TextView attendancename,totalworkingdays,totalpresentdays,totalview,percentage,subjectname;
    SharedPreferences sharedPreferences;
    private static final String filename = "login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);
        map();
    }

    public void map() {
        scrollView=findViewById(R.id.scrollview);
        scrollView.setVerticalScrollBarEnabled(false);
        TextView dateloop=findViewById(R.id.datesloop);
        TextView presentloop=findViewById(R.id.presentloop);
        sharedPreferences=getSharedPreferences(filename,0);
        String name=sharedPreferences.getString("name","");
        final Intent[] i = {getIntent()};
        String s = i[0].getStringExtra("monthyear");
        String classcode = i[0].getStringExtra("classcode");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Attendance");
        reference.child(classcode).child(s).addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("referncedata", snapshot.getValue() + "");
                reference.child(classcode).child(s).child(name).addValueEventListener(new ValueEventListener() {
                    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("mysnapshot2",snapshot.getKey()+"");
                        Log.d("mysnapshot3",name+"");
                        Log.d("mysnapshot",snapshot.getChildrenCount() +"");
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Log.d("mysnapshot4", dataSnapshot.child("lastdate").getValue() + "");
                            Log.d("mysnapshot4", dataSnapshot.child("status").getValue() + "");
                            if(dataSnapshot.child("status").getValue().toString().equals("P")){
                                c+=1;
                                date=(String) dataSnapshot.child("date").getValue();
                                status= (String) dataSnapshot.child("status").getValue();
                                Log.d("counter123",c+"");
                            }
                            list.add((dataSnapshot.child("subjectname").getValue().toString()));
                            list2.add((Long) dataSnapshot.child("lastdate").getValue());
                            list3.add((String)dataSnapshot.child("date").getValue());
                            list4.add((String)dataSnapshot.child("status").getValue());
                        }
                        LinkedHashSet<String> linkedHashSet=new LinkedHashSet<>(list);
                        linkedHashSet1=new LinkedHashSet<>(list2);
                        linkedHashSet2=new LinkedHashSet<>(list3);
                        ArrayList<String> arrayList=new ArrayList<>(linkedHashSet);
                        arrayList1=new ArrayList<>(linkedHashSet1);
                        arrayList2=new ArrayList<>(linkedHashSet2);
                        Log.d("mylist",list4+"");

                        for (int i=0;i<snapshot.getChildrenCount();i++){
                            text = text+""+arrayList2.get(i)+"\n";
                            if(list4.get(i).equals("P")){
                                text1 = text1+""+list4.get(i)+"resent\n";
                            }
                            else if(list4.get(i).equals("A")){
                                text1=text1+""+list4.get(i)+"bsent\n";
                            }
                        }
                        dateloop.setText(text);
                        presentloop.setText(text1);
                        Log.d("presentloop",text+"");
                        attendancename=findViewById(R.id.attendancename);
                        totalworkingdays=findViewById(R.id.totalworkdays);
                        totalpresentdays=findViewById(R.id.totalpresented);
                        subjectname=findViewById(R.id.subjectname);
                        percentage=findViewById(R.id.percentage);
                        totalview=findViewById(R.id.totalviewer);
                        attendancename.setText(name+"");
                        totalworkingdays.setText(snapshot.getChildrenCount()+"");
                        totalpresentdays.setText(c+"");
                        try {
                            subjectname.setText(arrayList.get(0) +"");
                        }
                        catch (Exception e){
                            Toast.makeText(Sheet.this, "This student is not joined in that month.", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder=new AlertDialog.Builder(Sheet.this);
                            builder.setTitle("StudyRoom Analytics").setMessage("Student is not joined in this month").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onBackPressed();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        totalview.setText(c+" / "+snapshot.getChildrenCount()+ " Days");
                        percentage.setText((float)c/(float)snapshot.getChildrenCount()*100 +"%");
                        Log.d("percentage",(((float)c/(float)snapshot.getChildrenCount())*100 +""));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}

