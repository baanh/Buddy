package com.buddy.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.buddy.entity.Task;
import com.buddy.repository.TaskRepository;

import java.util.Date;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        allTasks = taskRepository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() { return allTasks; }

    public void insert(Task task) { taskRepository.insert(task); }

    public void update(Task task) { taskRepository.update(task); }

    public void delete(Task task) { taskRepository.delete(task); }

    public void deleteAllTasks() { taskRepository.deleteAllTasks(); }

    public List<Task> findTasksBetweenDate(Date from, Date to) { return taskRepository.findTasksBetweenDate(from, to); }
}
