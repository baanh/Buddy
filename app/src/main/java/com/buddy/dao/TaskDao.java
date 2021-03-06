package com.buddy.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.buddy.entity.Task;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM task")
    void deleteAll();

    @Query("SELECT * FROM task")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task WHERE id = :taskId")
    Task findTaskById(String taskId);

    @Query("SELECT * FROM task WHERE startDate BETWEEN :from AND :to")
    List<Task> findTasksBetweenDate(Date from, Date to);
}
