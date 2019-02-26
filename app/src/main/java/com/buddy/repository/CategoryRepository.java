package com.buddy.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import com.buddy.dao.CategoryDao;
import com.buddy.database.BuddyRoomDatabase;
import com.buddy.entity.Category;

import java.util.List;

public class CategoryRepository {
    private CategoryDao categoryDao;
    private LiveData<List<Category>> allCategories;

    public CategoryRepository(Application application) {
        BuddyRoomDatabase db = BuddyRoomDatabase.getDatabase(application);
        categoryDao = db.categoryDao();
        allCategories = categoryDao.getAllCategories();
    }

    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }

    public void insert(Category taskCategory) {
        new CategoryRepository.InsertAsyncTask(categoryDao).execute(taskCategory);
    }

    public void update(Category taskCategory) {
        new CategoryRepository.UpdateAsyncTask(categoryDao).execute(taskCategory);
    }

    public void delete(Category taskCategory) {
        new CategoryRepository.DeleteTaskAsyncTask(categoryDao).execute(taskCategory);
    }

    public void deleteAllCategories() {
        new CategoryRepository.DeleteAllTasksAsyncTask(categoryDao).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao taskCategoryDao;

        InsertAsyncTask(CategoryDao dao) { taskCategoryDao = dao; }

        @Override
        protected Void doInBackground(Category... taskCategories) {
            taskCategoryDao.insert(taskCategories[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao taskCategoryDao;

        UpdateAsyncTask(CategoryDao dao) {
            taskCategoryDao = dao;
        }

        @Override
        protected Void doInBackground(final Category... taskCategories) {
            taskCategoryDao.update(taskCategories[0]);
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoryDao taskCategoryDao;

        DeleteTaskAsyncTask(CategoryDao dao) {
            taskCategoryDao = dao;
        }

        @Override
        protected Void doInBackground(final Category... taskCategories) {
            taskCategoryDao.delete(taskCategories[0]);
            return null;
        }
    }

    private static class DeleteAllTasksAsyncTask extends AsyncTask<Void, Void, Void> {
        private CategoryDao taskCategoryDao;

        DeleteAllTasksAsyncTask(CategoryDao dao) {
            taskCategoryDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            taskCategoryDao.deleteAll();
            return null;
        }
    }
}
