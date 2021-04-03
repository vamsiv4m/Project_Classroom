package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectclassroom.ClassDetails;
import com.example.projectclassroom.R;
import com.google.android.material.transition.Hold;

import java.util.List;
import java.util.TreeMap;

import editclass.EditClass;
import fragments.EnrollFragment;
import model.JoinData;

public class TeachAdapter extends RecyclerView.Adapter<TeachAdapter.Holderview> {
    Context context;
    List<JoinData> list;
    public TeachAdapter(Context context, List<JoinData> list) {
        this.context = context;
        this.list = list;
    }
    private final static String data="data";
    private final static String title="title";
    private final static String subject="subject";
    private final static String section="section";
    private final static String code="code";


    @NonNull
    @Override
    public Holderview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Holderview holderview=new Holderview(LayoutInflater.from(parent.getContext()).inflate(R.layout.teachlayout,parent,false));
        return holderview;
    }

    @Override
    public void onBindViewHolder(@NonNull Holderview holder, int position) {
        holder.subject.setText(list.get(position).getSubject());
        holder.section.setText(list.get(position).getSection());
        holder.professor.setText(list.get(position).getClassname());
        Glide.with(context).load(list.get(position).getImageurl()).into(holder.background);
        SharedPreferences sharedPreferences=context.getSharedPreferences(data,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor=sharedPreferences.edit();
        String t=holder.professor.getText().toString();
        String sec=holder.section.getText().toString();
        String sub=holder.subject.getText().toString();
        String code=list.get(position).getClass_code();
        editor.putString(title,t);
        editor.putString(section,sec);
        editor.putString(subject,sub);
        editor.putString(code,code);
        editor.apply();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ClassDetails.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("classname",""+holder.professor.getText());
                i.putExtra("subject", "" + holder.subject.getText());
                i.putExtra("section",""+holder.section.getText());
                i.putExtra("imglink",""+list.get(position).getImageurl());
                i.putExtra("code",""+list.get(position).getClass_code());
                v.getContext().startActivity(i);
                Toast.makeText(v.getContext(), "" + holder.subject.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.threedots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(context ,((Holderview)holder).threedots);
                popupMenu.inflate(R.menu.menu_recycler );
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.unenroll:
                                String cc=list.get(position).getClass_code();
                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                builder.setMessage("Classroom Code is : " + cc)
                                        .setCancelable(true)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        }).show();
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        Log.d("s",list.get(position).getSubject()+"");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holderview extends RecyclerView.ViewHolder {
        TextView subject,section,professor;
        ImageView background;
        Button threedots;
        public Holderview(@NonNull View itemView) {
            super(itemView);
            subject=itemView.findViewById(R.id.subText);
            section=itemView.findViewById(R.id.sec_text);
            professor=itemView.findViewById(R.id.prof_text);
            background=itemView.findViewById(R.id.backgroundimg);
            threedots=itemView.findViewById(R.id.imgthreedot);
        }
    }
}
