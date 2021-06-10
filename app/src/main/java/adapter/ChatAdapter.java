package adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectclassroom.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import model.ChatModel;

public class ChatAdapter extends RecyclerView.Adapter {
    private static final String filename = "login";
    private static final String user = "username";

    ArrayList<ChatModel> list;
    Context context;
    int sender_viewtype = 1;
    int reciever_viewtype = 2;

    public ChatAdapter(ArrayList<ChatModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("viewtype123", viewType + "");

        if (viewType == sender_viewtype) {
            SenderHolder senderHolder = new SenderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.senderlayout, parent, false));
            return senderHolder;
        } else {
            RecieverHolder recieverHolder = new RecieverHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recieverlayout, parent, false));
            return recieverHolder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(filename, 0);
        String u1 = sharedPreferences.getString(user, "");
        Log.d("users1234", u1 + "");
        if (u1.equals(list.get(position).getUsername())) {
            return sender_viewtype;
        } else {
            return reciever_viewtype;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getClass() == SenderHolder.class) {
            ((SenderHolder) holder).sendermsg.setText(list.get(position).getMsg());
            ((SenderHolder) holder).sendertime.setText(list.get(position).getTime());
        } else {
            ((RecieverHolder) holder).recievemsg.setText(list.get(position).getMsg());
            ((RecieverHolder) holder).recievetime.setText(list.get(position).getTime());
            ((RecieverHolder) holder).username.setText(list.get(position).getUsername());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class SenderHolder extends RecyclerView.ViewHolder {
        TextView sendermsg, sendertime;

        public SenderHolder(@NonNull View itemView) {
            super(itemView);
            sendermsg = itemView.findViewById(R.id.sendermsg);
            sendertime = itemView.findViewById(R.id.sendertime);
        }
    }

    public class RecieverHolder extends RecyclerView.ViewHolder {
        TextView recievemsg, recievetime, username;

        public RecieverHolder(@NonNull View itemView) {
            super(itemView);
            recievemsg = itemView.findViewById(R.id.recievermsg);
            recievetime = itemView.findViewById(R.id.recievetime);
            username = itemView.findViewById(R.id.uname);
        }
    }
}
