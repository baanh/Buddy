package com.buddy.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.buddy.dao.TaskDao;
import com.buddy.database.BuddyRoomDatabase;
import com.buddy.entity.Task;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository(Application application) {
        BuddyRoomDatabase db = BuddyRoomDatabase.getDatabase(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTask();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void insert(Task task) {
        new insertAsyncTask(taskDao).execute(task);
    }

    private static class insertAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao taskDao;

        insertAsyncTask(TaskDao dao) {
            taskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }
}
