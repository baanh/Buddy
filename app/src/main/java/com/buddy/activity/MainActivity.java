package com.buddy.activity;

import android.app.AlarmManager;
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
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.buddy.adapters.TaskListAdapter;
import com.buddy.entity.Task;
import com.buddy.main.R;
import com.buddy.util.Constants;
import com.buddy.viewmodel.TaskViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements TaskListAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    private TaskViewModel mTaskViewModel;
    private DrawerLayout drawerLayout;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Notification Image
        bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.icons8task64)).getBitmap();

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
                for(int i = 0; i < tasks.size(); i++)
                {
                    cancelAlarm(tasks.get(i).getId());
                    scheduleNotification(tasks.get(i),5);
                }
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
                cancelAlarm(mTaskListAdapter.getTaskAt(viewHolder.getAdapterPosition()).getId());
                mTaskViewModel.delete(mTaskListAdapter.getTaskAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Task deleted", Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(mTaskListRecycleView);

        FloatingActionButton btnNewTask = findViewById(R.id.btn_new_task);
        btnNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newTaskIntent = new Intent(getApplicationContext(), TaskNewEditActivity.class);
                startActivityForResult(newTaskIntent, Constants.NEW_TASK_REQUEST);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_task) {
            // Handle the camera action
        } else if (id == R.id.nav_category) {
            Intent newCategoryIntent = new Intent(this, CategoryNewEditActivity.class);
            startActivity(newCategoryIntent);
        } else if (id == R.id.nav_month) {
            Intent monthlyViewIntent = new Intent(this, TaskMonthlyViewActivity.class);
            startActivity(monthlyViewIntent);
        } else if (id == R.id.nav_day) {
            Intent dailyViewIntent = new Intent(this, TaskDailyViewActivity.class);
            startActivity(dailyViewIntent);
        } else if (id == R.id.nav_speak) {

        } else if (id == R.id.nav_web) {
            Intent intentWeb = new Intent(this, WebBrowserActivity.class);
            startActivity(intentWeb);
        } else if (id == R.id.nav_settings) {
            Intent intentSettings = new Intent(this, SettingsActivity.class);
            startActivity(intentSettings);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_delete_all_tasks:
                LiveData<List<Task>> taskList = mTaskViewModel.getAllTasks();

                for (int i = 0; i < taskList.getValue().size(); i++) {
                    cancelAlarm(taskList.getValue().get(i).getId());
                }
                mTaskViewModel.deleteAllTasks();
                Toast.makeText(getApplicationContext(), "All task deleted", Toast.LENGTH_LONG).show();
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

    public void scheduleNotification (Task task, int reminderTime) {
        String notice = "Reminder for " + task.getDescription() + " at " + String.format("%02d:%02d", task.getStartDate().getHours(), task.getStartDate().getMinutes());

        String channelID = Integer.toString(task.getId());

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelID)
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(task.getName())
                .setContentText(notice)
                .setSmallIcon(R.drawable.ic_notifications_custom);

        Date noticeDate = new Date(task.getStartDate().getTime() - (reminderTime * 60 * 1000));

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, noticeDate.getYear() + 1900);
        cal.set(Calendar.MONTH, noticeDate.getMonth());
        cal.set(Calendar.DATE, noticeDate.getDate());
        cal.set(Calendar.HOUR_OF_DAY, noticeDate.getHours());
        cal.set(Calendar.MINUTE, noticeDate.getMinutes());
        cal.set(Calendar.SECOND, 0);

        Log.i("Alarm Time", cal.getTime().toString());

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent activity = PendingIntent.getActivity(this, task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(activity);

        Notification notification = mBuilder.build();

        Intent notificationIntent = new Intent(this, BroadcastManager.class);
        notificationIntent.putExtra(BroadcastManager.NOTIFICATION_ID, task.getId());
        notificationIntent.putExtra(BroadcastManager.NOTIFICATION, notification);
        notificationIntent.putExtra(BroadcastManager.NOTIFICATION_CHANNEL, channelID);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, task.getId(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }
        else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }
        Log.i("Alarm Id", Integer.toString(task.getId()));
    }

    public void cancelAlarm(int ID)
    {
        Intent notificationIntent = new Intent(this, BroadcastManager.class);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ID, notificationIntent, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null)
        {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            Log.i("Alarm Id", Integer.toString(ID));
        }
    }
}
