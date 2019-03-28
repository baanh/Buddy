package com.buddy.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.buddy.dao.TaskDao;
import com.buddy.database.BuddyRoomDatabase;
import com.buddy.entity.Task;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository(Application application) {
        BuddyRoomDatabase db = BuddyRoomDatabase.getDatabase(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
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

    public List<Task> findTasksBetweenDate(Date from, Date to) {
        List<Task> tasks = null;
        try {
            tasks = new FindTasksBetweenDateAsyncTask(taskDao).execute(from, to).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    private static class FindTasksBetweenDateAsyncTask extends AsyncTask<Date, Void, List<Task>> {
        private TaskDao taskDao;

        FindTasksBetweenDateAsyncTask(TaskDao dao) { taskDao = dao; }

        @Override
        protected List<Task> doInBackground(Date... dates) {
            return taskDao.findTasksBetweenDate(dates[0], dates[1]);
        }
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
