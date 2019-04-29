package com.buddy.adapters;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.buddy.entity.Task;
import com.buddy.main.R;

import android.widget.TextView;

import java.util.Calendar;

/*
 * List Adapter for Task List RecycleView extends ListAdapter to deal with large dataset
 * Plus the use of DiffUtil class to add animations when user interacts with the list
 */
public class TaskListAdapter extends ListAdapter<Task, TaskListAdapter.TaskViewHolder> {
    private OnItemClickListener itemClickListener;

    public TaskListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getDescription().equals(newItem.getDescription());
        }
    };

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public Task getTaskAt(int position) {
        return getItem(position);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView taskName;
        private TextView taskDescription;
        private TextView taskSchedule;

        TaskViewHolder(View view) {
            super(view);
            taskName = view.findViewById(R.id.task_name);
            taskDescription = view.findViewById(R.id.task_description);
            taskSchedule = view.findViewById(R.id.task_schedule);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (itemClickListener != null && position != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_row, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder viewHolder, int position) {
        Task task = getItem(position);
        viewHolder.taskName.setText(task.getName());
        viewHolder.taskDescription.setText(task.getDescription());
        Calendar cal = Calendar.getInstance();
        cal.setTime(task.getStartDate());
        viewHolder.taskSchedule.setText(cal.get(Calendar.MONTH) + "/"
                + cal.get(Calendar.DAY_OF_MONTH) + "/"
                + cal.get(Calendar.YEAR));
    }
}

