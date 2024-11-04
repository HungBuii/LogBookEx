package com.gohool.firstlook.todolistsqlite.Activities;

import android.os.Bundle;

import com.gohool.firstlook.todolistsqlite.Data.DatabaseHandle;
import com.gohool.firstlook.todolistsqlite.Model.Task;
import com.gohool.firstlook.todolistsqlite.UI.RecycleViewAdapter;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gohool.firstlook.todolistsqlite.databinding.ActivityListBinding;

import com.gohool.firstlook.todolistsqlite.R;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecycleViewAdapter recyclerViewAdapter;
    private List<Task> taskList;
    private List<Task> taskListEdit;
    private DatabaseHandle db;


    private AppBarConfiguration appBarConfiguration;
    private ActivityListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAnchorView(R.id.fab)
//                        .setAction("Action", null).show();
            }
        });

        db = new DatabaseHandle(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        taskListEdit = new ArrayList<>();

        // Get task from db
        taskList = db.getAllTasks();

        for (Task t : taskList)
        {
            Task task = new Task();
            task.setId(t.getId());
            task.setTitle("Title: " + t.getTitle());
            task.setDescription("Description: " + t.getDescription());
            task.setDateItemAdded("Added on: " + t.getDateItemAdded());
            taskListEdit.add(task);
        }

        recyclerViewAdapter = new RecycleViewAdapter(this, taskListEdit);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

    }


}