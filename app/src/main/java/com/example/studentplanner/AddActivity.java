package com.example.studentplanner;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentplanner.model.TaskModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

public class AddActivity extends AppCompatActivity {

    FirebaseFirestore db;
    EditText newTitle,newDesc,newStatus;
    Button btnAddTask;
    String newTaskTitle,newTaskDesc,newTaskStatus;

//  String newPriority,newDeadline;


    String TAG = "Student Planner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("Add Task");

        newTitle = findViewById(R.id.newTitle);
        newDesc = findViewById(R.id.newDescription);
//        newPriority = findViewById(R.id.newPriority);
//        newDeadline = findViewById(R.id.newDeadline);
        newStatus = findViewById(R.id.newStatus);
        btnAddTask = findViewById(R.id.btnAddTask);

        db = FirebaseFirestore.getInstance();

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTaskTitle = newTitle.getText().toString().trim();
                newTaskDesc = newDesc.getText().toString().trim();
//                newTaskPriority = newPriority.getText().toString().trim();
//                newTaskDeadline = newDeadline.getText().toString().trim();
                newTaskStatus = newStatus.getText().toString().trim();

                if(newTaskTitle != null) {
               //     Toast.makeText(AddActivity.this, newTaskTitle, Toast.LENGTH_SHORT).show();
                    TaskModel taskModel = new TaskModel("",newTaskTitle,newTaskDesc,
                            "Pending", FirebaseAuth.getInstance().getUid(),"","");


                    db.collection("tasks").add(taskModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG,"Document Snapshot added within id: " +documentReference.getId());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document",e);
                        }
                    });
                }
            }
        });
    }
}