package com.buddy.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.buddy.entity.Category;
import com.buddy.repository.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;
    private List<Category> allCategories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        categoryRepository = new CategoryRepository(application);
        allCategories = categoryRepository.getAllCategories();
    }

    public List<Category> getAllCategories() { return allCategories; }
    public void insert(Category category) { categoryRepository.insert(category); }
    public void update(Category category) { categoryRepository.update(category); }
    public void delete(Category category) { categoryRepository.delete(category); }
    public void deleteAll() { categoryRepository.deleteAllCategories(); }
    public Category findCategoryById(int categoryId) { return categoryRepository.findCategoryById(categoryId); }
}
