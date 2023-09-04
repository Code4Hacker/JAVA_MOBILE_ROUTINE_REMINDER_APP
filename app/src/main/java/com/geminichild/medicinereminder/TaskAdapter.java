package com.geminichild.medicinereminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private int lastpos = -1;
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
        if(taskModel.getTaskComplete().toString().equals("true")){
            holder.completed.setChecked(true);
        }else {
            holder.completed.setChecked(false);
        }
        holder.notifytime.setText(taskModel.getNotifyTime().toString());
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View itemView, int position) {
        if(position > lastpos)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.startAnimation(animation);
            lastpos = position;
        }else {
            lastpos = 0;
        }
    }

    @Override
    public int getItemCount() {
        return taskModelList.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {
        TextView tasktitle, description, notifytime;
        CheckBox completed;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            tasktitle = itemView.findViewById(R.id.task_title);
            description = itemView.findViewById(R.id.task_description);
            completed = itemView.findViewById(R.id.task_complete);
            notifytime = itemView.findViewById(R.id.time_remindered);
        }
    }
}
