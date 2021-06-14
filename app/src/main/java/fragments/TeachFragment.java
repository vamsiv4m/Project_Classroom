package fragments;

import android.content.*;
import android.graphics.*;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.example.projectclassroom.R;
import com.google.firebase.database.*;
import java.util.*;
import adapter.TeachAdapter;
import attendence.AttendenceInterface;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        joinDataList = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_teach, container, false);
        koalaTeach = v.findViewById(R.id.koalateach);
        SwipeRefreshLayout swipe;
        noclass = v.findViewById(R.id.noclass);
        imgconstraint = v.findViewById(R.id.imgconstraint);
        progressBar = v.findViewById(R.id.progressTeach);
        progressBar.setVisibility(View.VISIBLE);
        swipe = v.findViewById(R.id.swipeTeach);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        adapter = new TeachAdapter(v.getContext(), joinDataList);
        recyclerView = v.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences(filename, Context.MODE_PRIVATE);
                String a = sharedPreferences.getString(user, "");
                joinDataList.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.child(a).child("class").getChildren()) {
                    if (dataSnapshot1 != null) {
                        Log.d("datasnapshot1", dataSnapshot1 + "");
                        ConstraintLayout layout = v.findViewById(R.id.constbackground);
                        layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        imgconstraint.animate().alpha(0).start();
                        FrameLayout frameLayout = v.findViewById(R.id.teachframe);
                        frameLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        JoinData data = dataSnapshot1.getValue(JoinData.class);
                        Log.d("d", dataSnapshot1.getValue() + "");
                        joinDataList.add(data);
                        Log.d("f", joinDataList.get(0).getSection() + "");
                    }
                }

                adapter.notifyDataSetChanged();

                noclass.animate().alpha(1).setDuration(1000).start();
                progressBar.setVisibility(View.GONE);

                swipe.setOnRefreshListener(() -> {
                    swipe.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                });

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
            }

            final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int position = viewHolder.getAdapterPosition();
                    String name = joinDataList.get(position).getClassname();
                    String sub = joinDataList.get(position).getSubject();
                    String sec = joinDataList.get(position).getSection();
                    String code = joinDataList.get(position).getClass_code();
                    switch (direction) {
                        case ItemTouchHelper.LEFT:
                            Intent i = new Intent(getContext(), EditClass.class);
                            i.putExtra("name", name);
                            i.putExtra("sec", sec);
                            i.putExtra("sub", sub);
                            i.putExtra("code", code);
                            startActivity(i);
                            final Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                adapter.notifyItemChanged(position);
                                assert getFragmentManager() != null;
                                getFragmentManager().beginTransaction().detach(TeachFragment.this).attach(TeachFragment.this).commit();
                            }, 500);
                            break;
                        case ItemTouchHelper.RIGHT:
                            Intent intent = new Intent(getContext(), AttendenceInterface.class);
                            intent.putExtra("code", code);
                            intent.putExtra("sec", sec);
                            intent.putExtra("sub", sub);
                            startActivity(intent);
                            adapter.notifyItemChanged(position);
                            break;
                    }
                }

                @Override
                public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeLeftActionIcon(R.drawable.settings)
                            .addSwipeLeftLabel("Settings")
                            .addSwipeRightActionIcon(R.drawable.attendance)
                            .addSwipeRightLabel("Attendance")
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