package fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.projectclassroom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import adapter.ChatAdapter;
import kotlin.random.URandomKt;
import model.ChatModel;

public class ChatFragment extends Fragment {
    private static final String filename = "login";
    private static final String user = "username";
    private static final String classcode = "code";
    private static final String profname = "prof";
    RecyclerView recyclerView;
    ArrayList<ChatModel> list = new ArrayList<>();

    public ChatFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(filename, 0);
        String users = sharedPreferences.getString(user, "");
        String prof = sharedPreferences.getString(profname, "");
        Log.d("users1234", users + "");
        ShapeableImageView send = v.findViewById(R.id.sendmsg);
        EditText textmsg = v.findViewById(R.id.sendmessage);

        String cc = sharedPreferences.getString(classcode, "");
        send.setOnClickListener(v1 -> {
            String randomString=FirebaseDatabase.getInstance().getReference().push().getKey();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); //format in given timezone
            String msgtext = textmsg.getEditableText().toString();
            if (!msgtext.isEmpty()) {
                Log.d("timestamp123", "file" + "");
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                reference.child("group").child(cc).child("chat").child(randomString).child("msg").setValue(msgtext);
                reference.child("group").child(cc).child("chat").child(randomString).child("username").setValue(users);
                reference.child("group").child(cc).child("chat").child(randomString).child("time").setValue(ServerValue.TIMESTAMP);
                textmsg.setText("");
            }
        });

//      Recycler view
        recyclerView = v.findViewById(R.id.chatrecycler);
        ChatAdapter chatAdapter = new ChatAdapter(list, getContext());
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(chatAdapter);
        TextView welmsg=v.findViewById(R.id.welcomemsg);
        TextView welmsg2=v.findViewById(R.id.welcomemsg2);
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot1:snapshot.child("group").child(cc).child("chat").getChildren()){
                    if(dataSnapshot1.exists()){
                        welmsg.animate().alpha(0).start();
                        Log.d("data1234",dataSnapshot1+"");
                        ChatModel chatModel=dataSnapshot1.getValue(ChatModel.class);
                        list.add(chatModel);
                        welmsg2.setText(prof+"'s Chatroom");
                        Log.d("list1234",list.get(0).getUsername()+"");
                        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                        chatAdapter.notifyDataSetChanged();
                    }
                    else {
                        welmsg.animate().translationY(-90).setDuration(800).start();
                        welmsg.animate().alpha(1).setDuration(800).start();
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return v;
    }
}