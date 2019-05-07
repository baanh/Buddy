package com.buddy.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.buddy.adapters.TaskExpandableListAdapter;
import com.buddy.entity.Task;
import com.buddy.main.R;
import com.buddy.viewmodel.TaskViewModel;

import android.widget.EditText;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;
import java.util.HashMap;
import java.util.List;

public class TaskDailyViewActivity extends AppCompatActivity {

    private ExpandableListView taskListView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;
    private TaskViewModel taskViewModel;
  
    private EditText taskText;
    private ArrayList<String> tasks = new ArrayList<String>();

    private TextToSpeech speaker;
    private static final String tag = "Widgets";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_by_day);
      
        //Initialize text to speech engine
        speaker = new TextToSpeech(this, (OnInitListener) this);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                if (tasks != null) {
                    for (Task task : tasks) {
                        List<String> taskDetail = new ArrayList<>();
                        listDataHeader.add(task.getName());
                        taskDetail.add("Start: " + task.getStartDate().toString());
                        taskDetail.add("End: " + task.getEndDate().toString());
                        taskDetail.add("Description: " + task.getDescription());
                        listHash.put(listDataHeader.get(listDataHeader.size() - 1), taskDetail);
                    }
                }
            }
        });
        taskListView = findViewById(R.id.task_expandable_list_view);
        listAdapter = new TaskExpandableListAdapter(this, listDataHeader, listHash);
        taskListView.setAdapter(listAdapter);
    }
    
    //speaks the contents of output
    @TargetApi(Build.VERSION_CODES.LOLLIPOP) //have no idea what this is but will show error without this line
    public void speak(String output){
        //	speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null);  //for APIs before 21
        speaker.speak(output, TextToSpeech.QUEUE_FLUSH, null, "Id 0");
    }

    // Implements TextToSpeech.OnInitListener.
    public void onInit(int status) {
        // status can be either TextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            // If a language is not be available, the result will indicate it.
            int result = speaker.setLanguage(Locale.US);

            //int result = speaker.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language data is missing or the language is not supported.
                Log.e(tag, "Language is not available.");
            } else {
                // The TTS engine has been successfully initialized
                speak("Please enter your bill amount");
                Log.i(tag, "TTS Initialization successful.");
            }
        } else {
            // Initialization failed.
            Log.e(tag, "Could not initialize TextToSpeech.");
        }
    }

    // on destroy
    public void onDestroy(){

        // shut down TTS engine
        if(speaker != null){
            speaker.stop();
            speaker.shutdown();
        }
        super.onDestroy();
    }

    public void getTodayTasks(ArrayList tasks){
        int num = tasks.size();
        speak("Hi Buddy, today you have "+num+"events.");
        for (int i=0; i<tasks.size(); i++){
            String taskName = tasks.get(i).toString();
            speak(taskName);
            /*If we also want it to announce the start and end time of each task,
            Might need a HashMap structure to store the task and its corresponding times,
            Not sure what form should the start time and end time be stored and read from*/
        }
    }
}
