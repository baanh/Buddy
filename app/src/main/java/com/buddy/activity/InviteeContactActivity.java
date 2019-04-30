package com.buddy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.buddy.adapters.InviteeListAdapter;
import com.buddy.adapters.UserContactItemDetailLookup;
import com.buddy.adapters.UserContactItemKeyProvider;
import com.buddy.entity.UserContact;
import com.buddy.main.R;
import com.buddy.util.Constants;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;

public class InviteeContactActivity extends AppCompatActivity {
    private RecyclerView inviteeRecyclerView;
    private InviteeListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SelectionTracker mSelectionTracker;
    private TextView txtSelectionCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitee_contact);

        Intent intent = getIntent();
        String contactString = intent.getStringExtra(Constants.EXTRA_INVITEES);

        List<UserContact> userContactList = new ArrayList<>();
        String[] contacts = contactString.split(", ");
        for (String contact : contacts) {
            String[] contactDetails = contact.split(";");
            userContactList.add(new UserContact(contactDetails[0], contactDetails[1], contactDetails[2]));
        }

        txtSelectionCount = findViewById(R.id.txt_selection_count);
        inviteeRecyclerView = findViewById(R.id.listView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        inviteeRecyclerView.setHasFixedSize(true);
        // add divider
        inviteeRecyclerView.addItemDecoration(new DividerItemDecoration(
                this, LinearLayoutManager.VERTICAL));

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        inviteeRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new InviteeListAdapter(userContactList);
        inviteeRecyclerView.setAdapter(mAdapter);

        mSelectionTracker = new SelectionTracker.Builder<>(
                "contact-items-selection",
                inviteeRecyclerView,
                new UserContactItemKeyProvider(1, userContactList),
                new UserContactItemDetailLookup(inviteeRecyclerView),
                StorageStrategy.createParcelableStorage(UserContact.class)
        ).withSelectionPredicate(SelectionPredicates.<UserContact>createSelectAnything()).build();

        mSelectionTracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onItemStateChanged(@NonNull Object key, boolean selected) {
                super.onItemStateChanged(key, selected);
            }

            @Override
            public void onSelectionRefresh() {
                super.onSelectionRefresh();
                txtSelectionCount.setText("Selection Count: 0");
            }

            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();

                if (mSelectionTracker.hasSelection()) {
                    txtSelectionCount.setText(String.format("Selection Count: %d", mSelectionTracker.getSelection().size()));
                } else {
                    txtSelectionCount.setText("Selection Count: 0");
                }
            }

            @Override
            public void onSelectionRestored() {
                super.onSelectionRestored();
                txtSelectionCount.setText("Selection Count: 0");
            }
        });


        mAdapter.setSelectionTracker(mSelectionTracker);

        if (savedInstanceState != null)
            mSelectionTracker.onRestoreInstanceState(savedInstanceState);
    }

    public void sendSMSMessage(View view) {
        // TODO Create sms templates using text files
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("6505551212", null,
                "Hi, my name is Luke!", null, null);
        Toast.makeText(getApplicationContext(), "SMS Sent.", Toast.LENGTH_LONG).show();
    }

    public void sendEmail(View view) {
        // TODO Create email templates using text files
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test Email Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi, this is a test email");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nguye2_anh@bentley.edu"});

        //need this to prompts email client only
        emailIntent.setType("message/rfc822");

        startActivity(Intent.createChooser(emailIntent, "Choose an Email client:"));
    }

    public void dial(View view) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mSelectionTracker.onSaveInstanceState(outState);
    }
}
