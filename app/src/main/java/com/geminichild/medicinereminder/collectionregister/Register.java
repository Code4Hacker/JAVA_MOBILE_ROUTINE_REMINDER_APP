package com.geminichild.medicinereminder.collectionregister;
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
import com.geminichild.medicinereminder.R;
import com.geminichild.medicinereminder.Registration;
import com.geminichild.medicinereminder.RegistrationAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends Fragment {
    Button new_btn;
    EditText full_name, email, passcode, confirm_pwd;
    RequestQueue requestQueue;

    final String request_register = "http://192.168.21.138/medical_reminder/grabup.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        full_name = (EditText) view.findViewById(R.id.fullname);
        email = (EditText) view.findViewById(R.id.email);
        passcode = (EditText) view.findViewById(R.id.passcode);
        confirm_pwd = (EditText) view.findViewById(R.id.confirm_pwd);
        new_btn = (Button) view.findViewById(R.id.signupBtn);

        new_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (full_name.getText().toString().length() < 6){
                    Toast.makeText(getActivity(), "Invalid Full Name", Toast.LENGTH_SHORT).show();
                } else if(email.getText().toString().length() < 10){
                    Toast.makeText(getActivity(), "Enter Correct Email", Toast.LENGTH_SHORT).show();
                } else if (passcode.getText().toString().length() < 6){
                    Toast.makeText(getActivity(), "Password low length", Toast.LENGTH_SHORT).show();
                }else if (!(confirm_pwd.getText().toString().equals(passcode.getText().toString()))){
                    Toast.makeText(getActivity(), "Password Not Match", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    postNewUser();
                }
            }
        });

    }
    private void postNewUser(){
        requestQueue = Volley.newRequestQueue(getActivity());
        final String fullname = full_name.getText().toString().trim();
        final String mail = email.getText().toString().trim();
        final String code_pass = passcode.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, request_register, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("success").toString();
                    if (result.equals("1")){
                        full_name.setText("");
                        email.setText("");
                        passcode.setText("");
                        confirm_pwd.setText("");
                        ((Registration) requireActivity()).navigatetoLogin();
                    }else{
                        Log.i("NOT RESULT MSQLI",result);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("RESULT MSQLI",error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", String.valueOf(fullname));
                params.put("mail_post", String.valueOf(mail));
                params.put("passcode", String.valueOf(code_pass));

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}