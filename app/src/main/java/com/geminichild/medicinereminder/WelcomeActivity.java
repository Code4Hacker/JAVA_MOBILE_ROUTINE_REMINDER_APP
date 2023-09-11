package com.geminichild.medicinereminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("credential", Context.MODE_PRIVATE);

        String status_email = sharedPreferences.getString("email","");
        String status_codes = sharedPreferences.getString("passcode","");


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }else{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_welcome);
        if (!(status_email.equals("") && status_codes.equals(""))){
            Intent intent = new Intent(WelcomeActivity.this, Dashboard.class);
            intent.putExtra("userMail", status_email);
            intent.putExtra("passcode", status_codes);

            startActivity(intent);
            finish();
            Toast.makeText(this, status_email, Toast.LENGTH_LONG).show();
        }

        Button openregiter = (Button) findViewById(R.id.getregister);

        openregiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, Registration.class));
            }
        });
    }
}