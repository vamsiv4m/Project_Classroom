 package adapter;

import android.annotation.SuppressLint;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectclassroom.ClassDetails;
import com.example.projectclassroom.MainActivity;
import com.example.projectclassroom.R;

import java.util.List;
import model.FetchData;

public class ClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<FetchData> list;
    private static final String codename="classcode";
    private static final String classcode="code";

    public ClassAdapter(Context context, List<FetchData> list) {
        this.context = context;
        this.list = list;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<FetchData> getList() {
        return list;
    }

    public void setList(List<FetchData> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==0){
            return new HolderView(LayoutInflater.from(parent.getContext()).inflate(R.layout.classeslayout,parent,false));

        }else{
            return new HolderView2(LayoutInflater.from(parent.getContext()).inflate(R.layout.joinclass_recycler_layout,parent,false));
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HolderView holder1 =(HolderView) holder;
        SharedPreferences sharedPreferences=getContext().getSharedPreferences(codename,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(classcode,list.get(position).getClass_code());
        editor.apply();
        Log.d("cc",""+list.get(position).getClass_code());
        holder1.subject.setText(list.get(position).getSubject());
        holder1.section.setText(list.get(position).getSection());
        holder1.professor.setText(list.get(position).getClassname());
        if(getItemViewType(position)==0) {

            // holder1.bgconstraint.setBackground(list.get(position).getImageurl());
            Glide.with(context).load(list.get(position).getImageurl()).into(holder1.background);
            FetchData fetchData=new FetchData();
            Log.d("link",list.get(position).getImageurl()+"");
            holder1.itemView.setOnClickListener(new View.OnClickListener()  {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ClassDetails.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("subject", "" + holder1.subject.getText());
                    i.putExtra("section",""+holder1.section.getText());
                    i.putExtra("imglink",""+list.get(position).getImageurl());
                    context.startActivity(i);
                    Toast.makeText(context, "" + holder1.subject.getText(), Toast.LENGTH_SHORT).show();
                }
            });
            holder1.imgmenubtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //creating a popup menu

                    PopupMenu popup = new PopupMenu(context, ((HolderView) holder).imgmenubtn);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.menu_recycler);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.unenroll:
                                    //handle menu1 click
                                    String cc=list.get(position).getClass_code();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setMessage("Classroom Code is : " + cc)
                                            .setCancelable(false)
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
                    //displaying the popup
                    popup.show();
                }
            });
        }
        else{
            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ClassDetails.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("subject", "" + holder1.subject.getText());
                    i.putExtra("section",""+ holder1.section.getText());
                    i.putExtra("imglink",""+list.get(position).getImageurl());
                    context.startActivity(i);
                    Toast.makeText(context, "" + holder1.subject.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HolderView extends RecyclerView.ViewHolder{
        TextView subject,professor,section;
        ConstraintLayout layout;
        Button imgmenubtn;
        ImageView background;
        ImageButton imageButton;
        public HolderView(@NonNull View itemView){
            super(itemView);
            subject=itemView.findViewById(R.id.subjectText);
            section=itemView.findViewById(R.id.sectionText);
            professor=itemView.findViewById(R.id.professorText);
            layout=itemView.findViewById(R.id.con);


            background=itemView.findViewById(R.id.bgimg);
            imgmenubtn=itemView.findViewById(R.id.imageButtonmenu);
        }
    }
    static class HolderView2 extends RecyclerView.ViewHolder{
        TextView subject,professor,section;
        ConstraintLayout layout;
        public HolderView2(@NonNull View itemView) {
            super(itemView);
            subject=itemView.findViewById(R.id.subtext);
            section=itemView.findViewById(R.id.profText);
            professor=itemView.findViewById(R.id.secText);
            layout=itemView.findViewById(R.id.constraint);
        }
    }
}