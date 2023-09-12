package com.geminichild.medicinereminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.geminichild.medicinereminder.dashboardfragments.AlarmsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        holder.taskid.setText(taskModel.getActivityId().toString());
        if(taskModel.getTaskComplete().toString().equals("true")){
            holder.completed.setChecked(true);

        }else {
            holder.completed.setChecked(false);
        }
        holder.notifytime.setText(taskModel.getNotifyTime().toString());
        holder.completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                final String taskCompleted = String.valueOf(b);
                final String userTask = holder.taskid.getText().toString();

                final String request_code_url ="http://192.168.138.1/medical_reminder/update_task.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, request_code_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String responded_cast = jsonObject.getString("success").toString();
                            if(responded_cast.equals("200")) {
                                Toast.makeText(context, "Task Updated Successful!", Toast.LENGTH_SHORT).show();
                            }else{
                                    Toast.makeText(context, "404", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("taskComplete", taskCompleted);
                        params.put("UserTask", userTask);

                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
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
        TextView tasktitle, description, notifytime, taskid;
        CheckBox completed;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            tasktitle = itemView.findViewById(R.id.task_title);
            description = itemView.findViewById(R.id.task_description);
            completed = itemView.findViewById(R.id.task_complete);
            notifytime = itemView.findViewById(R.id.time_remindered);
            taskid = itemView.findViewById(R.id.taskId);
        }
    }
}
