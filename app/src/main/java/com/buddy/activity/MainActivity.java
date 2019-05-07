package com.buddy.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
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
import com.buddy.util.Constants;
import com.buddy.viewmodel.TaskViewModel;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskListAdapter.OnItemClickListener {
    private TaskViewModel mTaskViewModel;
    private FloatingActionButton btnNewTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get recycleView to populate a list of tasks
        RecyclerView mTaskListRecycleView = findViewById(R.id.my_task_list);

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

        btnNewTask = findViewById(R.id.btn_new_task);
        btnNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newTaskIntent = new Intent(getApplicationContext(), TaskNewEditActivity.class);
                startActivityForResult(newTaskIntent, Constants.NEW_TASK_REQUEST);
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
            case R.id.push_notifications:
                pushNotifications();
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
            case R.id.monthly_view:
                Intent monthlyViewIntent = new Intent(this, TaskMonthlyViewActivity.class);
                startActivity(monthlyViewIntent);
                return true;
            case R.id.daily_view:
                Intent dailyViewIntent = new Intent(this, TaskDailyViewActivity.class);
                startActivity(dailyViewIntent);
                return true;
            case R.id.action_exit:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * This method called to edit clicked task
     */
    @Override
    public void onItemClick(Task task) {
        Intent intent = new Intent(this, TaskNewEditActivity.class);
        intent.putExtra(Constants.EXTRA_ID, task.getId());
        intent.putExtra(Constants.EXTRA_NAME, task.getName());
        intent.putExtra(Constants.EXTRA_DESC, task.getDescription());
        intent.putExtra(Constants.EXTRA_CATEGORY_ID, task.getCategoryId());
        intent.putExtra(Constants.EXTRA_START_DATE, task.getStartDate().getTime());
        intent.putExtra(Constants.EXTRA_END_DATE, task.getEndDate().getTime());
        intent.putExtra(Constants.EXTRA_NOTES, task.getNotes());
        intent.putExtra(Constants.EXTRA_INVITEES, task.getInvitees());
        startActivityForResult(intent, Constants.EDIT_TASK_REQUEST);
    }

    public Task createTaskFromIntent(Intent data) {
        // Check if user has chosen category
        int categoryId = data.getIntExtra(Constants.EXTRA_CATEGORY_ID, -1);
        if (categoryId == -1) {
            Toast.makeText(getApplicationContext(), "Task can't be created", Toast.LENGTH_LONG).show();
            return null;
        }
        // Get data from the response activity
        Task task = new Task();
        task.setCategoryId(categoryId);
        task.setNotes(data.getStringExtra(Constants.EXTRA_NOTES));
        task.setTimeLog(data.getStringExtra(Constants.EXTRA_TIME_LOG));

        Date startDate = new Date();
        startDate.setTime(data.getLongExtra(Constants.EXTRA_REPLY_START_DATE,-1));
        Date endDate = new Date();
        endDate.setTime(data.getLongExtra(Constants.EXTRA_REPLY_END_DATE,-1));

        task.setName(data.getStringExtra(Constants.EXTRA_REPLY_NAME));
        task.setDescription(data.getStringExtra(Constants.EXTRA_REPLY_DESC));
        task.setNotes(data.getStringExtra(Constants.EXTRA_NOTES));
        task.setStartDate(startDate);
        task.setEndDate(endDate);
        task.setInvitees(data.getStringExtra(Constants.EXTRA_REPLY_INVITEES));
        return task;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.NEW_TASK_REQUEST && resultCode == RESULT_OK) {
            Task task = createTaskFromIntent(data);
            mTaskViewModel.insert(task);
            Toast.makeText(getApplicationContext(), "Task saved", Toast.LENGTH_LONG).show();
        } else if (requestCode == Constants.EDIT_TASK_REQUEST && resultCode == RESULT_OK) {
            // Check if task has been created and assigned a category
            int id = data.getIntExtra(Constants.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(getApplicationContext(), "Task can't be updated", Toast.LENGTH_LONG).show();
                return;
            }
            Task task = createTaskFromIntent(data);
            task.setId(id);
            mTaskViewModel.update(task);
            Toast.makeText(getApplicationContext(), "Task saved", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Task not Saved", Toast.LENGTH_LONG).show();
        }
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
        mBuilder.setContentIntent(contentIntent)
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
