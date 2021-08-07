package fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectclassroom.R;
import com.example.projectclassroom.Sharewithclass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.SharefilesAdapter;
import model.Sharefilemodel;

public class ShareFilesFragment extends Fragment {
    private static final String filename = "login";
    private static final String classcode = "code";
    private static final String subject = "sub";
    private static final String section = "sec";
    TextView sub,sec,sendfiles;
    RecyclerView recycler;
    ImageView fileimage;

    public ShareFilesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        List<Sharefilemodel> list=new ArrayList<>();
        View v= inflater.inflate(R.layout.fragment_share_files, container, false);
        sub=v.findViewById(R.id.sub);
        sec=v.findViewById(R.id.sec);
        fileimage=v.findViewById(R.id.fileimage);
        sendfiles=v.findViewById(R.id.sendfiles);
        SharedPreferences sharedPreferences=getContext().getSharedPreferences(filename,0);
        String cc =sharedPreferences.getString(classcode,"");
        String su =sharedPreferences.getString(subject,"");
        String se =sharedPreferences.getString(section,"");
        sub.setText(su);
        sec.setText(se);
        SharefilesAdapter sharefilesAdapter=new SharefilesAdapter(v.getContext(),list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,true);
        linearLayoutManager.setStackFromEnd(true);
        recycler=v.findViewById(R.id.sharerecycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(sharefilesAdapter);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:snapshot.child("group").child(cc).child("files").getChildren()){
                    if(dataSnapshot1!=null){
                        fileimage.setVisibility(View.GONE);
                        sendfiles.setVisibility(View.GONE);
                        Log.d("datasnap123456",dataSnapshot1+"");
                        Sharefilemodel sharefilemodel=dataSnapshot1.getValue(Sharefilemodel.class);
                        list.add(sharefilemodel);
                        Log.d("listitems",list.get(0).getMsg()+"");
                    }
                    else{
                        fileimage.setVisibility(View.VISIBLE);
                        sendfiles.setVisibility(View.VISIBLE);
                    }
                }
                sharefilesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;

    }
}