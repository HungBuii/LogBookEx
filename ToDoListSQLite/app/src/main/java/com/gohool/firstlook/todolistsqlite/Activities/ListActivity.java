package com.gohool.firstlook.todolistsqlite.Activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gohool.firstlook.todolistsqlite.databinding.ActivityListBinding;

import com.gohool.firstlook.todolistsqlite.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    // Declare variables
    private RecyclerView recyclerView;
    private RecycleViewAdapter recyclerViewAdapter;
    private List<Task> taskList;
    private List<Task> taskListEdit;
    private DatabaseHandle db;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    private EditText titleTask;
    private EditText descriptionTask;
    private EditText dateStarted;
    private EditText dateFinished;
    private EditText duration;
    private Button saveButton;

    private AppBarConfiguration appBarConfiguration;
    private ActivityListBinding binding;

    // Override methods
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

        // Initialize variables
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
            task.setDateStarted("Started on: " + t.getDateStarted());
            task.setDateFinished("Finished on: " + t.getDateFinished());
            task.setDuration("Duration: " + t.getDuration());
            task.setStatus(t.getStatus());
            taskListEdit.add(task);
        }

        // Set adapter
        recyclerViewAdapter = new RecycleViewAdapter(this, taskListEdit);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

    }

    // Create Popup Dialog for adding new task
    private void createPopupDialog()
    {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        titleTask = (EditText) view.findViewById(R.id.taskItem);
        descriptionTask = (EditText) view.findViewById(R.id.descriptionTask);

        final Calendar calendar = Calendar.getInstance();
        final int yearDateStarted = calendar.get(Calendar.YEAR);
        final int monthDateStarted = calendar.get(Calendar.MONTH);
        final int dayDateStarted = calendar.get(Calendar.DAY_OF_MONTH);
        dateStarted = (EditText) view.findViewById(R.id.dateStarted);
        dateStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        DatabaseHandle db = new DatabaseHandle(ListActivity.this);
                        if (isValidDate(year, month, dayOfMonth))
                        {
                            @SuppressLint("DefaultLocale") String date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                            Toast.makeText(ListActivity.this, "Select date: " + date, Toast.LENGTH_SHORT).show();
                            dateStarted.setText(date);
                        }
                        else
                        {
                            Toast.makeText(ListActivity.this, "This date cannot be less than the current date!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, yearDateStarted, monthDateStarted, dayDateStarted);
                datePickerDialog.show();
            }
        });

        final int yearDateFinished = calendar.get(Calendar.YEAR);
        final int monthDateFinished = calendar.get(Calendar.MONTH);
        final int dayDateFinished  = calendar.get(Calendar.DAY_OF_MONTH);
        dateFinished = (EditText) view.findViewById(R.id.dateFinished);
        dateFinished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ListActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        DatabaseHandle db = new DatabaseHandle(ListActivity.this);
                        if (isValidDate(year, month, dayOfMonth))
                        {
                            @SuppressLint("DefaultLocale") String date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                            Toast.makeText(ListActivity.this, "Select date: " + date, Toast.LENGTH_SHORT).show();
                            dateFinished.setText(date);
                        }
                        else
                        {
                            Toast.makeText(ListActivity.this, "This date cannot be less than the current date!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, yearDateFinished, monthDateFinished, dayDateFinished);
                datePickerDialog.show();
            }
        });

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        duration = (EditText) view.findViewById(R.id.duration);
        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ListActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        DatabaseHandle db = new DatabaseHandle(ListActivity.this);
                        @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", hourOfDay, minute);
                        Toast.makeText(ListActivity.this, "Select time: " + time, Toast.LENGTH_SHORT).show();
                        duration.setText(time);
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

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

    private boolean isValidDate(int year, int month, int dayOfMonth)
    {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (year < currentYear) return false;
        else if (year == currentYear && month < currentMonth) return false;
        else if (year == currentYear && month == currentMonth && dayOfMonth < currentDay) return false;
        return true;
    }

    // Save task to database
    private void saveTaskToDB(View v)
    {
        Task task = new Task();
        String newTaskTitle = titleTask.getText().toString();
        String newTaskDescription = descriptionTask.getText().toString();
        String newDateStarted = dateStarted.getText().toString().trim();
        String newDateFinished = dateFinished.getText().toString().trim();
        String newDuration = duration.getText().toString().trim();
        String newStatus = "false";

        task.setTitle(newTaskTitle);
        task.setDescription(newTaskDescription);
        task.setDateStarted(newDateStarted);
        task.setDateFinished(newDateFinished);
        task.setDuration(newDuration);
        task.setStatus(newStatus);

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