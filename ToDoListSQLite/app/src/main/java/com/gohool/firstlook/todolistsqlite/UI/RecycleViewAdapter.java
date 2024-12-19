package com.gohool.firstlook.todolistsqlite.UI;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.gohool.firstlook.todolistsqlite.Activities.DetailActivity;
import com.gohool.firstlook.todolistsqlite.Activities.ListActivity;
import com.gohool.firstlook.todolistsqlite.Activities.MainActivity;
import com.gohool.firstlook.todolistsqlite.Data.DatabaseHandle;
import com.gohool.firstlook.todolistsqlite.Model.Task;
import com.gohool.firstlook.todolistsqlite.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    // Declare variables
    private Context context;
    private List<Task> taskList;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    // Constructor
    public RecycleViewAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    // Override methods
    @NonNull
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.ViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.titleTask.setText(task.getTitle());
        holder.descriptionTask.setText(task.getDescription());
        holder.dateAdded.setText(task.getDateItemAdded());
        holder.dateStarted.setText(task.getDateStarted());
        holder.dateFinished.setText(task.getDateFinished());
        holder.duration.setText(task.getDuration());
        holder.statusCB.setChecked(Boolean.parseBoolean(task.getStatus()));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int id;
        public TextView titleTask;
        public TextView descriptionTask;
        public TextView dateAdded;
        public TextView dateStarted;
        public TextView dateFinished;
        public TextView duration;
        public CheckBox statusCB;
        public Button editButton;
        public Button deleteButton;

        public ViewHolder(@NonNull View view, Context ctx) {
            super(view);
            context = ctx;

            // Initialize views
            titleTask = (TextView) view.findViewById(R.id.titleTask);
            descriptionTask = (TextView) view.findViewById(R.id.descriptionTask);
            dateAdded = (TextView) view.findViewById(R.id.dateAdded);
            dateStarted = (TextView) view.findViewById(R.id.dateStarted);
            dateFinished = (TextView) view.findViewById(R.id.dateFinished);
            duration = (TextView) view.findViewById(R.id.duration);
            statusCB = (CheckBox) view.findViewById(R.id.statusCB);

            editButton = (Button) view.findViewById(R.id.editButton);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            statusCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    DatabaseHandle db = new DatabaseHandle(context);
                    int position = getAdapterPosition();
                    Task task = taskList.get(position);
                    String convert = String.valueOf(isChecked);
                    task.setStatus(convert);
                    db.updateStatusTask(task);
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Go to next screen DetailActivity
                    int position = getAdapterPosition();

                    Task task = taskList.get(position);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("title", task.getTitle());
                    intent.putExtra("description", task.getDescription());
                    intent.putExtra("dateAdded", task.getDateItemAdded());
                    intent.putExtra("dateStarted", task.getDateStarted());
                    intent.putExtra("dateFinished", task.getDateFinished());
                    intent.putExtra("duration", task.getDuration());
                    intent.putExtra("id", task.getId());
                    context.startActivity(intent);
                }
            });
        }

        // Handle click events
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Task task = taskList.get(position);
            if (v.getId() == R.id.editButton) {
                editTask(task);
            }

            if (v.getId() == R.id.deleteButton) {
                deleteTask(task.getId());
            }
        }

        // Delete task
        public void deleteTask(int id) {
            // create an AlertDialog
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirm_dialog, null);

            Button noButton = (Button) view.findViewById(R.id.noButton);
            Button yesButton = (Button) view.findViewById(R.id.yesButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // delete task
                    DatabaseHandle db = new DatabaseHandle(context);
                    db.deleteTask(id);
                    taskList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();

                }
            });
        }

        // Edit task
        public void editTask(Task task) {
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popup, null);

            final EditText taskTitle = (EditText) view.findViewById(R.id.taskItem);
            final EditText descriptionTask = (EditText) view.findViewById(R.id.descriptionTask);
            final EditText dateStarted = (EditText) view.findViewById(R.id.dateStarted);
            final EditText dateFinished = (EditText) view.findViewById(R.id.dateFinished);
            final EditText duration = (EditText) view.findViewById(R.id.duration);
            final TextView title = (TextView) view.findViewById(R.id.titleTask);

            title.setText("Edit Task");

            String subTaskTitle = task.getTitle();
            taskTitle.setText(subTaskTitle.substring(7));

            String subTaskDescription = task.getDescription();
            descriptionTask.setText(subTaskDescription.substring(13));

            String subTaskDateStarted = task.getDateStarted();
            dateStarted.setText(subTaskDateStarted.substring(12));

            String subTaskDateFinished = task.getDateFinished();
            dateFinished.setText(subTaskDateFinished.substring(13));

            String subTaskDuration = task.getDuration();
            duration.setText(subTaskDuration.substring(10));

            final Calendar calendar = Calendar.getInstance();
            final int yearDateStarted = calendar.get(Calendar.YEAR);
            final int monthDateStarted = calendar.get(Calendar.MONTH);
            final int dayDateStarted = calendar.get(Calendar.DAY_OF_MONTH);
            dateStarted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            DatabaseHandle db = new DatabaseHandle(context);
                            if (isValidDate(year, month, dayOfMonth)) {
                                @SuppressLint("DefaultLocale") String date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                                Toast.makeText(context, "Select date: " + date, Toast.LENGTH_SHORT).show();
                                dateStarted.setText(date);
                            } else {
                                Toast.makeText(context, "This date cannot be less than the current date!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, yearDateStarted, monthDateStarted, dayDateStarted);
                    datePickerDialog.show();
                }
            });

            final int yearDateFinished = calendar.get(Calendar.YEAR);
            final int monthDateFinished = calendar.get(Calendar.MONTH);
            final int dayDateFinished = calendar.get(Calendar.DAY_OF_MONTH);
            dateFinished.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            DatabaseHandle db = new DatabaseHandle(context);
                            if (isValidDate(year, month, dayOfMonth)) {
                                @SuppressLint("DefaultLocale") String date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                                Toast.makeText(context, "Select date: " + date, Toast.LENGTH_SHORT).show();
                                dateFinished.setText(date);
                            } else {
                                Toast.makeText(context, "This date cannot be less than the current date!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, yearDateFinished, monthDateFinished, dayDateFinished);
                    datePickerDialog.show();
                }
            });

            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            duration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            DatabaseHandle db = new DatabaseHandle(context);
                            @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", hourOfDay, minute);
                            Toast.makeText(context, "Select time: " + time, Toast.LENGTH_SHORT).show();
                            duration.setText(time);
                        }
                    }, hour, minute, true);
                    timePickerDialog.show();
                }
            });

            Button saveButton = (Button) view.findViewById(R.id.saveButton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandle db = new DatabaseHandle(context);

                    // Update task
                    task.setTitle(taskTitle.getText().toString());
                    task.setDescription(descriptionTask.getText().toString());
                    task.setDateStarted(dateStarted.getText().toString().trim());
                    task.setDateFinished(dateFinished.getText().toString().trim());
                    task.setDuration(duration.getText().toString().trim());

                    if (!taskTitle.getText().toString().isEmpty() &&
                            !descriptionTask.getText().toString().isEmpty()) {
                        db.updateTask(task);
                        notifyItemChanged(getAdapterPosition(), task);
                    } else {
                        Toast.makeText(context, "Task cannot be blank", Toast.LENGTH_LONG).show();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            context.startActivity(new Intent(context, MainActivity.class));
                        }
                    }, 500);
                }
            });
        }

        private boolean isValidDate(int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            if (year < currentYear) return false;
            else if (year == currentYear && month < currentMonth) return false;
            else if (year == currentYear && month == currentMonth && dayOfMonth < currentDay)
                return false;
            return true;
        }

    }
}
