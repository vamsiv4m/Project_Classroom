package adapter;

import android.annotation.SuppressLint;
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
import java.util.ArrayList;
import model.ChatModel;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
            return new SenderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.senderlayout, parent, false));
        } else {
            return new RecieverHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recieverlayout, parent, false));
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String euser="";
        if (holder.getClass() == SenderHolder.class) {
            ((SenderHolder) holder).sendermsg.setText(list.get(position).getMsg());
        } else {
            ((RecieverHolder) holder).recievemsg.setText(list.get(position).getMsg());
            ((RecieverHolder) holder).username.setText(list.get(position).getUsername());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SenderHolder extends RecyclerView.ViewHolder {
        TextView sendermsg;

        public SenderHolder(@NonNull View itemView) {
            super(itemView);
            sendermsg = itemView.findViewById(R.id.sendermsg);
        }
    }

    public static class RecieverHolder extends RecyclerView.ViewHolder {
        TextView recievemsg, username;

        public RecieverHolder(@NonNull View itemView) {
            super(itemView);
            recievemsg = itemView.findViewById(R.id.recievermsg);
            username = itemView.findViewById(R.id.uname);
        }
    }
}
