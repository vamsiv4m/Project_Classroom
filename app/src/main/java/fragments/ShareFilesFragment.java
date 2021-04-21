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
    RecyclerView recycler;
    public ShareFilesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        List<Sharefilemodel> list=new ArrayList<>();
        View v= inflater.inflate(R.layout.fragment_share_files, container, false);

        SharedPreferences sharedPreferences=getContext().getSharedPreferences(filename,0);
        String cc =sharedPreferences.getString(classcode,"");
        SharefilesAdapter sharefilesAdapter=new SharefilesAdapter(v.getContext(),list);

        recycler=v.findViewById(R.id.sharerecycler);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(v.getContext()));
        recycler.setAdapter(sharefilesAdapter);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot1:snapshot.child("group").child(cc).child("files").getChildren()){
                    if(dataSnapshot1!=null){
                        Log.d("datasnap123456",dataSnapshot1+"");
                        Sharefilemodel sharefilemodel=dataSnapshot1.getValue(Sharefilemodel.class);
                        list.add(sharefilemodel);
                        Log.d("listitems",list.get(0).getMsg()+"");
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