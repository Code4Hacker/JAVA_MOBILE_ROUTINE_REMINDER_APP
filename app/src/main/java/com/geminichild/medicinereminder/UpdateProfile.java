package com.geminichild.medicinereminder;

import androidx.appcompat.app.AppCompatActivity;

//import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateProfile extends AppCompatActivity {
    Button returnDashboard;
    EditText updatename, updatepassword, updatecontact, updateemail, oldpassword;
    String passcodesintent;

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

        updatename.setText(profileInfo.getString("username"));
        updateemail.setText(profileInfo.getString("useremail"));
        updatecontact.setText(profileInfo.getString("contact"));
        passcodesintent = profileInfo.getString("password");



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
                    Toast.makeText(UpdateProfile.this, passcodesintent, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UpdateProfile.this, "Perfect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}