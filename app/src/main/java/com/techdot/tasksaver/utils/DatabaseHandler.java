package com.techdot.tasksaver.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.techdot.tasksaver.model.TodoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;   //Current version of the table
    private static final String NAME = "toDoListDatabase";
    private static final String TO_DO_TABLE = "toDo"; //Name of the table
    private static final String ID = "id";  // Name of the column having ID (It will be auto incremented
    private static final String TASK = "task";  //Name of the column containing the actual task
    private static final String STATUS = "status"; // Name of the column containing the status of the checkbox
    //SQLite statement : CREATE TABLE toDo (id INTEGER PRIMARY KEY AUTOINCREMENT, task TEXT, status INTEGER)
    private static final String CREATE_TABLE = "CREATE TABLE " + TO_DO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TASK + " TEXT, " + STATUS + " INTEGER)";

    private SQLiteDatabase db;  //Creating a variable for the actual database


    public DatabaseHandler(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the old version of the table
        db.execSQL("DROP TABLE IF EXISTS " + TO_DO_TABLE);
        //Recreate the new version of the table
        onCreate(db);
    }

    //Method to open the database
    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    //Method to insert a new task
    public void insertTask(TodoModel task) {
        ContentValues cv = new ContentValues(); //Create a ContentValue object to pass to db.insert
        cv.put("TASK" , task.getTask());    //Put the task string in cv
        cv.put(STATUS, 0);  //Put the status value in cv
        db.insert(TO_DO_TABLE, null, cv);   //Insert the new task into the database
    }

    public List<TodoModel> getAllTasks() {
        List<TodoModel> tasksList = new ArrayList<>();  //ArrayList to store all the tasks in the recyclerview
        Cursor cursor = null;   //cursor object for querying the recyclerview
        db.beginTransaction();  //begin the database transaction
        try {
            //Returns cursor over a given set
            cursor = db.query(TO_DO_TABLE, null, null, null, null, null, null);
            //Check is cursor object exists
            if (cursor != null) {
                //Move the cursor to the first object
                if (cursor.moveToFirst()) {
                    do {
                        TodoModel task = new TodoModel();   //Variable to store value of task the cursor is pointing
                        //Store the ID, task, status of the task the cursor is pointing in TodoModel object
                        task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
                        //Add the task to the taskList variable
                        tasksList.add(task);
                    } while (cursor.moveToNext());  //Move the cursor to the next object
                }
            }
        } finally {
            db.endTransaction();    //End the database transaction
            cursor.close(); //Close the cursor
        }
        return tasksList;
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();  // Create content value to pass to db.update
        cv.put(TASK, task); // Put the value of task in TASK column
        //Update the table at location whose id is passed to the function
        db.update(TO_DO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }
}
