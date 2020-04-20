package com.example.tasky;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoneTaskAdapter extends RecyclerView.Adapter<DoneTaskAdapter.MyViewHolder> {

    List<Task> tasks;
    MainActivity mainActivity;

    public DoneTaskAdapter(List<Task> myTasks, MainActivity mainActivity) {
        this.tasks = myTasks;
        this.mainActivity = mainActivity;
    }

    @Override
    public DoneTaskAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.done_task, parent, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Task done_task = tasks.get(position);
        holder.title.setText(done_task.getTitle());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.task_done_title);
        }

    }

}
