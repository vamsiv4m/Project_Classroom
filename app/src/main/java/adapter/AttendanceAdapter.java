package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectclassroom.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import model.Attendance_model;


public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyHolder> {
    Context context;
    public List<Attendance_model> list;

    public AttendanceAdapter(Context context, List<Attendance_model> list) {
        this.context = context;
        this.list = list;
    }

    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.attendancedesign, parent, false));
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.student_name.setText(list.get(position).getStudent_name());
        holder.sno.setText(list.get(position).getSno());
        holder.statusid.setText(list.get(position).getStatus());
        if(list.get(position).getStatus().equals("P")) holder.cardView.setBackgroundColor(Color.parseColor("#4037FF00"));

        else if(list.get(position).getStatus().equals("A")) holder.cardView.setBackgroundColor(Color.parseColor("#40FF0000"));

        else holder.cardView.setBackgroundColor(Color.parseColor("#ffffff"));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changestatus(position);
                holder.statusid.setText(list.get(position).getStatus());
            }

            private void changestatus(int position) {

                if ((list.get(position).getStatus()).equals("P")) {
                    //If the status is in p when we click then it changes to A.
                    list.get(position).setStatus("A");
                    holder.cardView.setBackgroundColor(Color.parseColor("#40FF0000"));
                    Log.d("statusabs", list.get(position).getStatus() + "");
                }

                else {
                    list.get(position).setStatus("P");
                    holder.cardView.setBackgroundColor(Color.parseColor("#4037FF00"));
                    Log.d("statuspre", list.get(position).getStatus() + "");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        ConstraintLayout cardView;
        TextView student_name, statusid,sno;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardbackground);
            student_name = itemView.findViewById(R.id.student_name);
            statusid = itemView.findViewById(R.id.statusid);
            sno = itemView.findViewById(R.id.sno);
        }
    }
}