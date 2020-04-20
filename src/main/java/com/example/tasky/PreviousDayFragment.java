package com.example.tasky;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PreviousDayFragment extends Fragment {
    DBHelper dbHelper;

    private MainActivity mainActivity;

    private RecyclerView previous_day_done_frag_recyclerview;
    private ArrayList<Task> done_tasks;
    private DoneTaskAdapter doneTaskAdapter;
    private LinearLayoutManager done_llm;

    ImageView previous_day_frag_done_no_data_img_view;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private RecyclerView previous_day_overdue_frag_recyclerview;
    private ArrayList<Task> overdue_tasks;
    private OverdueTaskAdapter overdueTaskAdapter;
    private LinearLayoutManager overdue_llm;

    ImageView previous_day_frag_overdue_no_data_img_view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.previous_day_fragment, container, false);

        dbHelper = new DBHelper(getActivity());

        done_tasks = new ArrayList<Task>();
        // load previous day done tasks from data base
        done_tasks = dbHelper.getDoneTasks(mainActivity.day_week);

//        done_tasks.add(new Task("Mom", "Call mom!", 3, "03:50", "03:50", null, 1, 0));
//        done_tasks.add(new Task("Android", "HW3 android", 3, "03:50", "03:50", null, 1, 0));
//        done_tasks.add(new Task("Party", "Go to party!", 3, "03:50", "03:50", null, 1, 0));
//        done_tasks.add(new Task("Doctor", "go to doctor.", 3, "03:50", "03:50", null, 1, 0));
//        done_tasks.add(new Task("Quiz OS", "Remember Os quiz.", 3, "03:50", "03:50", null, 1, 0));

        previous_day_frag_done_no_data_img_view = view.findViewById(R.id.previous_day_frag_done_no_data_img_view);
        previous_day_done_frag_recyclerview = view.findViewById(R.id.previous_day_done_frag_recyclerview);
        doneTaskAdapter = new DoneTaskAdapter(done_tasks, mainActivity);
        done_llm = new LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false);

        previous_day_done_frag_recyclerview.setLayoutManager(done_llm);
        previous_day_done_frag_recyclerview.setAdapter(doneTaskAdapter);

        if(done_tasks.size() <= 0) {
            previous_day_frag_done_no_data_img_view.setVisibility(View.VISIBLE);
            previous_day_done_frag_recyclerview.setVisibility(View.GONE);
        }
        else if(done_tasks.size() > 0) {
            previous_day_frag_done_no_data_img_view.setVisibility(View.GONE);
            previous_day_done_frag_recyclerview.setVisibility(View.VISIBLE);
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        overdue_tasks = new ArrayList<Task>();
        // load previous day done tasks from data base
        overdue_tasks = dbHelper.getOverdueTasks(mainActivity.day_week);

//        overdue_tasks.add(new Task("Mom", "Call mom!", 3, "03:50", "03:50", null, 0, 0));
//        overdue_tasks.add(new Task("Android", "HW3 android", 3, "03:50", "03:50", null, 0, 0));
//        overdue_tasks.add(new Task("Party", "Go to party!", 3, "03:50", "03:50", null, 0, 0));
//        overdue_tasks.add(new Task("Doctor", "go to doctor.", 3, "03:50", "03:50", null, 0, 0));
//        overdue_tasks.add(new Task("Quiz OS", "Remember Os quiz.", 3, "03:50", "03:50", null, 0, 0));

        previous_day_frag_overdue_no_data_img_view = view.findViewById(R.id.previous_day_frag_overdue_no_data_img_view);
        previous_day_overdue_frag_recyclerview = view.findViewById(R.id.previous_day_overdue_frag_recyclerview);
        overdueTaskAdapter = new OverdueTaskAdapter(overdue_tasks, mainActivity);
        overdue_llm = new LinearLayoutManager(mainActivity, LinearLayoutManager.VERTICAL, false);
        previous_day_overdue_frag_recyclerview.setNestedScrollingEnabled(false);

        previous_day_overdue_frag_recyclerview.setLayoutManager(overdue_llm);
        previous_day_overdue_frag_recyclerview.setAdapter(overdueTaskAdapter);

        if(overdue_tasks.size() <= 0) {
            previous_day_frag_overdue_no_data_img_view.setVisibility(View.VISIBLE);
            previous_day_overdue_frag_recyclerview.setVisibility(View.GONE);
        }
        else if(overdue_tasks.size() > 0) {
            previous_day_frag_overdue_no_data_img_view.setVisibility(View.GONE);
            previous_day_overdue_frag_recyclerview.setVisibility(View.VISIBLE);
        }

        return view;
    }
}
