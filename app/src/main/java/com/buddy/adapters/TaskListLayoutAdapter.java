package com.buddy.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.buddy.entity.Task;
import com.buddy.main.R;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TaskListLayoutAdapter extends RecyclerView.Adapter<TaskListLayoutAdapter.TaskListViewHolder> {

    private List<Task> taskList;

    public static class TaskListViewHolder extends RecyclerView.ViewHolder {

        // public ImageView taskImage;
        public TextView taskName;
        public TextView taskDescription;
        // public TextView taskSchedule;

        public TaskListViewHolder(View view) {
            super(view);
            taskName = (TextView) view.findViewById(R.id.task_name);
            taskDescription = (TextView) view.findViewById(R.id.task_description);
            // taskSchedule = (TextView) view.findViewById(R.id.task_schedule);
        }
    }

    public TaskListLayoutAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @Override
    public TaskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_row, parent, false);
        return new TaskListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TaskListViewHolder viewHolder, int position) {
        Task task = taskList.get(position);
        viewHolder.taskName.setText(task.getName());
        viewHolder.taskDescription.setText(task.getDescription());
        // viewHolder.taskSchedule.setText(task.getStartDate().toString() + task.getEndDate().toString());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}

