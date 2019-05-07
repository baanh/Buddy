package com.buddy.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.buddy.adapters.TaskExpandableListAdapter;
import com.buddy.entity.Task;
import com.buddy.main.R;
import com.buddy.util.Constants;
import com.buddy.viewmodel.TaskViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TaskDailyViewActivity extends AppCompatActivity
        implements TextToSpeech.OnInitListener {

    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;
    private TaskViewModel taskViewModel;
    private TextView txtTodayDate;

    private TextToSpeech speaker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_by_day);

        //Initialize text to speech engine
        speaker = new TextToSpeech(getApplicationContext(), this);
        txtTodayDate = findViewById(R.id.txt_today_date);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        // Find tasks between today and tomorrow
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter
                = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date mToday = formatter.parse(formatter.format(new Date()));
            Date mTomorrow = new Date(mToday.getTime() + (1000 * 60 * 60 * 24) - 1);

            txtTodayDate.setText(formatter.format(new Date()));
            List<Task> tasks = taskViewModel.findTasksBetweenDate(mToday, mTomorrow);
            for (Task task : tasks) {
                List<String> taskDetail = new ArrayList<>();
                listDataHeader.add(task.getName());
                taskDetail.add("Start: " + task.getStartDate().toString());
                taskDetail.add("End: " + task.getEndDate().toString());
                taskDetail.add("Description: " + task.getDescription());
                listHash.put(listDataHeader.get(listDataHeader.size() - 1), taskDetail);
            }
            speakTodayTasks(tasks);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ExpandableListView taskListView = findViewById(R.id.task_expandable_list_view);
        ExpandableListAdapter listAdapter = new TaskExpandableListAdapter(this, listDataHeader, listHash);
        taskListView.setAdapter(listAdapter);

        getSupportActionBar().setTitle("Day");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton btnNewTask = findViewById(R.id.btn_new_task);
        btnNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newTaskIntent = new Intent(getApplicationContext(), TaskNewEditActivity.class);
                startActivityForResult(newTaskIntent, Constants.NEW_TASK_REQUEST);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void speak(String output) {
        speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.ERROR) {
            speaker.setLanguage(Locale.US);
        }
    }

    public void onPause() {
        if (speaker != null) {
            speaker.stop();
            speaker.shutdown();
        }
        super.onPause();
    }

    public void speakTodayTasks(List<Task> tasks) {
        int num = tasks.size();
        speaker.speak("Hi Buddy, today you have events.", TextToSpeech.QUEUE_FLUSH, null);
//        for (Task task : tasks) {
//            String taskName = task.getName();
//            speak(taskName);
//            /*If we also want it to announce the start and end time of each task,
//            Might need a HashMap structure to store the task and its corresponding times,
//            Not sure what form should the start time and end time be stored and read from*/
//        }
    }
}
