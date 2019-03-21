package com.buddy.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import com.buddy.dao.CategoryDao;
import com.buddy.database.BuddyRoomDatabase;
import com.buddy.entity.Category;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CategoryRepository {
    private CategoryDao categoryDao;
    private List<Category> allCategories;

    public CategoryRepository(Application application) {
        BuddyRoomDatabase db = BuddyRoomDatabase.getDatabase(application);
        categoryDao = db.categoryDao();
    }

    public List<Category> getAllCategories() {
        try {
            allCategories = new GetAllAsyncTask(categoryDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allCategories;
    }

    public Category findCategoryById(int categoryId) {
        try {
            return new FindByIdAsyncTask(categoryDao).execute(categoryId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
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

    private static class GetAllAsyncTask extends  AsyncTask<Void, Void, List<Category>> {
        private CategoryDao categoryDao;

        GetAllAsyncTask(CategoryDao dao) { categoryDao = dao; }

        @Override
        protected List<Category> doInBackground(Void... voids) {
            return categoryDao.getAllCategories();
        }
    }

    private static class FindByIdAsyncTask extends AsyncTask<Integer, Category, Category> {
        private CategoryDao categoryDao;

        FindByIdAsyncTask(CategoryDao dao) { categoryDao = dao; }

        @Override
        protected Category doInBackground(Integer... ids) {
            return categoryDao.findCategoryById(ids[0]);
        }
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
