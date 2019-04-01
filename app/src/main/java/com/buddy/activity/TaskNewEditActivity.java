package com.buddy.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.buddy.util.Constants;
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
    private DateTimePickerFragment dateTimePickerFragment;
    private CategorySelectionFragment categorySelectionFragment;
    private EditText notesGist;
    private Button logStart, logEnd;
    private EditText loggedTime;
    private EditText txtAddress;

    private String notesData = "";
    private Date timeLogged;
    private String timeLog = "00:00";
    private Date startDate;
    private Date endDate;
    private Category category;

    public static final String EXTRA_TIME_LOG = "com.buddy.tasklistsql.EXTRA_TIME_LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_task);

        editTaskName = findViewById(R.id.edit_task_name);
        editTaskDescription = findViewById(R.id.edit_task_description);
        textStartTime = findViewById(R.id.textview_start_time);
        textEndTime = findViewById(R.id.textview_end_time);

        TextView txtViewMap = findViewById(R.id.txt_view_map);
        txtAddress = findViewById(R.id.edit_address);
        txtViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra(Constants.EXTRA_ADDRESS, txtAddress.getText().toString());
                startActivityForResult(intent, Constants.GET_MAP_REQUEST);
            }
        });

        LinearLayout categorySelect = findViewById(R.id.category_select_view);
        textCategory = categorySelect.findViewById(R.id.textView_category);
        notesGist = findViewById(R.id.notesGist);
        setNewEditEnvironment();

        // Select start time and end time, use Bundle to pass data to dialog object
        final Bundle args = new Bundle();
        dateTimePickerFragment = new DateTimePickerFragment();
        dateTimePickerFragment.setStartTime(startDate);
        dateTimePickerFragment.setEndTime(endDate);
        RelativeLayout startTimeView = findViewById(R.id.start_time_view);
        startTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString(DateTimePickerFragment.EXTRA_TIME, DateTimePickerFragment.START_TIME_LABEL);
                dateTimePickerFragment.setArguments(args);
                dateTimePickerFragment.show(getSupportFragmentManager(), "startTimePicker");
            }
        });
        RelativeLayout endTimeView = findViewById(R.id.end_time_view);
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
        if (!intent.hasExtra(Constants.EXTRA_ID)) {
            setTitle("New Task");
            loggedTime.setText(timeLog);
            return;
        }
        setTitle("Edit Task");
        editTaskName.setText(intent.getStringExtra(Constants.EXTRA_NAME));
        editTaskDescription.setText(intent.getStringExtra(Constants.EXTRA_DESC));
        notesData = intent.getStringExtra(Constants.EXTRA_NOTES) != null ? intent.getStringExtra(Constants.EXTRA_NOTES) : "";
        showShortNotes(notesData);
        startDate = new Date(intent.getLongExtra(Constants.EXTRA_START_DATE, -1));
        endDate = new Date(intent.getLongExtra(Constants.EXTRA_END_DATE, -1));
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        textStartTime.setText(cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH)
                + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
        cal.setTime(endDate);
        textEndTime.setText(cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH)
                + "/" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
        int categoryId = intent.getIntExtra(Constants.EXTRA_CATEGORY_ID, -1);
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
        response.putExtra(Constants.EXTRA_REPLY_NAME, name);
        response.putExtra(Constants.EXTRA_REPLY_DESC, description);
        response.putExtra(Constants.EXTRA_CATEGORY_ID, categoryId);
        response.putExtra(Constants.EXTRA_REPLY_START_DATE, startDate.getTime());
        response.putExtra(Constants.EXTRA_REPLY_END_DATE, endDate.getTime());
        response.putExtra(Constants.EXTRA_NOTES, notesData);
        response.putExtra(EXTRA_TIME_LOG, timeLog);

        int id = getIntent().getIntExtra(Constants.EXTRA_ID, -1);
        if (id != -1) {
            response.putExtra(Constants.EXTRA_ID, id);
        }

        setResult(RESULT_OK, response);
        finish();
    }

    public void closeTask(MenuItem view) {
        AlertDialog closingDialog = new AlertDialog.Builder(this).create();
        closingDialog.setTitle("Task not saved");
        closingDialog.setMessage("The current task is not saved. Are you sure you want to exit?");
        closingDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        closingDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    }
                });
        closingDialog.show();
    }

    public void openNotes(View view) {
        Intent intent = new Intent(TaskNewEditActivity.this, NotesActivity.class);
        intent.putExtra(Constants.EXTRA_NOTES, notesData);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            notesData = data.getStringExtra("notesData");
            showShortNotes(notesData);
        } else if (requestCode == Constants.GET_MAP_REQUEST && resultCode == RESULT_OK) {
            txtAddress.setText(data.getStringExtra(Constants.EXTRA_REPLY_ADDRESS));
        }
    }

    public void showShortNotes(String note) {
        if (note.length() < 50) {
            notesGist.setText(note);
        } else {
            notesGist.setText(note.substring(1, 50) + "...");
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
