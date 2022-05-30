package com.example.zooseeker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zooseeker.db.ExhibitWithGroup;

import java.util.ArrayList;
import java.util.List;

public class DirectionActivity extends AppCompatActivity {

    private Pathfinder pathy;
    private ListView directionList;
    private ArrayList<ExhibitWithGroup> selectedItems;
    UserSettings settings;

    List<String> directionsArray = new ArrayList<>();
    private ArrayAdapter<String> directionsAdapter;

    public DirectionActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        settings = (UserSettings) getApplication();
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        boolean directions = sharedPreferences.getBoolean(UserSettings.CUSTOM_DIRECTION, false);
        // Initialize variables
        directionList = findViewById(R.id.direction_list);
        Button nextButton = findViewById(R.id.next_button);

        Button backButton = findViewById(R.id.back_button);
        ImageButton settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this::onSettingsClicked);

        // Try to load the selected items list from previous activity
        if (getIntent().getParcelableArrayListExtra("selected_list") != null) {
            selectedItems = getIntent().getParcelableArrayListExtra("selected_list");
            Log.d("DirectionActivity", "Loaded arraylist from extra: " + selectedItems.toString());
        } else {
            Log.d("DirectionActivity", "Oopsie loading broke");
        }

        pathy = new Pathfinder(this, selectedItems);
        // could make this a call in the constructor (depends if we want to always optimize path first or not)
        if(!directions) {
            pathy.optimizeBriefSelectedItemsIDs();
        }else {
            pathy.optimizeSelectedItemsIDs();
        }


        directionsArray = pathy.next();

        //Create array to loop directions into
        directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
        directionList.setAdapter(directionsAdapter);


        //edit nextButton onClick
        nextButton.setOnClickListener(view -> {
            directionsArray = pathy.next();
            Log.d("DirectionActivity", "New directions: " + directionsArray.toString());
            directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
            directionList.setAdapter(directionsAdapter);
        });


        backButton.setOnClickListener(view -> {
            directionsArray = pathy.back();
            Log.d("DirectionActivity", "Previous directions: " + directionsArray.toString());
            directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
            directionList.setAdapter(directionsAdapter);
        });



    }

    void onSettingsClicked(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
