package fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapter.TeachAdapter;
import editclass.EditClass;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import model.JoinData;

public class TeachFragment extends Fragment {
    RecyclerView recyclerView;
    private static final String filename = "login";
    private static final String user = "username";
    ProgressBar progressBar;
    List<JoinData> joinDataList;
    ImageView koalaTeach;
    ConstraintLayout imgconstraint;
    TeachAdapter adapter;
    TextView noclass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        joinDataList = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_teach, container, false);
        koalaTeach=v.findViewById(R.id.koalateach);
        SwipeRefreshLayout swipe;
        noclass=v.findViewById(R.id.noclass);
        imgconstraint=v.findViewById(R.id.imgconstraint);
        progressBar=v.findViewById(R.id.progressTeach);
        progressBar.setVisibility(View.VISIBLE);
        swipe=v.findViewById(R.id.swipeTeach);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(filename, Context.MODE_PRIVATE);
                String a = sharedPreferences.getString(user, "");
                for (DataSnapshot dataSnapshot1 : snapshot.child(a).child("class").getChildren()) {
                    if (dataSnapshot1 != null) {
                        ConstraintLayout layout=v.findViewById(R.id.constbackground);
                        layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        imgconstraint.animate().alpha(0).start();
                        FrameLayout frameLayout=v.findViewById(R.id.teachframe);
                        frameLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        JoinData data = dataSnapshot1.getValue(JoinData.class);
                        Log.d("d", dataSnapshot1.getValue() + "");
                        joinDataList.add(data);
                        Log.d("f", joinDataList.get(0).getSection() + "");
                    }
                }
                noclass.animate().alpha(1).setDuration(1000).start();
                progressBar.setVisibility(View.GONE);
                adapter = new TeachAdapter(v.getContext(), joinDataList);
                recyclerView = v.findViewById(R.id.recyclerview);
                recyclerView.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);

                swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        swipe.setRefreshing(false);
                        getFragmentManager().beginTransaction().detach(TeachFragment.this).attach(TeachFragment.this).commit();
                    }
                });
                ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
            }

            ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }
                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position=viewHolder.getAdapterPosition();
                    String name=joinDataList.get(position).getClassname();
                    String sub=joinDataList.get(position).getSubject();
                    String sec=joinDataList.get(position).getSection();
                    String code=joinDataList.get(position).getClass_code();
                    SharedPreferences sharedPreferences=getActivity().getSharedPreferences(filename,Context.MODE_PRIVATE);
                    String a=sharedPreferences.getString(user,"");
                    if(direction==ItemTouchHelper.LEFT){
                        Intent i=new Intent(getContext(), EditClass.class);
                        i.putExtra("name",name);
                        i.putExtra("sec",sec);
                        i.putExtra("sub",sub);
                        i.putExtra("code",code);
                        startActivity(i);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyItemChanged(position);
                                getFragmentManager().beginTransaction().detach(TeachFragment.this).attach(TeachFragment.this).commit();
                            }
                        }, 500);
                    }
                }
                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeLeftActionIcon(R.drawable.settings)
                            .addSwipeLeftLabel("Settings")
                            .create()
                            .decorate();
                }
            };
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}