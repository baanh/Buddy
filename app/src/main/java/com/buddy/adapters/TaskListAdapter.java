package com.buddy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.buddy.entity.Task;
import com.buddy.main.R;

import android.widget.TextView;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        public TextView taskName;
        public TextView taskDescription;

        public TaskViewHolder(View view) {
            super(view);
            taskName = (TextView) view.findViewById(R.id.task_name);
            taskDescription = (TextView) view.findViewById(R.id.task_description);
        }
    }

    private final LayoutInflater mInflater;
    private List<Task> mTasks;

    public TaskListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_row, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder viewHolder, int position) {
        if (mTasks != null) {
            Task task = mTasks.get(position);
            viewHolder.taskName.setText(task.getName());
            viewHolder.taskDescription.setText(task.getDescription());
        } else {
            // Covers the case of data not being ready yet.
            viewHolder.taskName.setText("No task");
        }

    }

    public void setTasks(List<Task> tasks) {
        this.mTasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mTasks != null) {
            return mTasks.size();
        }
        return 0;
    }
}

