package com.example.tasky;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddNewTaskActivity extends AppCompatActivity {
    private String title;
    private String text;

    Button btn_cancel_add_new_task;
    Button btn_ok_add_new_task;

    EditText edit_text_text;
    EditText edit_text_title;

    TextView text_view_text;
    TextView text_view_title;
    TextView text_view_due_time_tmp;

    TextView text_view_due_time;
    int mHour, mMin;
    String AM_PM;

    AlphaAnimation buttonClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        btn_cancel_add_new_task = findViewById(R.id.btn_cancel_add_new_task);
        btn_cancel_add_new_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_cancel_add_new_task.startAnimation(buttonClick);

                Intent back_to_frag_activity_intent = new Intent(getApplicationContext(), TodayFragment.class);

                setResult(RESULT_CANCELED);

                // back to last activity
                finish();
            }
        });

        btn_ok_add_new_task = findViewById(R.id.btn_ok_add_new_task);
        btn_ok_add_new_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_ok_add_new_task.startAnimation(buttonClick);
                String title = edit_text_title.getText().toString();
                String body = edit_text_text.getText().toString();
                String due_time = text_view_due_time.getText().toString();

                if((!title.toString().equals("")) && (!body.toString().equals("")) && (!due_time.toString().equals(""))){
                    // pass title and text to the last activity
                    String created_time = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                    Intent result_intent = new Intent();
                    result_intent.putExtra("title", title);
                    result_intent.putExtra("body", body);
                    result_intent.putExtra("created_time", created_time);
                    result_intent.putExtra("due_time", due_time);

                    Task todoTask = new Task(title, body, MainActivity.day_week, created_time, due_time, null, 0, 0);


                    if(getIntent().hasExtra("src")){
                        String source = getIntent().getExtras().getString("src");

                        if(source.equals("TodayFragment")){
                            setResult(RESULT_OK, result_intent);
                            finish();
                        }

                        else if(source.equals("NextDayFragment")){
                            setResult(RESULT_OK, result_intent);
                            finish();
                        }
                    }
                }

                else{
                    Toast.makeText(getApplicationContext(), "You must fill all title, text and due time fields!", Toast.LENGTH_SHORT).show();

                    text_view_text.setText("*Text:");
                    text_view_title.setText("*Title:");
                    text_view_due_time_tmp.setText("*Due time:");

                }
            }
        });

        edit_text_text = (EditText) findViewById(R.id.edit_text_text);
        edit_text_title = (EditText) findViewById(R.id.edit_text_title);

        text_view_text = (TextView) findViewById(R.id.text_view_text);
        text_view_title = (TextView) findViewById(R.id.text_view_title);
        text_view_due_time = (TextView) findViewById(R.id.text_view_due_time);
        text_view_due_time_tmp = (TextView) findViewById(R.id.text_view_due_time_tmp);

        // button click animation
        buttonClick = new AlphaAnimation(1F, 0.8F);
    }

    public void DueTimeOnClick(View v) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddNewTaskActivity.this,
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

                text_view_due_time.setText(mHour + ":" + mMin + " " + AM_PM);
            }
        }, mHour, mMin, false);

        timePickerDialog.show();
    }
}
