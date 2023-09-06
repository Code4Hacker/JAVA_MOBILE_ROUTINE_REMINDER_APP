package com.geminichild.medicinereminder.dashboardfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.geminichild.medicinereminder.R;
import com.geminichild.medicinereminder.TaskAdapter;
import com.geminichild.medicinereminder.TaskModel;
import com.geminichild.medicinereminder.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class OthersFragment extends Fragment {
    private RecyclerView taskrecycle;
    private RequestQueue requestQueue;
    private List<TaskModel> taskList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_others, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskrecycle = (RecyclerView) view.findViewById(R.id.task_recycle);
        taskrecycle.setHasFixedSize(true);
        taskrecycle.setLayoutManager(new LinearLayoutManager(getActivity()));

        requestQueue = VolleySingleton.getmInstance(getActivity()).getRequestQueue();
        taskList = new ArrayList<>();
        fetchTasks();
    }


    public void fetchTasks() {
        String requesting_url = "http://192.168.138.1/medical_Reminder/content_post.php?UserId=1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requesting_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String findstatus = jsonObject.getString("status");
                    JSONObject status = new JSONObject(findstatus);
                    String getstatus = status.getString("success").toString();
                    if(getstatus.equals("200")){

                        String taskcontentHeader = jsonObject.getString("contents");
                        JSONArray jsonArray = new JSONArray(taskcontentHeader);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String title = jsonObject1.getString("ActivityTitle");
                            String description = jsonObject1.getString("ActivityDescription");
                            String notifyTime = jsonObject1.getString("NotifyTime");
                            String taskid = jsonObject1.getString("ActivityId");
                            String taskComplete = jsonObject1.getString("taskComplete");

                           TaskModel taskModel = new TaskModel(title, description, notifyTime, taskid, taskComplete);
                           taskList.add(taskModel);

                        }
                    }else {
                        Log.i("REQUEST QUESSS", "400");
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                TaskAdapter taskAdapter = new TaskAdapter(getActivity(), taskList);
                taskrecycle.setAdapter(taskAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response Errors", error.toString());
            }
        });
        requestQueue.add(stringRequest);
    }
}