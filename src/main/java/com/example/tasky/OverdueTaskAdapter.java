package com.example.tasky;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OverdueTaskAdapter extends RecyclerView.Adapter<OverdueTaskAdapter.MyViewHolder> {

    List<Task> tasks;
    MainActivity mainActivity;

    public OverdueTaskAdapter(List<Task> myTasks, MainActivity mainActivity) {
        this.tasks = myTasks;
        this.mainActivity = mainActivity;
    }

    @Override
    public OverdueTaskAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.overdue_task, parent, false);
        OverdueTaskAdapter.MyViewHolder mvh = new OverdueTaskAdapter.MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final OverdueTaskAdapter.MyViewHolder holder, final int position) {
        Task overdue_task = tasks.get(position);
        holder.title.setText(overdue_task.getTitle());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.task_overdue_title);
        }
    }

}
