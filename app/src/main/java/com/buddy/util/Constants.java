package com.buddy.util;

public final class Constants {
    public static final String EXTRA_END_DATE = "com.buddy.tasklistsql.EXTRA_END_DATE";
    public static final String EXTRA_ID = "com.buddy.tasklistsql.EXTRA_ID";
    public static final String EXTRA_NAME = "com.buddy.tasklistsql.EXTRA_NAME";
    public static final String EXTRA_DESC = "com.buddy.tasklistsql.EXTRA_DESC";
    public static final String EXTRA_CATEGORY_ID = "com.buddy.tasklistsql.EXTRA_CATEGORY_ID";
    public static final String EXTRA_START_DATE = "com.buddy.tasklistsql.EXTRA_START_DATE";
    public static final String EXTRA_NOTES = "com.buddy.tasklistsql.EXTRA_NOTES";
    public static final String EXTRA_ADDRESS = "com.buddy.tasklistsql.EXTRA_ADDRESS";
    public static final String EXTRA_INVITEES = "com.buddy.tasklistsql.EXTRA_INVITEES";
    public static final String EXTRA_REPLY_NAME = "com.buddy.tasklistsql.REPLY_NAME";
    public static final String EXTRA_REPLY_DESC = "com.buddy.tasklistsql.REPLY_DESC";
    public static final String EXTRA_REPLY_START_DATE = "com.buddy.tasklistsql.EXTRA_REPLY_START_DATE";
    public static final String EXTRA_REPLY_END_DATE = "com.buddy.tasklistsql.EXTRA_REPLY_END_DATE";
    public static final String EXTRA_REPLY_ADDRESS = "com.buddy.tasklistsql.EXTRA_REPLY_ADDRESS";
    public static final String EXTRA_REPLY_INVITEES = "com.buddy.tasklistsql.EXTRA_REPLY_INVITEES";
    public static final int NEW_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;
    public static final int GET_MAP_REQUEST = 3;
    public static final int AUTOCOMPLETE_THRESHOLD = 1;
    public static final String EXTRA_TIME_LOG = "com.buddy.tasklistsql.EXTRA_TIME_LOG";

    public static final String[] TASK_PRIORITY_ARRAY = new String[] {"High", "Medium", "Low"};

    // SMS & Email
    public static final String SMS_TEMPLATE = "From your team:\n" +
            "Task: [task_name]\n" +
            "Due: [task_due]\n" +
            "Description: [task_description]\n" +
            "Thanks!";
    public static final String EMAIL_TEMPLATE = "Hi there!\n" +
            "\n" +
            "You have new message from your team on Buddy:\n" +
            "\n" +
            "Task: [task_name]\n" +
            "Due: [task_due]\n" +
            "Description: [task_description]\n" +
            "\n" +
            "Thank you and good luck!\n" +
            "Buddy";
}
