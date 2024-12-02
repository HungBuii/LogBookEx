package com.gohool.firstlook.todolistsqlite.Model;

// Model Task
public class Task
{
    private int id;
    private String title;
    private String description;
    private String dateItemAdded;
    private String dateStarted;
    private String dateFinished;

    public Task() {
    }

    public Task(int id, String title, String description, String dateStarted, String dateFinished) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateItemAdded = dateItemAdded;
        this.dateStarted = dateStarted;
        this.dateFinished = dateFinished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public String getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(String dateFinished) {
        this.dateFinished = dateFinished;
    }
}
