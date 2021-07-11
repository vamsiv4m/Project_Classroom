package adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectclassroom.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.TreeSet;
import java.util.function.BiConsumer;

import attendence.Sheet;
import model.AttendanceMonthSheetModel;


public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.MyHolder> {
    Context context;
    List<AttendanceMonthSheetModel> list;
    List<String> list2=new ArrayList<String>();
    Intent i;

    public ListViewAdapter(Context context, List<AttendanceMonthSheetModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.attendance_monthly_sheet_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Log.d("textview123", list.get(position).getMonthyear() + "");
        String classcode = list.get(position).getClasscode();
        if (list.get(position).getMonthyear() != null) {
            holder.textView.setText(list.get(position).getMonthyear());
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(context, Sheet.class);
                i.putExtra("classcode", classcode + "");
                i.putExtra("monthyear", list.get(position).getMonthyear());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.listtext);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}