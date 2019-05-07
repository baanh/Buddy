package com.buddy.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.buddy.adapters.TaskExpandableListAdapter;
import com.buddy.entity.Task;
import com.buddy.main.R;
import com.buddy.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskDailyViewActivity extends AppCompatActivity {

    private ExpandableListView taskListView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;
    private TaskViewModel taskViewModel;                                                                            

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_by_day);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                if (tasks != null) {
                    for (Task task : tasks) {
                        List<String> taskDetail = new ArrayList<>();
                        listDataHeader.add(task.getName());
                        taskDetail.add("Start: " + task.getStartDate().toString());
                        taskDetail.add("End: " + task.getEndDate().toString());
                        taskDetail.add("Description: " + task.getDescription());
                        listHash.put(listDataHeader.get(listDataHeader.size() - 1), taskDetail);
                    }
                }
            }
        });
        taskListView = findViewById(R.id.task_expandable_list_view);
        listAdapter = new TaskExpandableListAdapter(this, listDataHeader, listHash);
        taskListView.setAdapter(listAdapter);
    }
}
