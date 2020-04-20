package com.example.tasky;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeletedTaskAdapter extends RecyclerView.Adapter<DeletedTaskAdapter.MyTaskViewHolder> {
    static List<Task> tasks;
    DeletedTaskActivity deletedTaskActivity;


    public DeletedTaskAdapter(List<Task> myTasks, DeletedTaskActivity deletedTaskActivity) {
        this.tasks = myTasks;
        this.deletedTaskActivity = deletedTaskActivity;
    }

    @Override
    public DeletedTaskAdapter.MyTaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.deleted_task, viewGroup, false);
        MyTaskViewHolder pvh = new MyTaskViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final DeletedTaskAdapter.MyTaskViewHolder viewHolder, final int position) {
        Task task = tasks.get(position);

        viewHolder.task_deleted_title.setText(task.getTitle());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class MyTaskViewHolder extends RecyclerView.ViewHolder {

        TextView task_deleted_title;

        MyTaskViewHolder(View itemView) {
            super(itemView);

            task_deleted_title = itemView.findViewById(R.id.task_deleted_title);

        }

    }
}
