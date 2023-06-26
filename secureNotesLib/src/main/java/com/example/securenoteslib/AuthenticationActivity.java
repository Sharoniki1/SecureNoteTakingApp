package com.example.securenoteslib;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class AuthenticationActivity extends AppCompatActivity {

    private TextInputEditText authentication_TXT_password;
    private Button authentication_BTN_startapp;
    private int batteryLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        findViews();
        initViews();
    }

    private void initViews() {
        authentication_BTN_startapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClicked();
            }
        });
    }

    private void findViews() {
        authentication_TXT_password = findViewById(R.id.authentication_TXT_password);
        authentication_BTN_startapp = findViewById(R.id.authentication_BTN_startapp);
    }

    private int getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return Math.round((float) level / (float) scale * 100.0f);
    }

    private void onLoginButtonClicked() {
        batteryLevel = getBatteryLevel();
        String password = authentication_TXT_password.getText().toString();
        if (password.equals(String.valueOf(batteryLevel))) { // Password matches battery level
            Toast.makeText(this, "Authentication succeed", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, AllNotesActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "Authentication failed!", Toast.LENGTH_LONG).show();
        }
    }

}