package com.example.tasky;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TaskActivity extends AppCompatActivity {

    DBHelper dbHelper;

    Button btn_back_task_info;
    Button btn_save_task_info;
    EditText edit_text_task_title;
    EditText edit_text_task_body;
    TextView text_view_task_due_time;
    TextView text_view_task_alarm_time;
    TextView text_view_created_time;
    TextView text_view_done;

    String AM_PM;
    int mHour, mMin;

    int id = 0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        dbHelper = new DBHelper(this);

        edit_text_task_title = findViewById(R.id.edit_text_task_title);

        edit_text_task_body = findViewById(R.id.edit_text_task_body);

        text_view_task_due_time = findViewById(R.id.text_view_task_due_time);

        text_view_task_alarm_time = findViewById(R.id.text_view_task_alarm_time);

        text_view_created_time = findViewById(R.id.text_view_created_time);

        text_view_done = findViewById(R.id.text_view_done);

        Intent intent = getIntent();
        if(intent.hasExtra("id")){
            id = intent.getIntExtra("id", 0);
        }

        fillTaskInformation(id);

        btn_back_task_info = findViewById(R.id.btn_back_task_info);
        btn_back_task_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_save_task_info = findViewById(R.id.btn_save_task_info);
        btn_save_task_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = dbHelper.getTask(id);

                // save new data
                int id = task.getId();
                String title = edit_text_task_title.getText().toString();
                String body = edit_text_task_body.getText().toString();
                int day = task.getDay();
                String created_time = task.getCreated_time();
                String due_time = text_view_task_due_time.getText().toString();

                String alarm_time;
                String tmp = text_view_task_alarm_time.getText().toString();
                if(tmp.isEmpty()){
                    alarm_time = null;
                }
                else{
                    alarm_time = tmp;

                    Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                    i.putExtra(AlarmClock.EXTRA_MESSAGE, task.getBody());
                    i.putExtra(AlarmClock.EXTRA_HOUR, mHour);
                    i.putExtra(AlarmClock.EXTRA_MINUTES, mMin);
                    startActivity(i);
                }

                int done = task.getDone();

                int deleted = task.getDeleted();

                Task newTask = new Task(id, title, body, day, created_time, due_time, alarm_time, done, deleted);

                dbHelper.editTask(newTask);

                int t = 0;
                for(int i = 0; i < TodayFragment.todo_tasks.size()-1; i++){
                    if(task.equals(TodayFragment.todo_tasks.get(i))){
                        t = i;
                    }
                }

                TodayFragment.todo_tasks.remove(t);
                TodayFragment.todo_tasks.add(newTask);
                TodayFragment.todoTaskAdapter.notifyDataSetChanged();

                finish();
            }
        });
    }

    public void fillTaskInformation(int id){
        Task task = dbHelper.getTask(id);

        edit_text_task_title.setText(task.getTitle());

        edit_text_task_body.setText(task.getBody());

        text_view_task_due_time.setText(task.getDue_time());

        String alarm_time = task.getAlarm_time();
        if(alarm_time ==  null){
            text_view_task_alarm_time.setText("No Alarm set.");
        }
        else{
            text_view_task_alarm_time.setText(alarm_time);
        }

        text_view_created_time.setText(task.getCreated_time());

        int done_int = task.getDone();

        if(done_int == 0){
            text_view_done.setText("NO");
        }
        else if(done_int == 1){
            text_view_done.setText("YES");
        }
    }

    public void TaskDueTimeOnClick(View v) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(TaskActivity.this,
                R.style.TimePickerTheme,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                            mHour = mHour - 12;
                        }

                        mHour = hourOfDay;
                        mMin = minute;

                        text_view_task_due_time.setText(mHour + ":" + mMin + " " + AM_PM);
                        Toast.makeText(TaskActivity.this, "Alarm set for " + mHour + ":" + mMin + " " + AM_PM + ".", Toast.LENGTH_SHORT).show();
                    }
                }, mHour, mMin, false);

        timePickerDialog.show();
    }

    public void TaskAlarmTimeOnClick(View v) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(TaskActivity.this,
                R.style.TimePickerTheme,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                            mHour = mHour - 12;
                        }

                        mHour = hourOfDay;
                        mMin = minute;

                        text_view_task_alarm_time.setText(mHour + ":" + mMin + " " + AM_PM);

                        Toast.makeText(TaskActivity.this, "Alarm set for " + mHour + ":" + mMin + " " + AM_PM + ".", Toast.LENGTH_SHORT).show();
                    }
                }, mHour, mMin, false);

        timePickerDialog.show();
    }
}
