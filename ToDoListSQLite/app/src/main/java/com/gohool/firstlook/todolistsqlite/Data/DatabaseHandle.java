package com.gohool.firstlook.todolistsqlite.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gohool.firstlook.todolistsqlite.Model.Task;
import com.gohool.firstlook.todolistsqlite.Util.CreateDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandle extends SQLiteOpenHelper
{
    private Context ctx;

    // Constructor
    public DatabaseHandle(@Nullable Context context) {
        super(context, CreateDB.DB_NAME, null, CreateDB.DB_VERSION);
        this.ctx = context;
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOLIST_TABLE = "CREATE TABLE " + CreateDB.TABLE_NAME + "("
                + CreateDB.KEY_ID + " INTEGER PRIMARY KEY,"
                + CreateDB.KEY_TASK_ITEM + " TEXT,"
                + CreateDB.KEY_DESCRIPTION_TASK + " TEXT,"
                + CreateDB.KEY_DATE_NAME + " LONG);";

        db.execSQL(CREATE_TODOLIST_TABLE);
    }

    // Upgrade table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CreateDB.TABLE_NAME);
        onCreate(db);
    }

    /*
        CRUD Operations (Create, Read, Update, Delete)
    */

    // Add task
    public void addTask(Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CreateDB.KEY_TASK_ITEM, task.getTitle());
        values.put(CreateDB.KEY_DESCRIPTION_TASK, task.getDescription());
        values.put(CreateDB.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        // Insert the row
        db.insert(CreateDB.TABLE_NAME, null, values);

        Log.d("Saved!!", "Saved to DB");
    }

    // Get a task
    public Task getTask(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(CreateDB.TABLE_NAME, new String[] {CreateDB.KEY_ID,
                        CreateDB.KEY_TASK_ITEM, CreateDB.KEY_DESCRIPTION_TASK,
                        CreateDB.KEY_DATE_NAME},
                CreateDB.KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);

        Task task = new Task();
        if (cursor != null)
        {
            cursor.moveToFirst();

            // Get task
            int idIndex = cursor.getColumnIndex(CreateDB.KEY_ID);
            if (idIndex >= 0)
            {
                task.setId(Integer.parseInt(cursor.getString(idIndex)));
            }

            int titleIndex = cursor.getColumnIndex(CreateDB.KEY_TASK_ITEM);
            if (titleIndex >= 0) {
                task.setTitle(cursor.getString(titleIndex));
            }

            int descriptionIndex = cursor.getColumnIndex(CreateDB.KEY_DESCRIPTION_TASK);
            if (descriptionIndex >= 0) {
                task.setDescription(cursor.getString(descriptionIndex));
            }

            // convert timestamp to something readable
            int dateIndex = cursor.getColumnIndex(CreateDB.KEY_DATE_NAME);
            if (dateIndex >= 0) {
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(dateIndex)).getTime());
                task.setDateItemAdded(formattedDate);
            }
        }
        return task;
    }

    // Get all tasks
    public List<Task> getAllTasks()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Task> taskList = new ArrayList<>();

        Cursor cursor = db.query(CreateDB.TABLE_NAME, new String[] {
                        CreateDB.KEY_ID, CreateDB.KEY_TASK_ITEM, CreateDB.KEY_DESCRIPTION_TASK, CreateDB.KEY_DATE_NAME},
                null, null, null, null, CreateDB.KEY_DATE_NAME + " DESC", null); // ASC: ascending, DESC: descending

        if (cursor.moveToFirst())
        {
            do {
                Task task = new Task();
                int idIndex = cursor.getColumnIndex(CreateDB.KEY_ID);
                if (idIndex >= 0)
                {
                    task.setId(Integer.parseInt(cursor.getString(idIndex)));
                }

                int titleIndex = cursor.getColumnIndex(CreateDB.KEY_TASK_ITEM);
                if (titleIndex >= 0) {
                    task.setTitle(cursor.getString(titleIndex));
                }

                int descriptionIndex = cursor.getColumnIndex(CreateDB.KEY_DESCRIPTION_TASK);
                if (descriptionIndex >= 0) {
                    task.setDescription(cursor.getString(descriptionIndex));
                }

                // convert timestamp to something readable
                int dateIndex = cursor.getColumnIndex(CreateDB.KEY_DATE_NAME);
                if (dateIndex >= 0) {
                    java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                    String formattedDate = dateFormat.format(new Date(cursor.getLong(dateIndex)).getTime());
                    task.setDateItemAdded(formattedDate);
                }

                // Add to the groceryList
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        return taskList;
    }

    // Update task
    public int updateTask(Task task)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CreateDB.KEY_TASK_ITEM, task.getTitle());
        values.put(CreateDB.KEY_DESCRIPTION_TASK, task.getDescription());
        values.put(CreateDB.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        // update row
        return db.update(CreateDB.TABLE_NAME, values, CreateDB.KEY_ID + "=?",
                new String[] {String.valueOf(task.getId())}); // return ID of row updated
    }

    // Delete task
    public void deleteTask(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CreateDB.TABLE_NAME, CreateDB.KEY_ID + "=?",
                new String[] {String.valueOf(id)});
        db.close();
    }

    // Get count
    public int getTaskCount()
    {
        String countQuery = "SELECT * FROM " + CreateDB.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null); // rawQuery: Runs the provided SQL and returns a "Cursor" over the result set (multiple results).

        return cursor.getCount();
    }

}
