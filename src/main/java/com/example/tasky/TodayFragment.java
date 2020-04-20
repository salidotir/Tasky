package com.example.tasky;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import java.util.List;

public class TodayFragment extends Fragment implements TodoTaskAdapter.OnTaskListener {
    DBHelper dbHelper;

    private MainActivity mainActivity;

    private RecyclerView today_todo_frag_recyclerview;
    public static ArrayList<Task> todo_tasks;
    public static TodoTaskAdapter todoTaskAdapter;
    private LinearLayoutManager todo_llm;

    ImageView today_frag_to_do_no_data_img_view;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private RecyclerView today_done_frag_recyclerview;
    public static ArrayList<Task> done_tasks;
    private DoneTaskAdapter doneTaskAdapter;
    private LinearLayoutManager done_llm;

    ImageView today_frag_done_no_data_img_view;

    ImageButton imgBtnAdd;

    int RESULT_CACELED = 0;
    int RESULT_OK = -1;

    int request_code = 1;

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

        Toast.makeText(getContext(), "Alarm at " + alarm + " removed successfully.", Toast.LENGTH_SHORT).show();

        // to notify recycler view that sth has been added to the list
        todoTaskAdapter.notifyItemChanged(position);
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
                            mHour = mHour - 12;
                        }
                        mHour = hourOfDay;
                        mMin = minute;

                        alarm = mHour + ":" + mMin + " " + AM_PM;

                        // add to data base
                        dbHelper.setAlarm(inp_tasks.get(position), alarm);

                        todo_tasks.get(position).setAlarm_time(alarm);

                        // to notify recycler view that sth has been added to the list
                        todoTaskAdapter.notifyItemChanged(position);

                        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                        i.putExtra(AlarmClock.EXTRA_MESSAGE, inp_tasks.get(position).getBody());
                        i.putExtra(AlarmClock.EXTRA_HOUR, mHour);
                        i.putExtra(AlarmClock.EXTRA_MINUTES, mMin);
                        startActivity(i);

                        Toast.makeText(getContext(), "Alarm set for " + alarm + ".", Toast.LENGTH_SHORT).show();
                    }
                }, mHour, mMin, false);

        timePickerDialog.show();
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
                }
                else{
                    Toast.makeText(getContext(), "Could not add task!", Toast.LENGTH_SHORT).show();
                }
            }

            if(todo_tasks.size() > 0) {
                today_frag_to_do_no_data_img_view.setVisibility(View.GONE);
                today_todo_frag_recyclerview.setVisibility(View.VISIBLE);
            }
            else if(todo_tasks.size() <= 0) {
                today_frag_to_do_no_data_img_view.setVisibility(View.VISIBLE);
                today_todo_frag_recyclerview.setVisibility(View.GONE);
            }
        }

        // to notify recycler view that sth has been added to the list
        todoTaskAdapter.notifyItemInserted(todo_tasks.size() - 1);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.today_fragment, container, false);

        dbHelper = new DBHelper(getActivity());

        todo_tasks = new ArrayList<Task>();

        // load today to_do tasks from data base
        todo_tasks = dbHelper.getTodoTasks(MainActivity.day_week);

        imgBtnAdd = view.findViewById(R.id.add_new_task_today_frag_btn);
        imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity_add_new_task_intent = new Intent(getContext(), AddNewTaskActivity.class);
                activity_add_new_task_intent.putExtra("src", "TodayFragment");
                startActivityForResult(activity_add_new_task_intent, request_code);
            }
        });

        mainActivity = (MainActivity) getActivity();

        today_frag_to_do_no_data_img_view = view.findViewById(R.id.today_frag_to_do_no_data_img_view);
        today_todo_frag_recyclerview = view.findViewById(R.id.today_todo_frag_recyclerview);
        todoTaskAdapter = new TodoTaskAdapter(todo_tasks, mainActivity, this);
        todo_llm = new LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false);

        today_todo_frag_recyclerview.setNestedScrollingEnabled(false);
        today_todo_frag_recyclerview.setLayoutManager(todo_llm);
        today_todo_frag_recyclerview.setAdapter(todoTaskAdapter);
        new ItemTouchHelper(todoItemTouchHelperCallBackLeft).attachToRecyclerView(today_todo_frag_recyclerview);
        new ItemTouchHelper(todoItemTouchHelperCallBackRight).attachToRecyclerView(today_todo_frag_recyclerview);

        if(todo_tasks.size() > 0) {
            today_frag_to_do_no_data_img_view.setVisibility(View.GONE);
            today_todo_frag_recyclerview.setVisibility(View.VISIBLE);
        }
        else if(todo_tasks.size() <= 0) {
            today_frag_to_do_no_data_img_view.setVisibility(View.VISIBLE);
            today_todo_frag_recyclerview.setVisibility(View.GONE);
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        done_tasks = new ArrayList<Task>();

        // load today done tasks from data base
        done_tasks = dbHelper.getDoneTasks(mainActivity.day_week);

        today_frag_done_no_data_img_view = view.findViewById(R.id.today_frag_done_no_data_img_view);
        today_done_frag_recyclerview = view.findViewById(R.id.today_done_frag_recyclerview);
        doneTaskAdapter = new DoneTaskAdapter(done_tasks, mainActivity);
        done_llm = new LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false);

        today_done_frag_recyclerview.setNestedScrollingEnabled(false);
        today_done_frag_recyclerview.setLayoutManager(done_llm);
        today_done_frag_recyclerview.setAdapter(doneTaskAdapter);

        if(done_tasks.size() <= 0) {
            today_frag_done_no_data_img_view.setVisibility(View.VISIBLE);
            today_done_frag_recyclerview.setVisibility(View.GONE);
        }
        else if(done_tasks.size() > 0) {
            today_frag_done_no_data_img_view.setVisibility(View.GONE);
            today_done_frag_recyclerview.setVisibility(View.VISIBLE);
        }

        new ItemTouchHelper(doneItemTouchHelperCallBackRight).attachToRecyclerView(today_done_frag_recyclerview);

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

    ItemTouchHelper.SimpleCallback todoItemTouchHelperCallBackRight = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // set done bit of this task in data base to 1.
            int position = viewHolder.getAdapterPosition();

            dbHelper.addDoneTask(todo_tasks.get(position));

            done_tasks.add(todo_tasks.get(position));
            todo_tasks.remove(position);

            // to notify recycler view that sth has been added to the list
            doneTaskAdapter.notifyItemInserted(done_tasks.size() - 1);
            todoTaskAdapter.notifyItemRemoved(position);
        }
    };

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    ItemTouchHelper.SimpleCallback doneItemTouchHelperCallBackRight = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // set done bit of this task in data base to 1.
            int position = viewHolder.getAdapterPosition();

            dbHelper.restoreDoneTask(done_tasks.get(position));

            todo_tasks.add(done_tasks.get(position));
            done_tasks.remove(position);

            // to notify recycler view that sth has been added to the list
            doneTaskAdapter.notifyItemRemoved(position);
            todoTaskAdapter.notifyItemInserted(todo_tasks.size() - 1);
        }
    };
}
