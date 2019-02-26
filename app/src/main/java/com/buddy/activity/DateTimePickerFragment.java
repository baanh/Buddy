package com.buddy.activity;

import android.app.Dialog;
import android.content.DialogInterface;
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

public class DateTimePickerFragment extends DialogFragment {
    private int year;
    private int month;
    private int dayOfMonth;
    private int hour;
    private int minute;

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_datetime, null);
        DatePicker datePicker = (DatePicker) v.findViewById(R.id.datePicker);
        datePicker.init(year, month, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateTimePickerFragment.this.year = year;
                DateTimePickerFragment.this.month = monthOfYear + 1;
                DateTimePickerFragment.this.dayOfMonth = dayOfMonth;
            }
        });

        TimePicker timePicker = (TimePicker) v.findViewById(R.id.timePicker);
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
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"Fuck you", Toast.LENGTH_SHORT).show();
                        TextView startTime = (TextView) getActivity().findViewById(R.id.textview_start_time);
                        startTime.setText(month + "/" + dayOfMonth + "/" + year + " " + hour + ":" + minute);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .create();
    }
}
