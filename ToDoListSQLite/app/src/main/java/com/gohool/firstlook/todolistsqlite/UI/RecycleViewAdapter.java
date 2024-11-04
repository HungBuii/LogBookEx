package com.gohool.firstlook.todolistsqlite.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.gohool.firstlook.todolistsqlite.Activities.DetailActivity;
import com.gohool.firstlook.todolistsqlite.Data.DatabaseHandle;
import com.gohool.firstlook.todolistsqlite.Model.Task;
import com.gohool.firstlook.todolistsqlite.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>
{
    private Context context;
    private List<Task> taskList;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecycleViewAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

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
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public int id;
        public TextView titleTask;
        public TextView descriptionTask;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;

        public ViewHolder(@NonNull View view, Context ctx) {
            super(view);
            context = ctx;

            titleTask = (TextView) view.findViewById(R.id.titleTask);
            descriptionTask = (TextView) view.findViewById(R.id.descriptionTask);
            dateAdded = (TextView) view.findViewById(R.id.dateAdded);

            editButton = (Button) view.findViewById(R.id.editButton);
            deleteButton = (Button) view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

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
                    intent.putExtra("id", task.getId());
                    context.startActivity(intent);
                }
            });
        }

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

        public void deleteTask(int id)
        {
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

        public void editTask(Task task)
        {
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.popup, null);

            final EditText taskTitle = (EditText) view.findViewById(R.id.taskItem);
            final EditText descriptionTask = (EditText) view.findViewById(R.id.descriptionTask);
            final TextView title = (TextView) view.findViewById(R.id.titleTask);

            title.setText("Edit Task");
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

                    if (!taskTitle.getText().toString().isEmpty() &&
                            !descriptionTask.getText().toString().isEmpty()) {
                        db.updateTask(task);
                        notifyItemChanged(getAdapterPosition(), task);
                    }
                    else {
                        Toast.makeText(context, "Task cannot be blank", Toast.LENGTH_LONG).show();
                    }

                    dialog.dismiss();
                }
            });
        }

    }
}
