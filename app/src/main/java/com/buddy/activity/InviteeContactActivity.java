package com.buddy.activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.buddy.entity.UserContact;
import com.buddy.main.R;
import com.buddy.util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InviteeContactActivity extends AppCompatActivity {
    private ListView inviteeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitee_contact);

        inviteeListView = findViewById(R.id.listView);

        Intent intent = getIntent();
        String taskName = intent.getStringExtra(Constants.EXTRA_NAME);
        Date startDate = new Date();
        startDate.setTime(intent.getLongExtra(Constants.EXTRA_START_DATE,-1));
        Date endDate = new Date();
        endDate.setTime(intent.getLongExtra(Constants.EXTRA_END_DATE,-1));
        String contactString = intent.getStringExtra(Constants.EXTRA_INVITEES);

        List<UserContact> userContactList = new ArrayList<>();
        String[] contacts = contactString.split(", ");
        for (String contact : contacts) {
            String[] contactDetails = contact.split(";");
            userContactList.add(new UserContact(contactDetails[0], contactDetails[1], contactDetails[2]));
        }
    }

    public void sendSMS(View view) {

    }

    public void sendEmail(View view) {

    }

    public void dial(View view) {

    }
}
