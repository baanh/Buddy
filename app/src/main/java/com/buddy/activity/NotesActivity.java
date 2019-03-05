package com.buddy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.buddy.main.R;

import static com.buddy.activity.TaskNewEditActivity.EXTRA_ID;
import static com.buddy.activity.TaskNewEditActivity.EXTRA_NOTES;

public class NotesActivity extends AppCompatActivity {

    private EditText notesText;

    @Override
    protected void onCreate (Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_notes);

        notesText = findViewById(R.id.notes);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_NOTES)) {
            notesText.setText(intent.getStringExtra(EXTRA_NOTES));
        } else {
            notesText.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }

    public void saveNotes(MenuItem view) {
        String notes = notesText.getText().toString();

        Intent response = new Intent();
        response.putExtra("notesData",notes);
        setResult(RESULT_OK, response);
        finish();
    }
}