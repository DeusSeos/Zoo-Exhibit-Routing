package com.example.zooseeker;

import android.content.Intent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zooseeker.db.ExhibitWithGroup;

import java.time.LocalDate;
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
        Button skipButton = findViewById(R.id.skip_button);
        Button mockButton = findViewById(R.id.mock_button);
        Button replanButton = findViewById(R.id.replan_button);

        EditText mockLocation = findViewById(R.id.location);
//        ImageButton settingsButton = findViewById(R.id.settings_button);
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


        directionsArray = pathy.summary();

        Persistence persistence = new Persistence();
        int persistenceIndex = persistence.loadIndex(this);

        Log.d("DirectionActivity" , "Pathfinder has index of: " + pathy.getFullPathIndex() );
        Log.d("DirectionActivity" , "Persistance has index of: " + persistenceIndex);

        if(persistenceIndex > -1){

            while (pathy.getFullPathIndex() < persistenceIndex){
                directionsArray = pathy.next();
            }
        }

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


        skipButton.setOnClickListener(view -> {
            directionsArray = pathy.skip();
            directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
            directionList.setAdapter(directionsAdapter);
        });

        replanButton.setOnClickListener(view -> {
            String s = mockLocation.getText().toString();
            directionsArray = pathy.update(s);
            directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
            directionList.setAdapter(directionsAdapter);
            mockLocation.setText("");
        });


        mockButton.setOnClickListener(view -> {
            String s = mockLocation.getText().toString();
            int flag = pathy.mock(s);
            Log.d("flag", String.valueOf(flag));
            // Use is off track
            if (flag == -1) {
                pathy.showAlert(this, "You are off-track!\nClick replan if need.");
                Log.d("replan", String.valueOf(Pathfinder.flag));
                return;
            }
            // User arrives
            if (flag == -2) {
                directionsArray = new ArrayList<String>();
                directionsArray.add("You arrived!");
                directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
                directionList.setAdapter(directionsAdapter);
                mockLocation.setText("");
                return;
            }

            // flag != -2, user is walking along the way
            if (pathy.isBack == true){
                directionsArray = directionsArray.subList(flag-1, directionsArray.size());
            } else {
                directionsArray = directionsArray.subList(flag, directionsArray.size());
            }
            directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
            directionList.setAdapter(directionsAdapter);
            mockLocation.setText("");
        });
    }


    void onSettingsClicked(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDirections();

    }

    private void updateDirections() {

        int current = pathy.getFullPathIndex();
        Log.d("DirectionActivity", "Current index: " + current);
        settings = (UserSettings) getApplication();
        SharedPreferences sharedPreferences = getSharedPreferences(UserSettings.PREFERENCES, MODE_PRIVATE);
        boolean directions = sharedPreferences.getBoolean(UserSettings.CUSTOM_DIRECTION, false);
        pathy.pathUpdate(selectedItems, directions);
//        directionsArray = pathy.next();
        for (int i = -1; i < current; i++) {
            directionsArray = pathy.next();
        }

        //Create array to loop directions into
        directionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, directionsArray);
        directionList.setAdapter(directionsAdapter);
    }


}
