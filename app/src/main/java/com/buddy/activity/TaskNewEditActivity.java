package com.buddy.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buddy.entity.Category;
import com.buddy.main.R;
import com.buddy.viewmodel.CategoryViewModel;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class TaskNewEditActivity extends AppCompatActivity {

    private static final String TAG = "Notes: ";
    private EditText editTaskName;
    private EditText editTaskDescription;
    private TextView textStartTime;
    private TextView textEndTime;
    private TextView textCategory;
    private RelativeLayout startTimeView;
    private RelativeLayout endTimeView;
    private LinearLayout categorySelect;
    private Category selectedCategory;
    private Button notesButton;
    private EditText notesGist;
    private Button logStart, logEnd;
    private EditText loggedTime;

    private String notesData = "";
    private Date timeLogged;
    private String timeLog = "00:00";
    private DateTimePickerFragment dateTimePickerFragment;
    private CategorySelectionFragment categorySelectionFragment;

    public static final String EXTRA_ID = "com.buddy.tasklistsql.EXTRA_ID";
    public static final String EXTRA_NAME = "com.buddy.tasklistsql.EXTRA_NAME";
    public static final String EXTRA_DESC = "com.buddy.tasklistsql.EXTRA_DESC";
    public static final String EXTRA_CATEGORY_ID = "com.buddy.tasklistsql.EXTRA_CATEGORY_ID";
    public static final String EXTRA_NOTES = "com.buddy.tasklistsql.EXTRA_NOTES";
    public static final String EXTRA_TIME_LOG = "com.buddy.tasklistsql.EXTRA_TIME_LOG";
    public static final String EXTRA_START_DATE = "com.buddy.tasklistsql.EXTRA_START_DATE";
    public static final String EXTRA_END_DATE = "com.buddy.tasklistsql.EXTRA_END_DATE";

    public static final String EXTRA_REPLY_NAME = "com.buddy.tasklistsql.REPLY_NAME";
    public static final String EXTRA_REPLY_DESC = "com.buddy.tasklistsql.REPLY_DESC";
    public static final String EXTRA_REPLY_START_DATE = "com.buddy.tasklistsql.EXTRA_REPLY_START_DATE";
    public static final String EXTRA_REPLY_END_DATE = "com.buddy.tasklistsql.EXTRA_REPLY_END_DATE";

    private Date startDate;
    private Date endDate;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_task);

        editTaskName = findViewById(R.id.edit_task_name);
        editTaskDescription = findViewById(R.id.edit_task_description);
        textStartTime = findViewById(R.id.textview_start_time);
        textEndTime = findViewById(R.id.textview_end_time);
        categorySelect = findViewById(R.id.category_select_view);
        textCategory = categorySelect.findViewById(R.id.textView_category);
        notesButton = findViewById(R.id.notesButton);
        notesGist = findViewById(R.id.notesGist);
        editTaskName = (EditText) findViewById(R.id.edit_task_name);
        editTaskDescription = (EditText) findViewById(R.id.edit_task_description);
        logEnd = findViewById(R.id.logEnd);
        logStart = findViewById(R.id.logStart);
        loggedTime = findViewById(R.id.loggedTime);

        final Bundle args = new Bundle();
        dateTimePickerFragment = new DateTimePickerFragment();
        dateTimePickerFragment.setStartTime(startDate);
        dateTimePickerFragment.setEndTime(endDate);
        startTimeView = findViewById(R.id.start_time_view);
        startTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString(DateTimePickerFragment.EXTRA_TIME, DateTimePickerFragment.START_TIME_LABEL);
                dateTimePickerFragment.setArguments(args);
                dateTimePickerFragment.show(getSupportFragmentManager(), "startTimePicker");
            }
        });
        endTimeView = findViewById(R.id.end_time_view);
        endTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString(DateTimePickerFragment.EXTRA_TIME, DateTimePickerFragment.END_TIME_LABEL);
                dateTimePickerFragment.setArguments(args);
                dateTimePickerFragment.show(getSupportFragmentManager(), "endTimePicker");
            }
        });

        // Select category
        categorySelectionFragment = new CategorySelectionFragment();
        categorySelect = findViewById(R.id.category_select_view);
        categorySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorySelectionFragment.show(getSupportFragmentManager(), "categorySelection");
            }
        });

        setNewEditEnvironment();
    }

    @SuppressLint("SetTextI18n")
    public void setNewEditEnvironment() {
        // Get data from request activity to identify whether it is a create new or edit action
        Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_ID)) {
            setTitle("New Task");
            loggedTime.setText(timeLog);
            return;
        }
        setTitle("Edit Task");

        editTaskName.setText(intent.getStringExtra(EXTRA_NAME));
        editTaskDescription.setText(intent.getStringExtra(EXTRA_DESC));
        notesData = intent.getStringExtra(EXTRA_NOTES) != null ? intent.getStringExtra(EXTRA_NOTES) : "";
        showShortNotes(notesData);

        startDate = new Date(intent.getLongExtra(EXTRA_START_DATE, -1));
        endDate = new Date(intent.getLongExtra(EXTRA_END_DATE, -1));
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        textStartTime.setText(cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH)
                + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
        cal.setTime(endDate);
        textEndTime.setText(cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH)
                + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));

        int categoryId = intent.getIntExtra(EXTRA_CATEGORY_ID, -1);
        CategoryViewModel categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        category = categoryViewModel.findCategoryById(categoryId);
        textCategory.setText(category.getName());

        timeLog = intent.getStringExtra(EXTRA_TIME_LOG);
        loggedTime.setText(timeLog);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_edit_toolbar_menu, menu);
        return true;
    }

    /*
     * Method revoked when users click on save task
     */
    public void saveTask(MenuItem view) {
        String name = editTaskName.getText().toString();
        String description = editTaskDescription.getText().toString();
        startDate = dateTimePickerFragment.getStartTime() != null ?
                dateTimePickerFragment.getStartTime() : startDate;
        endDate = dateTimePickerFragment.getEndTime() != null ?
                dateTimePickerFragment.getEndTime() : endDate;
        category = categorySelectionFragment.getSelectCategory() != null ?
                categorySelectionFragment.getSelectCategory() : category;
        int categoryId = category.getId();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please insert a name and description", Toast.LENGTH_LONG).show();
            return;
        }

        Intent response = new Intent();
        response.putExtra(EXTRA_REPLY_NAME, name);
        response.putExtra(EXTRA_REPLY_DESC, description);
        response.putExtra(EXTRA_CATEGORY_ID, categoryId);
        response.putExtra(EXTRA_REPLY_START_DATE, startDate.getTime());
        response.putExtra(EXTRA_REPLY_END_DATE, endDate.getTime());
        response.putExtra(EXTRA_NOTES, notesData);
        response.putExtra(EXTRA_TIME_LOG, timeLog);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            response.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, response);
        finish();
    }

    public void openNotes(View view) {
        Intent intent = new Intent(TaskNewEditActivity.this, NotesActivity.class    );
        intent.putExtra(EXTRA_NOTES, notesData);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                notesData = data.getStringExtra("notesData");
                showShortNotes(notesData);
            }
        }
    }

    public void showShortNotes(String note) {
        if (note.length() < 20) {
            notesGist.setText(note);
        }
        else {
            notesGist.setText(note.substring(1, 20) + "...");
        }
    }

    //Function to start the time log
    public void timeLogStart(View view) {
        logStart.setEnabled(false);
        logEnd.setEnabled(true);
        timeLogged = Calendar.getInstance().getTime();
    }

    //Function to stop and save the time log
    public void timeLogEnd(View view) {
        logStart.setEnabled(true);
        logEnd.setEnabled(false);
        long timeDifferenceMiliSec = Calendar.getInstance().getTime().getTime() - timeLogged.getTime();
        int hours = (int) timeDifferenceMiliSec / (1000 * 60 * 60);
        int mins = (int) (timeDifferenceMiliSec / (1000 * 60)) % 60;
        timeLog = String.format("%02d:%02d", hours, mins);
        loggedTime.setText(timeLog);
    }
}
