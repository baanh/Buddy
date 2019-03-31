package com.buddy.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.buddy.main.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskExpandableActivity extends AppCompatActivity {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_by_day);

        listView = (ExpandableListView)findViewById(R.id.lvExp);
        initData();
        listAdapter = new com.buddy.adapters.ExpandableListAdapter(this, listDataHeader, listHash);
        listView.setAdapter(listAdapter);
    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("Today's Task One");
        listDataHeader.add("Today's Task Two");
        listDataHeader.add("Today's Task Three");

        List<String> taskOne = new ArrayList<>();
        taskOne.add("Start time");
        taskOne.add("End time");
        taskOne.add("Location");
        taskOne.add("Description");
        taskOne.add("Notes");

        List<String> taskTwo = new ArrayList<>();
        taskTwo.add("Start time");
        taskTwo.add("End time");
        taskTwo.add("Location");
        taskTwo.add("Description");
        taskTwo.add("Notes");

        List<String> taskThree = new ArrayList<>();
        taskThree.add("Start time");
        taskThree.add("End time");
        taskThree.add("Location");
        taskThree.add("Description");
        taskThree.add("Notes");
    }
}
