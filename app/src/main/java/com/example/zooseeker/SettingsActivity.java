package com.example.zooseeker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private com.example.zooseeker.UserSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settings = (com.example.zooseeker.UserSettings) getApplication();
        loadSharedPreferences();
        Button button = findViewById(R.id.back_settings_button);
        RadioGroup radioGroup1 = findViewById(R.id.direction_buttons);
        button.setOnClickListener(this::onBackClicked);
        radioGroup1.setOnCheckedChangeListener((radioGroup, checkedButtonId) -> {
            RadioButton brief, detailed;
            brief = (RadioButton) findViewById(R.id.brief_direction);
            detailed = (RadioButton) findViewById(R.id.detailed_direction);
            if (brief.isChecked()) {
                settings.setDetailed(false);
                Toast.makeText(SettingsActivity.this, "Brief Directions Enabled", Toast.LENGTH_SHORT).show();
            } else if (detailed.isChecked()) {
                settings.setDetailed(true);
                Toast.makeText(SettingsActivity.this, "Detailed Directions Enabled", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(SettingsActivity.this, "Invalid Direction", Toast.LENGTH_SHORT).show();
            }
            saveSettings();
        });

    }

    private void saveSettings() {
        SharedPreferences.Editor editor = getSharedPreferences(com.example.zooseeker.UserSettings.PREFERENCES, MODE_PRIVATE).edit();
        editor.putBoolean(com.example.zooseeker.UserSettings.CUSTOM_DIRECTION, settings.getCustomDirection());
        editor.apply();
    }

    private void onBackClicked(View view) {
        finish();
    }

    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(com.example.zooseeker.UserSettings.PREFERENCES, MODE_PRIVATE);
        boolean detailed = sharedPreferences.getBoolean(com.example.zooseeker.UserSettings.CUSTOM_DIRECTION, false);

        if (detailed) {
            RadioButton radioButton;
            radioButton = findViewById(R.id.detailed_direction);
            radioButton.setChecked(true);
        } else {
            RadioButton radioButton;
            radioButton = findViewById(R.id.brief_direction);
            radioButton.setChecked(true);
        }


    }
}