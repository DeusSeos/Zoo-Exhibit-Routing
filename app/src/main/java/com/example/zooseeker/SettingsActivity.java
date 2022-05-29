package com.example.zooseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup radioGroup;

    private UserSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settings = (UserSettings) getApplication();
        loadSharedPreferences();

        radioGroup = findViewById(R.id.direction_buttons);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedButtonId) {
                switch (checkedButtonId) {
                    case R.id.direction_option_a:
                        settings.setCustomTheme(UserSettings.BRIEF_DIRECTION);
                        Toast.makeText(SettingsActivity.this, "Brief Directions Enabled", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.direction_option_b:
                        settings.setCustomTheme(UserSettings.DETAILED_DIRECTION);
                        Toast.makeText(SettingsActivity.this, "Detailed Directions Enabled", Toast.LENGTH_SHORT).show();
                        break;
                }
                SharedPreferences.Editor editor = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE).edit();
                editor.putString(UserSettings.Custom_Direction, settings.getCustomTheme());
                editor.apply();
            }
        });



    }

    private void loadSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        String directions = sharedPreferences.getString(UserSettings.Custom_Direction, UserSettings.BRIEF_DIRECTION);
        settings.setCustomTheme(directions);
    }
}