package com.geminichild.medicinereminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UpdateProfile extends AppCompatActivity {
    Button returnDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        returnDashboard = (Button) findViewById(R.id.returnDashboard);

        returnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Dashboard.class);

                finish();
            }
        });
    }
}