package com.geminichild.medicinereminder.dashboardfragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.geminichild.medicinereminder.Dashboard;
import com.geminichild.medicinereminder.R;
import com.geminichild.medicinereminder.RequestUrls;
import com.geminichild.medicinereminder.UpdateProfile;

public class ProfileFragment extends Fragment {
    Button edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserdata();
        edit = (Button) view.findViewById(R.id.editprofile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateProfile.class);
                startActivity(intent);
            }
        });
    }

    private void getUserdata() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("credential", Context.MODE_PRIVATE);
        final String passcode = sharedPreferences.getString("passcode","");
        final String email = sharedPreferences.getString("email","");
        if(!(passcode.equals("") && email.equals(""))) {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            RequestUrls requestUrls = new RequestUrls();
            String requestingUrl = requestUrls.mainUrl() + "grabin.php?mail_post="+email+"&passcode="+passcode;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, requestingUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(getActivity(), "Connection Error!", Toast.LENGTH_SHORT).show();
        }
    }


}
