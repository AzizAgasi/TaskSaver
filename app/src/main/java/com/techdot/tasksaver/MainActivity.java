package com.techdot.tasksaver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.techdot.tasksaver.adapter.TodoAdapter;
import com.techdot.tasksaver.model.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<TodoModel> tasksList;
    private TodoAdapter adapter;
    private RecyclerView todoRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        tasksList = new ArrayList<>();
        adapter = new TodoAdapter(this);
        todoRecyclerView = findViewById(R.id.tasksRecyclerView);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoRecyclerView.setAdapter(adapter);

        TodoModel trial = new TodoModel();
        trial.setId(0);
        trial.setStatus(0);
        trial.setTask("This is a dummy task");

        tasksList.add(trial);
        tasksList.add(trial);
        tasksList.add(trial);
        tasksList.add(trial);

        adapter.setTask(tasksList);
    }
}