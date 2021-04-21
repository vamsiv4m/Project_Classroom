package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectclassroom.R;

import java.util.ArrayList;
import java.util.List;

import model.Sharefilemodel;

public class SharefilesAdapter extends RecyclerView.Adapter<SharefilesAdapter.Myholder> {

    Context context;
    List<Sharefilemodel> list;

    public SharefilesAdapter(Context context, List<Sharefilemodel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Myholder myholder= new Myholder(LayoutInflater.from(context).inflate(R.layout.sharefileslayout,parent,false));
        myholder.setIsRecyclable(false);
        return myholder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {

        holder.sharemsg.setText(list.get(position).getMsg());
        holder.date.setText(list.get(position).getDate());
        holder.name.setText("By "+list.get(position).getUsername());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Myholder extends RecyclerView.ViewHolder{
        TextView sharemsg,date,name;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            sharemsg=itemView.findViewById(R.id.sharemsg);
            date=itemView.findViewById(R.id.date);
            name=itemView.findViewById(R.id.sharename);
        }
    }
}
