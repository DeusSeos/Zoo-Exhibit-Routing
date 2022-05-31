package com.example.zooseeker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private Button button;

    private com.example.zooseeker.UserSettings settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settings = (com.example.zooseeker.UserSettings) getApplication();
        loadSharedPreferences();
        button = findViewById(R.id.back_settings_button);
        radioGroup = findViewById(R.id.direction_buttons);
        button.setOnClickListener(this::onBackClicked);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedButtonId) {
                switch (checkedButtonId) {
                    case R.id.direction_option_a:
                        settings.setCustomDirection(false);
                        Toast.makeText(SettingsActivity.this, "Brief Directions Enabled", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.direction_option_b:
                        settings.setCustomDirection(true);
                        Toast.makeText(SettingsActivity.this, "Detailed Directions Enabled", Toast.LENGTH_SHORT).show();
                        break;
                }
                saveSettings();
            }
        });



    }

    private void saveSettings() {
        SharedPreferences.Editor editor = getSharedPreferences(com.example.zooseeker.UserSettings.PREFERENCES, MODE_PRIVATE).edit();
        editor.putBoolean(com.example.zooseeker.UserSettings.CUSTOM_DIRECTION, settings.getCustomDirection());
        editor.apply();
    }

    private void onBackClicked(View view) {
        Intent intent = getIntent();

        finish();}

    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(com.example.zooseeker.UserSettings.PREFERENCES, MODE_PRIVATE);
        boolean directions = sharedPreferences.getBoolean(com.example.zooseeker.UserSettings.CUSTOM_DIRECTION, false);
        RadioButton radioButton;
        if(!directions) {
            radioButton = findViewById(R.id.direction_option_a);
        } else {
            radioButton = findViewById(R.id.direction_option_b);
        }
        radioButton.setChecked(true);
    }
}