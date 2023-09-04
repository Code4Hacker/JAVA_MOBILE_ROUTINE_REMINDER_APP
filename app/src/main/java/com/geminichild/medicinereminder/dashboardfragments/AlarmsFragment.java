package com.geminichild.medicinereminder.dashboardfragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.geminichild.medicinereminder.R;
import com.geminichild.medicinereminder.databinding.FragmentAlarmsBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.HashMap;
import java.util.Map;

public class AlarmsFragment extends Fragment {
    Button addAlarm, apply, cancel, btn_date;
    EditText headline, description;
    MaterialTimePicker materialTimePicker;
    String notified;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    final String requesting_url = "http://192.168.59.138/medical_reminder/content_post.php";

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
                                Toast.makeText(getActivity(), materialTimePicker.getHour()+":"+materialTimePicker.getMinute(), Toast.LENGTH_SHORT).show();
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
    private void getAlarmsNotify(){
    }
    private void postAndGetTasks() {

        stringRequest = new StringRequest(Request.Method.POST, requesting_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();

                return super.getParams();
            }
        };
    }
}
