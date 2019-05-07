package com.buddy.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.buddy.adapters.TaskListAdapter;
import com.buddy.entity.Task;
import com.buddy.main.R;
import com.buddy.util.Constants;
import com.buddy.viewmodel.TaskViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TaskMonthlyViewActivity extends AppCompatActivity implements TaskListAdapter.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_monthly_view);

        RecyclerView taskListView = findViewById(R.id.task_list_view);
        RecyclerView.LayoutManager taskListLayoutManager = new LinearLayoutManager(this);
        taskListView.setLayoutManager(taskListLayoutManager);
        taskListView.addItemDecoration(new DividerItemDecoration(
                this, LinearLayoutManager.VERTICAL));
        final TaskListAdapter taskListAdapter = new TaskListAdapter();
        taskListView.setAdapter(taskListAdapter);

        CalendarView calendarView = findViewById(R.id.calendarView);

        // Set today as a default selected day
        final Calendar cal = Calendar.getInstance();
        calendarView.setDate(cal.getTimeInMillis());
        final TaskViewModel taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        // Find tasks between today and tomorrow
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter
                = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date mToday = formatter.parse(formatter.format(new Date()));
            Date mTomorrow = new Date(mToday.getTime() + (1000 * 60 * 60 * 24) - 1);

            List<Task> tasks = taskViewModel.findTasksBetweenDate(mToday, mTomorrow);
            taskListAdapter.submitList(tasks);
            taskListAdapter.notifyDataSetChanged();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                cal.set(year, month, dayOfMonth);
                List<Task> tasks = taskViewModel.findTasksBetweenDate(cal.getTime(),
                        new Date(cal.getTime().getTime() + (1000 * 60 * 60 * 24) - 1));
                taskListAdapter.submitList(tasks);
                taskListAdapter.notifyDataSetChanged();
            }
        });

        getSupportActionBar().setTitle("Month");

        FloatingActionButton btnNewTask = findViewById(R.id.btn_new_task);
        btnNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newTaskIntent = new Intent(getApplicationContext(), TaskNewEditActivity.class);
                startActivityForResult(newTaskIntent, Constants.NEW_TASK_REQUEST);
            }
        });
    }

    /*
     * This method called to edit clicked task
     */
    @Override
    public void onItemClick(Task task) {
        Toast.makeText(this, "Item clicked", Toast.LENGTH_LONG).show();
    }
}
