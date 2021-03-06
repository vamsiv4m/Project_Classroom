package com.example.projectclassroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import adapter.ClassAdapter;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import model.FetchData;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener {
    //variables
    Toolbar toolbar;
    NavigationView navigation;
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    ActionBarDrawerToggle actionBarDrawerToggle;
    SwipeRefreshLayout swipeRefreshLayout;
    private static final String filename = "login";
    private static final String user="username";
    private static final String email="email";
    private static final String codename="classcode";
    private static final String classcode="code";
    List<FetchData> fetchDataList=new ArrayList<>();

    ClassAdapter adapter;
    @Override
    protected void onStart() {
        super.onStart();
        navigation=findViewById(R.id.navigation);
        View navi = navigation.getHeaderView(0);
        SharedPreferences sharedPreferences=getSharedPreferences(filename,Context.MODE_PRIVATE);
        String a=sharedPreferences.getString(user,"");
        String m=sharedPreferences.getString(email,"");
        TextView unameheader=navi.findViewById(R.id.unameheader);
        TextView emailheader=navi.findViewById(R.id.emailheader);
        unameheader.setText(a);
        emailheader.setText(m);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_activity_1);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        progressBar=findViewById(R.id.progress);
        adapter = new ClassAdapter(MainActivity.this, fetchDataList);
        //To disable night mode view for our app ...
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //custom tool bar.
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawerLayout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        swipeRefreshLayout=findViewById(R.id.swiperefresh);
        navigation = findViewById(R.id.navigation);
        navigationDrawer();
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if (signInAccount != null) {
            View navi = navigation.getHeaderView(0);
            TextView googleusername = navi.findViewById(R.id.unameheader);
            TextView googlemailid = navi.findViewById(R.id.emailheader);
        }
        recyclerView = findViewById(R.id.recycler);
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //retreiving data from firebase using for loop
                SharedPreferences sharedPreferences=getSharedPreferences(filename,Context.MODE_PRIVATE);
                String a=sharedPreferences.getString(user,"");
                Log.d("a",""+a);

                for (DataSnapshot dataSnapshot1 : snapshot.child(a).child("class").getChildren()) {
                        FetchData data = dataSnapshot1.getValue(FetchData.class);
                        Log.d("d",dataSnapshot1+"");
                        fetchDataList.add(data);
                }

                progressBar.setVisibility(View.GONE);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);

                recyclerView.setLayoutManager(linearLayoutManager);

                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);
                        finish();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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
            SharedPreferences sharedPreferences=getSharedPreferences(filename,Context.MODE_PRIVATE);
            String a=sharedPreferences.getString(user,"");
            if(direction==ItemTouchHelper.LEFT){
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure to unenrol to class")
                        .setCancelable(false)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletedClass=fetchDataList.get(position).getClass_code();
                                fetchDataList.remove(position);
                                adapter.notifyItemRemoved(position);
                                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("users");
                                databaseReference.child(a).child("class").child(deletedClass).removeValue();
                            }
                        })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ImageView img=findViewById(R.id.bgimg);
                                img.setScaleType(ImageView.ScaleType.FIT_XY);
                                adapter.notifyItemChanged(position);
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent exitIntent =new Intent(Intent.ACTION_MAIN);
            exitIntent.addCategory(Intent.CATEGORY_HOME);
            exitIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(exitIntent);
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //....Logout implementation.....
        SharedPreferences sharedPreferences=getSharedPreferences(filename,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(item.getItemId()==R.id.logout) {
            editor.clear();
            editor.apply();
            //Logout
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return true;
    }

    public void navigationDrawer() {
        navigation.bringToFront();
        View nav = navigation.getHeaderView(0);
        TextView ntext = nav.findViewById(R.id.unameheader);
        navigation.setNavigationItemSelectedListener(this);
    }

//    public void logout(MenuItem item) {
//        SharedPreferences sharedPreferences=getSharedPreferences(filename,Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
//        //Logout
//        FirebaseAuth.getInstance().signOut();
//        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//        startActivity(i);
//        finish();
//    }

    @Override 
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.createclassmenu:
                i = new Intent(this, MainActivity2.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;
            case R.id.joinclassroommenu:
                i = new Intent(this, JoinClass.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void unenroll(MenuItem item) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        SharedPreferences sharedPreferences=getSharedPreferences(codename,Context.MODE_PRIVATE);
        String cc=sharedPreferences.getString(classcode,"");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}