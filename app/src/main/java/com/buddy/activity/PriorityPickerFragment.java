package com.buddy.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

import com.buddy.main.R;
import com.buddy.util.Constants;

public class PriorityPickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Priority")
                .setItems(Constants.TASK_PRIORITY_ARRAY, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        TextView txtPriority = getActivity().findViewById(R.id.txt_priority);
                        txtPriority.setText(Constants.TASK_PRIORITY_ARRAY[which]);
                    }
                });
        return builder.create();
    }
}
