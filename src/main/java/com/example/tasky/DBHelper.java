package com.example.tasky;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "tasky.db";

    public DBHelper(Context context){
        super(context, DATABASE_NAME,null,1);
    }

    // create tables of data base
    public void onCreate(SQLiteDatabase db){
        // create table week_no
        db.execSQL("create table week_no (week_no integer)");
        db.execSQL("insert into week_no(week_no) values(0)");

        // create password (password)
        db.execSQL("create table password (password text)");
        db.execSQL("insert into password(password) values('4444')");

        // created table tasks (id, title, body, day, created_time, due_time, alarm_time, done, deleted)
        db.execSQL("create table tasks" +
                "(id int primary key," +
                " title text," +
                " body text," +
                " day int," +
                " created_time text," +
                " due_time text," +
                " alarm_time text," +
                " done int," +
                " deleted int)");
    }

    // drop all tables and call onCreate() method again
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS week_no");
        db.execSQL("DROP TABLE IF EXISTS password");
        db.execSQL("DROP TABLE IF EXISTS tasks");

        onCreate(db);
    }

    public void reUpdateDatabase(){
        SQLiteDatabase db = getWritableDatabase();

        // drop tables
        db.execSQL("DROP TABLE IF EXISTS week_no");
        db.execSQL("DROP TABLE IF EXISTS password");
        db.execSQL("DROP TABLE IF EXISTS tasks");

        // create tables
        // create table week_no
        db.execSQL("create table week_no (week_no integer)");
        db.execSQL("insert into week_no(week_no) values(0)");

        // create password (password)
        db.execSQL("create table password (password text)");
        db.execSQL("insert into password(password) values('4444')");

        // created table tasks (id, title, body, day, created_time, due_time, alarm_time, done, deleted)
        db.execSQL("create table tasks" +
                "(id int primary key," +
                " title text," +
                " body text," +
                " day int," +
                " created_time text," +
                " due_time text," +
                " alarm_time text," +
                " done int," +
                " deleted int)");
    }

    public boolean setWeekSaved(int week_no){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update week_no set week_no = " + Integer.toString(week_no));

        return true;
    }

    public int getWeekSaved(){
        int week_no = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor week_no_cursor = db.rawQuery("select * from week_no" ,null);
        if(week_no_cursor.getCount() != 0){
            week_no_cursor.moveToNext();
            week_no = week_no_cursor.getInt(0);
        }

        return week_no;
    }

    public void sendTask2NextDay(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("update tasks set day=? where id=?", new String[]{Integer.toString((task.getDay()+1)%7), Integer.toString(task.getId())});
        cursor.moveToFirst();
        cursor.close();
    }

    public boolean setPassword(String new_password){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("update password set password = " + new_password);

        return true;
    }

    public String getPassword(){
        String password = "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor password_cursor = db.rawQuery("select * from password" ,null);
        if(password_cursor.getCount() != 0){
            password_cursor.moveToNext();
            password = password_cursor.getString(0);
        }

        return password;
    }

    public boolean addTodoTask(Task todo_task){

        int id = todo_task.getId();
        String title = todo_task.getTitle();
        String body = todo_task.getBody();
        int day = todo_task.getDay();
        String created_time = todo_task.getCreated_time();
        String due_time = todo_task.getDue_time();
        String alarm_time = todo_task.getAlarm_time();
        int done = todo_task.getDone();
        int deleted = todo_task.getDeleted();

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("title", title);
        values.put("body", body);
        values.put("day", day);
        values.put("created_time", created_time);
        values.put("due_time", due_time);
        values.put("alarm_time", alarm_time);
        values.put("done", done);
        values.put("deleted", deleted);

        long row = db.insert("tasks", null, values);
        if(row != -1){
            return true;
        }
        else{
            return false;
        }
    }

    public ArrayList<Task> getTodoTasks(int selected_day){
        ArrayList<Task> todo_tasks = new ArrayList<Task>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tasks where deleted<>1 and day=? and done=0", new String[] {Integer.toString(selected_day)});
        if(cursor.getCount() != 0){
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String body = cursor.getString(2);
                int day = cursor.getInt(3);
                String created_time = cursor.getString(4);
                String due_time = cursor.getString(5);
                String alarm_time = cursor.getString(6);
                int done = cursor.getInt(7);
                int deleted = cursor.getInt(8);

                Task todo_task = new Task(id, title, body, day, created_time, due_time, alarm_time, done, deleted);

                todo_tasks.add(todo_task);
            }
        }

        cursor.close();
        return todo_tasks;
    }

    public void editTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("delete from tasks where id=?", new String[]{Integer.toString(task.getId())});
        cursor.moveToFirst();
        cursor.close();

        addTodoTask(task);
    }

    public Task getTask(int tid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tasks where id=?", new String[]{Integer.toString(tid)});
        cursor.moveToFirst();
        if (cursor.getCount() != 0)
        {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String body = cursor.getString(2);
            int day = cursor.getInt(3);
            String created_time = cursor.getString(4);
            String due_time = cursor.getString(5);
            String alarm_time = cursor.getString(6);
            int done = cursor.getInt(7);
            int deleted = cursor.getInt(8);

            Task task = new Task(id, title, body, day, created_time, due_time, alarm_time, done, deleted);
            cursor.close();
            return task;
        }

        return null;
    }

    public int getLastIdInserted() {

        int id = -1;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tasks order by id desc limit ?", new String[]{Integer.toString(1)});
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            id = cursor.getInt(0);
        }
        cursor.close();

        return id;
    }

    public ArrayList<Task> getAllTasks(){
        ArrayList<Task> todo_tasks = new ArrayList<Task>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tasks", null);
        if(cursor.getCount() != 0){
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String body = cursor.getString(2);
                int day = cursor.getInt(3);
                String created_time = cursor.getString(4);
                String due_time = cursor.getString(5);
                String alarm_time = cursor.getString(6);
                int done = cursor.getInt(7);
                int deleted = cursor.getInt(8);

                Task todo_task = new Task(id, title, body, day, created_time, due_time, alarm_time, done, deleted);

                todo_tasks.add(todo_task);
            }
        }

        cursor.close();
        return todo_tasks;
    }

    public boolean addDoneTask(Task todo_task){

        int id = todo_task.getId();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("update tasks set done=1 where id = ?", new String[]{Integer.toString(id)});
        cursor.moveToFirst();
        cursor.close();

        return true;
    }

    public boolean restoreDoneTask(Task task) {
        int id = task.getId();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("update tasks set done=0 where id = ?", new String[]{Integer.toString(id)});
        cursor.moveToFirst();
        cursor.close();

        return true;
    }

    public ArrayList<Task> getDoneTasks(int selected_day){
        ArrayList<Task> done_tasks = new ArrayList<Task>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tasks where deleted<>1 and day=? and done=1", new String[] {Integer.toString(selected_day)});
        if(cursor.getCount() != 0){
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String body = cursor.getString(2);
                int day = cursor.getInt(3);
                String created_time = cursor.getString(4);
                String due_time = cursor.getString(5);
                String alarm_time = cursor.getString(6);
                int done = cursor.getInt(7);
                int deleted = cursor.getInt(8);

                Task done_task = new Task(id, title, body, day, created_time, due_time, alarm_time, done, deleted);

                done_tasks.add(done_task);
            }
        }

        cursor.close();
        return done_tasks;
    }

    public ArrayList<Task> getOverdueTasks(int selected_day){
        ArrayList<Task> overdue_tasks = new ArrayList<Task>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tasks where deleted<>1 and day=? and done=0", new String[] {Integer.toString(selected_day)});
        if(cursor.getCount() != 0){
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String body = cursor.getString(2);
                int day = cursor.getInt(3);
                String created_time = cursor.getString(4);
                String due_time = cursor.getString(5);
                String alarm_time = cursor.getString(6);
                int done = cursor.getInt(7);
                int deleted = cursor.getInt(8);

                Task overdue_task = new Task(id, title, body, day, created_time, due_time, alarm_time, done, deleted);

                overdue_tasks.add(overdue_task);
            }
        }

        cursor.close();
        return overdue_tasks;
    }

    public boolean deleteTask(Task task){

        int id = task.getId();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("update tasks set deleted=1 where id = ?", new String[]{Integer.toString(id)});
        cursor.moveToFirst();
        cursor.close();

        return true;
    }

    public ArrayList<Task> getDeletedTasks(){
        ArrayList<Task> deleted_tasks = new ArrayList<Task>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tasks where deleted==1", null);
        if(cursor.getCount() != 0){
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String body = cursor.getString(2);
                int day = cursor.getInt(3);
                String created_time = cursor.getString(4);
                String due_time = cursor.getString(5);
                String alarm_time = cursor.getString(6);
                int done = cursor.getInt(7);
                int deleted = cursor.getInt(8);

                Task deleted_task = new Task(id, title, body, day, created_time, due_time, alarm_time, done, deleted);

                deleted_tasks.add(deleted_task);
            }
        }

        cursor.close();
        return deleted_tasks;
    }

    public boolean restoreDeletedTask(Task task){
        int id = task.getId();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("update tasks set deleted=0 where id = ?", new String[]{Integer.toString(id)});
        cursor.moveToFirst();
        cursor.close();

        return true;
    }

    public boolean clearAllDeletedTasks() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("delete from tasks where deleted=?", new String[]{Integer.toString(1)});
        cursor.moveToFirst();
        cursor.close();

        return true;
    }

    public boolean setAlarm(Task task, String alarm){
        int id = task.getId();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("update tasks set alarm_time=? where id = ?", new String[]{alarm, Integer.toString(id)});
        cursor.moveToFirst();
        cursor.close();

        return true;
    }

    public boolean deleteAlarm(Task task){
        int id = task.getId();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("update tasks set alarm_time=NULL where id = ?", new String[]{Integer.toString(id)});
        cursor.moveToFirst();
        cursor.close();

        return true;
    }

}
