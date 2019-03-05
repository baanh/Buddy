package com.buddy.activity;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
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

public class TaskNewEditActivity extends AppCompatActivity
        implements CategorySelectionFragment.CategorySelectionDialogListener {

    private static final String TAG = "Notes: ";
    private EditText editTaskName;
    private EditText editTaskDescription;
    private RelativeLayout startTime;
    private RelativeLayout endTime;
    private LinearLayout categorySelect;
    private Category selectedCategory;
    private Button notesButton;
    private EditText notesGist;

    private String notesData = "";

    public static final String EXTRA_ID = "com.buddy.tasklistsql.EXTRA_ID";
    public static final String EXTRA_NAME = "com.buddy.tasklistsql.EXTRA_NAME";
    public static final String EXTRA_DESC = "com.buddy.tasklistsql.EXTRA_DESC";
    public static final String EXTRA_CATEGORY_ID = "com.buddy.tasklistsql.EXTRA_CATEGORY_ID";
    public static final String EXTRA_NOTES = "com.buddy.tasklistsql.EXTRA_NOTES";

    public static final String EXTRA_REPLY_NAME = "com.buddy.tasklistsql.REPLY_NAME";
    public static final String EXTRA_REPLY_DESC = "com.buddy.tasklistsql.REPLY_DESC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_task);

        editTaskName = (EditText) findViewById(R.id.edit_task_name);
        editTaskDescription = (EditText) findViewById(R.id.edit_task_description);
        notesButton = findViewById(R.id.notesButton);
        notesGist = findViewById(R.id.notesGist);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Task");
            editTaskName.setText(intent.getStringExtra(EXTRA_NAME));
            editTaskDescription.setText(intent.getStringExtra(EXTRA_DESC));
            notesData = intent.getStringExtra(EXTRA_NOTES);
            displayGist();
        } else {
            setTitle("New Task");
            displayGist();
        }

        startTime = (RelativeLayout) findViewById(R.id.start_time_view);
        endTime = (RelativeLayout) findViewById(R.id.end_time_view);

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dateTimePickerFragment = new DateTimePickerFragment();
                dateTimePickerFragment.show(getSupportFragmentManager(), "startTimePicker");
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dateTimePickerFragment = new DateTimePickerFragment();
                dateTimePickerFragment.show(getSupportFragmentManager(), "endTimePicker");
            }
        });

        categorySelect = (LinearLayout) findViewById(R.id.category_select_view);
        categorySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment categorySelectionFragment = new CategorySelectionFragment();
                categorySelectionFragment.show(getSupportFragmentManager(), "categorySelection");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_edit_toolbar_menu, menu);
        return true;
    }

    public void saveTask(MenuItem view) {
        String name = editTaskName.getText().toString();
        String description = editTaskDescription.getText().toString();
        int categoryId = this.selectedCategory.getId();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please insert a name and description", Toast.LENGTH_LONG).show();
            return;
        }

        Intent response = new Intent();
        response.putExtra(EXTRA_REPLY_NAME, name);
        response.putExtra(EXTRA_REPLY_DESC, description);
        response.putExtra(EXTRA_CATEGORY_ID, categoryId);
        response.putExtra(EXTRA_NOTES, notesData);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            response.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, response);
        finish();
    }

    @Override
    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
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
                displayGist();
            }
        }
    }

    public void displayGist() {
        if (notesData.length() < 50) {
            notesGist.setText(notesData);
        }
        else {
            notesGist.setText(notesData.substring(1, 50) + "...");
        }
    }
}
