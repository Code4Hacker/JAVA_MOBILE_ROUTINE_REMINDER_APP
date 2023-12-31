package com.geminichild.medicinereminder.dashboardfragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.geminichild.medicinereminder.Dashboard;
import com.geminichild.medicinereminder.R;
import com.geminichild.medicinereminder.Registration;
import com.geminichild.medicinereminder.RequestUrls;
import com.geminichild.medicinereminder.UpdateProfile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    Button edit;

    RequestUrls requestUrls = new RequestUrls();
    TextView username, useremail, usercontact, taskcount;
    String passcodes, url, id;
    ImageView profileimg, banner;
    LinearLayout logout;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username =  view.findViewById(R.id.username);
        useremail =  view.findViewById(R.id.useremail);
        usercontact =  view.findViewById(R.id.usercontact);
        taskcount = view.findViewById(R.id.tasksnumber);
        logout = view.findViewById(R.id.logout);
        profileimg = view.findViewById(R.id.shapeableImageView);
        banner = view.findViewById(R.id.banner);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Registration.class));
                getActivity().finish();
            }
        });

        getUserdata();
        taskGet();
        edit = view.findViewById(R.id.editprofile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getnewside();
            }
        });
    }

    private void getnewside() {
        intent = new Intent(getActivity(), UpdateProfile.class);
        intent.putExtra("username", String.valueOf(username.getText()));
        intent.putExtra("useremail", String.valueOf(useremail.getText()));
        intent.putExtra("contact", String.valueOf(usercontact.getText()));
        intent.putExtra("taskcount", taskcount.getText().toString());
        intent.putExtra("password", passcodes);
        intent.putExtra("profileimg", url);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void getUserdata() {

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("credential", Context.MODE_PRIVATE);
        final String passcode = sharedPreferences.getString("passcode","");
        final String email = sharedPreferences.getString("email","");
        if(!(passcode.equals("") && email.equals(""))) {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String requestingUrl = requestUrls.mainUrl() + "grabin.php?mail_post="+email+"&passcode="+passcode;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, requestingUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        JSONObject jsonObject1 = new JSONObject(status);
                        String statuscode = jsonObject1.getString("success");

                        switch(statuscode){
                            case "200":
                                String userArray = jsonObject.getString("user");
                                JSONObject jsonObject2 = new JSONObject(userArray);
                                username.setText(jsonObject2.getString("Fullname"));
                                useremail.setText(jsonObject2.getString("User_email"));
                                usercontact.setText(jsonObject2.getString("Phone"));
                                passcodes = jsonObject2.getString("Passcode");
                                url = requestUrls.mainUrl() + "IMAGES/" + jsonObject2.getString("Profile_img");
                                Picasso.get().load(url).placeholder(R.drawable.profileimg).error(R.drawable.profileimg).into(profileimg);
                                Picasso.get().load(url).placeholder(R.drawable.profileimg).error(R.drawable.profileimg).into(banner);
                                id = jsonObject2.getString("Id");

                                break;
                            case "404":
                                Toast.makeText(getActivity(), "User Not Found or Connection Error", Toast.LENGTH_SHORT).show();

                                break;
                            default:
                                Toast.makeText(getActivity(), "No Command", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

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
//    My Tasks
    private void taskGet(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
        String iduser = sharedPreferences.getString("user", "");
        if (!iduser.equals("")){
            String requescodes = requestUrls.mainUrl()+"update_task.php?user="+iduser;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, requescodes, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String task = jsonObject.getString("Tasks").toString();
                        Toast.makeText(getActivity(), task.toString(), Toast.LENGTH_SHORT).show();
                        if (task.length() > -1){
                            taskcount.setText("Tasks "+task);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", error.toString());
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }

    }
}
