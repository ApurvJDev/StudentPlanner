package com.example.studentplanner;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.model.TaskModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private ArrayList<TaskModel> taskDataSet;

    public void setFilteredList(ArrayList<TaskModel> filteredList) {
        this.taskDataSet = filteredList;
        notifyDataSetChanged();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskTitle,taskDesc,taskStatus,taskPriority,taskDeadline;
        CheckBox checkBox;
        ConstraintLayout itemTask;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            taskPriority = (TextView) view.findViewById(R.id.taskPriority);
            taskDeadline = (TextView) view.findViewById(R.id.taskDeadine);
            taskTitle = (TextView) view.findViewById(R.id.taskTitle);
            taskDesc = (TextView) view.findViewById(R.id.taskDescription);
            taskStatus = (TextView) view.findViewById(R.id.taskStatus);
            itemTask = (ConstraintLayout) view.findViewById(R.id.itemTask);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        }
    }

    public TaskListAdapter(ArrayList<TaskModel> taskDataSet) {
        this.taskDataSet = taskDataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_task, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        // viewHolder.getTextView().setText(taskDataSet[position]);
        viewHolder.taskTitle.setText(taskDataSet.get(position).getTaskTitle());
        viewHolder.taskDesc.setText(taskDataSet.get(position).getTaskDesc());
        viewHolder.taskStatus.setText(taskDataSet.get(position).getTaskStatus());




        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TaskModel completedTask = taskDataSet.get(position);

                boolean checked = ((CheckBox) v).isChecked();

                if(checked) {
                    completedTask.setTaskStatus("Completed");
                    FirebaseFirestore.getInstance().collection("tasks").document(taskDataSet.get(position).getTaskId())
                            .set(completedTask).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(v.getContext(), "Item Marked as Completed", Toast.LENGTH_SHORT).show();
                                    viewHolder.taskStatus.setText("Completed");
                                }
                            });
                }
            }
        });



        viewHolder.taskDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentYear = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        TaskModel deadlineTask = taskDataSet.get(position);

                        deadlineTask.setTaskDeadline("" +dayOfMonth+ "/" +(month+1)+ "/" +year );

                        FirebaseFirestore.getInstance().collection("tasks").document(taskDataSet.get(position).getTaskId())
                                .set(deadlineTask).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onSuccess(Void unused) {

                                        viewHolder.taskDeadline.setText("" +dayOfMonth+ "/" +(month+1)+ "/" +year);

                                        Toast.makeText(v.getContext(), "Task Deadline Set", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                },currentYear,currentMonth,currentDay);   //setting current day initially
                datePickerDialog.show();
            }
        });







        viewHolder.taskPriority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), viewHolder.taskPriority);
                popupMenu.inflate(R.menu.prioritymenu);
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.highPriority){
                            TaskModel highPriorityTask = taskDataSet.get(position);
                            highPriorityTask.setTaskPriority("High");
                            FirebaseFirestore.getInstance().collection("tasks").document(taskDataSet.get(position).getTaskId())
                                    .set(highPriorityTask).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            viewHolder.taskPriority.setText("High");
                                            Toast.makeText(v.getContext(), "Priority set as High", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else if (item.getItemId() == R.id.mediumPriority) {
                            TaskModel mediumPriorityTask = taskDataSet.get(position);
                            mediumPriorityTask.setTaskPriority("Medium");
                            FirebaseFirestore.getInstance().collection("tasks").document(taskDataSet.get(position).getTaskId())
                                    .set(mediumPriorityTask).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            viewHolder.taskPriority.setText("Medium");
                                            Toast.makeText(v.getContext(), "Priority set as Medium", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else {
                            TaskModel lowPriorityTask = taskDataSet.get(position);
                            lowPriorityTask.setTaskPriority("Low");
                            FirebaseFirestore.getInstance().collection("tasks").document(taskDataSet.get(position).getTaskId())
                                    .set(lowPriorityTask).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            viewHolder.taskPriority.setText("Low");
                                            Toast.makeText(v.getContext(), "Priority set as Low", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        return false;
                    }
                });
            }
        });





        viewHolder.itemTask.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(v.getContext(),viewHolder.itemTask);
                popupMenu.inflate(R.menu.taskmenu);
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.deleteMenu) {

                            FirebaseFirestore.getInstance().collection("tasks").document(taskDataSet.get(position).
                                    getTaskId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {

                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(v.getContext(), "Item Deleted ", Toast.LENGTH_SHORT).show();
                                    viewHolder.itemTask.setVisibility(View.GONE);
                                }
                            });
                        }
                        return false;
                    }

                });
            }
        });
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return taskDataSet.size();
    }
}
