package com.geminichild.medicinereminder.dashboardfragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.geminichild.medicinereminder.Dashboard;
import com.geminichild.medicinereminder.R;
import com.geminichild.medicinereminder.databinding.FragmentAlarmsBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AlarmsFragment extends Fragment {
    Button addAlarm, apply, cancel, btn_date;
    EditText headline, description;
    TextView preview;
    MaterialTimePicker materialTimePicker;
    String notified;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    final String requesting_url = "http://192.168.138.1/medical_reminder/content_post.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alarms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
                requestQueue = Volley.newRequestQueue(getActivity());

                addAlarm = (Button) view.findViewById(R.id.setAlarm);
                View add_task = LayoutInflater.from(getActivity()).inflate(R.layout.add_new_task, null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setView(add_task);

                final AlertDialog dialogTask = alertDialog.create();
                dialogTask.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                btn_date = (Button) add_task.findViewById(R.id.notifytime_add);
                headline = (EditText) add_task.findViewById(R.id.title_add);
                description  = (EditText) add_task.findViewById(R.id.description_add);
                preview = (TextView) add_task.findViewById(R.id.preview);


                apply = (Button) add_task.findViewById(R.id.apply);
                cancel = (Button) add_task.findViewById(R.id.cancel);

                btn_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialTimePicker = new MaterialTimePicker.Builder()
                                .setTimeFormat(TimeFormat.CLOCK_12H)
                                .setHour(12)
                                .setMinute(0)
                                .setTitleText("Select Alarm Time")
                                .build();
                        materialTimePicker.show(getActivity().getSupportFragmentManager(), "geminichild");
                        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(materialTimePicker.getHour() > 12){
                                    if(materialTimePicker.getMinute() > 9){
                                        preview.setText(materialTimePicker.getHour()+":"+materialTimePicker.getMinute()+" PM");
                                    }else {
                                        preview.setText(materialTimePicker.getHour()+":0"+materialTimePicker.getMinute()+ "PM");
                                    }
                                }else{
                                    if(materialTimePicker.getMinute() > 9){
                                        preview.setText(materialTimePicker.getHour()+":"+materialTimePicker.getMinute()+" AM");
                                    }else {
                                        preview.setText(materialTimePicker.getHour()+":0"+materialTimePicker.getMinute()+ " AM");
                                    }
                                }
                            }
                        });
                    }
                });
                addAlarm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogTask.show();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogTask.cancel();
                    }
                });
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        postAndGetTasks();
                    }
                });



    }
    private void postAndGetTasks() {
        final String activitytitle = headline.getText().toString().trim();
        final String activitydescr = description.getText().toString().trim();
        final String notified_at =  preview.getText().toString().trim();
        stringRequest = new StringRequest(Request.Method.POST, requesting_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String results = jsonObject.getString("success").toString();

                    if(results.equals("200")){
                        Toast.makeText(getActivity(), "sent", Toast.LENGTH_SHORT).show();
                        headline.setText("");
                        description.setText("");
                        preview.setText("");
//                       OthersFragment othersFragment = (OthersFragment) getActivity().getSupportFragmentManager().findFragmentByTag("OthersFragment");
//                       if(othersFragment ==null){
//                           Log.e("GTTSYSYYSYSYSYS",othersFragment.toString());
//                       }else {
//                           othersFragment.fetchTasks();
//                       }

                    }else{
                        Toast.makeText(getActivity(), "not sent", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR OCCURED", error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();
                params.put("ActivityTitle", String.valueOf(activitytitle));
                params.put("ActivityDescription", String.valueOf(activitydescr));
                params.put("NotifyTime", String.valueOf(notified_at));
                params.put("taskComplete", "false");
                params.put("NotifyDate", "13 June 2024");
                params.put("UserId", "1");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
