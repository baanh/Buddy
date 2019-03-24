package com.buddy.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.buddy.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    void insert(Category taskCategoryDao);

    @Update
    void update(Category taskCategoryDao);

    @Delete
    void delete(Category taskCategoryDao);

    @Query("DELETE FROM Category")
    void deleteAll();

    @Query("SELECT * FROM Category")
    List<Category> getAllCategories();

    @Query("SELECT * FROM Category WHERE id == :categoryId")
    Category findCategoryById(int categoryId);
}
