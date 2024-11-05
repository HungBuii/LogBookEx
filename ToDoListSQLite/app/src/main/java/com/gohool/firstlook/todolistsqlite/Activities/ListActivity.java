package com.gohool.firstlook.todolistsqlite.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.gohool.firstlook.todolistsqlite.Data.DatabaseHandle;
import com.gohool.firstlook.todolistsqlite.Model.Task;
import com.gohool.firstlook.todolistsqlite.UI.RecycleViewAdapter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gohool.firstlook.todolistsqlite.databinding.ActivityListBinding;

import com.gohool.firstlook.todolistsqlite.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecycleViewAdapter recyclerViewAdapter;
    private List<Task> taskList;
    private List<Task> taskListEdit;
    private DatabaseHandle db;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText titleTask;
    private EditText descriptionTask;
    private Button saveButton;

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
                createPopupDialog();
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

    private void createPopupDialog()
    {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        titleTask = (EditText) view.findViewById(R.id.taskItem);
        descriptionTask = (EditText) view.findViewById(R.id.descriptionTask);
        saveButton = (Button) view.findViewById(R.id.saveButton);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTaskToDB(v);
            }
        });
    }

    private void saveTaskToDB(View v)
    {
        Task task = new Task();
        String newTaskTitle = titleTask.getText().toString();
        String newTaskDescription = descriptionTask.getText().toString();

        task.setTitle(newTaskTitle);
        task.setDescription(newTaskDescription);

        // Save to db
        db.addTask(task);
        Snackbar.make(v, "Task saved", Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        }, 1000);
    }


}