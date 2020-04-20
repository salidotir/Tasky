package com.example.tasky;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoTaskAdapter extends RecyclerView.Adapter<TodoTaskAdapter.MyTaskViewHolder> {
    static List<Task> tasks;
    MainActivity mainActivity;

    OnTaskListener onTaskListener;

    public TodoTaskAdapter(List<Task> myTasks, MainActivity mainActivity, OnTaskListener onTaskListener) {
        this.tasks = myTasks;
        this.mainActivity = mainActivity;
        this.onTaskListener = onTaskListener;
    }

    @Override
    public TodoTaskAdapter.MyTaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_task, viewGroup, false);
        MyTaskViewHolder pvh = new MyTaskViewHolder(v, onTaskListener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final MyTaskViewHolder viewHolder, final int position) {
        Task task = tasks.get(position);
        if(task.getAlarm_time()!= null){
            viewHolder.alarm_background.setBackgroundResource(R.drawable.yellow_circle);
            viewHolder.btn_add_alarm.setVisibility(View.GONE);
            viewHolder.alarm_text_view.setText(task.getAlarm_time());
            viewHolder.alarm_text_view.setVisibility(View.VISIBLE);
        }

        else if(task.getAlarm_time() == null) {
            viewHolder.alarm_background.setBackgroundResource(R.drawable.round_white_rectangle);
            viewHolder.btn_add_alarm.setVisibility(View.VISIBLE);
            viewHolder.alarm_text_view.setVisibility(View.GONE);
            viewHolder.alarm_text_view.setText("null");
        }

        viewHolder.task_todo_title.setText(task.getTitle());
        viewHolder.task_todo_body.setText(task.getBody());
        viewHolder.task_todo_time.setText(task.getDue_time());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class MyTaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout alarm_background;
        TextView task_todo_title;
        TextView task_todo_body;
        TextView task_todo_time;
        ImageButton btn_add_alarm;
        ImageButton btn_sent_to_next_day;
        TextView alarm_text_view;

        ImageView todo_task_info;

        OnTaskListener onTaskListener;

        MyTaskViewHolder(View itemView, OnTaskListener onTaskListener) {
            super(itemView);

            task_todo_title = itemView.findViewById(R.id.task_todo_title);
            task_todo_body = itemView.findViewById(R.id.task_todo_body);
            task_todo_time = itemView.findViewById(R.id.task_todo_time);
            btn_add_alarm = itemView.findViewById(R.id.btn_add_alarm);
            todo_task_info = itemView.findViewById(R.id.todo_task_info);
            alarm_text_view = itemView.findViewById(R.id.alarm_text_view);
            alarm_background = itemView.findViewById(R.id.alarm_background);
            btn_sent_to_next_day = itemView.findViewById(R.id.btn_sent_to_next_day);
            this.onTaskListener = onTaskListener;

            btn_sent_to_next_day.setClickable(true);
            btn_sent_to_next_day.setOnClickListener(this);

            todo_task_info.setClickable(true);
            todo_task_info.setOnClickListener(this);

            btn_add_alarm.setClickable(true);
            btn_add_alarm.setOnClickListener(this);

            alarm_text_view.setClickable(true);
            alarm_text_view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_add_alarm:
                    onTaskListener.onAddAlarmBtnClick(TodoTaskAdapter.tasks, getAdapterPosition());
                    break;

                case R.id.alarm_text_view:
                    onTaskListener.onRemoveAlarmBtnClick(TodoTaskAdapter.tasks, getAdapterPosition());
                    break;

                case R.id.todo_task_info:
                    onTaskListener.onTodoTaskClick(TodoTaskAdapter.tasks, getAdapterPosition());
                    break;

                case R.id.btn_sent_to_next_day:
                    onTaskListener.onSend2NextClick(TodoTaskAdapter.tasks, getAdapterPosition());
                    break;

                default:
                    break;

            }
        }
    }

    public interface OnTaskListener {
        void onAddAlarmBtnClick(List<Task> tasks, int position);

        void onRemoveAlarmBtnClick(List<Task> tasks, int position);

        void onTodoTaskClick(List<Task> tasks, int position);

        void onSend2NextClick(List<Task> tasks, int position);
    }

}
