package com.buddy.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class TaskNewEditActivity extends AppCompatActivity {

    private EditText editTaskName;
    private EditText editTaskDescription;
    private TextView textStartTime;
    private TextView textEndTime;
    private TextView textCategory;
    private RelativeLayout startTimeView;
    private RelativeLayout endTimeView;
    private LinearLayout categorySelect;
    private DateTimePickerFragment dateTimePickerFragment;
    private CategorySelectionFragment categorySelectionFragment;

    public static final String EXTRA_ID = "com.buddy.tasklistsql.EXTRA_ID";
    public static final String EXTRA_NAME = "com.buddy.tasklistsql.EXTRA_NAME";
    public static final String EXTRA_DESC = "com.buddy.tasklistsql.EXTRA_DESC";
    public static final String EXTRA_CATEGORY_ID = "com.buddy.tasklistsql.EXTRA_CATEGORY_ID";
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

        setNewEditEnvironment();

        // Select start time and end time, use Bundle to pass data to dialog object
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
    }

    @SuppressLint("SetTextI18n")
    public void setNewEditEnvironment() {
        // Get data from request activity to identify whether it is a create new or edit action
        Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_ID)) {
            setTitle("New Task");
            return;
        }
        setTitle("Edit Task");
        editTaskName.setText(intent.getStringExtra(EXTRA_NAME));
        editTaskDescription.setText(intent.getStringExtra(EXTRA_DESC));
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

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            response.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, response);
        finish();
    }
}
