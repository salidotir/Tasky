package com.example.tasky;

public class Task {

    private int id;
    private String title;
    private String body;
    private int day;
    private String created_time;
    private String due_time;
    private String alarm_time;
    private int done;
    private int deleted;

    DBHelper dbHelper;

    public Task() {}

    public Task(int id, String title, String body, int day, String created_time, String due_time, String alarm_time, int done, int deleted) {
        this.setId(id);
        this.setTitle(title);
        this.setBody(body);
        this.setDay(day);
        this.setCreated_time(created_time);
        this.setDue_time(due_time);
        this.setAlarm_time(alarm_time);
        this.setDone(done);
        this.setDeleted(deleted);
    }

    public Task(String title, String body, int day, String created_time, String due_time, String alarm_time, int done, int deleted) {
        MainActivity.last_id = MainActivity.last_id + 1;

        this.setId(MainActivity.last_id);

        this.setTitle(title);
        this.setBody(body);
        this.setDay(day);
        this.setCreated_time(created_time);
        this.setDue_time(due_time);
        this.setAlarm_time(alarm_time);
        this.setDone(done);
        this.setDeleted(deleted);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getDue_time() {
        return due_time;
    }

    public void setDue_time(String due_time) {
        this.due_time = due_time;
    }

    public String getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getDone() {
        return done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}
