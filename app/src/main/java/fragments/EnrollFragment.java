package fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectclassroom.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.ClassAdapter;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import model.FetchData;

public class EnrollFragment extends Fragment {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ImageView koala;
    ClassAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    List<FetchData> fetchDataList=new ArrayList<>();
    private static final String filename="login";
    private static final String user="username";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_enroll, container, false);
        recyclerView=v.findViewById(R.id.enrollrecycler);
        progressBar=v.findViewById(R.id.progressEnroll);
        koala=v.findViewById(R.id.koalaimg);
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences=getContext().getSharedPreferences(filename, Context.MODE_PRIVATE);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        koala.animate().setDuration(800).start();
        adapter = new ClassAdapter(v.getContext(),fetchDataList);
        Log.d("join", fetchDataList+"");
        recyclerView = v.findViewById(R.id.enrollrecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("Range")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String a=sharedPreferences.getString(user,"");
                fetchDataList.clear();
                for(DataSnapshot dataSnapshot : snapshot.child(a).child("joinclass").getChildren()){
                    if(dataSnapshot!=null){
                        koala.animate().alpha((float) 0).start();
                         ConstraintLayout constraintLayout=v.findViewById(R.id.cback);
                         constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        FrameLayout frameLayout=v.findViewById(R.id.enrollframe);
                        frameLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        FetchData data = dataSnapshot.getValue(FetchData.class);
                        Log.d("d", dataSnapshot.getKey() + "");
                        fetchDataList.add(data);
                    }
                }

                adapter.notifyDataSetChanged();

                koala.animate().translationY(-300).setDuration(800).start();
                TextView joinyourfirstclass=v.findViewById(R.id.joinyourfirstclass);
                joinyourfirstclass.animate().alpha(1).setDuration(2000).start();
                progressBar.setVisibility(View.GONE);

                int time=500;
                swipeRefreshLayout=v.findViewById(R.id.swipe);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run(){
                                adapter.notifyDataSetChanged();
                            }
                        },time);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
            }

            String deletedClass=null;
            ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position=viewHolder.getAdapterPosition();
                    SharedPreferences sharedPreferences=getActivity().getSharedPreferences(filename,Context.MODE_PRIVATE);
                    String a=sharedPreferences.getString(user,"");
                    if(direction==ItemTouchHelper.LEFT){
                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                        builder.setMessage("Are you sure to unenrol to class")
                                .setCancelable(false)
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deletedClass=fetchDataList.get(position).getClass_code();
                                        fetchDataList.remove(position);
                                        adapter.notifyItemRemoved(position);
                                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("users");
                                        databaseReference.child(a).child("joinclass").child(deletedClass).removeValue();
                                        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction().replace(R.id.framelayout,new EnrollFragment());
                                        fragmentTransaction.commit();

                                    }
                                })
                                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        adapter.notifyItemChanged(position);
                                       // getFragmentManager().beginTransaction().detach(EnrollFragment.this).attach(EnrollFragment.this).commit();
                                    }
                                });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();
                    }
                }
                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeLeftActionIcon(R.drawable.ic_delete)
                            .addSwipeLeftLabel("Delete")
                            .create()
                            .decorate();
                }
            };

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(v.getContext(), error.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}