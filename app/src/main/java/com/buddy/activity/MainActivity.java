package com.buddy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.buddy.adapters.TaskListLayoutAdapter;
import com.buddy.entity.Task;
import com.buddy.main.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mTaskListRecycleView;
    private RecyclerView.Adapter mTaskListAdapter;
    private RecyclerView.LayoutManager mTaskListLayoutManager;
    private List<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add toolbar
        Toolbar mainToolBar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolBar);

        mTaskListRecycleView = (RecyclerView) findViewById(R.id.my_task_list);

        // use a LinearLayout Manager
        mTaskListLayoutManager = new LinearLayoutManager(this);
        mTaskListRecycleView.setLayoutManager(mTaskListLayoutManager);
        mTaskListRecycleView.setItemAnimator(new DefaultItemAnimator());

        // add divider
        mTaskListRecycleView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // specify an adapter
        mTaskListAdapter = new TaskListLayoutAdapter(taskList);
        mTaskListRecycleView.setAdapter(mTaskListAdapter);

        prepareTaskData();
    }

    private void prepareTaskData() {
        // Task task1 = new Task("Meet the Prof", "Discuss the assignment");
        // taskList.add(task1);

        Task task2 = new Task("Interview with Google", "Algorithms");
        taskList.add(task2);

        Task task3 = new Task("Interview with IBM", "System Design");
        taskList.add(task3);

        Task task4 = new Task("Interview with SAP", "Agile PM");
        taskList.add(task4);

        mTaskListAdapter.notifyDataSetChanged();
    }
}
