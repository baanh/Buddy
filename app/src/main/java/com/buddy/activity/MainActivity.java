package com.buddy.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.buddy.adapters.TaskListAdapter;
import com.buddy.entity.Task;
import com.buddy.main.R;
import com.buddy.viewmodel.TaskViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskListAdapter.OnItemClickListener {
    private RecyclerView mTaskListRecycleView;
    private TaskViewModel mTaskViewModel;

    private static final int NEW_TASK_REQUEST = 1;
    private static final int EDIT_TASK_REQUEST = 2;

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
        final TaskListAdapter mTaskListAdapter = new TaskListAdapter();
        // Set onClick listener for list items in recycle view
        mTaskListAdapter.setOnItemClickListener(this);
        mTaskListRecycleView.setAdapter(mTaskListAdapter);

        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        mTaskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                // Update the cached copy of the words in the adapter.
                mTaskListAdapter.submitList(tasks);
            }
        });

        // Add delete action when user swipes a task to the left or right
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mTaskViewModel.delete(mTaskListAdapter.getTaskAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(mTaskListRecycleView);
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
            case R.id.action_delete_all_tasks:
                mTaskViewModel.deleteAllTasks();
                Toast.makeText(getApplicationContext(), "All task deleted", Toast.LENGTH_LONG).show();
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
        startActivityForResult(newEditIntent, NEW_TASK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_TASK_REQUEST && resultCode == RESULT_OK) {
            Task task = new Task(data.getStringExtra(NewEditTaskActivity.EXTRA_REPLY_NAME),
                    data.getStringExtra(NewEditTaskActivity.EXTRA_REPLY_DESC));
            mTaskViewModel.insert(task);
            Toast.makeText(getApplicationContext(), "Task saved", Toast.LENGTH_LONG).show();
        } else if (requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(NewEditTaskActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(getApplicationContext(), "Task can't be updated", Toast.LENGTH_LONG).show();
                return;
            }

            Task task = new Task(data.getStringExtra(NewEditTaskActivity.EXTRA_REPLY_NAME),
                    data.getStringExtra(NewEditTaskActivity.EXTRA_REPLY_DESC));
            task.setId(id);
            mTaskViewModel.update(task);
            Toast.makeText(getApplicationContext(), "Task saved", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Not Saved", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(Task task) {
        Intent intent = new Intent(this, NewEditTaskActivity.class);
        intent.putExtra(NewEditTaskActivity.EXTRA_ID, task.getId());
        intent.putExtra(NewEditTaskActivity.EXTRA_NAME, task.getName());
        intent.putExtra(NewEditTaskActivity.EXTRA_DESC, task.getDescription());
        startActivityForResult(intent, EDIT_TASK_REQUEST);
    }
}
