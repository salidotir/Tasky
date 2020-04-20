package com.example.tasky;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeletedTaskActivity extends AppCompatActivity {

    DBHelper dbHelper;

    Button btn_clear_history;
    Button btn_back_history;

    private RecyclerView deleted_tasks_recycler_view;
    public static ArrayList<Task> deleted_tasks;
    private DeletedTaskAdapter deletedTaskAdapter;
    private LinearLayoutManager deleted_llm;

    ImageView deleted_tasks_activity_data_img_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleted_tasks);

        dbHelper = new DBHelper(DeletedTaskActivity.this);

        deleted_tasks = new ArrayList<Task>();

        // load deleted tasks from data base
        deleted_tasks = dbHelper.getDeletedTasks();

        btn_clear_history = findViewById(R.id.btn_clear_history);
        btn_clear_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete all deleted tasks from database
                dbHelper.clearAllDeletedTasks();

                deleted_tasks.clear();

                // to notify recycler view that sth has been added to the list
                deletedTaskAdapter.notifyDataSetChanged();
            }
        });

        btn_back_history = findViewById(R.id.btn_back_history);
        btn_back_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleted_tasks_activity_data_img_view = findViewById(R.id.deleted_tasks_activity_data_img_view);
        deleted_tasks_recycler_view = findViewById(R.id.deleted_tasks_recycler_view);
        deletedTaskAdapter = new DeletedTaskAdapter(deleted_tasks, DeletedTaskActivity.this);
        deleted_llm = new LinearLayoutManager(DeletedTaskActivity.this, LinearLayoutManager.VERTICAL, false);

        deleted_tasks_recycler_view.setNestedScrollingEnabled(false);
        deleted_tasks_recycler_view.setLayoutManager(deleted_llm);
        deleted_tasks_recycler_view.setAdapter(deletedTaskAdapter);

        new ItemTouchHelper(deletedItemTouchHelperCallBackRight).attachToRecyclerView(deleted_tasks_recycler_view);

        if(deleted_tasks.size() > 0) {
            deleted_tasks_activity_data_img_view.setVisibility(View.GONE);
            deleted_tasks_recycler_view.setVisibility(View.VISIBLE);
        }
        else if(deleted_tasks.size() <= 0) {
            deleted_tasks_activity_data_img_view.setVisibility(View.VISIBLE);
            deleted_tasks_recycler_view.setVisibility(View.GONE);
        }

    }


    ItemTouchHelper.SimpleCallback deletedItemTouchHelperCallBackRight = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // set done bit of this task in data base to 1.
            int position = viewHolder.getAdapterPosition();

            dbHelper.restoreDeletedTask(deleted_tasks.get(position));
            deleted_tasks.remove(position);

            // to notify recycler view that sth has been added to the list
            deletedTaskAdapter.notifyItemRemoved(position);
        }
    };

}
