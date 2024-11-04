package com.gohool.firstlook.todolistsqlite.Util;

public class CreateDB
{
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "ToDoListDB";
    public static final String TABLE_NAME = "ToDoListTBL";

    // Table columns
    public static final String KEY_ID = "id";
    public static final String KEY_TASK_ITEM = "task_item";
    public static final String KEY_DESCRIPTION_TASK = "description_task";
    public static final String KEY_DATE_NAME = "date_added";
}
