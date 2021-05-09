package com.techdot.tasksaver.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techdot.tasksaver.AddNewTask;
import com.techdot.tasksaver.MainActivity;
import com.techdot.tasksaver.R;
import com.techdot.tasksaver.model.TodoModel;
import com.techdot.tasksaver.utils.DatabaseHandler;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private MainActivity activity;
    private List<TodoModel> todoList;

    DatabaseHandler db;

    public TodoAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase();
        TodoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Code to update database when checkbox is checked
                    db.updateStatus(item.getId(), 1);
                } else {
                    //Code to update the database when checkbox is not checked
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public boolean toBoolean(int status) {
        return status != 0;
    }

    public void setTask(List<TodoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        TodoModel item = todoList.get(position);    // Get the item to be deleted
        db.deleteTask(item.getId());    // Delete the task from the database
        todoList.remove(item);  // Remove the task from the arrayList
        notifyDataSetChanged(); // Notify that the data set has been changed
    }

    public void editItem(int position) {
        TodoModel item = todoList.get(position);    // Get the item to be edited
        Bundle bundle = new Bundle();   // Create new bundle
        bundle.putInt("id", item.getId());  // Put the id of the item in the bundle
        bundle.putString("task", item.getTask());   // Put the task of the item in the bundle
        AddNewTask fragment = new AddNewTask(); // Create a new object of AddNewTask
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox task;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.task);
        }
    }
}
