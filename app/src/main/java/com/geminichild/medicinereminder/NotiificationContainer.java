package com.geminichild.medicinereminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class NotiificationContainer extends AppCompatActivity {
    TextView title, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notiification_container);

        title = findViewById(R.id.titletask);
        description = findViewById(R.id.desc);

        Intent intent = getIntent();
        String titles = intent.getStringExtra("headline");
        String descripti = intent.getStringExtra("desc");

        if (titles != null && descripti != null) {
            title.setText("Message: " + titles + " " + descripti);
        } else {
            title.setText("No data received.");
        }
        startActivity(new Intent(NotiificationContainer.this, Dashboard.class));
        finish();

    }
}