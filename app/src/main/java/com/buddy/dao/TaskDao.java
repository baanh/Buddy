package com.buddy.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.buddy.entity.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Query("DELETE FROM task")
    void deleteAll();

    @Query("SELECT * from task ORDER BY name ASC")
    LiveData<List<Task>> getAllTask();
}
