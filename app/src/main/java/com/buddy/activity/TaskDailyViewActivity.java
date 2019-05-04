package com.buddy.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.buddy.main.R;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Locale;

public class TaskDailyViewActivity extends AppCompatActivity {

    private EditText taskText;
    private ArrayList<String> tasks = new ArrayList<String>();

    private TextToSpeech speaker;
    private static final String tag = "Widgets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_by_day);

        //Initialize text to speech engine
        speaker = new TextToSpeech(this, (OnInitListener) this);
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
        tasks.add(taskText.getText().toString());
        int num = tasks.size();
        speak("Hi Buddy, today you have "+num+"events.");
        for (int task=0; )
    }
}
