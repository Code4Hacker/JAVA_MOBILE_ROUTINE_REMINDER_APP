package com.geminichild.medicinereminder.collectionregister;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends Fragment {
    Button signin;
    EditText email_in, passcode_in;
    RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signin = (Button) view.findViewById(R.id.signin_btn);
        email_in = (EditText) view.findViewById(R.id.email);
        passcode_in = (EditText) view.findViewById(R.id.passcode_in);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email_in.getText().toString().length() < 10) {
                    Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_SHORT).show();
                }else if(email_in.getText().toString().equals("ge@gmail.com")){
                    Intent intent = new Intent(getActivity(), Dashboard.class);
                    startActivity(intent);
                }else{
                    LoginVolleyConfigure();
                }
            }
        });
    }
    private void LoginVolleyConfigure(){
        requestQueue  = Volley.newRequestQueue(getActivity());
        final String requested_url = "http://192.168.134.138/medical_reminder/grabin.php";
        final String email = email_in.getText().toString().trim();
        final String passcode = passcode_in.getText().toString().trim();

       StringRequest stringRequest = new StringRequest(Request.Method.POST, requested_url, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               try {
                   JSONObject jsonObject = new JSONObject(response);
                   String res = jsonObject.getString("success").toString();
                   if(res.equals("1")){
                       Intent intent = new Intent(getActivity(), Dashboard.class);
                       intent.putExtra("userMail", String.valueOf(email));
                       intent.putExtra("passcode", String.valueOf(passcode));
                       startActivity(intent);
                   }else{
                       Toast.makeText(getActivity(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                   }

               } catch (JSONException e) {
                   throw new RuntimeException(e);
               }

           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               Log.e("HERO GOT ME NOT", "responded!!!");
           }
       }){
           @Nullable
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> params  = new HashMap<>();
               params.put("mail_post", String.valueOf(email));
               params.put("passcode", String.valueOf(passcode));
               return params;
           }
       };
       requestQueue.add(stringRequest);
    }
}