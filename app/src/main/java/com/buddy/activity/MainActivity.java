package com.buddy.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.buddy.adapters.TaskListAdapter;
import com.buddy.entity.Task;
import com.buddy.main.R;
import com.buddy.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mTaskListRecycleView;

    private List<Task> taskList = new ArrayList<>();

    private TaskViewModel mTaskViewModel;

    private final int NEW_EDIT_TASK_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get recycleView to populate a list of tasks
        mTaskListRecycleView = (RecyclerView) findViewById(R.id.my_task_list);

        // use a LinearLayout Manager
        RecyclerView.LayoutManager mTaskListLayoutManager = new LinearLayoutManager(this);
        mTaskListRecycleView.setLayoutManager(mTaskListLayoutManager);
        mTaskListRecycleView.setItemAnimator(new DefaultItemAnimator());

        // add divider
        mTaskListRecycleView.addItemDecoration(new DividerItemDecoration(
                this, LinearLayoutManager.VERTICAL));

        // specify an adapter
        final TaskListAdapter mTaskListAdapter = new TaskListAdapter(this);
        mTaskListRecycleView.setAdapter(mTaskListAdapter);

        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        mTaskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                // Update the cached copy of the words in the adapter.
                mTaskListAdapter.setTasks(tasks);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    * This function is revoked when user click Add or Edit a Task
    * */
    public void newEditTask(MenuItem view) {
        Intent newEditIntent = new Intent(this, NewEditTaskActivity.class);
        startActivityForResult(newEditIntent, NEW_EDIT_TASK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_EDIT_TASK_REQUEST && resultCode == RESULT_OK) {
            Task task = new Task(data.getStringExtra(NewEditTaskActivity.EXTRA_REPLY_NAME),
                    data.getStringExtra(NewEditTaskActivity.EXTRA_REPLY_DESC));
            mTaskViewModel.insert(task);
        } else {
            Toast.makeText(getApplicationContext(), "Not Saved", Toast.LENGTH_LONG).show();
        }
    }
}
