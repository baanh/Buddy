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
        new InsertAsyncTask(taskDao).execute(task);
    }

    public void update(Task task) {
        new UpdateAsyncTask(taskDao).execute(task);
    }

    public void delete(Task task) {
        new DeleteTaskAsyncTask(taskDao).execute(task);
    }

    public void deleteAllTasks() {
        new DeleteAllTasksAsyncTask(taskDao).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        InsertAsyncTask(TaskDao dao) { taskDao = dao; }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        UpdateAsyncTask(TaskDao dao) {
            taskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        DeleteTaskAsyncTask(TaskDao dao) {
            taskDao = dao;
        }

        @Override
        protected Void doInBackground(final Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }

    private static class DeleteAllTasksAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;

        DeleteAllTasksAsyncTask(TaskDao dao) {
            taskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            taskDao.deleteAll();
            return null;
        }
    }
}
