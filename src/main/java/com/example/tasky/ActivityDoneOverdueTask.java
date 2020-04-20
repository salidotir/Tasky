package com.example.tasky;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityDoneOverdueTask extends AppCompatActivity {

    DBHelper dbHelper;

    Button btn_back_task_info;
    EditText edit_text_task_title;
    EditText edit_text_task_body;
    TextView text_view_task_due_time;
    TextView text_view_task_alarm_time;
    TextView text_view_created_time;
    TextView text_view_done;

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

}
