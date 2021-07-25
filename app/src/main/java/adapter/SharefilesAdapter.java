package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectclassroom.R;

import org.w3c.dom.Document;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Sharefilemodel;
import sharefiles.DocumentViewer;

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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMM dd, yyyy hh:mm a");
        Timestamp timestamp=new Timestamp(list.get(position).getDate());
        Date date=new Date(timestamp.getTime());
        holder.sharemsg.setText(list.get(position).getMsg());
        holder.date.setText(simpleDateFormat.format(date));

        holder.name.setText("By "+list.get(position).getUsername());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"IntentReset", "SetJavaScriptEnabled"})
            @Override
            public void onClick(View v) {
                Log.d("myurl123",list.get(position).getUrl()+"");
                    if (list.get(position).getExtension().equals("jpg") ||
                            list.get(position).getExtension().equals("png") ||
                            list.get(position).getExtension().equals("jpeg") || list.get(position).getExtension().equals("tif")){
                        Intent i=new Intent(context,DocumentViewer.class);
                        i.putExtra("myimageurl",list.get(position).getUrl());
                        context.startActivity(i);
                    }else {
                        Intent i = new Intent(context, DocumentViewer.class);
                        i.putExtra("myurl", list.get(position).getUrl());
                        context.startActivity(i);
                    }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class Myholder extends RecyclerView.ViewHolder{
        TextView sharemsg,date,name;
        CardView cardView;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            sharemsg=itemView.findViewById(R.id.sharemsg);
            cardView=itemView.findViewById(R.id.sharefileclick);
            date=itemView.findViewById(R.id.date);
            name=itemView.findViewById(R.id.sharename);
        }
    }
}
