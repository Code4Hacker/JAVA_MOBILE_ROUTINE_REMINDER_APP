package com.geminichild.medicinereminder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import android.content.Intent;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {
    Button returnDashboard;
    EditText updatename, updatepassword, updatecontact, updateemail, oldpassword;
    TextView tasks,editicon;
    LinearLayout logout;
    String passcodesintent, id, path,encodeimg, urlimg;
    ImageView imageView;



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
        editicon = findViewById(R.id.edit_profile);
        logout = findViewById(R.id.logout);
        imageView = findViewById(R.id.shapeableImageView);


        updatename.setText(profileInfo.getString("username"));
        updateemail.setText(profileInfo.getString("useremail"));
        updatecontact.setText(profileInfo.getString("contact"));
        passcodesintent = profileInfo.getString("password");
        id = profileInfo.getString("id");
        urlimg = profileInfo.getString("profileimg");
        tasks.setText(profileInfo.getString("taskcount"));
        Picasso.get().load(urlimg).placeholder(R.drawable.profileimg).error(R.drawable.profileimg).into(imageView);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UpdateProfile.this, Registration.class));
                finish();
            }
        });
        editicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent,2);
                }else{
                    ActivityCompat.requestPermissions(UpdateProfile.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            Context context = UpdateProfile.this;

            path = RealPathUtil.getRealPath(context, uri);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            imageView.setImageBitmap(bitmap);
            Toast.makeText(context, uri.toString(), Toast.LENGTH_SHORT).show();
            encodetobitmap(bitmap);

        }
    }
    private void encodetobitmap(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteofimage = byteArrayOutputStream.toByteArray();
        encodeimg = android.util.Base64.encodeToString(byteofimage, Base64.DEFAULT);

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
                            finish();
                            Intent dashboardIntent = new Intent(UpdateProfile.this, Dashboard.class);
                            dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(dashboardIntent);


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
                Log.e("Error Volley", error.toString());
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
                params.put("img", encodeimg);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}