package com.geminichild.medicinereminder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import android.content.Intent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {
    Button returnDashboard;
    EditText updatename, updatepassword, updatecontact, updateemail, oldpassword;
    TextView tasks;
    LinearLayout logout;
    String passcodesintent, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Bundle profileInfo = getIntent().getExtras();
        updatename = (EditText) findViewById(R.id.upd_name);
        updateemail = (EditText) findViewById(R.id.upd_email);
        updatecontact = (EditText) findViewById(R.id.upd_contact);
        updatepassword = (EditText) findViewById(R.id.upd_pwd);
        oldpassword = (EditText) findViewById(R.id.old_pwd);
        tasks = findViewById(R.id.tasks);
        logout = findViewById(R.id.logout);


        updatename.setText(profileInfo.getString("username"));
        updateemail.setText(profileInfo.getString("useremail"));
        updatecontact.setText(profileInfo.getString("contact"));
        passcodesintent = profileInfo.getString("password");
        id = profileInfo.getString("id");
        tasks.setText(profileInfo.getString("taskcount"));

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateProfile.this, Registration.class));
                finish();
            }
        });



        returnDashboard = (Button) findViewById(R.id.returnDashboard);

        returnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updatename.getText().toString().equals("")){
                    Toast.makeText(UpdateProfile.this, "Username Should be filled!", Toast.LENGTH_SHORT).show();
                }else if(updateemail.getText().toString().equals("")){
                    Toast.makeText(UpdateProfile.this, "Email Should be filled!", Toast.LENGTH_SHORT).show();
                }else if( updatecontact.getText().toString().equals("")){
                    Toast.makeText(UpdateProfile.this, "Contact Should be filled!", Toast.LENGTH_SHORT).show();
                }else if(!(oldpassword.getText().toString().equals(passcodesintent))){
                    Toast.makeText(UpdateProfile.this, "Old Password is Invalid!", Toast.LENGTH_SHORT).show();
                }else if(updatepassword.getText().toString().length() < 7){
                    Toast.makeText(UpdateProfile.this, "New Password should Contain at least 6 Characters!", Toast.LENGTH_SHORT).show();
                }else{
                    updateUser();
                }
            }
        });
    }

    private void updateUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        RequestUrls requestUrls = new RequestUrls();
        String fullUrl = requestUrls.mainUrl()+"update_profile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, fullUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("success");

                    switch (status){
                        case "200":
                            Toast.makeText(UpdateProfile.this, "Update Success!", Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences = getSharedPreferences("credential", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", String.valueOf(updateemail.getText()));
                            editor.putString("passcode", String.valueOf(updatepassword.getText()));
                            editor.apply();
                            recreate();

                            break;
                        case "500":
                            Toast.makeText(UpdateProfile.this, "Internal Error, Check Connection Please! ", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(UpdateProfile.this, "We Can't Understand the Problem!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdateProfile.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("fullname", String.valueOf(updatename.getText()).trim());
                params.put("mail_post", String.valueOf(updateemail.getText()).trim());
                params.put("passcode", String.valueOf(updatepassword.getText()).trim());
                params.put("Phone", String.valueOf(updatecontact.getText()).trim());
                params.put("id", id);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}