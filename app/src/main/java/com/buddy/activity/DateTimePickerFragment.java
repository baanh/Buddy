package com.buddy.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.buddy.main.R;

import java.util.Calendar;
import java.util.Date;

public class DateTimePickerFragment extends DialogFragment {
    private int year;
    private int month;
    private int dayOfMonth;
    private int hour;
    private int minute;

    public static final String EXTRA_TIME = "com.buddy.dialog.EXTRA_TIME";
    public static final String START_TIME_LABEL = "com.buddy.dialog.START_TIME_LABEL";
    public static final String END_TIME_LABEL = "com.buddy.dialog.END_TIME_LABEL";

    private Date startTime = null;
    private Date endTime = null;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        final Bundle args = getArguments();
        setTimeForDialog(calendar, args);
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_datetime, null);
        DatePicker datePicker = v.findViewById(R.id.datePicker);
        datePicker.init(year, month, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateTimePickerFragment.this.year = year;
                // month of date picker starts from 0
                DateTimePickerFragment.this.month = monthOfYear + 1;
                DateTimePickerFragment.this.dayOfMonth = dayOfMonth;
            }
        });

        TimePicker timePicker = v.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hour, int minute) {
                DateTimePickerFragment.this.hour = hour;
                DateTimePickerFragment.this.minute = minute;
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Select date time")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Set chosen time for the calendar
                        calendar.set(year, month, dayOfMonth, hour, minute);
                        if (args.getString(EXTRA_TIME).equals(START_TIME_LABEL)) {
                            TextView startTimeView = getActivity().findViewById(R.id.textview_start_time);
                            startTimeView.setText(month + "/" + dayOfMonth + "/" + year + " " + hour + ":" + minute);
                            setStartTime(calendar.getTime());
                        } else if (args.getString(EXTRA_TIME).equals(END_TIME_LABEL)) {
                            TextView endTimeView = getActivity().findViewById(R.id.textview_end_time);
                            endTimeView.setText(month + "/" + dayOfMonth + "/" + year + " " + hour + ":" + minute);
                            setEndTime(calendar.getTime());
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .create();
    }

    public void setTimeForDialog(Calendar calendar, Bundle args) {
        // Set start time and end time if they exist
        // Otherwise, use the current time as the default values for the picker
        if (startTime != null && endTime != null) {
            if (args.getString(EXTRA_TIME).equals(START_TIME_LABEL)) {
                calendar.setTime(startTime);
            } else if (args.getString(EXTRA_TIME).equals(END_TIME_LABEL)) {
                calendar.setTime(endTime);
            }
        }
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
