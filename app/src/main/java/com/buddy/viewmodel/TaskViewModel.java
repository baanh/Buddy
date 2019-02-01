package com.buddy.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.buddy.entity.Task;
import com.buddy.repository.TaskRepository;

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
}
