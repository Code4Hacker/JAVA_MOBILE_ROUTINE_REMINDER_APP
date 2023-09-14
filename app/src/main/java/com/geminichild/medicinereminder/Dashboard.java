package com.geminichild.medicinereminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.geminichild.medicinereminder.dashboardfragments.AlarmsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONException;
import org.json.JSONObject;

public class Dashboard extends AppCompatActivity {
    public ViewPager2 viewPager2;
    BottomNavigationView bottomNavigationView;
    TextView username, emaiDashboard;

    String email_retrived;
    String pwd;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        username = (TextView) findViewById(R.id.profilename);
        emaiDashboard = (TextView) findViewById(R.id.emaiDashboard);
        viewPager2 = (ViewPager2) findViewById(R.id.dashboardview);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        DashBoardAdapter dashBoardAdapter = new DashBoardAdapter(this);
        viewPager2.setAdapter(dashBoardAdapter);
        if(savedInstanceState == null){
            viewPager2.setCurrentItem(1);
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
            Bundle extrasInfo = getIntent().getExtras();
            if(extrasInfo == null){
                email_retrived = null;
                pwd = null;
            }
            email_retrived = extrasInfo.getString("userMail");
            pwd = extrasInfo.getString("passcode");
            getFullUserInfo();
        }else{
            email_retrived = (String) savedInstanceState.getSerializable("userMail");
            pwd = (String) savedInstanceState.getSerializable("passcode");
        }
        getFullUserInfo();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.others){
                    viewPager2.setCurrentItem(0);
                    return true;
                }else if(item.getItemId() == R.id.alarms){
                    viewPager2.setCurrentItem(1);
                    return true;
                }if(item.getItemId() == R.id.profile){
                    viewPager2.setCurrentItem(2);
                    return true;
                }
                return false;
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                bottomNavigationView;
            }
        });
    }

    public void getFullUserInfo(){
        RequestUrls requestUrls = new RequestUrls();

        final String mailget = email_retrived.toString().trim();
        final String codepass = pwd.toString().trim();
        Toast.makeText(Dashboard.this, requestUrls.mainUrl(), Toast.LENGTH_LONG).show();
        final String request_get_user_url = requestUrls.mainUrl()+"grabin.php?mail_post="+mailget+"&passcode="+codepass;
        requestQueue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.GET, request_get_user_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String results = jsonObject.getString("status");
                   JSONObject status_respond = new JSONObject(results);
                   String status_res = status_respond.getString("success").toString();
                   if(status_res.equals("200")){

                       String usercontainer = jsonObject.getString("user");
                       JSONObject userdata = new JSONObject(usercontainer);
                       username.setText("Hi, " + userdata.getString("Fullname").toString());
                       emaiDashboard.setText(userdata.getString("User_email").toString());
                       SharedPreferences sharedPreferences = getSharedPreferences("userId", MODE_PRIVATE);
                       SharedPreferences.Editor editor = sharedPreferences.edit();
                       editor.putString("user", userdata.getString("Id"));
                       editor.apply();

                   }else{
                       Toast.makeText(Dashboard.this, "Status Invalid", Toast.LENGTH_SHORT).show();
                       Log.i("UserNOT  Credenial", status_res.toString());

                   }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error On Dashboard", error.toString());
            }
        });
        requestQueue.add(stringRequest);
    }
}