package com.example.tasky;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NextDayFragment extends Fragment implements TodoTaskAdapter.OnTaskListener {
    DBHelper dbHelper;

    private MainActivity mainActivity;

    private RecyclerView next_day_todo_frag_recyclerview;
    public static ArrayList<Task> todo_tasks;
    private TodoTaskAdapter todoTaskAdapter;
    private LinearLayoutManager todo_llm;

    ImageButton imgBtnAdd;

    ImageView next_day_frag_to_do_no_data_img_view;

    int request_code = 2;

    int RESULT_CACELED = 0;
    int RESULT_OK = -1;

    int mHour, mMin;
    String AM_PM;
    String alarm;

    @Override
    public void onSend2NextClick(List<Task> inp_tasks, int position){
        dbHelper.sendTask2NextDay(inp_tasks.get(position));

        todo_tasks.remove(position);
        todoTaskAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onTodoTaskClick(List<Task> inp_tasks, int position){
        Intent taskInfo = new Intent(getContext(), TaskActivity.class);
        taskInfo.putExtra("id", todo_tasks.get(position).getId());
        startActivity(taskInfo);
    }

    @Override
    public void onRemoveAlarmBtnClick(List<Task> inp_tasks, int position){
        // remove alarm for this task from database
        dbHelper.deleteAlarm(inp_tasks.get(position));

        todo_tasks.get(position).setAlarm_time(null);

        // to notify recycler view that sth has been added to the list
        todoTaskAdapter.notifyItemChanged(position);

        Toast.makeText(getContext(), "Alarm at " + alarm + " removed successfully.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddAlarmBtnClick(final List<Task> inp_tasks, final int position) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                R.style.TimePickerTheme,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
                        mHour = hourOfDay;
                        mMin = minute;

                        alarm = mHour + ":" + mMin + " " + AM_PM;

                        // add to data base
                        dbHelper.setAlarm(inp_tasks.get(position), alarm);

                        Toast.makeText(getContext(), "Alarm set for " + alarm + ".", Toast.LENGTH_SHORT).show();
                    }
                }, mHour, mMin, false);

        timePickerDialog.show();

        todo_tasks.get(position).setAlarm_time(alarm);

        // to notify recycler view that sth has been added to the list
        todoTaskAdapter.notifyItemChanged(position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String title, body, created_time, due_time;
        if(requestCode == requestCode && resultCode == RESULT_OK) {
            if(data.hasExtra("title") && data.hasExtra("body") && data.hasExtra("created_time") && data.hasExtra("due_time")){
                title = data.getStringExtra("title");
                body = data.getStringExtra("body");
                created_time = data.getStringExtra("created_time");
                due_time = data.getStringExtra("due_time");

                Task todoTask = new Task(title, body, MainActivity.day_week, created_time, due_time, null, 0, 0);
                boolean res = todo_tasks.add(todoTask);
                if(res == true){
                    // add to data base
                    dbHelper.addTodoTask(todoTask);

                    // to notify recycler view that sth has been added to the list
                    todoTaskAdapter.notifyItemInserted(todo_tasks.size() - 1);
                }
                else{
                    Toast.makeText(getContext(), "Could not add task!", Toast.LENGTH_SHORT).show();
                }
            }

            if (requestCode == requestCode && resultCode == RESULT_OK) {
                if (todo_tasks.size() > 0) {
                    next_day_frag_to_do_no_data_img_view.setVisibility(View.GONE);
                    next_day_todo_frag_recyclerview.setVisibility(View.VISIBLE);
                } else if (todo_tasks.size() <= 0) {
                    next_day_frag_to_do_no_data_img_view.setVisibility(View.VISIBLE);
                    next_day_todo_frag_recyclerview.setVisibility(View.GONE);
                }
                // to notify recycler view that sth has been added to the list
                todoTaskAdapter.notifyItemInserted(todo_tasks.size() - 1);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.next_day_fragment, container, false);

        dbHelper = new DBHelper(getActivity());

        todo_tasks = new ArrayList<Task>();

        // load next day to_do tasks from data base
        todo_tasks = dbHelper.getTodoTasks(MainActivity.day_week);

        imgBtnAdd = view.findViewById(R.id.add_new_task_next_day_frag_btn);
        imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_add_new_task_intent = new Intent(getContext(), AddNewTaskActivity.class);
                activity_add_new_task_intent.putExtra("src", "NextDayFragment");
                startActivityForResult(activity_add_new_task_intent, request_code);
            }
        });

        mainActivity = (MainActivity) getActivity();

        next_day_frag_to_do_no_data_img_view = view.findViewById(R.id.next_day_frag_to_do_no_data_img_view);
        next_day_todo_frag_recyclerview = view.findViewById(R.id.next_day_todo_frag_recyclerview);
        todoTaskAdapter = new TodoTaskAdapter(todo_tasks, mainActivity, this);
        todo_llm = new LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false);

        next_day_todo_frag_recyclerview.setLayoutManager(todo_llm);
        next_day_todo_frag_recyclerview.setAdapter(todoTaskAdapter);
        new ItemTouchHelper(todoItemTouchHelperCallBackLeft).attachToRecyclerView(next_day_todo_frag_recyclerview);

        if(todo_tasks.size() > 0) {
            next_day_frag_to_do_no_data_img_view.setVisibility(View.GONE);
            next_day_todo_frag_recyclerview.setVisibility(View.VISIBLE);
        }
        else if(todo_tasks.size() <= 0) {
            next_day_frag_to_do_no_data_img_view.setVisibility(View.VISIBLE);
            next_day_todo_frag_recyclerview.setVisibility(View.GONE);
        }

        return view;
    }

    ItemTouchHelper.SimpleCallback todoItemTouchHelperCallBackLeft = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // set deleted bit of this task in data base to 1.
            int position = viewHolder.getAdapterPosition();

            dbHelper.deleteTask(todo_tasks.get(position));

            todo_tasks.remove(position);

            // to notify recycler view that sth has been added to the list
            todoTaskAdapter.notifyItemRemoved(position);
        }
    };

}
