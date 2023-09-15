package com.geminichild.medicinereminder;

import androidx.appcompat.app.AppCompatActivity;

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

        Bundle extras = getIntent().getExtras();
//        title.setText(extras.getString("headline"));
//        description.setText(extras.getString("description"));
        Toast.makeText(this, extras.toString(), Toast.LENGTH_SHORT).show();

    }
}