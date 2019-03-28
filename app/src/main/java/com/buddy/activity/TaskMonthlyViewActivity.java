package com.buddy.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.widget.CalendarView;
import android.widget.Toast;

import com.buddy.adapters.TaskListAdapter;
import com.buddy.entity.Task;
import com.buddy.main.R;
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
        Date today = new Date();
        try {
            today = formatter.parse(formatter.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24) - 1);

        List<Task> tasks = taskViewModel.findTasksBetweenDate(today, tomorrow);
        taskListAdapter.submitList(tasks);
        taskListAdapter.notifyDataSetChanged();

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
    }

    /*
     * This method called to edit clicked task
     */
    @Override
    public void onItemClick(Task task) {
        Toast.makeText(this, "Item clicked", Toast.LENGTH_LONG).show();
    }
}
