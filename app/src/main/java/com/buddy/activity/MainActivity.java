package com.buddy.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.buddy.adapters.TaskListAdapter;
import com.buddy.entity.Task;
import com.buddy.main.R;
import com.buddy.viewmodel.TaskViewModel;

import java.util.Date;
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
        mTaskListRecycleView = findViewById(R.id.my_task_list);

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
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
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
            case R.id.push_notifications:
                pushNotifications();
                return true;
            case R.id.action_favorite:
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_delete_all_tasks:
                mTaskViewModel.deleteAllTasks();
                Toast.makeText(getApplicationContext(), "All task deleted", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_new_category:
                Intent newCategoryIntent = new Intent(this, CategoryNewEditActivity.class);
                startActivity(newCategoryIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * This function is revoked when user click Add a new Task
     */
    public void newEditTask(MenuItem view) {
        Intent newTaskIntent = new Intent(this, TaskNewEditActivity.class);
        startActivityForResult(newTaskIntent, NEW_TASK_REQUEST);
    }

    /*
     * This method called to edit clicked task
     */
    @Override
    public void onItemClick(Task task) {
        Intent intent = new Intent(this, TaskNewEditActivity.class);
        intent.putExtra(TaskNewEditActivity.EXTRA_ID, task.getId());
        intent.putExtra(TaskNewEditActivity.EXTRA_NAME, task.getName());
        intent.putExtra(TaskNewEditActivity.EXTRA_DESC, task.getDescription());
        intent.putExtra(TaskNewEditActivity.EXTRA_CATEGORY_ID, task.getCategoryId());
        intent.putExtra(TaskNewEditActivity.EXTRA_START_DATE, task.getStartDate().getTime());
        intent.putExtra(TaskNewEditActivity.EXTRA_END_DATE, task.getEndDate().getTime());
        intent.putExtra(TaskNewEditActivity.EXTRA_NOTES, task.getNotes());
        startActivityForResult(intent, EDIT_TASK_REQUEST);
    }

    public Task createTaskFromIntent(Intent data) {
        // Check if user has chosen category
        int categoryId = data.getIntExtra(TaskNewEditActivity.EXTRA_CATEGORY_ID, -1);
        if (categoryId == -1) {
            Toast.makeText(getApplicationContext(), "Task can't be created", Toast.LENGTH_LONG).show();
            return null;
        }
        // Get data from the response activity
        Task task = new Task();
        task.setCategoryId(categoryId);
        task.setNotes(data.getStringExtra(TaskNewEditActivity.EXTRA_NOTES));
        task.setTimeLog(data.getStringExtra(TaskNewEditActivity.EXTRA_TIME_LOG));

        Date startDate = new Date();
        startDate.setTime(data.getLongExtra(TaskNewEditActivity.EXTRA_REPLY_START_DATE,-1));
        Date endDate = new Date();
        endDate.setTime(data.getLongExtra(TaskNewEditActivity.EXTRA_REPLY_END_DATE,-1));

        task.setName(data.getStringExtra(TaskNewEditActivity.EXTRA_REPLY_NAME));
        task.setDescription(data.getStringExtra(TaskNewEditActivity.EXTRA_REPLY_DESC));
        task.setNotes(data.getStringExtra(TaskNewEditActivity.EXTRA_NOTES));
        task.setStartDate(startDate);
        task.setEndDate(endDate);
        return task;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_TASK_REQUEST && resultCode == RESULT_OK) {
            Task task = createTaskFromIntent(data);
            mTaskViewModel.insert(task);
            Toast.makeText(getApplicationContext(), "Task saved", Toast.LENGTH_LONG).show();
        } else if (requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK) {
            // Check if task has been created and assigned a category
            int id = data.getIntExtra(TaskNewEditActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(getApplicationContext(), "Task can't be updated", Toast.LENGTH_LONG).show();
                return;
            }
            Task task = createTaskFromIntent(data);
            task.setId(id);
            int categoryId = data.getIntExtra(TaskNewEditActivity.EXTRA_CATEGORY_ID, -1);
            if (categoryId == -1) {
                Toast.makeText(getApplicationContext(), "Task can't be created", Toast.LENGTH_LONG).show();
                return;
            }
            task.setCategoryId(categoryId);
            task.setNotes(data.getStringExtra(TaskNewEditActivity.EXTRA_NOTES));
            task.setTimeLog(data.getStringExtra(TaskNewEditActivity.EXTRA_TIME_LOG));

            mTaskViewModel.update(task);
            Toast.makeText(getApplicationContext(), "Task saved", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Not Saved", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(Task task) {
        Intent intent = new Intent(this, TaskNewEditActivity.class);
        intent.putExtra(TaskNewEditActivity.EXTRA_ID, task.getId());
        intent.putExtra(TaskNewEditActivity.EXTRA_NAME, task.getName());
        intent.putExtra(TaskNewEditActivity.EXTRA_DESC, task.getDescription());
        intent.putExtra(TaskNewEditActivity.EXTRA_NOTES, task.getNotes());
        startActivityForResult(intent, EDIT_TASK_REQUEST);
    }

    public void pushNotifications() {
        String channelID = "001";
        CharSequence channelName = "First Channel";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(channelID, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.icons8task64)).getBitmap();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelID)
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_notifications_custom);

        LiveData<List<Task>> taskList = mTaskViewModel.getAllTasks();

        for (int i = 0; i < taskList.getValue().size(); i++) {
            mBuilder
                    .setContentTitle(taskList.getValue().get(i).getName())
                    .setContentText(taskList.getValue().get(i).getDescription());
            notificationManager.notify(i, mBuilder.build());
        }

    }
}
