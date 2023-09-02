package com.geminichild.medicinereminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private Context context;
    private List<TaskModel> taskModelList;

    public TaskAdapter(Context context, List<TaskModel> taskModelList) {
        this.context = context;
        this.taskModelList = taskModelList;
    }
    @NonNull
    @Override
    public TaskAdapter.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.task_container, parent, false);
       return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.TaskHolder holder, int position) {
        TaskModel taskModel = taskModelList.get(position);
        holder.tasktitle.setText(taskModel.getActivityTitle().toString());
        holder.description.setText(taskModel.getActivityDescription().toString());
        holder.completed.setText(taskModel.getTaskComplete().toString());
        holder.notifytime.setText(taskModel.getNotifyTime().toString());
    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {
        TextView tasktitle, description, completed, notifytime;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            tasktitle = itemView.findViewById(R.id.task_title);
            description = itemView.findViewById(R.id.task_description);
            completed = itemView.findViewById(R.id.task_complete);
            notifytime = itemView.findViewById(R.id.time_remindered);
        }
    }
}
