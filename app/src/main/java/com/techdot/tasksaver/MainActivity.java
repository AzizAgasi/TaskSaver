package com.techdot.tasksaver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.techdot.tasksaver.adapter.TodoAdapter;
import com.techdot.tasksaver.model.TodoModel;
import com.techdot.tasksaver.utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private List<TodoModel> tasksList;
    private TodoAdapter adapter;
    private RecyclerView todoRecyclerView;
    private FloatingActionButton fab;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        fab = findViewById(R.id.fab);

        tasksList = new ArrayList<>();
        adapter = new TodoAdapter(this);
        todoRecyclerView = findViewById(R.id.tasksRecyclerView);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoRecyclerView.setAdapter(adapter);

        tasksList = db.getAllTasks();   //Get all the tasks in the database in the arrayList created
        Collections.reverse(tasksList); // Reverse the order of the arrayList containing the tasks
        adapter.setTask(tasksList); // Set the tasks

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.getInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        tasksList = db.getAllTasks();
        //To put the recently added tasks in top most section of application
        Collections.reverse(tasksList);
        adapter.setTask(tasksList);
        adapter.notifyDataSetChanged();
    }
}