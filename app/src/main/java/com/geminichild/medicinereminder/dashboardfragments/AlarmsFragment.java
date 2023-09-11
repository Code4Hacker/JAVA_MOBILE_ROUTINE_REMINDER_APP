package com.geminichild.medicinereminder.dashboardfragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import com.geminichild.medicinereminder.AlarmReceiver;
import com.geminichild.medicinereminder.Dashboard;
import com.geminichild.medicinereminder.R;
import com.geminichild.medicinereminder.databinding.FragmentAlarmsBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AlarmsFragment extends Fragment {
    Button addAlarm, apply, cancel, btn_date;
    EditText headline, description;
    TextView preview;
    MaterialTimePicker materialTimePicker;
    AlertDialog.Builder alertDialog;
    String notified;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    Calendar calendar;
    AlarmManager alarmManager;
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
                alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setView(add_task);

                final AlertDialog dialogTask = alertDialog.create();
                dialogTask.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                btn_date = (Button) add_task.findViewById(R.id.notifytime_add);
                headline = (EditText) add_task.findViewById(R.id.title_add);
                description  = (EditText) add_task.findViewById(R.id.description_add);
                preview = (TextView) add_task.findViewById(R.id.preview);


                apply = (Button) add_task.findViewById(R.id.apply);
                cancel = (Button) add_task.findViewById(R.id.cancel);
                createAlarm();
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
                                calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());
                                calendar.set(Calendar.MINUTE, materialTimePicker.getMinute());
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MILLISECOND, 0);

                                alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);

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
                        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
                        int requestCode = (int) System.currentTimeMillis();
                        postAndGetTasks(requestCode, intent, calendar);
                    }
                });



    }
    private void postAndGetTasks(int requestcode, Intent intent, Calendar calendar1) {
        final String activitytitle = headline.getText().toString().trim();
        final String activitydescr = description.getText().toString().trim();
        final String notified_at =  preview.getText().toString().trim();
        SimpleDateFormat formatme = new SimpleDateFormat("dd MMMM yyyy");
        String cal = formatme.format(new Date());
        Toast.makeText(getActivity(), cal.toString(), Toast.LENGTH_SHORT).show();
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
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), requestcode, intent, PendingIntent.FLAG_IMMUTABLE);
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                        Toast.makeText(getActivity(), "Alarmed", Toast.LENGTH_SHORT).show();

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
                params.put("NotifyDate", String.valueOf(cal));
                params.put("UserId", "1");
                params.put("RequestCode",  String.valueOf(requestcode));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void createAlarm(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "gemini";
            String dec = "Waking alarm";
            int important = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("geminichild", name, important);
            notificationChannel.setDescription(dec);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
