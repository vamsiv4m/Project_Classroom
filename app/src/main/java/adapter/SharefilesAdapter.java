package adapter;

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
    List<Sharefilemodel> list=new ArrayList<>();
    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Myholder myholder= new Myholder(LayoutInflater.from(context).inflate(R.layout.sharefileslayout,parent,false));
        return myholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, int position) {
        holder.sharemsg.setText(list.get(position).getMsg());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class Myholder extends RecyclerView.ViewHolder{
        TextView sharemsg;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            sharemsg=itemView.findViewById(R.id.sharemsg);
        }
    }
}
