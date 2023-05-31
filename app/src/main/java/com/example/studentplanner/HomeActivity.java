package com.example.studentplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentplanner.model.TaskModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {


    SearchView txtSearch;
    BottomNavigationView bottomNav;
    TextView userWb;
    CircleImageView imgUser;
    FirebaseFirestore db;
    FloatingActionButton fabAdd;
    RecyclerView taskListRv;
    ArrayList<TaskModel> taskDataList = new ArrayList<>();
    TaskListAdapter taskListAdapter;
    String TAG = "Homepage query docs";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        bottomNav = findViewById(R.id.bottomNav);
        userWb = findViewById(R.id.userWb);
        imgUser = findViewById(R.id.imgUser);
        taskListRv = findViewById(R.id.taskListRv);
        txtSearch = findViewById(R.id.txtSearch);


        txtSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });



        bottomNav.setSelectedItemId(R.id.navHome);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                if(id == R.id.navProfile){
                    Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        userWb.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(imgUser);


        taskListAdapter = new TaskListAdapter(taskDataList);

        taskListRv.setAdapter(taskListAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);

        taskListRv.setLayoutManager(layoutManager);

        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });

        db = FirebaseFirestore.getInstance();

        db.collection("tasks")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                TaskModel taskModel;

                                taskModel = document.toObject(TaskModel.class);

                                taskModel.setTaskId(document.getId());

                                taskDataList.add(taskModel);

                                taskListAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void filterList(String text) {
        ArrayList<TaskModel> filteredList = new ArrayList<>();
        for(TaskModel taskModel : taskDataList) {
            if( taskModel.getTaskTitle().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(taskModel);
            }
        }
        if(filteredList.isEmpty()) {
            Toast.makeText(this, "No Data Found !!", Toast.LENGTH_SHORT).show();
        }
        else {
            taskListAdapter.setFilteredList(filteredList);
        }
    }
}