package com.buddy.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.buddy.main.R;

public class NewEditTaskActivity extends AppCompatActivity {

    private EditText editTaskName;
    private EditText editTaskDescription;

    public static final String EXTRA_REPLY_NAME = "com.buddy.tasklistsql.REPLY_NAME";
    public static final String EXTRA_REPLY_DESC = "com.buddy.tasklistsql.REPLY_DESC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_task);

        editTaskName = (EditText) findViewById(R.id.edit_task_name);
        editTaskDescription = (EditText) findViewById(R.id.edit_task_description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_edit_toolbar_menu, menu);
        return true;
    }

    public void saveTask(MenuItem view) {
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(editTaskName.getText())) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            Log.i("Name: ", editTaskName.getText().toString());
            replyIntent.putExtra(EXTRA_REPLY_NAME, editTaskName.getText().toString());
            replyIntent.putExtra(EXTRA_REPLY_DESC, editTaskDescription.getText().toString());
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }
}
