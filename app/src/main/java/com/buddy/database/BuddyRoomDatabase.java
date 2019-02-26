package com.buddy.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.buddy.dao.CategoryDao;
import com.buddy.dao.TaskDao;
import com.buddy.entity.Category;
import com.buddy.entity.Task;

@Database(entities = {Task.class, Category.class}, version = 2)
public abstract class BuddyRoomDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
    public abstract CategoryDao categoryDao();

    private static volatile BuddyRoomDatabase INSTANCE;

    private static final String DATABASE_NAME = "buddy_database";

    public static BuddyRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BuddyRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BuddyRoomDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static BuddyRoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;
        private CategoryDao categoryDao;

        private PopulateDbAsyncTask(BuddyRoomDatabase db) {
            taskDao = db.taskDao();
            categoryDao = db.categoryDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Category category = new Category("Work");
            categoryDao.insert(category);
            Task task = new Task("Task 1", "Description 1");
            task.setCategoryId(categoryDao.getAllCategories().getValue().get(0).getId());
            taskDao.insert(new Task("Task 1", "Description 1"));
            return null;
        }
    }
}
